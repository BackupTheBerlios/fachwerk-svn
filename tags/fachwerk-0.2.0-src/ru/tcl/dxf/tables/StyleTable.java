// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/StyleTable.java

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
   Класс <code>StyleTable</code> представляет таблицу стилей.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class StyleTable extends Table {
    private Vector items = new Vector ();

    public StyleTable ()
    {
        super();
    }

    public StyleTable (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Поиск стиля с указанным именем. Если стиль не будет
       найден, то возвращается null.
       @param styleName имя стиля
       @return стиль в случае удачи, иначе null
       @since TCL DXF 1.0
       @roseuid 37132F64037E
     */
    public final Style find(String styleName) {
        if (styleName == Style.STANDART.getName())
            return Style.STANDART;

        for (Enumeration e = items.elements(); e.hasMoreElements() ;)
        {
            Style style = (Style )e.nextElement();
            if (style.getName().compareTo(styleName) == 0)
                return style;
        }

        return null;
    }
    
    /**
       Возвращает список стилей.
       @return список стилей.
       @since TCL DXF 1.0
       @roseuid 37132F640380
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F640381
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDTAB)
                return;

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.STYLE)
            {
                Style style = new Style(getDocument());
                style.readDXF(tokenizer);
                items.addElement(style);
                continue;
            }

            // Флаг 70, показывающий кол-во записей в таблице игнорируется
            if (tokenizer.getCode() == 70)
                continue;
                
            tokenizer.badInstruction();
        }
    }
}
