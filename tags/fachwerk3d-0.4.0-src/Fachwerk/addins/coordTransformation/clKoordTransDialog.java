/*
 * clCoordTransDialog.java
 *
 * Created on 7. June 2004
 */

package Fachwerk.addins.coordTransformation;

import java.util.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2004 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                      <qwert2003@users.berlios.de>
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
public class clKoordTransDialog extends javax.swing.JDialog {
        
    double faktor = 1;
    double dx = 0;
    double dz = 0;
    String einleitung;
    boolean OK = false;  
    ResourceBundle dialogRB;
    Locale locale;
    
    /** Creates new form clStringDialog 
     * p_was: Knoten: 1, Stab: 2*/
    public clKoordTransDialog(java.awt.Frame parent, String titel, Locale lc) {        
        super(parent, titel, true);
        locale = lc;
                              
        initComponents();
        // übersetzen
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-dialoge", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-dialoge für " + locale.toString());
            return;
        }
        else {
            knopfAbbruch.setText(tr("Abbruch"));
            knopfOK.setText(tr("OK"));
        }
        pack();
        zentriere(parent);
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        knopfOK = new javax.swing.JButton();
        knopfAbbruch = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lbFaktor = new javax.swing.JLabel();
        feldFaktor = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lbdx = new javax.swing.JLabel();
        felddx = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lbdz = new javax.swing.JLabel();
        felddz = new javax.swing.JTextField();
        lbEinleitung = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        knopfOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png")));
        knopfOK.setText("OK !");
        knopfOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfOKActionPerformed(evt);
            }
        });

        jPanel1.add(knopfOK);

        knopfAbbruch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_cancel.png")));
        knopfAbbruch.setText("Abbruch");
        knopfAbbruch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfAbbruchActionPerformed(evt);
            }
        });

        jPanel1.add(knopfAbbruch);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        lbFaktor.setText("* ");
        jPanel3.add(lbFaktor);

        feldFaktor.setColumns(3);
        feldFaktor.setText("1");
        feldFaktor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldFaktorActionPerformed(evt);
            }
        });

        jPanel3.add(feldFaktor);

        jPanel2.add(jPanel3);

        lbdx.setText("dx = ");
        jPanel4.add(lbdx);

        felddx.setColumns(3);
        felddx.setText("0");
        felddx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                felddxActionPerformed(evt);
            }
        });

        jPanel4.add(felddx);

        jPanel2.add(jPanel4);

        lbdz.setText(" dz = ");
        jPanel5.add(lbdz);

        felddz.setColumns(3);
        felddz.setText("0");
        felddz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                felddzActionPerformed(evt);
            }
        });

        jPanel5.add(felddz);

        jPanel2.add(jPanel5);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        lbEinleitung.setText(einleitung);
        getContentPane().add(lbEinleitung, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void felddzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_felddzActionPerformed
        try{
            double eingabedz = Double.parseDouble(felddz.getText());
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            felddz.setSelectionStart(0); felddz.setSelectionEnd(felddz.getText().length());
        }
    }//GEN-LAST:event_felddzActionPerformed

    private void felddxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_felddxActionPerformed
        try{
            double eingabedx = Double.parseDouble(felddx.getText());
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            felddx.setSelectionStart(0); felddx.setSelectionEnd(felddx.getText().length());
        }
    }//GEN-LAST:event_felddxActionPerformed

    private void feldFaktorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldFaktorActionPerformed
        try{
            double eingabefaktor = Double.parseDouble(feldFaktor.getText());
            if (Math.abs(eingabefaktor) < 1E-9) {
                System.out.println("Faktor < 1E-9");
                feldFaktor.setSelectionStart(0); feldFaktor.setSelectionEnd(feldFaktor.getText().length());
            }
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            feldFaktor.setSelectionStart(0); feldFaktor.setSelectionEnd(feldFaktor.getText().length());
        }
    }//GEN-LAST:event_feldFaktorActionPerformed

    private void knopfAbbruchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfAbbruchActionPerformed
        OK = false;
        setVisible(false);
    }//GEN-LAST:event_knopfAbbruchActionPerformed

    private void knopfOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfOKActionPerformed
        if (einlesen()) {
            OK = true;
            setVisible(false);
        }
    }//GEN-LAST:event_knopfOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        OK = false;
        setVisible(false);
        //dispose();
    }//GEN-LAST:event_closeDialog
   
    private boolean einlesen() {
        double eingabefaktor;
        double eingabedx;
        double eingabedz;
        
        // Faktor
        try{
            eingabefaktor = Double.parseDouble(feldFaktor.getText());
            if (Math.abs(eingabefaktor) < 1E-9) {
                System.out.println("Faktor < 1E-9");                
                feldFaktor.setSelectionStart(0); feldFaktor.setSelectionEnd(feldFaktor.getText().length());
                return false;
            }
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            feldFaktor.setSelectionStart(0); feldFaktor.setSelectionEnd(feldFaktor.getText().length());
            return false;
        }
        
        // dx
        try{
            eingabedx = Double.parseDouble(felddx.getText());
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            felddx.setSelectionStart(0); felddx.setSelectionEnd(felddx.getText().length());
            return false;
        }
        
        // dz
        try{
            eingabedz = Double.parseDouble(felddz.getText());
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            felddz.setSelectionStart(0); felddz.setSelectionEnd(felddz.getText().length());
            return false;
        }
        
        faktor = eingabefaktor;
        dx = eingabedx;
        dz = eingabedz;
        return true;
    }
    
    /** Gibt die eingegebene Antwort zurück. Bei einem Dialogabbruch: null
     @returns Faktor, dx, dz*/    
    public double[] getAntwort() {
        if (OK) {
            double[] antwort = new double[3];
            antwort[0] = faktor;
            antwort[1] = dx;
            antwort[2] = dz;
            return antwort;
        }
        else return null;
    }
    
    private void zentriere(java.awt.Frame hf) {
        java.awt.Point OL = hf.getLocationOnScreen();
        OL.translate(hf.getWidth()/2, hf.getHeight()/2);
        OL.translate(-this.getWidth()/2, -this.getHeight()/2);
        if (OL.getX() < 0) OL.setLocation(0, OL.getY());
        if (OL.getY() < 0) OL.setLocation(OL.getX(), 0);
        this.setLocation(OL);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField feldFaktor;
    private javax.swing.JTextField felddx;
    private javax.swing.JTextField felddz;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton knopfAbbruch;
    private javax.swing.JButton knopfOK;
    private javax.swing.JLabel lbEinleitung;
    private javax.swing.JLabel lbFaktor;
    private javax.swing.JLabel lbdx;
    private javax.swing.JLabel lbdz;
    // End of variables declaration//GEN-END:variables
    
}
