/** clHintergrundKugel3D
 * Created 17.10.2004



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

public class clHintergrundKugel3D {
    
    private double[] zentrum = new double[3];
    private double radius;
    
    clHintergrundKugel3D(double X, double Y, double Z, double radius) {
        zentrum[0] = X;
        zentrum[1] = Y;
        zentrum[2] = Z;
        this.radius = radius;
    }
    
    public boolean skaliere(double faktor) {
        assert (Math.abs(faktor) >= 1E-9);
        zentrum[0] = faktor * zentrum[0];
        zentrum[1] = faktor * zentrum[1];        
        zentrum[2] = faktor * zentrum[2];
        radius = faktor * radius;
        return true;
    }
    
    public void verschiebe(double dx, double dy, double dz) {
        zentrum[0] += dx;
        zentrum[1] += dy;  
        zentrum[2] += dz;
    }
    
    /** Setzt die Koordinaten der Hintergrundkugel neu.*/
    public void setzeneu(double X, double Y, double Z, double radius) {
        zentrum[0] = X;
        zentrum[1] = Y;
        zentrum[2] = Z;
        this.radius = radius;
    }
    
    
    /** Gibt die Koordinate des Zentrums der Hintergrundkugel zurück.
     */
    public double[] getZentrum() {
        return zentrum;
    }
    
    public double getRadius() {
        return radius;
    }
}