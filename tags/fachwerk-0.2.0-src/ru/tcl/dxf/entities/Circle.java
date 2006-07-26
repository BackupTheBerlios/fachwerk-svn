// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Circle.java

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
   Класс <code>Circle</code> представляет окружность.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Circle extends Entity {
    private double radius = 0;
    private static final int CENTER_X = 10;
    private static final int CENTER_Y = 20;
    private static final int CENTER_Z = 30;
    private static final int RADIUS = 40;
    private DXFPoint center = new DXFPoint ();

    public Circle ()
    {
        super();
    }

    public Circle (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты центра.
       @return координаты центра.
       @since TCL DXF 1.0
       @roseuid 37132F5A0207
     */
    public final DXFPoint getCenter() {
        return center;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Circle";
    }

    /**
       Возвращает радиус.
       @return радиус.
       @since TCL DXF 1.0
       @roseuid 37132F5A0208
     */
    public final double getRadius() {
        return radius;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5A023A
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
