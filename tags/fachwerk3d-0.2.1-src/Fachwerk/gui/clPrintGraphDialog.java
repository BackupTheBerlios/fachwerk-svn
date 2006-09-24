/*
 * clPrintGraphDialog.java
 *
 * Created on 28. April 2004
 */

package Fachwerk.gui;

import Fachwerk.statik.Fkt;
import java.math.*;
import java.util.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2004 A.Vontobel <qwert2003@users.sourceforge.net>
 *                                     <qwert2003@users.berlios.de>
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
public class clPrintGraphDialog extends javax.swing.JDialog {
    
    ResourceBundle dialogRB;
    Locale locale;
    double maxmst;
    double mst;
    double einheit = 1; // zB: cm --> 0.01 m
    boolean ABGEBROCHEN = false;
    
    final int MST_MAX = 0;
    final int MST_INDIVIDUELL = 1;
    final int MST_VORSCHLAG = 2;
    
    final double mstliste[] = {1, 2, 5}; // vorzuschlagende Massstäbe: 1:10, 1: 20, 1: 50, 1: 100, etc
    
        
    /** Dialog zur Abfrage über den Massstab, mit welchem die Graphik gedruckt werden soll. */
    public clPrintGraphDialog(java.awt.Frame parent, double maxmst, Locale lc) {
        super(parent, "Drucken Graph", true);        
        this.maxmst = maxmst;
        //mst = maxmst; // falls etwas schiefgeht.
        
        // übersetzen
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-dialoge", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-dialoge für " + locale.toString());
        }
        
        
        initComponents();
        rbMst_max.setText("1:" + Fkt.nf(maxmst, 1));        
        übersetzen();
        pack(); 
        zentriere(parent);
        show();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbMassstab = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rbMst_max = new javax.swing.JRadioButton();
        rbMst_individuell = new javax.swing.JRadioButton();
        rbMst_vorschlag = new javax.swing.JRadioButton();
        feldMst = new javax.swing.JTextField();
        lbEinheit = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        rb_m = new javax.swing.JRadioButton();
        rb_strangeunit = new javax.swing.JRadioButton();
        feldStrangeunit = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        knopfDrucken = new javax.swing.JButton();
        knopfAbbrechen = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lbMassstab.setText("Massstab");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(lbMassstab, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridLayout(0, 1));

        rbMst_max.setSelected(true);
        rbMst_max.setText("max");
        buttonGroup1.add(rbMst_max);
        jPanel5.add(rbMst_max);

        rbMst_individuell.setText("1 : ");
        buttonGroup1.add(rbMst_individuell);
        jPanel5.add(rbMst_individuell);

        rbMst_vorschlag.setText("1:10 , 1:20 , 1:50 , 1:100");
        buttonGroup1.add(rbMst_vorschlag);
        jPanel5.add(rbMst_vorschlag);

        jPanel4.add(jPanel5);

        feldMst.setColumns(6);
        feldMst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldMstFocusGained(evt);
            }
        });
        feldMst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                feldMstKeyTyped(evt);
            }
        });

        jPanel4.add(feldMst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jPanel4, gridBagConstraints);

        lbEinheit.setText("Einheit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(lbEinheit, gridBagConstraints);

        rb_m.setSelected(true);
        rb_m.setText("m");
        buttonGroup2.add(rb_m);
        rb_m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_mActionPerformed(evt);
            }
        });

        jPanel7.add(rb_m);

        buttonGroup2.add(rb_strangeunit);
        rb_strangeunit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_strangeunitActionPerformed(evt);
            }
        });

        jPanel7.add(rb_strangeunit);

        feldStrangeunit.setColumns(6);
        feldStrangeunit.setText("0.01");
        feldStrangeunit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldStrangeunitActionPerformed(evt);
            }
        });
        feldStrangeunit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldStrangeunitFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                feldStrangeunitFocusLost(evt);
            }
        });
        feldStrangeunit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                feldStrangeunitKeyTyped(evt);
            }
        });

        jPanel7.add(feldStrangeunit);

        jLabel1.setText("m");
        jPanel7.add(jLabel1);

        jPanel6.add(jPanel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jPanel6, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        knopfDrucken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png")));
        knopfDrucken.setText("drucken");
        knopfDrucken.setToolTipText("");
        knopfDrucken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfDruckenActionPerformed(evt);
            }
        });

        jPanel3.add(knopfDrucken);

        knopfAbbrechen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_cancel.png")));
        knopfAbbrechen.setText("abbrechen");
        knopfAbbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfAbbrechenActionPerformed(evt);
            }
        });

        jPanel3.add(knopfAbbrechen);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void feldStrangeunitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldStrangeunitActionPerformed
        einlesen();
        rbMst_max.setText("1:" + Fkt.nf(maxmst*einheit, 1));
    }//GEN-LAST:event_feldStrangeunitActionPerformed

    private void feldStrangeunitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldStrangeunitFocusLost
        einlesen();
        rbMst_max.setText("1:" + Fkt.nf(maxmst*einheit, 1));
    }//GEN-LAST:event_feldStrangeunitFocusLost

    private void rb_strangeunitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_strangeunitActionPerformed
        einlesen();
        rbMst_max.setText("1:" + Fkt.nf(maxmst*einheit, 1));
    }//GEN-LAST:event_rb_strangeunitActionPerformed

    private void rb_mActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_mActionPerformed
        rbMst_max.setText("1:" + Fkt.nf(maxmst, 1));
    }//GEN-LAST:event_rb_mActionPerformed

    private void feldStrangeunitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_feldStrangeunitKeyTyped
        rb_strangeunit.setSelected(true);
    }//GEN-LAST:event_feldStrangeunitKeyTyped

    private void feldStrangeunitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldStrangeunitFocusGained
        feldMst.setSelectionStart(0); feldMst.setSelectionEnd(feldMst.getText().length());
    }//GEN-LAST:event_feldStrangeunitFocusGained

    private void feldMstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldMstFocusGained
        feldMst.setSelectionStart(0); feldMst.setSelectionEnd(feldMst.getText().length());
    }//GEN-LAST:event_feldMstFocusGained

    private void feldMstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_feldMstKeyTyped
        rbMst_individuell.setSelected(true);
    }//GEN-LAST:event_feldMstKeyTyped

    private void knopfDruckenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfDruckenActionPerformed
        ABGEBROCHEN = false;
        if (einlesen()) {
            ABGEBROCHEN = false;
            setVisible(false);
        }
    }//GEN-LAST:event_knopfDruckenActionPerformed

    private void knopfAbbrechenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfAbbrechenActionPerformed
        ABGEBROCHEN = true;
        setVisible(false);
    }//GEN-LAST:event_knopfAbbrechenActionPerformed

    private void übersetzen() {
        this.setTitle(tr("titel-PrintGraphDialog"));
        
        knopfDrucken.setText(tr("drucken"));
        knopfAbbrechen.setText(tr("abbrechen"));
        lbMassstab.setText(tr("Massstab"));
        lbEinheit.setText(tr("Einheit"));
        rbMst_max.setToolTipText(tr("ttipMstmax"));
    }
    
    public boolean abgebrochen() {
        if (ABGEBROCHEN) return true;
        else return false;
    }
    
    /** @return gewählter Massstab*/
    public double getMst() {
        return mst;
    }
    
    /** @return gewählte Einheit z.B. cm --> 0.01 m*/
    public double getEinheit() {
        return einheit;
    }
    
    /**
     * Liest die Angaben ein. Liefert true, wenn erfolgreich
     */
    public boolean einlesen() {
        // Einheit einlesen
        if (rb_strangeunit.isSelected()) {
            try {
                    einheit = Double.parseDouble(feldStrangeunit.getText());
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.toString());
                    feldStrangeunit.selectAll();
                    return false;            
                }
            
        }
        else {
            einheit = 1d;
            assert rb_m.isSelected();
        }
        
        // Massstab-Wahl einlesen
        int mstwahl;
        try {
            if (rbMst_vorschlag.isSelected()) mstwahl = MST_VORSCHLAG;
            else {
                if (rbMst_individuell.isSelected()) mstwahl = MST_INDIVIDUELL;
                else {
                    mstwahl = MST_MAX;
                    assert rbMst_max.isSelected();
                }
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.toString());
            return false;            
        }       
        switch(mstwahl) {
            case MST_VORSCHLAG:
                mst = maxmst * einheit;
                double[] nächstgrösser = new double[mstliste.length];
                double temp;
                for (int i = 0; i < mstliste.length; i++) {
                    temp = Math.ceil(Math.log(mst/mstliste[i]) / Math.log(10));
                    nächstgrösser[i] = mstliste[i] * Math.pow(10, temp);
                }
                mst = Fkt.min(nächstgrösser);
                // debug: System.out.println("vorgeschlagener Massstab 1:" + Fkt.nf(mst,1));
                break;
            case MST_INDIVIDUELL:
                try {
                    mst = Double.parseDouble(feldMst.getText());
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.toString());
                    feldMst.selectAll();
                    return false;            
                }
                break;
            case MST_MAX:
            default:
                mst = maxmst * einheit;
        }
                
        
                
        
        return true;
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextField feldMst;
    private javax.swing.JTextField feldStrangeunit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton knopfAbbrechen;
    private javax.swing.JButton knopfDrucken;
    private javax.swing.JLabel lbEinheit;
    private javax.swing.JLabel lbMassstab;
    private javax.swing.JRadioButton rbMst_individuell;
    private javax.swing.JRadioButton rbMst_max;
    private javax.swing.JRadioButton rbMst_vorschlag;
    private javax.swing.JRadioButton rb_m;
    private javax.swing.JRadioButton rb_strangeunit;
    // End of variables declaration//GEN-END:variables
    
}
