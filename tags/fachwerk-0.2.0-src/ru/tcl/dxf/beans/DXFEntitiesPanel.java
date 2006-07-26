package ru.tcl.dxf.beans;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.entities.Entity;
import ru.tcl.dxf.entities.Entities;
import ru.tcl.dxf.beans.events.*;
import java.io.*;


public class DXFEntitiesPanel extends JPanel implements DXFDocumentListener, Serializable{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document = null;

    public DXFEntitiesPanel()
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

    public DXFEntitiesPanel(DXFDocument doc)
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

        table.setModel(new DXFEntitiesModel(document.getEntities()));
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


final class DXFEntitiesModel extends AbstractTableModel
{
    private static String[] columnNames = new String[7];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFEntitiesPanel.Table.EntityName");
            columnNames[1] = res.getString("DXFEntitiesPanel.Table.Color");
            columnNames[2] = res.getString("DXFEntitiesPanel.Table.Elevation");
            columnNames[3] = res.getString("DXFEntitiesPanel.Table.Thickness");
            columnNames[4] = res.getString("DXFEntitiesPanel.Table.ExtrusionDirection");
            columnNames[5] = res.getString("DXFEntitiesPanel.Table.Layer");
            columnNames[6] = res.getString("DXFEntitiesPanel.Table.LineType");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFEntitiesModel() {
        super();
    }

    public DXFEntitiesModel(Entities entities) {
        this();

        for (Enumeration e = entities.items(); e.hasMoreElements() ;)
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
        Entity entity = (Entity )data.elementAt(row);
        switch (col)
        {
            case 0: return entity.getEntityName();
            case 1: return new Integer(entity.getColorNumber());
            case 2: return new Double(entity.getElevation());
            case 3: return new Double(entity.getThickness());
            case 4: return entity.getExtrusionDirection().coords3dToString();
            case 5: return entity.getLayer().getName();
            case 6: return entity.getLineType().getName();
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

