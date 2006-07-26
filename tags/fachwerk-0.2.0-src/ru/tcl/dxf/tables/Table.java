// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/tables/Table.java

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
import ru.tcl.dxf.DXFObject;
import ru.tcl.dxf.DXFDocument;


/**
   Класс <code>Table</code> является базовым для всех
   таблиц в DXF файле.
   @author Basil Tunegov
   @version 1.0
   @since TCL DXF 1.0 
 */
public abstract class Table extends DXFObject
{
    public Table ()
    {
        super();
    }

    public Table (DXFDocument doc)
    {
        super(doc);
    }
}
