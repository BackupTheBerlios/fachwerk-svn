// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/DXFDocument.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf;

import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import ru.tcl.dxf.header.Header;
import ru.tcl.dxf.tables.Tables;
import ru.tcl.dxf.blocks.Blocks;
import ru.tcl.dxf.entities.Entities;


/**
   Класс <code>DXFDocument</code> представляет собой DXF документ.
   DXF - файл обмена описаниями чертежей. Файл содержит информацию
   в коде ASCII. Структура этого файла эквивалентна структуре файла
   чертежа (DWG), имеющего двоичный формат.
   <p>
   Все команды в файле DXF объединяются не более чем в 4 секции:
   заголовок, таблицы, блоки и примитивы. Каждая секция не является
   обязательной и может отсутствовать в файле DXF. В этом случае, при
   доступе к какой-либо секции, <code>DXFDocument</code> возвращает
   объект, созданный по умолчанию.
   <p>
   На данный момент поддерживаются DXF файлы версии 10.
   @author Basil Tunegov
   @version 1.3
   @since TCL DXF 1.0
   @see ru.tcl.dxf.header.Header
   @see ru.tcl.dxf.tables.Tables
   @see ru.tcl.dxf.blocks.Blocks
   @see ru.tcl.dxf.entities.Entities
 */
public class DXFDocument implements Serializable{
    private boolean viewErrors = false;
    private transient Vector errors = new Vector ();
    private Blocks blocks = new Blocks (this);
    private Entities entities = new Entities (this);
    private Header header = new Header (this);
    private Tables tables = new Tables (this);

    public DXFDocument ()
    {
    }

    /**
       Возвращает секцию блоков.
       @return секция блоков.
       @since TCL DXF 1.0
       @roseuid 37132F5101CE
     */
    public Blocks getBlocks() {
        return blocks;
    }

    /**
       Возвращает секцию примитивов.
       @return секция примитивов
       @since TCL DXF 1.0
       @roseuid 37132F5101CF
     */
    public Entities getEntities() {
        return entities;
    }

    /**
       Возвращает список ошибок.
       @return список ошибок
       @since TCL DXF 1.0
       @roseuid 37132F5101D0
     */
    public final Enumeration getErrors() {
        return errors.elements();
    }

    /**
       Возвращает секцию заголовка.
       @return секция заголовка
       @since TCL DXF 1.0
       @roseuid 37132F5101D1
     */
    public Header getHeader() {
        return header;
    }

    /**
       Возвращает секцию таблиц.
       @return секция таблиц
       @since TCL DXF 1.0
       @roseuid 37132F5101F4
     */
    public Tables getTables() {
        return tables;
    }

    /**
       Возвращает true, если при разборе DXF файле
       анализируются ошибки (нераспознанные команды).
       <p>
       Данные ошибки могут возникать, если версия DXF файла
       больше поддерживаемой библиотекой.
       @return true, если анализируются ошибки
       @since TCL DXF 1.0
       @roseuid 37132F5101F5
     */
    public final boolean getViewErrors() {
        return viewErrors;
    }

    /**
       Считывание и разбор DXF файла.
       @param file имя DXF файла
       @since TCL DXF 1.0
       @roseuid 37132F5101F6
     */
    public final void readDXF(String file) throws IOException {
        header   = new Header(this);
        tables   = new Tables(this);
        blocks   = new Blocks(this);
        entities = new Entities(this);

        DXFTokenizer tokenizer = new DXFTokenizer(file, viewErrors);

        boolean end = false;
        while (end == false)
        {
            tokenizer.nextToken();

            switch (tokenizer.getKey())
            {
                case DXFTokenizer.SECTION:
                {
                    readSection(tokenizer);
                    break;
                }
                case DXFTokenizer.EOF:
                {
                    end = true;
                    break;
                }
                default:
                {
                    tokenizer.badInstruction();
                    break;
                }
            }
        }

        errors = tokenizer.getErrors();
        tokenizer.close();
    }

    /**
       Считывание и разбор DXF файла.
       @param file объект, представляющий DXF файл
       @since TCL DXF 1.0
       @roseuid 37132F5101F8
     */
    public final void readDXF(File file) throws IOException {
        readDXF(file.getName());
    }

    /**
       Устанавливает значение атрибута "Считывание ошибок".
       Установка этого атрибута в false, при считывании
       DXF файлов может повысить скорость разбора файла.
       @param value анализировать ошибки или нет
       @since TCL DXF 1.0
       @roseuid 37132F5101FA
     */
    public final void setViewErrors(boolean value) {
        viewErrors = value;
    }

    /**
       Считывание секции.
       @param tokenizer входной поток
       @since TCL DXF 1.0
       @roseuid 37132F5101FC
     */
    private final void readSection(DXFTokenizer tokenizer) throws IOException {
        tokenizer.nextToken();

        switch (tokenizer.getKey())
        {
            case DXFTokenizer.HEADER:
            {
                header.readDXF(tokenizer);
                return;
            }

            case DXFTokenizer.TABLES:
            {
                tables.readDXF(tokenizer);
                return;
            }

            case DXFTokenizer.BLOCKS:
            {
                blocks.readDXF(tokenizer);
                return;
            }

            case DXFTokenizer.ENTITIES:
            {
                entities.readDXF(tokenizer);
                return;
            }
            default:
            {
                tokenizer.badInstruction();
                break;
            }
        }
    }
}
