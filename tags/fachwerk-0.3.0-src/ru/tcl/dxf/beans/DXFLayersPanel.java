package ru.tcl.dxf.beans;

import java.awt.*;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ru.tcl.dxf.tables.Layer;
import ru.tcl.dxf.tables.LayerTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFLayersPanel extends JPanel implements Serializable, DXFDocumentListener{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFLayersPanel()
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

    public DXFLayersPanel(DXFDocument doc)
    {
        this();
        setDocument(doc);
    }

    private void jbInit() throws Exception
    {
        this.setLayout(borderLayout);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.add(scrollpane, BorderLayout.CENTER);
    }

    public void setDocument(DXFDocument newDocument)
    {
        document = newDocument;

        if (newDocument instanceof DXFComponent)
            ((DXFComponent )newDocument).addDXFDocumentListener(this);

        table.setModel(new DXFLayersTableModel(document.getTables().getLayerTable()));
    }

    public DXFDocument getDocument()
    {
        return document;
    }

    public void documentChanged(DXFDocumentEvent e)
    {
        setDocument(e.getDocument());
    }
}


final class DXFLayersTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[5];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFLayersTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFLayersTablePanel.Table.Color");
            columnNames[2] = res.getString("DXFLayersTablePanel.Table.LineType");
            columnNames[3] = res.getString("DXFLayersTablePanel.Table.Frozen");
            columnNames[4] = res.getString("DXFLayersTablePanel.Table.Off");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFLayersTableModel() {
        super();
    }

    public DXFLayersTableModel(LayerTable table) {
        this();

        for (Enumeration e = table.items(); e.hasMoreElements() ;)
            data.addElement(e.nextElement());
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Layer layer = (Layer )data.elementAt(row);
        switch (col)
        {
            case 0: return layer.getName();
            case 1: return new Integer(layer.getColor());
            case 2: return layer.getLineType().getName();
            case 3: return new Boolean(layer.isFrozen());
            case 4: return new Boolean(layer.isOff());
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

