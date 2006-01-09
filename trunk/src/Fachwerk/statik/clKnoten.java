/*
 * clKnoten.java
 *
 * Created on 18. August 2003, 21:41
 */

package Fachwerk.statik;
import java.io.*;
import java.math.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2005 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clKnoten implements Serializable, inKonstante {
    
    /** Diese Nummer ist die Java-Checksumme dieser Klasse für Fachwerk-Dateien Version 0.1.
     *  Sie entstand bevor das Interface inKonstante verwendet wurde (für Attribute).
     */
    static final long serialVersionUID = -279231470735727569L;
    
    /* Die folgenden Konstanzen sind im Interface inKonstante deklariert.
    // Lagerattribute
    private static final int LOS = 0;
    private static final int FIX = 2;
    private static final int VERSCHIEBLICH = 1;
    // Stab- und Lagerattribute
    private static final int UNBEST = 0;
    private static final int BER = 1;
    private static final int GESETZT = 2; // für Lager nicht implementiert
    //private static final int WIDERSPR = 3;
    private static final int NICHTSETZBAR = 4;
    // Knotenattribute
    private static final int OFFEN = 0;
    private static final int FERTIG = 1;
    private static final int WIDERSPRUCH = 3;
    */
    
    private double X, Z; // Koordinaten (x nach rechts, z nach unten)
    
    private double Rx = 0, Rz = 0; // Auflagerkraft
    private double Ralpha = 0;     // Lagerrichtung bei verschieblichem Lager (Rtg der Verschiebung)
    private double Lx = 0, Lz = 0; // Knotenlast
    private int Lagerbed = LOS;
    private int Lagerstatus = GESETZT; // da LOS, TODO unklar, stiftet Verwirrung
    private int Knotenstatus = OFFEN;
    
    private final double TOL = 1E-12;
    
    /** Creates a new instance of clKnoten */
    public clKnoten(double p_x, double p_z) {
        X = p_x;
        Z = p_z;
    }
    
    /** Koordinaten setzen */
    public void setNeueKoord(double x, double z) {
        X = x;
        Z = z;
        Knotenstatus = OFFEN;
    }
    
    public double getX() {
        return X;
    }
    
    public double getZ() {
        return Z;
    }
    
    /** Lagerbedingung setzen.
     * @param p_Lagerbed Lagerbedingung: LOS|FIX (nicht jedoch VERSCHIEBLICH!)
     */
    public void setLager(int p_Lagerbed) {
        if (p_Lagerbed == LOS) {
            Lagerbed = p_Lagerbed;
            Rx = 0; Rz = 0; Ralpha = 0;
            Lagerstatus = GESETZT;
        }
        else if (p_Lagerbed == FIX) {
            Lagerbed = p_Lagerbed;
            Rx = 0; Rz = 0; Ralpha = 0; // zurücksetzten der Werte, allerdings ohne Bedeutung, nur um Progfehler zu erkennen
            Lagerstatus = UNBEST;
        }
        else {
            System.err.println("Programmfehler: clKnoten.setLager");
            assert false;
        }
    }
    
    /** Bei verschieblichen Lagern die Gleitrichtung setzen.
     * @param p_Lagerbed Lagerbedingung: zwingend VERSCHIEBLICH!
     * @param p_Ralpha Gleitrichtung (in Altgrad)
     */
    public void setLager(int p_Lagerbed, double p_Ralpha) {
        if (p_Lagerbed == VERSCHIEBLICH) {
            Lagerbed = p_Lagerbed;
            Ralpha = p_Ralpha;
            Lagerstatus = UNBEST;
        }
        else {
            System.err.println("Programmfehler: clKnoten.setLager(verschieblich)");
            assert false;
        }
    }
    
    /** Knotenlast setzen. */
    public void setLast(double p_Lx, double p_Lz) {
        Lx = p_Lx;
        Lz = p_Lz;
    }
    
    public double getLx() {
        return Lx;
    }
    
    public double getLz() {
        return Lz;
    }
    
    /** Gibt die Lagerbedingung an.
     * @return Lagerbedingung: LOS|FIX|VERSCHIEBLICH
     */
    public int getLagerbed() {
        return Lagerbed;
    }
    
    /** Gibt den Status des Lagers an.
     * @return Lagerstatus: UNBEST|BER|GESETZT
     */
    public int getLagerstatus() {
        return Lagerstatus;
    }
    
    /** Setzt den Status des Lagers.
     * @param p_Lagerstatus: UNBEST|BER|GESETZT
     */
    void setKnotenstatus(int p_Knotenstatus) {
        Knotenstatus = p_Knotenstatus;
    }
    
    /** Prüft, ob der Knotenstatus OFFEN ist.
     * @return true falls OFFEN, false ansonsten (FERTIG|WIDERSPRUCH)
     */
    public boolean istOffen() { // Knotenstatus
        if (Knotenstatus == OFFEN) return true;
        else return false;
    }
    
    /** Gibt den Knotenstatus an.
     * @return Knotenstatus: OFFEN|FERTIG|WIDERSPRUCH
     */
    public int getKnotenstatus() {
        return Knotenstatus;
    }
    
    /** Setzt die Lagerkraft.
     * @param neuer Lagerstatus: zwingend BER!
     */
    public boolean setLagerkraft(int p_Status, double p_Rx, double p_Rz) {
         switch (p_Status) { // neuer Lagerstatus
             case GESETZT: // noch nicht implementiert (in clFachwerk)
                System.err.println("FEHLER: Lagerkraft kann in dieser Programmversion nicht gesetzt werden!");
                assert false;
                return false;
            
             case BER:
                
                switch (Lagerstatus) { // bisheriger Lagerstatus
                    case UNBEST:
                        Lagerstatus = p_Status;
                        Rx = p_Rx;
                        Rz = p_Rz;
                        break;
                        
                    case GESETZT:
                        System.err.println("Programmfehler in clKnoten.setLagerkraft: GESETZT nicht implementiert");
                    case BER:
                        if (Math.abs(Rx - p_Rx) < TOL) {
                            Lagerstatus = p_Status;
                            Rx = p_Rx;
                            Rz = p_Rz;
                        }
                        else {
                            System.out.println("Widerspruch in Lagerkraft: neuer Wert widerspricht schon berechnetem!");
                            System.err.println("Programmfehler: clKnoten.setLagerkraft: zuerst Lagerstatus zurücksetzen");
                            assert false;
                            return false;
                        }
                        break;
                    default:
                        System.err.println("Programmfehler in clKnoten.setLagerkraft");
                        assert false;
                }
                break;
                
            default: // Status falsch
                System.err.println("Programmfehler: clKnoten.setLagerkraft");
                assert false;
                return false;
        }
        return true;
    }
    
    public double getRx() {
        return Rx;
    }
    
    public double getRz() {
        return Rz;
    }
    
    /** Gibt die Gleitrichtung des Lagers an. Nur bei verschieblichen Lagern von Belang.
    * @return Gleitrichtung in Altgrad
    */
    public double getRalpha() {
        if (Lagerbed == VERSCHIEBLICH) {
            return Ralpha;
        }
        else {
            return Ralpha;
        }
    }
    
    /** Setzt den Knotenstatus auf OFFEN zurück.*/
    public void zurücksetzen() {
        setKnotenstatus(OFFEN);
        switch (Lagerbed) {
            case LOS:
                Lagerstatus = GESETZT;
                break;
            case FIX:
            case VERSCHIEBLICH:
                Lagerstatus = UNBEST;
                Rx = 0;
                Rz = 0;
                break;
            default:
                assert false;
        }
    }
}
