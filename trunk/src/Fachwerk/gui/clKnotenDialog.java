/*
 * clKnotenDialog.java
 *
 * Created on 10. September 2003, 18:57
 */

package Fachwerk.gui;

import Fachwerk.statik.*;
import java.util.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2009 A.Vontobel <qwert2003@users.sourceforge.net>
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
public class clKnotenDialog extends javax.swing.JDialog implements inKonstante {
    
    int nr;
    clKnoten kn;
    ResourceBundle dialogRB;
    Locale locale;
    
    boolean GEÄNDERT = false;
    /*
     // Knotenattribute
    private final int OFFEN = 0;
    private final int FERTIG = 1;
    private final int WIDERSPRUCH = 3;
    // Lagerattribute
    private final int LOS = 0;
    private final int FIX = 2;
    private final int VERSCHIEBLICH = 1;
    */
    String status, Axstr, Azstr, xstr, zstr, Lxstr, Lzstr, alphastr, nrstr;
    double Ax, Az, x, z, Lx, Lz, alpha;
    int lagerbed;
    
    /** Creates new form clKnotenDialog */
    public clKnotenDialog(java.awt.Frame parent, int p_nr, clKnoten p_kn, Locale lc) {
        super(parent, "Knoten - Eigenschaften", true);
        nr = p_nr;
        kn = p_kn;
        
        // übersetzen
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-dialoge", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-dialoge für " + locale.toString());
        }
        
        // Knoteninfos einleisen
        
        // Nr
        nrstr = Integer.toString(nr);
        
        // Knotenstatus
        switch (kn.getKnotenstatus()) {
            case OFFEN:
                status = tr("OFFEN");
                break;
            case FERTIG:
                status = tr("FERTIG");
                break;
            case WIDERSPRUCH:
                status = tr("WIDERSPRUCH");
                break;
            default:
                assert false;
        }
        // Auflagerkräfte
        Axstr = Fkt.nf(kn.getRx(), 1);
        Azstr = Fkt.nf(kn.getRz(), 1);
        
        // Koord
        xstr = Double.toString(kn.getX());
        zstr = Double.toString(kn.getZ());
        
        // Lasten
        Lxstr = Double.toString(kn.getLx());
        Lzstr = Double.toString(kn.getLz());
        
        // Lagerbed
        lagerbed = kn.getLagerbed();
        alphastr = Double.toString(Math.toDegrees(kn.getRalpha()));
        
        
        initComponents();
        tastenbelegen();
        übersetzen();
        pack();
        zentriere(parent);
        // Cursor setzen
        if (kn.getKnotenstatus() == OFFEN) {
            // Testen, ob der Knoten soeben neu eingeführt wurde (Annahme)
            if (kn.getX()==0 && kn.getZ()==0 && kn.getLx()==0 && kn.getLz()==0 && kn.getLagerbed()==LOS) {
                feldX.requestFocus();
            }
            else feldLz.requestFocus();
        }
        else knopfNichtstun.requestFocus();
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rbLOS = new javax.swing.JRadioButton();
        rbVERSCH = new javax.swing.JRadioButton();
        rbFIX = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        feldAlpha = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        feldLx = new javax.swing.JTextField();
        feldLz = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        feldX = new javax.swing.JTextField();
        feldZ = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        feldStatus = new javax.swing.JTextField();
        feldAx = new javax.swing.JTextField();
        feldAz = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbNr = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        knopfÄndern = new javax.swing.JButton();
        knopfNichtstun = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel3.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel2.setText("Lagerkraft");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel4.setText("Ax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("kN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Az");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(jLabel7, gridBagConstraints);

        jLabel8.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel8, gridBagConstraints);

        jLabel10.setText("kN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel1.add(jLabel10, gridBagConstraints);

        jLabel9.setText("Koordinaten  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel11.setText("z");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Lagerbed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel12, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(204, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(0, 1));

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        rbLOS.setBackground(new java.awt.Color(204, 255, 255));
        buttonGroup1.add(rbLOS);
        if (lagerbed == LOS) rbLOS.setSelected(true);
        rbLOS.setText("LOS");
        jPanel5.add(rbLOS);

        rbVERSCH.setBackground(new java.awt.Color(204, 255, 255));
        buttonGroup1.add(rbVERSCH);
        if (lagerbed == VERSCHIEBLICH) rbVERSCH.setSelected(true);
        rbVERSCH.setText("VERSCHIEBLICH");
        jPanel5.add(rbVERSCH);

        rbFIX.setBackground(new java.awt.Color(204, 255, 255));
        buttonGroup1.add(rbFIX);
        if (lagerbed == FIX) rbFIX.setSelected(true);
        rbFIX.setText("FIX");
        jPanel5.add(rbFIX);

        jPanel4.add(jPanel5);

        jLabel17.setText("Gleitrichtung");
        jLabel17.setToolTipText("im Uhrzeigersinn");
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });

        jPanel4.add(jLabel17);

        feldAlpha.setColumns(6);
        feldAlpha.setText(alphastr);
        feldAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldAlphaActionPerformed(evt);
            }
        });
        feldAlpha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldAlphaFocusGained(evt);
            }
        });
        feldAlpha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                feldAlphaKeyTyped(evt);
            }
        });

        jPanel4.add(feldAlpha);

        jLabel18.setText("\u00b0");
        jPanel4.add(jLabel18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jPanel4, gridBagConstraints);

        jLabel13.setText("Last");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Lz");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel1.add(jLabel14, gridBagConstraints);

        jLabel15.setText("Lx");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel15, gridBagConstraints);

        jLabel16.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jLabel16, gridBagConstraints);

        feldLx.setColumns(5);
        feldLx.setText(Lxstr);
        feldLx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldLxActionPerformed(evt);
            }
        });
        feldLx.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldLxFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(feldLx, gridBagConstraints);

        feldLz.setColumns(5);
        feldLz.setText(Lzstr);
        feldLz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldLzActionPerformed(evt);
            }
        });
        feldLz.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldLzFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel1.add(feldLz, gridBagConstraints);

        jLabel19.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        jPanel1.add(jLabel19, gridBagConstraints);

        jLabel20.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 3);
        jPanel1.add(jLabel20, gridBagConstraints);

        jLabel21.setText("kN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(jLabel21, gridBagConstraints);

        jLabel22.setText("kN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 10, 0);
        jPanel1.add(jLabel22, gridBagConstraints);

        feldX.setColumns(6);
        feldX.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        feldX.setText(xstr);
        feldX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldXActionPerformed(evt);
            }
        });
        feldX.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldXFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(feldX, gridBagConstraints);

        feldZ.setColumns(6);
        feldZ.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        feldZ.setText(zstr);
        feldZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldZActionPerformed(evt);
            }
        });
        feldZ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldZFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(feldZ, gridBagConstraints);

        jLabel23.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        jPanel1.add(jLabel23, gridBagConstraints);

        jLabel24.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel24, gridBagConstraints);

        feldStatus.setBackground(new java.awt.Color(204, 255, 255));
        feldStatus.setColumns(11);
        feldStatus.setEditable(false);
        feldStatus.setText(status);
        jPanel1.add(feldStatus, new java.awt.GridBagConstraints());

        feldAx.setBackground(new java.awt.Color(204, 255, 255));
        feldAx.setColumns(5);
        feldAx.setEditable(false);
        feldAx.setText(Axstr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(feldAx, gridBagConstraints);

        feldAz.setBackground(new java.awt.Color(204, 255, 255));
        feldAz.setColumns(5);
        feldAz.setEditable(false);
        feldAz.setText(Azstr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(feldAz, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Knoten Nr");
        jPanel2.add(jLabel1);

        lbNr.setFont(new java.awt.Font("Dialog", 1, 14));
        lbNr.setText(nrstr);
        jPanel2.add(lbNr);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        knopfÄndern.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png")));
        knopfÄndern.setText("\u00fcbernehmen");
        knopfÄndern.setToolTipText("allf\u00e4llige \u00c4nderungen \u00fcbernehmen");
        knopfÄndern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfÄndernActionPerformed(evt);
            }
        });

        jPanel3.add(knopfÄndern);

        knopfNichtstun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_cancel.png")));
        knopfNichtstun.setText("NICHTS \u00c4NDERN !");
        knopfNichtstun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfNichtstunActionPerformed(evt);
            }
        });

        jPanel3.add(knopfNichtstun);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void feldAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldAlphaActionPerformed
        knopfÄndernActionPerformed(evt);
    }//GEN-LAST:event_feldAlphaActionPerformed

    private void feldXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldXActionPerformed
        feldZ.requestFocus();
    }//GEN-LAST:event_feldXActionPerformed

    private void feldLxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldLxActionPerformed
        feldLz.requestFocus();
    }//GEN-LAST:event_feldLxActionPerformed

    private void feldLzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldLzActionPerformed
        knopfÄndernActionPerformed(evt);
    }//GEN-LAST:event_feldLzActionPerformed

    private void feldZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldZActionPerformed
        knopfÄndernActionPerformed(evt);
    }//GEN-LAST:event_feldZActionPerformed

    private void feldAlphaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_feldAlphaKeyTyped
        rbVERSCH.setSelected(true);
    }//GEN-LAST:event_feldAlphaKeyTyped

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        rbVERSCH.setSelected(true);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void feldAlphaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldAlphaFocusGained
        feldAlpha.setSelectionStart(0); feldAlpha.setSelectionEnd(feldAlpha.getText().length());
    }//GEN-LAST:event_feldAlphaFocusGained

    private void feldLzFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldLzFocusGained
        feldLz.setSelectionStart(0); feldLz.setSelectionEnd(feldLz.getText().length());
    }//GEN-LAST:event_feldLzFocusGained

    private void feldLxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldLxFocusGained
        feldLx.setSelectionStart(0); feldLx.setSelectionEnd(feldLx.getText().length());
    }//GEN-LAST:event_feldLxFocusGained

    private void feldZFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldZFocusGained
        feldZ.setSelectionStart(0); feldZ.setSelectionEnd(feldZ.getText().length());
    }//GEN-LAST:event_feldZFocusGained

    private void feldXFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldXFocusGained
        feldX.setSelectionStart(0); feldX.setSelectionEnd(feldX.getText().length());
    }//GEN-LAST:event_feldXFocusGained

    private void knopfÄndernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfÄndernActionPerformed
        GEÄNDERT = true;
        setVisible(false);
    }//GEN-LAST:event_knopfÄndernActionPerformed

    private void knopfNichtstunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfNichtstunActionPerformed
        GEÄNDERT = false;
        setVisible(false);
    }//GEN-LAST:event_knopfNichtstunActionPerformed

    private void tastenbelegen() {
        javax.swing.ActionMap am = getRootPane().getActionMap();
        javax.swing.InputMap im = getRootPane().getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        // ESC - Taste
        javax.swing.KeyStroke EscapeStroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        Object EscapeObjekt = new Object();
        javax.swing.Action EscapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfNichtstunActionPerformed(evt);
            }
        };
        im.put(EscapeStroke, EscapeObjekt);
        am.put(EscapeObjekt, EscapeAction);
    }
    
    private void übersetzen() {
        this.setTitle(tr("titel-KnDialog"));
        
        knopfÄndern.setText(tr("uebernehmen"));
        knopfÄndern.setToolTipText(tr("ttipuebernehmen"));
        knopfNichtstun.setText(tr("nichtsaendern"));
        jLabel1.setText(tr("KnotenNr"));
        jLabel3.setText(tr("Status"));
        jLabel2.setText(tr("Lagerkraft"));
        jLabel9.setText(tr("Koordinaten"));
        jLabel12.setText(tr("Lagerbed"));
        jLabel13.setText(tr("Last"));
        jLabel17.setText(tr("Gleitrichtung"));
        jLabel17.setToolTipText(tr("ttipGleitrtg"));
        feldAlpha.setToolTipText(tr("ttipGleitrtg"));
        
        rbLOS.setText(tr("LOS"));
        rbVERSCH.setText(tr("VERSCHIEBLICH"));
        rbFIX.setText(tr("FIX"));
    }
    
    public boolean hatGeändert() {
        if (GEÄNDERT) return true;
        else return false;
    }
    
    /**
     * Liest neue Knotendaten ein. Liefert true, wenn erfolgreich
     */
    public boolean einlesen() {
        try {
            x = Double.parseDouble(feldX.getText());
            z = Double.parseDouble(feldZ.getText());
            Lx = Double.parseDouble(feldLx.getText());
            Lz = Double.parseDouble(feldLz.getText());
            alpha = Math.toRadians(Double.parseDouble(feldAlpha.getText()));
            
            int anzmarkiert = 0;
            if (rbFIX.isSelected()) {
                anzmarkiert++;
                lagerbed = FIX;
            }
            if (rbVERSCH.isSelected()) {
                anzmarkiert++;
                lagerbed = VERSCHIEBLICH;
            }
            if (rbLOS.isSelected()) {
                anzmarkiert++;
                lagerbed = LOS;
            }
            if (anzmarkiert != 1) throw new IllegalArgumentException("genau eine Lagerbedingung markieren!");
        }
        catch (NumberFormatException e) {
            System.out.println(e.toString());
            return false;
        }
        
        kn.setNeueKoord(x, z);
        kn.setLast(Lx, Lz);
        
        switch (lagerbed) {
            case VERSCHIEBLICH:
                kn.setLager(lagerbed, alpha);
                break;
            case LOS:
            case FIX:
                kn.setLager(lagerbed);
                break;
            default:
                assert false;
        }
        
        return true;
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
            System.err.println("Schluesselwort + " + key + " nicht gefunden fuer " + locale.toString() + " ; " + e.toString());
            return key;
        }
        return übersetzt;
    }

       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField feldAlpha;
    private javax.swing.JTextField feldAx;
    private javax.swing.JTextField feldAz;
    private javax.swing.JTextField feldLx;
    private javax.swing.JTextField feldLz;
    private javax.swing.JTextField feldStatus;
    private javax.swing.JTextField feldX;
    private javax.swing.JTextField feldZ;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton knopfNichtstun;
    private javax.swing.JButton knopfÄndern;
    private javax.swing.JLabel lbNr;
    private javax.swing.JRadioButton rbFIX;
    private javax.swing.JRadioButton rbLOS;
    private javax.swing.JRadioButton rbVERSCH;
    // End of variables declaration//GEN-END:variables
    
}
