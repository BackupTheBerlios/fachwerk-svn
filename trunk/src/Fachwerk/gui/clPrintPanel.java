/*
 * clPrintPanel.java
 *
 * Created on 16. November 2003, 13:57
 */

package Fachwerk.gui;

import Fachwerk.statik.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;
import java.awt.print.*;


/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2006 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                      <qwert2003@users.berlios.de>
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
public class clPrintPanel extends clHauptPanel implements Printable {
    
    // Aufteilung des Blattes
    final double antRes = 0.33;
    final double antHorLinks = 0.5;
    
    final double printskal = 0.5;
    final int schriftgrText = 10;
    final int schriftgrRes = 7;
    final int schriftgrSys = 6;
    
    double dpi = 72d / printskal;
    double druckvergr = 300d/dpi * (20.8-2*2.5)/(33.5-5.); // Druckaufl/Bildschirmaufl * BreiteA4/BreiteBildschirm
    
    double antSys = 1d - antRes;
    double antSysgra = antHorLinks * antRes;
    double antSystxt = antSys - antSysgra;
    double antVertikal;
    double antHorizontal;
    
    
    private boolean FEHLERmelden;
    private boolean WIDERSPRUCHmelden;
    private boolean KOMPLETTmelden;
    
    PageFormat pf;
    //private boolean schongedruckt = false;
    ResourceBundle druckRB;
    Locale locale;
    
    
    /** Creates a new instance of clPrintPanel */
    public clPrintPanel(clKnoten[] p_Knotenarray, clWissenderStab[] p_Stabarray, clHauptPanel hp,
                        boolean WIDERSPRUCHmelden, boolean FEHLERmelden, boolean KOMPLETTmelden, Locale lc) {
        super(p_Knotenarray, p_Stabarray, true);
        this.FEHLERmelden = FEHLERmelden;
        this.WIDERSPRUCHmelden = WIDERSPRUCHmelden;
        this.KOMPLETTmelden = KOMPLETTmelden;
        mechanismusRelKnVersch = hp.mechanismusRelKnVersch;
        
        // Skalierung der Pfeillänge
        MIT_Lasten = true;
        MIT_Auflagerkräften = true;
        maxPfeil = maxPfeil*druckvergr;
        spitzenlängeMax = spitzenlängeMax*druckvergr;
        spitzenlängeMin = spitzenlängeMin*druckvergr;
        
        locale = lc;
        druckRB = ResourceBundle.getBundle("Fachwerk/locales/gui-drucken", locale);
        if (druckRB == null) {
            System.err.println("FEHLER: gui-drucken für " + locale.toString());
        }
    }
    
    public void paint(Graphics g_1D) {
        return;
    }
        
        
    public int print(Graphics graphics, PageFormat pageformat, int pageIndex) throws PrinterException {
        if (pageIndex >= 1) return Printable.NO_SUCH_PAGE;
        /*if (schongedruckt) {
            paint(g);
            return Printable.PAGE_EXISTS;
        }*/
        this.pf = pageformat;
        g = (Graphics2D) graphics;
        g.scale(printskal, printskal);
        
        //auf den sichtbaren Bereich ausrichten
        g.translate(pf.getImageableX() / printskal, pf.getImageableY() / printskal);
        
        
        // -----------------
        // 1. Teil: Resultat
        // -----------------
        zoomAll(false);
        antVertikal = antRes; antHorizontal = 1.0d;
        //schriftgrStd = (int) ( (double)schriftgrStd / printskal * 0.7d);
        schriftgrStd = (int) (schriftgrRes / printskal);
        
        koord = new clKoord(ZoomPkt1, ZoomPkt2, this, maxPfeil);
        g.setPaint(Color.black);
        if (Kn != null) {
            darstellenFachwerk(true);
            darstellenLasten(false);
            darstellenAuflagerkräfte(false);
            darstellenStabkräfte();
            darstellenMechanismus();
        }
        
        // -------------------------
        // 2. Teil: System graphisch
        // -------------------------
        g.translate(0d,  antRes * pf.getImageableHeight() / printskal);
        antVertikal = antSysgra; antHorizontal = antHorLinks;
        schriftgrStd = (int) (schriftgrSys / printskal);
        zoomAll(false);
        koord = new clKoord(ZoomPkt1, ZoomPkt2, this, 2d*schriftgrStd); //maxPfeil);
        g.setPaint(Color.black);
        if (Kn != null) {
            darstellenFachwerk(false);
            darstellenLasten(true);
            darstellenKnNr();
            darstellenStabNr();
        }
        
        // --------------------------------------------------------------
        // 3. Teil: Text rechts von "System graphisch": Summen der Kräfte
        // --------------------------------------------------------------
        antVertikal = antSystxt; antHorizontal = 1.0d - antHorLinks;
        g.translate(antHorLinks * pf.getImageableWidth()/printskal, 0d);
        antVertikal = antSysgra;
        antHorizontal = 1d - antHorLinks;
        double Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        double Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        int anzmöglzeilen = 9; // mind. eine Zeile am Schluss freilassen
        String[] summenLR = summenkontrolle();
        double[] liste = {summenLR[0].length(), summenLR[1].length(), 35};
        int anzmöglbuchst = (int) Fkt.max(liste);
        int schriftgr = schriftskal(Feldhöhe,Feldbreite, anzmöglzeilen,anzmöglbuchst, (int) (schriftgrText / printskal));
        float za = (float)faktorza * (float)schriftgr;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        g.drawString(" " + tr("SummeLasten") + ":", 0f, (float)schriftgr+ 1f*za);
        g.drawString(summenLR[0], 0f, (float)schriftgr+ 2f*za);
        g.drawString(" " + tr("SummeLager") + ":", 0f, (float)schriftgr+ 3f*za);
        g.drawString(summenLR[1], 0f, (float)schriftgr+ 4f*za);
        if (KOMPLETTmelden) {
            g.drawString(" " + tr("Komplett"), 0f, (float)schriftgr+ 6f*za);
        }
        if (WIDERSPRUCHmelden) {
            assert !KOMPLETTmelden;
            g.setPaint(Color.red);
            g.drawString(" " + tr("WiderspruchSys") + "!", 0f, (float)schriftgr+ 6f*za);
        }
        if (FEHLERmelden) {
            assert !KOMPLETTmelden;
            g.setPaint(Color.red);
            g.drawString(" " + tr("FehlerBer") + "!", 0f, (float)schriftgr+ 7f*za);
        }
        g.setPaint(Color.black);
        
        // ----------------------------------------------
        // 4. Teil: Text unter "System graphisch": Knoten
        // ----------------------------------------------
        g.translate(-antHorLinks * pf.getImageableWidth()/printskal,  antSysgra * pf.getImageableHeight() / printskal);
        antVertikal = antSystxt;
        antHorizontal = antHorLinks;
        Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        
        anzmöglzeilen = 1 + (Kn.length - 1) + 2 + anzlager();
        int zeile = 0;
        String[] knotenspalte = new String[anzmöglzeilen];
        liste = new double[anzmöglzeilen];
        StringBuffer sb = new StringBuffer();
        
        knotenspalte[zeile] = tr("Knoten"); liste[zeile] = knotenspalte[zeile].length(); zeile++;
        for (int kn = 1; kn < Kn.length; kn++) {             
            sb.append("Nr ");
            sb.append(Fkt.nf(kn, 2) + ": ");
            //if (kn < 10) sb.append(" ");
            //sb.append(kn+": ");
            switch (Kn[kn].getKnotenstatus()) {
                case OFFEN:
                    if (Kn[kn].getLx() != 0 && Kn[kn].getLz() != 0) {
                        sb.append(Fkt.fix(Kn[kn].getX(), 2) +" "+ Fkt.fix(Kn[kn].getZ(), 2));
                    }
                    else {
                        sb.append(Fkt.nf(Kn[kn].getX(), 2, 2) +" ");
                        sb.append(Fkt.nf(Kn[kn].getZ(), 2, 2) +" ");
                    }
                    sb.append(" " + tr("OFFEN"));
                    break;
                case FERTIG:
                    sb.append(Fkt.nf(Kn[kn].getX(), 2, 2) +" ");
                    sb.append(Fkt.nf(Kn[kn].getZ(), 2, 2) +" ");
                    break;
                case WIDERSPRUCH:
                    sb.append(Fkt.fix(Kn[kn].getX(), 2) +" "+ Fkt.fix(Kn[kn].getZ(), 2) +" "+tr("WIDERSPRUCH"));
                    break;
                default:
                    sb.append(Fkt.fix(Kn[kn].getX(), 2) +" "+ Fkt.fix(Kn[kn].getZ(), 2));
                    sb.append(" Status "+Kn[kn].getKnotenstatus());
            }
            if (Kn[kn].getLx() != 0) sb.append(" Lx= " + Fkt.fix(Kn[kn].getLx(),1) + "kN"); 
            if (Kn[kn].getLz() != 0) sb.append(" Lz= " + Fkt.fix(Kn[kn].getLz(),1) + "kN");
            knotenspalte[zeile] = sb.toString();
            liste[zeile] = sb.length();
            zeile++;
            sb = new StringBuffer();
        }
        // Leerzeile
        knotenspalte[zeile] = ""; liste[zeile] = knotenspalte[zeile].length(); zeile++;
        
        // Lager
        knotenspalte[zeile] = tr("Lager"); liste[zeile] = knotenspalte[zeile].length(); zeile++;
        for (int kn = 1; kn < Kn.length; kn++) {
            switch (Kn[kn].getLagerbed()) {
                case FIX:
                    sb.append(Fkt.nf(kn, 2) +" " + tr("fest") + ": ");
                    switch (Kn[kn].getLagerstatus()) {
                        case BER:
                            sb.append("Rx = " + Fkt.fix(Kn[kn].getRx(),1) + "kN Rz= " + Fkt.fix(Kn[kn].getRz(),1) +"kN");
                            break;
                        case GESETZT:
                            sb.append("Rx = " + Fkt.fix(Kn[kn].getRx(),1) + "kN Rz= " + Fkt.fix(Kn[kn].getRz(),1)
                            + "kN " + tr("GESETZT"));
                            break;
                        case UNBEST:
                            sb.append("Rx, Ry = ???, " + tr("Status") + " " + tr("UNBESTIMMT"));
                            break;
                        default:
                            sb.append(tr("Status") + " " + Kn[kn].getLagerstatus() );
                    }
                    knotenspalte[zeile] = sb.toString();
                    liste[zeile] = sb.length();
                    zeile++;
                    sb = new StringBuffer();
                    break;
                case VERSCHIEBLICH:
                    sb.append(Fkt.nf(kn, 2) + " " + tr("Gleitl"));
                    sb.append("(" + '\u03B1' + "=" + Fkt.fix(Math.toDegrees(Kn[kn].getRalpha()),1)+"°) ");
                    switch (Kn[kn].getLagerstatus()) {
                        case BER:
                            sb.append("Rx= " + Fkt.fix(Kn[kn].getRx(),1) + "kN Rz= " + Fkt.fix(Kn[kn].getRz(),1) + "kN");
                            break;
                        case GESETZT:
                            sb.append("Rx= " + Fkt.fix(Kn[kn].getRx(),1) + "kN Rz= " + Fkt.fix(Kn[kn].getRz(),1) + "kN "+tr("GESETZT"));
                            break;
                        case UNBEST:
                            sb.append("R = ???, " + tr("UNBESTIMMT"));
                            break;
                        default:
                            sb.append(tr("Status") + " " + Kn[kn].getLagerstatus() );
                    }
                    knotenspalte[zeile] = sb.toString();
                    liste[zeile] = sb.length();
                    zeile++;
                    sb = new StringBuffer();
                    break;
                default:
            }
        }
        assert zeile == anzmöglzeilen : "Anz.Zeilen stimmt nicht (Knotenspalte)";
        
        anzmöglbuchst = (int) Fkt.max(liste);
        schriftgr = schriftskal(Feldhöhe,Feldbreite, anzmöglzeilen,anzmöglbuchst, (int) (schriftgrText / printskal));
        za = (float)faktorza * (float)schriftgr;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        for (int i = 0; i < knotenspalte.length; i++) {
            g.drawString(knotenspalte[i], 0f, (float) schriftgr + ((float) i) * za);
        }
        
        
        
        // ---------------------------------------------
        // 5. Teil: Text unter "System graphisch": Stäbe
        // ---------------------------------------------
        antVertikal = antSystxt; antHorizontal = 1.0d - antHorLinks;
        g.translate(antHorLinks * pf.getImageableWidth()/printskal, 0d);
        Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        
        anzmöglzeilen = 1 + (St.length - 1);
        String[] stabspalte = new String[anzmöglzeilen];
        liste = new double[anzmöglzeilen];
        sb = new StringBuffer();
        
        stabspalte[0] = " " + tr("Staebe");
        for (int st = 1; st < St.length; st++) {
            sb.append(" Nr " + Fkt.nf(st, 2));
            sb.append(" ("+tr("Kn")+" " + Fkt.nf(St[st].von, 2) + " - " + Fkt.nf(St[st].bis, 2) + "): ");
            switch (St[st].stab.getStatus()) {
                case BER:
                    sb.append("N = ");
                    sb.append(Fkt.nf(St[st].stab.getKraft(), 1, 5));
                    sb.append(" kN"); //Status BERECHNET");
                    break;
                case GESETZT:
                    sb.append("N = ");
                    sb.append(Fkt.nf(St[st].stab.getKraft(), 1, 5));
                    sb.append(" kN " + tr("GESETZT"));
                    break;
                case UNBEST:
                    sb.append("N = ???, " + tr("UNBESTIMMT"));
                    break;
                default:
                    sb.append(tr("Status") + " " + St[st].stab.getStatus() );
            }
            stabspalte[st] = sb.toString();
            liste[st] = sb.length();
            sb = new StringBuffer();
        }          
        anzmöglbuchst = (int) Fkt.max(liste);
        schriftgr = schriftskal(Feldhöhe,Feldbreite, anzmöglzeilen,anzmöglbuchst, (int) (schriftgrText / printskal));
        za = (float)faktorza * (float)schriftgr;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        for (int i = 0; i < stabspalte.length; i++) {
            g.drawString(stabspalte[i], 0f, (float) schriftgr + ((float) i) * za);
        }
        
        // drucken
        paint(g);
        //schongedruckt = true;
        return Printable.PAGE_EXISTS;
    }
    
    private String[] summenkontrolle() {
        // Summe Lasten, Summe Auflagerkräfte
        String[] summentxtLR = new String[2];
        StringBuffer sb = new StringBuffer();
        double SummeLx = 0; double SummeLz = 0; double SummeML = 0;
        double SummeAx = 0; double SummeAz = 0; double SummeMA = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            SummeLx += Kn[kn].getLx();
            SummeLz += Kn[kn].getLz();
            // das Moment sei positiv im Gegenuhrzeigersinn.
            SummeML += Kn[kn].getZ() * Kn[kn].getLx() - Kn[kn].getX() * Kn[kn].getLz();
            if (Kn[kn].getLagerbed() == FIX || Kn[kn].getLagerbed() == VERSCHIEBLICH) {
                SummeAx += Kn[kn].getRx();
                SummeAz += Kn[kn].getRz();
                SummeMA += Kn[kn].getZ() * Kn[kn].getRx() - Kn[kn].getX() * Kn[kn].getRz();
            }            
        }
        //Summe aller Lasten:
        sb.append(" Lx="); if(Fkt.fix(SummeLx,1) >= 0) sb.append(" ");
        sb.append(Fkt.fix(SummeLx,1) + "kN  ");
        sb.append("Lz=");  if(Fkt.fix(SummeLz,1) >= 0) sb.append(" ");
        sb.append(Fkt.fix(SummeLz,1) + "kN  ");
        sb.append("ML=");  if(Fkt.fix(SummeML,1) >= 0) sb.append(" ");
        sb.append(Fkt.fix(SummeML,1) + "kNm"); 
        summentxtLR[0] = sb.toString();
        sb = new StringBuffer();
        //Summe der berechneten Lagerkräfte:
        sb.append(" Ax="); if(Fkt.fix(SummeAx,1) >= 0) sb.append(" ");
        sb.append(Fkt.fix(SummeAx,1) + "kN  ");
        sb.append("Az=");  if(Fkt.fix(SummeAz,1) >= 0) sb.append(" "); 
        sb.append(Fkt.fix(SummeAz,1) + "kN  ");
        sb.append("MA=");  if(Fkt.fix(SummeMA,1) >= 0) sb.append(" ");
        sb.append(Fkt.fix(SummeMA,1) + "kNm");
        summentxtLR[1] = sb.toString();
        return summentxtLR;
    }
    
    private int anzlager() {
        int anz = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            switch (Kn[kn].getLagerbed()) {
                case FIX:
                case VERSCHIEBLICH:
                    anz++;
                case LOS:
                    break;
                default:
                    assert false: "Lagerbed: " + Kn[kn].getLagerbed();
            }
        }
        return anz;        
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
