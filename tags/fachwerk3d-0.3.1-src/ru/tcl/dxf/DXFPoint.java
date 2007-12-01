// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/DXFPoint.java

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

import java.io.Serializable;


/**
   Класс <code>DXFPoint</code> представляет точку  в трехмерном
   пространстве. Данный класс используется другими классами для
   представления своих свойств являющимися точками. В некоторых
   случаях данный класс может представлять точку в двумерном
   пространстве.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public final class DXFPoint implements Serializable {
    private double x = 0;
    private double y = 0;
    private double z = 0;

    /**
       Возвращает две координаты точки в строковом
       представлении.
       @return две координаты точки.
       @since TCL DXF 1.0
     */
    public final String coords2dToString() {
        return "[" + x + ", " + y + "]";
    }

    /**
       Возвращает две координаты точки в строковом
       представлении.
       @return две координаты точки.
       @since TCL DXF 1.0
     */
    public final String coords3dToString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    /**
       Возвращает x-координату точки.
       @return x-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F510310
     */
    public final double getX() {
        return x;
    }

    /**
       Возвращает y-координату точки.
       @return y-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F510311
     */
    public final double getY() {
        return y;
    }

    /**
       Возвращает z-координату точки.
       @return z-координата точки.
       @since TCL DXF 1.0
       @roseuid 37132F510312
     */
    public final double getZ() {
        return z;
    }

    /**
       Установливает новую x-координату точки.
       @param value x-координата точки
       @since TCL DXF 1.0
       @roseuid 37132F510313
     */
    public final synchronized void setX(double value) {
        x = value;
    }
    
    /**
       Установливает новую y-координату точки.
       @param value y-координата точки
       @since TCL DXF 1.0
       @roseuid 37132F51033F
     */
    public final synchronized void setY(double value) {
        y = value;
    }
    
    /**
       Установливает новую z-координату точки.
       @param value z-координата точки
       @since TCL DXF 1.0
       @roseuid 37132F510341
     */
    public final synchronized void setZ(double value) {
        z = value;
    }
}
