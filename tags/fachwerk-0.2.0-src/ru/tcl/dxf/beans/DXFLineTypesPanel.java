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

import ru.tcl.dxf.tables.LineType;
import ru.tcl.dxf.tables.LineTypeTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFLineTypesPanel extends JPanel implements Serializable, DXFDocumentListener{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFLineTypesPanel()
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

    public DXFLineTypesPanel(DXFDocument doc)
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

        table.setModel(new DXFLineTypesTableModel(document.getTables().getLineTypeTable()));
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


final class DXFLineTypesTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[3];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFLineTypesTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFLineTypesTablePanel.Table.Alignment");
            columnNames[2] = res.getString("DXFLineTypesTablePanel.Table.ASCIIType");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFLineTypesTableModel() {
        super();
    }

    public DXFLineTypesTableModel(LineTypeTable table) {
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
        LineType lineType = (LineType )data.elementAt(row);
        switch (col)
        {
            case 0: return lineType.getName();
            case 1: return new Integer(lineType.getAlignment());
            case 2: return lineType.getASCIIType();
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

