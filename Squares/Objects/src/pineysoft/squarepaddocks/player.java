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
public String _vvv5 = "";
public int _vvv6 = 0;
public int _vvv7 = 0;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _vvv0 = null;
public int _vvvv1 = 0;
public pineysoft.squarepaddocks.main _vvv2 = null;
public pineysoft.squarepaddocks.gameactivity _vvv3 = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim Name As String";
_vvv5 = "";
 //BA.debugLineNum = 4;BA.debugLine="Dim Score As Int";
_vvv6 = 0;
 //BA.debugLineNum = 5;BA.debugLine="Dim Colour As Int";
_vvv7 = 0;
 //BA.debugLineNum = 6;BA.debugLine="Dim PlayerImage As Bitmap";
_vvv0 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Dim PlayerType As Int ' 0 for human - 1 for droid";
_vvvv1 = 0;
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,String _namein,int _colourin) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 11;BA.debugLine="Public Sub Initialize(NameIn As String, ColourIn As Int)";
 //BA.debugLineNum = 12;BA.debugLine="Name = NameIn";
_vvv5 = _namein;
 //BA.debugLineNum = 13;BA.debugLine="Colour = ColourIn";
_vvv7 = _colourin;
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public String  _vvv4(String _namein,int _colourin,int _ptype) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Public Sub Initialize2(NameIn As String, ColourIn As Int, PType As Int)";
 //BA.debugLineNum = 17;BA.debugLine="Name = NameIn";
_vvv5 = _namein;
 //BA.debugLineNum = 18;BA.debugLine="Colour = ColourIn";
_vvv7 = _colourin;
 //BA.debugLineNum = 19;BA.debugLine="PlayerType = PType";
_vvvv1 = _ptype;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
