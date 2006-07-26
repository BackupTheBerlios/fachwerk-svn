/** clDXF, clHintergrundLinie, clHintergrundPunkt, clHintergrundKreis
 * Created 6.6.2004



 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2004, 2005 A.Vontobel <qwert2003@users.sourceforge.net>
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
import ru.tcl.dxf.DXFDocument;
import Fachwerk.gui.clParserBGD;


// ---------------------------------------------------------------------------
public class clDXF3D {
    ArrayList hgLinien = new ArrayList();
    ArrayList hgPunkte = new ArrayList();
    ArrayList hgKugeln = new ArrayList();
    private final int LINE = 1;
    private final int POLYLINE = 2;
    private final int LWPOLYLINE = 3;
    private final int CIRCLE = 4;
    private final int POINT = 5;
    private final int TEXT = 6;
    private final int ARC = 7;
    private final int INSERT = 8;
    private final int SOLID = 9;
    private final int DIMENSION = 10;    
    private final int SPHERE = 11; // nur bgd, nicht dxf
    private final int FEHLER = -1;
    
    public clDXF3D(String datei) {
        if (datei.endsWith("dxf")) {
            einlesenDXF(datei);
            //testen();
        }
        else {
            einlesenBGD(datei);
            //testen();
        }
    }
        
    private boolean einlesenDXF(String datei) {
        DXFDocument doc = new DXFDocument();
        ru.tcl.dxf.entities.Entity ent = null;
        ru.tcl.dxf.entities.Line line = null;
        ru.tcl.dxf.entities.Polyline pline = null;
        ru.tcl.dxf.entities.Lwpolyline lwpline = null;
        ru.tcl.dxf.entities.Circle circle = null;
        ru.tcl.dxf.entities.Vertex plineVertex = null;
        ru.tcl.dxf.entities.Point point = null;
//        ru.tcl.dxf.entities.Arc arc = null;
        ru.tcl.dxf.DXFPoint dxfpkt = null;        
        clHintergrundLinie3D aktLinie = null;
        clHintergrundPunkt3D aktPunkt = null;
        clHintergrundKugel3D aktKreis = null;
        int zählerLine = 0;
        int zählerPolyline = 0;
        int zählerLWPolyline = 0;
        int zählerCircle = 0;
        int zählerPoint = 0;
        int zählerText = 0;
        int zählerArc = 0;
        int zählerInsert = 0;
        int zählerSolid = 0;
        int zählerDimension = 0;
        int zählerSonst = 0;
        int entity = 0;
        String entityName;
        boolean ERSTERPKT;
        boolean ERSTERPKTlw;
        
        try {
            
            doc.readDXF(datei);
            
            System.out.println("DXF: " + datei);
            System.out.println("DXF version: " + doc.getHeader().getACADVER());    
            
            for (Enumeration e = doc.getEntities().items(); e.hasMoreElements() ;) {
                ent = (ru.tcl.dxf.entities.Entity) e.nextElement();
                
                // Information auf System.out
                //System.out.println(ent);
                //System.out.print(ent.getEntityName());
                
                // Kontrolle, ob der Layer ausgeschaltet ist.
                if (ent.getLayer().isFrozen()) continue;
                if (ent.getLayer().isOff()) continue;
                
                entity = 0;
                entityName = ent.getEntityName();
                if (entityName.equals("Line")) entity = LINE;
                if (entityName.equals("Polyline")) entity = POLYLINE;
                if (entityName.equals("LWPolyline")) entity = LWPOLYLINE;
                if (entityName.equals("Circle")) entity = CIRCLE;
                if (entityName.equals("Point")) entity = POINT;
                if (entityName.equals("Text")) entity = TEXT;
                if (entityName.equals("Arc")) entity = ARC;
                if (entityName.equals("Insert")) entity = INSERT;
                if (entityName.equals("Solid")) entity = SOLID;
                if (entityName.equals("Dimension")) entity = DIMENSION;
                
                switch (entity) {
                    case (LINE):
                        zählerLine++;
                        // LINIEN EINLESEN
                        line = (ru.tcl.dxf.entities.Line) ent;                        
                        aktLinie = new clHintergrundLinie3D(line.getStart().getX(), -line.getStart().getY(), -line.getStart().getZ(),
                        line.getEnd().getX(), -line.getEnd().getY(), -line.getEnd().getZ());
                        hgLinien.add(aktLinie);                        
                        //System.out.println("von " + line.getStart().coords2dToString());
                        //System.out.println("bis " + line.getEnd().coords2dToString());
                        break;
                    case POLYLINE:
                        zählerPolyline++;
                        // POLYLINIEN EINLESEN
                        pline = (ru.tcl.dxf.entities.Polyline) ent;                        
                        ERSTERPKT = true;
                        ru.tcl.dxf.DXFPoint vorherigerdxfpkt = null;
                        for (Enumeration ep = pline.items(); ep.hasMoreElements();) {                            
                            plineVertex = (ru.tcl.dxf.entities.Vertex) ep.nextElement();
                            dxfpkt = plineVertex.getLocation();
                            //System.out.println("Pkt " + dxfpkt.coords2dToString());                            
                            if (ERSTERPKT) {
                                vorherigerdxfpkt = dxfpkt;
                                ERSTERPKT = false;
                            }
                            else {
                                aktLinie = new clHintergrundLinie3D(vorherigerdxfpkt.getX(), -vorherigerdxfpkt.getY(), -vorherigerdxfpkt.getZ(),
                                dxfpkt.getX(), -dxfpkt.getY(), -dxfpkt.getZ());
                                hgLinien.add(aktLinie);
                                vorherigerdxfpkt = dxfpkt;
                            }
                        }                    
                        break;
                    case LWPOLYLINE:
                        zählerLWPolyline++;
                        // LWPOLYLINIEN EINLESEN
                        lwpline = (ru.tcl.dxf.entities.Lwpolyline) ent;                        
                        ERSTERPKTlw = true;
                        ru.tcl.dxf.DXFPoint vorherigerdxfpktlw = null;
                        
                        ru.tcl.dxf.DXFPoint pkte[] = lwpline.getPkte();
                        
                        for (int i = 0; i < pkte.length; i++) {
                            dxfpkt = pkte[i];
                            //System.out.println("Pkt " + dxfpkt.coords2dToString());                            
                            if (ERSTERPKTlw) {
                                vorherigerdxfpktlw = dxfpkt;
                                ERSTERPKTlw = false;
                            }
                            else {
                                aktLinie = new clHintergrundLinie3D(vorherigerdxfpktlw.getX(), -vorherigerdxfpktlw.getY(), -vorherigerdxfpktlw.getZ(),
                                dxfpkt.getX(), -dxfpkt.getY(), -dxfpkt.getZ());
                                hgLinien.add(aktLinie);
                                vorherigerdxfpktlw = dxfpkt;
                            }
                        }                    
                        break;                    
                    case POINT:
                        zählerPoint++;
                        point = (ru.tcl.dxf.entities.Point) ent;                    
                        aktPunkt = new clHintergrundPunkt3D(point.getX(), -point.getY(), -point.getZ());
                        hgPunkte.add(aktPunkt);                    
                        //System.out.println("Koord " + point.getX() + " , " + point.getY(), " + point.getZ());
                        break;  
                    case CIRCLE:
                        zählerCircle++;
                        break;
                    case ARC:                      
                        zählerArc++;
                        break;
                    case TEXT:
                        zählerText++;
                        break;
                    case INSERT:
                        zählerInsert++;
                        break;
                    case SOLID:
                        zählerSolid++;
                        break;
                    case DIMENSION:
                        zählerDimension++;
                        break;
                    default:
                        zählerSonst++;
                        System.out.println(entityName + " ignored");
                }
                              
            }            
            
            // Auswertung 
            System.out.println("summary:");
            if (zählerLine > 0) System.out.println("Line " + zählerLine);
            if (zählerPolyline > 0) System.out.println("Polyline " + zählerPolyline);
            if (zählerLWPolyline > 0) System.out.println("LWPolyline " + zählerLWPolyline);            
            if (zählerPoint > 0) System.out.println("Point " + zählerPoint);
            if (zählerCircle > 0) System.out.println("Circle " + zählerCircle + " ignored");
            if (zählerArc > 0) System.out.println("Arc " + zählerArc + " ignored");
            if (zählerText > 0) System.out.println("Text " + zählerText + " ignored");
            if (zählerInsert > 0) System.out.println("Insert " + zählerInsert + " ignored"); 
            if (zählerSolid > 0) System.out.println("Solid " + zählerSolid + " ignored");
            if (zählerDimension > 0) System.out.println("Dimension " + zählerDimension + " ignored");
            if (zählerSonst > 0) System.out.println("etc " + zählerSonst + " ignored");
                
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        System.out.println("");
        return true;          
    }    
    
    
    private boolean einlesenBGD(String datei) {        
        String entityName;
        int entityNameNr = 0;       
        clHintergrundLinie3D aktLinie = null;
        clHintergrundPunkt3D aktPunkt = null;
        clHintergrundKugel3D aktKreis = null;
        double[] data;
        
        System.out.println("BGD: " + datei);
        try {
            clParserBGD doc = new clParserBGD(datei);
        
            while (doc.hasMoreElements()) {
                entityNameNr = 0;
                
                entityName = doc.getEntityName();
                if (entityName.equals("Line")) entityNameNr = LINE;                
                if (entityName.equals("Sphere")) entityNameNr = SPHERE;
                if (entityName.equals("Circle")) entityNameNr = CIRCLE;
                if (entityName.equals("Point")) entityNameNr = POINT;
                if (entityName.equals("Arc")) entityNameNr = ARC;
                if (entityName.equals("Fehler")) entityNameNr = FEHLER;
                                                              
                switch (entityNameNr) {
                    case (POINT):
                        data = doc.getEntity();
                        aktPunkt = new clHintergrundPunkt3D(data[0], data[1], data[2]);
                        hgPunkte.add(aktPunkt);    
                        break;
                    case (LINE):
                        data = doc.getEntity();
                        aktLinie = new clHintergrundLinie3D(data[0], data[1], data[2], data[3], data[4], data[5]);
                        hgLinien.add(aktLinie);
                        break;
                    case (SPHERE):
                        data = doc.getEntity();                        
                        aktKreis = new clHintergrundKugel3D(data[0], data[1], data[2], data[3]);
                        hgKugeln.add(aktKreis);                        
                        break;
                    case (CIRCLE):
                        System.out.println("Circle ignored in 3D structure");                   
                        break;
                    case (ARC):
                        System.out.println("Arc ignored in 3D structure"); 
                        break;
                    case FEHLER:
                        System.out.println("Error while parsing entity.");
                        break;
                    default:
                        System.out.println("UNKNOWN entity: " + entityName);
                }
            }
        }
        catch (Exception e) {
            System.out.print("Parsing Error (bgd-file): ");
            System.out.println(e);            
            // debug
//            e.printStackTrace();
            return false;
        }
        System.out.println("");
        return true;             
    }
    
    public boolean skaliere(double faktor) {
        if (Math.abs(faktor) < 1E-9) {
            System.out.println("factor < 1E-9 not allowed");
            return false;
        }
        if (faktor == 1) return true;
        
        clHintergrundLinie3D aktuell;
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie3D) it.next();
            aktuell.skaliere(faktor);
        }
        clHintergrundPunkt3D aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt3D) it.next();
            aktpkt.skaliere(faktor);
        }
        clHintergrundKugel3D aktkreis;
        for (Iterator it = hgKugeln.iterator(); it.hasNext();) {
            aktkreis = (clHintergrundKugel3D) it.next();
            aktkreis.skaliere(faktor);
        }
        return true;
    }
    
    public void verschiebe(double dx, double dy, double dz) {
        if (dx == 0 && dz == 0) return;
        
        clHintergrundLinie3D aktuell;
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie3D) it.next();
            aktuell.verschiebe(dx, dy, dz);
        }
        clHintergrundPunkt3D aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt3D) it.next();
            aktpkt.verschiebe(dx, dy, dz);
        }
        clHintergrundKugel3D aktkreis;
        for (Iterator it = hgKugeln.iterator(); it.hasNext();) {
            aktkreis = (clHintergrundKugel3D) it.next();
            aktkreis.verschiebe(dx, dy, dz);
        }
    }
    
    /** Gibt die Liste mit den Hintergrund-Linien zurück.
     *Die Typenumwandlung erfolgt mit (clHintergrundLinie3D)
     *@returns ArrayList Hintergrundlinien
     */
    public ArrayList getHgLinien() {
        return hgLinien;
    }
    
    /** Gibt die Liste mit den Hintergrund-Punkten zurück.
     *Die Typenumwandlung erfolgt mit (clHintergrundPunkt3D)
     *@returns ArrayList Hintergrundpunkte
     */
    public ArrayList getHgPunkte() {
        return hgPunkte;
    }
    
    /** Gibt die Liste mit den Hintergrund-Kugeln zurück.
     *Die Typenumwandlung erfolgt mit (clHintergrundKugel3D)
     *@returns ArrayList Hintergrundkugeln
     */
    public ArrayList getHgKugeln() {
        return hgKugeln;
    }
    
        
    void testen() {
        clHintergrundLinie3D aktuell;
        clHintergrundPunkt3D aktpkt;
        System.out.println("");
        
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie3D) it.next();
            System.out.print("von " + aktuell.getVon()[0] + " " + aktuell.getVon()[1]);
            System.out.println("  bis " + aktuell.getBis()[0] + " " + aktuell.getBis()[1]);
        }
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt3D) it.next();
            System.out.println("Punkt " + aktpkt.getPkt()[0] + " " + aktpkt.getPkt()[1]);
        }
    }
    
    public static void main(String[] args) {               
        System.out.println("Open file: " + args[0]);
        new clDXF3D(args[0]);
        
    }
}
