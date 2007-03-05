/*
 * clStab.java
 *
 * Created on 18. August 2003, 21:42
 */

package Fachwerk.statik;

import java.io.*;

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
public class clStab implements Serializable, inKonstante {
    
    /** Diese Nummer ist die Java-Checksumme dieser Klasse für Fachwerk-Dateien Version 0.1.
     *  Sie entstand bevor das Interface inKonstante verwendet wurde (für Attribute).
     */
    static final long serialVersionUID = 6763416452051689127L;
        
    /* Die folgenden Konstanzen sind im Interface inKonstante deklariert.
    private static final int UNBEST = 0;
    private static final int BER = 1;
    private static final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    private static final int NICHTSETZBAR = 4;
    */
    
    private int Status = UNBEST;
    private double Kraft = 0; // Zug positiv
    
    
    
    /** Creates a new instance of clStab */
    public clStab() {
    }
    
    /** Setzt die Stabkraft.
     * @param p_Status der neue Stabstatus: BER|GESETZT
     * @param p_Kraft die neue Stabkraft
     * @return true wenn ok, false sonst.
     */
    public boolean setKraft(int p_Status, double p_Kraft) {
        switch (p_Status) {
            case BER:
            case GESETZT:
                switch (Status) {
                    case UNBEST:
                    case GESETZT:
                        Status = p_Status;
                        Kraft = p_Kraft;
                        break;
                    case BER:
                        System.err.println("Programmfehler: clStab.setKraft: zuerst StabStatus zurücksetzen");
                        assert false : "BERECHNETER Stab kann nicht gesetzt werden";
                        break;
                    case NICHTSETZBAR:
                        System.out.println("NICHTSETZBAR !");
                    default:
                        assert false;
                }
                break;
                
            default: // Status falsch
                System.err.println("Programmfehler: clStab.setKraft");
                return false;
        }
        return true;
    }
    
    /** Setzt die Stabkraft zurück. Der Stab hat danach den Status UNBEST.
     * Es muss gewählt werden, ob auch gesetzte Stabkräfte zurückgesetzt werden sollen.
     * @param auchgesetzteKraft true falls der Stab auch dann zurückgesetzt werden soll, wenn der Stab GESETZT ist.
     */
    public void zurücksetzen(boolean auchgesetzteKraft) {
        switch (Status) {
            case GESETZT:
                if (!auchgesetzteKraft) break;
            default:
                Status = UNBEST;
                Kraft = 0;
        }
    }
    
    /** Gibt die Stabkraft zurück.
     * @return Stabkraft
     */
    public double getKraft() {
        switch (Status) {
            case BER:
            case GESETZT:
                return Kraft;
                //break; weder nötig noch erlaubt nach return
            default:
                System.err.println("Programmfehler: clStab.getKraft: Stabstatus falsch");
                assert false;
                return 0d;
        }
    }
    
    /** Gibt den Status des Stabes an: UNBEST| BER |GESETZT|NICHTSETZBAR
     * @return Stabstatus
     */
    public int getStatus() {
        return Status;
    }
    
}
