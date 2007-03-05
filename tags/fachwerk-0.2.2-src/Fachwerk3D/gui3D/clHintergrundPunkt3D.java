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

public class clHintergrundPunkt3D {
    private double[] pkt = new double[3];
    
    clHintergrundPunkt3D(double X, double Y, double Z) {
        pkt[0] = X;
        pkt[1] = Y;
        pkt[2] = Z;
    }
    
    public boolean skaliere(double faktor) {
        assert (Math.abs(faktor) >= 1E-9);
        pkt[0] = faktor * pkt[0];
        pkt[1] = faktor * pkt[1];
        pkt[2] = faktor * pkt[2];
        return true;
    }
    
    public void verschiebe(double dx, double dy, double dz) {
        pkt[0] += dx;
        pkt[1] += dy;
        pkt[2] += dz;   
    }
    
    /** Setzt die Koordinaten des Hintergrundpunktes neu.*/
    public void setzeneu(double X, double Y, double Z) {
        pkt[0] = X;
        pkt[1] = Y;
        pkt[2] = Z;
    }
    
    /** Gibt die Koordinate des Hintergrundpunktes zurück.
     */
    public double[] getPkt() {
        return pkt;
    }
}