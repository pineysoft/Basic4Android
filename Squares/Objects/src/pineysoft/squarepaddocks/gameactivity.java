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
public static short _vvvvvvvvvvvv3 = (short)0;
public static int _vvvvvvvvvvvvvv5 = 0;
public static int _vvvvvvvvvvvvvv6 = 0;
public static anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvv3 = null;
public static boolean _vvvvvvvv3 = false;
public static int _vvvvvvvvvvvvv2 = 0;
public static int _vvvvvvvvvvv3 = 0;
public static anywheresoftware.b4a.audio.SoundPoolWrapper _v5 = null;
public static pineysoft.squarepaddocks.constants _v6 = null;
public static String _v7 = "";
public static anywheresoftware.b4a.phone.Phone.PhoneVibrate _v0 = null;
public static boolean _vv1 = false;
public static String _vv2 = "";
public static boolean _vv3 = false;
public pineysoft.squarepaddocks.gamesquare[][] _vvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvvv5 = null;
public static int _vvvvvvvvvvv6 = 0;
public static int _vvvvvvvvvvv5 = 0;
public static int _vvvvvvvvvvvvvv1 = 0;
public static int _vvvvvvvvvvvvvv2 = 0;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _vvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvv5 = null;
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
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvv5 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvv7 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvv1 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvv3 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvv5 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvv7 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvvvv1 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvvv5 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvvv7 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvvv2 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _vvvvvvvvvv3 = null;
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
public static int _vvvvvvvv6 = 0;
public static int _vvvvvvvv0 = 0;
public static int _vvvvvvvvv2 = 0;
public static int _vvvvvvvvv4 = 0;
public static int _vvvvvvvvv6 = 0;
public static int _vvvvvvvvv0 = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _chksounds = null;
public pineysoft.squarepaddocks.main _vvv2 = null;

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
 //BA.debugLineNum = 98;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 100;BA.debugLine="SPConstants.Initialize";
_v6._initialize(processBA);
 //BA.debugLineNum = 101;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 102;BA.debugLine="Activity.LoadLayout(\"StartScreen\")";
mostCurrent._activity.LoadLayout("StartScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 103;BA.debugLine="DisplayFrontScreen";
_vvvvvvv3();
 //BA.debugLineNum = 104;BA.debugLine="AnimateCharacters";
_vvvvvvv4();
 //BA.debugLineNum = 105;BA.debugLine="CreateColours";
_vvvvvvv5();
 //BA.debugLineNum = 106;BA.debugLine="LoadImages";
_vvvvvvv6();
 //BA.debugLineNum = 107;BA.debugLine="LoadSpinners";
_vvvvvvv7();
 //BA.debugLineNum = 108;BA.debugLine="UpdateLabels";
_vvvvvvv0();
 //BA.debugLineNum = 109;BA.debugLine="RestoreDefaults";
_vvvvvvvv1();
 }else {
 //BA.debugLineNum = 111;BA.debugLine="Activity.LoadLayout(\"StartScreen\")";
mostCurrent._activity.LoadLayout("StartScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 112;BA.debugLine="CreateColours";
_vvvvvvv5();
 //BA.debugLineNum = 113;BA.debugLine="LoadImages";
_vvvvvvv6();
 //BA.debugLineNum = 114;BA.debugLine="LoadSpinners";
_vvvvvvv7();
 //BA.debugLineNum = 115;BA.debugLine="InitialiseSounds";
_vvvvvvvv2();
 //BA.debugLineNum = 116;BA.debugLine="RestoreDefaults";
_vvvvvvvv1();
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
 //BA.debugLineNum = 1296;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event";
 //BA.debugLineNum = 1297;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1298;BA.debugLine="If inGame Then";
if (_vvvvvvvv3) { 
 //BA.debugLineNum = 1299;BA.debugLine="If pnlOuter.Left = 0dip Then";
if (mostCurrent._pnlouter.getLeft()==anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0))) { 
 //BA.debugLineNum = 1300;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 1301;BA.debugLine="DisplayFrontScreen";
_vvvvvvv3();
 //BA.debugLineNum = 1302;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1304;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 1305;BA.debugLine="Answ = Msgbox2(\"Do you want to quit this game, you will lose all progress.\", _ 			      \"W A R N I N G\", \"Yes\", \"\", \"No\", Null)";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("Do you want to quit this game, you will lose all progress.","W A R N I N G","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 1307;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 1308;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1310;BA.debugLine="DisplayFrontScreen";
_vvvvvvv3();
 //BA.debugLineNum = 1311;BA.debugLine="AnimateCharacters";
_vvvvvvv4();
 //BA.debugLineNum = 1312;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 };
 //BA.debugLineNum = 1318;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1319;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 450;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 452;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 446;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 448;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv4() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub AnimateCharacters";
 //BA.debugLineNum = 131;BA.debugLine="ResetIcons";
_vvvvvvvv4();
 //BA.debugLineNum = 132;BA.debugLine="anim1.InitializeTranslate(\"\", 0,0,75%x+(icon1.Width/2),25%y-start1)";
mostCurrent._vvvvvvvv5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon1.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_vvvvvvvv6));
 //BA.debugLineNum = 133;BA.debugLine="anim1.Duration = 1000";
mostCurrent._vvvvvvvv5.setDuration((long) (1000));
 //BA.debugLineNum = 134;BA.debugLine="anim1.StartOffset = 0";
mostCurrent._vvvvvvvv5.setStartOffset((long) (0));
 //BA.debugLineNum = 135;BA.debugLine="anim1.PersistAfter = False";
mostCurrent._vvvvvvvv5.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 136;BA.debugLine="anim1.Start(icon1)";
mostCurrent._vvvvvvvv5.Start((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 137;BA.debugLine="anim2.InitializeTranslate(\"\", 0,0,-(75%x+(icon2.width/2)),45%y-start2)";
mostCurrent._vvvvvvvv7.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon2.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_vvvvvvvv0));
 //BA.debugLineNum = 138;BA.debugLine="anim2.Duration = 1000";
mostCurrent._vvvvvvvv7.setDuration((long) (1000));
 //BA.debugLineNum = 139;BA.debugLine="anim2.StartOffset = 0";
mostCurrent._vvvvvvvv7.setStartOffset((long) (0));
 //BA.debugLineNum = 140;BA.debugLine="anim2.PersistAfter = False";
mostCurrent._vvvvvvvv7.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 141;BA.debugLine="anim2.Start(icon2)";
mostCurrent._vvvvvvvv7.Start((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 142;BA.debugLine="anim3.InitializeTranslate(\"\", 0,0,50%x+(icon3.width/2),25%y-start3)";
mostCurrent._vvvvvvvvv1.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon3.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_vvvvvvvvv2));
 //BA.debugLineNum = 143;BA.debugLine="anim3.Duration = 1000";
mostCurrent._vvvvvvvvv1.setDuration((long) (1000));
 //BA.debugLineNum = 144;BA.debugLine="anim3.StartOffset = 0";
mostCurrent._vvvvvvvvv1.setStartOffset((long) (0));
 //BA.debugLineNum = 145;BA.debugLine="anim3.PersistAfter = False";
mostCurrent._vvvvvvvvv1.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="anim3.Start(icon3)";
mostCurrent._vvvvvvvvv1.Start((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 147;BA.debugLine="anim4.InitializeTranslate(\"\", 0,0,-(50%x+(icon4.width/2)),45%y-start4)";
mostCurrent._vvvvvvvvv3.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon4.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_vvvvvvvvv4));
 //BA.debugLineNum = 148;BA.debugLine="anim4.Duration = 1000";
mostCurrent._vvvvvvvvv3.setDuration((long) (1000));
 //BA.debugLineNum = 149;BA.debugLine="anim4.StartOffset = 0";
mostCurrent._vvvvvvvvv3.setStartOffset((long) (0));
 //BA.debugLineNum = 150;BA.debugLine="anim4.PersistAfter	 = False";
mostCurrent._vvvvvvvvv3.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 151;BA.debugLine="anim4.Start(icon4)";
mostCurrent._vvvvvvvvv3.Start((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="anim5.InitializeTranslate(\"\", 0,0,25%x+(icon5.width/2),25%y-start5)";
mostCurrent._vvvvvvvvv5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon5.getWidth()/(double)2)),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)-_vvvvvvvvv6));
 //BA.debugLineNum = 153;BA.debugLine="anim5.Duration = 1000";
mostCurrent._vvvvvvvvv5.setDuration((long) (1000));
 //BA.debugLineNum = 154;BA.debugLine="anim5.StartOffset = 0";
mostCurrent._vvvvvvvvv5.setStartOffset((long) (0));
 //BA.debugLineNum = 155;BA.debugLine="anim5.PersistAfter = False";
mostCurrent._vvvvvvvvv5.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 156;BA.debugLine="anim5.Start(icon5)";
mostCurrent._vvvvvvvvv5.Start((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 157;BA.debugLine="anim6.InitializeTranslate(\"LastIcon\", 0,0,-(25%x+(icon6.width/2)),45%y-start6)";
mostCurrent._vvvvvvvvv7.InitializeTranslate(mostCurrent.activityBA,"LastIcon",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon6.getWidth()/(double)2))),(float) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)-_vvvvvvvvv0));
 //BA.debugLineNum = 158;BA.debugLine="anim6.Duration = 1000";
mostCurrent._vvvvvvvvv7.setDuration((long) (1000));
 //BA.debugLineNum = 159;BA.debugLine="anim6.StartOffset = 0";
mostCurrent._vvvvvvvvv7.setStartOffset((long) (0));
 //BA.debugLineNum = 160;BA.debugLine="anim6.PersistAfter = False";
mostCurrent._vvvvvvvvv7.setPersistAfter(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="anim6.Start(icon6)";
mostCurrent._vvvvvvvvv7.Start((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvv1() throws Exception{
 //BA.debugLineNum = 204;BA.debugLine="Sub AnimateGameScreens";
 //BA.debugLineNum = 205;BA.debugLine="animStartScr.InitializeTranslate(\"\",0,0,-100%x,0)";
mostCurrent._vvvvvvvvvv2.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 206;BA.debugLine="animStartScr.Duration = 400";
mostCurrent._vvvvvvvvvv2.setDuration((long) (400));
 //BA.debugLineNum = 207;BA.debugLine="animStartScr.PersistAfter = True";
mostCurrent._vvvvvvvvvv2.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 208;BA.debugLine="animStartScr.Start(pnlStartScreen)";
mostCurrent._vvvvvvvvvv2.Start((android.view.View)(mostCurrent._pnlstartscreen.getObject()));
 //BA.debugLineNum = 210;BA.debugLine="AnimGameScr.InitializeTranslate(\"GameScr\",100%x,0,0,0)";
mostCurrent._vvvvvvvvvv3.InitializeTranslate(mostCurrent.activityBA,"GameScr",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0),(float) (0),(float) (0));
 //BA.debugLineNum = 211;BA.debugLine="AnimGameScr.Duration = 400";
mostCurrent._vvvvvvvvvv3.setDuration((long) (400));
 //BA.debugLineNum = 212;BA.debugLine="AnimGameScr.PersistAfter = True";
mostCurrent._vvvvvvvvvv3.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 213;BA.debugLine="AnimGameScr.Start(pnlBase)";
mostCurrent._vvvvvvvvvv3.Start((android.view.View)(mostCurrent._pnlbase.getObject()));
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvv4() throws Exception{
 //BA.debugLineNum = 279;BA.debugLine="Sub AnimatePanelLeft";
 //BA.debugLineNum = 280;BA.debugLine="animPanel1.InitializeTranslate(\"LeftPanel\",-(pnlSelectionLeft.Width),0,pnlSelectionLeft.width + 5%x,0)";
mostCurrent._vvvvvvvvvv5.InitializeTranslate(mostCurrent.activityBA,"LeftPanel",(float) (-(mostCurrent._pnlselectionleft.getWidth())),(float) (0),(float) (mostCurrent._pnlselectionleft.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 281;BA.debugLine="animPanel1.PersistAfter = True";
mostCurrent._vvvvvvvvvv5.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 282;BA.debugLine="animPanel1.Duration = 500";
mostCurrent._vvvvvvvvvv5.setDuration((long) (500));
 //BA.debugLineNum = 283;BA.debugLine="animPanel1.Start(pnlSelectionLeft)";
mostCurrent._vvvvvvvvvv5.Start((android.view.View)(mostCurrent._pnlselectionleft.getObject()));
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvv6() throws Exception{
 //BA.debugLineNum = 290;BA.debugLine="Sub AnimatePanelRight";
 //BA.debugLineNum = 291;BA.debugLine="Log(100%x & \" \" & pnlSelectionRight.Width & \" \" & pnlSelectionRight.Top)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA))+" "+BA.NumberToString(mostCurrent._pnlselectionright.getWidth())+" "+BA.NumberToString(mostCurrent._pnlselectionright.getTop()));
 //BA.debugLineNum = 292;BA.debugLine="animPanel2.InitializeTranslate(\"RightPanel\",100%x,0,-45%x,0)";
mostCurrent._vvvvvvvvvv7.InitializeTranslate(mostCurrent.activityBA,"RightPanel",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (0),(float) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (45),mostCurrent.activityBA)),(float) (0));
 //BA.debugLineNum = 293;BA.debugLine="animPanel2.PersistAfter = True";
mostCurrent._vvvvvvvvvv7.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 294;BA.debugLine="animPanel2.Duration = 500";
mostCurrent._vvvvvvvvvv7.setDuration((long) (500));
 //BA.debugLineNum = 295;BA.debugLine="animPanel2.Start(pnlSelectionRight)";
mostCurrent._vvvvvvvvvv7.Start((android.view.View)(mostCurrent._pnlselectionright.getObject()));
 //BA.debugLineNum = 296;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvv0() throws Exception{
 //BA.debugLineNum = 265;BA.debugLine="Sub AnimateShading";
 //BA.debugLineNum = 266;BA.debugLine="Log(\"StartShading Anim\")";
anywheresoftware.b4a.keywords.Common.Log("StartShading Anim");
 //BA.debugLineNum = 267;BA.debugLine="animShading.InitializeScaleCenter(\"Shading\", 0.1,0.1,1,1, pnlShading)";
mostCurrent._vvvvvvvvvvv1.InitializeScaleCenter(mostCurrent.activityBA,"Shading",(float) (0.1),(float) (0.1),(float) (1),(float) (1),(android.view.View)(mostCurrent._pnlshading.getObject()));
 //BA.debugLineNum = 268;BA.debugLine="animShading.Duration = 750";
mostCurrent._vvvvvvvvvvv1.setDuration((long) (750));
 //BA.debugLineNum = 269;BA.debugLine="animShading.PersistAfter = True";
mostCurrent._vvvvvvvvvvv1.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 270;BA.debugLine="pnlShading.Visible = True";
mostCurrent._pnlshading.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 271;BA.debugLine="animShading.Start(pnlShading)";
mostCurrent._vvvvvvvvvvv1.Start((android.view.View)(mostCurrent._pnlshading.getObject()));
 //BA.debugLineNum = 272;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinue_click() throws Exception{
 //BA.debugLineNum = 1284;BA.debugLine="Sub btnContinue_Click";
 //BA.debugLineNum = 1285;BA.debugLine="vibrate.vibrate(25)";
_v0.Vibrate(processBA,(long) (25));
 //BA.debugLineNum = 1286;BA.debugLine="ReverseAnimate";
_vvvvvvvvvvv2();
 //BA.debugLineNum = 1287;BA.debugLine="End Sub";
return "";
}
public static String  _btncurrplayer_click() throws Exception{
int _rloop = 0;
int _cloop = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
int _iloop = 0;
 //BA.debugLineNum = 1414;BA.debugLine="Sub btnCurrPlayer_Click";
 //BA.debugLineNum = 1417;BA.debugLine="displayingDebug = displayingDebug + 1";
_vvvvvvvvvvv3 = (int) (_vvvvvvvvvvv3+1);
 //BA.debugLineNum = 1419;BA.debugLine="If displayingDebug = 4 Then displayingDebug = 0";
if (_vvvvvvvvvvv3==4) { 
_vvvvvvvvvvv3 = (int) (0);};
 //BA.debugLineNum = 1421;BA.debugLine="Dim rLoop As Int";
_rloop = 0;
 //BA.debugLineNum = 1422;BA.debugLine="Dim cLoop As Int";
_cloop = 0;
 //BA.debugLineNum = 1423;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1424;BA.debugLine="If displayingDebug = 3 Then";
if (_vvvvvvvvvvv3==3) { 
 //BA.debugLineNum = 1425;BA.debugLine="DisplayChainCounts";
_vvvvvvvvvvv4();
 }else {
 //BA.debugLineNum = 1427;BA.debugLine="lblDebugDisplay.Left = 100%x";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1428;BA.debugLine="For rLoop = 0 To gameHeight - 1";
{
final int step1209 = 1;
final int limit1209 = (int) (_vvvvvvvvvvv5-1);
for (_rloop = (int) (0); (step1209 > 0 && _rloop <= limit1209) || (step1209 < 0 && _rloop >= limit1209); _rloop = ((int)(0 + _rloop + step1209))) {
 //BA.debugLineNum = 1429;BA.debugLine="For cLoop = 0 To gameWidth - 1";
{
final int step1210 = 1;
final int limit1210 = (int) (_vvvvvvvvvvv6-1);
for (_cloop = (int) (0); (step1210 > 0 && _cloop <= limit1210) || (step1210 < 0 && _cloop >= limit1210); _cloop = ((int)(0 + _cloop + step1210))) {
 //BA.debugLineNum = 1430;BA.debugLine="square = gameSquares(rLoop, cLoop)";
_square = mostCurrent._vvvvvvvvvvv7[_rloop][_cloop];
 //BA.debugLineNum = 1431;BA.debugLine="Select displayingDebug";
switch (_vvvvvvvvvvv3) {
case 0:
 //BA.debugLineNum = 1433;BA.debugLine="square.fillLabel.Text = \"\"";
_square._vvvvvv6.setText((Object)(""));
 break;
case 1:
 //BA.debugLineNum = 1435;BA.debugLine="square.fillLabel.TextSize = 34";
_square._vvvvvv6.setTextSize((float) (34));
 //BA.debugLineNum = 1436;BA.debugLine="square.fillLabel.Text = square.sidesTaken";
_square._vvvvvv6.setText((Object)(_square._vvvvvv5));
 break;
case 2:
 //BA.debugLineNum = 1438;BA.debugLine="square.fillLabel.TextSize = 12";
_square._vvvvvv6.setTextSize((float) (12));
 //BA.debugLineNum = 1439;BA.debugLine="square.fillLabel.Text = \"\"";
_square._vvvvvv6.setText((Object)(""));
 //BA.debugLineNum = 1440;BA.debugLine="For iLoop = 0 To 3";
{
final int step1221 = 1;
final int limit1221 = (int) (3);
for (_iloop = (int) (0); (step1221 > 0 && _iloop <= limit1221) || (step1221 < 0 && _iloop >= limit1221); _iloop = ((int)(0 + _iloop + step1221))) {
 //BA.debugLineNum = 1441;BA.debugLine="square.fillLabel.Text = square.fillLabel.Text & square.sides(iLoop)";
_square._vvvvvv6.setText((Object)(_square._vvvvvv6.getText()+BA.ObjectToString(_square._vvvvvv3[_iloop])));
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
 //BA.debugLineNum = 1448;BA.debugLine="End Sub";
return "";
}
public static String  _btnok_click() throws Exception{
 //BA.debugLineNum = 1450;BA.debugLine="Sub btnOk_Click";
 //BA.debugLineNum = 1451;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 1452;BA.debugLine="DisplayFrontScreen";
_vvvvvvv3();
 //BA.debugLineNum = 1453;BA.debugLine="AnimateCharacters";
_vvvvvvv4();
 //BA.debugLineNum = 1454;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvv0(int _xcoord,int _ycoord) throws Exception{
pineysoft.squarepaddocks.point _touchpoint = null;
int _chosenside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
pineysoft.squarepaddocks.player _currplayer = null;
int _numberclosed = 0;
 //BA.debugLineNum = 506;BA.debugLine="Sub CalculateMove(xCoord As Int, yCoord As Int)";
 //BA.debugLineNum = 509;BA.debugLine="Dim touchPoint As Point";
_touchpoint = new pineysoft.squarepaddocks.point();
 //BA.debugLineNum = 510;BA.debugLine="Dim chosenSide As Int";
_chosenside = 0;
 //BA.debugLineNum = 511;BA.debugLine="touchPoint.Initialize(xCoord, yCoord)";
_touchpoint._initialize(processBA,_xcoord,_ycoord);
 //BA.debugLineNum = 512;BA.debugLine="Dim currentSquare As GameSquare = FindSelectedSquare(xCoord, yCoord)";
_currentsquare = _vvvvvvvvvvvv1(_xcoord,_ycoord);
 //BA.debugLineNum = 513;BA.debugLine="Dim currPlayer As Player = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get((int) (_vvvvvvvvvvvv3)));
 //BA.debugLineNum = 515;BA.debugLine="chosenSide = currentSquare.CalculateClosestEdge(touchPoint)";
_chosenside = _currentsquare._vvvv4(_touchpoint);
 //BA.debugLineNum = 516;BA.debugLine="Log(chosenSide)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_chosenside));
 //BA.debugLineNum = 519;BA.debugLine="If currentSquare.IsSideTaken(chosenSide) Then Return";
if (_currentsquare._vvvv0(_chosenside)) { 
if (true) return "";};
 //BA.debugLineNum = 521;BA.debugLine="UpdateTurn(canv, currentSquare, chosenSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_currentsquare,_chosenside);
 //BA.debugLineNum = 524;BA.debugLine="currentSquare.TakeSide(canv,chosenSide)";
_currentsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_chosenside);
 //BA.debugLineNum = 527;BA.debugLine="MarkOtherSide2(currentSquare, chosenSide)";
_vvvvvvvvvvvv6(_currentsquare,_chosenside);
 //BA.debugLineNum = 530;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _vvvvvvvvvvvv7(_currplayer);
 //BA.debugLineNum = 532;BA.debugLine="If numberClosed = 0 Then";
if (_numberclosed==0) { 
 //BA.debugLineNum = 533;BA.debugLine="UpdatePlayerNumber";
_vvvvvvvvvvvv0();
 //BA.debugLineNum = 534;BA.debugLine="currPlayer = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get((int) (_vvvvvvvvvvvv3)));
 //BA.debugLineNum = 535;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 //BA.debugLineNum = 536;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 537;BA.debugLine="Do While currPlayer.PlayerType = SPConstants.PLAYER_TYPE_DROID";
while (_currplayer._vvvv1==_v6._player_type_droid) {
 //BA.debugLineNum = 538;BA.debugLine="btnCurrPlayer.Text = \"D\"";
mostCurrent._btncurrplayer.setText((Object)("D"));
 //BA.debugLineNum = 539;BA.debugLine="MakeDroidMove(currPlayer)";
_vvvvvvvvvvvvv1(_currplayer);
 //BA.debugLineNum = 540;BA.debugLine="UpdatePlayerNumber";
_vvvvvvvvvvvv0();
 //BA.debugLineNum = 541;BA.debugLine="currPlayer = players.Get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get((int) (_vvvvvvvvvvvv3)));
 }
;
 //BA.debugLineNum = 543;BA.debugLine="btnCurrPlayer.Text = \"\"";
mostCurrent._btncurrplayer.setText((Object)(""));
 //BA.debugLineNum = 544;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 }else {
 //BA.debugLineNum = 546;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._vvv6 = (int) (_currplayer._vvv6+_numberclosed);
 //BA.debugLineNum = 547;BA.debugLine="If soundsOn Then sounds.Play(giggleSound,1,1,1,1,0)";
if (_vv1) { 
_v5.Play(_vvvvvvvvvvvvv2,(float) (1),(float) (1),(int) (1),(int) (1),(float) (0));};
 //BA.debugLineNum = 548;BA.debugLine="If currPlayer.Score > 0 Then";
if (_currplayer._vvv6>0) { 
 //BA.debugLineNum = 549;BA.debugLine="UpdateScore(currPlayer)";
_vvvvvvvvvvvvv3(_currplayer);
 };
 };
 //BA.debugLineNum = 553;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 555;BA.debugLine="CheckAndDisplayWinnerScreen";
_vvvvvvvvvvvvv4();
 //BA.debugLineNum = 557;BA.debugLine="End Sub";
return "";
}
public static boolean  _vvvvvvvvvvvvv4() throws Exception{
int _iloop = 0;
String _scoretext = "";
pineysoft.squarepaddocks.player _temp = null;
int _winnernum = 0;
int _bestscore = 0;
int _totalscore = 0;
pineysoft.squarepaddocks.player _currplayer = null;
pineysoft.squarepaddocks.player _p = null;
 //BA.debugLineNum = 641;BA.debugLine="Sub CheckAndDisplayWinnerScreen() As Boolean";
 //BA.debugLineNum = 642;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 643;BA.debugLine="Dim scoreText As String";
_scoretext = "";
 //BA.debugLineNum = 644;BA.debugLine="Dim temp As Player";
_temp = new pineysoft.squarepaddocks.player();
 //BA.debugLineNum = 645;BA.debugLine="Dim winnerNum As Int";
_winnernum = 0;
 //BA.debugLineNum = 646;BA.debugLine="Dim bestScore As Int = 0";
_bestscore = (int) (0);
 //BA.debugLineNum = 647;BA.debugLine="Dim totalScore As Int = 0";
_totalscore = (int) (0);
 //BA.debugLineNum = 648;BA.debugLine="For iLoop = 0 To players.Size - 1";
{
final int step547 = 1;
final int limit547 = (int) (mostCurrent._vvvvvvvvvvvv2.getSize()-1);
for (_iloop = (int) (0); (step547 > 0 && _iloop <= limit547) || (step547 < 0 && _iloop >= limit547); _iloop = ((int)(0 + _iloop + step547))) {
 //BA.debugLineNum = 649;BA.debugLine="temp = players.Get(iLoop)";
_temp = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get(_iloop));
 //BA.debugLineNum = 650;BA.debugLine="totalScore = totalScore + temp.Score";
_totalscore = (int) (_totalscore+_temp._vvv6);
 //BA.debugLineNum = 651;BA.debugLine="If temp.Score > bestScore Then";
if (_temp._vvv6>_bestscore) { 
 //BA.debugLineNum = 652;BA.debugLine="winnerNum = iLoop";
_winnernum = _iloop;
 //BA.debugLineNum = 653;BA.debugLine="bestScore = temp.Score";
_bestscore = _temp._vvv6;
 }else if(_temp._vvv6==_bestscore) { 
 //BA.debugLineNum = 655;BA.debugLine="winnerNum = -1";
_winnernum = (int) (-1);
 };
 }
};
 //BA.debugLineNum = 658;BA.debugLine="If totalScore = gameWidth  * gameHeight Then";
if (_totalscore==_vvvvvvvvvvv6*_vvvvvvvvvvv5) { 
 //BA.debugLineNum = 659;BA.debugLine="If pnlOuter.IsInitialized Then";
if (mostCurrent._pnlouter.IsInitialized()) { 
 //BA.debugLineNum = 660;BA.debugLine="If winnerNum <> -1 Then";
if (_winnernum!=-1) { 
 //BA.debugLineNum = 661;BA.debugLine="lblWinner.Text = \"Player \" & (winnerNum + 1) & \" is the winner!!!\"";
mostCurrent._lblwinner.setText((Object)("Player "+BA.NumberToString((_winnernum+1))+" is the winner!!!"));
 //BA.debugLineNum = 662;BA.debugLine="Dim currPlayer As Player = players.Get(winnerNum)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get(_winnernum));
 //BA.debugLineNum = 663;BA.debugLine="imageIcon.Bitmap = currPlayer.PlayerImage";
mostCurrent._imageicon.setBitmap((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 //BA.debugLineNum = 664;BA.debugLine="pnlOuter.Left = 0dip";
mostCurrent._pnlouter.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 665;BA.debugLine="imageIcon.Visible = True";
mostCurrent._imageicon.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 667;BA.debugLine="imageIcon.Visible = False";
mostCurrent._imageicon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 668;BA.debugLine="lblWinner.Text = \"IT'S A TIE\"";
mostCurrent._lblwinner.setText((Object)("IT'S A TIE"));
 //BA.debugLineNum = 669;BA.debugLine="pnlOuter.Left = 0dip";
mostCurrent._pnlouter.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 };
 };
 //BA.debugLineNum = 672;BA.debugLine="For iLoop = 1 To players.Size";
{
final int step571 = 1;
final int limit571 = mostCurrent._vvvvvvvvvvvv2.getSize();
for (_iloop = (int) (1); (step571 > 0 && _iloop <= limit571) || (step571 < 0 && _iloop >= limit571); _iloop = ((int)(0 + _iloop + step571))) {
 //BA.debugLineNum = 673;BA.debugLine="Dim p As Player = players.Get(iLoop - 1)";
_p = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get((int) (_iloop-1)));
 //BA.debugLineNum = 674;BA.debugLine="scoreText = scoreText & \"Player \" & iLoop & \": \" & p.Score & \" \"";
_scoretext = _scoretext+"Player "+BA.NumberToString(_iloop)+": "+BA.NumberToString(_p._vvv6)+" ";
 }
};
 //BA.debugLineNum = 676;BA.debugLine="lblScores.Text = scoreText";
mostCurrent._lblscores.setText((Object)(_scoretext));
 //BA.debugLineNum = 677;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 679;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 681;BA.debugLine="End Sub";
return false;
}
public static String  _chksounds_click() throws Exception{
 //BA.debugLineNum = 1469;BA.debugLine="Sub chkSounds_Click";
 //BA.debugLineNum = 1470;BA.debugLine="If soundsOn Then";
if (_vv1) { 
 //BA.debugLineNum = 1471;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._vvvvvvvvvvvvv5.Get(_v6._checkbox_off)));
 //BA.debugLineNum = 1472;BA.debugLine="soundsOn = False";
_vv1 = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 1474;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._vvvvvvvvvvvvv5.Get(_v6._checkbox_on)));
 //BA.debugLineNum = 1475;BA.debugLine="soundsOn = True";
_vv1 = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1477;BA.debugLine="End Sub";
return "";
}
public static int  _vvvvvvvvvvvv7(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.collections.List _allclosed = null;
int _closed = 0;
pineysoft.squarepaddocks.gamesquare _item = null;
 //BA.debugLineNum = 1142;BA.debugLine="Sub CloseCompletedSquares(currPlayer As Player) As Int";
 //BA.debugLineNum = 1143;BA.debugLine="Dim allClosed As List = FindAllForSides(4)";
_allclosed = new anywheresoftware.b4a.objects.collections.List();
_allclosed = _vvvvvvvvvvvvv6((int) (4));
 //BA.debugLineNum = 1144;BA.debugLine="Dim closed As Int";
_closed = 0;
 //BA.debugLineNum = 1146;BA.debugLine="For Each item As GameSquare In allClosed";
final anywheresoftware.b4a.BA.IterableList group970 = _allclosed;
final int groupLen970 = group970.getSize();
for (int index970 = 0;index970 < groupLen970 ;index970++){
_item = (pineysoft.squarepaddocks.gamesquare)(group970.Get(index970));
 //BA.debugLineNum = 1147;BA.debugLine="If item.Occupied = False Then";
if (_item._vvvvvv4==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1148;BA.debugLine="FillTheSquare(item, currPlayer)";
_vvvvvvvvvvvvv7(_item,_currplayer);
 //BA.debugLineNum = 1149;BA.debugLine="closed = closed + 1";
_closed = (int) (_closed+1);
 //BA.debugLineNum = 1150;BA.debugLine="item.Occupied = True";
_item._vvvvvv4 = anywheresoftware.b4a.keywords.Common.True;
 };
 }
;
 //BA.debugLineNum = 1154;BA.debugLine="Return closed";
if (true) return _closed;
 //BA.debugLineNum = 1155;BA.debugLine="End Sub";
return 0;
}
public static String  _vvvvvvvvvvvvv0() throws Exception{
int _colloop = 0;
int _rowloop = 0;
int _x = 0;
int _y = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
anywheresoftware.b4a.objects.LabelWrapper _lblsquare = null;
 //BA.debugLineNum = 463;BA.debugLine="Sub CreateBoard";
 //BA.debugLineNum = 464;BA.debugLine="Dim colLoop As Int";
_colloop = 0;
 //BA.debugLineNum = 465;BA.debugLine="Dim rowLoop As Int";
_rowloop = 0;
 //BA.debugLineNum = 466;BA.debugLine="Dim x As Int = columnSpacing / 2";
_x = (int) (_vvvvvvvvvvvvvv1/(double)2);
 //BA.debugLineNum = 467;BA.debugLine="Dim y As Int = rowSpacing / 2";
_y = (int) (_vvvvvvvvvvvvvv2/(double)2);
 //BA.debugLineNum = 468;BA.debugLine="Dim gameSquares(gameHeight,gameWidth) As GameSquare";
mostCurrent._vvvvvvvvvvv7 = new pineysoft.squarepaddocks.gamesquare[_vvvvvvvvvvv5][];
{
int d0 = mostCurrent._vvvvvvvvvvv7.length;
int d1 = _vvvvvvvvvvv6;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._vvvvvvvvvvv7[i0] = new pineysoft.squarepaddocks.gamesquare[d1];
for (int i1 = 0;i1 < d1;i1++) {
mostCurrent._vvvvvvvvvvv7[i0][i1] = new pineysoft.squarepaddocks.gamesquare();
}
}
}
;
 //BA.debugLineNum = 470;BA.debugLine="For rowLoop = 0 To gameHeight - 1";
{
final int step403 = 1;
final int limit403 = (int) (_vvvvvvvvvvv5-1);
for (_rowloop = (int) (0); (step403 > 0 && _rowloop <= limit403) || (step403 < 0 && _rowloop >= limit403); _rowloop = ((int)(0 + _rowloop + step403))) {
 //BA.debugLineNum = 471;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step404 = 1;
final int limit404 = (int) (_vvvvvvvvvvv6-1);
for (_colloop = (int) (0); (step404 > 0 && _colloop <= limit404) || (step404 < 0 && _colloop >= limit404); _colloop = ((int)(0 + _colloop + step404))) {
 //BA.debugLineNum = 472;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 473;BA.debugLine="Dim lblSquare As Label";
_lblsquare = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 474;BA.debugLine="lblSquare.Initialize(\"\")";
_lblsquare.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 475;BA.debugLine="lblSquare.Gravity = Gravity.FILL";
_lblsquare.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 476;BA.debugLine="square.Initialize(x,y,columnSpacing,rowSpacing,rowLoop,colLoop)";
_square._initialize(mostCurrent.activityBA,_x,_y,_vvvvvvvvvvvvvv1,_vvvvvvvvvvvvvv2,_rowloop,_colloop);
 //BA.debugLineNum = 477;BA.debugLine="square.fillLabel = lblSquare";
_square._vvvvvv6 = _lblsquare;
 //BA.debugLineNum = 478;BA.debugLine="panel1.AddView(lblSquare, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)";
mostCurrent._panel1.AddView((android.view.View)(_lblsquare.getObject()),(int) (_square._vvvvv7._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvv7._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_vvvvvvvvvvvvvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(int) (_vvvvvvvvvvvvvv2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 479;BA.debugLine="gameSquares(rowLoop, colLoop) = square";
mostCurrent._vvvvvvvvvvv7[_rowloop][_colloop] = _square;
 //BA.debugLineNum = 480;BA.debugLine="x = x + columnSpacing";
_x = (int) (_x+_vvvvvvvvvvvvvv1);
 }
};
 //BA.debugLineNum = 482;BA.debugLine="x = columnSpacing / 2";
_x = (int) (_vvvvvvvvvvvvvv1/(double)2);
 //BA.debugLineNum = 483;BA.debugLine="y = y + rowSpacing";
_y = (int) (_y+_vvvvvvvvvvvvvv2);
 }
};
 //BA.debugLineNum = 485;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv5() throws Exception{
 //BA.debugLineNum = 309;BA.debugLine="Sub CreateColours()";
 //BA.debugLineNum = 310;BA.debugLine="playerColours.Initialize";
_vvvvvvvvvvvvvv3.Initialize();
 //BA.debugLineNum = 311;BA.debugLine="playerColours.Add(Colors.Yellow)";
_vvvvvvvvvvvvvv3.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Yellow));
 //BA.debugLineNum = 312;BA.debugLine="playerColours.Add(Colors.Blue)";
_vvvvvvvvvvvvvv3.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Blue));
 //BA.debugLineNum = 313;BA.debugLine="playerColours.Add(Colors.Green)";
_vvvvvvvvvvvvvv3.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Green));
 //BA.debugLineNum = 314;BA.debugLine="playerColours.Add(Colors.Red)";
_vvvvvvvvvvvvvv3.Add((Object)(anywheresoftware.b4a.keywords.Common.Colors.Red));
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvv4() throws Exception{
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
 //BA.debugLineNum = 360;BA.debugLine="Sub CreatePlayers";
 //BA.debugLineNum = 361;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 362;BA.debugLine="players.Initialize";
mostCurrent._vvvvvvvvvvvv2.Initialize();
 //BA.debugLineNum = 363;BA.debugLine="Dim imagesTaken As Map";
_imagestaken = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 364;BA.debugLine="imagesTaken.Initialize";
_imagestaken.Initialize();
 //BA.debugLineNum = 365;BA.debugLine="For iLoop = 1 To numberOfPlayers";
{
final int step309 = 1;
final int limit309 = _vvvvvvvvvvvvvv5;
for (_iloop = (int) (1); (step309 > 0 && _iloop <= limit309) || (step309 < 0 && _iloop >= limit309); _iloop = ((int)(0 + _iloop + step309))) {
 //BA.debugLineNum = 366;BA.debugLine="Dim playerVal As Player";
_playerval = new pineysoft.squarepaddocks.player();
 //BA.debugLineNum = 367;BA.debugLine="playerVal.Initialize(\"Player \" & iLoop, playerColours.Get(iLoop - 1))";
_playerval._initialize(processBA,"Player "+BA.NumberToString(_iloop),(int)(BA.ObjectToNumber(_vvvvvvvvvvvvvv3.Get((int) (_iloop-1)))));
 //BA.debugLineNum = 368;BA.debugLine="If iLoop <= numberOfPlayers - numberOfDroids Then";
if (_iloop<=_vvvvvvvvvvvvvv5-_vvvvvvvvvvvvvv6) { 
 //BA.debugLineNum = 369;BA.debugLine="playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN";
_playerval._vvvv1 = _v6._player_type_human;
 }else {
 //BA.debugLineNum = 371;BA.debugLine="playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID";
_playerval._vvvv1 = _v6._player_type_droid;
 };
 //BA.debugLineNum = 373;BA.debugLine="Dim v As View = GetViewByTag1(pnlBase, \"P\" & iLoop)";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_v = _vvvvvvvvvvvvvv7((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"P"+BA.NumberToString(_iloop));
 //BA.debugLineNum = 374;BA.debugLine="If v Is Label Then";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 375;BA.debugLine="Dim lbl As Label = v";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl.setObject((android.widget.TextView)(_v.getObject()));
 //BA.debugLineNum = 376;BA.debugLine="If playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then";
if (_playerval._vvvv1==_v6._player_type_human) { 
 //BA.debugLineNum = 377;BA.debugLine="lbl.Text = \"Player \" & iLoop";
_lbl.setText((Object)("Player "+BA.NumberToString(_iloop)));
 }else {
 //BA.debugLineNum = 379;BA.debugLine="lbl.Text = \"Droid \" & iLoop";
_lbl.setText((Object)("Droid "+BA.NumberToString(_iloop)));
 };
 //BA.debugLineNum = 381;BA.debugLine="If iLoop <= 11 Then";
if (_iloop<=11) { 
 //BA.debugLineNum = 382;BA.debugLine="Dim vImage As View = GetViewByTag1(pnlBase, \"I\" & iLoop)";
_vimage = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_vimage = _vvvvvvvvvvvvvv7((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"I"+BA.NumberToString(_iloop));
 //BA.debugLineNum = 383;BA.debugLine="If vImage Is Label Then";
if (_vimage.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 384;BA.debugLine="Dim lblImage As Label = vImage";
_lblimage = new anywheresoftware.b4a.objects.LabelWrapper();
_lblimage.setObject((android.widget.TextView)(_vimage.getObject()));
 //BA.debugLineNum = 385;BA.debugLine="Dim imageNum As Int = Rnd(0,11)";
_imagenum = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (11));
 //BA.debugLineNum = 386;BA.debugLine="Do While imagesTaken.ContainsKey(imageNum)";
while (_imagestaken.ContainsKey((Object)(_imagenum))) {
 //BA.debugLineNum = 387;BA.debugLine="imageNum = Rnd(0,11)";
_imagenum = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (11));
 }
;
 //BA.debugLineNum = 389;BA.debugLine="imagesTaken.Put(imageNum, imageNum)";
_imagestaken.Put((Object)(_imagenum),(Object)(_imagenum));
 //BA.debugLineNum = 390;BA.debugLine="Dim bm As Bitmap";
_bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 391;BA.debugLine="bm.Initialize(File.DirAssets, \"monsterImage\" & (imageNum + 1) & \".png\")";
_bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"monsterImage"+BA.NumberToString((_imagenum+1))+".png");
 //BA.debugLineNum = 392;BA.debugLine="playerVal.PlayerImage = bm 'playerImages.Get(imageNum)";
_playerval._vvv0 = _bm;
 //BA.debugLineNum = 393;BA.debugLine="lblImage.SetBackgroundImage(playerVal.PlayerImage)";
_lblimage.SetBackgroundImage((android.graphics.Bitmap)(_playerval._vvv0.getObject()));
 //BA.debugLineNum = 394;BA.debugLine="If playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID Then";
if (_playerval._vvvv1==_v6._player_type_droid) { 
 //BA.debugLineNum = 395;BA.debugLine="lblImage.Text = \"D\"";
_lblimage.setText((Object)("D"));
 };
 };
 };
 };
 //BA.debugLineNum = 400;BA.debugLine="players.Add(playerVal)";
mostCurrent._vvvvvvvvvvvv2.Add((Object)(_playerval));
 }
};
 //BA.debugLineNum = 402;BA.debugLine="currentPlayer = 0";
_vvvvvvvvvvvv3 = (short) (0);
 //BA.debugLineNum = 403;BA.debugLine="Dim currPlayer As Player = players.get(currentPlayer)";
_currplayer = (pineysoft.squarepaddocks.player)(mostCurrent._vvvvvvvvvvvv2.Get((int) (_vvvvvvvvvvvv3)));
 //BA.debugLineNum = 404;BA.debugLine="btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)";
mostCurrent._btncurrplayer.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 //BA.debugLineNum = 405;BA.debugLine="If IsMaster Then";
if (_vv3) { 
 //BA.debugLineNum = 406;BA.debugLine="btnCurrPlayer.Text = \"M\"";
mostCurrent._btncurrplayer.setText((Object)("M"));
 }else {
 //BA.debugLineNum = 408;BA.debugLine="btnCurrPlayer.Text = \"S\"";
mostCurrent._btncurrplayer.setText((Object)("S"));
 };
 //BA.debugLineNum = 411;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvv4() throws Exception{
anywheresoftware.b4a.objects.collections.List _chains = null;
String _text = "";
pineysoft.squarepaddocks.gameactivity._mychain _item = null;
 //BA.debugLineNum = 1093;BA.debugLine="Public Sub DisplayChainCounts";
 //BA.debugLineNum = 1094;BA.debugLine="Dim chains As List = GetChainList";
_chains = new anywheresoftware.b4a.objects.collections.List();
_chains = _vvvvvvvvvvvvvv0();
 //BA.debugLineNum = 1095;BA.debugLine="Dim text As String";
_text = "";
 //BA.debugLineNum = 1096;BA.debugLine="For Each item As MyChain In chains";
final anywheresoftware.b4a.BA.IterableList group928 = _chains;
final int groupLen928 = group928.getSize();
for (int index928 = 0;index928 < groupLen928 ;index928++){
_item = (pineysoft.squarepaddocks.gameactivity._mychain)(group928.Get(index928));
 //BA.debugLineNum = 1097;BA.debugLine="text = text & item.square.RowPos & \", \" & item.square.ColPos & \" : \" & item.chainCount & CRLF";
_text = _text+BA.NumberToString(_item.square._vvvvv6)+", "+BA.NumberToString(_item.square._vvvvv5)+" : "+BA.NumberToString(_item.chainCount)+anywheresoftware.b4a.keywords.Common.CRLF;
 }
;
 //BA.debugLineNum = 1099;BA.debugLine="lblDebugDisplay.text = text";
mostCurrent._lbldebugdisplay.setText((Object)(_text));
 //BA.debugLineNum = 1100;BA.debugLine="lblDebugDisplay.Left = 0dip";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1101;BA.debugLine="lblDebugDisplay.Width = 100%x";
mostCurrent._lbldebugdisplay.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1102;BA.debugLine="lblDebugDisplay.Height = 100%y";
mostCurrent._lbldebugdisplay.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1103;BA.debugLine="lblDebugDisplay.Visible = True";
mostCurrent._lbldebugdisplay.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1104;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv3() throws Exception{
 //BA.debugLineNum = 1254;BA.debugLine="Sub DisplayFrontScreen";
 //BA.debugLineNum = 1255;BA.debugLine="If pnlBase.IsInitialized Then";
if (mostCurrent._pnlbase.IsInitialized()) { 
 //BA.debugLineNum = 1256;BA.debugLine="pnlBase.Left = 200%x";
mostCurrent._pnlbase.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (200),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 1258;BA.debugLine="pnlStartScreen.Left = 0dip";
mostCurrent._pnlstartscreen.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 1259;BA.debugLine="inGame = False";
_vvvvvvvv3 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 1260;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvv1() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _square = null;
int _rowloop = 0;
int _colloop = 0;
pineysoft.squarepaddocks.gamesquare _gsquare = null;
 //BA.debugLineNum = 487;BA.debugLine="Sub DrawBoard";
 //BA.debugLineNum = 488;BA.debugLine="Dim square As Rect";
_square = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 489;BA.debugLine="For rowLoop = 0 To gameHeight - 1";
{
final int step421 = 1;
final int limit421 = (int) (_vvvvvvvvvvv5-1);
for (_rowloop = (int) (0); (step421 > 0 && _rowloop <= limit421) || (step421 < 0 && _rowloop >= limit421); _rowloop = ((int)(0 + _rowloop + step421))) {
 //BA.debugLineNum = 490;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step422 = 1;
final int limit422 = (int) (_vvvvvvvvvvv6-1);
for (_colloop = (int) (0); (step422 > 0 && _colloop <= limit422) || (step422 < 0 && _colloop >= limit422); _colloop = ((int)(0 + _colloop + step422))) {
 //BA.debugLineNum = 491;BA.debugLine="Dim gSquare As GameSquare = gameSquares(rowLoop, colLoop)";
_gsquare = mostCurrent._vvvvvvvvvvv7[_rowloop][_colloop];
 //BA.debugLineNum = 493;BA.debugLine="square.Initialize(gSquare.TopLeft.Pos1-4dip,gSquare.TopLeft.Pos2-4dip,gSquare.TopLeft.Pos1+4dip,gSquare.TopLeft.Pos2+4dip)";
_square.Initialize((int) (_gsquare._vvvvv7._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv7._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv7._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv7._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 494;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 495;BA.debugLine="square.Initialize(gSquare.TopRight.Pos1-4dip,gSquare.TopRight.Pos2-4dip,gSquare.TopRight.Pos1+4dip,gSquare.TopRight.Pos2+4dip)";
_square.Initialize((int) (_gsquare._vvvvv0._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv0._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv0._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvv0._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 496;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 497;BA.debugLine="square.Initialize(gSquare.BottomLeft.Pos1-4dip,gSquare.BottomLeft.Pos2-4dip,gSquare.BottomLeft.Pos1+4dip,gSquare.BottomLeft.Pos2+4dip)";
_square.Initialize((int) (_gsquare._vvvvvv1._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv1._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv1._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv1._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 498;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 499;BA.debugLine="square.Initialize(gSquare.BottomRight.Pos1-4dip,gSquare.BottomRight.Pos2-4dip,gSquare.BottomRight.Pos1+4dip,gSquare.BottomRight.Pos2+4dip)";
_square.Initialize((int) (_gsquare._vvvvvv2._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv2._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv2._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_gsquare._vvvvvv2._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 500;BA.debugLine="canv.DrawRect(square, Colors.LightGray, True, 1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_square.getObject()),anywheresoftware.b4a.keywords.Common.Colors.LightGray,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 501;BA.debugLine="Log(\"Drawing \" & rowLoop & \", \" & colLoop)";
anywheresoftware.b4a.keywords.Common.Log("Drawing "+BA.NumberToString(_rowloop)+", "+BA.NumberToString(_colloop));
 }
};
 }
};
 //BA.debugLineNum = 504;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvv2(pineysoft.squarepaddocks.gamesquare _square) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _fillrect = null;
 //BA.debugLineNum = 1177;BA.debugLine="Sub EmptyTheSquare(square As GameSquare)";
 //BA.debugLineNum = 1178;BA.debugLine="Dim fillRect As Rect";
_fillrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1180;BA.debugLine="If square.fillLabel.IsInitialized Then";
if (_square._vvvvvv6.IsInitialized()) { 
 //BA.debugLineNum = 1181;BA.debugLine="square.fillLabel.SetBackgroundImage(Null)";
_square._vvvvvv6.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 }else {
 //BA.debugLineNum = 1183;BA.debugLine="fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.pos2  + 4dip, square.BottomRight.pos1  - 4dip, square.BottomRight.Pos2  - 4dip)";
_fillrect.Initialize((int) (_square._vvvvv7._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvv7._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvvv2._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvvv2._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 1184;BA.debugLine="canv.DrawRect(fillRect,SPConstants.BG_COLOUR,True,1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_fillrect.getObject()),_v6._bg_colour,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 };
 //BA.debugLineNum = 1186;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvv7(pineysoft.squarepaddocks.gamesquare _square,pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _fillrect = null;
anywheresoftware.b4a.objects.LabelWrapper _lblnew = null;
 //BA.debugLineNum = 1157;BA.debugLine="Sub FillTheSquare(square As GameSquare, currPlayer As Player)";
 //BA.debugLineNum = 1158;BA.debugLine="Dim fillRect As Rect";
_fillrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1160;BA.debugLine="If currPlayer.PlayerImage.IsInitialized Then";
if (_currplayer._vvv0.IsInitialized()) { 
 //BA.debugLineNum = 1161;BA.debugLine="If square.fillLabel.IsInitialized Then";
if (_square._vvvvvv6.IsInitialized()) { 
 //BA.debugLineNum = 1162;BA.debugLine="square.fillLabel.SetBackgroundImage(currPlayer.PlayerImage)";
_square._vvvvvv6.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 }else {
 //BA.debugLineNum = 1164;BA.debugLine="Dim lblNew As Label";
_lblnew = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1165;BA.debugLine="lblNew.Initialize(\"\")";
_lblnew.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1166;BA.debugLine="lblNew.SetBackgroundImage(currPlayer.PlayerImage)";
_lblnew.SetBackgroundImage((android.graphics.Bitmap)(_currplayer._vvv0.getObject()));
 //BA.debugLineNum = 1167;BA.debugLine="lblNew.Gravity = Gravity.FILL";
_lblnew.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 1168;BA.debugLine="square.fillLabel = lblNew";
_square._vvvvvv6 = _lblnew;
 //BA.debugLineNum = 1169;BA.debugLine="panel1.AddView(lblNew, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)";
mostCurrent._panel1.AddView((android.view.View)(_lblnew.getObject()),(int) (_square._vvvvv7._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvv7._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_vvvvvvvvvvvvvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(int) (_vvvvvvvvvvvvvv2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 };
 }else {
 //BA.debugLineNum = 1172;BA.debugLine="fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, square.BottomRight.Pos1  - 4dip, square.BottomRight.Pos2  - 4dip)";
_fillrect.Initialize((int) (_square._vvvvv7._vv0+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvv7._vvv1+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvvv2._vv0-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),(int) (_square._vvvvvv2._vvv1-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))));
 //BA.debugLineNum = 1173;BA.debugLine="canv.DrawRect(fillRect,currPlayer.colour,True,1dip)";
mostCurrent._vvvvvvvvvvvv5.DrawRect((android.graphics.Rect)(_fillrect.getObject()),_currplayer._vvv7,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 };
 //BA.debugLineNum = 1175;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _vvvvvvvvvvvvv6(int _checksides) throws Exception{
anywheresoftware.b4a.objects.collections.List _foundsquares = null;
int _row = 0;
int _col = 0;
 //BA.debugLineNum = 1240;BA.debugLine="Sub FindAllForSides(checkSides As Int) As List";
 //BA.debugLineNum = 1241;BA.debugLine="Dim foundSquares As List";
_foundsquares = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1242;BA.debugLine="foundSquares.Initialize";
_foundsquares.Initialize();
 //BA.debugLineNum = 1243;BA.debugLine="For row = 0 To gameHeight - 1";
{
final int step1052 = 1;
final int limit1052 = (int) (_vvvvvvvvvvv5-1);
for (_row = (int) (0); (step1052 > 0 && _row <= limit1052) || (step1052 < 0 && _row >= limit1052); _row = ((int)(0 + _row + step1052))) {
 //BA.debugLineNum = 1244;BA.debugLine="For col = 0 To gameWidth - 1";
{
final int step1053 = 1;
final int limit1053 = (int) (_vvvvvvvvvvv6-1);
for (_col = (int) (0); (step1053 > 0 && _col <= limit1053) || (step1053 < 0 && _col >= limit1053); _col = ((int)(0 + _col + step1053))) {
 //BA.debugLineNum = 1245;BA.debugLine="If gameSquares(row,col).sidesTaken = checkSides Then";
if (mostCurrent._vvvvvvvvvvv7[_row][_col]._vvvvvv5==_checksides) { 
 //BA.debugLineNum = 1246;BA.debugLine="foundSquares.Add(gameSquares(row,col))";
_foundsquares.Add((Object)(mostCurrent._vvvvvvvvvvv7[_row][_col]));
 };
 }
};
 }
};
 //BA.debugLineNum = 1251;BA.debugLine="Return foundSquares";
if (true) return _foundsquares;
 //BA.debugLineNum = 1252;BA.debugLine="End Sub";
return null;
}
public static pineysoft.squarepaddocks.gamesquare  _vvvvvvvvvvvv1(int _xpos,int _ypos) throws Exception{
int _colloop = 0;
int _rowloop = 0;
int _foundrow = 0;
int _foundcol = 0;
pineysoft.squarepaddocks.gamesquare _square = null;
 //BA.debugLineNum = 1188;BA.debugLine="Sub FindSelectedSquare(xpos As Int, ypos As Int) As GameSquare";
 //BA.debugLineNum = 1189;BA.debugLine="Dim colLoop As Int = 0";
_colloop = (int) (0);
 //BA.debugLineNum = 1190;BA.debugLine="Dim rowloop As Int = 0";
_rowloop = (int) (0);
 //BA.debugLineNum = 1191;BA.debugLine="Dim foundRow As Int = -1";
_foundrow = (int) (-1);
 //BA.debugLineNum = 1192;BA.debugLine="Dim foundCol As Int = -1";
_foundcol = (int) (-1);
 //BA.debugLineNum = 1193;BA.debugLine="Dim square As GameSquare";
_square = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1195;BA.debugLine="For rowloop = 0 To gameHeight - 1";
{
final int step1012 = 1;
final int limit1012 = (int) (_vvvvvvvvvvv5-1);
for (_rowloop = (int) (0); (step1012 > 0 && _rowloop <= limit1012) || (step1012 < 0 && _rowloop >= limit1012); _rowloop = ((int)(0 + _rowloop + step1012))) {
 //BA.debugLineNum = 1196;BA.debugLine="square = gameSquares(rowloop, 0)";
_square = mostCurrent._vvvvvvvvvvv7[_rowloop][(int) (0)];
 //BA.debugLineNum = 1197;BA.debugLine="If square.TopLeft.pos2 > ypos Then";
if (_square._vvvvv7._vvv1>_ypos) { 
 //BA.debugLineNum = 1198;BA.debugLine="If rowloop = 0 Then";
if (_rowloop==0) { 
 //BA.debugLineNum = 1199;BA.debugLine="foundRow = 0";
_foundrow = (int) (0);
 }else {
 //BA.debugLineNum = 1201;BA.debugLine="foundRow = rowloop - 1";
_foundrow = (int) (_rowloop-1);
 };
 //BA.debugLineNum = 1203;BA.debugLine="rowloop = gameHeight -1";
_rowloop = (int) (_vvvvvvvvvvv5-1);
 };
 }
};
 //BA.debugLineNum = 1207;BA.debugLine="If foundRow = -1 Then foundRow = gameHeight-1";
if (_foundrow==-1) { 
_foundrow = (int) (_vvvvvvvvvvv5-1);};
 //BA.debugLineNum = 1209;BA.debugLine="For colLoop = 0 To gameWidth - 1";
{
final int step1024 = 1;
final int limit1024 = (int) (_vvvvvvvvvvv6-1);
for (_colloop = (int) (0); (step1024 > 0 && _colloop <= limit1024) || (step1024 < 0 && _colloop >= limit1024); _colloop = ((int)(0 + _colloop + step1024))) {
 //BA.debugLineNum = 1210;BA.debugLine="square = gameSquares(foundRow, colLoop)";
_square = mostCurrent._vvvvvvvvvvv7[_foundrow][_colloop];
 //BA.debugLineNum = 1211;BA.debugLine="If square.TopLeft.pos1 > xpos  Then";
if (_square._vvvvv7._vv0>_xpos) { 
 //BA.debugLineNum = 1212;BA.debugLine="If colLoop = 0 Then";
if (_colloop==0) { 
 //BA.debugLineNum = 1213;BA.debugLine="foundCol = 0";
_foundcol = (int) (0);
 }else {
 //BA.debugLineNum = 1215;BA.debugLine="foundCol = colLoop - 1";
_foundcol = (int) (_colloop-1);
 };
 //BA.debugLineNum = 1217;BA.debugLine="colLoop = gameWidth - 1";
_colloop = (int) (_vvvvvvvvvvv6-1);
 };
 }
};
 //BA.debugLineNum = 1221;BA.debugLine="If foundCol = -1 Then foundCol = gameWidth - 1";
if (_foundcol==-1) { 
_foundcol = (int) (_vvvvvvvvvvv6-1);};
 //BA.debugLineNum = 1223;BA.debugLine="Return gameSquares(foundRow,foundCol)";
if (true) return mostCurrent._vvvvvvvvvvv7[_foundrow][_foundcol];
 //BA.debugLineNum = 1224;BA.debugLine="End Sub";
return null;
}
public static String  _gamescr_animationend() throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub GameScr_AnimationEnd";
 //BA.debugLineNum = 217;BA.debugLine="animStartScr.Stop(pnlStartScreen)";
mostCurrent._vvvvvvvvvv2.Stop((android.view.View)(mostCurrent._pnlstartscreen.getObject()));
 //BA.debugLineNum = 218;BA.debugLine="pnlStartScreen.Left = -100%x";
mostCurrent._pnlstartscreen.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 219;BA.debugLine="AnimGameScr.Stop(pnlBase)";
mostCurrent._vvvvvvvvvv3.Stop((android.view.View)(mostCurrent._pnlbase.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="pnlBase.Left = 0dip";
mostCurrent._pnlbase.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _vvvvvvvvvvvvvv0() throws Exception{
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
 //BA.debugLineNum = 987;BA.debugLine="Public Sub GetChainList() As List";
 //BA.debugLineNum = 988;BA.debugLine="Dim found2Sides As List = FindAllForSides(2)";
_found2sides = new anywheresoftware.b4a.objects.collections.List();
_found2sides = _vvvvvvvvvvvvv6((int) (2));
 //BA.debugLineNum = 989;BA.debugLine="Dim colPos As Int = 0";
_colpos = (int) (0);
 //BA.debugLineNum = 990;BA.debugLine="Dim rowPos As Int = 0";
_rowpos = (int) (0);
 //BA.debugLineNum = 991;BA.debugLine="Dim chainCounter As Int";
_chaincounter = 0;
 //BA.debugLineNum = 992;BA.debugLine="Dim reachedEnd As Boolean";
_reachedend = false;
 //BA.debugLineNum = 993;BA.debugLine="Dim checkSquare As GameSquare";
_checksquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 994;BA.debugLine="Dim startingSquare As GameSquare";
_startingsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 995;BA.debugLine="Dim chainList As List";
_chainlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 996;BA.debugLine="Dim edgeloop As Int";
_edgeloop = 0;
 //BA.debugLineNum = 997;BA.debugLine="Dim checkedSquares As Map";
_checkedsquares = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 998;BA.debugLine="Dim excludeEdgeNum As Int = -1";
_excludeedgenum = (int) (-1);
 //BA.debugLineNum = 999;BA.debugLine="Dim freeEdge As Int";
_freeedge = 0;
 //BA.debugLineNum = 1000;BA.debugLine="chainList.Initialize";
_chainlist.Initialize();
 //BA.debugLineNum = 1001;BA.debugLine="checkedSquares.Initialize";
_checkedsquares.Initialize();
 //BA.debugLineNum = 1003;BA.debugLine="For Each square As GameSquare In found2Sides";
final anywheresoftware.b4a.BA.IterableList group838 = _found2sides;
final int groupLen838 = group838.getSize();
for (int index838 = 0;index838 < groupLen838 ;index838++){
_square = (pineysoft.squarepaddocks.gamesquare)(group838.Get(index838));
 //BA.debugLineNum = 1004;BA.debugLine="startingSquare = square";
_startingsquare = _square;
 //BA.debugLineNum = 1005;BA.debugLine="excludeEdgeNum = -1";
_excludeedgenum = (int) (-1);
 //BA.debugLineNum = 1006;BA.debugLine="If checkedSquares.ContainsKey(square.rowPos & \"-\" & square.colPos) = False Then";
if (_checkedsquares.ContainsKey((Object)(BA.NumberToString(_square._vvvvv6)+"-"+BA.NumberToString(_square._vvvvv5)))==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1007;BA.debugLine="checkSquare = gameSquares(square.rowPos, square.colPos)";
_checksquare = mostCurrent._vvvvvvvvvvv7[_square._vvvvv6][_square._vvvvv5];
 //BA.debugLineNum = 1008;BA.debugLine="Dim freeEdges As List = checkSquare.GetFreeEdges(excludeEdgeNum)";
_freeedges = new anywheresoftware.b4a.objects.collections.List();
_freeedges = _checksquare._vvvv7(_excludeedgenum);
 //BA.debugLineNum = 1009;BA.debugLine="For edgeloop = 0 To freeEdges.Size - 1";
{
final int step844 = 1;
final int limit844 = (int) (_freeedges.getSize()-1);
for (_edgeloop = (int) (0); (step844 > 0 && _edgeloop <= limit844) || (step844 < 0 && _edgeloop >= limit844); _edgeloop = ((int)(0 + _edgeloop + step844))) {
 //BA.debugLineNum = 1010;BA.debugLine="checkSquare = gameSquares(square.rowPos, square.colPos)";
_checksquare = mostCurrent._vvvvvvvvvvv7[_square._vvvvv6][_square._vvvvv5];
 //BA.debugLineNum = 1011;BA.debugLine="chainCounter = 0";
_chaincounter = (int) (0);
 //BA.debugLineNum = 1012;BA.debugLine="Do While reachedEnd = False";
while (_reachedend==anywheresoftware.b4a.keywords.Common.False) {
 //BA.debugLineNum = 1013;BA.debugLine="rowPos = checkSquare.rowPos";
_rowpos = _checksquare._vvvvv6;
 //BA.debugLineNum = 1014;BA.debugLine="colPos = checkSquare.colPos";
_colpos = _checksquare._vvvvv5;
 //BA.debugLineNum = 1015;BA.debugLine="If chainCounter < 1 Then";
if (_chaincounter<1) { 
 //BA.debugLineNum = 1016;BA.debugLine="freeEdge = freeEdges.Get(edgeloop)";
_freeedge = (int)(BA.ObjectToNumber(_freeedges.Get(_edgeloop)));
 }else {
 //BA.debugLineNum = 1018;BA.debugLine="freeEdge = checkSquare.GetFreeEdges(excludeEdgeNum).Get(0)";
_freeedge = (int)(BA.ObjectToNumber(_checksquare._vvvv7(_excludeedgenum).Get((int) (0))));
 };
 //BA.debugLineNum = 1020;BA.debugLine="If checkSquare.sidesTaken <> 2 Then";
if (_checksquare._vvvvvv5!=2) { 
 //BA.debugLineNum = 1021;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1023;BA.debugLine="If freeEdge = SPConstants.TOP_SIDE Then";
if (_freeedge==_v6._top_side) { 
 //BA.debugLineNum = 1024;BA.debugLine="If rowPos = 0 Then";
if (_rowpos==0) { 
 //BA.debugLineNum = 1025;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1027;BA.debugLine="rowPos = rowPos - 1";
_rowpos = (int) (_rowpos-1);
 //BA.debugLineNum = 1028;BA.debugLine="excludeEdgeNum = SPConstants.BOTTOM_SIDE";
_excludeedgenum = _v6._bottom_side;
 };
 }else if(_freeedge==_v6._right_side) { 
 //BA.debugLineNum = 1031;BA.debugLine="If colPos = gameWidth - 1 Then";
if (_colpos==_vvvvvvvvvvv6-1) { 
 //BA.debugLineNum = 1032;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1034;BA.debugLine="colPos = colPos + 1";
_colpos = (int) (_colpos+1);
 //BA.debugLineNum = 1035;BA.debugLine="excludeEdgeNum = SPConstants.LEFT_SIDE";
_excludeedgenum = _v6._left_side;
 };
 }else if(_freeedge==_v6._bottom_side) { 
 //BA.debugLineNum = 1039;BA.debugLine="If rowPos = gameHeight - 1 Then";
if (_rowpos==_vvvvvvvvvvv5-1) { 
 //BA.debugLineNum = 1040;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1042;BA.debugLine="rowPos = rowPos + 1";
_rowpos = (int) (_rowpos+1);
 //BA.debugLineNum = 1043;BA.debugLine="excludeEdgeNum = SPConstants.TOP_SIDE";
_excludeedgenum = _v6._top_side;
 };
 }else {
 //BA.debugLineNum = 1046;BA.debugLine="If colPos = 0 Then";
if (_colpos==0) { 
 //BA.debugLineNum = 1047;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1049;BA.debugLine="colPos = colPos - 1";
_colpos = (int) (_colpos-1);
 //BA.debugLineNum = 1050;BA.debugLine="excludeEdgeNum = SPConstants.RIGHT_SIDE";
_excludeedgenum = _v6._right_side;
 };
 };
 };
 //BA.debugLineNum = 1054;BA.debugLine="If chainCounter > 1 AND checkSquare.rowPos = startingSquare.rowPos AND checkSquare.colPos = startingSquare.colPos Then";
if (_chaincounter>1 && _checksquare._vvvvv6==_startingsquare._vvvvv6 && _checksquare._vvvvv5==_startingsquare._vvvvv5) { 
 //BA.debugLineNum = 1055;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1056;BA.debugLine="edgeloop = freeEdges.Size - 1";
_edgeloop = (int) (_freeedges.getSize()-1);
 }else {
 //BA.debugLineNum = 1058;BA.debugLine="checkedSquares.Put(checkSquare.rowPos & \"-\" & checkSquare.colPos, checkSquare)";
_checkedsquares.Put((Object)(BA.NumberToString(_checksquare._vvvvv6)+"-"+BA.NumberToString(_checksquare._vvvvv5)),(Object)(_checksquare));
 //BA.debugLineNum = 1059;BA.debugLine="chainCounter = chainCounter + 1";
_chaincounter = (int) (_chaincounter+1);
 };
 //BA.debugLineNum = 1061;BA.debugLine="If reachedEnd <> True Then";
if (_reachedend!=anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1062;BA.debugLine="If gameSquares(rowPos,colPos).sidesTaken = 2 Then";
if (mostCurrent._vvvvvvvvvvv7[_rowpos][_colpos]._vvvvvv5==2) { 
 //BA.debugLineNum = 1063;BA.debugLine="checkSquare = gameSquares(rowPos,colPos)";
_checksquare = mostCurrent._vvvvvvvvvvv7[_rowpos][_colpos];
 }else {
 //BA.debugLineNum = 1065;BA.debugLine="reachedEnd = True";
_reachedend = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 }
;
 //BA.debugLineNum = 1069;BA.debugLine="Dim newChain As MyChain";
_newchain = new pineysoft.squarepaddocks.gameactivity._mychain();
 //BA.debugLineNum = 1070;BA.debugLine="newChain.Initialize()";
_newchain.Initialize();
 //BA.debugLineNum = 1071;BA.debugLine="newChain.square = square";
_newchain.square = _square;
 //BA.debugLineNum = 1072;BA.debugLine="newChain.chainCount = chainCounter";
_newchain.chainCount = _chaincounter;
 //BA.debugLineNum = 1073;BA.debugLine="If chainList.Size > 0 Then";
if (_chainlist.getSize()>0) { 
 //BA.debugLineNum = 1074;BA.debugLine="Dim tempitem As MyChain = chainList.Get(chainList.Size -1)";
_tempitem = (pineysoft.squarepaddocks.gameactivity._mychain)(_chainlist.Get((int) (_chainlist.getSize()-1)));
 //BA.debugLineNum = 1075;BA.debugLine="If tempitem.square.rowPos = square.rowPos AND tempitem.square.colPos = square.colPos Then";
if (_tempitem.square._vvvvv6==_square._vvvvv6 && _tempitem.square._vvvvv5==_square._vvvvv5) { 
 //BA.debugLineNum = 1076;BA.debugLine="tempitem.chainCount = tempitem.chainCount + chainCounter - 1";
_tempitem.chainCount = (int) (_tempitem.chainCount+_chaincounter-1);
 }else {
 //BA.debugLineNum = 1078;BA.debugLine="chainList.Add(newChain)";
_chainlist.Add((Object)(_newchain));
 };
 }else {
 //BA.debugLineNum = 1081;BA.debugLine="chainList.Add(newChain)";
_chainlist.Add((Object)(_newchain));
 };
 //BA.debugLineNum = 1084;BA.debugLine="reachedEnd = False";
_reachedend = anywheresoftware.b4a.keywords.Common.False;
 }
};
 //BA.debugLineNum = 1086;BA.debugLine="checkedSquares.Put(square.rowPos & \"-\" & square.colPos, square)";
_checkedsquares.Put((Object)(BA.NumberToString(_square._vvvvv6)+"-"+BA.NumberToString(_square._vvvvv5)),(Object)(_square));
 };
 }
;
 //BA.debugLineNum = 1089;BA.debugLine="chainList.SortType(\"chainCount\", True)";
_chainlist.SortType("chainCount",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1090;BA.debugLine="Return chainList";
if (true) return _chainlist;
 //BA.debugLineNum = 1091;BA.debugLine="End Sub";
return null;
}
public static int  _vvvvvvvvvvvvvvv3(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side) throws Exception{
pineysoft.squarepaddocks.gamesquare _checksquare = null;
 //BA.debugLineNum = 1106;BA.debugLine="Sub GetOtherSideSquareSides(currentSquare As GameSquare, side As Int) As Int";
 //BA.debugLineNum = 1108;BA.debugLine="Dim checkSquare As GameSquare = SubGetOppositeSquare(currentSquare, side)";
_checksquare = _vvvvvvvvvvvvvvv4(_currentsquare,_side);
 //BA.debugLineNum = 1110;BA.debugLine="If checkSquare.IsInitialized Then";
if (_checksquare.IsInitialized()) { 
 //BA.debugLineNum = 1111;BA.debugLine="Return checkSquare.sidesTaken";
if (true) return _checksquare._vvvvvv5;
 }else {
 //BA.debugLineNum = 1113;BA.debugLine="Return -1";
if (true) return (int) (-1);
 };
 //BA.debugLineNum = 1116;BA.debugLine="End Sub";
return 0;
}
public static anywheresoftware.b4a.objects.ConcreteViewWrapper  _vvvvvvvvvvvvvv7(anywheresoftware.b4a.objects.ActivityWrapper _searchview,String _tag) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _tempview = null;
int _vwloop = 0;
 //BA.debugLineNum = 1227;BA.debugLine="Sub GetViewByTag1(searchView As Activity, tag As String) As View";
 //BA.debugLineNum = 1229;BA.debugLine="Dim tempView As View";
_tempview = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1230;BA.debugLine="Dim vwloop As Int";
_vwloop = 0;
 //BA.debugLineNum = 1231;BA.debugLine="For vwloop = 0 To searchView.NumberOfViews - 1";
{
final int step1041 = 1;
final int limit1041 = (int) (_searchview.getNumberOfViews()-1);
for (_vwloop = (int) (0); (step1041 > 0 && _vwloop <= limit1041) || (step1041 < 0 && _vwloop >= limit1041); _vwloop = ((int)(0 + _vwloop + step1041))) {
 //BA.debugLineNum = 1232;BA.debugLine="tempView = searchView.GetView(vwloop)";
_tempview = _searchview.GetView(_vwloop);
 //BA.debugLineNum = 1233;BA.debugLine="If tempView.tag = tag Then";
if ((_tempview.getTag()).equals((Object)(_tag))) { 
 //BA.debugLineNum = 1234;BA.debugLine="Return tempView";
if (true) return _tempview;
 };
 }
};
 //BA.debugLineNum = 1237;BA.debugLine="Return tempView";
if (true) return _tempview;
 //BA.debugLineNum = 1238;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Private gameSquares(,) As GameSquare";
mostCurrent._vvvvvvvvvvv7 = new pineysoft.squarepaddocks.gamesquare[(int) (0)][];
{
int d0 = mostCurrent._vvvvvvvvvvv7.length;
int d1 = (int) (0);
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._vvvvvvvvvvv7[i0] = new pineysoft.squarepaddocks.gamesquare[d1];
for (int i1 = 0;i1 < d1;i1++) {
mostCurrent._vvvvvvvvvvv7[i0][i1] = new pineysoft.squarepaddocks.gamesquare();
}
}
}
;
 //BA.debugLineNum = 29;BA.debugLine="Dim gameTurns As List";
mostCurrent._vvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 30;BA.debugLine="Private gameWidth As Int = 7";
_vvvvvvvvvvv6 = (int) (7);
 //BA.debugLineNum = 31;BA.debugLine="Private gameHeight As Int = 9";
_vvvvvvvvvvv5 = (int) (9);
 //BA.debugLineNum = 32;BA.debugLine="Private columnSpacing As Int";
_vvvvvvvvvvvvvv1 = 0;
 //BA.debugLineNum = 33;BA.debugLine="Private rowSpacing As Int";
_vvvvvvvvvvvvvv2 = 0;
 //BA.debugLineNum = 34;BA.debugLine="Private panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private canv As Canvas";
mostCurrent._vvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private players As List";
mostCurrent._vvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 37;BA.debugLine="Private playerImages As List";
mostCurrent._vvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 38;BA.debugLine="Private checkBoxImages As List";
mostCurrent._vvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.collections.List();
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
mostCurrent._vvvvvvvv5 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Dim anim2 As AnimationPlus";
mostCurrent._vvvvvvvv7 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim anim3 As AnimationPlus";
mostCurrent._vvvvvvvvv1 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Dim anim4 As AnimationPlus";
mostCurrent._vvvvvvvvv3 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Dim anim5 As AnimationPlus";
mostCurrent._vvvvvvvvv5 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Dim anim6 As AnimationPlus";
mostCurrent._vvvvvvvvv7 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim animShading As AnimationPlus";
mostCurrent._vvvvvvvvvvv1 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Dim animPanel1 As AnimationPlus";
mostCurrent._vvvvvvvvvv5 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim animPanel2 As AnimationPlus";
mostCurrent._vvvvvvvvvv7 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Dim animStartScr As AnimationPlus";
mostCurrent._vvvvvvvvvv2 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Dim AnimGameScr As AnimationPlus";
mostCurrent._vvvvvvvvvv3 = new flm.b4a.animationplus.AnimationPlusWrapper();
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
_vvvvvvvv6 = 0;
 //BA.debugLineNum = 90;BA.debugLine="Dim start2 As Int";
_vvvvvvvv0 = 0;
 //BA.debugLineNum = 91;BA.debugLine="Dim start3 As Int";
_vvvvvvvvv2 = 0;
 //BA.debugLineNum = 92;BA.debugLine="Dim start4 As Int";
_vvvvvvvvv4 = 0;
 //BA.debugLineNum = 93;BA.debugLine="Dim start5 As Int";
_vvvvvvvvv6 = 0;
 //BA.debugLineNum = 94;BA.debugLine="Dim start6 As Int";
_vvvvvvvvv0 = 0;
 //BA.debugLineNum = 95;BA.debugLine="Private chkSounds As ImageView";
mostCurrent._chksounds = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvv7() throws Exception{
 //BA.debugLineNum = 413;BA.debugLine="Public Sub InitGamePlay";
 //BA.debugLineNum = 415;BA.debugLine="GameMode = SPConstants.GAMETYPE_MODE_LOC";
_vv2 = _v6._gametype_mode_loc;
 //BA.debugLineNum = 416;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_vv2).equals(_v6._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 417;BA.debugLine="numberOfPlayers = 1";
_vvvvvvvvvvvvvv5 = (int) (1);
 //BA.debugLineNum = 418;BA.debugLine="numberOfDroids = 0";
_vvvvvvvvvvvvvv6 = (int) (0);
 }else {
 //BA.debugLineNum = 420;BA.debugLine="numberOfPlayers = spnPlayers.SelectedItem";
_vvvvvvvvvvvvvv5 = (int)(Double.parseDouble(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 421;BA.debugLine="numberOfDroids = spnDroids.SelectedItem";
_vvvvvvvvvvvvvv6 = (int)(Double.parseDouble(mostCurrent._spndroids.getSelectedItem()));
 };
 //BA.debugLineNum = 423;BA.debugLine="gameHeight = sbRows.Value + 4";
_vvvvvvvvvvv5 = (int) (mostCurrent._sbrows.getValue()+4);
 //BA.debugLineNum = 424;BA.debugLine="gameWidth = sbColumns.Value + 4";
_vvvvvvvvvvv6 = (int) (mostCurrent._sbcolumns.getValue()+4);
 //BA.debugLineNum = 425;BA.debugLine="columnSpacing = panel1.Width / (gameWidth + 1)";
_vvvvvvvvvvvvvv1 = (int) (mostCurrent._panel1.getWidth()/(double)(_vvvvvvvvvvv6+1));
 //BA.debugLineNum = 426;BA.debugLine="rowSpacing = panel1.Height / (gameHeight + 1)";
_vvvvvvvvvvvvvv2 = (int) (mostCurrent._panel1.getHeight()/(double)(_vvvvvvvvvvv5+1));
 //BA.debugLineNum = 427;BA.debugLine="canv.Initialize(panel1)";
mostCurrent._vvvvvvvvvvvv5.Initialize((android.view.View)(mostCurrent._panel1.getObject()));
 //BA.debugLineNum = 428;BA.debugLine="gameTurns.Initialize";
mostCurrent._vvvvvvvvvvvvvvv5.Initialize();
 //BA.debugLineNum = 430;BA.debugLine="CreateBoard";
_vvvvvvvvvvvvv0();
 //BA.debugLineNum = 431;BA.debugLine="DrawBoard";
_vvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 432;BA.debugLine="CreatePlayers";
_vvvvvvvvvvvvvv4();
 //BA.debugLineNum = 433;BA.debugLine="SetDifficulty";
_vvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 434;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvv2() throws Exception{
 //BA.debugLineNum = 356;BA.debugLine="Sub InitialiseSounds";
 //BA.debugLineNum = 357;BA.debugLine="sounds.Initialize(10)";
_v5.Initialize((int) (10));
 //BA.debugLineNum = 358;BA.debugLine="giggleSound = sounds.Load(File.DirAssets, \"Giggle1.mp3\")";
_vvvvvvvvvvvvv2 = _v5.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Giggle1.mp3");
 //BA.debugLineNum = 359;BA.debugLine="End Sub";
return "";
}
public static String  _lasticon_animationend() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Sub LastIcon_AnimationEnd";
 //BA.debugLineNum = 224;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 225;BA.debugLine="anim1.Stop(icon1)";
mostCurrent._vvvvvvvv5.Stop((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 226;BA.debugLine="anim2.Stop(icon2)";
mostCurrent._vvvvvvvv7.Stop((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 227;BA.debugLine="anim3.Stop(icon3)";
mostCurrent._vvvvvvvvv1.Stop((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 228;BA.debugLine="anim4.Stop(icon4)";
mostCurrent._vvvvvvvvv3.Stop((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 229;BA.debugLine="anim5.Stop(icon5)";
mostCurrent._vvvvvvvvv5.Stop((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 230;BA.debugLine="anim6.Stop(icon6)";
mostCurrent._vvvvvvvvv7.Stop((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 231;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 232;BA.debugLine="SetIconAtPosition(icon1,75%x-(icon1.Width/2),25%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon1,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)-(mostCurrent._icon1.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 233;BA.debugLine="Log(\"Icon1 \" & icon1.Top & \" : \" & icon1.left)";
anywheresoftware.b4a.keywords.Common.Log("Icon1 "+BA.NumberToString(mostCurrent._icon1.getTop())+" : "+BA.NumberToString(mostCurrent._icon1.getLeft()));
 //BA.debugLineNum = 234;BA.debugLine="SetIconAtPosition(icon2,25%x-(icon2.Width/2),45%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon2,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)-(mostCurrent._icon2.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 235;BA.debugLine="SetIconAtPosition(icon3,50%x-(icon3.Width/2),25%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon3,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-(mostCurrent._icon3.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 236;BA.debugLine="SetIconAtPosition(icon4,50%x-(icon4.Width/2),45%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon4,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-(mostCurrent._icon4.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 237;BA.debugLine="SetIconAtPosition(icon5,25%x-(icon5.Width/2),25%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon5,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)-(mostCurrent._icon5.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 238;BA.debugLine="SetIconAtPosition(icon6,75%x-(icon6.Width/2),45%y)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon6,(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)-(mostCurrent._icon6.getWidth()/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 239;BA.debugLine="If pnlSelectionLeft.Left <= 0dip Then";
if (mostCurrent._pnlselectionleft.getLeft()<=anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0))) { 
 //BA.debugLineNum = 240;BA.debugLine="AnimateShading";
_vvvvvvvvvv0();
 };
 //BA.debugLineNum = 242;BA.debugLine="End Sub";
return "";
}
public static String  _lasticonend_animationend() throws Exception{
 //BA.debugLineNum = 193;BA.debugLine="Sub LastIconEnd_AnimationEnd";
 //BA.debugLineNum = 194;BA.debugLine="Activity.LoadLayout(\"layout1\")";
mostCurrent._activity.LoadLayout("layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 195;BA.debugLine="Activity.LoadLayout(\"winnerScreen\")";
mostCurrent._activity.LoadLayout("winnerScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 196;BA.debugLine="pnlOuter.Left = -100%x";
mostCurrent._pnlouter.setLeft((int) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 198;BA.debugLine="inGame = True";
_vvvvvvvv3 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 199;BA.debugLine="InitGamePlay";
_vvvvvvvvvvvvvvv7();
 //BA.debugLineNum = 200;BA.debugLine="SaveDefaults";
_vvvvvvvvvvvvvvvv2();
 //BA.debugLineNum = 201;BA.debugLine="AnimateGameScreens";
_vvvvvvvvvv1();
 //BA.debugLineNum = 202;BA.debugLine="End Sub";
return "";
}
public static String  _lbldebugdisplay_click() throws Exception{
 //BA.debugLineNum = 1456;BA.debugLine="Sub lblDebugDisplay_Click";
 //BA.debugLineNum = 1457;BA.debugLine="lblDebugDisplay.Left = 100%x";
mostCurrent._lbldebugdisplay.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1458;BA.debugLine="lblDebugDisplay.Visible = False";
mostCurrent._lbldebugdisplay.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1459;BA.debugLine="End Sub";
return "";
}
public static String  _leftpanel_animationend() throws Exception{
 //BA.debugLineNum = 286;BA.debugLine="Sub LeftPanel_AnimationEnd";
 //BA.debugLineNum = 287;BA.debugLine="Log(\"Start Right Panel Anim\")";
anywheresoftware.b4a.keywords.Common.Log("Start Right Panel Anim");
 //BA.debugLineNum = 288;BA.debugLine="AnimatePanelRight";
_vvvvvvvvvv6();
 //BA.debugLineNum = 289;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv6() throws Exception{
boolean _moreimages = false;
int _imageloop = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm2 = null;
 //BA.debugLineNum = 317;BA.debugLine="Sub LoadImages";
 //BA.debugLineNum = 318;BA.debugLine="Dim moreImages As Boolean = True";
_moreimages = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 319;BA.debugLine="Dim imageLoop As Int = 1";
_imageloop = (int) (1);
 //BA.debugLineNum = 321;BA.debugLine="playerImages.Initialize";
mostCurrent._vvvvvvvvvvvvvvv6.Initialize();
 //BA.debugLineNum = 334;BA.debugLine="checkBoxImages.Initialize";
mostCurrent._vvvvvvvvvvvvv5.Initialize();
 //BA.debugLineNum = 335;BA.debugLine="Dim bm As Bitmap";
_bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 336;BA.debugLine="bm.Initialize(File.DirAssets, \"checkboxOn.png\")";
_bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkboxOn.png");
 //BA.debugLineNum = 337;BA.debugLine="checkBoxImages.Add(bm)";
mostCurrent._vvvvvvvvvvvvv5.Add((Object)(_bm.getObject()));
 //BA.debugLineNum = 338;BA.debugLine="Dim bm2 As Bitmap";
_bm2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 339;BA.debugLine="bm2.Initialize(File.DirAssets, \"checkboxOff.png\")";
_bm2.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkboxOff.png");
 //BA.debugLineNum = 340;BA.debugLine="checkBoxImages.Add(bm2)";
mostCurrent._vvvvvvvvvvvvv5.Add((Object)(_bm2.getObject()));
 //BA.debugLineNum = 341;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv7() throws Exception{
 //BA.debugLineNum = 343;BA.debugLine="Sub LoadSpinners";
 //BA.debugLineNum = 345;BA.debugLine="spnPlayers.AddAll(Array As Int(2,3,4))";
mostCurrent._spnplayers.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new int[]{(int) (2),(int) (3),(int) (4)}));
 //BA.debugLineNum = 346;BA.debugLine="spnDroids.AddAll(Array As Int(0,1))";
mostCurrent._spndroids.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new int[]{(int) (0),(int) (1)}));
 //BA.debugLineNum = 347;BA.debugLine="spnDifficulty.AddAll(Array As String(\"Easy\",\"Medium\",\"Hard\"))";
mostCurrent._spndifficulty.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Easy","Medium","Hard"}));
 //BA.debugLineNum = 349;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvv1(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.collections.List _found3s = null;
anywheresoftware.b4a.objects.collections.List _found2s = null;
anywheresoftware.b4a.objects.collections.List _found1s = null;
anywheresoftware.b4a.objects.collections.List _found0s = null;
boolean _sideclaimed = false;
int _numberclosed = 0;
boolean _exitloop = false;
boolean _forceanyalways = false;
 //BA.debugLineNum = 682;BA.debugLine="Sub MakeDroidMove(currPlayer As Player)";
 //BA.debugLineNum = 683;BA.debugLine="Dim found3s As List";
_found3s = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 684;BA.debugLine="Dim found2s As List = FindAllForSides(2)";
_found2s = new anywheresoftware.b4a.objects.collections.List();
_found2s = _vvvvvvvvvvvvv6((int) (2));
 //BA.debugLineNum = 685;BA.debugLine="Dim found1s As List = FindAllForSides(1)";
_found1s = new anywheresoftware.b4a.objects.collections.List();
_found1s = _vvvvvvvvvvvvv6((int) (1));
 //BA.debugLineNum = 686;BA.debugLine="Dim found0s As List = FindAllForSides(0)";
_found0s = new anywheresoftware.b4a.objects.collections.List();
_found0s = _vvvvvvvvvvvvv6((int) (0));
 //BA.debugLineNum = 687;BA.debugLine="Dim sideClaimed As Boolean";
_sideclaimed = false;
 //BA.debugLineNum = 688;BA.debugLine="Dim numberClosed As Int = 1";
_numberclosed = (int) (1);
 //BA.debugLineNum = 689;BA.debugLine="Dim exitLoop As Boolean";
_exitloop = false;
 //BA.debugLineNum = 690;BA.debugLine="Dim forceAnyAlways As Boolean";
_forceanyalways = false;
 //BA.debugLineNum = 692;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_EASY Then";
if ((_v7).equals(_v6._difficulty_easy)) { 
 //BA.debugLineNum = 693;BA.debugLine="forceAnyAlways = True";
_forceanyalways = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 696;BA.debugLine="Do While exitLoop = False";
while (_exitloop==anywheresoftware.b4a.keywords.Common.False) {
 //BA.debugLineNum = 697;BA.debugLine="found3s = FindAllForSides(3)";
_found3s = _vvvvvvvvvvvvv6((int) (3));
 //BA.debugLineNum = 698;BA.debugLine="If found3s.Size > 0 Then";
if (_found3s.getSize()>0) { 
 //BA.debugLineNum = 699;BA.debugLine="TakeEasy3s(found3s, currPlayer)";
_vvvvvvvvvvvvvvvv3(_found3s,_currplayer);
 //BA.debugLineNum = 700;BA.debugLine="Log(\"Checking Doubles\")";
anywheresoftware.b4a.keywords.Common.Log("Checking Doubles");
 //BA.debugLineNum = 701;BA.debugLine="TakeDoubles";
_vvvvvvvvvvvvvvvv4();
 //BA.debugLineNum = 704;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _vvvvvvvvvvvv7(_currplayer);
 //BA.debugLineNum = 705;BA.debugLine="If numberClosed > 0 Then";
if (_numberclosed>0) { 
 //BA.debugLineNum = 706;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._vvv6 = (int) (_currplayer._vvv6+_numberclosed);
 }else {
 //BA.debugLineNum = 708;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 712;BA.debugLine="If CheckAndDisplayWinnerScreen Then";
if (_vvvvvvvvvvvvv4()) { 
 //BA.debugLineNum = 713;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 }else {
 //BA.debugLineNum = 716;BA.debugLine="exitLoop = True";
_exitloop = anywheresoftware.b4a.keywords.Common.True;
 };
 }
;
 //BA.debugLineNum = 720;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_EASY Then";
if ((_v7).equals(_v6._difficulty_easy)) { 
 //BA.debugLineNum = 721;BA.debugLine="If Rnd(1,3) = 1 Then";
if (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (3))==1) { 
 //BA.debugLineNum = 722;BA.debugLine="If found2s.Size > 0 Then";
if (_found2s.getSize()>0) { 
 //BA.debugLineNum = 723;BA.debugLine="Log(\"Doing easy Move\")";
anywheresoftware.b4a.keywords.Common.Log("Doing easy Move");
 //BA.debugLineNum = 724;BA.debugLine="sideClaimed = TakeSingle(found2s, True)";
_sideclaimed = _vvvvvvvvvvvvvvvv5(_found2s,anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 729;BA.debugLine="If sideClaimed = False AND found0s.Size > 0 Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False && _found0s.getSize()>0) { 
 //BA.debugLineNum = 730;BA.debugLine="Log(\"Checking 0's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 0's");
 //BA.debugLineNum = 731;BA.debugLine="If TakeSingle(found0s, forceAnyAlways) = False Then";
if (_vvvvvvvvvvvvvvvv5(_found0s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 732;BA.debugLine="Log(\"Checking 0's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 0's forced");
 //BA.debugLineNum = 733;BA.debugLine="sideClaimed = TakeSingle(found0s, True)";
_sideclaimed = _vvvvvvvvvvvvvvvv5(_found0s,anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 735;BA.debugLine="sideClaimed = True";
_sideclaimed = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 738;BA.debugLine="If sideClaimed = False AND found1s.Size > 0 Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False && _found1s.getSize()>0) { 
 //BA.debugLineNum = 739;BA.debugLine="Log(\"Checking 1's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 1's");
 //BA.debugLineNum = 740;BA.debugLine="If TakeSingle(found1s , forceAnyAlways) = False Then";
if (_vvvvvvvvvvvvvvvv5(_found1s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 741;BA.debugLine="If difficulty <> SPConstants.DIFFICULTY_HARD Then";
if ((_v7).equals(_v6._difficulty_hard) == false) { 
 //BA.debugLineNum = 742;BA.debugLine="Log(\"Checking 1's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 1's forced");
 //BA.debugLineNum = 743;BA.debugLine="sideClaimed = TakeSingle(found1s, True)";
_sideclaimed = _vvvvvvvvvvvvvvvv5(_found1s,anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 746;BA.debugLine="sideClaimed = True";
_sideclaimed = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 749;BA.debugLine="If sideClaimed = False Then";
if (_sideclaimed==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 750;BA.debugLine="Log(\"Checking 2's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 2's");
 //BA.debugLineNum = 751;BA.debugLine="If difficulty = SPConstants.DIFFICULTY_HARD Then";
if ((_v7).equals(_v6._difficulty_hard)) { 
 //BA.debugLineNum = 752;BA.debugLine="TakeSingle2 ' this one checks the chain count to get the least first";
_vvvvvvvvvvvvvvvv6();
 }else {
 //BA.debugLineNum = 754;BA.debugLine="If TakeSingle(found2s, forceAnyAlways) = False Then";
if (_vvvvvvvvvvvvvvvv5(_found2s,_forceanyalways)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 755;BA.debugLine="Log(\"Checking 2's forced\")";
anywheresoftware.b4a.keywords.Common.Log("Checking 2's forced");
 //BA.debugLineNum = 756;BA.debugLine="TakeSingle(found2s, True)";
_vvvvvvvvvvvvvvvv5(_found2s,anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 761;BA.debugLine="Dim numberClosed As Int = CloseCompletedSquares(currPlayer)";
_numberclosed = _vvvvvvvvvvvv7(_currplayer);
 //BA.debugLineNum = 762;BA.debugLine="If numberClosed > 0 Then";
if (_numberclosed>0) { 
 //BA.debugLineNum = 763;BA.debugLine="currPlayer.Score = currPlayer.Score + numberClosed";
_currplayer._vvv6 = (int) (_currplayer._vvv6+_numberclosed);
 };
 //BA.debugLineNum = 766;BA.debugLine="If currPlayer.Score > 0 Then";
if (_currplayer._vvv6>0) { 
 //BA.debugLineNum = 767;BA.debugLine="UpdateScore(currPlayer)";
_vvvvvvvvvvvvv3(_currplayer);
 };
 //BA.debugLineNum = 770;BA.debugLine="CheckAndDisplayWinnerScreen";
_vvvvvvvvvvvvv4();
 //BA.debugLineNum = 772;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvv7(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side,boolean _marktaken) throws Exception{
int _foundcol = 0;
int _foundrow = 0;
int _updateside = 0;
 //BA.debugLineNum = 595;BA.debugLine="Public Sub MarkOtherSide(currentSquare As GameSquare, side As Int, markTaken As Boolean)";
 //BA.debugLineNum = 596;BA.debugLine="Dim foundCol As Int = -1";
_foundcol = (int) (-1);
 //BA.debugLineNum = 597;BA.debugLine="Dim foundRow As Int = -1";
_foundrow = (int) (-1);
 //BA.debugLineNum = 598;BA.debugLine="Dim updateSide As Int";
_updateside = 0;
 //BA.debugLineNum = 600;BA.debugLine="foundRow = currentSquare.RowPos";
_foundrow = _currentsquare._vvvvv6;
 //BA.debugLineNum = 601;BA.debugLine="foundCol = currentSquare.ColPos";
_foundcol = _currentsquare._vvvvv5;
 //BA.debugLineNum = 604;BA.debugLine="Select side";
switch (BA.switchObjectToInt(_side,_v6._top_side,_v6._right_side,_v6._bottom_side,_v6._left_side)) {
case 0:
 //BA.debugLineNum = 606;BA.debugLine="If foundRow > 0 Then";
if (_foundrow>0) { 
 //BA.debugLineNum = 607;BA.debugLine="foundRow = foundRow - 1";
_foundrow = (int) (_foundrow-1);
 //BA.debugLineNum = 608;BA.debugLine="updateSide = SPConstants.BOTTOM_SIDE";
_updateside = _v6._bottom_side;
 }else {
 //BA.debugLineNum = 610;BA.debugLine="foundRow = -1";
_foundrow = (int) (-1);
 };
 break;
case 1:
 //BA.debugLineNum = 613;BA.debugLine="If foundCol < gameWidth - 1 Then";
if (_foundcol<_vvvvvvvvvvv6-1) { 
 //BA.debugLineNum = 614;BA.debugLine="foundCol = foundCol + 1";
_foundcol = (int) (_foundcol+1);
 //BA.debugLineNum = 615;BA.debugLine="updateSide = SPConstants.LEFT_SIDE";
_updateside = _v6._left_side;
 }else {
 //BA.debugLineNum = 617;BA.debugLine="foundCol = -1";
_foundcol = (int) (-1);
 };
 break;
case 2:
 //BA.debugLineNum = 621;BA.debugLine="If foundRow < gameHeight - 1 Then";
if (_foundrow<_vvvvvvvvvvv5-1) { 
 //BA.debugLineNum = 622;BA.debugLine="foundRow = foundRow + 1";
_foundrow = (int) (_foundrow+1);
 //BA.debugLineNum = 623;BA.debugLine="updateSide = SPConstants.TOP_SIDE";
_updateside = _v6._top_side;
 }else {
 //BA.debugLineNum = 625;BA.debugLine="foundRow = -1";
_foundrow = (int) (-1);
 };
 break;
case 3:
 //BA.debugLineNum = 628;BA.debugLine="If foundCol > 0 Then";
if (_foundcol>0) { 
 //BA.debugLineNum = 629;BA.debugLine="foundCol = foundCol - 1";
_foundcol = (int) (_foundcol-1);
 //BA.debugLineNum = 630;BA.debugLine="updateSide = SPConstants.RIGHT_SIDE";
_updateside = _v6._right_side;
 }else {
 //BA.debugLineNum = 632;BA.debugLine="foundCol = -1";
_foundcol = (int) (-1);
 };
 break;
}
;
 //BA.debugLineNum = 636;BA.debugLine="If foundRow <> -1 AND foundCol <> -1 Then";
if (_foundrow!=-1 && _foundcol!=-1) { 
 //BA.debugLineNum = 637;BA.debugLine="gameSquares(foundRow,foundCol).MarkSideTaken(updateSide, markTaken)";
mostCurrent._vvvvvvvvvvv7[_foundrow][_foundcol]._vvvvv1(_updateside,_marktaken);
 };
 //BA.debugLineNum = 639;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvv6(pineysoft.squarepaddocks.gamesquare _currentsquare,int _side) throws Exception{
 //BA.debugLineNum = 592;BA.debugLine="Public Sub MarkOtherSide2(currentSquare As GameSquare, side As Int)";
 //BA.debugLineNum = 593;BA.debugLine="MarkOtherSide(currentSquare, side, True)";
_vvvvvvvvvvvvvvvv7(_currentsquare,_side,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 594;BA.debugLine="End Sub";
return "";
}
public static String  _panel1_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 454;BA.debugLine="Sub Panel1_Touch (Action As Int, X As Float, Y As Float)";
 //BA.debugLineNum = 455;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_UP)) {
case 0:
 //BA.debugLineNum = 457;BA.debugLine="Log (\"X,Y = \" & X & \",\" & Y)";
anywheresoftware.b4a.keywords.Common.Log("X,Y = "+BA.NumberToString(_x)+","+BA.NumberToString(_y));
 //BA.debugLineNum = 458;BA.debugLine="CalculateMove(X,Y)";
_vvvvvvvvvvv0((int) (_x),(int) (_y));
 //BA.debugLineNum = 459;BA.debugLine="panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 break;
}
;
 //BA.debugLineNum = 461;BA.debugLine="End Sub";
return "";
}
public static String  _pnlstartscreen_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 1460;BA.debugLine="Sub pnlStartScreen_Touch (Action As Int, X As Float, Y As Float)";
 //BA.debugLineNum = 1462;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_UP)) {
case 0:
 //BA.debugLineNum = 1464;BA.debugLine="AnimateCharacters";
_vvvvvvv4();
 //BA.debugLineNum = 1465;BA.debugLine="AnimatePanelLeft";
_vvvvvvvvvv4();
 break;
}
;
 //BA.debugLineNum = 1468;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private currentPlayer As Short";
_vvvvvvvvvvvv3 = (short)0;
 //BA.debugLineNum = 10;BA.debugLine="Private numberOfPlayers As Int";
_vvvvvvvvvvvvvv5 = 0;
 //BA.debugLineNum = 11;BA.debugLine="Private numberOfDroids As Int";
_vvvvvvvvvvvvvv6 = 0;
 //BA.debugLineNum = 12;BA.debugLine="Private playerColours As List ' List of Ints";
_vvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Private inGame As Boolean";
_vvvvvvvv3 = false;
 //BA.debugLineNum = 14;BA.debugLine="Private giggleSound As Int";
_vvvvvvvvvvvvv2 = 0;
 //BA.debugLineNum = 15;BA.debugLine="Private displayingDebug As Int";
_vvvvvvvvvvv3 = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim sounds As SoundPool";
_v5 = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim SPConstants As Constants";
_v6 = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 18;BA.debugLine="Dim difficulty As String";
_v7 = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim vibrate As PhoneVibrate";
_v0 = new anywheresoftware.b4a.phone.Phone.PhoneVibrate();
 //BA.debugLineNum = 20;BA.debugLine="Dim soundsOn As Boolean = True";
_vv1 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 21;BA.debugLine="Dim GameMode As String";
_vv2 = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim IsMaster As Boolean = False";
_vv3 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvv0(pineysoft.squarepaddocks.turn _lastturn,pineysoft.squarepaddocks.player _currplayer) throws Exception{
 //BA.debugLineNum = 1392;BA.debugLine="Sub RemoveTurn(lastTurn As Turn, currPlayer As Player)";
 //BA.debugLineNum = 1395;BA.debugLine="If lastTurn.Square.sidesTaken = 4 Then";
if (_lastturn._vvvvvv7._vvvvvv5==4) { 
 //BA.debugLineNum = 1396;BA.debugLine="currPlayer.Score = currPlayer.Score - 1";
_currplayer._vvv6 = (int) (_currplayer._vvv6-1);
 //BA.debugLineNum = 1397;BA.debugLine="EmptyTheSquare(lastTurn.Square)";
_vvvvvvvvvvvvvvv2(_lastturn._vvvvvv7);
 };
 //BA.debugLineNum = 1401;BA.debugLine="lastTurn.Square.RemoveSide(canv, lastTurn.Edge)";
_lastturn._vvvvvv7._vvvvv3(mostCurrent._vvvvvvvvvvvv5,_lastturn._vvvvvv0);
 //BA.debugLineNum = 1402;BA.debugLine="MarkOtherSide(lastTurn.Square,lastTurn.Edge,False)";
_vvvvvvvvvvvvvvvv7(_lastturn._vvvvvv7,_lastturn._vvvvvv0,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1405;BA.debugLine="gameTurns.RemoveAt(gameTurns.Size - 1)";
mostCurrent._vvvvvvvvvvvvvvv5.RemoveAt((int) (mostCurrent._vvvvvvvvvvvvvvv5.getSize()-1));
 //BA.debugLineNum = 1408;BA.debugLine="If gameTurns.Size > 0 Then";
if (mostCurrent._vvvvvvvvvvvvvvv5.getSize()>0) { 
 //BA.debugLineNum = 1409;BA.debugLine="lastTurn = gameTurns.Get(gameTurns.Size - 1)";
_lastturn = (pineysoft.squarepaddocks.turn)(mostCurrent._vvvvvvvvvvvvvvv5.Get((int) (mostCurrent._vvvvvvvvvvvvvvv5.getSize()-1)));
 //BA.debugLineNum = 1410;BA.debugLine="lastTurn.Square.RedrawSide(canv, lastTurn.Edge)";
_lastturn._vvvvvv7._vvvvv2(mostCurrent._vvvvvvvvvvvv5,_lastturn._vvvvvv0);
 };
 //BA.debugLineNum = 1412;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvv4() throws Exception{
 //BA.debugLineNum = 249;BA.debugLine="Sub ResetIcons";
 //BA.debugLineNum = 250;BA.debugLine="start1 = Rnd(0,100%y)";
_vvvvvvvv6 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 251;BA.debugLine="start2 = Rnd(0,100%y)";
_vvvvvvvv0 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 252;BA.debugLine="start3 = Rnd(0,100%y)";
_vvvvvvvvv2 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 253;BA.debugLine="start4 = Rnd(0,100%y)";
_vvvvvvvvv4 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 254;BA.debugLine="start5 = Rnd(0,100%y)";
_vvvvvvvvv6 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 255;BA.debugLine="start6 = Rnd(0,100%y)";
_vvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 257;BA.debugLine="SetIconAtPosition(icon1,-(icon1.Width),start1)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon1,(int) (-(mostCurrent._icon1.getWidth())),_vvvvvvvv6);
 //BA.debugLineNum = 258;BA.debugLine="SetIconAtPosition(icon2,100%x,start2)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon2,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_vvvvvvvv0);
 //BA.debugLineNum = 259;BA.debugLine="SetIconAtPosition(icon3,-(icon3.Width),start3)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon3,(int) (-(mostCurrent._icon3.getWidth())),_vvvvvvvvv2);
 //BA.debugLineNum = 260;BA.debugLine="SetIconAtPosition(icon4,100%x,start4)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon4,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_vvvvvvvvv4);
 //BA.debugLineNum = 261;BA.debugLine="SetIconAtPosition(icon5,-(icon5.Width),start5)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon5,(int) (-(mostCurrent._icon5.getWidth())),_vvvvvvvvv6);
 //BA.debugLineNum = 262;BA.debugLine="SetIconAtPosition(icon6,100%x,start6)";
_vvvvvvvvvvvvvvvv1(mostCurrent._icon6,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_vvvvvvvvv0);
 //BA.debugLineNum = 263;BA.debugLine="End Sub";
return "";
}
public static boolean  _vvvvvvvv1() throws Exception{
anywheresoftware.b4a.objects.collections.Map _defaultsmap = null;
int _iloop = 0;
int _def_players = 0;
int _def_droids = 0;
String _def_diff = "";
 //BA.debugLineNum = 1321;BA.debugLine="Sub RestoreDefaults() As Boolean";
 //BA.debugLineNum = 1322;BA.debugLine="If File.Exists(File.DirInternal, \"Square_settings.txt\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt")) { 
 //BA.debugLineNum = 1323;BA.debugLine="Dim defaultsMap As Map = File.ReadMap(File.DirInternal, \"Square_settings.txt\")";
_defaultsmap = new anywheresoftware.b4a.objects.collections.Map();
_defaultsmap = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt");
 //BA.debugLineNum = 1324;BA.debugLine="If defaultsMap.IsInitialized Then";
if (_defaultsmap.IsInitialized()) { 
 //BA.debugLineNum = 1325;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 1326;BA.debugLine="Dim def_players As Int = defaultsMap.Get(\"players\")";
_def_players = (int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("players"))));
 //BA.debugLineNum = 1327;BA.debugLine="For iLoop = 0 To spnPlayers.Size - 1";
{
final int step1129 = 1;
final int limit1129 = (int) (mostCurrent._spnplayers.getSize()-1);
for (_iloop = (int) (0); (step1129 > 0 && _iloop <= limit1129) || (step1129 < 0 && _iloop >= limit1129); _iloop = ((int)(0 + _iloop + step1129))) {
 //BA.debugLineNum = 1328;BA.debugLine="If spnPlayers.GetItem(iLoop) = def_players Then";
if ((mostCurrent._spnplayers.GetItem(_iloop)).equals(BA.NumberToString(_def_players))) { 
 //BA.debugLineNum = 1329;BA.debugLine="spnPlayers.SelectedIndex = iLoop";
mostCurrent._spnplayers.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1330;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1333;BA.debugLine="UpdateDroidNumbers";
_vvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 1334;BA.debugLine="Dim def_droids As Int = defaultsMap.Get(\"droids\")";
_def_droids = (int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("droids"))));
 //BA.debugLineNum = 1335;BA.debugLine="For iLoop = 0 To spnDroids.Size - 1";
{
final int step1137 = 1;
final int limit1137 = (int) (mostCurrent._spndroids.getSize()-1);
for (_iloop = (int) (0); (step1137 > 0 && _iloop <= limit1137) || (step1137 < 0 && _iloop >= limit1137); _iloop = ((int)(0 + _iloop + step1137))) {
 //BA.debugLineNum = 1336;BA.debugLine="If spnDroids.GetItem(iLoop) = def_droids Then";
if ((mostCurrent._spndroids.GetItem(_iloop)).equals(BA.NumberToString(_def_droids))) { 
 //BA.debugLineNum = 1337;BA.debugLine="spnDroids.SelectedIndex = iLoop";
mostCurrent._spndroids.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1338;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1342;BA.debugLine="sbRows.Value = defaultsMap.Get(\"rows\")";
mostCurrent._sbrows.setValue((int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("rows")))));
 //BA.debugLineNum = 1343;BA.debugLine="lblRows.Text = \"Rows : \" & (sbRows.Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((mostCurrent._sbrows.getValue()+4))));
 //BA.debugLineNum = 1344;BA.debugLine="sbColumns.Value = defaultsMap.Get(\"columns\")";
mostCurrent._sbcolumns.setValue((int)(BA.ObjectToNumber(_defaultsmap.Get((Object)("columns")))));
 //BA.debugLineNum = 1345;BA.debugLine="lblColumns.Text = \"Columns : \" & (sbColumns.value + 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((mostCurrent._sbcolumns.getValue()+4))));
 //BA.debugLineNum = 1346;BA.debugLine="If defaultsMap.Get(\"Sound\") = True Then";
if ((_defaultsmap.Get((Object)("Sound"))).equals((Object)(anywheresoftware.b4a.keywords.Common.True))) { 
 //BA.debugLineNum = 1347;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._vvvvvvvvvvvvv5.Get(_v6._checkbox_on)));
 //BA.debugLineNum = 1348;BA.debugLine="soundsOn = True";
_vv1 = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 1350;BA.debugLine="chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))";
mostCurrent._chksounds.SetBackgroundImage((android.graphics.Bitmap)(mostCurrent._vvvvvvvvvvvvv5.Get(_v6._checkbox_off)));
 //BA.debugLineNum = 1351;BA.debugLine="soundsOn = False";
_vv1 = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 1354;BA.debugLine="Dim def_diff As String = defaultsMap.Get(\"Difficulty\")";
_def_diff = BA.ObjectToString(_defaultsmap.Get((Object)("Difficulty")));
 //BA.debugLineNum = 1355;BA.debugLine="For iLoop = 0 To spnDifficulty.Size - 1";
{
final int step1155 = 1;
final int limit1155 = (int) (mostCurrent._spndifficulty.getSize()-1);
for (_iloop = (int) (0); (step1155 > 0 && _iloop <= limit1155) || (step1155 < 0 && _iloop >= limit1155); _iloop = ((int)(0 + _iloop + step1155))) {
 //BA.debugLineNum = 1356;BA.debugLine="If spnDifficulty.GetItem(iLoop) = def_diff Then";
if ((mostCurrent._spndifficulty.GetItem(_iloop)).equals(_def_diff)) { 
 //BA.debugLineNum = 1357;BA.debugLine="spnDifficulty.SelectedIndex = iLoop";
mostCurrent._spndifficulty.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1358;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 }else {
 //BA.debugLineNum = 1362;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 1367;BA.debugLine="GameMode = SPConstants.GAMETYPE_MODE_LOC";
_vv2 = _v6._gametype_mode_loc;
 //BA.debugLineNum = 1368;BA.debugLine="If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then";
if ((_vv2).equals(_v6._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 1369;BA.debugLine="spnPlayers.Enabled = False";
mostCurrent._spnplayers.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1370;BA.debugLine="spnDroids.Enabled = False";
mostCurrent._spndroids.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 1372;BA.debugLine="spnPlayers.Enabled = True";
mostCurrent._spnplayers.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1373;BA.debugLine="spnDroids.Enabled = True";
mostCurrent._spndroids.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1376;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1377;BA.debugLine="End Sub";
return false;
}
public static String  _vvvvvvvvvvv2() throws Exception{
 //BA.debugLineNum = 165;BA.debugLine="Sub ReverseAnimate";
 //BA.debugLineNum = 167;BA.debugLine="anim1.InitializeTranslate(\"\", 0,0,-(75%x+(icon1.Width/2)),start1-25%y)";
mostCurrent._vvvvvvvv5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon1.getWidth()/(double)2))),(float) (_vvvvvvvv6-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 168;BA.debugLine="anim1.Duration = 700";
mostCurrent._vvvvvvvv5.setDuration((long) (700));
 //BA.debugLineNum = 169;BA.debugLine="anim1.PersistAfter = True";
mostCurrent._vvvvvvvv5.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 170;BA.debugLine="anim1.Start(icon1)";
mostCurrent._vvvvvvvv5.Start((android.view.View)(mostCurrent._icon1.getObject()));
 //BA.debugLineNum = 171;BA.debugLine="anim2.InitializeTranslate(\"\", 0,0,75%x+(icon2.Width/2),start2-45%y)";
mostCurrent._vvvvvvvv7.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA)+(mostCurrent._icon2.getWidth()/(double)2)),(float) (_vvvvvvvv0-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 172;BA.debugLine="anim2.Duration = 700";
mostCurrent._vvvvvvvv7.setDuration((long) (700));
 //BA.debugLineNum = 173;BA.debugLine="anim2.PersistAfter = True";
mostCurrent._vvvvvvvv7.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 174;BA.debugLine="anim2.Start(icon2)";
mostCurrent._vvvvvvvv7.Start((android.view.View)(mostCurrent._icon2.getObject()));
 //BA.debugLineNum = 175;BA.debugLine="anim3.InitializeTranslate(\"\", 0,0,-(50%x+(icon3.Width/2)),start3-25%y)";
mostCurrent._vvvvvvvvv1.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon3.getWidth()/(double)2))),(float) (_vvvvvvvvv2-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 176;BA.debugLine="anim3.Duration = 700";
mostCurrent._vvvvvvvvv1.setDuration((long) (700));
 //BA.debugLineNum = 177;BA.debugLine="anim3.PersistAfter = True";
mostCurrent._vvvvvvvvv1.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 178;BA.debugLine="anim3.Start(icon3)";
mostCurrent._vvvvvvvvv1.Start((android.view.View)(mostCurrent._icon3.getObject()));
 //BA.debugLineNum = 179;BA.debugLine="anim4.InitializeTranslate(\"\", 0,0,50%x+(icon4.Width/2),start4-45%y)";
mostCurrent._vvvvvvvvv3.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)+(mostCurrent._icon4.getWidth()/(double)2)),(float) (_vvvvvvvvv4-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 180;BA.debugLine="anim4.Duration = 700";
mostCurrent._vvvvvvvvv3.setDuration((long) (700));
 //BA.debugLineNum = 181;BA.debugLine="anim4.PersistAfter = True";
mostCurrent._vvvvvvvvv3.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 182;BA.debugLine="anim4.Start(icon4)";
mostCurrent._vvvvvvvvv3.Start((android.view.View)(mostCurrent._icon4.getObject()));
 //BA.debugLineNum = 183;BA.debugLine="anim5.InitializeTranslate(\"\", 0,0,-(25%x+(icon5.Width/2)),start5-25%y)";
mostCurrent._vvvvvvvvv5.InitializeTranslate(mostCurrent.activityBA,"",(float) (0),(float) (0),(float) (-(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon5.getWidth()/(double)2))),(float) (_vvvvvvvvv6-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA)));
 //BA.debugLineNum = 184;BA.debugLine="anim5.Duration = 700";
mostCurrent._vvvvvvvvv5.setDuration((long) (700));
 //BA.debugLineNum = 185;BA.debugLine="anim5.PersistAfter = True";
mostCurrent._vvvvvvvvv5.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 186;BA.debugLine="anim5.Start(icon5)";
mostCurrent._vvvvvvvvv5.Start((android.view.View)(mostCurrent._icon5.getObject()));
 //BA.debugLineNum = 187;BA.debugLine="anim6.InitializeTranslate(\"LastIconEnd\", 0,0,25%x+(icon6.Width/2),start6-45%y)";
mostCurrent._vvvvvvvvv7.InitializeTranslate(mostCurrent.activityBA,"LastIconEnd",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA)+(mostCurrent._icon6.getWidth()/(double)2)),(float) (_vvvvvvvvv0-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA)));
 //BA.debugLineNum = 188;BA.debugLine="anim6.Duration = 700";
mostCurrent._vvvvvvvvv7.setDuration((long) (700));
 //BA.debugLineNum = 189;BA.debugLine="anim6.PersistAfter = True";
mostCurrent._vvvvvvvvv7.setPersistAfter(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 190;BA.debugLine="anim6.Start(icon6)";
mostCurrent._vvvvvvvvv7.Start((android.view.View)(mostCurrent._icon6.getObject()));
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return "";
}
public static String  _rightpanel_animationend() throws Exception{
 //BA.debugLineNum = 298;BA.debugLine="Sub RightPanel_AnimationEnd";
 //BA.debugLineNum = 299;BA.debugLine="animPanel1.Stop(pnlSelectionLeft)";
mostCurrent._vvvvvvvvvv5.Stop((android.view.View)(mostCurrent._pnlselectionleft.getObject()));
 //BA.debugLineNum = 300;BA.debugLine="pnlSelectionLeft.Top = pnlShading.Top";
mostCurrent._pnlselectionleft.setTop(mostCurrent._pnlshading.getTop());
 //BA.debugLineNum = 301;BA.debugLine="pnlSelectionLeft.Left = 5%x";
mostCurrent._pnlselectionleft.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 303;BA.debugLine="animPanel2.Stop(pnlSelectionRight)";
mostCurrent._vvvvvvvvvv7.Stop((android.view.View)(mostCurrent._pnlselectionright.getObject()));
 //BA.debugLineNum = 304;BA.debugLine="pnlSelectionRight.Top = pnlShading.Top";
mostCurrent._pnlselectionright.setTop(mostCurrent._pnlshading.getTop());
 //BA.debugLineNum = 305;BA.debugLine="pnlSelectionRight.Left = 55%x";
mostCurrent._pnlselectionright.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (55),mostCurrent.activityBA));
 //BA.debugLineNum = 307;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvv2() throws Exception{
anywheresoftware.b4a.objects.collections.Map _defaults = null;
 //BA.debugLineNum = 1378;BA.debugLine="Sub SaveDefaults()";
 //BA.debugLineNum = 1379;BA.debugLine="Dim defaults As Map";
_defaults = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1380;BA.debugLine="defaults.Initialize";
_defaults.Initialize();
 //BA.debugLineNum = 1382;BA.debugLine="defaults.Put(\"players\",spnPlayers.SelectedItem)";
_defaults.Put((Object)("players"),(Object)(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 1383;BA.debugLine="defaults.Put(\"droids\",spnDroids.SelectedItem)";
_defaults.Put((Object)("droids"),(Object)(mostCurrent._spndroids.getSelectedItem()));
 //BA.debugLineNum = 1384;BA.debugLine="defaults.Put(\"rows\",sbRows.Value)";
_defaults.Put((Object)("rows"),(Object)(mostCurrent._sbrows.getValue()));
 //BA.debugLineNum = 1385;BA.debugLine="defaults.Put(\"columns\",sbColumns.Value)";
_defaults.Put((Object)("columns"),(Object)(mostCurrent._sbcolumns.getValue()));
 //BA.debugLineNum = 1386;BA.debugLine="defaults.Put(\"Sound\", soundsOn)";
_defaults.Put((Object)("Sound"),(Object)(_vv1));
 //BA.debugLineNum = 1387;BA.debugLine="defaults.Put(\"Difficulty\", spnDifficulty.SelectedItem)";
_defaults.Put((Object)("Difficulty"),(Object)(mostCurrent._spndifficulty.getSelectedItem()));
 //BA.debugLineNum = 1389;BA.debugLine="File.WriteMap(File.DirInternal, \"Square_settings.txt\", defaults)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"Square_settings.txt",_defaults);
 //BA.debugLineNum = 1391;BA.debugLine="End Sub";
return "";
}
public static String  _sbcolumns_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 1292;BA.debugLine="Sub sbColumns_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 1293;BA.debugLine="gameWidth = Value + 4";
_vvvvvvvvvvv6 = (int) (_value+4);
 //BA.debugLineNum = 1294;BA.debugLine="lblColumns.Text = \"Columns : \" & (Value	+ 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((_value+4))));
 //BA.debugLineNum = 1295;BA.debugLine="End Sub";
return "";
}
public static String  _sbrows_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 1288;BA.debugLine="Sub sbRows_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 1289;BA.debugLine="gameHeight = Value + 4";
_vvvvvvvvvvv5 = (int) (_value+4);
 //BA.debugLineNum = 1290;BA.debugLine="lblRows.Text = \"Rows : \" & (Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((_value+4))));
 //BA.debugLineNum = 1291;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvv0() throws Exception{
 //BA.debugLineNum = 436;BA.debugLine="Public Sub SetDifficulty";
 //BA.debugLineNum = 437;BA.debugLine="Select spnDifficulty.SelectedItem";
switch (BA.switchObjectToInt(mostCurrent._spndifficulty.getSelectedItem(),"Easy","Medium","Hard")) {
case 0:
 //BA.debugLineNum = 439;BA.debugLine="difficulty = SPConstants.DIFFICULTY_EASY";
_v7 = _v6._difficulty_easy;
 break;
case 1:
 //BA.debugLineNum = 441;BA.debugLine="difficulty = SPConstants.DIFFICULTY_MEDIUM";
_v7 = _v6._difficulty_medium;
 break;
case 2:
 //BA.debugLineNum = 443;BA.debugLine="difficulty = SPConstants.DIFFICULTY_HARD";
_v7 = _v6._difficulty_hard;
 break;
}
;
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvv1(anywheresoftware.b4a.objects.ImageViewWrapper _icon,int _left,int _top) throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub SetIconAtPosition(icon As ImageView,left As Int, top As Int)";
 //BA.debugLineNum = 245;BA.debugLine="icon.left = left";
_icon.setLeft(_left);
 //BA.debugLineNum = 246;BA.debugLine="icon.top = top";
_icon.setTop(_top);
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _shading_animationend() throws Exception{
 //BA.debugLineNum = 274;BA.debugLine="Sub Shading_AnimationEnd";
 //BA.debugLineNum = 275;BA.debugLine="Log(\"Start Left Panel Anim\")";
anywheresoftware.b4a.keywords.Common.Log("Start Left Panel Anim");
 //BA.debugLineNum = 276;BA.debugLine="AnimatePanelLeft";
_vvvvvvvvvv4();
 //BA.debugLineNum = 277;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvv2() throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub ShowSplashScreen";
 //BA.debugLineNum = 123;BA.debugLine="pnlShading.Visible = False";
mostCurrent._pnlshading.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 124;BA.debugLine="pnlSelectionLeft.Left = 0 - pnlSelectionLeft.Width";
mostCurrent._pnlselectionleft.setLeft((int) (0-mostCurrent._pnlselectionleft.getWidth()));
 //BA.debugLineNum = 125;BA.debugLine="pnlSelectionRight.Left = 100%x";
mostCurrent._pnlselectionright.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 126;BA.debugLine="UpdateLabels";
_vvvvvvv0();
 //BA.debugLineNum = 127;BA.debugLine="AnimateCharacters";
_vvvvvvv4();
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _spnplayers_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1262;BA.debugLine="Sub spnPlayers_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1263;BA.debugLine="UpdateDroidNumbers";
_vvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 1264;BA.debugLine="End Sub";
return "";
}
public static pineysoft.squarepaddocks.gamesquare  _vvvvvvvvvvvvvvv4(pineysoft.squarepaddocks.gamesquare _square,int _side) throws Exception{
pineysoft.squarepaddocks.gamesquare _othersquare = null;
 //BA.debugLineNum = 1118;BA.debugLine="Sub SubGetOppositeSquare(square As GameSquare, side As Int) As GameSquare";
 //BA.debugLineNum = 1119;BA.debugLine="Dim otherSquare As GameSquare";
_othersquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 1120;BA.debugLine="Select side";
switch (BA.switchObjectToInt(_side,_v6._top_side,_v6._right_side,_v6._bottom_side,_v6._left_side)) {
case 0:
 //BA.debugLineNum = 1122;BA.debugLine="If square.RowPos > 0 Then";
if (_square._vvvvv6>0) { 
 //BA.debugLineNum = 1123;BA.debugLine="otherSquare = gameSquares(square.RowPos - 1, square.ColPos)";
_othersquare = mostCurrent._vvvvvvvvvvv7[(int) (_square._vvvvv6-1)][_square._vvvvv5];
 };
 break;
case 1:
 //BA.debugLineNum = 1126;BA.debugLine="If square.ColPos < gameWidth - 1 Then";
if (_square._vvvvv5<_vvvvvvvvvvv6-1) { 
 //BA.debugLineNum = 1127;BA.debugLine="otherSquare = gameSquares(square.RowPos, square.ColPos + 1)";
_othersquare = mostCurrent._vvvvvvvvvvv7[_square._vvvvv6][(int) (_square._vvvvv5+1)];
 };
 break;
case 2:
 //BA.debugLineNum = 1130;BA.debugLine="If square.RowPos < gameHeight - 1 Then";
if (_square._vvvvv6<_vvvvvvvvvvv5-1) { 
 //BA.debugLineNum = 1131;BA.debugLine="otherSquare = gameSquares(square.RowPos + 1, square.ColPos)";
_othersquare = mostCurrent._vvvvvvvvvvv7[(int) (_square._vvvvv6+1)][_square._vvvvv5];
 };
 break;
case 3:
 //BA.debugLineNum = 1134;BA.debugLine="If square.RowPos > 0  Then";
if (_square._vvvvv6>0) { 
 //BA.debugLineNum = 1135;BA.debugLine="otherSquare = gameSquares(square.RowPos, square.ColPos - 1)";
_othersquare = mostCurrent._vvvvvvvvvvv7[_square._vvvvv6][(int) (_square._vvvvv5-1)];
 };
 break;
}
;
 //BA.debugLineNum = 1139;BA.debugLine="Return otherSquare";
if (true) return _othersquare;
 //BA.debugLineNum = 1140;BA.debugLine="End Sub";
return null;
}
public static String  _vvvvvvvvvvvvvvvv4() throws Exception{
anywheresoftware.b4a.objects.collections.List _found3s = null;
int _closeside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
 //BA.debugLineNum = 815;BA.debugLine="Public Sub TakeDoubles";
 //BA.debugLineNum = 816;BA.debugLine="Dim found3s As List";
_found3s = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 817;BA.debugLine="Dim closeSide As Int = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 819;BA.debugLine="found3s = FindAllForSides(3)";
_found3s = _vvvvvvvvvvvvv6((int) (3));
 //BA.debugLineNum = 821;BA.debugLine="For Each currentSquare As GameSquare In found3s";
final anywheresoftware.b4a.BA.IterableList group695 = _found3s;
final int groupLen695 = group695.getSize();
for (int index695 = 0;index695 < groupLen695 ;index695++){
_currentsquare = (pineysoft.squarepaddocks.gamesquare)(group695.Get(index695));
 //BA.debugLineNum = 822;BA.debugLine="If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then";
if (_currentsquare._vvvv0(_v6._left_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 823;BA.debugLine="If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken = 2 Then";
if (_currentsquare._vvvvv5==0 || mostCurrent._vvvvvvvvvvv7[_currentsquare._vvvvv6][(int) (_currentsquare._vvvvv5-1)]._vvvvvv5==2) { 
 //BA.debugLineNum = 824;BA.debugLine="closeSide = SPConstants.LEFT_SIDE";
_closeside = _v6._left_side;
 };
 }else if(_currentsquare._vvvv0(_v6._right_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 827;BA.debugLine="If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken = 2 Then";
if (_currentsquare._vvvvv5==_vvvvvvvvvvv6-1 || mostCurrent._vvvvvvvvvvv7[_currentsquare._vvvvv6][(int) (_currentsquare._vvvvv5+1)]._vvvvvv5==2) { 
 //BA.debugLineNum = 828;BA.debugLine="closeSide = SPConstants.RIGHT_SIDE";
_closeside = _v6._right_side;
 };
 }else if(_currentsquare._vvvv0(_v6._top_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 831;BA.debugLine="If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken = 2 Then";
if (_currentsquare._vvvvv6==0 || mostCurrent._vvvvvvvvvvv7[(int) (_currentsquare._vvvvv6-1)][_currentsquare._vvvvv5]._vvvvvv5==2) { 
 //BA.debugLineNum = 832;BA.debugLine="closeSide = SPConstants.TOP_SIDE";
_closeside = _v6._top_side;
 };
 }else if(_currentsquare._vvvv0(_v6._bottom_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 835;BA.debugLine="If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken = 2 Then";
if (_currentsquare._vvvvv6==_vvvvvvvvvvv5-1 || mostCurrent._vvvvvvvvvvv7[(int) (_currentsquare._vvvvv6+1)][_currentsquare._vvvvv5]._vvvvvv5==2) { 
 //BA.debugLineNum = 836;BA.debugLine="closeSide = SPConstants.BOTTOM_SIDE";
_closeside = _v6._bottom_side;
 };
 };
 //BA.debugLineNum = 840;BA.debugLine="If closeSide = -1 Then Return";
if (_closeside==-1) { 
if (true) return "";};
 //BA.debugLineNum = 843;BA.debugLine="UpdateTurn(canv, currentSquare, closeSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_currentsquare,_closeside);
 //BA.debugLineNum = 846;BA.debugLine="currentSquare.TakeSide(canv,closeSide)";
_currentsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_closeside);
 //BA.debugLineNum = 849;BA.debugLine="MarkOtherSide2(currentSquare, closeSide)";
_vvvvvvvvvvvv6(_currentsquare,_closeside);
 }
;
 //BA.debugLineNum = 853;BA.debugLine="End Sub";
return "";
}
public static boolean  _vvvvvvvvvvvvvvvv3(anywheresoftware.b4a.objects.collections.List _found3s,pineysoft.squarepaddocks.player _currplayer) throws Exception{
int _closeside = 0;
pineysoft.squarepaddocks.gamesquare _currentsquare = null;
 //BA.debugLineNum = 774;BA.debugLine="Public Sub TakeEasy3s(found3s As List, currPlayer As Player) As Boolean";
 //BA.debugLineNum = 775;BA.debugLine="Dim closeSide As Int = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 777;BA.debugLine="Log(\"Checking Found 3's\")";
anywheresoftware.b4a.keywords.Common.Log("Checking Found 3's");
 //BA.debugLineNum = 778;BA.debugLine="For Each currentSquare As GameSquare In found3s";
final anywheresoftware.b4a.BA.IterableList group663 = _found3s;
final int groupLen663 = group663.getSize();
for (int index663 = 0;index663 < groupLen663 ;index663++){
_currentsquare = (pineysoft.squarepaddocks.gamesquare)(group663.Get(index663));
 //BA.debugLineNum = 779;BA.debugLine="closeSide = -1";
_closeside = (int) (-1);
 //BA.debugLineNum = 780;BA.debugLine="Log(\"Row: \" & currentSquare.RowPos & \" Column: \" & currentSquare.ColPos)";
anywheresoftware.b4a.keywords.Common.Log("Row: "+BA.NumberToString(_currentsquare._vvvvv6)+" Column: "+BA.NumberToString(_currentsquare._vvvvv5));
 //BA.debugLineNum = 781;BA.debugLine="If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then";
if (_currentsquare._vvvv0(_v6._left_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 782;BA.debugLine="If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken <> 2 Then";
if (_currentsquare._vvvvv5==0 || mostCurrent._vvvvvvvvvvv7[_currentsquare._vvvvv6][(int) (_currentsquare._vvvvv5-1)]._vvvvvv5!=2) { 
 //BA.debugLineNum = 783;BA.debugLine="closeSide = SPConstants.LEFT_SIDE";
_closeside = _v6._left_side;
 };
 }else if(_currentsquare._vvvv0(_v6._right_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 786;BA.debugLine="If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken <> 2 Then";
if (_currentsquare._vvvvv5==_vvvvvvvvvvv6-1 || mostCurrent._vvvvvvvvvvv7[_currentsquare._vvvvv6][(int) (_currentsquare._vvvvv5+1)]._vvvvvv5!=2) { 
 //BA.debugLineNum = 787;BA.debugLine="closeSide = SPConstants.RIGHT_SIDE";
_closeside = _v6._right_side;
 };
 }else if(_currentsquare._vvvv0(_v6._top_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 790;BA.debugLine="If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken <> 2 Then";
if (_currentsquare._vvvvv6==0 || mostCurrent._vvvvvvvvvvv7[(int) (_currentsquare._vvvvv6-1)][_currentsquare._vvvvv5]._vvvvvv5!=2) { 
 //BA.debugLineNum = 791;BA.debugLine="closeSide = SPConstants.TOP_SIDE";
_closeside = _v6._top_side;
 };
 }else if(_currentsquare._vvvv0(_v6._bottom_side)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 794;BA.debugLine="If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken <> 2 Then";
if (_currentsquare._vvvvv6==_vvvvvvvvvvv5-1 || mostCurrent._vvvvvvvvvvv7[(int) (_currentsquare._vvvvv6+1)][_currentsquare._vvvvv5]._vvvvvv5!=2) { 
 //BA.debugLineNum = 795;BA.debugLine="closeSide = SPConstants.BOTTOM_SIDE";
_closeside = _v6._bottom_side;
 };
 };
 //BA.debugLineNum = 799;BA.debugLine="If closeSide = -1 Then Return False";
if (_closeside==-1) { 
if (true) return anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 802;BA.debugLine="UpdateTurn(canv, currentSquare, closeSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_currentsquare,_closeside);
 //BA.debugLineNum = 805;BA.debugLine="currentSquare.TakeSide(canv,closeSide)";
_currentsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_closeside);
 //BA.debugLineNum = 808;BA.debugLine="MarkOtherSide2(currentSquare, closeSide)";
_vvvvvvvvvvvv6(_currentsquare,_closeside);
 }
;
 //BA.debugLineNum = 811;BA.debugLine="Log(\"Finished Checking Found 3's\")";
anywheresoftware.b4a.keywords.Common.Log("Finished Checking Found 3's");
 //BA.debugLineNum = 812;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 813;BA.debugLine="End Sub";
return false;
}
public static boolean  _vvvvvvvvvvvvvvvv5(anywheresoftware.b4a.objects.collections.List _foundvalids,boolean _forceany) throws Exception{
int _rndnum = 0;
boolean _foundside = false;
int _rndside = 0;
int _loopcount = 0;
int _loopcountinner = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
int _sides = 0;
 //BA.debugLineNum = 855;BA.debugLine="Sub TakeSingle(foundValids As List, forceAny As Boolean) As Boolean";
 //BA.debugLineNum = 856;BA.debugLine="Dim rndnum As Int";
_rndnum = 0;
 //BA.debugLineNum = 857;BA.debugLine="Dim foundSide As Boolean = False";
_foundside = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 858;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 859;BA.debugLine="Dim loopCount As Int";
_loopcount = 0;
 //BA.debugLineNum = 860;BA.debugLine="Dim loopCountInner As Int";
_loopcountinner = 0;
 //BA.debugLineNum = 862;BA.debugLine="Do While foundSide = False AND loopCount < 50";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcount<50) {
 //BA.debugLineNum = 863;BA.debugLine="loopCountInner = 0";
_loopcountinner = (int) (0);
 //BA.debugLineNum = 864;BA.debugLine="If foundValids.Size > 0 Then";
if (_foundvalids.getSize()>0) { 
 //BA.debugLineNum = 865;BA.debugLine="If foundValids.Size > 1 Then";
if (_foundvalids.getSize()>1) { 
 //BA.debugLineNum = 866;BA.debugLine="rndnum = Rnd(1, foundValids.Size)";
_rndnum = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),_foundvalids.getSize());
 }else {
 //BA.debugLineNum = 868;BA.debugLine="rndnum = 1";
_rndnum = (int) (1);
 };
 //BA.debugLineNum = 871;BA.debugLine="Dim foundSquare As GameSquare = foundValids.Get(rndnum - 1)";
_foundsquare = (pineysoft.squarepaddocks.gamesquare)(_foundvalids.Get((int) (_rndnum-1)));
 //BA.debugLineNum = 873;BA.debugLine="Do While foundSide = False AND loopCountInner < 8";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcountinner<8) {
 //BA.debugLineNum = 874;BA.debugLine="rndSide = Rnd(1,4) - 1";
_rndside = (int) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (4))-1);
 //BA.debugLineNum = 875;BA.debugLine="If foundSquare.IsSideTaken(rndSide) = False Then";
if (_foundsquare._vvvv0(_rndside)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 876;BA.debugLine="If forceAny = True Then";
if (_forceany==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 877;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 879;BA.debugLine="Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)";
_sides = _vvvvvvvvvvvvvvv3(_foundsquare,_rndside);
 //BA.debugLineNum = 880;BA.debugLine="If sides < 2 Then";
if (_sides<2) { 
 //BA.debugLineNum = 881;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 885;BA.debugLine="loopCountInner = loopCountInner + 1";
_loopcountinner = (int) (_loopcountinner+1);
 //BA.debugLineNum = 886;BA.debugLine="Log(\"loop count inner is \" & loopCountInner)";
anywheresoftware.b4a.keywords.Common.Log("loop count inner is "+BA.NumberToString(_loopcountinner));
 }
;
 //BA.debugLineNum = 889;BA.debugLine="If foundSide = True Then";
if (_foundside==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 890;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_foundsquare,_rndside);
 //BA.debugLineNum = 892;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_rndside);
 //BA.debugLineNum = 894;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_vvvvvvvvvvvv6(_foundsquare,_rndside);
 };
 };
 //BA.debugLineNum = 897;BA.debugLine="loopCount = loopCount + 1";
_loopcount = (int) (_loopcount+1);
 //BA.debugLineNum = 898;BA.debugLine="Log(\"loop count is \" & loopCount)";
anywheresoftware.b4a.keywords.Common.Log("loop count is "+BA.NumberToString(_loopcount));
 }
;
 //BA.debugLineNum = 901;BA.debugLine="Return foundSide";
if (true) return _foundside;
 //BA.debugLineNum = 902;BA.debugLine="End Sub";
return false;
}
public static boolean  _vvvvvvvvvvvvvvvv6() throws Exception{
int _rndside = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
anywheresoftware.b4a.objects.collections.List _chainlist = null;
pineysoft.squarepaddocks.gameactivity._mychain _chain = null;
anywheresoftware.b4a.objects.collections.List _edges = null;
 //BA.debugLineNum = 959;BA.debugLine="Sub TakeSingle2() As Boolean";
 //BA.debugLineNum = 960;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 961;BA.debugLine="Dim foundSquare As GameSquare";
_foundsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 962;BA.debugLine="Dim chainList As List = GetChainList";
_chainlist = new anywheresoftware.b4a.objects.collections.List();
_chainlist = _vvvvvvvvvvvvvv0();
 //BA.debugLineNum = 964;BA.debugLine="If chainList.Size > 0 Then";
if (_chainlist.getSize()>0) { 
 //BA.debugLineNum = 965;BA.debugLine="Dim chain As MyChain = chainList.Get(0)";
_chain = (pineysoft.squarepaddocks.gameactivity._mychain)(_chainlist.Get((int) (0)));
 //BA.debugLineNum = 966;BA.debugLine="foundSquare = chain.square";
_foundsquare = _chain.square;
 //BA.debugLineNum = 968;BA.debugLine="Dim edges As List = foundSquare.GetFreeEdges(-1)";
_edges = new anywheresoftware.b4a.objects.collections.List();
_edges = _foundsquare._vvvv7((int) (-1));
 //BA.debugLineNum = 969;BA.debugLine="If edges.Size > 1 Then";
if (_edges.getSize()>1) { 
 //BA.debugLineNum = 970;BA.debugLine="rndSide = edges.Get(Rnd(0,edges.Size))";
_rndside = (int)(BA.ObjectToNumber(_edges.Get(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),_edges.getSize()))));
 }else {
 //BA.debugLineNum = 972;BA.debugLine="rndSide = edges.get(0)";
_rndside = (int)(BA.ObjectToNumber(_edges.Get((int) (0))));
 };
 //BA.debugLineNum = 975;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_foundsquare,_rndside);
 //BA.debugLineNum = 977;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_rndside);
 //BA.debugLineNum = 979;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_vvvvvvvvvvvv6(_foundsquare,_rndside);
 //BA.debugLineNum = 981;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 984;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 985;BA.debugLine="End Sub";
return false;
}
public static boolean  _vvvvvvvvvvvvvvvvv3(anywheresoftware.b4a.objects.collections.List _foundvalids,boolean _forceany) throws Exception{
int _rndnum = 0;
boolean _foundside = false;
int _rndside = 0;
int _loopcount = 0;
pineysoft.squarepaddocks.gamesquare _foundsquare = null;
anywheresoftware.b4a.objects.collections.List _edges = null;
int _sides = 0;
 //BA.debugLineNum = 905;BA.debugLine="Sub TakeSingle3(foundValids As List, forceAny As Boolean) As Boolean";
 //BA.debugLineNum = 907;BA.debugLine="Dim rndnum As Int";
_rndnum = 0;
 //BA.debugLineNum = 908;BA.debugLine="Dim foundSide As Boolean = False";
_foundside = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 909;BA.debugLine="Dim rndSide As Int";
_rndside = 0;
 //BA.debugLineNum = 910;BA.debugLine="Dim loopCount As Int";
_loopcount = 0;
 //BA.debugLineNum = 911;BA.debugLine="Dim foundSquare As GameSquare";
_foundsquare = new pineysoft.squarepaddocks.gamesquare();
 //BA.debugLineNum = 913;BA.debugLine="Do While foundSide = False AND loopCount < 50";
while (_foundside==anywheresoftware.b4a.keywords.Common.False && _loopcount<50) {
 //BA.debugLineNum = 914;BA.debugLine="If foundValids.Size > 0 Then";
if (_foundvalids.getSize()>0) { 
 //BA.debugLineNum = 915;BA.debugLine="If foundValids.Size > 1 Then";
if (_foundvalids.getSize()>1) { 
 //BA.debugLineNum = 916;BA.debugLine="rndnum = Rnd(1, foundValids.Size)";
_rndnum = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),_foundvalids.getSize());
 }else {
 //BA.debugLineNum = 918;BA.debugLine="rndnum = 1";
_rndnum = (int) (1);
 };
 //BA.debugLineNum = 921;BA.debugLine="foundSquare = foundValids.Get(rndnum - 1)";
_foundsquare = (pineysoft.squarepaddocks.gamesquare)(_foundvalids.Get((int) (_rndnum-1)));
 //BA.debugLineNum = 923;BA.debugLine="Dim edges As List = foundSquare.GetFreeEdges(-1)";
_edges = new anywheresoftware.b4a.objects.collections.List();
_edges = _foundsquare._vvvv7((int) (-1));
 //BA.debugLineNum = 924;BA.debugLine="If edges.Size = 0 Then";
if (_edges.getSize()==0) { 
 //BA.debugLineNum = 925;BA.debugLine="rndSide = Rnd(0,3)";
_rndside = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (3));
 }else if(_edges.getSize()==1) { 
 //BA.debugLineNum = 927;BA.debugLine="rndSide = edges.get(0)";
_rndside = (int)(BA.ObjectToNumber(_edges.Get((int) (0))));
 }else {
 //BA.debugLineNum = 929;BA.debugLine="rndSide = edges.Get(Rnd(0,edges.Size))";
_rndside = (int)(BA.ObjectToNumber(_edges.Get(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),_edges.getSize()))));
 };
 //BA.debugLineNum = 932;BA.debugLine="If foundSquare.IsSideTaken(rndSide) = False Then";
if (_foundsquare._vvvv0(_rndside)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 933;BA.debugLine="If forceAny = True Then";
if (_forceany==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 934;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 936;BA.debugLine="Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)";
_sides = _vvvvvvvvvvvvvvv3(_foundsquare,_rndside);
 //BA.debugLineNum = 937;BA.debugLine="If sides < 2 Then";
if (_sides<2) { 
 //BA.debugLineNum = 938;BA.debugLine="foundSide = True";
_foundside = anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 943;BA.debugLine="If foundSide = True Then";
if (_foundside==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 944;BA.debugLine="UpdateTurn(canv, foundSquare, rndSide)";
_vvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvv5,_foundsquare,_rndside);
 //BA.debugLineNum = 946;BA.debugLine="foundSquare.TakeSide(canv, rndSide)";
_foundsquare._vvvvv4(mostCurrent._vvvvvvvvvvvv5,_rndside);
 //BA.debugLineNum = 948;BA.debugLine="MarkOtherSide2(foundSquare, rndSide)";
_vvvvvvvvvvvv6(_foundsquare,_rndside);
 };
 };
 //BA.debugLineNum = 951;BA.debugLine="loopCount = loopCount + 1";
_loopcount = (int) (_loopcount+1);
 //BA.debugLineNum = 952;BA.debugLine="Log(\"loop count is \" & loopCount)";
anywheresoftware.b4a.keywords.Common.Log("loop count is "+BA.NumberToString(_loopcount));
 }
;
 //BA.debugLineNum = 955;BA.debugLine="Return foundSide";
if (true) return _foundside;
 //BA.debugLineNum = 957;BA.debugLine="End Sub";
return false;
}
public static String  _vvvvvvvvvvvvvvvvv1() throws Exception{
int _currentitem = 0;
int _iloop = 0;
 //BA.debugLineNum = 1266;BA.debugLine="Sub UpdateDroidNumbers";
 //BA.debugLineNum = 1267;BA.debugLine="Dim currentItem As Int = spnDroids.SelectedItem";
_currentitem = (int)(Double.parseDouble(mostCurrent._spndroids.getSelectedItem()));
 //BA.debugLineNum = 1268;BA.debugLine="numberOfPlayers = spnPlayers.SelectedItem";
_vvvvvvvvvvvvvv5 = (int)(Double.parseDouble(mostCurrent._spnplayers.getSelectedItem()));
 //BA.debugLineNum = 1269;BA.debugLine="spnDroids.Clear";
mostCurrent._spndroids.Clear();
 //BA.debugLineNum = 1270;BA.debugLine="Dim iLoop As Int";
_iloop = 0;
 //BA.debugLineNum = 1271;BA.debugLine="For iLoop = 0 To numberOfPlayers - 1";
{
final int step1076 = 1;
final int limit1076 = (int) (_vvvvvvvvvvvvvv5-1);
for (_iloop = (int) (0); (step1076 > 0 && _iloop <= limit1076) || (step1076 < 0 && _iloop >= limit1076); _iloop = ((int)(0 + _iloop + step1076))) {
 //BA.debugLineNum = 1272;BA.debugLine="spnDroids.Add(iLoop)";
mostCurrent._spndroids.Add(BA.NumberToString(_iloop));
 }
};
 //BA.debugLineNum = 1274;BA.debugLine="For iLoop = 0 To spnDroids.Size - 1";
{
final int step1079 = 1;
final int limit1079 = (int) (mostCurrent._spndroids.getSize()-1);
for (_iloop = (int) (0); (step1079 > 0 && _iloop <= limit1079) || (step1079 < 0 && _iloop >= limit1079); _iloop = ((int)(0 + _iloop + step1079))) {
 //BA.debugLineNum = 1275;BA.debugLine="spnDroids.SelectedIndex = iLoop";
mostCurrent._spndroids.setSelectedIndex(_iloop);
 //BA.debugLineNum = 1276;BA.debugLine="If spnDroids.SelectedItem = currentItem Then";
if ((mostCurrent._spndroids.getSelectedItem()).equals(BA.NumberToString(_currentitem))) { 
 //BA.debugLineNum = 1277;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 1280;BA.debugLine="If spnDroids.SelectedItem = -1 Then";
if ((mostCurrent._spndroids.getSelectedItem()).equals(BA.NumberToString(-1))) { 
 //BA.debugLineNum = 1281;BA.debugLine="spnDroids.SelectedIndex = 0";
mostCurrent._spndroids.setSelectedIndex((int) (0));
 };
 //BA.debugLineNum = 1283;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv0() throws Exception{
 //BA.debugLineNum = 351;BA.debugLine="Sub UpdateLabels";
 //BA.debugLineNum = 352;BA.debugLine="lblRows.Text = \"Rows : \" & (sbRows.Value + 4)";
mostCurrent._lblrows.setText((Object)("Rows : "+BA.NumberToString((mostCurrent._sbrows.getValue()+4))));
 //BA.debugLineNum = 353;BA.debugLine="lblColumns.Text = \"Columns : \" & (sbColumns.Value + 4)";
mostCurrent._lblcolumns.setText((Object)("Columns : "+BA.NumberToString((mostCurrent._sbcolumns.getValue()+4))));
 //BA.debugLineNum = 354;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvv0() throws Exception{
 //BA.debugLineNum = 587;BA.debugLine="Sub UpdatePlayerNumber";
 //BA.debugLineNum = 588;BA.debugLine="currentPlayer = currentPlayer + 1";
_vvvvvvvvvvvv3 = (short) (_vvvvvvvvvvvv3+1);
 //BA.debugLineNum = 589;BA.debugLine="If currentPlayer > numberOfPlayers - 1 Then currentPlayer = 0";
if (_vvvvvvvvvvvv3>_vvvvvvvvvvvvvv5-1) { 
_vvvvvvvvvvvv3 = (short) (0);};
 //BA.debugLineNum = 590;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvv3(pineysoft.squarepaddocks.player _currplayer) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _temp = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 //BA.debugLineNum = 559;BA.debugLine="Sub UpdateScore(currPlayer As Player)";
 //BA.debugLineNum = 560;BA.debugLine="Dim temp As View = GetViewByTag1(pnlBase, \"P\" & (currentPlayer + 1))";
_temp = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_temp = _vvvvvvvvvvvvvv7((anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(mostCurrent._pnlbase.getObject())),"P"+BA.NumberToString((_vvvvvvvvvvvv3+1)));
 //BA.debugLineNum = 561;BA.debugLine="If temp Is Label Then";
if (_temp.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 562;BA.debugLine="Dim lbl As Label = temp";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl.setObject((android.widget.TextView)(_temp.getObject()));
 //BA.debugLineNum = 563;BA.debugLine="If currPlayer.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then";
if (_currplayer._vvvv1==_v6._player_type_human) { 
 //BA.debugLineNum = 564;BA.debugLine="lbl.Text = \"Player \" & (currentPlayer + 1) & \" : \" & currPlayer.Score";
_lbl.setText((Object)("Player "+BA.NumberToString((_vvvvvvvvvvvv3+1))+" : "+BA.NumberToString(_currplayer._vvv6)));
 }else {
 //BA.debugLineNum = 566;BA.debugLine="lbl.Text = \"Droid \" & (currentPlayer + 1) & \" : \" & currPlayer.Score";
_lbl.setText((Object)("Droid "+BA.NumberToString((_vvvvvvvvvvvv3+1))+" : "+BA.NumberToString(_currplayer._vvv6)));
 };
 };
 //BA.debugLineNum = 569;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvv4(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnv,pineysoft.squarepaddocks.gamesquare _currentsquare,int _chosenside) throws Exception{
pineysoft.squarepaddocks.turn _lastturn = null;
pineysoft.squarepaddocks.turn _newturn = null;
 //BA.debugLineNum = 571;BA.debugLine="Sub UpdateTurn(cnv As Canvas, currentSquare As GameSquare, chosenSide As Int)";
 //BA.debugLineNum = 572;BA.debugLine="Dim lastTurn As Turn";
_lastturn = new pineysoft.squarepaddocks.turn();
 //BA.debugLineNum = 573;BA.debugLine="Dim newTurn As Turn";
_newturn = new pineysoft.squarepaddocks.turn();
 //BA.debugLineNum = 576;BA.debugLine="If gameTurns.Size > 0 Then";
if (mostCurrent._vvvvvvvvvvvvvvv5.getSize()>0) { 
 //BA.debugLineNum = 577;BA.debugLine="lastTurn = gameTurns.Get(gameTurns.Size - 1)";
_lastturn = (pineysoft.squarepaddocks.turn)(mostCurrent._vvvvvvvvvvvvvvv5.Get((int) (mostCurrent._vvvvvvvvvvvvvvv5.getSize()-1)));
 //BA.debugLineNum = 578;BA.debugLine="lastTurn.Square.DrawEdge2(canv,lastTurn.Edge,Colors.LightGray)";
_lastturn._vvvvvv7._vvvv6(mostCurrent._vvvvvvvvvvvv5,_lastturn._vvvvvv0,anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 };
 //BA.debugLineNum = 582;BA.debugLine="newTurn.Initialize(currentSquare, chosenSide, currentPlayer)";
_newturn._initialize(mostCurrent.activityBA,_currentsquare,_chosenside,(int) (_vvvvvvvvvvvv3));
 //BA.debugLineNum = 585;BA.debugLine="gameTurns.Add(newTurn)";
mostCurrent._vvvvvvvvvvvvvvv5.Add((Object)(_newturn));
 //BA.debugLineNum = 586;BA.debugLine="End Sub";
return "";
}
}
