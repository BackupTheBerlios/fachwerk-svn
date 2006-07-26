package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;

public class BlocksEditor extends PropertyEditorSupport
{
    private BlocksCustomEditor editor;

    public BlocksEditor()
    {
    }

    public boolean supportsCustomEditor()
    {
        return true;
    }

    public Component getCustomEditor()
    {
        if (editor == null)
        {
            editor = new BlocksCustomEditor(this);
        }
        return editor;
    }

    public String getJavaInitializationString()
    {
        return "Not implemented";
    }

    public boolean isPaintable()
    {
        return true;
    }

    public void paintValue(Graphics g, Rectangle r)
    {
    }
}

 