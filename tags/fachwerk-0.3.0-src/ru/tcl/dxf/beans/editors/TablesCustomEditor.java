package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;

import ru.tcl.dxf.tables.Tables;
import ru.tcl.dxf.beans.DXFTablesPanel;


public class TablesCustomEditor extends Panel
{
    private PropertyEditor editor;
    ru.tcl.dxf.beans.DXFTablesPanel dXFTablesPanel1 = new ru.tcl.dxf.beans.DXFTablesPanel();
    BorderLayout borderLayout1 = new BorderLayout();

    public TablesCustomEditor()
    {
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TablesCustomEditor(PropertyEditor editor)
    {
        this();
        this.editor = editor;
        dXFTablesPanel1.setDocument(((Tables )editor.getValue()).getDocument());
    }

    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(500, 300));
        this.setLayout(borderLayout1);
        this.add(dXFTablesPanel1, BorderLayout.CENTER);
    }
}

