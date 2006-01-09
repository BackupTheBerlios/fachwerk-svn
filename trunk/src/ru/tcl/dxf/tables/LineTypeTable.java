// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/LineTypeTable.java

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
   Класс <code>LineTypeTable</code> представляет таблицу
   типов линий.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class LineTypeTable extends Table {
    private Vector items = new Vector ();

    public LineTypeTable ()
    {
        super();
    }

    public LineTypeTable (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Поиск типа линии с указанным именем. Если тип линии не будет
       найден, то возвращается null.
       @param lineTypeName имя типа линии
       @return тип линии в случае удачи, иначе null
       @since TCL DXF 1.0
       @roseuid 37132F6302AB
     */
    public final LineType find(String lineTypeName) {
        if (lineTypeName == LineType.BYLAYER.getName())
            return LineType.BYLAYER;

        for (Enumeration e = items.elements(); e.hasMoreElements() ;)
        {
            LineType lineType = (LineType )e.nextElement();
            if (lineType.getName().compareTo(lineTypeName) == 0)
                return lineType;
        }

        return null;
    }
    
    /**
       Возвращает список типов линий.
       @return список типов линий.
       @since TCL DXF 1.0
       @roseuid 37132F6302AD
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F6302AE
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDTAB)
                return;

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.LTYPE)
            {
                LineType line_type = new LineType(getDocument());
                line_type.readDXF(tokenizer);
                items.addElement(line_type);
                continue;
            }

            // Флаг 70, показывающий кол-во записей в таблице игнорируется
            if (tokenizer.getCode() == 70)
                continue;

            tokenizer.badInstruction();
        }
    }
}
