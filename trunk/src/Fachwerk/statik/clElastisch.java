/*
 * clElastisch.java
 *
 * Created on 1. März 2007, 20:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Fachwerk.statik;


import java.util.*;
import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;


/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
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
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 */
public class clElastisch implements inKonstante {
    
    /** Nulltoleranz */
    private final double TOL = inKonstante.TOL_gls;
    
    /** Vollständige Lösung, wie von [clFachwerk].getCompleteSolution() erhalten.
     Im Fall von Fachwerk2D enthalten die Zeilen die Stabkräfte (Stab 1 hat Zeilenindex 0).
     Spaltenindex 0: Einheit 1
     Spaltenindex i, i > 0: Parameter i (soviel mal Parameter=Unbekannte i)*/
    private double[][] vollstLsg;
    protected clStab[] St;
    protected int anzSt;
    protected int statischeUnbestimmtheit;
    
    /** Stabsteifigkeiten (relativ).
     * EA wird u.U. durch clElastisch oder abgeleitete Klassen verändert.
     Stab mit Index 0 gibt es nicht.*/
    protected double[] EA;

    /** Stabsteifigkeiten unter Zug (ungewichtete Grundsteifigkeit).
     * EAzug wird nicht verändert in clElastisch
     Stab mit Index 0 gibt es nicht.*/
    protected double[] EAzug;
    
    /** Stablängen
     Stab mit Index 0 gibt es nicht.*/
    protected double[] L;
    
    /** pro Gleichung (Zeile): 1 + x*Param1 + x*Param2 + .. */
    private double[][] elastGLS;
    
    /** berechnete Parameter.
     * Vorsicht: 1 Parameter hat Index 1, Index 0 ist immer 1! */
    private double[] X;
    
    /** gelöste Beanspruchungen, bei Fachwerk Stabkräfte.
     Vorsicht: Stab 1 hat Index 0 in N, Stab 2 Index 1, etc.*/
    protected double[] N;
    
    static final boolean debug = false; // TODO
    
    /** Creates a new instance of clElastisch */
    public clElastisch(clStab[] Staebe) {
        St = Staebe;
        anzSt = St.length - 1;
        EA = new double[anzSt+1];
        EAzug = new double[anzSt+1];
        for (int st = 1; st <= anzSt; st++) {
            EA[st] = 1; // setzt die Steifigkeit auf 1
            EAzug[st] = 1; // setzt die Steifigkeit auf 1
        }
        // Stablängen
        L = new double[1 + anzSt];
    }
    
    /** Setzt die Stablängen */
    public void setL(double[] L) {
        this.L = L;
    }
    
    /** Setzt die Stablängen */
    public void setL(clKnoten[] Kn, int[][] Top) {
        for (int von = 1; von < Kn.length; von++) {
            for (int bis = 1; bis < Kn.length; bis++) {
                int stabnr = Top[von][bis];
                if (stabnr > 0) {
                    double dx = Kn[bis].getX() - Kn[von].getX();
                    double dz = Kn[bis].getZ() - Kn[von].getZ();
                    L[stabnr] = Math.sqrt(dx*dx + dz*dz);
                }
            }
        }
    }
    
    /** Setzt die Stabsteifigkeiten. Optional (Voreinstellung: 1) */
    public void setEA(double[] EAzug) {
        this.EAzug = EAzug;
        for (int st = 1; st <= anzSt; st++) EA[st] = EAzug[st];
    }
    
    /** Setzt die analytische Lösung der Stabkräfte.
     Zeilen: Stabkräfte
     Spaltenindex 0: *1, Index 1: *Param_1, Index i: *Param_i */
    public void setCompleteSolution(double[][] vollstLsg) throws Exception {
        if (vollstLsg == null) {
            System.err.println("[clElastisch.setCompleteSolution] analytic solution is empty.");
            throw new IllegalArgumentException("analytic solution is empty");
        }
        this.vollstLsg = vollstLsg;
        
        statischeUnbestimmtheit = vollstLsg[0].length - 1;
        elastGLS = new double[statischeUnbestimmtheit][1+statischeUnbestimmtheit];
        
        // debug, zeigt das gelöste Gleichungssystem
//        if (debug) {
//            boolean RUNDEN = true;
//            System.out.println("");
//            System.out.println("vollst. Gleichgewichtsloesung: (Unbestimmtheit " + statischeUnbestimmtheit + ")");
//            for (int i = 0; i < vollstLsg.length; i++) {
//                double[] d = vollstLsg[i];
//                System.out.print("Gl.(" + Fkt.nf(i,2) + ") ");
//                if (RUNDEN) {
//                    for (int j = 0; j < d.length; j++) {
//                        System.out.print(Fkt.nf(d[j], 3, 4) + "; ");
//                    }
//                    System.out.println("");
//                }
//                else System.out.println(cern.colt.Arrays.toString(d));
//            }
//            System.out.println("");
//        }
    }
    
    /* startet die elastische Berechnung*/
    public void rechnen() {
        if (statischeUnbestimmtheit < 1) {
            System.out.println("statisch bestimmt, nichts zu tun");
            return;
        }
        
        for (int param = 1; param <= statischeUnbestimmtheit; param++) {
            elastGLS[param - 1] = erstelleGleichung(param);
        }
        
        X = löseGLS(elastGLS);
        N = einsetzen();
    }
    
    /* startet gewichtete elastische Berechnung, wobei EA(Druckstäbe) > EA(Zugstäbe) */
    public void rechnen(double EAdruck_zu_EAzug) { // TODO Reduktionsfaktor für schiefe Zugstäbe
        rechnen();
        
        double[] Nalt = new double[N.length];
        boolean weiter_iterieren = true;
        int maxIterationen = 20;
        int iteration = 1;
        do {
            iteration++;
            for (int i = 0; i < N.length; i++) Nalt[i] = N[i]; // Nalt = N
            // Steifigkeit setzen
            for (int st = 1; st < EA.length; st++) {
                if (N[st-1] < 0) EA[st] = EAdruck_zu_EAzug * EAzug[st];
                else EA[st] = EAzug[st];
            }
            // Nochmals rechnen
            rechnen();
            // Abbruchkriterien
            if (iteration >= maxIterationen) weiter_iterieren = false;
            
            boolean ZugDruck_unverändert = true;
            for (int i = 0; i < N.length; i++) {
                if (Fkt.signum(N[i]) != Fkt.signum(Nalt[i])) {
                    ZugDruck_unverändert = false;
                    break; // Schlaufe in N
                }
            }
            if (ZugDruck_unverändert) weiter_iterieren = false;
        } while (weiter_iterieren);
        
        // if (debug) System.out.println("Anzahl Iterationen: " + iteration);
    }
    
    /** Gibt die berechneten Parameter zurück. Vorsicht: 1 Parameter hat Index 1, Index 0 ist immer 1!*/
    private double[] löseGLS(double[][] GLS) {
        int anzGl = GLS.length;
        assert anzGl > 0;
        
        // so dass A*x = b
        SparseDoubleMatrix2D A = new SparseDoubleMatrix2D(anzGl, GLS[0].length - 1);
        DenseDoubleMatrix2D b = new DenseDoubleMatrix2D(anzGl, 1);
        
        for (int i = 0; i < GLS.length; i++) {  // Zeilen i
            for (int j = 1; j < GLS[0].length; j++) { // Spalten
                A.set(i, j-1,  GLS[i][j]);
            }
            b.set(i,0, -GLS[i][0]);
        }

        // prüfen, ob Matrix singulär
        double det = cern.colt.matrix.linalg.Algebra.DEFAULT.det(A);
        double[] X1 = new double[GLS[0].length];
        if (det != 0) {
            DoubleMatrix2D x = cern.colt.matrix.linalg.Algebra.DEFAULT.solve(A, b);
            X1[0] = 1;
            for (int i = 0; i < x.rows(); i++) {
                X1[i + 1] = x.get(i, 0);
            }
        } else { //singuläre Matrix A
            System.err.println("[clElastisch.löseGLS] Problem: Matrix singulär: ");
            System.err.println(A.toString());
            throw new IllegalArgumentException("[clElastisch] Matrix singular");
        }

        return X1;
    }
    
    /** Erstellt eine kinematische Bedingung (elastisch) für den Parameter.
     Basiert auf der gleichen Idee wie Castigliano, ∂Ni/∂Param ist ein stat.zul.Belastungszustand.
     0 =! u = ∑ (alle Stäbe i) Li * Ni/EAi * ∂Ni/∂Param
     @param par ParameterNr*/
    private double[] erstelleGleichung(int par) {
        double[] gl = new double[1+statischeUnbestimmtheit]; // Gleichung
        
        for (int st = 1; st <= anzSt; st++) {
            double faktor = vollstLsg[st-1][par]; // = ∂Ni/∂Param
            if (faktor == 0) continue;
            faktor *= L[st] / EA[st];
            
            for (int j = 0; j <= statischeUnbestimmtheit; j++) {
                gl[j] += faktor * vollstLsg[st-1][j];
            }
        }
        
        
        return gl;
    }
    
    private double[] einsetzen() {
        double[] stabkräfte = new double[vollstLsg.length];
        for (int st = 1; st <= anzSt; st++) {
            for (int j = 0; j <= statischeUnbestimmtheit; j++) {
                stabkräfte[st-1] += X[j] * vollstLsg[st-1][j];
            }
        }
        return stabkräfte;
    }
    
    /** Schreibt die elastische Lösung auf die Konsole.*/
    public void resultatausgabe_direkt() {
        if (debug) {
            System.out.println("");
            for (int j = 1; j <= statischeUnbestimmtheit; j++) {
                System.out.println("Parameter " + j + " : " + Fkt.nf(X[j], 3, 4));
            }
            System.out.println("");
        }
        
        System.out.println("");
        System.out.println("Elastische Loesung:");
        for (int i = 0; i < N.length; i++) {
            System.out.println("Stab " + Fkt.nf(i+1, 2) + ": N = " + Fkt.nf(N[i], 3, 4) + " kN");
        }
    }
    
    /** Gibt die elastische Lösung für die Beanspruchungen (Stabkräfte) zurück. */
    public double[] getLsg() {
        return N;
    }
    
    /** Gibt den Index der Unbekannten an, die gesetzt werden können.
     * In der Regel zufällige Auswahl.
     * Anzahl = statische Unbestimmtheit. */
    public int[] getIndexMgdUmbek() {
        int[] mgdUnbek = new int[statischeUnbestimmtheit];
        double mgdchecksumme = Double.POSITIVE_INFINITY; // zur Auswahl, welche Kombination gewählt wird.
        
        /** Liste, welche für jeden Parameter angibt, welche Stabkraft von ihm abhängt. */
        int[][] Abhängigkeitsliste = new int[statischeUnbestimmtheit][];
        HashSet aktSet; // TODO Stack schneller
        for (int param = 1; param <= statischeUnbestimmtheit; param++) {
            aktSet = new HashSet();
            for (int i = 0; i < anzSt; i++) {
                if (Math.abs(vollstLsg[i][param]) > TOL) { // d.h. ungleich 0
                    aktSet.add(new Integer(i));
                }
            }
            Abhängigkeitsliste[param-1] = new int[aktSet.size()];
            int i = 0;
            for (Iterator it = aktSet.iterator(); it.hasNext();) {
                Abhängigkeitsliste[param-1][i] = ((Integer) it.next()).intValue();
                i++;
            }
        }
        /*// debug
        if (debug) {
            for (int param = 1; param <= statischeUnbestimmtheit; param++) {
                System.out.println("");
                System.out.print("Parameter " + param + ":  ");
                for (int i = 0; i < Abhängigkeitsliste[param-1].length; i++) {
                    System.out.print(Abhängigkeitsliste[param-1][i] + ", ");
                }
            }
            System.out.println("");
        }
        */
        
        // Vorselektion um weniger Kombinationen erstellen zu müssen.
        aktSet = new HashSet();
        for (int i = 0; i < anzSt; i++) {
            int anzAbh = 0;
            for (int param = 1; param <= statischeUnbestimmtheit; param++) {
                if (Math.abs(vollstLsg[i][param]) > TOL) { // d.h. ungleich 0
                    anzAbh++;
                }
            }
            if (anzAbh == 1) {
                if (aktSet.add(new Integer(i))) { // noch nicht vorhanden
                    for (int param = 1; param <= statischeUnbestimmtheit; param++) {
                        if (Math.abs(vollstLsg[i][param]) > TOL) {
                            Abhängigkeitsliste[param-1] = new int[1];
                            Abhängigkeitsliste[param-1][0] = i;
                            break;
                        }
                    }
                }
            }
        }
        
        
        // Kombinationen erstellen                                      // TODO sehr ineffizient!
        int[] zeiger = new int[statischeUnbestimmtheit];
        int durchgänge = 0;
        while (zeiger[0] < Abhängigkeitsliste[0].length) {
            // weitere Abbruchkriterien
            if (mgdchecksumme <= 2*statischeUnbestimmtheit) break; // Idealkombination gefunden.
            if (durchgänge > 200 && mgdchecksumme <= 10*statischeUnbestimmtheit) break;
            if (durchgänge > 1000 && mgdchecksumme < Double.POSITIVE_INFINITY) break;
            
            // debug
            if (debug && durchgänge % 10000 == 0) System.out.println("Durchgang " + durchgänge + " Checksumme: " + mgdchecksumme);
            
            aktSet = new HashSet();
            double checksumme = 0;
            for (int i = 0; i < Abhängigkeitsliste.length; i++) {
                aktSet.add(new Integer(Abhängigkeitsliste[i][zeiger[i]]));
                double aktwert = vollstLsg[Abhängigkeitsliste[i][zeiger[i]]][i+1];
                checksumme += Math.abs(aktwert) + 1d / Math.abs(aktwert);
            }
            // Prüfen, ob alle Parameter nur einfach vorkommen, sonst keine brauchbare Kombination.
            if (aktSet.size() == statischeUnbestimmtheit) {
                if (checksumme < mgdchecksumme) {
                    int p = 0;
                    for (Iterator it = aktSet.iterator(); it.hasNext();) {
                        mgdUnbek[p] = ((Integer) it.next()).intValue();
                        p++;
                    }
                    mgdchecksumme = checksumme;
                }
            }
            int zeigerindex = statischeUnbestimmtheit-1;
            while (zeigerindex >=0) {
                zeiger[zeigerindex] ++;
                if (zeiger[zeigerindex] < Abhängigkeitsliste[zeigerindex].length) break; // innere while Schlaufe
                // else
                if (zeigerindex > 0) zeiger[zeigerindex] = 0; // sonst bricht die übergeordnete while Schlaufe nie ab.
                zeigerindex--;
            }
            durchgänge++;
        }
        
        // TODO suchen mit Zufallsmethode, bis eine Kombination gefunden.
        
        java.util.Arrays.sort(mgdUnbek);
        
        /*// debug
        if (debug) {
            System.out.println("");
            System.out.println("unabhaengige Staebe (Index): ");
            for (int i = 0; i < mgdUnbek.length; i++) {
                System.out.print(mgdUnbek[i] + ", ");
            }
            System.out.println("");
        }
        */
        
        return mgdUnbek;
    }
    
    
    /**
     * Zu Testzwecken
     * und um zu zeigen, wie diese Klasse genutzt werden kann.
     */
    public static void main(String[] args) throws Exception {
        clKnoten[] testKn;
        clStab[] testSt;
        int [][] testTop;
        
        double EAdruck_zu_EAzug = 10;
        
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
        
        clElastisch elast = new clElastisch(testSt);
        elast.setL(testKn, testTop);
        elast.setCompleteSolution(fw.getCompleteSolution());
        elast.rechnen(EAdruck_zu_EAzug);
        elast.resultatausgabe_direkt();
        double[] N = elast.getLsg();
        int[] zusetzendeSt = elast.getIndexMgdUmbek();
        System.out.println("");
        System.out.println("Tip: zu setzende Staebe (quasielastisch berechnet)");
        for (int i = 0; i < zusetzendeSt.length; i++) {
            System.out.println("Stab " + Fkt.nf(zusetzendeSt[i]+1,2) + ": N = " + Fkt.nf(N[zusetzendeSt[i]],1,4) + " kN");
        }
        
        // Setzten der quasi-elastisch berechneten Stabkräfte
        for (int i = 0; i < zusetzendeSt.length; i++) {
            assert testSt[zusetzendeSt[i]+1].getStatus() == UNBEST: "Stab der gesetzt werden soll ist gar nicht unbestimmt!";
            testSt[zusetzendeSt[i]+1].setKraft(GESETZT, N[zusetzendeSt[i]]);
        }
        fw = new clFachwerk(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();
        fw.istvollständiggelöst(true);
    }
    
}
