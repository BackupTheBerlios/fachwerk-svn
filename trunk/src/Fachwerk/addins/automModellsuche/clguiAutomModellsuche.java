/*
 * clCoordTransDialog.java
 *
 * Created on 7. June 2004
 */

package Fachwerk.addins.automModellsuche;

import Fachwerk.addins.automModellsuche.*;
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
public class clguiAutomModellsuche extends javax.swing.JDialog {
        
    private int faktor = 0;
    private boolean LÖSCHENULLSTÄBE = true;
    private String einleitung;
    boolean OK = false;
    ResourceBundle dialogRB;
    Locale locale;
    
    /** Lasten Skalieren Dialog*/
    public clguiAutomModellsuche(java.awt.Frame parent, Locale lc) {
        super(parent, true); // TODO Titel
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
        
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-addins", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-addins für " + locale.toString());
            return;
        }
        else {
            jLabel1.setText(tr("lb-AutomModellsuche"));
            jLabel1.setToolTipText(tr("ttip-AutomModellsuche"));
            checkbox_löscheNullstäbe.setText(tr("checkbox-AutomModellsuche"));
            checkbox_löscheNullstäbe.setToolTipText(tr("ttip-AutomModellsuche"));
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
    
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        knopfOK = new javax.swing.JButton();
        knopfAbbruch = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        feldFaktor = new javax.swing.JTextField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel5 = new javax.swing.JPanel();
        checkbox_löscheNullstäbe = new javax.swing.JCheckBox();
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

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Automatische Modellsuche");
        jLabel1.setToolTipText("Eliminiert statisch überflüssige St\u00e4be in einem Optimierungsprozess.");
        jPanel4.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4);


        feldFaktor.setColumns(3);
        feldFaktor.setText("0");
        feldFaktor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldFaktorActionPerformed(evt);
            }
        });

        jPanel3.add(feldFaktor);

        jPanel3.add(jDesktopPane1);

        jPanel2.add(jPanel3);

        checkbox_löscheNullstäbe.setSelected(false);
        checkbox_löscheNullstäbe.setText("Nullstäbe löschen");
        checkbox_löscheNullstäbe.setToolTipText("Löscht (u.a. durch den Optimierungsprozess) gesetzte Nullst\u00e4be.");
        jPanel5.add(checkbox_löscheNullstäbe);

        jPanel2.add(jPanel5);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        lbEinleitung.setText(einleitung);
        getContentPane().add(lbEinleitung, java.awt.BorderLayout.NORTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void feldFaktorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldFaktorActionPerformed
        try{
            int eingabefaktor = Integer.parseInt(feldFaktor.getText());
            if (eingabefaktor < 0) {
                System.out.println("Faktor < 0");
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
    }//GEN-LAST:event_closeDialog
   
    private boolean einlesen() {
        int eingabefaktor;
        
        // Faktor
        try{
            eingabefaktor = Integer.parseInt(feldFaktor.getText());
            if (eingabefaktor < 0) {
                System.out.println("Faktor < 0");
                feldFaktor.selectAll();
                return false;
            }
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            feldFaktor.selectAll();
            return false;
        }
        
        faktor = eingabefaktor;
        
        // Auch Stabkräfte skalieren?
        LÖSCHENULLSTÄBE = checkbox_löscheNullstäbe.isSelected();
        
        return true;
    }
    
    /** Gibt den eingegebenen Skalierungsfaktor zurück. Bei einem Dialogabbruch: -1
     @returns Faktor*/
    public int getAntwort_Faktor() {
        if (OK) {
            int antwort = faktor;
            return antwort;
        }
        else return -1;
    }
    
    /** Gibt an, ob die gesetzten Nullstäbe gelöscht werden sollen.
     @returns LÖSCHENULLSTÄBE*/
    public boolean getAntwort_löscheNullstäbe() {
        return LÖSCHENULLSTÄBE;
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
    // Variables declaration
    private javax.swing.JCheckBox checkbox_löscheNullstäbe;
    private javax.swing.JTextField feldFaktor;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton knopfAbbruch;
    private javax.swing.JButton knopfOK;
    private javax.swing.JLabel lbEinleitung;
    // End of variables declaration//GEN-END:variables
    
}
