
//Title:        
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       
//Company:      
//Description:  

package ru.tcl.dxf.beans;

import java.beans.*;

public class DXFComponentBeanInfo extends SimpleBeanInfo
{
    Class beanClass = DXFComponent.class;
    String iconColor16x16Filename;
    String iconColor32x32Filename;
    String iconMono16x16Filename;
    String iconMono32x32Filename;

    
    public DXFComponentBeanInfo()
    {
    }

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try 
        {
            PropertyDescriptor _blocks = new PropertyDescriptor("blocks", beanClass, "getBlocks", "setBlocks");
            _blocks.setPropertyEditorClass(ru.tcl.dxf.beans.editors.BlocksEditor.class);
            
            PropertyDescriptor _entities = new PropertyDescriptor("entities", beanClass, "getEntities", "setEntities");
            _entities.setPropertyEditorClass(ru.tcl.dxf.beans.editors.EntitiesEditor.class);
            
            PropertyDescriptor _fileName = new PropertyDescriptor("fileName", beanClass, "getFileName", "setFileName");
            
            PropertyDescriptor _header = new PropertyDescriptor("header", beanClass, "getHeader", "setHeader");
            _header.setPropertyEditorClass(ru.tcl.dxf.beans.editors.HeaderEditor.class);
            
            PropertyDescriptor _tables = new PropertyDescriptor("tables", beanClass, "getTables", "setTables");
            _tables.setPropertyEditorClass(ru.tcl.dxf.beans.editors.TablesEditor.class);
            
            PropertyDescriptor[] pds = new PropertyDescriptor[] {
              _blocks,
              _entities,
              _fileName,
              _header,
              _tables,
            };
            return pds;
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind)
    {
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
          return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
        case BeanInfo.ICON_COLOR_32x32:
          return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
        case BeanInfo.ICON_MONO_16x16:
          return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
        case BeanInfo.ICON_MONO_32x32:
          return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
        }
        return null;
    }
}




















