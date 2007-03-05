// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Vertex.java

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

import java.io.Serializable;
import java.io.IOException;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;


/**
   Класс <code>Vertex</code> представляет вершины полилинии.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Vertex extends Entity {
    private double start_width = 0;
    private double end_width = 0;
    private double bulge = 0;
    private int flags = 0;
    private double tangent_direction = 0;
    private static final int LOCATION_X = 10;
    private static final int LOCATION_Y = 20;
    private static final int LOCATION_Z = 30;
    private static final int START_WIDTH = 40;
    private static final int END_WIDTH = 41;
    private static final int BULGE = 42;
    private static final int FLAGS = 70;
    private static final int TANGENT_DIRECTION = 50;
    private DXFPoint location = new DXFPoint ();

    public Vertex ()
    {
        super();
    }

    public Vertex (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает выпуклость.
       @return выпуклость.
       @since TCL DXF 1.0
       @roseuid 37132F61021F
     */
    public final double getBulge() {
        return bulge;
    }

    /**
       Возвращает конечную ширину.
       @return конечная ширина.
       @since TCL DXF 1.0
       @roseuid 37132F610220
     */
    public final double getEndWidth() {
        return end_width;
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Vertex";
    }

    /**
       Возвращает координаты вершины.
       @return координаты вершины.
       @since TCL DXF 1.0
       @roseuid 37132F610221
     */
    public final DXFPoint getLocation() {
        return location;
    }
    
    /**
       Возвращает начальную ширину.
       @return начальная ширина.
       @since TCL DXF 1.0
       @roseuid 37132F610222
     */
    public final double getStartWidth() {
        return start_width;
    }
    
    /**
       Возвращает угол касательной к кривой.
       @return угол касательной к кривой.
       @since TCL DXF 1.0
       @roseuid 37132F610223
     */
    public final double getTangentDirection() {
        return tangent_direction;
    }
    
    /**
       Возвращает true, если вершина сплайна создана сглаживающим сплайном.
       @return true, если вершина сплайна создана сглаживающим сплайном.
       @since TCL DXF 1.0
       @roseuid 37132F610224
     */
    public final boolean isCreatedBySplineFitting() {
        return (flags & 8) == 8;
    }
    
    /**
       Возвращает true, если это дополнительная вершина за счет
       сглаживания кривой.
       @return true, если это дополнительная вершина за счет
       сглаживания кривой.
       @since TCL DXF 1.0
       @roseuid 37132F610225
     */
    public final boolean isExtraVertex() {
        return (flags & 1) == 1;
    }
    
    /**
       Возвращает true, если это вершина трехмерной многоугольной сети.
       @return true, если это вершина трехмерной многоугольной сети.
       @since TCL DXF 1.0
       @roseuid 37132F610226
     */
    public final boolean isPolygonMeshVertex() {
        return (flags & 64) == 64;
    }
    
    /**
       Возвращает true, если это вершина трехмерной полилинии.
       @return true, если это вершина трехмерной полилинии.
       @since TCL DXF 1.0
       @roseuid 37132F610227
     */
    public final boolean isPolyline3DVertex() {
        return (flags & 32) == 32;
    }
    
    /**
       Возвращает true, если это точка управления фрейма сплайна.
       @return true, если это точка управления фрейма сплайна.
       @since TCL DXF 1.0
       @roseuid 37132F610228
     */
    public final boolean isSplineFrameControlPoint() {
        return (flags & 16) == 16;
    }
    
    /**
       Возвращает true, если вершина имеет описание касательной в точке
       сопряжения кривых.
       @return true, если вершина имеет описание касательной в точке
       сопряжения кривых.
       @since TCL DXF 1.0
       @roseuid 37132F610229
     */
    public final boolean isTangentDefine() {
        return (flags & 2) == 2;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F61022A
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        boolean end = false;

        while (end == false)
        {
            tokenizer.nextToken();

            if (readCommonPropertiesDXF(tokenizer) == true)
                continue;

            switch ( tokenizer.getCode() )
            {
                case 0:
                {
                    end = true;
                    break;
                }
                case 9:
                {
                    end = true;
                    break;
                }
                case LOCATION_X:
                {
                    location.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case LOCATION_Y:
                {
                    location.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case LOCATION_Z:
                {
                    location.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case START_WIDTH:
                {
                    start_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case END_WIDTH:
                {
                    end_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case BULGE:
                {
                    bulge = tokenizer.getCommandAsDouble();
                    break;
                }
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
                    break;
                }
                case TANGENT_DIRECTION:
                {
                    tangent_direction = tokenizer.getCommandAsDouble();
                    break;
                }
                default:
                {
                    tokenizer.badInstruction();
                    break;
                }
            }
        }

        tokenizer.pushBack();
    }
}
