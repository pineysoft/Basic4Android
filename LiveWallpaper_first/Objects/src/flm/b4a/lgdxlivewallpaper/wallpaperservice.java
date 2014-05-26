package flm.b4a.lgdxlivewallpaper;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class wallpaperservice extends android.app.Service {
	public static class wallpaperservice_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, wallpaperservice.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static wallpaperservice mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return wallpaperservice.class;
	}
	@Override
	public void onCreate() {
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "flm.b4a.lgdxlivewallpaper", "flm.b4a.lgdxlivewallpaper.wallpaperservice");
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        processBA.setActivityPaused(false);
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "flm.b4a.lgdxlivewallpaper.wallpaperservice", processBA, _service);
		}
        BA.LogInfo("** Service (wallpaperservice) Create **");
        processBA.raiseEvent(null, "service_create");
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		handleStart(intent);
    }
    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    	handleStart(intent);
		return android.app.Service.START_NOT_STICKY;
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (wallpaperservice) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = new anywheresoftware.b4a.objects.IntentWrapper();
    			if (intent != null) {
    				if (intent.hasExtra("b4a_internal_intent"))
    					iw.setObject((android.content.Intent) intent.getParcelableExtra("b4a_internal_intent"));
    				else
    					iw.setObject(intent);
    			}
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() {
        BA.LogInfo("** Service (wallpaperservice) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
	}
public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.libgdx.lgLiveWallpaper.LW_Manager _lw = null;
public static anywheresoftware.b4a.libgdx.graphics.lgGL _gl = null;
public static anywheresoftware.b4a.libgdx.graphics.lgSpriteBatch _batch = null;
public static anywheresoftware.b4a.libgdx.graphics.lgOrthographicCamera _camera = null;
public static anywheresoftware.b4a.libgdx.graphics.lgScissorStack _scissorstack = null;
public static com.badlogic.gdx.math.Rectangle _scissors = null;
public static com.badlogic.gdx.math.Rectangle _clipbounds = null;
public static float _clipbdsvalue = 0f;
public static float _clipbdsinc = 0f;
public static boolean _enabled = false;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _texsmiley = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _texbg = null;
public static anywheresoftware.b4a.objects.collections.List _lstsmileys = null;
public flm.b4a.lgdxlivewallpaper.main _main = null;
public static class _typsmiley{
public boolean IsInitialized;
public float X;
public float Y;
public float dX;
public float dY;
public com.badlogic.gdx.graphics.Color Color;
public void Initialize() {
IsInitialized = true;
X = 0f;
Y = 0f;
dX = 0f;
dY = 0f;
Color = new com.badlogic.gdx.graphics.Color();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _createsmileys() throws Exception{
com.badlogic.gdx.math.MathUtils _mu = null;
int _i = 0;
flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _smiley = null;
 //BA.debugLineNum = 60;BA.debugLine="Sub CreateSmileys";
 //BA.debugLineNum = 62;BA.debugLine="lstSmileys.Initialize";
_lstsmileys.Initialize();
 //BA.debugLineNum = 63;BA.debugLine="Dim MU As lgMathUtils";
_mu = new com.badlogic.gdx.math.MathUtils();
 //BA.debugLineNum = 64;BA.debugLine="For i = 1 To 5";
{
final int step38 = 1;
final int limit38 = (int) (5);
for (_i = (int) (1); (step38 > 0 && _i <= limit38) || (step38 < 0 && _i >= limit38); _i = ((int)(0 + _i + step38))) {
 //BA.debugLineNum = 65;BA.debugLine="Dim Smiley As typSmiley";
_smiley = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 66;BA.debugLine="Smiley.Initialize";
_smiley.Initialize();
 //BA.debugLineNum = 67;BA.debugLine="Smiley.X = Rnd(64dip * 0.5, LW.Graphics.Width - (64dip * 1.5))";
_smiley.X = (float) (anywheresoftware.b4a.keywords.Common.Rnd((int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))*0.5),(int) (_lw.Graphics().getWidth()-(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))*1.5))));
 //BA.debugLineNum = 68;BA.debugLine="Smiley.Y = Rnd(64dip * 0.5, LW.Graphics.Height - (64dip * 1.5))";
_smiley.Y = (float) (anywheresoftware.b4a.keywords.Common.Rnd((int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))*0.5),(int) (_lw.Graphics().getHeight()-(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))*1.5))));
 //BA.debugLineNum = 69;BA.debugLine="Smiley.dX = Rnd(3, 6)";
_smiley.dX = (float) (anywheresoftware.b4a.keywords.Common.Rnd((int) (3),(int) (6)));
 //BA.debugLineNum = 70;BA.debugLine="Smiley.dY = Rnd(3, 6)";
_smiley.dY = (float) (anywheresoftware.b4a.keywords.Common.Rnd((int) (3),(int) (6)));
 //BA.debugLineNum = 71;BA.debugLine="If MU.RandomBoolean Then Smiley.dX = -Smiley.dX";
if (_mu.randomBoolean()) { 
_smiley.dX = (float) (-_smiley.dX);};
 //BA.debugLineNum = 72;BA.debugLine="If MU.RandomBoolean Then Smiley.dY = -Smiley.dY";
if (_mu.randomBoolean()) { 
_smiley.dY = (float) (-_smiley.dY);};
 //BA.debugLineNum = 73;BA.debugLine="Smiley.Color.setRGBA(1, 1, 1, 1)";
_smiley.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (1));
 //BA.debugLineNum = 74;BA.debugLine="lstSmileys.Add(Smiley)";
_lstsmileys.Add((Object)(_smiley));
 }
};
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _lg_create() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub LG_Create";
 //BA.debugLineNum = 40;BA.debugLine="Log(\"--- CREATE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- CREATE ---");
 //BA.debugLineNum = 43;BA.debugLine="Batch.Initialize";
_batch.Initialize();
 //BA.debugLineNum = 46;BA.debugLine="texSmiley.Initialize(\"skater.png\")";
_texsmiley.Initialize("skater.png");
 //BA.debugLineNum = 47;BA.debugLine="texBG.Initialize(\"HockeyBoardPortrait_Stars.png\")";
_texbg.Initialize("HockeyBoardPortrait_Stars.png");
 //BA.debugLineNum = 50;BA.debugLine="CreateSmileys";
_createsmileys();
 //BA.debugLineNum = 53;BA.debugLine="ClipBdsValue = 0.2";
_clipbdsvalue = (float) (0.2);
 //BA.debugLineNum = 54;BA.debugLine="ClipBdsInc = 0.005";
_clipbdsinc = (float) (0.005);
 //BA.debugLineNum = 57;BA.debugLine="Enabled = False";
_enabled = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _lg_dispose() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub LG_Dispose";
 //BA.debugLineNum = 178;BA.debugLine="Log(\"--- DISPOSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- DISPOSE ---");
 //BA.debugLineNum = 181;BA.debugLine="texSmiley.dispose";
_texsmiley.dispose();
 //BA.debugLineNum = 182;BA.debugLine="Batch.dispose";
_batch.dispose();
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _lg_offsetchange(float _x_offset,float _y_offset,float _x_offsetstep,float _y_offsetstep,int _x_pixeloffset,int _y_pixeloffset) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)";
 //BA.debugLineNum = 87;BA.debugLine="Log(\"OffsetChange: \" & X_Offset & \",\" & Y_Offset & \"  \" & X_OffsetStep & \",\" & Y_OffsetStep & \"  \" & X_PixelOffset & \",\" & Y_PixelOffset)";
anywheresoftware.b4a.keywords.Common.Log("OffsetChange: "+BA.NumberToString(_x_offset)+","+BA.NumberToString(_y_offset)+"  "+BA.NumberToString(_x_offsetstep)+","+BA.NumberToString(_y_offsetstep)+"  "+BA.NumberToString(_x_pixeloffset)+","+BA.NumberToString(_y_pixeloffset));
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _lg_pause() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Sub LG_Pause";
 //BA.debugLineNum = 163;BA.debugLine="Log(\"--- PAUSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- PAUSE ---");
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
return "";
}
public static String  _lg_previewstatechange(boolean _ispreview) throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub LG_PreviewStateChange(IsPreview As Boolean)";
 //BA.debugLineNum = 91;BA.debugLine="Log(\"PreviewState = \" & IsPreview)";
anywheresoftware.b4a.keywords.Common.Log("PreviewState = "+BA.ObjectToString(_ispreview));
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _lg_render() throws Exception{
int _x = 0;
int _y = 0;
flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _smiley = null;
int _i = 0;
 //BA.debugLineNum = 94;BA.debugLine="Sub LG_Render";
 //BA.debugLineNum = 96;BA.debugLine="GL.glClearColor(0, 0, 0, 1)";
_gl.glClearColor((float) (0),(float) (0),(float) (0),(float) (1));
 //BA.debugLineNum = 97;BA.debugLine="GL.glClear(GL.GL10_COLOR_BUFFER_BIT)";
_gl.glClear(_gl.GL10_COLOR_BUFFER_BIT);
 //BA.debugLineNum = 100;BA.debugLine="If LW.Input.JustTouched Then";
if (_lw.Input().justTouched()) { 
 //BA.debugLineNum = 101;BA.debugLine="Enabled = Not(Enabled)";
_enabled = anywheresoftware.b4a.keywords.Common.Not(_enabled);
 //BA.debugLineNum = 102;BA.debugLine="Log(\"X=\" & LW.Input.X & \", Y=\" & LW.Input.Y)";
anywheresoftware.b4a.keywords.Common.Log("X="+BA.NumberToString(_lw.Input().getX())+", Y="+BA.NumberToString(_lw.Input().getY()));
 };
 //BA.debugLineNum = 106;BA.debugLine="LG_Update";
_lg_update();
 //BA.debugLineNum = 109;BA.debugLine="Batch.ProjectionMatrix = Camera.Combined";
_batch.setProjectionMatrix(_camera.getCombined());
 //BA.debugLineNum = 112;BA.debugLine="Batch.Begin";
_batch.Begin();
 //BA.debugLineNum = 113;BA.debugLine="If Enabled Then";
if (_enabled) { 
 //BA.debugLineNum = 114;BA.debugLine="Dim X As Int = ClipBdsValue * LW.Graphics.Width";
_x = (int) (_clipbdsvalue*_lw.Graphics().getWidth());
 //BA.debugLineNum = 115;BA.debugLine="Dim Y As Int = ClipBdsValue * LW.Graphics.Height";
_y = (int) (_clipbdsvalue*_lw.Graphics().getHeight());
 //BA.debugLineNum = 116;BA.debugLine="ClipBounds.Set(X, Y, LW.Graphics.Width - (X * 2), LW.Graphics.Height - (Y * 2))";
_clipbounds.Set((float) (_x),(float) (_y),(float) (_lw.Graphics().getWidth()-(_x*2)),(float) (_lw.Graphics().getHeight()-(_y*2)));
 //BA.debugLineNum = 117;BA.debugLine="ScissorStack.CalculateScissors(Camera, 0, 0, Camera.ViewportWidth, Camera.ViewportHeight, Batch.TransformMatrix, ClipBounds, Scissors)";
_scissorstack.CalculateScissors(_camera,(float) (0),(float) (0),_camera.getViewportWidth(),_camera.getViewportHeight(),_batch.getTransformMatrix(),_clipbounds,_scissors);
 //BA.debugLineNum = 118;BA.debugLine="ScissorStack.PushScissors(Scissors)";
_scissorstack.PushScissors(_scissors);
 };
 //BA.debugLineNum = 121;BA.debugLine="Batch.DrawTex2(texBG, 0,0,LW.Graphics.Width, LW.Graphics.Height)";
_batch.DrawTex2(_texbg,(float) (0),(float) (0),(float) (_lw.Graphics().getWidth()),(float) (_lw.Graphics().getHeight()));
 //BA.debugLineNum = 123;BA.debugLine="Dim Smiley As typSmiley";
_smiley = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 124;BA.debugLine="For i = 0 To lstSmileys.Size - 1";
{
final int step81 = 1;
final int limit81 = (int) (_lstsmileys.getSize()-1);
for (_i = (int) (0); (step81 > 0 && _i <= limit81) || (step81 < 0 && _i >= limit81); _i = ((int)(0 + _i + step81))) {
 //BA.debugLineNum = 125;BA.debugLine="Smiley = lstSmileys.Get(i)";
_smiley = (flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley)(_lstsmileys.Get(_i));
 //BA.debugLineNum = 126;BA.debugLine="Batch.Color = Smiley.Color";
_batch.setColor(_smiley.Color);
 //BA.debugLineNum = 127;BA.debugLine="Batch.DrawTex2(texSmiley, Smiley.X, Smiley.Y, 64dip, 64dip)";
_batch.DrawTex2(_texsmiley,_smiley.X,_smiley.Y,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))));
 }
};
 //BA.debugLineNum = 130;BA.debugLine="If Enabled Then";
if (_enabled) { 
 //BA.debugLineNum = 131;BA.debugLine="Batch.Flush";
_batch.Flush();
 //BA.debugLineNum = 132;BA.debugLine="ScissorStack.PopScissors";
_scissorstack.PopScissors();
 };
 //BA.debugLineNum = 134;BA.debugLine="Batch.End";
_batch.End();
 //BA.debugLineNum = 137;BA.debugLine="If Enabled Then";
if (_enabled) { 
 //BA.debugLineNum = 138;BA.debugLine="ClipBdsValue = ClipBdsValue + ClipBdsInc";
_clipbdsvalue = (float) (_clipbdsvalue+_clipbdsinc);
 //BA.debugLineNum = 139;BA.debugLine="If ClipBdsValue < 0.03 OR ClipBdsValue > 0.45 Then ClipBdsInc = -ClipBdsInc";
if (_clipbdsvalue<0.03 || _clipbdsvalue>0.45) { 
_clipbdsinc = (float) (-_clipbdsinc);};
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resize(int _width,int _height) throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub LG_Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 79;BA.debugLine="Log(\"--- RESIZE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESIZE ---");
 //BA.debugLineNum = 82;BA.debugLine="Camera.Initialize";
_camera.Initialize();
 //BA.debugLineNum = 83;BA.debugLine="Camera.SetToOrtho(False)";
_camera.SetToOrtho(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resume() throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub LG_Resume";
 //BA.debugLineNum = 167;BA.debugLine="Log(\"--- RESUME ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESUME ---");
 //BA.debugLineNum = 170;BA.debugLine="CreateSmileys";
_createsmileys();
 //BA.debugLineNum = 173;BA.debugLine="ClipBdsValue = 0.2";
_clipbdsvalue = (float) (0.2);
 //BA.debugLineNum = 174;BA.debugLine="ClipBdsInc = 0.005";
_clipbdsinc = (float) (0.005);
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _lg_update() throws Exception{
int _i = 0;
flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _smiley = null;
 //BA.debugLineNum = 143;BA.debugLine="Sub LG_Update";
 //BA.debugLineNum = 145;BA.debugLine="Camera.Update";
_camera.Update();
 //BA.debugLineNum = 149;BA.debugLine="For i = 0 To lstSmileys.Size - 1";
{
final int step98 = 1;
final int limit98 = (int) (_lstsmileys.getSize()-1);
for (_i = (int) (0); (step98 > 0 && _i <= limit98) || (step98 < 0 && _i >= limit98); _i = ((int)(0 + _i + step98))) {
 //BA.debugLineNum = 150;BA.debugLine="Dim Smiley As typSmiley = lstSmileys.Get(i)";
_smiley = (flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley)(_lstsmileys.Get(_i));
 //BA.debugLineNum = 151;BA.debugLine="Smiley.x = Smiley.x + Smiley.dx";
_smiley.X = (float) (_smiley.X+_smiley.dX);
 //BA.debugLineNum = 152;BA.debugLine="Smiley.y = Smiley.y + Smiley.dy";
_smiley.Y = (float) (_smiley.Y+_smiley.dY);
 //BA.debugLineNum = 153;BA.debugLine="If Smiley.x < Abs(Smiley.dx) OR Smiley.x >= LW.Graphics.Width - 64dip Then";
if (_smiley.X<anywheresoftware.b4a.keywords.Common.Abs(_smiley.dX) || _smiley.X>=_lw.Graphics().getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))) { 
 //BA.debugLineNum = 154;BA.debugLine="Smiley.dx = -Smiley.dx";
_smiley.dX = (float) (-_smiley.dX);
 };
 //BA.debugLineNum = 156;BA.debugLine="If Smiley.y < Abs(Smiley.dy) OR Smiley.y >= LW.Graphics.Height - 64dip Then";
if (_smiley.Y<anywheresoftware.b4a.keywords.Common.Abs(_smiley.dY) || _smiley.Y>=_lw.Graphics().getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))) { 
 //BA.debugLineNum = 157;BA.debugLine="Smiley.dy = -Smiley.dy";
_smiley.dY = (float) (-_smiley.dY);
 };
 }
};
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim LW As lgLiveWallpaper";
_lw = new anywheresoftware.b4a.libgdx.lgLiveWallpaper.LW_Manager();
 //BA.debugLineNum = 8;BA.debugLine="Dim GL As lgGL";
_gl = new anywheresoftware.b4a.libgdx.graphics.lgGL();
 //BA.debugLineNum = 9;BA.debugLine="Dim Batch As lgSpriteBatch";
_batch = new anywheresoftware.b4a.libgdx.graphics.lgSpriteBatch();
 //BA.debugLineNum = 10;BA.debugLine="Dim Camera As lgOrthographicCamera";
_camera = new anywheresoftware.b4a.libgdx.graphics.lgOrthographicCamera();
 //BA.debugLineNum = 11;BA.debugLine="Dim ScissorStack As lgScissorStack";
_scissorstack = new anywheresoftware.b4a.libgdx.graphics.lgScissorStack();
 //BA.debugLineNum = 12;BA.debugLine="Dim Scissors As lgMathRectangle";
_scissors = new com.badlogic.gdx.math.Rectangle();
 //BA.debugLineNum = 13;BA.debugLine="Dim ClipBounds As lgMathRectangle";
_clipbounds = new com.badlogic.gdx.math.Rectangle();
 //BA.debugLineNum = 14;BA.debugLine="Dim ClipBdsValue, ClipBdsInc As Float";
_clipbdsvalue = 0f;
_clipbdsinc = 0f;
 //BA.debugLineNum = 15;BA.debugLine="Dim Enabled As Boolean = False";
_enabled = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 16;BA.debugLine="Dim texSmiley As lgTexture";
_texsmiley = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 17;BA.debugLine="Dim texBG As lgTexture";
_texbg = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 18;BA.debugLine="Dim lstSmileys As List";
_lstsmileys = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)";
;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
com.badlogic.gdx.backends.android.AndroidApplicationConfiguration _config = null;
 //BA.debugLineNum = 25;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 27;BA.debugLine="Dim Config As lgConfiguration";
_config = new com.badlogic.gdx.backends.android.AndroidApplicationConfiguration();
 //BA.debugLineNum = 28;BA.debugLine="Config.useAccelerometer = False";
_config.useAccelerometer = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 29;BA.debugLine="Config.useCompass = False";
_config.useCompass = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 32;BA.debugLine="LW.Initialize(Config, True, \"LG\")";
_lw.Initialize(processBA,_config,anywheresoftware.b4a.keywords.Common.True,"LG");
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
}
