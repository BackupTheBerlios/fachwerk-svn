// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Layer.java

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
   Класс <code>Layer</code> представляет собой элемент таблицы слоев.
   Каждый такой элемент представляет слой чертежа.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Layer extends Entry {
    private int color = 0;
    private static final int COLOR = 62;
    private static final int LINE_TYPE = 6;
    private LineType line_type = new LineType ();
    public static final Layer DEFAULT = new Layer (null, "0");

    public Layer() {
        super();
    }

    public Layer (DXFDocument doc)
    {
        super(doc);
    }

    public Layer (DXFDocument doc, String name) {
        super(doc, name);
    }

    /**
       Возвращает цвет слоя.
       @return цвет слоя.
       @since TCL DXF 1.0
       @roseuid 37132F6201E6
     */
    public final int getColor() {
        return color;
    }

    /**
       Возвращает имя типа линии.
       @return имя типа линии.
       @since TCL DXF 1.0
       @roseuid 37132F6201E7
     */
    public final LineType getLineType() {
        return line_type;
    }

    /**
       Возвращает true, если слой заморожен.
       @return true, если слой заморожен.
       @since TCL DXF 1.0
       @roseuid 37132F6201E8
     */
    public final boolean isFrozen() {
        return (getFlags() & 1) == 1;
    }

    /**
       Возвращает true, если слой выключен.
       @return true, если слой выключен.
       @since TCL DXF 1.0
       @roseuid 37132F6201E9
     */
    public final boolean isOff() {
        return color < 0;
    }

    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F6201EA
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
                case COLOR:
                {
                    color = tokenizer.getCommandAsInt();
                    break;
                }
                case LINE_TYPE:
                {
                    line_type = getDocument().getTables().getLineTypeTable().find(tokenizer.getCommand());
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
