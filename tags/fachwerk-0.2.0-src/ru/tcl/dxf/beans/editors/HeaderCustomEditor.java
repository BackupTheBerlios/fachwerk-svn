package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;

import ru.tcl.dxf.header.Header;
import ru.tcl.dxf.beans.DXFHeaderPanel;


public class HeaderCustomEditor extends Panel
{
    private PropertyEditor editor;
    DXFHeaderPanel dXFHeaderPanel1 = new DXFHeaderPanel();
    BorderLayout borderLayout1 = new BorderLayout();

    public HeaderCustomEditor()
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

    public HeaderCustomEditor(PropertyEditor editor)
    {
        this();
        this.editor = editor;
        dXFHeaderPanel1.setDocument(((Header )editor.getValue()).getDocument());
    }

    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(500, 300));
        this.setLayout(borderLayout1);
        this.add(dXFHeaderPanel1, BorderLayout.CENTER);
    }
}

