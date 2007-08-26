package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;
import java.io.*;

import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.blocks.Blocks;
import ru.tcl.dxf.beans.DXFBlocksPanel;


public class BlocksCustomEditor extends Panel
{
    private PropertyEditor editor;
    DXFBlocksPanel dXFBlocksPanel1 = new DXFBlocksPanel();
    BorderLayout borderLayout1 = new BorderLayout();

    public BlocksCustomEditor()
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

    public BlocksCustomEditor(PropertyEditor editor)
    {
        this();
        this.editor = editor;
        dXFBlocksPanel1.setDocument(((Blocks )editor.getValue()).getDocument());
    }

    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(500, 300));
        this.setLayout(borderLayout1);
        this.add(dXFBlocksPanel1, BorderLayout.CENTER);
    }
}

