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

import ru.tcl.dxf.tables.Vport;
import ru.tcl.dxf.tables.VportTable;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.beans.events.*;


public class DXFVportsPanel extends JPanel implements Serializable, DXFDocumentListener
{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document;

    public DXFVportsPanel()
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

    public DXFVportsPanel(DXFDocument doc)
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

        table.setModel(new DXFVportsTableModel(document.getTables().getVportTable()));
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


final class DXFVportsTableModel extends AbstractTableModel
{
    private static String[] columnNames = new String[30];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFVportsTablePanel.Table.Name");
            columnNames[1] = res.getString("DXFVportsTablePanel.Table.SnapSpacingX");
            columnNames[2] = res.getString("DXFVportsTablePanel.Table.SnapSpacingY");
            columnNames[3] = res.getString("DXFVportsTablePanel.Table.GridSpacingX");
            columnNames[4] = res.getString("DXFVportsTablePanel.Table.GridSpacingY");
            columnNames[5] = res.getString("DXFVportsTablePanel.Table.ViewHeight");
            columnNames[6] = res.getString("DXFVportsTablePanel.Table.AspectRatio");
            columnNames[7] = res.getString("DXFVportsTablePanel.Table.LensLength");
            columnNames[8] = res.getString("DXFVportsTablePanel.Table.FrontClipping");
            columnNames[9] = res.getString("DXFVportsTablePanel.Table.BackClipping");
            columnNames[10] = res.getString("DXFVportsTablePanel.Table.SnapRotationAngle");
            columnNames[11] = res.getString("DXFVportsTablePanel.Table.ViewTwistAngle");
            columnNames[12] = res.getString("DXFVportsTablePanel.Table.CircleZoomPercent");
            columnNames[13] = res.getString("DXFVportsTablePanel.Table.SnapStyle");
            columnNames[14] = res.getString("DXFVportsTablePanel.Table.SnapIsopair");
            columnNames[15] = res.getString("DXFVportsTablePanel.Table.LeftCorner");
            columnNames[16] = res.getString("DXFVportsTablePanel.Table.RightCorner");
            columnNames[17] = res.getString("DXFVportsTablePanel.Table.ViewCenter");
            columnNames[18] = res.getString("DXFVportsTablePanel.Table.SnapBase");
            columnNames[19] = res.getString("DXFVportsTablePanel.Table.ViewDirection");
            columnNames[20] = res.getString("DXFVportsTablePanel.Table.ViewTarget");
            columnNames[21] = res.getString("DXFVportsTablePanel.Table.BackClippingOn");
            columnNames[22] = res.getString("DXFVportsTablePanel.Table.Disabled");
            columnNames[23] = res.getString("DXFVportsTablePanel.Table.FastZoomOn");
            columnNames[24] = res.getString("DXFVportsTablePanel.Table.FrontClippingOn");
            columnNames[25] = res.getString("DXFVportsTablePanel.Table.FrontClipNotAtEye");
            columnNames[26] = res.getString("DXFVportsTablePanel.Table.GridModeOn");
            columnNames[27] = res.getString("DXFVportsTablePanel.Table.PerspectiveViewActive");
            columnNames[28] = res.getString("DXFVportsTablePanel.Table.SnapModeOn");
            columnNames[29] = res.getString("DXFVportsTablePanel.Table.UCSOn");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFVportsTableModel() {
        super();
    }

    public DXFVportsTableModel(VportTable table) {
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
        Vport vport = (Vport )data.elementAt(row);
        switch (col)
        {
            case 0: return vport.getName();
            case 1: return new Double(vport.getSnapSpacingX());
            case 2: return new Double(vport.getSnapSpacingY());
            case 3: return new Double(vport.getGridSpacingX());
            case 4: return new Double(vport.getGridSpacingY());
            case 5: return new Double(vport.getViewHeight());
            case 6: return new Double(vport.getAspectRatio());
            case 7: return new Double(vport.getLensLenght());
            case 8: return new Double(vport.getFrontClipping());
            case 9: return new Double(vport.getBackClipping());
            case 10: return new Double(vport.getSnapRotationAngle());
            case 11: return new Double(vport.getViewTwistAngle());
            case 12: return new Integer(vport.getCircleZoomPercent());
            case 13: return new Integer(vport.getSnapStyle());
            case 14: return new Integer(vport.getSnapIsopair());
            case 15: return vport.getLeftCorner().coords3dToString();
            case 16: return vport.getRightCorner().coords3dToString();
            case 17: return vport.getViewCenter().coords3dToString();
            case 18: return vport.getSnapBase().coords3dToString();
            case 19: return vport.getViewDirection().coords3dToString();
            case 20: return vport.getViewTarget().coords3dToString();
            case 21: return new Boolean(vport.isBackClippingOn());
            case 22: return new Boolean(vport.isDisabled());
            case 23: return new Boolean(vport.isFastZoomOn());
            case 24: return new Boolean(vport.isFrontClippingOn());
            case 25: return new Boolean(vport.isFrontClipNotAtEye());
            case 26: return new Boolean(vport.isGridModeOn());
            case 27: return new Boolean(vport.isPerspectiveViewActive());
            case 28: return new Boolean(vport.isSnapModeOn());
            case 29: return new Boolean(vport.isUCSOn());
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

