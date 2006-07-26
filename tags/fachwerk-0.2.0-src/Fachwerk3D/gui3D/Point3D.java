/*
 * Point3D.java
 *
 * Created on 2. August 2004, 21:45
 */

package Fachwerk3D.gui3D;


/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                     <qwert2003@users.berlios.de>
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
public class Point3D {
    
    private double x, y, z;
    
    /** Creates a new instance of Point3D */
    public Point3D() {
    }
    
    /** Creates a new instance of Point3D */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public String toString() {
        return "Point3D[" +x+ ", " +y+ ", " +z+ "]";
    }
    
    public double distance(Point3D zweiterPkt) {
        double dx = x - zweiterPkt.getX();
        double dy = y - zweiterPkt.getY();
        double dz = z - zweiterPkt.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
