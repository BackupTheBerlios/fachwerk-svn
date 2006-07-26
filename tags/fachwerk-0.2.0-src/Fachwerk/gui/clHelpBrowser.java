/*
 * clHelpBrowser.java
 *
 * Created on 23. November 2003, 00:43
 */

package Fachwerk.gui;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.event.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events

import java.net.URL;
import java.io.IOException;
import java.util.*;

/**
 * Fachwerk - treillis
 *
 * Copyright (c) 2003 - 2005 A.Vontobel <qwert2003@users.sourceforge.net>
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
 *
 *
 * @author  av
 */
public class clHelpBrowser extends javax.swing.JFrame {

    private Locale locale;    
    private JEditorPane editorPane;
    private URL helpURL; // Startseite der jeweiligen Sprache
    private URL vorherigeSeite;
    private String spracheinstellungsdatei;
    
    // zu Testzwecken:
    private static boolean direktaufruf = false;
    
    /** Creates new help browser */
    public clHelpBrowser(Locale locale, String spracheinstellungsdatei) {
        super("Fachwerk --- Hilfe / aide / aiuto / help"); // " + '\u043F'+'\u043E'+'\u043C'+'\u043E'+'\u0449'+'\u044C');

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
                }
        });
        //locale = Locale.getDefault();
        this.locale = locale;
        this.spracheinstellungsdatei = spracheinstellungsdatei;
                
        //Create an editor pane.
        JEditorPane editorPane = createEditorPane();
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(800, 600));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        
        getContentPane().add(editorScrollPane, java.awt.BorderLayout.CENTER);
        tastenbelegen();
        pack();
        show();
    }    
    
    private JEditorPane createEditorPane() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                editorpaneHyperlinkUpdate(evt);
            }
        });
        editorPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editorPaneMouseClicked(evt);
            }
        });
        
        ResourceBundle lset = ResourceBundle.getBundle(spracheinstellungsdatei, locale);
        String startseite = lset.getString("startpage");
        //System.out.println("classpath = " + System.getProperty("java.class.path"));
        //System.out.println("Startpage = " + startseite); 
        helpURL = clHelpBrowser.class.getResource(startseite);
        vorherigeSeite = helpURL; // Initialisierung
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: " + startseite);
        }

        return editorPane;
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        if (direktaufruf) System.exit(0);
        else dispose();
    }

    /** zu Testzwecken
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        direktaufruf = true;
        Locale loc;
        if (args.length == 0) loc = Locale.getDefault();
        else loc = new Locale(args[0]);        
        new clHelpBrowser(loc, "Fachwerk/locales/LangSetting").show();
    }
    
    public void editorpaneHyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();
            
            URL nochaktuelleSeite = editorPane.getPage();
            try {                 
                pane.setPage(e.getURL());                                
            } 
            catch (IOException exept) {
                System.err.println("Attempted to read a bad URL: " + e.getURL());
                try {pane.setPage(nochaktuelleSeite);}                 
                catch (IOException exepthoffnungslos) {
                    System.err.println("Fallback URL failed: " + nochaktuelleSeite);
                }
            }
            vorherigeSeite = nochaktuelleSeite;                 
         }
     }
     
     private void editorPaneMouseClicked(java.awt.event.MouseEvent evt) {        
         if (evt.getButton() == evt.BUTTON2 || // mittlere Maustaste gedrückt oder
             (evt.getButton() == evt.BUTTON3 && evt.getClickCount() > 1)) { // Doppelklick rechte Maustaste             
             URL nochaktuelleSeite = editorPane.getPage();
             try {                 
                 editorPane.setPage(helpURL);                
             } 
             catch (IOException exept) {
                 System.err.println("Attempted to read a bad URL: " + helpURL);
             }
             vorherigeSeite = nochaktuelleSeite;
         }
         
         if (evt.getButton() == evt.BUTTON3 && evt.getClickCount() == 1) { // rechte Maustaste gedrückt             
             URL nochaktuelleSeite = editorPane.getPage();
             try {                 
                 editorPane.setPage(vorherigeSeite);                
             } 
             catch (IOException exept) {
                 System.err.println("Attempted to read a bad URL: " + vorherigeSeite);
             }
             vorherigeSeite = nochaktuelleSeite;
         }      
     }
     
     private void tastenbelegen() {                
        javax.swing.ActionMap am = getRootPane().getActionMap();
        javax.swing.InputMap im = getRootPane().getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        // ESC - Taste
        Object EscapeObjekt = new Object();
        javax.swing.KeyStroke EscapeStroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        javax.swing.Action EscapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (direktaufruf) System.exit(0);   // Befehl
                else dispose();                     //   "
            }
        };
        im.put(EscapeStroke, EscapeObjekt);
        am.put(EscapeObjekt, EscapeAction);
        
        // F1 - Taste
        Object F1Objekt = new Object();
        javax.swing.KeyStroke F1Stroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0);
        javax.swing.Action F1Action = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (direktaufruf) System.exit(0);   // Befehl
                else dispose();                     //   "
            }
        };
        im.put(F1Stroke, F1Objekt);
        am.put(F1Objekt, F1Action);
                
    }
    
    
    
    
}
