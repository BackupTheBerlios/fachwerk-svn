// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Line3d.java

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
   Класс <code>Line3d</code> представляет линию в пространстве.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Line3d extends Entity {
    private static final int START_X = 10;
    private static final int START_Y = 20;
    private static final int START_Z = 30;
    private static final int END_X = 11;
    private static final int END_Y = 21;
    private static final int END_Z = 31;
    private DXFPoint start = new DXFPoint ();
    private DXFPoint end = new DXFPoint ();

    public Line3d ()
    {
        super();
    }

    public Line3d (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты конечной точки.
       @return координаты конечной точки.
       @since TCL DXF 1.0
       @roseuid 37132F5E016E
     */
    public final DXFPoint getEnd() {
        return end;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Line3d";
    }

    /**
       Возвращает координаты начальной точки.
       @return координаты начальной точки.
       @since TCL DXF 1.0
       @roseuid 37132F5E016F
     */
    public final DXFPoint getStart() {
        return start;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5E0170
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        boolean is_end = false;

        while (is_end == false)
        {
            tokenizer.nextToken();

            if (readCommonPropertiesDXF(tokenizer) == true)
                continue;

            switch ( tokenizer.getCode() )
            {
                case 0:
                {
                    is_end = true;
                    break;
                }
                case 9:
                {
                    is_end = true;
                    break;
                }
                case START_X:
                {
                    start.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case START_Y:
                {
                    start.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case START_Z:
                {
                    start.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case END_X:
                {
                    end.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case END_Y:
                {
                    end.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case END_Z:
                {
                    end.setZ( tokenizer.getCommandAsDouble() );
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
