

package Fachwerk3D.gui3D;

import java.util.*;
import Fachwerk.statik.Fkt;

/**
 * Fachwerk3D - treillis3D
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
public class clBlickRtgDialog extends javax.swing.JDialog {
    
    private double[] n;
    boolean OK = false;
    ResourceBundle dialogRB;
    Locale locale;
    
    /** Creates new form clZoomDialog */
    public clBlickRtgDialog(double[] n, java.awt.Frame parent, Locale lc) {
        super(parent, "Blickrichtung", true);
        this.n = n;
        locale = lc;
        initComponents();
                
        feldnx.setText(Fkt.nf(n[0],3));
        feldny.setText(Fkt.nf(n[1],3));
        feldnz.setText(Fkt.nf(n[2],3));
        
        übersetzen();
        tastenbelegen();
        pack();
        zentriere(parent);
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        feldnx = new javax.swing.JTextField();
        feldny = new javax.swing.JTextField();
        feldnz = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        knopfOK = new javax.swing.JButton();
        knopfAbbrechen = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        feldnx.setColumns(5);
        feldnx.setText(""+n[0]);
        feldnx.setToolTipText("kleinster z-Wert");
        feldnx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldnxActionPerformed(evt);
            }
        });
        feldnx.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldnxFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(feldnx, gridBagConstraints);

        feldny.setColumns(5);
        feldny.setText(""+n[1]);
        feldny.setToolTipText("kleinster x-Wert");
        feldny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldnyActionPerformed(evt);
            }
        });
        feldny.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldnyFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel1.add(feldny, gridBagConstraints);

        feldnz.setColumns(5);
        feldnz.setText(""+n[2]);
        feldnz.setToolTipText("grösster x-Wert");
        feldnz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldnzActionPerformed(evt);
            }
        });
        feldnz.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldnzFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel1.add(feldnz, gridBagConstraints);

        jLabel1.setText("nx = ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("ny = ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("nz = ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jLabel3, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        knopfOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png"))); // NOI18N
        knopfOK.setText("OK !");
        knopfOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfOKActionPerformed(evt);
            }
        });
        jPanel2.add(knopfOK);

        knopfAbbrechen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_cancel.png"))); // NOI18N
        knopfAbbrechen.setText("Abbruch");
        knopfAbbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfAbbrechenActionPerformed(evt);
            }
        });
        jPanel2.add(knopfAbbrechen);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void feldnzFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldnzFocusGained
        feldnz.selectAll();
    }//GEN-LAST:event_feldnzFocusGained

    private void feldnyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldnyFocusGained
        feldny.selectAll();
    }//GEN-LAST:event_feldnyFocusGained

    private void feldnxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldnxFocusGained
        feldnx.selectAll();
    }//GEN-LAST:event_feldnxFocusGained

    private void knopfAbbrechenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfAbbrechenActionPerformed
        setVisible(false);
        OK = false;
    }//GEN-LAST:event_knopfAbbrechenActionPerformed

    private void knopfOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfOKActionPerformed
        okdurchEnteroderKnopf();
    }//GEN-LAST:event_knopfOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
    }//GEN-LAST:event_closeDialog

private void feldnxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldnxActionPerformed
    feldny.requestFocus();
}//GEN-LAST:event_feldnxActionPerformed

private void feldnyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldnyActionPerformed
    feldnz.requestFocus();
}//GEN-LAST:event_feldnyActionPerformed

private void feldnzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldnzActionPerformed
    okdurchEnteroderKnopf();
}//GEN-LAST:event_feldnzActionPerformed
    
private void okdurchEnteroderKnopf() {
    OK = true;

    n[0] = Fkt.holZahl(feldnx.getText());
    n[1] = Fkt.holZahl(feldny.getText());
    n[2] = Fkt.holZahl(feldnz.getText());

    setVisible(false);
}

private void übersetzen() {
        
        dialogRB = ResourceBundle.getBundle("Fachwerk3D/locales3D/gui3D-dialoge", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-dialoge für " + locale.toString());
        }
        this.setTitle(tr("titel-Blickrtg"));
        knopfOK.setText(tr("OK"));
        knopfAbbrechen.setText(tr("Abbruch"));
    }
    
    public boolean isOK() {
        return OK;
    }
    
    public double[] get() {
        return n;
    }
    
    private void tastenbelegen() {
        javax.swing.ActionMap am = getRootPane().getActionMap();
        javax.swing.InputMap im = getRootPane().getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        // ESC - Taste
        Object EscapeObjekt = new Object();
        javax.swing.KeyStroke EscapeStroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        javax.swing.Action EscapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK = false;
                setVisible(false);
            }
        };
        im.put(EscapeStroke, EscapeObjekt);
        am.put(EscapeObjekt, EscapeAction);
    }
   
    private void zentriere(java.awt.Frame hf) {
        java.awt.Point OL = hf.getLocationOnScreen();
        OL.translate(hf.getWidth()/2, 10);
        OL.translate(-this.getWidth()/2, 0);
        if (OL.getX() < 0) OL.setLocation(0, OL.getY());
        if (OL.getY() < 0) OL.setLocation(OL.getX(), 0);
        this.setLocation(OL);
    }
    
    private String tr(String key) {
        String übersetzt;
        try {übersetzt = dialogRB.getString(key);}
        catch (MissingResourceException e) {
            System.err.println("Schluesselwort " + key + " nicht gefunden fuer " + locale.toString() + " ; " + e.toString());
            return key;
        }
        return übersetzt;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField feldnx;
    private javax.swing.JTextField feldny;
    private javax.swing.JTextField feldnz;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton knopfAbbrechen;
    private javax.swing.JButton knopfOK;
    // End of variables declaration//GEN-END:variables
    
}