// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Face3d.java

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
   Класс <code>Face3d</code> представляет трехмерные поверхности.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Face3d extends Entity {
    private int flags = 0;
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
    private static final int FLAGS = 70;
    private DXFPoint first_corner = new DXFPoint ();
    private DXFPoint second_corner = new DXFPoint ();
    private DXFPoint third_corner = new DXFPoint ();
    private DXFPoint fourth_corner = new DXFPoint ();
    
    public Face3d ()
    {
        super();
    }

    public Face3d (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Face3d";
    }

    /**
       Возвращает координаты первого угла.
       @return координаты первого угла.
       @since TCL DXF 1.0
       @roseuid 37132F5C03A5
     */
    public final DXFPoint getFirstCorner() {
        return first_corner;
    }
    
    /**
       Возвращает координаты четвертого угла.
       @return координаты четвертого угла.
       @since TCL DXF 1.0
       @roseuid 37132F5C03A6
     */
    public final DXFPoint getFourthCorner() {
        return fourth_corner;
    }
    
    /**
       Возвращает координаты второго угла.
       @return координаты второго угла.
       @since TCL DXF 1.0
       @roseuid 37132F5C03A7
     */
    public final DXFPoint getSecondCorner() {
        return second_corner;
    }
    
    /**
       Возвращает координаты третьего угла.
       @return координаты третьего угла.
       @since TCL DXF 1.0
       @roseuid 37132F5C03A8
     */
    public final DXFPoint getThirdCorner() {
        return third_corner;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5C03A9
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
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
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
    
    /**
       Видимость первого края.
       @return true, если край виден.
       @since TCL DXF 1.0
       @roseuid 37132F5C03AB
     */
    public final boolean visibleFirstEdge() {
        return (flags & 1) == 1;
    }
    
    /**
       Видимость четвертого края.
       @return true, если край виден.
       @since TCL DXF 1.0
       @roseuid 37132F5C03AC
     */
    public final boolean visibleFourthEdge() {
        return (flags & 8) == 8;
    }
    
    /**
       Видимость второго края.
       @return true, если край виден.
       @since TCL DXF 1.0
       @roseuid 37132F5C03AD
     */
    public final boolean visibleSecondEdge() {
        return (flags & 2) == 2;
    }
    
    /**
       Видимость третьего края.
       @return true, если край виден.
       @since TCL DXF 1.0
       @roseuid 37132F5C03AE
     */
    public final boolean visibleThirdEdge() {
        return (flags & 4) == 4;
    }
}
