// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Insert.java

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
import java.util.Enumeration;
import java.util.Vector;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;
import ru.tcl.dxf.blocks.Block;


/**
   С помощью данного класса повторно вводится в изображение
   описанный ранее объект.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Insert extends Entity {
    private double x_scale = 1;
    private double y_scale = 1;
    private double z_scale = 1;
    private double rotation_angle = 0;
    private int column_counts = 1;
    private int row_counts = 1;
    private double column_spacing = 0;
    private double row_spacing = 0;
    private static final int ATTRIBUTES_FOLLOW = 66;
    private static final int BLOCK_NAME = 2;
    private static final int INSERTION_X = 10;
    private static final int INSERTION_Y = 20;
    private static final int INSERTION_Z = 30;
    private static final int X_SCALE = 41;
    private static final int Y_SCALE = 42;
    private static final int Z_SCALE = 43;
    private static final int ROTATION_ANGLE = 50;
    private static final int COLUMN_COUNTS = 70;
    private static final int ROW_COUNTS = 71;
    private static final int COLUMN_SPACING = 44;
    private static final int ROW_SPACING = 45;
    private Block block = null;
    private DXFPoint insertion = new DXFPoint ();
    private Vector attributes = new Vector ();
    
    public Insert ()
    {
        super();
    }

    public Insert (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает список атрибутов.
       @return список атрибутов.
       @since TCL DXF 1.0
       @roseuid 37132F5D0215
     */
    public final Enumeration getAttributes() {
        return attributes.elements();
    }
    
    /**
       Возвращает имя вводимого блока.
       @return имя вводимого блока.
       @since TCL DXF 1.0
       @roseuid 37132F5D0216
     */
    public final Block getBlock() {
        return null; //block_name;
    }
    
    /**
       Возвращает число колонок при многократном выводе (по умолчанию = 1).
       @return число колонок при многократном выводе.
       @since TCL DXF 1.0
       @roseuid 37132F5D0217
     */
    public final int getColumnCounts() {
        return column_counts;
    }
    
    /**
       Возвращает расстояние между колонками.
       @return расстояние между колонками.
       @since TCL DXF 1.0
       @roseuid 37132F5D0218
     */
    public final double getColumnSpacing() {
        return column_spacing;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Insert";
    }

    /**
       Возвращает координаты точки блока.
       @return координаты точки блока.
       @since TCL DXF 1.0
       @roseuid 37132F5D0219
     */
    public final DXFPoint getInsertion() {
        return insertion;
    }
    
    /**
       Возвращает угол поворота (по умочанию = 0).
     *
       @return угол поворота.
       @since TCL DXF 1.0
       @roseuid 37132F5D021A
     */
    public final double getRotationAngle() {
        return rotation_angle;
    }
    
    /**
       Возвращает число рядов при многократном выводе (по умолчанию = 1).
       @return число рядов при многократном выводе.
       @since TCL DXF 1.0
       @roseuid 37132F5D021B
     */
    public final int getRowCounts() {
        return row_counts;
    }
    
    /**
       Возвращает расстояние между рядами.
       @return расстояние между рядами.
       @since TCL DXF 1.0
       @roseuid 37132F5D021C
     */
    public final double getRowSpacing() {
        return row_spacing;
    }
    
    /**
       Возвращает масштабный коэффициент по x-оси.
       @return масштабный коэффициент по x-оси.
       @since TCL DXF 1.0
       @roseuid 37132F5D021D
     */
    public final double getXScale() {
        return x_scale;
    }
    
    /**
       Возвращает масштабный коэффициент по y-оси.
       @return масштабный коэффициент по y-оси.
       @since TCL DXF 1.0
       @roseuid 37132F5D021E
     */
    public final double getYScale() {
        return y_scale;
    }
    
    /**
       Возвращает масштабный коэффициент по z-оси.
       @return масштабный коэффициент по z-оси.
       @since TCL DXF 1.0
       @roseuid 37132F5D021F
     */
    public final double getZScale() {
        return z_scale;
    }
    
    /**
       Возвращает список примитивов.
       @return список примитивов.
       @since TCL DXF 1.0
       @roseuid 37132F5D0220
     */
    public final Enumeration items() {
        return block.items();
    }
    
    /**
       Возвращает "полный" список примитивов, т.е все
       примитивы представляющие графические объекты плюс
       примитивы объектов <code>Insert</code> представленных
       в списке.
       @return "полный" список примитивов.
       @since TCL DXF 1.0
       @roseuid 37132F5D0221
     */
    public final Enumeration itemsFull() {
        return block.itemsFull();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5D0222
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        boolean end               = false;
        int     attributes_follow = 0;

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
                case ATTRIBUTES_FOLLOW:
                {
                    attributes_follow = tokenizer.getCommandAsInt();
                    break;
                }
                case BLOCK_NAME:
                {
                    block = getDocument().getBlocks().find(tokenizer.getCommand());
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
                case X_SCALE:
                {
                    x_scale = tokenizer.getCommandAsDouble();
                    break;
                }
                case Y_SCALE:
                {
                    y_scale = tokenizer.getCommandAsDouble();
                    break;
                }
                case Z_SCALE:
                {
                    z_scale = tokenizer.getCommandAsDouble();
                    break;
                }
                case ROTATION_ANGLE:
                {
                    rotation_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case COLUMN_COUNTS:
                {
                    column_counts = tokenizer.getCommandAsInt();
                    break;
                }
                case ROW_COUNTS:
                {
                    row_counts = tokenizer.getCommandAsInt();
                    break;
                }
                case COLUMN_SPACING:
                {
                    column_spacing = tokenizer.getCommandAsDouble();
                    break;
                }
                case ROW_SPACING:
                {
                    row_spacing = tokenizer.getCommandAsDouble();
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

        if (attributes_follow == 1)
            readAttributesDXF(tokenizer);
    }

    /**
       Считывание аттрибутов из dxf файла.
       @param tokenizer входной поток
       @since TCL DXF 1.0
       @roseuid 37132F5D024E
     */
    private void readAttributesDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            switch (tokenizer.getKey())
            {
                case DXFTokenizer.SEQEND:
                {
                    return;
                }
                case DXFTokenizer.ATTRIB:
                {
                    Attrib attrib = new Attrib(getDocument());
                    attrib.readDXF(tokenizer);
                    attributes.addElement(attrib);
                    break;
                }
                default:
                {
                    tokenizer.badInstruction();
                    break;
                }
            }
        }
    }
}
