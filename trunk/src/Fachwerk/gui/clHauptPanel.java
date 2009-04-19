/*
 * clHauptPanel.java
 *
 * Created on 6. September 2003, 13:09
 */

package Fachwerk.gui;

import Fachwerk.statik.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;


/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2006 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                                                      <qwert2003@users.berlios.de>
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
public class clHauptPanel extends javax.swing.JPanel implements inKonstante {
    
    // Datensatz    
    protected clKnoten[] Kn;
    protected clWissenderStab[] St;
    
    protected clDXF dxf; // Hintergrund
    protected double[][] mechanismusRelKnVersch; // Mechanismus (1.Index Knoten, 2. Index Rtg, Wert Relativverschiebung)
    
    // Zustandsvariablen
    protected boolean MIT_KnNr = false;
    protected boolean MIT_StabNr = false;
    protected boolean MIT_Auflagerkräften = false;
    protected boolean MIT_Lasten = false;
    protected boolean MIT_Stabkräften = false;
    protected boolean MIT_Hintergrund = false;
    protected boolean MIT_Mechanismus = false;
    private boolean MIT_Hilfslinie = false;
    private boolean MIT_Hilfsrechteck = false;
    
    private final int DESELEKT = 0;
    private final int KNOTEN = 1;
    private final int STAB = 2;
    private int[] Selektion = new int[2]; // index 0: STAB,KNOTEN,DESELEKT; index 1: Nr mit 1 beginnend
    protected boolean ZOOMALL = true;
    private boolean HINTERGRUNDSELEKTIERT = false;
    private Point2D hintergrundselpkt = new Point2D.Double();
    
    
    
     // Programmiertechnische Variablen
    protected Graphics2D g; 
    protected clKoord koord;
    boolean debug = true;
    protected Point2D ZoomPkt1 = new Point2D.Double(); // Zoom-Variablen
    protected Point2D ZoomPkt2 = new Point2D.Double();
    private Point2D hilfslin_von = new Point2D.Double(); // für Hilfslinie und -rechteck in Panelkoord.
    private Point2D hilfslin_bis = new Point2D.Double();
    protected double maxMechSkal; // max. Skalierfaktor für Mechanismen (durch zoomall bestimmt)
    
    // maximale Pfeillänge
    protected double maxPfeil = 80; // hängt mit ZoomAll() zusammen.
    protected double spitzenlängeMax = 15;
    protected double spitzenlängeMin = 7;
    protected float lagerhöhe = 14;
    
    // Schrift    
    protected int schriftgrStd = 10; // Standartschriftgrösse
    protected double faktorza = 1.2; // Zeilensabstand
    private final double bzuh = 0.61; // Breite zur Höhe eines Buchstabens
    
    // Mechanismus, max. dargestellte Verschiebung: Max(Zoomausschnittbreite,-höhe)/mechDarstFaktor
    protected final double mechDarstFaktor = 15;
    
    /*
     // Lagerattribute
    protected final int LOS = 0;
    protected final int FIX = 2;
    protected final int VERSCHIEBLICH = 1;
    // Stabattribute
    protected final int UNBEST = 0;
    protected final int BER = 1;
    protected final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    protected final int NICHTSETZBAR = 4;
    // Knotenattribute
    protected final int OFFEN = 0;
    protected final int FERTIG = 1;
    protected final int WIDERSPRUCH = 3;
    */
    
    /** Creates new form clHauptPanel */
    public clHauptPanel(clKnoten[] p_Knotenarray, clWissenderStab[] p_Stabarray) {
        
        Kn = new clKnoten[p_Knotenarray.length];
        St = new clWissenderStab[p_Stabarray.length];
        
        for (int i = 0; i < Kn.length; i++) {
            Kn[i] = p_Knotenarray[i];
        }
        for (int i = 0; i < St.length; i++) {
            St[i] = p_Stabarray[i];
        }
        
        initComponents();
    }
    
    
    public clHauptPanel(clKnoten[] p_Knotenarray, clWissenderStab[] p_Stabarray, boolean drucken) {
        assert drucken = true;
        
        Kn = new clKnoten[p_Knotenarray.length];
        St = new clWissenderStab[p_Stabarray.length];
        
        for (int i = 0; i < Kn.length; i++) {
            Kn[i] = p_Knotenarray[i];
        }
        for (int i = 0; i < St.length; i++) {
            St[i] = p_Stabarray[i];
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMinimumSize(new java.awt.Dimension(600, 400));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    
    // ------------------------------------------------------
    // EIGENTLICHER ZEICHNUNGVORGANG (nicht direkt aufrufber)
    // ------------------------------------------------------
    
    public void paint(Graphics g_1D) {
        g = (Graphics2D) g_1D;
        
        // Zeichenfläche weiss übermalen
        Rectangle2D.Double übermalen = new Rectangle2D.Double(0d, 0d, this.getWidth(),this.getHeight());
        g.setPaint(Color.white); g.fill(übermalen); g.setPaint(Color.black);
        
        if (ZOOMALL) {
            zoomAll(false);
            koord = new clKoord(ZoomPkt1, ZoomPkt2, this, maxPfeil);
        }
        else koord = new clKoord(ZoomPkt1, ZoomPkt2, this);
        
        if (Kn != null) {
            if (MIT_Hintergrund) darstellenHintergrund();
            darstellenFachwerk(true);
            if (MIT_KnNr) darstellenKnNr();
            if (MIT_StabNr) darstellenStabNr();
            if (MIT_Lasten) darstellenLasten(MIT_Stabkräften);
            if (MIT_Auflagerkräften) darstellenAuflagerkräfte(MIT_Stabkräften);
            if (MIT_Stabkräften) darstellenStabkräfte();
            if (MIT_Mechanismus) darstellenMechanismus();
            if (MIT_Hilfslinie || MIT_Hilfsrechteck) darstellenHilfslinien();
            darstellenHilfspunkt();
        }
    }
    
    
    protected void darstellenFachwerk(boolean mitResultaten) {
        // Durchlaufvariablen
        Point2D pkt = new Point2D.Double(); // jeweils aktueller Pkt;
        Point2D pkt2 = new Point2D.Double(); // jeweils aktueller Pkt;
        double durchmesser;
        
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            pkt.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getZ());
            pkt2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getZ());
            Line2D.Double linie = new Line2D.Double(koord.panel(pkt),koord.panel(pkt2));
            
            if (mitResultaten) { // im allgemeinen, ausser wenn nur das System gezeichnet werden soll (zum drucken).
                // falls selektiert zuerst breiteren Stab malen
                if (Selektion[0] == STAB && Selektion[1] == i) {
                    g.setPaint(Color.ORANGE);
                    g.setStroke(new BasicStroke(9f));
                    g.draw(linie);
                    g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
                }
                
                switch (St[i].stab.getStatus()) {
                    case UNBEST:
                        //BasicStroke stil = new BasicStroke(1f,CAP_BUTT,JOIN_BEVEL,10f,{5f,10f},4f);
                        g.setStroke(new BasicStroke(1f));
                        break;
                    case BER:
                        g.setStroke(new BasicStroke(3f));
                        if (St[i].stab.getKraft() < 0) g.setPaint(Color.GREEN);
                        break;
                    case GESETZT:
                        g.setPaint(Color.blue);
                        if (St[i].stab.getKraft() < 0) g.setPaint(Color.CYAN);
                        g.setStroke(new BasicStroke(3f));
                        break;
                    default:
                        assert false;
                }
            }
            g.draw(linie);
            
            g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
        }
        
        // Knoten
        for (int i = 1; i < Kn.length; i++) {
            pkt.setLocation(Kn[i].getX(), Kn[i].getZ());
            switch (Kn[i].getKnotenstatus()) {
                case OFFEN:
                    durchmesser = 6;  // bis und mit version 0.07: 5
                    break;
                case WIDERSPRUCH:
                    durchmesser = 12;
                    g.setPaint(Color.red);
                    break;
                case FERTIG:
                    durchmesser = 3;  // bis und mit version 0.07: 6
                    break;
                default:
                    durchmesser = 1;
                    assert false;
            }
            switch (Kn[i].getLagerbed()) {
                case LOS:
                    break;
                case FIX:
                    g.draw(Lagerfix(koord.panel(pkt)));
                    break;
                case VERSCHIEBLICH:
                    g.draw(Lagerversch(koord.panel(pkt), Kn[i].getRalpha()));
                    
                    break;
                default:
                    assert false;
            }
            
            
            g.fill(kreis(koord.panel(pkt), durchmesser));
            
            if (Selektion[0] == KNOTEN && Selektion[1] == i) {
                g.setPaint(Color.ORANGE); g.setStroke(new BasicStroke(2f));
                g.draw(kreis(koord.panel(pkt), 2d * durchmesser));
            }
            
            g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
        }
        
    }
    
    
    protected void darstellenKnNr() {
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgrStd));
        Point2D pkt_m = new Point2D.Double();
        Point2D pkt_pix = new Point2D.Double();
        // Knoten
        for (int i = 1; i < Kn.length; i++) {
            pkt_m.setLocation(Kn[i].getX(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt_m);
            pkt_pix.setLocation(pkt_pix.getX() + schriftgrStd/2d, pkt_pix.getY() - schriftgrStd/3d);
            g.drawString(""+i, (float) pkt_pix.getX(), (float) pkt_pix.getY());            
        }
    }
    
    protected void darstellenStabNr() {
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgrStd));
        Point2D pkt_m1 = new Point2D.Double();
        Point2D pkt_m2 = new Point2D.Double();
        Point2D pkt_pix = new Point2D.Double();        
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        double dx, dy;
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            pkt_m1.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getZ());
            pkt_m2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getZ());
            pkt_pix = koord.panel(mitte(pkt_m1, pkt_m2));
            // Rotationswinkel ermitteln
            dx = pkt_m2.getX() - pkt_m1.getX();
            dy = pkt_m2.getY() - pkt_m1.getY();
            if (dx == 0d) alphaRot = -Math.PI/2d;
            else alphaRot = Math.atan(dy/dx);
            // Rotieren und schieben
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);
            g.translate(0d, -schriftgrStd/3d);
            g.drawString(""+i, (float) pkt_pix.getX(), (float) pkt_pix.getY());
            g.setTransform(aT); // Rotation und Verschiebung zurückstellen
        }
    }
    
    protected void darstellenLasten(boolean kN_beschriften) {
        // Pfeile
        double TOL = maxkraft()/100d;
        double l; // Pfeillänge
        double L; // Kraft
        double x, z;
        Point2D pkt = new Point2D.Double();
        Point2D pkt_pix = new Point2D.Double();
        // Beschriftung
        int schriftgr = schriftgrStd + 2;
        g.setFont(new Font("Monospaced", Font.BOLD, schriftgr));
        g.setPaint(Color.blue); // gilt für Schrift, Pfeilfarbe wird direkt gesetzt
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        String beschriftung;
        
        for (int i = 1; i < Kn.length; i++) {
            pkt.setLocation(Kn[i].getX(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt);
            x = pkt_pix.getX();
            z = pkt_pix.getY();
            L = Math.sqrt(Math.pow(Kn[i].getLx(), 2) + Math.pow(Kn[i].getLz(), 2));
            // Pfeil zeichnen
            if (L > TOL) pfeil(x, z, 
            x + Kn[i].getLx() / maxkraft() * maxPfeil, 
            z + Kn[i].getLz() / maxkraft() * maxPfeil,Color.blue);
            
            // Beschriften aller Lasten
            if (!kN_beschriften || L == 0d ) continue;
            beschriftung = Fkt.nf(L,0);
            // Rotationswinkel ermitteln
            if (Kn[i].getLx() == 0d) alphaRot = -Math.PI/2d;
            else alphaRot = Math.atan(Kn[i].getLz()/Kn[i].getLx());
            // Zentrum platzieren, rotieren und schieben
            double pfeillänge_pix = L / maxkraft() * maxPfeil;
            double spitzenlänge = pfeillänge_pix / 4d;
            if (spitzenlänge > spitzenlängeMax) spitzenlänge = spitzenlängeMax;
            if (spitzenlänge < spitzenlängeMin) spitzenlänge = spitzenlängeMin;
            double rotpkt = pfeillänge_pix - spitzenlänge - bzuh*schriftgr*beschriftung.length() / 2d - 1d; // von Pfeilbeginn in Pfeilrtg gemessen
            pkt_pix.setLocation(x + Kn[i].getLx() / L * rotpkt, z + Kn[i].getLz() / L * rotpkt);
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);
            g.translate(-bzuh*schriftgr*beschriftung.length() / 2d, -schriftgr/4d);
            g.drawString(beschriftung, (float) pkt_pix.getX(), (float) pkt_pix.getY());
            g.setTransform(aT); // Rotation und Verschiebung zurückstellen
        }
        g.setPaint(Color.black);
    }
    
    protected void darstellenAuflagerkräfte(boolean kN_beschriften) {
        double TOL = maxkraft()/100d;
        double l; // Pfeillänge
        double A; // Kraft
        double x, z;
        Point2D pkt = new Point2D.Double();
        Point2D pkt_pix = new Point2D.Double();
        // Beschriftung
        int schriftgr = schriftgrStd + 2;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        g.setPaint(Color.red); // gilt für Schrift, Pfeilfarbe wird direkt gesetzt
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        String beschriftung;
        
        for (int i = 1; i < Kn.length; i++) {
            pkt.setLocation(Kn[i].getX(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt);
            x = pkt_pix.getX();
            z = pkt_pix.getY();
            A = Math.sqrt(Math.pow(Kn[i].getRx(), 2) + Math.pow(Kn[i].getRz(), 2));
            if (A > TOL) pfeil(x, z, 
            x + Kn[i].getRx() / maxkraft() * maxPfeil, 
            z + Kn[i].getRz() / maxkraft() * maxPfeil,Color.red);
            
            // Beschriften aller (bekannter) Auflagerkräfte
            if (!kN_beschriften || A == 0d ) continue;
            beschriftung = Fkt.nf(A,0);
            // Rotationswinkel ermitteln
            if (Math.abs(Kn[i].getRx()) < TOL_resultatcheck) {
                if (Math.abs(A) < TOL_resultatcheck) alphaRot = 0;
                else alphaRot = -Math.PI/2d;
            }
            else alphaRot = Math.atan(Kn[i].getRz()/Kn[i].getRx());
            // Zentrum platzieren, rotieren und schieben
            double pfeillänge_pix = A / maxkraft() * maxPfeil;
            double spitzenlänge = pfeillänge_pix / 4d;
            if (spitzenlänge > spitzenlängeMax) spitzenlänge = spitzenlängeMax;
            if (spitzenlänge < spitzenlängeMin) spitzenlänge = spitzenlängeMin;
            double rotpkt = pfeillänge_pix - spitzenlänge - bzuh*schriftgr*beschriftung.length() / 2d - 1d; // von Pfeilbeginn in Pfeilrtg gemessen
            pkt_pix.setLocation(x + Kn[i].getRx() / A * rotpkt, z + Kn[i].getRz() / A * rotpkt);
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);
            g.translate(-bzuh*schriftgr*beschriftung.length() / 2d, -schriftgr/4d);
            g.drawString(beschriftung, (float) pkt_pix.getX(), (float) pkt_pix.getY());
            g.setTransform(aT); // Rotation und Verschiebung zurückstellen
        }
        g.setPaint(Color.black);
    }
    
    protected void darstellenStabkräfte() {
        int schriftgr = schriftgrStd + 2;
        Font SchriftPlain = new Font("Monospaced", Font.PLAIN, schriftgr);
        Font SchriftFett  = new Font("Monospaced", Font.BOLD, schriftgr);
        g.setFont(SchriftPlain);
        Point2D pkt_m1 = new Point2D.Double();
        Point2D pkt_m2 = new Point2D.Double();
        Point2D pkt_pix = new Point2D.Double();
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        double dx, dy;
        String beschriftung;
        
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            pkt_m1.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getZ());
            pkt_m2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getZ());
            pkt_pix = koord.panel(mitte(pkt_m1, pkt_m2));
            
            // Rotationswinkel ermitteln
            dx = pkt_m2.getX() - pkt_m1.getX();
            dy = pkt_m2.getY() - pkt_m1.getY();
            if (dx == 0d) alphaRot = -Math.PI/2d;
            else alphaRot = Math.atan(dy/dx);
            // Rotieren und schieben
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);
            g.translate(0d, -schriftgr/3d);
            
            switch (St[i].stab.getStatus()) {
                case UNBEST:
                    break;
                case GESETZT:
                    g.setPaint(Color.blue);
                    g.setFont(SchriftFett);
                    // Programmfluss geht weiter
                case BER:
                    beschriftung = Fkt.nf(St[i].stab.getKraft(),0);
                    // zentrieren
                    g.translate(-bzuh*schriftgr*beschriftung.length() / 2d, 0d);
                    g.drawString(beschriftung, (float) pkt_pix.getX(), (float) pkt_pix.getY()); // + " kN"
                    break;
                default:
                    assert false;
            }
            g.setTransform(aT); // Rotation und Verschiebung zurückstellen
            g.setFont(SchriftPlain);
            g.setPaint(Color.black);
        }
    }
    
    
    protected void darstellenHintergrund() {
        assert dxf != null;
        // Durchlaufvariablen
        Point2D pkt = new Point2D.Double(); // jeweils aktueller Pkt;
        Point2D pkt2 = new Point2D.Double(); // jeweils aktueller Pkt;
        
        g.setPaint(Color.gray); g.setStroke(new BasicStroke(1f));
        double durchmesser = 3;
        
        // Hintergrundlinien
        ArrayList hgLinien = dxf.getHgLinien();
        clHintergrundLinie aktuell;
        Line2D.Float linie;
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie) it.next();
            pkt.setLocation(aktuell.getVon()[0], aktuell.getVon()[1]);
            pkt2.setLocation(aktuell.getBis()[0], aktuell.getBis()[1]);
            linie = new Line2D.Float(koord.panel(pkt),koord.panel(pkt2));
            g.draw(linie);
            linie = null;
        }              
        
        // Hintergrundpunkte
        ArrayList hgPunkte = dxf.getHgPunkte();
        clHintergrundPunkt aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt) it.next();
            pkt.setLocation(aktpkt.getPkt()[0], aktpkt.getPkt()[1]);
            g.fill(kreis(koord.panel(pkt), durchmesser));
        }
        
        // Hintergrundkreise
        ArrayList hgKreise = dxf.getHgKreise();  
        clHintergrundKreis aktkreis;
        double kreisdurchmesser;
        for (Iterator it = hgKreise.iterator(); it.hasNext();) {
            aktkreis = (clHintergrundKreis) it.next();
            pkt.setLocation(aktkreis.getZentrum()[0], aktkreis.getZentrum()[1]);
            kreisdurchmesser = 2d * aktkreis.getRadius();
            g.draw(kreis(koord.panel(pkt), koord.panel(kreisdurchmesser)));
        }
        
        // Hintergrundbögen
        ArrayList hgBogen = dxf.getHgBogen();
        clHintergrundBogen aktbogen;
        double radius;
        float startwinkel;
        float extent;
        Point2D pktpanel;
        Rectangle2D.Float quadr;
        Arc2D.Float bogen;
        for (Iterator it = hgBogen.iterator(); it.hasNext();) {
            aktbogen = (clHintergrundBogen) it.next();
            radius = aktbogen.getRadius();
            pkt.setLocation(aktbogen.getZentrum()[0]-radius, aktbogen.getZentrum()[1]-radius);
            pktpanel = koord.panel(pkt);
            quadr = new Rectangle2D.Float((float)pktpanel.getX(), (float)pktpanel.getY(), 
                                                 (float)koord.panel(2d*radius), (float)koord.panel(2d*radius));
            startwinkel = (float)aktbogen.getSektor()[0];
            extent = (float)aktbogen.getSektor()[1] - (float)aktbogen.getSektor()[0];
            if (extent < 0) extent += 360;
            bogen = new Arc2D.Float(quadr, startwinkel, extent, java.awt.geom.Arc2D.OPEN);
            g.draw(bogen);
        }        
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
    }
    
    protected void darstellenMechanismus() { // TODO fliessende gesetzte Stäbe dick
        if (mechanismusRelKnVersch == null) return; // erlaubt den generellen Aufruf von darstellenMechanismus(), in clPrintPanel
        // Durchlaufvariablen
        Point2D pkt = new Point2D.Double(); // jeweils aktueller Pkt;
        Point2D pkt2 = new Point2D.Double(); // jeweils aktueller Pkt;
        double dx1; double dz1; // Relativverschiebung des Mechanismus; jeweils aktueller Pkt;
        double dx2; double dz2; // Relativverschiebung des Mechanismus; jeweils aktueller Pkt;
        double[] ausschnitt = new double[2];
        ausschnitt[0] = Math.abs(ZoomPkt2.getX()-ZoomPkt1.getX());
        ausschnitt[1] = Math.abs(ZoomPkt2.getY()-ZoomPkt1.getY());
        double skal = (Fkt.max(ausschnitt) / mechDarstFaktor + maxMechSkal) / 2d;
        if (skal > maxMechSkal) skal = maxMechSkal;
        g.setPaint(Color.red); g.setStroke(new BasicStroke(2f));
        
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            dx1 = mechanismusRelKnVersch[St[i].von][0];
            dz1 = mechanismusRelKnVersch[St[i].von][1];
            dx2 = mechanismusRelKnVersch[St[i].bis][0];
            dz2 = mechanismusRelKnVersch[St[i].bis][1];
            pkt.setLocation(Kn[St[i].von].getX() + dx1*skal, Kn[St[i].von].getZ() + dz1*skal);
            pkt2.setLocation(Kn[St[i].bis].getX() + dx2*skal, Kn[St[i].bis].getZ() + dz2*skal);
            
            Line2D.Double linie = new Line2D.Double(koord.panel(pkt),koord.panel(pkt2));
            g.draw(linie);
        }
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
    }
    
    private void darstellenHilfslinien() {
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
        if (MIT_Hilfslinie) {
            Line2D.Double linie = new Line2D.Double(hilfslin_von, hilfslin_bis);
            g.draw(linie);
        }
        if (MIT_Hilfsrechteck) {
            Rectangle2D rechteck = new Rectangle2D.Double(hilfslin_von.getX(), hilfslin_von.getY(),
                        hilfslin_bis.getX()-hilfslin_von.getX(), hilfslin_bis.getY()-hilfslin_von.getY());
            g.draw(rechteck);
        }
    }
    
    private void darstellenHilfspunkt() { // Selektierter Hintergrundpunkt
        if (HINTERGRUNDSELEKTIERT) { // eigentlich unnötige
            double pktdurchmesser = 5;
            g.setPaint(Color.orange); g.setStroke(new BasicStroke(1f));
            g.fill(kreis(koord.panel(hintergrundselpkt), pktdurchmesser));
            g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
        }
    }
    
    
    // --------------------
    // ÖFFENTLICHE METHODEN
    // --------------------
    
    public void neuzeichnen() {
        invalidate();
        repaint();
    }
    
    
    public void zoomAll(boolean befehl) {
        double minX;
        double maxX;
        double minZ;
        double maxZ;
        
        ZOOMALL = true;
        
        if (Kn.length-1 >= 1) {
            
            minX = Kn[1].getX();
            maxX = Kn[1].getX();
            minZ = Kn[1].getZ();
            maxZ = Kn[1].getZ();
            for (int i= 2; i < Kn.length; i++) {
                if (Kn[i].getX() > maxX) maxX = Kn[i].getX();
                if (Kn[i].getX() < minX) minX = Kn[i].getX();
                if (Kn[i].getZ() > maxZ) maxZ = Kn[i].getZ();
                if (Kn[i].getZ() < minZ) minZ = Kn[i].getZ();
            }
            if (minX == maxX && minZ == maxZ) {
                minX = Kn[1].getX() - 6;
                maxX = Kn[1].getX() + 6;
                minZ = Kn[1].getZ() - 4;
                maxZ = Kn[1].getZ() + 4;
            }
        }
        else {
            assert (Kn.length-1 < 1);
            minX = -1;
            maxX = 11;
            minZ = -4;
            maxZ = 4;           
        }
        
        Point2D obenlinks = new Point2D.Double();
        Point2D untenrechts = new Point2D.Double();
        obenlinks.setLocation(minX, minZ);
        untenrechts.setLocation(maxX, maxZ);
        
        final double Lx = untenrechts.getX() - obenlinks.getX();
        final double Lz = untenrechts.getY() - obenlinks.getY();
        if (Lx>Lz) maxMechSkal = Lx/mechDarstFaktor;
        else maxMechSkal = Lz/mechDarstFaktor;
        
        ZoomPkt1 = obenlinks;
        ZoomPkt2 = untenrechts;
        if (befehl) neuzeichnen();
        
    }    
    
    public void zoomxy(Point2D p_Pkt1, Point2D p_Pkt2) {
        if (p_Pkt1.equals(p_Pkt2)) zoomAll(true);
        else {
            ZOOMALL = false;
            ZoomPkt1 = p_Pkt1;
            ZoomPkt2 = p_Pkt2;
            neuzeichnen();
        }
        
    }
    
    public void ZeigeKnNr(boolean zeigen) {
        if (zeigen) MIT_KnNr = true;
        else MIT_KnNr = false;
        neuzeichnen();
    }
    
    public void ZeigeStabNr(boolean zeigen) {
        if (zeigen) MIT_StabNr = true;
        else MIT_StabNr = false;
        neuzeichnen();
    }
    
    public void ZeigeStabkräfte(boolean zeigen) {
        if (zeigen) MIT_Stabkräften = true;
        else MIT_Stabkräften = false;
        neuzeichnen();
    }
    
    public void ZeigeLasten(boolean zeigen) {
        if (zeigen) MIT_Lasten = true;
        else MIT_Lasten = false;
        neuzeichnen();
    }
    
    public void ZeigeAuflagerkräfte(boolean zeigen) {
        if (zeigen) MIT_Auflagerkräften = true;
        else MIT_Auflagerkräften = false;
        neuzeichnen();
    }
    
    public void ZeigeHintergrund(boolean zeigen, clDXF dxf) {
        if (zeigen) {
            assert dxf != null;
            MIT_Hintergrund = true;
            this.dxf = dxf;
        }
        else MIT_Hintergrund = false;
        neuzeichnen();
    }
    
    public void ZeigeMechanismus(boolean zeigen, double[][] mechanismusRelKnVersch) {
        if (zeigen && mechanismusRelKnVersch != null) {
            MIT_Mechanismus = true;
            this.mechanismusRelKnVersch = mechanismusRelKnVersch;
        }
        else MIT_Mechanismus = false;
    }
    
    public void ZeigeHilfslinie(boolean zeigen) {
        assert zeigen == false;
        MIT_Hilfslinie = false;
        neuzeichnen();
    }
    public void ZeigeHilfslinie(boolean zeigen, Point2D von_panel, Point2D bis_panel) {
        MIT_Hilfslinie = zeigen;
        hilfslin_von = von_panel;
        hilfslin_bis = bis_panel;
        neuzeichnen();
    }
    
    public void ZeigeHilfsRechteck(boolean zeigen) {
        assert zeigen == false;
        MIT_Hilfsrechteck = false;
        neuzeichnen();
    }
    public void ZeigeHilfsRechteck(boolean zeigen, Point2D von_panel, Point2D bis_panel) {
        MIT_Hilfsrechteck = zeigen;
        double minX, maxX, minZ, maxZ;
        if (von_panel.getX() < bis_panel.getX()) {
            minX = von_panel.getX();
            maxX = bis_panel.getX();
        }
        else {
            minX = bis_panel.getX();
            maxX = von_panel.getX();
        }
        if (von_panel.getY() < bis_panel.getY()) {
            minZ = von_panel.getY();
            maxZ = bis_panel.getY();
        }
        else {
            minZ = bis_panel.getY();
            maxZ = von_panel.getY();
        }
        hilfslin_von.setLocation(minX, minZ);
        hilfslin_bis.setLocation(maxX, maxZ);
        neuzeichnen();
    }
    
    public void selekt(int[] p_Selektion) {                     //(int was, int nr) {
        switch (p_Selektion[0]) {
            case KNOTEN:                
            case STAB:
            case DESELEKT:
                Selektion[0] = p_Selektion[0];
                Selektion[1] = p_Selektion[1];
                break;
            default:
                assert false;
        }
        neuzeichnen();
    }
    
    public void selektHintergrund(double pktx, double pktz) {
        HINTERGRUNDSELEKTIERT = true;
        //assert Selektion[0] == DESELEKT; darf nicht erwartet werden, da sonst SCHIEBEN nicht geht.
        hintergrundselpkt.setLocation(pktx, pktz);
        neuzeichnen();
    }
    
    public void deselektHintergrund() {
        HINTERGRUNDSELEKTIERT = false;
        neuzeichnen();
    }
    
    public clKoord getKoord() { 
        return koord;
    }
    
    /** Gibt die Knotenliste zurück. Nötig für befehlDruckenGraph. */
    public clKnoten[] getKn(){
        return Kn;
    }
    
    /** Gibt die Stabliste zurück. Nötig für befehlDruckenGraph. */
    public clWissenderStab[] getSt(){
        return St;
    }
    /** Gibt die Eckpunkte des Zoomfensters zurück. Nötig für befehlDruckenGraph.
    */
    public Point2D[] getZoomPkte() {
        Point2D[] zoomPkte = {ZoomPkt1, ZoomPkt2};
        return zoomPkte;
    }
    
    /** Gibt an, ob ZOOMALL aktiviert ist.
     */
    public boolean istZoomAll() {
        return ZOOMALL;
    }
    
    
    /** Gibt an, welche Layer aktiv sind. Nötig für befehlDruckenGraph.
     * Reihenfolge: KnNr, StabNr, Auflagerkräfte, Lasten, Stabkräfte, Hintergrund, Mechanismus
    */
    public boolean[] getAktiveLayer() {
        boolean layer[] = new boolean[7];
        layer[0] = MIT_KnNr;
        layer[1] = MIT_StabNr;
        layer[2] = MIT_Lasten;
        layer[3] = MIT_Auflagerkräften;
        layer[4] = MIT_Stabkräften;
        layer[5] = MIT_Hintergrund;
        layer[6] = MIT_Mechanismus;
        return layer;
    }
    
    
    // --------------------------
    // INTERNE ZEICHNUNGSELEMENTE
    // --------------------------
    
    protected int schriftskal(double Feldhöhe, double Feldbreite, int anzzeilen, int anzbuchst, int Wunschgrösse) {
        int schriftgr = Wunschgrösse;
        if ((double)anzzeilen * faktorza * (double)schriftgr > Feldhöhe) {
            schriftgr = (int) (Feldhöhe / ((double)anzzeilen * faktorza));
            // debug
            //if (b.verbose) System.out.println("Schrift klein, da Feldhöhe massgebend: " + schriftgr);
        }
        if ((double)anzbuchst * bzuh * (double)schriftgr > Feldbreite) {
            //if (b.verbose) System.out.println((double)anzbuchst * bzuh * (double)schriftgr + " > " + Feldbreite);
            schriftgr = (int) (Feldbreite / ((double)anzbuchst * bzuh));
            // debug
            //if (b.verbose) System.out.println("Schrift klein, da Feldbreite massgebend: " + schriftgr);           
        }
        if (schriftgr < 2) schriftgr = 0;
        // debug
        //if (b.verbose) System.out.println("Schriftgrösse: " + schriftgr); 
        return schriftgr;            
    }
    
    private void pfeil(double vonX, double vonZ, double bisX, double bisZ, Paint farbe) {
        Paint origPaint = g.getPaint(); Stroke origStroke = g.getStroke(); // ursprüngliche Stricheinstellungen
        g.setPaint(farbe); g.setStroke(new BasicStroke(2.0f));
        double spitzenlänge = Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2)) / 4d; // bei allfälliger Anpassung, auch darstellenLasten() resp. Auflkr. anpassen
        if (spitzenlänge > spitzenlängeMax) spitzenlänge = spitzenlängeMax; //15;
        if (spitzenlänge < spitzenlängeMin) spitzenlänge = spitzenlängeMin; //7;
        double dx = spitzenlänge * (bisX-vonX) / Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2));
        double dz = spitzenlänge * (bisZ-vonZ) / Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2));
        if (Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2)) >= spitzenlänge) {
            g.draw(new Line2D.Double(vonX,vonZ,bisX - dx/2d ,bisZ - dz/2d));
        }
        GeneralPath spitze = new GeneralPath();
        spitze.moveTo((float)bisX, (float)bisZ);
        spitze.lineTo((float)(bisX - dx + dz/2d), (float)(bisZ - dz - dx/2d));
        spitze.lineTo((float)(bisX - dx - dz/2d), (float)(bisZ - dz + dx/2d));
        spitze.closePath();
        g.fill(spitze);
        g.setPaint(origPaint); g.setStroke(origStroke); // ursprüngliche Stricheinstellungen setzen
    }
    
    
    private GeneralPath Lagerfix(Point2D pkt) {
        float höhe = lagerhöhe;
        GeneralPath polygon = new GeneralPath();
        polygon.moveTo((float) pkt.getX(), (float) pkt.getY());
        polygon.lineTo((float) (pkt.getX() - 0.6f * höhe), (float) (pkt.getY() + höhe));
        polygon.lineTo((float) (pkt.getX() + 0.6f * höhe), (float) (pkt.getY() + höhe));
        polygon.closePath();
        return polygon;
    }
    
    private GeneralPath Lagerversch(Point2D pkt, double alpha) {
        float h = lagerhöhe;
        float b = 0.6f * h;
        float c = (float) Math.cos(alpha);
        float s = (float) Math.sin(alpha);
        GeneralPath polygon = new GeneralPath();        
        polygon.moveTo((float) pkt.getX(), (float) pkt.getY());
        polygon.lineTo((float) (pkt.getX() - c * b - s * h), (float) (pkt.getY() - s * b + c * h));
        polygon.lineTo((float) (pkt.getX() + c * b - s * h), (float) (pkt.getY() + s * b + c * h));
        polygon.closePath();
        polygon.moveTo((float) (pkt.getX() - c * b - s * 1.2f * h), (float) (pkt.getY() - s * b + c * 1.2f * h));
        polygon.lineTo((float) (pkt.getX() + c * b - s * 1.2f * h), (float) (pkt.getY() + s * b + c * 1.2f * h));
        return polygon;
    }
    
    private Ellipse2D.Double kreis(Point2D zentrum, double durchmesser) {
        Ellipse2D.Double derkreis = new Ellipse2D.Double(zentrum.getX() - durchmesser/2d, zentrum.getY() - durchmesser/2d,
        durchmesser, durchmesser);
        return derkreis;
    }
    
    private Point2D mitte(Point2D pkt1, Point2D pkt2) {
        Point2D pkt = new Point2D.Double();
        pkt.setLocation((pkt1.getX()+pkt2.getX())/2d, (pkt1.getY()+pkt2.getY())/2d);
        return pkt;
    }
    
    private double maxkraft() {
        double max = 1;
        double L, R;
        for (int i = 1; i < Kn.length; i++) {
            if (MIT_Lasten || !MIT_Auflagerkräften) {
                L = Math.sqrt(Math.pow(Kn[i].getLx(), 2) + Math.pow(Kn[i].getLz(), 2));
                if (L > max) max = L;
            }
            
            if (MIT_Auflagerkräften) {
                R = Math.sqrt(Math.pow(Kn[i].getRx(), 2) + Math.pow(Kn[i].getRz(), 2));
                if (R > max) max = R;
            }
        }
        return max;
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
