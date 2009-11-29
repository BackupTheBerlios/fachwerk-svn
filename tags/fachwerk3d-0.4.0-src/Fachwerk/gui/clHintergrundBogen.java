/** clDXF, clHintergrundLinie, clHintergrundPunkt, clHintergrundKreis
 * Created 6.6.2004



 * Fachwerk - treillis
 *
 * Copyright (c) 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                               <qwert2003@users.berlios.de>
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

package Fachwerk.gui;

public class clHintergrundBogen {
    // private double X;
    // private double Z;
    private double[] zentrum = new double[2];
    private double radius;
    // private double startwinkel;
    // private double endwinkel;
    private final double[] sektor = new double[2];
    
    clHintergrundBogen(double X, double Z, double radius, double startwinkel, double endwinkel) {
        // this.X = X;
        // this.Z = Z;
        zentrum[0] = X;
        zentrum[1] = Z;
        this.radius = radius;
        // this.startwinkel = startwinkel;
        // this.endwinkel = endwinkel;
        sektor[0] = startwinkel;
        sektor[1] = endwinkel;
    }
    
    public boolean skaliere(double faktor) {
        assert (Math.abs(faktor) >= 1E-9);
        zentrum[0] = faktor * zentrum[0];
        zentrum[1] = faktor * zentrum[1];
        radius = faktor * radius;
        return true;
    }
    
    public void verschiebe(double dx, double dz) {
        zentrum[0] += dx;
        zentrum[1] += dz;   
    }
    
    /** Gibt die Koordinate des Hintergrundpunktes zurück.
     */
    public double[] getZentrum() {
        // double[] pkt = {X, Z};
        // return pkt;
        return zentrum;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public double[] getSektor() {
        // double[] sektor = {startwinkel, endwinkel};
        return sektor;
    }
}
