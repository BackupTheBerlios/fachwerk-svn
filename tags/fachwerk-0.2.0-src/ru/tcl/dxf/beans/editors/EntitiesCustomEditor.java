package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;

import ru.tcl.dxf.entities.Entities;
import ru.tcl.dxf.beans.DXFEntitiesPanel;


public class EntitiesCustomEditor extends Panel
{
    private PropertyEditor editor;
    DXFEntitiesPanel dXFEntitiesPanel1 = new DXFEntitiesPanel();
    BorderLayout borderLayout1 = new BorderLayout();

    public EntitiesCustomEditor()
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

    public EntitiesCustomEditor(PropertyEditor editor)
    {
        this();
        this.editor = editor;
        dXFEntitiesPanel1.setDocument(((Entities )editor.getValue()).getDocument());
    }

    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(500, 300));
        this.setLayout(borderLayout1);
        this.add(dXFEntitiesPanel1, BorderLayout.CENTER);
    }
}

