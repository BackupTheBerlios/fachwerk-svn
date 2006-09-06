/*
 * treillis.java
 *
 * Created on 6. September 2003, 13:34
 */

package Fachwerk;

import Fachwerk.gui.*;
import Fachwerk.statik.*;
import Fachwerk.addins.findeOrt.*;
import Fachwerk.addins.coordTransformation.*;
import Fachwerk.addins.skaliereLasten.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.print.*;


/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2006 A.Vontobel <qwert2003@users.sourceforge.net>
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
 */
public class treillis extends clOberflaeche implements inKonstante {
    
    private static final String PROGNAME = "Fachwerk"; // in clOberflaeche nochmals hart kodiert (Titel)
    private static final int HAUPTVER = 0;
    private static final int UNTERVER = 21; // zweistellig, d.h. für Ver 1.3 UNTERVER = 30
    private final String FILEPROGNAME = "treillis";
    private final int FILEVER = 1;
    
    public boolean OptionVorber = true;
    public boolean OptionGLS = true;
    public boolean OptionMechanismus = true;
    private boolean OptionVerbose = false;
    
    private boolean keinWIDERSPRUCH = true;
    private boolean keinFEHLER = true;
    private boolean VOLLSTÄNDIGGELÖST_OK = false;
    
    private LinkedList Knotenliste = new LinkedList();
    private LinkedList Stabliste = new LinkedList();
    private clDXF dxf; // wird nicht mitgespeichert!
    
    private clHauptPanel hp; // = new clHauptPanel(null,null); // Knotenliste, Stabliste);
    ResourceBundle meldungenRB;
    private JFileChooser fc = new JFileChooser(); //Create a file chooser
    private String dateiname = "new.fwk";
    private String dxfdateiname;
    
    private final double SNAPRADIUS = 6;
    private final double SNAPORTHO = 4;
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
    private final int SCHIEBEKNOTEN = 2;
    private final int NEUERKNOTEN = 3;
    private final int NEUERSTAB = 4;
    private final int NEUERKNOTENSNAP = 5;
    private int mausAufgabe = NICHTS;
    
    
    
    //private boolean mausaktiv = false; // setzt die MausListener beim Laden, bei Dialogen etc inaktiv
    
    /** Creates a new instance of treillis */
    public treillis(Locale lc) {
        super("Fachwerk - rein statisches Fachwerkprogramm", lc);   // Titel wird in clOberflaeche überschrieben
        meldungenRB = ResourceBundle.getBundle("Fachwerk/locales/gui-meldungen", locale);
        if (meldungenRB == null) {
            System.err.println("FEHLER: gui-meldungen für " + locale.toString());
        }
        neu();
    }
    public treillis(Locale lc, String dateiname) {
        super("Fachwerk - rein statisches Fachwerkprogramm", lc);   // Titel wird in clOberflaeche überschrieben
        meldungenRB = ResourceBundle.getBundle("Fachwerk/locales/gui-meldungen", locale);
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
        treillis fw;
        boolean help = false;
        
        String titel = "" + PROGNAME + " version " + HAUPTVER + ".";
        if (UNTERVER < 10) titel += "0";
        titel += Integer.toString(UNTERVER);
        System.out.println(titel);
        System.out.println("Copyright (C) 2003-2006 A. Vontobel");
        System.out.println("Fachwerk comes with ABSOLUTELY NO WARRANTY;");
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
            System.out.println("Fachwerk calculates strut-and-tie models used by structural engineers");
            System.out.println("for analysing and designing reinforced concrete structures.");
            System.out.println("The program only uses the equilibrium conditions, thus it is ");
            System.out.println("not assuming elastic behaviour.");
            System.out.println('\n');
            System.out.println("Usage: fachwerk [OPTION]... [file]...");
            System.out.println('\n');
            System.out.println("Options:");
            System.out.println("    --help or -h             print this help, then exit");
            System.out.println("    --version or -V          print the version, then exit");
            System.out.println("    --language or -l         Language i.e. 'de', 'en', 'fr'");
            System.out.println("    --language_country       Language and country i.e. 'de CH', 'en GB'");
            System.out.println("    --look                   LookAndFeel: Java|System|Steel|Ocean|undef");
            System.out.println("                             or i.e. com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            System.out.println('\n');
            System.out.println("Example 1: fachwerk file.fwk");
            System.out.println("Example 2: fachwerk -l en");
            System.out.println("Example 3: fachwerk -l fr file.fwk");
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
            fw = new treillis(lc);
        }
        else {
            fw = new treillis(lc, dateiname);
        }
        
        fw.pack(); fw.show();
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
            case SCHIEBEKNOTEN: // lassen
            case ZOOMxy: // lassen
            default:
        }        
        
        // BEGINN
        keinFEHLER = true;
        VOLLSTÄNDIGGELÖST_OK = false;
        //Datenaufbereiten
        clKnoten[] Knotenarray = new clKnoten[Knotenliste.size() + 1];
        clStab[] Stabarray = new clStab[Stabliste.size() + 1];
        int[][] Topologie = new int[Knotenarray.length][Knotenarray.length];
        
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten) it.next();
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            clWissenderStab wst = (clWissenderStab) it.next();
            Stabarray[i] = wst.stab;
            Topologie[wst.von][wst.bis] = i;
            i++;
        }
        
        try {
            clFachwerk fachwerk = new clFachwerk(Knotenarray, Stabarray, Topologie);
            fachwerk.setVerbose(OptionVerbose);
            keinWIDERSPRUCH = fachwerk.rechnen(OptionVorber,OptionGLS,OptionMechanismus);
            fachwerk.resultatausgabe_direkt();
            if (keinWIDERSPRUCH) VOLLSTÄNDIGGELÖST_OK = fachwerk.istvollständiggelöst(false); // false, das resutatcheck() soeben in .rechnen() durchgeführt
            aktualisieren(true, true);
            if (!keinWIDERSPRUCH) LayerMechanismius(true, fachwerk.getMechanismus());
            if (keinWIDERSPRUCH) LayerStKraft(true);
            LayerAuflKraft(true);
            LayerLasten(true);
        }
//        catch (ArithmeticException e) { // TODO entfernen, falls bewährt. Funktion in Fw0.14 nach clFachwerk verlegt.
//            String meldung;
//            if (e.getMessage() != null) meldung = e.getMessage();
//            else meldung = e.toString();
//            System.out.println(meldung);
//            aktualisieren(true, true);
//            // im Statusfeld WIDERSPRUCH anzeigen, in Statuszeile Fehlermeldung
//            feldStatusFw.setText(tr("WIDERSPRUCH"));
//            keinWIDERSPRUCH = false;
//            feldStatuszeile.setText(meldung);
//        }
        catch (Exception e) {
            System.out.println(e.toString());
            aktualisieren(true, true);
            // im Statusfeld FEHLER anzeigen, in Statuszeile Fehlermeldung
            feldStatusFw.setText(tr("FEHLER"));
            keinFEHLER = false;
            if (e.getMessage().equals("Mechanismusberechnung fehlgeschlagen.")) {
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
        //aktualisieren(true);
        setKnopfKnoten(false); setKnopfStab(false);
    }
    
    protected void befehlDrucken() {
        clKnoten[] Knotenarray = new clKnoten[Knotenliste.size() + 1];
        clWissenderStab[] Stabarray = new clWissenderStab[Stabliste.size() + 1];
        boolean WIDERSPRUCHmelden = !keinWIDERSPRUCH;
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten) it.next();
            if (!WIDERSPRUCHmelden && Knotenarray[i].getKnotenstatus() == WIDERSPRUCH) WIDERSPRUCHmelden = true;
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            Stabarray[i] = (clWissenderStab) it.next();
            i++;
        }
        clPrintPanel printpanel = new clPrintPanel(Knotenarray, Stabarray, hp,
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
        clPrintGraphPanel printpanel = new clPrintGraphPanel(this, hp, locale);
        
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
                clKnotenDialog kndialog = new clKnotenDialog(this, nr, (clKnoten) Knotenliste.get(Selektion[1]-1), locale);
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
                clStabDialog stdialog = new clStabDialog(this, nr, (clWissenderStab) Stabliste.get(Selektion[1]-1), locale);
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
        setKnopfSchiebeKn(false);
        setKnopfNeuerStab(false);
        setKnopfZoomMaus(false);
        setKnopfNeuerKnotenSnap(false);
        Knotenliste.add(new clKnoten(0,0));
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
        setKnopfSchiebeKn(false);
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
        setKnopfSchiebeKn(false);
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
        setKnopfKnoten(false);
        setKnopfStab(false);
        LayerKnNr(true);
        clNeuerStabDialog dialog = new clNeuerStabDialog(this, locale);
        if (!dialog.getOK()) return;
        int[] vonbis = dialog.einlesen();
        // Kontrolle, ob Knoten existieren und ob sie nicht gleich sind.
        if (vonbis[0] < 1 || vonbis[1] < 1) return;
        if (vonbis[0] > Knotenliste.size() || vonbis[1] > Knotenliste.size()) {
            //String meldung = "Eingabefehler: Beide Knoten müssen existieren!";
            String meldung = tr("errBeideKnotenMuessenExistieren");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            return;
        }
        if (vonbis[0] == vonbis[1]) {
            //String meldung = "Eingabefehler: Ein Knoten darf nicht mit sich selber verbunden werden!";
            String meldung = tr("errKnotenMitSichSelbstVerbunden");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            return;
        }
        // Kontrolle, ob nicht schon ein Stab zwischen diesen Knoten existiert
        boolean stabschonvorhanden = false;
        clWissenderStab aktWSt;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab) it.next();
            if (aktWSt.von == vonbis[0] && aktWSt.bis == vonbis[1]) stabschonvorhanden = true;
            if (aktWSt.von == vonbis[1] && aktWSt.bis == vonbis[0]) stabschonvorhanden = true;
            if (stabschonvorhanden) break;
        }
        if (stabschonvorhanden) {
            //String meldung = "Eingabefehler: Ein Stab zwischen diesen Knoten existiert bereits!";
            String meldung = tr("errStabSchonVorhanden");
            System.out.println(meldung);
            feldStatuszeile.setText(meldung);
            return;
        }
        
        Stabliste.add(new clWissenderStab(new clStab(),vonbis[0], vonbis[1]));
        zurücksetzen(false);
        aktualisieren(false, true);
        selModus = NICHTSÄNDERN;
        Selektion[0] = STAB;
        Selektion[1] = Stabliste.size();
        selektionAnpassen();
    }
    
    protected void befehlErstelleNeuenStabMaus(boolean status) {
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfZoomMaus(false);
        setKnopfSchiebeKn(false);
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
        clHelpBrowser hilfsbrowser = new clHelpBrowser(locale, "Fachwerk/locales/LangSetting");
    }
    
    protected void befehlInfo() {
        clInfo info = new clInfo(this, PROGNAME, HAUPTVER, UNTERVER);
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
                dxf = new clDXF(dxfdateiname);
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
                fc.addChoosableFileFilter(new StdFileFilter("fwk", "Fachwerk Data"));
                
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
                // Datei ist keine Fachwerk-Datei
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
                // Knotennummern anzeigen, bisherigen Zustand merken
                boolean layerKnNr_bisherigerZustd = LayerKnNr();
                LayerKnNr(true);
                // in Dialog  bestätigen
                //meldung = "Der Knoten " + nr + " und alle anschl. Stäbe werden gelöscht.";
                meldung = tr("okKNOTENLOESCHENwarnung_Knoten")+" " + nr + " "+tr("okKNOTENLOESCHENwarnung_wirdgeloescht");
                bestätige = new clOK(this, tr("okKNOTENLOESCHEN"), meldung, locale);
                LayerKnNr(layerKnNr_bisherigerZustd);
                if (!bestätige.ok()) break;
                
                // anschliessende Stäbe löschen
                clWissenderStab st;
                for (int i = 0; i < Stabliste.size(); i++) {
                    st = (clWissenderStab) Stabliste.get(i);
                    if (st.von == nr || st.bis == nr) {
                        Stabliste.remove(i);
                        i--;
                    }
                }
                
                // Knoten entfernen
                Knotenliste.remove(nr-1);
                
                // Anschlussknoten der verbleibenden Stäbe umnummerieren.
                for (Iterator it = Stabliste.iterator(); it.hasNext();) {
                    st = (clWissenderStab) it.next();
                    if (st.von > nr) st.von--;
                    if (st.bis > nr) st.bis--;
                }
                zurücksetzen(false);
                break;
            case STAB:
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
    
    protected void befehlOptionVorberechnung(boolean status) {
        OptionVorber = status;
    }
    
    protected void befehlSchiebeKnMaus(boolean status) { 
        setKnopfKnoten(false);
        setKnopfStab(false);
        setKnopfNeuerStab(false);
        setKnopfZoomMaus(false);
        setKnopfNeuerKnoten(false);
        setKnopfNeuerKnotenSnap(false);
        if (status) {
            mausAufgabe = SCHIEBEKNOTEN; 
            selModus = KNOTEN;
        }
        else {
            mausAufgabe = NICHTS;
            selModus = AUTOMATISCH;
        }
    }
    
    protected void befehlSelektiereKnotenNr() {
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
        selektionAnpassen();
    }
    
    protected void befehlSelektiereStabNr() {
        LayerStKraft(false);
        LayerStNr(true);
        clWaehlenDialog welchen = new clWaehlenDialog(this, STAB, locale);
        int nr = welchen.getNr();
        if (nr > 0 && nr <= Stabliste.size()) {
            Selektion[0] = STAB;
            Selektion[1] = nr;
            setKnopfStab(false); //true);
            setKnopfKnoten(false);
            selModus = NICHTSÄNDERN;
        }
        else {
            Selektion[0] = DESELEKT;
            setKnopfStab(false);
            setKnopfKnoten(false);
            selModus = AUTOMATISCH;
        }
        selektionAnpassen();
    }
    
    protected void befehlSpracheGewechselt() {
        meldungenRB = ResourceBundle.getBundle("Fachwerk/locales/gui-meldungen", locale);
        if (meldungenRB == null) {
            System.err.println("FEHLER: gui-meldungen für " + locale.toString());
        }
    }
    
    protected void befehlSpeichern() {
        try {
            fc.resetChoosableFileFilters();
            fc.addChoosableFileFilter(new StdFileFilter("fwk", "Fachwerk Data"));
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
        } catch (IOException e) {
            System.err.println(e.toString());
            feldStatuszeile.setText(e.toString());
        }
        this.setTitle(PROGNAME + " - " + dateiname);
        //String meldung = "Fachwerk-Datei " + dateiname + " gespeichert.";
        String meldung = tr("infoGESPEICHERT_Fachwerkdatei") + " " + dateiname + " " + tr("infoGESPEICHERT_gespeichert");
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
        setKnopfSchiebeKn(false);
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
    
    protected void befehlZoomPan(double dx, double dz) {   // dx und dz in m
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        pkt1.setLocation(hp.getZoomPkte()[0].getX()-dx, hp.getZoomPkte()[0].getY()-dz);
        pkt2.setLocation(hp.getZoomPkte()[1].getX()-dx, hp.getZoomPkte()[1].getY()-dz);
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
    }
    
    protected void befehlZoomxy() {
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        clZoomDialog dialog = new clZoomDialog(this, locale);
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
       clguiFindeOrt fo = new clguiFindeOrt(Selektion[1], Knotenliste, Stabliste, this, locale);
       aktualisieren(true, true);
       LayerStNr(false);
       LayerStKraft(true);
       LayerLasten(true);
       LayerAuflKraft(true);
       selektionAnpassen();
       
       if (fo.getMeldung().equals("")) ;//nichts tun
       else feldStatuszeile.setText(fo.getMeldung());
    }
    
    protected void befehlAddinKoordTransFWK() {
        clKoordTransFWK ctfwk = new clKoordTransFWK(Knotenliste, this, locale);
        zurücksetzen(false);
        aktualisieren(true, true);
        selektionAnpassen();
    }
    
    protected void befehlAddinKoordTransDXF() {
        if (dxf != null) {
            clKoordTransDXF ctdxf = new clKoordTransDXF(dxf, this, locale);        
            hp.neuzeichnen();
        }
    }
    
    protected void befehlAddinSkaliereLasten() {
        clLastenSkalieren sl = new clLastenSkalieren(Knotenliste, Stabliste, this, locale);
        if (sl.isModified()) { // Skalierung wurde durchgeführt, d.h. nicht abgebrochen und Faktor!=1
            zurücksetzen(false);
            aktualisieren(true, true);
            LayerLasten(true);
            LayerStKraft(true);
            selektionAnpassen();
        }
    }
    
    
    // ----------------
    // INTERNE METHODEN
    // ----------------
    
    void test() {
        clKnoten aktuellKn;
        clWissenderStab aktuellSt;
        
        Knotenliste.add(new clKnoten(0,6));
        Knotenliste.add(new clKnoten(4,2));
        Knotenliste.add(new clKnoten(4,6));
        Knotenliste.add(new clKnoten(9,2));
        Knotenliste.add(new clKnoten(9,6));
        Knotenliste.add(new clKnoten(13,2));
        Knotenliste.add(new clKnoten(13,6));
        
        Stabliste.add(new clWissenderStab(new clStab(),1,2));
        Stabliste.add(new clWissenderStab(new clStab(),1,3));
        Stabliste.add(new clWissenderStab(new clStab(),2,3));
        Stabliste.add(new clWissenderStab(new clStab(),2,4));
        Stabliste.add(new clWissenderStab(new clStab(),2,5));
        Stabliste.add(new clWissenderStab(new clStab(),4,3));
        Stabliste.add(new clWissenderStab(new clStab(),5,3));
        Stabliste.add(new clWissenderStab(new clStab(),5,4));
        Stabliste.add(new clWissenderStab(new clStab(),4,6));
        Stabliste.add(new clWissenderStab(new clStab(),7,4));
        Stabliste.add(new clWissenderStab(new clStab(),5,7));
        
        // Lagerbed und Lasten zuordnen
        aktuellKn = (clKnoten) Knotenliste.get(6-1); aktuellKn.setLager(VERSCHIEBLICH,Math.toRadians(-90));
        aktuellKn = (clKnoten) Knotenliste.get(7-1); aktuellKn.setLager(FIX);
        aktuellKn = (clKnoten) Knotenliste.get(1-1); aktuellKn.setLast(0,100);
        aktuellKn = (clKnoten) Knotenliste.get(2-1); aktuellKn.setLast(50,20);
        
        // eine Stabkraft zuweisen
        aktuellSt = (clWissenderStab) Stabliste.get(5-1); 
        aktuellSt.stab.setKraft(GESETZT, -32.01562119);
    }
    
    /** Erstellt das Hauptpanel (Zeichnung) neu.
     * @param LAYERAUS Schaltet alle Layer aus.
     * @param STANDARDSICHT Stellt die Standardansicht (zoom all) wieder her.
     */
    void aktualisieren(boolean LAYERAUS, boolean STANDARDSICHT) {
        clKnoten[] Knotenarray = new clKnoten[Knotenliste.size() + 1];
        clWissenderStab[] Stabarray = new clWissenderStab[Stabliste.size() + 1];
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            Knotenarray[i] = (clKnoten) it.next();
            i++;
        }
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            Stabarray[i] = (clWissenderStab) it.next();
            i++;
        }
        java.awt.Dimension grösse = null;
        Point2D[] bisherigeZoomPkte = null;
        boolean bisherigZoomAll = true; // wird überschrieben
        
        if (hp != null) {
            grösse = hp.getSize();
            if (!STANDARDSICHT) {
                bisherigeZoomPkte = hp.getZoomPkte();
                bisherigZoomAll = hp.istZoomAll();
            }
            this.getContentPane().remove(hp);
        }
        
        hp = new clHauptPanel(Knotenarray, Stabarray);
        if (grösse != null) hp.setPreferredSize(grösse);
        if (!STANDARDSICHT) {
            if (bisherigeZoomPkte != null) hp.zoomxy(bisherigeZoomPkte[0], bisherigeZoomPkte[1]);
            if (bisherigZoomAll) hp.zoomAll(false); // zoomAll(false), da der Befehl zum Neuzeichnen sowieso folgt.
        }
        
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
        clKnoten aktKn;
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKn = (clKnoten) it.next();
            aktKn.zurücksetzen();
            i++;
        }
        
        clWissenderStab aktWSt;
        i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab) it.next();
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
        double dx, dz; // Abstand zwischen Knoten in Koord.rtg.
        double ax, az; // Komponenten: Nx = ax*N
        
        switch (Selektion[0]) {
            case DESELEKT:
                text = "";
                break;
                
            case STAB:
                clWissenderStab aktWSt = (clWissenderStab) Stabliste.get(Selektion[1]-1);
                switch (aktWSt.stab.getStatus()) {
                    case BER:
                        N = aktWSt.stab.getKraft();
                        // Kraftkomponenten
                        dx = ((clKnoten) Knotenliste.get(aktWSt.bis-1)).getX() - ((clKnoten) Knotenliste.get(aktWSt.von-1)).getX();
                        dz = ((clKnoten) Knotenliste.get(aktWSt.bis-1)).getZ() - ((clKnoten) Knotenliste.get(aktWSt.von-1)).getZ();
                        ax = Math.abs(dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d)));
                        az = Math.abs(dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d)));
                        
                        text = tr("StabNr") + " " + Selektion[1] + ": N = " + Fkt.nf(N,1)
                                + " kN (" + tr("berechnet") + ")";
                        text += "  Nx=" + Fkt.nf(ax*N,1) + "kN Nz=" + Fkt.nf(az*N,1) + "kN";
                        break;
                    case GESETZT:
                        N = aktWSt.stab.getKraft();
                        // Kraftkomponenten
                        dx = ((clKnoten) Knotenliste.get(aktWSt.bis-1)).getX() - ((clKnoten) Knotenliste.get(aktWSt.von-1)).getX();
                        dz = ((clKnoten) Knotenliste.get(aktWSt.bis-1)).getZ() - ((clKnoten) Knotenliste.get(aktWSt.von-1)).getZ();
                        ax = Math.abs(dx / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d)));
                        az = Math.abs(dz / Math.sqrt(Math.pow(dx,2d) + Math.pow(dz,2d)));
                        
                        text = tr("StabNr") + " " + Selektion[1] + ": N = " + Fkt.nf(N,1)
                                + " kN (" + tr("gesetzt") + ")";
                        text += "  Nx=" + Fkt.nf(ax*N,1) + "kN Nz=" + Fkt.nf(az*N,1) + "kN";
                        break;
                    case UNBEST:
                        text = tr("StabNr") + " " + Selektion[1] + ": " + tr("unbestimmt");
                        break;
                    default:
                        text = tr("StabNr") + " " + Selektion[1] + ": " + tr("Status") + " " + aktWSt.stab.getStatus();
                }
                break;
                
            case KNOTEN:
                clKnoten aktKn = (clKnoten) Knotenliste.get(Selektion[1]-1);
                text = tr("Knoten") + " " + Selektion[1] + ": x = " + Fkt.nf(aktKn.getX(), 2)
                        + ", z = " + Fkt.nf(aktKn.getZ(), 2) + ";  ";
                // Last
                if (aktKn.getLx() != 0 || aktKn.getLz() != 0) {
                    text += "Lx = " + Fkt.nf(aktKn.getLx(),1) + " kN, Lz = " + Fkt.nf(aktKn.getLz(),1) + " kN;  ";
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
                                text += "Ax = " + Fkt.nf(aktKn.getRx(),1) + " kN, Az = " + Fkt.nf(aktKn.getRz(),1) 
                                        + " kN (" + tr("festgelagert") + ");  ";
                                break;
                            case GESETZT:
                                assert false: " noch nicht implementiert";
                            default:
                                assert false;
                        }
                        break;
                    case VERSCHIEBLICH:
                        switch (aktKn.getLagerstatus()) {
                            case OFFEN:
                                text += tr("verschieblich") + " " + tr("inRtg1") + " " +Fkt.nf(Math.toDegrees(aktKn.getRalpha()),1)
                                        + tr("inRtg2") + " (" + tr("offen") + ")";
                                break;
                            case BER:
                                text += "Ax = " + Fkt.nf(aktKn.getRx(),1) + " kN, Az = " + Fkt.nf(aktKn.getRz(),1)
                                        + " kN (" + tr("verschieblich") + ");  ";
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
    
    // -------------------
    // Handlungen der Maus
    // -------------------
    private Point2D von = new Point2D.Double(); // in Fachwerkkoord (zB Metern)
    private Point2D bis = new Point2D.Double(); // in Fachwerkkoord (zB Metern)
    int vonnr = -1;
    private boolean PAN = false; // für PAN
    private clPan pan; // für PAN, Behälter für die nötigen Variablen.
    
    
    protected void nachrichtMausGeklickt(java.awt.event.MouseEvent maus) {   // NOCH UNVOLLSTÄNDIG
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        switch (selModus) {
            case KNOTEN:
                if (Selektion[0] == KNOTEN) {
                    if (maus.getButton() == maus.BUTTON1) {
                        selModus = NICHTSÄNDERN;
                        setKnopfKnoten(false);
                    }
                }
                break;
            case STAB:
                if (Selektion[0] == STAB) {
                    if (maus.getButton() == maus.BUTTON1) {
                        selModus = NICHTSÄNDERN;
                        setKnopfStab(false);
                    }
                }
                break;
            case HINTERGRUND:
                assert mausAufgabe == NEUERKNOTENSNAP;
                double[] pktkoord = snapHintergrund(hp.getKoord(), mzp(maus.getPoint()));
                if (pktkoord[0] > 0) {
                    Knotenliste.add(new clKnoten(pktkoord[1],pktkoord[2]));
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
                    clWissenderStab stab = (clWissenderStab) Stabliste.get(stabnr-1);
                    // Neuen Knoten auf bestehendem Stab hinzufügen, 2 neue Stäbe, alter Stab löschen.
                    Knotenliste.add(new clKnoten(stabpktkoord[1],stabpktkoord[2]));
                    Stabliste.add(new clWissenderStab(new clStab(), stab.von, Knotenliste.size()));
                    Stabliste.add(new clWissenderStab(new clStab(), Knotenliste.size(), stab.bis));
                    // falls der zu löschende Stab eine gesetzte Kraft hatte, diese einem der beiden neuen Stäben zuweisen.
                    if (stab.stab.getStatus() == GESETZT) {
                        double N = stab.stab.getKraft();
                        ((clWissenderStab) Stabliste.getLast()).stab.setKraft(GESETZT, N);
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
                if (maus.getButton() == maus.BUTTON1) {
                    Selektion[0] = DESELEKT;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    selektionAnpassen();
                }
                if (maus.getButton() == maus.BUTTON3) befehlEigenschaften();
                break;
            case DESELEKT: // noch nicht gebraucht
                break;
            case AUTOMATISCH:
                if (maus.getButton() == maus.BUTTON3) { // rechte Maustaste
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
        
        clKoord koord = hp.getKoord();
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
                    hp.selektHintergrund(pktkoord[1], pktkoord[2]);
                    feldStatuszeile.setText("x = " + Fkt.nf(pktkoord[1], 3) + " z = " + Fkt.nf(pktkoord[2], 3));
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
                    hp.selektHintergrund(stabpktkoord[1], stabpktkoord[2]);
                    feldStatuszeile.setText(tr("StabNr") + " " + (int)stabpktkoord[0]
                            + " x = " + Fkt.nf(stabpktkoord[1], 3) + " z = " + Fkt.nf(stabpktkoord[2], 3));
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
        clKoord koord = hp.getKoord();
        int[] sel;
        switch (mausAufgabe) {
            case ZOOMxy:
                von = new Point2D.Double();
                von = koord.m(mzp(maus.getPoint()));
                break;
            case SCHIEBEKNOTEN:
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                von = new Point2D.Double();
                if (sel[0] == KNOTEN) {
                    von.setLocation(((clKnoten) Knotenliste.get(sel[1]-1)).getX(),
                                    ((clKnoten) Knotenliste.get(sel[1]-1)).getZ());
                    Selektion = sel;
                    vonnr = sel[1];
                    selModus = NICHTSÄNDERN;
                }
                else {
                    vonnr = -1;
                    return;
                }
                break;
            case NEUERSTAB:
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                von = new Point2D.Double();
                if (sel[0] == KNOTEN) {
                    von.setLocation(((clKnoten) Knotenliste.get(sel[1]-1)).getX(),
                                    ((clKnoten) Knotenliste.get(sel[1]-1)).getZ());
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
                if (maus.getButton()==maus.BUTTON2 || (maus.getModifiersEx()==(maus.CTRL_DOWN_MASK | maus.BUTTON1_DOWN_MASK))) {
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
        clKoord koord = hp.getKoord();
        bis = koord.m(mzp(maus.getPoint()));
        feldX.setText(""+Fkt.nf(bis.getX(),2));
        feldZ.setText(""+Fkt.nf(bis.getY(),2));
        int[] sel;
        //System.out.println("bis: " + bis.toString());
        switch (mausAufgabe) {
            case ZOOMxy:
                hp.ZeigeHilfsRechteck(true, koord.panel(von), koord.panel(bis));
                break;
            case SCHIEBEKNOTEN:
                if (vonnr < 1) return;
                if (LayerHintergrund() && dxf != null) {
                    double[] snappkt = snapHintergrund(koord, mzp(maus.getPoint()));
                    if (snappkt[0] > 0) {
                        bis.setLocation(snappkt[1], snappkt[2]);
                        hp.selektHintergrund(snappkt[1], snappkt[2]);
                        feldStatuszeile.setText("x0 = " + Fkt.nf(von.getX(),3) + " z0 = " + Fkt.nf(von.getY(),3)
                                    + "  -->  x = " + Fkt.nf(snappkt[1], 3) + " z = " + Fkt.nf(snappkt[2], 3));
                    }
                    else {
                        hp.deselektHintergrund();
                        feldStatuszeile.setText("x0 = " + Fkt.nf(von.getX(),3) + " z0 = " + Fkt.nf(von.getY(),3));
                        // snap orthogonal
                        if (Math.abs(koord.panel(bis).getX()-koord.panel(von).getX()) <= SNAPORTHO)
                            bis.setLocation(von.getX(), bis.getY());
                        if (Math.abs(koord.panel(bis).getY()-koord.panel(von).getY()) <= SNAPORTHO)
                            bis.setLocation(bis.getX(), von.getY());
                    }
                }
                else {  // snap orthogonal
                    if (Math.abs(koord.panel(bis).getX()-koord.panel(von).getX()) <= SNAPORTHO) 
                        bis.setLocation(von.getX(), bis.getY());
                    if (Math.abs(koord.panel(bis).getY()-koord.panel(von).getY()) <= SNAPORTHO) 
                        bis.setLocation(bis.getX(), von.getY());
                }
                hp.ZeigeHilfslinie(true, koord.panel(von), koord.panel(bis));
                break;
            case NEUERSTAB:
                if (vonnr < 1) return;
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                if (sel[0] == KNOTEN) {
                    bis.setLocation(((clKnoten) Knotenliste.get(sel[1]-1)).getX(),
                                    ((clKnoten) Knotenliste.get(sel[1]-1)).getZ());
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
            setKnopfSchiebeKn(false);
            setKnopfNeuerStab(false);
            setKnopfZoomMaus(false);
            return;
        }
        clKoord koord = hp.getKoord();
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
            case SCHIEBEKNOTEN:
                if (vonnr < 1) {
                    String meldung = tr("errKeinGueltigerKnoten");
                    System.out.println(meldung);
                    feldStatuszeile.setText(meldung);
                    setKnopfSchiebeKn(false);
                    mausAufgabe = NICHTS;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    return;
                }
                if (LayerHintergrund() && dxf != null) {
                    double[] snappkt = snapHintergrund(koord, mzp(maus.getPoint()));
                    if (snappkt[0] > 0) bis.setLocation(snappkt[1], snappkt[2]);
                    else { // snap orthogonal
                        if (Math.abs(koord.panel(bis).getX()-koord.panel(von).getX()) <= SNAPORTHO) 
                            bis.setLocation(von.getX(), bis.getY());
                        if (Math.abs(koord.panel(bis).getY()-koord.panel(von).getY()) <= SNAPORTHO) 
                            bis.setLocation(bis.getX(), von.getY());
                    }
                }
                else { // snap orthogonal
                    if (Math.abs(koord.panel(bis).getX()-koord.panel(von).getX()) <= SNAPORTHO)
                        bis.setLocation(von.getX(), bis.getY());
                    if (Math.abs(koord.panel(bis).getY()-koord.panel(von).getY()) <= SNAPORTHO)
                        bis.setLocation(bis.getX(), von.getY());
                }
                clKnoten aktKn = (clKnoten) Knotenliste.get(vonnr-1);
                aktKn.setNeueKoord(bis.getX(), bis.getY());
                zurücksetzen(false);                    // ZUTUN: Zoom beibehalten (wiederherstellen)
                selModus = NICHTSÄNDERN;
                Selektion[0] = KNOTEN;
                Selektion[1] = vonnr;
                selektionAnpassen();
                setKnopfSchiebeKn(false);
                mausAufgabe = NICHTS;
                break;
            case NEUERSTAB:
                sel = snapKnoten(koord, mzp(maus.getPoint()));
                if (sel[0] != KNOTEN) {
                    setKnopfNeuerStab(false);
                    mausAufgabe = NICHTS;
                    selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
                    return;
                }
                bis.setLocation(((clKnoten) Knotenliste.get(sel[1]-1)).getX(),
                                ((clKnoten) Knotenliste.get(sel[1]-1)).getZ());
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
                clWissenderStab aktWSt;
                for (Iterator it = Stabliste.iterator(); it.hasNext();) {
                    aktWSt = (clWissenderStab) it.next();
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
                
                Stabliste.add(new clWissenderStab(new clStab(), vonnr, sel[1]));
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
                    PAN = false;
                }
        }
    }
    
    protected void nachrichtMausRadGedreht(java.awt.event.MouseWheelEvent maus) {
        if (hp == null) return;
        if (hp.getKoord() == null) return;
        if (!mausimPanel(maus)) return;
        int rtg = maus.getWheelRotation();
        if (rtg == 0) assert false;
        
        Point2D pktm = new Point2D.Double(); // Mittelpunkt
        pktm.setLocation(((hp.getZoomPkte()[0]).getX() + (hp.getZoomPkte()[1]).getX()) / 2d,
        ((hp.getZoomPkte()[0]).getY() + (hp.getZoomPkte()[1]).getY()) / 2d);
        
        double dx = Math.abs((hp.getZoomPkte()[1]).getX() - (hp.getZoomPkte()[0]).getX());
        double dz = Math.abs((hp.getZoomPkte()[1]).getY() - (hp.getZoomPkte()[0]).getY());
        
        Point2D pkt1 = new Point2D.Double();
        Point2D pkt2 = new Point2D.Double();
        if (rtg > 0) { // hinauszoomen          TODO
        // Zoomfenster vergrössern: mit drei Klicks verdoppeln
        pkt1.setLocation(pktm.getX() - Math.pow(2d, 1d/3d) * dx/2d, pktm.getY() - Math.pow(2d, 1d/3d) * dz/2d);
        pkt2.setLocation(pktm.getX() + Math.pow(2d, 1d/3d) * dx/2d, pktm.getY() + Math.pow(2d, 1d/3d) * dz/2d);
        if (!pkt1.equals(pkt2)) hp.zoomxy(pkt1, pkt2);
        }
        if (rtg < 0) { // hineinzoomen          TODO
        // Zoomfenster verkleinern: mit drei Klicks halbieren
        pkt1.setLocation(pktm.getX() - Math.pow(0.5, 1d/3d) * dx/2d, pktm.getY() - Math.pow(0.5, 1d/3d) * dz/2d);
        pkt2.setLocation(pktm.getX() + Math.pow(0.5, 1d/3d) * dx/2d, pktm.getY() + Math.pow(0.5, 1d/3d) * dz/2d);
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
    private int[] snapKnoten(clKoord koord, Point2D mauspkt) { // in Panelkoord
        int[] sel = new int[2];
        clKnoten aktKn;
        Point2D p = new Point2D.Double();
        int i = 1;
        for (Iterator it = Knotenliste.iterator(); it.hasNext();) {
            aktKn = (clKnoten) it.next();
            p.setLocation(koord.panel(new Point2D.Double(aktKn.getX(), aktKn.getZ())));
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
    private int[] snapStab(clKoord koord, Point2D mauspkt) { // in Panelkoord
        int[] sel = new int[2];
        clKnoten aktKn;
        Point2D p = new Point2D.Double();
        Point2D p2 = new Point2D.Double();
        clWissenderStab aktWSt;
        int i = 1;
        for (Iterator it = Stabliste.iterator(); it.hasNext();) {
            aktWSt = (clWissenderStab) it.next();
            aktKn = (clKnoten) Knotenliste.get(aktWSt.von-1);
            p.setLocation(koord.panel(new Point2D.Double(aktKn.getX(), aktKn.getZ())));
            aktKn = (clKnoten) Knotenliste.get(aktWSt.bis-1);
            p2.setLocation(koord.panel(new Point2D.Double(aktKn.getX(), aktKn.getZ())));
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
    
    /** Selektiert einen Punkt der Hintergrundzeichnung.
     *  Gibt einen Array zurück. [0]==1, [1]=x-koord, [2]=z-koord oder [0]==0 (nichts gefunden).
     */
    private double[] snapHintergrund(clKoord koord, Point2D mauspkt) { // in Panelkoord
        assert dxf != null;
        double snapradiushintergrund = 4;
        double[] pktkoord = new double[3];
        clKnoten aktKn;
        Point2D pkt = new Point2D.Double();
        
        // Hintergrundpunkte
        ArrayList hgPunkte = dxf.getHgPunkte();
        clHintergrundPunkt aktpkt;
        for (Iterator it = hgPunkte.iterator(); it.hasNext();) {
            aktpkt = (clHintergrundPunkt) it.next();
            pkt.setLocation(aktpkt.getPkt()[0], aktpkt.getPkt()[1]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
        }
        // Hintergrundlinien-Enden
        ArrayList hgLinien = dxf.getHgLinien();
        clHintergrundLinie aktuell;
        Point2D pkt2 = new Point2D.Double();
        for (Iterator it = hgLinien.iterator(); it.hasNext();) {
            aktuell = (clHintergrundLinie) it.next();
            pkt.setLocation(aktuell.getVon()[0], aktuell.getVon()[1]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
            pkt2.setLocation(aktuell.getBis()[0], aktuell.getBis()[1]);
            if (mauspkt.distance(koord.panel(pkt2)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt2.getX();
                pktkoord[2] = pkt2.getY();
                return pktkoord;
            }
        }
        // Hintergrundkreise: Mittelpunkte
        ArrayList hgKreise = dxf.getHgKreise();
        clHintergrundKreis aktkreis;
        for (Iterator it = hgKreise.iterator(); it.hasNext();) {
            aktkreis = (clHintergrundKreis) it.next();
            pkt.setLocation(aktkreis.getZentrum()[0], aktkreis.getZentrum()[1]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
        }
        // Hintergrundbögen: Anfangs- und Endpunkte und Mittelpunkt
        ArrayList hgBogen = dxf.getHgBogen();
        clHintergrundBogen aktbogen;
        for (Iterator it = hgBogen.iterator(); it.hasNext();) {
            aktbogen = (clHintergrundBogen) it.next();
            pkt.setLocation(aktbogen.getZentrum()[0] + aktbogen.getRadius()*Math.cos(Math.toRadians(aktbogen.getSektor()[0])),
                            aktbogen.getZentrum()[1] - aktbogen.getRadius()*Math.sin(Math.toRadians(aktbogen.getSektor()[0])));
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
            pkt.setLocation(aktbogen.getZentrum()[0] + aktbogen.getRadius()*Math.cos(Math.toRadians(aktbogen.getSektor()[1])),
                            aktbogen.getZentrum()[1] - aktbogen.getRadius()*Math.sin(Math.toRadians(aktbogen.getSektor()[1])));
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
            pkt.setLocation(aktbogen.getZentrum()[0], aktbogen.getZentrum()[1]);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiushintergrund) {
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
                return pktkoord;
            }
        }
        // Schnittpunkt zweier Hintergrundlinien
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
            if (Math.abs(Fkt.det2x2(linie1[2]-linie1[0], linie2[0]-linie2[2], linie1[3]-linie1[1], linie2[1]-linie2[3])) > TOL_gls) { // Ausschliessen, dass Linien parallel
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
        // Punkt auf Linie
        else if (ERSTELINIEgefunden) { // TODO else entfernen?
            //System.out.println("Linie gefunden");
            linie2[0] = koord.m(mauspkt).getX(); linie2[1] = koord.m(mauspkt).getY();
            linie2[2] = linie2[0] + linie1[3]-linie1[1]; linie2[3] = linie2[1] - (linie1[2]-linie1[0]); // Senkrechte zur Linie1
            if (Math.abs(Fkt.det2x2(linie1[2]-linie1[0], linie2[0]-linie2[2], linie1[3]-linie1[1], linie2[1]-linie2[3])) > TOL_gls) { // Ausschliessen, dass Determinante = 0
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
        }
        // Hintergrundkreise: Punkt auf Kreis
        for (Iterator it = hgKreise.iterator(); it.hasNext();) {
            aktkreis = (clHintergrundKreis) it.next();
            pkt.setLocation(aktkreis.getZentrum()[0], aktkreis.getZentrum()[1]);
            if (Math.abs(mauspkt.distance(koord.panel(pkt)) - koord.panel(aktkreis.getRadius())) <= snapradiushintergrund-1) {
                double dx0 = koord.m(mauspkt).getX() - pkt.getX();
                double dy0 = koord.m(mauspkt).getY() - pkt.getY();
                double norm = Math.sqrt(dx0*dx0 + dy0*dy0);
                pktkoord[0] = 1;
                pktkoord[1] = pkt.getX() + aktkreis.getRadius() * dx0/norm;
                pktkoord[2] = pkt.getY() + aktkreis.getRadius() * dy0/norm;
                return pktkoord;
            }
        }
        // Hintergrundbögen: Punkt auf Bogen
        for (Iterator it = hgBogen.iterator(); it.hasNext();) {
            aktbogen = (clHintergrundBogen) it.next();
            pkt.setLocation(aktbogen.getZentrum()[0], aktbogen.getZentrum()[1]);
            if (Math.abs(mauspkt.distance(koord.panel(pkt)) - koord.panel(aktbogen.getRadius())) <= snapradiushintergrund-1) {
                double dx0 = koord.m(mauspkt).getX() - pkt.getX();
                double dy0 = koord.m(mauspkt).getY() - pkt.getY();
                double startwinkel = aktbogen.getSektor()[0];
                double endwinkel = aktbogen.getSektor()[1];
                double winkel = Math.toDegrees(Math.atan2(-dy0, dx0));
                while (winkel < 0) winkel += 360;
                while (winkel >= 360 ) winkel -= 360;
                while (startwinkel < 0) startwinkel += 360;
                while (startwinkel >= 360 ) startwinkel -= 360;
                while (endwinkel < 0) endwinkel += 360;
                while (endwinkel >= 360 ) endwinkel -= 360;
                
                if (winkel > startwinkel && winkel < endwinkel) {
                        double norm = Math.sqrt(dx0*dx0 + dy0*dy0);
                        pktkoord[0] = 1;
                        pktkoord[1] = pkt.getX() + aktbogen.getRadius() * dx0/norm;
                        pktkoord[2] = pkt.getY() + aktbogen.getRadius() * dy0/norm;
                return pktkoord;
                }
            }
        }
        
        // nichts gefunden
        pktkoord[0] = 0;
        return pktkoord;
    }
    
    
    private double[] snapStabteiler(clKoord koord, Point2D mauspkt) { // in Panelkoord
        double snapradiusteiler = SNAPRADIUS;
        double[] pktkoord = new double[3];
        
        // Befindet sich Maus über Stab? Wenn nein retour, wenn ja über welchem?
        int[] sel = snapStab(koord, mauspkt);
        if (sel[0] != STAB) {
            pktkoord[0] = 0;
            return pktkoord;
        }
        
        clWissenderStab aktWSt = (clWissenderStab) Stabliste.get(sel[1]-1);
        clKnoten aktKn = (clKnoten) Knotenliste.get(aktWSt.von-1);
        Point2D stabanfang = new Point2D.Double();
        stabanfang.setLocation(aktKn.getX(), aktKn.getZ());
        aktKn = (clKnoten) Knotenliste.get(aktWSt.bis-1);        
        double dx = aktKn.getX() - stabanfang.getX();
        double dz = aktKn.getZ() - stabanfang.getY(); // ist z-Koordinate !
        
        // Kontrolle, ob die Knoten in Panelkoord nicht zu nahe beisammen.
        if (koord.panel(Math.sqrt(Math.pow(dx,2d)+Math.pow(dz,2d))) < 2d*snapradiusteiler) {
            pktkoord[0] = 0;
            return pktkoord;
        }
        
        Point2D pkt = new Point2D.Double();
        
        double[] s = {0.5, 1d/3d, 2d/3d, 0.2, 0.8};
        
        for (int i = 0; i < s.length; i++) {
            pkt.setLocation(stabanfang.getX() + s[i] * dx, stabanfang.getY() + s[i] * dz);
            if (mauspkt.distance(koord.panel(pkt)) <= snapradiusteiler) {
                pktkoord[0] = sel[1]; // Stabnummer
                pktkoord[1] = pkt.getX();
                pktkoord[2] = pkt.getY();
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
        switch (taste) {
            case KeyEvent.VK_ESCAPE:    // ESC gedrückt
                mausAufgabe = NICHTS;
                von = new Point2D.Double();
                bis = new Point2D.Double();
                vonnr = -1;
                setKnopfNeuerStab(false);
                setKnopfZoomMaus(false);
                setKnopfSchiebeKn(false);
                setKnopfNeuerKnotenSnap(false); hp.deselektHintergrund();
                selModus = AUTOMATISCH; setKnopfStab(false); setKnopfKnoten(false);
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
                setKnopfZoomMaus(true);
                befehlZoomMaus(true);
                break;
            /*
            case KeyEvent.VK_F5:        // F5 gedrückt
                befehlErstelleNeuenKnoten();
                break;
            */
            case KeyEvent.VK_F6:        // F6 gedrückt
                setKnopfNeuerKnotenSnap(true);
                befehlErstelleNeuenKnotenSnap(true);
                break;
            case KeyEvent.VK_F7:        // F7 gedrückt
                setKnopfNeuerStab(true);
                befehlErstelleNeuenStabMaus(true);
                break;
            /*
            case KeyEvent.VK_F9:       // F9 gedrückt
                befehlBerechne(); 
                break;
            */
            case KeyEvent.VK_F11:        // F11 gedrückt
                setKnopfKnoten(true);
                befehlGewähltIstKnoten(true);
                break;
            case KeyEvent.VK_F12:        // F12 gedrückt
                setKnopfStab(true);
                befehlGewähltIstStab(true);
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

}
