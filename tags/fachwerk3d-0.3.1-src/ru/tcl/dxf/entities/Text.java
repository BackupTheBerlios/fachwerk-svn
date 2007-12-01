// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Text.java

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
import ru.tcl.dxf.tables.Style;


/**
   Класс <code>Text</code> представляет текст.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Text extends Entity {
    private double height = 0;
    private double rotation_angle = 0;
    private double x_scale = 1;
    private double obliquing_angle = 0;
    private int generation_flags = 0;
    private int justification_type = 0;
    private static final int INSERTION_X = 10;
    private static final int INSERTION_Y = 20;
    private static final int INSERTION_Z = 30;
    private static final int HEIGHT = 40;
    private static final int TEXT = 1;
    private static final int ROTATION_ANGLE = 50;
    private static final int X_SCALE = 41;
    private static final int OBLIQUING_ANGLE = 51;
    private static final int STYLE = 7;
    private static final int GENERATION_FLAGS = 71;
    private static final int JUSTIFICATION_TYPE = 72;
    private static final int ALIGNMENT_X = 11;
    private static final int ALIGNMENT_Y = 21;
    private static final int ALIGNMENT_Z = 31;
    /**
       Константа выравнивания текста.
       Выравненный влево.
     */
    public static final int LEFT_JUSTIFIED = 0;
    /**
       Константа выравнивания текста.
       Отцентрированный.
     */
    public static final int CENTERED = 1;
    /**
       Константа выравнивания текста.
       Выравненный вправо.
     */
    public static final int RIGHT_JUSTIFIED = 2;
    /**
       Константа выравнивания текста.
       Выравненный относительно указаной точки.
     */
    public static final int ALLIGN_BETWEEN_TWO_POINTS = 3;
    /**
       Константа выравнивания текста.
       Середина в указанной точки.
     */
    public static final int MIDDLE_CENTERED = 4;
    /**
       Константа выравнивания текста.
       Вписанный.
     */
    public static final int FIT_BETWEEN_TWO_POINTS = 5;
    private DXFPoint insertion = new DXFPoint ();
    private String text = new String ();
    private Style style = Style.STANDART;
    private DXFPoint alignment = new DXFPoint ();

    public Text ()
    {
        super();
    }

    public Text (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты точки выравнивания.
       @return координаты точки выравнивания.
       @since TCL DXF 1.0
       @roseuid 37132F600268
     */
    public final DXFPoint getAlignment() {
        return alignment;
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Text";
    }

    /**
       Возвращает высоту текста.
       @return высота текста.
       @since TCL DXF 1.0
       @roseuid 37132F600269
     */
    public final double getHeight() {
        return height;
    }
    
    /**
       Возвращает координаты точки вставки текста.
       @return координаты точки вставки текста.
       @since TCL DXF 1.0
       @roseuid 37132F60026A
     */
    public final DXFPoint getInsertion() {
        return insertion;
    }
    
    /**
       Возвращает тип выравнивания текста.
       @return тип выравнивания текста.
       @since TCL DXF 1.0
       @roseuid 37132F600294
     */
    public final int getJustificationType() {
        return justification_type;
    }
    
    /**
       Возвращает угол наклона литер (по умолчанию = 0).
       @return угол наклона литер.
       @since TCL DXF 1.0
       @roseuid 37132F600295
     */
    public final double getObliquingAngle() {
        return obliquing_angle;
    }
    
    /**
       Возвращает угол поворота текста (по умолчанию = 0).
       @return угол поворота текста.
       @since TCL DXF 1.0
       @roseuid 37132F600296
     */
    public final double getRotationAngle() {
        return rotation_angle;
    }
    
    /**
       Возвращает имя стиля начертания (по умолчанию = STANDART).
       @return имя стиля начертания.
       @since TCL DXF 1.0
       @roseuid 37132F600297
     */
    public final Style getStyle() {
        return style;
    }
    
    /**
       Возвращает текст.
       @return текст.
       @since TCL DXF 1.0
       @roseuid 37132F600298
     */
    public final String getText() {
        return text;
    }
    
    /**
       Возвращает линейный масштаб по оси x (по умолчанию = 1).
       @return линейный масштаб по оси x.
       @since TCL DXF 1.0
       @roseuid 37132F600299
     */
    public final double getXScale() {
        return x_scale;
    }
    
    /**
       Возвращает true, если текст зеркально отображен по оси х.
       @return true, если текст зеркально отображен по оси х.
       @since TCL DXF 1.0
       @roseuid 37132F60029A
     */
    public final boolean isBackwards() {
        return (generation_flags & 2) == 2;
    }
    
    /**
       Возвращает true, если текст перевернут относительно оси х.
       @return true, если текст перевернут относительно оси х.
       @since TCL DXF 1.0
       @roseuid 37132F60029B
     */
    public final boolean isUpsideDown() {
        return (generation_flags & 4) == 4;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F60029C
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
                case HEIGHT:
                {
                    height = tokenizer.getCommandAsDouble();
                    break;
                }
                case TEXT:
                {
                    text = tokenizer.getCommand();
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
                case STYLE:
                {
                    style = getDocument().getTables().getStyleTable().find(tokenizer.getCommand());
                    break;
                }
                case GENERATION_FLAGS:
                {
                    generation_flags = tokenizer.getCommandAsInt();
                    break;
                }
                case JUSTIFICATION_TYPE:
                {
                    justification_type = tokenizer.getCommandAsInt();
                    break;
                }
                case ALIGNMENT_X:
                {
                    alignment.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case ALIGNMENT_Y:
                {
                    alignment.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case ALIGNMENT_Z:
                {
                    alignment.setZ( tokenizer.getCommandAsDouble() );
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
