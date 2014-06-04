package pineysoft.squarepaddocks;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class constants extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "pineysoft.squarepaddocks.constants");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            if (BA.isShellModeRuntimeCheck(ba)) {
			    ba.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.constants",
                    ba);
                return;
		    }
        }
        ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public int _player_type_human = 0;
public int _player_type_droid = 0;
public int _top_side = 0;
public int _right_side = 0;
public int _bottom_side = 0;
public int _left_side = 0;
public int _vvvv2 = 0;
public int _vvvv3 = 0;
public char _side_taken = '\0';
public char _side_available = '\0';
public int _checkbox_on = 0;
public int _checkbox_off = 0;
public String _difficulty_easy = "";
public String _difficulty_medium = "";
public String _difficulty_hard = "";
public String _gametype_mode_loc = "";
public String _gametype_mode_bt = "";
public String _gametype_mode_wifi = "";
public int _game_master = 0;
public int _game_slave = 0;
public int _bg_colour = 0;
public int _current_side_colour = 0;
public pineysoft.squarepaddocks.main _vvv2 = null;
public pineysoft.squarepaddocks.gameactivity _vvv3 = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim PLAYER_TYPE_HUMAN As Int";
_player_type_human = 0;
 //BA.debugLineNum = 4;BA.debugLine="Dim PLAYER_TYPE_DROID As Int";
_player_type_droid = 0;
 //BA.debugLineNum = 6;BA.debugLine="Dim TOP_SIDE As Int";
_top_side = 0;
 //BA.debugLineNum = 7;BA.debugLine="Dim RIGHT_SIDE As Int";
_right_side = 0;
 //BA.debugLineNum = 8;BA.debugLine="Dim BOTTOM_SIDE As Int";
_bottom_side = 0;
 //BA.debugLineNum = 9;BA.debugLine="Dim LEFT_SIDE As Int";
_left_side = 0;
 //BA.debugLineNum = 11;BA.debugLine="Dim HORIZONTAL As Int";
_vvvv2 = 0;
 //BA.debugLineNum = 12;BA.debugLine="Dim VERTICAL As Int";
_vvvv3 = 0;
 //BA.debugLineNum = 14;BA.debugLine="Dim SIDE_TAKEN As Char";
_side_taken = '\0';
 //BA.debugLineNum = 15;BA.debugLine="Dim SIDE_AVAILABLE As Char";
_side_available = '\0';
 //BA.debugLineNum = 17;BA.debugLine="Dim CHECKBOX_ON As Int = 0";
_checkbox_on = (int) (0);
 //BA.debugLineNum = 18;BA.debugLine="Dim CHECKBOX_OFF As Int = 1";
_checkbox_off = (int) (1);
 //BA.debugLineNum = 20;BA.debugLine="Dim DIFFICULTY_EASY As String = \"E\"";
_difficulty_easy = "E";
 //BA.debugLineNum = 21;BA.debugLine="Dim DIFFICULTY_MEDIUM As String = \"M\"";
_difficulty_medium = "M";
 //BA.debugLineNum = 22;BA.debugLine="Dim DIFFICULTY_HARD As String = \"H\"";
_difficulty_hard = "H";
 //BA.debugLineNum = 24;BA.debugLine="Dim GAMETYPE_MODE_LOC As String = \"L\"";
_gametype_mode_loc = "L";
 //BA.debugLineNum = 25;BA.debugLine="Dim GAMETYPE_MODE_BT As String = \"B\"";
_gametype_mode_bt = "B";
 //BA.debugLineNum = 26;BA.debugLine="Dim GAMETYPE_MODE_WIFI As String = \"W\"";
_gametype_mode_wifi = "W";
 //BA.debugLineNum = 28;BA.debugLine="Dim GAME_MASTER As Int = 0";
_game_master = (int) (0);
 //BA.debugLineNum = 29;BA.debugLine="Dim GAME_SLAVE As Int = 1";
_game_slave = (int) (1);
 //BA.debugLineNum = 31;BA.debugLine="Dim BG_COLOUR As Int = Colors.RGB(30,144,255)";
_bg_colour = __c.Colors.RGB((int) (30),(int) (144),(int) (255));
 //BA.debugLineNum = 32;BA.debugLine="Dim CURRENT_SIDE_COLOUR As Int = Colors.Red";
_current_side_colour = __c.Colors.Red;
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 36;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 37;BA.debugLine="PLAYER_TYPE_HUMAN = 0";
_player_type_human = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="PLAYER_TYPE_DROID = 1";
_player_type_droid = (int) (1);
 //BA.debugLineNum = 40;BA.debugLine="TOP_SIDE = 0";
_top_side = (int) (0);
 //BA.debugLineNum = 41;BA.debugLine="RIGHT_SIDE = 1";
_right_side = (int) (1);
 //BA.debugLineNum = 42;BA.debugLine="BOTTOM_SIDE = 2";
_bottom_side = (int) (2);
 //BA.debugLineNum = 43;BA.debugLine="LEFT_SIDE = 3";
_left_side = (int) (3);
 //BA.debugLineNum = 45;BA.debugLine="SIDE_TAKEN = \"X\"";
_side_taken = BA.ObjectToChar("X");
 //BA.debugLineNum = 46;BA.debugLine="SIDE_AVAILABLE = \"O\"";
_side_available = BA.ObjectToChar("O");
 //BA.debugLineNum = 48;BA.debugLine="HORIZONTAL = 0";
_vvvv2 = (int) (0);
 //BA.debugLineNum = 49;BA.debugLine="VERTICAL = 1";
_vvvv3 = (int) (1);
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
