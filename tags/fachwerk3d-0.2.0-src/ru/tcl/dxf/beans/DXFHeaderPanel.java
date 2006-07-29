package ru.tcl.dxf.beans;

import java.awt.*;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.JPanel;                       // modified A.V. June 2004
import javax.swing.JScrollPane;                  // modified A.V. June 2004
import javax.swing.JTable;                       // modified A.V. June 2004
import javax.swing.table.AbstractTableModel;     // modified A.V. June 2004

import ru.tcl.dxf.DXFDocument;
import ru.tcl.dxf.DXFPoint;
import ru.tcl.dxf.header.Header;
import ru.tcl.dxf.beans.events.*;


public class DXFHeaderPanel extends JPanel implements Serializable, DXFDocumentListener{
    private BorderLayout borderLayout = new BorderLayout();
    private JTable table = new JTable();
    private JScrollPane scrollpane = JTable.createScrollPaneForTable(table);
    private DXFDocument document = null;

    public DXFHeaderPanel()
    {
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public DXFHeaderPanel(DXFDocument doc)
    {
        this();
        setDocument(doc);
    }

    private void jbInit() throws Exception
    {
        this.setLayout(borderLayout);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.add(scrollpane, BorderLayout.CENTER);
    }

    public void setDocument(DXFDocument newDocument)
    {
        document = newDocument;

        if (newDocument instanceof DXFComponent)
            ((DXFComponent )newDocument).addDXFDocumentListener(this);

        table.setModel(new DXFHeaderModel(document.getHeader()));
    }

    public DXFDocument getDocument()
    {
        return document;
    }

    public void documentChanged(DXFDocumentEvent e)
    {
        setDocument(e.getDocument());
    }
}

final class DXFHeaderModel extends AbstractTableModel
{
    private static String[] columnNames = new String[2];

    static
    {
        try
        {
            ResourceBundle res = ResourceBundle.getBundle("ru.tcl.dxf.beans.resources.beans");

            columnNames[0] = res.getString("DXFHeaderPanel.Table.Name");
            columnNames[1] = res.getString("DXFHeaderPanel.Table.Value");
        }
        catch (MissingResourceException e)
        {
            System.err.println("ru.tcl.dxf.beans.resources.beans not found");
            System.exit(1);
        }
    }

    private Header header;

    private DXFHeaderModel() {
        super();
    }

    public DXFHeaderModel(Header header) {
        this();
        this.header = header;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return 124;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        switch (col)
        {
            case 0:
            {
                switch (row)
                {
                    case 0: return "ANGBASE";
                    case 1: return "ANGDIR";
                    case 2: return "ATTDIA";
                    case 3: return "ATTMODE";
                    case 4: return "ATTREQ";
                    case 5: return "AUNITS";
                    case 6: return "AUPREC";
                    case 7: return "AXISMODE";
                    case 8: return "BLIPMODE";
                    case 9: return "CECOLOR";
                    case 10: return "CHAMFERA";
                    case 11: return "CHAMFERB";
                    case 12: return "COORDS";
                    case 13: return "DIMALT";
                    case 14: return "DIMALTD";
                    case 15: return "DIMALTF";
                    case 16: return "DIMASO";
                    case 17: return "DIMASZ";
                    case 18: return "DIMCEN";
                    case 19: return "DIMDLE";
                    case 20: return "DIMDLI";
                    case 21: return "DIMEXE";
                    case 22: return "DIMEXO";
                    case 23: return "DIMLFAC";
                    case 24: return "DIMLIM";
                    case 25: return "DIMRND";
                    case 26: return "DIMSAH";
                    case 27: return "DIMSCALE";
                    case 28: return "DIMSE1";
                    case 29: return "DIMSE2";
                    case 30: return "DIMSHO";
                    case 31: return "DIMSOXD";
                    case 32: return "DIMTAD";
                    case 33: return "DIMTIH";
                    case 34: return "DIMTIX";
                    case 35: return "DIMTM";
                    case 36: return "DIMTOFL";
                    case 37: return "DIMTOH";
                    case 38: return "DIMTOL";
                    case 39: return "DIMTP";
                    case 40: return "DIMTSZ";
                    case 41: return "DIMTVP";
                    case 42: return "DIMTXT";
                    case 43: return "DIMZIN";
                    case 44: return "DRAGMODE";
                    case 45: return "ELEVATION";
                    case 46: return "FASTZOOM";
                    case 47: return "FILLETRAD";
                    case 48: return "FILLMODE";
                    case 49: return "FLATLAND";
                    case 50: return "GRIDMODE";
                    case 51: return "HANDLING";
                    case 52: return "LIMCHECK";
                    case 53: return "LTSCALE";
                    case 54: return "LUNITS";
                    case 55: return "LUPREC";
                    case 56: return "MIRRTEXT";
                    case 57: return "ORTHOMODE";
                    case 58: return "OSMODE";
                    case 59: return "PDMODE";
                    case 60: return "PDSIZE";
                    case 61: return "PLINEWID";
                    case 62: return "QTEXTMODE";
                    case 63: return "REGENMODE";
                    case 64: return "SKETCHINC";
                    case 65: return "SKPOLY";
                    case 66: return "SNAPANG";
                    case 67: return "SNAPISOPAIR";
                    case 68: return "SNAPMODE";
                    case 69: return "SNAPSTYLE";
                    case 70: return "SPLFRAME";
                    case 71: return "SPLINESEGS";
                    case 72: return "SPLINETYPE";
                    case 73: return "SURFTAB1";
                    case 74: return "SURFTAB2";
                    case 75: return "SURFTYPE";
                    case 76: return "SURFU";
                    case 77: return "SURFV";
                    case 78: return "TDCREATE";
                    case 79: return "TDINDWG";
                    case 80: return "TDUPDATE";
                    case 81: return "TDUSRTIMER";
                    case 82: return "TEXTSIZE";
                    case 83: return "THICKNESS";
                    case 84: return "TRACEWID";
                    case 85: return "USERI1";
                    case 86: return "USERI2";
                    case 87: return "USERI3";
                    case 88: return "USERI4";
                    case 89: return "USERI5";
                    case 90: return "USERR1";
                    case 91: return "USERR2";
                    case 92: return "USERR3";
                    case 93: return "USERR4";
                    case 94: return "USERR5";
                    case 95: return "USRTIMER";
                    case 96: return "VIEWSIZE";
                    case 97: return "WORLDVIEW";
                    case 98: return "ACADVER";
                    case 99: return "AXISUNIT";
                    case 100: return "CELTYPE";
                    case 101: return "CLAYER";
                    case 102: return "DIMAPOST";
                    case 103: return "DIMBLK";
                    case 104: return "DIMBLK1";
                    case 105: return "DIMBLK2";
                    case 106: return "DIMPOST";
                    case 107: return "EXTMAX";
                    case 108: return "EXTMIN";
                    case 109: return "GRIDUNIT";
                    case 110: return "HANDSEED";
                    case 111: return "INSBASE";
                    case 112: return "LIMMAX";
                    case 113: return "LIMMIN";
                    case 114: return "MENU";
                    case 115: return "SNAPBASE";
                    case 116: return "SNAPUNIT";
                    case 117: return "TEXTSTYLE";
                    case 118: return "UCSNAME";
                    case 119: return "UCSORG";
                    case 120: return "UCSXDIR";
                    case 121: return "UCSYDIR";
                    case 122: return "VIEWCTR";
                    case 123: return "VIEWDIR";
                }
            }
            case 1:
            {
                switch (row)
                {
                    case 0: return new Double(header.getANGBASE());
                    case 1: return new Integer(header.getANGDIR());
                    case 2: return new Integer(header.getATTDIA());
                    case 3: return new Integer(header.getATTMODE());
                    case 4: return new Integer(header.getATTREQ());
                    case 5: return new Integer(header.getAUNITS());
                    case 6: return new Integer(header.getAUPREC());
                    case 7: return new Integer(header.getAXISMODE());
                    case 8: return new Integer(header.getBLIPMODE());
                    case 9: return new Integer(header.getCECOLOR());
                    case 10: return new Double(header.getCHAMFERA());
                    case 11: return new Double(header.getCHAMFERB());
                    case 12: return new Integer(header.getCOORDS());
                    case 13: return new Integer(header.getDIMALT());
                    case 14: return new Integer(header.getDIMALTD());
                    case 15: return new Double(header.getDIMALTF());
                    case 16: return new Integer(header.getDIMASO());
                    case 17: return new Double(header.getDIMASZ());
                    case 18: return new Double(header.getDIMCEN());
                    case 19: return new Double(header.getDIMDLE());
                    case 20: return new Double(header.getDIMDLI());
                    case 21: return new Double(header.getDIMEXE());
                    case 22: return new Double(header.getDIMEXO());
                    case 23: return new Double(header.getDIMLFAC());
                    case 24: return new Integer(header.getDIMLIM());
                    case 25: return new Double(header.getDIMRND());
                    case 26: return new Integer(header.getDIMSAH());
                    case 27: return new Double(header.getDIMSCALE());
                    case 28: return new Integer(header.getDIMSE1());
                    case 29: return new Integer(header.getDIMSE2());
                    case 30: return new Integer(header.getDIMSHO());
                    case 31: return new Integer(header.getDIMSOXD());
                    case 32: return new Integer(header.getDIMTAD());
                    case 33: return new Integer(header.getDIMTIH());
                    case 34: return new Integer(header.getDIMTIX());
                    case 35: return new Double(header.getDIMTM());
                    case 36: return new Integer(header.getDIMTOFL());
                    case 37: return new Integer(header.getDIMTOH());
                    case 38: return new Integer(header.getDIMTOL());
                    case 39: return new Double(header.getDIMTP());
                    case 40: return new Double(header.getDIMTSZ());
                    case 41: return new Double(header.getDIMTVP());
                    case 42: return new Double(header.getDIMTXT());
                    case 43: return new Integer(header.getDIMZIN());
                    case 44: return new Integer(header.getDRAGMODE());
                    case 45: return new Double(header.getELEVATION());
                    case 46: return new Integer(header.getFASTZOOM());
                    case 47: return new Double(header.getFILLETRAD());
                    case 48: return new Integer(header.getFILLMODE());
                    case 49: return new Integer(header.getFLATLAND());
                    case 50: return new Integer(header.getGRIDMODE());
                    case 51: return new Integer(header.getHANDLING());
                    case 52: return new Integer(header.getLIMCHECK());
                    case 53: return new Double(header.getLTSCALE());
                    case 54: return new Integer(header.getLUNITS());
                    case 55: return new Integer(header.getLUPREC());
                    case 56: return new Integer(header.getMIRRTEXT());
                    case 57: return new Integer(header.getORTHOMODE());
                    case 58: return new Integer(header.getOSMODE());
                    case 59: return new Integer(header.getPDMODE());
                    case 60: return new Double(header.getPDSIZE());
                    case 61: return new Double(header.getPLINEWID());
                    case 62: return new Integer(header.getQTEXTMODE());
                    case 63: return new Integer(header.getREGENMODE());
                    case 64: return new Double(header.getSKETCHINC());
                    case 65: return new Integer(header.getSKPOLY());
                    case 66: return new Double(header.getSNAPANG());
                    case 67: return new Integer(header.getSNAPISOPAIR());
                    case 68: return new Integer(header.getSNAPMODE());
                    case 69: return new Integer(header.getSNAPSTYLE());
                    case 70: return new Integer(header.getSPLFRAME());
                    case 71: return new Integer(header.getSPLINESEGS());
                    case 72: return new Integer(header.getSPLINETYPE());
                    case 73: return new Integer(header.getSURFTAB1());
                    case 74: return new Integer(header.getSURFTAB2());
                    case 75: return new Integer(header.getSURFTYPE());
                    case 76: return new Integer(header.getSURFU());
                    case 77: return new Integer(header.getSURFV());
                    case 78: return new Double(header.getTDCREATE());
                    case 79: return new Double(header.getTDINDWG());
                    case 80: return new Double(header.getTDUPDATE());
                    case 81: return new Double(header.getTDUSRTIMER());
                    case 82: return new Double(header.getTEXTSIZE());
                    case 83: return new Double(header.getTHICKNESS());
                    case 84: return new Double(header.getTRACEWID());
                    case 85: return new Integer(header.getUSERI1());
                    case 86: return new Integer(header.getUSERI2());
                    case 87: return new Integer(header.getUSERI3());
                    case 88: return new Integer(header.getUSERI4());
                    case 89: return new Integer(header.getUSERI5());
                    case 90: return new Double(header.getUSERR1());
                    case 91: return new Double(header.getUSERR2());
                    case 92: return new Double(header.getUSERR3());
                    case 93: return new Double(header.getUSERR4());
                    case 94: return new Double(header.getUSERR5());
                    case 95: return new Integer(header.getUSRTIMER());
                    case 96: return new Double(header.getVIEWSIZE());
                    case 97: return new Integer(header.getWORLDVIEW());
                    case 98: return header.getACADVER();
                    case 99: return header.getAXISUNIT().coords2dToString();
                    case 100: return header.getCELTYPE();
                    case 101: return header.getCLAYER();
                    case 102: return header.getDIMAPOST();
                    case 103: return header.getDIMBLK();
                    case 104: return header.getDIMBLK1();
                    case 105: return header.getDIMBLK2();
                    case 106: return header.getDIMPOST();
                    case 107: return header.getEXTMAX().coords2dToString();
                    case 108: return header.getEXTMIN().coords2dToString();
                    case 109: return header.getGRIDUNIT().coords2dToString();
                    case 110: return header.getHANDSEED();
                    case 111: return header.getINSBASE().coords2dToString();
                    case 112: return header.getLIMMAX().coords2dToString();
                    case 113: return header.getLIMMIN().coords2dToString();
                    case 114: return header.getMENU();
                    case 115: return header.getSNAPBASE().coords3dToString();
                    case 116: return header.getSNAPUNIT().coords3dToString();
                    case 117: return header.getTEXTSTYLE();
                    case 118: return header.getUCSNAME();
                    case 119: return header.getUCSORG().coords3dToString();
                    case 120: return header.getUCSXDIR().coords3dToString();
                    case 121: return header.getUCSYDIR().coords3dToString();
                    case 122: return header.getVIEWCTR().coords3dToString();
                    case 123: return header.getVIEWDIR().coords3dToString();
                }
            }
        }
        return "error!";
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

