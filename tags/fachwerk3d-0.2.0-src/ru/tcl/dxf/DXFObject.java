// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/DXFObject.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
BY LICENCE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE
OR ITS DERIVATIVES.

*/

package ru.tcl.dxf;

import java.io.Serializable;
import java.io.IOException;


/**
   Класс <code>DXFObject</code> является базовым для всех
   классов в данном пакете. В нем определен один метод
   <code>readDXF</code>, который служит для считывания
   объекта из DXF файла. Вы не должны явно использовать
   данный метод.
   @author Basil Tunegov
   @version 1.5
   @since TCL DXF 1.0
 */
public abstract class DXFObject implements Serializable {
    private DXFDocument document = null;

    public DXFObject ()
    {
    }

    public DXFObject (DXFDocument doc)
    {
        this();
        setDocument(doc);
    }

    public final DXFDocument getDocument () {
        return document;
    }

    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5002A9
     */
    public abstract void readDXF(DXFTokenizer tokenizer) throws IOException;

    public synchronized void setDocument (DXFDocument value) {
        document = value;
    }
}
