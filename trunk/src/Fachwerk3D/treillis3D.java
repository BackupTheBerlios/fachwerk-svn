/*
 * treillis.java
 *
 * Created on 6. September 2003, 13:34
 */

package Fachwerk3D;

import Fachwerk3D.gui3D.*;
import Fachwerk3D.statik3D.*;
import Fachwerk.statik.Fkt;
import Fachwerk.statik.clStab;
import Fachwerk.gui.clOK;
import Fachwerk.gui.clWaehlenDialog;
import Fachwerk.gui.StdFileFilter;
import Fachwerk.gui.clNeuerStabDialog;
import Fachwerk.gui.clHelpBrowser;
import Fachwerk.gui.clPan;
import Fachwerk3D.addins3D.findeOrt3D.*;
import Fachwerk3D.addins3D.coordTransformation3D.*;
import Fachwerk3D.addins3D.skaliereLasten3D.*;
import Fachwerk3D.addins3D.export.*;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.*;
import java.awt.print.*;


/**
 * Fachwerk3D - treillis3D
 *
 * Copyright (c) 2003 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                      <qwert2003@users.berlios.de>
 *
 * Das Programm könnte FEHLER enthalten. Sämtliche Resultate sind
 * SORGFÄLTIG auf ihre PLAUSIBILITäT zu prüfen!
 *
 * Dieses einfach zu bedienende Fachwerkprogramm verwendet ausschliesslich die
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
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 */
public class treillis3D extends clOberflaeche3D implements inKonstante3D {
    
    private static final String PROGNAME = "Fachwerk3D"; // in clOberflaeche nochmals hart kodiert (Titel)
    private static final int HAUPTVER = 0;
    private static final int UNTERVER = 33; // zweistellig, d.h. für Ver 1.3 UNTERVER = 30
    private static final int JAHR = 2009;
    private final String FILEPROGNAME = "treillis3D";
    private final int FILEVER = 1;
    
    public boolean OptionVorber = false;
    public boolean OptionGLS = true;
    public boolean OptionMechanismus = true;
    private boolean OptionVerbose = false;
    
    private boolean keinWIDERSPRUCH = true;
    private boolean keinFEHLER = true;
    private boolean VOLLSTÄNDIGGELÖST_OK = false;
    
    private LinkedList Knotenliste = new LinkedList();
    private LinkedList Stabliste = new LinkedList();
    private clDXF3D dxf; // wird nicht mitgespeichert!
    
    private clHauptPanel3D hp; // = new clHauptPanel(null,null); // Knotenliste, Stabliste);
    ResourceBundle meldungenRB;
    private JFileChooser fc = new JFileChooser(); //Create a file chooser
    private String dateiname = "new.fwk3d";
    private String dxfdateiname;
    
    private final double SNAPRADIUS = 6;
    //    private final double SNAPORTHO = 4;
    /*
    // Lagerattribute
    private final int LOS = 0;
    private final int FIX = 2;
    private final int VERSCHIEBLICH = 1;
    // Stabattribute
    private final int UNBEST = 0;
    private final int BER = 1;
    private final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    private final int NICHTSETZBAR = 4;
    // Knotenattribute
    private final int OFFEN = 0;
    private final int FERTIG = 1;
    private final int WIDERSPRUCH = 3;
    */
    private final int DESELEKT = 0;
    private final int KNOTEN = 1;
    private final int STAB = 2;
    private final int HINTERGRUND = 3;
    private final int PKTAUFSTAB = 4;
    private int[] Selektion = new int[2]; // index 0: STAB,KNOTEN,DESELEKT; index 1: Nr mit 1 beginnend
    private final int NICHTSÄNDERN = -1;
    private final int AUTOMATISCH = -2;
    private int selModus = AUTOMATISCH;
    
    private final int NICHTS = 0;
    private final int ZOOMxy = 1;
    //private final int SCHIEBEKNOTEN = 2; nur für 2D sinnvoll.
    private final int NEUERKNOTEN = 3;
    private final int NEUERSTAB = 4;
    private final int NEUERKNOTENSNAP = 5;
    private int mausAufgabe = NICHTS;
    
    
    
    //private boolean mausaktiv = false; // setzt die MausListener beim Laden, bei Dialogen etc inaktiv
    
    /** Creates a new instance of treillis */
    public treillis3D(Locale lc) {
        super("Fachwerk3D - rein statisches Fachwerkprogramm", lc);   // Titel wird in clOberflaeche überschrieben
        meldungenRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-meldungen", locale);
        if (meldungenRB == null) {
            System.err.println("FEHLER: gui-meldungen für " + locale.toString());
        }
        neu();
    }
    public treillis3D(Locale lc, String dateiname) {
        super("Fachwerk3D - rein statisches Fachwerkprogramm", lc);   // Titel wird in clOberflaeche überschrieben
        meldungenRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-meldungen", locale);
        if (meldungenRB == null) {
            System.err.println("FEHLER: gui-meldungen für " + locale.toString());
        }
        this.dateiname = dateiname;
        befehlLaden(true);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String sprache;
        String land;
        String dateiname = "";
        String lookAndFeel = "Steel";
        Locale lc = Locale.getDefault();
        treillis3D fw;
        boolean help = false;
        
        String titel = "" + PROGNAME + " version " + HAUPTVER + ".";
        if (UNTERVER < 10) titel += "0";
        titel += Integer.toString(UNTERVER);
        System.out.println(titel);
        System.out.println("Copyright (C) 2003-" + JAHR + " A. Vontobel");
        System.out.println("Fachwerk3D comes with ABSOLUTELY NO WARRANTY;");
        System.out.println("for details read the GNU General Public Licence (GPL) version 2.");
        System.out.println("All results must be verified CAREFULLY in order to state that they");
        System.out.println("are PLAUSIBLE!");
        System.out.println("");
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                help = true;
                break;
            }
            if (args[i].equals("-V") || args[i].equals("--version")) {
                return;  // BEENDET DAS PROGRAMM
            }
            if (args[i].equals("-l") || args[i].equals("--language")) {
                if (i+1 >= args.length) {help = true; break;}
                i++;
                sprache = args[i];
                lc = new Locale(sprache);
                continue;
            }
            if (args[i].equals("--language_country")) {
                if (i+2 >= args.length) {help = true; break;}
                i++;
                sprache = args[i];
                i++;
                land = args[i];
                lc = new Locale(sprache, land);
                continue;
            }
            if (args[i].equals("--look")) {
                if (i+1 >= args.length) {help = true; break;}
                i++;
                lookAndFeel = args[i];
                continue;
            }
            
            // wenn keine bekannte Option erkannt wurde:
            if (args[i].startsWith("-")) {help = true; break;}
            else {
                dateiname = args[i];
            }
        }
        
        
        
        if (help) {
            System.out.println("");
            System.out.println("Fachwerk3D calculates strut-and-tie models used by structural engineers");
            System.out.println("for analysing and designing reinforced concrete structures.");
            System.out.println("The program only uses the equilibrium conditions, thus it is ");
            System.out.println("not assuming elastic behaviour.");
            System.out.println('\n');
            System.out.println("Usage: fachwerk3d [OPTION]... [file]...");
            System.out.println('\n');
            System.out.println("Options:");
            System.out.println("    --help or -h             print this help, then exit");
            System.out.println("    --version or -V          print the version, then exit");
            System.out.println("    --language or -l         Language i.e. 'de', 'en', 'fr'");
            System.out.println("    --language_country       Language and country i.e. 'de CH', 'en GB'");
            System.out.println("    --look                   LookAndFeel: Java|System|Steel|Ocean|undef");
            System.out.println("                             or i.e. com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            System.out.println('\n');
            System.out.println("Example 1: fachwerk3d file.fwk3d");
            System.out.println("Example 2: fachwerk3d -l en");
            System.out.println("Example 3: fachwerk3d -l fr file.fwk3d");
            System.out.println("");
            return; //System.exit(0); // BEENDET DAS PROGRAMM
        }
        
        // LookAndFeel setzen.
        boolean laf_gesetzt = false;
        try {
            if (!laf_gesetzt && lookAndFeel.equalsIgnoreCase("Java")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                laf_gesetzt = true;
            }
            if (!laf_gesetzt && lookAndFeel.equalsIgnoreCase("System")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                laf_gesetzt = true;
            }
            if (!laf_gesetzt && lookAndFeel.equalsIgnoreCase("Steel")) {
                javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
                laf_gesetzt = true;
            }
            if (!laf_gesetzt && lookAndFeel.equalsIgnoreCase("Ocean")) { // TODO ab Java 1.5
                // TODO: Zur Zeit inaktiv, um kompatibel mit Java1.4 zu bleiben.
                System.out.println("Note: The theme 'Ocean' will not be applied directly (would break JRE1.4),");
                System.out.println("the Java-LookAndFeel is loaded instead (usually 'Ocean' on JRE1.5).");
                System.out.println("");
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                
                /*
                javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.OceanTheme());
                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
                */
                laf_gesetzt = true;
            }
            if (!laf_gesetzt && lookAndFeel.equalsIgnoreCase("undef")) {
                // nicht setzten, damit von Befehlszeile aus festlegbar
                // z.B. java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel ...
                laf_gesetzt = true;
            }
            
            if (!laf_gesetzt) {
                UIManager.setLookAndFeel(lookAndFeel);
            }
        }
        catch (Exception e) { // macht nichts
            System.err.println("LookAndFeel " + lookAndFeel +" could not be set. Standard LookAndFeel used instead.");
        }
        
        
        if (dateiname.equals("")) {
            fw = new treillis3D(lc);
        }
        else {
            fw = new treillis3D(lc, dateiname);
        }
        
        fw.pack();
        fw.setVisible(true);
    }
    
    
    // -------------
    // GUI - BEFEHLE
    // -------------
    
    protected void befehlAllesZurücksetzen() {
        zurücksetzen(true);
        aktualisieren(true, true);
    }
    
    protected void befehlBeenden() {
        if (Knotenliste.size() > 0) {
            //clOK bestätige = new clOK(this, "BEENDEN", "Allfällige nicht gespeicherte Daten gehen verloren.", locale);
            clOK bestätige = new clOK(this, tr("okBEENDEN"), tr("okBEENDENwarnung"), locale);
            if (!bestätige.ok()) return;
        }
        System.out.println("bye");
        System.exit(0);
    }
    
    protected void befehlBerechne() {
        // GUI Vorarbeiten
        switch (mausAufgabe) {
            case NEUERSTAB:
                mausAufgabe = NICHTS;
                setKnopfNeuerStab(false);
                break;
            case NEUERKNOTENSNAP:
                mausAufgabe = NICHTS;
                setKnopfNeuerKnotenSnap(false);
                break;
            case NEUERKNOTEN:
                assert false;
                mausAufgabe = NICHTS;
                break;
            case ZOOMxy: // lassen
            default:
        }
        
        // BEGINN
        keinFEHLER = true;
        VOLLSTÄNDIGGELÖST_OK = false;
        //Datenaufbereiten
        clKnoten3D[] Knotenarray = new clKnoten3D[Knotenliste.size() + 1];
        clStab[] Stabarray = new clStab[Stabliste.size() + 1];
        int[][] Topologie = new int[Knotenarray.length][Knotenarray.length];
        
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten3D) it.next();
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            clWissenderStab3D wst = (clWissenderStab3D) it.next();
            Stabarray[i] = wst.stab;
            Topologie[wst.von][wst.bis] = i;
            i++;
        }
        
        int statUnbesth = Integer.MIN_VALUE; // Integer.MIN_VALUE bedeutet unbekannt/noch_nicht_berechnet.
        try {
            clFachwerk3D fachwerk = new clFachwerk3D(Knotenarray, Stabarray, Topologie);
            fachwerk.setVerbose(OptionVerbose);
            keinWIDERSPRUCH = fachwerk.rechnen(OptionVorber,OptionGLS,OptionMechanismus);
            statUnbesth = fachwerk.getStatischeUnbestimmtheit();
            fachwerk.resultatausgabe_direkt();
            if (keinWIDERSPRUCH) VOLLSTÄNDIGGELÖST_OK = fachwerk.istvollständiggelöst(false); // false, das resutatcheck() soeben in .rechnen() durchgeführt
            aktualisieren(true, true);
            if (!keinWIDERSPRUCH) LayerMechanismius(true, fachwerk.getMechanismus());
            if (keinWIDERSPRUCH) LayerStKraft(true);
            LayerAuflKraft(true);
            LayerLasten(true);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            aktualisieren(true, true);
            // im Statusfeld FEHLER anzeigen, in Statuszeile Fehlermeldung
            feldStatusFw.setText(tr("FEHLER"));
            keinFEHLER = false;
            feldStatuszeile.setText(e.toString());
            if (e.getMessage().equals("Mechanismusberechnung fehlgeschlagen.")) {
                feldStatuszeile.setText(tr("errMechanismusberFehlgeschlagen"));
            }
            else feldStatuszeile.setText(e.toString());
        }
        
        if (keinWIDERSPRUCH && keinFEHLER) {
            if (VOLLSTÄNDIGGELÖST_OK) feldStatusFw.setText(tr("OK-COMPLETE"));
            else if (statUnbesth == Integer.MIN_VALUE) feldStatusFw.setText(tr("OK"));
            else {
                feldStatusFw.setText(tr("OK") + " ("+statUnbesth+")");
                assert statUnbesth >= 0;
            }
            selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
        }
        else {
            if (!keinWIDERSPRUCH) feldStatusFw.setText(tr("WIDERSPRUCH"));
            if (!keinFEHLER) feldStatusFw.setText(tr("FEHLER"));
            selModus = NICHTSÄNDERN;
        }
        Selektion[0] = DESELEKT;
        //aktualisieren(true);
        setKnopfKnoten(false); setKnopfStab(false);
    }
    
    protected void befehlBlickrtg() {
        double[] n = new double[3];
        clBlickRtgDialog dialog = new clBlickRtgDialog(hp.getBlickRtg(), this,locale);
        if (dialog.isOK()) {
            n = dialog.get();
            hp.setBlickRtg(n);
            schieberBlickrtgSetzen();
        }
    }
    
    protected void befehlBlickrtgMaus(double winkelHor, double winkelVer) {
        double teta = Math.toRadians(winkelVer);
        double phi = Math.toRadians(90d - winkelHor);
        double[] n = new double[3];
        n[0] = - Math.cos(teta)*Math.cos(phi);
        n[1] = - Math.cos(teta)*Math.sin(phi);
        n[2] = Math.sin(teta);
        hp.setBlickRtg(n);
    }
    
    protected void befehlDrucken() {
        clKnoten3D[] Knotenarray = new clKnoten3D[Knotenliste.size() + 1];
        clWissenderStab3D[] Stabarray = new clWissenderStab3D[Stabliste.size() + 1];
        boolean WIDERSPRUCHmelden = !keinWIDERSPRUCH;
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten3D) it.next();
            if (!WIDERSPRUCHmelden && Knotenarray[i].getKnotenstatus() == WIDERSPRUCH) WIDERSPRUCHmelden = true;
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            Stabarray[i] = (clWissenderStab3D) it.next();
            i++;
        }
        
        clPrintPanel3D printpanel = new clPrintPanel3D(Knotenarray, Stabarray, hp,
                                    WIDERSPRUCHmelden, !keinFEHLER, VOLLSTÄNDIGGELÖST_OK, locale);
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        //PageFormat df = pj.defaultPage();
        
        pj.setPrintable(printpanel);
        if(pj.printDialog()) {
            try   {
                pj.print();
            }
            catch(Exception e)  {
                String meldung = tr("errFehlerBeimDrucken") +": " + e.getMessage();
                System.out.println(meldung);
                feldStatuszeile.setText(meldung);
            }
        }
    }
    
    protected void befehlDruckenGraph() {
        clPrintGraphPanel3D printpanel = new clPrintGraphPanel3D(this, hp, locale);
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        //PageFormat df = pj.defaultPage();
        
        pj.setPrintable(printpanel);
        if(pj.printDialog()) {
            try   {
                pj.print();
            }
            catch(Exception e)  {
                String meldung = tr("errFehlerBeimDrucken") +": " + e.getMessage();
                System.out.println(meldung);
                feldStatuszeile.setText(meldung);
            }
        }
    }
    
    protected void befehlEigenschaften() {
        int nr;
        switch (Selektion[0]) {
            case DESELEKT:
                break;
                
            case KNOTEN:
                nr = Selektion[1];
                clKnotenDialog3D kndialog = new clKnotenDialog3D(this, nr, (clKnoten3D) Knotenliste.get(Selektion[1]-1), locale);
                if (kndialog.hatGeändert()) {
                    kndialog.einlesen();
                    zurücksetzen(false);
                    Selektion[0] = KNOTEN;
                    Selektion[1] = nr;
                    selektionAnpassen();
                    LayerLasten(true);
                }
                break;
            case STAB:
                nr = Selektion[1];
                clStabDialog3D stdialog = new clStabDialog3D(this, nr, (clWissenderStab3D) Stabliste.get(Selektion[1]-1), locale);
                if (stdialog.hatGeändert()) {
                    stdialog.einlesen();
                    zurücksetzen(false);
                    Selektion[0] = STAB;
                    Selektion[1] = nr;
                    selektionAnpassen();
                }
                break;
            default:
                assert false;
        }
    }
    
    protected void befehlErstelleNeuenKnoten() {
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfNeuerStab(false);
        setKnopfZoomMaus(false);
        setKnopfNeuerKnotenSnap(false);
        Knotenliste.add(new clKnoten3D(0,0,0));
        Selektion[0] = KNOTEN;
        Selektion[1] = Knotenliste.size();
        selektionAnpassen();
        befehlEigenschaften();
        zurücksetzen(false);
        aktualisieren(false, true);
        selModus = NICHTSÄNDERN;
        Selektion[0] = KNOTEN;
        Selektion[1] = Knotenliste.size();
        selektionAnpassen();
    }
    
    protected void befehlErstelleNeuenKnotenMaus(boolean status) {                         // NOCH NICHT IMPLEMENTIERT
        setKnopfNeuerStab(false);
        setKnopfZoomMaus(false);
        setKnopfNeuerKnotenSnap(false);
        // vorläufig:
        befehlErstelleNeuenKnoten();
        setKnopfNeuerKnoten(false);
    }
    
    protected void befehlErstelleNeuenKnotenSnap(boolean status) {
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfNeuerStab(false);
        setKnopfZoomMaus(false);
        
        if (status) {
            mausAufgabe = NEUERKNOTENSNAP;
            if (LayerHintergrund()) selModus = HINTERGRUND;
            else selModus = PKTAUFSTAB;
            Selektion[0] = DESELEKT;
        }
        else {
            mausAufgabe = NICHTS;
            selModus = AUTOMATISCH;
            hp.deselektHintergrund();
        }
        selektionAnpassen();
    }
    
    protected void befehlErstelleNeuenStab() {
        boolean bisher_layerKnNr = LayerKnNr();
        boolean bisher_layerStNr = LayerStNr();
        LayerKnNr(true);
        LayerStNr(false);
        LayerStKraft(false);
        setKnopfKnoten(false);
        setKnopfStab(false);
        clNeuerStabDialog dialog = new clNeuerStabDialog(this, locale);
        if (!dialog.getOK()) {
            LayerKnNr(bisher_layerKnNr);
            LayerStNr(bisher_layerStNr);
            return;
        }
        int[] vonbis = dialog.einlesen();
        // Kontrolle, ob Knoten existieren und ob sie nicht gleich sind.
        if (vonbis[0] < 1 || vonbis[1] < 1) return;
        if (vonbis[0] > Knotenliste.size() || vonbis[1] > Knotenliste.size()) {
            //String meldung = "Eingabefehler: Beide Knoten müssen existieren!";
            String meldung = tr("errBeideKnotenMuessenExistieren");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            LayerKnNr(bisher_layerKnNr);
            LayerStNr(bisher_layerStNr);
            return;
        }
        if (vonbis[0] == vonbis[1]) {
            //String meldung = "Eingabefehler: Ein Knoten darf nicht mit sich selber verbunden werden!";
            String meldung = tr("errKnotenMitSichSelbstVerbunden");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            LayerKnNr(bisher_layerKnNr);
            LayerStNr(bisher_layerStNr);
            return;
        }
        // Kontrolle, ob nicht schon ein Stab zwischen diesen Knoten existiert
        boolean stabschonvorhanden = false;
        clWissenderStab3D aktWSt;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab3D) it.next();
            if (aktWSt.von == vonbis[0] && aktWSt.bis == vonbis[1]) stabschonvorhanden = true;
            if (aktWSt.von == vonbis[1] && aktWSt.bis == vonbis[0]) stabschonvorhanden = true;
            if (stabschonvorhanden) break;
        }
        if (stabschonvorhanden) {
            //String meldung = "Eingabefehler: Ein Stab zwischen diesen Knoten existiert bereits!";
            String meldung = tr("errStabSchonVorhanden");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            LayerKnNr(bisher_layerKnNr);
            LayerStNr(bisher_layerStNr);
            return;
        }
        
        Stabliste.add(new clWissenderStab3D(new clStab(),vonbis[0], vonbis[1]));
        zurücksetzen(false);
        aktualisieren(false, true);
        selModus = NICHTSÄNDERN;
        Selektion[0] = STAB;
        Selektion[1] = Stabliste.size();
        LayerKnNr(bisher_layerKnNr);
        LayerStNr(bisher_layerStNr);
        selektionAnpassen();
    }
    
    protected void befehlErstelleNeuenStabMaus(boolean status) {
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfZoomMaus(false);
        setKnopfNeuerKnoten(false);
        setKnopfNeuerKnotenSnap(false);
        
        if (status) {
            mausAufgabe = NEUERSTAB;
            selModus = KNOTEN;
        }
        else mausAufgabe = NICHTS;
        
        setKnopfKnoten(false);
        setKnopfStab(false);
    }
    
    protected void befehlGewähltIstKnoten(boolean status) {
        if (status) {
            setKnopfStab(false);
            selModus = KNOTEN;
            if (Selektion[0] == STAB) {
                Selektion[0] = DESELEKT;
            }
        }
        else {
            Selektion[0] = DESELEKT;
            selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
        }
        hp.selekt(Selektion);
    }
    
    protected void befehlGewähltIstStab(boolean status) {
        if (status) {
            setKnopfKnoten(false);
            selModus = STAB;
            if (Selektion[0] == KNOTEN) {
                Selektion[0] = DESELEKT;
            }
        }
        else {
            Selektion[0] = DESELEKT;
            selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
        }
        hp.selekt(Selektion);
    }
    
    protected void befehlHilfe() {
        clHelpBrowser hilfsbrowser = new clHelpBrowser(locale, "Fachwerk3D/locales3D/LangSetting3D");
    }
    
    protected void befehlInfo() {
        clInfo3D info = new clInfo3D(this, PROGNAME, HAUPTVER, UNTERVER, JAHR);
    }
    
    protected void befehlLadeHintergrund() {
        String prog, meldung;
        try {
            fc.resetChoosableFileFilters();
            fc.addChoosableFileFilter(new StdFileFilter("dxf", "Drawing Exchange File"));
            fc.addChoosableFileFilter(new StdFileFilter("bgd", "Background Data File"));
            fc.addChoosableFileFilter(new StdFileFilter("csv", "Background Points"));
            String[] filter = {"dxf","bgd","csv"};
            fc.addChoosableFileFilter(new StdFileFilter(filter, "Drawings and Points"));
            if (dxfdateiname != null) fc.setSelectedFile(new File(dxfdateiname));
            else fc.setSelectedFile(new File(""));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File datei = fc.getSelectedFile();
                dxfdateiname = datei.getPath();
                dxf = new clDXF3D(dxfdateiname);
            }
            else {
                LayerHintergrund(false);
                return;
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
            feldStatuszeile.setText(e.toString());
            LayerHintergrund(false);
            return;
        }
        System.gc();
        aktualisieren(true, true);
        LayerHintergrund(true);
        if (mausAufgabe == NEUERKNOTENSNAP) selModus = HINTERGRUND;
    }
    
    protected void befehlLaden(boolean progstart) {
        if (!progstart && Knotenliste.size() > 0) {
            //Warnung "Allfällige nicht gespeicherte Daten gehen verloren."
            clOK bestätige = new clOK(this, tr("okLADEN"), tr("okLADENwarnung"), locale);
            if (!bestätige.ok()) return;
        }
        
        neu();
        String prog, meldung;
        int hauptversionsNrProg, fileversion, unterversionsNrProg;
        
        try {
            if (dateiname != null) fc.setSelectedFile(new File(dateiname));
            if (!progstart) {
                fc.resetChoosableFileFilters();
                fc.addChoosableFileFilter(new StdFileFilter("fwk3d", "Fachwerk3D Data"));
                
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File datei = fc.getSelectedFile();
                    dateiname = datei.getPath();
                }
                else {
                    dateiname = "";
                    this.setTitle(PROGNAME);
                    return;
                }
            }
            FileInputStream fs = new FileInputStream(dateiname);
            ObjectInputStream is = new ObjectInputStream(fs);
            
            prog = (String) is.readUTF();
            if (!prog.equals(FILEPROGNAME)) {
                // Datei ist keine Fachwerk3D-Datei
                meldung = tr("Fehler") + ": " + dateiname + " " + tr("errKeineFachwerkDatei");
                throw new IOException(meldung);
            }
            hauptversionsNrProg = is.readInt();
            fileversion = is.readInt();
            unterversionsNrProg = is.readInt();
            Knotenliste = (LinkedList) is.readObject();
            Stabliste = (LinkedList) is.readObject();
            
            is.close();
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            feldStatuszeile.setText(e.toString());
            return;
        } catch (FileNotFoundException e) {
            meldung = tr("errDateiNichtGefunden") +": " + dateiname;
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            return;
        } catch (IOException e) {
            System.err.println(e.toString());
            feldStatuszeile.setText(e.toString());
            return;
        }
        String temp = "";
        if (unterversionsNrProg < 10) temp = "0";
        temp += unterversionsNrProg;
        //meldung = "Fachwerk-Datei " + dateiname + " geladen. (erstellt mit Version "
        //+ hauptversionsNrProg+"."+temp+" Dateiformat "+hauptversionsNrProg+"."+fileversion+")";
        meldung = tr("infoGELADEN_Fachwerkdatei") + " " + dateiname + " " + tr("infoGELADEN_geladen") + " "
        + hauptversionsNrProg+"."+temp+" "+tr("infoGELADEN_Dateiformat")+" "+hauptversionsNrProg+"."+fileversion+")";
        System.out.println(meldung);
        
        this.setTitle(PROGNAME + " - " + dateiname);
        Selektion[0] = DESELEKT;
        aktualisieren(true, true);
        setKnopfKnoten(false); setKnopfStab(false);
        selModus = NICHTSÄNDERN;
        feldStatuszeile.setText(meldung);
    }
    
    protected void befehlLöschen() {
        clOK bestätige;
        String meldung;
        
        switch (Selektion[0]) {
            case DESELEKT:
                break;
                
            case KNOTEN:
                int nr = Selektion[1];
                clWissenderStab3D st; // Variable für Prüfung auf angeschlossene Stäbe.
                
                // Falls Stäbe an den Knoten angeschlossen sind, nachfragen vor dem Löschen des Knotens.
                boolean knotenAngeschlossen = false;
                for (int i = 0; i < Stabliste.size(); i++) {
                    st = (clWissenderStab3D) Stabliste.get(i);
                    if (st.von == nr || st.bis == nr) {
                        knotenAngeschlossen = true;
                        break; // For-Schlaufe
                    }
                }
                if (knotenAngeschlossen) {
                    // Knotennummern anzeigen, bisherigen Zustand merken
                    boolean layerKnNr_bisherigerZustd = LayerKnNr();
                    LayerKnNr(true);
                    // in Dialog  bestätigen
                    //meldung = "Der Knoten " + nr + " und alle anschl. Stäbe werden gelöscht.";
                    meldung = tr("okKNOTENLOESCHENwarnung_Knoten")+" " + nr + " "+tr("okKNOTENLOESCHENwarnung_wirdgeloescht");
                    bestätige = new clOK(this, tr("okKNOTENLOESCHEN"), meldung, locale);
                    LayerKnNr(layerKnNr_bisherigerZustd);
                    if (!bestätige.ok()) break;
                }
                
                // anschliessende Stäbe löschen
                for (int i = 0; i < Stabliste.size(); i++) {
                    st = (clWissenderStab3D) Stabliste.get(i);
                    if (st.von == nr || st.bis == nr) {
                        Stabliste.remove(i);
                        i--;
                    }
                }
                
                // Knoten entfernen
                Knotenliste.remove(nr-1);
                
                // Anschlussknoten der verbleibenden Stäbe umnummerieren.
                for (Iterator it = Stabliste.iterator(); it.hasNext();) {
                    st = (clWissenderStab3D) it.next();
                    if (st.von > nr) st.von--;
                    if (st.bis > nr) st.bis--;
                }
                zurücksetzen(false);
                break;
            case STAB:
                // Um Bestätigung für das Löschen nachfragen, wenn Stabkraft gesetzt oder berechnet.
                boolean nachfragen;
                switch (((clWissenderStab3D) Stabliste.get(Selektion[1]-1)).stab.getStatus()) {
                    case UNBEST:
                        nachfragen = false;
                        break;
                    default:
                        nachfragen = true;
                }
                if (nachfragen) {
                    // Stabnummern anzeigen, damit sichtbar Stabkraft ausblenden
                    boolean layerStNr_bisherigerZustd = LayerStNr();
                    boolean layerStKraft_bisherigerZustd = LayerStKraft();
                    LayerStKraft(false);
                    LayerStNr(true);
                    // in Dialog bestätigen
                    //meldung = "Der Stab " + Selektion[1] + " wird gelöscht.";
                    meldung = tr("okSTABLOESCHENwarnung_Stab") + " " + Selektion[1] + " " + tr("okSTABLOESCHENwarnung_wirdgeloescht");
                    bestätige = new clOK(this, tr("okSTABLOESCHEN"), meldung, locale);
                    LayerStNr(layerStNr_bisherigerZustd);
                    if (!bestätige.ok()) {
                        LayerStKraft(layerStKraft_bisherigerZustd);
                        break;
                    }
                }
                
                Stabliste.remove(Selektion[1]-1);
                zurücksetzen(false);
                break;
            default:
                assert false;
        }
        selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
    }
    
    protected void befehlNeu() {
        if (Knotenliste.size() > 0) {
            //clOK bestätige = new clOK(this, "NEU", "Allfällige nicht gespeicherte Daten gehen verloren.", locale);
            clOK bestätige = new clOK(this, tr("okNEU"), tr("okNEUwarnung"), locale);
            if (!bestätige.ok()) return;
        }
        fc.setSelectedFile(new File(""));
        dateiname = "";
        this.setTitle(PROGNAME);
        neu();
    }
    
    protected void befehlNeuZeichnen() {
        Selektion[0] = DESELEKT;
        aktualisieren(true, true);
        setKnopfKnoten(false); setKnopfStab(false);
        selModus = AUTOMATISCH;
    }
    
    protected void befehlOptionGLS(boolean status) {
        OptionGLS = status;
    }
    
    protected void befehlOptionMechanismus(boolean status) {
        OptionMechanismus = status;
    }
    
    protected void befehlOptionVerbose(boolean verbose){
        OptionVerbose = verbose;
    }
    
    protected void befehlOptionVorberechnung(boolean status) { // im Prinzip überflüssig
        OptionVorber = status;
    }
    
    
    protected void befehlSelektiereKnotenNr() {
        boolean bisher_layerKnNr = LayerKnNr();
        LayerKnNr(true);
        clWaehlenDialog welchen = new clWaehlenDialog(this, KNOTEN, locale);
        int nr = welchen.getNr();
        if (nr > 0 && nr <= Knotenliste.size()) {
            Selektion[0] = KNOTEN;
            Selektion[1] = nr;
            setKnopfStab(false);
            setKnopfKnoten(false); //true);
            selModus = NICHTSÄNDERN;
        }
        else {
            Selektion[0] = DESELEKT;
            setKnopfStab(false);
            setKnopfKnoten(false);
            selModus = AUTOMATISCH;
        }
        LayerKnNr(bisher_layerKnNr);
        selektionAnpassen();
    }
    
    protected void befehlSelektiereStabNr() {
        boolean bisher_layerStKraft = LayerStKraft();
        boolean bisher_layerStNr = LayerStNr();
        LayerStKraft(false);
        LayerStNr(true);
        clWaehlenDialog welchen = new clWaehlenDialog(this, STAB, locale);
        int nr = welchen.getNr();
        if (nr > 0 && nr <= Stabliste.size()) {
            Selektion[0] = STAB;
            Selektion[1] = nr;
            setKnopfStab(false);
            setKnopfKnoten(false);
            selModus = NICHTSÄNDERN;
        }
        else {
            Selektion[0] = DESELEKT;
            setKnopfStab(false);
            setKnopfKnoten(false);
            selModus = AUTOMATISCH;
        }
        LayerStKraft(bisher_layerStKraft);
        LayerStNr(bisher_layerStNr);
        selektionAnpassen();
    }
    
    protected void befehlSpracheGewechselt() {
        meldungenRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-meldungen", locale);
        if (meldungenRB == null) {
            System.err.println("FEHLER: gui-meldungen für " + locale.toString());
        }
    }
    
    protected void befehlSpeichern() {
        String meldung = "";
        try {
            fc.resetChoosableFileFilters();
            fc.addChoosableFileFilter(new StdFileFilter("fwk3d", "Fachwerk3D Data"));
            if (dateiname != null) fc.setSelectedFile(new File(dateiname));
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File datei = fc.getSelectedFile();
                if (datei.exists()) {
                    clOK bestätige = new clOK(this, tr("okUEBERSCHREIBEN"), tr("okUEBERSCHREIBENwarnung_Datei")+" "
                    + datei.getName()+" " + tr("okUEBERSCHREIBENwarnung_ueberschreiben"), locale);
                    if (!bestätige.ok()) return;
                }
                dateiname = datei.getPath();
            } else return;
            FileOutputStream fs = new FileOutputStream(dateiname);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeUTF(FILEPROGNAME);
            os.writeInt(HAUPTVER); // HauptversionsNr des Progs
            os.writeInt(FILEVER); // Dateityp-version
            os.writeInt(UNTERVER); // zweistellige Unterversionsnummer
            os.writeObject(Knotenliste);
            os.writeObject(Stabliste);
            os.close();
            meldung = tr("infoGESPEICHERT_Fachwerkdatei") + " " + dateiname + " " + tr("infoGESPEICHERT_gespeichert");
        } catch (IOException e) {
            System.err.println(e.toString());
            feldStatuszeile.setText(e.toString());
            meldung =  tr("errFehlerBeimSpeichern") + " " + e.getMessage();
        }
        this.setTitle(PROGNAME + " - " + dateiname);
        System.out.println(meldung);
        selModus = NICHTSÄNDERN;
        feldStatuszeile.setText(meldung);
    }
    
    protected void befehlBspdaten() {
        if (Knotenliste.size() > 0) {
            //clOK bestätige = new clOK(this, "Beispiel laden", "Allfällige nicht gespeicherte Daten gehen verloren.", locale);
            clOK bestätige = new clOK(this, tr("oKBEISPIEL"), tr("oKBEISPIEL_warnung"), locale);
            if (!bestätige.ok()) return;
        }
        fc = new JFileChooser();
        dateiname = "";
        this.setTitle(PROGNAME);
        neu();
        test();
        Selektion[0] = DESELEKT;
        aktualisieren(true, true);
        setKnopfKnoten(false); setKnopfStab(false);
        selModus = AUTOMATISCH;
    }
    
    protected void befehlZeigeAuflagerkräfte(boolean status) {
        LayerAuflKraft(status);
    }
    
    protected void befehlZeigeKnNr(boolean status) {
        LayerKnNr(status);
    }
    
    protected void befehlZeigeLasten(boolean status) {
        LayerLasten(status);
    }
    
    protected void befehlZeigeStabNr(boolean status) {
        LayerStNr(status);
    }
    
    protected void befehlZeigeStabkräfte(boolean status) {
        LayerStKraft(status);
    }
    
    protected void befehlZeigeHintergrund(boolean status) {
        if (status == true && dxf == null) { // noch kein dxf geladen.
            befehlLadeHintergrund();
            return;
        }
        
        if (status) {
            assert dxf!=null;
            if (mausAufgabe == NEUERKNOTENSNAP)  selModus = HINTERGRUND;
        }
        else {
            if (mausAufgabe == NEUERKNOTENSNAP)  selModus = PKTAUFSTAB;
        }
        LayerHintergrund(status);
    }
    
    
    protected void befehlZoomAll() {
        hp.zoomAll(true);
    }
    
    protected void befehlZoomMaus(boolean status) {
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfNeuerStab(false);
        setKnopfNeuerKnoten(false);
        setKnopfNeuerKnotenSnap(false);
        
        if (status) mausAufgabe = ZOOMxy;
        else mausAufgabe = NICHTS;
    }
    
    protected void befehlZoomIn() {
        Point2D pktm = new Point2D.Double(); // Mittelpunkt
        pktm.setLocation(((hp.getZoomPkte()[0]).getX() + (hp.getZoomPkte()[1]).getX()) / 2d,
        ((hp.getZoomPkte()[0]).getY() + (hp.getZoomPkte()[1]).getY()) / 2d);
        
        double dx = Math.abs((hp.getZoomPkte()[1]).getX() - (hp.getZoomPkte()[0]).getX());
        double dz = Math.abs((hp.getZoomPkte()[1]).getY() - (hp.getZoomPkte()[0]).getY());
        
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        // Zoomfenster halbieren
        pkt1.setLocation(pktm.getX() - dx/4d, pktm.getY() - dz/4d);
        pkt2.setLocation(pktm.getX() + dx/4d, pktm.getY() + dz/4d);
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
    }
    
    protected void befehlZoomOut() {
        Point2D pktm = new Point2D.Double(); // Mittelpunkt
        pktm.setLocation(((hp.getZoomPkte()[0]).getX() + (hp.getZoomPkte()[1]).getX()) / 2d,
        ((hp.getZoomPkte()[0]).getY() + (hp.getZoomPkte()[1]).getY()) / 2d);
        
        double dx = Math.abs((hp.getZoomPkte()[1]).getX() - (hp.getZoomPkte()[0]).getX());
        double dz = Math.abs((hp.getZoomPkte()[1]).getY() - (hp.getZoomPkte()[0]).getY());
        
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        // Zoomfenster verdoppeln
        pkt1.setLocation(pktm.getX() - dx, pktm.getY() - dz);
        pkt2.setLocation(pktm.getX() + dx, pktm.getY() + dz);
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
    }
    
    protected void befehlZoomPan(double dxsi, double dzeta) {   // dxsi und dzeta in m auf der projizierten Ebene
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        pkt1.setLocation(hp.getZoomPkte()[0].getX()-dxsi, hp.getZoomPkte()[0].getY()-dzeta);
        pkt2.setLocation(hp.getZoomPkte()[1].getX()-dxsi, hp.getZoomPkte()[1].getY()-dzeta);
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
    }
    
    protected void befehlZoomxy() {
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        clZoomDialog3D dialog = new clZoomDialog3D(this, locale);
        pkt1 = dialog.get()[0];
        pkt2 = dialog.get()[1];
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
    }
    
    protected void befehlZurücksetzen() {
        Selektion[0] = DESELEKT;
        zurücksetzen(false);
        LayerAuflKraft(false);
        aktualisieren(false, true);
    }
    
    // -------
    // ZUSÄTZE
    // -------
    protected void befehlAddinFindeort() {
       if (Selektion[0] != KNOTEN) {
           System.out.println(tr("errKeinKnotenSelektiert"));
           feldStatuszeile.setText(tr("errKeinKnotenSelektiert"));
           return;
       }
       LayerStNr(true);
       LayerKnNr(false);
       LayerLasten(true);
       LayerStKraft(false);
       clguiFindeOrt3D fo = new clguiFindeOrt3D(Selektion[1], Knotenliste, Stabliste, this, locale);
       aktualisieren(true, true);
       LayerStNr(false);
       LayerStKraft(true);
       LayerLasten(true);
       LayerAuflKraft(true);
       selektionAnpassen();
       
       if (fo.getMeldung().equals("")) ;//nichts tun
       else feldStatuszeile.setText(fo.getMeldung());
    }
    
    protected void befehlAddinRateStabkräfte() {
        double EAdruck_zu_EAzug = 10; // +- willkürlich, entspricht etwa ρ = 1.6 %
        String meldungElastischGesetzt = "";
        
        zurücksetzen(false);
        
        // GUI Vorarbeiten
        switch (mausAufgabe) {
            case NEUERSTAB:
                mausAufgabe = NICHTS;
                setKnopfNeuerStab(false);
                break;
            case NEUERKNOTENSNAP:
                mausAufgabe = NICHTS;
                setKnopfNeuerKnotenSnap(false);
                break;
            case NEUERKNOTEN:
                assert false;
                mausAufgabe = NICHTS;
                break;
            case ZOOMxy: // lassen
            default:
        }
        
        // BEGINN
        keinFEHLER = true;
        VOLLSTÄNDIGGELÖST_OK = false;
        //Datenaufbereiten
        clKnoten3D[] Knotenarray = new clKnoten3D[Knotenliste.size() + 1];
        clStab[] Stabarray = new clStab[Stabliste.size() + 1];
        int[][] Topologie = new int[Knotenarray.length][Knotenarray.length];
        
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten3D) it.next();
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            clWissenderStab3D wst = (clWissenderStab3D) it.next();
            Stabarray[i] = wst.stab;
            Topologie[wst.von][wst.bis] = i;
            i++;
        }
        
        try {
            clFachwerk3D fachwerk = new clFachwerk3D(Knotenarray, Stabarray, Topologie);
            fachwerk.setVerbose(OptionVerbose);
            keinWIDERSPRUCH = fachwerk.rechnen(OptionVorber,true,OptionMechanismus); // OptionGLS=true
            if (OptionVerbose) fachwerk.resultatausgabe_direkt();
            if (keinWIDERSPRUCH) {
                VOLLSTÄNDIGGELÖST_OK = fachwerk.istvollständiggelöst(false); // false, das resutatcheck() soeben in .rechnen() durchgeführt
                
                if (VOLLSTÄNDIGGELÖST_OK) {
                    System.out.println("completely solved, nothing to guess."); // TODO übersetzen
                }
                else {
                    // Elastische Berechnung
                    clElastisch3D elast = new clElastisch3D(Stabarray);
                    elast.setL(Knotenarray, Topologie);
                    elast.setCompleteSolution(fachwerk.getCompleteSolution());
                    elast.rechnen(EAdruck_zu_EAzug);
                    if (OptionVerbose) elast.resultatausgabe_direkt();
                    double[] N = elast.getLsg();
                    int[] zusetzendeSt = elast.getIndexMgdUmbek();
                    System.out.println("");
                    System.out.println("automatisch gesetzte Staebe (elastisch berechnet, EAc/EAt="+EAdruck_zu_EAzug+")");
                    for (int s = 0; s < zusetzendeSt.length; s++) {
                        System.out.println("Stab " + Fkt.nf(zusetzendeSt[s]+1,2) + ": N = " + Fkt.nf(N[zusetzendeSt[s]],1,4) + " kN");
                    }
                    
                    // Setzten der quasi-elastisch berechneten Stabkräfte
                    StringBuffer sb = new StringBuffer();
                    sb.append("neu gesetzt: ");
                    for (int s = 0; s < zusetzendeSt.length; s++) {
                        int stabnr = zusetzendeSt[s]+1;
                        assert Stabarray[stabnr].getStatus() == UNBEST: "Stab der gesetzt werden soll ist gar nicht unbestimmt!";
                        Stabarray[stabnr].setKraft(GESETZT, N[zusetzendeSt[s]]);
                        if (s > 0) sb.append(", ");
                        sb.append(stabnr);
                    }
                    meldungElastischGesetzt = sb.toString();
                }
            }
            
            aktualisieren(true, true);
            if (meldungElastischGesetzt.length() > 0) feldStatuszeile.setText(meldungElastischGesetzt);
            if (!keinWIDERSPRUCH) LayerMechanismius(true, fachwerk.getMechanismus());
            if (keinWIDERSPRUCH) LayerStKraft(true);
            LayerAuflKraft(true);
            LayerLasten(true);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            aktualisieren(true, true);
            // im Statusfeld FEHLER anzeigen, in Statuszeile Fehlermeldung
            feldStatusFw.setText(tr("FEHLER"));
            keinFEHLER = false;
            if (e.getMessage() != null && e.getMessage().equals("Mechanismusberechnung fehlgeschlagen.")) { // TODO testen e.getMessage() != null
                feldStatuszeile.setText(tr("errMechanismusberFehlgeschlagen"));
            }
            else feldStatuszeile.setText(e.toString());
        }
        
        if (keinWIDERSPRUCH && keinFEHLER) {
            if (VOLLSTÄNDIGGELÖST_OK) feldStatusFw.setText(tr("OK-COMPLETE"));
            else feldStatusFw.setText(tr("OK"));
            selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
        }
        else {
            if (!keinWIDERSPRUCH) feldStatusFw.setText(tr("WIDERSPRUCH"));
            if (!keinFEHLER) feldStatusFw.setText(tr("FEHLER"));
            selModus = NICHTSÄNDERN;
        }
        Selektion[0] = DESELEKT;
        setKnopfKnoten(false); setKnopfStab(false);
    }
    
    protected void befehlAddinKoordTransFWK() {
        clKoordTransFWK3D ctfwk = new clKoordTransFWK3D(Knotenliste, this, locale);
        zurücksetzen(false);
        aktualisieren(true, true);
        selektionAnpassen();
    }
    
    protected void befehlAddinKoordTransDXF() {
        if (dxf != null) {
            clKoordTransDXF3D ctdxf = new clKoordTransDXF3D(dxf, this, locale);
            hp.neuzeichnen();
        }
    }
    
    protected void befehlAddinSkaliereLasten() {
        clLastenSkalieren3D sl = new clLastenSkalieren3D(Knotenliste, Stabliste, this, locale);
        if (sl.isModified()) { // Skalierung wurde durchgeführt, d.h. nicht abgebrochen und Faktor!=1
            zurücksetzen(false);
            aktualisieren(true, true);
            LayerLasten(true);
            LayerStKraft(true);
            selektionAnpassen();
        }
    }
    
    protected void befehlAddinExportInput() {
        String outfile;
        if (dateiname.length() > 0) outfile = dateiname + ".out.csv";
        else outfile = "fwk3d.out.csv";
        clExportInput3D exp = new clExportInput3D(Knotenliste, Stabliste, outfile, locale);
    }
    
    
    // ----------------
    // INTERNE METHODEN
    // ----------------
    
    void test() {
        clKnoten3D aktuellKn;
        clWissenderStab3D aktuellSt;
        
        Knotenliste.add(new clKnoten3D( 0, 0, 0.1));
        Knotenliste.add(new clKnoten3D( 1, 1, 0.9));
        Knotenliste.add(new clKnoten3D( 1,-1, 0.9));
        Knotenliste.add(new clKnoten3D(-1,-1, 0.9));
        Knotenliste.add(new clKnoten3D(-1, 1, 0.9));
        
        Stabliste.add(new clWissenderStab3D(new clStab(),5,4));
        Stabliste.add(new clWissenderStab3D(new clStab(),4,3));
        Stabliste.add(new clWissenderStab3D(new clStab(),3,2));
        Stabliste.add(new clWissenderStab3D(new clStab(),5,2));
        Stabliste.add(new clWissenderStab3D(new clStab(),1,2));
        Stabliste.add(new clWissenderStab3D(new clStab(),1,3));
        Stabliste.add(new clWissenderStab3D(new clStab(),1,4));
        Stabliste.add(new clWissenderStab3D(new clStab(),1,5));
        Stabliste.add(new clWissenderStab3D(new clStab(),5,3));
        
        // Lagerbed und Lasten zuordnen
        aktuellKn = (clKnoten3D) Knotenliste.get(2-1); aktuellKn.setLager(VERSCHIEBLICH, 0,0,1);
        aktuellKn = (clKnoten3D) Knotenliste.get(3-1); aktuellKn.setLager(VERSCHIEBLICH, 0,0,1);
        aktuellKn = (clKnoten3D) Knotenliste.get(4-1); aktuellKn.setLager(VERSCHIEBLICH, 0,0,1);
        aktuellKn = (clKnoten3D) Knotenliste.get(5-1); aktuellKn.setLager(VERSCHIEBLICH, 0,0,1);
        aktuellKn = (clKnoten3D) Knotenliste.get(1-1); aktuellKn.setLast(0,0,500);
        
        // eine Stabkraft zuweisen
        aktuellSt = (clWissenderStab3D) Stabliste.get(9-1);
        aktuellSt.stab.setKraft(GESETZT, 100);
    }
    
    /** Erstellt das Hauptpanel (Zeichnung) neu.
     * @param LAYERAUS Schaltet alle Layer aus.
     * @param STANDARDSICHT Stellt den Standard-Blickwinkel wieder her.
     */
    void aktualisieren(boolean LAYERAUS, boolean STANDARDSICHT) {
        clKnoten3D[] Knotenarray = new clKnoten3D[Knotenliste.size() + 1];
        clWissenderStab3D[] Stabarray = new clWissenderStab3D[Stabliste.size() + 1];
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten3D) it.next();
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            Stabarray[i] = (clWissenderStab3D) it.next();
            i++;
        }
        java.awt.Dimension grösse = null;
        double[] bisherigeBlickrtg = null;
        Point2D[] bisherigeZoomPkte = null;
        boolean bisherigZoomAll = true; // wird überschrieben
        
        if (hp != null) {
            grösse = hp.getSize();
            if (!STANDARDSICHT) {
                bisherigeBlickrtg = hp.getBlickRtg();
                bisherigeZoomPkte = hp.getZoomPkte();
                bisherigZoomAll = hp.istZoomAll();
            }
            this.getContentPane().remove(hp);
        }
        
        hp = new clHauptPanel3D(Knotenarray, Stabarray, false);
        if (grösse != null) hp.setPreferredSize(grösse);
        if (!STANDARDSICHT) {
            if (bisherigeBlickrtg != null) hp.setBlickRtg(bisherigeBlickrtg);
            if (bisherigeZoomPkte != null) hp.zoomxy(bisherigeZoomPkte[0], bisherigeZoomPkte[1]);
            if (bisherigZoomAll) hp.zoomAll(false); // zoomAll(false), da der Befehl zum Neuzeichnen sowieso folgt.
        }
        schieberBlickrtgSetzen();
        
        this.getContentPane().add(hp, java.awt.BorderLayout.CENTER);
        
        if (LAYERAUS) {        // setzt Knöpfe zurück
            setLayerKnNr(false);
            setLayerStNr(false);
            setLayerStKraft(false);
            setLayerLasten(false);
            setLayerAuflKraft(false);
            setLayerHintergrund(false);
        }
        else {                  // setzt Knöpfe (und v.a. Hauptpanel hp) auf Zustand vor Aktualisierung
            befehlZeigeKnNr(LayerKnNr());
            befehlZeigeStabNr(LayerStNr());
            befehlZeigeStabkräfte(LayerStKraft());
            befehlZeigeLasten(LayerLasten());
            befehlZeigeAuflagerkräfte(LayerAuflKraft());
            befehlZeigeHintergrund(LayerHintergrund());
        }
        
        // Fachwerk-Statusfeld und Statuszeile zurücksetzen
        feldStatusFw.setText("");
        feldStatuszeile.setText("");
        
        //Selektion[0] = DESELEKT;
        
        invalidate(); pack();
        repaint();
        //mausaktiv = true;
        //setKnopfKnoten(false); setKnopfStab(false);
        //selModus = AUTOMATISCH;
    }
    
    void neu() {
        System.out.println(tr("infoNEUESFACHWERKMODELL"));
        Knotenliste = new LinkedList();
        Stabliste = new LinkedList();
        keinFEHLER = true;
        keinWIDERSPRUCH = true;
        VOLLSTÄNDIGGELÖST_OK = false;
        //mausaktiv = false;
        
        if (hp != null) this.getContentPane().remove(hp);
        hp = null;
        
        Selektion[0] = DESELEKT;
        aktualisieren(true, true);
        setKnopfKnoten(false); setKnopfStab(false);
        selModus = AUTOMATISCH;
    }
    
    /** Setzt die Berechnung zurück. Alle Stabkräfte sind danach unbestimmt, die Knoten offen.
     * @param alles true: Löscht zusätzlich alle gesetzen Stabkräfte.
     */
    public void zurücksetzen(boolean alles) { // true würde auch gesetzte Kraft löschen
        clKnoten3D aktKn;
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKn = (clKnoten3D) it.next();
            aktKn.zurücksetzen();
            i++;
        }
        
        clWissenderStab3D aktWSt;
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab3D) it.next();
            aktWSt.stab.zurücksetzen(alles);
            i++;
        }
        
        keinFEHLER = true;
        keinWIDERSPRUCH = true;
        VOLLSTÄNDIGGELÖST_OK = false;
        
        Selektion[0] = DESELEKT;
        LayerMechanismius(false, null);
        // Aktualisiert Hauptpanel. Falls man die Standardsicht (2.Param) will, aktualisieren(false,true) separat aufrufen.
        aktualisieren(false, false);
        setKnopfKnoten(false); setKnopfStab(false);
        selModus = AUTOMATISCH;
    }
    
    void LayerHintergrund(boolean status) {
        hp.ZeigeHintergrund(status, dxf);
        setLayerHintergrund(status);
    }
    void LayerKnNr(boolean status) {
        hp.ZeigeKnNr(status);
        setLayerKnNr(status);
    }
    void LayerStNr(boolean status) {
        hp.ZeigeStabNr(status);
        setLayerStNr(status);
    }
    void LayerStKraft(boolean status) {
        hp.ZeigeStabkräfte(status);
        setLayerStKraft(status);
    }
    void LayerLasten(boolean status) {
        hp.ZeigeLasten(status);
        setLayerLasten(status);
    }
    void LayerAuflKraft(boolean status) {
        hp.ZeigeAuflagerkräfte(status);
        setLayerAuflKraft(status);
    }
    void LayerMechanismius(boolean status, double[][] mechanismusRelKnVersch) {
        hp.ZeigeMechanismus(status, mechanismusRelKnVersch);
    }
    
    void selektionAnpassen() {
        // selektierten Stab / Knoten hervorheben
        hp.selekt(Selektion);
        // Statuszeile anpassen
        String text;
        double N; // Stabkraft
        double dx, dy, dz; // Abstand zwischen Knoten in Koord.rtg.
        double ax, ay, az; // Komponenten: Nx = ax*N
        
        switch (Selektion[0]) {
            case DESELEKT:
                text = "";
                break;
                
            case STAB:
                clWissenderStab3D aktWSt = (clWissenderStab3D) Stabliste.get(Selektion[1]-1);
                switch (aktWSt.stab.getStatus()) {
                    case BER:
                        N = aktWSt.stab.getKraft();
                        // Kraftkomponenten
                        dx = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getX() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getX();
                        dy = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getY() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getY();
                        dz = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getZ() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getZ();
                        ax = Math.abs(dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        ay = Math.abs(dy / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        az = Math.abs(dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        
                        text = tr("StabNr") + " " + Selektion[1] + ": N = " + Fkt.nf(N,1)
                                + " kN (" + tr("berechnet") + ")";
                        text += "  Nx=" + Fkt.nf(ax*N,1) + "kN Ny=" + Fkt.nf(ay*N,1) + "kN Nz=" + Fkt.nf(az*N,1) + "kN";
                        break;
                    case GESETZT:
                        N = aktWSt.stab.getKraft();
                        // Kraftkomponenten
                        dx = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getX() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getX();
                        dy = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getY() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getY();
                        dz = ((clKnoten3D) Knotenliste.get(aktWSt.bis-1)).getZ() - ((clKnoten3D) Knotenliste.get(aktWSt.von-1)).getZ();
                        ax = Math.abs(dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        ay = Math.abs(dy / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        az = Math.abs(dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dy,2d) + Math.pow(dz,2d)));
                        
                        text = tr("StabNr") + " " + Selektion[1] + ": N = " + Fkt.nf(N,1)
                                + " kN (" + tr("gesetzt") + ")";
                        text += "  Nx=" + Fkt.nf(ax*N,1) + "kN Ny=" + Fkt.nf(ay*N,1) + "kN Nz=" + Fkt.nf(az*N,1) + "kN";
                        break;
                    case UNBEST:
                        text = tr("StabNr") + " " + Selektion[1] + ": " + tr("unbestimmt");
                        break;
                    default:
                        text = tr("StabNr") + " " + Selektion[1] + ": " + tr("Status") + " " + aktWSt.stab.getStatus();
                }
                break;
                
            case KNOTEN:
                clKnoten3D aktKn = (clKnoten3D) Knotenliste.get(Selektion[1]-1);
                text = tr("Knoten") + " " + Selektion[1] + ": x = " + Fkt.nf(aktKn.getX(), 2)
                + ", y = " + Fkt.nf(aktKn.getY(), 2)+ ", z = " + Fkt.nf(aktKn.getZ(), 2) + ";  ";
                // Last
                if (aktKn.getLx() != 0 || aktKn.getLy() != 0 || aktKn.getLz() != 0) {
                    text += "Lx = " + Fkt.nf(aktKn.getLx(),1) + " kN, Ly = " + Fkt.nf(aktKn.getLy(),1) + " kN, " +
                    "Lz = " + Fkt.nf(aktKn.getLz(),1) + " kN;  ";
                }
                // Lagerung
                switch (aktKn.getLagerbed()) {
                    case LOS:
                        break;
                    case FIX:
                        switch (aktKn.getLagerstatus()) {
                            case OFFEN:
                                text += tr("festgelagert") + " (" + tr("offen") + ")";
                                break;
                            case BER:
                                text += "Ax = " + Fkt.nf(aktKn.getRx(),1) + " kN, Ay = " + Fkt.nf(aktKn.getRy(),1) + " kN, Az = "
                                        + Fkt.nf(aktKn.getRz(),1)  + " kN (" + tr("festgelagert") + ");  ";
                                break;
                            case GESETZT:
                                assert false: " noch nicht implementiert";
                            default:
                                assert false;
                        }
                        break;
                    case VERSCHIEBLICH:
                        double[] blRtg = aktKn.getRrtg(); // blockierte Rtg
                        switch (aktKn.getLagerstatus()) {
                            case OFFEN:
                                text += tr("verschieblich") + ", " + tr("blockierteRtg") + " dx=" + Fkt.nf(blRtg[0],3)
                                        + " dy=" + Fkt.nf(blRtg[1],3) + " dz=" + Fkt.nf(blRtg[2],3) + " (" + tr("offen") + ")";
                                break;
                            case BER:
                                text += "Ax = " + Fkt.nf(aktKn.getRx(),1) + " kN, Ay = " + Fkt.nf(aktKn.getRy(),1) + " kN, Az = "
                                        + Fkt.nf(aktKn.getRz(),1)  + " kN (" + tr("verschieblich") + ");  ";
                                break;
                            case GESETZT:
                                assert false: " noch nicht implementiert";
                            default:
                                assert false;
                        }
                        break;
                    case SCHINENLAGER:
                        double[] frRtg = aktKn.getRrtg(); // freie Rtg
                        switch (aktKn.getLagerstatus()) {
                            case OFFEN:
                                text += tr("schinenlager") + ", " + tr("freieRtg") + " dx=" + Fkt.nf(frRtg[0],3)
                                        + " dy=" + Fkt.nf(frRtg[1],3) + " dz=" + Fkt.nf(frRtg[2],3) + " (" + tr("offen") + ")";
                                break;
                            case BER:
                                text += "Ax = " + Fkt.nf(aktKn.getRx(),1) + " kN, Ay = " + Fkt.nf(aktKn.getRy(),1) + " kN, Az = "
                                        + Fkt.nf(aktKn.getRz(),1)  + " kN (" + tr("schinenlager") + ");  ";
                                break;
                            case GESETZT:
                                assert false: " noch nicht implementiert";
                            default:
                                assert false;
                        }
                        break;
                    default:
                        assert false;
                }
                break;
                
            default:
                text = "";
                assert false;
        }
        feldStatuszeile.setText(text);
    }
    
    private String tr(String key) {
        String übersetzt;
        try {übersetzt = meldungenRB.getString(key);}
        catch (MissingResourceException e) {
            System.err.println("Schluesselwort " + key + " nicht gefunden fuer " + locale.toString() + " ; " + e.toString());
            return key;
        }
        return übersetzt;
    }
    
    private void schieberBlickrtgSetzen() {
        double n[] = hp.getBlickRtg();
        double r = Math.sqrt(n[0]*n[0] + n[1]*n[1] + n[2]*n[2]);
        double teta = Math.asin(n[2] / r);
        double winkelVer = Math.toDegrees(teta);
        double phi = Math.asin(- n[0] / (r * Math.cos(teta)));
        if (- n[1] / (r * Math.cos(teta)) < 0) {
            if (phi > 0) phi = Math.PI - phi;
            else phi = -Math.PI - phi;
        }
        double winkelHor = Math.toDegrees(phi);
        
        sliderHorizontal.setValue((int) winkelHor);
        sliderVertical.setValue((int) winkelVer);
        // debug
        //System.out.println("n: "+n[0] +" "+ n[1] +" "+ n[2]);
        //System.out.println("winkelHor = " + Fkt.nf(winkelHor,1));
        //System.out.println("winkelVer = " + Fkt.nf(winkelVer,1));
    }
    
    // -------------------
    // Handlungen der Maus
    // -------------------
    private Point2D von = new Point2D.Double(); // in projizierten Fachwerkkoord (zB Metern)
    private Point2D bis = new Point2D.Double(); // in projizierten Fachwerkkoord (zB Metern)
    int vonnr = -1;
    private boolean PAN = false; // für PAN
    private clPan pan; // für PAN, Behälter für die nötigen Variablen.
    
    
    protected void nachrichtMausGeklickt(java.awt.event.MouseEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        switch (selModus) {
            case KNOTEN:
                if (Selektion[0] == KNOTEN) {
                    if (maus.getButton() == MouseEvent.BUTTON1) {
                        selModus = NICHTSÄNDERN;
                        setKnopfKnoten(false);
                    }
                }
                break;
            case STAB:
                if (Selektion[0] == STAB) {
                    if (maus.getButton() == MouseEvent.BUTTON1) {
                        selModus = NICHTSÄNDERN;
                        setKnopfStab(false);
                    }
                }
                break;
            case HINTERGRUND:
                assert mausAufgabe == NEUERKNOTENSNAP;
                double[] pktkoord = snapHintergrund(hp.getKoord(), mzp(maus.getPoint()));
                if (pktkoord[0] > 0) {
                    Knotenliste.add(new clKnoten3D(pktkoord[1],pktkoord[2],pktkoord[3]));
                    zurücksetzen(false);
                    Selektion[0] = KNOTEN;
                    Selektion[1] = Knotenliste.size();
                    selModus = NICHTSÄNDERN;
                    selektionAnpassen();
                }
                else selModus = AUTOMATISCH;
                mausAufgabe = NICHTS;
                setKnopfNeuerKnotenSnap(false); // nötig für JRE1.5, da wegziehen und Maustaste loslassen neu als Klick gilt.
                hp.deselektHintergrund();
                break;
            case PKTAUFSTAB:
                assert mausAufgabe == NEUERKNOTENSNAP;
                double[] stabpktkoord = snapStabteiler(hp.getKoord(), mzp(maus.getPoint()));
                if (stabpktkoord[0] > 0) {
                    int stabnr = (int) stabpktkoord[0];
                    clWissenderStab3D stab = (clWissenderStab3D) Stabliste.get(stabnr-1);
                    // Neuen Knoten auf bestehendem Stab hinzufügen, 2 neue Stäbe, alter Stab löschen.
                    Knotenliste.add(new clKnoten3D(stabpktkoord[1] ,stabpktkoord[2] ,stabpktkoord[3]));
                    Stabliste.add(new clWissenderStab3D(new clStab(), stab.von, Knotenliste.size()));
                    Stabliste.add(new clWissenderStab3D(new clStab(), Knotenliste.size(), stab.bis));
                    // falls der zu löschende Stab eine gesetzte Kraft hatte, diese einem der beiden neuen Stäben zuweisen.
                    if (stab.stab.getStatus() == GESETZT) {
                        double N = stab.stab.getKraft();
                        ((clWissenderStab3D) Stabliste.getLast()).stab.setKraft(GESETZT, N);
                    }
                    Stabliste.remove(stabnr - 1);
                    zurücksetzen(false);
                    Selektion[0] = KNOTEN;
                    Selektion[1] = Knotenliste.size();
                    selModus = NICHTSÄNDERN;
                    selektionAnpassen();
                }
                else selModus = AUTOMATISCH;
                mausAufgabe = NICHTS;
                setKnopfNeuerKnotenSnap(false); // nötig für JRE1.5, da wegziehen und Maustaste loslassen neu als Klick gilt.
                hp.deselektHintergrund();
                break;
            case NICHTSÄNDERN:
                if (maus.getButton() == MouseEvent.BUTTON1) {
                    Selektion[0] = DESELEKT;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    selektionAnpassen();
                }
                if (maus.getButton() == MouseEvent.BUTTON3) befehlEigenschaften();
                break;
            case DESELEKT: // noch nicht gebraucht
                break;
            case AUTOMATISCH:
                if (maus.getButton() == MouseEvent.BUTTON3) { // rechte Maustaste
                    if (Selektion[0] == STAB || Selektion[0] == KNOTEN) befehlEigenschaften();
                }
                break;
            default:
        }
    
    }
    
    protected void nachrichtMausBewegt(java.awt.event.MouseEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        if (!mausimPanel(maus)) {
            feldX.setText(""); feldZ.setText("");
            return;
        }
        
        clKoord3D koord = hp.getKoord();
        Point2D ort = new Point2D.Double(); // in Fachwerkkoord (zB Metern)
        ort = koord.m(mzp(maus.getPoint()));
        // Mauskoord (im m) anzeigen
        feldX.setText(""+Fkt.nf(ort.getX(),2));
        feldZ.setText(""+Fkt.nf(ort.getY(),2));
        // Selektionsmodus
        switch (selModus) {
            case AUTOMATISCH: // im Anzeigemodus
                Selektion = snapKnoten(koord, mzp(maus.getPoint()));
                if (Selektion[0] != KNOTEN) Selektion = snapStab(koord, mzp(maus.getPoint()));
                selektionAnpassen();
                break;
            case KNOTEN:
                Selektion = snapKnoten(koord, mzp(maus.getPoint()));
                selektionAnpassen();
                break;
            case STAB:
                Selektion = snapStab(koord, mzp(maus.getPoint()));
                selektionAnpassen();
                break;
            case HINTERGRUND:
                double[] pktkoord = snapHintergrund(koord, mzp(maus.getPoint()));
                if (pktkoord[0] > 0) {
                    hp.selektHintergrund(pktkoord[1], pktkoord[2], pktkoord[3]);
                    feldStatuszeile.setText("x = " + Fkt.nf(pktkoord[1], 3) + " y = " + Fkt.nf(pktkoord[2], 3)+ " z = " + Fkt.nf(pktkoord[3], 3));
                }
                else {
                    hp.deselektHintergrund();
                    feldStatuszeile.setText("");
                }
                //selektionAnpassen();
                break;
            case PKTAUFSTAB:
                double[] stabpktkoord = snapStabteiler(koord, mzp(maus.getPoint()));
                if (stabpktkoord[0] > 0) {
                    hp.selektHintergrund(stabpktkoord[1], stabpktkoord[2], stabpktkoord[3]);
                    feldStatuszeile.setText(tr("StabNr") + " " + (int)stabpktkoord[0]
                            + " x = " + Fkt.nf(stabpktkoord[1], 3) + " y = " + Fkt.nf(stabpktkoord[2], 3) + " z = " + Fkt.nf(stabpktkoord[3], 3));
                }
                else {
                    hp.deselektHintergrund();
                    feldStatuszeile.setText("");
                }
                break;
            case NICHTSÄNDERN:
            default:
        }
    }
    
    protected void nachrichtMausGedrückt(java.awt.event.MouseEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        if (!mausimPanel(maus)) {
            // ev von verändern
            return;
        }
        clKoord3D koord = hp.getKoord();
        int[] sel;
        switch (mausAufgabe) {
            case ZOOMxy:
                von = new Point2D.Double();
                von = koord.m(mzp(maus.getPoint()));
                break;
            case NEUERSTAB:
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                von = new Point2D.Double();
                if (sel[0] == KNOTEN) {
                    clKnoten3D vonkn = (clKnoten3D) Knotenliste.get(sel[1]-1);
                    Point3D von3d = new Point3D(vonkn.getX(), vonkn.getY(), vonkn.getZ());
                    von.setLocation(koord.projiziere(von3d));
                    Selektion = sel;
                    vonnr = sel[1];
                }
                else {
                    vonnr = -1;
                    return;
                }
                break;
                
            case NEUERKNOTEN: // wird nicht durch MausGedrückt bedient
            case NEUERKNOTENSNAP: // wird nicht durch MausGedrückt bedient
            case NICHTS:
            default:
                // für Zoom-Pan
                if (PAN) PAN = false; // um zu verhindern, dass PAN aktiv bleibt, wenn der Mauszeiger aus dem Panel hinausgezogen wird.
                // PAN: mittlere Maustaste oder Ctrl-linke Maustaste
                if (maus.getButton()==MouseEvent.BUTTON2 || (maus.getModifiersEx()==(MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK))) {
                    PAN = true;
                    pan = new clPan(mzp(maus.getPoint()), koord.m(1d), hp.getZoomPkte());
                }
        }
    }
    
    /** zeichnet lediglich ein Rechteck oder Strich solange die Maus gezogen wird
     */
    protected void nachrichtMausGezogen(java.awt.event.MouseEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        if (!mausimPanel(maus)) return;
        clKoord3D koord = hp.getKoord();
        bis = koord.m(mzp(maus.getPoint()));
        feldX.setText(""+Fkt.nf(bis.getX(),2));
        feldZ.setText(""+Fkt.nf(bis.getY(),2));
        int[] sel;
        //System.out.println("bis: " + bis.toString());
        switch (mausAufgabe) {
            case ZOOMxy:
                hp.ZeigeHilfsRechteck(true, koord.panel(von), koord.panel(bis));
                break;
            case NEUERSTAB:
                if (vonnr < 1) return;
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                if (sel[0] == KNOTEN) {
                    clKnoten3D biskn = (clKnoten3D) Knotenliste.get(sel[1]-1);
                    Point3D bis3d = new Point3D(biskn.getX(), biskn.getY(), biskn.getZ());
                    bis.setLocation(koord.projiziere(bis3d));
                    feldX.setText(""+Fkt.nf(bis.getX(),2));
                    feldZ.setText(""+Fkt.nf(bis.getY(),2));
                    if (von.equals(bis)) {
                        return;
                    }
                }
                Selektion = sel;
                selektionAnpassen();
                hp.ZeigeHilfslinie(true, koord.panel(von), koord.panel(bis));
                break;
                
            case NEUERKNOTEN: // wird nicht durch MausGezogen bedient
            case NEUERKNOTENSNAP:  // wird nicht durch MausGezogen bedient
            case NICHTS:
            default:
                // PAN: mittlere Maustaste oder Ctrl-linke Maustaste
                if (PAN) {
                    hp.zoomxy(pan.getOL(mzp(maus.getPoint())), pan.getUR(mzp(maus.getPoint())));
                }
        }
    }
    
    protected void nachrichtMausLosgelassen(java.awt.event.MouseEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        hp.ZeigeHilfslinie(false);
        hp.ZeigeHilfsRechteck(false);
        if (!mausimPanel(maus)) {
            mausAufgabe = NICHTS;
            selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
            setKnopfNeuerStab(false);
            setKnopfZoomMaus(false);
            return;
        }
        clKoord3D koord = hp.getKoord();
        bis = koord.m(mzp(maus.getPoint()));
        int[] sel;
        switch (mausAufgabe) {
            case ZOOMxy:
                if (!von.equals(bis)){
                    hp.zoomxy(von, bis);
                }
                else {
                }
                mausAufgabe = NICHTS;
                selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                setKnopfZoomMaus(false);
                break;
            case NEUERSTAB:
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                if (sel[0] != KNOTEN) {
                    setKnopfNeuerStab(false);
                    mausAufgabe = NICHTS;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    return;
                }
                clKnoten3D biskn = (clKnoten3D) Knotenliste.get(sel[1]-1);
                Point3D bis3d = new Point3D(biskn.getX(), biskn.getY(), biskn.getZ());
                bis.setLocation(koord.projiziere(bis3d));
                //bis.setLocation(((clKnoten3D) Knotenliste.get(sel[1]-1)).getX(),
                //                ((clKnoten3D) Knotenliste.get(sel[1]-1)).getZ());
                if (von.equals(bis)) {
                    setKnopfNeuerStab(false);
                    mausAufgabe = NICHTS;
                    return;
                }
                if (vonnr < 1) {
                    String meldung = tr("errKeinGueltigerAusgangsknoten");
                    System.out.println(meldung);
                    feldStatuszeile.setText(meldung);
                    setKnopfNeuerStab(false);
                    mausAufgabe = NICHTS;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    return;
                }
                // Kontrolle, ob nicht schon ein Stab zwischen diesen Knoten existiert
                boolean stabschonvorhanden = false;
                clWissenderStab3D aktWSt;
                for (Iterator it = Stabliste.iterator(); it.hasNext();) {
                    aktWSt = (clWissenderStab3D) it.next();
                    if (aktWSt.von == vonnr && aktWSt.bis == sel[1]) stabschonvorhanden = true;
                    if (aktWSt.von == sel[1] && aktWSt.bis == vonnr) stabschonvorhanden = true;
                    if (stabschonvorhanden) break;
                }
                if (stabschonvorhanden) {
                    //String meldung = "Eingabefehler: Ein Stab zwischen diesen Knoten existiert bereits!";
                    String meldung = tr("errStabSchonVorhanden");
                    System.out.println(meldung);
                    feldStatuszeile.setText(meldung);
                    setKnopfNeuerStab(false);
                    mausAufgabe = NICHTS;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    return;
                }
                
                Stabliste.add(new clWissenderStab3D(new clStab(), vonnr, sel[1]));
                zurücksetzen(false);
                selModus = NICHTSÄNDERN;
                Selektion[0] = STAB;
                Selektion[1] = Stabliste.size();
                selektionAnpassen();
                setKnopfNeuerStab(false);
                mausAufgabe = NICHTS;
                break;
                
            case NEUERKNOTEN: // wird nicht durch MausLosgelassen bedient
            case NEUERKNOTENSNAP: // wird nicht durch MausLosgelassen bedient
            case NICHTS:
            default:
                // PAN: mittlere Maustaste oder Ctrl-linke Maustaste
                if (PAN) {
                    hp.zoomxy(pan.getOL(mzp(maus.getPoint())), pan.getUR(mzp(maus.getPoint())));
                }
        }
    }
    
    protected void nachrichtMausRadGedreht(java.awt.event.MouseWheelEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        if (!mausimPanel(maus)) return;
        int rtg = maus.getWheelRotation();
        if (rtg == 0) assert false;
        
        Point2D pktm = new Point2D.Double(); // Zoomzentrum (Punkt, der nicht verschoben wird.)
        clKoord3D koord = hp.getKoord();
        pktm = koord.m(mzp(maus.getPoint()));
        
        double dxl = Math.abs(pktm.getX() - (hp.getZoomPkte()[0]).getX());
        double dxr = Math.abs((hp.getZoomPkte()[1]).getX() - pktm.getX());
        double dzo = Math.abs(pktm.getY() - (hp.getZoomPkte()[0]).getY());
        double dzu = Math.abs((hp.getZoomPkte()[1]).getY() - pktm.getY());
        
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        if (rtg > 0) { // hinauszoomen
            // Zoomfenster vergrössern: mit drei Klicks verdoppeln
            pkt1.setLocation(pktm.getX() - Math.pow(2d, 1d/3d) * dxl, pktm.getY() - Math.pow(2d, 1d/3d) * dzo);
            pkt2.setLocation(pktm.getX() + Math.pow(2d, 1d/3d) * dxr, pktm.getY() + Math.pow(2d, 1d/3d) * dzu);
            if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
        }
        if (rtg < 0) { // hineinzoomen
            // Zoomfenster verkleinern: mit drei Klicks halbieren
            pkt1.setLocation(pktm.getX() - Math.pow(0.5, 1d/3d) * dxl, pktm.getY() - Math.pow(0.5, 1d/3d) * dzo);
            pkt2.setLocation(pktm.getX() + Math.pow(0.5, 1d/3d) * dxr, pktm.getY() + Math.pow(0.5, 1d/3d) * dzu);
            if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
        }
    }
    
    /** Wandelt Punkt von Mauskoordinaten (Frame treillis) in Hauptpanel-koordinaten (Zeichnungspapier).
     * Abkürzung für "Maus- zu Panelkoordinaten"
    */
    private Point2D mzp(java.awt.Point pkt_inMauskoord) {
        double x = pkt_inMauskoord.getX();
        double z = pkt_inMauskoord.getY();
        x += this.getLocationOnScreen().getX() - hp.getLocationOnScreen().getX();
        z += this.getLocationOnScreen().getY() - hp.getLocationOnScreen().getY();
        //debug:
        //System.out.println("xPix = " +  x + ", zPix = " + z) ;
        Point2D pkt_inPanelkoord = new Point2D.Double();
        pkt_inPanelkoord.setLocation(x,  z);
        return pkt_inPanelkoord;
    }
    
    private boolean mausimPanel(java.awt.event.MouseEvent maus) {
        Point2D OL = new Point2D.Double();
        OL.setLocation(0, 0);
        Point2D OR = new Point2D.Double();
        OR.setLocation(hp.getWidth() , 0);
        double B = hp.getHeight();
        Point2D mauspkt_inPanel = new Point2D.Double();
        mauspkt_inPanel = mzp(maus.getPoint());
        return mausimRechteck(mauspkt_inPanel, OL, OR, B);
    }
    
    /** Rechteck in Rtg n (definiert durch P1 und P2) und mit Breite t = B
     */
    private boolean mausimRechteck(Point2D mauspkt_inPanel, Point2D p_p1_Panel, Point2D p_p2_Panel, double Bpix) {
        Point2D p1 = p_p1_Panel; Point2D p2 = p_p2_Panel;
        double dx = p2.getX() - p1.getX();
        double dz = p2.getY() - p1.getY();
        double phi = Math.atan2(dz, dx);
        double Lpix = p1.distance(p2);
        double dmx = mauspkt_inPanel.getX() - p1.getX();
        double dmz = mauspkt_inPanel.getY() - p1.getY();
        double n = dmx  * Math.cos(phi) + dmz * Math.sin(phi);
        double t = -dmx * Math.sin(phi) + dmz * Math.cos(phi);
        if (n < 0) return false;
        if (n > Lpix) return false;
        if (t < 0) return false;
        if (t > Bpix) return false;
        return true;
        
    }
    
    /** selektiert einen Knoten oder einen Stab in der Nähe des Mauspunktes (in Panelkoord),
     *  in dieser Reihenfolge. Genauer gesagt liefert die Methode nur eine Selektion als
     *  Rückgabewert, ohne sie selber vorzunehmen.
     */
    private int[] snapKnoten(clKoord3D koord, Point2D mauspkt) { // in Panelkoord
        int[] sel = new int[2];
        clKnoten3D aktKn;
        Point2D p = new Point2D.Double();
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKn = (clKnoten3D) it.next();
            p.setLocation(koord.panel(new Point3D(aktKn.getX(), aktKn.getY(), aktKn.getZ())));
            if (mauspkt.distance(p) <= SNAPRADIUS) {
                sel[0] = KNOTEN; sel[1] = i;
                return sel;
            }
            i++;
        }
        // nichts gefunden
        sel[0] = DESELEKT; sel[1] = 0;
        return sel;
    }
    /** Selektiert einen Stab in der Nähe des Mauspunktes (in Panelkoord).
     *  Genauer gesagt liefert die Methode nur eine Selektion als Rückgabewert, ohne sie selber vorzunehmen.
     */
    private int[] snapStab(clKoord3D koord, Point2D mauspkt) { // in Panelkoord
        int[] sel = new int[2];
        clKnoten3D aktKn;
        Point2D p = new Point2D.Double();
        Point2D p2 = new Point2D.Double();
        clWissenderStab3D aktWSt;
        int i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab3D) it.next();
            aktKn = (clKnoten3D) Knotenliste.get(aktWSt.von-1);
            p.setLocation(koord.panel(new Point3D(aktKn.getX(), aktKn.getY(), aktKn.getZ())));
            aktKn = (clKnoten3D) Knotenliste.get(aktWSt.bis-1);
            p2.setLocation(koord.panel(new Point3D(aktKn.getX(), aktKn.getY(), aktKn.getZ())));
            if (mausimRechteck(mauspkt, p, p2, SNAPRADIUS) || mausimRechteck(mauspkt, p2, p, SNAPRADIUS)) {
                sel[0] = STAB; sel[1] = i;
                return sel;
            }
            i++;
        }
        // nichts gefunden
        sel[0] = DESELEKT; sel[1] = 0;
        return sel;
    }
    
    /** TODO snapHintergrund: Schnittpunkte, Pkte auf Linien
     */
    private double[] snapHintergrund(clKoord3D koord, Point2D mauspkt) { // in Panelkoord
        assert dxf != null;
        double snapradiushintergrund = 4;
        double[] pktkoord = new double[4];
        clKnoten3D aktKn;
        Point3D pkt = new Point3D();
        
        // Hintergrundpunkte
        ArrayList hgPunkte = dxf.getHgPunkte();
        clHintergrundPunkt3D aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt3D) it.next();
            pkt.setLocation(aktpkt.getPkt()[0], aktpkt.getPkt()[1], aktpkt.getPkt()[2]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                pktkoord[3] = pkt.getZ();
                return pktkoord;
            }
        }
        // Hintergrundlinien-Enden
        ArrayList hgLinien = dxf.getHgLinien();
        clHintergrundLinie3D aktuell;
        Point3D pkt2 = new Point3D();
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie3D) it.next();
            pkt.setLocation(aktuell.getVon()[0], aktuell.getVon()[1], aktuell.getVon()[2]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                pktkoord[3] = pkt.getZ();
                return pktkoord;
            }
            pkt2.setLocation(aktuell.getBis()[0], aktuell.getBis()[1], aktuell.getBis()[2]);
            if (mauspkt.distance(koord.panel(pkt2)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt2.getX();
                pktkoord[2] = pkt2.getY();
                pktkoord[3] = pkt2.getZ();
                return pktkoord;
            }
        }
        // Hintergrundkugeln: Mittelpunkte
        ArrayList hgKugeln = dxf.getHgKugeln();
        clHintergrundKugel3D aktkugel;
        for (Iterator it = hgKugeln.iterator(); it.hasNext();) {
            aktkugel = (clHintergrundKugel3D) it.next();
            pkt.setLocation(aktkugel.getZentrum()[0], aktkugel.getZentrum()[1], aktkugel.getZentrum()[2]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                pktkoord[3] = pkt.getZ();
                return pktkoord;
            }
        }
        
        // Schnittpunkt zweier Hintergrundlinien  TODO
        /*
        boolean ERSTELINIEgefunden = false;
        boolean ZWEITELINIEgefunden = false;
        double[] linie1 = new double[4];
        double[] linie2 = new double[4];
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie) it.next();
            pkt.setLocation(aktuell.getVon()[0], aktuell.getVon()[1]);
            pkt2.setLocation(aktuell.getBis()[0], aktuell.getBis()[1]);
            if (mausimRechteck(mauspkt, koord.panel(pkt), koord.panel(pkt2), snapradiushintergrund-1)
                || mausimRechteck(mauspkt, koord.panel(pkt2), koord.panel(pkt), snapradiushintergrund-1)) {
                if (!ERSTELINIEgefunden) {
                    ERSTELINIEgefunden = true;
                    linie1[0] = pkt.getX(); linie1[1] = pkt.getY(); linie1[2] = pkt2.getX(); linie1[3] = pkt2.getY();
                }
                else {
                    ZWEITELINIEgefunden = true;
                    linie2[0] = pkt.getX(); linie2[1] = pkt.getY(); linie2[2] = pkt2.getX(); linie2[3] = pkt2.getY();
                    break; // for-Schlaufe abbrechen
                }
            }
        }
        if (ZWEITELINIEgefunden) {
            if (Math.abs(Fkt.det2x2(linie1[2]-linie1[0], linie2[0]-linie2[2], linie1[3]-linie1[1], linie2[1]-linie2[2])) > TOL_gls) { // Ausschliessen, dass Linien parallel
                double[] s_t = Fkt.GLS2x2(linie1[2]-linie1[0], linie2[0]-linie2[2], linie2[0]-linie1[0],
                                          linie1[3]-linie1[1], linie2[1]-linie2[3], linie2[1]-linie1[1]);
                //debug: //System.out.println("Schnittpkt gefunden: s=" + Fkt.nf(s_t[0],2) + " t=" + Fkt.nf(s_t[1],2));
                if (s_t[0] >= 0 && s_t[0] <= 1 && s_t[1]>=0 && s_t[1]<= 1) { // Linien kreuzen sich, nicht ihre Verlängerung
                    pktkoord[0] = 1;
                    pktkoord[1] = linie1[0] + s_t[0] * (linie1[2]-linie1[0]);
                    pktkoord[2] = linie1[1] + s_t[0] * (linie1[3]-linie1[1]);
                    // debug
                    //System.out.println("Schnittpkt: " + Fkt.nf(pktkoord[1],2) + " " + Fkt.nf(pktkoord[2],2));
                    return pktkoord;
                }
            }
        }
         *
         */
        
        // Punkt auf Linie  TODO
        /*
        else if (ERSTELINIEgefunden) { // TODO else entfernen
            //System.out.println("Linie gefunden");
            linie2[0] = koord.m(mauspkt).getX(); linie2[1] = koord.m(mauspkt).getY();
            linie2[2] = linie2[0] + linie1[3]-linie1[1]; linie2[3] = linie2[1] - (linie1[2]-linie1[0]); // Senkrechte zur Linie1
            double[] s_t = Fkt.GLS2x2(linie1[2]-linie1[0], linie2[0]-linie2[2], linie2[0]-linie1[0],
                                      linie1[3]-linie1[1], linie2[1]-linie2[3], linie2[1]-linie1[1]);
            if (s_t[0] >= 0 && s_t[0] <= 1) {
                pktkoord[0] = 1;
                pktkoord[1] = linie1[0] + s_t[0] * (linie1[2]-linie1[0]);
                pktkoord[2] = linie1[1] + s_t[0] * (linie1[3]-linie1[1]);
                // debug
                //System.out.println("Linienpkt: " + Fkt.nf(pktkoord[1],2) + " " + Fkt.nf(pktkoord[2],2));
                return pktkoord;
            }
        }
         */
        
        // nichts gefunden
        pktkoord[0] = 0;
        return pktkoord;
    }
    
    
    private double[] snapStabteiler(clKoord3D koord, Point2D mauspkt) {
        double snapradiusteiler = SNAPRADIUS;
        double[] pktkoord = new double[4];
        
        // Befindet sich Maus über Stab? Wenn nein retour, wenn ja über welchem?
        int[] sel = snapStab(koord, mauspkt);
        if (sel[0] != STAB) {
            pktkoord[0] = 0;
            return pktkoord;
        }
        
        clWissenderStab3D aktWSt = (clWissenderStab3D) Stabliste.get(sel[1]-1);
        clKnoten3D aktKn = (clKnoten3D) Knotenliste.get(aktWSt.von-1);
        Point3D stabanfang = new Point3D(aktKn.getX(), aktKn.getY(), aktKn.getZ());
        aktKn = (clKnoten3D) Knotenliste.get(aktWSt.bis-1);
        double dx = aktKn.getX() - stabanfang.getX();
        double dy = aktKn.getY() - stabanfang.getY();
        double dz = aktKn.getZ() - stabanfang.getZ();
        
        // Kontrolle, ob die Knoten in Panelkoord nicht zu nahe beisammen.
        if (koord.panel(Math.sqrt(Math.pow(dx,2d)+Math.pow(dy,2d)+Math.pow(dz,2d))) < 2d*snapradiusteiler) {
            pktkoord[0] = 0;
            return pktkoord;
        }
        
        Point3D pkt = new Point3D();
        
        double[] s = {0.5, 1d/3d, 2d/3d, 0.2, 0.8};
        
        for (int i = 0; i < s.length; i++) {
            pkt.setLocation(stabanfang.getX() + s[i] * dx, stabanfang.getY() + s[i] * dy, stabanfang.getZ() + s[i] * dz);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiusteiler) {
                pktkoord[0] = sel[1]; // Stabnummer
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                pktkoord[3] = pkt.getZ();
                return pktkoord;
            }
        }
        
        // nichts gefunden
        pktkoord[0] = 0;
        return pktkoord;
    }
    
    
    // -------------------
    // Handlungen der Tastatur
    // -------------------
    
    /** Achtung: Nur Tasten, die in clOberflaeche registriert sind, gehen.*/
    protected void nachrichtTasteGedrückt(int taste) {
        boolean jetztdrücken;
        switch (taste) {
            case KeyEvent.VK_ESCAPE:    // ESC gedrückt
                mausAufgabe = NICHTS;
                von = new Point2D.Double();
                bis = new Point2D.Double();
                vonnr = -1;
                setKnopfNeuerStab(false);
                setKnopfZoomMaus(false);
                setKnopfNeuerKnotenSnap(false); hp.deselektHintergrund();
                Selektion[0] = DESELEKT;
                selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                selektionAnpassen();
                break;
            /* BEREITS IN clOberflaeche definiert
            case KeyEvent.VK_F1:        // F1 gedrückt
                befehlHilfe();
                break;
            case KeyEvent.VK_F2:        // F2 gedrückt
                befehlNeuZeichnen();
                break;
            case KeyEvent.VK_F3:        // F3 gedrückt
                befehlZoomAll();
                break;
            */
            case KeyEvent.VK_F4:        // F4 gedrückt
                jetztdrücken = !KnopfZoomMausistgedrückt();
                setKnopfZoomMaus(jetztdrücken);
                befehlZoomMaus(jetztdrücken);
                break;
            /*
            case KeyEvent.VK_F5:        // F5 gedrückt
                befehlErstelleNeuenKnoten();
                break;
            */
            case KeyEvent.VK_F6:        // F6 gedrückt
                jetztdrücken = !KnopfNeuerKnotenSnapistgedrückt();
                setKnopfNeuerKnotenSnap(jetztdrücken);
                befehlErstelleNeuenKnotenSnap(jetztdrücken);
                break;
            case KeyEvent.VK_F7:        // F7 gedrückt
                jetztdrücken = !KnopfNeuerStabistgedrückt();
                setKnopfNeuerStab(jetztdrücken);
                befehlErstelleNeuenStabMaus(jetztdrücken);
                break;
            /*
            case KeyEvent.VK_F9:       // F9 gedrückt
                befehlBerechne();
                break;
            */
            case KeyEvent.VK_F11:        // F11 gedrückt
                jetztdrücken = !KnopfKnotenistgedrückt();
                setKnopfKnoten(jetztdrücken);
                befehlGewähltIstKnoten(jetztdrücken);
                break;
            case KeyEvent.VK_F12:        // F12 gedrückt
                jetztdrücken = !KnopfStabistgedrückt();
                setKnopfStab(jetztdrücken);
                befehlGewähltIstStab(jetztdrücken);
                break;
            case KeyEvent.VK_MINUS:     // Minus gedrückt
            case KeyEvent.VK_SUBTRACT:
                befehlZoomOut();
                break;
            case KeyEvent.VK_PLUS:      // Plus gedrückt
            case KeyEvent.VK_ADD:
                befehlZoomIn();
                break;
            /*
            case KeyEvent.VK_DELETE:    // DEL gedrückt
                befehlLöschen();
                break;
            */
            default:
                //System.out.println("taste " + taste);
        }
    }
    /** Pfeiltasten*/
    protected void nachrichtPfeilTasteGedrückt(int taste, int maske) {
        switch (maske) {
            // Ctrl - Pfeiltaste dreht den Betrachter um das Modell
            case InputEvent.CTRL_DOWN_MASK:
                switch (taste) {
                    case KeyEvent.VK_LEFT:      // Ctrl-Pfeil links
                        int wertL = sliderHorizontal.getValue() - sliderHorizontal.getMinorTickSpacing();
                        if (wertL < sliderHorizontal.getMinimum()) {
                            wertL = sliderHorizontal.getMinimum();
                        }
                        sliderHorizontal.setValue(wertL);
                        befehlBlickrtgMaus(sliderHorizontal.getValue(), sliderVertical.getValue());
                        break;
                    case KeyEvent.VK_RIGHT:     // Ctrl-Pfeil rechts
                        int wertR = sliderHorizontal.getValue() + sliderHorizontal.getMinorTickSpacing();
                        if (wertR > sliderHorizontal.getMaximum()) {
                            wertR = sliderHorizontal.getMaximum();
                        }
                        sliderHorizontal.setValue(wertR);
                        befehlBlickrtgMaus(sliderHorizontal.getValue(), sliderVertical.getValue());
                        break;
                    case KeyEvent.VK_DOWN:      // Ctrl-Pfeil unten
                        int wertU = sliderVertical.getValue() - sliderVertical.getMinorTickSpacing();
                        if (wertU < sliderVertical.getMinimum()) {
                            wertU = sliderVertical.getMinimum();
                        }
                        sliderVertical.setValue(wertU);
                        befehlBlickrtgMaus(sliderHorizontal.getValue(), sliderVertical.getValue());
                        break;
                    case KeyEvent.VK_UP:        // Ctrl-Pfeil oben
                        int wertO = sliderVertical.getValue() + sliderVertical.getMinorTickSpacing();
                        if (wertO > sliderVertical.getMaximum()) {
                            wertO = sliderVertical.getMaximum();
                        }
                        sliderVertical.setValue(wertO);
                        befehlBlickrtgMaus(sliderHorizontal.getValue(), sliderVertical.getValue());
                        break;
                    default:
                }
                break;
            // Shift - Pfeiltaste bewegt den Betrachter
            case InputEvent.SHIFT_DOWN_MASK:
                double faktorX = 0;
                double faktorZ = 0;
                switch (taste) {
                    case KeyEvent.VK_LEFT:      // Shift-Pfeil links
                        faktorX = -0.10;
                        break;
                    case KeyEvent.VK_RIGHT:     // Shift-Pfeil rechts
                        faktorX = 0.10;
                        break;
                    case KeyEvent.VK_DOWN:      // Shift-Pfeil unten
                        faktorZ = 0.10;
                        break;
                    case KeyEvent.VK_UP:        // Shift-Pfeil oben
                        faktorZ = -0.10;
                        break;
                    default:
                }
                Point2D OL = hp.getZoomPkte()[0];
                Point2D UR = hp.getZoomPkte()[1];
                double dx = UR.getX() - OL.getX();
                double dz = UR.getY() - OL.getY();
                OL.setLocation(OL.getX() + faktorX * dx, OL.getY() + faktorZ * dz);
                UR.setLocation(UR.getX() + faktorX * dx, UR.getY() + faktorZ * dz);
                hp.zoomxy(OL, UR);
                break;
            case 0:
            default:
        }
    }

}
