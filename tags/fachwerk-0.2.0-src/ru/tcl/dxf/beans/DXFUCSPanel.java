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

import ru.tcl.dxf.tables.UCS;
import ru.tcl.dxf.tables.UCSTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFUCSPanel extends JPanel implements Serializable, DXFDocumentListener{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFUCSPanel()
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

    public DXFUCSPanel(DXFDocument doc)
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

        table.setModel(new DXFUCSTableModel(document.getTables().getUCSTable()));
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


final class DXFUCSTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[4];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFUCSTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFUCSTablePanel.Table.Origin");
            columnNames[2] = res.getString("DXFUCSTablePanel.Table.XAxisDirection");
            columnNames[3] = res.getString("DXFUCSTablePanel.Table.YAxisDirection");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFUCSTableModel() {
        super();
    }

    public DXFUCSTableModel(UCSTable table) {
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
        UCS ucs = (UCS )data.elementAt(row);
        switch (col)
        {
            case 0: return ucs.getName();
            case 1: return ucs.getOrigin().coords3dToString();
            case 2: return ucs.getXAxisDirection().coords3dToString();
            case 3: return ucs.getYAxisDirection().coords3dToString();
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

