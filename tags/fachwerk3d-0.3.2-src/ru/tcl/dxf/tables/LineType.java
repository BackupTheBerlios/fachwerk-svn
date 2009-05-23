// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/LineType.java

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
import java.util.Vector;
import java.util.Enumeration;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>LineType</code> представляет элемент таблицы
   типов линий.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class LineType extends Entry {
    private int alignment = 0;
    private double pattern_length = 0;
    private static final int ASCII_TYPE = 3;
    private static final int ALIGNMENT = 72;
    private static final int DASH_COUNTS = 73;
    private static final int PATTERN_LENGTH = 40;
    private static final int DASH_LENGTH = 49;
    private String ascii_type = new String ();
    private Vector items = new Vector ();
    public static final LineType BYLAYER = new LineType ();

    public LineType ()
    {
        super();
    }

    public LineType (DXFDocument doc)
    {
        super(doc);
    }

    public final int getAlignment() {
        return alignment;
    }

    /**
       Возвращает строковое представление линии.
       @return строковое представление линии.
       @since TCL DXF 1.0
       @roseuid 37132F63012D
     */
    public final String getASCIIType() {
        return ascii_type;
    }

    /**
       Возвращает длину образца линии.
       @return длина образца линии.
       @since TCL DXF 1.0
       @roseuid 37132F63012E
     */
    public final double getPatternLength() {
        return pattern_length;
    }
    
    /**
       Возвращает вектор штрихов линии. Значения штрихов
       интерпритируются следующим образом:
       отрицательное значение = пропуск,
       0                      = точка,
       положительное значение = штрих.
       @return вектор штрихов линии.
       @since TCL DXF 1.0
       @roseuid 37132F63012F
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F630130
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
                case ASCII_TYPE:
                {
                    ascii_type = tokenizer.getCommand();
                    break;
                }
                case ALIGNMENT:
                {
                    alignment = tokenizer.getCommandAsInt();
                    break;
                }
                case DASH_COUNTS:
                {
                    break;
                }
                case PATTERN_LENGTH:
                {
                    pattern_length = tokenizer.getCommandAsDouble();
                    break;
                }
                case DASH_LENGTH:
                {
                    items.addElement(new Double(tokenizer.getCommandAsDouble()));
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
