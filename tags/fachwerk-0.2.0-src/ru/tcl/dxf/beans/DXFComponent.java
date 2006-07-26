package ru.tcl.dxf.beans;

import java.io.Serializable;
import java.util.Vector;

import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.header.Header;
import ru.tcl.dxf.tables.Tables;
import ru.tcl.dxf.blocks.Blocks;
import ru.tcl.dxf.entities.Entities;
import ru.tcl.dxf.beans.events.DXFDocumentEvent;
import ru.tcl.dxf.beans.events.DXFDocumentListener;

public class DXFComponent extends DXFDocument implements Serializable{
    private String fileName = new String();
    private transient Vector DXFDocumentListeners;

    public DXFComponent()
    {
    }

    public Blocks getBlocks()
    {
        return super.getBlocks();
    }

    public Entities getEntities()
    {
        return super.getEntities();
    }

    public String getFileName()
    {
        return fileName;
    }

    public Header getHeader()
    {
        return super.getHeader();
    }

    public Tables getTables()
    {
        return super.getTables();
    }

    public final void setBlocks(Blocks value)
    {
    }

    public final void setEntities(Entities value)
    {
    }

    public void setFileName(String newFileName)
    {
        try
        {
            readDXF(newFileName);
            fileName = newFileName;
            fireDocumentChanged(new DXFDocumentEvent(this));
        }
        catch (Exception e)
        {
            System.err.println("Can't read file");
        }
    }

    public final void setHeader(Header value)
    {
    }

    public final void setTables(Tables value)
    {
    }

    public synchronized void removeDXFDocumentListener(DXFDocumentListener l)
    {
        if (DXFDocumentListeners != null && DXFDocumentListeners.contains(l))
        {
            Vector v = (Vector) DXFDocumentListeners.clone();
            v.removeElement(l);
            DXFDocumentListeners = v;
        }
    }

    public synchronized void addDXFDocumentListener(DXFDocumentListener l)
    {
        Vector v = DXFDocumentListeners == null ? new Vector(2) : (Vector) DXFDocumentListeners.clone();
        if (!v.contains(l))
        {
            v.addElement(l);
            DXFDocumentListeners = v;
        }
    }

    protected void fireDocumentChanged(DXFDocumentEvent e)
    {
        if (DXFDocumentListeners != null)
        {
            Vector listeners = DXFDocumentListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
              ((DXFDocumentListener) listeners.elementAt(i)).documentChanged(e);
        }
    }
}
