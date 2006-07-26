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
import java.util.Vector;
import java.util.Enumeration;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>Polyline</code> представляет проходящую через несколько
   точек широкую ломаную линию.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
 */
public class Polyline extends Entity {
    private int flags = 0;
    private double default_start_width = 0;
    private double default_end_width = 0;
    private int mesh_m_vertex_count = 0;
    private int mesh_n_vertex_count = 0;
    private int smooth_surface_m_densities = 0;
    private int smooth_surface_n_densities = 0;
    private int smooth_surface_type = 0;
    private static final int VERTICES_FOLLOW = 66;
    private static final int FLAGS = 70;
    private static final int DEFAULT_START_WIDTH = 40;
    private static final int DEFAULT_END_WIDTH = 41;
    private static final int MESH_M_VERTEX_COUNT = 71;
    private static final int MESH_N_VERTEX_COUNT = 72;
    private static final int SMOOTH_SURFACE_M_DENSITIES = 73;
    private static final int SMOOTH_SURFACE_N_DENSITIES = 74;
    private static final int SMOOTH_SURFACE_TYPE = 75;
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

    public Polyline ()
    {
        super();
    }

    public Polyline (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает конечную ширину по умолчанию.
       @return конечная ширина по умолчанию.
       @since TCL DXF 1.0
       @roseuid 37132F5F0085
     */
    public final double getDefaultEndWidth() {
        return default_end_width;
    }

    /**
       Возвращает начальную ширину по умолчанию.
       @return начальная ширина по умолчанию.
       @since TCL DXF 1.0
       @roseuid 37132F5F0086
     */
    public final double getDefaultStartWidth() {
        return default_start_width;
    }
    
    /**
       Возвращает наименование примитива.
       @return наименование примитива.
       @since TCL DXF 1.0
     */
    public final String getEntityName() {
        return "Polyline";
    }

    /**
       Возвращает число вершин поверхности в первом направлении.
       @return число вершин в первом направлении.
       @since TCL DXF 1.0
       @roseuid 37132F5F0087
     */
    public final int getMeshMVertexCount() {
        return mesh_m_vertex_count;
    }
    
    /**
       Возвращает число вершин поверхности во втором направлении.
       @return число вершин во втором направлении.
       @since TCL DXF 1.0
       @roseuid 37132F5F0088
     */
    public final int getMeshNVertexCount() {
        return mesh_n_vertex_count;
    }
    
    /**
       Возвращает плотность сглаживания поверхности в первом направлении.
       @return плотность сглаживания поверхности в первом направлении.
       @since TCL DXF 1.0
       @roseuid 37132F5F0089
     */
    public final int getSmoothSurfaceMDensities() {
        return smooth_surface_m_densities;
    }
    
    /**
       Возвращает плотность сглаживания поверхности во втором направлении.
       @return плотность сглаживания поверхности во втором направлении.
       @since TCL DXF 1.0
       @roseuid 37132F5F008A
     */
    public final int getSmoothSurfaceNDensities() {
        return smooth_surface_n_densities;
    }
    
    /**
       Возвращает тип сглаживания поверхности.
       @return тип сглаживания поверхности.
       @since TCL DXF 1.0
       @roseuid 37132F5F008B
     */
    public final int getSmoothSurfaceType() {
        return smooth_surface_type;
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
        int     vertices_follow = 0;

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
                case VERTICES_FOLLOW:
                {
                    vertices_follow = tokenizer.getCommandAsInt();
                    break;
                }
                case FLAGS:
                {
                    flags = tokenizer.getCommandAsInt();
                    break;
                }
                case DEFAULT_START_WIDTH:
                {
                    default_start_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case DEFAULT_END_WIDTH:
                {
                    default_end_width = tokenizer.getCommandAsDouble();
                    break;
                }
                case MESH_M_VERTEX_COUNT:
                {
                    mesh_m_vertex_count = tokenizer.getCommandAsInt();
                    break;
                }
                case MESH_N_VERTEX_COUNT:
                {
                    mesh_n_vertex_count = tokenizer.getCommandAsInt();
                    break;
                }
                case SMOOTH_SURFACE_M_DENSITIES:
                {
                    smooth_surface_m_densities = tokenizer.getCommandAsInt();
                    break;
                }
                case SMOOTH_SURFACE_N_DENSITIES:
                {
                    smooth_surface_m_densities = tokenizer.getCommandAsInt();
                    break;
                }
                case SMOOTH_SURFACE_TYPE:
                {
                    smooth_surface_type = tokenizer.getCommandAsInt();
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

        if (vertices_follow == 1)
            readVerticesDXF(tokenizer);
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
