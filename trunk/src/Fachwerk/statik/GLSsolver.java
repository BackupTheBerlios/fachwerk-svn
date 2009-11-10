/*
 * GLSsolver.java
 *
 * Created on 26. August 2003, 22:09
 */

package Fachwerk.statik;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;



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
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 */
public final class GLSsolver {
    
    /* ZUTUN
     * - Beim Entdecken von Widersprüchen ursprüngliche Zeile und so betroffenen Knoten angeben
     * - In Klasse Infos zum Berechnungsablauf speichern, insb. Fehler
     * - Methoden einbauen, welche Infos zu Fehlern und Widersprüchen und Warnungen liefern.
     * - Colt Methoden in separatem Thread laufen lassen
     */
    
    // --------------------------------------
    // Variablendeklararion und EINSTELLUNGEN
    // --------------------------------------
    
    private DenseDoubleMatrix2D A;
    private DoubleMatrix2D R;
    private DoubleMatrix2D c;
    
    private double[][] xLsg; // Lösung x: nur eindeutige (d.h. parameterunabhängige xi) werden zurückgegeben
    // index 0: Wert = 0 bedeutet xi unbestimmt,  Wert = 1 bedeutet xi bestimmt
    // index 1: eigentlicher Wert (nur wenn xi bestimmt, dh index 0 = 1, sonst Wert 0)
    
    private double[][] x; // vollständige Lösung x: bei unbestimmten xi werden die Abhängigkeiten von den unbek. Parametern angegeben.
    // Status1 (bestimmt), kN, alpha, beta (Parameter)
    
    private int anzUnbestParam; // Anzahl unbestimmter Parameter (d.h. Anz. fehlende unabh. Gl.)
    
    private double TOL = inKonstante.TOL_gls; // double TOL = 1E-10;
    // womöglich wird der Toleranzcheck mittels der in colt eigebauten Toleranz über cardinality
    // durchgeführt. Ist vorteilhaft, da der Rang des GLS mit dieser internen Toleranz bestimmt wird.
    // dieser Wert muss grösser (ev. >=) sein als in clFachwerk zB +E-11
    
    public boolean debug = false;
    private boolean solved = false;
    
        
    
    /** Creates a new instance of testmatrix */
    public GLSsolver(double[][] p_MatrixgleichNull) throws IllegalArgumentException {
        
        // --------------------------------------
        // Kontrolle, ob Eingabematrix rechteckig
        // --------------------------------------
        
        int nplus1 =  p_MatrixgleichNull[0].length;
        for (int i = 1; i < p_MatrixgleichNull.length; i++) {  // Zeilen i
            if  (p_MatrixgleichNull[i].length != nplus1) {
                System.err.println("Programmfehler: Matrix des GLS ist nicht rechteckig! (im solver entdeckt)");
                throw new IllegalArgumentException();
            }
        }
        if (nplus1 <=1) throw new IllegalArgumentException("keine Unbekannte"); // keine Unbekannte!!!
        
        // Umgeht einen Fehler in der colt-Bibliothek // TODO wenn behoben, Workaround entfernen
        // ------
        int anzGl = p_MatrixgleichNull.length;   
        if (anzGl < nplus1-1) { // anzGleichungen < anz Unbekannte
            if (debug) System.out.println("WorkAround fuer Fehler in colt: 0 = 0 Gleichungen anhaengen");
            anzGl = nplus1-1; // = Anzahl Unbek, 0 0 0 ... 0 = 0 Zeile angehängt
        }
        
        // -------------------------
        // Daten in A und b einlesen
        // -------------------------
        
        // so dass A*x = b
        A = new DenseDoubleMatrix2D(anzGl, (nplus1-1));
        DenseDoubleMatrix2D b = new DenseDoubleMatrix2D(anzGl, 1);
        
        for (int i = 0; i < p_MatrixgleichNull.length; i++) {  // Zeilen i
            for (int j = 0; j < nplus1 - 1; j++) { // Spalten
                A.set(i, j,  p_MatrixgleichNull[i][j]);
            }
            b.set(i,0, -p_MatrixgleichNull[i][nplus1-1]);
        }
        
        if (debug) {
            System.out.println(" A = " + A.toString());
            System.out.println(" b = " + b.toString());
            System.out.println("");
        }

        // TODO separater Thread, damit Berechnung abbrechbar


        // --------------
        // LR - Zerlegung
        // --------------
        
        LUDecomposition ALU = new LUDecomposition(A);
        if (debug) System.out.println(ALU.toString());
        
        DoubleMatrix2D L = ALU.getL();
        R = ALU.getU();
        int[] piv = ALU.getPivot();
        
        
        Algebra alg = new Algebra();
//        if (debug) System.out.println("L = " + L.toString());
//        if (debug) System.out.println("Kontrolle L*R = " + alg.mult(L,R).toString());
//        if (debug) System.out.println("Kontrolle P*b = " + alg.permute(b, piv, null) );
//        
//        if (debug) System.out.println("Rx = c: R = " + R.toString());
//        if (debug) System.out.println("alg.permute(b, piv, null) = " + alg.permute(b, piv, null).toString());
        
        c = alg.solve(L,  alg.permute(b, piv, null)); // TODO: kann zu Problemen führen, 
                                                                     // wenn weniger Gleichungen als Unbek --> s.Workaround oben
        
        if (debug) System.out.println("Lc = Pb:  c = " + c.toString());
        
        if (debug) {
            System.out.println("Rang A: " + alg.rank(A));
            System.out.println("Rang R: " + alg.rank(R));
        }
        
        if (debug) { // sehr langsame Operation
            assert (alg.rank(A) == alg.rank(R)) : "Rang von A ungleich Rang von R --> Programmfehler";
            // getestet: Berechnung rank(A) minimal schneller als rank(R)
        }
        anzUnbestParam = A.columns() - alg.rank(A);
        if (debug) System.out.println("Anz unbest Parameter: " + anzUnbestParam);
    }
    

    /* Überschreibt die standardmässig gesetzte Null-Toleranz inKonstante.TOL_gls.
     * @param toleranz Werte kleiner als toleranz werden bei Kontrollen als Null angesehen*/
    public void setTol(double toleranz) {
        assert toleranz > 0;
        assert toleranz < 1E-5;
        TOL = toleranz;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  // Beispiel aus Stoffer,1994,"Lineare Algebra" S.49
        double[][] gls = new double[7][6];
        gls[0][0] =  2; gls[0][1] = -1; gls[0][2] =  3; gls[0][3] = -1; gls[0][4] =  1;
        gls[1][0] =  2; gls[1][1] = -1; gls[1][2] =  3; gls[1][3] =  0; gls[1][4] = -1;
        gls[2][0] = -4; gls[2][1] =  2; gls[2][2] = -4; gls[2][3] =  5; gls[2][4] = -5;
        gls[3][0] =  0; gls[3][1] =  0; gls[3][2] = -2; gls[3][3] =  2; gls[3][4] = -7;
        gls[4][0] = -2; gls[4][1] =  1; gls[4][2] = -1; gls[4][3] =  0; gls[4][4] =  4;
        // Zeile 5: alles Nullen
        // Zeile 6: selbe Gleichung wie Zeile 0
        gls[6][0] =  2; gls[6][1] = -1; gls[6][2] =  3; gls[6][3] = -1; gls[6][4] =  1;
        
        
        gls[0][5] =  2; gls[1][5] =  3;gls[2][5] = -3; gls[3][5] =  4; gls[4][5] = -5; 
        // Zeile 5: Null, Zeile 6: wie Zeile 0
        gls[6][5] =  2;
        
        GLSsolver myglssolver = new GLSsolver(gls);
        myglssolver.debug = true;
        myglssolver.solve();
    }
    
    /** Gibt die Lösung x des Gleichungssystems zurück: nur eindeutige (d.h. parameterunabhängige xi) werden zurückgegeben.
                 index 0: Wert = 0 bedeutet xi unbestimmt,  Wert = 1 bedeutet xi bestimmt
                 index 1: eigentlicher Wert (nur wenn xi bestimmt, dh index 0 = 1, sonst Wert 0)
     */
    public final double[][] solve() throws ArithmeticException {
        
        // --------------------------------------------------------------------------
        // EIGENTLICHER SOLVER für bestimmte Lösungsvariablen in unbestimmen Systemen
        // --------------------------------------------------------------------------
        
        int gebrauchteUnbestParam = 0;
        x = new double[A.columns()][2 + anzUnbestParam]; // Status 1 (bestimmt), kN, alpha, beta (Parameter)
        
        int z = A.rows()-1; // Zeilenvariable, beginnt zuunterst
        
        // Gleichungen mit lauter Nullen
        while (R.viewRow(z).cardinality() == 0 // nachfolgende Tests massgebend, dieser jedoch schnell
        || (Fkt.max(R.viewRow(z).toArray()) < TOL && Fkt.min(R.viewRow(z).toArray()) > -TOL)) {
            double cwert;
            if (z < c.rows()) cwert = c.get(z, 0);
            else cwert = 0;
            if (Math.abs(cwert) > TOL) {
                System.out.println("widersprüchliche Gleichungen im System! Zeile "+z);
                throw new ArithmeticException("Widerspruch im Gleichungssystem!");
            }
            z--;
            if (z <= 0) {
                System.out.println("lauter Nullen im GLS");
                break;
            }
        }
        
        // Verarbeiten der Gleichungen (von unten her)
        for (z = z; z >= 0; z--) {
            // finde erste nicht-Null in Zeile (Pivot)
            int p = -1 ; // Pivot: erste Zahl welche nicht null ist
            pivotfinden:
            for (int i = 0; i < R.columns(); i++) {
                if (Math.abs(R.get(z,i)) > TOL) { // Versuch, numerische Probleme (Überbestimmtheit) zu vermeiden
                    p = i;
                    break pivotfinden;
                }
            }
            
            // Fall Kein Pivot gefunden (d.h. linker Teil der Gleichung aus lauter Nullen)
            if (p < 0) {
                if (debug ) System.out.println("Warnung: kein Pivot gefunden in Zeile " + z);
                // Kontrolle, ob rechte Seite (c) auch null --> ok, sonst Widerspruch im GLS
                if (Math.abs(c.get(z, 0)) > TOL) {
                    System.out.println("widersprüchliche Gleichungen im System! Zeile "+z);
                    throw new ArithmeticException("Widerspruch im Gleichungssystem!");
                }
                else {
                    if (debug) System.out.println("Entwarnung: Zeile " + z + " besteht aus lauter Nullen (ok)");
                    continue;
                }
            }
            
            // kontrollieren, ob es in der Gleichung (Zeile) eine neue Unbestimmte Variable (i.d.R. Pivot) hat.
            boolean alleVarBestimmt = true;
            int effPivot = p; // effektiver Pivot (1. Unbestimmte Variable der Zeile), i.d.R. Pivot
            for (int i = p; i < R.columns(); i++) {
                if (x[i][0] == 0 && Math.abs(R.viewRow(z).get(i)) > TOL) {
                    alleVarBestimmt = false;
                    effPivot = i; // i.d.R. effPivot=p, aber nicht immer.
                    break;
                }
            }
            
            if (alleVarBestimmt) { // alle Variablen (inkl.Pivot) schon bestimmt! 
                // CHECKEN, ob (Zeile "+z+") nicht widersprüchlich
                double[] kontrolle = new double[1 + anzUnbestParam];
                for (int j = 0; j < kontrolle.length; j++) kontrolle[j] = 0;
                for (int i = p; i < R.columns(); i++) {
                    for (int j = 0; j < kontrolle.length; j++) {
                        kontrolle[j] += R.viewRow(z).get(i) * x[i][j+1];
                    }
                }
                kontrolle[0] -= c.get(z,0);
                
                // TODO TESTEN!
                boolean alleParamNull = true;
                int bekParam = -1; // Parameter der aus der Gleichung bestimmt werden kann.
                for (int j = kontrolle.length - 1; j > 0; j--) {
                    if (Math.abs(kontrolle[j]) > TOL) {
                        alleParamNull = false;
                        if (bekParam < 0) bekParam = j;
                    }
                }
                // Überprüfen, ob Gleichung widersprüchlich ist
                if (alleParamNull) { // TODO ev. nochmals prüfen ob alle 0 mit geringerer Toleranz (Problem fastNull*Param ≠ 0 könnte bedeuten dass Param = 0). Zumindestens wenn noch Parameter zu vergeben.
                    double obnull = Math.abs(kontrolle[0]);
                    if (obnull > TOL) { // TODO eventuell sogar weniger streng prüfen (Faktor 2-10).
                        System.out.println("");
                        System.out.println("Widerspruch im Gleichungssystem! (Zeile "+z+") " + obnull +" ungleich 0"); // TODO: URSPRÜNGLICH ZEILE (piv) ANGEBEN!
                        System.out.println("eventuell numerisches Problem");
                        throw new ArithmeticException("Widerspruch im Gleichungssystem!");
                    }
                    else continue; // nächste Gleichung
                }
                // else
                // Ein schon vergebener Parameter kann ausgerechnet werden
                
                // Schlaufe über bisherige Lösung
                assert bekParam > 0; 
                for (int xi = 0; xi < x.length; xi++) {
                    double faktor = x[xi][1+bekParam];
                    if (Math.abs(faktor) < TOL) continue;
                    // Einsetzen
                    assert x[xi][0] > 0; // bestimmt
                    for (int j = 0; j < kontrolle.length; j++) {
                        if (j != bekParam) {
                            x[xi][j+1] += -kontrolle[j] * faktor / kontrolle[bekParam];
                        }
                    }
                }
                for (int xi = 0; xi < x.length; xi++) {
                    // Parameter nachrutschen
                    if (bekParam < anzUnbestParam) { // d.h. nicht der letzte zu vergebende Parameter.
                        for (int j = bekParam; j < anzUnbestParam; j++) {
                            x[xi][j+1] = x[xi][j+2];
                            x[xi][j+2] = 0;
                        }
                    }
                    else x[xi][bekParam+1] = 0;
                }
                if (debug) System.err.println("VORSICHT, wenig GETESTETES Modul des Solvers im Einsatz."); // TODO Warnung entfernen, da vermutlich i.O.
                gebrauchteUnbestParam --;
            }
            
            // Normalfall, unbestimmter (effektiver) Pivot vorhanden
            else {
                
                // unbekannte
                x[effPivot][1] = c.get(z,0) / R.viewRow(z).get(effPivot);
                for (int i = R.columns()-1; i >= p; i--) { // R.Spalten, da dies AnzUnbek x entspricht
                    if (i == effPivot) continue;
                    if (x[i][0] == 0) { // unbestimmt, aber nicht Pivot
                        if (Math.abs(R.viewRow(z).get(i)) > TOL ) { // TODO testen!!!
                            if (gebrauchteUnbestParam >= anzUnbestParam) {
                                System.err.println("Programmfehler in solver: gebrauchteUnbestParam >= anzUnbestParam");
                                throw new AssertionError("Programmfehler in solver: gebrauchteUnbestParam >= anzUnbestParam");
                            }
                            x[i][gebrauchteUnbestParam + 2] = 1; // neuer Parameter (alpha, beta) setzen
                            x[i][0] = 1; // bestimmt (auch wenn von Parameter abhängig).
                            
                            gebrauchteUnbestParam++;
                        }
                    }
                    
                    x[effPivot][1] += -R.viewRow(z).get(i) * x[i][1] / R.viewRow(z).get(effPivot);
                    for (int j = 0; j < gebrauchteUnbestParam; j++) {
                        x[effPivot][2+j] += -R.viewRow(z).get(i) * x[i][2+j] / R.viewRow(z).get(effPivot);
                    }
                }
                x[effPivot][0] = 1;
            }
        }   
        
        
        if (debug) {
            System.out.println("");
            for (int i = 0; i < x.length; i++) {
                System.out.print("x"+i+" = " + Fkt.nf(x[i][1],3));
                for (int j = 2; j < x[i].length; j++) {
                    System.out.print(", P" + (j-1) + " = " + Fkt.nf(x[i][j],3));
                }
                System.out.println("");
            }
        }
        
        // ------------------
        // Lösung zurückgeben
        // ------------------
        
                // Lösung x: nur eindeutige (d.h. parameterunabhängige xi) werden zurückgegeben
                // index 0: Wert = 0 bedeutet xi unbestimmt,  Wert = 1 bedeutet xi bestimmt
                // index 1: eigentlicher Wert (nur wenn xi bestimmt, dh index 0 = 1, sonst Wert 0)
        xLsg = new double[R.columns()][2];
        
        for (int i = 0; i < x.length; i++) {
            boolean bestimmt;
            if (x[i][0] > 0) {
                bestimmt = true;
                // schauen, ob Lösungsvariable xi bestimmt, dh unabhängig von überzähligen Parametern
                for (int j = 2; j < x[i].length; j++) {
                    if (Math.abs(x[i][j]) > TOL) bestimmt = false;
                }
            }
            else bestimmt = false;
            
            if (bestimmt) {
                xLsg[i][0] = 1;
                xLsg[i][1] = x[i][1];
            }
            else xLsg[i][0] = 0;
        }
        
        solved = true;
        return xLsg;
    }
    
    /** Gibt die vollständige Lösung des Gleichungssystems zurück.
    index 0: Wert = 0 bedeutet xi unbestimmt,  Wert = 1 bedeutet xi bestimmt
    index 1: eigentlicher Wert (wenn xi bestimmt, dh index 0 = 1), sonst Wert ohne den Anteil der Unbekannten)
    index 2..Ende: Parameter*Unbekannte */
    public double[][] getCompleteSolution() {
        assert solved: "ERROR: Call solve() first!";
        return x;
    }
    
    /** @return Anzahl unbestimmter Parameter. D.h. Anzahl fehlender unabhängiger Gleichungen.*/
    public int getAnzUnbestParam() {
        return anzUnbestParam;
    }
}
