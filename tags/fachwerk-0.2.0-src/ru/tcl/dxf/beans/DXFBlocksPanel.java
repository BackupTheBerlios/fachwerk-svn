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

import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.blocks.Block;
import ru.tcl.dxf.blocks.Blocks;
import ru.tcl.dxf.beans.events.*;


public class DXFBlocksPanel extends JPanel implements DXFDocumentListener, Serializable{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document = null;

    public DXFBlocksPanel()
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

    public DXFBlocksPanel(DXFDocument doc)
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

        table.setModel(new DXFBlocksModel(newDocument.getBlocks()));
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


final class DXFBlocksModel extends AbstractTableModel
{
    private static String[] columnNames = new String[4];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFBlocksPanel.Table.Name");
            columnNames[1] = res.getString("DXFBlocksPanel.Table.Base");
            columnNames[2] = res.getString("DXFBlocksPanel.Table.Anonymous");
            columnNames[3] = res.getString("DXFBlocksPanel.Table.Attributes");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Vector data = new Vector();

    private DXFBlocksModel() {
        super();
    }

    public DXFBlocksModel(Blocks blocks) {
        this();

        for (Enumeration e = blocks.items(); e.hasMoreElements() ;)
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
        Block block = (Block )data.elementAt(row);
        switch (col)
        {
            case 0: return block.getName();
            case 1: return block.getBase().coords2dToString();
            case 2: return new Boolean(block.isAnonymous());
            case 3: return new Boolean(block.isHaveAttributes());
            default: throw new InternalError();
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

