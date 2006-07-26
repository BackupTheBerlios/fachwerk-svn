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

import ru.tcl.dxf.tables.Style;
import ru.tcl.dxf.tables.StyleTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFStylesPanel extends JPanel implements Serializable, DXFDocumentListener{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFStylesPanel()
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

    public DXFStylesPanel(DXFDocument doc)
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

        table.setModel(new DXFStylesTableModel(document.getTables().getStyleTable()));
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


final class DXFStylesTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[11];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFStylesTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFStylesTablePanel.Table.FixedHeight");
            columnNames[2] = res.getString("DXFStylesTablePanel.Table.WidthFactor");
            columnNames[3] = res.getString("DXFStylesTablePanel.Table.ObliquingAngle");
            columnNames[4] = res.getString("DXFStylesTablePanel.Table.Backwards");
            columnNames[5] = res.getString("DXFStylesTablePanel.Table.ShapeTable");
            columnNames[6] = res.getString("DXFStylesTablePanel.Table.UpsideDown");
            columnNames[7] = res.getString("DXFStylesTablePanel.Table.VerticallyText");
            columnNames[8] = res.getString("DXFStylesTablePanel.Table.LastHeightUsed");
            columnNames[9] = res.getString("DXFStylesTablePanel.Table.PrimaryFileName");
            columnNames[10] = res.getString("DXFStylesTablePanel.Table.BigfontFileName");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFStylesTableModel() {
        super();
    }

    public DXFStylesTableModel(StyleTable table) {
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
        Style style = (Style )data.elementAt(row);
        switch (col)
        {
            case 0: return style.getName();
            case 1: return new Double(style.getFixedHeight());
            case 2: return new Double(style.getWidthFactor());
            case 3: return new Double(style.getObliquingAngle());
            case 4: return new Boolean(style.isBackwards());
            case 5: return new Boolean(style.isShapeTable());
            case 6: return new Boolean(style.isUpsideDown());
            case 7: return new Boolean(style.isVerticallyText());
            case 8: return new Double(style.getLastHeightUsed());
            case 9: return style.getPrimaryFileName();
            case 10: return style.getBigfontFileName();
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
