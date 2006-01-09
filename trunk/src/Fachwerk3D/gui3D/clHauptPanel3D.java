/*
 * clHauptPanel.java
 *
 * Created on 6. September 2003, 13:09
 */

package Fachwerk3D.gui3D;

import Fachwerk3D.statik3D.*;
import Fachwerk.statik.Fkt;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;


/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2003 - 2005 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clHauptPanel3D extends javax.swing.JPanel implements inKonstante3D {
    
    // Datensatz    
    protected clKnoten3D[] Kn;
    protected clWissenderStab3D[] St;
    
    protected double[] projektionsrtg_n = {-0.3,-0.9, 0.2}; // Sichtrichtung zu Beginn  // TODO {0, -1, 0};
    
    protected clDXF3D dxf; // Hintergrund
    
    // Zustandsvariablen
    protected boolean MIT_KnNr = false;
    protected boolean MIT_StabNr = false;
    protected boolean MIT_Auflagerkräften = false;
    protected boolean MIT_Lasten = false;
    protected boolean MIT_Stabkräften = false;
    protected boolean MIT_Hintergrund = false;
    protected boolean MIT_MiniKoordSys = true;
    private boolean MIT_Hilfslinie = false;
    private boolean MIT_Hilfsrechteck = false;
    
    private final int DESELEKT = 0;
    private final int KNOTEN = 1;
    private final int STAB = 2;
    private int[] Selektion = new int[2]; // index 0: STAB,KNOTEN,DESELEKT; index 1: Nr mit 1 beginnend
    protected boolean ZOOMALL = true;
    private boolean HINTERGRUNDSELEKTIERT = false;
    private Point3D hintergrundselpkt = new Point3D();
    
    
    
     // Programmiertechnische Variablen
    protected Graphics2D g; 
    protected clKoord3D koord = new clKoord3D(projektionsrtg_n);
    boolean debug = true;
    protected Point2D ZoomPkt1 = new Point2D.Double(); // Zoom-Variablen
    protected Point2D ZoomPkt2 = new Point2D.Double();
    private Point2D hilfslin_von = new Point2D.Double(); // für Hilfslinie und -rechteck in Panelkoord.
    private Point2D hilfslin_bis = new Point2D.Double();
    
    // maximale Pfeillänge
    protected double maxPfeil = 80; // hängt mit ZoomAll() zusammen.
    protected double spitzenlängeMax = 15;
    protected double spitzenlängeMin = 7;
    protected float lagerhöhe = 14;
    
    // Schrift    
    protected int schriftgrStd = 10; // Standartschriftgrösse
    protected double faktorza = 1.2; // Zeilensabstand
    private final double bzuh = 0.61; // Breite zur Höhe eines Buchstabens
    
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
    
        
    /** Creates new Panel clHauptPanel */
    public clHauptPanel3D(clKnoten3D[] p_Knotenarray, clWissenderStab3D[] p_Stabarray, boolean drucken) {
        
        Kn = new clKnoten3D[p_Knotenarray.length];
        St = new clWissenderStab3D[p_Stabarray.length];
        
        for (int i = 0; i < Kn.length; i++) {
            Kn[i] = p_Knotenarray[i];
        }
        for (int i = 0; i < St.length; i++) {
            St[i] = p_Stabarray[i];
        }
        
        if (!drucken) initComponents();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        setMinimumSize(new java.awt.Dimension(600, 400));
        setPreferredSize(new java.awt.Dimension(800, 600));
    }//GEN-END:initComponents

    
    // ------------------------------------------------------
    // EIGENTLICHER ZEICHNUNGVORGANG (nicht direkt aufrufber)
    // ------------------------------------------------------
    
    public void paint(Graphics g_1D) {
        //if (aktualisierunggesperrt) return; // während dem Drucken
        g = (Graphics2D) g_1D;
        
        // Zeichenfläche weiss übermalen
        Rectangle2D.Double übermalen = new Rectangle2D.Double(0d, 0d, this.getWidth(),this.getHeight());
        g.setPaint(Color.white); g.fill(übermalen); g.setPaint(Color.black);
        
        if (ZOOMALL) {
            zoomAll(false);
            koord.zoome(ZoomPkt1, ZoomPkt2, this, maxPfeil);
        }
        else {
            koord.zoome(ZoomPkt1, ZoomPkt2, this);
        }
        
        if (Kn != null) {
            if (MIT_MiniKoordSys) darstellenMiniKoordSys();
            if (MIT_Hintergrund) darstellenHintergrund();
            darstellenFachwerk(true);
            if (MIT_KnNr) darstellenKnNr();
            if (MIT_StabNr) darstellenStabNr();
            if (MIT_Lasten) darstellenLasten();
            if (MIT_Auflagerkräften) darstellenAuflagerkräfte();
            if (MIT_Stabkräften) darstellenStabkräfte();
            if (MIT_Hilfslinie || MIT_Hilfsrechteck) darstellenHilfslinien();
            darstellenHilfspunkt();
        }
        else {
            //if (debug) System.out.println("hp.paint: Kn inexistent");
            //if (debug) testZeigeKn();
        }
    }
    
    
    protected void darstellenFachwerk(boolean mitResultaten) {
        // Durchlaufvariablen
        Point3D pkt = new Point3D(); // jeweils aktueller Pkt;
        Point3D pkt2 = new Point3D(); // jeweils aktueller Pkt;
        double durchmesser;
        
        //if (debug)  System.out.println("darstellenFachwerk läuft. Anz Knoten: " + (Kn.length-1));
        //if (debug) testZeigeKn();
        
        // Stäbe    // bis und mit version 0.07: nach Knoten
        for (int i = 1; i < St.length; i++) {
            pkt.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getY(), Kn[St[i].von].getZ());
            pkt2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getY(), Kn[St[i].bis].getZ());
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
            pkt.setLocation(Kn[i].getX(), Kn[i].getY(), Kn[i].getZ());
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
                    Lagerversch(g, koord.panel(pkt), Kn[i].getRrtg());
                    break;
                case SCHINENLAGER:
                    Lagerschine(g, koord.panel(pkt), Kn[i].getRrtg());
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
        Point3D pkt_m = new Point3D();
        Point2D pkt_pix = new Point2D.Double();
        // Knoten
        for (int i = 1; i < Kn.length; i++) {
            pkt_m.setLocation(Kn[i].getX(), Kn[i].getY(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt_m);
            pkt_pix.setLocation(pkt_pix.getX() + schriftgrStd/2d, pkt_pix.getY() - schriftgrStd/2d);
            g.drawString(""+i, (float) pkt_pix.getX(), (float) pkt_pix.getY());            
        }
    }
    
    protected void darstellenStabNr() {
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgrStd));
        Point3D pkt_m1 = new Point3D();
        Point3D pkt_m2 = new Point3D();
        Point2D pkt_pix = new Point2D.Double();        
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        double dx, dy;
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            pkt_m1.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getY(), Kn[St[i].von].getZ());
            pkt_m2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getY(), Kn[St[i].bis].getZ());
            pkt_pix = mitte(koord.panel(pkt_m1), koord.panel(pkt_m2));
            // Rotationswinkel ermitteln
            dx = koord.panel(pkt_m2).getX() - koord.panel(pkt_m1).getX();  //pkt_m2.getX() - pkt_m1.getX();
            dy = koord.panel(pkt_m2).getY() - koord.panel(pkt_m1).getY();  //pkt_m2.getY() - pkt_m1.getY();
            if (dx == 0d) alphaRot = -Math.PI/2d;
            else alphaRot = Math.atan(dy/dx);
            // Rotieren und schieben
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);            
            g.translate(0d, -schriftgrStd/3);
            //pkt_pix.setLocation(pkt_pix.getX(),pkt_pix.getY() - schriftgrStd/2);
            g.drawString(""+i, (float) pkt_pix.getX(), (float) pkt_pix.getY());
            g.setTransform(aT); // Rotation und Verschiebung zurückstellen
        }
    }
    
    protected void darstellenLasten() {
        double TOL = maxkraft()/100d;
        double l; // Pfeillänge
        double L; // projizierte Kraft
        double x, z;
        Point3D pkt = new Point3D();
        Point2D pkt_pix = new Point2D.Double();
        for (int i = 1; i < Kn.length; i++) {
            pkt.setLocation(Kn[i].getX(), Kn[i].getY(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt);
            x = pkt_pix.getX();
            z = pkt_pix.getY();
            //L = Math.sqrt(Math.pow(Kn[i].getLx(), 2) + Math.pow(Kn[i].getLz(), 2));
            Point2D pfeilrtg = koord.projiziere(new Point3D(Kn[i].getLx(), Kn[i].getLy(), Kn[i].getLz()));
            L = Math.sqrt(Math.pow(pfeilrtg.getX(), 2) + Math.pow(pfeilrtg.getY(), 2));
            if (L > TOL) pfeil(x, z, 
            x + pfeilrtg.getX() / maxkraft() * maxPfeil, 
            z + pfeilrtg.getY() / maxkraft() * maxPfeil,Color.blue);
        }
    }
    
    protected void darstellenAuflagerkräfte() {
        double TOL = maxkraft()/100d;
        double l; // Pfeillänge
        double A; // projizierte Kraft
        double x, z;
        Point3D pkt = new Point3D();
        Point2D pkt_pix = new Point2D.Double();
        for (int i = 1; i < Kn.length; i++) {
            pkt.setLocation(Kn[i].getX(), Kn[i].getY(), Kn[i].getZ());
            pkt_pix = koord.panel(pkt);
            x = pkt_pix.getX();
            z = pkt_pix.getY();
            Point2D pfeilrtg = koord.projiziere(new Point3D(Kn[i].getRx(), Kn[i].getRy(), Kn[i].getRz()));
            A = Math.sqrt(Math.pow(pfeilrtg.getX(), 2) + Math.pow(pfeilrtg.getY(), 2));
            if (A > TOL) pfeil(x, z, 
            x + pfeilrtg.getX() / maxkraft() * maxPfeil, 
            z + pfeilrtg.getY() / maxkraft() * maxPfeil,Color.red);
        }
    }
    
    protected void darstellenStabkräfte() {
        int schriftgr = schriftgrStd + 2;
        Font SchriftPlain = new Font("Monospaced", Font.PLAIN, schriftgr);
        Font SchriftFett  = new Font("Monospaced", Font.BOLD, schriftgr);
        g.setFont(SchriftPlain);
        Point3D pkt_m1 = new Point3D();
        Point3D pkt_m2 = new Point3D();
        Point2D pkt_pix = new Point2D.Double();
        AffineTransform aT = g.getTransform(); // ursprünglicher Zustand vor dem Rotieren
        AffineTransform rotiert;
        double alphaRot; // Rotationswinkel
        double dx, dy;
        String beschriftung;
        
        // Stäbe
        for (int i = 1; i < St.length; i++) {
            pkt_m1.setLocation(Kn[St[i].von].getX(), Kn[St[i].von].getY(), Kn[St[i].von].getZ());
            pkt_m2.setLocation(Kn[St[i].bis].getX(), Kn[St[i].bis].getY(), Kn[St[i].bis].getZ());
            pkt_pix = mitte(koord.panel(pkt_m1), koord.panel(pkt_m2));
            
            // Rotationswinkel ermitteln
            dx = koord.panel(pkt_m2).getX() - koord.panel(pkt_m1).getX();
            dy = koord.panel(pkt_m2).getY() - koord.panel(pkt_m1).getY();
            if (dx == 0d) alphaRot = -Math.PI/2d;
            else alphaRot = Math.atan(dy/dx);
            // Rotieren und schieben
            rotiert = AffineTransform.getRotateInstance(alphaRot, (float) pkt_pix.getX(),(float) pkt_pix.getY());
            g.transform(rotiert);
            g.translate(0d, -schriftgr/3);
            
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
        Point3D pkt = new Point3D(); // jeweils aktueller Pkt;
        Point3D pkt2 = new Point3D(); // jeweils aktueller Pkt;
        
        g.setPaint(Color.gray); g.setStroke(new BasicStroke(1f));
        double durchmesser = 3;
        
        // Hintergrundlinien
        ArrayList hgLinien = dxf.getHgLinien();
        clHintergrundLinie3D aktuell;
        Line2D.Float linie;
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie3D) it.next();
            pkt.setLocation(aktuell.getVon()[0], aktuell.getVon()[1], aktuell.getVon()[2]);
            pkt2.setLocation(aktuell.getBis()[0], aktuell.getBis()[1], aktuell.getBis()[2]);
            linie = new Line2D.Float(koord.panel(pkt),koord.panel(pkt2));
            g.draw(linie);
            linie = null;
        }              
        
        // Hintergrundpunkte
        ArrayList hgPunkte = dxf.getHgPunkte();
        clHintergrundPunkt3D aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt3D) it.next();
            pkt.setLocation(aktpkt.getPkt()[0], aktpkt.getPkt()[1], aktpkt.getPkt()[2]);
            g.fill(kreis(koord.panel(pkt), durchmesser));
        }
        
        // Hintergrundkugeln
        ArrayList hgKugeln = dxf.getHgKugeln();  
        clHintergrundKugel3D aktkugel;
        double kreisdurchmesser;
        for (Iterator it = hgKugeln.iterator(); it.hasNext();) {
            aktkugel = (clHintergrundKugel3D) it.next();
            pkt.setLocation(aktkugel.getZentrum()[0], aktkugel.getZentrum()[1], aktkugel.getZentrum()[2]);
            kreisdurchmesser = 2d * aktkugel.getRadius();
            g.draw(kreis(koord.panel(pkt), koord.panel(kreisdurchmesser)));
        } 
        
        // Selektierter Hintergrundpunkt
        if (HINTERGRUNDSELEKTIERT) {
            double pktdurchmesser = 5;
            g.setPaint(Color.orange); g.setStroke(new BasicStroke(1f));
            g.fill(kreis(koord.panel(hintergrundselpkt), pktdurchmesser));
        }
        
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
    }
    
    protected void darstellenMiniKoordSys() {
        g.setPaint(Color.gray); g.setStroke(new BasicStroke(1f));
        Point2D.Double o = new Point2D.Double(30, 30);        
        Point2D x = koord.projiziere(new Point3D(25, 0, 0));
        Point2D y = koord.projiziere(new Point3D( 0,25, 0));
        Point2D z = koord.projiziere(new Point3D( 0, 0,25));
        x.setLocation(x.getX() + o.getX(), x.getY() + o.getY());
        y.setLocation(y.getX() + o.getX(), y.getY() + o.getY());
        z.setLocation(z.getX() + o.getX(), z.getY() + o.getY());
        g.draw(new Line2D.Double(o, x));
        g.draw(new Line2D.Double(o, y));
        g.draw(new Line2D.Double(o, z));
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1f));
        // x - Achse beschriften
        int schriftgr = 8;
        g.setFont(new Font("Monospaced", Font.PLAIN, schriftgr));
        g.drawString("X", (float) (x.getX() - 0.9 * bzuh * schriftgr/2d), (float) (x.getY() + 0.9*schriftgr/2d));
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
            Point2D Kn1 = koord.projiziere(new Point3D(Kn[1].getX(), Kn[1].getY(), Kn[1].getZ()));
            minX = Kn1.getX();
            maxX = Kn1.getX();
            minZ = Kn1.getY();
            maxZ = Kn1.getY();
            
            Point2D Kni;
            for (int i= 2; i < Kn.length; i++) {
                Kni = koord.projiziere(new Point3D(Kn[i].getX(), Kn[i].getY(), Kn[i].getZ()));
                if (Kni.getX() > maxX) maxX = Kni.getX();
                if (Kni.getX() < minX) minX = Kni.getX();
                if (Kni.getY() > maxZ) maxZ = Kni.getY();
                if (Kni.getY() < minZ) minZ = Kni.getY();
            }
            if (minX == maxX && minZ == maxZ) {
                minX = Kn1.getX() - 6;
                maxX = Kn1.getX() + 6;
                minZ = Kn1.getY() - 4;
                maxZ = Kn1.getY() + 4;
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
    
    public double[] getBlickRtg() {
        double [] n = new double[3];
        n[0] = projektionsrtg_n[0];
        n[1] = projektionsrtg_n[1];
        n[2] = projektionsrtg_n[2];
        return n;
    }
    
    /** Setzt die Projektionsrichtung n */
    public void setBlickRtg(double[] n) {
        assert n.length == 3;
        projektionsrtg_n[0] = n[0];
        projektionsrtg_n[1] = n[1];
        projektionsrtg_n[2] = n[2];
        koord = new clKoord3D(projektionsrtg_n);
        neuzeichnen();
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
    
    public void ZeigeHintergrund(boolean zeigen, clDXF3D dxf) {
        if (zeigen) {
            assert dxf != null;
            MIT_Hintergrund = true;
            this.dxf = dxf;
        }
        else MIT_Hintergrund = false;
        neuzeichnen();
    }
    
    public void ZeigeMiniKoordSys(boolean zeigen) {
        if (zeigen) MIT_MiniKoordSys = true;
        else MIT_MiniKoordSys = false;
        neuzeichnen();
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
    
    public void selektHintergrund(double pktx, double pkty, double pktz) {
        HINTERGRUNDSELEKTIERT = true;
        //assert Selektion[0] == DESELEKT; darf nicht erwartet werden, da sonst SCHIEBEN nicht geht.
        hintergrundselpkt.setLocation(pktx, pkty, pktz);
        neuzeichnen();
    }
    
    public void deselektHintergrund() {
        HINTERGRUNDSELEKTIERT = false;
        neuzeichnen();
    }
    
    public clKoord3D getKoord() { 
        return koord;
    }
    
    /** Gibt die Knotenliste zurück. Nötig für befehlDruckenGraph. */
    public clKnoten3D[] getKn(){
        return Kn;
    }
    
    /** Gibt die Stabliste zurück. Nötig für befehlDruckenGraph. */
    public clWissenderStab3D[] getSt(){
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
     * Reihenfolge: KnNr, StabNr, Auflagerkräfte, Lasten, Stabkräfte
    */
    public boolean[] getAktiveLayer() {
        boolean layer[] = new boolean[6];
        layer[0] = MIT_KnNr;
        layer[1] = MIT_StabNr;
        layer[2] = MIT_Lasten;
        layer[3] = MIT_Auflagerkräften;
        layer[4] = MIT_Stabkräften;
        layer[5] = MIT_Hintergrund;
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
        g.setPaint(farbe); g.setStroke(new BasicStroke(2.0f));        
        double spitzenlänge = Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2)) / 4d;
        if (spitzenlänge > spitzenlängeMax) spitzenlänge = spitzenlängeMax; //15;
        if (spitzenlänge < spitzenlängeMin) spitzenlänge = spitzenlängeMin; //7;
        double dx = spitzenlänge * (bisX-vonX) / Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2));
        double dz = spitzenlänge * (bisZ-vonZ) / Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2));
        if (Math.sqrt(Math.pow((bisZ-vonZ),2)+Math.pow((bisX-vonX),2)) >= spitzenlänge) { // NEU IN VER 0.05
            g.draw(new Line2D.Double(vonX,vonZ,bisX - dx/2d ,bisZ - dz/2d));
        }    
        GeneralPath spitze = new GeneralPath();
        spitze.moveTo((float)bisX, (float)bisZ);
        spitze.lineTo((float)(bisX - dx + dz/2d), (float)(bisZ - dz - dx/2d));
        spitze.lineTo((float)(bisX - dx - dz/2d), (float)(bisZ - dz + dx/2d));
        spitze.closePath();
        g.fill(spitze);
        g.setPaint(Color.black); g.setStroke(new BasicStroke(1.0f));
    }
    
        
    private GeneralPath Lagerfix(Point2D pkt) {
        float höhe = lagerhöhe; //14;
        GeneralPath polygon = new GeneralPath();        
        polygon.moveTo((float) pkt.getX(), (float) pkt.getY());
        polygon.lineTo((float) (pkt.getX() - 0.6f * höhe), (float) (pkt.getY() + höhe));
        polygon.lineTo((float) (pkt.getX() + 0.6f * höhe), (float) (pkt.getY() + höhe));
        polygon.closePath();
        return polygon;
    }
    
    private void Lagerversch(Graphics2D g, Point2D pkt, double[] p_rtg) {
        double höhe = lagerhöhe;
        double[] rtg = Fkt.normiere(p_rtg);
        Point2D rtg_2D = koord.projiziere(new Point3D(rtg[0], rtg[1], rtg[2]));
        Point2D piramidenboden = new Point2D.Double();
        piramidenboden.setLocation(pkt.getX()+höhe*rtg_2D.getX(), pkt.getY()+höhe*rtg_2D.getY());
        Point2D[] quadrat = quadratimRaum(piramidenboden, 1.2 * höhe, rtg);
        g.draw(new Line2D.Double(quadrat[0], quadrat[1]));
        g.draw(new Line2D.Double(quadrat[1], quadrat[2]));
        g.draw(new Line2D.Double(quadrat[2], quadrat[3]));
        g.draw(new Line2D.Double(quadrat[3], quadrat[0]));
        g.draw(new Line2D.Double(quadrat[0], pkt));
        g.draw(new Line2D.Double(quadrat[1], pkt));
        g.draw(new Line2D.Double(quadrat[2], pkt));
        g.draw(new Line2D.Double(quadrat[3], pkt));
        // Gleitplatte
        Point2D gleitplatte = new Point2D.Double();
        gleitplatte.setLocation(pkt.getX()+1.2*höhe*rtg_2D.getX(), pkt.getY()+1.2*höhe*rtg_2D.getY());
        quadrat = quadratimRaum(gleitplatte, 1.4 * höhe, rtg);
        g.draw(new Line2D.Double(quadrat[0], quadrat[1]));
        g.draw(new Line2D.Double(quadrat[1], quadrat[2]));
        g.draw(new Line2D.Double(quadrat[2], quadrat[3]));
        g.draw(new Line2D.Double(quadrat[3], quadrat[0]));
    }
    
    private void Lagerschine(Graphics2D g, Point2D pkt, double[] p_rtg) {
        double höhe = lagerhöhe;
        double länge = lagerhöhe*3d;
        double spurweite = 1.0d * höhe;
        
        double[] rtg = Fkt.normiere(p_rtg);
        clKoord3D tmp = new clKoord3D(rtg);
        double[] uw = tmp.getProjKoordSys();
        Point3D u = new Point3D(uw[0],uw[1], uw[2]);
        Point3D w = new Point3D(uw[3],uw[4], uw[5]); // in der Regel parallel zur z-Achse
        Point2D u_2D = koord.projiziere(u);
        Point2D w_2D = koord.projiziere(w);
        Point2D rtg_2D = koord.projiziere(new Point3D(rtg[0],rtg[1],rtg[2]));
        Point2D piramidenboden = new Point2D.Double();
        piramidenboden.setLocation(pkt.getX()+höhe*w_2D.getX(), pkt.getY()+höhe*w_2D.getY());        
        double[] w_ = new double[3]; w_[0] = w.getX(); w_[1] = w.getY(); w_[2] = w.getZ(); // gleich wie w, als Array
        Point2D[] quadrat = quadratimRaum(piramidenboden, 1.2 * höhe, w_);
        g.draw(new Line2D.Double(quadrat[0], quadrat[1]));
        g.draw(new Line2D.Double(quadrat[1], quadrat[2]));
        g.draw(new Line2D.Double(quadrat[2], quadrat[3]));
        g.draw(new Line2D.Double(quadrat[3], quadrat[0]));
        g.draw(new Line2D.Double(quadrat[0], pkt));
        g.draw(new Line2D.Double(quadrat[1], pkt));
        g.draw(new Line2D.Double(quadrat[2], pkt));
        g.draw(new Line2D.Double(quadrat[3], pkt));
        // Schinen
        Point2D schinenzentrum = new Point2D.Double();
        schinenzentrum.setLocation(pkt.getX()+1.2*höhe*w_2D.getX(), pkt.getY()+1.2*höhe*w_2D.getY());
        Point2D P1 = new Point2D.Double(); Point2D P2 = new Point2D.Double();
        if (Math.abs(länge*rtg_2D.getX()) > 2 || Math.abs(länge*rtg_2D.getY()) > 2) {
            g.setStroke(new BasicStroke(2.0f));
            P1.setLocation(schinenzentrum.getX() + spurweite/2d*u_2D.getX() + länge/2d*rtg_2D.getX(), 
                           schinenzentrum.getY() + spurweite/2d*u_2D.getY() + länge/2d*rtg_2D.getY()); 
            P2.setLocation(schinenzentrum.getX() + spurweite/2d*u_2D.getX() - länge/2d*rtg_2D.getX(), 
                           schinenzentrum.getY() + spurweite/2d*u_2D.getY() - länge/2d*rtg_2D.getY());
            g.draw(new Line2D.Double(P1, P2));
            P1.setLocation(schinenzentrum.getX() - spurweite/2d*u_2D.getX() + länge/2d*rtg_2D.getX(), 
                           schinenzentrum.getY() - spurweite/2d*u_2D.getY() + länge/2d*rtg_2D.getY()); 
            P2.setLocation(schinenzentrum.getX() - spurweite/2d*u_2D.getX() - länge/2d*rtg_2D.getX(), 
                           schinenzentrum.getY() - spurweite/2d*u_2D.getY() - länge/2d*rtg_2D.getY());
            g.draw(new Line2D.Double(P1, P2));  
            g.setStroke(new BasicStroke(1.0f));
        }
        else {
            P1.setLocation(schinenzentrum.getX() + spurweite/2d*u_2D.getX(), 
                           schinenzentrum.getY() + spurweite/2d*u_2D.getY()); 
            P2.setLocation(schinenzentrum.getX() - spurweite/2d*u_2D.getX(), 
                           schinenzentrum.getY() - spurweite/2d*u_2D.getY());
            g.fill(kreis(P1, 2d));
            g.fill(kreis(P2, 2d));
        }
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
                L = Math.sqrt(Math.pow(Kn[i].getLx(), 2) + Math.pow(Kn[i].getLy(), 2) + Math.pow(Kn[i].getLz(), 2));
                if (L > max) max = L;
            }
            
            if (MIT_Auflagerkräften) {
                R = Math.sqrt(Math.pow(Kn[i].getRx(), 2) + Math.pow(Kn[i].getRy(), 2) + Math.pow(Kn[i].getRz(), 2));
                if (R > max) max = R;
            }
        }
        return max;
    }
    
    private Point2D[] quadratimRaum(Point2D zentrum, double länge, double[] rtg) {
        // Zunächst wird (im 3D-Raum) ein Koordinatensystem auf der Normalenebene aufgespannt. 
        // (Aufgabe analog zum Koordsys auf der Proj.ebene in clKoord3D).
        clKoord3D tmp = new clKoord3D(rtg);
        double[] uw_3D = tmp.getProjKoordSys();
        Point3D u_3D = new Point3D(uw_3D[0],uw_3D[1], uw_3D[2]);
        Point3D w_3D = new Point3D(uw_3D[3],uw_3D[4], uw_3D[5]);
        // Das Koordsys im 3D-Raum wird projiziert.
        Point2D u = koord.projiziere(u_3D);
        Point2D w = koord.projiziere(w_3D);
        // Eckpunkte:
        Point2D[] quadrat = new Point2D[4];
        quadrat[0] = new Point2D.Double(zentrum.getX() + länge/2d*u.getX() + länge/2d*w.getX(),
                                        zentrum.getY() + länge/2d*u.getY() + länge/2d*w.getY());
        quadrat[1] = new Point2D.Double(zentrum.getX() + länge/2d*u.getX() - länge/2d*w.getX(),
                                        zentrum.getY() + länge/2d*u.getY() - länge/2d*w.getY());
        quadrat[2] = new Point2D.Double(zentrum.getX() - länge/2d*u.getX() - länge/2d*w.getX(),
                                        zentrum.getY() - länge/2d*u.getY() - länge/2d*w.getY());
        quadrat[3] = new Point2D.Double(zentrum.getX() - länge/2d*u.getX() + länge/2d*w.getX(),
                                        zentrum.getY() - länge/2d*u.getY() + länge/2d*w.getY());
        return quadrat;
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
