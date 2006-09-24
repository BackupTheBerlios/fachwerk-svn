package ru.tcl.dxf.beans.events;

import java.util.EventObject;
import ru.tcl.dxf.beans.DXFComponent;

public class DXFDocumentEvent extends EventObject
{
    public DXFDocumentEvent(Object source)
    {
        super(source);
    }

    public DXFComponent getDocument()
    {
        return (DXFComponent )source;
    }
}
