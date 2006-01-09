package ru.tcl.dxf.beans.events;

import java.util.EventListener;

public interface DXFDocumentListener extends EventListener
{
    public void documentChanged(DXFDocumentEvent e);
}
