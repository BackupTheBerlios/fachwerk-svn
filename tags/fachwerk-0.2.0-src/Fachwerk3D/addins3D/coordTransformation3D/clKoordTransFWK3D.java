/*
 * clKoordTransFwk.java
 *
 * Created on 7. Juni 2004, 22:30
 */

package Fachwerk3D.addins3D.coordTransformation3D;

import Fachwerk3D.statik3D.clKnoten3D;
import java.util.*;


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
 *
 * @author  A.Vontobel
 */
public class clKoordTransFWK3D {
    
    ResourceBundle dialogRB;
    Locale locale;
    String titel;
    double[] transformation;
    private LinkedList Knotenliste;
    
    /** Creates a new instance of clKoordTransFwk */
    public clKoordTransFWK3D(LinkedList Knotenliste, java.awt.Frame parent, Locale lc) {
        this.Knotenliste = Knotenliste;
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-addins", locale); // aus Fachwerk
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-addins für " + locale.toString());
            return;
        }
        titel = tr("titel-KoordTransFWK");
        clKoordTransDialog3D dialog = new clKoordTransDialog3D(parent, titel, locale);
        
        transformation = dialog.getAntwort();
        if (transformation != null) transformieren();
    }
    
    private void transformieren() { 
        clKnoten3D aktKnoten;
        double x;
        double y;
        double z;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKnoten = (clKnoten3D) it.next();
            
            // Skalieren
            x = transformation[0] * aktKnoten.getX();
            y = transformation[0] * aktKnoten.getY();
            z = transformation[0] * aktKnoten.getZ();
            
            // Verschieben
            x += transformation[1];
            y += transformation[2];
            z += transformation[3];
            
            aktKnoten.setNeueKoord(x, y, z);
        }
    }
    
    
    
    private String tr(String key) {        
        String übersetzt;
        try {übersetzt = dialogRB.getString(key);}
        catch (MissingResourceException e) {
            System.err.println("Schluesselwort + " + key + " nicht gefunden fuer " + locale.toString() + " ; " + e.toString());
            return key;
        }        
        return übersetzt;
    }    
    
}
