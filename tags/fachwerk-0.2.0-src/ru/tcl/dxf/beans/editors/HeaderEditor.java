package ru.tcl.dxf.beans.editors;

import java.awt.*;
import java.beans.*;

public class HeaderEditor extends PropertyEditorSupport
{
    private HeaderCustomEditor editor;

    public HeaderEditor()
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
            editor = new HeaderCustomEditor(this);
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

 