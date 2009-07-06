/*
 * clKoord.java
 *
 * Created on 7. September 2003, 10:20
 *
 * Fachwerk - treillis
 *
 * Copyright (c) 2003, 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                     <qwert2003@users.berlios.de>
 *
 * Das Programm könnte FEHLER enthalten. Sämtliche Resultate sind
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
 

package Fachwerk.gui;

import Fachwerk.statik.*;
import javax.swing.*;
import java.awt.geom.*;

/**
 *
 * Diese Klasse dient der Konvertierung von Fachwerkmodell- [m] Panelkoordinaten
 * und umgekehrt.
 *
 * @author  av
 */
public class clKoord implements Cloneable {
    
    boolean debug = false;
    
    private double x0, z0, lx, lz;    
    private double x0_pix, z0_pix, lx_pix, lz_pix;
    
    //private Point2D transformierterPkt;  // nur "Arbeitspunkt", um Speicher nicht jedesmal neu anzufordern.
    
    /** Creates a new instance of clKoord 
     *  Eingabe in Metern, d.h. Fachwerkkoordinaten.
     */
    public clKoord(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche) {   
                
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
    }
    
    /** Creates a new instance of clKoord 
     *  Eingabe in Metern, d.h. Fachwerkkoordinaten.
     */    
    public clKoord(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche, double rand) {   
                
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
    }
    
    /** Creates a new instance of clKoord 
     *  Eingabe in Metern, d.h. Fachwerkkoordinaten.
     *  massstabsetzen = true, prixprom: Pixel pro Meter
     */
    public clKoord(Point2D Pkt1_in_m, Point2D Pkt2_in_m, JPanel zeichenfläche, boolean massstabsetzen, double pixprom) { 
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
    }
    
    
    
    /** Gibt den Punkt in Fachwerkkoordinaten [m] zurück.
     *  Eingabe in Panelkoordinaten [pixel].
     */
    public Point2D m(Point2D Pkt_in_panel) {        
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
        double lm = lx * l_pix / lx_pix;
        return lm;
    }
    
    /** Gibt den Punkt in Panelkoordinaten [pixel] zurück.
     *  Eingabe in Fachwerkkoordinaten [m].
     */
    public Point2D panel(Point2D Pkt_in_m) {
        double x_pix = x0_pix;
        double z_pix = z0_pix;
        x_pix += lx_pix * (Pkt_in_m.getX() - x0) / lx;
        z_pix += lz_pix * (Pkt_in_m.getY() - z0) / lz;
                
        //transformierterPkt = new Point2D.Double();
        //transformierterPkt.setLocation(x_pix, z_pix);
        //return transformierterPkt;
        return new Point2D.Double(x_pix, z_pix);        
    }
    
    /** Eingabe: Meter
     *  Ausgabe: MPixel
     */
    public double panel(double l_m) {
        double lpix = l_m * lx_pix / lx;
        return lpix;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        } 
    }
}
