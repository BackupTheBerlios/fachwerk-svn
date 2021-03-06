// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Attrib.java

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
   Класс <code>Attrib</code> представляет атрибут.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Attrib extends Entity {
    private double height = 0;
    private int flags = 0;
    private int field_length = 0;
    private double rotation_angle = 0;
    private double x_scale = 1;
    private double obliquing_angle = 0;
    private int generation_flags = 0;
    private int justification_type = 0;
    private static final int INSERTION_X = 10;
    private static final int INSERTION_Y = 20;
    private static final int INSERTION_Z = 30;
    private static final int HEIGHT = 40;
    private static final int VALUE = 1;
    private static final int NAME = 2;
    private static final int FLAGS = 70;
    private static final int FIELD_LENGTH = 73;
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
    private String value = new String ();
    private String name = new String ();
    private Style style = Style.STANDART;
    private DXFPoint alignment = new DXFPoint ();

    public Attrib ()
    {
        super();
    }

    public Attrib (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты точки выравнивания.
       @return координаты точки выравнивания.
       @since TCL DXF 1.0
       @roseuid 37132F5A008C
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
        return "Attrib";
    }

    /**
       Возвращает длину поля (по умолчанию = 0).
       @return длина поля.
       @since TCL DXF 1.0
       @roseuid 37132F5A008D
     */
    public final double getFieldLength() {
        return field_length;
    }
    
    /**
       Возвращает высоту текста.
       @return высота текста.
       @since TCL DXF 1.0
       @roseuid 37132F5A008E
     */
    public final double getHeight() {
        return height;
    }
    
    /**
       Возвращает координаты точки вставки текста.
       @return координаты точки вставки текста.
       @since TCL DXF 1.0
       @roseuid 37132F5A008F
     */
    public final DXFPoint getInsertion() {
        return insertion;
    }
    
    /**
       Возвращает тип выравнивания текста.
       @return тип выравнивания текста.
       @since TCL DXF 1.0
       @roseuid 37132F5A0090
     */
    public final int getJustificationType() {
        return justification_type;
    }
    
    /**
       Возвращает имя атрибута.
       @return имя атрибута.
       @since TCL DXF 1.0
       @roseuid 37132F5A0091
     */
    public final String getName() {
        return name;
    }
    
    /**
       Возвращает угол наклона литер (по умолчанию = 0).
       @return угол наклона литер.
       @since TCL DXF 1.0
       @roseuid 37132F5A0092
     */
    public final double getObliquingAngle() {
        return obliquing_angle;
    }
    
    /**
       Возвращает угол поворота текста (по умолчанию = 0).
       @return угол поворота текста.
       @since TCL DXF 1.0
       @roseuid 37132F5A0093
     */
    public final double getRotationAngle() {
        return rotation_angle;
    }
    
    /**
       Возвращает имя стиля начертания (по умолчанию = STANDART).
       @return имя стиля начертания.
       @since TCL DXF 1.0
       @roseuid 37132F5A0094
     */
    public final Style getStyle() {
        return style;
    }
    
    /**
       Возвращает значение атрибута.
       @return значение атрибута.
       @since TCL DXF 1.0
       @roseuid 37132F5A0095
     */
    public final String getValue() {
        return value;
    }
    
    /**
       Возвращает линейный масштаб по оси x (по умолчанию = 1).
       @return линейный масштаб по оси x.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B4
     */
    public final double getXScale() {
        return x_scale;
    }
    
    /**
       Возвращает true, если текст зеркально отображен по оси х.
       @return true, если текст зеркально отображен по оси х.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B5
     */
    public final boolean isBackwards() {
        return (generation_flags & 2) == 2;
    }
    
    /**
       Возвращает true, если атрибут неизменяемый.
       @return true, если атрибут неизменяемый.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B6
     */
    public final boolean isConstant() {
        return (flags & 2) == 2;
    }
    
    /**
       Возвращает true, если атрибут невидимый.
       @return true, если атрибут невидимый.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B7
     */
    public final boolean isInvisible() {
        return (flags & 1) == 1;
    }
    
    /**
       Возвращает true, если не требуется подсказки при вставке.
       @return true, если не требуется подсказки при вставке.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B8
     */
    public final boolean isPreset() {
        return (flags & 8) == 8;
    }
    
    /**
       Возвращает true, если текст перевернут относительно оси х.
       @return true, если текст перевернут относительно оси х.
       @since TCL DXF 1.0
       @roseuid 37132F5A00B9
     */
    public final boolean isUpsideDown() {
        return (generation_flags & 4) == 4;
    }
    
    /**
       Возвращает true, если требуется подтверждение при вводе
       атрибута.
       @return true, если требуется подтверждение при вводе атрибута.
       @since TCL DXF 1.0
       @roseuid 37132F5A00BA
     */
    public final boolean isVerificationRequired() {
        return (flags & 4) == 4;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5A00BB
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
                case VALUE:
                {
                    value = tokenizer.getCommand();
                    break;
                }
                case NAME:
                {
                    name = tokenizer.getCommand();
                    break;
                }
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
                    break;
                }
                case FIELD_LENGTH:
                {
                    field_length = tokenizer.getCommandAsInt();
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
