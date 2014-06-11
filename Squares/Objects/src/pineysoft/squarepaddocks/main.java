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
public static anywheresoftware.b4a.phone.Phone.ContentChooser _cc = null;
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _admin = null;
public static anywheresoftware.b4a.objects.collections.List _btdevices = null;
public static boolean _searchinprogress = false;
public static String _ip = "";
public static boolean _ismaster = false;
public static boolean _gamestart = false;
public static pineysoft.squarepaddocks.constants _spconstants = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconnectwifi = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblip = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnrpaireddevices = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtip = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconnectbt = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblwifistatus = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbtstatus = null;
public anywheresoftware.b4a.objects.IME _ime = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblprogress = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfile = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmakediscoverable = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbtsearch = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtmessage = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstartgame = null;
public b4a.example.dateutils _dateutils = null;
public pineysoft.squarepaddocks.gameactivity _gameactivity = null;
public pineysoft.squarepaddocks.netconn _netconn = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (gameactivity.mostCurrent != null);
return vis;}
public static class _nameandmac{
public boolean IsInitialized;
public String name;
public String mac;
public void Initialize() {
IsInitialized = true;
name = "";
mac = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.Serial _serial1 = null;
anywheresoftware.b4a.objects.collections.Map _paireddevices = null;
int _i = 0;
pineysoft.squarepaddocks.main._nameandmac _nm = null;
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 46;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 47;BA.debugLine="StartService(NetConn)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="SpConstants.Initialize";
_spconstants._initialize(processBA);
 //BA.debugLineNum = 49;BA.debugLine="btDevices.Initialize";
_btdevices.Initialize();
 //BA.debugLineNum = 50;BA.debugLine="admin.Initialize(\"Admin\")";
_admin.Initialize(processBA,"Admin");
 //BA.debugLineNum = 51;BA.debugLine="CC.Initialize(\"CC\")";
_cc.Initialize("CC");
 //BA.debugLineNum = 55;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 56;BA.debugLine="serial1.Initialize(\"\")";
_serial1.Initialize("");
 //BA.debugLineNum = 57;BA.debugLine="Dim pairedDevices As Map = serial1.GetPairedDevices";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
_paireddevices = _serial1.GetPairedDevices();
 //BA.debugLineNum = 58;BA.debugLine="For i = 0 To pairedDevices.Size - 1";
{
final int step38 = 1;
final int limit38 = (int) (_paireddevices.getSize()-1);
for (_i = (int) (0); (step38 > 0 && _i <= limit38) || (step38 < 0 && _i >= limit38); _i = ((int)(0 + _i + step38))) {
 //BA.debugLineNum = 59;BA.debugLine="Dim nm As NameAndMac";
_nm = new pineysoft.squarepaddocks.main._nameandmac();
 //BA.debugLineNum = 60;BA.debugLine="nm.Initialize";
_nm.Initialize();
 //BA.debugLineNum = 61;BA.debugLine="nm.mac = pairedDevices.GetValueAt(i)";
_nm.mac = BA.ObjectToString(_paireddevices.GetValueAt(_i));
 //BA.debugLineNum = 62;BA.debugLine="nm.name = pairedDevices.GetKeyAt(i)";
_nm.name = BA.ObjectToString(_paireddevices.GetKeyAt(_i));
 //BA.debugLineNum = 63;BA.debugLine="btDevices.Add(nm)";
_btdevices.Add((Object)(_nm));
 }
};
 };
 //BA.debugLineNum = 66;BA.debugLine="IME.Initialize(\"\")";
mostCurrent._ime.Initialize("");
 //BA.debugLineNum = 67;BA.debugLine="Activity.LoadLayout(\"GameTypeScreen\")";
mostCurrent._activity.LoadLayout("GameTypeScreen",mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="Activity.AddMenuItem(\"Disconnect\", \"mnuDisconnect\")";
mostCurrent._activity.AddMenuItem("Disconnect","mnuDisconnect");
 //BA.debugLineNum = 70;BA.debugLine="IME.SetCustomFilter(edtIP, edtIP.INPUT_TYPE_DECIMAL_NUMBERS, \"0123456789.\")";
mostCurrent._ime.SetCustomFilter((android.widget.EditText)(mostCurrent._edtip.getObject()),mostCurrent._edtip.INPUT_TYPE_DECIMAL_NUMBERS,"0123456789.");
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 100;BA.debugLine="ip = edtIP.Text";
_ip = mostCurrent._edtip.getText();
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
pineysoft.squarepaddocks.main._nameandmac _nm = null;
 //BA.debugLineNum = 73;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 74;BA.debugLine="edtIP.Text = ip";
mostCurrent._edtip.setText((Object)(_ip));
 //BA.debugLineNum = 75;BA.debugLine="spnrPairedDevices.Clear";
mostCurrent._spnrpaireddevices.Clear();
 //BA.debugLineNum = 76;BA.debugLine="For Each nm As NameAndMac In btDevices";
final anywheresoftware.b4a.BA.IterableList group54 = _btdevices;
final int groupLen54 = group54.getSize();
for (int index54 = 0;index54 < groupLen54 ;index54++){
_nm = (pineysoft.squarepaddocks.main._nameandmac)(group54.Get(index54));
 //BA.debugLineNum = 77;BA.debugLine="spnrPairedDevices.Add(nm.name)";
mostCurrent._spnrpaireddevices.Add(_nm.name);
 }
;
 //BA.debugLineNum = 80;BA.debugLine="If admin.IsEnabled = False Then";
if (_admin.IsEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 81;BA.debugLine="If admin.Enable = False Then";
if (_admin.Enable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 82;BA.debugLine="ToastMessageShow(\"Error enabling Bluetooth adapter.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error enabling Bluetooth adapter.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 84;BA.debugLine="ToastMessageShow(\"Enabling Bluetooth adapter...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Enabling Bluetooth adapter...",anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 88;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 89;BA.debugLine="UpdateProgress";
_updateprogress();
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _admin_devicefound(String _name,String _macaddress) throws Exception{
pineysoft.squarepaddocks.main._nameandmac _nm = null;
 //BA.debugLineNum = 187;BA.debugLine="Sub Admin_DeviceFound (Name As String, MacAddress As String)";
 //BA.debugLineNum = 188;BA.debugLine="Log(Name & \":\" & MacAddress)";
anywheresoftware.b4a.keywords.Common.Log(_name+":"+_macaddress);
 //BA.debugLineNum = 189;BA.debugLine="spnrPairedDevices.Add(Name)";
mostCurrent._spnrpaireddevices.Add(_name);
 //BA.debugLineNum = 190;BA.debugLine="Dim nm As NameAndMac";
_nm = new pineysoft.squarepaddocks.main._nameandmac();
 //BA.debugLineNum = 191;BA.debugLine="nm.Initialize";
_nm.Initialize();
 //BA.debugLineNum = 192;BA.debugLine="nm.Name = Name";
_nm.name = _name;
 //BA.debugLineNum = 193;BA.debugLine="nm.mac = MacAddress";
_nm.mac = _macaddress;
 //BA.debugLineNum = 194;BA.debugLine="btDevices.Add(nm)";
_btdevices.Add((Object)(_nm));
 //BA.debugLineNum = 195;BA.debugLine="SetBTStatus(\"Searching for devices (\" & btDevices.Size & \" found)\")";
_setbtstatus("Searching for devices ("+BA.NumberToString(_btdevices.getSize())+" found)");
 //BA.debugLineNum = 196;BA.debugLine="End Sub";
return "";
}
public static String  _admin_discoveryfinished() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub Admin_DiscoveryFinished";
 //BA.debugLineNum = 178;BA.debugLine="searchInProgress = False";
_searchinprogress = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 179;BA.debugLine="If spnrPairedDevices.Size = 0 Then";
if (mostCurrent._spnrpaireddevices.getSize()==0) { 
 //BA.debugLineNum = 180;BA.debugLine="SetBTStatus(\"No BT devices found.\")";
_setbtstatus("No BT devices found.");
 }else {
 //BA.debugLineNum = 182;BA.debugLine="SetBTStatus(spnrPairedDevices.Size & \" device(s) found.\")";
_setbtstatus(BA.NumberToString(mostCurrent._spnrpaireddevices.getSize())+" device(s) found.");
 };
 //BA.debugLineNum = 184;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 185;BA.debugLine="End Sub";
return "";
}
public static String  _admin_statechanged(int _newstate,int _oldstate) throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub Admin_StateChanged (NewState As Int, OldState As Int)";
 //BA.debugLineNum = 104;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static String  _btnbtsearch_click() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub btnBTSearch_Click";
 //BA.debugLineNum = 161;BA.debugLine="spnrPairedDevices.Clear";
mostCurrent._spnrpaireddevices.Clear();
 //BA.debugLineNum = 162;BA.debugLine="btDevices.Clear";
_btdevices.Clear();
 //BA.debugLineNum = 163;BA.debugLine="If admin.StartDiscovery	= False Then";
if (_admin.StartDiscovery()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 164;BA.debugLine="ToastMessageShow(\"Error starting discovery process.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error starting discovery process.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 166;BA.debugLine="searchInProgress = True";
_searchinprogress = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 167;BA.debugLine="SetBTStatus(\"Searching for BT devices...\")";
_setbtstatus("Searching for BT devices...");
 //BA.debugLineNum = 168;BA.debugLine="UpdateUI";
_updateui();
 };
 //BA.debugLineNum = 170;BA.debugLine="End Sub";
return "";
}
public static String  _btnchoosefile_click() throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub btnChooseFile_Click";
 //BA.debugLineNum = 211;BA.debugLine="CC.Show(\"*/*\", \"Choose file to send\")";
_cc.Show(processBA,"*/*","Choose file to send");
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _btnconnectbt_click() throws Exception{
pineysoft.squarepaddocks.main._nameandmac _nm = null;
 //BA.debugLineNum = 153;BA.debugLine="Sub btnConnectBT_Click";
 //BA.debugLineNum = 154;BA.debugLine="If spnrPairedDevices.SelectedIndex = -1 Then Return";
if (mostCurrent._spnrpaireddevices.getSelectedIndex()==-1) { 
if (true) return "";};
 //BA.debugLineNum = 155;BA.debugLine="Dim nm As NameAndMac = btDevices.Get(spnrPairedDevices.SelectedIndex)";
_nm = (pineysoft.squarepaddocks.main._nameandmac)(_btdevices.Get(mostCurrent._spnrpaireddevices.getSelectedIndex()));
 //BA.debugLineNum = 156;BA.debugLine="CallSubDelayed2(NetConn, \"ConnectBT\", nm.mac)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()),"ConnectBT",(Object)(_nm.mac));
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _btnconnectwifi_click() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub btnConnectWifi_Click";
 //BA.debugLineNum = 150;BA.debugLine="CallSubDelayed2(NetConn, \"ConnectWifi\", edtIP.Text)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()),"ConnectWifi",(Object)(mostCurrent._edtip.getText()));
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _btnmakediscoverable_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 198;BA.debugLine="Sub btnMakeDiscoverable_Click";
 //BA.debugLineNum = 200;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 201;BA.debugLine="i.Initialize(\"android.bluetooth.adapter.action.REQUEST_DISCOVERABLE\", \"\")";
_i.Initialize("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE","");
 //BA.debugLineNum = 202;BA.debugLine="i.PutExtra(\"android.bluetooth.adapter.extra.DISCOVERABLE_DURATION\", 300)";
_i.PutExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION",(Object)(300));
 //BA.debugLineNum = 203;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
return "";
}
public static String  _btnstartgame_click() throws Exception{
 //BA.debugLineNum = 256;BA.debugLine="Sub btnStartGame_Click";
 //BA.debugLineNum = 257;BA.debugLine="If NetConn.BTConnected Then";
if (mostCurrent._netconn._btconnected) { 
 //BA.debugLineNum = 258;BA.debugLine="GameActivity.GameMode = SpConstants.GAMETYPE_MODE_BT";
mostCurrent._gameactivity._gamemode = _spconstants._gametype_mode_bt;
 }else if(mostCurrent._netconn._wificonnected) { 
 //BA.debugLineNum = 260;BA.debugLine="GameActivity.GameMode = SpConstants.GAMETYPE_MODE_WIFI";
mostCurrent._gameactivity._gamemode = _spconstants._gametype_mode_wifi;
 };
 //BA.debugLineNum = 263;BA.debugLine="If GameActivity.GameMode <> SpConstants.GAMETYPE_MODE_LOC Then";
if ((mostCurrent._gameactivity._gamemode).equals(_spconstants._gametype_mode_loc) == false) { 
 //BA.debugLineNum = 264;BA.debugLine="CallSubDelayed2(\"NetConn\",\"SendMessage\",\"M,\" & GameActivity.GameMode)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(mostCurrent.activityBA,(Object)("NetConn"),"SendMessage",(Object)("M,"+mostCurrent._gameactivity._gamemode));
 };
 //BA.debugLineNum = 267;BA.debugLine="GameActivity.IsMaster = True";
mostCurrent._gameactivity._ismaster = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 268;BA.debugLine="StartActivity(GameActivity)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._gameactivity.getObject()));
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
public static String  _cc_result(boolean _success,String _dir,String _filename) throws Exception{
String _normalizedfile = "";
 //BA.debugLineNum = 214;BA.debugLine="Sub CC_Result (Success As Boolean, Dir As String, FileName As String)";
 //BA.debugLineNum = 215;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 216;BA.debugLine="Dim normalizedFile As String = GetPathFromContentResult(FileName)";
_normalizedfile = _getpathfromcontentresult(_filename);
 //BA.debugLineNum = 217;BA.debugLine="If File.Exists(\"\", normalizedFile) == False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists("",_normalizedfile)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 218;BA.debugLine="ToastMessageShow(\"File is not accessible.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("File is not accessible.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 219;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 221;BA.debugLine="CallSubDelayed3(NetConn, \"SendFile\", \"\", normalizedFile)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed3(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()),"SendFile",(Object)(""),(Object)(_normalizedfile));
 }else {
 //BA.debugLineNum = 223;BA.debugLine="If LastException.IsInitialized Then ToastMessageShow(LastException.Message, True)";
if (anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).IsInitialized()) { 
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);};
 };
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _edtip_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 206;BA.debugLine="Sub edtIP_TextChanged (Old As String, New As String)";
 //BA.debugLineNum = 207;BA.debugLine="UpdateUI 'the btnConnectWifi.Enabled depends on the text";
_updateui();
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
return "";
}
public static String  _getpathfromcontentresult(String _uristring) throws Exception{
String[] _proj = null;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
Object _uri = null;
String _res = "";
 //BA.debugLineNum = 228;BA.debugLine="Sub GetPathFromContentResult(UriString As String) As String";
 //BA.debugLineNum = 229;BA.debugLine="If UriString.StartsWith(\"/\") Then Return UriString 'If the user used a file manager to choose the image";
if (_uristring.startsWith("/")) { 
if (true) return _uristring;};
 //BA.debugLineNum = 230;BA.debugLine="Dim Proj() As String";
_proj = new String[(int) (0)];
java.util.Arrays.fill(_proj,"");
 //BA.debugLineNum = 231;BA.debugLine="Proj = Array As String(\"_data\")";
_proj = new String[]{"_data"};
 //BA.debugLineNum = 232;BA.debugLine="Dim Cursor As Cursor";
_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 233;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 234;BA.debugLine="Dim Uri As Object";
_uri = new Object();
 //BA.debugLineNum = 235;BA.debugLine="Uri = r.RunStaticMethod(\"android.net.Uri\", \"parse\", _         Array As Object(UriString), _         Array As String(\"java.lang.String\"))";
_uri = _r.RunStaticMethod("android.net.Uri","parse",new Object[]{(Object)(_uristring)},new String[]{"java.lang.String"});
 //BA.debugLineNum = 238;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(processBA));
 //BA.debugLineNum = 239;BA.debugLine="r.Target = r.RunMethod(\"getContentResolver\")";
_r.Target = _r.RunMethod("getContentResolver");
 //BA.debugLineNum = 240;BA.debugLine="Cursor = r.RunMethod4(\"query\", _     Array As Object(Uri, Proj, Null, Null, Null), _     Array As String(\"android.net.Uri\", _         \"[Ljava.lang.String;\", \"java.lang.String\", _         \"[Ljava.lang.String;\", \"java.lang.String\"))";
_cursor.setObject((android.database.Cursor)(_r.RunMethod4("query",new Object[]{_uri,(Object)(_proj),anywheresoftware.b4a.keywords.Common.Null,anywheresoftware.b4a.keywords.Common.Null,anywheresoftware.b4a.keywords.Common.Null},new String[]{"android.net.Uri","[Ljava.lang.String;","java.lang.String","[Ljava.lang.String;","java.lang.String"})));
 //BA.debugLineNum = 246;BA.debugLine="Cursor.Position = 0";
_cursor.setPosition((int) (0));
 //BA.debugLineNum = 247;BA.debugLine="Dim res As String";
_res = "";
 //BA.debugLineNum = 248;BA.debugLine="res = Cursor.GetString(\"_data\")";
_res = _cursor.GetString("_data");
 //BA.debugLineNum = 249;BA.debugLine="Cursor.Close";
_cursor.Close();
 //BA.debugLineNum = 250;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
gameactivity._process_globals();
netconn._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Dim btnConnectWifi As Button";
mostCurrent._btnconnectwifi = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim lblIP As Label";
mostCurrent._lblip = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim spnrPairedDevices As Spinner";
mostCurrent._spnrpaireddevices = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim edtIP As EditText";
mostCurrent._edtip = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim btnConnectBT As Button";
mostCurrent._btnconnectbt = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim lblWifiStatus As Label";
mostCurrent._lblwifistatus = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim lblBTStatus As Label";
mostCurrent._lblbtstatus = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim IME As IME";
mostCurrent._ime = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 37;BA.debugLine="Dim lblProgress As Label";
mostCurrent._lblprogress = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim lblFile As Label";
mostCurrent._lblfile = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim btnMakeDiscoverable As Button";
mostCurrent._btnmakediscoverable = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim btnBTSearch As Button";
mostCurrent._btnbtsearch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private txtMessage As EditText";
mostCurrent._txtmessage = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private btnStartGame As Button";
mostCurrent._btnstartgame = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _mnudisconnect_click() throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Sub mnuDisconnect_Click";
 //BA.debugLineNum = 93;BA.debugLine="CallSub(NetConn, \"Disconnect\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(mostCurrent.activityBA,(Object)(mostCurrent._netconn.getObject()),"Disconnect");
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private CC As ContentChooser";
_cc = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 17;BA.debugLine="Private admin As BluetoothAdmin";
_admin = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 18;BA.debugLine="Private btDevices As List";
_btdevices = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Type NameAndMac (name As String, mac As String)";
;
 //BA.debugLineNum = 20;BA.debugLine="Private searchInProgress As Boolean";
_searchinprogress = false;
 //BA.debugLineNum = 21;BA.debugLine="Private ip As String";
_ip = "";
 //BA.debugLineNum = 22;BA.debugLine="Public IsMaster As Boolean";
_ismaster = false;
 //BA.debugLineNum = 23;BA.debugLine="Public gameStart As Boolean";
_gamestart = false;
 //BA.debugLineNum = 24;BA.debugLine="Public SpConstants As Constants";
_spconstants = new pineysoft.squarepaddocks.constants();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _setbtstatus(String _status) throws Exception{
 //BA.debugLineNum = 172;BA.debugLine="Sub SetBTStatus(status As String)";
 //BA.debugLineNum = 173;BA.debugLine="NetConn.BTstatus = status";
mostCurrent._netconn._btstatus = _status;
 //BA.debugLineNum = 174;BA.debugLine="lblBTStatus.Text = status";
mostCurrent._lblbtstatus.setText((Object)(_status));
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _txtmessage_enterpressed() throws Exception{
 //BA.debugLineNum = 253;BA.debugLine="Sub txtMessage_EnterPressed";
 //BA.debugLineNum = 254;BA.debugLine="CallSubDelayed2(\"NetConn\",\"SendMessage\",\"C,\" & txtMessage.Text)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(mostCurrent.activityBA,(Object)("NetConn"),"SendMessage",(Object)("C,"+mostCurrent._txtmessage.getText()));
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _updateprogress() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Public Sub UpdateProgress";
 //BA.debugLineNum = 144;BA.debugLine="lblProgress.Text = NetConn.progressText";
mostCurrent._lblprogress.setText((Object)(mostCurrent._netconn._progresstext));
 //BA.debugLineNum = 145;BA.debugLine="ProgressBar1.Progress = NetConn.progressValue";
mostCurrent._progressbar1.setProgress(mostCurrent._netconn._progressvalue);
 //BA.debugLineNum = 146;BA.debugLine="lblFile.Text = NetConn.lblFile";
mostCurrent._lblfile.setText((Object)(mostCurrent._netconn._lblfile));
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _updateui() throws Exception{
boolean _wifi = false;
boolean _bt = false;
boolean _startgame = false;
boolean _discover = false;
 //BA.debugLineNum = 107;BA.debugLine="Public Sub UpdateUI";
 //BA.debugLineNum = 109;BA.debugLine="Dim wifi = True, bt = True, startGame = True, discover = True As Boolean";
_wifi = anywheresoftware.b4a.keywords.Common.True;
_bt = anywheresoftware.b4a.keywords.Common.True;
_startgame = anywheresoftware.b4a.keywords.Common.True;
_discover = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 110;BA.debugLine="If admin.IsEnabled = False Then";
if (_admin.IsEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 111;BA.debugLine="bt = False";
_bt = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 112;BA.debugLine="discover = False";
_discover = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 114;BA.debugLine="lblIP.Text = NetConn.MyIP";
mostCurrent._lblip.setText((Object)(mostCurrent._netconn._myip));
 //BA.debugLineNum = 115;BA.debugLine="lblWifiStatus.Text = NetConn.WifiStatus";
mostCurrent._lblwifistatus.setText((Object)(mostCurrent._netconn._wifistatus));
 //BA.debugLineNum = 116;BA.debugLine="lblBTStatus.Text = NetConn.BTStatus";
mostCurrent._lblbtstatus.setText((Object)(mostCurrent._netconn._btstatus));
 //BA.debugLineNum = 117;BA.debugLine="If NetConn.WifiConnected Then lblWifiStatus.TextColor = Colors.Green Else lblWifiStatus.TextColor = Colors.Red";
if (mostCurrent._netconn._wificonnected) { 
mostCurrent._lblwifistatus.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._lblwifistatus.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 118;BA.debugLine="If NetConn.BTConnected Then lblBTStatus.TextColor = Colors.Green Else lblBTStatus.TextColor = Colors.Red";
if (mostCurrent._netconn._btconnected) { 
mostCurrent._lblbtstatus.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._lblbtstatus.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 119;BA.debugLine="If spnrPairedDevices.Size = 0 Then bt = False";
if (mostCurrent._spnrpaireddevices.getSize()==0) { 
_bt = anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 120;BA.debugLine="If NetConn.BTConnected OR NetConn.WifiConnected Then";
if (mostCurrent._netconn._btconnected || mostCurrent._netconn._wificonnected) { 
 //BA.debugLineNum = 121;BA.debugLine="wifi = False";
_wifi = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 122;BA.debugLine="bt = False";
_bt = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 123;BA.debugLine="discover = False";
_discover = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 125;BA.debugLine="gameStart = False";
_gamestart = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 128;BA.debugLine="If wifi AND Regex.IsMatch(\"[^.]+\\.[^.]+\\.[^.]+\\.[^.]+\", edtIP.Text) = False Then";
if (_wifi && anywheresoftware.b4a.keywords.Common.Regex.IsMatch("[^.]+\\.[^.]+\\.[^.]+\\.[^.]+",mostCurrent._edtip.getText())==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 129;BA.debugLine="wifi = False";
_wifi = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 131;BA.debugLine="If NetConn.MyIP.Length = 0 OR NetConn.MyIP = \"127.0.0.1\" Then";
if (mostCurrent._netconn._myip.length()==0 || (mostCurrent._netconn._myip).equals("127.0.0.1")) { 
 //BA.debugLineNum = 132;BA.debugLine="wifi = False";
_wifi = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 134;BA.debugLine="If NetConn.SendingFile OR NetConn.ReceivingFile Then gameStart = False";
if (mostCurrent._netconn._sendingfile || mostCurrent._netconn._receivingfile) { 
_gamestart = anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 135;BA.debugLine="If searchInProgress Then bt = False";
if (_searchinprogress) { 
_bt = anywheresoftware.b4a.keywords.Common.False;};
 //BA.debugLineNum = 136;BA.debugLine="btnConnectBT.Enabled = bt";
mostCurrent._btnconnectbt.setEnabled(_bt);
 //BA.debugLineNum = 137;BA.debugLine="btnConnectWifi.Enabled = wifi";
mostCurrent._btnconnectwifi.setEnabled(_wifi);
 //BA.debugLineNum = 138;BA.debugLine="btnBTSearch.Enabled = discover";
mostCurrent._btnbtsearch.setEnabled(_discover);
 //BA.debugLineNum = 139;BA.debugLine="btnMakeDiscoverable.Enabled = discover";
mostCurrent._btnmakediscoverable.setEnabled(_discover);
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
}
