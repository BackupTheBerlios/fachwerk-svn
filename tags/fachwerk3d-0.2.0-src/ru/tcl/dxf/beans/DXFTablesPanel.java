package ru.tcl.dxf.beans;

import java.awt.*;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ru.tcl.dxf.DXFDocument;


public class DXFTablesPanel extends JPanel implements Serializable{
    private static ResourceBundle res;

    static
    {
        try
        {
            res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans",
                                           Locale.getDefault());
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    BorderLayout borderLayout = new BorderLayout();
    JTabbedPane jTabbedPane1 = new JTabbedPane();
    JPanel layersPanel = new JPanel();
    JPanel lineTypesPanel = new JPanel();
    JPanel stylesPanel = new JPanel();
    JPanel ucsPanel = new JPanel();
    JPanel viewsPanel = new JPanel();
    JPanel vportsPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    DXFLayersPanel dxfLayersPanel = new DXFLayersPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    DXFLineTypesPanel dxfLineTypesPanel = new DXFLineTypesPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    DXFStylesPanel dxfStylesPanel = new DXFStylesPanel();
    BorderLayout borderLayout4 = new BorderLayout();
    DXFUCSPanel dxfUCSPanel = new DXFUCSPanel();
    BorderLayout borderLayout5 = new BorderLayout();
    DXFViewsPanel dxfViewsPanel = new DXFViewsPanel();
    BorderLayout borderLayout6 = new BorderLayout();
    DXFVportsPanel dxfVportsPanel = new DXFVportsPanel();

    public DXFTablesPanel()
    {
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setLayout(borderLayout);
        layersPanel.setLayout(borderLayout1);
        lineTypesPanel.setLayout(borderLayout2);
        stylesPanel.setLayout(borderLayout3);
        ucsPanel.setLayout(borderLayout4);
        viewsPanel.setLayout(borderLayout5);
        vportsPanel.setLayout(borderLayout6);
        this.add(jTabbedPane1, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.Layers"), layersPanel);
        layersPanel.add(dxfLayersPanel, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.LineTypes"), lineTypesPanel);
        lineTypesPanel.add(dxfLineTypesPanel, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.Styles"), stylesPanel);
        stylesPanel.add(dxfStylesPanel, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.UCS"), ucsPanel);
        ucsPanel.add(dxfUCSPanel, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.Views"), viewsPanel);
        viewsPanel.add(dxfViewsPanel, BorderLayout.CENTER);
        jTabbedPane1.addTab(res.getString("DXFTablesPanel.VPorts"), vportsPanel);
        vportsPanel.add(dxfVportsPanel, BorderLayout.CENTER);
    }

    public DXFDocument getDocument()
    {
        return dxfLayersPanel.getDocument();
    }

    public void setDocument(DXFDocument newDocument)
    {
        dxfLayersPanel.setDocument(newDocument);
        dxfLineTypesPanel.setDocument(newDocument);
        dxfStylesPanel.setDocument(newDocument);
        dxfUCSPanel.setDocument(newDocument);
        dxfViewsPanel.setDocument(newDocument);
        dxfVportsPanel.setDocument(newDocument);
    }
}

