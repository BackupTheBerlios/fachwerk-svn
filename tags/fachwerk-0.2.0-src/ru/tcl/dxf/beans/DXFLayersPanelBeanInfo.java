
//Title:        
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       
//Company:      
//Description:  

package ru.tcl.dxf.beans;

import java.beans.*;

public class DXFLayersPanelBeanInfo extends SimpleBeanInfo
{
    Class beanClass = DXFLayersPanel.class;
    String iconColor16x16Filename;
    String iconColor32x32Filename;
    String iconMono16x16Filename;
    String iconMono32x32Filename;

    
    public DXFLayersPanelBeanInfo()
    {
    }

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try 
        {
            PropertyDescriptor _document = new PropertyDescriptor("document", beanClass, "getDocument", "setDocument");
            
            PropertyDescriptor[] pds = new PropertyDescriptor[] {
              _document,
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

    public BeanInfo[] getAdditionalBeanInfo()
    {
        Class superclass = beanClass.getSuperclass();
        try 
        {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}


 