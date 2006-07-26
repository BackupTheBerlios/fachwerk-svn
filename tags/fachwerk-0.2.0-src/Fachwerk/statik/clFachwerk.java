/*
 * clFachwerk.java
 *
 * Created on 18. August 2003, 21:43
 */

package Fachwerk.statik;

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
public class clFachwerk implements inKonstante {
    
    /* Die folgenden Konstanzen sind im Interface inKonstante deklariert.
    // Lagerattribute
    private static final int LOS = 0;
    private static final int FIX = 2;
    private static final int VERSCHIEBLICH = 1;
    // Stabattribute
    private static final int UNBEST = 0;
    private static final int BER = 1;
    private static final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    private static final int NICHTSETZBAR = 4;
    // Knotenattribute
    private static final int OFFEN = 0;
    private static final int FERTIG = 1;
    private static final int WIDERSPRUCH = 3;
     */
    
    // Datensatz:
    private int anzKn;
    private int anzSt;
    
    private clKnoten[] Kn;
    private clStab[] St;
    
    private int[][] Top; // Topologie: Top[2][5] = 4 bedeutet, dass zwischen den Knoten 2 umd 5 der Stab Nummer 4 ist.
                         //            Top[2][6] = 0 bedeutet, dass es dazwischen keinen Stab gibt.
                         // Top ist eine symmetrische Matrix : Top[2][5] = Top[5][2]
                         // (Speicherverschleiss, dafür Fehler vermeidend)
                         // Index 0 gibt es nicht, da kein Knoten 0 existiert!
    
    // berechnete Datensätze
    private double[][] ax, az; // x und z Komponenten der Stäbe. [von Knoten][bis Knoten]
    
    // Programmtechnische Variablen
    //private int anzFG; // verbleibende Freiheitsgrade (unbestimmte Stabkräfte)
    
    
    private final double TOL = TOL_vorberechnung;
    //private final double TOL = 1E-11; // um Gleichheit von Stabkräften zu erkennen (in rVorberechnung)
    private final double TOLresultatcheck = TOL_resultatcheck;
    //private final double TOLresultatcheck = 1E-10; // dito, jedoch lascherer Wert, zB. TOL des GLS-Solvers
    
    private boolean WIDERSPRUCHaufgetreten = false;
    private int statischeUnbestimmtheit = Integer.MIN_VALUE; // zum Erkennen ob berechnet.
    private double[][] mechanismusRelKnVersch;
    
    private boolean verbose = false;
    private boolean debug = false;
    
    
    /** Creates a new instance of clFachwerk */
    public clFachwerk(clKnoten[] p_kn, clStab[] p_st, int[][] p_Top) {
        anzKn = p_kn.length - 1; // Knoten 0 existiert nicht
        anzSt = p_st.length - 1; // Stab 0 existiert nicht
        
        System.out.println("");
        System.out.println("--------------");
        System.out.println("Fachwerkmodell mit " + anzSt + " Staeben und " + anzKn + " Knoten.");
        
        if (p_Top.length != p_kn.length) System.err.println("Programmfehler: clFachwerk.initalisieren: Topologie falsch");
        Kn = p_kn;
        St = p_st;
        Top = p_Top;
    }
    
    /** Gibt ausgiebig Informationen auf der Konsole aus. */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /**
     *
     * zu Testzwecken
     */
    public static void main(String[] args) throws Exception {
        clKnoten[] testKn;
        clStab[] testSt;
        int [][] testTop;
        
        int bsp = 0;
        if (args.length > 0) bsp = Integer.parseInt(args[0]);
        switch (bsp) {
            case 1:
                testKn = new clKnoten[8];
                testSt = new clStab[12];
                testTop = new int[8][8];
                
                testKn[1] = new clKnoten(0d,6d);
                testKn[2] = new clKnoten(2d,2d);
                testKn[3] = new clKnoten(3d,5d);
                testKn[4] = new clKnoten(6d,1d);
                testKn[5] = new clKnoten(6d,5d);
                testKn[6] = new clKnoten(9d,6d);
                testKn[7] = new clKnoten(11d,0d);
                
                for (int i = 1 ; i <= 11; i++) {
                    testSt[i] = new clStab();
                }
                
                // Topologie schreiben
                testTop[1][2] = 1;
                testTop[1][3] = 2;
                testTop[2][3] = 5;
                testTop[2][4] = 3;
                testTop[2][5] = 4;
                testTop[4][3] = 6;
                testTop[5][3] = 7;
                testTop[5][4] = 10;
                testTop[5][6] = 11;
                testTop[4][6] = 9;
                testTop[7][4] = 8;
                
                
                // Lasten und Auflagerkräfte zuordnen
                testKn[6].setLager(FIX);
                testKn[7].setLager(FIX);
                testKn[1].setLast(0,100d);
                
                //testKn[4].setLager(VERSCHIEBLICH,0); //
                
                // zB eine Stabkraft zuweisen
                testSt[4].setKraft(GESETZT, 100);
                
                //testSt[6].setKraft(GESETZT, 10);  //
                //testSt[8].setKraft(GESETZT, 2); // WIDERSPRUCH!!
                
                break;
                
            case 0:
            default:
                testKn = new clKnoten[8];
                testSt = new clStab[12];
                testTop = new int[8][8];
                
                testKn[1] = new clKnoten(0,6);
                testKn[2] = new clKnoten(4,2);
                testKn[3] = new clKnoten(4,6);
                testKn[4] = new clKnoten(9,2);
                testKn[5] = new clKnoten(9,6);
                testKn[6] = new clKnoten(13,2);
                testKn[7] = new clKnoten(13,6);
                
                for (int i = 1 ; i <= 11; i++) {
                    testSt[i] = new clStab();
                }
                
                
                // Topologie schreiben
                testTop[1][2] = 1;
                testTop[1][3] = 2;
                testTop[2][3] = 3;
                testTop[2][4] = 4;
                testTop[2][5] = 5;
                testTop[4][3] = 6;
                testTop[5][3] = 7;
                testTop[5][4] = 8;
                testTop[5][7] = 11;
                testTop[4][6] = 9;
                testTop[7][4] = 10;
                
                
                // Lasten und Auflagerkräfte zuordnen
                testKn[6].setLager(FIX);
                testKn[7].setLager(FIX);
                testKn[1].setLast(0,100d);
                
                // zB eine Stabkraft zuweisen
                testSt[5].setKraft(GESETZT, -32.01562119);
        }
        
        
        clFachwerk fw = new clFachwerk(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    /** Berechnet das Fachwerkmodell
     * @return false: Widerspruch aufgetreten. true: Berechnung durchgeführt ohne Widerspruch.
     */
    public boolean rechnen(boolean optionVORBER, boolean optionGLS, boolean optionMECHANISMUS) throws Exception {
        
        if (check() == false) {
            System.err.println("Fehler beim check: clFachwerk.rechnen");
            return false;
        }
        
        rGeometrie();
        
        
        if (optionVORBER) rVorberechnung();
        
        try {
            if (optionGLS && !WIDERSPRUCHaufgetreten) rGleichgewichtsGLS();
        }
        catch (ArithmeticException wid_e) {
            WIDERSPRUCHaufgetreten = true;
            if (wid_e.getMessage() != null) System.out.println(wid_e.getMessage());
            else System.out.println(wid_e.toString());
        }
        
        rKnotenstatusSetzen();
        
        boolean OKkomplett = false;
        if (resultatcheck()) { // falls keine Widerspruch entdeckt wird:
            OKkomplett = istvollständiggelöst(false); // false, da resultatcheck() soeben durchgeführt
        }
        else verbose = true; // gibt mehr Infos aus, wenn resultatausgabe_direkt() aufgerufen wird.
        
        if (optionMECHANISMUS && !OKkomplett) {
            
            try {
                WIDERSPRUCHaufgetreten = rMechanismusVerletztGlgew();
            }
            catch (Exception e) {
                // wenn WIDERSPRUCHaufgetreten (in vorheriger statischer Berechnung), 
                // soll dies angezeigt werden (statt FEHLER), auch wenn die Mechanismusber fehlgeschlagen ist.
                if (!WIDERSPRUCHaufgetreten) throw e;
            }
        }
        
        return !WIDERSPRUCHaufgetreten;
    }
    
    
    boolean check() {  // Datentests, Topologiematrix spiegeln
        boolean FEHLER = false;
        
        // Topologie:
        // Matrix muss quadratisch sein
        for (int kn = 1; kn < Top.length; kn++) {
            if (Top[kn].length != Top.length) {
                FEHLER = true;
                System.err.println("Programmfehler: die Topologiematrix muss quadratisch sein");
            }
        }
        // kein Knoten darf mit sich selbst verbunden sein
        for (int kn = 1; kn < Top.length; kn++) {
            if (Top[kn][kn] > 0) {
                FEHLER = true;
                System.err.println("der Knoten " + kn + " ist mit sich selbst verbunden!");
            }
        }
        // Knoten null existiert nicht
        for (int kn = 0; kn < Top.length; kn++) {
            if (Top[kn][0] > 0) {
                FEHLER = true;
                System.err.println("Programmfehler: es existiert kein Knoten 0 (in Topologie gefunden)");
            }
        }
        for (int kn = 0; kn < Top.length; kn++) {
            if (Top[0][kn] > 0) {
                FEHLER = true;
                System.err.println("Programmfehler: es existiert kein Knoten 0 (in Topologie gefunden)");
            }
        }
        
        // Topologiematrix spiegeln (d.h. symmetrisch machen)
        for (int kni = 1; kni < Top.length; kni++) {
            for (int knk = 1; knk < Top.length; knk++) {
                if (Top[kni][knk] > 0) {
                    if (Top[knk][kni] == 0) Top[knk][kni] = Top[kni][knk];
                    else {
                        if (Top[knk][kni] != Top[kni][knk]) {
                            FEHLER = true;
                            System.err.println("Programmfehler: Topologiematrix muss symmetrisch sein");
                        }
                    }
                }
            }
        }
        
        // Kontrolle ob an jedem Knoten mind 2 Stäbe oder 1 Stab + Lager --> ERZEUGT JEDOCH KEINEN FEHLER
        for (int kni = 1; kni < Top.length; kni++) {
            int anzangeschlStäbe = 0;
            for (int knk = 1; knk < Top.length; knk++) {
                if (Top[kni][knk] > 0) {
                    anzangeschlStäbe ++;
                }
            }
            if (anzangeschlStäbe < 1) {
                System.out.println("WARNUNG: fragliche Eingabe: Der Knoten " + kni +" ist mit keinem Stab verbunden!");
            }
            if (anzangeschlStäbe == 1) {
                if (Kn[kni].getLagerbed() == LOS) {
                    System.out.println("WARNUNG: fragliche Eingabe: Der Knoten " + kni +" ist ev. ungenuegend gehalten!");
                }
            }
        }
        
        
        if (FEHLER) return false;
        else return true;
    }
    
    private void rGeometrie() { // x und z - Komponenten der Stabkräfte berechnen
        ax = new double[Kn.length][Kn.length];
        az = new double[Kn.length][Kn.length];
        for (int von = 1; von < Kn.length; von++) {
            for (int bis = 1; bis < Kn.length; bis++) {
                if (Top[von][bis] > 0) {
                    double dx = Kn[bis].getX() - Kn[von].getX();
                    double dz = Kn[bis].getZ() - Kn[von].getZ();
                    // debug
                    //System.out.println("von Kn "+von+" bis "+bis+" : dx = "+Fkt.nf(dx,2)+", dz = "+Fkt.nf(dz,2));
                    ax[von][bis] = dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d));
                    //System.out.println("von Kn "+von+" bis "+bis+" : ax = "+Fkt.nf(ax[von][bis],2));
                    az[von][bis] = dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d));
                    //System.out.println("von Kn "+von+" bis "+bis+" : az = "+Fkt.nf(az[von][bis],2));
                }
            }
        }
    }
    
    private void rVorberechnung() {     // Knoten durchlaufen
        // wenn bei einem Kn nur 2 FG:
        // Stäbe berechnen (Knotengleichgew, Matrix invertieren)
        // von Neuem beginnen, bis anzFG = konst
        if (verbose) System.out.print("Beginne die Vorberechnung...");
        
        boolean neu;
        
        grosseSchlaufe:
            do {
                // alle Knoten durchlaufen
                
                neu = false;
                for (int kni = 1; kni < Kn.length; kni++) {
                    int anzUnbekStäbe = 0;
                    int anzLagerkräfte = 0;
                    double resultX, resultZ, alpha;
                    int knk1, knk2;
                    double Sk[];
                    
                    for (int knk = 1; knk < Kn.length; knk++) {
                        if ((Top[kni][knk] > 0)
                        && (St[Top[kni][knk]].getStatus() == UNBEST) ) {
                            anzUnbekStäbe ++;
                        }
                    }
                    if (Kn[kni].getLagerbed() == FIX) anzLagerkräfte = 2;
                    if (Kn[kni].getLagerbed() == VERSCHIEBLICH) anzLagerkräfte = 1;
                    
                    // NOCH UNVOLLSTÄNDIG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    switch (anzUnbekStäbe) {
                        
                        // alle Stabkräfte bekannt
                        case 0: // alle Stabkräfte bekannt
                            
                            switch (anzLagerkräfte) {
                                case 2: // Lagerkräfte berechnen
                                    resultX = Kn[kni].getLx();
                                    resultZ = Kn[kni].getLz();
                                    
                                    for (int knk = 1; knk < Kn.length; knk++) {
                                        if (Top[kni][knk] > 0)  {
                                            if ((St[Top[kni][knk]].getStatus() == BER)
                                            || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                                resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                                resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                            }
                                            if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                                System.err.println("Programmfehler in Fachwerk.rVorberechnung");
                                            }
                                        }
                                    } // Schlaufenende
                                    
                                    Kn[kni].setLagerkraft(BER,-resultX,-resultZ);
                                    Kn[kni].setKnotenstatus(FERTIG);
                                    
                                    break;
                                case 1: // Lagerkraft berechnen + Knotenkontrolle nach Widerspruch
                                    if (Kn[kni].getLagerbed() != VERSCHIEBLICH) {
                                        System.err.println("Programmfehler in Fachwerk.rVorberechnung: verschiebliches Lager erwartet");
                                    }
                                    //  ENTHÄLT EV FEHLER!!!!!!! KONTROLLIEREN
                                /*
                                resultX = Kn[kni].getLx();
                                resultZ = Kn[kni].getLz();
                                 
                                for (int knk = 1; knk < Kn.length; knk++) {
                                    if (Top[kni][knk] > 0)  {
                                        if ((St[Top[kni][knk]].getStatus() == BER)
                                        || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                            resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                            resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                        }
                                        if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                            System.out.println("Programmfehler in Fachwerk.rVorberechnung");
                                        }
                                    }
                                } // Schlaufenende
                                 
                                 
                                alpha = Kn[kni].getRalpha();
                                // Kontrolle  // Kontrolle führt zu falschen Fehlermeldungen
                                double resultGleitrtg = -resultX * Math.sin(alpha) + resultZ * Math.cos(alpha);
                                if (Math.abs(resultGleitrtg) > TOL) {
                                    System.out.println("Knoten "+kni+": Widerspruch entdeckt: Resultierende in Gleitrtg: "+resultGleitrtg +" ungleich 0  (0 unbekSt,1 Lager)");
                                    Kn[kni].setKnotenstatus(WIDERSPRUCH);
                                    WIDERSPRUCHaufgetreten = true;
                                    break grosseSchlaufe;
                                }
                                 
                                // Lagerkraft berechnen
                                double R = - (resultX * Math.cos(alpha) + resultZ * Math.sin(alpha));
                                Kn[kni].setLagerkraft(BER,R * Math.cos(alpha), R * Math.sin(alpha));
                                Kn[kni].setKnotenstatus(FERTIG);
                                 */
                                    break;
                                default: // Knotenkontrolle nach Widerspruch
                                    resultX = Kn[kni].getLx();
                                    resultZ = Kn[kni].getLz();
                                    
                                    for (int knk = 1; knk < Kn.length; knk++) {
                                        if (Top[kni][knk] > 0)  {
                                            if ((St[Top[kni][knk]].getStatus() == BER)
                                            || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                                resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                                resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                            }
                                            if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                                System.err.println("Programmfehler in Fachwerk.rVorberechnung");
                                            }
                                        }
                                    } // Schlaufenende
                                    if (Math.abs(resultX) > TOL) {
                                        System.out.println("Knoten "+kni+": Widerspruch entdeckt: Resultierende X: "+resultX +" ungleich 0  (0 unbekSt,0 Lager)");
                                        Kn[kni].setKnotenstatus(WIDERSPRUCH);
                                        WIDERSPRUCHaufgetreten = true;
                                        break grosseSchlaufe;
                                    }
                                    if (Math.abs(resultZ) > TOL) {
                                        System.out.println("Knoten "+kni+": Widerspruch entdeckt: Resultierende Z: "+resultZ +" ungleich 0  (0 unbekSt,0 Lager)");
                                        Kn[kni].setKnotenstatus(WIDERSPRUCH);
                                        WIDERSPRUCHaufgetreten = true;
                                        break grosseSchlaufe;
                                    }
                                    if ((resultX < TOL)&&(resultZ < TOL)) Kn[kni].setKnotenstatus(FERTIG);
                                    
                            }
                            break;
                            
                            
                            // 1 Stab unbekannt
                        case 1: // unbekannte Stabkraft berechnen + Knotenkontrolle nach Widerspruch
                            switch (anzLagerkräfte) {
                                case 0: // fehlende Stabkraft berechnen + Knotenkontrolle nach Widerspruch
                                    resultX = Kn[kni].getLx();
                                    resultZ = Kn[kni].getLz();
                                    knk1 = 0; // Zielknoten des unbekannten Stabes
                                    for (int knk = 1; knk < Kn.length; knk++) {
                                        if (Top[kni][knk] > 0)  {
                                            if ((St[Top[kni][knk]].getStatus() == BER)
                                            || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                                resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                                resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                            }
                                            if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                                if (knk1 == 0) knk1 = knk;
                                                else System.err.println("Programmfehler in Fachwerk.rVorberechnung");
                                            }
                                        }
                                    } // Schlaufenende
                                    if (Math.abs(ax[kni][knk1]) > 0.707) { // unbekannte Stabkraft über Glgew in x-Rtg bestimmen
                                        St[Top[kni][knk1]].setKraft(BER,-resultX/ax[kni][knk1]);
                                        //Kontrolle
                                        if (Math.abs(resultZ+az[kni][knk1]*St[Top[kni][knk1]].getKraft()) > TOL) {
                                            System.out.println("Knoten "+kni+": Widerspruch entdeckt: Resultierende Z: "+resultZ +" ungleich 0  (1 unbekSt,0 Lager)");
                                            Kn[kni].setKnotenstatus(WIDERSPRUCH);
                                            WIDERSPRUCHaufgetreten = true;
                                            break grosseSchlaufe;
                                        }
                                        else Kn[kni].setKnotenstatus(FERTIG);
                                    }
                                    else { // unbekannte Stabkraft über Glgew in z-Rtg bestimmen
                                        St[Top[kni][knk1]].setKraft(BER,-resultZ/az[kni][knk1]);
                                        //Kontrolle
                                        if (Math.abs(resultX+ax[kni][knk1]*St[Top[kni][knk1]].getKraft()) > TOL) {
                                            System.out.println("Knoten "+kni+": Widerspruch entdeckt: Resultierende X: "+resultX +" ungleich 0  (1 unbekSt,0 Lager)");
                                            Kn[kni].setKnotenstatus(WIDERSPRUCH);
                                            WIDERSPRUCHaufgetreten = true;
                                            break grosseSchlaufe;
                                        }
                                        else Kn[kni].setKnotenstatus(FERTIG);
                                    }
                                    
                                    
                                    neu = true;
                                    break;
                                case 1: // NOCH OFFEN:  UND FEHLERBEHAFTET
                                    //fehlende Stabkraft + Lagerkraft berechnen
                                    if (Kn[kni].getLagerbed() != VERSCHIEBLICH) {
                                        System.err.println("Programmfehler in Fachwerk.rVorberechnung: verschiebliches Lager erwartet");
                                    }
                                    resultX = Kn[kni].getLx();
                                    resultZ = Kn[kni].getLz();
                                    knk1 = 0; // Zielknoten des unbekannten Stabes
                                    for (int knk = 1; knk < Kn.length; knk++) {
                                        if (Top[kni][knk] > 0)  {
                                            if ((St[Top[kni][knk]].getStatus() == BER)
                                            || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                                resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                                resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                            }
                                            if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                                if (knk1 == 0) knk1 = knk;
                                                else System.err.println("Programmfehler in Fachwerk.rVorberechnung");
                                            }
                                        }
                                    } // Schlaufenende
                                    alpha = Kn[kni].getRalpha(); // ev alpha verkehrt herum genommen!!!!!!!!
                                    
                                    if(Math.abs(ax[kni][knk1] * Math.cos(alpha) - az[kni][knk1] * Math.sin(alpha)) < TOL)
                                        break; // Stab senkrecht zu Gleitrichtung
                                    
                                /* ev verkehrt
                                // det-Kontrolle fehlt
                                Sk = Fkt.GLS2x2(ax[kni][knk1], Math.cos(alpha), -resultX,
                                                az[kni][knk1], Math.sin(alpha), -resultZ);
                                St[Top[kni][knk1]].setKraft(BER,Sk[0]);
                                Kn[kni].setLagerkraft(BER,Sk[1] * Math.cos(alpha), Sk[1] * Math.sin(alpha));
                                 **/
                                    // noch testen!!!!!!!!!!
                                /*
                                // det-Kontrolle fehlt
                                Sk = Fkt.GLS2x2(ax[kni][knk1], -Math.sin(alpha), -resultX,
                                                az[kni][knk1], Math.cos(alpha), -resultZ);
                                St[Top[kni][knk1]].setKraft(BER,Sk[0]);
                                Kn[kni].setLagerkraft(BER,Sk[1] * -Math.sin(alpha), Sk[1] * Math.cos(alpha));
                                 
                                Kn[kni].setKnotenstatus(FERTIG);
                                 
                                 
                                neu = true; //false; // sobald programmiert --> true
                                 */
                                    break;
                                default: // zu viele unbekannte: wird nicht in der Vorberechnung behandelt
                            }
                            break;
                            
                            
                            // 2 Stäbe unbekannt
                        case 2: // unbekannte Stabkräfte berechnen, falls Knoten nicht gelagert
                            if (anzLagerkräfte == 0) { // sonst zuviele Unbekannte
                                resultX = Kn[kni].getLx();
                                resultZ = Kn[kni].getLz();
                                knk1 = 0; // Zielknoten des ersten unbekannten Stabes
                                knk2 = 0; // Zielknoten des zweiten unbekannten Stabes
                                for (int knk = 1; knk < Kn.length; knk++) {
                                    if (Top[kni][knk] > 0)  {
                                        if ((St[Top[kni][knk]].getStatus() == BER)
                                        || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                                            resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                            resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                        }
                                        if (St[Top[kni][knk]].getStatus() == UNBEST) {
                                            if (knk1 == 0) knk1 = knk;
                                            else {
                                                if (knk2 == 0) knk2 = knk;
                                                else System.err.println("Programmfehler in Fachwerk.rVorberechnung");
                                            }
                                        }
                                    }
                                } // Schlaufenende
                                // Kontrolle, ob die beiden unbek Stäbe nicht auf einer Geraden liegen
                                if (Fkt.det2x2(ax[kni][knk1], ax[kni][knk2], az[kni][knk1], az[kni][knk2]) == 0d) break;
                                Sk = Fkt.GLS2x2(ax[kni][knk1], ax[kni][knk2], -resultX,
                                        az[kni][knk1], az[kni][knk2], -resultZ);
                                St[Top[kni][knk1]].setKraft(BER,Sk[0]);
                                St[Top[kni][knk2]].setKraft(BER,Sk[1]);
                                Kn[kni].setKnotenstatus(FERTIG);
                                neu = true;
                                
                                
                            } // else: zu viele unbekannte: wird nicht in der Vorberechnung behandelt
                            break;
                            
                            
                        default: // mehr als zwei Stäbe unbekannt: wird nicht in der Vorberechnung behandelt
                            
                    }
                }
            }
            while (neu && !WIDERSPRUCHaufgetreten);
            
            if (verbose) System.out.println("fertig.");
    }
    
    
    private void rGleichgewichtsGLS() throws Exception {
        // das GLS enthält als Unbekannte: (in dieser Reihenfolge)
        // - unbek Stabkräfte
        // - je 2 Lagerkräfte pro offenem Knoten (auch für verschiebliche Lager 2)
        
        // das GLS enthält folgende Gleichungen: (in dieser Reihenfolge)
        // - pro offenem Knoten 2 (in x- und z-Rtg)
        // - globales Gleichgewicht: 3 (x-Rtg, z-Rtg, Momentenbed. um (0,0)
        // - pro verschieblichem Lager: 1 (Verhältnis zw Rx und Rz)
        
        int anzUnbestSt = 0; // Startwerte
        int anzOffeneKn = 0;
        int anzOffeneGelagerteKn = 0;
        int anzOffeneVerschKn = 0;
        int anzUnbek;
        int anzGL;
        
        for (int st = 1; st < St.length; st++) {
            if (St[st].getStatus() == UNBEST) anzUnbestSt++;
        }
        for (int kn = 1; kn < Kn.length; kn++) {
            if (Kn[kn].istOffen()) {
                anzOffeneKn++;
                if (Kn[kn].getLagerbed() != LOS) {
                    assert (Kn[kn].getLagerstatus() != GESETZT) : "Setzen von Lagerkraeften nicht implementiert. Lagerstatus " + Kn[kn].getLagerstatus();
                    anzOffeneGelagerteKn++;
                    if (Kn[kn].getLagerbed() == VERSCHIEBLICH) {
                        anzOffeneVerschKn++;
                    }
                }
            }
        }
        
        anzUnbek = anzUnbestSt + 2 * anzOffeneGelagerteKn;
        anzGL = 2 * anzOffeneKn + 3 + anzOffeneVerschKn;
        
        if (true) { // if (verbose)
            System.out.println("Gleichungssystem:");
            System.out.println("Anzahl Unbekannte:  " + anzUnbek);
            System.out.println("Anzahl Gleichungen: " + anzGL + " (nicht alle unabhaengig voneinander)");
        }
        
        if (anzUnbek == 0) {
            System.out.println("Nichts zu lösen, alles bekannt");
            return; // nichts zu lösen, wenn alles bekannt.
        }
        
        // ----------------------------------------------------------------------------------
        // Indizes erstellen, um zB aus Stabnr auf UnbekNr schliessen zu können und umgekehrt
        // ----------------------------------------------------------------------------------
        
        int[] ausStab = new int[St.length]; // liefert den Index der Unbekannten (Stabkraft) aus der Stabnummer
        int[][] ausKnoten = new int[Kn.length][2]; // liefert den Index der Unbekannten aus der Knotennummer
                                                   // zweiter Index:   Wert = 0: Lagerkraft in x-Rtg, Wert = 1: z-Rtg:
        int[] stabnr = new int[anzUnbek]; // liefert die Stabnummer aus dem Index
        int[] knnr = new int[anzUnbek]; // liefert die Knotennummer aus dem Index
        
        int index = 0; // Index-Zähler für Unbekannte im GLS (d.h. UnbekNr) : Startwert
        
        for (int st = 1; st < St.length; st++) {
            if (St[st].getStatus() == UNBEST) {
                ausStab[st] = index;
                stabnr[index] = st;
                index++;
            }
            else {
                ausStab[st] = -1; // Stabkraft keine Unbekannte
            }
        }
        
        for (int kn = 1; kn < Kn.length; kn++) {
            if ( Kn[kn].istOffen() && (Kn[kn].getLagerbed() != LOS)) {
                // x-Rtg
                ausKnoten[kn][0] = index;
                knnr[index] = kn;
                index++;
                // z-Rtg
                ausKnoten[kn][1] = index;
                knnr[index] = kn;
                index++;
            }
            else {
                ausKnoten[kn][0] = -1; // sollte nicht abgefragt werden.
                ausKnoten[kn][1] = -1;
            }
        }
        assert (index == anzUnbek) : index + " ungleich " + anzUnbek;
        if (index != anzUnbek) {
            System.err.println("Fehler (Anz. Unbek): " + index + " ungleich " + anzUnbek);
            System.err.println("WARNUNG: Programm nicht unterbrochen. Vorsicht mit Resultaten.");
//            WIDERSPRUCHaufgetreten = true; // TODO, ev nicht Widerspruch sonder Fehler, falls dies auftritt: nachgehen
//            throw new IllegalArgumentException("Die Anzahl Unbekannter stimmt nicht. Ev. Programmfehler."); // TODO
        }
        
        
        // ---------------------------
        // Gleichungssystem aufstellen
        // ---------------------------
        
        double[][] GLS;
        try { // TODO wozu dieser try-Block?
            GLS = new double[anzGL][anzUnbek+1];
        }
        catch (Exception e) {
            WIDERSPRUCHaufgetreten = true;
            throw e;
        }
        
        int gl = 0; // Zähler für Gleichungsnummer: Startwert
        
        // offene Knoten:
        for (int kni = 1; kni < Kn.length; kni++) {
            if (Kn[kni].istOffen()) {
                
                // äussere Lasten
                double resultX = Kn[kni].getLx();
                double resultZ = Kn[kni].getLz();
                
                // Auflagerkräfte
                if (Kn[kni].getLagerbed() != LOS) {
                    switch (Kn[kni].getLagerstatus()) {
                        case UNBEST:
                            GLS[gl][ausKnoten[kni][0]] = 1; // x-Rtg
                            GLS[gl+1][ausKnoten[kni][1]] = 1; // z-Rtg
                            break;
                        case GESETZT: // noch nicht implementiert, hier jedoch schon berücksichtigt
                        case BER: // kann zur Zeit noch nicht auftreten, weil Knoten nicht mehr Offen wäre
                            resultX += Kn[kni].getRx();
                            resultZ += Kn[kni].getRz();
                            break;
                        default:
                            assert false : "Knoten " + kni + ": Lagerstatus: " + Kn[kni].getLagerstatus();
                    }
                }
                
                // Stäbe
                for (int knk = 1; knk < Kn.length; knk++) {
                    if (Top[kni][knk] > 0)  {  // sonst kein Stab vorhanden
                        switch (St[Top[kni][knk]].getStatus()) {
                            case UNBEST:
                                GLS[gl][ausStab[Top[kni][knk]]] = ax[kni][knk]; // x-Rtg
                                GLS[gl+1][ausStab[Top[kni][knk]]] = az[kni][knk]; // z-Rtg
                                break;
                            case GESETZT:
                            case BER:
                                resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                                resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                                break;
                            default:
                                assert false : "Stab " + Top[kni][knk] + ": Status: " + St[Top[kni][knk]].getStatus();
                        }
                    }
                } // Ende Zielknoten-Schlaufe
                
                GLS[gl][anzUnbek] = resultX;
                gl++;
                GLS[gl][anzUnbek] = resultZ;
                gl++;
            }
        }
        
        // globales Gleichgewicht
        double resultX = 0;
        double resultZ = 0;
        double resultM = 0; // P.S.: Mi = -Fx * z + Fz * x
        
        for (int kni = 1; kni < Kn.length; kni++) {
            
            // äussere Lasten
            resultX += Kn[kni].getLx();
            resultZ += Kn[kni].getLz();
            resultM += - Kn[kni].getLx() * Kn[kni].getZ() + Kn[kni].getLz() * Kn[kni].getX();
            
            // Auflagerkräfte
            if (Kn[kni].getLagerbed() != LOS) {
                switch (Kn[kni].getLagerstatus()) {
                    case UNBEST:
                        GLS[gl][ausKnoten[kni][0]] = 1; // Auflagerkraft in x-Rtg
                        GLS[gl+1][ausKnoten[kni][1]] = 1; // Auflagerkraft in z-Rtg
                        GLS[gl+2][ausKnoten[kni][0]] = - Kn[kni].getZ(); // Auflagerkraft in x-Rtg für M-Bed
                        GLS[gl+2][ausKnoten[kni][1]] = Kn[kni].getX(); // Auflagerkraft in z-Rtg für M-Bed
                        break;
                    case GESETZT: // noch nicht implementiert, hier jedoch schon berücksichtigt
                    case BER:
                        resultX += Kn[kni].getRx();
                        resultZ += Kn[kni].getRz();
                        resultM += - Kn[kni].getRx() * Kn[kni].getZ() + Kn[kni].getRz() * Kn[kni].getX();
                        break;
                    default:
                        assert false : "Knoten " + kni + ": Lagerstatus: " + Kn[kni].getLagerstatus();
                }
            }
        }
        GLS[gl][anzUnbek] = resultX;
        gl++;
        GLS[gl][anzUnbek] = resultZ;
        gl++;
        GLS[gl][anzUnbek] = resultM;
        gl++;
        
        // verschiebliche Lager
        for (int kn = 1; kn < Kn.length; kn++) {
            if ((Kn[kn].getLagerstatus() == OFFEN) && (Kn[kn].getLagerbed() == VERSCHIEBLICH)) {
                double alpha = Kn[kn].getRalpha();
                GLS[gl][ausKnoten[kn][0]] = Math.cos(alpha); // Rx
                GLS[gl][ausKnoten[kn][1]] = Math.sin(alpha); // Rz
                gl++;
            }
        }
        
        assert (gl == anzGL) : gl + " ungleich " + anzGL;
        if (gl != anzGL) {
            System.err.println("Fehler (Anz. Gleichungen): " + gl + " ungleich " + anzGL);
            System.err.println("WARNUNG: Programm nicht unterbrochen. Vorsicht mit Resultaten.");
//            WIDERSPRUCHaufgetreten = true; // TODO, ev nicht Widerspruch sondern Fehler, falls dies auftritt: nachgehen
//            throw new IllegalArgumentException("Die Anzahl Gleichungen stimmt nicht. Ev. Programmfehler."); // TODO
        }
        
        
        // ----------------------
        // Gleichungssystem lösen
        // ----------------------
        
        // GLS lösen
        if (verbose) System.out.print("Beginne das Gleichungssystem zu loesen... ");
        GLSsolver solver = new GLSsolver(GLS);
        statischeUnbestimmtheit = solver.getAnzUnbestParam();
        double[][] xLsg = solver.solve();
        
        assert (xLsg.length == anzUnbek) : "xLsg.length = " + xLsg.length + " ungleich anzUnbek " + anzUnbek;
        if (verbose) System.out.println("fertig.");
        
        // Lösung einsetzten in Datensatz
        for (int i = 0; i < anzUnbestSt; i++) { // Stäbe
            if (xLsg[i][0] > 0) {
                St[stabnr[i]].setKraft(BER, xLsg[i][1]);
            }
            else { // Kontrolle, ob unbest
                assert St[stabnr[i]].getStatus() == UNBEST : "Stab "+stabnr[i]+": Status "+St[stabnr[i]].getStatus();
            }
        }
        
        for (int i = anzUnbestSt; i < anzUnbek; i = i + 2) { // Lagerkraft
            assert knnr[i] == knnr[i+1];
            if ((xLsg[i][0] > 0) && (xLsg[i+1][0] > 0)) {
                Kn[knnr[i]].setLagerkraft(BER,xLsg[i][1],xLsg[i+1][1]);
            }
        }
    }
    
    private void rKnotenstatusSetzen() {
        // Knotenstatus setzen
        for (int kni = 1; kni < Kn.length; kni++) {
            switch (Kn[kni].getKnotenstatus()) {
                case OFFEN:
                    switch (Kn[kni].getLagerstatus()) {
                        case GESETZT:
                        case BER:
                            // kontrollieren ob alle Stäbe rundum bestimmt sind.
                            int anzUnbekStäbe = 0;
                            for (int knk = 1; knk < Kn.length; knk++) {
                                if ((Top[kni][knk] > 0)
                                && (St[Top[kni][knk]].getStatus() == UNBEST) ) {
                                    anzUnbekStäbe ++;
                                }
                            }
                            if (anzUnbekStäbe == 0) Kn[kni].setKnotenstatus(FERTIG);
                            break;
                        case UNBEST: // Knotenstatus unverändert lassen.
                            break;
                        default:
                            assert false: "Knoten " + kni + ": Lagerstatus " + Kn[kni].getLagerstatus();
                    }
                    break;
                default:
            }
        }
    }
    
    /** Knotengleichgewichte kontrollieren für FERTIGE Knoten.
     */
    boolean resultatcheck() {
        for (int kni = 1; kni < Kn.length; kni++) {
            if (Kn[kni].getKnotenstatus() == FERTIG) {
                double resultX = Kn[kni].getLx();
                double resultZ = Kn[kni].getLz();
                
                if (Kn[kni].getLagerstatus() != LOS) {
                    resultX += Kn[kni].getRx();
                    resultZ += Kn[kni].getRz();
                }
                
                for (int knk = 1; knk < Kn.length; knk++) {
                    if (Top[kni][knk] > 0)  {
                        if ((St[Top[kni][knk]].getStatus() == BER)
                        || (St[Top[kni][knk]].getStatus() == GESETZT)) {
                            resultX += ax[kni][knk] * St[Top[kni][knk]].getKraft();
                            resultZ += az[kni][knk] * St[Top[kni][knk]].getKraft();
                        }
                        else assert false;
                    }
                }
                
                if (Double.isNaN(resultX) || Double.isNaN(resultZ)) {
                    System.out.println("Knoten "+kni+": Widerspruch (NaN) entdeckt bei Schlusskontrolle");
                    Kn[kni].setKnotenstatus(WIDERSPRUCH);
                    WIDERSPRUCHaufgetreten = true;
                }
                
                if (Math.abs(resultX) > TOLresultatcheck) {
                    System.out.println("Knoten "+kni+": Widerspruch: Resultierende X: "+resultX +" ungleich 0  (0 unbekSt,0 Lager) entdeckt bei Schlusskontrolle");
                    Kn[kni].setKnotenstatus(WIDERSPRUCH);
                    WIDERSPRUCHaufgetreten = true;
                }
                if (Math.abs(resultZ) > TOLresultatcheck) {
                    System.out.println("Knoten "+kni+": Widerspruch: Resultierende Z: "+resultZ +" ungleich 0  (0 unbekSt,0 Lager) entdeckt bei Schlusskontrolle");
                    Kn[kni].setKnotenstatus(WIDERSPRUCH);
                    WIDERSPRUCHaufgetreten = true;
                }
            }
        }
        if (WIDERSPRUCHaufgetreten) return false;
        else return true;
    }
    
    /** Gibt an, ob das Fachwerkmodell vollständig gelöst wurde (alle Knoten FERTIG).
     *  Für externe Aufrufe soll die Gleichgewichtskontrolle angeschaltet werden, dann wird
     *  resultatcheck() (Kontrolle der Knotengleichgewichte) durchgeführt. Ein erkannter
     *  Widerspruch löst dann eine Exception aus, ohne GlgewKontrolle wird WIDERSPRUCHaufgetreten = true.;
     */
    public boolean istvollständiggelöst(boolean GlgewKontrolle) throws Exception {
        // Gleichgewichtskontrolle an den fertig gelösten Knoten (optional)
        if (GlgewKontrolle) {
            if (resultatcheck() == false) {
                System.out.println("!!! ACHTUNG: Das System ist WIDERSPUECHLICH !!!!");
                System.out.println("es kann sich um einen Eingabefehler, ein numerisches Problem");
                System.out.println("oder allenfalls um einen Programmfehler handeln.");
                throw new Exception("WIDERSPRUCH - INCONSISTENT- CONTRADICTOIRE");
            }
        }
        
        // Überprüfung, ob alle Knoten fertig gelöst sind
        for (int kni = 1; kni < Kn.length; kni++) {
            if (Kn[kni].getKnotenstatus() != FERTIG) {
                if (statischeUnbestimmtheit == Integer.MIN_VALUE) { // d.h. nicht berechnet.
                    if (verbose) System.out.println("statische Unbestimmtheit: unbekannt");
                }
                else System.out.println("statische Unbestimmtheit: " + statischeUnbestimmtheit);
                System.out.println("THE EQUILIBRIUM CHECK IS ONLY COMPLETE IF ALL FORCES ARE KNOWN!");
                return false;
            }
        }
        
        // Plausibilitätskontrolle (Summe der angreifenden Kräfte = Summe der Auflagerkräfte)
        // Summe Lasten, Summe Auflagerkräfte
        double SummeLx = 0; double SummeLz = 0;
        double SummeMLy = 0;
        double SummeAx = 0; double SummeAz = 0;
        double SummeMAy = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            SummeLx += Kn[kn].getLx();
            SummeLz += Kn[kn].getLz();
            SummeMLy += Kn[kn].getZ() * Kn[kn].getLx() - Kn[kn].getX() * Kn[kn].getLz();
            if (Kn[kn].getLagerbed() == FIX || Kn[kn].getLagerbed() == VERSCHIEBLICH) {
                SummeAx += Kn[kn].getRx();
                SummeAz += Kn[kn].getRz();
                SummeMAy += Kn[kn].getZ() * Kn[kn].getRx() - Kn[kn].getX() * Kn[kn].getRz();
            }
        }
        boolean plausibel = true;
        if (Math.abs(SummeLx + SummeAx) > TOLresultatcheck) plausibel = false;
        if (Math.abs(SummeLz + SummeAz) > TOLresultatcheck) plausibel = false;
        if (Math.abs(SummeMLy + SummeMAy) > TOLresultatcheck) plausibel = false;
        if (!plausibel) {
            System.out.println("Die Plausibilitaetspruefung ist NICHT OK (SUMME F <> 0) --- NOT OK !!!");
            WIDERSPRUCHaufgetreten = true;
            if (GlgewKontrolle) throw new Exception("NICHT PLAUSIBLE - SUM F <> 0");
        }
        
        System.out.println("Das Fachwerkmodell ist VOLLSTAENDIG GELOEST. --- COMPLETELY SOLVED");
        return true;
    }
    
    private boolean rMechanismusVerletztGlgew() throws Exception {
        clMechanismus mech = new clMechanismus(Kn, St, Top);
        mech.setVerbose(verbose);
        mech.setModellSchonGeprüft(true);
        boolean fertigberechnet = mech.rechnen();
        
        if (!fertigberechnet) System.out.println("[clFachwerk] Warnung: Mechanismen nicht fertig gerechnet.");
        if (true) { // verbose
            System.out.println("");
            if (mech.istStabil()) System.out.println("Das System ist STABIL."); // TODO übersetzen
            else System.out.println("Das System ist instabil.");
        }
        if (mech.verletztGleichgewicht()) {
            System.out.println("Das System ist vermutlich NICHT IM GLEICHGEWICHT!"); // TODO übersetzen
            System.out.println("");
            mechanismusRelKnVersch = mech.getVerschobeneLage();
        }
        else {
            System.out.println("OK, kein Mechanismus gefunden, der das Glgew verletzt."); // TODO übersetzen
            System.out.println("");
        }
        return mech.verletztGleichgewicht();
    }
    
    public void resultatausgabe_direkt() {
        // Knotenstatus
        if (verbose) {
            for (int kn = 1; kn < Kn.length; kn++) {
                switch (Kn[kn].getKnotenstatus()) {
                    case OFFEN:
                        System.out.println("Knoten "+kn+": Status OFFEN");
                        break;
                    case FERTIG:
                        System.out.println("Knoten "+kn+": Status FERTIG");
                        break;
                    case WIDERSPRUCH:
                        System.out.println("Knoten "+kn+": Status WIDERSPRUCH");
                        break;
                    default:
                }
            }
        }
        
        // Stabkräfte
        if (verbose) {
            for (int st = 1; st < St.length; st++) {
                int vonkn = 0; // wenn dann in der Ausg. noch null steht, ist ein Fehler passiert
                int biskn = 0;
                schlaufeTop:
                    for (int i = 1; i < Top.length; i++) {
                        for (int j = 1; j < Top[i].length; j++) {
                            if (Top[i][j] == st) {
                                vonkn = i;
                                biskn = j;
                                break schlaufeTop;
                            }
                        }
                    }
                    System.out.print("Stab " + st + " (Kn " + vonkn + " - " + biskn + "): ");
                    switch (St[st].getStatus()) {
                        case BER:
                            System.out.println("N = " + Fkt.nf(St[st].getKraft(), 1) + "  Status BERECHNET");
                            break;
                        case GESETZT:
                            System.out.println("N = " + Fkt.nf(St[st].getKraft(), 1) + "  Status GESETZT");
                            break;
                        case UNBEST:
                            System.out.println("N = ???, Status UNBESTIMMT");
                            break;
                        default:
                            System.out.println("Status " + St[st].getStatus() );
                    }
            }
        }
        
        // Lager
        if (verbose) {
            for (int kn = 1; kn < Kn.length; kn++) {
                switch (Kn[kn].getLagerbed()) {
                    case FIX:
                        System.out.print("Knoten "+kn+" fest gelagert: ");
                        switch (Kn[kn].getLagerstatus()) {
                            case BER:
                                System.out.println("Rx = " + Fkt.nf(Kn[kn].getRx(),1) + ", Rz = " + Fkt.nf(Kn[kn].getRz(),1)
                                + "  Status BERECHNET");
                                break;
                            case GESETZT:
                                System.out.println("Rx = " + Fkt.nf(Kn[kn].getRx(),1) + ", Rz = " + Fkt.nf(Kn[kn].getRz(),1)
                                + "  Status GESETZT");
                                break;
                            case UNBEST:
                                System.out.println("Rx, Ry = ???, Status UNBESTIMMT");
                                break;
                            default:
                                System.out.println("Status " + Kn[kn].getLagerstatus() );
                        }
                        break;
                    case VERSCHIEBLICH:
                        System.out.print("Knoten "+kn+" verschieblich gelagert (a = "+Fkt.nf(Math.toDegrees(Kn[kn].getRalpha()),1)+"°): ");
                        switch (Kn[kn].getLagerstatus()) {
                            case BER:
                                System.out.println("Rx = " + Fkt.nf(Kn[kn].getRx(),1) + ", Rz = " + Fkt.nf(Kn[kn].getRz(),1)
                                + "  Status BERECHNET");
                                break;
                            case GESETZT:
                                System.out.println("Rx = " + Fkt.nf(Kn[kn].getRx(),1) + ", Rz = " + Fkt.nf(Kn[kn].getRz(),1)
                                + "  Status GESETZT");
                                break;
                            case UNBEST:
                                System.out.println("R = ???, Status UNBESTIMMT");
                                break;
                            default:
                                System.out.println("Status " + Kn[kn].getLagerstatus() );
                        }
                        break;
                    default:
                }
            }
        }
        
        // Summe Lasten, Summe Auflagerkräfte
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
        System.out.println("Summe aller Lasten:");
        System.out.print("Lx = " + Fkt.nf(SummeLx,1) + "  ");
        System.out.print("Lz = " + Fkt.nf(SummeLz,1) + "  ");
        System.out.println("ML = " + Fkt.nf(SummeML,1));
        System.out.println("Summe der berechneten Lagerkraefte:");
        System.out.print("Ax = " + Fkt.nf(SummeAx,1) + "  ");
        System.out.print("Az = " + Fkt.nf(SummeAz,1) + "  ");
        System.out.println("MA = " + Fkt.nf(SummeMA,1));
        
        if (WIDERSPRUCHaufgetreten) {
            System.out.println("!!! ACHTUNG: Das System ist WIDERSPUECHLICH !!!!");
            System.out.println("es kann sich um einen Eingabefehler, ein numerisches Problem");
            System.out.println("oder allenfalls um einen Programmfehler handeln.");
        }
        System.out.println("");
    }
    
    /** Diese Methode liefert einen zufälligen Mechanismus, der das Gleichgewicht verletzt.
     * Gibt es keinen oder wurde die Mechanismusberechnung nicht durchgeführt, gibt die Methode null zurück.
     * @return Relativverschiebung in x und z Rtg. für jeden Knoten (Knoten 1: Index1).*/
    public double[][] getMechanismus() {
        return mechanismusRelKnVersch;
    }
    
}
