// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Point.java

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


/**
   Класс <code>Point</code> представляет точку.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Point extends Entity {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double angle_x_axis = 0;
    private static final int X = 10;
    private static final int Y = 20;
    private static final int Z = 30;
    private static final int ANGLE_X_AXIS = 50;

    public Point ()
    {
        super();
    }

    public Point (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает угол с x-осью.
       @return угол с x-осью.
       @since TCL DXF 1.0
       @roseuid 37132F5E02B2
     */
    public final double getAngleXAxis() {
        return angle_x_axis;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Point";
    }

    /**
       Возвращает x-координату точки.
       @return x-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F5E02B3
     */
    public final double getX() {
        return x;
    }
    
    /**
       Возвращает y-координату точки.
       @return y-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F5E02B4
     */
    public final double getY() {
        return y;
    }
    
    /**
       Возвращает z-координату точки.
       @return z-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F5E02B5
     */
    public final double getZ() {
        return z;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5E02B6
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
                case X:
                {
                    x = tokenizer.getCommandAsDouble();
                    break;
                }
                case Y:
                {
                    y = tokenizer.getCommandAsDouble();
                    break;
                }
                case Z:
                {
                    z = tokenizer.getCommandAsDouble();
                    break;
                }
                case ANGLE_X_AXIS:
                {
                    angle_x_axis = tokenizer.getCommandAsDouble();
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
