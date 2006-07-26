// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/View.java

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
   Класс <code>View</code> представляет элемент таблицы видов.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class View extends Entry {
    private double height = 0;
    private double width = 0;
    private double lens_length = 0;
    private double front_clipping = 0;
    private double back_clipping = 0;
    private double twist_angle = 0;
    private int view_mode = 0;
    private static final int HEIGHT = 40;
    private static final int WIDTH = 41;
    private static final int CENTER_X = 10;
    private static final int CENTER_Y = 20;
    private static final int DIRECTION_X = 11;
    private static final int DIRECTION_Y = 21;
    private static final int DIRECTION_Z = 31;
    private static final int TARGET_X = 12;
    private static final int TARGET_Y = 22;
    private static final int TARGET_Z = 32;
    private static final int LENS_LENGTH = 42;
    private static final int FRONT_CLIPPING = 43;
    private static final int BACK_CLIPPING = 44;
    private static final int TWIST_ANGLE = 45;
    private static final int VIEW_MODE = 71;
    private DXFPoint center = new DXFPoint ();
    private DXFPoint direction = new DXFPoint ();
    private DXFPoint target = new DXFPoint ();

    public View ()
    {
        super();
    }

    public View (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает заднюю секцию плоскости.
       @return задняя секция плоскости.
       @since TCL DXF 1.0
       @roseuid 37132F6601F0
     */
    public final double getBackClipping() {
        return back_clipping;
    }
    
    /**
       Возвращает центральную точку вида (x, y).
       @return центральная точка вида.
       @since TCL DXF 1.0
       @roseuid 37132F6601F1
     */
    public final DXFPoint getCenter() {
        return center;
    }
    
    /**
       Возвращает направление взгляда.
       @return направление взгляда.
       @since TCL DXF 1.0
       @roseuid 37132F6601F2
     */
    public final DXFPoint getDirection() {
        return direction;
    }
    
    /**
       Возвращает переднюю секцию плоскости.
       @return передняя секция плоскости.
       @since TCL DXF 1.0
       @roseuid 37132F6601F3
     */
    public final double getFrontClipping() {
        return front_clipping;
    }
    
    /**
       Возвращает высоту окна просмотра.
       @return высота окна просмотра.
       @since TCL DXF 1.0
       @roseuid 37132F6601F4
     */
    public final double getHeight() {
        return height;
    }
    
    /**
       Возвращает фокусное расстояние объектива.
       @return фокусное расстояние объектива.
       @since TCL DXF 1.0
       @roseuid 37132F6601F5
     */
    public final double getLensLenght() {
        return lens_length;
    }
    
    /**
       Возвращает координаты целевой точки.
       @return координаты целевой точки.
       @since TCL DXF 1.0
       @roseuid 37132F6601F6
     */
    public final DXFPoint getTarget() {
        return target;
    }
    
    /**
       Возвращает угол вращения.
       @return угол вращения.
       @since TCL DXF 1.0
       @roseuid 37132F6601F7
     */
    public final double getTwistAngle() {
        return twist_angle;
    }

    /**
       Возвращает ширину окна просмотра.
       @return ширина окна просмотра.
       @since TCL DXF 1.0
       @roseuid 37132F6601F8
     */
    public final double getWidth() {
        return width;
    }
    
    /**
       Возвращает true, если активна задняя плоскость отсечения.
       @return true, если активна задняя плоскость отсечения.
       @since TCL DXF 1.0
       @roseuid 37132F66021C
     */
    public final boolean isBackClippingOn() {
        return (view_mode & 4) == 4;
    }
    
    /**
       Возвращает true, если выключены все режимы.
       @return true, если выключены все режимы.
       @since TCL DXF 1.0
       @roseuid 37132F66021D
     */
    public final boolean isDisabled() {
        return view_mode == 0;
    }
    
    /**
       Возвращает true, если активна передняя плоскость отсечения.
       @return true, если активна передняя плоскость отсечения.
       @since TCL DXF 1.0
       @roseuid 37132F66021E
     */
    public final boolean isFrontClippingOn() {
        return (view_mode & 2) == 2;
    }
    
    /**
       Возвращает true, если передняя плоскость отсечения
       не находится в поле зрения.
       @return true, если передняя плоскость отсечения
       не находится в поле зрения.
       @since TCL DXF 1.0
       @roseuid 37132F66021F
     */
    public final boolean isFrontClipNotAtEye() {
        return (view_mode & 16) == 16;
    }
    
    /**
       Возвращает true, если активна перспективная проекция.
       @return true, если активна перспективная проекция.
       @since TCL DXF 1.0
       @roseuid 37132F660220
     */
    public final boolean isPerspectiveViewActive() {
        return (view_mode & 1) == 1;
    }
    
    /**
       Возвращает true, если активен режим UCS.
       @return true, если активен режим UCS.
       @since TCL DXF 1.0
       @roseuid 37132F660221
     */
    public final boolean isUCSOn() {
        return (view_mode & 8) == 8;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F660222
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
                case HEIGHT:
                {
                    height = tokenizer.getCommandAsDouble();
                    break;
                }
                case WIDTH:
                {
                    width = tokenizer.getCommandAsDouble();
                    break;
                }
                case CENTER_X:
                {
                    center.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case CENTER_Y:
                {
                    center.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case DIRECTION_X:
                {
                    direction.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case DIRECTION_Y:
                {
                    direction.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case DIRECTION_Z:
                {
                    direction.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case TARGET_X:
                {
                    target.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case TARGET_Y:
                {
                    target.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case TARGET_Z:
                {
                    target.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case LENS_LENGTH:
                {
                    lens_length = tokenizer.getCommandAsDouble();
                    break;
                }
                case FRONT_CLIPPING:
                {
                    front_clipping = tokenizer.getCommandAsDouble();
                    break;
                }
                case BACK_CLIPPING:
                {
                    back_clipping = tokenizer.getCommandAsDouble();
                    break;
                }
                case TWIST_ANGLE:
                {
                    twist_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case VIEW_MODE:
                {
                    view_mode = tokenizer.getCommandAsInt();
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
