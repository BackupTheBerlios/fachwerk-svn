/*
 * interfaceKonstante.java
 *
 * Created on 24. Januar 2004, 20:07
 */

package Fachwerk3D.statik3D;

/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2004 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
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
 
 *
 * @author  A.Vontobel
 */
public interface inKonstante3D extends Fachwerk.statik.inKonstante {
    
    
    // Vorsicht beim Ändern der Werte, welche die serialisierten Klassen clKnoten, clStab, clWissenderStab betreffen.
    // Nach Änderung unbedingt testen, ob alte Dateien noch geladen werden können!
    
    /*
    // Lagerattribute
    static final int LOS = 0;
    static final int FIX = 2;
    static final int VERSCHIEBLICH = 1;
    // Stabattribute
    static final int UNBEST = 0;
    static final int BER = 1;
    static final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    static final int NICHTSETZBAR = 4;
    // Knotenattribute
    static final int OFFEN = 0;
    static final int FERTIG = 1;
    static final int WIDERSPRUCH = 3;
    
    // aus clFachwerk
    final double TOL_vorberechnung = 1E-11; // um Gleichheit von Stabkräften zu erkennen
                                      // Vorschlag 1E-11
    final double TOL_resultatcheck = 1E-10; // dito, jedoch lascherer Wert, zB. TOL des GLS-Solvers
    
    // aus clGLSsolver
    final double TOL_gls = 1E-10;
    
    // aus clFindeOrt (Zusätze)
    final double TOL_finde = 1E-12;
    */

    // aus clFachwerk3D
    static final int SCHINENLAGER = 3;
    static final double TOL_gls3D = 5E-9;
    static final double TOL_resultatcheck3D = 5E-9; // dito, jedoch lascherer Wert, zB. TOL des GLS-Solvers

}
