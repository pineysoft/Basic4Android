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
public String _vv6 = "";
public int _vv7 = 0;
public int _vv0 = 0;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _vvv1 = null;
public int _vvv2 = 0;
public pineysoft.squarepaddocks.main _vv4 = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim Name As String";
_vv6 = "";
 //BA.debugLineNum = 4;BA.debugLine="Dim Score As Int";
_vv7 = 0;
 //BA.debugLineNum = 5;BA.debugLine="Dim Colour As Int";
_vv0 = 0;
 //BA.debugLineNum = 6;BA.debugLine="Dim PlayerImage As Bitmap";
_vvv1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Dim PlayerType As Int ' 0 for human - 1 for droid";
_vvv2 = 0;
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,String _namein,int _colourin) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 11;BA.debugLine="Public Sub Initialize(NameIn As String, ColourIn As Int)";
 //BA.debugLineNum = 12;BA.debugLine="Name = NameIn";
_vv6 = _namein;
 //BA.debugLineNum = 13;BA.debugLine="Colour = ColourIn";
_vv0 = _colourin;
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public String  _vv5(String _namein,int _colourin,int _ptype) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Public Sub Initialize2(NameIn As String, ColourIn As Int, PType As Int)";
 //BA.debugLineNum = 17;BA.debugLine="Name = NameIn";
_vv6 = _namein;
 //BA.debugLineNum = 18;BA.debugLine="Colour = ColourIn";
_vv0 = _colourin;
 //BA.debugLineNum = 19;BA.debugLine="PlayerType = PType";
_vvv2 = _ptype;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
