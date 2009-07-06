/*
 * clParserBGD.java
 *
 * Created on 11. September 2004, 19:52
 *

 * -------------------
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

import java.io.*;
import java.util.*;

/**
 *  Parser für Hintergrunddateien *.BGD (Alternative zu dxf).
 * 
 */
public class clParserBGD {
    
    private final boolean debug = false;//true;
    
    private static final int OFFEN = -1;
    private static final int OTHER = 0;
    private static final int LINE = 1;
    private static final int POLYLINE = 2;
    private static final int CIRCLE = 4;
    private static final int POINT = 5;
    private static final int ARC = 7;
    private static final int SPHERE = 11;
    
    boolean FEHLER;
    private int zeilennr = 0;
    
    String datei;
    private static String Fehlermeldung = "";
    BufferedReader eingabe;
    private int entityname = OFFEN;
    private double[] entity;
    double[] letzterpkt;
    
    /** Creates a new instance of clParserBGD */
    public clParserBGD(String datei) {
        this.datei = datei;
        try {
            File datei_STATIKin = new File(datei);
            FileReader eingabestrom = new FileReader(datei_STATIKin);
            eingabe = new BufferedReader(eingabestrom);
        }
        catch(FileNotFoundException e) {
            Fehlermeldung = "Datei " + datei + " nicht vorhanden!" + '\n' + "Fehlermeldung: " + e;
            System.out.println(Fehlermeldung);
        }
        
    }
    
    
    /** Gibt an, ob ein weiteres Element vorhanden ist. Falls ja, wird es eingelesen.*/
    public boolean hasMoreElements() {
        FEHLER = false;
        String wort[] = zeileEinlesen();
        if (wort == null) return false; // Dateiende
        
        // debug
        if (debug) {
            for (int i = 0; i < wort.length; i++) System.out.print(wort[i] + " ");
            System.out.println("");
        }
        
        boolean ZEILEVORRÜCKEN = true; // wird benötigt, wenn Element Punkt nicht durch P gekennzeichnet ist.
        boolean STEUERZEICHEN = false;
        double[] neuerpkt;

        
        if (wort[0].startsWith("P") || wort[0].startsWith("p")) {
            entityname = POINT;
            STEUERZEICHEN = true;
        }
        if (wort[0].startsWith("L") || wort[0].startsWith("l")) {
            entityname = LINE;
            STEUERZEICHEN = true;
        }
        if (wort[0].startsWith("C") || wort[0].startsWith("c") || wort[0].startsWith("K") || wort[0].startsWith("k")
            && !(wort[0].startsWith("Kugel") || wort[0].startsWith("KUGEL") || wort[0].startsWith("kugel"))) {
            entityname = CIRCLE;
            STEUERZEICHEN = true;
        }
        if (wort[0].startsWith("S") || wort[0].startsWith("s")
            || wort[0].startsWith("Kugel") || wort[0].startsWith("KUGEL") || wort[0].startsWith("kugel")) {
            entityname = SPHERE;
            STEUERZEICHEN = true;
        }
        if (wort[0].startsWith("A") || wort[0].startsWith("a") || wort[0].startsWith("B") || wort[0].startsWith("b")) {
            entityname = ARC;
            STEUERZEICHEN = true;
        }
        if (!STEUERZEICHEN) {
            if (debug) System.out.println("kein Steuerzeichen");
            try {
                Double.parseDouble(wort[0]); // Löst einen Fehler aus, wenn keine Zahl
                switch(entityname) { // Fragt den bisherigen entitynamen ab.
                    case OFFEN: // Erstes Element, ohne Steuerzeichen
                        entityname = POINT;
                        if (debug) System.out.println("POINT, ohne Steuerzeichen");
                    case POINT:
                        ZEILEVORRÜCKEN = false;
                        break;
                    case LINE: // entityname noch vom letzten Element her gesetzt.
                        ZEILEVORRÜCKEN = false;
                        break;
                    default:
                        System.out.println("Zeile "+zeilennr+": Steuerzeichen erwartet. Vorgefunden: " + wort[0]);
                        entityname = OTHER;
                }
            }
            catch (NumberFormatException e) {
                if (debug) System.out.println("NumberFormatException, Zahl erwartet");
                entityname = OTHER;
            }
        }

        if (debug) System.out.println("entitname = " + entityname);
        double[] werte;
        
        switch (entityname) {
            case POINT:
                if (ZEILEVORRÜCKEN) {
                    wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                }
                entity = pktParsen(wort);
                if (entity == null) FEHLER = true;
                break;
            case LINE:
                if (ZEILEVORRÜCKEN) { // bedeutet auch, dass es die erste Linie einer allfälligen Polylinie ist.
                    wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                    letzterpkt = pktParsen(wort);
                    if (letzterpkt == null) {FEHLER = true; break;}
                    wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                }
                neuerpkt = pktParsen(wort);
                if (neuerpkt == null) {FEHLER = true; break;}
                entity = new double[6];
                for (int i = 0; i < 3; i++) entity[i] = letzterpkt[i];
                for (int i = 0; i < 3; i++) entity[i+3] = neuerpkt[i];
                letzterpkt = neuerpkt;
                break;
            case SPHERE:
            case CIRCLE:
                if (ZEILEVORRÜCKEN) {
                    wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                }
                
                neuerpkt = pktParsen(wort);
                if (neuerpkt == null) {FEHLER = true; break;}
                entity = new double[4];
                for (int i = 0; i < 3; i++) entity[i] = neuerpkt[i];
                wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                werte = werteParsen(wort, 1);
                if (werte == null) {FEHLER = true; break;}
                entity[3] = werte[0]; // Radius
                break;
            case ARC:
                if (ZEILEVORRÜCKEN) {
                    wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                }
                neuerpkt = pktParsen(wort);
                if (neuerpkt == null) {FEHLER = true; break;}
                entity = new double[6];
                for (int i = 0; i < 3; i++) entity[i] = neuerpkt[i];
                wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                werte = werteParsen(wort, 3);
                if (werte == null) {FEHLER = true; break;}
                for (int i = 0; i < 3; i++) entity[i+3] = werte[i]; // Radius, Startwinkel, Endwinkel
                /*
                switch (wort.length) {
                    case 3:
                        werte = werteParsen(wort, 3);
                        if (werte == null) {FEHLER = true; break;}
                        for (int i = 0; i < 3; i++) entity[i+3] = werte[i]; // Radius, Startwinkel, Endwinkel
                        break;
                    case 1:
                        werte = werteParsen(wort, 1);
                        if (werte == null) {FEHLER = true; break;}
                        entity[3] = werte[0]; // Radius
                        wort = zeileEinlesen(); if (wort == null) return false; // Dateiende
                        werte = werteParsen(wort, 2);
                        if (werte == null) {FEHLER = true; break;}
                        for (int i = 0; i < 2; i++) entity[i+4] = werte[i]; // Startwinkel, Endwinkel
                        System.out.println("Zeile "+zeilennr+": This data format for ARC might not be supported in future.");
                        break;
                    default:
                        if (wort.length > 3) System.out.println("Warnung: zu viele Parameter in Zeile " + zeilennr);
                        if (wort.length < 1) System.out.println("Warnung: zu wenige Parameter in Zeile " + zeilennr);
                        if (wort.length == 2) System.out.println("Warnung: falsche Anzahl Parameter in Zeile " + zeilennr);
                        FEHLER = true;
                }
                */
                break;
            case OTHER:
            default:
        }
        return true;
    }
    
    public String getEntityName() {
        assert entityname != OFFEN;
        String name;
        switch (entityname) {
            case POINT:
                name = "Point";
                break;
            case LINE:
                name = "Line";
                break;
            case CIRCLE:
                name = "Circle";
                break;
            case ARC:
                name = "Arc";
                break;
            case SPHERE:
                name = "Sphere";
                break;
            case OTHER:
            default:
                name = "Other";
        }
        if (FEHLER) name = "Fehler";
        return name;
    }
    
    public double[] getEntity() {
        boolean OK;
        switch (entityname) {
            case POINT:
            case LINE:
            case CIRCLE:
            case ARC:
            case SPHERE:
                OK = true;
                break;
            case OTHER:
            default:
                OK = false;
        }
        if (FEHLER) OK = false;
        
        if (OK) return entity;
        else return null;
    }
    
    private String[] zeileEinlesen() {
        String zeile = "";
        StringTokenizer tokens;
        String[] wort = null; // = new String[10];
        boolean LEEREZEILE;
        do {           
            zeilennr++;
            LEEREZEILE = false;
            try {
                zeile = eingabe.readLine();
            }
            catch(IOException e) {
                Fehlermeldung = "Fehler in der Datei " + datei + ", Zeile " + zeilennr + '\n' + "Fehlermeldung: " + e;
                System.out.println(Fehlermeldung);
                LEEREZEILE = true;
                continue;
            }
            if (zeile == null) { // Dateiende erreicht
                wort = null;
                return wort;
            }
            tokens = new StringTokenizer(zeile, " ,;\t");
            wort = new String[tokens.countTokens()];
            for (int i = 0; tokens.hasMoreTokens(); i++) {
                wort[i] = tokens.nextToken();
            }
            if (wort.length == 0) {
                LEEREZEILE = true;
                continue;
            }
            if (wort[0].startsWith("#") || wort[0].startsWith("//") || wort[0].startsWith("rem")) {
                LEEREZEILE = true;
                continue;
            }
        }
        while (LEEREZEILE == true);
        return wort;
    }
    
    private double[] pktParsen(String[] wort) {
        double[] pkt = new double[3];
        switch (wort.length) {
            case 3:
                try {
                    pkt[0] = Double.parseDouble(wort[0]);
                    pkt[1] = Double.parseDouble(wort[1]);
                    pkt[2] = Double.parseDouble(wort[2]);
                }
                catch (NumberFormatException e) {
                    System.out.println("Nicht 3 Zahlen in Zeile " + zeilennr + "\n Fehlermeldung: " + e.getMessage());
                    return null;
                }
                break;
            case 2:
                try {
                    pkt[0] = Double.parseDouble(wort[0]);   // x
                    pkt[1] = 0;                             // y = 0 in 2D
                    pkt[2] = Double.parseDouble(wort[1]);   // z
                }
                catch (NumberFormatException e) {
                    System.out.println("Nicht 3 Zahlen in Zeile " + zeilennr + "\n Fehlermeldung: " + e.getMessage());
                    return null;
                }
                break;
            default:
                if (wort.length > 3) System.out.println("Warnung: zu viele Parameter in Zeile " + zeilennr);
                if (wort.length < 2) System.out.println("Warnung: zu wenige Parameter in Zeile " + zeilennr);
                return null;
        }
        return pkt;
    }
    
    private double[] werteParsen(String[] wort, int anzahl) {
        assert anzahl > 0;
        double[] werte = new double[anzahl];
        if (wort.length == anzahl) {
            try {
                for (int i = 0; i < anzahl; i++) werte[i] = Double.parseDouble(wort[i]);
            }
            catch (NumberFormatException e) {
                System.out.println("Nicht " +anzahl+ " Zahlen in Zeile " + zeilennr + "\n Fehlermeldung: " + e.getMessage());
                return null;
            }
        }
        else {
            if (wort.length > anzahl) {
                if (wort[anzahl].startsWith("#") || wort[anzahl].startsWith("//") || wort[anzahl].startsWith("rem")) {
                    try {
                        for (int i = 0; i < anzahl; i++) werte[i] = Double.parseDouble(wort[i]);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Nicht " +anzahl+ " Zahlen in Zeile " + zeilennr + "\n Fehlermeldung: " + e.getMessage());
                        return null;
                    }
                }
                else {
                    System.out.println("Warnung: zu viele Parameter in Zeile " + zeilennr);
                    return null;
                }
            }
            if (wort.length < anzahl){
                System.out.println("Warnung: zu wenige Parameter in Zeile " + zeilennr);
                return null;
            }
        }
        return werte;
    }
    
}
