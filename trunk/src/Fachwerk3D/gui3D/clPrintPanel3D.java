/*
 * clPrintPanel.java
 *
 * Created on 16. November 2003, 13:57
 */

package Fachwerk3D.gui3D;

import Fachwerk3D.statik3D.*;
import Fachwerk.statik.Fkt;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;


/**
 * Fachwerk3D - treillis3D
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
public class clPrintPanel3D extends clHauptPanel3D implements Printable {
    
    // Aufteilung des Blattes
    final double antGra = 0.28;
    final double antSysinfo = 0.12;
    double antTxt = 1d - antGra - antSysinfo;
    final double antHorLinks = 0.6;
    double antVertikal;
    double antHorizontal;
    
    final double printskal = 0.5;
    final int schriftgrText = 10;
    final int schriftgrSys = 6;
    
    double dpi = 72d / printskal;
    double druckvergr = 300d/dpi * (20.8-2*2.5)/(33.5-5.); // Druckaufl/Bildschirmaufl * BreiteA4/BreiteBildschirm
    
    
    private boolean FEHLERmelden;
    private boolean WIDERSPRUCHmelden;
    private boolean KOMPLETTmelden;
    
    PageFormat pf;
    ResourceBundle druckRB;
    Locale locale;
    
    
    /** Creates a new instance of clPrintPanel */
    public clPrintPanel3D(clKnoten3D[] p_Knotenarray, clWissenderStab3D[] p_Stabarray, clHauptPanel3D hp,
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
        // 1. Teil: System graphisch
        // -----------------
        antVertikal = antGra; antHorizontal = 1.0d;
        schriftgrStd = (int) (schriftgrSys / printskal);
        
        double[] n = {-0.3,-0.9, 0.2};
        koord = new clKoord3D(n);
        zoomAll(false);
        koord.zoome(ZoomPkt1, ZoomPkt2, this, 1.5d * schriftgrSys);
        g.setPaint(Color.black);
        if (Kn != null) {
            darstellenMiniKoordSys();
            darstellenFachwerk(false);
            darstellenLasten(true);
            darstellenKnNr();
            darstellenStabNr();
            darstellenMechanismus();
        }
        
        
        // --------------------------------------------------------------
        // 2. Teil: Systeminfo
        // --------------------------------------------------------------
        
        g.translate(0d,  antGra * pf.getImageableHeight() / printskal);
        antVertikal = antSysinfo;
        antHorizontal = 1d;
        double Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        double Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        
        int anzmöglzeilen = 8; // mind. eine Zeile am Schluss freilassen
        String[] summenLR = summenkontrolle();
        double[] liste = {summenLR[0].length(), summenLR[1].length(), 35};
        int anzmöglbuchst = (int) Fkt.max(liste);
        int schriftgr = schriftskal(Feldhöhe,Feldbreite, anzmöglzeilen,anzmöglbuchst, (int) (schriftgrText / printskal));
        float za = (float)faktorza * (float)schriftgr;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        g.drawString(tr("SummeLasten") + ":", 0f, (float)schriftgr+ 1f*za);
        g.drawString(summenLR[0], 0f, (float)schriftgr+ 2f*za);
        g.drawString(tr("SummeLager") + ":", 0f, (float)schriftgr+ 3f*za);
        g.drawString(summenLR[1], 0f, (float)schriftgr+ 4f*za);
        if (KOMPLETTmelden) {
            g.drawString(tr("Komplett"), 0f, (float)schriftgr+ 6f*za);
        }
        if (WIDERSPRUCHmelden) {
            assert !KOMPLETTmelden;
            g.setPaint(Color.red);
            g.drawString(tr("WiderspruchSys") + "!", 0f, (float)schriftgr+ 6f*za);
        }
        if (FEHLERmelden) {
            assert !KOMPLETTmelden;
            g.setPaint(Color.red);
            g.drawString(tr("FehlerBer") + "!", 0f, (float)schriftgr+ 7f*za);
        }
        
        g.setPaint(Color.black);
        
        // ----------------------------------------------
        // 3. Teil: (links) Knoten
        // ----------------------------------------------
        g.translate(0,  antSysinfo * pf.getImageableHeight() / printskal);
        antVertikal = antTxt;
        antHorizontal = antHorLinks;
        Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        
        anzmöglzeilen = 1 + (Kn.length - 1) + 2 + anzlager();
        int zeile = 0;
        String[] knotenspalte = new String[anzmöglzeilen];
        liste = new double[anzmöglzeilen];
        StringBuffer sb = new StringBuffer();
        
        sb.append(tr("Knoten"));
        while (7 > sb.length()) {
            sb.append(" ");
        }
        sb.append("x     y     z   ");
        sb.append("Lx[kN] Ly[kN] Lz[kN]  ");
        sb.append("Status");
        
        knotenspalte[zeile] = sb.toString();
        liste[zeile] = sb.length();
        zeile++;
        sb = new StringBuffer();
            
        for (int kn = 1; kn < Kn.length; kn++) {
            sb.append(Fkt.nf(kn, 2) + ": ");
            sb.append(Fkt.nf(Kn[kn].getX(), 2, 2) +" ");
            sb.append(Fkt.nf(Kn[kn].getY(), 2, 2) +" ");
            sb.append(Fkt.nf(Kn[kn].getZ(), 2, 2) +" ");
            
            if (Kn[kn].getLx() != 0) sb.append(Fkt.nf(Kn[kn].getLx(),1,4) + " ");
            else sb.append("       ");
            if (Kn[kn].getLy() != 0) sb.append(Fkt.nf(Kn[kn].getLy(),1,4) + " ");
            else sb.append("       ");
            if (Kn[kn].getLz() != 0) sb.append(Fkt.nf(Kn[kn].getLz(),1,4) + " ");
            else sb.append("       ");
            
            switch (Kn[kn].getKnotenstatus()) {
                case OFFEN:
                    sb.append(" " + tr("OFFEN"));
                    break;
                case FERTIG:
                    //sb.append(Fkt.nf(Kn[kn].getZ(), 2) +" ");// FERTIG");
                    break;
                case WIDERSPRUCH:
                    sb.append(" "+tr("WIDERSPRUCH"));
                    break;
                default:
                    sb.append(" Status "+Kn[kn].getKnotenstatus());
            }
            
            knotenspalte[zeile] = sb.toString();
            liste[zeile] = sb.length();
            zeile++;
            sb = new StringBuffer();
        }
        // Leerzeile
        knotenspalte[zeile] = ""; liste[zeile] = knotenspalte[zeile].length(); zeile++;
        
        // Lager
        sb.append(tr("Lager"));
        while (7 > sb.length()) {
            sb.append(" ");
        }
        sb.append("Rx[kN]  Ry[kN]  Rz[kN]  ");
        sb.append(tr("Typ"));
        
        knotenspalte[zeile] = sb.toString();
        liste[zeile] = sb.length();
        zeile++;
        sb = new StringBuffer();
        
        for (int kn = 1; kn < Kn.length; kn++) {
            switch (Kn[kn].getLagerbed()) {
                case FIX:
                    sb.append(Fkt.nf(kn, 2) +":   ");// + tr("fest") + " ");
                    switch (Kn[kn].getLagerstatus()) {
                        case BER:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("fest") + " ");
                            break;
                        case GESETZT:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("fest") + " ");
                            sb.append(" " + tr("GESETZT"));
                            break;
                        case UNBEST:
                            sb.append(tr("fest") + " ");
                            sb.append(tr("Status") + " " + tr("UNBESTIMMT"));
                            break;
                        default:
                            sb.append(tr("fest") + " ");
                            sb.append(tr("Status") + " " + Kn[kn].getLagerstatus() );
                    }
                    knotenspalte[zeile] = sb.toString();
                    liste[zeile] = sb.length();
                    zeile++;
                    sb = new StringBuffer();
                    break;
                case VERSCHIEBLICH:
                    sb.append(Fkt.nf(kn, 2) + ":   ");// + tr("Gleitl") + " ");
                    switch (Kn[kn].getLagerstatus()) {
                        case BER:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("Gleitl") + " ");
                            break;
                        case GESETZT:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("Gleitl") + " ");
                            sb.append(" " + tr("GESETZT"));
                            break;
                        case UNBEST:
                            sb.append(tr("Gleitl") + " ");
                            sb.append(tr("UNBESTIMMT"));
                            sb.append(" (");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[0],2) + ",");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[1],2) + ",");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[2],2) + ")");
                            break;
                        default:
                            sb.append(tr("Gleitl") + " ");
                            sb.append(tr("Status") + " " + Kn[kn].getLagerstatus() );
                    }
                    knotenspalte[zeile] = sb.toString();
                    liste[zeile] = sb.length();
                    zeile++;
                    sb = new StringBuffer();
                    break;
                case SCHINENLAGER:
                    sb.append(Fkt.nf(kn, 2) + ":   ");// + tr("Schine") + " ");
                    switch (Kn[kn].getLagerstatus()) {
                        case BER:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("Schine") + " ");
                            break;
                        case GESETZT:
                            sb.append(Fkt.nf(Kn[kn].getRx(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRy(),1,5) + " ");
                            sb.append(Fkt.nf(Kn[kn].getRz(),1,5) + " ");
                            sb.append(tr("Schine") + " ");
                            sb.append(" " + tr("GESETZT"));
                            break;
                        case UNBEST:
                            sb.append(tr("Schine") + " ");
                            sb.append(tr("UNBESTIMMT"));
                            sb.append(" (");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[0],2) + ",");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[1],2) + ",");
                            sb.append(Fkt.nf(Kn[kn].getRrtg()[2],2) + ")");
                            break;
                        default:
                            sb.append(tr("Schine") + " ");
                            sb.append(tr("Status") + " " + Kn[kn].getLagerstatus() );
                    }
                    knotenspalte[zeile] = sb.toString();
                    liste[zeile] = sb.length();
                    zeile++;
                    sb = new StringBuffer();
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
        // 4. Teil: (rechts) Stäbe
        // ---------------------------------------------
        
        g.translate(antHorLinks * pf.getImageableWidth()/printskal, 0d);
        antVertikal = antTxt; antHorizontal = 1.0d - antHorLinks;
        Feldhöhe = antVertikal * pf.getImageableHeight() / printskal;
        Feldbreite = antHorizontal * pf.getImageableWidth() / printskal;
        
        anzmöglzeilen = 1 + (St.length - 1);
        String[] stabspalte = new String[anzmöglzeilen];
        liste = new double[anzmöglzeilen];
        sb = new StringBuffer();
        
        stabspalte[0] = " " + tr("Staebe");
        for (int st = 1; st < St.length; st++) {
            sb.append(" ");
            sb.append(Fkt.nf(st, 2));
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
        double SummeLx = 0; double SummeLy = 0; double SummeLz = 0;
        double SummeMLx = 0; double SummeMLy = 0; double SummeMLz = 0;
        double SummeAx = 0; double SummeAy = 0; double SummeAz = 0;
        double SummeMAx = 0; double SummeMAy = 0; double SummeMAz = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            SummeLx += Kn[kn].getLx();
            SummeLy += Kn[kn].getLy();
            SummeLz += Kn[kn].getLz();
            SummeMLx += Kn[kn].getY() * Kn[kn].getLz() - Kn[kn].getZ() * Kn[kn].getLy();
            SummeMLy += Kn[kn].getZ() * Kn[kn].getLx() - Kn[kn].getX() * Kn[kn].getLz();
            SummeMLz += Kn[kn].getX() * Kn[kn].getLy() - Kn[kn].getY() * Kn[kn].getLx();
            if (Kn[kn].getLagerbed() == FIX || Kn[kn].getLagerbed() == VERSCHIEBLICH
                                            || Kn[kn].getLagerbed() == SCHINENLAGER) {
                SummeAx += Kn[kn].getRx();
                SummeAy += Kn[kn].getRy();
                SummeAz += Kn[kn].getRz();
                SummeMAx += Kn[kn].getY() * Kn[kn].getRz() - Kn[kn].getZ() * Kn[kn].getRy();
                SummeMAy += Kn[kn].getZ() * Kn[kn].getRx() - Kn[kn].getX() * Kn[kn].getRz();
                SummeMAz += Kn[kn].getX() * Kn[kn].getRy() - Kn[kn].getY() * Kn[kn].getRx();
            }
        }
        
        StringBuffer sb = new StringBuffer();
        String[] summentxtLR = new String[2];
        
        //Summe aller Lasten:
        sb.append(" Lx="); if(Fkt.fix(SummeLx,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeLx,1) + "kN  ");
        sb.append(" Ly="); if(Fkt.fix(SummeLy,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeLy,1) + "kN  ");
        sb.append(" Lz="); if(Fkt.fix(SummeLz,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeLz,1) + "kN  ");
        sb.append(" MLx="); if(Fkt.fix(SummeMLx,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMLx,1) + "kNm  ");
        sb.append(" MLy="); if(Fkt.fix(SummeMLy,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMLy,1) + "kNm  ");
        sb.append(" MLz="); if(Fkt.fix(SummeMLz,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMLz,1) + "kNm");
       
        summentxtLR[0] = sb.toString();
        sb = new StringBuffer();
        
        //Summe der berechneten Lagerkräfte:
        sb.append(" Ax="); if(Fkt.fix(SummeAx,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeAx,1) + "kN  ");
        sb.append(" Ay="); if(Fkt.fix(SummeAy,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeAy,1) + "kN  ");
        sb.append(" Az="); if(Fkt.fix(SummeAz,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeAz,1) + "kN  ");
        sb.append(" MAx="); if(Fkt.fix(SummeMAx,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMAx,1) + "kNm  ");
        sb.append(" MAy="); if(Fkt.fix(SummeMAy,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMAy,1) + "kNm  ");
        sb.append(" MAz="); if(Fkt.fix(SummeMAz,1) >= 0) sb.append(" ");
        sb.append(Fkt.nf(SummeMAz,1) + "kNm");
        
        summentxtLR[1] = sb.toString();
        return summentxtLR;
    }
    
    private int anzlager() {
        int anz = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            switch (Kn[kn].getLagerbed()) {
                case FIX:
                case VERSCHIEBLICH:
                case SCHINENLAGER:
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
