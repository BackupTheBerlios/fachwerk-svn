// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Shape.java

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
   Класс <code>Shape</code> представляет символ.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Shape extends Entity {
    private double size = 0;
    private double rotation_angle = 0;
    private double x_scale = 1;
    private double obliquing_angle = 0;
    private static final int INSERTION_X = 10;
    private static final int INSERTION_Y = 20;
    private static final int INSERTION_Z = 30;
    private static final int SIZE = 40;
    private static final int SHAPE_NAME = 1;
    private static final int ROTATION_ANGLE = 50;
    private static final int X_SCALE = 41;
    private static final int OBLIQUING_ANGLE = 51;
    private DXFPoint insertion = new DXFPoint ();
    private String shape_name = new String ();
    
    public Shape ()
    {
        super();
    }

    public Shape (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Shape";
    }

    /**
       Возвращает координаты точки вставки символа.
       @return координаты точки вставки символа.
       @since TCL DXF 1.0
       @roseuid 37132F5F0240
     */
    public final DXFPoint getInsertion() {
        return insertion;
    }
    
    /**
       Возвращает угол наклона (по умолчанию = 0).
       @return угол наклона.
       @since TCL DXF 1.0
       @roseuid 37132F5F0241
     */
    public final double getObliquingAngle() {
        return obliquing_angle;
    }
    
    /**
       Возвращает угол поворота (по умолчанию = 0).
       @return угол поворота.
       @since TCL DXF 1.0
       @roseuid 37132F5F0242
     */
    public final double getRotationAngle() {
        return rotation_angle;
    }
    
    /**
       Возвращает имя графического символа (формы).
       @return имя графического символа (формы).
       @since TCL DXF 1.0
       @roseuid 37132F5F026C
     */
    public final String getShapeName() {
        return shape_name;
    }
    
    /**
       Возвращает размер символа.
       @return размер символа.
       @since TCL DXF 1.0
       @roseuid 37132F5F026D
     */
    public final double getSize() {
        return size;
    }
    
    /**
       Возвращает линейный масштаб по оси x (по умолчанию = 1).
       @return линейный масштаб по оси x.
       @since TCL DXF 1.0
       @roseuid 37132F5F026E
     */
    public final double getXScale() {
        return x_scale;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5F026F
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
                case INSERTION_X:
                {
                    insertion.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case INSERTION_Y:
                {
                    insertion.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case INSERTION_Z:
                {
                    insertion.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SIZE:
                {
                    size = tokenizer.getCommandAsDouble();
                    break;
                }
                case SHAPE_NAME:
                {
                    shape_name = tokenizer.getCommand();
                    break;
                }
                case ROTATION_ANGLE:
                {
                    rotation_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case X_SCALE:
                {
                    x_scale = tokenizer.getCommandAsDouble();
                    break;
                }
                case OBLIQUING_ANGLE:
                {
                    obliquing_angle = tokenizer.getCommandAsDouble();
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
