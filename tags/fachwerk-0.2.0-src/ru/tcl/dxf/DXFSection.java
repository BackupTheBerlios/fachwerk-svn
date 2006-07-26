// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/DXFSection.java

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
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFTokenizer;


/**
   Класс <code>DXFSection</code> является базовым для всех классов
   представляющих собой секции. Секции описывают различные виды
   информации, представленной в DXF файле.
   @author Basil Tunegov
   @version 1.2
   @since TCL DXF 1.0
   @see ru.tcl.dxf.header.Header
   @see ru.tcl.dxf.tables.Tables
   @see ru.tcl.dxf.blocks.Blocks
   @see ru.tcl.dxf.entities.Entities
 */
public abstract class DXFSection extends DXFObject {

    public DXFSection ()
    {
        super();
    }

    public DXFSection (DXFDocument doc)
    {
        super(doc);
    }
}
