// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Style.java

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


/**
   Класс <code>Style</code> представляет элемент таблицы стилей.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Style extends Entry {
    private double fixed_height = 0;
    private double width_factor = 0;
    private double obliquing_angle = 0;
    private int generation_flags = 0;
    private double last_height_used = 0;
    private static final int FIXED_HEIGHT = 40;
    private static final int WIDTH_FACTOR = 41;
    private static final int OBLIQUING_ANGLE = 50;
    private static final int GENERATION_FLAGS = 71;
    private static final int LAST_HEIGHT_USED = 42;
    private static final int PRIMARY_FILE_NAME = 3;
    private static final int BIGFONT_FILE_NAME = 4;
    private String primary_file_name = new String ();
    private String bigfont_file_name = new String ();
    public static final Style STANDART = new Style ();

    public Style ()
    {
        super();
    }

    public Style (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает имя файла Bigfont.
       @return имя файла Bigfont.
       @since TCL DXF 1.0
       @roseuid 37132F640204
     */
    public final String getBigfontFileName() {
        return bigfont_file_name;
    }

    /**
       Возвращает фиксированную высоту текста (0 = не задано).
       @return фиксированная высота текста.
       @since TCL DXF 1.0
       @roseuid 37132F640205
     */
    public final double getFixedHeight() {
        return fixed_height;
    }
    
    /**
       Возвращает высоту, заданную в последний раз.
       @return высота, заданная в последний раз.
       @since TCL DXF 1.0
       @roseuid 37132F640206
     */
    public final double getLastHeightUsed() {
        return last_height_used;
    }
    
    /**
       Возвращает угол наклона.
       @return угол наклона.
       @since TCL DXF 1.0
       @roseuid 37132F640207
     */
    public final double getObliquingAngle() {
        return obliquing_angle;
    }
    
    /**
       Возвращает имя набора символов.
       @return имя набора символов.
       @since TCL DXF 1.0
       @roseuid 37132F640230
     */
    public final String getPrimaryFileName() {
        return primary_file_name;
    }
    
    /**
       Возвращает степень сжатия.
       @return степень сжатия.
       @since TCL DXF 1.0
       @roseuid 37132F640231
     */
    public final double getWidthFactor() {
        return width_factor;
    }
    
    /**
       Возвращает true, если текст зеркально отображен по оси х.
       @return true, если текст зеркально отображен по оси х.
       @since TCL DXF 1.0
       @roseuid 37132F640232
     */
    public final boolean isBackwards() {
        return (generation_flags & 2) == 2;
    }
    
    /**
       Возвращает true, если это таблица символов.
       @return true, если это таблица символов.
       @since TCL DXF 1.0
       @roseuid 37132F640233
     */
    public final boolean isShapeTable() {
        return (getFlags() & 1) == 1;
    }
    
    /**
       Возвращает true, если текст перевернут относительно оси х.
       @return true, если текст перевернут относительно оси х.
       @since TCL DXF 1.0
       @roseuid 37132F640234
     */
    public final boolean isUpsideDown() {
        return (generation_flags & 4) == 4;
    }
    
    /**
       Возвращает true, если текст вертикально ориентирован.
       @return true, если текст вертикально ориентирован.
       @since TCL DXF 1.0
       @roseuid 37132F640235
     */
    public final boolean isVerticallyText() {
        return (getFlags() & 4) == 4;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F640236
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
                case FIXED_HEIGHT:
                {
                    fixed_height = tokenizer.getCommandAsDouble();
                    break;
                }
                case WIDTH_FACTOR:
                {
                    width_factor = tokenizer.getCommandAsDouble();
                    break;
                }
                case OBLIQUING_ANGLE:
                {
                    obliquing_angle = tokenizer.getCommandAsDouble();
                    break;
                }
                case GENERATION_FLAGS:
                {
                    generation_flags = tokenizer.getCommandAsInt();
                    break;
                }
                case LAST_HEIGHT_USED:
                {
                    last_height_used = tokenizer.getCommandAsDouble();
                    break;
                }
                case PRIMARY_FILE_NAME:
                {
                    primary_file_name = tokenizer.getCommand();
                    break;
                }
                case BIGFONT_FILE_NAME:
                {
                    bigfont_file_name = tokenizer.getCommand();
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
