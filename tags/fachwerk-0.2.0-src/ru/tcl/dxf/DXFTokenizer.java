// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/DXFTokenizer.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

slight modification by A.V 2004

*/

package ru.tcl.dxf;

import java.io.Serializable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;


/**
   Класс <code>DXFTokenizer</code> представляет собой "разборщик"
   DXF файлов. Он является внутренним классом пакета и помогает
   другим классам при считывании их из файла. Вы не должны явно
   использовать данный класс.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public final class DXFTokenizer {
    private boolean isBack = false;
    private int line = 0;
    private int code = - 1;
    private int key = - 1;
    private boolean viewErrors = false;
    public static final int BLOCKS = 1;
    public static final int ENDSEC = 2;
    public static final int EOF = 3;
    public static final int ENTITIES = 4;
    public static final int HEADER = 5;
    public static final int SECTION = 6;
    public static final int TABLE = 7;
    public static final int TABLES = 8;
    public static final int ARC = 15;
    public static final int ATTDEF = 16;
    public static final int ATTRIB = 17;
    public static final int BLOCK = 18;
    public static final int CIRCLE = 19;
    public static final int DIMENSION = 20;
    public static final int FACE3D = 21;
    public static final int INSERT = 22;
    public static final int LINE = 23;
    public static final int LINE3D = 24;
    public static final int POINT = 25;
    public static final int POLYLINE = 26;
    public static final int LWPOLYLINE = 1126;   // added A.V. June 2004
    public static final int SEQEND = 27;
    public static final int SHAPE = 28;
    public static final int SOLID = 29;
    public static final int TEXT = 30;
    public static final int TRACE = 31;
    public static final int LAYER = 32;
    public static final int LTYPE = 33;
    public static final int STYLE = 34;
    public static final int ENDTAB = 35;
    public static final int VERTEX = 36;
    public static final int VIEW = 37;
    public static final int VPORT = 38;
    public static final int UCS = 39;
    public static final int ENDBLK = 40;
    public static final int ACADVER = 41;
    public static final int ANGBASE = 42;
    public static final int ANGDIR = 43;
    public static final int ATTDIA = 44;
    public static final int ATTMODE = 45;
    public static final int ATTREQ = 46;
    public static final int AUNITS = 47;
    public static final int AUPREC = 48;
    public static final int AXISMODE = 49;
    public static final int AXISUNIT = 50;
    public static final int BLIPMODE = 51;
    public static final int CECOLOR = 52;
    public static final int CELTYPE = 53;
    public static final int CHAMFERA = 54;
    public static final int CHAMFERB = 55;
    public static final int CLAYER = 56;
    public static final int COORDS = 57;
    public static final int DIMALT = 58;
    public static final int DIMALTD = 59;
    public static final int DIMALTF = 60;
    public static final int DIMAPOST = 61;
    public static final int DIMASO = 62;
    public static final int DIMASZ = 63;
    public static final int DIMBLK = 64;
    public static final int DIMBLK1 = 65;
    public static final int DIMBLK2 = 66;
    public static final int DIMCEN = 67;
    public static final int DIMDLE = 68;
    public static final int DIMDLI = 69;
    public static final int DIMEXE = 70;
    public static final int DIMEXO = 71;
    public static final int DIMLFAC = 72;
    public static final int DIMLIM = 73;
    public static final int DIMPOST = 74;
    public static final int DIMRND = 75;
    public static final int DIMSAH = 76;
    public static final int DIMSCALE = 77;
    public static final int DIMSE1 = 78;
    public static final int DIMSE2 = 79;
    public static final int DIMSHO = 80;
    public static final int DIMSOXD = 81;
    public static final int DIMTAD = 82;
    public static final int DIMTIH = 83;
    public static final int DIMTIX = 84;
    public static final int DIMTM = 85;
    public static final int DIMTOFL = 86;
    public static final int DIMTOH = 87;
    public static final int DIMTOL = 88;
    public static final int DIMTP = 89;
    public static final int DIMTSZ = 90;
    public static final int DIMTVP = 91;
    public static final int DIMTXT = 92;
    public static final int DIMZIN = 93;
    public static final int DRAGMODE = 94;
    public static final int ELEVATION = 95;
    public static final int EXTMAX = 96;
    public static final int EXTMIN = 97;
    public static final int FILLETRAD = 98;
    public static final int FILLMODE = 99;
    public static final int FLATLAND = 100;
    public static final int GRIDMODE = 101;
    public static final int GRIDUNIT = 102;
    public static final int HANDLING = 103;
    public static final int HANDSEED = 104;
    public static final int INSBASE = 105;
    public static final int LIMCHECK = 106;
    public static final int LIMMAX = 107;
    public static final int LIMMIN = 108;
    public static final int LTSCALE = 109;
    public static final int LUNITS = 110;
    public static final int LUPREC = 111;
    public static final int MENU = 112;
    public static final int MIRRTEXT = 113;
    public static final int ORTHOMODE = 114;
    public static final int OSMODE = 115;
    public static final int PDMODE = 116;
    public static final int PDSIZE = 117;
    public static final int PLINEWID = 118;
    public static final int QTEXTMODE = 119;
    public static final int REGENMODE = 120;
    public static final int SKETCHINC = 121;
    public static final int SKPOLY = 122;
    public static final int SNAPANG = 123;
    public static final int SNAPBASE = 124;
    public static final int SNAPISOPAIR = 125;
    public static final int SNAPMODE = 126;
    public static final int SNAPSTYLE = 127;
    public static final int SNAPUNIT = 128;
    public static final int SPLFRAME = 129;
    public static final int SPLINESEGS = 130;
    public static final int SPLINETYPE = 131;
    public static final int SURFTAB1 = 132;
    public static final int SURFTAB2 = 133;
    public static final int SURFTYPE = 134;
    public static final int SURFU = 135;
    public static final int SURFV = 136;
    public static final int TDCREATE = 137;
    public static final int TDINDWG = 138;
    public static final int TDUPDATE = 139;
    public static final int TDUSRTIMER = 140;
    public static final int TEXTSIZE = 141;
    public static final int TEXTSTYLE = 142;
    public static final int THICKNESS = 143;
    public static final int TRACEWID = 144;
    public static final int UCSNAME = 145;
    public static final int UCSORG = 146;
    public static final int UCSXDIR = 147;
    public static final int UCSYDIR = 148;
    public static final int USERI1 = 149;
    public static final int USERI2 = 150;
    public static final int USERI3 = 151;
    public static final int USERI4 = 152;
    public static final int USERI5 = 153;
    public static final int USERR1 = 154;
    public static final int USERR2 = 155;
    public static final int USERR3 = 156;
    public static final int USERR4 = 157;
    public static final int USERR5 = 158;
    public static final int USRTIMER = 159;
    public static final int VIEWCTR = 160;
    public static final int VIEWDIR = 161;
    public static final int VIEWSIZE = 162;
    public static final int WORLDVIEW = 163;
    private String command = new String ();
    private BufferedReader reader = null;
    private Vector errors = new Vector ();
    private static Hashtable tokens = new Hashtable ();

    static {
        tokens.put("BLOCKS", new Integer(BLOCKS));
        tokens.put("ENDSEC", new Integer(ENDSEC));
        tokens.put("EOF", new Integer(EOF));
        tokens.put("ENTITIES", new Integer(ENTITIES));
        tokens.put("HEADER", new Integer(HEADER));
        tokens.put("SECTION", new Integer(SECTION));
        tokens.put("TABLE", new Integer(TABLE));
        tokens.put("TABLES", new Integer(TABLES));
        tokens.put("LAYER", new Integer(LAYER));
        tokens.put("LTYPE", new Integer(LTYPE));
        tokens.put("STYLE", new Integer(STYLE));
        tokens.put("VIEW", new Integer(VIEW));
        tokens.put("VPORT", new Integer(VPORT));
        tokens.put("UCS", new Integer(UCS));
        tokens.put("ARC", new Integer(ARC));
        tokens.put("ATTDEF", new Integer(ATTDEF));
        tokens.put("ATTRIBUTE", new Integer(ATTRIB));
        tokens.put("BLOCK", new Integer(BLOCK));
        tokens.put("CIRCLE", new Integer(CIRCLE));
        tokens.put("DIMENSION", new Integer(DIMENSION));
        tokens.put("3DFACE", new Integer(FACE3D));
        tokens.put("INSERT", new Integer(INSERT));
        tokens.put("LINE", new Integer(LINE));
        tokens.put("3DLINE", new Integer(LINE3D));
        tokens.put("POINT", new Integer(POINT));
        tokens.put("LWPOLYLINE", new Integer(LWPOLYLINE)); // added A.V. June 2004
        tokens.put("POLYLINE", new Integer(POLYLINE));
        tokens.put("SEQEND", new Integer(SEQEND));
        tokens.put("SHAPE", new Integer(SHAPE));
        tokens.put("SOLID", new Integer(SOLID));
        tokens.put("TEXT", new Integer(TEXT));
        tokens.put("TRACE", new Integer(TRACE));
        tokens.put("ENDTAB", new Integer(ENDTAB));
        tokens.put("VERTEX", new Integer(VERTEX));
        tokens.put("ENDBLK", new Integer(ENDBLK));
        tokens.put("$ACADVER", new Integer(ACADVER));
        tokens.put("$ANGBASE", new Integer(ANGBASE));
        tokens.put("$ANGDIR", new Integer(ANGDIR));
        tokens.put("$ATTDIA", new Integer(ATTDIA));
        tokens.put("$ATTMODE", new Integer(ATTMODE));
        tokens.put("$ATTREQ", new Integer(ATTREQ));
        tokens.put("$AUNITS", new Integer(AUNITS));
        tokens.put("$AUPREC", new Integer(AUPREC));
        tokens.put("$AXISMODE", new Integer(AXISMODE));
        tokens.put("$AXISUNIT", new Integer(AXISUNIT));
        tokens.put("$BLIPMODE", new Integer(BLIPMODE));
        tokens.put("$CECOLOR", new Integer(CECOLOR));
        tokens.put("$CELTYPE", new Integer(CELTYPE));
        tokens.put("$CHAMFERA", new Integer(CHAMFERA));
        tokens.put("$CHAMFERB", new Integer(CHAMFERB));
        tokens.put("$CLAYER", new Integer(CLAYER));
        tokens.put("$COORDS", new Integer(COORDS));
        tokens.put("$DIMALT", new Integer(DIMALT));
        tokens.put("$DIMALTD", new Integer(DIMALTD));
        tokens.put("$DIMALTF", new Integer(DIMALTF));
        tokens.put("$DIMAPOST", new Integer(DIMAPOST));
        tokens.put("$DIMASO", new Integer(DIMASO));
        tokens.put("$DIMASZ", new Integer(DIMASZ));
        tokens.put("$DIMBLK", new Integer(DIMBLK));
        tokens.put("$DIMBLK1", new Integer(DIMBLK1));
        tokens.put("$DIMBLK2", new Integer(DIMBLK2));
        tokens.put("$DIMCEN", new Integer(DIMCEN));
        tokens.put("$DIMDLE", new Integer(DIMDLE));
        tokens.put("$DIMDLI", new Integer(DIMDLI));
        tokens.put("$DIMEXE", new Integer(DIMEXE));
        tokens.put("$DIMEXO", new Integer(DIMEXO));
        tokens.put("$DIMLFAC", new Integer(DIMLFAC));
        tokens.put("$DIMLIM", new Integer(DIMLIM));
        tokens.put("$DIMPOST", new Integer(DIMPOST));
        tokens.put("$DIMRND", new Integer(DIMRND));
        tokens.put("$DIMSAH", new Integer(DIMSAH));
        tokens.put("$DIMSCALE", new Integer(DIMSCALE));
        tokens.put("$DIMSE1", new Integer(DIMSE1));
        tokens.put("$DIMSE2", new Integer(DIMSE2));
        tokens.put("$DIMSHO", new Integer(DIMSHO));
        tokens.put("$DIMSOXD", new Integer(DIMSOXD));
        tokens.put("$DIMTAD", new Integer(DIMTAD));
        tokens.put("$DIMTIH", new Integer(DIMTIH));
        tokens.put("$DIMTIX", new Integer(DIMTIX));
        tokens.put("$DIMTM", new Integer(DIMTM));
        tokens.put("$DIMTOFL", new Integer(DIMTOFL));
        tokens.put("$DIMTOH", new Integer(DIMTOH));
        tokens.put("$DIMTOL", new Integer(DIMTOL));
        tokens.put("$DIMTP", new Integer(DIMTP));
        tokens.put("$DIMTSZ", new Integer(DIMTSZ));
        tokens.put("$DIMTVP", new Integer(DIMTVP));
        tokens.put("$DIMTXT", new Integer(DIMTXT));
        tokens.put("$DIMZIN", new Integer(DIMZIN));
        tokens.put("$DRAGMODE", new Integer(DRAGMODE));
        tokens.put("$ELEVATION", new Integer(ELEVATION));
        tokens.put("$EXTMAX", new Integer(EXTMAX));
        tokens.put("$EXTMIN", new Integer(EXTMIN));
        tokens.put("$FILLETRAD", new Integer(FILLETRAD));
        tokens.put("$FILLMODE", new Integer(FILLMODE));
        tokens.put("$FLATLAND", new Integer(FLATLAND));
        tokens.put("$GRIDMODE", new Integer(GRIDMODE));
        tokens.put("$GRIDUNIT", new Integer(GRIDUNIT));
        tokens.put("$HANDLING", new Integer(HANDLING));
        tokens.put("$HANDSEED", new Integer(HANDSEED));
        tokens.put("$INSBASE", new Integer(INSBASE));
        tokens.put("$LIMCHECK", new Integer(LIMCHECK));
        tokens.put("$LIMMAX", new Integer(LIMMAX));
        tokens.put("$LIMMIN", new Integer(LIMMIN));
        tokens.put("$LTSCALE", new Integer(LTSCALE));
        tokens.put("$LUNITS", new Integer(LUNITS));
        tokens.put("$LUPREC", new Integer(LUPREC));
        tokens.put("$MENU", new Integer(MENU));
        tokens.put("$MIRRTEXT", new Integer(MIRRTEXT));
        tokens.put("$ORTHOMODE", new Integer(ORTHOMODE));
        tokens.put("$OSMODE", new Integer(OSMODE));
        tokens.put("$PDMODE", new Integer(PDMODE));
        tokens.put("$PDSIZE", new Integer(PDSIZE));
        tokens.put("$PLINEWID", new Integer(PLINEWID));
        tokens.put("$QTEXTMODE", new Integer(QTEXTMODE));
        tokens.put("$REGENMODE", new Integer(REGENMODE));
        tokens.put("$SKETCHINC", new Integer(SKETCHINC));
        tokens.put("$SKPOLY", new Integer(SKPOLY));
        tokens.put("$SNAPANG", new Integer(SNAPANG));
        tokens.put("$SNAPBASE", new Integer(SNAPBASE));
        tokens.put("$SNAPISOPAIR", new Integer(SNAPISOPAIR));
        tokens.put("$SNAPMODE", new Integer(SNAPMODE));
        tokens.put("$SNAPSTYLE", new Integer(SNAPSTYLE));
        tokens.put("$SNAPUNIT", new Integer(SNAPUNIT));
        tokens.put("$SPLFRAME", new Integer(SPLFRAME));
        tokens.put("$SPLINESEGS", new Integer(SPLINESEGS));
        tokens.put("$SPLINETYPE", new Integer(SPLINETYPE));
        tokens.put("$SURFTAB1", new Integer(SURFTAB1));
        tokens.put("$SURFTAB2", new Integer(SURFTAB2));
        tokens.put("$SURFTYPE", new Integer(SURFTYPE));
        tokens.put("$SURFU", new Integer(SURFU));
        tokens.put("$SURFV", new Integer(SURFV));
        tokens.put("$TDCREATE", new Integer(TDCREATE));
        tokens.put("$TDINDWG", new Integer(TDINDWG));
        tokens.put("$TDUPDATE", new Integer(TDUPDATE));
        tokens.put("$TDUSRTIMER", new Integer(TDUSRTIMER));
        tokens.put("$TEXTSIZE", new Integer(TEXTSIZE));
        tokens.put("$TEXTSTYLE", new Integer(TEXTSTYLE));
        tokens.put("$THICKNESS", new Integer(THICKNESS));
        tokens.put("$TRACEWID", new Integer(TRACEWID));
        tokens.put("$UCSNAME", new Integer(UCSNAME));
        tokens.put("$UCSORG", new Integer(UCSORG));
        tokens.put("$UCSXDIR", new Integer(UCSXDIR));
        tokens.put("$UCSYDIR", new Integer(UCSYDIR));
        tokens.put("$USERI1", new Integer(USERI1));
        tokens.put("$USERI2", new Integer(USERI2));
        tokens.put("$USERI3", new Integer(USERI3));
        tokens.put("$USERI4", new Integer(USERI4));
        tokens.put("$USERI5", new Integer(USERI5));
        tokens.put("$USERR1", new Integer(USERR1));
        tokens.put("$USERR2", new Integer(USERR2));
        tokens.put("$USERR3", new Integer(USERR3));
        tokens.put("$USERR4", new Integer(USERR4));
        tokens.put("$USERR5", new Integer(USERR5));
        tokens.put("$USRTIMER", new Integer(USRTIMER));
        tokens.put("$VIEWCTR", new Integer(VIEWCTR));
        tokens.put("$VIEWDIR", new Integer(VIEWDIR));
        tokens.put("$VIEWSIZE", new Integer(VIEWSIZE));
        tokens.put("$WORLDVIEW", new Integer(WORLDVIEW));
    }
    /**
       Конструктор по умолчанию.
       @since TCL DXF 1.0
       @roseuid 37132F530175
     */
    private DXFTokenizer() {
    }
    
    /**
       Конструктор.
       @since TCL DXF 1.0
       @roseuid 37132F530176
     */
    public DXFTokenizer(String file, boolean viewErrors) throws IOException {
        this.viewErrors = viewErrors;
        FileReader fr = new FileReader(file);
        reader = new BufferedReader(fr);
    }

    /**
       Уведомление о неопознанной команде.
       @since TCL DXF 1.0
       @roseuid 37132F53017A
     */
    public final void badInstruction() {
        if (viewErrors == true)
        {
            errors.addElement("Unknow instruction at line " +
                              String.valueOf(line) +
                              ": code [" + String.valueOf(code) +
                              "], command [" + command + "]");
        }
    }
    
    /**
       Закрытие потока.
       @since TCL DXF 1.0
       @roseuid 37132F53017B
     */
    public final void close() throws IOException {
        reader.close();
    }
    
    /**
       Возвращает код команды.
       @return код команды.
       @since TCL DXF 1.0
       @roseuid 37132F53017C
     */
    public final int getCode() {
        return code;
    }
    
    /**
       Возвращает значение команды как <code>String</code>.
       @return значение команды как <code>String</code>.
       @since TCL DXF 1.0
       @roseuid 37132F53017D
     */
    public final String getCommand() {
        return new String(command);
    }
    
    /**
       Возвращает значение команды как <code>double</code>.
       @return значение команды как <code>double</code>.
       @since TCL DXF 1.0
       @roseuid 37132F53017E
     */
    public final double getCommandAsDouble() {
        String s = command.trim();
        return new Double(s).doubleValue();
    }
    
    /**
       Возвращает значение команды как <code>int</code>.
       @return значение команды как <code>int</code>.
       @since TCL DXF 1.0
       @roseuid 37132F53017F
     */
    public final int getCommandAsInt() {
        String s = command.trim();
        return new Integer(s).intValue();
    }

    /**
       Возвращает ключ команды.
       @since TCL DXF 1.0
       @roseuid 37132F530181
     */
    public final int getKey() {
        return key;
    }
    
    /**
       Возвращает список ошибок.
       @return список ошибок.
       @since TCL DXF 1.0
       @roseuid 37132F530182
     */
    public final Vector getErrors() {
        return errors;
    }
    
    /**
       Считывает следующую лексему.
       @since TCL DXF 1.0
       @roseuid 37132F530183
     */
    public final void nextToken() throws IOException {
        if (isBack == true)
            isBack = false;
        else
        {
            String str;

            str = reader.readLine();
            str = str.trim();
            code = new Integer(str).intValue();
            
            command = reader.readLine();
            Integer n = (Integer)tokens.get(command);

            if (n != null)
                key = n.intValue();
            else
                key = -1;

            line += 2;
        }
    }
    
    /**
       Возврат к предыдущей лексеме.
       @since TCL DXF 1.0
       @roseuid 37132F530184
     */
    public final void pushBack() {
        isBack = true;
    }
}
