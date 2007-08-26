// Source file: c:/work/projects/libs/tunegov class libraries/tcl dxf/java/classes/ru/tcl/dxf/header/Header.java

/*
TCL DXF, version 1.0 betta
Copyright (c) 1999 by Basil Tunegov

THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENCE AS A RESULT OF USING, MODIFYING
OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

*/

package ru.tcl.dxf.header;

import java.io.Serializable;
import java.io.IOException;
import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFSection;
import ru.tcl.dxf.DXFTokenizer;
import ru.tcl.dxf.DXFPoint;


/**
   Класс <code>Header</code> представляет секцию заголовка в DXF файле.
   Данная секция содержит в себе переменные, определяющие рабочие
   параметры чертежа и его окружения. Полезность этих переменных
   зависит от вашего конкретного приложения.
   <p>
   Секция заголовка содержит значения, определяющие рабочие параметры
   чертежа и его окружения. Более подробный список этих параметров
   приведен в оригинальной документации по формату DXF файлов.
   Для примера можно назвать некоторые из значений, которые содержатся
   в секции заголовка, например:
   <p>
   номер версии системы AutoCAD<p>
   координаты габаритов чертежа<p>
   параметры размеров<p>
   <p>
   Полезность этой информации зависит от вашего конкретного приложения.
   @author Basil Tunegov
   @version 1.1
   @since TCL DXF 1.0
 */
public class Header extends DXFSection {
    private double ANGBASE = 0;
    private int ANGDIR = 0;
    private int ATTDIA = 0;
    private int ATTMODE = 0;
    private int ATTREQ = 0;
    private int AUNITS = 0;
    private int AUPREC = 0;
    private int AXISMODE = 0;
    private int BLIPMODE = 0;
    private int CECOLOR = 0;
    private double CHAMFERA = 0;
    private double CHAMFERB = 0;
    private int COORDS = 0;
    private int DIMALT = 0;
    private int DIMALTD = 0;
    private double DIMALTF = 0;
    private int DIMASO = 0;
    private double DIMASZ = 0;
    private double DIMCEN = 0;
    private double DIMDLE = 0;
    private double DIMDLI = 0;
    private double DIMEXE = 0;
    private double DIMEXO = 0;
    private double DIMLFAC = 0;
    private int DIMLIM = 0;
    private double DIMRND = 0;
    private int DIMSAH = 0;
    private double DIMSCALE = 0;
    private int DIMSE1 = 0;
    private int DIMSE2 = 0;
    private int DIMSHO = 0;
    private int DIMSOXD = 0;
    private int DIMTAD = 0;
    private int DIMTIH = 0;
    private int DIMTIX = 0;
    private double DIMTM = 0;
    private int DIMTOFL = 0;
    private int DIMTOH = 0;
    private int DIMTOL = 0;
    private double DIMTP = 0;
    private double DIMTSZ = 0;
    private double DIMTVP = 0;
    private double DIMTXT = 0;
    private int DIMZIN = 0;
    private int DRAGMODE = 0;
    private double ELEVATION = 0;
    private int FASTZOOM = 0;
    private double FILLETRAD = 0;
    private int FILLMODE = 0;
    private int FLATLAND = 0;
    private int GRIDMODE = 0;
    private int HANDLING = 0;
    private int LIMCHECK = 0;
    private double LTSCALE = 0;
    private int LUNITS = 0;
    private int LUPREC = 0;
    private int MIRRTEXT = 0;
    private int ORTHOMODE = 0;
    private int OSMODE = 0;
    private int PDMODE = 0;
    private double PDSIZE = 0;
    private double PLINEWID = 0;
    private int QTEXTMODE = 0;
    private int REGENMODE = 0;
    private double SKETCHINC = 0;
    private int SKPOLY = 0;
    private double SNAPANG = 0;
    private int SNAPISOPAIR = 0;
    private int SNAPMODE = 0;
    private int SNAPSTYLE = 0;
    private int SPLFRAME = 0;
    private int SPLINESEGS = 0;
    private int SPLINETYPE = 0;
    private int SURFTAB1 = 0;
    private int SURFTAB2 = 0;
    private int SURFTYPE = 0;
    private int SURFU = 0;
    private int SURFV = 0;
    private double TDCREATE = 0;
    private double TDINDWG = 0;
    private double TDUPDATE = 0;
    private double TDUSRTIMER = 0;
    private double TEXTSIZE = 0;
    private double THICKNESS = 0;
    private double TRACEWID = 0;
    private int USERI1 = 0;
    private int USERI2 = 0;
    private int USERI3 = 0;
    private int USERI4 = 0;
    private int USERI5 = 0;
    private double USERR1 = 0;
    private double USERR2 = 0;
    private double USERR3 = 0;
    private double USERR4 = 0;
    private double USERR5 = 0;
    private int USRTIMER = 0;
    private double VIEWSIZE = 0;
    private int WORLDVIEW = 0;
    private String ACADVER = new String ();
    private DXFPoint AXISUNIT = new DXFPoint ();
    private String CELTYPE = new String ();
    private String CLAYER = new String ();
    private String DIMAPOST = new String ();
    private String DIMBLK = new String ();
    private String DIMBLK1 = new String ();
    private String DIMBLK2 = new String ();
    private String DIMPOST = new String ();
    private DXFPoint EXTMAX = new DXFPoint ();
    private DXFPoint EXTMIN = new DXFPoint ();
    private DXFPoint GRIDUNIT = new DXFPoint ();
    private String HANDSEED = new String ();
    private DXFPoint INSBASE = new DXFPoint ();
    private DXFPoint LIMMAX = new DXFPoint ();
    private DXFPoint LIMMIN = new DXFPoint ();
    private String MENU = new String ();
    private DXFPoint SNAPBASE = new DXFPoint ();
    private DXFPoint SNAPUNIT = new DXFPoint ();
    private String TEXTSTYLE = new String ();
    private String UCSNAME = new String ();
    private DXFPoint UCSORG = new DXFPoint ();
    private DXFPoint UCSXDIR = new DXFPoint ();
    private DXFPoint UCSYDIR = new DXFPoint ();
    private DXFPoint VIEWCTR = new DXFPoint ();
    private DXFPoint VIEWDIR = new DXFPoint ();

    public Header ()
    {
        super();
    }

    public Header (DXFDocument doc)
    {
        super(doc);
    }

    /**
       Возвращает версию базы данных системы AUTOCAD.
       @return версия базы данных системы AUTOCAD.
       @since TCL DXF 1.0.0
       @roseuid 37132F570182
     */
    public final String getACADVER() {
        return ACADVER;
    }
    
    /**
       Возвращает установку точки отсчета (направления) угла 0.
       @return установка точки отсчета (направления) угла 0.
       @since TCL DXF 1.0.0
       @roseuid 37132F570183
     */
    public final double getANGBASE() {
        return ANGBASE;
    }
    
    /**
       Возвращает направление поворота при отсчета угла.
       0 = против часовой стрелки.
       1 = по часовой стрелке.
       @return направление поворота при отсчета угла.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701B8
     */
    public final int getANGDIR() {
        return ANGDIR;
    }
    
    /**
       Attribute entry dialogues, 1 = on, 0  = off.
       @return attribute entry dialogues.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701B9
     */
    public final int getATTDIA() {
        return ATTDIA;
    }
    
    /**
       Возвращает режим видимости атрибутов.
       0 = атрибуты не отображаются.
       1 = нормальный, отображаются только "видимые" атрибуты.
       2 = отображаются все атрибуты.
       @return режим видимости атрибутов.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BA
     */
    public final int getATTMODE() {
        return ATTMODE;
    }
    
    /**
       Attribute prompting during INSERT, 1 = on, 0 = off.
       @return attribute prompting during INSERT.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BB
     */
    public final int getATTREQ() {
        return ATTREQ;
    }
    
    /**
       Возвращает формат едениц измерения для углов.
       1 = Гадусы в десятичном виде.
       2 = Градусы/минуты/секунды.
       3 = Грады.
       4 = Радианы.
       5 = Геодезический формат.
       @return формат едениц измерения для углов.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BC
     */
    public final int getAUNITS() {
        return AUNITS;
    }
    
    /**
       Возвращает точность представления угла (число знаков
       в представлении дробной части значения).
       @return точность представления угла.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BD
     */
    public final int getAUPREC() {
        return AUPREC;
    }
    
    /**
       Возвращает отображение осей координат (0 = OFF, 1 = ON).
       @return отображение осей координат.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BE
     */
    public final int getAXISMODE() {
        return AXISMODE;
    }
    
    /**
       Возвращает цену деления по оси координат x и y.
       @return цена деления по оси координат x и y.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701BF
     */
    public final DXFPoint getAXISUNIT() {
        return AXISUNIT;
    }
    
    /**
       Возвращает отображение вспомогательных отметок
       (0 = OFF, 1 = ON).
       @return отображение вспомогательных отметок.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C0
     */
    public final int getBLIPMODE() {
        return BLIPMODE;
    }
    
    /**
       Возвращает текущий цвет объекта (0 = BYBLOCK, 256 = BYLAYER).
       @return текущий цвет объекта.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C1
     */
    public final int getCECOLOR() {
        return CECOLOR;
    }
    
    /**
       Возвращает текущий тип линии.
       @return текущий тип линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C2
     */
    public final String getCELTYPE() {
        return CELTYPE;
    }
    
    /**
       First chamfer distance.
       @return first chamfer distance.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C3
     */
    public final double getCHAMFERA() {
        return CHAMFERA;
    }
    
    /**
       Second chamfer distance.
       @return second chamfer distance.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C4
     */
    public final double getCHAMFERB() {
        return CHAMFERB;
    }
    
    /**
       Возвращает имя слоя.
       @return имя слоя.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C5
     */
    public final String getCLAYER() {
        return CLAYER;
    }
    
    /**
       Возвращает режим отображения координат.
       0 = Статическое отображение.
       1 = Непрерывное динамическое отображение.
       2 = При выполнении условия.
       @return режим отображения координат.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C6
     */
    public final int getCOORDS() {
        return COORDS;
    }
    
    /**
       Alternate unit dimensioning performed if nonzero.
       @return alternate unit dimensioning performed if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C7
     */
    public final int getDIMALT() {
        return DIMALT;
    }
    
    /**
       Alternate unit decimal places.
       @return alternate unit decimal places.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C8
     */
    public final int getDIMALTD() {
        return DIMALTD;
    }
    
    /**
       Alternate unit scale factor.
       @return alternate unit scale factor.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701C9
     */
    public final double getDIMALTF() {
        return DIMALTF;
    }
    
    /**
       Alternate dimensioning suffix.
       @return alternate dimensioning suffix.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701EA
     */
    public final String getDIMAPOST() {
        return DIMAPOST;
    }
    
    /**
       1=create associative dimensioning, 0=draw individual entities.
       @return 1=create associative dimensioning, 0=draw individual entities.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701EB
     */
    public final int getDIMASO() {
        return DIMASO;
    }
    
    /**
       Возвращает длину размерных стрелок.
       @return длина размерных стрелок.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701EC
     */
    public final double getDIMASZ() {
        return DIMASZ;
    }
    
    /**
       Возвращает имя блока размерных стрелок.
       @return имя блока размерных стрелок.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701ED
     */
    public final String getDIMBLK() {
        return DIMBLK;
    }
    
    /**
       First arrow block name.
       @return first arrow block name.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701EE
     */
    public final String getDIMBLK1() {
        return DIMBLK1;
    }
    
    /**
       Ssecond arrow block name.
       @return second arrow block name.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701EF
     */
    public final String getDIMBLK2() {
        return DIMBLK2;
    }
    
    /**
       Возвращает размер отметки (маркера) центра.
       @return размер отметки (маркера) центра.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F0
     */
    public final double getDIMCEN() {
        return DIMCEN;
    }
    
    /**
       Возвращает длину концов размерной линии, выходящих за выносные линии.
       @return длина концов размерной линии, выходящих за выносные линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F1
     */
    public final double getDIMDLE() {
        return DIMDLE;
    }
    
    /**
       Возвращает интервал между последовательными размерными линиями.
       @return интервал между последовательными размерными линиями.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F2
     */
    public final double getDIMDLI() {
        return DIMDLI;
    }
    
    /**
       Возвращает длину концов выносных линий, выходящих за размерные линии.
       @return длина концов выносных линий, выходящих за размерные линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F3
     */
    public final double getDIMEXE() {
        return DIMEXE;
    }
    
    /**
       Возвращает расстояние между границей объекта и началом выносной линии.
       @return расстояние между границей объекта и началом выносной линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F4
     */
    public final double getDIMEXO() {
        return DIMEXO;
    }
    
    /**
       Linear measurements scale factor.
       @return linear measurements scale factor.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F5
     */
    public final double getDIMLFAC() {
        return DIMLFAC;
    }
    
    /**
       Возвращает размерные линии (0 = OFF, 1 = ON).
       @return размерные линии (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F6
     */
    public final int getDIMLIM() {
        return DIMLIM;
    }
    
    /**
       General dimensioning suffix.
       @return general dimensioning suffix.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F7
     */
    public final String getDIMPOST() {
        return DIMPOST;
    }
    
    /**
       Возвращает точность задания размеров.
       @return точность задания размеров.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F8
     */
    public final double getDIMRND() {
        return DIMRND;
    }
    
    /**
       Use separate arrow blocks if nonzero.
       @return use separate arrow blocks if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F5701F9
     */
    public final int getDIMSAH() {
        return DIMSAH;
    }
    
    /**
       Возвращает линейный масштаб для размеров.
       @return линейный масштаб для размеров.
       @since TCL DXF 1.0.0
       @roseuid 37132F570226
     */
    public final double getDIMSCALE() {
        return DIMSCALE;
    }
    
    /**
       Подавление вычерчивания 1-ой выносной линии
       (0 = OFF, 1 = ON).
       @return подавление вычерчивания 1-ой выносной линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F570227
     */
    public final int getDIMSE1() {
        return DIMSE1;
    }
    
    /**
       Подавление вычерчивания 2-ой выносной линии
       (0 = OFF, 1 = ON).
       @return подавление вычерчивания 2-ой выносной линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F570228
     */
    public final int getDIMSE2() {
        return DIMSE2;
    }
    
    /**
       1=Recompute dimensions while dragging,
       0=drag original image.
       @return 1=Recompute dimensions while dragging,
       0=drag original image.
       @since TCL DXF 1.0.0
       @roseuid 37132F570229
     */
    public final int getDIMSHO() {
        return DIMSHO;
    }
    
    /**
       Suppress outside-extensions dimension lines if nonzero.
       @return suppress outside-extensions dimension lines if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F57022A
     */
    public final int getDIMSOXD() {
        return DIMSOXD;
    }
    
    /**
       Надпись помещается над размерной линией (0 = OFF, 1 = ON).
       @return надпись помещается над размерной линией (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F57022B
     */
    public final int getDIMTAD() {
        return DIMTAD;
    }
    
    /**
       Надпись, помещенная между выносными линиями,вычерчивается
       горизонтально (0 = OFF, 1 = ON).
       @return надпись, помещенная между выносными линиями, вычерчивается
       горизонтально.
       @since TCL DXF 1.0.0
       @roseuid 37132F57022C
     */
    public final int getDIMTIH() {
        return DIMTIH;
    }
    
    /**
       Force text inside extensions if nonzero.
       @return force text inside extensions if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F57022D
     */
    public final int getDIMTIX() {
        return DIMTIX;
    }
    
    /**
       Возвращает значение минусового допуска.
       @return значение минусового допуска.
       @since TCL DXF 1.0.0
       @roseuid 37132F57022E
     */
    public final double getDIMTM() {
        return DIMTM;
    }
    
    /**
       If text outside extensions, force line between extensions if nonzero.
       @return if text outside extensions, force line between
       extensions if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F57022F
     */
    public final int getDIMTOFL() {
        return DIMTOFL;
    }
    
    /**
       Надпись, помещенная за выносными линиями, вычерчивается
       горизонтально (0 = OFF, 1 = ON).
       @return надпись, помещенная за выносными линиями, вычерчивается
       горизонтально.
       @since TCL DXF 1.0.0
       @roseuid 37132F570230
     */
    public final int getDIMTOH() {
        return DIMTOH;
    }
    
    /**
       Размерные надписи включают допуски (0 = OFF, 1 = ON).
       @return размерные надписи включают допуски.
       @since TCL DXF 1.0.0
       @roseuid 37132F570231
     */
    public final int getDIMTOL() {
        return DIMTOL;
    }
    
    /**
       Возвращает значение плюсового допуска.
       @return значение плюсового допуска.
       @since TCL DXF 1.0.0
       @roseuid 37132F570232
     */
    public final double getDIMTP() {
        return DIMTP;
    }
    
    /**
       Возвращает размер рисок (используемых вместо стрелок).
       @return размер рисок (используемых вместо стрелок).
       @since TCL DXF 1.0.0
       @roseuid 37132F570233
     */
    public final double getDIMTSZ() {
        return DIMTSZ;
    }
    
    /**
       Text vertical position.
       @return text vertical position.
       @since TCL DXF 1.0.0
       @roseuid 37132F570258
     */
    public final double getDIMTVP() {
        return DIMTVP;
    }
    
    /**
       Возвращает высоту текста в размерной надписи.
       @return высота текста в размерной надписи.
       @since TCL DXF 1.0.0
       @roseuid 37132F570259
     */
    public final double getDIMTXT() {
        return DIMTXT;
    }
    
    /**
       Отображение нулевых размеров (0 = OFF, 1 = ON).
       @return отображение нулевых размеров.
       @since TCL DXF 1.0.0
       @roseuid 37132F57025A
     */
    public final int getDIMZIN() {
        return DIMZIN;
    }
    
    /**
       Возвращает режим отслеживания.
       0 = OFF
       1 = ON
       2 = Auto
       @return режим отслеживания.
       @since TCL DXF 1.0.0
       @roseuid 37132F57025B
     */
    public final int getDRAGMODE() {
        return DRAGMODE;
    }
    
    /**
       Возвращает текущий уровень возвышения.
       @return текущий уровень возвышения.
       @since TCL DXF 1.0.0
       @roseuid 37132F57025C
     */
    public final double getELEVATION() {
        return ELEVATION;
    }
    
    /**
       Возвращает габариты чертежа (координата верхнего правого угла).
       @return габариты чертежа (координата верхнего правого угла).
       @since TCL DXF 1.0.0
       @roseuid 37132F57025D
     */
    public final DXFPoint getEXTMAX() {
        return EXTMAX;
    }
    
    /**
       Возвращает габариты чертежа (координата нижнего левого угла).
       @return габариты чертежа (координата нижнего левого угла).
       @since TCL DXF 1.0.0
       @roseuid 37132F57025E
     */
    public final DXFPoint getEXTMIN() {
        return EXTMIN;
    }
    
    /**
       Fast zoom enabled if nonzero.
       @return fast zoom enabled if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F57025F
     */
    public final int getFASTZOOM() {
        return FASTZOOM;
    }
    
    /**
       Возвращает радиус сопряжения.
       @return радиус сопряжения.
       @since TCL DXF 1.0.0
       @roseuid 37132F570260
     */
    public final double getFILLETRAD() {
        return FILLETRAD;
    }
    
    /**
       Режим закраски (0 = OFF, 1 = ON).
       @return режим закраски.
       @since TCL DXF 1.0.0
       @roseuid 37132F570261
     */
    public final int getFILLMODE() {
        return FILLMODE;
    }
    
    /**
       Force compatibility with older versions if nonzero.
       @return force compatibility with older versions if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F570262
     */
    public final int getFLATLAND() {
        return FLATLAND;
    }
    
    /**
       Отображение координатной сетки (0 = OFF, 1 = ON).
       @return Отображение координатной сетки (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F570263
     */
    public final int getGRIDMODE() {
        return GRIDMODE;
    }
    
    /**
       Возвращает шаг координатной сетки по оси x и y.
       @return шаг координатной сетки по оси x и y.
       @since TCL DXF 1.0.0
       @roseuid 37132F570264
     */
    public final DXFPoint getGRIDUNIT() {
        return GRIDUNIT;
    }
    
    /**
       Handles enabled if nonzero.
       @return handles enabled if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F570294
     */
    public final int getHANDLING() {
        return HANDLING;
    }
    
    /**
       Next available handle.
       @return next available handle.
       @since TCL DXF 1.0.0
       @roseuid 37132F570295
     */
    public final String getHANDSEED() {
        return HANDSEED;
    }
    
    /**
       Возвращает базовую точку вставки (x, y, z).
       @return базовая точка вставки (x, y, z).
       @since TCL DXF 1.0.0
       @roseuid 37132F570296
     */
    public final DXFPoint getINSBASE() {
        return INSBASE;
    }
    
    /**
       Контроль границ чертежа (0 = OFF, 1 = ON).
       @return контроль границ чертежа (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F570297
     */
    public final int getLIMCHECK() {
        return LIMCHECK;
    }
    
    /**
       Возвращает границы чертежа (координата верхнего правого угла).
       @return границы чертежа (координата верхнего правого угла).
       @since TCL DXF 1.0.0
       @roseuid 37132F570298
     */
    public final DXFPoint getLIMMAX() {
        return LIMMAX;
    }
    
    /**
       Возвращает границы чертежа (координата нижнего левого угла).
       @return границы чертежа (координата нижнего левого угла).
       @since TCL DXF 1.0.0
       @roseuid 37132F570299
     */
    public final DXFPoint getLIMMIN() {
        return LIMMIN;
    }
    
    /**
       Возвращает линейный масштаб для типов линии.
       @return линейный масштаб для типов линии.
       @since TCL DXF 1.0.0
       @roseuid 37132F57029A
     */
    public final double getLTSCALE() {
        return LTSCALE;
    }
    
    /**
       Возвращает формат представления координат и расстояний.
       1 = Научный (экспотенциальный).
       2 = Десятичный.
       3 = Технический.
       4 = Архитектурный.
       @return формат представления координат и расстояний.
       @since TCL DXF 1.0.0
       @roseuid 37132F57029B
     */
    public final int getLUNITS() {
        return LUNITS;
    }
    
    /**
       Возвращает точность представления координат и расстояний
       (число знаков в представлении дробной части значения).
       @return точность представления координат и расстояний.
       @since TCL DXF 1.0.0
       @roseuid 37132F57029C
     */
    public final int getLUPREC() {
        return LUPREC;
    }
    
    /**
       Возвращает имя файла текущего меню.
       @return имя файла текущего меню.
       @since TCL DXF 1.0.0
       @roseuid 37132F57029D
     */
    public final String getMENU() {
        return MENU;
    }
    
    /**
       MIRROR text if nonzero.
       @return MIRROR text if nonzero.
       @since TCL DXF 1.0.0
       @roseuid 37132F57029E
     */
    public final int getMIRRTEXT() {
        return MIRRTEXT;
    }
    
    /**
       Режим "ортогонального" рисования (0 = OFF, 1 = ON).
       @return режим "ортогонального" рисования (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F57029F
     */
    public final int getORTHOMODE() {
        return ORTHOMODE;
    }
    
    /**
       Возвращает режим нахождения заданной точки объекта.
       @return режим нахождения заданной точки объекта.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702C6
     */
    public final int getOSMODE() {
        return OSMODE;
    }
    
    /**
       Возвращает режим отображения точки.
       @return режим отображения точки.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702C7
     */
    public final int getPDMODE() {
        return PDMODE;
    }
    
    /**
       Возвращает размер точки на экране.
       @return размер точки на экране.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702C8
     */
    public final double getPDSIZE() {
        return PDSIZE;
    }
    
    /**
       Возвращает текущую (по умолчанию) ширину полилинии.
       @return текущая (по умолчанию) ширина полилинии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702C9
     */
    public final double getPLINEWID() {
        return PLINEWID;
    }
    
    /**
       Режим быстрого отбражения текста (0 = OFF, 1 = ON).
       @return режим быстрого отбражения текста (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CA
     */
    public final int getQTEXTMODE() {
        return QTEXTMODE;
    }
    
    /**
       Режим автоматической регенерации (0 = OFF, 1 = ON).
       @return режим автоматической регенерации (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CB
     */
    public final int getREGENMODE() {
        return REGENMODE;
    }
    
    /**
       Возвращает текущее приращение при ручной прорисовке.
       @return текущее приращение при ручной прорисовке.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CC
     */
    public final double getSKETCHINC() {
        return SKETCHINC;
    }
    
    /**
       0=sketch lines, 1=sketch polylines.
       @return 0=sketch lines, 1=sketch polylines.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CD
     */
    public final int getSKPOLY() {
        return SKPOLY;
    }
    
    /**
       Возвращает угол поворота сетки привязки.
       @return угол поворота сетки привязки.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CE
     */
    public final double getSNAPANG() {
        return SNAPANG;
    }
    
    /**
       Возвращает базовую точку привязки.
       @return базовая точка привязки.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702CF
     */
    public final DXFPoint getSNAPBASE() {
        return SNAPBASE;
    }
    
    /**
       Возвращает вид в изометрии.
       0 = Слева.
       1 = Сверху.
       2 = Справа.
       @return вид в изометрии.
       @since TCL DXF 1.0.0
       @roseuid 37132F5702D0
     */
    public final int getSNAPISOPAIR() {
        return SNAPISOPAIR;
    }
    
    /**
       Режим установки разрешающей способности (0 = OFF, 1 = ON).
       @return режим установки разрешающей способности (0 = OFF, 1 = ON).
       @since TCL DXF 1.0.0
       @roseuid 37132F5702D1
     */
    public final int getSNAPMODE() {
        return SNAPMODE;
    }
    
    /**
       Возвращает тип сетки привязки.
       0 = Стандартная.
       1 = Изометрическая.
       @return тип сетки привязки.
       @since TCL DXF 1.0.0
       @roseuid 37132F570302
     */
    public final int getSNAPSTYLE() {
        return SNAPSTYLE;
    }
    
    /**
       Возвращает интервал разрешения по оси x и y.
       @return интервал разрешения по оси x и y.
       @since TCL DXF 1.0.0
       @roseuid 37132F570303
     */
    public final DXFPoint getSNAPUNIT() {
        return SNAPUNIT;
    }
    
    /**
       Spline control polygon display, 1 = on, 0 = off.
       @return spline control polygon display, 1 = on, 0 = off.
       @since TCL DXF 1.0.0
       @roseuid 37132F570304
     */
    public final int getSPLFRAME() {
        return SPLFRAME;
    }
    
    /**
       Number of line segments per spline patch.
       @return number of line segments per spline patch.
       @since TCL DXF 1.0.0
       @roseuid 37132F570305
     */
    public final int getSPLINESEGS() {
        return SPLINESEGS;
    }
    
    /**
       Spline curve type for "PEDIT Spline".
       @return spline curve type for "PEDIT Spline".
       @since TCL DXF 1.0.0
       @roseuid 37132F570306
     */
    public final int getSPLINETYPE() {
        return SPLINETYPE;
    }
    
    /**
       Number of mesh tabulations in first direction.
       @return number of mesh tabulations in first direction.
       @since TCL DXF 1.0.0
       @roseuid 37132F570307
     */
    public final int getSURFTAB1() {
        return SURFTAB1;
    }
    
    /**
       Number of mesh tabulations in second direction.
       @return number of mesh tabulations in second direction.
       @since TCL DXF 1.0.0
       @roseuid 37132F570308
     */
    public final int getSURFTAB2() {
        return SURFTAB2;
    }
    
    /**
       Surface type for "PEDIT Smooth".
       @return surface type for "PEDIT Smooth".
       @since TCL DXF 1.0.0
       @roseuid 37132F570309
     */
    public final int getSURFTYPE() {
        return SURFTYPE;
    }
    
    /**
       Surface density (for "PEDIT Smooth") in M direction.
       @return surface density (for "PEDIT Smooth") in M direction.
       @since TCL DXF 1.0.0
       @roseuid 37132F57030A
     */
    public final int getSURFU() {
        return SURFU;
    }
    
    /**
       Surface density (for "PEDIT Smooth") in N direction.
       @return surface density (for "PEDIT Smooth") in N direction.
       @since TCL DXF 1.0.0
       @roseuid 37132F57030B
     */
    public final int getSURFV() {
        return SURFV;
    }
    
    /**
       Date/time of drawing creation.
       @return date/time of drawing creation.
       @since TCL DXF 1.0.0
       @roseuid 37132F570334
     */
    public final double getTDCREATE() {
        return TDCREATE;
    }
    
    /**
       Cumulative editing time for this drawing.
       @return cumulative editing time for this drawing.
       @since TCL DXF 1.0.0
       @roseuid 37132F570335
     */
    public final double getTDINDWG() {
        return TDINDWG;
    }
    
    /**
       Date/time of last drawing update.
       @return date/time of last drawing update.
       @since TCL DXF 1.0.0
       @roseuid 37132F570336
     */
    public final double getTDUPDATE() {
        return TDUPDATE;
    }
    
    /**
       User elapsed timer.
       @return user elapsed timer.
       @since TCL DXF 1.0.0
       @roseuid 37132F570337
     */
    public final double getTDUSRTIMER() {
        return TDUSRTIMER;
    }
    
    /**
       Возвращает текущую (по умолчанию) высоту текста.
       @return текущая (по умолчанию) высота текста.
       @since TCL DXF 1.0.0
       @roseuid 37132F570338
     */
    public final double getTEXTSIZE() {
        return TEXTSIZE;
    }
    
    /**
       Current text style name.
       @return current text style name.
       @since TCL DXF 1.0.0
       @roseuid 37132F570339
     */
    public final String getTEXTSTYLE() {
        return TEXTSTYLE;
    }
    
    /**
       Возвращает текущую высоту (толщину) объектов.
       @return текущая высота (толщина) объектов.
       @since TCL DXF 1.0.0
       @roseuid 37132F57033A
     */
    public final double getTHICKNESS() {
        return THICKNESS;
    }
    
    /**
       Возвращает текущую ширину полосы.
       @return текущая ширина полосы.
       @since TCL DXF 1.0.0
       @roseuid 37132F57033B
     */
    public final double getTRACEWID() {
        return TRACEWID;
    }
    
    /**
       Name of current UCS.
       @return name of current UCS.
       @since TCL DXF 1.0.0
       @roseuid 37132F57033C
     */
    public final String getUCSNAME() {
        return UCSNAME;
    }
    
    /**
       Origin of current UCS (in WCS).
       Возвращает .
       @return origin of current UCS (in WCS).
       @since TCL DXF 1.0.0
       @roseuid 37132F57033D
     */
    public final DXFPoint getUCSORG() {
        return UCSORG;
    }
    
    /**
       Direction of current UCS's X axis (in World coordinates).
       @return direction of current UCS's X axis (in World coordinates).
       @since TCL DXF 1.0.0
       @roseuid 37132F570370
     */
    public final DXFPoint getUCSXDIR() {
        return UCSXDIR;
    }
    
    /**
       Direction of current UCS's Y axis (in World coordinates).
       @return direction of current UCS's Y axis (in World coordinates).
       @since TCL DXF 1.0.0
       @roseuid 37132F570371
     */
    public final DXFPoint getUCSYDIR() {
        return UCSYDIR;
    }
    
    /**
       Возвращает целочисленной значение, заданное пользователем.
       @return целочисленной значение, заданное пользователем.
       @since TCL DXF 1.0.0
       @roseuid 37132F570372
     */
    public final int getUSERI1() {
        return USERI1;
    }
    
    /**
       Возвращает целочисленной значение, заданное пользователем.
       @return целочисленной значение, заданное пользователем.
       @since TCL DXF 1.0.0
       @roseuid 37132F570373
     */
    public final int getUSERI2() {
        return USERI2;
    }
    
    /**
       Возвращает целочисленной значение, заданное пользователем.
       @return целочисленной значение, заданное пользователем.
       @since TCL DXF 1.0.0
       @roseuid 37132F570374
     */
    public final int getUSERI3() {
        return USERI3;
    }
    
    /**
       Возвращает целочисленной значение, заданное пользователем.
       @return целочисленной значение, заданное пользователем.
       @since TCL DXF 1.0.0
       @roseuid 37132F570375
     */
    public final int getUSERI4() {
        return USERI4;
    }
    
    /**
       Возвращает целочисленной значение, заданное пользователем.
       @return целочисленной значение, заданное пользователем.
       @since TCL DXF 1.0.0
       @roseuid 37132F570376
     */
    public final int getUSERI5() {
        return USERI5;
    }
    
    /**
       Возвращает действительное значение, заданное пользователем.
       @return действительное значение, заданное пользователем.
       @since TCL DXF 1.0
       @roseuid 37132F570377
     */
    public final double getUSERR1() {
        return USERR1;
    }
    
    /**
       Возвращает действительное значение, заданное пользователем.
       @return действительное значение, заданное пользователем.
       @since TCL DXF 1.0
       @roseuid 37132F570378
     */
    public final double getUSERR2() {
        return USERR2;
    }
    
    /**
       Возвращает действительное значение, заданное пользователем.
       @return действительное значение, заданное пользователем.
       @since TCL DXF 1.0
       @roseuid 37132F570379
     */
    public final double getUSERR3() {
        return USERR3;
    }
    
    /**
       Возвращает действительное значение, заданное пользователем.
       @return действительное значение, заданное пользователем.
       @since TCL DXF 1.0
       @roseuid 37132F5703A2
     */
    public final double getUSERR4() {
        return USERR4;
    }
    
    /**
       Возвращает действительное значение, заданное пользователем.
       @return действительное значение, заданное пользователем.
       @since TCL DXF 1.0
       @roseuid 37132F5703A3
     */
    public final double getUSERR5() {
        return USERR5;
    }
    
    /**
       0=timer off, 1=timer on.
       @return 0=timer off, 1=timer on.
       @since TCL DXF 1.0
       @roseuid 37132F5703A4
     */
    public final int getUSRTIMER() {
        return USRTIMER;
    }
    
    /**
       Возвращает положение текущего вида (его центр) на экране.
       @return положение текущего вида (его центр) на экране.
       @since TCL DXF 1.0
       @roseuid 37132F5703A5
     */
    public final DXFPoint getVIEWCTR() {
        return VIEWCTR;
    }
    
    /**
       Возвращает текущую точку наблюдения.
       @return текущая точка наблюдения.
       @since TCL DXF 1.0
       @roseuid 37132F5703A6
     */
    public final DXFPoint getVIEWDIR() {
        return VIEWDIR;
    }
    
    /**
       Возвращает высоту текущего вида на экране.
       @return высота текущего вида на экране.
       @since TCL DXF 1.0
       @roseuid 37132F5703A7
     */
    public final double getVIEWSIZE() {
        return VIEWSIZE;
    }
    
    /**
       1=set UCS to WCS during DVIEW/VPOINT, 0=don't change UCS
       @return 1=set UCS to WCS during DVIEW/VPOINT, 0=don't change UCS
       @since TCL DXF 1.0
       @roseuid 37132F5703A8
     */
    public final int getWORLDVIEW() {
        return WORLDVIEW;
    }
    
    /**
       @roseuid 37132F5703A9
     */
    public void readDXF(DXFTokenizer tokenizer) throws IOException {
        while (true)
        {
            tokenizer.nextToken();

            if (tokenizer.getCode() == 0 && tokenizer.getKey() == DXFTokenizer.ENDSEC)
                return;

            if (tokenizer.getCode() == 9)
            {
                switch (tokenizer.getKey())
                {
                    case DXFTokenizer.ACADVER:
                    {
                        tokenizer.nextToken();
                        ACADVER = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.ANGBASE:
                    {
                        tokenizer.nextToken();
                        ANGBASE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.ANGDIR:
                    {
                        tokenizer.nextToken();
                        ANGDIR = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.ATTDIA:
                    {
                        tokenizer.nextToken();
                        ATTDIA = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.ATTMODE:
                    {
                        tokenizer.nextToken();
                        ATTMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.ATTREQ:
                    {
                        tokenizer.nextToken();
                        ATTREQ = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.AUNITS:
                    {
                        tokenizer.nextToken();
                        AUNITS = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.AUPREC:
                    {
                        tokenizer.nextToken();
                        AUPREC = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.AXISMODE:
                    {
                        tokenizer.nextToken();
                        AXISMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.AXISUNIT:
                    {
                        AXISUNIT = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.BLIPMODE:
                    {
                        tokenizer.nextToken();
                        BLIPMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.CECOLOR:
                    {
                        tokenizer.nextToken();
                        CECOLOR = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.CELTYPE:
                    {
                        tokenizer.nextToken();
                        CELTYPE = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.CHAMFERA:
                    {
                        tokenizer.nextToken();
                        CHAMFERA = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.CHAMFERB:
                    {
                        tokenizer.nextToken();
                        CHAMFERB = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.CLAYER:
                    {
                        tokenizer.nextToken();
                        CLAYER = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.COORDS:
                    {
                        tokenizer.nextToken();
                        COORDS = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMALT:
                    {
                        tokenizer.nextToken();
                        DIMALT = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMALTD:
                    {
                        tokenizer.nextToken();
                        DIMALTD = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMALTF:
                    {
                        tokenizer.nextToken();
                        DIMALTF = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMAPOST:
                    {
                        tokenizer.nextToken();
                        DIMAPOST = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.DIMASO:
                    {
                        tokenizer.nextToken();
                        DIMASO = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMASZ:
                    {
                        tokenizer.nextToken();
                        DIMASZ = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMBLK:
                    {
                        tokenizer.nextToken();
                        DIMBLK = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.DIMBLK1:
                    {
                        tokenizer.nextToken();
                        DIMBLK1 = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.DIMBLK2:
                    {
                        tokenizer.nextToken();
                        DIMBLK2 = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.DIMCEN:
                    {
                        tokenizer.nextToken();
                        DIMCEN = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMDLE:
                    {
                        tokenizer.nextToken();
                        DIMDLE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMDLI:
                    {
                        tokenizer.nextToken();
                        DIMDLI = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMEXE:
                    {
                        tokenizer.nextToken();
                        DIMEXE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMEXO:
                    {
                        tokenizer.nextToken();
                        DIMEXO = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMLFAC:
                    {
                        tokenizer.nextToken();
                        DIMLFAC = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMLIM:
                    {
                        tokenizer.nextToken();
                        DIMLIM = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMPOST:
                    {
                        tokenizer.nextToken();
                        DIMPOST = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.DIMRND:
                    {
                        tokenizer.nextToken();
                        DIMRND = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMSAH:
                    {
                        tokenizer.nextToken();
                        DIMSAH = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMSCALE:
                    {
                        tokenizer.nextToken();
                        DIMSCALE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMSE1:
                    {
                        tokenizer.nextToken();
                        DIMSE1 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMSE2:
                    {
                        tokenizer.nextToken();
                        DIMSE2 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMSHO:
                    {
                        tokenizer.nextToken();
                        DIMSHO = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMSOXD:
                    {
                        tokenizer.nextToken();
                        DIMSOXD = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTAD:
                    {
                        tokenizer.nextToken();
                        DIMTAD = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTIH:
                    {
                        tokenizer.nextToken();
                        DIMTIH = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTIX:
                    {
                        tokenizer.nextToken();
                        DIMTIX = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTM:
                    {
                        tokenizer.nextToken();
                        DIMTM = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMTOFL:
                    {
                        tokenizer.nextToken();
                        DIMTOFL = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTOH:
                    {
                        tokenizer.nextToken();
                        DIMTOH = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTOL:
                    {
                        tokenizer.nextToken();
                        DIMTOL = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DIMTP:
                    {
                        tokenizer.nextToken();
                        DIMTP = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMTSZ:
                    {
                        tokenizer.nextToken();
                        DIMTSZ = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMTVP:
                    {
                        tokenizer.nextToken();
                        DIMTVP = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMTXT:
                    {
                        tokenizer.nextToken();
                        DIMTXT = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.DIMZIN:
                    {
                        tokenizer.nextToken();
                        DIMZIN = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.DRAGMODE:
                    {
                        tokenizer.nextToken();
                        DRAGMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.ELEVATION:
                    {
                        tokenizer.nextToken();
                        ELEVATION = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.EXTMAX:
                    {
                        EXTMAX = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.EXTMIN:
                    {
                        EXTMIN = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.FILLETRAD:
                    {
                        tokenizer.nextToken();
                        FILLETRAD = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.FILLMODE:
                    {
                        tokenizer.nextToken();
                        FILLMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.FLATLAND:
                    {
                        tokenizer.nextToken();
                        FLATLAND = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.GRIDMODE:
                    {
                        tokenizer.nextToken();
                        GRIDMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.GRIDUNIT:
                    {
                        GRIDUNIT = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.HANDLING:
                    {
                        tokenizer.nextToken();
                        HANDLING = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.HANDSEED:
                    {
                        tokenizer.nextToken();
                        HANDSEED = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.INSBASE:
                    {
                        INSBASE = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.LIMCHECK:
                    {
                        tokenizer.nextToken();
                        LIMCHECK = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.LIMMAX:
                    {
                        LIMMAX = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.LIMMIN:
                    {
                        LIMMIN = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.LTSCALE:
                    {
                        tokenizer.nextToken();
                        LTSCALE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.LUNITS:
                    {
                        tokenizer.nextToken();
                        LUNITS = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.LUPREC:
                    {
                        tokenizer.nextToken();
                        LUPREC = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.MENU:
                    {
                        tokenizer.nextToken();
                        MENU = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.MIRRTEXT:
                    {
                        tokenizer.nextToken();
                        MIRRTEXT = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.ORTHOMODE:
                    {
                        tokenizer.nextToken();
                        ORTHOMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.OSMODE:
                    {
                        tokenizer.nextToken();
                        OSMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.PDMODE:
                    {
                        tokenizer.nextToken();
                        PDMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.PDSIZE:
                    {
                        tokenizer.nextToken();
                        PDSIZE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.PLINEWID:
                    {
                        tokenizer.nextToken();
                        PLINEWID = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.QTEXTMODE:
                    {
                        tokenizer.nextToken();
                        QTEXTMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.REGENMODE:
                    {
                        tokenizer.nextToken();
                        REGENMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SKETCHINC:
                    {
                        tokenizer.nextToken();
                        SKETCHINC = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.SKPOLY:
                    {
                        tokenizer.nextToken();
                        SKPOLY = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SNAPANG:
                    {
                        tokenizer.nextToken();
                        SNAPANG = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.SNAPBASE:
                    {
                        SNAPBASE = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.SNAPISOPAIR:
                    {
                        tokenizer.nextToken();
                        SNAPISOPAIR = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SNAPMODE:
                    {
                        tokenizer.nextToken();
                        SNAPMODE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SNAPSTYLE:
                    {
                        tokenizer.nextToken();
                        SNAPSTYLE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SNAPUNIT:
                    {
                        SNAPUNIT = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.SPLFRAME:
                    {
                        tokenizer.nextToken();
                        SPLFRAME = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SPLINESEGS:
                    {
                        tokenizer.nextToken();
                        SPLINESEGS = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SPLINETYPE:
                    {
                        tokenizer.nextToken();
                        SPLINETYPE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SURFTAB1:
                    {
                        tokenizer.nextToken();
                        SURFTAB1 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SURFTAB2:
                    {
                        tokenizer.nextToken();
                        SURFTAB2 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SURFTYPE:
                    {
                        tokenizer.nextToken();
                        SURFTYPE = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SURFU:
                    {
                        tokenizer.nextToken();
                        SURFU = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.SURFV:
                    {
                        tokenizer.nextToken();
                        SURFV = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.TDCREATE:
                    {
                        tokenizer.nextToken();
                        TDCREATE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TDINDWG:
                    {
                        tokenizer.nextToken();
                        TDINDWG = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TDUPDATE:
                    {
                        tokenizer.nextToken();
                        TDUPDATE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TDUSRTIMER:
                    {
                        tokenizer.nextToken();
                        TDUSRTIMER = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TEXTSIZE:
                    {
                        tokenizer.nextToken();
                        TEXTSIZE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TEXTSTYLE:
                    {
                        tokenizer.nextToken();
                        TEXTSTYLE = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.THICKNESS:
                    {
                        tokenizer.nextToken();
                        THICKNESS = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.TRACEWID:
                    {
                        tokenizer.nextToken();
                        TRACEWID = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.UCSNAME:
                    {
                        tokenizer.nextToken();
                        UCSNAME = tokenizer.getCommand();
                        break;
                    }
                    case DXFTokenizer.UCSORG:
                    {
                        UCSORG = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.UCSXDIR:
                    {
                        UCSXDIR = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.UCSYDIR:
                    {
                        UCSYDIR = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.USERI1:
                    {
                        tokenizer.nextToken();
                        USERI1 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.USERI2:
                    {
                        tokenizer.nextToken();
                        USERI2 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.USERI3:
                    {
                        tokenizer.nextToken();
                        USERI3 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.USERI4:
                    {
                        tokenizer.nextToken();
                        USERI4 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.USERI5:
                    {
                        tokenizer.nextToken();
                        USERI5 = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.USERR1:
                    {
                        tokenizer.nextToken();
                        USERR1 = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.USERR2:
                    {
                        tokenizer.nextToken();
                        USERR2 = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.USERR3:
                    {
                        tokenizer.nextToken();
                        USERR3 = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.USERR4:
                    {
                        tokenizer.nextToken();
                        USERR4 = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.USERR5:
                    {
                        tokenizer.nextToken();
                        USERR5 = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.USRTIMER:
                    {
                        tokenizer.nextToken();
                        USRTIMER = tokenizer.getCommandAsInt();
                        break;
                    }
                    case DXFTokenizer.VIEWCTR:
                    {
                        VIEWCTR = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.VIEWDIR:
                    {
                        VIEWDIR = readPoint(tokenizer);
                        break;
                    }
                    case DXFTokenizer.VIEWSIZE:
                    {
                        tokenizer.nextToken();
                        VIEWSIZE = tokenizer.getCommandAsDouble();
                        break;
                    }
                    case DXFTokenizer.WORLDVIEW:
                    {
                        tokenizer.nextToken();
                        WORLDVIEW = tokenizer.getCommandAsInt();
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
    
    /**
       Считывание точки из dxf файла.
       @param tokenizer входной поток
       @since TCL DXF 1.0
       @roseuid 37132F5703AB
     */
    private final DXFPoint readPoint(DXFTokenizer tokenizer) throws IOException {
        DXFPoint   point = new DXFPoint();
        boolean    end   = false;

        while (end == false)
        {
            tokenizer.nextToken();

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
                case 10:
                {
                    point.setX( tokenizer.getCommandAsDouble() );
                    break;
                }
                case 20:
                {
                    point.setY( tokenizer.getCommandAsDouble() );
                    break;
                }
                case 30:
                {
                    point.setZ( tokenizer.getCommandAsDouble() );
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

        return point;
    }
}
