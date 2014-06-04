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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "pineysoft.squarepaddocks", "pineysoft.squarepaddocks.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "pineysoft.squarepaddocks", "pineysoft.squarepaddocks.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _vv4 = null;
public static anywheresoftware.b4a.objects.Serial _vv5 = null;
public static anywheresoftware.b4a.objects.collections.List _vv6 = null;
public static pineysoft.squarepaddocks.main._nameandmac _vv7 = null;
public static boolean _vv3 = false;
public anywheresoftware.b4a.objects.ButtonWrapper _btngtcontinue = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _rdbbluetooth = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _rdblocalplay = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _rdbwifi = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndiscoverme = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsearch = null;
public pineysoft.squarepaddocks.gameactivity _vvv3 = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (gameactivity.mostCurrent != null);
return vis;}
public static class _nameandmac{
public boolean IsInitialized;
public String Name;
public String Mac;
public void Initialize() {
IsInitialized = true;
Name = "";
Mac = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 39;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 40;BA.debugLine="admin.Initialize(\"admin\")";
_vv4.Initialize(processBA,"admin");
 //BA.debugLineNum = 41;BA.debugLine="serial1.Initialize(\"serial1\")";
_vv5.Initialize("serial1");
 };
 //BA.debugLineNum = 43;BA.debugLine="Activity.LoadLayout(\"GameTypeScreen\")";
mostCurrent._activity.LoadLayout("GameTypeScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 44;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 45;BA.debugLine="rdbLocalPlay.Checked = True";
mostCurrent._rdblocalplay.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 46;BA.debugLine="UpdateRadioButtonSettings";
_vvvvvvv2();
 };
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 57;BA.debugLine="If UserClosed = True Then";
if (_userclosed==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 58;BA.debugLine="serial1.Disconnect";
_vv5.Disconnect();
 };
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 52;BA.debugLine="btnSearch.Enabled = False";
mostCurrent._btnsearch.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 53;BA.debugLine="btnDiscoverMe.Enabled = False";
mostCurrent._btndiscoverme.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _admin_devicefound(String _name,String _macaddress) throws Exception{
pineysoft.squarepaddocks.main._nameandmac _nm = null;
 //BA.debugLineNum = 142;BA.debugLine="Sub Admin_DeviceFound (Name As String, MacAddress As String)";
 //BA.debugLineNum = 143;BA.debugLine="Log(Name & \":\" & MacAddress)";
anywheresoftware.b4a.keywords.Common.Log(_name+":"+_macaddress);
 //BA.debugLineNum = 144;BA.debugLine="Dim nm As NameAndMac";
_nm = new pineysoft.squarepaddocks.main._nameandmac();
 //BA.debugLineNum = 145;BA.debugLine="nm.Name = Name";
_nm.Name = _name;
 //BA.debugLineNum = 146;BA.debugLine="nm.Mac = MacAddress";
_nm.Mac = _macaddress;
 //BA.debugLineNum = 147;BA.debugLine="foundDevices.Add(nm)";
_vv6.Add((Object)(_nm));
 //BA.debugLineNum = 148;BA.debugLine="ProgressDialogShow(\"Searching for devices (~ device found)...\".Replace(\"~\", foundDevices.Size))";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Searching for devices (~ device found)...".replace("~",BA.NumberToString(_vv6.getSize())));
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _admin_discoveryfinished() throws Exception{
anywheresoftware.b4a.objects.collections.List _l = null;
int _i = 0;
pineysoft.squarepaddocks.main._nameandmac _nm = null;
int _res = 0;
 //BA.debugLineNum = 119;BA.debugLine="Sub Admin_DiscoveryFinished";
 //BA.debugLineNum = 120;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 121;BA.debugLine="If foundDevices.Size = 0 Then";
if (_vv6.getSize()==0) { 
 //BA.debugLineNum = 122;BA.debugLine="ToastMessageShow(\"No device found.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("No device found.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 124;BA.debugLine="Dim l As List";
_l = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 125;BA.debugLine="l.Initialize";
_l.Initialize();
 //BA.debugLineNum = 126;BA.debugLine="For i = 0 To foundDevices.Size - 1";
{
final int step93 = 1;
final int limit93 = (int) (_vv6.getSize()-1);
for (_i = (int) (0); (step93 > 0 && _i <= limit93) || (step93 < 0 && _i >= limit93); _i = ((int)(0 + _i + step93))) {
 //BA.debugLineNum = 127;BA.debugLine="Dim nm As NameAndMac";
_nm = new pineysoft.squarepaddocks.main._nameandmac();
 //BA.debugLineNum = 128;BA.debugLine="nm = foundDevices.Get(i)";
_nm = (pineysoft.squarepaddocks.main._nameandmac)(_vv6.Get(_i));
 //BA.debugLineNum = 129;BA.debugLine="l.Add(nm.Name)";
_l.Add((Object)(_nm.Name));
 }
};
 //BA.debugLineNum = 131;BA.debugLine="Dim res As Int";
_res = 0;
 //BA.debugLineNum = 132;BA.debugLine="res = InputList(l, \"Choose device to connect\", -1)";
_res = anywheresoftware.b4a.keywords.Common.InputList(_l,"Choose device to connect",(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 133;BA.debugLine="If res <> DialogResponse.CANCEL Then";
if (_res!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 134;BA.debugLine="connectedDevice = foundDevices.Get(res)";
_vv7 = (pineysoft.squarepaddocks.main._nameandmac)(_vv6.Get(_res));
 //BA.debugLineNum = 135;BA.debugLine="ProgressDialogShow(\"Trying to connect to: \" & connectedDevice.Name & \" (\" & connectedDevice.Mac & \")\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Trying to connect to: "+_vv7.Name+" ("+_vv7.Mac+")");
 //BA.debugLineNum = 136;BA.debugLine="GameActivity.IsMaster = True";
mostCurrent._vvv3._vv3 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 137;BA.debugLine="serial1.Connect(connectedDevice.Mac)";
_vv5.Connect(processBA,_vv7.Mac);
 };
 };
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _admin_statechanged(int _newstate,int _oldstate) throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub Admin_StateChanged (NewState As Int, OldState As Int)";
 //BA.debugLineNum = 63;BA.debugLine="btnSearch.Enabled = (NewState = admin.STATE_ON)";
mostCurrent._btnsearch.setEnabled((_newstate==_vv4.STATE_ON));
 //BA.debugLineNum = 64;BA.debugLine="btnDiscoverMe.Enabled = btnSearch.Enabled";
mostCurrent._btndiscoverme.setEnabled(mostCurrent._btnsearch.getEnabled());
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _btndiscoverme_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 101;BA.debugLine="Sub btnDiscoverMe_Click";
 //BA.debugLineNum = 103;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 104;BA.debugLine="i.Initialize(\"android.bluetooth.adapter.action.REQUEST_DISCOVERABLE\", \"\")";
_i.Initialize("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE","");
 //BA.debugLineNum = 105;BA.debugLine="i.PutExtra(\"android.bluetooth.adapter.extra.DISCOVERABLE_DURATION\", 300)";
_i.PutExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION",(Object)(300));
 //BA.debugLineNum = 106;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 108;BA.debugLine="serial1.Listen";
_vv5.Listen(processBA);
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
return "";
}
public static String  _btngtcontinue_click() throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub btnGTContinue_Click";
 //BA.debugLineNum = 84;BA.debugLine="StartActivity(GameActivity)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._vvv3.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _btnsearch_click() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub btnSearch_Click";
 //BA.debugLineNum = 111;BA.debugLine="foundDevices.Initialize";
_vv6.Initialize();
 //BA.debugLineNum = 112;BA.debugLine="If admin.StartDiscovery	= False Then";
if (_vv4.StartDiscovery()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 113;BA.debugLine="ToastMessageShow(\"Error starting discovery process.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error starting discovery process.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 115;BA.debugLine="ProgressDialogShow(\"Searching for devices...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Searching for devices...");
 };
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
gameactivity._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 29;BA.debugLine="Dim btnGTContinue As Button";
mostCurrent._btngtcontinue = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private rdbBlueTooth As RadioButton";
mostCurrent._rdbbluetooth = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private rdbLocalPlay As RadioButton";
mostCurrent._rdblocalplay = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private rdbWifi As RadioButton";
mostCurrent._rdbwifi = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private btnDiscoverMe As Button";
mostCurrent._btndiscoverme = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private btnSearch As Button";
mostCurrent._btnsearch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim admin As BluetoothAdmin";
_vv4 = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 19;BA.debugLine="Dim serial1 As Serial";
_vv5 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 20;BA.debugLine="Dim foundDevices As List";
_vv6 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Type NameAndMac (Name As String, Mac As String)";
;
 //BA.debugLineNum = 22;BA.debugLine="Dim connectedDevice As NameAndMac";
_vv7 = new pineysoft.squarepaddocks.main._nameandmac();
 //BA.debugLineNum = 23;BA.debugLine="Dim IsMaster As Boolean";
_vv3 = false;
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _rdbbluetooth_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub rdbBlueTooth_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 87;BA.debugLine="If admin.IsEnabled = False Then";
if (_vv4.IsEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 88;BA.debugLine="If admin.Enable = False Then";
if (_vv4.Enable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 89;BA.debugLine="ToastMessageShow(\"Error enabling Bluetooth adapter.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error enabling Bluetooth adapter.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 90;BA.debugLine="rdbLocalPlay.Checked = True";
mostCurrent._rdblocalplay.setChecked(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 92;BA.debugLine="ToastMessageShow(\"Enabling Bluetooth adapter...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Enabling Bluetooth adapter...",anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 96;BA.debugLine="Admin_StateChanged(admin.STATE_ON, 0)";
_admin_statechanged(_vv4.STATE_ON,(int) (0));
 };
 //BA.debugLineNum = 99;BA.debugLine="UpdateRadioButtonSettings";
_vvvvvvv2();
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public static String  _rdblocalplay_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 174;BA.debugLine="Sub rdbLocalPlay_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 175;BA.debugLine="UpdateRadioButtonSettings";
_vvvvvvv2();
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
pineysoft.squarepaddocks.constants _spconst = null;
 //BA.debugLineNum = 152;BA.debugLine="Sub Serial1_Connected (Success As Boolean)";
 //BA.debugLineNum = 154;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 155;BA.debugLine="Log(\"connected: \" & Success)";
anywheresoftware.b4a.keywords.Common.Log("connected: "+BA.ObjectToString(_success));
 //BA.debugLineNum = 156;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 157;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 //BA.debugLineNum = 158;BA.debugLine="ToastMessageShow(\"Error connecting: \" & LastException.Message, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error connecting: "+anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 160;BA.debugLine="Dim spConst As Constants";
_spconst = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 161;BA.debugLine="spConst.Initialize";
_spconst._initialize(processBA);
 //BA.debugLineNum = 163;BA.debugLine="If rdbBlueTooth.Checked Then";
if (mostCurrent._rdbbluetooth.getChecked()) { 
 //BA.debugLineNum = 164;BA.debugLine="GameActivity.GameMode = spConst.GAMETYPE_MODE_BT";
mostCurrent._vvv3._vv2 = _spconst._gametype_mode_bt;
 }else if(mostCurrent._rdbwifi.getChecked()) { 
 //BA.debugLineNum = 166;BA.debugLine="GameActivity.GameMode = spConst.GAMETYPE_MODE_WIFI";
mostCurrent._vvv3._vv2 = _spconst._gametype_mode_wifi;
 }else {
 //BA.debugLineNum = 168;BA.debugLine="GameActivity.GameMode = spConst.GAMETYPE_MODE_LOC";
mostCurrent._vvv3._vv2 = _spconst._gametype_mode_loc;
 };
 //BA.debugLineNum = 171;BA.debugLine="StartActivity(GameActivity)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._vvv3.getObject()));
 };
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvv2() throws Exception{
pineysoft.squarepaddocks.constants _spconst = null;
 //BA.debugLineNum = 67;BA.debugLine="Sub UpdateRadioButtonSettings";
 //BA.debugLineNum = 68;BA.debugLine="Dim spConst As Constants";
_spconst = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 69;BA.debugLine="spConst.Initialize";
_spconst._initialize(processBA);
 //BA.debugLineNum = 71;BA.debugLine="If rdbLocalPlay.checked Then";
if (mostCurrent._rdblocalplay.getChecked()) { 
 //BA.debugLineNum = 72;BA.debugLine="btnDiscoverMe.Enabled = False";
mostCurrent._btndiscoverme.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 73;BA.debugLine="btnSearch.Enabled = False";
mostCurrent._btnsearch.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 74;BA.debugLine="btnGTContinue.Enabled = True";
mostCurrent._btngtcontinue.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 75;BA.debugLine="GameActivity.GameMode = spConst.GAMETYPE_MODE_LOC";
mostCurrent._vvv3._vv2 = _spconst._gametype_mode_loc;
 }else if(mostCurrent._rdbbluetooth.getChecked()) { 
 //BA.debugLineNum = 77;BA.debugLine="btnDiscoverMe.Enabled = True";
mostCurrent._btndiscoverme.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 78;BA.debugLine="btnSearch.Enabled = True";
mostCurrent._btnsearch.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 79;BA.debugLine="btnGTContinue.Enabled = False";
mostCurrent._btngtcontinue.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 80;BA.debugLine="GameActivity.GameMode = spConst.GAMETYPE_MODE_BT";
mostCurrent._vvv3._vv2 = _spconst._gametype_mode_bt;
 };
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
}
