// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/blocks/Blocks.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf.blocks;

import java.util.Enumeration;
import java.util.Vector;
import java.io.Serializable;
import java.io.IOException;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFSection;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>Blocks</code> представляет собой
   секцию блоков в DXF файле.
   <p>
   Секция блоков DXF файла содержит подробную информацию об
   объектах для всех определенных в чертеже блоков. Эти блоки
   могут быть образованы при выполнении операций нанесения размеров,
   штриховки, вставки изображений из графических библиотек или
   просто быть новыми блоками, созданными во время сеанса работы.
   В секции блоков остаются даже те блоки чертежа, которые были
   стерты с экрана дисплея при работе с системой AutoCAD.
   <p>
   В секции блоков каждый блок определяется индивидуально.
   Хотя при вставке блока возможно, чтобы определение этого блока
   входило в определение другого блока, ни один из блоков не
   определяется внутри других. В DXF файлах отсутствуют вложенные
   определения блоков.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Blocks extends DXFSection {
    private Vector items = new Vector ();
    
    public Blocks ()
    {
        super();
    }

    public Blocks (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Поиск блока с указанным именем. Если блок не будет
       найден, то возвращается null.
       @param blockName имя блока
       @return блок в случае удачи, иначе null
       @since TCL DXF 1.0
       @roseuid 37132F550193
     */
    public final Block find(String blockName) {
        for (Enumeration e = items.elements(); e.hasMoreElements() ;)
        {
            Block block = (Block )e.nextElement();
            if (block.getName().compareTo(blockName) == 0)
                return block;
        }

        return null;
    }
    
    /**
       Возвращает список блоков.
       @return список блоков.
       @since TCL DXF 1.0
       @roseuid 37132F550195
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F550196
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDSEC)
                return;

            if (tokenizer.getKey() == DXFTokenizer.BLOCK)
            {
                Block obj = new Block(getDocument());
                obj.readDXF(tokenizer);
                items.addElement(obj);
                continue;
            }
        }
    }
}
