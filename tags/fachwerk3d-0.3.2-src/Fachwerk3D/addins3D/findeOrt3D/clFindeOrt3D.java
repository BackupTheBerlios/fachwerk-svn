/*
 * clFindeOrt.java
 *
 * Created on 19. Januar 2004, 20:36
 */

package Fachwerk3D.addins3D.findeOrt3D;

import Fachwerk3D.statik3D.clKnoten3D;
import Fachwerk3D.statik3D.inKonstante3D;
import Fachwerk3D.statik3D.clFachwerk3D;
import Fachwerk.statik.clStab;
import Fachwerk.statik.Fkt;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2004 - 2007 A.Vontobel <qwert2003@users.sourceforge.net>
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
 * Dieses Programm ist freie Software. Sie können es unter den Bedingungen
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
 *
 * @author  A.Vontobel
 */
public class clFindeOrt3D implements inKonstante3D {
    
    boolean verbose = true;
    private final double TOL = TOL_finde;
    private final double TOL_resultat = TOL_vorberechnung;
    int maxdurchläufe = 1000;
    
    // Eingabevariablen
    private clKnoten3D Knoten; // zu verschiebender Knoten
    private clStab Stab; // Stab mit gesuchter Kraft
    private double[] vektor;
    private double Fziel;
    
    // eigentliche Fachwerkdaten
    private clKnoten3D[] Kn; // Knotenarray
    private clStab[] St;   // Stabarray
    private int[][] Top;   // Topologie;
    
    // Zustandsinfos
    private boolean FEHLER = false;       // Fehler während der Berechnung
    private boolean WIDERSPRUCH = false;  // Widerspruch während der Berechnung
    private boolean UNBESTIMMT = false;   // Stab ist unbestimmt!
    private boolean UNABHÄNGIG = false;   // Stab ist unabhängig! d.h. Kraft ändert sich durch Schieben des Knotens nicht.
    private boolean OK = false; // gewünschte Genauigkeit der Stabkraft erreicht
    private double abweichung;
   
    
    // interne Variablen
    private double[] ursprKoord = new double[3];
    private double[] kn1 = new double[2]; // s, F: Koordinaten {x,y,z} = ursprKoord + s * vektor
    private double[] kn2 = new double[2];
    boolean OptionVorber;
    boolean OptionGLS;
    
    /** Creates a new instance of clFindeOrt */
    public clFindeOrt3D(clKnoten3D zuverschKnoten, double[] vektor, clStab Stab, double Wunschkraft, 
                      clKnoten3D[] Knotenarray, clStab[] Stabarray, int[][] Topologie,
                      boolean OptionVorber, boolean OptionGLS) {
        this.Knoten = zuverschKnoten;
        this.vektor = vektor;
        this.Stab = Stab;
        this.Fziel = Wunschkraft;
        this.Kn = Knotenarray;
        this.St = Stabarray;
        this.Top = Topologie;
        this.OptionVorber = OptionVorber; this.OptionGLS = OptionGLS;
        
        // Ausgangskoordinaten merken
        ursprKoord[0] = Knoten.getX();
        ursprKoord[1] = Knoten.getY();
        ursprKoord[2] = Knoten.getZ();
    }
    
    
    /**
     * Startet die Iteration, d.h. sucht die gewünschte Knotenposition
     * @return true wenn keine Probleme aufgetaucht sind.
     */
    public boolean start() {
        boolean berOK = true;
        boolean unverändert = false;
        
        // Kontrollen
        //berechnen
        berOK = berechnen();
        if (!berOK) return false;
        
        //falls Stab unbest --> Abbruch
        if (Stab.getStatus() == UNBEST) {
            UNBESTIMMT = true;
            System.out.println("Stab unbestimmt! --> Iteration unmoeglich");
            return false;
        }
        
        // Startknoten (ursprünglicher Knoten)
        kn2[0] = 0;
        kn2[1] = Stab.getKraft();
        if (verbose) System.out.println("Startknoten x=" + Fkt.nf(ursprKoord[0], 3) + " y=" + Fkt.nf(ursprKoord[1], 3) + " z=" + Fkt.nf(ursprKoord[2], 3) + ":");
        if (verbose) System.out.println("Vektor dx=" + Fkt.nf(vektor[0], 3) + " dy=" + Fkt.nf(vektor[1], 3) + " dz=" + Fkt.nf(vektor[2], 3) + ":");
        if (verbose) System.out.println("s = " + Fkt.nf(kn2[0], 4, 2) + " N =" + Fkt.nf(kn2[1],2,4));
        
        // kontrollieren, ob der Knoten in der ursprünglichen Position die Bedingung bereits erfüllt.
        abweichung = Math.abs(kn2[1] - Fziel);
        if (abweichung < TOL) {
            OK = true;
            System.out.println("Bereits die urspruengliche Knotenposition erfuellt die gestellte Bedingung.");
            return true;
        }
        
        // Nullknoten berechnen
        kn1[0] = 1; // urspKoord + 1 * vektor
        Knoten.setNeueKoord(ursprKoord[0] + kn1[0] * vektor[0],
                            ursprKoord[1] + kn1[0] * vektor[1], 
                            ursprKoord[2] + kn1[0] * vektor[2]);
        berOK = berechnen();
        if (!berOK) return false;
        kn1[1] = Stab.getKraft();
        if (verbose) System.out.println("s = " + Fkt.nf(kn1[0], 4, 2) + " N =" + Fkt.nf(kn1[1],2,4));
        if (Math.abs(kn2[1] - kn1[1]) < TOL) {
            //unverändert = true;
            UNABHÄNGIG = true;
            return false;
        }
        
        // Iteration beginnen
        double s_neu;
        int zähler = 1;
        
        while (zähler < maxdurchläufe && !unverändert && Math.abs(kn2[1] - Fziel) > TOL) {
            zähler ++;
            s_neu = Newton(kn1[0], kn1[1], kn2[0], kn2[1], Fziel);
            kn1[0] = kn2[0]; kn1[1] = kn2[1];
            kn2[0] = s_neu;
            Knoten.setNeueKoord(ursprKoord[0] + kn2[0] * vektor[0],  
                                ursprKoord[1] + kn2[0] * vektor[1],
                                ursprKoord[2] + kn2[0] * vektor[2]);
            berOK = berechnen();
            if (!berOK) return false;
            if (Stab.getStatus() == UNBEST) {
                System.out.println("Stab ploetzlich unbestimmt! --> Iteration abgebrochen");
                return false;
            }
            kn2[1] = Stab.getKraft();
            if (Math.abs(kn2[1] - kn1[1]) < TOL) unverändert = true;
            if (verbose) System.out.println(Fkt.nf(zähler,4) + " s = " + Fkt.nf(kn2[0], 4, 2) + " N =" + Fkt.nf(kn2[1],2,4));
        }
        System.out.println("Neue Position: " + Fkt.nf(Knoten.getX(), 12) + ", " + Fkt.nf(Knoten.getY(), 12) + ", " + Fkt.nf(Knoten.getZ(), 12));
        System.out.println("Neue Kraft im Stab: " + Fkt.nf(Stab.getKraft(),3) + "kN (Ziel: " + Fkt.nf(Fziel,3) + "kN)");
        abweichung = Math.abs(kn2[1] - Fziel);
        if (abweichung < TOL_resultat) {
            OK = true;
            System.out.println("Genauigkeit erreicht!");
            return true;
        }
        else {
            OK = false;
            System.out.println("Abweichung: " + abweichung + " kN");
            return false;
        }
        
    }
    
    private double Newton(double s1, double F1, double s2, double F2, double F_Ziel) throws ArithmeticException {
        if (F1 == F2) throw new ArithmeticException();
        double s3 = s2 + (F_Ziel-F2) * (s1-s2)/(F1-F2);
        return s3;
    }
    
    private boolean berechnen() {
        boolean keinFEHLER = true;
        boolean keinWIDERSPRUCH = true;
        
        zurücksetzen();
        try {
            clFachwerk3D fachwerk = new clFachwerk3D(Kn, St, Top);
            keinWIDERSPRUCH = fachwerk.rechnen(OptionVorber,OptionGLS, false);
        }
        catch (Exception e) {
            System.err.println(e.toString());
            keinFEHLER = false;
        }
        
        if (keinWIDERSPRUCH && keinFEHLER) {
            return true;
        }
        else {
            if (!keinWIDERSPRUCH) System.out.println("WIDERSPRUCH!");
            if (!keinFEHLER) System.out.println("FEHLER!");
            return false;
        }
        
    }
    
    /** Setzt die Berechnung zurück. Alle Stabkräfte sind danach unbestimmt, die Knoten offen.
     */
    private void zurücksetzen() {
        for (int i = 1; i < Kn.length; i++) {
            Kn[i].zurücksetzen();
        }
        for (int i = 1; i < St.length; i++) {
            St[i].zurücksetzen(false);
        }
    }
}
