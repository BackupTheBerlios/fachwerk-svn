// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/UCS.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf.tables;

import java.io.Serializable;
import java.io.IOException;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;


/**
   Класс <code>UCS</code> представляет элемент таблицы
   пользовательской системы координат.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class UCS extends Entry {
    private static final int ORIGIN_X = 10;
    private static final int ORIGIN_Y = 20;
    private static final int ORIGIN_Z = 30;
    private static final int X_AXIS_DIRECTION_X = 11;
    private static final int X_AXIS_DIRECTION_Y = 21;
    private static final int X_AXIS_DIRECTION_Z = 31;
    private static final int Y_AXIS_DIRECTION_X = 12;
    private static final int Y_AXIS_DIRECTION_Y = 22;
    private static final int Y_AXIS_DIRECTION_Z = 32;
    private DXFPoint origin = new DXFPoint ();
    private DXFPoint x_axis_direction = new DXFPoint ();
    private DXFPoint y_axis_direction = new DXFPoint ();

    public UCS ()
    {
        super();
    }

    public UCS (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает начало системы координат пользователя
       в мировых координатах.
       @return начало системы координат пользователя
       в мировых координатах.
       @since TCL DXF 1.0
       @roseuid 37132F6503E2
     */
    public final DXFPoint getOrigin() {
        return origin;
    }
    
    /**
       Возвращает направление x-оси в мировых координатах.
       @return направление x-оси в мировых координатах.
       @since TCL DXF 1.0
       @roseuid 37132F6503E3
     */
    public final DXFPoint getXAxisDirection() {
        return x_axis_direction;
    }
    
    /**
       Возвращает направление y-оси в мировых координатах.
       @return направление y-оси в мировых координатах.
       @since TCL DXF 1.0
       @roseuid 37132F6503E4
     */
    public final DXFPoint getYAxisDirection() {
        return y_axis_direction;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F6503E5
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
                case ORIGIN_X:
                {
                    origin.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case ORIGIN_Y:
                {
                    origin.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case ORIGIN_Z:
                {
                    origin.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case X_AXIS_DIRECTION_X:
                {
                    x_axis_direction.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case X_AXIS_DIRECTION_Y:
                {
                    x_axis_direction.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case X_AXIS_DIRECTION_Z:
                {
                    x_axis_direction.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case Y_AXIS_DIRECTION_X:
                {
                    y_axis_direction.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case Y_AXIS_DIRECTION_Y:
                {
                    y_axis_direction.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case Y_AXIS_DIRECTION_Z:
                {
                    y_axis_direction.setZ( tokenizer.getCommandAsDouble() );
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
