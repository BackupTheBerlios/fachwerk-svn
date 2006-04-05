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
public class clMechanismus implements inKonstante {
    
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
    
    private final double TOL = TOL_vorberechnung;
    private final double TOLresultatcheck = TOL_resultatcheck;
    
    
    // berechnete Datensätze
    // Rotation vom Betrag 1 um Achse y, d.h. im Gegenuhrzeigersinn.
    private double rYx[][]; // Rot. um y-Achse, x-Komponente
    private double rYz[][]; // [KnotenNr.][infolge Rot. um KnotenNr.]
    
    
    private boolean INSTABIL = false; // i.d.R. harmlos, da Glgew. trotzdem eingehalten sein kann.
    private boolean INSTABIL_KEIN_GLGEW = false; // Gleichgewichtsbedingung verletzt
    
    private boolean modellgeprüft = false;
    private boolean fertigberechnet = false;
    private boolean verbose = false;
    private final boolean debug = false;
    
    
    /** Creates a new instance of clMechanismus */
    public clMechanismus(clKnoten[] p_kn, clStab[] p_st, int[][] p_Top) {
        anzKn = p_kn.length - 1; // Knoten 0 existiert nicht
        anzSt = p_st.length - 1; // Stab 0 existiert nicht
        
        
        if (p_Top.length != p_kn.length) System.err.println("Programmfehler: [clMechanismus.initalisieren]: Topologie falsch");
        Kn = p_kn;
        St = p_st;
        Top = p_Top;
    }
    
    /** Gibt ausgiebig Informationen auf der Konsole aus. */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    public void setModellSchonGeprüft(boolean schongeprüft) {
        modellgeprüft = schongeprüft;
    }
    
    /** Gibt an, ob das Modell stabil ist. Dies ist oft nicht der Fall, das Gleichgewicht
        kann trotzdem eingehalten sein.*/
    public boolean istStabil() throws Exception {
        if (fertigberechnet) {
            return !INSTABIL;
        }
        else {
            assert false;
            throw new Exception("[clMechanismus] Programmfehler: zuerst berechnen, dann Stabilitaet abfragen.");
        }
    }
    
    /** Gibt an, ob ein Mechanismus gefunden worden ist, der eine Gleichgewichtsverletzung beweist.*/
    public boolean verletztGleichgewicht() throws Exception {
        if (fertigberechnet) {
            return INSTABIL_KEIN_GLGEW;
        }
        else {
            assert false;
            throw new Exception("[clMechanismus] Programmfehler: zuerst berechnen, dann Stabilitaet abfragen.");
        }
    }
    
    /**
     * 
     * zu Testzwecken
     */
    public static void main(String[] args) throws Exception {
        clKnoten[] testKn;
        clStab[] testSt;
        int [][] testTop;
        
        int bsp = 2;
        if (args.length > 0) bsp = Integer.parseInt(args[0]);
        switch (bsp) {
            case 2:
                testKn = new clKnoten[2+1];
                testSt = new clStab[1+1];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten(0d,0d);
                testKn[2] = new clKnoten(10d,0d);
                
                for (int i = 1 ; i < testSt.length; i++) {
                    testSt[i] = new clStab();
                }
                
                // Topologie schreiben
                testTop[1][2] = 1;
                
                
                // Lasten und Auflagerkräfte zuordnen
                testKn[1].setLager(VERSCHIEBLICH, Math.toRadians(180d));
                testKn[2].setLager(VERSCHIEBLICH, Math.toRadians(180d));
                testKn[2].setLast(0d,100d);
                break;
                
            case 1:
                testKn = new clKnoten[2+1];
                testSt = new clStab[1+1];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten(0d,5d);
                testKn[2] = new clKnoten(0d,0d);
                
                for (int i = 1 ; i < testSt.length; i++) {
                    testSt[i] = new clStab();
                }
                
                // Topologie schreiben
                testTop[1][2] = 1;
                
                
                // Lasten und Auflagerkräfte zuordnen
                testKn[1].setLager(FIX);
                testKn[2].setLast(0d,100d);
                break;
                
                
            case 0:
            default:
                testKn = new clKnoten[4+1];
                testSt = new clStab[3+1];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten(0d,5d);
                testKn[2] = new clKnoten(3d,0d);
                testKn[3] = new clKnoten(9d,0d);
                testKn[4] = new clKnoten(9d,5d);
                
                for (int i = 1 ; i < testSt.length; i++) {
                    testSt[i] = new clStab();
                }
                
                // Topologie schreiben
                testTop[1][2] = 1;
                testTop[2][3] = 2;
                testTop[3][4] = 3;
                
                
                // Lasten und Auflagerkräfte zuordnen
                testKn[1].setLager(FIX);
                testKn[4].setLager(FIX);
                testKn[2].setLast(0,100d);
                testKn[3].setLast(-60,0d);
                break;
        }
        
        
        clMechanismus mech = new clMechanismus(testKn, testSt, testTop);
        mech.setVerbose(true);
        mech.rechnen();
        
        System.out.println("");
        if (mech.istStabil()) System.out.println("Das System ist STABIL.");
        else System.out.println("Das System ist nicht stabil.");
        if (mech.verletztGleichgewicht()) System.out.println("Das System ist NICHT IM GLEICHGEWICHT!");
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    /** Führt die kinematische Untersuchung durch.
     * @return false: Berechnung abgebrochen. true: Berechnung durchgeführt.
     */
    public boolean rechnen() throws Exception {
        
        if (verbose) {
            System.out.println("");
            System.out.println("--------------");
            System.out.println("Mechanismuspruefung");
            System.out.println("Modell mit " + anzSt + " Staeben und " + anzKn + " Knoten.");
        }
                
        if (!modellgeprüft) {
            if (check() == false) {
                System.err.println("[clMechanismus] Fehler beim check");
                return false;
            }
        }
        
        fertigberechnet = rMechanismen();
        
        
        return fertigberechnet;
    }
    
    
    /** Diese Prüfung ist identisch mit jener in clFachwerk. Falls clMechanismus von dort aus
        aufgerufen wurde, ist dieser check überflüssig: setModellSchonGeprüft(true)*/
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
    
    /** Sucht einen Mechanismus, der das Gleichgewicht verletzt.
     * @return false: Berechnung abgebrochen. true: Berechnung durchgeführt.*/
    private boolean rMechanismen() {
        
        // 1.) Überprüfen, ob Einzelknoten existiert, der das Glgew. verletzt.
        // nötig, da anschliessende Ber. davon ausgeht, dass alle Knoten durch Stäbe verbunden sind,
        // oder zumindest ein Lager haben.
        for (int kni = 1; kni < Top.length; kni++) {
            int anzangeschlStäbe = 0;
            for (int knk = 1; knk < Top.length; knk++) {
                if (Top[kni][knk] > 0) {
                    anzangeschlStäbe ++;
                }
            }
            if (anzangeschlStäbe < 1) {
                if (verbose) System.out.println("WARNUNG: Der Knoten " + kni +" ist mit keinem Stab verbunden!");
                INSTABIL = true;
                
                if (Kn[kni].getLx() != 0 || Kn[kni].getLz() != 0) {
                    System.out.println("Loser Knoten verletzt Gleichgewicht: " + kni);
                    INSTABIL_KEIN_GLGEW = true;
                    return true;
                }
            }
        }
        
        // 2.) Rotationsmatrix aufstellen
        // Rotation vom Betrag 1 um Achse y, d.h. im Gegenuhrzeigersinn.
        rYx = new double[Kn.length][Kn.length]; // Rot. um y-Achse, x-Komponente
        rYz = new double[Kn.length][Kn.length]; // [KnotenNr.][infolge Rot. um KnotenNr.]
        for (int von = 1; von < Kn.length; von++) {
            for (int bis = 1; bis < Kn.length; bis++) {
                if (Top[von][bis] > 0) {
                    double dx = Kn[bis].getX() - Kn[von].getX();
                    double dz = Kn[bis].getZ() - Kn[von].getZ();
                    rYx[bis][von] = dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d));
                    rYz[bis][von] = -dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d));
                    
                    if (debug) {
                        System.out.println("von Kn "+von+" bis "+bis+" : dx = "+Fkt.nf(dx,2)+", dz = "+Fkt.nf(dz,2));
                        System.out.println("am Kn "+bis+" inf.Rot.um "+von+" : rYx = "+Fkt.nf(rYx[bis][von],2));
                        System.out.println("am Kn "+bis+" inf.Rot.um "+von+" : rYz = "+Fkt.nf(rYz[bis][von],2));
                    }
                }
            }
        }
        
        // 3.) Gleichungssystem der Knotenverschiebungen aufstellen
        
        // das GLS enthält als Unbekannte: (in dieser Reihenfolge)
        // - Leistung der äusseren Kräfte PL
        // - Knotenverschiebungen: 2 (x,z-Rtg) pro Knoten
        // - Stabrotationen: 1 (um Y-Achse) pro Stab
        
        // das GLS enthält folgende Gleichungen: (in dieser Reihenfolge)
        // - Leistung der äusseren Kräfte
        // - Knotenverschiebungen: 2 (in x- und z-Rtg) pro Stab
        // - bekannte Verschiebungen (=0) der Lager: 2 (x,z-Rtg fix), 1 (verschiebliche Lager)
        
        int anzSt = St.length-1;
        int anzKn = Kn.length-1;
        int anzUnbek = 1 + 2*anzKn + 1*anzSt;
        
        int anzFixeKn = 0; // Startwerte
        int anzVerschKn = 0;
        for (int kn = 1; kn < Kn.length; kn++) {
            switch(Kn[kn].getLagerbed()) {
                case FIX:
                    anzFixeKn++;
                    break;
                case VERSCHIEBLICH:
                    anzVerschKn++;
                case LOS:
                    break;
                default:
                    assert false;
            }
        }
        int anzGL = 1 + 2*anzSt + 2*anzFixeKn + 1*anzVerschKn;
        
        // Abzählkriterium. Der umgekehrte Schluss ist jedoch nicht zwingend richtig!
        if (anzGL < anzUnbek) INSTABIL = true;
        
        if (verbose) {
            System.out.println("Gleichungssystem der Mechanismen:");
            System.out.println("Anzahl Unbekannte:  " + anzUnbek);
            System.out.println("Anzahl Gleichungen: " + anzGL);
        }
        
        if (anzUnbek == 0) {
            System.out.println("Gleichungssystem der Mechanismen: keine Unbekannten");
            assert INSTABIL_KEIN_GLGEW = false;
            return true;
        }
        
        // ----------------------------------------------------------------------------------
        // Indizes erstellen, um zB aus Knotennr/Rtg auf UnbekNr schliessen zu können und umgekehrt
        // ----------------------------------------------------------------------------------
        
        int[] ausStab = new int[St.length]; // liefert den Index der Unbekannten (Stabrotation) aus der Stabnummer
        int[][] ausKnoten = new int[Kn.length][2]; // liefert den Index der Unbekannten aus der Knotennummer
                                              // zweiter Index: Verschiebung in x-Rtg, zRtg: 
        int[] stabnr = new int[anzUnbek]; // liefert die Stabnummer aus dem Index
        int[] knnr = new int[anzUnbek]; // liefert die Knotennummer aus dem Index
        
        // Index-Zähler für Unbekannte im GLS (d.h. UnbekNr)
        int index = 1; // 0 ist PL: Leistung der äusseren Lasten
        
        for (int kn = 1; kn < Kn.length; kn++) {
            // x-Rtg
            ausKnoten[kn][0] = index;
            knnr[index] = kn;
            index++;
            // z-Rtg
            ausKnoten[kn][1] = index;
            knnr[index] = kn;
            index++;
        }
        
        for (int st = 1; st < St.length; st++) {
            ausStab[st] = index;
            stabnr[index] = st;
            index++;
        }
        
        
        assert (index == anzUnbek) : index + " ungleich " + anzUnbek;
        if (index != anzUnbek) {
            System.err.println("Fehler (Anz. Unbek): " + index + " ungleich " + anzUnbek);
            System.err.println("WARNUNG: Programm nicht unterbrochen. Vorsicht mit Resultaten.");
        }
        
        // Gleichunssystem aufstellen
        // --------------------------
        double[][] GLS;
        GLS = new double[anzGL][anzUnbek+1];
        int gl = 0; // Zähler für Gleichungsnummer: Startwert
        
        // Leistung der äussseren Arbeit
        GLS[0][0] = -1; // aus -1*PL + dix*Lix+diz*Liz = 0
        for (int kni = 1; kni < Kn.length; kni++) {
            GLS[0][ausKnoten[kni][0]] = Kn[kni].getLx();
            GLS[0][ausKnoten[kni][1]] = Kn[kni].getLz();
        }
        gl++;
        
        // Knotenverschiebungen: Stäbe suchen
        for (int kni = 1; kni < Kn.length; kni++) {
            for (int knk = 1; knk < Kn.length; knk++) {
                    if (Top[kni][knk] > 0)  {  // sonst kein Stab vorhanden
                        if (knk > kni) { // sonst Gleichung für diesen Stab schon aufgestellt.
                            GLS[gl][ausKnoten[kni][0]] = 1; // x-Rtg
                            GLS[gl][ausKnoten[knk][0]] = -1;
                            GLS[gl][ausStab[Top[kni][knk]]] = rYx[knk][kni];
                            gl++;
                            
                            GLS[gl][ausKnoten[kni][1]] = 1; // z-Rtg
                            GLS[gl][ausKnoten[knk][1]] = -1;
                            GLS[gl][ausStab[Top[kni][knk]]] = rYz[knk][kni];
                            gl++;
                        }
                    }
                } // Ende Zielknoten-Schlaufe
        }
        
        // bekannte Verschiebungen (Lager)
        for (int kn = 1; kn < Kn.length; kn++) {
            switch(Kn[kn].getLagerbed()) {
                case FIX:
                    GLS[gl][ausKnoten[kn][0]] = -1; // x-Rtg: -1*dx+0=0
                    gl++;
                    GLS[gl][ausKnoten[kn][1]] = -1; // z-Rtg
                    gl++;
                    break;
                case VERSCHIEBLICH:
                    // Vorsicht: Gleitrichtung im Uhrzeigersinn von x-Achse definiert
                    double alpha = Kn[kn].getRalpha();
                    GLS[gl][ausKnoten[kn][0]] = Math.sin(alpha); // *dx // TODO ev. besser wie neuen Stab behandeln.
                    GLS[gl][ausKnoten[kn][1]] = -Math.cos(alpha); // *dz
                    // numerische Fehler im Solver vermeiden, die entstehen durch
                    // Math.sin(Math.PI) = 1.2246467991473532E-16  != 0
                    if (Math.abs(GLS[gl][ausKnoten[kn][0]]) < TOL) GLS[gl][ausKnoten[kn][0]] = 0;
                    if (Math.abs(GLS[gl][ausKnoten[kn][1]]) < TOL) GLS[gl][ausKnoten[kn][1]] = 0;
                    // debug
                    System.out.println("Knoten " + kn + ": alpha=" + alpha+" "+ GLS[gl][ausKnoten[kn][0]] + ", " + GLS[gl][ausKnoten[kn][1]]);
                    gl++;
                    break;
                case LOS:
                    break;
                default:
                    assert false;
            }
        }
        
        assert (gl == anzGL) : gl + " ungleich " + anzGL;
        if (gl != anzGL) {
            System.err.println("Fehler (Anz. Gleichungen des Mechanismus): " + gl + " ungleich " + anzGL);
            System.err.println("WARNUNG: Programm nicht unterbrochen. Vorsicht mit Resultaten.");
        }
        
        if (debug) {
            System.out.println("GLS");
            System.out.print("P(L) ");
            for (int i = 1; i <= anzKn; i++) System.out.print("Kn "+i+"      ");
            System.out.println("Stabrotationen");
            for (int i = 0; i < GLS.length; i++) {
                for (int j = 0; j < GLS[i].length; j++) {
                    System.out.print(GLS[i][j]);
                    System.out.print("  ");
                }
                System.out.println("");
            }
        }
        
        // ----------------------
        // Gleichungssystem lösen
        // ----------------------
        
        // GLS lösen
        if (verbose) System.out.print("Beginne das Gleichungssystem fuer Mechanismen zu loesen... ");
        double[][] xLsg = GLSsolver.solve(GLS, debug);
        assert (xLsg.length == anzUnbek) : "xLsg.length = " + xLsg.length + " ungleich anzUnbek " + anzUnbek;
        if (verbose) System.out.println("fertig.");
        
        // Auswerten der Lösung
        // --------------------
        
        // Leistung der äusseren Lasten. Wenn = 0 --> System im Gleichgewicht
        if (xLsg[0][0] > 0) { // Lösung bestimmt
            if (Math.abs(xLsg[0][1]) < TOL) {
                if (verbose) System.out.println("OK, kein Mechanismus gefunden, der das Glgew verletzt.");
                assert !INSTABIL_KEIN_GLGEW;
            }
            else {
                System.out.println("Gleichgewichtsbedingung verletzt");
                System.out.println("Leistung der aeusseren Lasten: " + xLsg[0][1]);
                INSTABIL_KEIN_GLGEW = true;
            }
        }
        else { // nicht eindeutig
                System.out.println("Gleichgewichtsbedingung vermutlich verletzt"); // TODO
                INSTABIL_KEIN_GLGEW = true;
        }
        
        
        // Prüfen auf Stabilität
        for (int i = ausKnoten[1][0]; i <= ausKnoten[anzKn][1]; i++) {
            // Rtg bestimmen
            String rtg = "?";
            if (ausKnoten[knnr[i]][0] == i) rtg = "x";
            if (ausKnoten[knnr[i]][1] == i) rtg = "z";
            
            if (xLsg[i][0] == 0) { // unbestimmt
                if (INSTABIL_KEIN_GLGEW || verbose) {
                    System.out.print("Knoten " + knnr[i] + ": ");
                    System.out.println(rtg + " instabil");
                }
                INSTABIL = true;
            }
            else { // bestimmte Verschiebung
                if (Math.abs(xLsg[i][1]) > TOL) { // ungleich null
                    if (INSTABIL_KEIN_GLGEW || verbose) {
                        System.out.print("Knoten " + knnr[i] + ": ");
                        System.out.println(rtg + " instabil:" + xLsg[i][1]);
                    }
                    INSTABIL = true;
                    assert false; // ist nicht möglich, ohne dass die Skalierung (=eine Unbek) vorgeg.
                }
                else {
                    if (verbose) System.out.println("Knoten " + knnr[i] + ": " + rtg + " stabil");
                }
            }
        }
        
        return true;
    }
}
