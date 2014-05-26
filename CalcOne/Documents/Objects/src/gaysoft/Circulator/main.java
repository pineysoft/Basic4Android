package gaysoft.Circulator;

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
			processBA = new BA(this.getApplicationContext(), null, null, "gaysoft.Circulator", "gaysoft.Circulator.main");
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
		activityBA = new BA(this, layout, processBA, "gaysoft.Circulator", "gaysoft.Circulator.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "gaysoft.Circulator.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static int _leftsidemargin = 0;
public static int _rightsidemargin = 0;
public static int _centerh = 0;
public static int _centerv = 0;
public static float _result = 0f;
public static boolean _btnhit = false;
public static String _currentnum = "";
public static int _fromx = 0;
public static int _fromy = 0;
public static int _tox = 0;
public static int _toy = 0;
public static anywheresoftware.b4a.objects.Timer _timerpress = null;
public static anywheresoftware.b4a.objects.Timer _timerhold = null;
public static int _vibratesetting = 0;
public static int _displayscrollleftpos = 0;
public static int _displayscrollwidth = 0;
public static int _displayscrolltoppos = 0;
public static int _displayscrollheight = 0;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _piccanvas = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _destrect = null;
public anywheresoftware.b4a.objects.collections.List _lbllist = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndivide = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnminus = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmultiply = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnplus = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl8 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl9 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnclear = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldisplay = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldec1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnop = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnundo = null;
public static boolean _hasdecimal = false;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbracket = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper _tbtnmode = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcomplete = null;
public anywheresoftware.b4a.objects.LabelWrapper _lastlabel = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbig = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblviewcalc = null;
public anywheresoftware.b4a.phone.Phone.PhoneVibrate _vib = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblmoving = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldisplaybox = null;
public flm.b4a.animationplus.AnimationPlusWrapper _displayanim = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblanimated = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scvcomplete = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbracketright = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsqroot = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsquared = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnpercent = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnpowerofx = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 85;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 87;BA.debugLine="InitGlobalVariables";
_initglobalvariables();
 //BA.debugLineNum = 90;BA.debugLine="InitViews";
_initviews();
 //BA.debugLineNum = 93;BA.debugLine="FillLabelList";
_filllabellist();
 //BA.debugLineNum = 96;BA.debugLine="SetupTimers";
_setuptimers();
 //BA.debugLineNum = 99;BA.debugLine="SetupAnimations";
_setupanimations();
 //BA.debugLineNum = 101;BA.debugLine="FixUpExtraButtons";
_fixupextrabuttons();
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 203;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 204;BA.debugLine="If timerPress.IsInitialized Then";
if (_timerpress.IsInitialized()) { 
 //BA.debugLineNum = 205;BA.debugLine="timerPress.Enabled = False";
_timerpress.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 207;BA.debugLine="If timerHold.IsInitialized Then";
if (_timerhold.IsInitialized()) { 
 //BA.debugLineNum = 208;BA.debugLine="timerHold.Enabled = False";
_timerhold.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 195;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 196;BA.debugLine="End Sub";
return "";
}
public static String  _animation_animationend() throws Exception{
 //BA.debugLineNum = 198;BA.debugLine="Sub Animation_AnimationEnd";
 //BA.debugLineNum = 199;BA.debugLine="lblAnimated.Text = \"\"";
mostCurrent._lblanimated.setText((Object)(""));
 //BA.debugLineNum = 200;BA.debugLine="lblAnimated.Color = Colors.Transparent";
mostCurrent._lblanimated.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 201;BA.debugLine="End Sub";
return "";
}
public static String  _btnbracket_click() throws Exception{
 //BA.debugLineNum = 664;BA.debugLine="Sub btnBracket_Click";
 //BA.debugLineNum = 665;BA.debugLine="lblComplete.Text = lblComplete.Text & \" \" & btnOp.Text & \" (\"";
mostCurrent._lblcomplete.setText((Object)(mostCurrent._lblcomplete.getText()+" "+mostCurrent._btnop.getText()+" ("));
 //BA.debugLineNum = 666;BA.debugLine="End Sub";
return "";
}
public static String  _btnbracket_longclick() throws Exception{
 //BA.debugLineNum = 667;BA.debugLine="Sub btnBracket_LongClick";
 //BA.debugLineNum = 668;BA.debugLine="lblComplete.Text = lblComplete.Text & \" )\"";
mostCurrent._lblcomplete.setText((Object)(mostCurrent._lblcomplete.getText()+" )"));
 //BA.debugLineNum = 669;BA.debugLine="UpdateExpression";
_updateexpression();
 //BA.debugLineNum = 670;BA.debugLine="End Sub";
return "";
}
public static String  _btnclear_click() throws Exception{
 //BA.debugLineNum = 408;BA.debugLine="Sub btnClear_Click";
 //BA.debugLineNum = 409;BA.debugLine="ClearLines";
_clearlines();
 //BA.debugLineNum = 410;BA.debugLine="lblDisplay.Text = \"\"";
mostCurrent._lbldisplay.setText((Object)(""));
 //BA.debugLineNum = 411;BA.debugLine="lblComplete.Text = \"\"";
mostCurrent._lblcomplete.setText((Object)(""));
 //BA.debugLineNum = 412;BA.debugLine="result = 0";
_result = (float) (0);
 //BA.debugLineNum = 413;BA.debugLine="End Sub";
return "";
}
public static String  _btndivide_click() throws Exception{
 //BA.debugLineNum = 654;BA.debugLine="Sub btnDivide_Click";
 //BA.debugLineNum = 655;BA.debugLine="btnOp.Text = \"÷\"";
mostCurrent._btnop.setText((Object)("÷"));
 //BA.debugLineNum = 656;BA.debugLine="End Sub";
return "";
}
public static String  _btnminus_click() throws Exception{
 //BA.debugLineNum = 648;BA.debugLine="Sub btnMinus_Click";
 //BA.debugLineNum = 649;BA.debugLine="btnOp.Text = \"-\"";
mostCurrent._btnop.setText((Object)("-"));
 //BA.debugLineNum = 650;BA.debugLine="End Sub";
return "";
}
public static String  _btnmultiply_click() throws Exception{
 //BA.debugLineNum = 651;BA.debugLine="Sub btnMultiply_Click";
 //BA.debugLineNum = 652;BA.debugLine="btnOp.Text = \"*\"";
mostCurrent._btnop.setText((Object)("*"));
 //BA.debugLineNum = 653;BA.debugLine="End Sub";
return "";
}
public static String  _btnop_click() throws Exception{
 //BA.debugLineNum = 680;BA.debugLine="Sub btnOp_Click";
 //BA.debugLineNum = 681;BA.debugLine="Select btnOp.Text";
switch (BA.switchObjectToInt(mostCurrent._btnop.getText(),"+","-","*","÷")) {
case 0:
 //BA.debugLineNum = 683;BA.debugLine="btnOp.Text = \"-\"";
mostCurrent._btnop.setText((Object)("-"));
 break;
case 1:
 //BA.debugLineNum = 685;BA.debugLine="btnOp.Text = \"*\"";
mostCurrent._btnop.setText((Object)("*"));
 break;
case 2:
 //BA.debugLineNum = 687;BA.debugLine="btnOp.Text = \"÷\"";
mostCurrent._btnop.setText((Object)("÷"));
 break;
case 3:
 //BA.debugLineNum = 689;BA.debugLine="btnOp.Text = \"+\"";
mostCurrent._btnop.setText((Object)("+"));
 break;
}
;
 //BA.debugLineNum = 691;BA.debugLine="End Sub";
return "";
}
public static String  _btnplus_click() throws Exception{
 //BA.debugLineNum = 657;BA.debugLine="Sub btnPlus_Click";
 //BA.debugLineNum = 658;BA.debugLine="btnOp.Text = \"+\"";
mostCurrent._btnop.setText((Object)("+"));
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _btnundo_click() throws Exception{
 //BA.debugLineNum = 660;BA.debugLine="Sub btnUndo_Click";
 //BA.debugLineNum = 661;BA.debugLine="CutLastNumber";
_cutlastnumber();
 //BA.debugLineNum = 662;BA.debugLine="UpdateCompleteLabel(lblComplete.Text)";
_updatecompletelabel(mostCurrent._lblcomplete.getText());
 //BA.debugLineNum = 663;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.LabelWrapper  _checklabels(int _x,int _y) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _retlabel = null;
int _i = 0;
 //BA.debugLineNum = 232;BA.debugLine="Sub CheckLabels(x As Int, y As Int) As Label";
 //BA.debugLineNum = 233;BA.debugLine="Dim retLabel As Label";
_retlabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 234;BA.debugLine="For I = 0 To lblList.Size - 1";
{
final int step163 = 1;
final int limit163 = (int) (mostCurrent._lbllist.getSize()-1);
for (_i = (int) (0); (step163 > 0 && _i <= limit163) || (step163 < 0 && _i >= limit163); _i = ((int)(0 + _i + step163))) {
 //BA.debugLineNum = 236;BA.debugLine="retLabel = GetALabel(x,y,lblList.Get(I))";
_retlabel = _getalabel(_x,_y,(anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(mostCurrent._lbllist.Get(_i))));
 //BA.debugLineNum = 237;BA.debugLine="If retLabel.IsInitialized Then";
if (_retlabel.IsInitialized()) { 
 //BA.debugLineNum = 238;BA.debugLine="I = lblList.Size - 1";
_i = (int) (mostCurrent._lbllist.getSize()-1);
 };
 }
};
 //BA.debugLineNum = 242;BA.debugLine="If retLabel.IsInitialized AND retLabel.Text = \".\" AND lblDisplay.Text.Contains(\".\") Then";
if (_retlabel.IsInitialized() && (_retlabel.getText()).equals(".") && mostCurrent._lbldisplay.getText().contains(".")) { 
 //BA.debugLineNum = 243;BA.debugLine="retLabel = Null";
_retlabel.setObject((android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 245;BA.debugLine="Return retLabel";
if (true) return _retlabel;
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return null;
}
public static String  _clearlines() throws Exception{
 //BA.debugLineNum = 644;BA.debugLine="Sub ClearLines";
 //BA.debugLineNum = 645;BA.debugLine="picCanvas.DrawRect(DestRect, Colors.Transparent, True, 1dip)";
mostCurrent._piccanvas.DrawRect((android.graphics.Rect)(mostCurrent._destrect.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 646;BA.debugLine="Panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 647;BA.debugLine="End Sub";
return "";
}
public static String  _cutlastnumber() throws Exception{
int _foundat = 0;
String _before = "";
int _iloop = 0;
anywheresoftware.b4a.agraham.expressionevaluator.Evaluator _exp = null;
int _numi = 0;
double _numd = 0;
 //BA.debugLineNum = 538;BA.debugLine="Public Sub CutLastNumber";
 //BA.debugLineNum = 539;BA.debugLine="Dim foundAt As Int = 0";
_foundat = (int) (0);
 //BA.debugLineNum = 540;BA.debugLine="Dim before As String";
_before = "";
 //BA.debugLineNum = 541;BA.debugLine="For iLoop = lblComplete.Text.Length - 1 To 0 Step -1";
{
final int step425 = (int) (-1);
final int limit425 = (int) (0);
for (_iloop = (int) (mostCurrent._lblcomplete.getText().length()-1); (step425 > 0 && _iloop <= limit425) || (step425 < 0 && _iloop >= limit425); _iloop = ((int)(0 + _iloop + step425))) {
 //BA.debugLineNum = 542;BA.debugLine="If lblComplete.Text.CharAt(iLoop) = \" \" Then";
if (mostCurrent._lblcomplete.getText().charAt(_iloop)==BA.ObjectToChar(" ")) { 
 //BA.debugLineNum = 543;BA.debugLine="foundAt = iLoop";
_foundat = _iloop;
 //BA.debugLineNum = 544;BA.debugLine="iLoop = 0";
_iloop = (int) (0);
 };
 }
};
 //BA.debugLineNum = 548;BA.debugLine="If foundAt > 0 Then";
if (_foundat>0) { 
 //BA.debugLineNum = 549;BA.debugLine="before = lblComplete.Text.SubString2(0,foundAt - 2)";
_before = mostCurrent._lblcomplete.getText().substring((int) (0),(int) (_foundat-2));
 //BA.debugLineNum = 550;BA.debugLine="lblComplete.Text = before";
mostCurrent._lblcomplete.setText((Object)(_before));
 };
 //BA.debugLineNum = 553;BA.debugLine="Dim exp As Evaluator";
_exp = new anywheresoftware.b4a.agraham.expressionevaluator.Evaluator();
 //BA.debugLineNum = 554;BA.debugLine="exp.Initialize";
_exp.Initialize();
 //BA.debugLineNum = 555;BA.debugLine="result = exp.Evaluate(lblComplete.Text)";
_result = (float) (_exp.Evaluate(mostCurrent._lblcomplete.getText()));
 //BA.debugLineNum = 557;BA.debugLine="Dim numI As Int = result";
_numi = (int) (_result);
 //BA.debugLineNum = 558;BA.debugLine="Dim numD As Double";
_numd = 0;
 //BA.debugLineNum = 559;BA.debugLine="numD = numI";
_numd = _numi;
 //BA.debugLineNum = 561;BA.debugLine="If numD <> result Then";
if (_numd!=_result) { 
 //BA.debugLineNum = 562;BA.debugLine="lblDisplay.Text = result";
mostCurrent._lbldisplay.setText((Object)(_result));
 }else {
 //BA.debugLineNum = 564;BA.debugLine="lblDisplay.Text = numI";
mostCurrent._lbldisplay.setText((Object)(_numi));
 };
 //BA.debugLineNum = 566;BA.debugLine="End Sub";
return "";
}
public static String  _deleteviewbytag(anywheresoftware.b4a.objects.ActivityWrapper _act,String _tag,boolean _backwards) throws Exception{
int _loopvar = 0;
anywheresoftware.b4a.objects.ConcreteViewWrapper _currentview = null;
 //BA.debugLineNum = 415;BA.debugLine="Sub DeleteViewByTag(act As Activity, tag As String, backwards As Boolean)";
 //BA.debugLineNum = 416;BA.debugLine="Dim loopVar As Int";
_loopvar = 0;
 //BA.debugLineNum = 417;BA.debugLine="Dim currentView As View";
_currentview = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 418;BA.debugLine="If backwards Then";
if (_backwards) { 
 //BA.debugLineNum = 419;BA.debugLine="For loopVar = act.NumberOfViews - 1 To 0 Step -1";
{
final int step322 = (int) (-1);
final int limit322 = (int) (0);
for (_loopvar = (int) (_act.getNumberOfViews()-1); (step322 > 0 && _loopvar <= limit322) || (step322 < 0 && _loopvar >= limit322); _loopvar = ((int)(0 + _loopvar + step322))) {
 //BA.debugLineNum = 420;BA.debugLine="currentView = act.GetView(loopVar)";
_currentview = _act.GetView(_loopvar);
 //BA.debugLineNum = 421;BA.debugLine="If currentView.tag = tag Then";
if ((_currentview.getTag()).equals((Object)(_tag))) { 
 //BA.debugLineNum = 422;BA.debugLine="act.RemoveViewAt(loopVar)";
_act.RemoveViewAt(_loopvar);
 };
 }
};
 }else {
 //BA.debugLineNum = 426;BA.debugLine="For loopVar = 0 To act.NumberOfViews - 1";
{
final int step329 = 1;
final int limit329 = (int) (_act.getNumberOfViews()-1);
for (_loopvar = (int) (0); (step329 > 0 && _loopvar <= limit329) || (step329 < 0 && _loopvar >= limit329); _loopvar = ((int)(0 + _loopvar + step329))) {
 //BA.debugLineNum = 427;BA.debugLine="currentView = act.GetView(loopVar)";
_currentview = _act.GetView(_loopvar);
 //BA.debugLineNum = 428;BA.debugLine="If currentView.tag = tag Then";
if ((_currentview.getTag()).equals((Object)(_tag))) { 
 //BA.debugLineNum = 429;BA.debugLine="act.RemoveViewAt(loopVar)";
_act.RemoveViewAt(_loopvar);
 };
 }
};
 };
 //BA.debugLineNum = 433;BA.debugLine="End Sub";
return "";
}
public static String  _filllabellist() throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub FillLabelList()";
 //BA.debugLineNum = 217;BA.debugLine="lblList.Initialize";
mostCurrent._lbllist.Initialize();
 //BA.debugLineNum = 218;BA.debugLine="lblList.Clear";
mostCurrent._lbllist.Clear();
 //BA.debugLineNum = 219;BA.debugLine="lblList.Add(lbl1)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl1.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="lblList.Add(lbl2)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl2.getObject()));
 //BA.debugLineNum = 221;BA.debugLine="lblList.Add(lbl3)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl3.getObject()));
 //BA.debugLineNum = 222;BA.debugLine="lblList.Add(lbl4)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl4.getObject()));
 //BA.debugLineNum = 223;BA.debugLine="lblList.Add(lbl5)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl5.getObject()));
 //BA.debugLineNum = 224;BA.debugLine="lblList.Add(lbl6)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl6.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="lblList.Add(lbl7)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl7.getObject()));
 //BA.debugLineNum = 226;BA.debugLine="lblList.Add(lbl8)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl8.getObject()));
 //BA.debugLineNum = 227;BA.debugLine="lblList.Add(lbl9)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl9.getObject()));
 //BA.debugLineNum = 228;BA.debugLine="lblList.Add(lbl0)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbl0.getObject()));
 //BA.debugLineNum = 229;BA.debugLine="lblList.Add(lblDec1)";
mostCurrent._lbllist.Add((Object)(mostCurrent._lbldec1.getObject()));
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public static int  _findlabelxpos(String _value) throws Exception{
int _iloop = 0;
anywheresoftware.b4a.objects.LabelWrapper _templabel = null;
 //BA.debugLineNum = 288;BA.debugLine="Sub FindLabelXpos(value As String) As Int";
 //BA.debugLineNum = 289;BA.debugLine="Dim iloop As Int";
_iloop = 0;
 //BA.debugLineNum = 290;BA.debugLine="Dim tempLabel As Label";
_templabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 291;BA.debugLine="For iloop = 0 To lblList.Size - 1";
{
final int step205 = 1;
final int limit205 = (int) (mostCurrent._lbllist.getSize()-1);
for (_iloop = (int) (0); (step205 > 0 && _iloop <= limit205) || (step205 < 0 && _iloop >= limit205); _iloop = ((int)(0 + _iloop + step205))) {
 //BA.debugLineNum = 292;BA.debugLine="tempLabel = lblList.Get(iloop)";
_templabel.setObject((android.widget.TextView)(mostCurrent._lbllist.Get(_iloop)));
 //BA.debugLineNum = 293;BA.debugLine="If tempLabel.Text = value Then";
if ((_templabel.getText()).equals(_value)) { 
 //BA.debugLineNum = 294;BA.debugLine="Return tempLabel.Left";
if (true) return _templabel.getLeft();
 };
 }
};
 //BA.debugLineNum = 298;BA.debugLine="Return 0";
if (true) return (int) (0);
 //BA.debugLineNum = 299;BA.debugLine="End Sub";
return 0;
}
public static String  _fixupextrabuttons() throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 176;BA.debugLine="Sub FixUpExtraButtons";
 //BA.debugLineNum = 177;BA.debugLine="Dim jo As JavaObject = btnBracket";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._btnbracket.getObject()));
 //BA.debugLineNum = 178;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 180;BA.debugLine="jo = btnBracketRight";
_jo.setObject((java.lang.Object)(mostCurrent._btnbracketright.getObject()));
 //BA.debugLineNum = 181;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 183;BA.debugLine="jo = btnSqRoot";
_jo.setObject((java.lang.Object)(mostCurrent._btnsqroot.getObject()));
 //BA.debugLineNum = 184;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 186;BA.debugLine="jo = btnSquared";
_jo.setObject((java.lang.Object)(mostCurrent._btnsquared.getObject()));
 //BA.debugLineNum = 187;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 189;BA.debugLine="jo = btnPercent";
_jo.setObject((java.lang.Object)(mostCurrent._btnpercent.getObject()));
 //BA.debugLineNum = 190;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 192;BA.debugLine="jo = btnPowerOfx";
_jo.setObject((java.lang.Object)(mostCurrent._btnpowerofx.getObject()));
 //BA.debugLineNum = 193;BA.debugLine="jo.RunMethod(\"setPadding\", Array As Object(0, 0, 0, 0))";
_jo.RunMethod("setPadding",new Object[]{(Object)(0),(Object)(0),(Object)(0),(Object)(0)});
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.LabelWrapper  _getalabel(int _x,int _y,anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
 //BA.debugLineNum = 248;BA.debugLine="Sub GetALabel(x As Int, y As Int, lbl As Label) As Label";
 //BA.debugLineNum = 250;BA.debugLine="x = x + Panel1.Left";
_x = (int) (_x+mostCurrent._panel1.getLeft());
 //BA.debugLineNum = 251;BA.debugLine="y = y + Panel1.top";
_y = (int) (_y+mostCurrent._panel1.getTop());
 //BA.debugLineNum = 254;BA.debugLine="If x < centerH AND y  < centerV Then";
if (_x<_centerh && _y<_centerv) { 
 //BA.debugLineNum = 255;BA.debugLine="If IsIn(x, lbl.Left, lbl.Left + lbl.width + leftSideMargin) AND IsIn(y, lbl.Top - 15, lbl.Top + lbl.Height + 15) Then";
if (_isin(_x,_lbl.getLeft(),(int) (_lbl.getLeft()+_lbl.getWidth()+_leftsidemargin)) && _isin(_y,(int) (_lbl.getTop()-15),(int) (_lbl.getTop()+_lbl.getHeight()+15))) { 
 //BA.debugLineNum = 256;BA.debugLine="Return lbl";
if (true) return _lbl;
 };
 };
 //BA.debugLineNum = 261;BA.debugLine="If x > centerH AND y < centerV Then";
if (_x>_centerh && _y<_centerv) { 
 //BA.debugLineNum = 262;BA.debugLine="If IsIn(x, lbl.Left - rightSideMargin, lbl.Left + lbl.width) AND IsIn(y, lbl.Top - 15, lbl.Top + lbl.Height + 15)  Then";
if (_isin(_x,(int) (_lbl.getLeft()-_rightsidemargin),(int) (_lbl.getLeft()+_lbl.getWidth())) && _isin(_y,(int) (_lbl.getTop()-15),(int) (_lbl.getTop()+_lbl.getHeight()+15))) { 
 //BA.debugLineNum = 263;BA.debugLine="Return lbl";
if (true) return _lbl;
 };
 };
 //BA.debugLineNum = 268;BA.debugLine="If x < centerH AND y > centerV Then";
if (_x<_centerh && _y>_centerv) { 
 //BA.debugLineNum = 269;BA.debugLine="If IsIn(x, lbl.Left, lbl.Left + lbl.width + leftSideMargin) AND IsIn(y, lbl.Top - 15, lbl.Top + lbl.Height + 15) Then";
if (_isin(_x,_lbl.getLeft(),(int) (_lbl.getLeft()+_lbl.getWidth()+_leftsidemargin)) && _isin(_y,(int) (_lbl.getTop()-15),(int) (_lbl.getTop()+_lbl.getHeight()+15))) { 
 //BA.debugLineNum = 270;BA.debugLine="Return lbl";
if (true) return _lbl;
 };
 };
 //BA.debugLineNum = 275;BA.debugLine="If x > centerH AND y > centerV Then";
if (_x>_centerh && _y>_centerv) { 
 //BA.debugLineNum = 276;BA.debugLine="If IsIn(x, lbl.Left - rightSideMargin, lbl.Left + lbl.width) AND IsIn(y, lbl.Top - 15, lbl.Top + lbl.Height + 15) Then";
if (_isin(_x,(int) (_lbl.getLeft()-_rightsidemargin),(int) (_lbl.getLeft()+_lbl.getWidth())) && _isin(_y,(int) (_lbl.getTop()-15),(int) (_lbl.getTop()+_lbl.getHeight()+15))) { 
 //BA.debugLineNum = 277;BA.debugLine="Return lbl";
if (true) return _lbl;
 };
 };
 //BA.debugLineNum = 281;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 282;BA.debugLine="End Sub";
return null;
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 39;BA.debugLine="Dim Obj1 As Reflector";
mostCurrent._obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 40;BA.debugLine="Dim picCanvas As Canvas";
mostCurrent._piccanvas = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim DestRect As Rect";
mostCurrent._destrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim lblList As List";
mostCurrent._lbllist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 43;BA.debugLine="Private btnDivide As Button";
mostCurrent._btndivide = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private btnMinus As Button";
mostCurrent._btnminus = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private btnMultiply As Button";
mostCurrent._btnmultiply = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private btnPlus As Button";
mostCurrent._btnplus = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private lbl0 As Label";
mostCurrent._lbl0 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lbl1 As Label";
mostCurrent._lbl1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private lbl2 As Label";
mostCurrent._lbl2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private lbl3 As Label";
mostCurrent._lbl3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lbl4 As Label";
mostCurrent._lbl4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lbl5 As Label";
mostCurrent._lbl5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private lbl6 As Label";
mostCurrent._lbl6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private lbl7 As Label";
mostCurrent._lbl7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private lbl8 As Label";
mostCurrent._lbl8 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private lbl9 As Label";
mostCurrent._lbl9 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private btnClear As Button";
mostCurrent._btnclear = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private lblDisplay As Label";
mostCurrent._lbldisplay = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private lblDec1 As Label";
mostCurrent._lbldec1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private btnOp As Button";
mostCurrent._btnop = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private btnUndo As Button";
mostCurrent._btnundo = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private hasDecimal As Boolean";
_hasdecimal = false;
 //BA.debugLineNum = 64;BA.debugLine="Private btnBracket As Button";
mostCurrent._btnbracket = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private tbtnMode As ToggleButton";
mostCurrent._tbtnmode = new anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private lblComplete As Label";
mostCurrent._lblcomplete = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Dim lastLabel As Label";
mostCurrent._lastlabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Dim lblBig As Label";
mostCurrent._lblbig = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim lblViewCalc As Label";
mostCurrent._lblviewcalc = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private vib As PhoneVibrate";
mostCurrent._vib = new anywheresoftware.b4a.phone.Phone.PhoneVibrate();
 //BA.debugLineNum = 72;BA.debugLine="Private lblMoving As Label";
mostCurrent._lblmoving = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private lblDisplayBox As Label";
mostCurrent._lbldisplaybox = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private displayAnim As AnimationPlus";
mostCurrent._displayanim = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private lblAnimated As Label";
mostCurrent._lblanimated = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private scvComplete As HorizontalScrollView";
mostCurrent._scvcomplete = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private btnBracketRight As Button";
mostCurrent._btnbracketright = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private btnSqRoot As Button";
mostCurrent._btnsqroot = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private btnSquared As Button";
mostCurrent._btnsquared = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private btnPercent As Button";
mostCurrent._btnpercent = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private btnPowerOfx As Button";
mostCurrent._btnpowerofx = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
public static String  _initglobalvariables() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub InitGlobalVariables";
 //BA.debugLineNum = 106;BA.debugLine="centerH = Activity.Width/2";
_centerh = (int) (mostCurrent._activity.getWidth()/(double)2);
 //BA.debugLineNum = 107;BA.debugLine="centerV = Activity.Height/2";
_centerv = (int) (mostCurrent._activity.getHeight()/(double)2);
 //BA.debugLineNum = 109;BA.debugLine="lblBig.Initialize(\"\")";
mostCurrent._lblbig.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 110;BA.debugLine="lblBig.Tag = \"BigLabel\"";
mostCurrent._lblbig.setTag((Object)("BigLabel"));
 //BA.debugLineNum = 111;BA.debugLine="lblBig.Color = Colors.Transparent";
mostCurrent._lblbig.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 113;BA.debugLine="displayScrollLeftPos = 5dip";
_displayscrollleftpos = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5));
 //BA.debugLineNum = 114;BA.debugLine="displayScrollWidth = 100%x - 15dip";
_displayscrollwidth = (int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (15)));
 //BA.debugLineNum = 115;BA.debugLine="displayScrollTopPos = 5dip";
_displayscrolltoppos = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5));
 //BA.debugLineNum = 116;BA.debugLine="displayScrollHeight = 55dip";
_displayscrollheight = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55));
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _initviews() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub InitViews";
 //BA.debugLineNum = 133;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
mostCurrent._activity.LoadLayout("Layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 135;BA.debugLine="picCanvas.Initialize(Panel1)";
mostCurrent._piccanvas.Initialize((android.view.View)(mostCurrent._panel1.getObject()));
 //BA.debugLineNum = 136;BA.debugLine="DestRect.Initialize(0dip, 0dip, 100%x, 100%y)";
mostCurrent._destrect.Initialize(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 137;BA.debugLine="picCanvas.DrawRect(DestRect, Colors.Transparent, True, 1dip)";
mostCurrent._piccanvas.DrawRect((android.graphics.Rect)(mostCurrent._destrect.getObject()),anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 138;BA.debugLine="Panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 140;BA.debugLine="Obj1.Target = Panel1";
mostCurrent._obj1.Target = (Object)(mostCurrent._panel1.getObject());
 //BA.debugLineNum = 141;BA.debugLine="Obj1.SetOnTouchListener(\"Panel1_OnTouch\")";
mostCurrent._obj1.SetOnTouchListener(processBA,"Panel1_OnTouch");
 //BA.debugLineNum = 143;BA.debugLine="Activity.AddView(lblBig,0,0,0,0)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblbig.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 145;BA.debugLine="lblMoving.Width = -2";
mostCurrent._lblmoving.setWidth((int) (-2));
 //BA.debugLineNum = 148;BA.debugLine="scvComplete.Initialize(100,\"\")";
mostCurrent._scvcomplete.Initialize(mostCurrent.activityBA,(int) (100),"");
 //BA.debugLineNum = 149;BA.debugLine="scvComplete.Color = Colors.ARGB(255,142,180,152)";
mostCurrent._scvcomplete.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (142),(int) (180),(int) (152)));
 //BA.debugLineNum = 150;BA.debugLine="Activity.AddView(scvComplete, displayScrollLeftPos, displayScrollTopPos, displayScrollWidth, displayScrollHeight)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scvcomplete.getObject()),_displayscrollleftpos,_displayscrolltoppos,_displayscrollwidth,_displayscrollheight);
 //BA.debugLineNum = 153;BA.debugLine="lblComplete.Initialize(\"lblComplete\")";
mostCurrent._lblcomplete.Initialize(mostCurrent.activityBA,"lblComplete");
 //BA.debugLineNum = 155;BA.debugLine="lblComplete.TextColor = Colors.Black";
mostCurrent._lblcomplete.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 156;BA.debugLine="lblComplete.Color = Colors.Transparent";
mostCurrent._lblcomplete.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 157;BA.debugLine="lblComplete.TextSize = 30";
mostCurrent._lblcomplete.setTextSize((float) (30));
 //BA.debugLineNum = 160;BA.debugLine="Activity.AddView(lblComplete,0,0,0,0)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblcomplete.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 161;BA.debugLine="lblComplete.Width = picCanvas.MeasureStringWidth(lblComplete.Text, lblComplete.Typeface, lblComplete.TextSize) + 20dip";
mostCurrent._lblcomplete.setWidth((int) (mostCurrent._piccanvas.MeasureStringWidth(mostCurrent._lblcomplete.getText(),mostCurrent._lblcomplete.getTypeface(),mostCurrent._lblcomplete.getTextSize())+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))));
 //BA.debugLineNum = 162;BA.debugLine="RemoveMultiLine(lblComplete)";
_removemultiline(mostCurrent._lblcomplete);
 //BA.debugLineNum = 164;BA.debugLine="lblComplete.RemoveView";
mostCurrent._lblcomplete.RemoveView();
 //BA.debugLineNum = 167;BA.debugLine="scvComplete.Panel.AddView(lblComplete,displayScrollWidth - lblComplete.Width,displayScrollTopPos,displayScrollWidth,displayScrollHeight)";
mostCurrent._scvcomplete.getPanel().AddView((android.view.View)(mostCurrent._lblcomplete.getObject()),(int) (_displayscrollwidth-mostCurrent._lblcomplete.getWidth()),_displayscrolltoppos,_displayscrollwidth,_displayscrollheight);
 //BA.debugLineNum = 168;BA.debugLine="scvComplete.Panel.Width = lblComplete.Width";
mostCurrent._scvcomplete.getPanel().setWidth(mostCurrent._lblcomplete.getWidth());
 //BA.debugLineNum = 170;BA.debugLine="lblDisplayBox.Initialize(\"\")";
mostCurrent._lbldisplaybox.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 171;BA.debugLine="lblDisplayBox.SetBackgroundImage(LoadBitmap(File.DirAssets, \"displayPanel.png\"))";
mostCurrent._lbldisplaybox.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"displayPanel.png").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="Activity.AddView(lblDisplayBox,0,0,100%x,60dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lbldisplaybox.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static boolean  _isin(int _number,int _numstart,int _numend) throws Exception{
 //BA.debugLineNum = 284;BA.debugLine="Sub IsIn(number As Int,  numStart As Int, numEnd As Int) As Boolean";
 //BA.debugLineNum = 285;BA.debugLine="Return number >= numStart AND number <= numEnd";
if (true) return _number>=_numstart && _number<=_numend;
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return false;
}
public static String  _lblcomplete_click() throws Exception{
 //BA.debugLineNum = 704;BA.debugLine="Sub lblComplete_Click";
 //BA.debugLineNum = 705;BA.debugLine="lblViewCalc.Initialize(\"lblViewCalc\")";
mostCurrent._lblviewcalc.Initialize(mostCurrent.activityBA,"lblViewCalc");
 //BA.debugLineNum = 706;BA.debugLine="lblViewCalc.Color = Colors.ARGB(200,255,255,255)";
mostCurrent._lblviewcalc.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 707;BA.debugLine="lblViewCalc.TextColor = Colors.Black";
mostCurrent._lblviewcalc.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 708;BA.debugLine="lblViewCalc.Text = lblComplete.Text";
mostCurrent._lblviewcalc.setText((Object)(mostCurrent._lblcomplete.getText()));
 //BA.debugLineNum = 709;BA.debugLine="lblViewCalc.TextSize = 30";
mostCurrent._lblviewcalc.setTextSize((float) (30));
 //BA.debugLineNum = 710;BA.debugLine="Activity.AddView(lblViewCalc, 0,0,100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblviewcalc.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 711;BA.debugLine="End Sub";
return "";
}
public static String  _lblviewcalc_click() throws Exception{
 //BA.debugLineNum = 713;BA.debugLine="Sub lblViewCalc_Click";
 //BA.debugLineNum = 714;BA.debugLine="lblViewCalc.RemoveView";
mostCurrent._lblviewcalc.RemoveView();
 //BA.debugLineNum = 715;BA.debugLine="End Sub";
return "";
}
public static boolean  _panel1_ontouch(Object _viewtag,int _action,float _x,float _y,Object _motionevent) throws Exception{
String _number = "";
anywheresoftware.b4a.objects.LabelWrapper _currentlabel = null;
 //BA.debugLineNum = 300;BA.debugLine="Sub Panel1_OnTouch(viewtag As Object, action As Int, X As Float, Y As Float, motionevent As Object) As Boolean";
 //BA.debugLineNum = 301;BA.debugLine="Dim number As String = \"x\"";
_number = "x";
 //BA.debugLineNum = 302;BA.debugLine="Dim currentLabel As Label";
_currentlabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 303;BA.debugLine="currentLabel.Initialize(\"\")";
_currentlabel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 304;BA.debugLine="Select action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP,mostCurrent._activity.ACTION_MOVE)) {
case 0:
 //BA.debugLineNum = 306;BA.debugLine="displayAnim.Stop(lblAnimated)";
mostCurrent._displayanim.Stop((android.view.View)(mostCurrent._lblanimated.getObject()));
 //BA.debugLineNum = 308;BA.debugLine="btnHit = False";
_btnhit = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 309;BA.debugLine="lblDisplay.Text = \"\"";
mostCurrent._lbldisplay.setText((Object)(""));
 //BA.debugLineNum = 310;BA.debugLine="fromx = X";
_fromx = (int) (_x);
 //BA.debugLineNum = 311;BA.debugLine="fromy = Y";
_fromy = (int) (_y);
 //BA.debugLineNum = 312;BA.debugLine="picCanvas.DrawCircle(fromx, fromy, 4dip, Colors.Red, True, 1dip)";
mostCurrent._piccanvas.DrawCircle((float) (_fromx),(float) (_fromy),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 313;BA.debugLine="currentLabel = CheckLabels(X,Y)";
_currentlabel = _checklabels((int) (_x),(int) (_y));
 //BA.debugLineNum = 314;BA.debugLine="If currentLabel.IsInitialized Then";
if (_currentlabel.IsInitialized()) { 
 //BA.debugLineNum = 315;BA.debugLine="vib.vibrate(vibrateSetting)";
mostCurrent._vib.Vibrate(processBA,(long) (_vibratesetting));
 //BA.debugLineNum = 316;BA.debugLine="lblMoving.Color = Colors.ARGB(180,255,255,255)";
mostCurrent._lblmoving.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (180),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 317;BA.debugLine="lblMoving.Left = Panel1.Left + X - 80";
mostCurrent._lblmoving.setLeft((int) (mostCurrent._panel1.getLeft()+_x-80));
 //BA.debugLineNum = 318;BA.debugLine="lblMoving.Top = Panel1.Top + Y - 210";
mostCurrent._lblmoving.setTop((int) (mostCurrent._panel1.getTop()+_y-210));
 //BA.debugLineNum = 319;BA.debugLine="number = currentLabel.Text";
_number = _currentlabel.getText();
 //BA.debugLineNum = 320;BA.debugLine="UpdateLabel(X, number, currentLabel)";
_updatelabel((int) (_x),_number,_currentlabel);
 };
 break;
case 1:
 //BA.debugLineNum = 324;BA.debugLine="timerPress.Enabled = False";
_timerpress.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 325;BA.debugLine="timerHold.Enabled = False";
_timerhold.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 326;BA.debugLine="lblBig.Color = Colors.Transparent";
mostCurrent._lblbig.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 329;BA.debugLine="tox = X";
_tox = (int) (_x);
 //BA.debugLineNum = 330;BA.debugLine="toy = Y";
_toy = (int) (_y);
 //BA.debugLineNum = 331;BA.debugLine="picCanvas.DrawCircle(tox, toy, 4dip, Colors.Red, True, 1dip)";
mostCurrent._piccanvas.DrawCircle((float) (_tox),(float) (_toy),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 332;BA.debugLine="UpdateDisplays";
_updatedisplays();
 //BA.debugLineNum = 333;BA.debugLine="hasDecimal = False";
_hasdecimal = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 334;BA.debugLine="lblAnimated.Text = lblMoving.Text";
mostCurrent._lblanimated.setText((Object)(mostCurrent._lblmoving.getText()));
 //BA.debugLineNum = 335;BA.debugLine="lblMoving.Text = \"\"";
mostCurrent._lblmoving.setText((Object)(""));
 //BA.debugLineNum = 336;BA.debugLine="lblMoving.Color = Colors.Transparent";
mostCurrent._lblmoving.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 337;BA.debugLine="displayAnim.Start(lblAnimated)";
mostCurrent._displayanim.Start((android.view.View)(mostCurrent._lblanimated.getObject()));
 break;
case 2:
 //BA.debugLineNum = 340;BA.debugLine="tox = X";
_tox = (int) (_x);
 //BA.debugLineNum = 341;BA.debugLine="toy = Y";
_toy = (int) (_y);
 //BA.debugLineNum = 342;BA.debugLine="picCanvas.DrawCircle(fromx, fromy, 4dip, Colors.Red, True, 1dip)";
mostCurrent._piccanvas.DrawCircle((float) (_fromx),(float) (_fromy),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 343;BA.debugLine="picCanvas.DrawLine(fromx,fromy,tox,toy,Colors.Red,8dip)";
mostCurrent._piccanvas.DrawLine((float) (_fromx),(float) (_fromy),(float) (_tox),(float) (_toy),anywheresoftware.b4a.keywords.Common.Colors.Red,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 344;BA.debugLine="picCanvas.DrawCircle(tox, toy, 4dip, Colors.Red, True, 1dip)";
mostCurrent._piccanvas.DrawCircle((float) (_tox),(float) (_toy),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (4))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 345;BA.debugLine="fromx = tox";
_fromx = _tox;
 //BA.debugLineNum = 346;BA.debugLine="fromy = toy";
_fromy = _toy;
 //BA.debugLineNum = 347;BA.debugLine="currentLabel = CheckLabels(X,Y)";
_currentlabel = _checklabels((int) (_x),(int) (_y));
 //BA.debugLineNum = 348;BA.debugLine="If currentLabel.IsInitialized Then";
if (_currentlabel.IsInitialized()) { 
 //BA.debugLineNum = 349;BA.debugLine="number = currentLabel.Text";
_number = _currentlabel.getText();
 //BA.debugLineNum = 350;BA.debugLine="timerHold.Enabled = False";
_timerhold.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 351;BA.debugLine="timerPress.Enabled = True";
_timerpress.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 353;BA.debugLine="number = \"x\"";
_number = "x";
 //BA.debugLineNum = 354;BA.debugLine="timerPress.Enabled = False";
_timerpress.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 355;BA.debugLine="lblBig.Color = Colors.Transparent";
mostCurrent._lblbig.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 356;BA.debugLine="currentNum = \"x\"";
_currentnum = "x";
 //BA.debugLineNum = 357;BA.debugLine="lblMoving.Left = Panel1.Left + X - 80";
mostCurrent._lblmoving.setLeft((int) (mostCurrent._panel1.getLeft()+_x-80));
 //BA.debugLineNum = 358;BA.debugLine="lblMoving.Top = Panel1.Top + Y - 210";
mostCurrent._lblmoving.setTop((int) (mostCurrent._panel1.getTop()+_y-210));
 //BA.debugLineNum = 359;BA.debugLine="UpdateMargins";
_updatemargins();
 };
 //BA.debugLineNum = 361;BA.debugLine="If currentLabel.IsInitialized AND number <> currentNum Then";
if (_currentlabel.IsInitialized() && (_number).equals(_currentnum) == false) { 
 //BA.debugLineNum = 362;BA.debugLine="vib.vibrate(vibrateSetting)";
mostCurrent._vib.Vibrate(processBA,(long) (_vibratesetting));
 //BA.debugLineNum = 363;BA.debugLine="UpdateLabel(X,number, currentLabel)";
_updatelabel((int) (_x),_number,_currentlabel);
 };
 break;
}
;
 //BA.debugLineNum = 366;BA.debugLine="Panel1.Invalidate";
mostCurrent._panel1.Invalidate();
 //BA.debugLineNum = 367;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
return false;
}
public static String  _performcalculation() throws Exception{
anywheresoftware.b4a.agraham.expressionevaluator.Evaluator _exp = null;
String _calculation = "";
float _num = 0f;
int _numi = 0;
float _numf = 0f;
 //BA.debugLineNum = 488;BA.debugLine="Sub PerformCalculation";
 //BA.debugLineNum = 489;BA.debugLine="If tbtnMode.Checked Then";
if (mostCurrent._tbtnmode.getChecked()) { 
 //BA.debugLineNum = 490;BA.debugLine="Dim exp As Evaluator";
_exp = new anywheresoftware.b4a.agraham.expressionevaluator.Evaluator();
 //BA.debugLineNum = 491;BA.debugLine="Dim calculation As String = lblComplete.Text";
_calculation = mostCurrent._lblcomplete.getText();
 //BA.debugLineNum = 492;BA.debugLine="calculation = calculation.Replace(\"÷\",\"/\")";
_calculation = _calculation.replace("÷","/");
 //BA.debugLineNum = 493;BA.debugLine="exp.Initialize";
_exp.Initialize();
 //BA.debugLineNum = 495;BA.debugLine="result = exp.Evaluate(calculation)";
_result = (float) (_exp.Evaluate(_calculation));
 }else {
 //BA.debugLineNum = 497;BA.debugLine="Dim num As Float = lblDisplay.Text";
_num = (float)(Double.parseDouble(mostCurrent._lbldisplay.getText()));
 //BA.debugLineNum = 499;BA.debugLine="Select btnOp.Text";
switch (BA.switchObjectToInt(mostCurrent._btnop.getText(),"+","-","*","÷")) {
case 0:
 //BA.debugLineNum = 501;BA.debugLine="result = result + num";
_result = (float) (_result+_num);
 break;
case 1:
 //BA.debugLineNum = 503;BA.debugLine="result = result - num";
_result = (float) (_result-_num);
 break;
case 2:
 //BA.debugLineNum = 505;BA.debugLine="If result = 0 Then";
if (_result==0) { 
 //BA.debugLineNum = 506;BA.debugLine="result = num * 1";
_result = (float) (_num*1);
 }else {
 //BA.debugLineNum = 508;BA.debugLine="result = result * num";
_result = (float) (_result*_num);
 };
 break;
case 3:
 //BA.debugLineNum = 511;BA.debugLine="If result = 0 Then";
if (_result==0) { 
 //BA.debugLineNum = 512;BA.debugLine="result = num / 1";
_result = (float) (_num/(double)1);
 }else {
 //BA.debugLineNum = 514;BA.debugLine="result = result / num";
_result = (float) (_result/(double)_num);
 };
 break;
}
;
 };
 //BA.debugLineNum = 519;BA.debugLine="Dim numI As Int = result";
_numi = (int) (_result);
 //BA.debugLineNum = 520;BA.debugLine="Dim numF As Float";
_numf = 0f;
 //BA.debugLineNum = 521;BA.debugLine="numF = numI";
_numf = (float) (_numi);
 //BA.debugLineNum = 523;BA.debugLine="If numF <> result Then";
if (_numf!=_result) { 
 //BA.debugLineNum = 524;BA.debugLine="lblDisplay.Text = result";
mostCurrent._lbldisplay.setText((Object)(_result));
 }else {
 //BA.debugLineNum = 526;BA.debugLine="lblDisplay.Text = numI";
mostCurrent._lbldisplay.setText((Object)(_numi));
 };
 //BA.debugLineNum = 528;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private leftSideMargin As Int";
_leftsidemargin = 0;
 //BA.debugLineNum = 19;BA.debugLine="Private rightSideMargin As Int";
_rightsidemargin = 0;
 //BA.debugLineNum = 20;BA.debugLine="Dim centerH As Int";
_centerh = 0;
 //BA.debugLineNum = 21;BA.debugLine="Dim centerV As Int";
_centerv = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim result As Float";
_result = 0f;
 //BA.debugLineNum = 23;BA.debugLine="Dim btnHit As Boolean";
_btnhit = false;
 //BA.debugLineNum = 24;BA.debugLine="Dim currentNum As String";
_currentnum = "";
 //BA.debugLineNum = 25;BA.debugLine="Dim fromx,fromy,tox,toy As Int";
_fromx = 0;
_fromy = 0;
_tox = 0;
_toy = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim timerPress As Timer";
_timerpress = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 27;BA.debugLine="Dim timerHold As Timer";
_timerhold = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 28;BA.debugLine="Dim vibrateSetting As Int = 25";
_vibratesetting = (int) (25);
 //BA.debugLineNum = 29;BA.debugLine="Private displayScrollLeftPos As Int";
_displayscrollleftpos = 0;
 //BA.debugLineNum = 30;BA.debugLine="Private displayScrollWidth As Int";
_displayscrollwidth = 0;
 //BA.debugLineNum = 31;BA.debugLine="Private displayScrollTopPos As Int";
_displayscrolltoppos = 0;
 //BA.debugLineNum = 32;BA.debugLine="Private displayScrollHeight As Int";
_displayscrollheight = 0;
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _redocalc() throws Exception{
int _charloop = 0;
String _currentchar = "";
String _currentnumstr = "";
float _currenttotal = 0f;
String _storedop = "";
int _numi = 0;
double _numd = 0;
 //BA.debugLineNum = 588;BA.debugLine="Sub RedoCalc";
 //BA.debugLineNum = 589;BA.debugLine="Dim charloop As Int";
_charloop = 0;
 //BA.debugLineNum = 590;BA.debugLine="Dim currentChar As String";
_currentchar = "";
 //BA.debugLineNum = 591;BA.debugLine="Dim currentNumStr As String = \"\"";
_currentnumstr = "";
 //BA.debugLineNum = 592;BA.debugLine="Dim currentTotal As Float = 0";
_currenttotal = (float) (0);
 //BA.debugLineNum = 593;BA.debugLine="Dim storedOp As String";
_storedop = "";
 //BA.debugLineNum = 595;BA.debugLine="For charloop = 0 To lblComplete.Text.Length - 1";
{
final int step470 = 1;
final int limit470 = (int) (mostCurrent._lblcomplete.getText().length()-1);
for (_charloop = (int) (0); (step470 > 0 && _charloop <= limit470) || (step470 < 0 && _charloop >= limit470); _charloop = ((int)(0 + _charloop + step470))) {
 //BA.debugLineNum = 596;BA.debugLine="currentChar = lblComplete.Text.SubString2(charloop,charloop+1)";
_currentchar = mostCurrent._lblcomplete.getText().substring(_charloop,(int) (_charloop+1));
 //BA.debugLineNum = 598;BA.debugLine="If IsNumber(currentChar) OR currentChar = \".\" Then";
if (anywheresoftware.b4a.keywords.Common.IsNumber(_currentchar) || (_currentchar).equals(".")) { 
 //BA.debugLineNum = 599;BA.debugLine="currentNumStr = currentNumStr & currentChar";
_currentnumstr = _currentnumstr+_currentchar;
 }else if((_currentchar).equals("+") || (_currentchar).equals("-") || (_currentchar).equals("*") || (_currentchar).equals("÷")) { 
 //BA.debugLineNum = 601;BA.debugLine="If storedOp = \"\" Then";
if ((_storedop).equals("")) { 
 //BA.debugLineNum = 602;BA.debugLine="currentTotal = currentNumStr";
_currenttotal = (float)(Double.parseDouble(_currentnumstr));
 }else {
 //BA.debugLineNum = 604;BA.debugLine="Select storedOp";
switch (BA.switchObjectToInt(_storedop,"+","-","*","÷")) {
case 0:
 //BA.debugLineNum = 606;BA.debugLine="currentTotal = currentTotal + currentNumStr";
_currenttotal = (float) (_currenttotal+(double)(Double.parseDouble(_currentnumstr)));
 break;
case 1:
 //BA.debugLineNum = 608;BA.debugLine="currentTotal = currentTotal - currentNumStr";
_currenttotal = (float) (_currenttotal-(double)(Double.parseDouble(_currentnumstr)));
 break;
case 2:
 //BA.debugLineNum = 610;BA.debugLine="currentTotal = currentTotal * currentNumStr";
_currenttotal = (float) (_currenttotal*(double)(Double.parseDouble(_currentnumstr)));
 break;
case 3:
 //BA.debugLineNum = 612;BA.debugLine="currentTotal = currentTotal / currentNumStr";
_currenttotal = (float) (_currenttotal/(double)(double)(Double.parseDouble(_currentnumstr)));
 break;
}
;
 };
 //BA.debugLineNum = 615;BA.debugLine="storedOp = currentChar";
_storedop = _currentchar;
 //BA.debugLineNum = 616;BA.debugLine="currentNumStr = \"\"";
_currentnumstr = "";
 };
 }
};
 //BA.debugLineNum = 621;BA.debugLine="Select storedOp";
switch (BA.switchObjectToInt(_storedop,"+","-","*","÷")) {
case 0:
 //BA.debugLineNum = 623;BA.debugLine="currentTotal = currentTotal + currentNumStr";
_currenttotal = (float) (_currenttotal+(double)(Double.parseDouble(_currentnumstr)));
 break;
case 1:
 //BA.debugLineNum = 625;BA.debugLine="currentTotal = currentTotal - currentNumStr";
_currenttotal = (float) (_currenttotal-(double)(Double.parseDouble(_currentnumstr)));
 break;
case 2:
 //BA.debugLineNum = 627;BA.debugLine="currentTotal = currentTotal * currentNumStr";
_currenttotal = (float) (_currenttotal*(double)(Double.parseDouble(_currentnumstr)));
 break;
case 3:
 //BA.debugLineNum = 629;BA.debugLine="currentTotal = currentTotal / currentNumStr";
_currenttotal = (float) (_currenttotal/(double)(double)(Double.parseDouble(_currentnumstr)));
 break;
}
;
 //BA.debugLineNum = 632;BA.debugLine="result = currentTotal";
_result = _currenttotal;
 //BA.debugLineNum = 634;BA.debugLine="Dim numI As Int = result";
_numi = (int) (_result);
 //BA.debugLineNum = 635;BA.debugLine="Dim numD As Double";
_numd = 0;
 //BA.debugLineNum = 636;BA.debugLine="numD = numI";
_numd = _numi;
 //BA.debugLineNum = 638;BA.debugLine="If numD <> result Then";
if (_numd!=_result) { 
 //BA.debugLineNum = 639;BA.debugLine="lblDisplay.Text = result";
mostCurrent._lbldisplay.setText((Object)(_result));
 }else {
 //BA.debugLineNum = 641;BA.debugLine="lblDisplay.Text = numI";
mostCurrent._lbldisplay.setText((Object)(_numi));
 };
 //BA.debugLineNum = 643;BA.debugLine="End Sub";
return "";
}
public static String  _removemultiline(anywheresoftware.b4a.objects.LabelWrapper _lbl) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 530;BA.debugLine="Private Sub RemoveMultiLine(lbl As Label)";
 //BA.debugLineNum = 533;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 534;BA.debugLine="r.Target = lbl";
_r.Target = (Object)(_lbl.getObject());
 //BA.debugLineNum = 535;BA.debugLine="r.RunMethod2(\"setSingleLine\", True, \"java.lang.boolean\")";
_r.RunMethod2("setSingleLine",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True),"java.lang.boolean");
 //BA.debugLineNum = 536;BA.debugLine="End Sub";
return "";
}
public static String  _setupanimations() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub SetupAnimations";
 //BA.debugLineNum = 127;BA.debugLine="displayAnim.InitializeScaleCenter(\"Animation\",1,1,4,4,lblAnimated)";
mostCurrent._displayanim.InitializeScaleCenter(mostCurrent.activityBA,"Animation",(float) (1),(float) (1),(float) (4),(float) (4),(android.view.View)(mostCurrent._lblanimated.getObject()));
 //BA.debugLineNum = 128;BA.debugLine="displayAnim.Duration = 250";
mostCurrent._displayanim.setDuration((long) (250));
 //BA.debugLineNum = 129;BA.debugLine="displayAnim.RepeatMode = displayAnim.REPEAT_REVERSE";
mostCurrent._displayanim.setRepeatMode(mostCurrent._displayanim.REPEAT_REVERSE);
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _setuptimers() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub SetupTimers";
 //BA.debugLineNum = 121;BA.debugLine="timerPress.Initialize(\"timerPress\",800)";
_timerpress.Initialize(processBA,"timerPress",(long) (800));
 //BA.debugLineNum = 122;BA.debugLine="timerHold.Initialize(\"timerHold\",400)";
_timerhold.Initialize(processBA,"timerHold",(long) (400));
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _tbtnmode_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 671;BA.debugLine="Sub tbtnMode_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 673;BA.debugLine="If tbtnMode.Checked Then";
if (mostCurrent._tbtnmode.getChecked()) { 
 //BA.debugLineNum = 674;BA.debugLine="UpdateExpression";
_updateexpression();
 }else {
 //BA.debugLineNum = 676;BA.debugLine="RedoCalc";
_redocalc();
 };
 //BA.debugLineNum = 679;BA.debugLine="End Sub";
return "";
}
public static String  _timerhold_tick() throws Exception{
 //BA.debugLineNum = 701;BA.debugLine="Sub timerHold_Tick";
 //BA.debugLineNum = 702;BA.debugLine="If lblDisplay.Text <> \".\" Then lblDisplay.Text = lblDisplay.Text & lastLabel.Text";
if ((mostCurrent._lbldisplay.getText()).equals(".") == false) { 
mostCurrent._lbldisplay.setText((Object)(mostCurrent._lbldisplay.getText()+mostCurrent._lastlabel.getText()));};
 //BA.debugLineNum = 703;BA.debugLine="End Sub";
return "";
}
public static String  _timerpress_tick() throws Exception{
 //BA.debugLineNum = 693;BA.debugLine="Sub timerPress_Tick";
 //BA.debugLineNum = 694;BA.debugLine="If lblDisplay.Text <> \".\" Then";
if ((mostCurrent._lbldisplay.getText()).equals(".") == false) { 
 //BA.debugLineNum = 695;BA.debugLine="lblDisplay.Text = lblDisplay.Text & lastLabel.Text";
mostCurrent._lbldisplay.setText((Object)(mostCurrent._lbldisplay.getText()+mostCurrent._lastlabel.getText()));
 //BA.debugLineNum = 696;BA.debugLine="timerPress.Enabled = False";
_timerpress.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 697;BA.debugLine="timerHold.Enabled = True";
_timerhold.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 699;BA.debugLine="End Sub";
return "";
}
public static String  _txt1_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Sub txt1_TextChanged (Old As String, New As String)";
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return "";
}
public static String  _updatecompletelabel(String _newtextvalue) throws Exception{
int _width = 0;
 //BA.debugLineNum = 462;BA.debugLine="Sub UpdateCompleteLabel(newTextValue As String)";
 //BA.debugLineNum = 465;BA.debugLine="Dim width As Int = picCanvas.MeasureStringWidth(newTextValue, lblComplete.Typeface, lblComplete.TextSize)";
_width = (int) (mostCurrent._piccanvas.MeasureStringWidth(_newtextvalue,mostCurrent._lblcomplete.getTypeface(),mostCurrent._lblcomplete.getTextSize()));
 //BA.debugLineNum = 467;BA.debugLine="lblComplete.width = width";
mostCurrent._lblcomplete.setWidth(_width);
 //BA.debugLineNum = 468;BA.debugLine="lblComplete.Text = newTextValue";
mostCurrent._lblcomplete.setText((Object)(_newtextvalue));
 //BA.debugLineNum = 469;BA.debugLine="If lblComplete.width < scvComplete.width Then";
if (mostCurrent._lblcomplete.getWidth()<mostCurrent._scvcomplete.getWidth()) { 
 //BA.debugLineNum = 470;BA.debugLine="lblComplete.RemoveView";
mostCurrent._lblcomplete.RemoveView();
 //BA.debugLineNum = 471;BA.debugLine="scvComplete.Panel.AddView(lblComplete,displayScrollWidth - lblComplete.width,displayScrollTopPos,displayScrollWidth,displayScrollHeight)";
mostCurrent._scvcomplete.getPanel().AddView((android.view.View)(mostCurrent._lblcomplete.getObject()),(int) (_displayscrollwidth-mostCurrent._lblcomplete.getWidth()),_displayscrolltoppos,_displayscrollwidth,_displayscrollheight);
 //BA.debugLineNum = 472;BA.debugLine="scvComplete.Panel.width = lblComplete.width";
mostCurrent._scvcomplete.getPanel().setWidth(mostCurrent._lblcomplete.getWidth());
 }else {
 //BA.debugLineNum = 474;BA.debugLine="If lblComplete.Left <> displayScrollLeftPos Then";
if (mostCurrent._lblcomplete.getLeft()!=_displayscrollleftpos) { 
 //BA.debugLineNum = 475;BA.debugLine="lblComplete.RemoveView";
mostCurrent._lblcomplete.RemoveView();
 //BA.debugLineNum = 476;BA.debugLine="scvComplete.Panel.AddView(lblComplete,displayScrollLeftPos,displayScrollTopPos,displayScrollWidth,displayScrollHeight)";
mostCurrent._scvcomplete.getPanel().AddView((android.view.View)(mostCurrent._lblcomplete.getObject()),_displayscrollleftpos,_displayscrolltoppos,_displayscrollwidth,_displayscrollheight);
 //BA.debugLineNum = 477;BA.debugLine="scvComplete.Panel.width = lblComplete.width";
mostCurrent._scvcomplete.getPanel().setWidth(mostCurrent._lblcomplete.getWidth());
 //BA.debugLineNum = 478;BA.debugLine="scvComplete.ScrollPosition = scvComplete.Panel.width";
mostCurrent._scvcomplete.setScrollPosition(mostCurrent._scvcomplete.getPanel().getWidth());
 }else {
 //BA.debugLineNum = 480;BA.debugLine="scvComplete.Panel.width = lblComplete.width";
mostCurrent._scvcomplete.getPanel().setWidth(mostCurrent._lblcomplete.getWidth());
 //BA.debugLineNum = 481;BA.debugLine="scvComplete.RequestFocus";
mostCurrent._scvcomplete.RequestFocus();
 //BA.debugLineNum = 482;BA.debugLine="scvComplete.ScrollPosition = lblComplete.width";
mostCurrent._scvcomplete.setScrollPosition(mostCurrent._lblcomplete.getWidth());
 };
 };
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _updatedisplays() throws Exception{
String _tempcomplete = "";
 //BA.debugLineNum = 435;BA.debugLine="Sub UpdateDisplays";
 //BA.debugLineNum = 436;BA.debugLine="If lblDisplay.Text <> \".\" AND lblDisplay.Text <> \"0\" Then";
if ((mostCurrent._lbldisplay.getText()).equals(".") == false && (mostCurrent._lbldisplay.getText()).equals("0") == false) { 
 //BA.debugLineNum = 437;BA.debugLine="Dim tempComplete As String = lblComplete.Text";
_tempcomplete = mostCurrent._lblcomplete.getText();
 //BA.debugLineNum = 438;BA.debugLine="If btnHit Then";
if (_btnhit) { 
 //BA.debugLineNum = 439;BA.debugLine="If lblDisplay.Text.EndsWith(\".\") Then lblDisplay.Text = lblDisplay.Text & \"0\"";
if (mostCurrent._lbldisplay.getText().endsWith(".")) { 
mostCurrent._lbldisplay.setText((Object)(mostCurrent._lbldisplay.getText()+"0"));};
 //BA.debugLineNum = 440;BA.debugLine="If tempComplete = \"\" Then";
if ((_tempcomplete).equals("")) { 
 //BA.debugLineNum = 441;BA.debugLine="tempComplete = lblDisplay.Text";
_tempcomplete = mostCurrent._lbldisplay.getText();
 }else {
 //BA.debugLineNum = 443;BA.debugLine="If tempComplete.EndsWith(\"(\") Then";
if (_tempcomplete.endsWith("(")) { 
 //BA.debugLineNum = 444;BA.debugLine="tempComplete = tempComplete & \" \" & lblDisplay.Text";
_tempcomplete = _tempcomplete+" "+mostCurrent._lbldisplay.getText();
 }else if(_tempcomplete.endsWith(".")) { 
 //BA.debugLineNum = 446;BA.debugLine="tempComplete = tempComplete & \" \" & btnOp.Text & \" \" & lblDisplay.Text & \"0\"";
_tempcomplete = _tempcomplete+" "+mostCurrent._btnop.getText()+" "+mostCurrent._lbldisplay.getText()+"0";
 }else {
 //BA.debugLineNum = 448;BA.debugLine="tempComplete = tempComplete & \" \" & btnOp.Text & \" \" & lblDisplay.Text";
_tempcomplete = _tempcomplete+" "+mostCurrent._btnop.getText()+" "+mostCurrent._lbldisplay.getText();
 };
 };
 //BA.debugLineNum = 452;BA.debugLine="UpdateCompleteLabel(tempComplete)";
_updatecompletelabel(_tempcomplete);
 //BA.debugLineNum = 454;BA.debugLine="PerformCalculation";
_performcalculation();
 };
 };
 //BA.debugLineNum = 458;BA.debugLine="ClearLines";
_clearlines();
 //BA.debugLineNum = 460;BA.debugLine="End Sub";
return "";
}
public static String  _updateexpression() throws Exception{
String _calculation = "";
anywheresoftware.b4a.agraham.expressionevaluator.Evaluator _exp = null;
int _numi = 0;
float _numf = 0f;
 //BA.debugLineNum = 568;BA.debugLine="Sub UpdateExpression";
 //BA.debugLineNum = 569;BA.debugLine="If tbtnMode.Checked Then";
if (mostCurrent._tbtnmode.getChecked()) { 
 //BA.debugLineNum = 570;BA.debugLine="Dim calculation As String = lblComplete.Text";
_calculation = mostCurrent._lblcomplete.getText();
 //BA.debugLineNum = 571;BA.debugLine="calculation = calculation.Replace(\"÷\",\"/\")";
_calculation = _calculation.replace("÷","/");
 //BA.debugLineNum = 572;BA.debugLine="Dim exp As Evaluator";
_exp = new anywheresoftware.b4a.agraham.expressionevaluator.Evaluator();
 //BA.debugLineNum = 573;BA.debugLine="exp.Initialize";
_exp.Initialize();
 //BA.debugLineNum = 574;BA.debugLine="result = exp.Evaluate(calculation)";
_result = (float) (_exp.Evaluate(_calculation));
 //BA.debugLineNum = 576;BA.debugLine="Dim numI As Int = result";
_numi = (int) (_result);
 //BA.debugLineNum = 577;BA.debugLine="Dim numF As Float";
_numf = 0f;
 //BA.debugLineNum = 578;BA.debugLine="numF = numI";
_numf = (float) (_numi);
 //BA.debugLineNum = 580;BA.debugLine="If numF <> result Then";
if (_numf!=_result) { 
 //BA.debugLineNum = 581;BA.debugLine="lblDisplay.Text = result";
mostCurrent._lbldisplay.setText((Object)(_result));
 }else {
 //BA.debugLineNum = 583;BA.debugLine="lblDisplay.Text = numI";
mostCurrent._lbldisplay.setText((Object)(_numi));
 };
 };
 //BA.debugLineNum = 586;BA.debugLine="End Sub";
return "";
}
public static String  _updatelabel(int _x,String _number,anywheresoftware.b4a.objects.LabelWrapper _currentlabel) throws Exception{
 //BA.debugLineNum = 384;BA.debugLine="Public Sub UpdateLabel(X As Int, number As String, currentLabel As Label)";
 //BA.debugLineNum = 385;BA.debugLine="If number = \".\" Then";
if ((_number).equals(".")) { 
 //BA.debugLineNum = 386;BA.debugLine="If hasDecimal = False Then";
if (_hasdecimal==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 387;BA.debugLine="lblDisplay.Text = lblDisplay.Text & number";
mostCurrent._lbldisplay.setText((Object)(mostCurrent._lbldisplay.getText()+_number));
 //BA.debugLineNum = 388;BA.debugLine="btnHit = True";
_btnhit = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 389;BA.debugLine="currentNum = number";
_currentnum = _number;
 //BA.debugLineNum = 390;BA.debugLine="hasDecimal = True";
_hasdecimal = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 391;BA.debugLine="lastLabel = currentLabel";
mostCurrent._lastlabel = _currentlabel;
 };
 }else {
 //BA.debugLineNum = 394;BA.debugLine="lblDisplay.Text = lblDisplay.Text & number";
mostCurrent._lbldisplay.setText((Object)(mostCurrent._lbldisplay.getText()+_number));
 //BA.debugLineNum = 395;BA.debugLine="currentNum = number";
_currentnum = _number;
 //BA.debugLineNum = 396;BA.debugLine="btnHit = True";
_btnhit = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 397;BA.debugLine="lastLabel = currentLabel";
mostCurrent._lastlabel = _currentlabel;
 //BA.debugLineNum = 399;BA.debugLine="lblBig.Color = Colors.Red";
mostCurrent._lblbig.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 400;BA.debugLine="lblBig.Left = currentLabel.Left - 30";
mostCurrent._lblbig.setLeft((int) (_currentlabel.getLeft()-30));
 //BA.debugLineNum = 401;BA.debugLine="lblBig.Top =  currentLabel.Top - 30";
mostCurrent._lblbig.setTop((int) (_currentlabel.getTop()-30));
 //BA.debugLineNum = 402;BA.debugLine="lblBig.width = currentLabel.Width + 60";
mostCurrent._lblbig.setWidth((int) (_currentlabel.getWidth()+60));
 //BA.debugLineNum = 403;BA.debugLine="lblBig.Height =  currentLabel.Height + 60";
mostCurrent._lblbig.setHeight((int) (_currentlabel.getHeight()+60));
 };
 //BA.debugLineNum = 406;BA.debugLine="lblMoving.Text = lblDisplay.Text";
mostCurrent._lblmoving.setText((Object)(mostCurrent._lbldisplay.getText()));
 //BA.debugLineNum = 407;BA.debugLine="End Sub";
return "";
}
public static String  _updatemargins() throws Exception{
 //BA.debugLineNum = 370;BA.debugLine="Public Sub UpdateMargins()";
 //BA.debugLineNum = 371;BA.debugLine="If lastLabel.IsInitialized Then";
if (mostCurrent._lastlabel.IsInitialized()) { 
 //BA.debugLineNum = 372;BA.debugLine="If lastLabel.Text <> \"0\" Then";
if ((mostCurrent._lastlabel.getText()).equals("0") == false) { 
 //BA.debugLineNum = 373;BA.debugLine="If lastLabel.Left < centerH Then";
if (mostCurrent._lastlabel.getLeft()<_centerh) { 
 //BA.debugLineNum = 374;BA.debugLine="leftSideMargin = 10";
_leftsidemargin = (int) (10);
 //BA.debugLineNum = 375;BA.debugLine="rightSideMargin = 50";
_rightsidemargin = (int) (50);
 }else {
 //BA.debugLineNum = 377;BA.debugLine="leftSideMargin = 50";
_leftsidemargin = (int) (50);
 //BA.debugLineNum = 378;BA.debugLine="rightSideMargin = 10";
_rightsidemargin = (int) (10);
 };
 };
 };
 //BA.debugLineNum = 382;BA.debugLine="End Sub";
return "";
}
}
