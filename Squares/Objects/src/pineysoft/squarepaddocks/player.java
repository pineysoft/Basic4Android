package pineysoft.squarepaddocks;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class player extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "pineysoft.squarepaddocks.player");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            if (BA.isShellModeRuntimeCheck(ba)) {
			    ba.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.player",
                    ba);
                return;
		    }
        }
        ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _name = "";
public int _score = 0;
public int _colour = 0;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _playerimage = null;
public int _playerimagenum = 0;
public int _playertype = 0;
public b4a.example.dateutils _dateutils = null;
public pineysoft.squarepaddocks.main _main = null;
public pineysoft.squarepaddocks.gameactivity _gameactivity = null;
public pineysoft.squarepaddocks.netconn _netconn = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim Name As String";
_name = "";
 //BA.debugLineNum = 4;BA.debugLine="Dim Score As Int";
_score = 0;
 //BA.debugLineNum = 5;BA.debugLine="Dim Colour As Int";
_colour = 0;
 //BA.debugLineNum = 6;BA.debugLine="Dim PlayerImage As Bitmap";
_playerimage = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Dim PlayerImageNum As Int";
_playerimagenum = 0;
 //BA.debugLineNum = 8;BA.debugLine="Dim PlayerType As Int ' 0 for human - 1 for droid";
_playertype = 0;
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,String _namein,int _colourin) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 12;BA.debugLine="Public Sub Initialize(NameIn As String, ColourIn As Int)";
 //BA.debugLineNum = 13;BA.debugLine="Name = NameIn";
_name = _namein;
 //BA.debugLineNum = 14;BA.debugLine="Colour = ColourIn";
_colour = _colourin;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public String  _initialize2(String _namein,int _colourin,int _ptype) throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Public Sub Initialize2(NameIn As String, ColourIn As Int, PType As Int)";
 //BA.debugLineNum = 18;BA.debugLine="Name = NameIn";
_name = _namein;
 //BA.debugLineNum = 19;BA.debugLine="Colour = ColourIn";
_colour = _colourin;
 //BA.debugLineNum = 20;BA.debugLine="PlayerType = PType";
_playertype = _ptype;
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
