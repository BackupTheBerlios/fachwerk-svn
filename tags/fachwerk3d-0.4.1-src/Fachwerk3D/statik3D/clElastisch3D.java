/*
 * clElastisch3D.java
 *
 * Created on 1. März 2007, 20:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Fachwerk3D.statik3D;


import Fachwerk.statik.clStab;
import Fachwerk.statik.Fkt;


/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2007 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clElastisch3D extends Fachwerk.statik.clElastisch {
    
    
    /**
     * Creates a new instance of clElastisch3D
     */
    public clElastisch3D(clStab[] Staebe) {
        super(Staebe);
    }
    
    /** Setzt die Stablängen */
    public void setL(clKnoten3D[] Kn, int[][] Top) {
        for (int von = 1; von < Kn.length; von++) {
            for (int bis = 1; bis < Kn.length; bis++) {
                int stabnr = Top[von][bis];
                if (stabnr > 0) {
                    double dx = Kn[bis].getX() - Kn[von].getX();
                    double dy = Kn[bis].getY() - Kn[von].getY();
                    double dz = Kn[bis].getZ() - Kn[von].getZ();
                    L[stabnr] = Math.sqrt(dx*dx + dy*dy + dz*dz);
                }
            }
        }
    }
    
    
    /**
     * Zu Testzwecken
     * und um zu zeigen, wie diese Klasse genutzt werden kann.
     */
    public static void main(String[] args) throws Exception {
        clKnoten3D[] testKn;
        clStab[] testSt;
        int [][] testTop;
        
        double EAdruck_zu_EAzug = 10;
        
        int bsp = 2;
        if (args.length > 0) bsp = Integer.parseInt(args[0]);
        switch (bsp) {
            case 2:
                testKn = new clKnoten3D[1+4];
                testSt = new clStab[1+3];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten3D(0,0,0);
                testKn[2] = new clKnoten3D(0,0,-2);
                testKn[3] = new clKnoten3D(0,0,4);
                testKn[4] = new clKnoten3D(0,0,6);
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
                testKn[1].setLast(0,0,100d);
                
                break;
            
            
            case 1:
                testKn = new clKnoten3D[1+3];
                testSt = new clStab[1+2];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten3D(0,0,0);
                testKn[2] = new clKnoten3D(0,0,5);
                testKn[3] = new clKnoten3D(0,0,8);
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
                testKn[2].setLast(0,0,100d);
                
                break;
            
            case 0:
            default:
                testKn = new clKnoten3D[1+ 7];
                testSt = new clStab[1 +11];
                testTop = new int[testKn.length][testKn.length];
                
                testKn[1] = new clKnoten3D(0,0,6);
                testKn[2] = new clKnoten3D(4,0,2);
                testKn[3] = new clKnoten3D(4,0,6);
                testKn[4] = new clKnoten3D(9,0,2);
                testKn[5] = new clKnoten3D(9,0,6);
                testKn[6] = new clKnoten3D(13,0,2);
                testKn[7] = new clKnoten3D(13,0,6);
                
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
                testKn[1].setLast(0,0,100d);
        }
        
        
        clFachwerk3D fw = new clFachwerk3D(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();
        
        clElastisch3D elast = new clElastisch3D(testSt);
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
        fw = new clFachwerk3D(testKn, testSt, testTop);
        fw.setVerbose(true);
        fw.rechnen(true,true,true);
        fw.resultatausgabe_direkt();
        fw.istvollständiggelöst(true);
    }
    
}
