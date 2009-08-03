/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Fachwerk.addins.automModellsuche;

import Fachwerk.statik.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2009 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                      <qwert2003@users.berlios.de>
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

/**
 *
 * @author av
 */
public class clAutomModellsuche extends clElastisch implements inKonstante {

    /** Nulltoleranz */
    private final double TOL = inKonstante.TOL_gls;
    
    /** Ab wann gilt ein Stab als horizontal/vertikal/45°-diagonal?*/
    double TOLgeom = 0.001;

    /** Bricht den Optimierungsvorgang ab.*/
    int maxIterationen = 400;

    static final boolean debug = true; // TODO


    // Vorgabewerte Referenzwerte
    // hergeleitet aus:
    // nel = Es / Ec = 205/31 = 6.6
    // npl = fsd/fcd = 435/16.5 = 26.4
    // bevorzugungDruckstrebe = 2.5 // implizite Annahme, dass Betonquerschnitt selten ausgenutzt

    // Gewichtungen
    // bezogen auf γ_s = 1;
    /** Gewichtungsfaktor Beton-Druckstreben. Bevorzugungsfaktor * fsd/fcd * Ec/Es */
    double γ_c = 10; // bevorzugungDruckstrebe * npl/nel = 2.5 * 16.4 / 6.6 = 10
    /** Gewichtungsfaktor für nicht horizontal oder vertikal verlaufende Zugbänder. */
   double γ_sred = 0.005;
    /** Gewichtungsfaktor für unter 45° verlaufende Zugbänder. */
    double γ_s45 = 0.1;



    /** Stablängen
     Stab mit Index 0 gibt es nicht.*/
    protected double[] Ldx = new double[1 + anzSt];;
    protected double[] Ldz = new double[1 + anzSt];;
    
    private clKnoten[] Kn;
    private int[][] Top;


    /** Creates a new instance of clElastisch */
    public clAutomModellsuche(clStab[] Staebe, clKnoten[] Kn, int[][] Top) {
        super(Staebe);

        for (int von = 1; von < Kn.length; von++) {
            for (int bis = 1; bis < Kn.length; bis++) {
                int stabnr = Top[von][bis];
                if (stabnr > 0) {
                    double dx = Kn[bis].getX() - Kn[von].getX();
                    double dz = Kn[bis].getZ() - Kn[von].getZ();
                    Ldx[stabnr] = dx;
                    Ldz[stabnr] = dz;
                    L[stabnr] = Math.sqrt(dx*dx + dz*dz);
                }
            }
        }

        this.Kn = Kn;
        this.Top = Top;
    }

    /** Führt einen Optimierungsschritt durch. Berechnet das Modell elastisch
     und setzt anschliessend As = Nsd / fsd und dergleichen.
     Ist eine Stabkraft beinahe 0, wird der Stab zu null gesetzt.
     Gibt true zurück, solange das Modell nicht verändert wurde (kein Stab neu gesetzt).
     Beim Rückgabewert false muss das Fachwerkmodell neu berechnet werden. */
    private boolean optimiereSchritt() {
        boolean MODELL_UNVERAENDERT = true;

        if (statischeUnbestimmtheit < 1) {
            System.err.println("[clAutomModellsuche.optimiereSchritt] statisch bestimmt, nichts zu tun");
            return MODELL_UNVERAENDERT;
        }

        rechnen();

        // Steifigkeit setzen
        for (int st = 1; st < EA.length; st++) {
            double Nst = N[st - 1];
            //if (debug) System.out.println("Nst = " + Nst);

            if (Math.abs(Nst) < TOL || Double.isNaN(Nst)) { // Nullstab
                if (debug) if (Double.isNaN(Nst)) System.out.println("N[Stab " +st + "] = NaN");
                EA[st] = Double.MIN_VALUE; // Division durch 0 verhindern
                int status = St[st].getStatus();
                assert status != NICHTSETZBAR: "[clAutomModellsuche.optimiereSchritt] Programmfehler: Status NICHTSETZBAR z.Z. nicht verwendet.";
                if (status == inKonstante.BER || status == inKonstante.UNBEST) {
                    St[st].zurücksetzen(false);
                    St[st].setKraft(GESETZT, 0);
                    MODELL_UNVERAENDERT = false;
                }
                else assert status == inKonstante.GESETZT && Math.abs(St[st].getKraft()) < TOL: "Status: " + St[st].getStatus();
                continue; // Schlaufe über Stäbe
            }

            if (Nst < 0) { // Druckstrebe
                EA[st] = γ_c * Nst; // eigentlich Ac = Nst / fcd, infolge Normierung auf Stahl und Gewichtung verkuerzt.
            }
            
            else { // Zuggurt
                assert Nst > 0: "[clAutomModellsuche.optimiereSchritt] N[Stab " +st + "] = " + Nst + " nicht > 0!";
                assert L[st] > 0: "[clAutomModellsuche.optimiereSchritt] L[Stab " +st + "] = " + L[st] + " nicht > 0!";

                if (Math.abs(Ldx[st] / L[st]) < TOLgeom || Math.abs(Ldz[st] / L[st]) < TOLgeom || St[st].getStatus() == inKonstante.GESETZT) {
                    // Stab vertikal oder horizontal oder ist ein gesetzter Zugstab
                    EA[st] = Nst; // eigentlich As = Nst / fsd, infolge Normierung auf Stahl verkürzt
                }
                else if (Math.abs(Math.abs(Ldx[st] / Ldz[st]) - 1) < TOLgeom) {
                    // Stab unter 45°
                    EA[st] = γ_s45 * Nst;
                }
                else {
                    // Zugstab unpraktisch gelegen
                    EA[st] = γ_sred * Nst;
                }
            }
        }
        return MODELL_UNVERAENDERT;
    }

    /** Führt in einem Optimierungsprozedere eine Eliminierung wenig wirksamer Stäbe durch.
     Rückgabewert false, falls die maximale Anzahl Iterationen erreicht wurde.*/
    public boolean optimiere(int gewünschteMaxStatUnbestimmtheit) {
        assert gewünschteMaxStatUnbestimmtheit >= 0;
        if (gewünschteMaxStatUnbestimmtheit < 0) gewünschteMaxStatUnbestimmtheit = 0;

        final boolean OptionVorber = true;
        final boolean OptionGLS = true;
        final boolean OptionMechanismus = true;

        boolean keinWIDERSPRUCH = true;
        //boolean keinFEHLER = true;
        boolean VOLLSTÄNDIGGELÖST_OK = false;
        clFachwerk fw;
//        clFachwerk fw = new clFachwerk(Kn, St, Top);
//        fw.setVerbose(false);
//        try {
//            keinWIDERSPRUCH = fw.rechnen(OptionVorber,OptionGLS,OptionMechanismus);
//            //int statUnbesth = fw.getStatischeUnbestimmtheit();
//            fw.resultatausgabe_direkt();
//            if (keinWIDERSPRUCH) VOLLSTÄNDIGGELÖST_OK = fw.istvollständiggelöst(false); // false, das resutatcheck() soeben in .rechnen() durchgeführt
//            setCompleteSolution(fw.getCompleteSolution()); // setzt auch super.statischeUnbestimmtheit
//        }
//        catch (Exception e) {
//            System.out.println(e.toString());
//        }

        boolean weiter_iterieren = true;
        boolean maxIterationenErreicht = false;
        int iteration = 1;
        boolean modellunverändert = false;
        do {
            iteration++;


            if (!modellunverändert) { // wenn zwischenzeitlich Stäbe gesetzt wurden, neu berechnen!
                try {
                    fw = new clFachwerk(Kn, St, Top);
                    fw.setVerbose(false);
                    keinWIDERSPRUCH = fw.rechnen(OptionVorber, OptionGLS, OptionMechanismus);
                    //int statUnbesth = fw.getStatischeUnbestimmtheit();
                    fw.resultatausgabe_direkt();
                    if (keinWIDERSPRUCH) {
                        VOLLSTÄNDIGGELÖST_OK = fw.istvollständiggelöst(false); // false, da resutatcheck() soeben in .rechnen() durchgeführt
                    }
                    if (VOLLSTÄNDIGGELÖST_OK) {
                        statischeUnbestimmtheit = 0;
                    }
                    else {
                        double [][] vollstLsg = fw.getCompleteSolution();
                        assert vollstLsg != null: "[clAutomModellsuche.optimiere] vollstLst == null";
                        setCompleteSolution(vollstLsg); // setzt auch super.statischeUnbestimmtheit
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }


            if (statischeUnbestimmtheit > gewünschteMaxStatUnbestimmtheit) {
                modellunverändert = optimiereSchritt();
            }
            else weiter_iterieren = false;

            // Abbruchkriterien
            if (iteration >= maxIterationen) {
                maxIterationenErreicht = true;
                weiter_iterieren = false;
            }
        } while (weiter_iterieren);
        
        System.out.println();
        System.out.println("Anzahl Iterationen: " + iteration);
        if (debug) {
            if (maxIterationenErreicht) System.out.println("Abbruch durch Erreichen der maximalen Anzahl Iterationen.");
        }

        return !maxIterationenErreicht;
    }


    /**
     * Zu Testzwecken
     * und um zu zeigen, wie diese Klasse genutzt werden kann.
     */
    public static void main(String[] args) throws Exception {
        clKnoten[] testKn;
        clStab[] testSt;
        int [][] testTop;


        int bsp = 2;
        if (args.length > 0) bsp = Integer.parseInt(args[0]);
        switch (bsp) {
            case 2:
                testKn = new clKnoten[1+4];
                testSt = new clStab[1+3];
                testTop = new int[testKn.length][testKn.length];

                testKn[1] = new clKnoten(0,0);
                testKn[2] = new clKnoten(0,-2);
                testKn[3] = new clKnoten(0,4);
                testKn[4] = new clKnoten(0,6);
                // Stäbe
                for (int i = 1 ; i < testSt.length; i++) {
                    testSt[i] = new clStab();
                }
                // Topologie schreiben
                testTop[1][2] = 1;
                testTop[1][3] = 2;
                testTop[1][4] = 3;
                // Lasten und Auflagerkräfte zuordnen
                testKn[2].setLager(FIX);
                testKn[3].setLager(FIX);
                testKn[4].setLager(FIX);
                testKn[1].setLast(0,100d);

                break;


            case 1:
                testKn = new clKnoten[1+3];
                testSt = new clStab[1+2];
                testTop = new int[testKn.length][testKn.length];

                testKn[1] = new clKnoten(0,0);
                testKn[2] = new clKnoten(0,5);
                testKn[3] = new clKnoten(0,8);
                // Stäbe
                for (int i = 1 ; i < testSt.length; i++) {
                    testSt[i] = new clStab();
                }
                // Topologie schreiben
                testTop[1][2] = 1;
                testTop[2][3] = 2;
                // Lasten und Auflagerkräfte zuordnen
                testKn[1].setLager(FIX);
                testKn[3].setLager(FIX);
                testKn[2].setLast(0,100d);

                break;

            case 0:
            default:
                testKn = new clKnoten[1+ 7];
                testSt = new clStab[1 +11];
                testTop = new int[testKn.length][testKn.length];

                testKn[1] = new clKnoten(0,6);
                testKn[2] = new clKnoten(4,2);
                testKn[3] = new clKnoten(4,6);
                testKn[4] = new clKnoten(9,2);
                testKn[5] = new clKnoten(9,6);
                testKn[6] = new clKnoten(13,2);
                testKn[7] = new clKnoten(13,6);

                for (int i = 1 ; i < testSt.length; i++) {
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
        }


        clFachwerk fw = new clFachwerk(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();

        clAutomModellsuche autom = new clAutomModellsuche(testSt, testKn, testTop);
        autom.optimiere(0);
        autom.resultatausgabe_direkt();

        //double[] N = autom.getLsg();
        System.out.println("");

        // Löschen der Nullstäbe
        int anzNullstäbe = 0;
        for (int st = 1; st < testSt.length; st++) {
            if (testSt[st].getStatus() == GESETZT && testSt[st].getKraft() == 0) anzNullstäbe++;
        }
        System.out.println("Anzahl gesetzter Nullstäbe: " + anzNullstäbe);
        System.out.println("");

        fw = new clFachwerk(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();
        //fw.istvollständiggelöst(true);
    }
}
