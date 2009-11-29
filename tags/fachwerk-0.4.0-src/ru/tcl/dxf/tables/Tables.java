// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Tables.java

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
import ru.tcl.dxf.DXFSection;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>Tables</code> представляет собой секцию таблиц
   в DXF файле.
   <p>
   Если в секции заголовка содержатся простые рабочие значения,
   то в секции таблиц информация разбита по группам, представляющим
   более сложные рабочие значения чертежа. Секция таблиц разделяется
   на четыре подсекции. В каждой подсекции может быть несколько записей.
   Ниже перечислены эти подсекции:
   <p>
   тип линий (LINETYPE)<p>
   слой (LAYER)<p>
   шрифт (STYLE)<p>
   вид (VIEW)<p>
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Tables extends DXFSection {
    private LineTypeTable line_type_table = new LineTypeTable ();
    private LayerTable layer_table = new LayerTable ();
    private StyleTable style_table = new StyleTable ();
    private UCSTable ucs_table = new UCSTable ();
    private ViewTable view_table = new ViewTable ();
    private VportTable vport_table = new VportTable ();

    public Tables ()
    {
        super();
    }

    public Tables (DXFDocument doc)
    {
        super(doc);

        line_type_table.setDocument(doc);
        layer_table.setDocument(doc);
        style_table.setDocument(doc);
        ucs_table.setDocument(doc);
        view_table.setDocument(doc);
        vport_table.setDocument(doc);
    }

    /**
       Возвращает таблицу слоев.
       @return таблица слоев.
       @since TCL DXF 1.0
       @roseuid 37132F65022F
     */
    public final LayerTable getLayerTable() {
        return layer_table;
    }

    /**
       Возвращает таблицу типов линий.
       @return таблица типов линий.
       @since TCL DXF 1.0
       @roseuid 37132F650230
     */
    public final LineTypeTable getLineTypeTable() {
        return line_type_table;
    }

    /**
       Возвращает таблицу стилей.
       @return таблица стилей.
       @since TCL DXF 1.0
       @roseuid 37132F650231
     */
    public final StyleTable getStyleTable() {
        return style_table;
    }

    /**
       Возвращает таблицу пользовательской системы координат.
       @return таблица пользовательской системы координат.
       @since TCL DXF 1.0
       @roseuid 37132F650232
     */
    public final UCSTable getUCSTable() {
        return ucs_table;
    }

    /**
       Возвращает таблицу видов.
       @return таблица видов.
       @since TCL DXF 1.0
       @roseuid 37132F650233
     */
    public final ViewTable getViewTable() {
        return view_table;
    }

    /**
       Возвращает таблицу видовых экранов.
       @return таблица видовых экранов.
       @since TCL DXF 1.0
       @roseuid 37132F650262
     */
    public final VportTable getVportTable() {
        return vport_table;
    }

    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F650263
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            int c;
            switch ((c = tokenizer.getCode()))
            {
                case 0:
                {
                    switch (tokenizer.getKey())
                    {
                        case DXFTokenizer.ENDSEC:
                        {
                            return;
                        }
                        case DXFTokenizer.TABLE:
                        {
                            break;
                        }
                        default:
                        {
                            tokenizer.badInstruction();
                            break;
                        }
                    }

                    break;
                }
                case 2:
                {
                    switch (tokenizer.getKey())
                    {
                        case DXFTokenizer.LTYPE:
                        {
                            line_type_table.readDXF(tokenizer);
                            break;
                        }
                        case DXFTokenizer.LAYER:
                        {
                            layer_table.readDXF(tokenizer);
                            break;
                        }
                        case DXFTokenizer.STYLE:
                        {
                            style_table.readDXF(tokenizer);
                            break;
                        }
                        case DXFTokenizer.VIEW:
                        {
                            view_table.readDXF(tokenizer);
                            break;
                        }
                        case DXFTokenizer.VPORT:
                        {
                            vport_table.readDXF(tokenizer);
                            break;
                        }
                        case DXFTokenizer.UCS:
                        {
                            ucs_table.readDXF(tokenizer);
                            break;
                        }
                        default:
                        {
                            tokenizer.badInstruction();
                            break;
                        }
                    }

                    break;
                }
                default:
                {
                    tokenizer.badInstruction();
                    break;
                }
            }
        }
    }
}
