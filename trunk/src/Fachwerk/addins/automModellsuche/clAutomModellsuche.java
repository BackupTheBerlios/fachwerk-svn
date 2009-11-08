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

    static final boolean debug = false;

    /** Nulltoleranz */
    private final double TOL = inKonstante.TOL_gls;
    
    /** Ab wann gilt ein Stab als horizontal/vertikal/45°-diagonal?*/
    double TOLgeom = 0.002; // > 0!

    /** Bricht den Optimierungsvorgang ab.*/
    int maxIterationen = 10000000;
    /** Bricht den Optimierungsvorgang ab.*/
    int maxIterationenSeitModelländerung = 200000;
    /** Der (primitive) Ersatzalgorithmus kommt frühestens nach dieser Anzahl Iterationen zum Einsatz.*/
    int minIterSeitModelländ = 50;


    // Vorgabewerte Referenzwerte
    // hergeleitet aus:
    // nel = Es / Ec = 205/31 = 6.6
    // npl = fsd/fcd = 435/16.5 = 26.4
    // bevorzugungDruckstrebe = 2.5 // implizite Annahme, dass Betonquerschnitt selten ausgenutzt

    // Gewichtungen
    // bezogen auf γ_s = 1;
    /** Gewichtungsfaktor Beton-Druckstreben. Bevorzugungsfaktor * fsd/fcd * Ec/Es */
    double γ_c = 11; // bevorzugungDruckstrebe * npl/nel = 2.5 * 16.4 / 6.6 = 10
    /** Gewichtungsfaktor für nicht horizontal oder vertikal verlaufende Zugbänder. */
   double γ_sred = 0.005;
    /** Gewichtungsfaktor für unter 45° verlaufende Zugbänder. */
    double γ_s45 = 0.1;



    /** Stablängen
     Stab mit Index 0 gibt es nicht.*/
    protected double[] Ldx = new double[1 + anzSt];
    protected double[] Ldz = new double[1 + anzSt];
    
    private clKnoten[] Kn;
    private int[][] Top;


    /** Creates a new instance of clAutomModellsuche.
     * Reduziert die statische Unbestimmtheit des Systems in einem Optimierungsvorgang.*/
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
        
        if (debug) {
            maxIterationen = 1000;
            maxIterationenSeitModelländerung = 200;
        }
    }

    /** Führt einen Optimierungsschritt durch. Berechnet das Modell elastisch
     und setzt anschliessend As = Nsd / fsd und dergleichen.
     Ist eine Stabkraft beinahe 0, wird der Stab zu null gesetzt.
     Gibt true zurück, solange das Modell nicht verändert wurde (kein Stab neu gesetzt).
     Beim Rückgabewert false muss das Fachwerkmodell neu berechnet werden. */
    private boolean optimiereSchritt(double[] verformungsenergie) {
        boolean MODELL_UNVERAENDERT = true;

        /** Die Verformungsenegie = ∑ N * N/EA * L über alle Stäbe.
         Dient zu Vergleichszwecken*/
        double energie = 0;

        if (statischeUnbestimmtheit < 1) {
            System.err.println("[clAutomModellsuche.optimiereSchritt] statisch bestimmt, nichts zu tun");
            return MODELL_UNVERAENDERT;
        }

        rechnen();

        // Steifigkeit setzen
        for (int st = 1; st < EA.length; st++) {
            double Nst = N[st - 1];
            if (debug) System.out.println("N[Stab " +st + "] = " + Nst);

            if (debug && Double.isNaN(Nst)) { // debug
                System.out.println ("debugMsg: [clAutomModellsuche.optimiereSchritt] Stab " + st + " ist null.");
            }

            energie += Nst * Nst / EA[st] * L[st];

            if (Math.abs(Nst) < TOL || Double.isNaN(Nst)) { // Nullstab
                if (debug) if (Double.isNaN(Nst)) System.out.println("N[Stab " +st + "] = NaN");
                EA[st] = Double.MIN_VALUE; // Division durch 0 verhindern
                int status = St[st].getStatus();
                switch (status) {
                    case UNBEST:
                    case BER:
                        St[st].zurücksetzen(false);
                        St[st].setKraft(GESETZT, 0);
                        if (debug) System.out.println("Stab " +st + " zu 0 gesetzt");
                        MODELL_UNVERAENDERT = false;
                        break;
                    case GESETZT:
                        assert Math.abs(St[st].getKraft()) < TOL;
                        break;
                    case NICHTSETZBAR:
                        assert false: "[clAutomModellsuche.optimiereSchritt] Programmfehler: Status NICHTSETZBAR z.Z. nicht verwendet.";
                        break;
                    default:
                        assert false: "[clAutomModellsuche.optimiereSchritt] Programmfehler: Status: " + status;
                }
                //if (debug) System.out.println("EA[Stab " +st + "] = " + EA[st]);
                continue; // Schlaufe über Stäbe
            }

            if (Nst < 0) { // Druckstrebe
                EA[st] = -γ_c * Nst; // eigentlich Ac = |Nst| / fcd, infolge Normierung auf Stahl und Gewichtung verkuerzt.
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
            //if (debug) System.out.println("EA[Stab " +st + "] = " + EA[st]);
        }
        verformungsenergie[0] = energie;
        return MODELL_UNVERAENDERT;
    }

    /** Führt in einem Optimierungsprozedere eine Eliminierung wenig wirksamer Stäbe durch.
     Rückgabewert false, falls die Optimierung nicht zum gewünschten Ziel kam,
     * z.B. da die maximale Anzahl Iterationen erreicht wurde.
     * Die Strategie ist zweistufig. Zunächst werden die unwirksamen Stäbe entfernt,
     * d.h. die gewichtete Verformungsenergie minimiert.
     * Falls der Optimierungsprozess festfährt, wird einfach jeweils der (überzählige) Stab mit
     * der kleinsten Kraft (aus vorheriger Optimierung) entfernt.
     @param gewünschteMaxStatUnbestimmtheit Der Reduktionsprozess wird abgebrochen,
     * sobald die statische Bestimmtheit kleiner gleich der gewünschten ist.
     @param auchStrategie2Anwenden Wenn der Reduktionsprozess festgefahren ist,
     * wird jeweils derjenige überzählige Stab mit der kleinsten Kraft eliminiert.
     * (Holzhackermethode).
     @param nureinSchritt Sobald eine Stabkraft null gesetzt worden ist, den Prozess abbrechen.*/
    public boolean optimiere(int gewünschteMaxStatUnbestimmtheit, boolean auchStrategie2Anwenden, boolean nureinSchritt) {
        assert gewünschteMaxStatUnbestimmtheit >= 0;
        if (gewünschteMaxStatUnbestimmtheit < 0) gewünschteMaxStatUnbestimmtheit = 0;

        boolean optimierungerfolgreichbeendet = false;

        final boolean OptionVorber = false;
        final boolean OptionGLS = true;
        final boolean OptionMechanismus = false; // zu langsam

        boolean keinWIDERSPRUCH = true;
        //boolean keinFEHLER = true;
        boolean VOLLSTÄNDIGGELÖST_OK = false;
        clFachwerk fw;

        boolean weiter_iterieren = true;
        boolean maxIterationenErreicht = false;
        int iteration = 0;
        int iterationSeitModelländerung = 0;
        double alteEnergie = -1;
        double[] energie = new double[1];
        boolean modellunverändert = false;
        do {
            iteration++;
            iterationSeitModelländerung++;

            if (!modellunverändert) { // wenn zwischenzeitlich Stäbe gesetzt wurden, neu berechnen!
                iterationSeitModelländerung = 0;
                try {
                    // Knotenstatus aller Stäbe zurücksetzen (Knoten 0 gibt es nicht)
                    for (int k = 1; k < Kn.length; k++) Kn[k].zurücksetzen();
                    for (int s = 1; s < St.length; s++) St[s].zurücksetzen(false); // setzt berechnete Stabkräfte zurück.
                    VOLLSTÄNDIGGELÖST_OK = false;
                    keinWIDERSPRUCH = true;
                    fw = new clFachwerk(Kn, St, Top);
                    fw.setVerbose(false);
                    fw.setQuiet(true);
                    keinWIDERSPRUCH = fw.rechnen(OptionVorber, OptionGLS, OptionMechanismus);
                    if (keinWIDERSPRUCH) {
                        VOLLSTÄNDIGGELÖST_OK = fw.istvollständiggelöst(false); // false, da resultatcheck() soeben in .rechnen() durchgeführt
                        statischeUnbestimmtheit = fw.getStatischeUnbestimmtheit();
                    
                        if (VOLLSTÄNDIGGELÖST_OK) { //statischeUnbestimmtheit = 0;
                            assert statischeUnbestimmtheit == 0 : "stat. Unbestimmtheit ungleich 0: " + statischeUnbestimmtheit;
                        } else {
                            double[][] vollstLsg = fw.getCompleteSolution();
                            assert vollstLsg != null : "[clAutomModellsuche.optimiere] vollstLst == null (stat.Unbesth. " + statischeUnbestimmtheit + ")";
                            setCompleteSolution(vollstLsg); // setzt auch super.statischeUnbestimmtheit
                        }
                    }
                    else { // Widerspruch
                        System.out.println("[clAutomModellsuche.optimiere] Widerspruch aufgetreten.");
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }


            if (statischeUnbestimmtheit > gewünschteMaxStatUnbestimmtheit && keinWIDERSPRUCH) {
                alteEnergie = energie[0];
                if (debug) System.out.println("Iteration " + iteration);

                // Berechnungsschritt
                modellunverändert = optimiereSchritt(energie);

                if (modellunverändert && Math.abs(energie[0] - alteEnergie) < TOL_finde && iterationSeitModelländerung >= minIterSeitModelländ) { // die Optimierung ist festgefahren
                    System.out.println("");
                    System.out.println("Optimierungsprozess festgefahren, Deformationsenergie konstant: " + energie[0] + " = " + alteEnergie);
                    
                    if (debug) {
                        for (int st = 1; st <= N.length; st++) System.out.println("N[Stab " +st + "] = " + N[st-1]);
                    }
                    
                    boolean reduktionsprozessAbbrechen = false;

                    if (auchStrategie2Anwenden) {
                        // Strategie:
                        // Die kleinste (ungesetzte) Stabkraft zu Null setzen
                        double Nmin = Double.MAX_VALUE;
                        int stabmin = -1;
                        for (int st = 1; st <= N.length; st++) {
                            if (St[st].getStatus() == UNBEST && Math.abs(N[st - 1]) < Nmin) {
                                stabmin = st;
                                Nmin = Math.abs(N[st - 1]);
                            }
                        }

                        if (stabmin > 0) {
                            St[stabmin].zurücksetzen(false);
                            St[stabmin].setKraft(GESETZT, 0);
                            for (int i = 0; i < EA.length; i++) EA[i] = EAzug[i]; // wahrscheinlich nicht nötig TODO testen
                            modellunverändert = false; // erzwingt eine Neuberechnung
                            alteEnergie = -1;
                            System.out.println("Alternativer Reduktionsprozess angewendet.");
                        }
                        else reduktionsprozessAbbrechen = true;
                    }
                    else reduktionsprozessAbbrechen = true;

                    if (reduktionsprozessAbbrechen) {
                        weiter_iterieren = false;
                        System.out.println("Automatischer Reduktionsprozess abgebrochen.");
                        System.out.println("Bitte von Hand ueberzaehlige Staebe zu Null setzen.");
                    }
                }
            }
            else {
                weiter_iterieren = false;
                if (keinWIDERSPRUCH) optimierungerfolgreichbeendet = true;
            }

            if (nureinSchritt && !modellunverändert) weiter_iterieren = false; // mind. ein Stab wurde gesetzt --> automatischen Prozess abbrechen

            // Abbruchkriterien
            if (iteration >= maxIterationen || iterationSeitModelländerung >= maxIterationenSeitModelländerung) {
                maxIterationenErreicht = true;
                weiter_iterieren = false;
            }
        } while (weiter_iterieren);
        
        System.out.println();
        System.out.println("Anzahl Iterationen: " + iteration);
        if (maxIterationenErreicht) {
            System.out.println("Anzahl Iterationen seit letzter Modellreduktion: " + iterationSeitModelländerung);
            System.out.println("ABBRUCH durch Erreichen der maximalen Anzahl Iterationen!");
        }

        return optimierungerfolgreichbeendet;
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
        autom.optimiere(0, false, false);
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
