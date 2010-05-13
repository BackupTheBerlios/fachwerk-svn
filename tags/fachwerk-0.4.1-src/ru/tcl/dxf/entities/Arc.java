// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Arc.java

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
   Класс <code>Arc</code> представляет дугу окружности.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Arc extends Entity {
    private double radius = 0;
    private double start_angle = 0;
    private double end_angle = 0;
    private static final int CENTER_X = 10;
    private static final int CENTER_Y = 20;
    private static final int CENTER_Z = 30;
    private static final int RADIUS = 40;
    private static final int START_ANGLE = 50;
    private static final int END_ANGLE = 51;
    private DXFPoint center = new DXFPoint ();

    public Arc ()
    {
        super();
    }

    public Arc (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты центра.
       @return координаты центра.
       @since TCL DXF 1.0
       @roseuid 37132F58032B
     */
    public final DXFPoint getCenter() {
        return center;
    }
    
    /**
       Возвращает конечный угол дуги окружности.
       @return конечный угол дуги окружности.
       @since TCL DXF 1.0
       @roseuid 37132F58032C
     */
    public final double getEndAngle() {
        return end_angle;
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Arc";
    }

    /**
       Возвращает радиус.
       @return радиус.
       @since TCL DXF 1.0
       @roseuid 37132F58032D
     */
    public final double getRadius() {
        return radius;
    }
    
    /**
       Возвращает начальный угол дуги окружности.
       @return начальный угол дуги окружности.
       @since TCL DXF 1.0
       @roseuid 37132F58032E
     */
    public final double getStartAngle() {
        return start_angle;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F58032F
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
                case CENTER_X:
                {
                    center.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case CENTER_Y:
                {
                    center.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case CENTER_Z:
                {
                    center.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case RADIUS:
                {
                    radius = tokenizer.getCommandAsDouble();
                    break;
                }
                case START_ANGLE:
                {
                    start_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case END_ANGLE:
                {
                    end_angle = tokenizer.getCommandAsDouble();
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
