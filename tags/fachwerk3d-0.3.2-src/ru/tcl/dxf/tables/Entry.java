// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Entry.java

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
import ru.tcl.dxf.DXFObject;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>Entry</code> является базовым для всех
   элементов таблиц. В нем инкапсулированы общие свойства
   элементов таблиц секции TABLES.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public abstract class Entry extends DXFObject {
    private int flags = 0;
    private static final int NAME = 2;
    private static final int FLAGS = 70;
    private String name = new String ();

    /**
       @roseuid 37132F620099
     */
    public Entry() {
        super();
    }

    public Entry (DXFDocument doc)
    {
        super(doc);
    }

    public Entry (DXFDocument doc, String name)
    {
        this(doc);
        this.name = name;
    }

    /**
       Возвращает флажки элемента таблицы.
       @return флажки элемента таблицы.
       @since TCL DXF 1.0
       @roseuid 37132F62009C
     */
    public final int getFlags() {
        return flags;
    }
    
    /**
       Возвращает имя элемента таблицы.
       @return имя элемента таблицы.
       @since TCL DXF 1.0
       @roseuid 37132F62009D
     */
    public final String getName() {
        return name;
    }
    
    /**
       Считывает общее свойство табличных элементов. Возвращает true,
       если считывание прошло успешно.
       @return true, если считывание прошло успешно.
       @since TCL DXF 1.0
       @roseuid 37132F62009E
     */
    protected boolean readCommonPropertiesDXF(DXFTokenizer tokenizer) {
        switch ( tokenizer.getCode() )
        {
            case NAME:
            {
                name = tokenizer.getCommand();
                return true;
            }
            case FLAGS:
            {
                flags = tokenizer.getCommandAsInt();
                return true;
            }
        }

        return false;
    }
}
