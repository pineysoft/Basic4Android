package pineysoft.squarepaddocks;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class turn extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "pineysoft.squarepaddocks.turn");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            if (BA.isShellModeRuntimeCheck(ba)) {
			    ba.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.turn",
                    ba);
                return;
		    }
        }
        ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public pineysoft.squarepaddocks.gamesquare _square = null;
public int _edge = 0;
public int _playernum = 0;
public pineysoft.squarepaddocks.main _main = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim Square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 4;BA.debugLine="Dim Edge As Int";
_edge = 0;
 //BA.debugLineNum = 5;BA.debugLine="Dim PlayerNum As Int";
_playernum = 0;
 //BA.debugLineNum = 6;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,pineysoft.squarepaddocks.gamesquare _msquare,int _medge,int _mplayernum) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 9;BA.debugLine="Public Sub Initialize(mSquare As GameSquare, mEdge As Int, mPlayerNum As Int)";
 //BA.debugLineNum = 10;BA.debugLine="Square = mSquare";
_square = _msquare;
 //BA.debugLineNum = 11;BA.debugLine="Edge = mEdge";
_edge = _medge;
 //BA.debugLineNum = 12;BA.debugLine="PlayerNum = mPlayerNum";
_playernum = _mplayernum;
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
