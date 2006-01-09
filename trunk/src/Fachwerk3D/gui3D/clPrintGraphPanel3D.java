/*
 * clPrintPanel.java
 *
 * Created on 16. November 2003, 13:57
 */

package Fachwerk3D.gui3D;

import Fachwerk.statik.Fkt;
import Fachwerk.gui.clPrintGraphDialog;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;
import java.awt.print.*;


/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2003, 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                     <qwert2003@users.berlios.de>
 *
 * Das Programm enthält bestimmt noch FEHLER. Sämtliche Resultate sind
 * SORGFÄLTIG auf ihre PLAUSIBILITäT zu prüfen!
 *
 * Dieses einfache Fachwerkprogramm verwendet ausschliesslich die
 * Gleichgewichtsbedingungen zur Bestimmung der Stabkräfte.
 * Bei statisch unbestimmten Systemen können die überzähligen Stabkräfte
 * zugewiesen werden.
 * Das Programm bezweckt, die Anwendung des unteren (statischen)
 * Grenzwertsatzes der Plastizitätstheorie zu erleichtern.
 *
 * -------------------------------------------------------------
 *
 * Deses Programm ist freie Software. Sie können es unter den Bedingungen
 * der GNU General Public License, Version 2, wie von der Free Software
 * Foundation herausgegeben, weitergeben und/oder modifizieren.
 *
 * Die Veröffentlichung dieses Programms erfolgt in der Hoffnung, dass es
 * Ihnen von Nutzen sein wird, aber OHNE JEDE GEWÄHRLEISTUNG - sogar ohne
 * die implizite Gewährleistung der MARKTREIFE oder der EIGNUNG FüR EINEN
 * BESTIMMTEN ZWECK.  Details finden Sie in der GNU General Public License.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen  mit
 * diesem Programm erhalten haben. Falls nicht, schreiben Sie an die
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA. 
 */
public class clPrintGraphPanel3D extends clHauptPanel3D implements Printable {    
    
    // Aufteilung des Blattes
    final double antGraph = 0.9; 
    
    final double printskal = 0.5;
    final int schriftgrText = 10;
    final int schriftgrGraph = 7; //8;
    //final int schriftgrSys = 6;
    
    double antTxt = 1d - antGraph; 
    double antVertikal;
    double antHorizontal;
    
    final double zoll = 0.0254; // [m]
    double dpi = 72d / printskal;
    double einheit = 1d; // zB: cm --> 0.001 m
    boolean MASSSTABABGEFRAGT = false;
    double mst = 1; // wird überschrieben
    // Für Pfeillänge, etc (Masse in Pixel)
    double druckvergr = 300d/dpi * (20.8-2*2.5)/(33.5-5.); // Druckaufl/Bildschirmaufl * BreiteA4/BreiteBildschirm
    
    Frame parent;
    PageFormat pf; 
    //private boolean schongedruckt = false;
    ResourceBundle druckRB;
    Locale locale;
    
    double[] n; // Projektionsrichtung
    
            
    /** Creates a new instance of clPrintGraphPanel */
    public clPrintGraphPanel3D(java.awt.Frame parent, clHauptPanel3D hp, Locale lc) {
        super(hp.getKn(), hp.getSt(), true);
        this.parent = parent;
        dxf = hp.dxf;
        n = hp.getBlickRtg();
               
        
        Point2D[] zoomPkte = hp.getZoomPkte();
        ZoomPkt1 = zoomPkte[0];
        ZoomPkt2 = zoomPkte[1];
        ZOOMALL = hp.ZOOMALL;
        
        boolean[] aktiveLayer = hp.getAktiveLayer();
        MIT_KnNr = aktiveLayer[0];
        MIT_StabNr = aktiveLayer[1];
        MIT_Lasten = aktiveLayer[2];
        MIT_Auflagerkräften = aktiveLayer[3];
        MIT_Stabkräften = aktiveLayer[4];
        MIT_Hintergrund = aktiveLayer[5];
        
        maxPfeil = maxPfeil * druckvergr;
        spitzenlängeMax = spitzenlängeMax * druckvergr;
        spitzenlängeMin = spitzenlängeMin * druckvergr; 
        lagerhöhe = lagerhöhe  * (float)druckvergr; //
        
        locale = lc;
        druckRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-drucken", locale);
        if (druckRB == null) {
            System.err.println("FEHLER: gui-drucken für " + locale.toString());
        }
    }
    
    public void paint(Graphics g_1D) {
        return;
    }
    
            
    public int print(Graphics graphics, PageFormat pageformat, int pageIndex) throws PrinterException {
        if (pageIndex >= 1) return Printable.NO_SUCH_PAGE;      
        
        this.pf = pageformat;           
        g = (Graphics2D) graphics;
        g.scale(printskal, printskal);
                   
        //auf den sichtbaren Bereich ausrichten
        g.translate(pf.getImageableX() / printskal, pf.getImageableY() / printskal);  
                       
                
        // -----------------
        // 1. Teil: Graphik
        // -----------------        
        antVertikal = antGraph; antHorizontal = 1.0d;
        schriftgrStd = (int) (schriftgrGraph / printskal);
        
        double zusRandabst = 2d*schriftgrText;
        if (MIT_Lasten||MIT_Auflagerkräften) zusRandabst = maxPfeil;
        
        // -----------------------------
        // Intermezzo: Massstab abfragen
        // -----------------------------
        koord = new clKoord3D(n);
        if (!MASSSTABABGEFRAGT) {
            if (ZOOMALL) {
                koord.zoome(ZoomPkt1, ZoomPkt2, this, zusRandabst);
            }
            else koord.zoome(ZoomPkt1, ZoomPkt2, this);
            // maximalen Massstab bestimmen        
            double minzoomfaktor = koord.m(dpi/zoll);
            
            // Massstab abfragen
            clPrintGraphDialog dialog = new clPrintGraphDialog(parent, minzoomfaktor, locale);
            if (dialog.abgebrochen()) throw new PrinterException(tr("druckenabgebrochen")); //return Printable.NO_SUCH_PAGE; // abgebrochen
            einheit = dialog.getEinheit();
            mst = dialog.getMst();             
            // debug
            //mst = 100d;
            MASSSTABABGEFRAGT = true;
            //debug
            //System.out.println("minzoomfaktor " + Fkt.nf(minzoomfaktor,1));
            //System.out.println("Massstab 1:" + Fkt.nf(mst,1) + "  Einheit " + Fkt.nf(einheit,3) + "m");            
        }                
        
        koord.zoome(ZoomPkt1, ZoomPkt2, this, true, einheit*(dpi/zoll)/mst);
                        
        g.setPaint(Color.black);
                       
        if (Kn != null) {
            if (MIT_Hintergrund) darstellenHintergrund();
            darstellenFachwerk(true);
            if (MIT_KnNr) darstellenKnNr();
            if (MIT_StabNr) darstellenStabNr();
            if (MIT_Lasten) darstellenLasten();
            if (MIT_Auflagerkräften) darstellenAuflagerkräfte();
            if (MIT_Stabkräften) darstellenStabkräfte();  
        }
        
        // -------------------------
        // 2. Teil: Text
        // -------------------------
        g.translate(0d,  antGraph * pf.getImageableHeight() / printskal);
        antVertikal = antTxt; antHorizontal = 1.0d;
        double Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        double Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        int anzmöglzeilen = 2; // mind. eine Zeile am Schluss freilassen 
        int anzmöglbuchst = 30;
        int schriftgr = schriftskal(Feldhöhe,Feldbreite, anzmöglzeilen,anzmöglbuchst, (int) (schriftgrText / printskal));      
        float za = (float)faktorza * (float)schriftgr;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        
        // Zoomfaktor bestimmen        
        double zoomfaktor = koord.m(dpi/zoll) * einheit;
        g.drawString("1 : " + Fkt.nf(zoomfaktor,1), 0f, (float)schriftgr+ 1f*za);
     
        // Koordinatenkreuz unten in der Mitte
        g.translate(pf.getImageableWidth() / 2d / printskal, 0);
        darstellenMiniKoordSys();
        
        // drucken
        paint(g);
        //schongedruckt = true;
        return Printable.PAGE_EXISTS;
    }
    
  
    
    /**überschreibt die JPanel-Methode und liefert den (skalierten) Druckbereich, welcher zur Verfügung steht.  */
    public int getWidth() {
        return (int) (pf.getImageableWidth() / printskal * antHorizontal);
    }
    
    /**überschreibt die JPanel-Methode und liefert den (skalierten) Druckbereich, welcher zur Verfügung steht.  */
    public int getHeight() {
        return (int) (pf.getImageableHeight() / printskal * antVertikal);
    }
    
    private String tr(String key) {        
        String übersetzt;
        try {übersetzt = druckRB.getString(key);}
        catch (MissingResourceException e) {
            System.err.println("Schluesselwort " + key + " nicht gefunden fuer " + locale.toString() + " ; " + e.toString());
            return key;
        }        
        return übersetzt;
    }
    
}
