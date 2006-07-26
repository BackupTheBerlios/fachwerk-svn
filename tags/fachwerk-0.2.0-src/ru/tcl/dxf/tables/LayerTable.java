// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/LayerTable.java

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
   Класс <code>LayerTable</code> представляет собой таблицу слоев.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class LayerTable extends Table {
    private Vector items = new Vector ();
    
    public LayerTable ()
    {
        super();
    }

    public LayerTable (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Поиск слоя с указанным именем. Если слой не будет
       найден, то возвращается null.
       @param layerName имя слоя
       @return слой в случае удачи, иначе null
       @since TCL DXF 1.0
       @roseuid 37132F62032F
     */
    public final Layer find(String layerName) {
        if (layerName == Layer.DEFAULT.getName())
            return Layer.DEFAULT;

        for (Enumeration e = items.elements(); e.hasMoreElements() ;)
        {
            Layer layer = (Layer )e.nextElement();
            if (layer.getName().compareTo(layerName) == 0)
                return layer;
        }

        return null;
    }
    
    /**
       Возвращает список слоев.
       @return список слоев.
       @since TCL DXF 1.0
       @roseuid 37132F620331
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F620332
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDTAB)
                return;

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.LAYER)
            {
                Layer layer = new Layer(getDocument());
                layer.readDXF(tokenizer);
                items.addElement(layer);
                continue;
            }

            // Флаг 70, показывающий кол-во записей в таблице игнорируется
            if (tokenizer.getCode() == 70)
                continue;
                
            tokenizer.badInstruction();
        }
    }
}
