// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/blocks/Block.java

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

import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import ru.tcl.dxf.DXFObject;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;
import ru.tcl.dxf.entities.Arc;
import ru.tcl.dxf.entities.Attdef;
import ru.tcl.dxf.entities.Attrib;
import ru.tcl.dxf.entities.Circle;
import ru.tcl.dxf.entities.Dimension;
import ru.tcl.dxf.entities.Face3d;
import ru.tcl.dxf.entities.Insert;
import ru.tcl.dxf.entities.Line;
import ru.tcl.dxf.entities.Line3d;
import ru.tcl.dxf.entities.Point;
import ru.tcl.dxf.entities.Polyline;
import ru.tcl.dxf.entities.Shape;
import ru.tcl.dxf.entities.Solid;
import ru.tcl.dxf.entities.Text;
import ru.tcl.dxf.entities.Trace;
import ru.tcl.dxf.entities.Entity;


/**
   Класс <code>Block</code> представляет блок примитивов.
   @author Basil Tunegov
   @version 1.3
   @since TCL DXF 1.0
 */
public class Block extends DXFObject {
    private int flags = 0;
    private static final int BASE_X = 10;
    private static final int BASE_Y = 20;
    private static final int BASE_Z = 30;
    private static final int NAME = 2;
    private static final int FLAGS = 70;
    private DXFPoint base = new DXFPoint ();
    private String name = new String ();
    private Vector items = new Vector ();

    public Block ()
    {
        super();
    }

    public Block (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает базовую точку.
       @return базовую точку.
       @since TCL DXF 1.0
       @roseuid 37132F5401AF
     */
    public final DXFPoint getBase() {
        return base;
    }

    /**
       Возвращает имя блока.
       @return имя блока.
       @since TCL DXF 1.0
       @roseuid 37132F5401D6
     */
    public final String getName() {
        return name;
    }
    
    /**
       Возвращает true, если блок без имени.
       @return true, если блок без имени.
       @since TCL DXF 1.0
       @roseuid 37132F5401D7
     */
    public final boolean isAnonymous() {
        return (flags & 1) == 1;
    }
    
    /**
       Возвращает true, если следует описание атрибутов.
       @return true, если следует описание атрибутов.
       @since TCL DXF 1.0
       @roseuid 37132F5401D8
     */
    public final boolean isHaveAttributes() {
        return (flags & 2) == 2;
    }
    
    /**
       Возвращает список примитивов.
       @return список примитивов.
       @since TCL DXF 1.0
       @roseuid 37132F5401D9
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Возвращает "полный" список примитивов, т.е все
       примитивы представляющие графические объекты плюс
       примитивы объектов <code>Insert</code> представленных
       в блоке.
       @return "полный" список примитивов.
       @since TCL DXF 1.0
       @see ru.tcl.dxf.entities.Insert
       @see ru.tcl.dxf.blocks.Block
       @roseuid 37132F5401DA
     */
    public final Enumeration itemsFull() {
        return new BlockEnumerator(items);
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5401DB
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            switch (tokenizer.getCode())
            {
                case BASE_X:
                {
                    base.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case BASE_Y:
                {
                    base.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case BASE_Z:
                {
                    base.setZ( tokenizer.getCommandAsDouble() );
                    break;
                }
                case NAME:
                {
                    name = tokenizer.getCommand();
                    break;
                }
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
                    break;
                }
                case 0:
                {
                    switch (tokenizer.getKey())
                    {
                        case DXFTokenizer.ENDBLK:
                        {
                            return;
                        }
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

final class BlockEnumerator implements Enumeration {
    int count;
    Vector vector;

    /**
       @roseuid 37132F540245
     */
    BlockEnumerator(Vector v) {
        count = 0;
        vector = new Vector(v.size(), 64);
        setVector(v.elements());
    }

    /**
       @roseuid 37132F540247
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

    /**
       @roseuid 37132F540249
     */
    public boolean hasMoreElements() {
        return count < vector.size();
    }
    
    /**
       @roseuid 37132F54024A
     */
    public Object nextElement() {
        synchronized (vector)
        {
            if (count < vector.size())
                return vector.elementAt(count++);
        }

        throw new NoSuchElementException("BlockEnumerator");
    }
}
