/*
 * clStabDialog.java
 *
 * Created on 11. September 2003, 19:55
 */

package Fachwerk3D.gui3D;

import Fachwerk.statik.Fkt;
import Fachwerk3D.statik3D.*;
import java.util.*;

/**
 * Fachwerk3D - treillis3D
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
public class clStabDialog3D extends javax.swing.JDialog implements inKonstante3D {
    
    int nr;
    clWissenderStab3D st;
    ResourceBundle dialogRB;
    Locale locale;
    
    boolean GEÄNDERT = false;
    boolean ZURÜCKSETZEN = false;
    /*
    // Stabattribute
    private final int UNBEST = 0;
    private final int BER = 1;
    private final int GESETZT = 2;
    //private static final int WIDERSPR = 3;
    private final int NICHTSETZBAR = 4;
    */
    String statusstr, nrstr, vonknstr, bisknstr, kraftstr, lstr;
    double kraft;
    int status;
    //int vonkn, biskn;
    
    /** Creates new form clStabDialog */
    public clStabDialog3D(java.awt.Frame parent, int p_nr, clWissenderStab3D p_st, Locale lc) {
        super(parent, "Stab - Eigenschaften", true);        
        nr = p_nr;
        st = p_st;
        
        // übersetzen
        locale = lc;
        dialogRB = ResourceBundle.getBundle("Fachwerk/locales/gui-dialoge", locale);
        if (dialogRB == null) {
            System.err.println("FEHLER: gui-dialoge für " + locale.toString());
        }
        
        // Knoteninfos einleisen
        
        // Nr
        nrstr = Integer.toString(nr);
        
        // Stabstatus
        status = st.stab.getStatus();
        switch (status) {
            case UNBEST:
                statusstr = tr("UNBESTIMMT");
                break;
            case BER:
                statusstr = tr("BERECHNET");
                break;
            case GESETZT:
                statusstr = tr("GESETZT");
                break;
            case NICHTSETZBAR:
                assert false;
                break;
            default:
                assert false;              
        }
        
        // Stabkraft
        //kraft = st.stab.getKraft();
        switch (status) {
            case GESETZT:
                kraft = st.stab.getKraft();
                kraftstr = Double.toString(kraft);
                break;
            case UNBEST:
                kraftstr = "";
                break;
            case BER:
                kraft = st.stab.getKraft();
                kraftstr = Fkt.nf(kraft, 1);
                break;
            
            case NICHTSETZBAR:
                kraftstr = "---";
                assert false;
                break;
            default:
                kraftstr = "";
                assert false;              
        }
        
        // Anschlussknoten
        vonknstr = Integer.toString(st.von);
        bisknstr = Integer.toString(st.bis);
        
        
        
        
        initComponents();
        tastenbelegen();
        übersetzen();
        pack(); 
        zentriere(parent);
        // Cursor setzen
        if (status==UNBEST || status==GESETZT) feldKraft.requestFocus();
        else btNichtstun.requestFocus();
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        feldVon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        feldBis = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        knopfZurücksetzen = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        feldKraft = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbNr = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btÄndern = new javax.swing.JButton();
        btNichtstun = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(3, 1));

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel4.setBackground(new java.awt.Color(204, 255, 255));
        jLabel2.setText("verbindet Knoten ");
        jPanel4.add(jLabel2);

        feldVon.setBackground(new java.awt.Color(204, 255, 255));
        feldVon.setColumns(3);
        feldVon.setEditable(false);
        feldVon.setText(vonknstr);
        jPanel4.add(feldVon);

        jLabel3.setText("mit");
        jPanel4.add(jLabel3);

        feldBis.setBackground(new java.awt.Color(204, 255, 255));
        feldBis.setColumns(3);
        feldBis.setEditable(false);
        feldBis.setText(bisknstr);
        jPanel4.add(feldBis);

        jPanel1.add(jPanel4);

        jPanel7.setBackground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Status:  ");
        jPanel7.add(jLabel5);

        lbStatus.setText(statusstr);
        jPanel7.add(lbStatus);

        knopfZurücksetzen.setText("--> UNBESTIMMT");
        knopfZurücksetzen.setToolTipText("l\u00f6scht die gesetzte Kraft");
        if (status == GESETZT) knopfZurücksetzen.setEnabled(true);
        else knopfZurücksetzen.setEnabled(false);
        knopfZurücksetzen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfZurücksetzenActionPerformed(evt);
            }
        });

        jPanel7.add(knopfZurücksetzen);

        jPanel1.add(jPanel7);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Kraft N = ");
        jPanel5.add(jLabel6);

        feldKraft.setColumns(7);
        switch (status) {
            case UNBEST:
            case GESETZT:
            feldKraft.setEditable(true);
            break;
            default:
            feldKraft.setEditable(false);
        }
        feldKraft.setText(kraftstr);
        feldKraft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feldKraftActionPerformed(evt);
            }
        });
        feldKraft.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feldKraftFocusGained(evt);
            }
        });

        jPanel5.add(feldKraft);

        jLabel7.setText("kN");
        jPanel5.add(jLabel7);

        jPanel1.add(jPanel5);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Stab Nr ");
        jPanel2.add(jLabel1);

        lbNr.setFont(new java.awt.Font("Dialog", 1, 14));
        lbNr.setText(nrstr);
        jPanel2.add(lbNr);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        btÄndern.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png")));
        btÄndern.setText("\u00e4ndern");
        btÄndern.setToolTipText("allf\u00e4llige \u00c4nderungen \u00fcbernehmen");
        btÄndern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btÄndernActionPerformed(evt);
            }
        });

        jPanel3.add(btÄndern);

        btNichtstun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_cancel.png")));
        btNichtstun.setText("NICHTS TUN !");
        btNichtstun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNichtstunActionPerformed(evt);
            }
        });

        jPanel3.add(btNichtstun);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void feldKraftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feldKraftActionPerformed
        // Verhalten, wie Knopf "ändern" gedrückt
        GEÄNDERT = true;
        setVisible(false);
    }//GEN-LAST:event_feldKraftActionPerformed

    private void feldKraftFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feldKraftFocusGained
        feldKraft.setSelectionStart(0); feldKraft.setSelectionEnd(feldKraft.getText().length());
    }//GEN-LAST:event_feldKraftFocusGained

    private void knopfZurücksetzenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfZurücksetzenActionPerformed
        if (knopfZurücksetzen.isSelected()) ZURÜCKSETZEN = true;
        else ZURÜCKSETZEN = false;
    }//GEN-LAST:event_knopfZurücksetzenActionPerformed

    private void btNichtstunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNichtstunActionPerformed
        GEÄNDERT = false;
        setVisible(false);
    }//GEN-LAST:event_btNichtstunActionPerformed

    private void btÄndernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btÄndernActionPerformed
        GEÄNDERT = true;
        setVisible(false);
    }//GEN-LAST:event_btÄndernActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        //dispose();
    }//GEN-LAST:event_closeDialog
    
    private void tastenbelegen() {
        javax.swing.ActionMap am = getRootPane().getActionMap();
        javax.swing.InputMap im = getRootPane().getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        // ESC - Taste
        javax.swing.KeyStroke EscapeStroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        Object EscapeObjekt = new Object();
        javax.swing.Action EscapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNichtstunActionPerformed(evt);
            }
        };
        im.put(EscapeStroke, EscapeObjekt);
        am.put(EscapeObjekt, EscapeAction);
    }
    
    private void übersetzen() {
        this.setTitle(tr("titel-StDialog"));
        btÄndern.setText(tr("uebernehmen"));
        btÄndern.setToolTipText(tr("ttipuebernehmen"));
        btNichtstun.setText(tr("nichtsaendern"));
                         
        jLabel1.setText(tr("StabNr"));
        jLabel2.setText(tr("verbindetKnoten"));
        jLabel3.setText(tr("mit"));
        jLabel5.setText(tr("Status"));
        knopfZurücksetzen.setText(tr("knUnbestimmt"));
        knopfZurücksetzen.setToolTipText(tr("ttipknUnbestimmt"));
        jLabel6.setText(tr("KraftN"));	
    }
    
    public boolean hatGeändert() {
        if (GEÄNDERT) return true;
        else return false;
    }
    
    /**
     * Liest neue Knotendaten ein. Liefert true, wenn erfolgreich
     */
    public boolean einlesen() {
        if (ZURÜCKSETZEN) {
            st.stab.zurücksetzen(true);
            return true;
        }
        
        else {       
            try {
                kraft = Fkt.holZahl(feldKraft.getText());                                       
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.toString());
                return false;            
            }
                    
            switch (status) {
                case UNBEST:
                case GESETZT:
                    st.stab.setKraft(GESETZT, kraft);
                    break;
                    
                case BER:
                    System.out.println("es kann nichts geändert werden");
                    break;            
                case NICHTSETZBAR:
                    assert false;
                    break;
                default:
                    assert false;              
            }                        
            return true;
        }
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
    private javax.swing.JButton btNichtstun;
    private javax.swing.JButton btÄndern;
    private javax.swing.JTextField feldBis;
    private javax.swing.JTextField feldKraft;
    private javax.swing.JTextField feldVon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JToggleButton knopfZurücksetzen;
    private javax.swing.JLabel lbNr;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables
    
}
