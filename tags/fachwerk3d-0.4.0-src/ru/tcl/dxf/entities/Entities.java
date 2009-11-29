// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Entities.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf.entities;

import java.io.Serializable;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFSection;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>Entities</code> представляет собой секцию примитивов
   в DXF файле.
   <p>
   В секции примитивов содержится список всех активных графических объектов.
   В секции сохраняются все подробности, необходимые для воспроизведения
   чертежа. Поскольку имеются постоянные ссылки на секцию блоков и таблиц,
   данные секции уже определены и готовы к тому, чтобы быть использованными
   в отдельных графических объектах. Каждый объект имеет свой собственный
   формат описания.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Entities extends DXFSection {
    private Vector items = new Vector ();

    public Entities ()
    {
        super();
    }

    public Entities (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает список примитивов.
       @return список примитивов.
       @since TCL DXF 1.0
       @roseuid 37132F5B0341
     */
    public final Enumeration items() {
        return items.elements();
    }

    /**
       Возвращает "полный" список примитивов, т.е все
       примитивы представляющие графические объекты плюс
       примитивы объектов <code>Insert</code> представленных
       в списке.
       @return "полный" список примитивов.
       @since TCL DXF 1.0
       @see ru.tcl.dxf.entities.Insert
       @see ru.tcl.dxf.blocks.Block
       @roseuid 37132F5B0342
     */
    public final Enumeration itemsFull() {
        return new EntitiesEnumerator(items);
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5B0343
     */
    public final void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDSEC)
                return;

            switch (tokenizer.getKey())
            {
                case DXFTokenizer.ARC:
                {
                    Arc obj = new Arc(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.ATTDEF:
                {
                    Attdef obj = new Attdef(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.ATTRIB:
                {
                    Attrib obj = new Attrib(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.CIRCLE:
                {
                    Circle obj = new Circle(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.DIMENSION:
                {
                    Dimension obj = new Dimension(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.FACE3D:
                {
                    Face3d obj = new Face3d(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.INSERT:
                {
                    Insert obj = new Insert(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.LINE:
                {
                    Line obj = new Line(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.LINE3D:
                {
                    Line3d obj = new Line3d(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.POINT:
                {
                    Point obj = new Point(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.POLYLINE:
                {
                    Polyline obj = new Polyline(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.LWPOLYLINE:  // added A.V. May 2004
                {
                    Lwpolyline obj = new Lwpolyline(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.SHAPE:
                {
                    Shape obj = new Shape(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.SOLID:
                {
                    Solid obj = new Solid(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.TEXT:
                {
                    Text obj = new Text(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
                    break;
                }
                case DXFTokenizer.TRACE:
                {
                    Trace obj = new Trace(getDocument());
                    obj.readDXF(tokenizer);
                    items.addElement(obj);
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
final class EntitiesEnumerator implements Enumeration {
    int count;
    Vector vector;

    /**
       @roseuid 37132F5B037F
     */
    EntitiesEnumerator(Vector v) {
        count = 0;
        vector = new Vector(v.size(), 64);
        setVector(v.elements());
    }

    /**
       @roseuid 37132F5B03AD
     */
    public boolean hasMoreElements() {
        return count < vector.size();
    }
    
    /**
       @roseuid 37132F5B03AE
     */
    public Object nextElement() {
        synchronized (vector)
        {
            if (count < vector.size())
                return vector.elementAt(count++);
        }

        throw new NoSuchElementException("BlockEnumerator");
    }
    
    /**
       @roseuid 37132F5B03AF
     */
    private void setVector(Enumeration e) {
        for (; e.hasMoreElements() ;)
        {
            Object o = e.nextElement();

            if (o instanceof Insert)
                setVector(((Insert )o).itemsFull());
            else
                vector.addElement(o);
        }
    }
}
