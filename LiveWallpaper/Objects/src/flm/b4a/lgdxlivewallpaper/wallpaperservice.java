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
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _texskater = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _texpuck = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _texbg = null;
public static anywheresoftware.b4a.objects.collections.List _lstsmileys = null;
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _sstar = null;
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _spuck = null;
public static anywheresoftware.b4a.objects.Timer _playtimer = null;
public static boolean _allowplay = false;
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
public static String  _createtextures() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub CreateTextures";
 //BA.debugLineNum = 58;BA.debugLine="lstSmileys.Initialize";
_lstsmileys.Initialize();
 //BA.debugLineNum = 60;BA.debugLine="sStar.Initialize";
_sstar.Initialize();
 //BA.debugLineNum = 61;BA.debugLine="sStar.X = 80dip";
_sstar.X = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 62;BA.debugLine="sStar.Y = LW.Graphics.Height + 64dip";
_sstar.Y = (float) (_lw.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64)));
 //BA.debugLineNum = 63;BA.debugLine="sStar.dX = 1dip";
_sstar.dX = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 64;BA.debugLine="sStar.dY = 5dip";
_sstar.dY = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 65;BA.debugLine="sStar.Color.setRGBA(1, 1, 1, .75)";
_sstar.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 //BA.debugLineNum = 67;BA.debugLine="sPuck.Initialize";
_spuck.Initialize();
 //BA.debugLineNum = 68;BA.debugLine="sPuck.X = sStar.X + 5dip";
_spuck.X = (float) (_sstar.X+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 69;BA.debugLine="sPuck.Y = LW.Graphics.Height + 12dip";
_spuck.Y = (float) (_lw.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12)));
 //BA.debugLineNum = 70;BA.debugLine="sPuck.dX = 1dip";
_spuck.dX = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 71;BA.debugLine="sPuck.dY = 5dip";
_spuck.dY = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 72;BA.debugLine="sPuck.Color.setRGBA(1, 1, 1, .75)";
_spuck.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _lg_create() throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub LG_Create";
 //BA.debugLineNum = 41;BA.debugLine="Log(\"--- CREATE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- CREATE ---");
 //BA.debugLineNum = 44;BA.debugLine="Batch.Initialize";
_batch.Initialize();
 //BA.debugLineNum = 47;BA.debugLine="texSkater.Initialize(\"skater.png\")";
_texskater.Initialize("skater.png");
 //BA.debugLineNum = 48;BA.debugLine="texPuck.Initialize(\"Puck.png\")";
_texpuck.Initialize("Puck.png");
 //BA.debugLineNum = 49;BA.debugLine="texBG.Initialize(\"HockeyBoardPortrait_Stars.png\")";
_texbg.Initialize("HockeyBoardPortrait_Stars.png");
 //BA.debugLineNum = 52;BA.debugLine="sStar.Initialize";
_sstar.Initialize();
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _lg_dispose() throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub LG_Dispose";
 //BA.debugLineNum = 172;BA.debugLine="Log(\"--- DISPOSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- DISPOSE ---");
 //BA.debugLineNum = 175;BA.debugLine="texSkater.dispose";
_texskater.dispose();
 //BA.debugLineNum = 176;BA.debugLine="texPuck.dispose";
_texpuck.dispose();
 //BA.debugLineNum = 177;BA.debugLine="texBG.dispose";
_texbg.dispose();
 //BA.debugLineNum = 178;BA.debugLine="Batch.dispose";
_batch.dispose();
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public static String  _lg_offsetchange(float _x_offset,float _y_offset,float _x_offsetstep,float _y_offsetstep,int _x_pixeloffset,int _y_pixeloffset) throws Exception{
 //BA.debugLineNum = 84;BA.debugLine="Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)";
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static String  _lg_pause() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub LG_Pause";
 //BA.debugLineNum = 156;BA.debugLine="playTimer.Enabled = False";
_playtimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 157;BA.debugLine="Log(\"--- PAUSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- PAUSE ---");
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _lg_previewstatechange(boolean _ispreview) throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub LG_PreviewStateChange(IsPreview As Boolean)";
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _lg_render() throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Sub LG_Render";
 //BA.debugLineNum = 94;BA.debugLine="GL.glClearColor(0, 0, 0, 1)";
_gl.glClearColor((float) (0),(float) (0),(float) (0),(float) (1));
 //BA.debugLineNum = 95;BA.debugLine="GL.glClear(GL.GL10_COLOR_BUFFER_BIT)";
_gl.glClear(_gl.GL10_COLOR_BUFFER_BIT);
 //BA.debugLineNum = 98;BA.debugLine="If LW.Input.JustTouched Then";
if (_lw.Input().justTouched()) { 
 //BA.debugLineNum = 99;BA.debugLine="Log(\"X=\" & LW.Input.X & \", Y=\" & LW.Input.Y)";
anywheresoftware.b4a.keywords.Common.Log("X="+BA.NumberToString(_lw.Input().getX())+", Y="+BA.NumberToString(_lw.Input().getY()));
 //BA.debugLineNum = 100;BA.debugLine="sStar.X = Abs(LW.Input.X)";
_sstar.X = (float) (anywheresoftware.b4a.keywords.Common.Abs(_lw.Input().getX()));
 //BA.debugLineNum = 101;BA.debugLine="sStar.Y = Abs(LW.Input.Y)";
_sstar.Y = (float) (anywheresoftware.b4a.keywords.Common.Abs(_lw.Input().getY()));
 };
 //BA.debugLineNum = 105;BA.debugLine="LG_Update";
_lg_update();
 //BA.debugLineNum = 108;BA.debugLine="Batch.ProjectionMatrix = Camera.Combined";
_batch.setProjectionMatrix(_camera.getCombined());
 //BA.debugLineNum = 111;BA.debugLine="Batch.Begin";
_batch.Begin();
 //BA.debugLineNum = 113;BA.debugLine="Batch.DrawTex2(texBG, 0,0,LW.Graphics.Width, LW.Graphics.Height)";
_batch.DrawTex2(_texbg,(float) (0),(float) (0),(float) (_lw.Graphics().getWidth()),(float) (_lw.Graphics().getHeight()));
 //BA.debugLineNum = 115;BA.debugLine="Batch.Color = sStar.Color";
_batch.setColor(_sstar.Color);
 //BA.debugLineNum = 116;BA.debugLine="Batch.DrawTex2(texSkater, sStar.X, sStar.Y, 64dip, 64dip)";
_batch.DrawTex2(_texskater,_sstar.X,_sstar.Y,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))));
 //BA.debugLineNum = 117;BA.debugLine="Batch.DrawTex2(texPuck, sPuck.X, sPuck.Y + 34dip, 14dip, 10dip)";
_batch.DrawTex2(_texpuck,_spuck.X,(float) (_spuck.Y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (34))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (14))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 119;BA.debugLine="Batch.End";
_batch.End();
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resize(int _width,int _height) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub LG_Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 77;BA.debugLine="Log(\"--- RESIZE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESIZE ---");
 //BA.debugLineNum = 80;BA.debugLine="Camera.Initialize";
_camera.Initialize();
 //BA.debugLineNum = 81;BA.debugLine="Camera.SetToOrtho(False)";
_camera.SetToOrtho(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resume() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub LG_Resume";
 //BA.debugLineNum = 161;BA.debugLine="Log(\"--- RESUME ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESUME ---");
 //BA.debugLineNum = 162;BA.debugLine="playTimer.Enabled = True";
_playtimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 164;BA.debugLine="CreateTextures";
_createtextures();
 //BA.debugLineNum = 166;BA.debugLine="allowPlay = False";
_allowplay = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 167;BA.debugLine="playTimer.Enabled = True";
_playtimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _lg_update() throws Exception{
 //BA.debugLineNum = 123;BA.debugLine="Sub LG_Update";
 //BA.debugLineNum = 125;BA.debugLine="Camera.Update";
_camera.Update();
 //BA.debugLineNum = 128;BA.debugLine="If allowPlay Then";
if (_allowplay) { 
 //BA.debugLineNum = 130;BA.debugLine="If sStar.y > 180dip Then";
if (_sstar.Y>anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180))) { 
 //BA.debugLineNum = 131;BA.debugLine="sStar.x = sStar.x + sStar.dx";
_sstar.X = (float) (_sstar.X+_sstar.dX);
 //BA.debugLineNum = 132;BA.debugLine="sStar.y = sStar.y - sStar.dy";
_sstar.Y = (float) (_sstar.Y-_sstar.dY);
 //BA.debugLineNum = 133;BA.debugLine="sPuck.x = sPuck.x + sPuck.dx";
_spuck.X = (float) (_spuck.X+_spuck.dX);
 //BA.debugLineNum = 134;BA.debugLine="sPuck.y = sPuck.y - sPuck.dy";
_spuck.Y = (float) (_spuck.Y-_spuck.dY);
 }else {
 //BA.debugLineNum = 138;BA.debugLine="sStar.x = sStar.x + sStar.dx * 4";
_sstar.X = (float) (_sstar.X+_sstar.dX*4);
 //BA.debugLineNum = 140;BA.debugLine="sPuck.Y = sPuck.Y - (sPuck.dy * 2)";
_spuck.Y = (float) (_spuck.Y-(_spuck.dY*2));
 };
 //BA.debugLineNum = 143;BA.debugLine="If sStar.X > LW.Graphics.Width Then";
if (_sstar.X>_lw.Graphics().getWidth()) { 
 //BA.debugLineNum = 144;BA.debugLine="sStar.y = LW.Graphics.Height + 64dip";
_sstar.Y = (float) (_lw.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64)));
 //BA.debugLineNum = 145;BA.debugLine="sStar.X = 80dip";
_sstar.X = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 146;BA.debugLine="sPuck.X = sStar.X + 5dip";
_spuck.X = (float) (_sstar.X+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 147;BA.debugLine="sPuck.Y = LW.Graphics.Height + 12dip";
_spuck.Y = (float) (_lw.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12)));
 //BA.debugLineNum = 148;BA.debugLine="allowPlay = False";
_allowplay = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 149;BA.debugLine="playTimer.Enabled = True";
_playtimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static String  _playt_tick() throws Exception{
 //BA.debugLineNum = 181;BA.debugLine="Sub playT_tick";
 //BA.debugLineNum = 183;BA.debugLine="allowPlay = True";
_allowplay = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 186;BA.debugLine="playTimer.Enabled = False";
_playtimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 11;BA.debugLine="Dim texSkater As lgTexture";
_texskater = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 12;BA.debugLine="Dim texPuck As lgTexture";
_texpuck = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 13;BA.debugLine="Dim texBG As lgTexture";
_texbg = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 14;BA.debugLine="Dim lstSmileys As List";
_lstsmileys = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 15;BA.debugLine="Dim sStar As typSmiley";
_sstar = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 16;BA.debugLine="Dim sPuck As typSmiley";
_spuck = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 17;BA.debugLine="Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)";
;
 //BA.debugLineNum = 18;BA.debugLine="Dim playTimer As Timer";
_playtimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 19;BA.debugLine="Dim allowPlay As Boolean";
_allowplay = false;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 33;BA.debugLine="playTimer.Initialize(\"playT\",5000)";
_playtimer.Initialize(processBA,"playT",(long) (5000));
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
}
