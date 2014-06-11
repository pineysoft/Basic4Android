package pineysoft.squarepaddocks;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class gameactivity extends Activity implements B4AActivity{
	public static gameactivity mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "pineysoft.squarepaddocks", "pineysoft.squarepaddocks.gameactivity");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (gameactivity).");
				p.finish();
			}
		}
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "pineysoft.squarepaddocks", "pineysoft.squarepaddocks.gameactivity");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.gameactivity", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (gameactivity) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (gameactivity) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
		return true;
	}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return gameactivity.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (gameactivity) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (gameactivity) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public static short _currentplayer = (short)0;
public static int _numberofplayers = 0;
public static int _numberofdroids = 0;
public static anywheresoftware.b4a.objects.collections.List _playercolours = null;
public static boolean _ingame = false;
public static int _gigglesound = 0;
public static int _displayingdebug = 0;
public static anywheresoftware.b4a.audio.SoundPoolWrapper _sounds = null;
public static pineysoft.squarepaddocks.constants _spconstants = null;
public static String _difficulty = "";
public static anywheresoftware.b4a.phone.Phone.PhoneVibrate _vibrate = null;
public static boolean _soundson = false;
public static String _gamemode = "";
public static boolean _ismaster = false;
public static boolean _myturn = false;
public pineysoft.squarepaddocks.gamesquare[][] _gamesquares = null;
public anywheresoftware.b4a.objects.collections.List _gameturns = null;
public static int _gamewidth = 0;
public static int _gameheight = 0;
public static int _columnspacing = 0;
public static int _rowspacing = 0;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _canv = null;
public anywheresoftware.b4a.objects.collections.List _players = null;
public anywheresoftware.b4a.objects.collections.List _checkboximages = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer1image = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer2image = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer3image = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayer4image = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncontinue = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnplayers = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _sbcolumns = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _sbrows = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlstartscreen = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcolumns = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblplayers = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblrows = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlbase = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncurrplayer = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _icon6 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnldisplay = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlouter = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageicon = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblwinner = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim1 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim2 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim3 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim4 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim5 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _anim6 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _animshading = null;
public flm.b4a.animationplus.AnimationPlusWrapper _animpanel1 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _animpanel2 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _animstartscr = null;
public flm.b4a.animationplus.AnimationPlusWrapper _animgamescr = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spndroids = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblscores = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spndifficulty = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlselectionleft = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlselectionright = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlshading = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldifficulty = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldroids = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsound = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldebugdisplay = null;
public static int _start1 = 0;
public static int _start2 = 0;
public static int _start3 = 0;
public static int _start4 = 0;
public static int _start5 = 0;
public static int _start6 = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _chksounds = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgamemodedescr = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtmessage = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstartgame = null;
public b4a.example.dateutils _dateutils = null;
public pineysoft.squarepaddocks.main _main = null;
public pineysoft.squarepaddocks.netconn _netconn = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static class _mychain{
public boolean IsInitialized;
public pineysoft.squarepaddocks.gamesquare square;
public int chainCount;
public void Initialize() {
IsInitialized = true;
square = new pineysoft.squarepaddocks.gamesquare();
chainCount = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 103;BA.debugLine="SPConstants.Initialize";
_spconstants._initialize(processBA);
 //BA.debugLineNum = 104;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 105;BA.debugLine="Activity.LoadLayout(\"StartScreen\")";
mostCurrent._activity.LoadLayout("StartScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 106;BA.debugLine="If NetConn.BTStatus = \"Connected\" Then";
if ((mostCurrent._netconn._btstatus).equals("Connected")) { 
 //BA.debugLineNum = 107;BA.debugLine="GameMode = SPConstants.GAMETYPE_MODE_LOC";
_gamemode = _spconstants._gametype_mode_loc;
 };
 //BA.debugLineNum = 109;BA.debugLine="DisplayFrontScreen";
_displayfrontscreen();
 //BA.debugLineNum = 110;BA.debugLine="AnimateCharacters";
_animatecharacters();
 //BA.debugLineNum = 111;BA.debugLine="CreateColours";
_createcolours();
 //BA.debugLineNum = 112;BA.debugLine="LoadImages";
_loadimages();
 //BA.debugLineNum = 113;BA.debugLine="LoadSpinners";
_loadspinners();
 //BA.debugLineNum = 114;BA.debugLine="UpdateLabels";
_updatelabels();
 //BA.debugLineNum = 115;BA.debugLine="RestoreDefaults";
_restoredefaults();
 //BA.debugLineNum = 116;BA.debugLine="InitialiseSounds";
_initialisesounds();
 //BA.debugLineNum = 117;BA.debugLine="UpdateGameModeSettings";
_updategamemodesettings();
 }else {
 //BA.debugLineNum = 119;BA.debugLine="Activity.LoadLayout(\"StartScreen\")";
mostCurrent._activity.LoadLayout("StartScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 120;BA.debugLine="AnimateCharacters";
_animatecharacters();
 //BA.debugLineNum = 121;BA.debugLine="CreateColours";
_createcolours();
 //BA.debugLineNum = 122;BA.debugLine="LoadImages";
_loadimages();
 //BA.debugLineNum = 123;BA.debugLine="LoadSpinners";
_loadspinners();
 //BA.debugLineNum = 124;BA.debugLine="RestoreDefaults";
_restoredefaults();
 //BA.debugLineNum = 125;BA.debugLine="UpdateGameModeSettings";
_updategamemodesettings();
 };
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
 //BA.debugLineNum = 1543;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event";
 //BA.debugLineNum = 1544;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1545;BA.debugLine="If inGame Then";
if (_ingame) { 
 //BA.debugLineNum = 1546;BA.debugLine="inGame = False";
_ingame = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1547;BA.debugLine="If pnlOuter.Left = 0dip Then";
if (mostCurrent._pnlouter.getLeft()==anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0))) { 
 //BA.debugLineNum = 1548;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 1549;BA.debugLine="DisplayFrontScreen";
_displayfrontscreen();
 //BA.debugLineNum = 1550;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1552;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 1553;BA.debugLine="Answ = Msgbox2(\"Do you want to quit this game, you will lose all progress.\", _ 			      \"W A R N I N G\", \"Yes\", \"\", \"No\", Null)";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("Do you want to quit this game, you will lose all progress.","W A R N I N G","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 1555;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 1556;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1558;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 1559;BA.debugLine="SendMessage(SPConstants.GAME_MSG_CLOSE_GAME)";
_sendmessage(_spconstants._game_msg_close_game);
 };
 //BA.debugLineNum = 1561;BA.debugLine="CloseGame";
_closegame();
 //BA.debugLineNum = 1562;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 }else {
 //BA.debugLineNum = 1566;BA.debugLine="SendMessage(SPConstants.GAME_MSG_CLOSE_START)";
_sendmessage(_spconstants._game_msg_close_start);
 };
 };
 //BA.debugLineNum = 1570;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1571;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 654;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 656;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 650;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 652;BA.debugLine="End Sub";
return "";
}
public static String  _animatecharacters() throws Exception{
 //BA.debugLineNum = 327;BA.debugLine="Sub AnimateCharacters";
 //BA.debugLineNum = 328;BA.debugLine="ResetIcons";
_reseticons();
 //BA.debugLineNum = 329;BA.debugLine="anim1.InitializeTranslate(\"\", 0,0,75%x+(icon1.Width/2),25%y-start1)";
mostCurrent._anim1.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon1.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_start1));
 //BA.debugLineNum = 330;BA.debugLine="anim1.Duration = 1000";
mostCurrent._anim1.setDuration((long) (1000));
 //BA.debugLineNum = 331;BA.debugLine="anim1.StartOffset = 0";
mostCurrent._anim1.setStartOffset((long) (0));
 //BA.debugLineNum = 332;BA.debugLine="anim1.PersistAfter = False";
mostCurrent._anim1.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 333;BA.debugLine="anim1.Start(icon1)";
mostCurrent._anim1.Start((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 334;BA.debugLine="anim2.InitializeTranslate(\"\", 0,0,-(75%x+(icon2.width/2)),45%y-start2)";
mostCurrent._anim2.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon2.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_start2));
 //BA.debugLineNum = 335;BA.debugLine="anim2.Duration = 1000";
mostCurrent._anim2.setDuration((long) (1000));
 //BA.debugLineNum = 336;BA.debugLine="anim2.StartOffset = 0";
mostCurrent._anim2.setStartOffset((long) (0));
 //BA.debugLineNum = 337;BA.debugLine="anim2.PersistAfter = False";
mostCurrent._anim2.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 338;BA.debugLine="anim2.Start(icon2)";
mostCurrent._anim2.Start((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 339;BA.debugLine="anim3.InitializeTranslate(\"\", 0,0,50%x+(icon3.width/2),25%y-start3)";
mostCurrent._anim3.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon3.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_start3));
 //BA.debugLineNum = 340;BA.debugLine="anim3.Duration = 1000";
mostCurrent._anim3.setDuration((long) (1000));
 //BA.debugLineNum = 341;BA.debugLine="anim3.StartOffset = 0";
mostCurrent._anim3.setStartOffset((long) (0));
 //BA.debugLineNum = 342;BA.debugLine="anim3.PersistAfter = False";
mostCurrent._anim3.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="anim3.Start(icon3)";
mostCurrent._anim3.Start((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 344;BA.debugLine="anim4.InitializeTranslate(\"\", 0,0,-(50%x+(icon4.width/2)),45%y-start4)";
mostCurrent._anim4.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon4.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_start4));
 //BA.debugLineNum = 345;BA.debugLine="anim4.Duration = 1000";
mostCurrent._anim4.setDuration((long) (1000));
 //BA.debugLineNum = 346;BA.debugLine="anim4.StartOffset = 0";
mostCurrent._anim4.setStartOffset((long) (0));
 //BA.debugLineNum = 347;BA.debugLine="anim4.PersistAfter	 = False";
mostCurrent._anim4.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 348;BA.debugLine="anim4.Start(icon4)";
mostCurrent._anim4.Start((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 349;BA.debugLine="anim5.InitializeTranslate(\"\", 0,0,25%x+(icon5.width/2),25%y-start5)";
mostCurrent._anim5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon5.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_start5));
 //BA.debugLineNum = 350;BA.debugLine="anim5.Duration = 1000";
mostCurrent._anim5.setDuration((long) (1000));
 //BA.debugLineNum = 351;BA.debugLine="anim5.StartOffset = 0";
mostCurrent._anim5.setStartOffset((long) (0));
 //BA.debugLineNum = 352;BA.debugLine="anim5.PersistAfter = False";
mostCurrent._anim5.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 353;BA.debugLine="anim5.Start(icon5)";
mostCurrent._anim5.Start((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 354;BA.debugLine="anim6.InitializeTranslate(\"LastIcon\", 0,0,-(25%x+(icon6.width/2)),45%y-start6)";
mostCurrent._anim6.InitializeTranslate(mostCurrent.activityBA,"LastIcon",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon6.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_start6));
 //BA.debugLineNum = 355;BA.debugLine="anim6.Duration = 1000";
mostCurrent._anim6.setDuration((long) (1000));
 //BA.debugLineNum = 356;BA.debugLine="anim6.StartOffset = 0";
mostCurrent._anim6.setStartOffset((long) (0));
 //BA.debugLineNum = 357;BA.debugLine="anim6.PersistAfter = False";
mostCurrent._anim6.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 358;BA.debugLine="anim6.Start(icon6)";
mostCurrent._anim6.Start((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 360;BA.debugLine="End Sub";
return "";
}
public static String  _animategamescreens() throws Exception{
 //BA.debugLineNum = 400;BA.debugLine="Sub AnimateGameScreens";
 //BA.debugLineNum = 401;BA.debugLine="animStartScr.InitializeTranslate(\"\",0,0,-100%x,0)";
mostCurrent._animstartscr.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 402;BA.debugLine="animStartScr.Duration = 400";
mostCurrent._animstartscr.setDuration((long) (400));
 //BA.debugLineNum = 403;BA.debugLine="animStartScr.PersistAfter = True";
mostCurrent._animstartscr.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 404;BA.debugLine="animStartScr.Start(pnlStartScreen)";
mostCurrent._animstartscr.Start((android.view.View)(mostCurrent._pnlstartscreen.getObject()));
 //BA.debugLineNum = 406;BA.debugLine="AnimGameScr.InitializeTranslate(\"GameScr\",100%x,0,0,0)";
mostCurrent._animgamescr.InitializeTranslate(mostCurrent.activityBA,"GameScr",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0),(float) (0),(float) (0));
 //BA.debugLineNum = 407;BA.debugLine="AnimGameScr.Duration = 400";
mostCurrent._animgamescr.setDuration((long) (400));
 //BA.debugLineNum = 408;BA.debugLine="AnimGameScr.PersistAfter = True";
mostCurrent._animgamescr.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 409;BA.debugLine="AnimGameScr.Start(pnlBase)";
mostCurrent._animgamescr.Start((android.view.View)(mostCurrent._pnlbase.getObject()));
 //BA.debugLineNum = 410;BA.debugLine="End Sub";
return "";
}
public static String  _animatepanelleft() throws Exception{
 //BA.debugLineNum = 475;BA.debugLine="Sub AnimatePanelLeft";
 //BA.debugLineNum = 476;BA.debugLine="animPanel1.InitializeTranslate(\"LeftPanel\",-(pnlSelectionLeft.Width),0,pnlSelectionLeft.width + 5%x,0)";
mostCurrent._animpanel1.InitializeTranslate(mostCurrent.activityBA,"LeftPanel",(float) (-(mostCurrent._pnlselectionleft.getWidth())),(float) (0),(float) (mostCurrent._pnlselectionleft.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 477;BA.debugLine="animPanel1.PersistAfter = True";
mostCurrent._animpanel1.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 478;BA.debugLine="animPanel1.Duration = 500";
mostCurrent._animpanel1.setDuration((long) (500));
 //BA.debugLineNum = 479;BA.debugLine="animPanel1.Start(pnlSelectionLeft)";
mostCurrent._animpanel1.Start((android.view.View)(mostCurrent._pnlselectionleft.getObject()));
 //BA.debugLineNum = 480;BA.debugLine="End Sub";
return "";
}
public static String  _animatepanelright() throws Exception{
 //BA.debugLineNum = 486;BA.debugLine="Sub AnimatePanelRight";
 //BA.debugLineNum = 487;BA.debugLine="Log(100%x & \" \" & pnlSelectionRight.Width & \" \" & pnlSelectionRight.Top)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA))+" "+BA.NumberToString(mostCurrent._pnlselectionright.getWidth())+" "+BA.NumberToString(mostCurrent._pnlselectionright.getTop()));
 //BA.debugLineNum = 488;BA.debugLine="animPanel2.InitializeTranslate(\"RightPanel\",100%x,0,-45%x,0)";
mostCurrent._animpanel2.InitializeTranslate(mostCurrent.activityBA,"RightPanel",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0),(float) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (45),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 489;BA.debugLine="animPanel2.PersistAfter = True";
mostCurrent._animpanel2.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 490;BA.debugLine="animPanel2.Duration = 500";
mostCurrent._animpanel2.setDuration((long) (500));
 //BA.debugLineNum = 491;BA.debugLine="animPanel2.Start(pnlSelectionRight)";
mostCurrent._animpanel2.Start((android.view.View)(mostCurrent._pnlselectionright.getObject()));
 //BA.debugLineNum = 492;BA.debugLine="End Sub";
return "";
}
public static String  _animateshading() throws Exception{
 //BA.debugLineNum = 461;BA.debugLine="Sub AnimateShading";
 //BA.debugLineNum = 462;BA.debugLine="Log(\"StartShading Anim\")";
anywheresoftware.b4a.keywords.Common.Log("StartShading Anim");
 //BA.debugLineNum = 463;BA.debugLine="animShading.InitializeScaleCenter(\"Shading\", 0.1,0.1,1,1, pnlShading)";
mostCurrent._animshading.InitializeScaleCenter(mostCurrent.activityBA,"Shading",(float) (0.1),(float) (0.1),(float) (1),(float) (1),(android.view.View)(mostCurrent._pnlshading.getObject()));
 //BA.debugLineNum = 464;BA.debugLine="animShading.Duration = 750";
mostCurrent._animshading.setDuration((long) (750));
 //BA.debugLineNum = 465;BA.debugLine="animShading.PersistAfter = True";
mostCurrent._animshading.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 466;BA.debugLine="pnlShading.Visible = True";
mostCurrent._pnlshading.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 467;BA.debugLine="animShading.Start(pnlShading)";
mostCurrent._animshading.Start((android.view.View)(mostCurrent._pnlshading.getObject()));
 //BA.debugLineNum = 468;BA.debugLine="End Sub";
return "";
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 189;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 190;BA.debugLine="ToastMessageShow(\"Connection is broken.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Connection is broken.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 191;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _astream_newdata(byte[] _buffer) throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub AStream_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 186;BA.debugLine="ProcessStreamMessage(BytesToString(Buffer, 0, Buffer.Length, \"UTF8\"))";
_processstreammessage(anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"));
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 194;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 195;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 196;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinue_click() throws Exception{
String _msg = "";
 //BA.debugLineNum = 1512;BA.debugLine="Sub btnContinue_Click";
 //BA.debugLineNum = 1513;BA.debugLine="vibrate.vibrate(25)";
_vibrate.Vibrate(processBA,(long) (25));
 //BA.debugLineNum = 1514;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 1515;BA.debugLine="Dim msg As String = SPConstants.GAME_MSG_START_GAME";
_msg = _spconstants._game_msg_start_game;
 //BA.debugLineNum = 1516;BA.debugLine="SendMessage(msg)";
_sendmessage(_msg);
 //BA.debugLineNum = 1517;BA.debugLine="If IsMaster Then";
if (_ismaster) { 
 //BA.debugLineNum = 1518;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 };
 }else {
 //BA.debugLineNum = 1521;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1522;BA.debugLine="numberOfDroids = spnDroids.SelectedItem";
_numberofdroids = (int)(Double.parseDouble(mostCurrent._spndroids.getSelectedItem()));
 };
 //BA.debugLineNum = 1524;BA.debugLine="ReverseAnimate";
_reverseanimate();
 //BA.debugLineNum = 1525;BA.debugLine="End Sub";
return "";
}
public static String  _btncurrplayer_click() throws Exception{
int _rloop = 0;
int _cloop = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
int _iloop = 0;
 //BA.debugLineNum = 1683;BA.debugLine="Sub btnCurrPlayer_Click";
 //BA.debugLineNum = 1686;BA.debugLine="displayingDebug = displayingDebug + 1";
_displayingdebug = (int) (_displayingdebug+1);
 //BA.debugLineNum = 1688;BA.debugLine="If displayingDebug = 4 Then displayingDebug = 0";
if (_displayingdebug==4) { 
_displayingdebug = (int) (0);};
 //BA.debugLineNum = 1690;BA.debugLine="Dim rLoop As Int";
_rloop = 0;
 //BA.debugLineNum = 1691;BA.debugLine="Dim cLoop As Int";
_cloop = 0;
 //BA.debugLineNum = 1692;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1693;BA.debugLine="If displayingDebug = 3 Then";
if (_displayingdebug==3) { 
 //BA.debugLineNum = 1694;BA.debugLine="DisplayChainCounts";
_displaychaincounts();
 }else {
 //BA.debugLineNum = 1696;BA.debugLine="lblDebugDisplay.Left = 100%x";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1697;BA.debugLine="For rLoop = 0 To gameHeight - 1";
{
final int step1453 = 1;
final int limit1453 = (int) (_gameheight-1);
for (_rloop = (int) (0); (step1453 > 0 && _rloop <= limit1453) || (step1453 < 0 && _rloop >= limit1453); _rloop = ((int)(0 + _rloop + step1453))) {
 //BA.debugLineNum = 1698;BA.debugLine="For cLoop = 0 To gameWidth - 1";
{
final int step1454 = 1;
final int limit1454 = (int) (_gamewidth-1);
for (_cloop = (int) (0); (step1454 > 0 && _cloop <= limit1454) || (step1454 < 0 && _cloop >= limit1454); _cloop = ((int)(0 + _cloop + step1454))) {
 //BA.debugLineNum = 1699;BA.debugLine="square = gameSquares(rLoop, cLoop)";
_square = mostCurrent._gamesquares[_rloop][_cloop];
 //BA.debugLineNum = 1700;BA.debugLine="Select displayingDebug";
switch (_displayingdebug) {
case 0:
 //BA.debugLineNum = 1702;BA.debugLine="square.fillLabel.Text = \"\"";
_square._filllabel.setText((Object)(""));
 break;
case 1:
 //BA.debugLineNum = 1704;BA.debugLine="square.fillLabel.TextSize = 34";
_square._filllabel.setTextSize((float) (34));
 //BA.debugLineNum = 1705;BA.debugLine="square.fillLabel.Text = square.sidesTaken";
_square._filllabel.setText((Object)(_square._sidestaken));
 break;
case 2:
 //BA.debugLineNum = 1707;BA.debugLine="square.fillLabel.TextSize = 12";
_square._filllabel.setTextSize((float) (12));
 //BA.debugLineNum = 1708;BA.debugLine="square.fillLabel.Text = \"\"";
_square._filllabel.setText((Object)(""));
 //BA.debugLineNum = 1709;BA.debugLine="For iLoop = 0 To 3";
{
final int step1465 = 1;
final int limit1465 = (int) (3);
for (_iloop = (int) (0); (step1465 > 0 && _iloop <= limit1465) || (step1465 < 0 && _iloop >= limit1465); _iloop = ((int)(0 + _iloop + step1465))) {
 //BA.debugLineNum = 1710;BA.debugLine="square.fillLabel.Text = square.fillLabel.Text & square.sides(iLoop)";
_square._filllabel.setText((Object)(_square._filllabel.getText()+BA.ObjectToString(_square._sides[_iloop])));
 }
};
 break;
}
;
 }
};
 }
};
 };
 //BA.debugLineNum = 1717;BA.debugLine="End Sub";
return "";
}
public static String  _btnok_click() throws Exception{
 //BA.debugLineNum = 1719;BA.debugLine="Sub btnOk_Click";
 //BA.debugLineNum = 1720;BA.debugLine="SendMessage(SPConstants.GAME_MSG_CLOSE_GAME)";
_sendmessage(_spconstants._game_msg_close_game);
 //BA.debugLineNum = 1721;BA.debugLine="CloseGame";
_closegame();
 //BA.debugLineNum = 1722;BA.debugLine="End Sub";
return "";
}
public static boolean  _calculatemove(int _xcoord,int _ycoord) throws Exception{
pineysoft.squarepaddocks.point _touchpoint = null;
int _chosenside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
int _itemsclosed = 0;
String _move = "";
 //BA.debugLineNum = 717;BA.debugLine="Sub CalculateMove(xCoord As Int, yCoord As Int) As Boolean";
 //BA.debugLineNum = 720;BA.debugLine="Dim touchPoint As Point";
_touchpoint = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 721;BA.debugLine="Dim chosenSide As Int";
_chosenside = 0;
 //BA.debugLineNum = 722;BA.debugLine="touchPoint.Initialize(xCoord, yCoord)";
_touchpoint._initialize(processBA,_xcoord,_ycoord);
 //BA.debugLineNum = 723;BA.debugLine="Dim currentSquare As GameSquare = FindSelectedSquare(xCoord, yCoord)";
_currentsquare = _findselectedsquare(_xcoord,_ycoord);
 //BA.debugLineNum = 725;BA.debugLine="chosenSide = currentSquare.CalculateClosestEdge(touchPoint)";
_chosenside = _currentsquare._calculateclosestedge(_touchpoint);
 //BA.debugLineNum = 726;BA.debugLine="Log(chosenSide)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_chosenside));
 //BA.debugLineNum = 729;BA.debugLine="If currentSquare.IsSideTaken(chosenSide) Then Return False";
if (_currentsquare._issidetaken(_chosenside)) { 
if (true) return anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 731;BA.debugLine="Dim itemsClosed As Int = CalculateMove2(currentSquare, chosenSide)";
_itemsclosed = _calculatemove2(_currentsquare,_chosenside);
 //BA.debugLineNum = 733;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 734;BA.debugLine="Dim move As String = SPConstants.GAME_MSG_PROCESS_TURN & \",\" & itemsClosed & \",\" & currentSquare.RowPos & \",\" & currentSquare.ColPos & \",\" & chosenSide";
_move = _spconstants._game_msg_process_turn+","+BA.NumberToString(_itemsclosed)+","+BA.NumberToString(_currentsquare._rowpos)+","+BA.NumberToString(_currentsquare._colpos)+","+BA.NumberToString(_chosenside);
 //BA.debugLineNum = 735;BA.debugLine="SendMessage(move)";
_sendmessage(_move);
 }else {
 //BA.debugLineNum = 737;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 740;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 741;BA.debugLine="End Sub";
return false;
}
public static int  _calculatemove2(pineysoft.squarepaddocks.gamesquare _currentsquare,int _chosenside) throws Exception{
pineysoft.squarepaddocks.player _currplayer = null;
int _numberclosed = 0;
 //BA.debugLineNum = 743;BA.debugLine="Sub CalculateMove2(currentSquare As GameSquare, chosenSide As Int) As Int";
 //BA.debugLineNum = 744;BA.debugLine="Dim currPlayer As Player = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (_currentplayer)));
 //BA.debugLineNum = 746;BA.debugLine="UpdateTurn(canv, currentSquare, chosenSide)";
_updateturn(mostCurrent._canv,_currentsquare,_chosenside);
 //BA.debugLineNum = 749;BA.debugLine="currentSquare.TakeSide(canv,chosenSide)";
_currentsquare._takeside(mostCurrent._canv,_chosenside);
 //BA.debugLineNum = 752;BA.debugLine="MarkOtherSide2(currentSquare, chosenSide)";
_markotherside2(_currentsquare,_chosenside);
 //BA.debugLineNum = 755;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _closecompletedsquares(_currplayer);
 //BA.debugLineNum = 757;BA.debugLine="If numberClosed = 0 Then";
if (_numberclosed==0) { 
 //BA.debugLineNum = 758;BA.debugLine="UpdatePlayerNumber";
_updateplayernumber();
 //BA.debugLineNum = 759;BA.debugLine="currPlayer = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (_currentplayer)));
 //BA.debugLineNum = 760;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 //BA.debugLineNum = 761;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 762;BA.debugLine="Do While currPlayer.PlayerType = SPConstants.PLAYER_TYPE_DROID";
while (_currplayer._playertype==_spconstants._player_type_droid) {
 //BA.debugLineNum = 763;BA.debugLine="btnCurrPlayer.Text = \"D\"";
mostCurrent._btncurrplayer.setText((Object)("D"));
 //BA.debugLineNum = 764;BA.debugLine="MakeDroidMove(currPlayer)";
_makedroidmove(_currplayer);
 //BA.debugLineNum = 765;BA.debugLine="UpdatePlayerNumber";
_updateplayernumber();
 //BA.debugLineNum = 766;BA.debugLine="currPlayer = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (_currentplayer)));
 }
;
 //BA.debugLineNum = 768;BA.debugLine="btnCurrPlayer.Text = \"\"";
mostCurrent._btncurrplayer.setText((Object)(""));
 //BA.debugLineNum = 769;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 }else {
 //BA.debugLineNum = 771;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._score = (int) (_currplayer._score+_numberclosed);
 //BA.debugLineNum = 772;BA.debugLine="If soundsOn Then sounds.Play(giggleSound,1,1,1,1,0)";
if (_soundson) { 
_sounds.Play(_gigglesound,(float) (1),(float) (1),(int) (1),(int) (1),(float) (0));};
 //BA.debugLineNum = 773;BA.debugLine="If currPlayer.Score > 0 Then";
if (_currplayer._score>0) { 
 //BA.debugLineNum = 774;BA.debugLine="UpdateScore(currPlayer)";
_updatescore(_currplayer);
 };
 //BA.debugLineNum = 776;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 779;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 781;BA.debugLine="CheckAndDisplayWinnerScreen";
_checkanddisplaywinnerscreen();
 //BA.debugLineNum = 783;BA.debugLine="Return numberClosed";
if (true) return _numberclosed;
 //BA.debugLineNum = 785;BA.debugLine="End Sub";
return 0;
}
public static boolean  _checkanddisplaywinnerscreen() throws Exception{
int _iloop = 0;
String _scoretext = "";
pineysoft.squarepaddocks.player _temp = null;
int _winnernum = 0;
int _bestscore = 0;
int _totalscore = 0;
pineysoft.squarepaddocks.player _currplayer = null;
pineysoft.squarepaddocks.player _p = null;
 //BA.debugLineNum = 869;BA.debugLine="Sub CheckAndDisplayWinnerScreen() As Boolean";
 //BA.debugLineNum = 870;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 871;BA.debugLine="Dim scoreText As String";
_scoretext = "";
 //BA.debugLineNum = 872;BA.debugLine="Dim temp As Player";
_temp = new pineysoft.squarepaddocks.player();
 //BA.debugLineNum = 873;BA.debugLine="Dim winnerNum As Int";
_winnernum = 0;
 //BA.debugLineNum = 874;BA.debugLine="Dim bestScore As Int = 0";
_bestscore = (int) (0);
 //BA.debugLineNum = 875;BA.debugLine="Dim totalScore As Int = 0";
_totalscore = (int) (0);
 //BA.debugLineNum = 876;BA.debugLine="For iLoop = 0 To players.Size - 1";
{
final int step752 = 1;
final int limit752 = (int) (mostCurrent._players.getSize()-1);
for (_iloop = (int) (0); (step752 > 0 && _iloop <= limit752) || (step752 < 0 && _iloop >= limit752); _iloop = ((int)(0 + _iloop + step752))) {
 //BA.debugLineNum = 877;BA.debugLine="temp = players.Get(iLoop)";
_temp = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get(_iloop));
 //BA.debugLineNum = 878;BA.debugLine="totalScore = totalScore + temp.Score";
_totalscore = (int) (_totalscore+_temp._score);
 //BA.debugLineNum = 879;BA.debugLine="If temp.Score > bestScore Then";
if (_temp._score>_bestscore) { 
 //BA.debugLineNum = 880;BA.debugLine="winnerNum = iLoop";
_winnernum = _iloop;
 //BA.debugLineNum = 881;BA.debugLine="bestScore = temp.Score";
_bestscore = _temp._score;
 }else if(_temp._score==_bestscore) { 
 //BA.debugLineNum = 883;BA.debugLine="winnerNum = -1";
_winnernum = (int) (-1);
 };
 }
};
 //BA.debugLineNum = 886;BA.debugLine="If totalScore = gameWidth  * gameHeight Then";
if (_totalscore==_gamewidth*_gameheight) { 
 //BA.debugLineNum = 887;BA.debugLine="If pnlOuter.IsInitialized Then";
if (mostCurrent._pnlouter.IsInitialized()) { 
 //BA.debugLineNum = 888;BA.debugLine="If winnerNum <> -1 Then";
if (_winnernum!=-1) { 
 //BA.debugLineNum = 889;BA.debugLine="lblWinner.Text = \"Player \" & (winnerNum + 1) & \" is the winner!!!\"";
mostCurrent._lblwinner.setText((Object)("Player "+BA.NumberToString((_winnernum+1))+" is the winner!!!"));
 //BA.debugLineNum = 890;BA.debugLine="Dim currPlayer As Player = players.Get(winnerNum)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get(_winnernum));
 //BA.debugLineNum = 891;BA.debugLine="imageIcon.Bitmap = currPlayer.PlayerImage";
mostCurrent._imageicon.setBitmap((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 //BA.debugLineNum = 892;BA.debugLine="pnlOuter.Left = 0dip";
mostCurrent._pnlouter.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 893;BA.debugLine="imageIcon.Visible = True";
mostCurrent._imageicon.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 895;BA.debugLine="imageIcon.Visible = False";
mostCurrent._imageicon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 896;BA.debugLine="lblWinner.Text = \"IT'S A TIE\"";
mostCurrent._lblwinner.setText((Object)("IT'S A TIE"));
 //BA.debugLineNum = 897;BA.debugLine="pnlOuter.Left = 0dip";
mostCurrent._pnlouter.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 };
 };
 //BA.debugLineNum = 900;BA.debugLine="For iLoop = 1 To players.Size";
{
final int step776 = 1;
final int limit776 = mostCurrent._players.getSize();
for (_iloop = (int) (1); (step776 > 0 && _iloop <= limit776) || (step776 < 0 && _iloop >= limit776); _iloop = ((int)(0 + _iloop + step776))) {
 //BA.debugLineNum = 901;BA.debugLine="Dim P As Player = players.Get(iLoop - 1)";
_p = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (_iloop-1)));
 //BA.debugLineNum = 902;BA.debugLine="scoreText = scoreText & \"Player \" & iLoop & \": \" & P.Score & \" \"";
_scoretext = _scoretext+"Player "+BA.NumberToString(_iloop)+": "+BA.NumberToString(_p._score)+" ";
 }
};
 //BA.debugLineNum = 904;BA.debugLine="lblScores.Text = scoreText";
mostCurrent._lblscores.setText((Object)(_scoretext));
 //BA.debugLineNum = 905;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 907;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 909;BA.debugLine="End Sub";
return false;
}
public static String  _chksounds_click() throws Exception{
 //BA.debugLineNum = 1737;BA.debugLine="Sub chkSounds_Click";
 //BA.debugLineNum = 1738;BA.debugLine="If soundsOn Then";
if (_soundson) { 
 //BA.debugLineNum = 1739;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._checkboximages.Get(_spconstants._checkbox_off)));
 //BA.debugLineNum = 1740;BA.debugLine="soundsOn = False";
_soundson = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 1742;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._checkboximages.Get(_spconstants._checkbox_on)));
 //BA.debugLineNum = 1743;BA.debugLine="soundsOn = True";
_soundson = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1745;BA.debugLine="End Sub";
return "";
}
public static int  _closecompletedsquares(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.collections.List _allclosed = null;
int _closed = 0;
pineysoft.squarepaddocks.gamesquare _item = null;
 //BA.debugLineNum = 1370;BA.debugLine="Sub CloseCompletedSquares(currPlayer As Player) As Int";
 //BA.debugLineNum = 1371;BA.debugLine="Dim allClosed As List = FindAllForSides(4)";
_allclosed = new anywheresoftware.b4a.objects.collections.List();
_allclosed = _findallforsides((int) (4));
 //BA.debugLineNum = 1372;BA.debugLine="Dim closed As Int";
_closed = 0;
 //BA.debugLineNum = 1374;BA.debugLine="For Each item As GameSquare In allClosed";
final anywheresoftware.b4a.BA.IterableList group1175 = _allclosed;
final int groupLen1175 = group1175.getSize();
for (int index1175 = 0;index1175 < groupLen1175 ;index1175++){
_item = (pineysoft.squarepaddocks.gamesquare)(group1175.Get(index1175));
 //BA.debugLineNum = 1375;BA.debugLine="If item.Occupied = False Then";
if (_item._occupied==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1376;BA.debugLine="FillTheSquare(item, currPlayer)";
_fillthesquare(_item,_currplayer);
 //BA.debugLineNum = 1377;BA.debugLine="closed = closed + 1";
_closed = (int) (_closed+1);
 //BA.debugLineNum = 1378;BA.debugLine="item.Occupied = True";
_item._occupied = anywheresoftware.b4a.keywords.Common.True;
 };
 }
;
 //BA.debugLineNum = 1382;BA.debugLine="Return closed";
if (true) return _closed;
 //BA.debugLineNum = 1383;BA.debugLine="End Sub";
return 0;
}
public static String  _closegame() throws Exception{
 //BA.debugLineNum = 1573;BA.debugLine="Sub CloseGame()";
 //BA.debugLineNum = 1574;BA.debugLine="If pnlOuter.IsInitialized Then";
if (mostCurrent._pnlouter.IsInitialized()) { 
 //BA.debugLineNum = 1575;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 1576;BA.debugLine="DisplayFrontScreen";
_displayfrontscreen();
 //BA.debugLineNum = 1577;BA.debugLine="AnimateCharacters";
_animatecharacters();
 };
 //BA.debugLineNum = 1579;BA.debugLine="End Sub";
return "";
}
public static String  _closestartscreen() throws Exception{
 //BA.debugLineNum = 1581;BA.debugLine="Sub CloseStartScreen()";
 //BA.debugLineNum = 1582;BA.debugLine="ToastMessageShow(\"The other player has return to the start screen - connection is closed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("The other player has return to the start screen - connection is closed",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1583;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 1584;BA.debugLine="End Sub";
return "";
}
public static String  _createboard() throws Exception{
int _colloop = 0;
int _rowloop = 0;
int _x = 0;
int _y = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
anywheresoftware.b4a.objects.LabelWrapper _lblsquare = null;
 //BA.debugLineNum = 674;BA.debugLine="Sub CreateBoard";
 //BA.debugLineNum = 675;BA.debugLine="Dim colLoop As Int";
_colloop = 0;
 //BA.debugLineNum = 676;BA.debugLine="Dim rowLoop As Int";
_rowloop = 0;
 //BA.debugLineNum = 677;BA.debugLine="Dim x As Int = columnSpacing / 2";
_x = (int) (_columnspacing/(double)2);
 //BA.debugLineNum = 678;BA.debugLine="Dim y As Int = rowSpacing / 2";
_y = (int) (_rowspacing/(double)2);
 //BA.debugLineNum = 679;BA.debugLine="Dim gameSquares(gameHeight,gameWidth) As GameSquare";
mostCurrent._gamesquares = new pineysoft.squarepaddocks.gamesquare[_gameheight][];
{
int d0 = mostCurrent._gamesquares.length;
int d1 = _gamewidth;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._gamesquares[i0] = new pineysoft.squarepaddocks.gamesquare[d1];
for (int i1 = 0;i1 < d1;i1++) {
mostCurrent._gamesquares[i0][i1] = new pineysoft.squarepaddocks.gamesquare();
}
}
}
;
 //BA.debugLineNum = 681;BA.debugLine="For rowLoop = 0 To gameHeight - 1";
{
final int step596 = 1;
final int limit596 = (int) (_gameheight-1);
for (_rowloop = (int) (0); (step596 > 0 && _rowloop <= limit596) || (step596 < 0 && _rowloop >= limit596); _rowloop = ((int)(0 + _rowloop + step596))) {
 //BA.debugLineNum = 682;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step597 = 1;
final int limit597 = (int) (_gamewidth-1);
for (_colloop = (int) (0); (step597 > 0 && _colloop <= limit597) || (step597 < 0 && _colloop >= limit597); _colloop = ((int)(0 + _colloop + step597))) {
 //BA.debugLineNum = 683;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 684;BA.debugLine="Dim lblSquare As Label";
_lblsquare = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 685;BA.debugLine="lblSquare.Initialize(\"\")";
_lblsquare.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 686;BA.debugLine="lblSquare.Gravity = Gravity.FILL";
_lblsquare.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 687;BA.debugLine="square.Initialize(x,y,columnSpacing,rowSpacing,rowLoop,colLoop)";
_square._initialize(mostCurrent.activityBA,_x,_y,_columnspacing,_rowspacing,_rowloop,_colloop);
 //BA.debugLineNum = 688;BA.debugLine="square.fillLabel = lblSquare";
_square._filllabel = _lblsquare;
 //BA.debugLineNum = 689;BA.debugLine="panel1.AddView(lblSquare, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)";
mostCurrent._panel1.AddView((android.view.View)(_lblsquare.getObject()),(int) (_square._topleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._topleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_columnspacing-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(int) (_rowspacing-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 690;BA.debugLine="gameSquares(rowLoop, colLoop) = square";
mostCurrent._gamesquares[_rowloop][_colloop] = _square;
 //BA.debugLineNum = 691;BA.debugLine="x = x + columnSpacing";
_x = (int) (_x+_columnspacing);
 }
};
 //BA.debugLineNum = 693;BA.debugLine="x = columnSpacing / 2";
_x = (int) (_columnspacing/(double)2);
 //BA.debugLineNum = 694;BA.debugLine="y = y + rowSpacing";
_y = (int) (_y+_rowspacing);
 }
};
 //BA.debugLineNum = 696;BA.debugLine="End Sub";
return "";
}
public static String  _createcolours() throws Exception{
 //BA.debugLineNum = 505;BA.debugLine="Sub CreateColours()";
 //BA.debugLineNum = 506;BA.debugLine="playerColours.Initialize";
_playercolours.Initialize();
 //BA.debugLineNum = 507;BA.debugLine="playerColours.Add(Colors.Yellow)";
_playercolours.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Yellow));
 //BA.debugLineNum = 508;BA.debugLine="playerColours.Add(Colors.Blue)";
_playercolours.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Blue));
 //BA.debugLineNum = 509;BA.debugLine="playerColours.Add(Colors.Green)";
_playercolours.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Green));
 //BA.debugLineNum = 510;BA.debugLine="playerColours.Add(Colors.Red)";
_playercolours.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Red));
 //BA.debugLineNum = 511;BA.debugLine="End Sub";
return "";
}
public static String  _createplayers() throws Exception{
int _iloop = 0;
anywheresoftware.b4a.objects.collections.Map _imagestaken = null;
pineysoft.squarepaddocks.player _playerval = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _vimage = null;
anywheresoftware.b4a.objects.LabelWrapper _lblimage = null;
int _imagenum = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
pineysoft.squarepaddocks.player _currplayer = null;
 //BA.debugLineNum = 541;BA.debugLine="Sub CreatePlayers";
 //BA.debugLineNum = 542;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 543;BA.debugLine="players.Initialize";
mostCurrent._players.Initialize();
 //BA.debugLineNum = 544;BA.debugLine="Dim imagesTaken As Map";
_imagestaken = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 545;BA.debugLine="imagesTaken.Initialize";
_imagestaken.Initialize();
 //BA.debugLineNum = 546;BA.debugLine="For iLoop = 1 To numberOfPlayers";
{
final int step471 = 1;
final int limit471 = _numberofplayers;
for (_iloop = (int) (1); (step471 > 0 && _iloop <= limit471) || (step471 < 0 && _iloop >= limit471); _iloop = ((int)(0 + _iloop + step471))) {
 //BA.debugLineNum = 547;BA.debugLine="Dim playerVal As Player";
_playerval = new pineysoft.squarepaddocks.player();
 //BA.debugLineNum = 548;BA.debugLine="playerVal.Initialize(\"Player \" & iLoop, playerColours.Get(iLoop - 1))";
_playerval._initialize(processBA,"Player "+BA.NumberToString(_iloop),(int)(BA.ObjectToNumber(_playercolours.Get((int) (_iloop-1)))));
 //BA.debugLineNum = 549;BA.debugLine="If iLoop <= numberOfPlayers - numberOfDroids Then";
if (_iloop<=_numberofplayers-_numberofdroids) { 
 //BA.debugLineNum = 550;BA.debugLine="playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN";
_playerval._playertype = _spconstants._player_type_human;
 }else {
 //BA.debugLineNum = 552;BA.debugLine="playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID";
_playerval._playertype = _spconstants._player_type_droid;
 };
 //BA.debugLineNum = 554;BA.debugLine="Dim v As View = GetViewByTag1(pnlBase, \"P\" & iLoop)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_v = _getviewbytag1((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"P"+BA.NumberToString(_iloop));
 //BA.debugLineNum = 555;BA.debugLine="If v Is Label Then";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 556;BA.debugLine="Dim lbl As Label = v";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl.setObject((android.widget.TextView)(_v.getObject()));
 //BA.debugLineNum = 557;BA.debugLine="If playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then";
if (_playerval._playertype==_spconstants._player_type_human) { 
 //BA.debugLineNum = 558;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC  Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 559;BA.debugLine="If iLoop = 1 Then";
if (_iloop==1) { 
 //BA.debugLineNum = 560;BA.debugLine="If IsMaster Then";
if (_ismaster) { 
 //BA.debugLineNum = 561;BA.debugLine="lbl.Text = \"You\"";
_lbl.setText((Object)("You"));
 }else {
 //BA.debugLineNum = 563;BA.debugLine="lbl.Text = \"Them\"";
_lbl.setText((Object)("Them"));
 };
 }else {
 //BA.debugLineNum = 566;BA.debugLine="If IsMaster Then";
if (_ismaster) { 
 //BA.debugLineNum = 567;BA.debugLine="lbl.Text = \"Them\"";
_lbl.setText((Object)("Them"));
 }else {
 //BA.debugLineNum = 569;BA.debugLine="lbl.Text = \"You\"";
_lbl.setText((Object)("You"));
 };
 };
 }else {
 //BA.debugLineNum = 573;BA.debugLine="lbl.Text = \"Player \" & iLoop";
_lbl.setText((Object)("Player "+BA.NumberToString(_iloop)));
 };
 }else {
 //BA.debugLineNum = 576;BA.debugLine="lbl.Text = \"Droid \" & iLoop";
_lbl.setText((Object)("Droid "+BA.NumberToString(_iloop)));
 };
 //BA.debugLineNum = 578;BA.debugLine="If iLoop <= 11 Then";
if (_iloop<=11) { 
 //BA.debugLineNum = 579;BA.debugLine="Dim vImage As View = GetViewByTag1(pnlBase, \"I\" & iLoop)";
_vimage = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_vimage = _getviewbytag1((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"I"+BA.NumberToString(_iloop));
 //BA.debugLineNum = 580;BA.debugLine="If vImage Is Label Then";
if (_vimage.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 581;BA.debugLine="Dim lblImage As Label = vImage";
_lblimage = new anywheresoftware.b4a.objects.LabelWrapper();
_lblimage.setObject((android.widget.TextView)(_vimage.getObject()));
 //BA.debugLineNum = 582;BA.debugLine="Dim imageNum As Int = Rnd(0,11)";
_imagenum = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (11));
 //BA.debugLineNum = 583;BA.debugLine="Do While imagesTaken.ContainsKey(imageNum)";
while (_imagestaken.ContainsKey((Object)(_imagenum))) {
 //BA.debugLineNum = 584;BA.debugLine="imageNum = Rnd(0,11)";
_imagenum = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (11));
 }
;
 //BA.debugLineNum = 586;BA.debugLine="imagesTaken.Put(imageNum, imageNum)";
_imagestaken.Put((Object)(_imagenum),(Object)(_imagenum));
 //BA.debugLineNum = 587;BA.debugLine="Dim bm As Bitmap";
_bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 588;BA.debugLine="bm.Initialize(File.DirAssets, \"monsterImage\" & (imageNum + 1) & \".png\")";
_bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"monsterImage"+BA.NumberToString((_imagenum+1))+".png");
 //BA.debugLineNum = 589;BA.debugLine="playerVal.PlayerImage = bm 'playerImages.Get(imageNum)";
_playerval._playerimage = _bm;
 //BA.debugLineNum = 590;BA.debugLine="playerVal.PlayerImageNum = imageNum + 1";
_playerval._playerimagenum = (int) (_imagenum+1);
 //BA.debugLineNum = 591;BA.debugLine="lblImage.SetBackgroundImage(playerVal.PlayerImage)";
_lblimage.SetBackgroundImage((android.graphics.Bitmap)(_playerval._playerimage.getObject()));
 //BA.debugLineNum = 592;BA.debugLine="If playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID Then";
if (_playerval._playertype==_spconstants._player_type_droid) { 
 //BA.debugLineNum = 593;BA.debugLine="lblImage.Text = \"D\"";
_lblimage.setText((Object)("D"));
 };
 };
 };
 };
 //BA.debugLineNum = 598;BA.debugLine="players.Add(playerVal)";
mostCurrent._players.Add((Object)(_playerval));
 }
};
 //BA.debugLineNum = 600;BA.debugLine="currentPlayer = 0";
_currentplayer = (short) (0);
 //BA.debugLineNum = 601;BA.debugLine="Dim currPlayer As Player = players.get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (_currentplayer)));
 //BA.debugLineNum = 602;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 //BA.debugLineNum = 603;BA.debugLine="If IsMaster Then";
if (_ismaster) { 
 //BA.debugLineNum = 604;BA.debugLine="btnCurrPlayer.Text = \"M\"";
mostCurrent._btncurrplayer.setText((Object)("M"));
 }else {
 //BA.debugLineNum = 606;BA.debugLine="btnCurrPlayer.Text = \"S\"";
mostCurrent._btncurrplayer.setText((Object)("S"));
 };
 //BA.debugLineNum = 609;BA.debugLine="End Sub";
return "";
}
public static String  _displaychaincounts() throws Exception{
anywheresoftware.b4a.objects.collections.List _chains = null;
String _text = "";
pineysoft.squarepaddocks.gameactivity._mychain _item = null;
 //BA.debugLineNum = 1321;BA.debugLine="Public Sub DisplayChainCounts";
 //BA.debugLineNum = 1322;BA.debugLine="Dim chains As List = GetChainList";
_chains = new anywheresoftware.b4a.objects.collections.List();
_chains = _getchainlist();
 //BA.debugLineNum = 1323;BA.debugLine="Dim text As String";
_text = "";
 //BA.debugLineNum = 1324;BA.debugLine="For Each item As MyChain In chains";
final anywheresoftware.b4a.BA.IterableList group1133 = _chains;
final int groupLen1133 = group1133.getSize();
for (int index1133 = 0;index1133 < groupLen1133 ;index1133++){
_item = (pineysoft.squarepaddocks.gameactivity._mychain)(group1133.Get(index1133));
 //BA.debugLineNum = 1325;BA.debugLine="text = text & item.square.RowPos & \", \" & item.square.ColPos & \" : \" & item.chainCount & CRLF";
_text = _text+BA.NumberToString(_item.square._rowpos)+", "+BA.NumberToString(_item.square._colpos)+" : "+BA.NumberToString(_item.chainCount)+anywheresoftware.b4a.keywords.Common.CRLF;
 }
;
 //BA.debugLineNum = 1327;BA.debugLine="lblDebugDisplay.text = text";
mostCurrent._lbldebugdisplay.setText((Object)(_text));
 //BA.debugLineNum = 1328;BA.debugLine="lblDebugDisplay.Left = 0dip";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1329;BA.debugLine="lblDebugDisplay.Width = 100%x";
mostCurrent._lbldebugdisplay.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1330;BA.debugLine="lblDebugDisplay.Height = 100%y";
mostCurrent._lbldebugdisplay.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1331;BA.debugLine="lblDebugDisplay.Visible = True";
mostCurrent._lbldebugdisplay.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1332;BA.debugLine="End Sub";
return "";
}
public static String  _displayfrontscreen() throws Exception{
 //BA.debugLineNum = 1482;BA.debugLine="Sub DisplayFrontScreen";
 //BA.debugLineNum = 1483;BA.debugLine="If pnlBase.IsInitialized Then";
if (mostCurrent._pnlbase.IsInitialized()) { 
 //BA.debugLineNum = 1484;BA.debugLine="pnlBase.Left = 200%x";
mostCurrent._pnlbase.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (200),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 1486;BA.debugLine="pnlStartScreen.Left = 0dip";
mostCurrent._pnlstartscreen.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1487;BA.debugLine="inGame = False";
_ingame = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1488;BA.debugLine="End Sub";
return "";
}
public static String  _drawboard() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _square = null;
int _rowloop = 0;
int _colloop = 0;
pineysoft.squarepaddocks.gamesquare _gsquare = null;
 //BA.debugLineNum = 698;BA.debugLine="Sub DrawBoard";
 //BA.debugLineNum = 699;BA.debugLine="Dim square As Rect";
_square = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 700;BA.debugLine="For rowLoop = 0 To gameHeight - 1";
{
final int step614 = 1;
final int limit614 = (int) (_gameheight-1);
for (_rowloop = (int) (0); (step614 > 0 && _rowloop <= limit614) || (step614 < 0 && _rowloop >= limit614); _rowloop = ((int)(0 + _rowloop + step614))) {
 //BA.debugLineNum = 701;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step615 = 1;
final int limit615 = (int) (_gamewidth-1);
for (_colloop = (int) (0); (step615 > 0 && _colloop <= limit615) || (step615 < 0 && _colloop >= limit615); _colloop = ((int)(0 + _colloop + step615))) {
 //BA.debugLineNum = 702;BA.debugLine="Dim gSquare As GameSquare = gameSquares(rowLoop, colLoop)";
_gsquare = mostCurrent._gamesquares[_rowloop][_colloop];
 //BA.debugLineNum = 704;BA.debugLine="square.Initialize(gSquare.TopLeft.Pos1-4dip,gSquare.TopLeft.Pos2-4dip,gSquare.TopLeft.Pos1+4dip,gSquare.TopLeft.Pos2+4dip)";
_square.Initialize((int) (_gsquare._topleft._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topleft._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 705;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 706;BA.debugLine="square.Initialize(gSquare.TopRight.Pos1-4dip,gSquare.TopRight.Pos2-4dip,gSquare.TopRight.Pos1+4dip,gSquare.TopRight.Pos2+4dip)";
_square.Initialize((int) (_gsquare._topright._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topright._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topright._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._topright._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 707;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 708;BA.debugLine="square.Initialize(gSquare.BottomLeft.Pos1-4dip,gSquare.BottomLeft.Pos2-4dip,gSquare.BottomLeft.Pos1+4dip,gSquare.BottomLeft.Pos2+4dip)";
_square.Initialize((int) (_gsquare._bottomleft._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomleft._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 709;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 710;BA.debugLine="square.Initialize(gSquare.BottomRight.Pos1-4dip,gSquare.BottomRight.Pos2-4dip,gSquare.BottomRight.Pos1+4dip,gSquare.BottomRight.Pos2+4dip)";
_square.Initialize((int) (_gsquare._bottomright._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomright._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomright._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._bottomright._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 711;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 712;BA.debugLine="Log(\"Drawing \" & rowLoop & \", \" & colLoop)";
anywheresoftware.b4a.keywords.Common.Log("Drawing "+BA.NumberToString(_rowloop)+", "+BA.NumberToString(_colloop));
 }
};
 }
};
 //BA.debugLineNum = 715;BA.debugLine="End Sub";
return "";
}
public static String  _emptythesquare(pineysoft.squarepaddocks.gamesquare _square) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _fillrect = null;
 //BA.debugLineNum = 1405;BA.debugLine="Sub EmptyTheSquare(square As GameSquare)";
 //BA.debugLineNum = 1406;BA.debugLine="Dim fillRect As Rect";
_fillrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1408;BA.debugLine="If square.fillLabel.IsInitialized Then";
if (_square._filllabel.IsInitialized()) { 
 //BA.debugLineNum = 1409;BA.debugLine="square.fillLabel.SetBackgroundImage(Null)";
_square._filllabel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 }else {
 //BA.debugLineNum = 1411;BA.debugLine="fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.pos2  + 4dip, square.BottomRight.pos1  - 4dip, square.BottomRight.Pos2  - 4dip)";
_fillrect.Initialize((int) (_square._topleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._topleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._bottomright._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._bottomright._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 1412;BA.debugLine="canv.DrawRect(fillRect,SPConstants.BG_COLOUR,True,1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_fillrect.getObject()),_spconstants._bg_colour,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 };
 //BA.debugLineNum = 1414;BA.debugLine="End Sub";
return "";
}
public static String  _fillthesquare(pineysoft.squarepaddocks.gamesquare _square,pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _fillrect = null;
anywheresoftware.b4a.objects.LabelWrapper _lblnew = null;
 //BA.debugLineNum = 1385;BA.debugLine="Sub FillTheSquare(square As GameSquare, currPlayer As Player)";
 //BA.debugLineNum = 1386;BA.debugLine="Dim fillRect As Rect";
_fillrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1388;BA.debugLine="If currPlayer.PlayerImage.IsInitialized Then";
if (_currplayer._playerimage.IsInitialized()) { 
 //BA.debugLineNum = 1389;BA.debugLine="If square.fillLabel.IsInitialized Then";
if (_square._filllabel.IsInitialized()) { 
 //BA.debugLineNum = 1390;BA.debugLine="square.fillLabel.SetBackgroundImage(currPlayer.PlayerImage)";
_square._filllabel.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 }else {
 //BA.debugLineNum = 1392;BA.debugLine="Dim lblNew As Label";
_lblnew = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1393;BA.debugLine="lblNew.Initialize(\"\")";
_lblnew.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1394;BA.debugLine="lblNew.SetBackgroundImage(currPlayer.PlayerImage)";
_lblnew.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._playerimage.getObject()));
 //BA.debugLineNum = 1395;BA.debugLine="lblNew.Gravity = Gravity.FILL";
_lblnew.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 1396;BA.debugLine="square.fillLabel = lblNew";
_square._filllabel = _lblnew;
 //BA.debugLineNum = 1397;BA.debugLine="panel1.AddView(lblNew, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)";
mostCurrent._panel1.AddView((android.view.View)(_lblnew.getObject()),(int) (_square._topleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._topleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_columnspacing-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(int) (_rowspacing-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 };
 }else {
 //BA.debugLineNum = 1400;BA.debugLine="fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, square.BottomRight.Pos1  - 4dip, square.BottomRight.Pos2  - 4dip)";
_fillrect.Initialize((int) (_square._topleft._pos1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._topleft._pos2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._bottomright._pos1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._bottomright._pos2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 1401;BA.debugLine="canv.DrawRect(fillRect,currPlayer.colour,True,1dip)";
mostCurrent._canv.DrawRect((android.graphics.Rect)(_fillrect.getObject()),_currplayer._colour,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 };
 //BA.debugLineNum = 1403;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _findallforsides(int _checksides) throws Exception{
anywheresoftware.b4a.objects.collections.List _foundsquares = null;
int _row = 0;
int _col = 0;
 //BA.debugLineNum = 1468;BA.debugLine="Sub FindAllForSides(checkSides As Int) As List";
 //BA.debugLineNum = 1469;BA.debugLine="Dim foundSquares As List";
_foundsquares = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1470;BA.debugLine="foundSquares.Initialize";
_foundsquares.Initialize();
 //BA.debugLineNum = 1471;BA.debugLine="For row = 0 To gameHeight - 1";
{
final int step1257 = 1;
final int limit1257 = (int) (_gameheight-1);
for (_row = (int) (0); (step1257 > 0 && _row <= limit1257) || (step1257 < 0 && _row >= limit1257); _row = ((int)(0 + _row + step1257))) {
 //BA.debugLineNum = 1472;BA.debugLine="For col = 0 To gameWidth - 1";
{
final int step1258 = 1;
final int limit1258 = (int) (_gamewidth-1);
for (_col = (int) (0); (step1258 > 0 && _col <= limit1258) || (step1258 < 0 && _col >= limit1258); _col = ((int)(0 + _col + step1258))) {
 //BA.debugLineNum = 1473;BA.debugLine="If gameSquares(row,col).sidesTaken = checkSides Then";
if (mostCurrent._gamesquares[_row][_col]._sidestaken==_checksides) { 
 //BA.debugLineNum = 1474;BA.debugLine="foundSquares.Add(gameSquares(row,col))";
_foundsquares.Add((Object)(mostCurrent._gamesquares[_row][_col]));
 };
 }
};
 }
};
 //BA.debugLineNum = 1479;BA.debugLine="Return foundSquares";
if (true) return _foundsquares;
 //BA.debugLineNum = 1480;BA.debugLine="End Sub";
return null;
}
public static pineysoft.squarepaddocks.gamesquare  _findselectedsquare(int _xpos,int _ypos) throws Exception{
int _colloop = 0;
int _rowloop = 0;
int _foundrow = 0;
int _foundcol = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
 //BA.debugLineNum = 1416;BA.debugLine="Sub FindSelectedSquare(xpos As Int, ypos As Int) As GameSquare";
 //BA.debugLineNum = 1417;BA.debugLine="Dim colLoop As Int = 0";
_colloop = (int) (0);
 //BA.debugLineNum = 1418;BA.debugLine="Dim rowloop As Int = 0";
_rowloop = (int) (0);
 //BA.debugLineNum = 1419;BA.debugLine="Dim foundRow As Int = -1";
_foundrow = (int) (-1);
 //BA.debugLineNum = 1420;BA.debugLine="Dim foundCol As Int = -1";
_foundcol = (int) (-1);
 //BA.debugLineNum = 1421;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1423;BA.debugLine="For rowloop = 0 To gameHeight - 1";
{
final int step1217 = 1;
final int limit1217 = (int) (_gameheight-1);
for (_rowloop = (int) (0); (step1217 > 0 && _rowloop <= limit1217) || (step1217 < 0 && _rowloop >= limit1217); _rowloop = ((int)(0 + _rowloop + step1217))) {
 //BA.debugLineNum = 1424;BA.debugLine="square = gameSquares(rowloop, 0)";
_square = mostCurrent._gamesquares[_rowloop][(int) (0)];
 //BA.debugLineNum = 1425;BA.debugLine="If square.TopLeft.pos2 > ypos Then";
if (_square._topleft._pos2>_ypos) { 
 //BA.debugLineNum = 1426;BA.debugLine="If rowloop = 0 Then";
if (_rowloop==0) { 
 //BA.debugLineNum = 1427;BA.debugLine="foundRow = 0";
_foundrow = (int) (0);
 }else {
 //BA.debugLineNum = 1429;BA.debugLine="foundRow = rowloop - 1";
_foundrow = (int) (_rowloop-1);
 };
 //BA.debugLineNum = 1431;BA.debugLine="rowloop = gameHeight -1";
_rowloop = (int) (_gameheight-1);
 };
 }
};
 //BA.debugLineNum = 1435;BA.debugLine="If foundRow = -1 Then foundRow = gameHeight-1";
if (_foundrow==-1) { 
_foundrow = (int) (_gameheight-1);};
 //BA.debugLineNum = 1437;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step1229 = 1;
final int limit1229 = (int) (_gamewidth-1);
for (_colloop = (int) (0); (step1229 > 0 && _colloop <= limit1229) || (step1229 < 0 && _colloop >= limit1229); _colloop = ((int)(0 + _colloop + step1229))) {
 //BA.debugLineNum = 1438;BA.debugLine="square = gameSquares(foundRow, colLoop)";
_square = mostCurrent._gamesquares[_foundrow][_colloop];
 //BA.debugLineNum = 1439;BA.debugLine="If square.TopLeft.pos1 > xpos  Then";
if (_square._topleft._pos1>_xpos) { 
 //BA.debugLineNum = 1440;BA.debugLine="If colLoop = 0 Then";
if (_colloop==0) { 
 //BA.debugLineNum = 1441;BA.debugLine="foundCol = 0";
_foundcol = (int) (0);
 }else {
 //BA.debugLineNum = 1443;BA.debugLine="foundCol = colLoop - 1";
_foundcol = (int) (_colloop-1);
 };
 //BA.debugLineNum = 1445;BA.debugLine="colLoop = gameWidth - 1";
_colloop = (int) (_gamewidth-1);
 };
 }
};
 //BA.debugLineNum = 1449;BA.debugLine="If foundCol = -1 Then foundCol = gameWidth - 1";
if (_foundcol==-1) { 
_foundcol = (int) (_gamewidth-1);};
 //BA.debugLineNum = 1451;BA.debugLine="Return gameSquares(foundRow,foundCol)";
if (true) return mostCurrent._gamesquares[_foundrow][_foundcol];
 //BA.debugLineNum = 1452;BA.debugLine="End Sub";
return null;
}
public static String  _gamescr_animationend() throws Exception{
 //BA.debugLineNum = 412;BA.debugLine="Sub GameScr_AnimationEnd";
 //BA.debugLineNum = 413;BA.debugLine="animStartScr.Stop(pnlStartScreen)";
mostCurrent._animstartscr.Stop((android.view.View)(mostCurrent._pnlstartscreen.getObject()));
 //BA.debugLineNum = 414;BA.debugLine="pnlStartScreen.Left = -100%x";
mostCurrent._pnlstartscreen.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 415;BA.debugLine="AnimGameScr.Stop(pnlBase)";
mostCurrent._animgamescr.Stop((android.view.View)(mostCurrent._pnlbase.getObject()));
 //BA.debugLineNum = 416;BA.debugLine="pnlBase.Left = 0dip";
mostCurrent._pnlbase.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 417;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getchainlist() throws Exception{
anywheresoftware.b4a.objects.collections.List _found2sides = null;
int _colpos = 0;
int _rowpos = 0;
int _chaincounter = 0;
boolean _reachedend = false;
pineysoft.squarepaddocks.gamesquare _checksquare = null;
pineysoft.squarepaddocks.gamesquare _startingsquare = null;
anywheresoftware.b4a.objects.collections.List _chainlist = null;
int _edgeloop = 0;
anywheresoftware.b4a.objects.collections.Map _checkedsquares = null;
int _excludeedgenum = 0;
int _freeedge = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
anywheresoftware.b4a.objects.collections.List _freeedges = null;
pineysoft.squarepaddocks.gameactivity._mychain _newchain = null;
pineysoft.squarepaddocks.gameactivity._mychain _tempitem = null;
 //BA.debugLineNum = 1215;BA.debugLine="Public Sub GetChainList() As List";
 //BA.debugLineNum = 1216;BA.debugLine="Dim found2Sides As List = FindAllForSides(2)";
_found2sides = new anywheresoftware.b4a.objects.collections.List();
_found2sides = _findallforsides((int) (2));
 //BA.debugLineNum = 1217;BA.debugLine="Dim colPos As Int = 0";
_colpos = (int) (0);
 //BA.debugLineNum = 1218;BA.debugLine="Dim rowPos As Int = 0";
_rowpos = (int) (0);
 //BA.debugLineNum = 1219;BA.debugLine="Dim chainCounter As Int";
_chaincounter = 0;
 //BA.debugLineNum = 1220;BA.debugLine="Dim reachedEnd As Boolean";
_reachedend = false;
 //BA.debugLineNum = 1221;BA.debugLine="Dim checkSquare As GameSquare";
_checksquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1222;BA.debugLine="Dim startingSquare As GameSquare";
_startingsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1223;BA.debugLine="Dim chainList As List";
_chainlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1224;BA.debugLine="Dim edgeloop As Int";
_edgeloop = 0;
 //BA.debugLineNum = 1225;BA.debugLine="Dim checkedSquares As Map";
_checkedsquares = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1226;BA.debugLine="Dim excludeEdgeNum As Int = -1";
_excludeedgenum = (int) (-1);
 //BA.debugLineNum = 1227;BA.debugLine="Dim freeEdge As Int";
_freeedge = 0;
 //BA.debugLineNum = 1228;BA.debugLine="chainList.Initialize";
_chainlist.Initialize();
 //BA.debugLineNum = 1229;BA.debugLine="checkedSquares.Initialize";
_checkedsquares.Initialize();
 //BA.debugLineNum = 1231;BA.debugLine="For Each square As GameSquare In found2Sides";
final anywheresoftware.b4a.BA.IterableList group1043 = _found2sides;
final int groupLen1043 = group1043.getSize();
for (int index1043 = 0;index1043 < groupLen1043 ;index1043++){
_square = (pineysoft.squarepaddocks.gamesquare)(group1043.Get(index1043));
 //BA.debugLineNum = 1232;BA.debugLine="startingSquare = square";
_startingsquare = _square;
 //BA.debugLineNum = 1233;BA.debugLine="excludeEdgeNum = -1";
_excludeedgenum = (int) (-1);
 //BA.debugLineNum = 1234;BA.debugLine="If checkedSquares.ContainsKey(square.rowPos & \"-\" & square.colPos) = False Then";
if (_checkedsquares.ContainsKey((Object)(BA.NumberToString(_square._rowpos)+"-"+BA.NumberToString(_square._colpos)))==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1235;BA.debugLine="checkSquare = gameSquares(square.rowPos, square.colPos)";
_checksquare = mostCurrent._gamesquares[_square._rowpos][_square._colpos];
 //BA.debugLineNum = 1236;BA.debugLine="Dim freeEdges As List = checkSquare.GetFreeEdges(excludeEdgeNum)";
_freeedges = new anywheresoftware.b4a.objects.collections.List();
_freeedges = _checksquare._getfreeedges(_excludeedgenum);
 //BA.debugLineNum = 1237;BA.debugLine="For edgeloop = 0 To freeEdges.Size - 1";
{
final int step1049 = 1;
final int limit1049 = (int) (_freeedges.getSize()-1);
for (_edgeloop = (int) (0); (step1049 > 0 && _edgeloop <= limit1049) || (step1049 < 0 && _edgeloop >= limit1049); _edgeloop = ((int)(0 + _edgeloop + step1049))) {
 //BA.debugLineNum = 1238;BA.debugLine="checkSquare = gameSquares(square.rowPos, square.colPos)";
_checksquare = mostCurrent._gamesquares[_square._rowpos][_square._colpos];
 //BA.debugLineNum = 1239;BA.debugLine="chainCounter = 0";
_chaincounter = (int) (0);
 //BA.debugLineNum = 1240;BA.debugLine="Do While reachedEnd = False";
while (_reachedend==anywheresoftware.b4a.keywords.Common.False) {
 //BA.debugLineNum = 1241;BA.debugLine="rowPos = checkSquare.rowPos";
_rowpos = _checksquare._rowpos;
 //BA.debugLineNum = 1242;BA.debugLine="colPos = checkSquare.colPos";
_colpos = _checksquare._colpos;
 //BA.debugLineNum = 1243;BA.debugLine="If chainCounter < 1 Then";
if (_chaincounter<1) { 
 //BA.debugLineNum = 1244;BA.debugLine="freeEdge = freeEdges.Get(edgeloop)";
_freeedge = (int)(BA.ObjectToNumber(_freeedges.Get(_edgeloop)));
 }else {
 //BA.debugLineNum = 1246;BA.debugLine="freeEdge = checkSquare.GetFreeEdges(excludeEdgeNum).Get(0)";
_freeedge = (int)(BA.ObjectToNumber(_checksquare._getfreeedges(_excludeedgenum).Get((int) (0))));
 };
 //BA.debugLineNum = 1248;BA.debugLine="If checkSquare.sidesTaken <> 2 Then";
if (_checksquare._sidestaken!=2) { 
 //BA.debugLineNum = 1249;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1251;BA.debugLine="If freeEdge = SPConstants.TOP_SIDE Then";
if (_freeedge==_spconstants._top_side) { 
 //BA.debugLineNum = 1252;BA.debugLine="If rowPos = 0 Then";
if (_rowpos==0) { 
 //BA.debugLineNum = 1253;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1255;BA.debugLine="rowPos = rowPos - 1";
_rowpos = (int) (_rowpos-1);
 //BA.debugLineNum = 1256;BA.debugLine="excludeEdgeNum = SPConstants.BOTTOM_SIDE";
_excludeedgenum = _spconstants._bottom_side;
 };
 }else if(_freeedge==_spconstants._right_side) { 
 //BA.debugLineNum = 1259;BA.debugLine="If colPos = gameWidth - 1 Then";
if (_colpos==_gamewidth-1) { 
 //BA.debugLineNum = 1260;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1262;BA.debugLine="colPos = colPos + 1";
_colpos = (int) (_colpos+1);
 //BA.debugLineNum = 1263;BA.debugLine="excludeEdgeNum = SPConstants.LEFT_SIDE";
_excludeedgenum = _spconstants._left_side;
 };
 }else if(_freeedge==_spconstants._bottom_side) { 
 //BA.debugLineNum = 1267;BA.debugLine="If rowPos = gameHeight - 1 Then";
if (_rowpos==_gameheight-1) { 
 //BA.debugLineNum = 1268;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1270;BA.debugLine="rowPos = rowPos + 1";
_rowpos = (int) (_rowpos+1);
 //BA.debugLineNum = 1271;BA.debugLine="excludeEdgeNum = SPConstants.TOP_SIDE";
_excludeedgenum = _spconstants._top_side;
 };
 }else {
 //BA.debugLineNum = 1274;BA.debugLine="If colPos = 0 Then";
if (_colpos==0) { 
 //BA.debugLineNum = 1275;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1277;BA.debugLine="colPos = colPos - 1";
_colpos = (int) (_colpos-1);
 //BA.debugLineNum = 1278;BA.debugLine="excludeEdgeNum = SPConstants.RIGHT_SIDE";
_excludeedgenum = _spconstants._right_side;
 };
 };
 };
 //BA.debugLineNum = 1282;BA.debugLine="If chainCounter > 1 AND checkSquare.rowPos = startingSquare.rowPos AND checkSquare.colPos = startingSquare.colPos Then";
if (_chaincounter>1 && _checksquare._rowpos==_startingsquare._rowpos && _checksquare._colpos==_startingsquare._colpos) { 
 //BA.debugLineNum = 1283;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1284;BA.debugLine="edgeloop = freeEdges.Size - 1";
_edgeloop = (int) (_freeedges.getSize()-1);
 }else {
 //BA.debugLineNum = 1286;BA.debugLine="checkedSquares.Put(checkSquare.rowPos & \"-\" & checkSquare.colPos, checkSquare)";
_checkedsquares.Put((Object)(BA.NumberToString(_checksquare._rowpos)+"-"+BA.NumberToString(_checksquare._colpos)),(Object)(_checksquare));
 //BA.debugLineNum = 1287;BA.debugLine="chainCounter = chainCounter + 1";
_chaincounter = (int) (_chaincounter+1);
 };
 //BA.debugLineNum = 1289;BA.debugLine="If reachedEnd <> True Then";
if (_reachedend!=anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1290;BA.debugLine="If gameSquares(rowPos,colPos).sidesTaken = 2 Then";
if (mostCurrent._gamesquares[_rowpos][_colpos]._sidestaken==2) { 
 //BA.debugLineNum = 1291;BA.debugLine="checkSquare = gameSquares(rowPos,colPos)";
_checksquare = mostCurrent._gamesquares[_rowpos][_colpos];
 }else {
 //BA.debugLineNum = 1293;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 }
;
 //BA.debugLineNum = 1297;BA.debugLine="Dim newChain As MyChain";
_newchain = new pineysoft.squarepaddocks.gameactivity._mychain();
 //BA.debugLineNum = 1298;BA.debugLine="newChain.Initialize()";
_newchain.Initialize();
 //BA.debugLineNum = 1299;BA.debugLine="newChain.square = square";
_newchain.square = _square;
 //BA.debugLineNum = 1300;BA.debugLine="newChain.chainCount = chainCounter";
_newchain.chainCount = _chaincounter;
 //BA.debugLineNum = 1301;BA.debugLine="If chainList.Size > 0 Then";
if (_chainlist.getSize()>0) { 
 //BA.debugLineNum = 1302;BA.debugLine="Dim tempitem As MyChain = chainList.Get(chainList.Size -1)";
_tempitem = (pineysoft.squarepaddocks.gameactivity._mychain)(_chainlist.Get((int) (_chainlist.getSize()-1)));
 //BA.debugLineNum = 1303;BA.debugLine="If tempitem.square.rowPos = square.rowPos AND tempitem.square.colPos = square.colPos Then";
if (_tempitem.square._rowpos==_square._rowpos && _tempitem.square._colpos==_square._colpos) { 
 //BA.debugLineNum = 1304;BA.debugLine="tempitem.chainCount = tempitem.chainCount + chainCounter - 1";
_tempitem.chainCount = (int) (_tempitem.chainCount+_chaincounter-1);
 }else {
 //BA.debugLineNum = 1306;BA.debugLine="chainList.Add(newChain)";
_chainlist.Add((Object)(_newchain));
 };
 }else {
 //BA.debugLineNum = 1309;BA.debugLine="chainList.Add(newChain)";
_chainlist.Add((Object)(_newchain));
 };
 //BA.debugLineNum = 1312;BA.debugLine="reachedEnd = False";
_reachedend = anywheresoftware.b4a.keywords.Common.False;
 }
};
 //BA.debugLineNum = 1314;BA.debugLine="checkedSquares.Put(square.rowPos & \"-\" & square.colPos, square)";
_checkedsquares.Put((Object)(BA.NumberToString(_square._rowpos)+"-"+BA.NumberToString(_square._colpos)),(Object)(_square));
 };
 }
;
 //BA.debugLineNum = 1317;BA.debugLine="chainList.SortType(\"chainCount\", True)";
_chainlist.SortType("chainCount",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1318;BA.debugLine="Return chainList";
if (true) return _chainlist;
 //BA.debugLineNum = 1319;BA.debugLine="End Sub";
return null;
}
public static int  _getothersidesquaresides(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side) throws Exception{
pineysoft.squarepaddocks.gamesquare _checksquare = null;
 //BA.debugLineNum = 1334;BA.debugLine="Sub GetOtherSideSquareSides(currentSquare As GameSquare, side As Int) As Int";
 //BA.debugLineNum = 1336;BA.debugLine="Dim checkSquare As GameSquare = SubGetOppositeSquare(currentSquare, side)";
_checksquare = _subgetoppositesquare(_currentsquare,_side);
 //BA.debugLineNum = 1338;BA.debugLine="If checkSquare.IsInitialized Then";
if (_checksquare.IsInitialized()) { 
 //BA.debugLineNum = 1339;BA.debugLine="Return checkSquare.sidesTaken";
if (true) return _checksquare._sidestaken;
 }else {
 //BA.debugLineNum = 1341;BA.debugLine="Return -1";
if (true) return (int) (-1);
 };
 //BA.debugLineNum = 1344;BA.debugLine="End Sub";
return 0;
}
public static String  _getsettings() throws Exception{
String _settings = "";
 //BA.debugLineNum = 161;BA.debugLine="Sub GetSettings() As String";
 //BA.debugLineNum = 162;BA.debugLine="Dim settings As String";
_settings = "";
 //BA.debugLineNum = 164;BA.debugLine="settings = SPConstants.GAME_MSG_SET_SETTINGS & \",\" & sbRows.Value & \",\" & sbColumns.Value & \",\" & spnDifficulty.SelectedItem";
_settings = _spconstants._game_msg_set_settings+","+BA.NumberToString(mostCurrent._sbrows.getValue())+","+BA.NumberToString(mostCurrent._sbcolumns.getValue())+","+mostCurrent._spndifficulty.getSelectedItem();
 //BA.debugLineNum = 166;BA.debugLine="Return settings";
if (true) return _settings;
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.ConcreteViewWrapper  _getviewbytag1(anywheresoftware.b4a.objects.ActivityWrapper _searchview,String _tag) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _tempview = null;
int _vwloop = 0;
 //BA.debugLineNum = 1455;BA.debugLine="Sub GetViewByTag1(searchView As Activity, tag As String) As View";
 //BA.debugLineNum = 1457;BA.debugLine="Dim tempView As View";
_tempview = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1458;BA.debugLine="Dim vwloop As Int";
_vwloop = 0;
 //BA.debugLineNum = 1459;BA.debugLine="For vwloop = 0 To searchView.NumberOfViews - 1";
{
final int step1246 = 1;
final int limit1246 = (int) (_searchview.getNumberOfViews()-1);
for (_vwloop = (int) (0); (step1246 > 0 && _vwloop <= limit1246) || (step1246 < 0 && _vwloop >= limit1246); _vwloop = ((int)(0 + _vwloop + step1246))) {
 //BA.debugLineNum = 1460;BA.debugLine="tempView = searchView.GetView(vwloop)";
_tempview = _searchview.GetView(_vwloop);
 //BA.debugLineNum = 1461;BA.debugLine="If tempView.tag = tag Then";
if ((_tempview.getTag()).equals((Object)(_tag))) { 
 //BA.debugLineNum = 1462;BA.debugLine="Return tempView";
if (true) return _tempview;
 };
 }
};
 //BA.debugLineNum = 1465;BA.debugLine="Return tempView";
if (true) return _tempview;
 //BA.debugLineNum = 1466;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 29;BA.debugLine="Private gameSquares(,) As GameSquare";
mostCurrent._gamesquares = new pineysoft.squarepaddocks.gamesquare[(int) (0)][];
{
int d0 = mostCurrent._gamesquares.length;
int d1 = (int) (0);
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._gamesquares[i0] = new pineysoft.squarepaddocks.gamesquare[d1];
for (int i1 = 0;i1 < d1;i1++) {
mostCurrent._gamesquares[i0][i1] = new pineysoft.squarepaddocks.gamesquare();
}
}
}
;
 //BA.debugLineNum = 30;BA.debugLine="Dim gameTurns As List";
mostCurrent._gameturns = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 31;BA.debugLine="Private gameWidth As Int = 7";
_gamewidth = (int) (7);
 //BA.debugLineNum = 32;BA.debugLine="Private gameHeight As Int = 9";
_gameheight = (int) (9);
 //BA.debugLineNum = 33;BA.debugLine="Private columnSpacing As Int";
_columnspacing = 0;
 //BA.debugLineNum = 34;BA.debugLine="Private rowSpacing As Int";
_rowspacing = 0;
 //BA.debugLineNum = 35;BA.debugLine="Private panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private canv As Canvas";
mostCurrent._canv = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private players As List";
mostCurrent._players = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 38;BA.debugLine="Private checkBoxImages As List";
mostCurrent._checkboximages = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 39;BA.debugLine="Private lblPlayer1 As Label";
mostCurrent._lblplayer1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblPlayer1Image As Label";
mostCurrent._lblplayer1image = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lblPlayer2 As Label";
mostCurrent._lblplayer2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private lblPlayer2Image As Label";
mostCurrent._lblplayer2image = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lblPlayer3 As Label";
mostCurrent._lblplayer3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblPlayer3Image As Label";
mostCurrent._lblplayer3image = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private lblPlayer4 As Label";
mostCurrent._lblplayer4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private lblPlayer4Image As Label";
mostCurrent._lblplayer4image = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private btnContinue As Button";
mostCurrent._btncontinue = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private spnPlayers As Spinner";
mostCurrent._spnplayers = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private sbColumns As SeekBar";
mostCurrent._sbcolumns = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private sbRows As SeekBar";
mostCurrent._sbrows = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private pnlStartScreen As Panel";
mostCurrent._pnlstartscreen = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private icon1 As ImageView";
mostCurrent._icon1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private icon2 As ImageView";
mostCurrent._icon2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private icon3 As ImageView";
mostCurrent._icon3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private icon4 As ImageView";
mostCurrent._icon4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private lblColumns As Label";
mostCurrent._lblcolumns = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private lblPlayers As Label";
mostCurrent._lblplayers = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private lblRows As Label";
mostCurrent._lblrows = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private pnlBase As Panel";
mostCurrent._pnlbase = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private btnCurrPlayer As Button";
mostCurrent._btncurrplayer = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private icon5 As ImageView";
mostCurrent._icon5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private icon6 As ImageView";
mostCurrent._icon6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private pnlDisplay As Panel";
mostCurrent._pnldisplay = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private pnlOuter As Panel";
mostCurrent._pnlouter = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private imageIcon As ImageView";
mostCurrent._imageicon = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private lblWinner As Label";
mostCurrent._lblwinner = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Dim anim1 As AnimationPlus";
mostCurrent._anim1 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Dim anim2 As AnimationPlus";
mostCurrent._anim2 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim anim3 As AnimationPlus";
mostCurrent._anim3 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Dim anim4 As AnimationPlus";
mostCurrent._anim4 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Dim anim5 As AnimationPlus";
mostCurrent._anim5 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Dim anim6 As AnimationPlus";
mostCurrent._anim6 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim animShading As AnimationPlus";
mostCurrent._animshading = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Dim animPanel1 As AnimationPlus";
mostCurrent._animpanel1 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim animPanel2 As AnimationPlus";
mostCurrent._animpanel2 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Dim animStartScr As AnimationPlus";
mostCurrent._animstartscr = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Dim AnimGameScr As AnimationPlus";
mostCurrent._animgamescr = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private spnDroids As Spinner";
mostCurrent._spndroids = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private lblScores As Label";
mostCurrent._lblscores = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private spnDifficulty As Spinner";
mostCurrent._spndifficulty = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private pnlSelectionLeft As Panel";
mostCurrent._pnlselectionleft = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private pnlSelectionRight As Panel";
mostCurrent._pnlselectionright = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private pnlShading As Panel";
mostCurrent._pnlshading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private lblDifficulty As Label";
mostCurrent._lbldifficulty = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private lblDroids As Label";
mostCurrent._lbldroids = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private lblSound As Label";
mostCurrent._lblsound = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private lblDebugDisplay As Label";
mostCurrent._lbldebugdisplay = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Type MyChain(square As GameSquare, chainCount As Int)";
;
 //BA.debugLineNum = 89;BA.debugLine="Dim start1 As Int";
_start1 = 0;
 //BA.debugLineNum = 90;BA.debugLine="Dim start2 As Int";
_start2 = 0;
 //BA.debugLineNum = 91;BA.debugLine="Dim start3 As Int";
_start3 = 0;
 //BA.debugLineNum = 92;BA.debugLine="Dim start4 As Int";
_start4 = 0;
 //BA.debugLineNum = 93;BA.debugLine="Dim start5 As Int";
_start5 = 0;
 //BA.debugLineNum = 94;BA.debugLine="Dim start6 As Int";
_start6 = 0;
 //BA.debugLineNum = 95;BA.debugLine="Private chkSounds As ImageView";
mostCurrent._chksounds = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private lblGameModeDescr As Label";
mostCurrent._lblgamemodedescr = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private txtMessage As EditText";
mostCurrent._txtmessage = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private btnStartGame As Button";
mostCurrent._btnstartgame = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
return "";
}
public static String  _initgameplay() throws Exception{
pineysoft.squarepaddocks.player _ply = null;
String _msg = "";
 //BA.debugLineNum = 611;BA.debugLine="Public Sub InitGamePlay";
 //BA.debugLineNum = 612;BA.debugLine="gameHeight = sbRows.Value + 4";
_gameheight = (int) (mostCurrent._sbrows.getValue()+4);
 //BA.debugLineNum = 613;BA.debugLine="gameWidth = sbColumns.Value + 4";
_gamewidth = (int) (mostCurrent._sbcolumns.getValue()+4);
 //BA.debugLineNum = 614;BA.debugLine="columnSpacing = panel1.Width / (gameWidth + 1)";
_columnspacing = (int) (mostCurrent._panel1.getWidth()/(double)(_gamewidth+1));
 //BA.debugLineNum = 615;BA.debugLine="rowSpacing = panel1.Height / (gameHeight + 1)";
_rowspacing = (int) (mostCurrent._panel1.getHeight()/(double)(_gameheight+1));
 //BA.debugLineNum = 616;BA.debugLine="canv.Initialize(panel1)";
mostCurrent._canv.Initialize((android.view.View)(mostCurrent._panel1.getObject()));
 //BA.debugLineNum = 617;BA.debugLine="gameTurns.Initialize";
mostCurrent._gameturns.Initialize();
 //BA.debugLineNum = 619;BA.debugLine="CreateBoard";
_createboard();
 //BA.debugLineNum = 620;BA.debugLine="DrawBoard";
_drawboard();
 //BA.debugLineNum = 621;BA.debugLine="CreatePlayers";
_createplayers();
 //BA.debugLineNum = 622;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false && _ismaster) { 
 //BA.debugLineNum = 623;BA.debugLine="Dim ply As Player = players.Get(0)";
_ply = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (0)));
 //BA.debugLineNum = 624;BA.debugLine="Dim msg As String = SpConstants.GAME_MSG_SET_PLAYERS & \",\" & ply.PlayerImageNum";
_msg = _spconstants._game_msg_set_players+","+BA.NumberToString(_ply._playerimagenum);
 //BA.debugLineNum = 625;BA.debugLine="ply = players.Get(1)";
_ply = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get((int) (1)));
 //BA.debugLineNum = 626;BA.debugLine="SendMessage(msg & \",\" & ply.PlayerImageNum)";
_sendmessage(_msg+","+BA.NumberToString(_ply._playerimagenum));
 };
 //BA.debugLineNum = 628;BA.debugLine="SetDifficulty";
_setdifficulty();
 //BA.debugLineNum = 629;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 630;BA.debugLine="numberOfPlayers = 2";
_numberofplayers = (int) (2);
 //BA.debugLineNum = 631;BA.debugLine="numberOfDroids = 0";
_numberofdroids = (int) (0);
 //BA.debugLineNum = 632;BA.debugLine="ShowChatWindow(True)";
_showchatwindow(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 634;BA.debugLine="numberOfPlayers = spnPlayers.SelectedItem";
_numberofplayers = (int)(Double.parseDouble(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 635;BA.debugLine="numberOfDroids = spnDroids.SelectedItem";
_numberofdroids = (int)(Double.parseDouble(mostCurrent._spndroids.getSelectedItem()));
 //BA.debugLineNum = 636;BA.debugLine="ShowChatWindow(False)";
_showchatwindow(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 638;BA.debugLine="End Sub";
return "";
}
public static String  _initialisesounds() throws Exception{
 //BA.debugLineNum = 537;BA.debugLine="Sub InitialiseSounds";
 //BA.debugLineNum = 538;BA.debugLine="sounds.Initialize(10)";
_sounds.Initialize((int) (10));
 //BA.debugLineNum = 539;BA.debugLine="giggleSound = sounds.Load(File.DirAssets, \"Giggle1.mp3\")";
_gigglesound = _sounds.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Giggle1.mp3");
 //BA.debugLineNum = 540;BA.debugLine="End Sub";
return "";
}
public static String  _lasticon_animationend() throws Exception{
 //BA.debugLineNum = 419;BA.debugLine="Sub LastIcon_AnimationEnd";
 //BA.debugLineNum = 420;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 421;BA.debugLine="anim1.Stop(icon1)";
mostCurrent._anim1.Stop((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 422;BA.debugLine="anim2.Stop(icon2)";
mostCurrent._anim2.Stop((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 423;BA.debugLine="anim3.Stop(icon3)";
mostCurrent._anim3.Stop((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 424;BA.debugLine="anim4.Stop(icon4)";
mostCurrent._anim4.Stop((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 425;BA.debugLine="anim5.Stop(icon5)";
mostCurrent._anim5.Stop((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 426;BA.debugLine="anim6.Stop(icon6)";
mostCurrent._anim6.Stop((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 427;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 428;BA.debugLine="SetIconAtPosition(icon1,75%x-(icon1.Width/2),25%y)";
_seticonatposition(mostCurrent._icon1,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)-(mostCurrent._icon1.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 429;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 430;BA.debugLine="SetIconAtPosition(icon2,25%x-(icon2.Width/2),45%y)";
_seticonatposition(mostCurrent._icon2,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)-(mostCurrent._icon2.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 431;BA.debugLine="SetIconAtPosition(icon3,50%x-(icon3.Width/2),25%y)";
_seticonatposition(mostCurrent._icon3,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-(mostCurrent._icon3.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 432;BA.debugLine="SetIconAtPosition(icon4,50%x-(icon4.Width/2),45%y)";
_seticonatposition(mostCurrent._icon4,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-(mostCurrent._icon4.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 433;BA.debugLine="SetIconAtPosition(icon5,25%x-(icon5.Width/2),25%y)";
_seticonatposition(mostCurrent._icon5,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)-(mostCurrent._icon5.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 434;BA.debugLine="SetIconAtPosition(icon6,75%x-(icon6.Width/2),45%y)";
_seticonatposition(mostCurrent._icon6,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)-(mostCurrent._icon6.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 435;BA.debugLine="If pnlSelectionLeft.Left <= 0dip Then";
if (mostCurrent._pnlselectionleft.getLeft()<=anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0))) { 
 //BA.debugLineNum = 436;BA.debugLine="AnimateShading";
_animateshading();
 };
 //BA.debugLineNum = 438;BA.debugLine="End Sub";
return "";
}
public static String  _lasticonend_animationend() throws Exception{
 //BA.debugLineNum = 390;BA.debugLine="Sub LastIconEnd_AnimationEnd";
 //BA.debugLineNum = 391;BA.debugLine="Activity.LoadLayout(\"layout1\")";
mostCurrent._activity.LoadLayout("layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 392;BA.debugLine="Activity.LoadLayout(\"winnerScreen\")";
mostCurrent._activity.LoadLayout("winnerScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 393;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 394;BA.debugLine="InitGamePlay";
_initgameplay();
 //BA.debugLineNum = 395;BA.debugLine="SaveDefaults";
_savedefaults();
 //BA.debugLineNum = 396;BA.debugLine="inGame = True";
_ingame = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 397;BA.debugLine="AnimateGameScreens";
_animategamescreens();
 //BA.debugLineNum = 398;BA.debugLine="End Sub";
return "";
}
public static String  _lbldebugdisplay_click() throws Exception{
 //BA.debugLineNum = 1724;BA.debugLine="Sub lblDebugDisplay_Click";
 //BA.debugLineNum = 1725;BA.debugLine="lblDebugDisplay.Left = 100%x";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1726;BA.debugLine="lblDebugDisplay.Visible = False";
mostCurrent._lbldebugdisplay.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1727;BA.debugLine="End Sub";
return "";
}
public static String  _leftpanel_animationend() throws Exception{
 //BA.debugLineNum = 482;BA.debugLine="Sub LeftPanel_AnimationEnd";
 //BA.debugLineNum = 483;BA.debugLine="Log(\"Start Right Panel Anim\")";
anywheresoftware.b4a.keywords.Common.Log("Start Right Panel Anim");
 //BA.debugLineNum = 484;BA.debugLine="AnimatePanelRight";
_animatepanelright();
 //BA.debugLineNum = 485;BA.debugLine="End Sub";
return "";
}
public static String  _loadimages() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm2 = null;
 //BA.debugLineNum = 513;BA.debugLine="Sub LoadImages";
 //BA.debugLineNum = 515;BA.debugLine="checkBoxImages.Initialize";
mostCurrent._checkboximages.Initialize();
 //BA.debugLineNum = 516;BA.debugLine="Dim bm As Bitmap";
_bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 517;BA.debugLine="bm.Initialize(File.DirAssets, \"checkboxOn.png\")";
_bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkboxOn.png");
 //BA.debugLineNum = 518;BA.debugLine="checkBoxImages.Add(bm)";
mostCurrent._checkboximages.Add((Object)(_bm.getObject()));
 //BA.debugLineNum = 519;BA.debugLine="Dim bm2 As Bitmap";
_bm2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 520;BA.debugLine="bm2.Initialize(File.DirAssets, \"checkboxOff.png\")";
_bm2.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkboxOff.png");
 //BA.debugLineNum = 521;BA.debugLine="checkBoxImages.Add(bm2)";
mostCurrent._checkboximages.Add((Object)(_bm2.getObject()));
 //BA.debugLineNum = 522;BA.debugLine="End Sub";
return "";
}
public static String  _loadspinners() throws Exception{
 //BA.debugLineNum = 524;BA.debugLine="Sub LoadSpinners";
 //BA.debugLineNum = 526;BA.debugLine="spnPlayers.AddAll(Array As Int(2,3,4))";
mostCurrent._spnplayers.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new int[]{(int) (2),(int) (3),(int) (4)}));
 //BA.debugLineNum = 527;BA.debugLine="spnDroids.AddAll(Array As Int(0,1))";
mostCurrent._spndroids.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new int[]{(int) (0),(int) (1)}));
 //BA.debugLineNum = 528;BA.debugLine="spnDifficulty.AddAll(Array As String(\"Easy\",\"Medium\",\"Hard\"))";
mostCurrent._spndifficulty.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Easy","Medium","Hard"}));
 //BA.debugLineNum = 530;BA.debugLine="End Sub";
return "";
}
public static String  _makedroidmove(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.collections.List _found3s = null;
anywheresoftware.b4a.objects.collections.List _found2s = null;
anywheresoftware.b4a.objects.collections.List _found1s = null;
anywheresoftware.b4a.objects.collections.List _found0s = null;
boolean _sideclaimed = false;
int _numberclosed = 0;
boolean _exitloop = false;
boolean _forceanyalways = false;
 //BA.debugLineNum = 910;BA.debugLine="Sub MakeDroidMove(currPlayer As Player)";
 //BA.debugLineNum = 911;BA.debugLine="Dim found3s As List";
_found3s = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 912;BA.debugLine="Dim found2s As List = FindAllForSides(2)";
_found2s = new anywheresoftware.b4a.objects.collections.List();
_found2s = _findallforsides((int) (2));
 //BA.debugLineNum = 913;BA.debugLine="Dim found1s As List = FindAllForSides(1)";
_found1s = new anywheresoftware.b4a.objects.collections.List();
_found1s = _findallforsides((int) (1));
 //BA.debugLineNum = 914;BA.debugLine="Dim found0s As List = FindAllForSides(0)";
_found0s = new anywheresoftware.b4a.objects.collections.List();
_found0s = _findallforsides((int) (0));
 //BA.debugLineNum = 915;BA.debugLine="Dim sideClaimed As Boolean";
_sideclaimed = false;
 //BA.debugLineNum = 916;BA.debugLine="Dim numberClosed As Int = 1";
_numberclosed = (int) (1);
 //BA.debugLineNum = 917;BA.debugLine="Dim exitLoop As Boolean";
_exitloop = false;
 //BA.debugLineNum = 918;BA.debugLine="Dim forceAnyAlways As Boolean";
_forceanyalways = false;
 //BA.debugLineNum = 920;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_EASY Then";
if ((_difficulty).equals(_spconstants._difficulty_easy)) { 
 //BA.debugLineNum = 921;BA.debugLine="forceAnyAlways = True";
_forceanyalways = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 924;BA.debugLine="Do While exitLoop = False";
while (_exitloop==anywheresoftware.b4a.keywords.Common.False) {
 //BA.debugLineNum = 925;BA.debugLine="found3s = FindAllForSides(3)";
_found3s = _findallforsides((int) (3));
 //BA.debugLineNum = 926;BA.debugLine="If found3s.Size > 0 Then";
if (_found3s.getSize()>0) { 
 //BA.debugLineNum = 927;BA.debugLine="TakeEasy3s(found3s, currPlayer)";
_takeeasy3s(_found3s,_currplayer);
 //BA.debugLineNum = 928;BA.debugLine="Log(\"Checking Doubles\")";
anywheresoftware.b4a.keywords.Common.Log("Checking Doubles");
 //BA.debugLineNum = 929;BA.debugLine="TakeDoubles";
_takedoubles();
 //BA.debugLineNum = 932;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _closecompletedsquares(_currplayer);
 //BA.debugLineNum = 933;BA.debugLine="If numberClosed > 0 Then";
if (_numberclosed>0) { 
 //BA.debugLineNum = 934;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._score = (int) (_currplayer._score+_numberclosed);
 }else {
 //BA.debugLineNum = 936;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 940;BA.debugLine="If CheckAndDisplayWinnerScreen Then";
if (_checkanddisplaywinnerscreen()) { 
 //BA.debugLineNum = 941;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 }else {
 //BA.debugLineNum = 944;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 }
;
 //BA.debugLineNum = 948;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_EASY Then";
if ((_difficulty).equals(_spconstants._difficulty_easy)) { 
 //BA.debugLineNum = 949;BA.debugLine="If Rnd(1,3) = 1 Then";
if (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (3))==1) { 
 //BA.debugLineNum = 950;BA.debugLine="If found2s.Size > 0 Then";
if (_found2s.getSize()>0) { 
 //BA.debugLineNum = 951;BA.debugLine="Log(\"Doing easy Move\")";
anywheresoftware.b4a.keywords.Common.Log("Doing easy Move");
 //BA.debugLineNum = 952;BA.debugLine="sideClaimed = TakeSingle(found2s, True)";
_sideclaimed = _takesingle(_found2s,anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 957;BA.debugLine="If sideClaimed = False AND found0s.Size > 0 Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False && _found0s.getSize()>0) { 
 //BA.debugLineNum = 958;BA.debugLine="Log(\"Checking 0's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 0's");
 //BA.debugLineNum = 959;BA.debugLine="If TakeSingle(found0s, forceAnyAlways) = False Then";
if (_takesingle(_found0s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 960;BA.debugLine="Log(\"Checking 0's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 0's forced");
 //BA.debugLineNum = 961;BA.debugLine="sideClaimed = TakeSingle(found0s, True)";
_sideclaimed = _takesingle(_found0s,anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 963;BA.debugLine="sideClaimed = True";
_sideclaimed = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 966;BA.debugLine="If sideClaimed = False AND found1s.Size > 0 Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False && _found1s.getSize()>0) { 
 //BA.debugLineNum = 967;BA.debugLine="Log(\"Checking 1's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 1's");
 //BA.debugLineNum = 968;BA.debugLine="If TakeSingle(found1s , forceAnyAlways) = False Then";
if (_takesingle(_found1s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 969;BA.debugLine="If difficulty <> SPConstants.DIFFICULTY_HARD Then";
if ((_difficulty).equals(_spconstants._difficulty_hard) == false) { 
 //BA.debugLineNum = 970;BA.debugLine="Log(\"Checking 1's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 1's forced");
 //BA.debugLineNum = 971;BA.debugLine="sideClaimed = TakeSingle(found1s, True)";
_sideclaimed = _takesingle(_found1s,anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 974;BA.debugLine="sideClaimed = True";
_sideclaimed = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 977;BA.debugLine="If sideClaimed = False Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 978;BA.debugLine="Log(\"Checking 2's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 2's");
 //BA.debugLineNum = 979;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_HARD Then";
if ((_difficulty).equals(_spconstants._difficulty_hard)) { 
 //BA.debugLineNum = 980;BA.debugLine="TakeSingle2 ' this one checks the chain count to get the least first";
_takesingle2();
 }else {
 //BA.debugLineNum = 982;BA.debugLine="If TakeSingle(found2s, forceAnyAlways) = False Then";
if (_takesingle(_found2s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 983;BA.debugLine="Log(\"Checking 2's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 2's forced");
 //BA.debugLineNum = 984;BA.debugLine="TakeSingle(found2s, True)";
_takesingle(_found2s,anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 989;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _closecompletedsquares(_currplayer);
 //BA.debugLineNum = 990;BA.debugLine="If numberClosed > 0 Then";
if (_numberclosed>0) { 
 //BA.debugLineNum = 991;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._score = (int) (_currplayer._score+_numberclosed);
 };
 //BA.debugLineNum = 994;BA.debugLine="If currPlayer.Score > 0 Then";
if (_currplayer._score>0) { 
 //BA.debugLineNum = 995;BA.debugLine="UpdateScore(currPlayer)";
_updatescore(_currplayer);
 };
 //BA.debugLineNum = 998;BA.debugLine="CheckAndDisplayWinnerScreen";
_checkanddisplaywinnerscreen();
 //BA.debugLineNum = 1000;BA.debugLine="End Sub";
return "";
}
public static String  _markotherside(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side,boolean _marktaken) throws Exception{
int _foundcol = 0;
int _foundrow = 0;
int _updateside = 0;
 //BA.debugLineNum = 823;BA.debugLine="Public Sub MarkOtherSide(currentSquare As GameSquare, side As Int, markTaken As Boolean)";
 //BA.debugLineNum = 824;BA.debugLine="Dim foundCol As Int = -1";
_foundcol = (int) (-1);
 //BA.debugLineNum = 825;BA.debugLine="Dim foundRow As Int = -1";
_foundrow = (int) (-1);
 //BA.debugLineNum = 826;BA.debugLine="Dim updateSide As Int";
_updateside = 0;
 //BA.debugLineNum = 828;BA.debugLine="foundRow = currentSquare.RowPos";
_foundrow = _currentsquare._rowpos;
 //BA.debugLineNum = 829;BA.debugLine="foundCol = currentSquare.ColPos";
_foundcol = _currentsquare._colpos;
 //BA.debugLineNum = 832;BA.debugLine="Select side";
switch (BA.switchObjectToInt(_side,_spconstants._top_side,_spconstants._right_side,_spconstants._bottom_side,_spconstants._left_side)) {
case 0:
 //BA.debugLineNum = 834;BA.debugLine="If foundRow > 0 Then";
if (_foundrow>0) { 
 //BA.debugLineNum = 835;BA.debugLine="foundRow = foundRow - 1";
_foundrow = (int) (_foundrow-1);
 //BA.debugLineNum = 836;BA.debugLine="updateSide = SPConstants.BOTTOM_SIDE";
_updateside = _spconstants._bottom_side;
 }else {
 //BA.debugLineNum = 838;BA.debugLine="foundRow = -1";
_foundrow = (int) (-1);
 };
 break;
case 1:
 //BA.debugLineNum = 841;BA.debugLine="If foundCol < gameWidth - 1 Then";
if (_foundcol<_gamewidth-1) { 
 //BA.debugLineNum = 842;BA.debugLine="foundCol = foundCol + 1";
_foundcol = (int) (_foundcol+1);
 //BA.debugLineNum = 843;BA.debugLine="updateSide = SPConstants.LEFT_SIDE";
_updateside = _spconstants._left_side;
 }else {
 //BA.debugLineNum = 845;BA.debugLine="foundCol = -1";
_foundcol = (int) (-1);
 };
 break;
case 2:
 //BA.debugLineNum = 849;BA.debugLine="If foundRow < gameHeight - 1 Then";
if (_foundrow<_gameheight-1) { 
 //BA.debugLineNum = 850;BA.debugLine="foundRow = foundRow + 1";
_foundrow = (int) (_foundrow+1);
 //BA.debugLineNum = 851;BA.debugLine="updateSide = SPConstants.TOP_SIDE";
_updateside = _spconstants._top_side;
 }else {
 //BA.debugLineNum = 853;BA.debugLine="foundRow = -1";
_foundrow = (int) (-1);
 };
 break;
case 3:
 //BA.debugLineNum = 856;BA.debugLine="If foundCol > 0 Then";
if (_foundcol>0) { 
 //BA.debugLineNum = 857;BA.debugLine="foundCol = foundCol - 1";
_foundcol = (int) (_foundcol-1);
 //BA.debugLineNum = 858;BA.debugLine="updateSide = SPConstants.RIGHT_SIDE";
_updateside = _spconstants._right_side;
 }else {
 //BA.debugLineNum = 860;BA.debugLine="foundCol = -1";
_foundcol = (int) (-1);
 };
 break;
}
;
 //BA.debugLineNum = 864;BA.debugLine="If foundRow <> -1 AND foundCol <> -1 Then";
if (_foundrow!=-1 && _foundcol!=-1) { 
 //BA.debugLineNum = 865;BA.debugLine="gameSquares(foundRow,foundCol).MarkSideTaken(updateSide, markTaken)";
mostCurrent._gamesquares[_foundrow][_foundcol]._marksidetaken(_updateside,_marktaken);
 };
 //BA.debugLineNum = 867;BA.debugLine="End Sub";
return "";
}
public static String  _markotherside2(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side) throws Exception{
 //BA.debugLineNum = 820;BA.debugLine="Public Sub MarkOtherSide2(currentSquare As GameSquare, side As Int)";
 //BA.debugLineNum = 821;BA.debugLine="MarkOtherSide(currentSquare, side, True)";
_markotherside(_currentsquare,_side,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 822;BA.debugLine="End Sub";
return "";
}
public static String  _panel1_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 658;BA.debugLine="Sub Panel1_Touch (Action As Int, X As Float, Y As Float)";
 //BA.debugLineNum = 659;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_UP)) {
case 0:
 //BA.debugLineNum = 661;BA.debugLine="If myTurn = True Then";
if (_myturn==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 662;BA.debugLine="myTurn = False";
_myturn = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 663;BA.debugLine="Log (\"X,Y = \" & X & \",\" & Y)";
anywheresoftware.b4a.keywords.Common.Log("X,Y = "+BA.NumberToString(_x)+","+BA.NumberToString(_y));
 //BA.debugLineNum = 664;BA.debugLine="If CalculateMove(X,Y) <> True Then";
if (_calculatemove((int) (_x),(int) (_y))!=anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 665;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 667;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 }else {
 //BA.debugLineNum = 669;BA.debugLine="ToastMessageShow(\"Wait your turn...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Wait your turn...",anywheresoftware.b4a.keywords.Common.False);
 };
 break;
}
;
 //BA.debugLineNum = 672;BA.debugLine="End Sub";
return "";
}
public static String  _pnlstartscreen_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 1728;BA.debugLine="Sub pnlStartScreen_Touch (Action As Int, X As Float, Y As Float)";
 //BA.debugLineNum = 1730;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_UP)) {
case 0:
 //BA.debugLineNum = 1732;BA.debugLine="AnimateCharacters";
_animatecharacters();
 //BA.debugLineNum = 1733;BA.debugLine="AnimatePanelLeft";
_animatepanelleft();
 break;
}
;
 //BA.debugLineNum = 1736;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private currentPlayer As Short";
_currentplayer = (short)0;
 //BA.debugLineNum = 10;BA.debugLine="Private numberOfPlayers As Int";
_numberofplayers = 0;
 //BA.debugLineNum = 11;BA.debugLine="Private numberOfDroids As Int";
_numberofdroids = 0;
 //BA.debugLineNum = 12;BA.debugLine="Private playerColours As List ' List of Ints";
_playercolours = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Private inGame As Boolean";
_ingame = false;
 //BA.debugLineNum = 14;BA.debugLine="Private giggleSound As Int";
_gigglesound = 0;
 //BA.debugLineNum = 15;BA.debugLine="Private displayingDebug As Int";
_displayingdebug = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim sounds As SoundPool";
_sounds = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim SPConstants As Constants";
_spconstants = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 18;BA.debugLine="Dim difficulty As String";
_difficulty = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim vibrate As PhoneVibrate";
_vibrate = new anywheresoftware.b4a.phone.Phone.PhoneVibrate();
 //BA.debugLineNum = 20;BA.debugLine="Dim soundsOn As Boolean = True";
_soundson = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 21;BA.debugLine="Dim GameMode As String";
_gamemode = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim IsMaster As Boolean = False";
_ismaster = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 23;BA.debugLine="Dim myTurn As Boolean";
_myturn = false;
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _processchat(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
 //BA.debugLineNum = 293;BA.debugLine="Sub ProcessChat(msg As String)";
 //BA.debugLineNum = 294;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 295;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 296;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 298;BA.debugLine="ToastMessageShow(lstSettings.Get(1),False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToString(_lstsettings.Get((int) (1))),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 299;BA.debugLine="End Sub";
return "";
}
public static String  _processcolumnchange(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
int _value = 0;
 //BA.debugLineNum = 310;BA.debugLine="Sub ProcessColumnChange(msg As String)";
 //BA.debugLineNum = 311;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 312;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 313;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 314;BA.debugLine="Dim value As Int = lstSettings.Get(1)";
_value = (int)(BA.ObjectToNumber(_lstsettings.Get((int) (1))));
 //BA.debugLineNum = 315;BA.debugLine="sbColumns.value = value";
mostCurrent._sbcolumns.setValue(_value);
 //BA.debugLineNum = 316;BA.debugLine="lblColumns.Text = (value + 4)";
mostCurrent._lblcolumns.setText((Object)((_value+4)));
 //BA.debugLineNum = 317;BA.debugLine="End Sub";
return "";
}
public static String  _processgamemode(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
 //BA.debugLineNum = 243;BA.debugLine="Sub ProcessGameMode(msg As String)";
 //BA.debugLineNum = 244;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 245;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 246;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 247;BA.debugLine="GameMode = lstSettings.Get(1)";
_gamemode = BA.ObjectToString(_lstsettings.Get((int) (1)));
 //BA.debugLineNum = 248;BA.debugLine="UpdateGameModeSettings";
_updategamemodesettings();
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _processrowchange(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
int _value = 0;
 //BA.debugLineNum = 301;BA.debugLine="Sub ProcessRowChange(msg As String)";
 //BA.debugLineNum = 302;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 303;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 304;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 305;BA.debugLine="Dim value As Int = lstSettings.Get(1)";
_value = (int)(BA.ObjectToNumber(_lstsettings.Get((int) (1))));
 //BA.debugLineNum = 306;BA.debugLine="sbRows.value = value";
mostCurrent._sbrows.setValue(_value);
 //BA.debugLineNum = 307;BA.debugLine="lblRows.Text = (value + 4)";
mostCurrent._lblrows.setText((Object)((_value+4)));
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _processstreammessage(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
String _selection = "";
 //BA.debugLineNum = 201;BA.debugLine="Sub ProcessStreamMessage(msg As String)";
 //BA.debugLineNum = 202;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 203;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 204;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 205;BA.debugLine="Dim selection As String = lstSettings.Get(0)";
_selection = BA.ObjectToString(_lstsettings.Get((int) (0)));
 //BA.debugLineNum = 207;BA.debugLine="ToastMessageShow(msg,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(_msg,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="Select selection";
switch (BA.switchObjectToInt(_selection,_spconstants._game_msg_set_game_type,_spconstants._game_msg_set_settings,_spconstants._game_msg_process_turn,_spconstants._game_msg_start_game,_spconstants._game_msg_set_players,_spconstants._game_msg_close_game,_spconstants._game_msg_process_chat,_spconstants._game_msg_set_columns,_spconstants._game_msg_set_rows,_spconstants._game_msg_close_start)) {
case 0:
 //BA.debugLineNum = 211;BA.debugLine="ProcessGameMode(msg)";
_processgamemode(_msg);
 break;
case 1:
 //BA.debugLineNum = 214;BA.debugLine="SetSettings(msg)";
_setsettings(_msg);
 break;
case 2:
 //BA.debugLineNum = 217;BA.debugLine="ProcessStreamTurn(msg)";
_processstreamturn(_msg);
 break;
case 3:
 //BA.debugLineNum = 220;BA.debugLine="ReverseAnimate";
_reverseanimate();
 break;
case 4:
 //BA.debugLineNum = 223;BA.debugLine="ProcessStreamPlayers(msg)";
_processstreamplayers(_msg);
 break;
case 5:
 //BA.debugLineNum = 226;BA.debugLine="ToastMessageShow(\"The Other Player closed the Game\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("The Other Player closed the Game",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 227;BA.debugLine="CloseGame";
_closegame();
 break;
case 6:
 //BA.debugLineNum = 230;BA.debugLine="ProcessChat(msg)";
_processchat(_msg);
 break;
case 7:
 //BA.debugLineNum = 233;BA.debugLine="ProcessColumnChange(msg)";
_processcolumnchange(_msg);
 break;
case 8:
 //BA.debugLineNum = 236;BA.debugLine="ProcessRowChange(msg)";
_processrowchange(_msg);
 break;
case 9:
 //BA.debugLineNum = 239;BA.debugLine="CloseStartScreen";
_closestartscreen();
 break;
}
;
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _processstreamplayers(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
int _iloop = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
pineysoft.squarepaddocks.player _ply = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _vimage = null;
anywheresoftware.b4a.objects.LabelWrapper _lblimage = null;
 //BA.debugLineNum = 266;BA.debugLine="Sub ProcessStreamPlayers(msg As String)";
 //BA.debugLineNum = 267;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 268;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 269;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 271;BA.debugLine="If GameMode = SPConstants.GAMETYPE_MODE_BT AND players.IsInitialized = False Then";
if ((_gamemode).equals(_spconstants._gametype_mode_bt) && mostCurrent._players.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 272;BA.debugLine="CreatePlayers";
_createplayers();
 };
 //BA.debugLineNum = 275;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 276;BA.debugLine="For iLoop = 0 To 1";
{
final int step233 = 1;
final int limit233 = (int) (1);
for (_iloop = (int) (0); (step233 > 0 && _iloop <= limit233) || (step233 < 0 && _iloop >= limit233); _iloop = ((int)(0 + _iloop + step233))) {
 //BA.debugLineNum = 277;BA.debugLine="Dim bm As Bitmap";
_bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 278;BA.debugLine="Dim ply As Player = players.Get(iLoop)";
_ply = (pineysoft.squarepaddocks.player)(mostCurrent._players.Get(_iloop));
 //BA.debugLineNum = 279;BA.debugLine="bm.Initialize(File.DirAssets, \"monsterImage\" & (lstSettings.get(iLoop + 1)) & \".png\")";
_bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"monsterImage"+BA.ObjectToString((_lstsettings.Get((int) (_iloop+1))))+".png");
 //BA.debugLineNum = 280;BA.debugLine="ply.PlayerImage = bm";
_ply._playerimage = _bm;
 //BA.debugLineNum = 281;BA.debugLine="ply.PlayerImageNum = lstSettings.get(iLoop + 1)";
_ply._playerimagenum = (int)(BA.ObjectToNumber(_lstsettings.Get((int) (_iloop+1))));
 //BA.debugLineNum = 282;BA.debugLine="Dim vImage As View = GetViewByTag1(pnlBase, \"I\" & (iLoop + 1))";
_vimage = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_vimage = _getviewbytag1((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"I"+BA.NumberToString((_iloop+1)));
 //BA.debugLineNum = 283;BA.debugLine="If vImage Is Label Then";
if (_vimage.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 284;BA.debugLine="Dim lblImage As Label = vImage";
_lblimage = new anywheresoftware.b4a.objects.LabelWrapper();
_lblimage.setObject((android.widget.TextView)(_vimage.getObject()));
 //BA.debugLineNum = 285;BA.debugLine="lblImage.SetBackgroundImage(ply.PlayerImage)";
_lblimage.SetBackgroundImage((android.graphics.Bitmap)(_ply._playerimage.getObject()));
 };
 //BA.debugLineNum = 287;BA.debugLine="If iLoop = 0 Then";
if (_iloop==0) { 
 //BA.debugLineNum = 288;BA.debugLine="btnCurrPlayer.SetBackgroundImage(ply.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_ply._playerimage.getObject()));
 };
 }
};
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return "";
}
public static String  _processstreamturn(String _msg) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
int _value = 0;
 //BA.debugLineNum = 250;BA.debugLine="Sub ProcessStreamTurn(msg As String)";
 //BA.debugLineNum = 251;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 252;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 253;BA.debugLine="Dim lstSettings As List = strF.Split(msg,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_msg,",");
 //BA.debugLineNum = 254;BA.debugLine="Dim currentSquare As GameSquare = gameSquares(lstSettings.Get(2), lstSettings.Get(3))";
_currentsquare = mostCurrent._gamesquares[(int)(BA.ObjectToNumber(_lstsettings.Get((int) (2))))][(int)(BA.ObjectToNumber(_lstsettings.Get((int) (3))))];
 //BA.debugLineNum = 256;BA.debugLine="CalculateMove2(currentSquare, lstSettings.Get(4))";
_calculatemove2(_currentsquare,(int)(BA.ObjectToNumber(_lstsettings.Get((int) (4)))));
 //BA.debugLineNum = 258;BA.debugLine="Dim value As Int = lstSettings.Get(1)";
_value = (int)(BA.ObjectToNumber(_lstsettings.Get((int) (1))));
 //BA.debugLineNum = 259;BA.debugLine="If value = 0 Then";
if (_value==0) { 
 //BA.debugLineNum = 260;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 262;BA.debugLine="myTurn = False";
_myturn = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 264;BA.debugLine="End Sub";
return "";
}
public static String  _removeturn(pineysoft.squarepaddocks.turn _lastturn,pineysoft.squarepaddocks.player _currplayer) throws Exception{
 //BA.debugLineNum = 1661;BA.debugLine="Sub RemoveTurn(lastTurn As Turn, currPlayer As Player)";
 //BA.debugLineNum = 1664;BA.debugLine="If lastTurn.Square.sidesTaken = 4 Then";
if (_lastturn._square._sidestaken==4) { 
 //BA.debugLineNum = 1665;BA.debugLine="currPlayer.Score = currPlayer.Score - 1";
_currplayer._score = (int) (_currplayer._score-1);
 //BA.debugLineNum = 1666;BA.debugLine="EmptyTheSquare(lastTurn.Square)";
_emptythesquare(_lastturn._square);
 };
 //BA.debugLineNum = 1670;BA.debugLine="lastTurn.Square.RemoveSide(canv, lastTurn.Edge)";
_lastturn._square._removeside(mostCurrent._canv,_lastturn._edge);
 //BA.debugLineNum = 1671;BA.debugLine="MarkOtherSide(lastTurn.Square,lastTurn.Edge,False)";
_markotherside(_lastturn._square,_lastturn._edge,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1674;BA.debugLine="gameTurns.RemoveAt(gameTurns.Size - 1)";
mostCurrent._gameturns.RemoveAt((int) (mostCurrent._gameturns.getSize()-1));
 //BA.debugLineNum = 1677;BA.debugLine="If gameTurns.Size > 0 Then";
if (mostCurrent._gameturns.getSize()>0) { 
 //BA.debugLineNum = 1678;BA.debugLine="lastTurn = gameTurns.Get(gameTurns.Size - 1)";
_lastturn = (pineysoft.squarepaddocks.turn)(mostCurrent._gameturns.Get((int) (mostCurrent._gameturns.getSize()-1)));
 //BA.debugLineNum = 1679;BA.debugLine="lastTurn.Square.RedrawSide(canv, lastTurn.Edge)";
_lastturn._square._redrawside(mostCurrent._canv,_lastturn._edge);
 };
 //BA.debugLineNum = 1681;BA.debugLine="End Sub";
return "";
}
public static String  _reseticons() throws Exception{
 //BA.debugLineNum = 445;BA.debugLine="Sub ResetIcons";
 //BA.debugLineNum = 446;BA.debugLine="start1 = Rnd(0,100%y)";
_start1 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 447;BA.debugLine="start2 = Rnd(0,100%y)";
_start2 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 448;BA.debugLine="start3 = Rnd(0,100%y)";
_start3 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 449;BA.debugLine="start4 = Rnd(0,100%y)";
_start4 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 450;BA.debugLine="start5 = Rnd(0,100%y)";
_start5 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 451;BA.debugLine="start6 = Rnd(0,100%y)";
_start6 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 453;BA.debugLine="SetIconAtPosition(icon1,-(icon1.Width),start1)";
_seticonatposition(mostCurrent._icon1,(int) (-(mostCurrent._icon1.getWidth())),_start1);
 //BA.debugLineNum = 454;BA.debugLine="SetIconAtPosition(icon2,100%x,start2)";
_seticonatposition(mostCurrent._icon2,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_start2);
 //BA.debugLineNum = 455;BA.debugLine="SetIconAtPosition(icon3,-(icon3.Width),start3)";
_seticonatposition(mostCurrent._icon3,(int) (-(mostCurrent._icon3.getWidth())),_start3);
 //BA.debugLineNum = 456;BA.debugLine="SetIconAtPosition(icon4,100%x,start4)";
_seticonatposition(mostCurrent._icon4,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_start4);
 //BA.debugLineNum = 457;BA.debugLine="SetIconAtPosition(icon5,-(icon5.Width),start5)";
_seticonatposition(mostCurrent._icon5,(int) (-(mostCurrent._icon5.getWidth())),_start5);
 //BA.debugLineNum = 458;BA.debugLine="SetIconAtPosition(icon6,100%x,start6)";
_seticonatposition(mostCurrent._icon6,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_start6);
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static boolean  _restoredefaults() throws Exception{
anywheresoftware.b4a.objects.collections.Map _defaultsmap = null;
int _iloop = 0;
int _def_players = 0;
int _def_droids = 0;
String _def_diff = "";
 //BA.debugLineNum = 1586;BA.debugLine="Sub RestoreDefaults() As Boolean";
 //BA.debugLineNum = 1587;BA.debugLine="If File.Exists(File.DirInternal, \"Square_settings.txt\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt")) { 
 //BA.debugLineNum = 1588;BA.debugLine="Dim defaultsMap As Map = File.ReadMap(File.DirInternal, \"Square_settings.txt\")";
_defaultsmap = new anywheresoftware.b4a.objects.collections.Map();
_defaultsmap = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt");
 //BA.debugLineNum = 1589;BA.debugLine="If defaultsMap.IsInitialized Then";
if (_defaultsmap.IsInitialized()) { 
 //BA.debugLineNum = 1590;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 1591;BA.debugLine="Dim def_players As Int = defaultsMap.Get(\"players\")";
_def_players = (int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("players"))));
 //BA.debugLineNum = 1592;BA.debugLine="If GameMode = SPConstants.GAMETYPE_MODE_BT Then";
if ((_gamemode).equals(_spconstants._gametype_mode_bt)) { 
 //BA.debugLineNum = 1593;BA.debugLine="def_players = 0";
_def_players = (int) (0);
 };
 //BA.debugLineNum = 1595;BA.debugLine="For iLoop = 0 To spnPlayers.Size - 1";
{
final int step1371 = 1;
final int limit1371 = (int) (mostCurrent._spnplayers.getSize()-1);
for (_iloop = (int) (0); (step1371 > 0 && _iloop <= limit1371) || (step1371 < 0 && _iloop >= limit1371); _iloop = ((int)(0 + _iloop + step1371))) {
 //BA.debugLineNum = 1596;BA.debugLine="If spnPlayers.GetItem(iLoop) = def_players Then";
if ((mostCurrent._spnplayers.GetItem(_iloop)).equals(BA.NumberToString(_def_players))) { 
 //BA.debugLineNum = 1597;BA.debugLine="spnPlayers.SelectedIndex = iLoop";
mostCurrent._spnplayers.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1598;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1601;BA.debugLine="UpdateDroidNumbers";
_updatedroidnumbers();
 //BA.debugLineNum = 1602;BA.debugLine="Dim def_droids As Int = defaultsMap.Get(\"droids\")";
_def_droids = (int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("droids"))));
 //BA.debugLineNum = 1603;BA.debugLine="If GameMode = SPConstants.GAMETYPE_MODE_BT Then";
if ((_gamemode).equals(_spconstants._gametype_mode_bt)) { 
 //BA.debugLineNum = 1604;BA.debugLine="def_droids = 0";
_def_droids = (int) (0);
 };
 //BA.debugLineNum = 1606;BA.debugLine="For iLoop = 0 To spnDroids.Size - 1";
{
final int step1382 = 1;
final int limit1382 = (int) (mostCurrent._spndroids.getSize()-1);
for (_iloop = (int) (0); (step1382 > 0 && _iloop <= limit1382) || (step1382 < 0 && _iloop >= limit1382); _iloop = ((int)(0 + _iloop + step1382))) {
 //BA.debugLineNum = 1607;BA.debugLine="If spnDroids.GetItem(iLoop) = def_droids Then";
if ((mostCurrent._spndroids.GetItem(_iloop)).equals(BA.NumberToString(_def_droids))) { 
 //BA.debugLineNum = 1608;BA.debugLine="spnDroids.SelectedIndex = iLoop";
mostCurrent._spndroids.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1609;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1613;BA.debugLine="sbRows.Value = defaultsMap.Get(\"rows\")";
mostCurrent._sbrows.setValue((int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("rows")))));
 //BA.debugLineNum = 1614;BA.debugLine="lblRows.Text = \"Rows : \" & (sbRows.Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((mostCurrent._sbrows.getValue()+4))));
 //BA.debugLineNum = 1615;BA.debugLine="sbColumns.Value = defaultsMap.Get(\"columns\")";
mostCurrent._sbcolumns.setValue((int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("columns")))));
 //BA.debugLineNum = 1616;BA.debugLine="lblColumns.Text = \"Columns : \" & (sbColumns.value + 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((mostCurrent._sbcolumns.getValue()+4))));
 //BA.debugLineNum = 1617;BA.debugLine="If defaultsMap.Get(\"Sound\") = True Then";
if ((_defaultsmap.Get((Object)("Sound"))).equals((Object)(anywheresoftware.b4a.keywords.Common.True))) { 
 //BA.debugLineNum = 1618;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._checkboximages.Get(_spconstants._checkbox_on)));
 //BA.debugLineNum = 1619;BA.debugLine="soundsOn = True";
_soundson = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1621;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._checkboximages.Get(_spconstants._checkbox_off)));
 //BA.debugLineNum = 1622;BA.debugLine="soundsOn = False";
_soundson = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 1625;BA.debugLine="Dim def_diff As String = defaultsMap.Get(\"Difficulty\")";
_def_diff = BA.ObjectToString(_defaultsmap.Get((Object)("Difficulty")));
 //BA.debugLineNum = 1626;BA.debugLine="For iLoop = 0 To spnDifficulty.Size - 1";
{
final int step1400 = 1;
final int limit1400 = (int) (mostCurrent._spndifficulty.getSize()-1);
for (_iloop = (int) (0); (step1400 > 0 && _iloop <= limit1400) || (step1400 < 0 && _iloop >= limit1400); _iloop = ((int)(0 + _iloop + step1400))) {
 //BA.debugLineNum = 1627;BA.debugLine="If spnDifficulty.GetItem(iLoop) = def_diff Then";
if ((mostCurrent._spndifficulty.GetItem(_iloop)).equals(_def_diff)) { 
 //BA.debugLineNum = 1628;BA.debugLine="spnDifficulty.SelectedIndex = iLoop";
mostCurrent._spndifficulty.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1629;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 }else {
 //BA.debugLineNum = 1633;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 1637;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 1638;BA.debugLine="spnPlayers.Enabled = False";
mostCurrent._spnplayers.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1639;BA.debugLine="spnDroids.Enabled = False";
mostCurrent._spndroids.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 1641;BA.debugLine="spnPlayers.Enabled = True";
mostCurrent._spnplayers.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1642;BA.debugLine="spnDroids.Enabled = True";
mostCurrent._spndroids.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1645;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1646;BA.debugLine="End Sub";
return false;
}
public static String  _reverseanimate() throws Exception{
 //BA.debugLineNum = 362;BA.debugLine="Sub ReverseAnimate";
 //BA.debugLineNum = 364;BA.debugLine="anim1.InitializeTranslate(\"\", 0,0,-(75%x+(icon1.Width/2)),start1-25%y)";
mostCurrent._anim1.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon1.getWidth()/(double)2))),(float) (_start1-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 365;BA.debugLine="anim1.Duration = 700";
mostCurrent._anim1.setDuration((long) (700));
 //BA.debugLineNum = 366;BA.debugLine="anim1.PersistAfter = True";
mostCurrent._anim1.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 367;BA.debugLine="anim1.Start(icon1)";
mostCurrent._anim1.Start((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 368;BA.debugLine="anim2.InitializeTranslate(\"\", 0,0,75%x+(icon2.Width/2),start2-45%y)";
mostCurrent._anim2.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon2.getWidth()/(double)2)),(float) (_start2-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 369;BA.debugLine="anim2.Duration = 700";
mostCurrent._anim2.setDuration((long) (700));
 //BA.debugLineNum = 370;BA.debugLine="anim2.PersistAfter = True";
mostCurrent._anim2.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 371;BA.debugLine="anim2.Start(icon2)";
mostCurrent._anim2.Start((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 372;BA.debugLine="anim3.InitializeTranslate(\"\", 0,0,-(50%x+(icon3.Width/2)),start3-25%y)";
mostCurrent._anim3.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon3.getWidth()/(double)2))),(float) (_start3-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 373;BA.debugLine="anim3.Duration = 700";
mostCurrent._anim3.setDuration((long) (700));
 //BA.debugLineNum = 374;BA.debugLine="anim3.PersistAfter = True";
mostCurrent._anim3.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 375;BA.debugLine="anim3.Start(icon3)";
mostCurrent._anim3.Start((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 376;BA.debugLine="anim4.InitializeTranslate(\"\", 0,0,50%x+(icon4.Width/2),start4-45%y)";
mostCurrent._anim4.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon4.getWidth()/(double)2)),(float) (_start4-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 377;BA.debugLine="anim4.Duration = 700";
mostCurrent._anim4.setDuration((long) (700));
 //BA.debugLineNum = 378;BA.debugLine="anim4.PersistAfter = True";
mostCurrent._anim4.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 379;BA.debugLine="anim4.Start(icon4)";
mostCurrent._anim4.Start((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 380;BA.debugLine="anim5.InitializeTranslate(\"\", 0,0,-(25%x+(icon5.Width/2)),start5-25%y)";
mostCurrent._anim5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon5.getWidth()/(double)2))),(float) (_start5-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 381;BA.debugLine="anim5.Duration = 700";
mostCurrent._anim5.setDuration((long) (700));
 //BA.debugLineNum = 382;BA.debugLine="anim5.PersistAfter = True";
mostCurrent._anim5.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 383;BA.debugLine="anim5.Start(icon5)";
mostCurrent._anim5.Start((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 384;BA.debugLine="anim6.InitializeTranslate(\"LastIconEnd\", 0,0,25%x+(icon6.Width/2),start6-45%y)";
mostCurrent._anim6.InitializeTranslate(mostCurrent.activityBA,"LastIconEnd",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon6.getWidth()/(double)2)),(float) (_start6-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 385;BA.debugLine="anim6.Duration = 700";
mostCurrent._anim6.setDuration((long) (700));
 //BA.debugLineNum = 386;BA.debugLine="anim6.PersistAfter = True";
mostCurrent._anim6.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 387;BA.debugLine="anim6.Start(icon6)";
mostCurrent._anim6.Start((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 388;BA.debugLine="End Sub";
return "";
}
public static String  _rightpanel_animationend() throws Exception{
 //BA.debugLineNum = 494;BA.debugLine="Sub RightPanel_AnimationEnd";
 //BA.debugLineNum = 495;BA.debugLine="animPanel1.Stop(pnlSelectionLeft)";
mostCurrent._animpanel1.Stop((android.view.View)(mostCurrent._pnlselectionleft.getObject()));
 //BA.debugLineNum = 496;BA.debugLine="pnlSelectionLeft.Top = pnlShading.Top";
mostCurrent._pnlselectionleft.setTop(mostCurrent._pnlshading.getTop());
 //BA.debugLineNum = 497;BA.debugLine="pnlSelectionLeft.Left = 5%x";
mostCurrent._pnlselectionleft.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 499;BA.debugLine="animPanel2.Stop(pnlSelectionRight)";
mostCurrent._animpanel2.Stop((android.view.View)(mostCurrent._pnlselectionright.getObject()));
 //BA.debugLineNum = 500;BA.debugLine="pnlSelectionRight.Top = pnlShading.Top";
mostCurrent._pnlselectionright.setTop(mostCurrent._pnlshading.getTop());
 //BA.debugLineNum = 501;BA.debugLine="pnlSelectionRight.Left = 55%x";
mostCurrent._pnlselectionright.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (55),mostCurrent.activityBA));
 //BA.debugLineNum = 503;BA.debugLine="End Sub";
return "";
}
public static String  _savedefaults() throws Exception{
anywheresoftware.b4a.objects.collections.Map _defaults = null;
 //BA.debugLineNum = 1647;BA.debugLine="Sub SaveDefaults()";
 //BA.debugLineNum = 1648;BA.debugLine="Dim defaults As Map";
_defaults = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1649;BA.debugLine="defaults.Initialize";
_defaults.Initialize();
 //BA.debugLineNum = 1651;BA.debugLine="defaults.Put(\"players\",spnPlayers.SelectedItem)";
_defaults.Put((Object)("players"),(Object)(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 1652;BA.debugLine="defaults.Put(\"droids\",spnDroids.SelectedItem)";
_defaults.Put((Object)("droids"),(Object)(mostCurrent._spndroids.getSelectedItem()));
 //BA.debugLineNum = 1653;BA.debugLine="defaults.Put(\"rows\",sbRows.Value)";
_defaults.Put((Object)("rows"),(Object)(mostCurrent._sbrows.getValue()));
 //BA.debugLineNum = 1654;BA.debugLine="defaults.Put(\"columns\",sbColumns.Value)";
_defaults.Put((Object)("columns"),(Object)(mostCurrent._sbcolumns.getValue()));
 //BA.debugLineNum = 1655;BA.debugLine="defaults.Put(\"Sound\", soundsOn)";
_defaults.Put((Object)("Sound"),(Object)(_soundson));
 //BA.debugLineNum = 1656;BA.debugLine="defaults.Put(\"Difficulty\", spnDifficulty.SelectedItem)";
_defaults.Put((Object)("Difficulty"),(Object)(mostCurrent._spndifficulty.getSelectedItem()));
 //BA.debugLineNum = 1658;BA.debugLine="File.WriteMap(File.DirInternal, \"Square_settings.txt\", defaults)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt",_defaults);
 //BA.debugLineNum = 1660;BA.debugLine="End Sub";
return "";
}
public static String  _sbcolumns_valuechanged(int _value,boolean _userchanged) throws Exception{
String _msg = "";
 //BA.debugLineNum = 1535;BA.debugLine="Sub sbColumns_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 1536;BA.debugLine="gameWidth = Value + 4";
_gamewidth = (int) (_value+4);
 //BA.debugLineNum = 1537;BA.debugLine="lblColumns.Text = \"Columns : \" & (Value	+ 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((_value+4))));
 //BA.debugLineNum = 1538;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false && _ismaster) { 
 //BA.debugLineNum = 1539;BA.debugLine="Dim msg As String = SPConstants.GAME_MSG_SET_COLUMNS & \",\" & Value";
_msg = _spconstants._game_msg_set_columns+","+BA.NumberToString(_value);
 //BA.debugLineNum = 1540;BA.debugLine="SendMessage(msg)";
_sendmessage(_msg);
 };
 //BA.debugLineNum = 1542;BA.debugLine="End Sub";
return "";
}
public static String  _sbrows_valuechanged(int _value,boolean _userchanged) throws Exception{
String _msg = "";
 //BA.debugLineNum = 1527;BA.debugLine="Sub sbRows_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 1528;BA.debugLine="gameHeight = Value + 4";
_gameheight = (int) (_value+4);
 //BA.debugLineNum = 1529;BA.debugLine="lblRows.Text = \"Rows : \" & (Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((_value+4))));
 //BA.debugLineNum = 1530;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false && _ismaster) { 
 //BA.debugLineNum = 1531;BA.debugLine="Dim msg As String = SPConstants.GAME_MSG_SET_ROWS & \",\" & Value";
_msg = _spconstants._game_msg_set_rows+","+BA.NumberToString(_value);
 //BA.debugLineNum = 1532;BA.debugLine="SendMessage(msg)";
_sendmessage(_msg);
 };
 //BA.debugLineNum = 1534;BA.debugLine="End Sub";
return "";
}
public static String  _sendmessage(String _msg) throws Exception{
 //BA.debugLineNum = 129;BA.debugLine="Sub SendMessage(msg As String)";
 //BA.debugLineNum = 130;BA.debugLine="CallSubDelayed2(NetConn,\"SendMessage\",msg)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()),"SendMessage",(Object)(_msg));
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
public static String  _setdifficulty() throws Exception{
 //BA.debugLineNum = 640;BA.debugLine="Public Sub SetDifficulty";
 //BA.debugLineNum = 641;BA.debugLine="Select spnDifficulty.SelectedItem";
switch (BA.switchObjectToInt(mostCurrent._spndifficulty.getSelectedItem(),"Easy","Medium","Hard")) {
case 0:
 //BA.debugLineNum = 643;BA.debugLine="difficulty = SPConstants.DIFFICULTY_EASY";
_difficulty = _spconstants._difficulty_easy;
 break;
case 1:
 //BA.debugLineNum = 645;BA.debugLine="difficulty = SPConstants.DIFFICULTY_MEDIUM";
_difficulty = _spconstants._difficulty_medium;
 break;
case 2:
 //BA.debugLineNum = 647;BA.debugLine="difficulty = SPConstants.DIFFICULTY_HARD";
_difficulty = _spconstants._difficulty_hard;
 break;
}
;
 //BA.debugLineNum = 649;BA.debugLine="End Sub";
return "";
}
public static String  _seticonatposition(anywheresoftware.b4a.objects.ImageViewWrapper _icon,int _left,int _top) throws Exception{
 //BA.debugLineNum = 440;BA.debugLine="Sub SetIconAtPosition(icon As ImageView,left As Int, top As Int)";
 //BA.debugLineNum = 441;BA.debugLine="icon.left = left";
_icon.setLeft(_left);
 //BA.debugLineNum = 442;BA.debugLine="icon.top = top";
_icon.setTop(_top);
 //BA.debugLineNum = 443;BA.debugLine="End Sub";
return "";
}
public static String  _setsettings(String _settings) throws Exception{
adr.stringfunctions.stringfunctions _strf = null;
anywheresoftware.b4a.objects.collections.List _lstsettings = null;
int _iloop = 0;
 //BA.debugLineNum = 169;BA.debugLine="Sub SetSettings(settings As String)";
 //BA.debugLineNum = 170;BA.debugLine="Dim strF As StringFunctions";
_strf = new adr.stringfunctions.stringfunctions();
 //BA.debugLineNum = 171;BA.debugLine="strF.Initialize";
_strf._initialize(processBA);
 //BA.debugLineNum = 172;BA.debugLine="Dim lstSettings As List = strF.Split(settings,\",\")";
_lstsettings = new anywheresoftware.b4a.objects.collections.List();
_lstsettings = _strf._vvvvvv5(_settings,",");
 //BA.debugLineNum = 174;BA.debugLine="sbRows.Value = lstSettings.Get(1)";
mostCurrent._sbrows.setValue((int)(BA.ObjectToNumber(_lstsettings.Get((int) (1)))));
 //BA.debugLineNum = 175;BA.debugLine="sbColumns.Value = lstSettings.Get(2)";
mostCurrent._sbcolumns.setValue((int)(BA.ObjectToNumber(_lstsettings.Get((int) (2)))));
 //BA.debugLineNum = 177;BA.debugLine="For iLoop = 0 To spnDifficulty.Size - 1";
{
final int step155 = 1;
final int limit155 = (int) (mostCurrent._spndifficulty.getSize()-1);
for (_iloop = (int) (0); (step155 > 0 && _iloop <= limit155) || (step155 < 0 && _iloop >= limit155); _iloop = ((int)(0 + _iloop + step155))) {
 //BA.debugLineNum = 178;BA.debugLine="If spnDifficulty.GetItem(iLoop) = lstSettings.Get(3) Then";
if ((mostCurrent._spndifficulty.GetItem(_iloop)).equals(BA.ObjectToString(_lstsettings.Get((int) (3))))) { 
 //BA.debugLineNum = 179;BA.debugLine="spnDifficulty.SelectedIndex = iLoop";
mostCurrent._spndifficulty.setSelectedIndex(_iloop);
 //BA.debugLineNum = 180;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _shading_animationend() throws Exception{
 //BA.debugLineNum = 470;BA.debugLine="Sub Shading_AnimationEnd";
 //BA.debugLineNum = 471;BA.debugLine="Log(\"Start Left Panel Anim\")";
anywheresoftware.b4a.keywords.Common.Log("Start Left Panel Anim");
 //BA.debugLineNum = 472;BA.debugLine="AnimatePanelLeft";
_animatepanelleft();
 //BA.debugLineNum = 473;BA.debugLine="End Sub";
return "";
}
public static String  _showchatwindow(boolean _msg) throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub ShowChatWindow(msg As Boolean)";
 //BA.debugLineNum = 134;BA.debugLine="txtMessage.Visible = msg";
mostCurrent._txtmessage.setVisible(_msg);
 //BA.debugLineNum = 135;BA.debugLine="lblPlayer3.Visible = (msg = False)";
mostCurrent._lblplayer3.setVisible((_msg==anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 136;BA.debugLine="lblPlayer3Image.Visible = (msg = False)";
mostCurrent._lblplayer3image.setVisible((_msg==anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 137;BA.debugLine="lblPlayer4.Visible = (msg = False)";
mostCurrent._lblplayer4.setVisible((_msg==anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 138;BA.debugLine="lblPlayer4Image.Visible = (msg = False)";
mostCurrent._lblplayer4image.setVisible((_msg==anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _showsplashscreen() throws Exception{
 //BA.debugLineNum = 319;BA.debugLine="Sub ShowSplashScreen";
 //BA.debugLineNum = 320;BA.debugLine="pnlShading.Visible = False";
mostCurrent._pnlshading.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 321;BA.debugLine="pnlSelectionLeft.Left = 0 - pnlSelectionLeft.Width";
mostCurrent._pnlselectionleft.setLeft((int) (0-mostCurrent._pnlselectionleft.getWidth()));
 //BA.debugLineNum = 322;BA.debugLine="pnlSelectionRight.Left = 100%x";
mostCurrent._pnlselectionright.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 323;BA.debugLine="UpdateLabels";
_updatelabels();
 //BA.debugLineNum = 324;BA.debugLine="AnimateCharacters";
_animatecharacters();
 //BA.debugLineNum = 325;BA.debugLine="End Sub";
return "";
}
public static String  _spnplayers_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1490;BA.debugLine="Sub spnPlayers_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1491;BA.debugLine="UpdateDroidNumbers";
_updatedroidnumbers();
 //BA.debugLineNum = 1492;BA.debugLine="End Sub";
return "";
}
public static pineysoft.squarepaddocks.gamesquare  _subgetoppositesquare(pineysoft.squarepaddocks.gamesquare _square,int _side) throws Exception{
pineysoft.squarepaddocks.gamesquare _othersquare = null;
 //BA.debugLineNum = 1346;BA.debugLine="Sub SubGetOppositeSquare(square As GameSquare, side As Int) As GameSquare";
 //BA.debugLineNum = 1347;BA.debugLine="Dim otherSquare As GameSquare";
_othersquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1348;BA.debugLine="Select side";
switch (BA.switchObjectToInt(_side,_spconstants._top_side,_spconstants._right_side,_spconstants._bottom_side,_spconstants._left_side)) {
case 0:
 //BA.debugLineNum = 1350;BA.debugLine="If square.RowPos > 0 Then";
if (_square._rowpos>0) { 
 //BA.debugLineNum = 1351;BA.debugLine="otherSquare = gameSquares(square.RowPos - 1, square.ColPos)";
_othersquare = mostCurrent._gamesquares[(int) (_square._rowpos-1)][_square._colpos];
 };
 break;
case 1:
 //BA.debugLineNum = 1354;BA.debugLine="If square.ColPos < gameWidth - 1 Then";
if (_square._colpos<_gamewidth-1) { 
 //BA.debugLineNum = 1355;BA.debugLine="otherSquare = gameSquares(square.RowPos, square.ColPos + 1)";
_othersquare = mostCurrent._gamesquares[_square._rowpos][(int) (_square._colpos+1)];
 };
 break;
case 2:
 //BA.debugLineNum = 1358;BA.debugLine="If square.RowPos < gameHeight - 1 Then";
if (_square._rowpos<_gameheight-1) { 
 //BA.debugLineNum = 1359;BA.debugLine="otherSquare = gameSquares(square.RowPos + 1, square.ColPos)";
_othersquare = mostCurrent._gamesquares[(int) (_square._rowpos+1)][_square._colpos];
 };
 break;
case 3:
 //BA.debugLineNum = 1362;BA.debugLine="If square.RowPos > 0  Then";
if (_square._rowpos>0) { 
 //BA.debugLineNum = 1363;BA.debugLine="otherSquare = gameSquares(square.RowPos, square.ColPos - 1)";
_othersquare = mostCurrent._gamesquares[_square._rowpos][(int) (_square._colpos-1)];
 };
 break;
}
;
 //BA.debugLineNum = 1367;BA.debugLine="Return otherSquare";
if (true) return _othersquare;
 //BA.debugLineNum = 1368;BA.debugLine="End Sub";
return null;
}
public static String  _takedoubles() throws Exception{
anywheresoftware.b4a.objects.collections.List _found3s = null;
int _closeside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
 //BA.debugLineNum = 1043;BA.debugLine="Public Sub TakeDoubles";
 //BA.debugLineNum = 1044;BA.debugLine="Dim found3s As List";
_found3s = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1045;BA.debugLine="Dim closeSide As Int = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 1047;BA.debugLine="found3s = FindAllForSides(3)";
_found3s = _findallforsides((int) (3));
 //BA.debugLineNum = 1049;BA.debugLine="For Each currentSquare As GameSquare In found3s";
final anywheresoftware.b4a.BA.IterableList group900 = _found3s;
final int groupLen900 = group900.getSize();
for (int index900 = 0;index900 < groupLen900 ;index900++){
_currentsquare = (pineysoft.squarepaddocks.gamesquare)(group900.Get(index900));
 //BA.debugLineNum = 1050;BA.debugLine="If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then";
if (_currentsquare._issidetaken(_spconstants._left_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1051;BA.debugLine="If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken = 2 Then";
if (_currentsquare._colpos==0 || mostCurrent._gamesquares[_currentsquare._rowpos][(int) (_currentsquare._colpos-1)]._sidestaken==2) { 
 //BA.debugLineNum = 1052;BA.debugLine="closeSide = SPConstants.LEFT_SIDE";
_closeside = _spconstants._left_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._right_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1055;BA.debugLine="If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken = 2 Then";
if (_currentsquare._colpos==_gamewidth-1 || mostCurrent._gamesquares[_currentsquare._rowpos][(int) (_currentsquare._colpos+1)]._sidestaken==2) { 
 //BA.debugLineNum = 1056;BA.debugLine="closeSide = SPConstants.RIGHT_SIDE";
_closeside = _spconstants._right_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._top_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1059;BA.debugLine="If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken = 2 Then";
if (_currentsquare._rowpos==0 || mostCurrent._gamesquares[(int) (_currentsquare._rowpos-1)][_currentsquare._colpos]._sidestaken==2) { 
 //BA.debugLineNum = 1060;BA.debugLine="closeSide = SPConstants.TOP_SIDE";
_closeside = _spconstants._top_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._bottom_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1063;BA.debugLine="If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken = 2 Then";
if (_currentsquare._rowpos==_gameheight-1 || mostCurrent._gamesquares[(int) (_currentsquare._rowpos+1)][_currentsquare._colpos]._sidestaken==2) { 
 //BA.debugLineNum = 1064;BA.debugLine="closeSide = SPConstants.BOTTOM_SIDE";
_closeside = _spconstants._bottom_side;
 };
 };
 //BA.debugLineNum = 1068;BA.debugLine="If closeSide = -1 Then Return";
if (_closeside==-1) { 
if (true) return "";};
 //BA.debugLineNum = 1071;BA.debugLine="UpdateTurn(canv, currentSquare, closeSide)";
_updateturn(mostCurrent._canv,_currentsquare,_closeside);
 //BA.debugLineNum = 1074;BA.debugLine="currentSquare.TakeSide(canv,closeSide)";
_currentsquare._takeside(mostCurrent._canv,_closeside);
 //BA.debugLineNum = 1077;BA.debugLine="MarkOtherSide2(currentSquare, closeSide)";
_markotherside2(_currentsquare,_closeside);
 }
;
 //BA.debugLineNum = 1081;BA.debugLine="End Sub";
return "";
}
public static boolean  _takeeasy3s(anywheresoftware.b4a.objects.collections.List _found3s,pineysoft.squarepaddocks.player _currplayer) throws Exception{
int _closeside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
 //BA.debugLineNum = 1002;BA.debugLine="Public Sub TakeEasy3s(found3s As List, currPlayer As Player) As Boolean";
 //BA.debugLineNum = 1003;BA.debugLine="Dim closeSide As Int = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 1005;BA.debugLine="Log(\"Checking Found 3's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking Found 3's");
 //BA.debugLineNum = 1006;BA.debugLine="For Each currentSquare As GameSquare In found3s";
final anywheresoftware.b4a.BA.IterableList group868 = _found3s;
final int groupLen868 = group868.getSize();
for (int index868 = 0;index868 < groupLen868 ;index868++){
_currentsquare = (pineysoft.squarepaddocks.gamesquare)(group868.Get(index868));
 //BA.debugLineNum = 1007;BA.debugLine="closeSide = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 1008;BA.debugLine="Log(\"Row: \" & currentSquare.RowPos & \" Column: \" & currentSquare.ColPos)";
anywheresoftware.b4a.keywords.Common.Log("Row: "+BA.NumberToString(_currentsquare._rowpos)+" Column: "+BA.NumberToString(_currentsquare._colpos));
 //BA.debugLineNum = 1009;BA.debugLine="If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then";
if (_currentsquare._issidetaken(_spconstants._left_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1010;BA.debugLine="If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken <> 2 Then";
if (_currentsquare._colpos==0 || mostCurrent._gamesquares[_currentsquare._rowpos][(int) (_currentsquare._colpos-1)]._sidestaken!=2) { 
 //BA.debugLineNum = 1011;BA.debugLine="closeSide = SPConstants.LEFT_SIDE";
_closeside = _spconstants._left_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._right_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1014;BA.debugLine="If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken <> 2 Then";
if (_currentsquare._colpos==_gamewidth-1 || mostCurrent._gamesquares[_currentsquare._rowpos][(int) (_currentsquare._colpos+1)]._sidestaken!=2) { 
 //BA.debugLineNum = 1015;BA.debugLine="closeSide = SPConstants.RIGHT_SIDE";
_closeside = _spconstants._right_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._top_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1018;BA.debugLine="If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken <> 2 Then";
if (_currentsquare._rowpos==0 || mostCurrent._gamesquares[(int) (_currentsquare._rowpos-1)][_currentsquare._colpos]._sidestaken!=2) { 
 //BA.debugLineNum = 1019;BA.debugLine="closeSide = SPConstants.TOP_SIDE";
_closeside = _spconstants._top_side;
 };
 }else if(_currentsquare._issidetaken(_spconstants._bottom_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1022;BA.debugLine="If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken <> 2 Then";
if (_currentsquare._rowpos==_gameheight-1 || mostCurrent._gamesquares[(int) (_currentsquare._rowpos+1)][_currentsquare._colpos]._sidestaken!=2) { 
 //BA.debugLineNum = 1023;BA.debugLine="closeSide = SPConstants.BOTTOM_SIDE";
_closeside = _spconstants._bottom_side;
 };
 };
 //BA.debugLineNum = 1027;BA.debugLine="If closeSide = -1 Then Return False";
if (_closeside==-1) { 
if (true) return anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 1030;BA.debugLine="UpdateTurn(canv, currentSquare, closeSide)";
_updateturn(mostCurrent._canv,_currentsquare,_closeside);
 //BA.debugLineNum = 1033;BA.debugLine="currentSquare.TakeSide(canv,closeSide)";
_currentsquare._takeside(mostCurrent._canv,_closeside);
 //BA.debugLineNum = 1036;BA.debugLine="MarkOtherSide2(currentSquare, closeSide)";
_markotherside2(_currentsquare,_closeside);
 }
;
 //BA.debugLineNum = 1039;BA.debugLine="Log(\"Finished Checking Found 3's\")";
anywheresoftware.b4a.keywords.Common.Log("Finished Checking Found 3's");
 //BA.debugLineNum = 1040;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1041;BA.debugLine="End Sub";
return false;
}
public static boolean  _takesingle(anywheresoftware.b4a.objects.collections.List _foundvalids,boolean _forceany) throws Exception{
int _rndnum = 0;
boolean _foundside = false;
int _rndside = 0;
int _loopcount = 0;
int _loopcountinner = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
int _sides = 0;
 //BA.debugLineNum = 1083;BA.debugLine="Sub TakeSingle(foundValids As List, forceAny As Boolean) As Boolean";
 //BA.debugLineNum = 1084;BA.debugLine="Dim rndnum As Int";
_rndnum = 0;
 //BA.debugLineNum = 1085;BA.debugLine="Dim foundSide As Boolean = False";
_foundside = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1086;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 1087;BA.debugLine="Dim loopCount As Int";
_loopcount = 0;
 //BA.debugLineNum = 1088;BA.debugLine="Dim loopCountInner As Int";
_loopcountinner = 0;
 //BA.debugLineNum = 1090;BA.debugLine="Do While foundSide = False AND loopCount < 50";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcount<50) {
 //BA.debugLineNum = 1091;BA.debugLine="loopCountInner = 0";
_loopcountinner = (int) (0);
 //BA.debugLineNum = 1092;BA.debugLine="If foundValids.Size > 0 Then";
if (_foundvalids.getSize()>0) { 
 //BA.debugLineNum = 1093;BA.debugLine="If foundValids.Size > 1 Then";
if (_foundvalids.getSize()>1) { 
 //BA.debugLineNum = 1094;BA.debugLine="rndnum = Rnd(1, foundValids.Size)";
_rndnum = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),_foundvalids.getSize());
 }else {
 //BA.debugLineNum = 1096;BA.debugLine="rndnum = 1";
_rndnum = (int) (1);
 };
 //BA.debugLineNum = 1099;BA.debugLine="Dim foundSquare As GameSquare = foundValids.Get(rndnum - 1)";
_foundsquare = (pineysoft.squarepaddocks.gamesquare)(_foundvalids.Get((int) (_rndnum-1)));
 //BA.debugLineNum = 1101;BA.debugLine="Do While foundSide = False AND loopCountInner < 8";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcountinner<8) {
 //BA.debugLineNum = 1102;BA.debugLine="rndSide = Rnd(1,4) - 1";
_rndside = (int) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (4))-1);
 //BA.debugLineNum = 1103;BA.debugLine="If foundSquare.IsSideTaken(rndSide) = False Then";
if (_foundsquare._issidetaken(_rndside)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1104;BA.debugLine="If forceAny = True Then";
if (_forceany==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1105;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1107;BA.debugLine="Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)";
_sides = _getothersidesquaresides(_foundsquare,_rndside);
 //BA.debugLineNum = 1108;BA.debugLine="If sides < 2 Then";
if (_sides<2) { 
 //BA.debugLineNum = 1109;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 1113;BA.debugLine="loopCountInner = loopCountInner + 1";
_loopcountinner = (int) (_loopcountinner+1);
 //BA.debugLineNum = 1114;BA.debugLine="Log(\"loop count inner is \" & loopCountInner)";
anywheresoftware.b4a.keywords.Common.Log("loop count inner is "+BA.NumberToString(_loopcountinner));
 }
;
 //BA.debugLineNum = 1117;BA.debugLine="If foundSide = True Then";
if (_foundside==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1118;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_updateturn(mostCurrent._canv,_foundsquare,_rndside);
 //BA.debugLineNum = 1120;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._takeside(mostCurrent._canv,_rndside);
 //BA.debugLineNum = 1122;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_markotherside2(_foundsquare,_rndside);
 };
 };
 //BA.debugLineNum = 1125;BA.debugLine="loopCount = loopCount + 1";
_loopcount = (int) (_loopcount+1);
 //BA.debugLineNum = 1126;BA.debugLine="Log(\"loop count is \" & loopCount)";
anywheresoftware.b4a.keywords.Common.Log("loop count is "+BA.NumberToString(_loopcount));
 }
;
 //BA.debugLineNum = 1129;BA.debugLine="Return foundSide";
if (true) return _foundside;
 //BA.debugLineNum = 1130;BA.debugLine="End Sub";
return false;
}
public static boolean  _takesingle2() throws Exception{
int _rndside = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
anywheresoftware.b4a.objects.collections.List _chainlist = null;
pineysoft.squarepaddocks.gameactivity._mychain _chain = null;
anywheresoftware.b4a.objects.collections.List _edges = null;
 //BA.debugLineNum = 1187;BA.debugLine="Sub TakeSingle2() As Boolean";
 //BA.debugLineNum = 1188;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 1189;BA.debugLine="Dim foundSquare As GameSquare";
_foundsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1190;BA.debugLine="Dim chainList As List = GetChainList";
_chainlist = new anywheresoftware.b4a.objects.collections.List();
_chainlist = _getchainlist();
 //BA.debugLineNum = 1192;BA.debugLine="If chainList.Size > 0 Then";
if (_chainlist.getSize()>0) { 
 //BA.debugLineNum = 1193;BA.debugLine="Dim chain As MyChain = chainList.Get(0)";
_chain = (pineysoft.squarepaddocks.gameactivity._mychain)(_chainlist.Get((int) (0)));
 //BA.debugLineNum = 1194;BA.debugLine="foundSquare = chain.square";
_foundsquare = _chain.square;
 //BA.debugLineNum = 1196;BA.debugLine="Dim edges As List = foundSquare.GetFreeEdges(-1)";
_edges = new anywheresoftware.b4a.objects.collections.List();
_edges = _foundsquare._getfreeedges((int) (-1));
 //BA.debugLineNum = 1197;BA.debugLine="If edges.Size > 1 Then";
if (_edges.getSize()>1) { 
 //BA.debugLineNum = 1198;BA.debugLine="rndSide = edges.Get(Rnd(0,edges.Size))";
_rndside = (int)(BA.ObjectToNumber(_edges.Get(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),_edges.getSize()))));
 }else {
 //BA.debugLineNum = 1200;BA.debugLine="rndSide = edges.get(0)";
_rndside = (int)(BA.ObjectToNumber(_edges.Get((int) (0))));
 };
 //BA.debugLineNum = 1203;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_updateturn(mostCurrent._canv,_foundsquare,_rndside);
 //BA.debugLineNum = 1205;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._takeside(mostCurrent._canv,_rndside);
 //BA.debugLineNum = 1207;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_markotherside2(_foundsquare,_rndside);
 //BA.debugLineNum = 1209;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1212;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1213;BA.debugLine="End Sub";
return false;
}
public static boolean  _takesingle3(anywheresoftware.b4a.objects.collections.List _foundvalids,boolean _forceany) throws Exception{
int _rndnum = 0;
boolean _foundside = false;
int _rndside = 0;
int _loopcount = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
anywheresoftware.b4a.objects.collections.List _edges = null;
int _sides = 0;
 //BA.debugLineNum = 1133;BA.debugLine="Sub TakeSingle3(foundValids As List, forceAny As Boolean) As Boolean";
 //BA.debugLineNum = 1135;BA.debugLine="Dim rndnum As Int";
_rndnum = 0;
 //BA.debugLineNum = 1136;BA.debugLine="Dim foundSide As Boolean = False";
_foundside = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1137;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 1138;BA.debugLine="Dim loopCount As Int";
_loopcount = 0;
 //BA.debugLineNum = 1139;BA.debugLine="Dim foundSquare As GameSquare";
_foundsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1141;BA.debugLine="Do While foundSide = False AND loopCount < 50";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcount<50) {
 //BA.debugLineNum = 1142;BA.debugLine="If foundValids.Size > 0 Then";
if (_foundvalids.getSize()>0) { 
 //BA.debugLineNum = 1143;BA.debugLine="If foundValids.Size > 1 Then";
if (_foundvalids.getSize()>1) { 
 //BA.debugLineNum = 1144;BA.debugLine="rndnum = Rnd(1, foundValids.Size)";
_rndnum = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),_foundvalids.getSize());
 }else {
 //BA.debugLineNum = 1146;BA.debugLine="rndnum = 1";
_rndnum = (int) (1);
 };
 //BA.debugLineNum = 1149;BA.debugLine="foundSquare = foundValids.Get(rndnum - 1)";
_foundsquare = (pineysoft.squarepaddocks.gamesquare)(_foundvalids.Get((int) (_rndnum-1)));
 //BA.debugLineNum = 1151;BA.debugLine="Dim edges As List = foundSquare.GetFreeEdges(-1)";
_edges = new anywheresoftware.b4a.objects.collections.List();
_edges = _foundsquare._getfreeedges((int) (-1));
 //BA.debugLineNum = 1152;BA.debugLine="If edges.Size = 0 Then";
if (_edges.getSize()==0) { 
 //BA.debugLineNum = 1153;BA.debugLine="rndSide = Rnd(0,3)";
_rndside = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (3));
 }else if(_edges.getSize()==1) { 
 //BA.debugLineNum = 1155;BA.debugLine="rndSide = edges.get(0)";
_rndside = (int)(BA.ObjectToNumber(_edges.Get((int) (0))));
 }else {
 //BA.debugLineNum = 1157;BA.debugLine="rndSide = edges.Get(Rnd(0,edges.Size))";
_rndside = (int)(BA.ObjectToNumber(_edges.Get(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),_edges.getSize()))));
 };
 //BA.debugLineNum = 1160;BA.debugLine="If foundSquare.IsSideTaken(rndSide) = False Then";
if (_foundsquare._issidetaken(_rndside)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1161;BA.debugLine="If forceAny = True Then";
if (_forceany==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1162;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1164;BA.debugLine="Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)";
_sides = _getothersidesquaresides(_foundsquare,_rndside);
 //BA.debugLineNum = 1165;BA.debugLine="If sides < 2 Then";
if (_sides<2) { 
 //BA.debugLineNum = 1166;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 1171;BA.debugLine="If foundSide = True Then";
if (_foundside==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1172;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_updateturn(mostCurrent._canv,_foundsquare,_rndside);
 //BA.debugLineNum = 1174;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._takeside(mostCurrent._canv,_rndside);
 //BA.debugLineNum = 1176;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_markotherside2(_foundsquare,_rndside);
 };
 };
 //BA.debugLineNum = 1179;BA.debugLine="loopCount = loopCount + 1";
_loopcount = (int) (_loopcount+1);
 //BA.debugLineNum = 1180;BA.debugLine="Log(\"loop count is \" & loopCount)";
anywheresoftware.b4a.keywords.Common.Log("loop count is "+BA.NumberToString(_loopcount));
 }
;
 //BA.debugLineNum = 1183;BA.debugLine="Return foundSide";
if (true) return _foundside;
 //BA.debugLineNum = 1185;BA.debugLine="End Sub";
return false;
}
public static String  _txtmessage_enterpressed() throws Exception{
 //BA.debugLineNum = 1747;BA.debugLine="Sub txtMessage_EnterPressed";
 //BA.debugLineNum = 1748;BA.debugLine="SendMessage(SPConstants.GAME_MSG_PROCESS_CHAT & \",\" & txtMessage.Text)";
_sendmessage(_spconstants._game_msg_process_chat+","+mostCurrent._txtmessage.getText());
 //BA.debugLineNum = 1749;BA.debugLine="txtMessage.Text = \"\"";
mostCurrent._txtmessage.setText((Object)(""));
 //BA.debugLineNum = 1750;BA.debugLine="End Sub";
return "";
}
public static String  _updatedroidnumbers() throws Exception{
int _currentitem = 0;
int _iloop = 0;
 //BA.debugLineNum = 1494;BA.debugLine="Sub UpdateDroidNumbers";
 //BA.debugLineNum = 1495;BA.debugLine="Dim currentItem As Int = spnDroids.SelectedItem";
_currentitem = (int)(Double.parseDouble(mostCurrent._spndroids.getSelectedItem()));
 //BA.debugLineNum = 1496;BA.debugLine="numberOfPlayers = spnPlayers.SelectedItem";
_numberofplayers = (int)(Double.parseDouble(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 1497;BA.debugLine="spnDroids.Clear";
mostCurrent._spndroids.Clear();
 //BA.debugLineNum = 1498;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 1499;BA.debugLine="For iLoop = 0 To numberOfPlayers - 1";
{
final int step1281 = 1;
final int limit1281 = (int) (_numberofplayers-1);
for (_iloop = (int) (0); (step1281 > 0 && _iloop <= limit1281) || (step1281 < 0 && _iloop >= limit1281); _iloop = ((int)(0 + _iloop + step1281))) {
 //BA.debugLineNum = 1500;BA.debugLine="spnDroids.Add(iLoop)";
mostCurrent._spndroids.Add(BA.NumberToString(_iloop));
 }
};
 //BA.debugLineNum = 1502;BA.debugLine="For iLoop = 0 To spnDroids.Size - 1";
{
final int step1284 = 1;
final int limit1284 = (int) (mostCurrent._spndroids.getSize()-1);
for (_iloop = (int) (0); (step1284 > 0 && _iloop <= limit1284) || (step1284 < 0 && _iloop >= limit1284); _iloop = ((int)(0 + _iloop + step1284))) {
 //BA.debugLineNum = 1503;BA.debugLine="spnDroids.SelectedIndex = iLoop";
mostCurrent._spndroids.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1504;BA.debugLine="If spnDroids.SelectedItem = currentItem Then";
if ((mostCurrent._spndroids.getSelectedItem()).equals(BA.NumberToString(_currentitem))) { 
 //BA.debugLineNum = 1505;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1508;BA.debugLine="If spnDroids.SelectedItem = -1 Then";
if ((mostCurrent._spndroids.getSelectedItem()).equals(BA.NumberToString(-1))) { 
 //BA.debugLineNum = 1509;BA.debugLine="spnDroids.SelectedIndex = 0";
mostCurrent._spndroids.setSelectedIndex((int) (0));
 };
 //BA.debugLineNum = 1511;BA.debugLine="End Sub";
return "";
}
public static String  _updategamemessages(String _msg) throws Exception{
 //BA.debugLineNum = 198;BA.debugLine="Sub UpdateGameMessages(msg As String)";
 //BA.debugLineNum = 199;BA.debugLine="ProcessStreamMessage(msg)";
_processstreammessage(_msg);
 //BA.debugLineNum = 200;BA.debugLine="End Sub";
return "";
}
public static String  _updategamemodesettings() throws Exception{
String _settings = "";
 //BA.debugLineNum = 142;BA.debugLine="Sub UpdateGameModeSettings";
 //BA.debugLineNum = 143;BA.debugLine="lblGameModeDescr.Text = \"Local\"";
mostCurrent._lblgamemodedescr.setText((Object)("Local"));
 //BA.debugLineNum = 144;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 145;BA.debugLine="If IsMaster Then";
if (_ismaster) { 
 //BA.debugLineNum = 146;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 147;BA.debugLine="Dim settings As String = GetSettings";
_settings = _getsettings();
 //BA.debugLineNum = 148;BA.debugLine="SendMessage(settings)";
_sendmessage(_settings);
 }else {
 //BA.debugLineNum = 150;BA.debugLine="myTurn = False";
_myturn = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 152;BA.debugLine="If GameMode = SPConstants.GAMETYPE_MODE_BT Then";
if ((_gamemode).equals(_spconstants._gametype_mode_bt)) { 
 //BA.debugLineNum = 153;BA.debugLine="lblGameModeDescr.Text = \"Bluetooth\"";
mostCurrent._lblgamemodedescr.setText((Object)("Bluetooth"));
 }else {
 //BA.debugLineNum = 155;BA.debugLine="lblGameModeDescr.Text = \"Wifi\"";
mostCurrent._lblgamemodedescr.setText((Object)("Wifi"));
 };
 }else {
 //BA.debugLineNum = 158;BA.debugLine="myTurn = True";
_myturn = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _updatelabels() throws Exception{
 //BA.debugLineNum = 532;BA.debugLine="Sub UpdateLabels";
 //BA.debugLineNum = 533;BA.debugLine="lblRows.Text = \"Rows : \" & (sbRows.Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((mostCurrent._sbrows.getValue()+4))));
 //BA.debugLineNum = 534;BA.debugLine="lblColumns.Text = \"Columns : \" & (sbColumns.Value + 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((mostCurrent._sbcolumns.getValue()+4))));
 //BA.debugLineNum = 535;BA.debugLine="End Sub";
return "";
}
public static String  _updateplayernumber() throws Exception{
 //BA.debugLineNum = 815;BA.debugLine="Sub UpdatePlayerNumber";
 //BA.debugLineNum = 816;BA.debugLine="currentPlayer = currentPlayer + 1";
_currentplayer = (short) (_currentplayer+1);
 //BA.debugLineNum = 817;BA.debugLine="If currentPlayer > numberOfPlayers - 1 Then currentPlayer = 0";
if (_currentplayer>_numberofplayers-1) { 
_currentplayer = (short) (0);};
 //BA.debugLineNum = 818;BA.debugLine="End Sub";
return "";
}
public static String  _updatescore(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _temp = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 //BA.debugLineNum = 787;BA.debugLine="Sub UpdateScore(currPlayer As Player)";
 //BA.debugLineNum = 788;BA.debugLine="Dim temp As View = GetViewByTag1(pnlBase, \"P\" & (currentPlayer + 1))";
_temp = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_temp = _getviewbytag1((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"P"+BA.NumberToString((_currentplayer+1)));
 //BA.debugLineNum = 789;BA.debugLine="If temp Is Label Then";
if (_temp.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 790;BA.debugLine="Dim lbl As Label = temp";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl.setObject((android.widget.TextView)(_temp.getObject()));
 //BA.debugLineNum = 791;BA.debugLine="If currPlayer.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then";
if (_currplayer._playertype==_spconstants._player_type_human) { 
 //BA.debugLineNum = 792;BA.debugLine="lbl.Text = \"Player \" & (currentPlayer + 1) & \" : \" & currPlayer.Score";
_lbl.setText((Object)("Player "+BA.NumberToString((_currentplayer+1))+" : "+BA.NumberToString(_currplayer._score)));
 }else {
 //BA.debugLineNum = 794;BA.debugLine="lbl.Text = \"Droid \" & (currentPlayer + 1) & \" : \" & currPlayer.Score";
_lbl.setText((Object)("Droid "+BA.NumberToString((_currentplayer+1))+" : "+BA.NumberToString(_currplayer._score)));
 };
 };
 //BA.debugLineNum = 797;BA.debugLine="End Sub";
return "";
}
public static String  _updateturn(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,pineysoft.squarepaddocks.gamesquare _currentsquare,int _chosenside) throws Exception{
pineysoft.squarepaddocks.turn _lastturn = null;
pineysoft.squarepaddocks.turn _newturn = null;
 //BA.debugLineNum = 799;BA.debugLine="Sub UpdateTurn(cnv As Canvas, currentSquare As GameSquare, chosenSide As Int)";
 //BA.debugLineNum = 800;BA.debugLine="Dim lastTurn As Turn";
_lastturn = new pineysoft.squarepaddocks.turn();
 //BA.debugLineNum = 801;BA.debugLine="Dim newTurn As Turn";
_newturn = new pineysoft.squarepaddocks.turn();
 //BA.debugLineNum = 804;BA.debugLine="If gameTurns.Size > 0 Then";
if (mostCurrent._gameturns.getSize()>0) { 
 //BA.debugLineNum = 805;BA.debugLine="lastTurn = gameTurns.Get(gameTurns.Size - 1)";
_lastturn = (pineysoft.squarepaddocks.turn)(mostCurrent._gameturns.Get((int) (mostCurrent._gameturns.getSize()-1)));
 //BA.debugLineNum = 806;BA.debugLine="lastTurn.Square.DrawEdge2(canv,lastTurn.Edge,Colors.LightGray)";
_lastturn._square._drawedge2(mostCurrent._canv,_lastturn._edge,anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 };
 //BA.debugLineNum = 810;BA.debugLine="newTurn.Initialize(currentSquare, chosenSide, currentPlayer)";
_newturn._initialize(mostCurrent.activityBA,_currentsquare,_chosenside,(int) (_currentplayer));
 //BA.debugLineNum = 813;BA.debugLine="gameTurns.Add(newTurn)";
mostCurrent._gameturns.Add((Object)(_newturn));
 //BA.debugLineNum = 814;BA.debugLine="End Sub";
return "";
}
}
