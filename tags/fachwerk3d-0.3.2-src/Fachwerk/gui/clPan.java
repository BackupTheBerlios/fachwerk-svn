/*
 * clPan.java
 *
 * Created on 7. November 2005, 21:59
 *
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
 


package Fachwerk.gui;

import java.awt.geom.*;

/**
 *
 * @author av
 */
public class clPan {
    
    Point2D OLo, URo, Mo;
    double m_pro_pix;
    
    /** Creates a new instance of clPan 
     Lässt die zoomPkte unverändert.*/
    public clPan(Point2D maus_pix, double m_pro_pix, Point2D[] zoomPkte) {
        Mo = maus_pix;
        OLo = zoomPkte[0];
        URo = zoomPkte[1];
        this.m_pro_pix = m_pro_pix;
    }
    
    public Point2D getOL(Point2D maus_pix) {
        double OLx = OLo.getX() - m_pro_pix * (maus_pix.getX()-Mo.getX());
        double OLz = OLo.getY() - m_pro_pix * (maus_pix.getY()-Mo.getY());
        return new Point2D.Double(OLx, OLz);
    }
    
    public Point2D getUR(Point2D maus_pix) {
        double URx = URo.getX() - m_pro_pix * (maus_pix.getX()-Mo.getX());
        double URz = URo.getY() - m_pro_pix * (maus_pix.getY()-Mo.getY());
        return new Point2D.Double(URx, URz);
    }
}
