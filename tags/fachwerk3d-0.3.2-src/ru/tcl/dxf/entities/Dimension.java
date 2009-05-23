// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Dimension.java

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
import ru.tcl.dxf.blocks.Block;


/**
   Класс <code>Dimension</code> представляет размерные надписи
   рисунка.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Dimension extends Entity {
    private int type = 0;
    private double leader_length = 0;
    private double angle = 0;
    private double direction = 0;
    private static final int BLOCK_NAME = 2;
    private static final int DEFINITION_X = 10;
    private static final int DEFINITION_Y = 20;
    private static final int DEFINITION_Z = 30;
    private static final int MIDDLE_X = 11;
    private static final int MIDDLE_Y = 21;
    private static final int MIDDLE_Z = 31;
    private static final int INSERTION_X = 12;
    private static final int INSERTION_Y = 22;
    private static final int INSERTION_Z = 32;
    private static final int TYPE = 70;
    private static final int TEXT = 1;
    private static final int SPECIAL_1_X = 13;
    private static final int SPECIAL_1_Y = 23;
    private static final int SPECIAL_1_Z = 33;
    private static final int SPECIAL_2_X = 14;
    private static final int SPECIAL_2_Y = 24;
    private static final int SPECIAL_2_Z = 34;
    private static final int SPECIAL_3_X = 15;
    private static final int SPECIAL_3_Y = 25;
    private static final int SPECIAL_3_Z = 35;
    private static final int SPECIAL_4_X = 16;
    private static final int SPECIAL_4_Y = 26;
    private static final int SPECIAL_4_Z = 36;
    private static final int LEADER_LENGTH = 40;
    private static final int ANGLE = 50;
    private static final int DIRECTION = 51;
    /**
       Константа типа размера.
       Повернутый, горизонтальный или вертикальный.
     */
    public static final int LINEAR = 0;
    /**
       Константа типа размера.
       Выравненный.
     */
    public static final int ALIGNED = 1;
    /**
       Константа типа размера.
       Угловой.
     */
    public static final int ANGULAR = 2;
    /**
       Константа типа размера.
       Диаметр.
     */
    public static final int DIAMETER = 3;
    /**
       Константа типа размера.
       Радиус.
     */
    public static final int RADIUS = 4;
    /**
       Константа типа размера.
       Заданный пользователем.
     */
    public static final int USER_DEFINED = 128;
    private Block block = null;
    private DXFPoint definition = new DXFPoint ();
    private DXFPoint middle = new DXFPoint ();
    private DXFPoint insertion = new DXFPoint ();
    private String text = new String ();
    private DXFPoint special_1 = new DXFPoint ();
    private DXFPoint special_2 = new DXFPoint ();
    private DXFPoint special_3 = new DXFPoint ();
    private DXFPoint special_4 = new DXFPoint ();

    public Dimension ()
    {
        super();
    }

    public Dimension (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает угол наклонного горизонтального или
       вертикального размера.
       @return Возвращает угол наклонного горизонтального или
       вертикального размера.
       @since TCL DXF 1.0
       @roseuid 37132F5B011E
     */
    public final double getAngle() {
        return angle;
    }

    /**
       Возвращает имя блока.
       @return имя блока.
       @since TCL DXF 1.0
       @roseuid 37132F5B0154
     */
    public final Block getBlock() {
        return block;
    }
    
    /**
       Возвращает координаты точки, используемой при задании
       размерной линии (линейные размеры).
       @return координаты точки, используемой при задании
       размерной линии (линейные размеры).
       @since TCL DXF 1.0
       @roseuid 37132F5B0155
     */
    public final DXFPoint getDefinition() {
        return definition;
    }
    
    /**
       Возвращает ориентацию размерного текста.
       @return ориентация размерного текста.
       @since TCL DXF 1.0
       @roseuid 37132F5B0156
     */
    public final double getDirection() {
        return direction;
    }

    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Dimension";
    }

    /**
       Возвращает координаты точки, используемой для задания
       размерных линий при простановке размеров от базовой линии.
       @return координаты точки, используемой для задания
       размерных линий при простановке размеров от базовой линии.
       @since TCL DXF 1.0
       @roseuid 37132F5B0157
     */
    public final DXFPoint getInsertion() {
        return insertion;
    }
    
    /**
       Возвращает длину выносной линии при разметке размеров диаметров
       и радиусов.
       @return длинf выносной линии при разметке размеров диаметров
       и радиусов.
       @since TCL DXF 1.0
       @roseuid 37132F5B0158
     */
    public final double getLeaderLength() {
        return leader_length;
    }
    
    /**
       Возвращает координаты центра размерного текста.
       @return координаты центра размерного текста.
       @since TCL DXF 1.0
       @roseuid 37132F5B0159
     */
    public final DXFPoint getMiddle() {
        return middle;
    }
    
    /**
       Возвращает координаты специальной точки 1.
       Первая линия продолжения для линейного размера или
       конечная точка первой линии для углового размера.
       @return координаты специальной точки 1.
       @since TCL DXF 1.0
       @roseuid 37132F5B015A
     */
    public final DXFPoint getSpecial1() {
        return special_1;
    }
    
    /**
       Возвращает координаты специальной точки 2.
       Вторая линия продолжения для линейного размера или
       начальная точка первой линии для углового размера.
       @return координаты специальной точки 2.
       @since TCL DXF 1.0
       @roseuid 37132F5B015B
     */
    public final DXFPoint getSpecial2() {
        return special_2;
    }
    
    /**
       Возвращает координаты точки, используемой при образмеривании
       круга, или дуги и радиуса.
       Первая точка размерной линии для радиуса и диаметра или
       конечная точка второй линии для углового размера.
       @return координаты точки, используемой при образмеривании
       круга, или дуги и радиуса.
       @since TCL DXF 1.0
       @roseuid 37132F5B015C
     */
    public final DXFPoint getSpecial3() {
        return special_3;
    }
    
    /**
       Возвращает координаты точки, определяющей размерную дугу
       при простановке углового размера.
       @return координаты точки, определяющей размерную дугу
       при простановке углового размера.
       @since TCL DXF 1.0
       @roseuid 37132F5B015D
     */
    public final DXFPoint getSpecial4() {
        return special_4;
    }
    
    /**
       Возвращает текст разметки размеров или количество измерений.
       @return текст разметки размеров или количество измерений.
       @since TCL DXF 1.0
       @roseuid 37132F5B015E
     */
    public final String getText() {
        return text;
    }
    
    /**
       Возвращает тип размера.
       @return тип размера.
       @since TCL DXF 1.0
       @roseuid 37132F5B015F
     */
    public final int getType() {
        return type;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5B0160
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        boolean end = false;

        while (end == false)
        {
            tokenizer.nextToken();

            if (readCommonPropertiesDXF(tokenizer) == true)
                continue;

            switch (tokenizer.getCode())
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
                case BLOCK_NAME:
                {
                    block = getDocument().getBlocks().find(tokenizer.getCommand());
                    break;
                }
                case DEFINITION_X:
                {
                    definition.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case DEFINITION_Y:
                {
                    definition.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case DEFINITION_Z:
                {
                    definition.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case MIDDLE_X:
                {
                    middle.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case MIDDLE_Y:
                {
                    middle.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case MIDDLE_Z:
                {
                    middle.setZ( tokenizer.getCommandAsDouble() );
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
                case TYPE:
                {
                    type = tokenizer.getCommandAsInt();
                    break;
                }
                case TEXT:
                {
                    text = tokenizer.getCommand();
                    break;
                }
                case SPECIAL_1_X:
                {
                    special_1.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_1_Y:
                {
                    special_1.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_1_Z:
                {
                    special_1.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_2_X:
                {
                    special_2.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_2_Y:
                {
                    special_2.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_2_Z:
                {
                    special_2.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_3_X:
                {
                    special_3.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_3_Y:
                {
                    special_3.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_3_Z:
                {
                    special_3.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_4_X:
                {
                    special_4.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_4_Y:
                {
                    special_4.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SPECIAL_4_Z:
                {
                    special_4.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case LEADER_LENGTH:
                {
                    leader_length = tokenizer.getCommandAsDouble();
                    break;
                }
                case ANGLE:
                {
                    angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case DIRECTION:
                {
                    direction = tokenizer.getCommandAsDouble();
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
