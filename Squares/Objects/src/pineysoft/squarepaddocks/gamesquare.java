package pineysoft.squarepaddocks;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class gamesquare extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "pineysoft.squarepaddocks.gamesquare");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            if (BA.isShellModeRuntimeCheck(ba)) {
			    ba.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.gamesquare",
                    ba);
                return;
		    }
        }
        ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public int _colpos = 0;
public int _rowpos = 0;
public pineysoft.squarepaddocks.point _topleft = null;
public pineysoft.squarepaddocks.point _topright = null;
public pineysoft.squarepaddocks.point _bottomleft = null;
public pineysoft.squarepaddocks.point _bottomright = null;
public char[] _sides = null;
public boolean _occupied = false;
public pineysoft.squarepaddocks.constants _spconstants = null;
public int _sidestaken = 0;
public anywheresoftware.b4a.objects.LabelWrapper _filllabel = null;
public pineysoft.squarepaddocks.main _main = null;
public int  _calculateclosestedge(pineysoft.squarepaddocks.point _xypos) throws Exception{
double _deltax = 0;
double _deltay = 0;
double _deg = 0;
 //BA.debugLineNum = 31;BA.debugLine="Public Sub CalculateClosestEdge(xypos As Point) As Int";
 //BA.debugLineNum = 32;BA.debugLine="Dim deltax As Double";
_deltax = 0;
 //BA.debugLineNum = 33;BA.debugLine="Dim deltaY As Double";
_deltay = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim deg As Double";
_deg = 0;
 //BA.debugLineNum = 38;BA.debugLine="If xypos.Pos2 < TopLeft.Pos2 Then";
if (_xypos._pos2<_topleft._pos2) { 
 //BA.debugLineNum = 39;BA.debugLine="Return SpConstants.TOP_SIDE";
if (true) return _spconstants._top_side;
 }else if(_xypos._pos2>_bottomleft._pos2) { 
 //BA.debugLineNum = 41;BA.debugLine="Return SpConstants.BOTTOM_SIDE";
if (true) return _spconstants._bottom_side;
 };
 //BA.debugLineNum = 44;BA.debugLine="If xypos.Pos1 < TopLeft.Pos1 Then";
if (_xypos._pos1<_topleft._pos1) { 
 //BA.debugLineNum = 45;BA.debugLine="Return SpConstants.LEFT_SIDE";
if (true) return _spconstants._left_side;
 }else if(_xypos._pos1>_topright._pos1) { 
 //BA.debugLineNum = 47;BA.debugLine="Return SpConstants.RIGHT_SIDE";
if (true) return _spconstants._right_side;
 };
 //BA.debugLineNum = 51;BA.debugLine="If xypos.Pos1 < TopLeft.Pos1 + (TopRight.Pos1 - TopLeft.Pos1) / 2 Then";
if (_xypos._pos1<_topleft._pos1+(_topright._pos1-_topleft._pos1)/(double)2) { 
 //BA.debugLineNum = 52;BA.debugLine="If xypos.Pos2 < (TopLeft.Pos2 + (BottomLeft.Pos2 - TopLeft.Pos2) / 2) Then";
if (_xypos._pos2<(_topleft._pos2+(_bottomleft._pos2-_topleft._pos2)/(double)2)) { 
 //BA.debugLineNum = 53;BA.debugLine="deltax = TopLeft.Pos1 - xypos.Pos1";
_deltax = _topleft._pos1-_xypos._pos1;
 //BA.debugLineNum = 54;BA.debugLine="deltaY = TopLeft.Pos2 - xypos.Pos2";
_deltay = _topleft._pos2-_xypos._pos2;
 //BA.debugLineNum = 55;BA.debugLine="deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)";
_deg = __c.ATan2(_deltay,_deltax)*(180/(double)3.14159265359);
 //BA.debugLineNum = 56;BA.debugLine="Log(\"Top Left Deg = \" & deg)";
__c.Log("Top Left Deg = "+BA.NumberToString(_deg));
 //BA.debugLineNum = 57;BA.debugLine="If deg > -135 Then";
if (_deg>-135) { 
 //BA.debugLineNum = 58;BA.debugLine="Return SpConstants.LEFT_SIDE";
if (true) return _spconstants._left_side;
 }else {
 //BA.debugLineNum = 60;BA.debugLine="Return SpConstants.TOP_SIDE";
if (true) return _spconstants._top_side;
 };
 }else {
 //BA.debugLineNum = 63;BA.debugLine="deltax = BottomLeft.Pos1 - xypos.Pos1";
_deltax = _bottomleft._pos1-_xypos._pos1;
 //BA.debugLineNum = 64;BA.debugLine="deltaY = BottomLeft.Pos2 - xypos.Pos2";
_deltay = _bottomleft._pos2-_xypos._pos2;
 //BA.debugLineNum = 65;BA.debugLine="deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)";
_deg = __c.ATan2(_deltay,_deltax)*(180/(double)3.14159265359);
 //BA.debugLineNum = 66;BA.debugLine="Log(\"Bottom Left Deg = \" & deg)";
__c.Log("Bottom Left Deg = "+BA.NumberToString(_deg));
 //BA.debugLineNum = 67;BA.debugLine="If deg > 135 Then";
if (_deg>135) { 
 //BA.debugLineNum = 68;BA.debugLine="Return SpConstants.BOTTOM_SIDE";
if (true) return _spconstants._bottom_side;
 }else {
 //BA.debugLineNum = 70;BA.debugLine="Return SpConstants.LEFT_SIDE";
if (true) return _spconstants._left_side;
 };
 };
 }else {
 //BA.debugLineNum = 74;BA.debugLine="If xypos.Pos2 < (TopLeft.Pos2 + (BottomLeft.Pos2 - TopLeft.Pos2) / 2) Then";
if (_xypos._pos2<(_topleft._pos2+(_bottomleft._pos2-_topleft._pos2)/(double)2)) { 
 //BA.debugLineNum = 75;BA.debugLine="deltax = TopRight.Pos1 - xypos.Pos1";
_deltax = _topright._pos1-_xypos._pos1;
 //BA.debugLineNum = 76;BA.debugLine="deltaY = TopRight.Pos2 - xypos.Pos2";
_deltay = _topright._pos2-_xypos._pos2;
 //BA.debugLineNum = 77;BA.debugLine="deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)";
_deg = __c.ATan2(_deltay,_deltax)*(180/(double)3.14159265359);
 //BA.debugLineNum = 78;BA.debugLine="Log(\"Top Right Deg = \" & deg)";
__c.Log("Top Right Deg = "+BA.NumberToString(_deg));
 //BA.debugLineNum = 79;BA.debugLine="If deg < -45 Then";
if (_deg<-45) { 
 //BA.debugLineNum = 80;BA.debugLine="Return SpConstants.RIGHT_SIDE";
if (true) return _spconstants._right_side;
 }else {
 //BA.debugLineNum = 82;BA.debugLine="Return SpConstants.TOP_SIDE";
if (true) return _spconstants._top_side;
 };
 }else {
 //BA.debugLineNum = 85;BA.debugLine="deltax = BottomRight.Pos1 - xypos.Pos1";
_deltax = _bottomright._pos1-_xypos._pos1;
 //BA.debugLineNum = 86;BA.debugLine="deltaY = BottomRight.Pos2 - xypos.Pos2";
_deltay = _bottomright._pos2-_xypos._pos2;
 //BA.debugLineNum = 87;BA.debugLine="deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)";
_deg = __c.ATan2(_deltay,_deltax)*(180/(double)3.14159265359);
 //BA.debugLineNum = 88;BA.debugLine="Log(\"Bottom Right Deg = \" & deg)";
__c.Log("Bottom Right Deg = "+BA.NumberToString(_deg));
 //BA.debugLineNum = 89;BA.debugLine="If deg < 45 Then";
if (_deg<45) { 
 //BA.debugLineNum = 90;BA.debugLine="Return SpConstants.BOTTOM_SIDE";
if (true) return _spconstants._bottom_side;
 }else {
 //BA.debugLineNum = 92;BA.debugLine="Return SpConstants.RIGHT_SIDE";
if (true) return _spconstants._right_side;
 };
 };
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return 0;
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim ColPos As Int";
_colpos = 0;
 //BA.debugLineNum = 4;BA.debugLine="Dim RowPos As Int";
_rowpos = 0;
 //BA.debugLineNum = 5;BA.debugLine="Dim TopLeft As Point";
_topleft = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 6;BA.debugLine="Dim TopRight As Point";
_topright = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 7;BA.debugLine="Dim BottomLeft As Point";
_bottomleft = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 8;BA.debugLine="Dim BottomRight As Point";
_bottomright = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 9;BA.debugLine="Dim sides(4) As Char";
_sides = new char[(int) (4)];
;
 //BA.debugLineNum = 10;BA.debugLine="Dim Occupied As Boolean";
_occupied = false;
 //BA.debugLineNum = 11;BA.debugLine="Dim SpConstants As Constants";
_spconstants = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 12;BA.debugLine="Dim sidesTaken As Int";
_sidestaken = 0;
 //BA.debugLineNum = 13;BA.debugLine="Dim fillLabel As Label";
_filllabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public String  _drawedge(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,pineysoft.squarepaddocks.point _startpoint,pineysoft.squarepaddocks.point _endpoint,int _color,int _direction) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _line = null;
 //BA.debugLineNum = 139;BA.debugLine="Public Sub DrawEdge(cnv As Canvas, startPoint As Point, endPoint As Point, color As Int, direction As Int)";
 //BA.debugLineNum = 141;BA.debugLine="Dim line As Rect";
_line = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 142;BA.debugLine="If direction = SpConstants.VERTICAL Then";
if (_direction==_spconstants._vertical) { 
 //BA.debugLineNum = 143;BA.debugLine="line.Initialize(startPoint.Pos1-2dip, startPoint.Pos2+4dip, endPoint.Pos1+2dip, endPoint.Pos2-4dip)";
_line.Initialize((int) (_startpoint._pos1-__c.DipToCurrent((int) (2))),(int) (_startpoint._pos2+__c.DipToCurrent((int) (4))),(int) (_endpoint._pos1+__c.DipToCurrent((int) (2))),(int) (_endpoint._pos2-__c.DipToCurrent((int) (4))));
 }else {
 //BA.debugLineNum = 145;BA.debugLine="line.Initialize(startPoint.Pos1+4dip, startPoint.Pos2-2dip, endPoint.Pos1-4dip, endPoint.Pos2+2dip)";
_line.Initialize((int) (_startpoint._pos1+__c.DipToCurrent((int) (4))),(int) (_startpoint._pos2-__c.DipToCurrent((int) (2))),(int) (_endpoint._pos1-__c.DipToCurrent((int) (4))),(int) (_endpoint._pos2+__c.DipToCurrent((int) (2))));
 };
 //BA.debugLineNum = 148;BA.debugLine="cnv.DrawRect(line,color,True,2dip)";
_cnv.DrawRect((android.graphics.Rect)(_line.getObject()),_color,__c.True,(float) (__c.DipToCurrent((int) (2))));
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public String  _drawedge2(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,int _side,int _color) throws Exception{
pineysoft.squarepaddocks.point _startpoint = null;
pineysoft.squarepaddocks.point _endpoint = null;
int _direction = 0;
 //BA.debugLineNum = 150;BA.debugLine="Public Sub DrawEdge2(cnv As Canvas, side As Int, color As Int)";
 //BA.debugLineNum = 151;BA.debugLine="Dim startPoint As Point";
_startpoint = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 152;BA.debugLine="Dim endPoint As Point";
_endpoint = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 153;BA.debugLine="Dim direction As Int";
_direction = 0;
 //BA.debugLineNum = 154;BA.debugLine="Select side";
switch (BA.switchObjectToInt(_side,_spconstants._top_side,_spconstants._right_side,_spconstants._bottom_side,_spconstants._left_side)) {
case 0:
 //BA.debugLineNum = 156;BA.debugLine="startPoint = TopLeft";
_startpoint = _topleft;
 //BA.debugLineNum = 157;BA.debugLine="endPoint = TopRight";
_endpoint = _topright;
 //BA.debugLineNum = 158;BA.debugLine="direction = SpConstants.HORIZONTAL";
_direction = _spconstants._horizontal;
 break;
case 1:
 //BA.debugLineNum = 160;BA.debugLine="startPoint = TopRight";
_startpoint = _topright;
 //BA.debugLineNum = 161;BA.debugLine="endPoint = BottomRight";
_endpoint = _bottomright;
 //BA.debugLineNum = 162;BA.debugLine="direction = SpConstants.VERTICAL";
_direction = _spconstants._vertical;
 break;
case 2:
 //BA.debugLineNum = 164;BA.debugLine="startPoint = BottomLeft";
_startpoint = _bottomleft;
 //BA.debugLineNum = 165;BA.debugLine="endPoint = BottomRight";
_endpoint = _bottomright;
 //BA.debugLineNum = 166;BA.debugLine="direction = SpConstants.HORIZONTAL";
_direction = _spconstants._horizontal;
 break;
case 3:
 //BA.debugLineNum = 168;BA.debugLine="startPoint = TopLeft";
_startpoint = _topleft;
 //BA.debugLineNum = 169;BA.debugLine="endPoint = BottomLeft";
_endpoint = _bottomleft;
 //BA.debugLineNum = 170;BA.debugLine="direction = SpConstants.VERTICAL";
_direction = _spconstants._vertical;
 break;
}
;
 //BA.debugLineNum = 173;BA.debugLine="DrawEdge(cnv,startPoint, endPoint, color, direction)";
_drawedge(_cnv,_startpoint,_endpoint,_color,_direction);
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.collections.List  _getfreeedges(int _exclude) throws Exception{
anywheresoftware.b4a.objects.collections.List _freesides = null;
int _iloop = 0;
 //BA.debugLineNum = 123;BA.debugLine="Public Sub GetFreeEdges(exclude As Int) As List";
 //BA.debugLineNum = 124;BA.debugLine="Dim freeSides As List";
_freesides = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 125;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 127;BA.debugLine="freeSides.Initialize";
_freesides.Initialize();
 //BA.debugLineNum = 129;BA.debugLine="For iLoop = 0 To 3";
{
final int step108 = 1;
final int limit108 = (int) (3);
for (_iloop = (int) (0); (step108 > 0 && _iloop <= limit108) || (step108 < 0 && _iloop >= limit108); _iloop = ((int)(0 + _iloop + step108))) {
 //BA.debugLineNum = 130;BA.debugLine="If sides(iLoop) = \"O\" Then";
if (_sides[_iloop]==BA.ObjectToChar("O")) { 
 //BA.debugLineNum = 131;BA.debugLine="If iLoop <> exclude Then";
if (_iloop!=_exclude) { 
 //BA.debugLineNum = 132;BA.debugLine="freeSides.Add(iLoop)";
_freesides.Add((Object)(_iloop));
 };
 };
 }
};
 //BA.debugLineNum = 137;BA.debugLine="Return freeSides";
if (true) return _freesides;
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,int _x,int _y,int _width,int _height,int _row,int _col) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 16;BA.debugLine="Public Sub Initialize(x As Int, y As Int, width As Int, height As Int, row As Int, col As Int)";
 //BA.debugLineNum = 17;BA.debugLine="TopLeft.Initialize(x,y)";
_topleft._initialize(ba,_x,_y);
 //BA.debugLineNum = 18;BA.debugLine="TopRight.Initialize(x+width,y)";
_topright._initialize(ba,(int) (_x+_width),_y);
 //BA.debugLineNum = 19;BA.debugLine="BottomLeft.Initialize(x,y+height)";
_bottomleft._initialize(ba,_x,(int) (_y+_height));
 //BA.debugLineNum = 20;BA.debugLine="BottomRight.Initialize(x+width,y+height)";
_bottomright._initialize(ba,(int) (_x+_width),(int) (_y+_height));
 //BA.debugLineNum = 21;BA.debugLine="SpConstants.Initialize";
_spconstants._initialize(ba);
 //BA.debugLineNum = 22;BA.debugLine="ColPos = col";
_colpos = _col;
 //BA.debugLineNum = 23;BA.debugLine="RowPos = row";
_rowpos = _row;
 //BA.debugLineNum = 24;BA.debugLine="sides(0) = \"O\"";
_sides[(int) (0)] = BA.ObjectToChar("O");
 //BA.debugLineNum = 25;BA.debugLine="sides(1) = \"O\"";
_sides[(int) (1)] = BA.ObjectToChar("O");
 //BA.debugLineNum = 26;BA.debugLine="sides(2) = \"O\"";
_sides[(int) (2)] = BA.ObjectToChar("O");
 //BA.debugLineNum = 27;BA.debugLine="sides(3) = \"O\"";
_sides[(int) (3)] = BA.ObjectToChar("O");
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public boolean  _issidetaken(int _side) throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Public Sub IsSideTaken(side As Int) As Boolean";
 //BA.debugLineNum = 101;BA.debugLine="Return sides(side) = SpConstants.SIDE_TAKEN";
if (true) return _sides[_side]==_spconstants._side_taken;
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return false;
}
public String  _marksidetaken(int _side,boolean _marktaken) throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Public Sub MarkSideTaken(side As Int, markTaken As Boolean)";
 //BA.debugLineNum = 107;BA.debugLine="If markTaken Then";
if (_marktaken) { 
 //BA.debugLineNum = 108;BA.debugLine="If sides(side) =SpConstants.SIDE_AVAILABLE Then";
if (_sides[_side]==_spconstants._side_available) { 
 //BA.debugLineNum = 109;BA.debugLine="sides(side) = SpConstants.SIDE_TAKEN";
_sides[_side] = _spconstants._side_taken;
 //BA.debugLineNum = 110;BA.debugLine="sidesTaken = sidesTaken + 1";
_sidestaken = (int) (_sidestaken+1);
 };
 }else {
 //BA.debugLineNum = 113;BA.debugLine="sides(side) = SpConstants.SIDE_AVAILABLE";
_sides[_side] = _spconstants._side_available;
 //BA.debugLineNum = 114;BA.debugLine="sidesTaken = sidesTaken - 1";
_sidestaken = (int) (_sidestaken-1);
 };
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return "";
}
public String  _redrawside(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,int _side) throws Exception{
 //BA.debugLineNum = 181;BA.debugLine="Public Sub RedrawSide(cnv As Canvas, side As Int)";
 //BA.debugLineNum = 182;BA.debugLine="DrawEdge2(cnv, side, SpConstants.CURRENT_SIDE_COLOUR)";
_drawedge2(_cnv,_side,_spconstants._current_side_colour);
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public String  _removeside(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,int _side) throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Public Sub RemoveSide(cnv As Canvas, side As Int)";
 //BA.debugLineNum = 177;BA.debugLine="DrawEdge2(cnv, side, SpConstants.BG_COLOUR)";
_drawedge2(_cnv,_side,_spconstants._bg_colour);
 //BA.debugLineNum = 178;BA.debugLine="MarkSideTaken(side,False)";
_marksidetaken(_side,__c.False);
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public String  _takeside(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,int _side) throws Exception{
 //BA.debugLineNum = 118;BA.debugLine="Public Sub TakeSide(cnv As Canvas, side As Int)";
 //BA.debugLineNum = 119;BA.debugLine="DrawEdge2(cnv,side,Colors.Red)";
_drawedge2(_cnv,_side,__c.Colors.Red);
 //BA.debugLineNum = 120;BA.debugLine="MarkSideTaken(side,True)";
_marksidetaken(_side,__c.True);
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
