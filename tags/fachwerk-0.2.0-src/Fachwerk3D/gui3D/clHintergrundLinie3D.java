/** clDXF, clHintergrundLinie, clHintergrundPunkt, clHintergrundKreis
 * Created 6.6.2004



 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                               <qwert2003@users.berlios.de>
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

package Fachwerk3D.gui3D;

import java.util.*;

public class clHintergrundLinie3D {
    private double[] von = new double[3];
    private double[] bis = new double[3];
    
    clHintergrundLinie3D(double vonX, double vonY, double vonZ, double bisX, double bisY, double bisZ) {
        von[0] = vonX;
        von[1] = vonY;
        von[2] = vonZ;
        bis[0] = bisX;
        bis[1] = bisY;
        bis[2] = bisZ;        
    }
    
    public boolean skaliere(double faktor) {
        assert (Math.abs(faktor) >= 1E-9);
        von[0] = faktor * von[0];
        von[1] = faktor * von[1];
        von[2] = faktor * von[2];
        bis[0] = faktor * bis[0];
        bis[1] = faktor * bis[1];
        bis[2] = faktor * bis[2];
        return true;
    }
    
    public void verschiebe(double dx, double dy, double dz) {
        von[0] += dx;
        von[1] += dy;
        von[2] += dz;
        bis[0] += dx;
        bis[1] += dy;
        bis[2] += dz;
    }
    
    /** Setzt die Koordinaten der Hintergrundlinie neu.*/
    public void setzeneu(double vonX, double vonY, double vonZ, double bisX, double bisY, double bisZ) {
        von[0] = vonX;
        von[1] = vonY;
        von[2] = vonZ;
        bis[0] = bisX;
        bis[1] = bisY;
        bis[2] = bisZ;  
    }
    
    /** Gibt den Startpunkt der Hintergrundlinie zurück.
     */
    public double[] getVon() {
        return von;
    }
    
    /** Gibt den Endpunkt der Hintergrundlinie zurück.
     */
    public double[] getBis() {
        return bis;
    }
}