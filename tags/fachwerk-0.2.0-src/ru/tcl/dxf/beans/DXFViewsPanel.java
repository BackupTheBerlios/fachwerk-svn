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

import ru.tcl.dxf.tables.View;
import ru.tcl.dxf.tables.ViewTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFViewsPanel extends JPanel implements Serializable, DXFDocumentListener
{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFViewsPanel()
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

    public DXFViewsPanel(DXFDocument doc)
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

        table.setModel(new DXFViewsTableModel(document.getTables().getViewTable()));
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


final class DXFViewsTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[16];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFViewsTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFViewsTablePanel.Table.Center");
            columnNames[2] = res.getString("DXFViewsTablePanel.Table.Direction");
            columnNames[3] = res.getString("DXFViewsTablePanel.Table.Target");
            columnNames[4] = res.getString("DXFViewsTablePanel.Table.Height");
            columnNames[5] = res.getString("DXFViewsTablePanel.Table.Width");
            columnNames[6] = res.getString("DXFViewsTablePanel.Table.LensLength");
            columnNames[7] = res.getString("DXFViewsTablePanel.Table.FrontClipping");
            columnNames[8] = res.getString("DXFViewsTablePanel.Table.BackClipping");
            columnNames[9] = res.getString("DXFViewsTablePanel.Table.TwistAngle");
            columnNames[10] = res.getString("DXFViewsTablePanel.Table.BackClipping");
            columnNames[11] = res.getString("DXFViewsTablePanel.Table.Disabled");
            columnNames[12] = res.getString("DXFViewsTablePanel.Table.FrontClippingOn");
            columnNames[13] = res.getString("DXFViewsTablePanel.Table.FrontClipNotAtEye");
            columnNames[14] = res.getString("DXFViewsTablePanel.Table.PerspectiveViewActive");
            columnNames[15] = res.getString("DXFViewsTablePanel.Table.UCSOn");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFViewsTableModel() {
        super();
    }

    public DXFViewsTableModel(ViewTable table) {
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
        View view = (View )data.elementAt(row);
        switch (col)
        {
            case 0: return view.getName();
            case 1: return view.getCenter().coords3dToString();
            case 2: return view.getDirection().coords3dToString();
            case 3: return view.getTarget().coords3dToString();
            case 4: return new Double(view.getHeight());
            case 5: return new Double(view.getWidth());
            case 6: return new Double(view.getLensLenght());
            case 7: return new Double(view.getFrontClipping());
            case 8: return new Double(view.getBackClipping());
            case 9: return new Double(view.getTwistAngle());
            case 10: return new Boolean(view.isBackClippingOn());
            case 11: return new Boolean(view.isDisabled());
            case 12: return new Boolean(view.isFrontClippingOn());
            case 13: return new Boolean(view.isFrontClipNotAtEye());
            case 14: return new Boolean(view.isPerspectiveViewActive());
            case 15: return new Boolean(view.isUCSOn());
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

