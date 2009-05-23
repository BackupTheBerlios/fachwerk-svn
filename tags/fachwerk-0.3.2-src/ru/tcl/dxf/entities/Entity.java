// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Entity.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf.entities;

import ru.tcl.dxf.DXFObject;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;
import ru.tcl.dxf.tables.LineType;
import ru.tcl.dxf.tables.Layer;


/**
   Класс <code>Entity</code> является базовым для всех
   примитивов.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public abstract class Entity extends DXFObject {
    private double elevation = 0;
    private double thickness = 0;
    private int color_number = 0;
    private static final int LAYER_NAME = 8;
    private static final int LINE_TYPE_NAME = 6;
    private static final int ELEVATION = 38;
    private static final int THICKNESS = 39;
    private static final int COLOR_NUMBER = 62;
    private static final int EXTRUSION_DIRECTION_X = 210;
    private static final int EXTRUSION_DIRECTION_Y = 220;
    private static final int EXTRUSION_DIRECTION_Z = 230;
    private LineType line_type = LineType.BYLAYER;
    private Layer layer = Layer.DEFAULT;
    private DXFPoint extrusion_direction = new DXFPoint ();

    public Entity ()
    {
        super();
    }

    public Entity (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает номер цвета (по умолчанию BYLAYER).
       @return номер цвета.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B1
     */
    public final int getColorNumber() {
        return color_number;
    }
    
    /**
       Возвращает уровень возвышения (по умолчанию = 0).
       @return уровень возвышения.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B2
     */
    public final double getElevation() {
        return elevation;
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public abstract String getEntityName();

    /**
       Возвращает направление экструзии.
       @return направление экструзии.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B3
     */
    public final DXFPoint getExtrusionDirection() {
        return extrusion_direction;
    }

    /**
       Возвращает имя слоя.
       @return имя слоя.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B4
     */
    public final Layer getLayer() {
        return layer;
    }

    /**
       Возвращает имя типа линии (по умоляанию BYLAYER).
       @return имя типа линии.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B5
     */
    public final LineType getLineType() {
        return line_type;
    }

    /**
       Возвращает высоту (толщину) (по умолчанию = 0).
       @return высота (толщина).
       @since TCL DXF 1.0
       @roseuid 37132F5C01B6
     */
    public final double getThickness() {
        return thickness;
    }
    
    /**
       Считывает общее свойство примитивов. Возвращает true,
       если считывание прошло успешно.
       @return true, если считывание прошло успешно.
       @since TCL DXF 1.0
       @roseuid 37132F5C01B7
     */
    protected boolean readCommonPropertiesDXF(DXFTokenizer tokenizer) {
        switch (tokenizer.getCode())
        {
            case LAYER_NAME:
            {
                layer = getDocument().getTables().getLayerTable().find(tokenizer.getCommand());
                return true;
            }
            case LINE_TYPE_NAME:
            {
                line_type = getDocument().getTables().getLineTypeTable().find(tokenizer.getCommand());
                return true;
            }
            case ELEVATION:
            {
                elevation = tokenizer.getCommandAsDouble();
                return true;
            }
            case THICKNESS:
            {
                thickness = tokenizer.getCommandAsDouble();
                return true;
            }
            case COLOR_NUMBER:
            {
                color_number = tokenizer.getCommandAsInt();
                return true;
            }
            case EXTRUSION_DIRECTION_X:
            {
                extrusion_direction.setX( tokenizer.getCommandAsDouble() );
                return true;
            }
            case EXTRUSION_DIRECTION_Y:
            {
                extrusion_direction.setY( tokenizer.getCommandAsDouble() );
                return true;
            }
            case EXTRUSION_DIRECTION_Z:
            {
                extrusion_direction.setZ( tokenizer.getCommandAsDouble() );
                return true;
            }
        }

        return false;
    }
}
