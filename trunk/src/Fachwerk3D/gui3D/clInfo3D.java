/*
 * clAbout.java
 *
 * Created on 15. September 2003, 19:56
 */

package Fachwerk3D.gui3D;
import java.awt.*;

/**
 *
 * @author  av  <qwert2003@users.berlios.de>
 */
public class clInfo3D extends javax.swing.JDialog {
    
    private String PROGNAME;
    private int HAUPTVER;
    private int UNTERVER;
    private int JAHR;
    
    private final String Copyright1 =
    "Copyright (c) 2003 - ";
    private final String Copyright2 = " A.Vontobel <qwert2003@users.berlios.de>" + '\n' +
    "                                     <qwert2003@users.sourceforge.net>" + '\n' + '\n';
    private String Copyright;
    
    private final String Lizenz_de = 
    "Dieses einfache Fachwerkprogramm verwendet ausschliesslich die " +
    "Gleichgewichtsbedingungen zur Bestimmung der Stabkräfte." + '\n' +
    "Bei statisch unbestimmten Systemen können die überzähligen Stabkräfte " +
    "zugewiesen werden." + '\n' +
    "Das Programm bezweckt, die Anwendung des unteren (statischen) " +
    "Grenzwertsatzes der Plastizitätstheorie zu erleichtern." + '\n' + '\n' +
    '\n' +
    "VORSICHT:" + '\n' +
    "---------" + '\n' +
    "Das Programm könnte FEHLER enthalten. Sämtliche Resultate sind " +
    "SORGFÄLTIG auf ihre PLAUSIBILITÄT zu prüfen!" +'\n' +
    '\n' +'\n' +
    "HAFTUNGSAUSSCHLUSS:" +'\n' +
    "-------------------" + '\n' +
    "Dieses Programm ist freie Software. Sie können es unter den Bedingungen " +
    "der GNU General Public License, Version 2, wie von der Free Software " +
    "Foundation herausgegeben, weitergeben und/oder modifizieren. " + '\n' + 
    '\n' +
    "Die Veröffentlichung dieses Programms erfolgt in der Hoffnung, dass es " +
    "Ihnen von Nutzen sein wird, aber OHNE JEDE GEWÄHRLEISTUNG - sogar ohne " +
    "die implizite Gewährleistung der MARKTREIFE oder der EIGNUNG FÜR EINEN " +
    "BESTIMMTEN ZWECK.  Details finden Sie in der GNU General Public License." + '\n' +
    '\n' +
    "Sie sollten eine Kopie der GNU General Public License zusammen mit " +
    "diesem Programm erhalten haben. Falls nicht, schreiben Sie an die " + 
    "Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA." +
    '\n' + '\n' +
    '\n' +
    "Das Programm verwendet eine externe Mathematikbibliothek namens colt.jar. " +
    "Bitte deren (freie) Lizenz beachten.";
    
    private final String Lizenz_en =
    
    "Fachwerk3D calculates strut-and-tie models used by structural engineers for analysing " +
    "and designing reinforced concrete structures. The program only uses the equilibrium " +
    "conditions, thus it is not assuming elastic behaviour. If the system is statically " +
    "indeterminate, the redundant forces can be set." + '\n' +
    "This program aims to simplify the application of the theory of plasticity's " +
    "lower bound (static) theorem." + '\n' + '\n' +
    '\n' +
    "CAUTION:" +'\n' +
    "--------" +'\n' +
    "This program could contain ERRORS. All results must be verified " +
    "CAREFULLY in order to state that they are PLAUSIBLE!" + '\n' +
    '\n' + '\n' +
    "DISCLAIMER:" +'\n' +
    "-----------" + '\n' +
    "This program is free software; you can redistribute it and/or modify " +
    "it under the terms of the GNU General Public License version 2 as published by " +
    "the Free Software Foundation." +
    '\n' + '\n' +
    "This program is distributed in the hope that it will be useful, " +
    "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the " +
    "GNU General Public License for more details." +
    '\n' + '\n' +
    "You should have received a copy of the GNU General Public License " +
    "along with this program; if not, write to the Free Software " +
    "Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA" +
    '\n' + '\n' +
    '\n' +
    "This program uses an external library for mathematics called colt.jar. " +
    "Please pay attention to its (free) licence.";
    
    private String text_de;
    private String text_en;
    
    
    /** Creates new form clInfo3D */
    public clInfo3D(java.awt.Frame parent, String prog, int hauptver, int unterver, int jahr) {
        super(parent, "Info", true);
        
        PROGNAME = prog;
        HAUPTVER = hauptver;
        UNTERVER = unterver;
        JAHR = jahr;
        
        Copyright = Copyright1 + JAHR + Copyright2;
        
        text_de = "Fachwerk3D - rein statisches Fachwerkprogramm" + '\n' + "Version " + HAUPTVER + ".";
        if (UNTERVER < 10) text_de += "0";
        text_de += Integer.toString(UNTERVER) + '\n'+ '\n';
        text_de += Copyright;
        text_de += Lizenz_de;
        
        text_en = "Fachwerk3D - equilibrium condition solely" + '\n' + "Version " + HAUPTVER + ".";
        if (UNTERVER < 10) text_en += "0";
        text_en += Integer.toString(UNTERVER) + '\n'+ '\n';
        text_en += Copyright;
        text_en += Lizenz_en;
        
        initComponents();
        pack();
        zentriere(parent);
        show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane_de = new javax.swing.JScrollPane();
        feldText_de = new javax.swing.JTextArea();
        jScrollPane_en = new javax.swing.JScrollPane();
        feldText_en = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        knopfOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(530, 550));
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        feldText_de.setEditable(false);
        feldText_de.setFont(new java.awt.Font("Monospaced", 0, 12));
        feldText_de.setLineWrap(true);
        feldText_de.setTabSize(0);
        feldText_de.setText(text_de);
        feldText_de.setWrapStyleWord(true);
        feldText_de.setCaret(feldText_de.getCaret());
        feldText_de.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane_de.setViewportView(feldText_de);

        jTabbedPane1.addTab("deutsch", jScrollPane_de);

        feldText_en.setEditable(false);
        feldText_en.setFont(new java.awt.Font("Monospaced", 0, 12));
        feldText_en.setLineWrap(true);
        feldText_en.setTabSize(0);
        feldText_en.setText(text_en);
        feldText_en.setWrapStyleWord(true);
        feldText_en.setCaret(feldText_en.getCaret());
        feldText_en.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane_en.setViewportView(feldText_en);

        jTabbedPane1.addTab("english", jScrollPane_en);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        knopfOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fachwerk/gui/icons/button_ok.png")));
        knopfOK.setText("OK");
        knopfOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knopfOKActionPerformed(evt);
            }
        });

        jPanel2.add(knopfOK);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        repaint();
    }//GEN-LAST:event_jPanel1ComponentResized

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        repaint();
    }//GEN-LAST:event_formComponentResized

    private void knopfOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knopfOKActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_knopfOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    private void zentriere(java.awt.Frame hf) {
        java.awt.Point OL = hf.getLocationOnScreen();
        OL.translate(hf.getWidth()/2, hf.getHeight()/2);
        OL.translate(-this.getWidth()/2, -this.getHeight()/2);
        if (OL.getX() < 0) OL.setLocation(0, OL.getY());
        if (OL.getY() < 0) OL.setLocation(OL.getX(), 0);
        this.setLocation(OL);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea feldText_de;
    private javax.swing.JTextArea feldText_en;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane_de;
    private javax.swing.JScrollPane jScrollPane_en;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton knopfOK;
    // End of variables declaration//GEN-END:variables
    
}
