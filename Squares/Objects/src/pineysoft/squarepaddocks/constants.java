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
public int _horizontal = 0;
public int _vertical = 0;
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
public String _game_msg_set_game_type = "";
public String _game_msg_set_settings = "";
public String _game_msg_process_turn = "";
public String _game_msg_start_game = "";
public String _game_msg_set_players = "";
public String _game_msg_close_game = "";
public String _game_msg_process_chat = "";
public String _game_msg_set_columns = "";
public String _game_msg_set_rows = "";
public String _game_msg_close_start = "";
public int _game_master = 0;
public int _game_slave = 0;
public int _bg_colour = 0;
public int _current_side_colour = 0;
public b4a.example.dateutils _dateutils = null;
public pineysoft.squarepaddocks.main _main = null;
public pineysoft.squarepaddocks.gameactivity _gameactivity = null;
public pineysoft.squarepaddocks.netconn _netconn = null;
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
_horizontal = 0;
 //BA.debugLineNum = 12;BA.debugLine="Dim VERTICAL As Int";
_vertical = 0;
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
 //BA.debugLineNum = 28;BA.debugLine="Dim GAME_MSG_SET_GAME_TYPE As String = \"M\"";
_game_msg_set_game_type = "M";
 //BA.debugLineNum = 29;BA.debugLine="Dim GAME_MSG_SET_SETTINGS As String = \"S\"";
_game_msg_set_settings = "S";
 //BA.debugLineNum = 30;BA.debugLine="Dim GAME_MSG_PROCESS_TURN As String = \"T\"";
_game_msg_process_turn = "T";
 //BA.debugLineNum = 31;BA.debugLine="Dim GAME_MSG_START_GAME As String = \"G\"";
_game_msg_start_game = "G";
 //BA.debugLineNum = 32;BA.debugLine="Dim GAME_MSG_SET_PLAYERS As String = \"P\"";
_game_msg_set_players = "P";
 //BA.debugLineNum = 33;BA.debugLine="Dim GAME_MSG_CLOSE_GAME As String = \"X\"";
_game_msg_close_game = "X";
 //BA.debugLineNum = 34;BA.debugLine="Dim GAME_MSG_PROCESS_CHAT As String = \"C\"";
_game_msg_process_chat = "C";
 //BA.debugLineNum = 35;BA.debugLine="Dim GAME_MSG_SET_COLUMNS As String = \"GC\"";
_game_msg_set_columns = "GC";
 //BA.debugLineNum = 36;BA.debugLine="Dim GAME_MSG_SET_ROWS As String = \"GR\"";
_game_msg_set_rows = "GR";
 //BA.debugLineNum = 37;BA.debugLine="Dim GAME_MSG_CLOSE_START As String = \"X2\"";
_game_msg_close_start = "X2";
 //BA.debugLineNum = 39;BA.debugLine="Dim GAME_MASTER As Int = 0";
_game_master = (int) (0);
 //BA.debugLineNum = 40;BA.debugLine="Dim GAME_SLAVE As Int = 1";
_game_slave = (int) (1);
 //BA.debugLineNum = 42;BA.debugLine="Dim BG_COLOUR As Int = Colors.RGB(30,144,255)";
_bg_colour = __c.Colors.RGB((int) (30),(int) (144),(int) (255));
 //BA.debugLineNum = 43;BA.debugLine="Dim CURRENT_SIDE_COLOUR As Int = Colors.Red";
_current_side_colour = __c.Colors.Red;
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 47;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 48;BA.debugLine="PLAYER_TYPE_HUMAN = 0";
_player_type_human = (int) (0);
 //BA.debugLineNum = 49;BA.debugLine="PLAYER_TYPE_DROID = 1";
_player_type_droid = (int) (1);
 //BA.debugLineNum = 51;BA.debugLine="TOP_SIDE = 0";
_top_side = (int) (0);
 //BA.debugLineNum = 52;BA.debugLine="RIGHT_SIDE = 1";
_right_side = (int) (1);
 //BA.debugLineNum = 53;BA.debugLine="BOTTOM_SIDE = 2";
_bottom_side = (int) (2);
 //BA.debugLineNum = 54;BA.debugLine="LEFT_SIDE = 3";
_left_side = (int) (3);
 //BA.debugLineNum = 56;BA.debugLine="SIDE_TAKEN = \"X\"";
_side_taken = BA.ObjectToChar("X");
 //BA.debugLineNum = 57;BA.debugLine="SIDE_AVAILABLE = \"O\"";
_side_available = BA.ObjectToChar("O");
 //BA.debugLineNum = 59;BA.debugLine="HORIZONTAL = 0";
_horizontal = (int) (0);
 //BA.debugLineNum = 60;BA.debugLine="VERTICAL = 1";
_vertical = (int) (1);
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
