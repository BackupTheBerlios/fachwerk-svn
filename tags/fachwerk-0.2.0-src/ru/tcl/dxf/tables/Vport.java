// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Vport.java

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
   Класс <code>Vport</code> представляет элемент таблицы
   видовых экранов.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Vport extends Entry {
    private double snap_spacing_x = 0;
    private double snap_spacing_y = 0;
    private double grid_spacing_x = 0;
    private double grid_spacing_y = 0;
    private double view_height = 0;
    private double aspect_ratio = 0;
    private double lens_length = 0;
    private double front_clipping = 0;
    private double back_clipping = 0;
    private double snap_rotation_angle = 0;
    private double view_twist_angle = 0;
    private int view_mode = 0;
    private int circle_zoom_percent = 0;
    private int fast_zoom_setting = 0;
    private int ucs_icon_setting = 0;
    private int snap = 0;
    private int grid = 0;
    private int snap_style = 0;
    private int snap_isopair = 0;
    private static final int LEFT_CORNER_X = 10;
    private static final int LEFT_CORNER_Y = 20;
    private static final int RIGHT_CORNER_X = 11;
    private static final int RIGHT_CORNER_Y = 21;
    private static final int VIEW_CENTER_X = 12;
    private static final int VIEW_CENTER_Y = 22;
    private static final int SNAP_BASE_X = 13;
    private static final int SNAP_BASE_Y = 23;
    private static final int SNAP_SPACING_X = 14;
    private static final int SNAP_SPACING_Y = 24;
    private static final int GRID_SPACING_X = 15;
    private static final int GRID_SPACING_Y = 25;
    private static final int VIEW_DIRECTION_X = 16;
    private static final int VIEW_DIRECTION_Y = 26;
    private static final int VIEW_DIRECTION_Z = 36;
    private static final int VIEW_TARGET_X = 17;
    private static final int VIEW_TARGET_Y = 27;
    private static final int VIEW_TARGET_Z = 37;
    private static final int VIEW_HEIGHT = 40;
    private static final int ASPECT_RATIO = 41;
    private static final int LENS_LENGTH = 42;
    private static final int FRONT_CLIPPING = 43;
    private static final int BACK_CLIPPING = 44;
    private static final int SNAP_ROTATION_ANGLE = 50;
    private static final int VIEW_TWIST_ANGLE = 51;
    private static final int VIEW_MODE = 71;
    private static final int CIRCLE_ZOOM_PERCENT = 72;
    private static final int FAST_ZOOM_SETTING = 73;
    private static final int UCSICON_SETTING = 74;
    private static final int SNAP = 75;
    private static final int GRID = 76;
    private static final int SNAP_STYLE = 77;
    private static final int SNAP_ISOPAIR = 78;
    /**
       Константа изометрического стиля (изометрический план).
       Левый.
     */
    public static final int SNAP_ISOPAIR_LEFT = 0;
    /**
       Константа изометрического стиля (изометрический план).
       Верхний.
     */
    public static final int SNAP_ISOPAIR_TOP = 1;
    /**
       Константа изометрического стиля (изометрический план).
       Правый.
     */
    public static final int SNAP_ISOPAIR_RIGHT = 2;
    /**
       Константа способа привязки.
       Стандартный.
     */
    public static final int SNAP_STYLE_STANDART = 0;
    /**
       Константа способа привязки.
       Изометрический.
     */
    public static final int SNAP_STYLE_ISOMETRIC = 1;
    private DXFPoint left_corner = new DXFPoint ();
    private DXFPoint right_corner = new DXFPoint ();
    private DXFPoint view_center = new DXFPoint ();
    private DXFPoint snap_base = new DXFPoint ();
    private DXFPoint view_direction = new DXFPoint ();
    private DXFPoint view_target = new DXFPoint ();

    public Vport ()
    {
        super();
    }

    public Vport (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает соотношение сторон видового экрана.
       @return соотношение сторон видового экрана.
       @since TCL DXF 1.0
       @roseuid 37132F6702F1
     */
    public final double getAspectRatio() {
        return aspect_ratio;
    }
    
    /**
       Возвращает заднюю секцию плоскости.
       @return задняя секция плоскости.
       @since TCL DXF 1.0
       @roseuid 37132F6702F2
     */
    public final double getBackClipping() {
        return back_clipping;
    }
    
    /**
       Возвращает точность аппроксимации окружности.
       @return точность аппроксимации окружности.
       @since TCL DXF 1.0
       @roseuid 37132F6702F3
     */
    public final int getCircleZoomPercent() {
        return circle_zoom_percent;
    }
    
    /**
       Возвращает переднюю секцию плоскости.
       @return передняя секция плоскости.
       @since TCL DXF 1.0
       @roseuid 37132F6702F4
     */
    public final double getFrontClipping() {
        return front_clipping;
    }
    
    /**
       Возвращает интервал сетки в x-направлении.
       @return интервал сетки в x-направлении.
       @since TCL DXF 1.0
       @roseuid 37132F6702F5
     */
    public final double getGridSpacingX() {
        return grid_spacing_x;
    }
    
    /**
       Возвращает интервал сетки в y-направлении.
       @return интервал сетки в y-направлении.
       @since TCL DXF 1.0
       @roseuid 37132F6702F6
     */
    public final double getGridSpacingY() {
        return grid_spacing_y;
    }
    
    /**
       Возвращает левый нижний угол видового экрана (x, y).
       @return левый нижний угол видового экрана.
       @since TCL DXF 1.0
       @roseuid 37132F6702F7
     */
    public final DXFPoint getLeftCorner() {
        return left_corner;
    }
    
    /**
       Возвращает фокусное расстояние объектива.
       @return фокусное расстояние объектива.
       @since TCL DXF 1.0
       @roseuid 37132F6702F8
     */
    public final double getLensLenght() {
        return lens_length;
    }
    
    /**
       Возвращает правый верхний угол видового экрана (x, y).
       @return правый верхний угол видового экрана.
       @since TCL DXF 1.0
       @roseuid 37132F6702F9
     */
    public final DXFPoint getRightCorner() {
        return right_corner;
    }
    
    /**
       Возвращает базовую точку шаговой привязки (x, y).
       @return базовая точка шаговой привязки.
       @since TCL DXF 1.0
       @roseuid 37132F6702FA
     */
    public final DXFPoint getSnapBase() {
        return snap_base;
    }
    
    /**
       Возвращает изометрический стиль.
       @return изометрический стиль.
       @since TCL DXF 1.0
       @roseuid 37132F6702FB
     */
    public final int getSnapIsopair() {
        return snap_isopair;
    }
    
    /**
       Возвращает угол поворота сетки привязки.
       @return угол поворота сетки привязки.
       @since TCL DXF 1.0
       @roseuid 37132F6702FC
     */
    public final double getSnapRotationAngle() {
        return snap_rotation_angle;
    }
    
    /**
       Возвращает интервал шаговой привязки в x-направлении.
       @return интервал шаговой привязки в x-направлении.
       @since TCL DXF 1.0
       @roseuid 37132F6702FD
     */
    public final double getSnapSpacingX() {
        return snap_spacing_x;
    }
    
    /**
       Возвращает интервал шаговой привязки в y-направлении.
       @return интервал шаговой привязки в y-направлении.
       @since TCL DXF 1.0
       @roseuid 37132F6702FE
     */
    public final double getSnapSpacingY() {
        return snap_spacing_y;
    }
    
    /**
       Возвращает способ привязки.
       @return способ привязки.
       @since TCL DXF 1.0
       @roseuid 37132F6702FF
     */
    public final int getSnapStyle() {
        return snap_style;
    }
    
    /**
       Возвращает пиктограмму системы координат.
       @return пиктограмма системы координат.
       @since TCL DXF 1.0
       @roseuid 37132F670300
     */
    public final int getUCSICONSetting() {
        return ucs_icon_setting;
    }
    
    /**
       Возвращает среднюю точку вида (x, y).
       @return средняя точка вида.
       @since TCL DXF 1.0
       @roseuid 37132F670301
     */
    public final DXFPoint getViewCenter() {
        return view_center;
    }
    
    /**
       Возвращает направление взгляда.
       @return направление взгляда.
       @since TCL DXF 1.0
       @roseuid 37132F670302
     */
    public final DXFPoint getViewDirection() {
        return view_direction;
    }
    
    /**
       Возвращает высоту вида.
       @return высота вида.
       @since TCL DXF 1.0
       @roseuid 37132F670320
     */
    public final double getViewHeight() {
        return view_height;
    }
    
    /**
       Возвращает координаты точки цели.
       @return координаты точки цели.
       @since TCL DXF 1.0
       @roseuid 37132F670321
     */
    public final DXFPoint getViewTarget() {
        return view_target;
    }
    
    /**
       Возвращает угол вращения вида.
       @return угол вращения вида.
       @since TCL DXF 1.0
       @roseuid 37132F670322
     */
    public final double getViewTwistAngle() {
        return view_twist_angle;
    }
    
    /**
       Возвращает true, если активна задняя плоскость отсечения.
       @return true, если активна задняя плоскость отсечения.
       @since TCL DXF 1.0
       @roseuid 37132F670323
     */
    public final boolean isBackClippingOn() {
        return (view_mode & 4) == 4;
    }
    
    /**
       Возвращает true, если выключены все режимы.
       @return true, если выключены все режимы.
       @since TCL DXF 1.0
       @roseuid 37132F670324
     */
    public final boolean isDisabled() {
        return view_mode == 0;
    }
    
    /**
       Возвращает true, если включен режим fast zoom.
       @return true, если включен режим fast zoom.
       @since TCL DXF 1.0
       @roseuid 37132F670325
     */
    public final boolean isFastZoomOn() {
        return fast_zoom_setting != 0;
    }
    
    /**
       Возвращает true, если активна передняя плоскость отсечения.
       @return true, если активна передняя плоскость отсечения.
       @since TCL DXF 1.0
       @roseuid 37132F670326
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
     *
       @roseuid 37132F670327
     */
    public final boolean isFrontClipNotAtEye() {
        return (view_mode & 16) == 16;
    }
    
    /**
       Возвращает true, если GRID включен.
       @return true, если GRID включен.
       @since TCL DXF 1.0
       @roseuid 37132F670328
     */
    public final boolean isGridModeOn() {
        return grid != 0;
    }
    
    /**
       Возвращает true, если активна перспективная проекция.
       @return true, если активна перспективная проекция.
       @since TCL DXF 1.0
       @roseuid 37132F670329
     */
    public final boolean isPerspectiveViewActive() {
        return (view_mode & 1) == 1;
    }
    
    /**
       Возвращает true, если SNAP включен.
       @return true, если SNAP включен.
       @since TCL DXF 1.0
       @roseuid 37132F67032A
     */
    public final boolean isSnapModeOn() {
        return snap != 0;
    }
    
    /**
       Возвращает true, если активен режим UCS.
       @return true, если активен режим UCS.
       @since TCL DXF 1.0
       @roseuid 37132F67032B
     */
    public final boolean isUCSOn() {
        return (view_mode & 8) == 8;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F67032C
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
                case LEFT_CORNER_X:
                {
                    left_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case LEFT_CORNER_Y:
                {
                    left_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case RIGHT_CORNER_X:
                {
                    right_corner.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case RIGHT_CORNER_Y:
                {
                    right_corner.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_CENTER_X:
                {
                    view_center.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_CENTER_Y:
                {
                    view_center.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SNAP_BASE_X:
                {
                    snap_base.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SNAP_BASE_Y:
                {
                    snap_base.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case SNAP_SPACING_X:
                {
                    snap_spacing_x = tokenizer.getCommandAsDouble();
                    break;
                }
                case SNAP_SPACING_Y:
                {
                    snap_spacing_y = tokenizer.getCommandAsDouble();
                    break;
                }
                case GRID_SPACING_X:
                {
                    grid_spacing_x = tokenizer.getCommandAsDouble();
                    break;
                }
                case GRID_SPACING_Y:
                {
                    grid_spacing_y = tokenizer.getCommandAsDouble();
                    break;
                }
                case VIEW_DIRECTION_X:
                {
                    view_direction.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_DIRECTION_Y:
                {
                    view_direction.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_DIRECTION_Z:
                {
                    view_direction.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_TARGET_X:
                {
                    view_target.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_TARGET_Y:
                {
                    view_target.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_TARGET_Z:
                {
                    view_target.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case VIEW_HEIGHT:
                {
                    view_height = tokenizer.getCommandAsDouble();
                    break;
                }
                case ASPECT_RATIO:
                {
                    aspect_ratio = tokenizer.getCommandAsDouble();
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
                case SNAP_ROTATION_ANGLE:
                {
                    snap_rotation_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case VIEW_TWIST_ANGLE:
                {
                    view_twist_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case VIEW_MODE:
                {
                    view_mode = tokenizer.getCommandAsInt();
                    break;
                }
                case CIRCLE_ZOOM_PERCENT:
                {
                    circle_zoom_percent = tokenizer.getCommandAsInt();
                    break;
                }
                case FAST_ZOOM_SETTING:
                {
                    fast_zoom_setting = tokenizer.getCommandAsInt();
                    break;
                }
                case UCSICON_SETTING:
                {
                    ucs_icon_setting = tokenizer.getCommandAsInt();
                    break;
                }
                case SNAP:
                {
                    snap = tokenizer.getCommandAsInt();
                    break;
                }
                case GRID:
                {
                    grid = tokenizer.getCommandAsInt();
                    break;
                }
                case SNAP_STYLE:
                {
                    snap_style = tokenizer.getCommandAsInt();
                    break;
                }
                case SNAP_ISOPAIR:
                {
                    snap_isopair = tokenizer.getCommandAsInt();
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
