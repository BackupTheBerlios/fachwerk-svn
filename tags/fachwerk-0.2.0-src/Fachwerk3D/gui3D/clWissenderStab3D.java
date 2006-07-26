/*
 * clWissenderStab.java
 *
 * Created on 6. September 2003, 20:00
 */

package Fachwerk3D.gui3D;

import Fachwerk.statik.clStab;
import java.io.*;

/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2003, 2004 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clWissenderStab3D implements Serializable {
    
    /** Diese Nummer ist die Java-Checksumme dieser Klasse für Fachwerk3D-Dateien ab Version 0.1.
     */
    static final long serialVersionUID = 5768031754662366307L;
    
    public clStab stab;
    public int von; // Ausgangsknoten des Stabes
    public int bis; // Endknoten
    public int gruppe = 0; // Gruppennummer (noch nicht verwendet)
    
    /** Creates a new instance of clWissenderStab */
    public clWissenderStab3D(clStab p_stab, int p_von, int p_bis) {
        stab = p_stab;
        von = p_von;
        bis = p_bis;
    }
    
}
