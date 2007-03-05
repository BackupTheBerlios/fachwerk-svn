// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/VportTable.java

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
   Класс <code>VportTable</code> представляет таблицу
   видовых экранов.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class VportTable extends Table {
    private Vector items = new Vector ();

    public VportTable ()
    {
        super();
    }

    public VportTable (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает список видовых экранов.
       @return список видовых экранов.
       @since TCL DXF 1.0
       @roseuid 37132F6800C2
     */
    public final Enumeration items() {
        return items.elements();
    }

    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F6800C3
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDTAB)
                return;

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.VPORT)
            {
                Vport vport = new Vport(getDocument());
                vport.readDXF(tokenizer);
                items.addElement(vport);
                continue;
            }

            // Флаг 70, показывающий кол-во записей в таблице игнорируется
            if (tokenizer.getCode() == 70)
                continue;
                
            tokenizer.badInstruction();
        }
    }
}
