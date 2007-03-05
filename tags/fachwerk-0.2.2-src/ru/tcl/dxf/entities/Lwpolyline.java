// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/entities/Polyline.java

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
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;
import java.util.*;


/**
   Класс <code>Lwpolyline</code> представляет проходящую через несколько
   точек широкую ломаную линию.
 * It has been implemented by A.V. June 2003. It may be incomplete.
   @author A. Vontobel
   @since fw 0.09
 */
public class Lwpolyline extends Entity {
    private int flags = 0;
    private double start_width = 0; //default_start_width = 0;
    private double end_width = 0; //default_end_width = 0;
    private double constant_width = 0;
    private int vertex_count = 0;   
    private double buldge = 0;
    private static final int PKT_X = 10;
    private static final int PKT_Y = 20;
    //private static final int VERTICES_FOLLOW = 66;
    private static final int FLAGS = 70;
    private static final int START_WIDTH = 40;
    private static final int END_WIDTH = 41;
    private static final int BULDGE = 42;
    private static final int CONSTANT_WIDTH = 43;    
    private static final int VERTEX_COUNT = 90;
    
    private DXFPoint pkt = new DXFPoint ();
    private ArrayList pktliste = new ArrayList ();
    
    /**
       Константа сглаживания поверхности.
       Без сглаживания.
     */
    public static final int NO_SMOOTH = 0;
    /**
       Константа сглаживания поверхности.
       Квадратичная кривая.
     */
    public static final int QUADRATIC_BSPLINE = 5;
    /**
       Константа сглаживания поверхности.
       Кубическая кривая B-сплайн.
     */
    public static final int CUBIC_BSPLINE = 6;
    /**
       Константа сглаживания поверхности.
       Кривая Безье.
     */
    public static final int BEZIER = 8;
    private Vector items = new Vector ();

    public Lwpolyline()
    {
        super();
    }

    public Lwpolyline(DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает координаты точки.
       @return координаты точки.
       @since fw 0.09
     */
    public final DXFPoint[] getPkte() {
        DXFPoint[] pkte = new DXFPoint[vertex_count];
        pktliste.toArray(pkte);
        pktliste.toString();
        
        
        /*int i = 0;
        for (Iterator it = pktliste.iterator(); it.hasNext();) {            
            pkt = (DXFPoint) it.next();
            pkte[i] = pkt;
            i++;
        }*/
        return pkte;
    }
    
    /**
       Возвращает конечную ширину по умолчанию.
       @return конечная ширина по умолчанию.
       @since TCL DXF 1.0
       @roseuid 37132F5F0085
     */
    public final double getEndWidth() { //getDefaultEndWidth() {
        return end_width; //default_end_width;
    }

    /**
       Возвращает начальную ширину по умолчанию.
       @return начальная ширина по умолчанию.
       @since TCL DXF 1.0
       @roseuid 37132F5F0086
     */
    public final double getStartWidth() {//getDefaultStartWidth() {
        return start_width; //default_start_width;
    }
    
    /**
       Возвращает ширину по умолчанию.
       @return ширина по умолчанию.
       @since fw0.09
     */
    public final double getConstantWidth() {
        return constant_width;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "LWPolyline";
    }

    /**
       Возвращает число вершин поверхности в первом направлении.
       @return число вершин в первом направлении.
       @since TCL DXF 1.0
       @roseuid 37132F5F0087
     */
    public final int getVertexCount() {
        return vertex_count;
    }
    
    public final double getBuldge() {
        return buldge;
    }
    
   
    
    /**
       Возвращает список вершин полилинии.
       @return список вершин.
       @since TCL DXF 1.0
       @roseuid 37132F5F008C
     */
    public final Enumeration items() {
        return items.elements();
    }
    
    /**
       Возвращает true, если 3D многоугольная сеть замкнута.
       @return true, если 3D многоугольная сеть замкнута.
       @since TCL DXF 1.0
       @roseuid 37132F5F008D
     */
    public final boolean isClosedPolygonMesh3D() {
        return (flags & 32) == 32;
    }
    
    /**
       Возвращает true, если полилиния замкнутая.
       @return true, если полилиния замкнутая.
       @since TCL DXF 1.0
       @roseuid 37132F5F008E
     */
    public final boolean isClosedPolyline() {
        return (flags & 1) == 1;
    }
    
    /**
       Возвращает true, если добавлены вершины сглаживания кривых.
       @return true, если добавлены вершины сглаживания кривых.
       @since TCL DXF 1.0
       @roseuid 37132F5F008F
     */
    public final boolean isCurveFit() {
        return (flags & 2) == 2;
    }
    
    /**
       Возвращает true, если это 3D многоугольная сеть.
       @return true, если это 3D многоугольная сеть.
       @since TCL DXF 1.0
       @roseuid 37132F5F0090
     */
    public final boolean isPolygonMesh3D() {
        return (flags & 16) == 16;
    }
    
    /**
       Возвращает true, если это 3D полилиния.
       @return true, если это 3D полилиния.
       @since TCL DXF 1.0
       @roseuid 37132F5F00B4
     */
    public final boolean isPolyline3D() {
        return (flags & 8) == 8;
    }
    
    /**
       Возвращает true, если добавлены вершины сглаживающих сплайнов.
       @return true, если добавлены вершины сглаживающих сплайнов.
       @since TCL DXF 1.0
       @roseuid 37132F5F00B5
     */
    public final boolean isSplineFit() {
        return (flags & 4) == 4;
    }
    
    /**
       Считывание объекта из DXF файла. Вы не должны явно
       использовать данный метод.
       @param tokenizer разборщик DXF файлов
       @since TCL DXF 1.0
       @roseuid 37132F5F00B6
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        boolean end             = false;
        //int     vertices_follow = 0;

        while (end == false)
        {
            tokenizer.nextToken();

            if (readCommonPropertiesDXF(tokenizer) == true)
                continue;

            switch ( tokenizer.getCode() )
            {
                case 0:
                {
                    end = true;
                    break;
                }
                case 9:
                {
                    end = true;
                    break;
                }
                /*case VERTICES_FOLLOW:
                {
                    vertices_follow = tokenizer.getCommandAsInt();
                    break;
                }*/
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
                    break;
                }
                case START_WIDTH:
                {
                    start_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case END_WIDTH:
                {
                    end_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case CONSTANT_WIDTH:
                {
                    constant_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case VERTEX_COUNT:
                {
                    vertex_count = tokenizer.getCommandAsInt();
                    break;
                }    
                case PKT_X:
                {
                    pkt = new DXFPoint ();
                    pkt.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case PKT_Y:
                {
                    pkt.setY( tokenizer.getCommandAsDouble() );
                    pktliste.add(pkt);
                    break;
                }
                default:
                {
                    tokenizer.badInstruction();
                    break;
                }
            }
        }

        tokenizer.pushBack();

        //if (vertices_follow == 1)
        //    readVerticesDXF(tokenizer);
    }
    
    /**
       Считывание вершин из dxf файла.
       @param tokenizer входной поток
       @since TCL DXF 1.0
       @roseuid 37132F5F00B8
     */
    private void readVerticesDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            switch (tokenizer.getKey())
            {
                case DXFTokenizer.SEQEND:
                {
                    return;
                }
                case DXFTokenizer.VERTEX:
                {
                    Vertex vertex = new Vertex(getDocument());
                    vertex.readDXF(tokenizer);
                    items.addElement(vertex);
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
