package Fachwerk3D.addins3D.export;
/*
 * export3d.java
 *
 * Created on 30. November 2004, 21:23
 */

import Fachwerk3D.statik3D.*;
import Fachwerk3D.gui3D.clWissenderStab3D;
import java.util.*;
import java.io.*;



/**
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
public class clExportInput3D implements inKonstante3D {
    String infile;
    String outfile;
    String ausgabetext;
    private LinkedList Knotenliste = new LinkedList();
    private LinkedList Stabliste = new LinkedList();
    private final String FILEPROGNAME = "treillis3D";
    private final String NZ = System.getProperty("line.separator"); //"\n"; // neue Zeile
    private final String SEP =",";
    
    private boolean WEITER = true;
    private boolean verbose = false;
    
    Locale locale = Locale.getDefault();
    ResourceBundle dialogRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-addins", locale);
    
    
    public clExportInput3D(LinkedList Knotenliste, LinkedList Stabliste, String outfile, Locale lc) {
        this.Knotenliste = Knotenliste;
        this.Stabliste = Stabliste;
        this.outfile = outfile;
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-addins", locale);
        if (WEITER) WEITER = zusammenstellen();
        if (WEITER) WEITER = zieldateiSchreiben(outfile);
    }
    
    /** Creates a new instance of export3d. Für externen Aufruf */
    public clExportInput3D(String in, String out) {
        infile = in;
        outfile = out;
        if (WEITER) WEITER = einlesen(infile);
        if (WEITER) WEITER = zusammenstellen();
        if (WEITER) WEITER = zieldateiSchreiben(outfile);
    }
    
    /** Externer Aufruf */
    public static void main(String[] args) {
        String outfile = "";
        String infile = "";
        
        if (args.length > 0) {
            infile = args[0];
            if (args.length > 1) outfile = args[1];
            else outfile = infile + ".out.csv";
        }
        else {
            System.out.println("Gebrauch: java clExportInput3D fachwerkdatei.fwk3d [Ausgabedatei.csv]");
            System.exit(1);
        }
        
        new clExportInput3D(infile, outfile);
    }
    
    private boolean einlesen(String dateiname) {
        String prog, meldung;
        int hauptversionsNrProg, fileversion, unterversionsNrProg;
        
        try {
            FileInputStream fs = new FileInputStream(dateiname);
            ObjectInputStream is = new ObjectInputStream(fs);
            
            prog = (String) is.readUTF();
            if (!prog.equals(FILEPROGNAME)) {
                meldung = "Fehler: " + dateiname + " ist keine Fachwerk3d-Datei";
                //                meldung = tr("Fehler") + ": " + dateiname + " " + tr("errKeineFachwerkDatei");
                throw new IOException(meldung);
            }
            hauptversionsNrProg = is.readInt();
            fileversion = is.readInt();
            unterversionsNrProg = is.readInt();
            Knotenliste = (LinkedList) is.readObject();
            Stabliste = (LinkedList) is.readObject();
            
            is.close();
        }
        catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            return false;
        }
        catch (FileNotFoundException e) {
            //System.err.println(e.toString());
            //            meldung = tr("errDateiNichtGefunden") +": " + dateiname;
            meldung = "Datei " + dateiname + " nicht gefunden";
            System.err.println(meldung);
            return false;
        }
        catch (IOException e) {
            System.err.println(e.toString());
            return false;
        }
        
        String temp = "";
        if (unterversionsNrProg < 10) temp = "0";
        temp += unterversionsNrProg;
        
        meldung = "Fachwerk-Datei " + dateiname + " geladen. (erstellt mit Version "
        + hauptversionsNrProg+"."+temp+" Dateiformat "+hauptversionsNrProg+"."+fileversion+")";
        //        meldung = tr("infoGELADEN_Fachwerkdatei") + " " + dateiname + " " + tr("infoGELADEN_geladen") + " "
        //        + hauptversionsNrProg+"."+temp+" "+tr("infoGELADEN_Dateiformat")+" "+hauptversionsNrProg+"."+fileversion+")";
        
        System.out.println(meldung);
        return true;
    }
    
    
    private boolean zusammenstellen() {
        StringBuffer o = new StringBuffer();
        
        o.append("Fachwerk 3D" + NZ + NZ);
        o.append(tr("Knoten") + NZ);
        o.append(tr("nr") + SEP + "x" + SEP + "y" + SEP + "z" + SEP + "Lx" + SEP + "Ly" + SEP + "Lz" + SEP + tr("Lagerung") + SEP + tr("Rtg")+" x" + SEP + tr("Rtg")+" y" + SEP + tr("Rtg")+" z");
        o.append(NZ + NZ);
        
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            clKnoten3D kn = (clKnoten3D) it.next();
            o.append(i + SEP);
            o.append(kn.getX() + SEP);
            o.append(kn.getY() + SEP);
            o.append(kn.getZ() + SEP);
            o.append(kn.getLx() + SEP);
            o.append(kn.getLy() + SEP);
            o.append(kn.getLz() + SEP);
            switch (kn.getLagerbed()) {
                case LOS:
                    o.append("" + SEP);
                    break;
                case FIX:
                    o.append(tr("fix") + SEP);
                    break;
                case VERSCHIEBLICH:
                    o.append(tr("blockierteRtg") + SEP);
                    o.append(kn.getRrtg()[0] + SEP);
                    o.append(kn.getRrtg()[1] + SEP);
                    o.append(kn.getRrtg()[2] + SEP);
                    break;
                case SCHINENLAGER:
                    o.append(tr("freie Rtg.") + SEP);
                    o.append(kn.getRrtg()[0] + SEP);
                    o.append(kn.getRrtg()[1] + SEP);
                    o.append(kn.getRrtg()[2] + SEP);
                    break;
                default:
                    o.append(kn.getLagerbed() + SEP);
                    o.append(kn.getRrtg()[0] + SEP);
                    o.append(kn.getRrtg()[1] + SEP);
                    o.append(kn.getRrtg()[2] + SEP);
            }
            o.append(NZ);
            i++;
        }
        
        o.append(NZ + NZ);
        o.append(tr("Staebe") + NZ);
        o.append(tr("nr") + SEP + tr("Gruppe") + SEP + tr("vonKnoten") + SEP + tr("bisKnoten") + SEP + tr("gesetzt") + SEP + tr("Kraft"));
        o.append(NZ + NZ);
        
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            clWissenderStab3D wst = (clWissenderStab3D) it.next();
            o.append(i + SEP);
            o.append(wst.gruppe + SEP);
            o.append(wst.von + SEP);
            o.append(wst.bis + SEP);
            switch(wst.stab.getStatus()) {
                case GESETZT:
                    o.append(tr("gesetzt") + SEP);
                    o.append(wst.stab.getKraft() + SEP);
                    break;
                default:
                    o.append("" + SEP);
            }
            o.append(NZ);
            i++;
        }
        
        ausgabetext = o.toString();
        return true;
    }
    
    private boolean zieldateiSchreiben(String dateiname) {
        boolean ZIELDATEIGESCHRIEBEN = false;
        try {
            File datei = new File(dateiname);
            FileWriter ausgabestrom = new FileWriter(datei);
            BufferedWriter ausgabe = new BufferedWriter(ausgabestrom);
            
            ausgabe.write(ausgabetext);
            ausgabe.flush();
            ausgabe.close();
            ZIELDATEIGESCHRIEBEN = true;
        }
        catch(IOException e) {
            String Fehlermeldung = tr("FehlerbeimSchreiben") + dateiname + NZ + tr("Fehlermeldung") + ": " + e;
            System.err.println(Fehlermeldung);
            verbose = true;
        }
        System.out.println("");
        if (ZIELDATEIGESCHRIEBEN) System.out.println(tr("Zieldatei") + " " + dateiname + " " + tr("geschrieben")+".");
        return true;
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
