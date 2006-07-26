/*
 * clKoord.java
 *
 * Created on 7. September 2003, 10:20
 *
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
 

package Fachwerk3D.gui3D;

import Fachwerk.statik.Fkt;
import javax.swing.*;
import java.awt.geom.*;

/**
 *
 * Diese Klasse dient der Projizierung der Fachwerkkordinaten auf die Darstellungsebene
 * sowie der Konvertierung von Projektionsflächen- [m] in Panelkoordinaten und umgekehrt.
 *
 * @author  av
 */
public class clKoord3D implements Cloneable {
    
    boolean debug = false;
    boolean debug3D = false;
    
    private double x0, z0, lx, lz;    
    private double x0_pix, z0_pix, lx_pix, lz_pix;
    
    private double[] n; //normierte Normale zur Projektionsfläche
    private double[] u;
    private double[] w;
    
    //boolean ZOOMGESETZT = false;
    
    
    public clKoord3D(double[] Projektionsrtg_n) {
        n = Fkt.normiere(Projektionsrtg_n);
        
        if (debug3D) System.out.println("n: " + n[0] + " " + n[1] + " " + n[2]);
        
        if (n[0] == 0 && n[1] == 0 && n[2] == 0) {
            System.err.println("Programmfehler: Vektor der Länge Null nicht erlaubt!");
            throw new ArithmeticException("Vektor der Länge Null nicht erlaubt!");
        }
        if (Math.abs(n[0]*n[0] + n[1]*n[1] + n[2]*n[2] - 1d) > 1E-10) { // n muss normiert sein
            System.err.println("Programmfehler: Normale nicht normiert. LxL = " + (n[0]*n[0] + n[1]*n[1] + n[2]*n[2]));
            throw new ArithmeticException("Vektor nicht normiert.");
        }
        
        // Koordinatensystem auf der Projektionsfläche: u und w Einheitsvektoren
        // Pkt_projiziert = (0,0,0) + xsi*u + zeta*w 
        u = new double[3];
        w = new double[3];
       
        
        if (n[0] == 0 && n[1] == 0) {  // n parallel zur z-Rtg
            // u parallel zur x-Rtg (per Definition)
            u[0] = 1;
            u[1] = 0;
            u[2] = 0;
            
            // w = n x u (Kreuzprodukt)
            w[0] = 0;
            w[1] = n[2]; // nz = 1 oder -1
            w[2] = 0;
            assert Math.abs(w[0]*w[0] + w[1]*w[1] +w[2]*w[2] -1) < 1E-10; //"w nicht normiert";
        }
        else { // Normalfall
            // u = ez x n (Kreuzprodukt): u hat keine z Komponente (per Definition)
            u[0] = -n[1];
            u[1] = n[0];
            u[2] = 0;
            u = Fkt.normiere(u);
            assert Math.abs(u[0]*u[0] + u[1]*u[1] +u[2]*u[2] -1) < 1E-10; //"u nicht normiert";
            
            // w = n x u (Kreuzprodukt)
            w[0] = -n[0]*n[2];
            w[1] = -n[1]*n[2];
            w[2] = n[0]*n[0] + n[1]*n[1];
            w = Fkt.normiere(w);
            assert Math.abs(w[0]*w[0] + w[1]*w[1] +w[2]*w[2] -1) < 1E-10; //"w nicht normiert";
        }
        
    }
    
    /** Projiziert den Punkt 3D auf die Projektionsfläche. Diese Ebene geht durch (0,0,0) und hat die
        normierte Normale n.
     *  Auf dieser Ebene gibt es ein lokales Koordinatensystem.
     *  xsi (vektor u) hat keine Komponente in z-Rtg (per Definition). Ist die Projektionsebene vertikal, ist
     *  xsi (vektor u) gleich x
     *
     *  Sowohl die Eingabe wie die Ausgabe hat die Einheit Meter [m]
     */
    public Point2D projiziere(Point3D Pkt) { //, double[] n) {
        
        
        // Projektion:
        // P + a.n = 0 + xsi.u + zeta.w
        // resp. einfacher. xsi = p . u (Skalarprodukt), zeta = p . w (Skalarprodukt)
        double xsi = Pkt.getX() * u[0] + Pkt.getY() * u[1] + Pkt.getZ() * u[2];
        double zeta = Pkt.getX() * w[0] + Pkt.getY() * w[1] + Pkt.getZ() * w[2];
        
        return new Point2D.Double(xsi, zeta);
    }
    
    
    /** 
     *  Eingabe in Metern, d.h. auf der Projektionsfläche.
     */
    public void zoome(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche) {   
                
        // metrische Werte, d.h. Fachwerkkoordinaten
        double[] xwerte = {Pkt1_in_m.getX(), Pkt2_in_m.getX()};
        double[] zwerte = {Pkt1_in_m.getY(), Pkt2_in_m.getY()};
        x0 = Fkt.min(xwerte);
        z0 = Fkt.min(zwerte);
        lx = Fkt.max(xwerte) - x0;
        lz = Fkt.max(zwerte) - z0;
        
        if (debug) System.out.println("Pkt1: "+Pkt1_in_m.toString());
        if (debug) System.out.println("Pkt2: "+Pkt2_in_m.toString());
        //if (debug) System.out.println("x0 =" + Fkt.fix(x0,1) + "    lx =  " + Fkt.fix(lx,1));
        //if (debug) System.out.println("z0 =" + Fkt.fix(z0,1) + "    lz =  " + Fkt.fix(lz,1));
        
        // Pixelwerte, d.h. Koordinatensystem des Panels
        x0_pix = 0;
        z0_pix = 0;
        //x0_pix = zeichenfläche.getLocation().getX();
        //z0_pix = zeichenfläche.getLocation().getY();        
        lx_pix = (double) zeichenfläche.getWidth();
        lz_pix = (double) zeichenfläche.getHeight();
        //if (debug) System.out.println("lx_pix: " + lx_pix + "   lz_pix: " + lz_pix);
        
        // lx oder lz an die Form der Zeichenfläche anpassen, damit das Bild nicht verzogen wird
        boolean Xmgd; // X-Rtg massgebend, sonst Z-Rtg
        if ((lx / lx_pix) > (lz / lz_pix)) Xmgd = true;
        else Xmgd = false;
        
        if (Xmgd) {
            // x0 und lx bleiben unverändert
            double lzalt = lz;
            lz = lx * lz_pix / lx_pix;
            assert Math.abs(lx / lx_pix  -  lz / lz_pix) < 0.01;
            assert lz > lzalt;
            z0 -= (lz - lzalt) / 2d; // zentrieren in z-Rtg
        }
        
        else { // z-Rtg massgebend
            // z0 und lz bleiben unverändert
            double lxalt = lx;
            lx = lz * lx_pix / lz_pix;
            assert Math.abs(lx / lx_pix  -  lz / lz_pix) < 0.01;
            assert lx > lxalt;
            x0 -= (lx - lxalt) / 2d; // zentrieren in x-Rtg
        }
        
        if (debug) System.out.println("in Meter: x0=" + Fkt.fix(x0,1) + " lx="+Fkt.fix(lx,1)
         + " z0="+Fkt.fix(z0,1) + " lz="+Fkt.fix(lz,1));
        if (debug) System.out.println("in Pixel: x0=" + Fkt.fix(x0_pix,1) + " lx="+Fkt.fix(lx_pix,1)
         + " z0="+Fkt.fix(z0_pix,1) + " lz="+Fkt.fix(lz_pix,1));
        if (debug) System.out.println("x-Rtg ist massgebend: " + Xmgd);
        
        //ZOOMGESETZT = true;
    }
    
    /**  
     *  Eingabe in Metern, d.h. auf der Projektionsfläche.
     */    
    public void zoome(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche, double rand) {   
                
        // metrische Werte, d.h. Fachwerkkoordinaten
        double[] xwerte = {Pkt1_in_m.getX(), Pkt2_in_m.getX()};
        double[] zwerte = {Pkt1_in_m.getY(), Pkt2_in_m.getY()};
        x0 = Fkt.min(xwerte);
        z0 = Fkt.min(zwerte);
        lx = Fkt.max(xwerte) - x0;
        lz = Fkt.max(zwerte) - z0;
                
        // Pixelwerte, d.h. Koordinatensystem des Panels
        x0_pix = rand;
        z0_pix = rand;             
        lx_pix = (double) zeichenfläche.getWidth()  - x0_pix - rand;
        lz_pix = (double) zeichenfläche.getHeight() - z0_pix - rand;        
        
        // lx oder lz an die Form der Zeichenfläche anpassen, damit das Bild nicht verzogen wird
        boolean Xmgd; // X-Rtg massgebend, sonst Z-Rtg
        if ((lx / lx_pix) > (lz / lz_pix)) Xmgd = true;
        else Xmgd = false;
        
        if (Xmgd) {
            // x0 und lx bleiben unverändert
            double lzalt = lz;
            lz = lx * lz_pix / lx_pix;
            assert Math.abs(lx / lx_pix  -  lz / lz_pix) < 0.01;
            assert lz > lzalt;
            z0 -= (lz - lzalt) / 2d; // zentrieren in z-Rtg
        }
        
        else { // z-Rtg massgebend
            // z0 und lz bleiben unverändert
            double lxalt = lx;
            lx = lz * lx_pix / lz_pix;
            assert Math.abs(lx / lx_pix  -  lz / lz_pix) < 0.01;
            assert lx > lxalt;
            x0 -= (lx - lxalt) / 2d; // zentrieren in x-Rtg
        }
        
        //ZOOMGESETZT = true;
    }
    
    /** 
     *  Eingabe in Metern, d.h. auf der Projektionsfläche.
     *  massstabsetzen = true, prixprom: Pixel pro Meter
     */
    public void zoome(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche, boolean massstabsetzen, double pixprom) { 
        assert massstabsetzen; // nur hier, um anderen Konstruktor zu haben als jener mit Rand
        // metrische Werte, d.h. Fachwerkkoordinaten
        double[] xwerte = {Pkt1_in_m.getX(), Pkt2_in_m.getX()};
        double[] zwerte = {Pkt1_in_m.getY(), Pkt2_in_m.getY()};
        x0 = Fkt.min(xwerte);
        z0 = Fkt.min(zwerte);
        lx = Fkt.max(xwerte) - x0;
        lz = Fkt.max(zwerte) - z0;
                
        // Pixelwerte, d.h. Koordinatensystem des Panels
        lx_pix = lx * pixprom;
        lz_pix = lz * pixprom;        
        x0_pix = (double) (zeichenfläche.getWidth()  - lx_pix) / 2d;
        z0_pix = (double) (zeichenfläche.getHeight() - lz_pix) / 2d;
        if (x0_pix < 0 || z0_pix < 0) System.out.println("Zeichnung groesser als der Papierbereich!");
        
        //ZOOMGESETZT = true;
    }
    
    
    
    /** Gibt den Punkt in Meter [m] auf der Projektionsfläche zurück.
     *  Eingabe in Panelkoordinaten [pixel].
     */
    public Point2D m(Point2D Pkt_in_panel) {        
        //assert ZOOMGESETZT;
        double x_in_m = x0;
        double z_in_m = z0;
        x_in_m += lx * (Pkt_in_panel.getX() - x0_pix) / lx_pix;
        z_in_m += lz * (Pkt_in_panel.getY() - z0_pix) / lz_pix;
        
        //transformierterPkt = new Point2D.Double();
        //transformierterPkt.setLocation(x_in_m, z_in_m);
        //return transformierterPkt;
        return new Point2D.Double(x_in_m, z_in_m);
    }
   
    
    /** Eingabe: Pixel
     *  Ausgabe: Meter
     */
    public double m(double l_pix) {
        //assert ZOOMGESETZT;
        double lm = lx * l_pix / lx_pix;
        return lm;
    }
    
    /** Gibt den Punkt in Panelkoordinaten [pixel] zurück.
     *  Eingabe in auf die Ebene projizierte Fachwerkkoordinaten [m].
     */
    public Point2D panel(Point2D projPkt_in_m) {
        //assert ZOOMGESETZT;
        double x_pix = x0_pix;
        double z_pix = z0_pix;
        x_pix += lx_pix * (projPkt_in_m.getX() - x0) / lx;
        z_pix += lz_pix * (projPkt_in_m.getY() - z0) / lz;
                
        //transformierterPkt = new Point2D.Double();
        //transformierterPkt.setLocation(x_pix, z_pix);
        //return transformierterPkt;
        return new Point2D.Double(x_pix, z_pix);        
    }
    
    
    public Point2D panel(Point3D FwkPkt_in_m) {
        //assert ZOOMGESETZT;
        
        return panel(projiziere(FwkPkt_in_m));
    }
    
    
    /** Eingabe: Meter auf der Projektionsfläche
     *  Ausgabe: MPixel
     */
    public double panel(double l_m) {
        //assert ZOOMGESETZT;
        double lpix = l_m * lx_pix / lx;
        return lpix;
    }
    
    
    /* nach Fachwerk.statik.Fkt verlegt
    private double[] normiere(double[] vektor) {
        assert vektor.length > 0;
        double nenner = 0;
        for (int i = 0; i < vektor.length; i++) {
            nenner += vektor[i]*vektor[i];
        }
        nenner = Math.sqrt(nenner);
        
        double[] normiert = new double[vektor.length];
        for (int i = 0; i < vektor.length; i++) {
            normiert[i] = vektor[i] / nenner;
        }
        return normiert;
    }
    */
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        } 
    }
    
    public double[] getProjKoordSys() {
        double[] ret = new double[6];
        ret[0] = u[0]; ret[1] = u[1]; ret[2] = u[2]; 
        ret[3] = w[0]; ret[4] = w[1]; ret[5] = w[2];
        return ret;
    }
    
    /** Zu Testzwecken
     */
    public static void main(String[] args) {
        double n[] = new double[3];
        double P[] = new double[3];
        
        java.io.BufferedReader eingabe = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String eingabeZeile;        
        try {
            // n einlesen
            System.out.println("Sichtrichtung n angeben");
            System.out.print("nx ny nz   ");
            eingabeZeile = eingabe.readLine();
            java.util.StringTokenizer tokens = new java.util.StringTokenizer(eingabeZeile);
            int anz = tokens.countTokens();
            if (anz != 3) System.exit(0);
            for (int i = 0; i < anz; i++) {
                n[i] = Double.parseDouble(tokens.nextToken());
            }
            clKoord3D koord = new clKoord3D(n);
            double[] uw = koord.getProjKoordSys();
            System.out.println("u = " + Fkt.nf(uw[0],2) + ", " + Fkt.nf(uw[1],2) + ", " + Fkt.nf(uw[2],2));
            System.out.println("w = " + Fkt.nf(uw[3],2) + ", " + Fkt.nf(uw[4],2) + ", " + Fkt.nf(uw[5],2));
            
            // Pkte einlesen
            while (true) {
                System.out.println("");
                System.out.println("Punkt im Raum angeben");
                System.out.print("Px Py Pz   ");
                eingabeZeile = eingabe.readLine();
                tokens = new java.util.StringTokenizer(eingabeZeile);
                anz = tokens.countTokens();
                if (anz != 3) System.exit(0);
                for (int i = 0; i < anz; i++) {
                    P[i] = Double.parseDouble(tokens.nextToken());
                }
                Point3D Pkt = new Point3D(P[0], P[1], P[2]);
                Point2D proj = koord.projiziere(Pkt);
                System.out.println("projiziert: xsi = " + Fkt.nf(proj.getX(),2) + " zeta = " + Fkt.nf(proj.getY(),2));
            }
            
        }
        catch(java.io.IOException e) {
            System.err.println("Fehler bei der Eingabe.");
        }
        
    }
}
