/*
 * clKoordTransFwk.java
 *
 * Created on 7. Juni 2004, 22:30
 */

package Fachwerk3D.addins3D.skaliereLasten3D;

import Fachwerk3D.statik3D.clKnoten3D;
import Fachwerk3D.gui3D.clWissenderStab3D;
import java.util.*;
import Fachwerk.addins.skaliereLasten.clguiLastenSkalieren;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2004 - 2005 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clLastenSkalieren3D {
    
    ResourceBundle dialogRB;
    Locale locale;
    String titel;
    private double faktor = 1;
    private boolean INKLSTABKRÄFTE = false;
    private LinkedList Knotenliste;
    private LinkedList Stabliste;
    private boolean MODIFIZIERT = false;
    
    /** Creates a new instance of clLastenSkalieren3D */
    public clLastenSkalieren3D(LinkedList Knotenliste, LinkedList Stabliste, java.awt.Frame parent, Locale lc) {
        this.Knotenliste = Knotenliste;
        this.Stabliste = Stabliste;
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-addins", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-addins für " + locale.toString());
            return;
        }
        titel = tr("titel-SkaliereLasten");
        clguiLastenSkalieren dialog = new clguiLastenSkalieren(parent, titel, locale);
        
        faktor = dialog.getAntwort_Faktor();
        INKLSTABKRÄFTE = dialog.getAntwort_inklStäbe();
        if (faktor != 1) {
            MODIFIZIERT = true;
            skalieren();
        }
    }
    
    private void skalieren() {
        clKnoten3D aktKnoten;
        clWissenderStab3D aktWSt;
        double Lx;
        double Ly;
        double Lz;
        double N;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKnoten = (clKnoten3D) it.next();
            
            // Skalieren
            Lx = faktor * aktKnoten.getLx();
            Ly = faktor * aktKnoten.getLy();
            Lz = faktor * aktKnoten.getLz();
            aktKnoten.setLast(Lx, Ly, Lz);
            
        }
        
        // gesetzte Stabkräfte skalieren
        if (INKLSTABKRÄFTE) {
            for (Iterator it = Stabliste.iterator(); it.hasNext();) {
                aktWSt = (clWissenderStab3D) it.next();
                if (aktWSt.stab.getStatus() == aktWSt.stab.GESETZT) {
                    N = faktor * aktWSt.stab.getKraft();
                    aktWSt.stab.setKraft(aktWSt.stab.GESETZT, N);
                }
            }
        }
        
        // Für künftige Versionen: Gesetzte Lagerkräfte skalieren
    }
    
    /** Gibt an, ob etwas am Modell verändert worden ist.
     * Dies ist dann nicht der Fall, wenn der Dialog abgebrochen worden ist oder der Faktor 1 ist.
     */
    public boolean isModified() {
        return MODIFIZIERT;
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
