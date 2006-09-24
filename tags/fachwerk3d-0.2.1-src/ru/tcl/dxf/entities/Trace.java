// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Trace.java

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
   Класс <code>Trace</code> представляет соединяющую 4 точки
   замкнутую линию, которую можно трактовать как контур четырехугольника
   либо как полосу.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0 
 */
public class Trace extends Entity {
    private static final int FIRST_CORNER_X = 10;
    private static final int FIRST_CORNER_Y = 20;
    private static final int FIRST_CORNER_Z = 30;
    private static final int SECOND_CORNER_X = 11;
    private static final int SECOND_CORNER_Y = 21;
    private static final int SECOND_CORNER_Z = 31;
    private static final int THIRD_CORNER_X = 12;
    private static final int THIRD_CORNER_Y = 22;
    private static final int THIRD_CORNER_Z = 32;
    private static final int FOURTH_CORNER_X = 13;
    private static final int FOURTH_CORNER_Y = 23;
    private static final int FOURTH_CORNER_Z = 33;
    private DXFPoint first_corner = new DXFPoint ();
    private DXFPoint second_corner = new DXFPoint ();
    private DXFPoint third_corner = new DXFPoint ();
    private DXFPoint fourth_corner = new DXFPoint ();

    public Trace ()
    {
        super();
    }

    public Trace (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Trace";
    }

    /**
       Возвращает координаты первого угла.
       @return координаты первого угла.
       @since TCL DXF 1.0
       @roseuid 37132F6100A3
     */
    public final DXFPoint getFirstCorner() {
        return first_corner;
    }
    
    /**
       Возвращает координаты четвертого угла.
       @return координаты четвертого угла.
       @since TCL DXF 1.0
       @roseuid 37132F6100A4
     */
    public final DXFPoint getFourthCorner() {
        return fourth_corner;
    }
    
    /**
       Возвращает координаты второго угла.
       @return координаты второго угла.
       @since TCL DXF 1.0
       @roseuid 37132F6100A5
     */
    public final DXFPoint getSecondCorner() {
        return second_corner;
    }
    
    /**
       Возвращает координаты третьего угла.
       @return координаты третьего угла.
       @since TCL DXF 1.0
       @roseuid 37132F6100A6
     */
    public final DXFPoint getThirdCorner() {
        return third_corner;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F6100A7
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
                case FIRST_CORNER_X:
                {
                    first_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case FIRST_CORNER_Y:
                {
                    first_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case FIRST_CORNER_Z:
                {
                    first_corner.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SECOND_CORNER_X:
                {
                    second_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SECOND_CORNER_Y:
                {
                    second_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SECOND_CORNER_Z:
                {
                    second_corner.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case THIRD_CORNER_X:
                {
                    third_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case THIRD_CORNER_Y:
                {
                    third_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case THIRD_CORNER_Z:
                {
                    third_corner.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case FOURTH_CORNER_X:
                {
                    fourth_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case FOURTH_CORNER_Y:
                {
                    fourth_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case FOURTH_CORNER_Z:
                {
                    fourth_corner.setZ( tokenizer.getCommandAsDouble() );
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
