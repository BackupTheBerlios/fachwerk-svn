/*
 * clKnoten.java
 *
 * Created on 18. August 2003, 21:41
 */

package Fachwerk3D.statik3D;
import java.io.*;
import java.math.*;

/**
 * Fachwerk3D - treillis3D
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
public class clKnoten3D implements Serializable, inKonstante3D {
    
    /** Diese Nummer ist die Java-Checksumme dieser Klasse für Fachwerk3D-Dateien Version 0.1.
     *  Sie entstand bevor das Interface inKonstante verwendet wurde (für Attribute).
     */
    static final long serialVersionUID = 8945584485674900631L; //
    
    /* Die folgenden Konstanzen sind im Interface inKonstante3D deklariert.
    // Lagerattribute
    private static final int LOS = 0;
    private static final int FIX = 2;
    private static final int VERSCHIEBLICH = 1;
    private static final int SCHINENLAGER = 3;
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
    
    private double X, Z, Y; // Koordinaten (x nach rechts, z nach unten, y nach vorne)
    
    private double Rx = 0, Rz = 0, Ry = 0; // Auflagerkraft
    private double dRx = 0, dRz = 0, dRy = 0;  // Lagerrichtung bei verschieblichem Lager (blockierte Rtg)
                                               // resp. Gleitrtg bei Schinenlager
    private double Lx = 0, Lz = 0, Ly = 0; // Knotenlast             
    private int Lagerbed = LOS;
    private int Lagerstatus = GESETZT; // da LOS, TODO unklar, stiftet Verwirrung
    private int Knotenstatus = OFFEN;
    
    private final double TOL = 1E-12;
    
    /** Creates a new instance of clKnoten3D */
    public clKnoten3D(double p_x, double p_y, double p_z) {
        X = p_x; Z = p_z; Y = p_y;
    }
    
    /** Koordinaten setzen */
    public void setNeueKoord(double x, double y, double z) {
        X = x;
        Z = z;
        Y = y;
        Knotenstatus = OFFEN;
    }
    
    public double getX() {
        return X;
    }
    
    public double getZ() {
        return Z;
    }
    
    public double getY() {
        return Y;
    }
    
    /** Lagerbedingung setzen.
     * @param p_Lagerbed Lagerbedingung: LOS|FIX (nicht jedoch VERSCHIEBLICH!)
     */
    public void setLager(int p_Lagerbed) {
        if (p_Lagerbed == LOS) {
            Lagerbed = p_Lagerbed;
            Rx = 0; Rz = 0; Ry = 0; dRx = 0; dRy = 0; dRz = 0;
            Lagerstatus = GESETZT;
        }
        else if (p_Lagerbed == FIX) {
            Lagerbed = p_Lagerbed;
            Rx = 0; Rz = 0; Ry = 0; dRx = 0; dRy = 0; dRz = 0; // zurücksetzten der Werte, allerdings ohne Bedeutung, nur um Progfehler zu erkennen
            Lagerstatus = UNBEST;
        }
        else {
            System.err.println("Programmfehler: clKnoten.setLager");
            assert false;
        }
    }
    
    /** Bei verschieblichen Lagern die blockierte (resp. bei Schinenlagern die freie) Richtung setzen.
     * @param p_Lagerbed Lagerbedingung: VERSCHIEBLICH oder SCHINENLAGER
     * @param p_dRx x-Komponente der freien/blockierten Rtg. y,z analog.
     */
    public void setLager(int p_Lagerbed, double p_dRx, double p_dRy, double p_dRz) {
        if (p_Lagerbed == VERSCHIEBLICH || p_Lagerbed == SCHINENLAGER) {
            Lagerbed = p_Lagerbed;
            if (p_dRx*p_dRx + p_dRy*p_dRy + p_dRz*p_dRz > 0) {
                dRx = p_dRx;
                dRy = p_dRy;
                dRz = p_dRz;
            }            
            else { // alle Komponenten der blockierten (oder freien) Richtung null: (sollte nicht vorkommen)
                dRx = 0;
                dRy = 0;
                dRz = 1;
                System.err.println("Warnung: Knotenrichtung 0,0,0 --> zu 0,0,1 gesetzt");
                assert false;
            }
            Lagerstatus = UNBEST;
        }
        else {
            System.err.println("Programmfehler: clKnoten.setLager(verschieblich)");
            assert false;
        }
    }
    
    /** Knotenlast setzen. */
    public void setLast(double p_Lx, double p_Ly, double p_Lz) {
        Lx = p_Lx;
        Lz = p_Lz;
        Ly = p_Ly;
    }
    
    public double getLx() {
        return Lx;
    }
    
    public double getLz() {
        return Lz;
    }
    
    public double getLy() {
        return Ly;
    }
    
    /** Gibt die Lagerbedingung an.
     * @return Lagerbedingung: LOS|FIX|VERSCHIEBLICH|SCHINENLAGER
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
    public boolean setLagerkraft(int p_Status, double p_Rx, double p_Ry, double p_Rz) {
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
                        Ry = p_Ry;
                        break;
                        
                    case GESETZT:
                        System.err.println("Programmfehler in clKnoten.setLagerkraft: GESETZT nicht implementiert");
                    case BER:
                        if (Math.abs(Rx - p_Rx) < TOL) {
                            Lagerstatus = p_Status;
                            Rx = p_Rx;
                            Rz = p_Rz;
                            Ry = p_Ry;
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
    
    public double getRy() {
        return Ry;
    }
    
/** @return Gibt den Vektor der blockierten Richtung für verschiebliche Lager
    resp. jenen der Gleitrichtung bei Schinenlagern zurück. Der Betrag hat keine Bedeutung.*/
public double[] getRrtg() {
        if (Lagerbed == VERSCHIEBLICH) {
            double[] rtg = {dRx, dRy, dRz};
            return rtg;
        }
        else {
            double[] rtg = {dRx, dRy, dRz};
            return rtg;
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
            case SCHINENLAGER:
                Lagerstatus = UNBEST;
                Rx = 0;
                Rz = 0;
                Ry = 0;
                break;
            default:
                assert false;
        }
    }
}
