/*
 * Fkt.java
 *
 * Created on 26. Dezember 2002, 16:29
 */

package Fachwerk.statik;

/** 
 * Copyright (c) 2003 - 2006 A.Vontobel <qwert2003@users.berlios.de>
 *                                      <qwert2003@users.sourceforge.net>
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
public final class Fkt {
    
    /** Creates a new instance of Funktionen */
    public Fkt() {
    }
    
    public static double holZahl(java.lang.String str) {
        Double a;
        
        try {
            a = Double.valueOf  ( str );
            return  a.doubleValue ();
        }
        catch (NumberFormatException e)
        {
            return 0.0; // ev besser: return Double.NaN;
        }
    }
    
    public static int holInt(java.lang.String str) {
        int a;
        
        try {
            a = Integer.parseInt( str );
            return  a;
        }
        catch (NumberFormatException e)
        {
            return -1;  // ev besser Fehler weiterleiten
        }
    }
    
    public static double fix(double zahl, int anzStellen) {
        double erweitern = Math.pow(10d, anzStellen);
        return Math.round(erweitern * zahl) / erweitern;
    }
    
    public static double max(double[] liste) {
        double maximal = liste[0];
        for (int i=0 ; i < liste.length; i++) {
            if (liste[i] > maximal) maximal = liste[i];
        }
        return maximal;
    }
    
    public static double max(double[][] liste) {
        double maximal = liste[0][0];
        for (int i=0 ; i < liste.length; i++) {
            for (int j = 0; j < liste[i].length; j++) {
                if (liste[i][j] > maximal) maximal = liste[i][j];
            }
        }
        return maximal;
    }
    
    public static double min(double[] liste) {
        double minimal = liste[0];
        for (int i=0 ; i < liste.length; i++) {
            if (liste[i] < minimal) minimal = liste[i];
        }
        return minimal;
    }
    
    /**
     * löst das 2x2 GLS
     *                    ax1 * s1 + ax2 * s2 = bx
     *                    az1 * s1 + az2 * s2 = bz
     * nach [s1,s2] auf.
     *
     * Vor Gebrauch nötigenfalls überprüfen, dass Determinante != 0, siehe Fkt.det2x2(...)
     */
    public static double[] GLS2x2(double ax1, double ax2, double bx, double az1, double az2, double bz) {
        if (det2x2(ax1, ax2 ,az1, az2) == 0d) {
            System.err.println("Warnung: Determinante = 0, eventuell falsche Resultate!");
        }
        //assert det2x2(ax1, ax2 ,az1, az2) != 0: "Determinante = 0";
        double[] s = new double[2];
        if (Math.abs(ax1) > Math.abs(az1)) {
            s[1] = (bz - bx* az1/ax1) / (az2 - ax2* az1/ax1);
            s[0] = (bx - ax2 * s[1]) / ax1;
            return s;
        }
        else {
            s[1] = (bx - bz* ax1/az1) / (ax2 - az2* ax1/az1);
            s[0] = (bz - az2 * s[1]) / az1;
            return s;
        }
    }
    
    public static double det2x2(double ax1, double ax2, double az1, double az2) {
        return ax1 * az2 - ax2 * az1;
    }
    
    /** Normiert einen Vektor, der ursprüngliche Vektor bleibt unverändert
     @returns normierter Vektor */
    public static double[] normiere(double[] vektor) {
        assert vektor.length > 0;
        double nenner = 0;
        for (int i = 0; i < vektor.length; i++) {
            nenner += vektor[i]*vektor[i];
        }
        nenner = Math.sqrt(nenner);
        
        double[] normiert = new double[vektor.length];
        for (int i = 0; i < vektor.length; i++) {
            normiert[i] = vektor[i] / nenner;
        }
        return normiert;
    }
    
    
    /** Gibt einen Zahlenwert als formatierten String zurück.
     */
    public static String nf(int zahl, int minimaleAnzahlStellen) {
        if (minimaleAnzahlStellen < 1) {
            System.err.print("Formatierungsfehler: minimale Anzahlstellen: ");
            System.err.println(minimaleAnzahlStellen + " < 1");
        }
        StringBuffer ausg = new StringBuffer();
        ausg.append(zahl);
        while (ausg.length() < minimaleAnzahlStellen) {
            ausg.insert(0, " ");
        }
        return ausg.toString();
    }
    
    /** Gibt einen Zahlenwert als formatierten String zurück.
     */
    public static String nf(double zahl, int AnzNachkommaStellen) {
	if (Double.isNaN(zahl)) return "NaN";
	if (Double.isInfinite(zahl)) {
            if (zahl == Double.NEGATIVE_INFINITY) return "-oo";
            else return "+oo";
	}
        StringBuffer ausg = new StringBuffer();
        if (AnzNachkommaStellen < 0) {
            System.err.print("Formatierungsfehler: Anzahl Nachkommastellen: ");
            System.err.println(AnzNachkommaStellen + " < 0");
            ausg.append(zahl);
            return ausg.toString();
        }
        double gerundet = fix(zahl, AnzNachkommaStellen);
        if (AnzNachkommaStellen == 0) ausg.append((int) gerundet);
        else {
            ausg.append(gerundet);
            int vorhNkst = ausg.length() - 1 - ausg.indexOf(".");
            for (int i = vorhNkst; i < AnzNachkommaStellen; i++) {
                ausg.append("0");
            }
        }
        return ausg.toString();
    }
    
    /** Gibt einen Zahlenwert als formatierten String zurück.
     */
    public static String nf(double zahl, int AnzNachkommaStellen, int minAnzStellenVorKomma) {
        StringBuffer ausg = new StringBuffer();
        if (AnzNachkommaStellen < 0 || minAnzStellenVorKomma < 0) {
            System.err.println("Formatierungsfehler");
            ausg.append(zahl);
            return ausg.toString();
        }
        ausg.append(nf(zahl, AnzNachkommaStellen));
        while (ausg.length() < (minAnzStellenVorKomma + 1 + AnzNachkommaStellen)) {
            ausg.insert(0, " ");
        }
        return ausg.toString();
    }
}
