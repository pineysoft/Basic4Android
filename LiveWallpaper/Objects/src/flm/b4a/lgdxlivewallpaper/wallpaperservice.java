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
public static anywheresoftware.b4a.libgdx.lgLiveWallpaper.LW_Manager _v5 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgGL _v6 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgSpriteBatch _v7 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgOrthographicCamera _v0 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _vv1 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _vv2 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _vv3 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _vv4 = null;
public static anywheresoftware.b4a.libgdx.graphics.lgTexture _vv5 = null;
public static anywheresoftware.b4a.objects.collections.List _vv6 = null;
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _vv7 = null;
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley _vv0 = null;
public static anywheresoftware.b4a.objects.Timer _vvv1 = null;
public static boolean _vvv2 = false;
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typpoint _vvv3 = null;
public flm.b4a.lgdxlivewallpaper.main _vvv7 = null;
public static class _typpoint{
public boolean IsInitialized;
public float x;
public float y;
public void Initialize() {
IsInitialized = true;
x = 0f;
y = 0f;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
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
public static String  _vvv5() throws Exception{
 //BA.debugLineNum = 81;BA.debugLine="Sub CreateTextures";
 //BA.debugLineNum = 83;BA.debugLine="lstSmileys.Initialize";
_vv6.Initialize();
 //BA.debugLineNum = 84;BA.debugLine="sStar.Initialize";
_vv7.Initialize();
 //BA.debugLineNum = 85;BA.debugLine="sPuck.Initialize";
_vv0.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="shootingPoint = GetShootingPosition";
_vvv3 = _vvv6();
 //BA.debugLineNum = 88;BA.debugLine="If texBG = texBGP Then";
if ((_vv5).equals(_vv3)) { 
 //BA.debugLineNum = 89;BA.debugLine="sStar.X = 80dip";
_vv7.X = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 90;BA.debugLine="sStar.Y = LW.Graphics.Height + 64dip";
_vv7.Y = (float) (_v5.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64)));
 //BA.debugLineNum = 91;BA.debugLine="sStar.dY = 5dip";
_vv7.dY = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 92;BA.debugLine="Log(\"Sstar.dx = (\" & shootingPoint.x & \" - \" & sStar.X & \") / ((\" & shootingPoint.y & \" - \" & sStar.Y & \") / \" & sStar.dY)";
anywheresoftware.b4a.keywords.Common.Log("Sstar.dx = ("+BA.NumberToString(_vvv3.x)+" - "+BA.NumberToString(_vv7.X)+") / (("+BA.NumberToString(_vvv3.y)+" - "+BA.NumberToString(_vv7.Y)+") / "+BA.NumberToString(_vv7.dY));
 //BA.debugLineNum = 93;BA.debugLine="sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)";
_vv7.dX = (float) ((_vvv3.x-_vv7.X)/(double)((_vv7.Y-_vvv3.y)/(double)_vv7.dY));
 //BA.debugLineNum = 94;BA.debugLine="sStar.Color.setRGBA(1, 1, 1, .75)";
_vv7.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 //BA.debugLineNum = 96;BA.debugLine="sPuck.X = sStar.X + 5dip";
_vv0.X = (float) (_vv7.X+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 97;BA.debugLine="sPuck.Y = LW.Graphics.Height + 12dip";
_vv0.Y = (float) (_v5.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12)));
 //BA.debugLineNum = 98;BA.debugLine="sPuck.dX = sStar.dX";
_vv0.dX = _vv7.dX;
 //BA.debugLineNum = 99;BA.debugLine="sPuck.dY = sStar.dY";
_vv0.dY = _vv7.dY;
 //BA.debugLineNum = 100;BA.debugLine="sPuck.Color.setRGBA(1, 1, 1, .75)";
_vv0.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 }else {
 //BA.debugLineNum = 102;BA.debugLine="sStar.X = 0";
_vv7.X = (float) (0);
 //BA.debugLineNum = 103;BA.debugLine="sStar.Y = LW.Graphics.Height";
_vv7.Y = (float) (_v5.Graphics().getHeight());
 //BA.debugLineNum = 104;BA.debugLine="sStar.dX = 5dip";
_vv7.dX = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 105;BA.debugLine="sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)";
_vv7.dY = (float) ((_vvv3.y-_vv7.Y)/(double)((_vv7.X-_vvv3.x)/(double)_vv7.dX));
 //BA.debugLineNum = 106;BA.debugLine="sStar.Color.setRGBA(1, 1, 1, .75)";
_vv7.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 //BA.debugLineNum = 108;BA.debugLine="sPuck.X = sStar.X";
_vv0.X = _vv7.X;
 //BA.debugLineNum = 109;BA.debugLine="sPuck.Y = LW.Graphics.Height - 32dip";
_vv0.Y = (float) (_v5.Graphics().getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 110;BA.debugLine="sPuck.dX = sStar.dx";
_vv0.dX = _vv7.dX;
 //BA.debugLineNum = 111;BA.debugLine="sPuck.dY = sStar.dY";
_vv0.dY = _vv7.dY;
 //BA.debugLineNum = 112;BA.debugLine="sPuck.Color.setRGBA(1, 1, 1, .75)";
_vv0.Color.setRGBA((float) (1),(float) (1),(float) (1),(float) (.75));
 };
 //BA.debugLineNum = 114;BA.debugLine="Log(\"X Movement is \" & sStar.dX)";
anywheresoftware.b4a.keywords.Common.Log("X Movement is "+BA.NumberToString(_vv7.dX));
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static flm.b4a.lgdxlivewallpaper.wallpaperservice._typpoint  _vvv6() throws Exception{
flm.b4a.lgdxlivewallpaper.wallpaperservice._typpoint _newpoint = null;
 //BA.debugLineNum = 31;BA.debugLine="Sub GetShootingPosition() As typPoint";
 //BA.debugLineNum = 32;BA.debugLine="Dim newPoint As typPoint";
_newpoint = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typpoint();
 //BA.debugLineNum = 33;BA.debugLine="newPoint.Initialize";
_newpoint.Initialize();
 //BA.debugLineNum = 34;BA.debugLine="If texBG = texBGP Then";
if ((_vv5).equals(_vv3)) { 
 //BA.debugLineNum = 35;BA.debugLine="Log(\"Shooting Poistion for Portrait\")";
anywheresoftware.b4a.keywords.Common.Log("Shooting Poistion for Portrait");
 //BA.debugLineNum = 36;BA.debugLine="newPoint.x = LW.Graphics.Width / 2";
_newpoint.x = (float) (_v5.Graphics().getWidth()/(double)2);
 //BA.debugLineNum = 37;BA.debugLine="newPoint.y = LW.Graphics.Height * .2";
_newpoint.y = (float) (_v5.Graphics().getHeight()*.2);
 }else {
 //BA.debugLineNum = 39;BA.debugLine="newPoint.x = LW.Graphics.Width * .7";
_newpoint.x = (float) (_v5.Graphics().getWidth()*.7);
 //BA.debugLineNum = 40;BA.debugLine="newPoint.y = LW.Graphics.Height / 2";
_newpoint.y = (float) (_v5.Graphics().getHeight()/(double)2);
 };
 //BA.debugLineNum = 43;BA.debugLine="Return newPoint";
if (true) return _newpoint;
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return null;
}
public static String  _lg_create() throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub LG_Create";
 //BA.debugLineNum = 61;BA.debugLine="Log(\"--- CREATE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- CREATE ---");
 //BA.debugLineNum = 64;BA.debugLine="Batch.Initialize";
_v7.Initialize();
 //BA.debugLineNum = 68;BA.debugLine="texSkater.Initialize(\"skater.png\")";
_vv1.Initialize("skater.png");
 //BA.debugLineNum = 69;BA.debugLine="texPuck.Initialize(\"puck.png\")";
_vv2.Initialize("puck.png");
 //BA.debugLineNum = 70;BA.debugLine="texBGP.Initialize(\"hockeyboardportrait_stars.png\")";
_vv3.Initialize("hockeyboardportrait_stars.png");
 //BA.debugLineNum = 71;BA.debugLine="texBGL.Initialize(\"hockeyboardlandscape_stars.png\")";
_vv4.Initialize("hockeyboardlandscape_stars.png");
 //BA.debugLineNum = 74;BA.debugLine="texBG = texBGP";
_vv5 = _vv3;
 //BA.debugLineNum = 77;BA.debugLine="sStar.Initialize";
_vv7.Initialize();
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _lg_dispose() throws Exception{
 //BA.debugLineNum = 271;BA.debugLine="Sub LG_Dispose";
 //BA.debugLineNum = 272;BA.debugLine="Log(\"--- DISPOSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- DISPOSE ---");
 //BA.debugLineNum = 275;BA.debugLine="texSkater.dispose";
_vv1.dispose();
 //BA.debugLineNum = 276;BA.debugLine="texPuck.dispose";
_vv2.dispose();
 //BA.debugLineNum = 277;BA.debugLine="texBG.dispose";
_vv5.dispose();
 //BA.debugLineNum = 278;BA.debugLine="texBGL.dispose";
_vv4.dispose();
 //BA.debugLineNum = 279;BA.debugLine="texBGP.dispose";
_vv3.dispose();
 //BA.debugLineNum = 280;BA.debugLine="Batch.dispose";
_v7.dispose();
 //BA.debugLineNum = 281;BA.debugLine="End Sub";
return "";
}
public static String  _lg_offsetchange(float _x_offset,float _y_offset,float _x_offsetstep,float _y_offsetstep,int _x_pixeloffset,int _y_pixeloffset) throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)";
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _lg_pause() throws Exception{
 //BA.debugLineNum = 254;BA.debugLine="Sub LG_Pause";
 //BA.debugLineNum = 255;BA.debugLine="playTimer.Enabled = False";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 256;BA.debugLine="allowPlay = False";
_vvv2 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 257;BA.debugLine="Log(\"--- PAUSE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- PAUSE ---");
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
return "";
}
public static String  _lg_previewstatechange(boolean _ispreview) throws Exception{
 //BA.debugLineNum = 141;BA.debugLine="Sub LG_PreviewStateChange(IsPreview As Boolean)";
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _lg_render() throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub LG_Render";
 //BA.debugLineNum = 147;BA.debugLine="GL.glClearColor(0, 0, 0, 1)";
_v6.glClearColor((float) (0),(float) (0),(float) (0),(float) (1));
 //BA.debugLineNum = 148;BA.debugLine="GL.glClear(GL.GL10_COLOR_BUFFER_BIT)";
_v6.glClear(_v6.GL10_COLOR_BUFFER_BIT);
 //BA.debugLineNum = 151;BA.debugLine="If LW.Input.JustTouched Then";
if (_v5.Input().justTouched()) { 
 //BA.debugLineNum = 152;BA.debugLine="sStar.X = Abs(LW.Input.X)";
_vv7.X = (float) (anywheresoftware.b4a.keywords.Common.Abs(_v5.Input().getX()));
 //BA.debugLineNum = 153;BA.debugLine="sStar.Y = LW.Graphics.Height - Abs(LW.Input.Y)";
_vv7.Y = (float) (_v5.Graphics().getHeight()-anywheresoftware.b4a.keywords.Common.Abs(_v5.Input().getY()));
 //BA.debugLineNum = 154;BA.debugLine="sPuck.X = sStar.X - 5dip";
_vv0.X = (float) (_vv7.X-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 155;BA.debugLine="sPuck.Y =  LW.Graphics.Height - Abs(LW.Input.Y) - 48dip";
_vv0.Y = (float) (_v5.Graphics().getHeight()-anywheresoftware.b4a.keywords.Common.Abs(_v5.Input().getY())-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48)));
 //BA.debugLineNum = 156;BA.debugLine="If texBG = texBGP Then";
if ((_vv5).equals(_vv3)) { 
 //BA.debugLineNum = 157;BA.debugLine="sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)";
_vv7.dX = (float) ((_vvv3.x-_vv7.X)/(double)((_vv7.Y-_vvv3.y)/(double)_vv7.dY));
 //BA.debugLineNum = 158;BA.debugLine="sPuck.dX = sStar.dX";
_vv0.dX = _vv7.dX;
 }else {
 //BA.debugLineNum = 160;BA.debugLine="sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)";
_vv7.dY = (float) ((_vvv3.y-_vv7.Y)/(double)((_vv7.X-_vvv3.x)/(double)_vv7.dX));
 //BA.debugLineNum = 161;BA.debugLine="sPuck.dY = sStar.dY";
_vv0.dY = _vv7.dY;
 };
 //BA.debugLineNum = 163;BA.debugLine="allowPlay = True";
_vvv2 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 164;BA.debugLine="playTimer.Enabled = False";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 168;BA.debugLine="LG_Update";
_lg_update();
 //BA.debugLineNum = 171;BA.debugLine="Batch.ProjectionMatrix = Camera.Combined";
_v7.setProjectionMatrix(_v0.getCombined());
 //BA.debugLineNum = 174;BA.debugLine="Batch.Begin";
_v7.Begin();
 //BA.debugLineNum = 176;BA.debugLine="Batch.DrawTex2(texBG, 0,0,LW.Graphics.Width, LW.Graphics.Height)";
_v7.DrawTex2(_vv5,(float) (0),(float) (0),(float) (_v5.Graphics().getWidth()),(float) (_v5.Graphics().getHeight()));
 //BA.debugLineNum = 178;BA.debugLine="Batch.DrawTex2(texSkater, sStar.X, sStar.Y, 64dip, 64dip)";
_v7.DrawTex2(_vv1,_vv7.X,_vv7.Y,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64))));
 //BA.debugLineNum = 179;BA.debugLine="Batch.DrawTex2(texPuck, sPuck.X, sPuck.Y + 34dip, 14dip, 10dip)";
_v7.DrawTex2(_vv2,_vv0.X,(float) (_vv0.Y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (34))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (14))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 181;BA.debugLine="Batch.End";
_v7.End();
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resize(int _width,int _height) throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub LG_Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 118;BA.debugLine="Log(\"--- RESIZE ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESIZE ---");
 //BA.debugLineNum = 121;BA.debugLine="Camera.Initialize";
_v0.Initialize();
 //BA.debugLineNum = 122;BA.debugLine="Camera.SetToOrtho(False)";
_v0.SetToOrtho(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 124;BA.debugLine="If LW.Graphics.Width > LW.Graphics.Height Then";
if (_v5.Graphics().getWidth()>_v5.Graphics().getHeight()) { 
 //BA.debugLineNum = 125;BA.debugLine="If texBG = texBGP Then";
if ((_vv5).equals(_vv3)) { 
 //BA.debugLineNum = 126;BA.debugLine="texBG = texBGL";
_vv5 = _vv4;
 };
 }else {
 //BA.debugLineNum = 129;BA.debugLine="If texBG = texBGL Then";
if ((_vv5).equals(_vv4)) { 
 //BA.debugLineNum = 130;BA.debugLine="texBG = texBGP";
_vv5 = _vv3;
 };
 };
 //BA.debugLineNum = 134;BA.debugLine="shootingPoint = GetShootingPosition";
_vvv3 = _vvv6();
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _lg_resume() throws Exception{
 //BA.debugLineNum = 260;BA.debugLine="Sub LG_Resume";
 //BA.debugLineNum = 261;BA.debugLine="Log(\"--- RESUME ---\")";
anywheresoftware.b4a.keywords.Common.Log("--- RESUME ---");
 //BA.debugLineNum = 262;BA.debugLine="playTimer.Enabled = True";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 263;BA.debugLine="allowPlay = False";
_vvv2 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 264;BA.debugLine="CreateTextures";
_vvv5();
 //BA.debugLineNum = 266;BA.debugLine="allowPlay = False";
_vvv2 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 267;BA.debugLine="playTimer.Enabled = True";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 269;BA.debugLine="End Sub";
return "";
}
public static String  _lg_update() throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub LG_Update";
 //BA.debugLineNum = 187;BA.debugLine="Camera.Update";
_v0.Update();
 //BA.debugLineNum = 190;BA.debugLine="If allowPlay Then";
if (_vvv2) { 
 //BA.debugLineNum = 191;BA.debugLine="If texBG = texBGP Then";
if ((_vv5).equals(_vv3)) { 
 //BA.debugLineNum = 193;BA.debugLine="If sStar.y > shootingPoint.y Then";
if (_vv7.Y>_vvv3.y) { 
 //BA.debugLineNum = 194;BA.debugLine="sStar.x = sStar.x + sStar.dx";
_vv7.X = (float) (_vv7.X+_vv7.dX);
 //BA.debugLineNum = 195;BA.debugLine="sStar.y = sStar.y - sStar.dy";
_vv7.Y = (float) (_vv7.Y-_vv7.dY);
 //BA.debugLineNum = 196;BA.debugLine="sPuck.x = sPuck.x + sPuck.dx";
_vv0.X = (float) (_vv0.X+_vv0.dX);
 //BA.debugLineNum = 197;BA.debugLine="sPuck.y = sPuck.y - sPuck.dy";
_vv0.Y = (float) (_vv0.Y-_vv0.dY);
 }else {
 //BA.debugLineNum = 201;BA.debugLine="If sStar.dX > 0 Then";
if (_vv7.dX>0) { 
 //BA.debugLineNum = 202;BA.debugLine="sStar.x = sStar.x + 6";
_vv7.X = (float) (_vv7.X+6);
 }else {
 //BA.debugLineNum = 204;BA.debugLine="sStar.x = sStar.x - 6";
_vv7.X = (float) (_vv7.X-6);
 };
 //BA.debugLineNum = 207;BA.debugLine="sPuck.Y = sPuck.Y - (sPuck.dy * 2)";
_vv0.Y = (float) (_vv0.Y-(_vv0.dY*2));
 };
 //BA.debugLineNum = 210;BA.debugLine="If sStar.X > LW.Graphics.Width OR sStar.X < 0 Then";
if (_vv7.X>_v5.Graphics().getWidth() || _vv7.X<0) { 
 //BA.debugLineNum = 211;BA.debugLine="sStar.X = 80dip";
_vv7.X = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 212;BA.debugLine="sStar.Y = LW.Graphics.Height + 64dip";
_vv7.Y = (float) (_v5.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (64)));
 //BA.debugLineNum = 213;BA.debugLine="sPuck.X = sStar.X + 5dip";
_vv0.X = (float) (_vv7.X+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 214;BA.debugLine="sPuck.Y = LW.Graphics.Height + 12dip";
_vv0.Y = (float) (_v5.Graphics().getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12)));
 //BA.debugLineNum = 215;BA.debugLine="sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)";
_vv7.dX = (float) ((_vvv3.x-_vv7.X)/(double)((_vv7.Y-_vvv3.y)/(double)_vv7.dY));
 //BA.debugLineNum = 216;BA.debugLine="sPuck.dX = sStar.dX";
_vv0.dX = _vv7.dX;
 //BA.debugLineNum = 217;BA.debugLine="allowPlay = False";
_vvv2 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 218;BA.debugLine="playTimer.Enabled = True";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 222;BA.debugLine="If sStar.x < shootingPoint.x Then";
if (_vv7.X<_vvv3.x) { 
 //BA.debugLineNum = 223;BA.debugLine="sStar.x = sStar.x + sStar.dx";
_vv7.X = (float) (_vv7.X+_vv7.dX);
 //BA.debugLineNum = 224;BA.debugLine="sStar.y = sStar.y - sStar.dy";
_vv7.Y = (float) (_vv7.Y-_vv7.dY);
 //BA.debugLineNum = 225;BA.debugLine="sPuck.x = sPuck.x + sPuck.dx";
_vv0.X = (float) (_vv0.X+_vv0.dX);
 //BA.debugLineNum = 226;BA.debugLine="sPuck.y = sPuck.y - sPuck.dy";
_vv0.Y = (float) (_vv0.Y-_vv0.dY);
 }else {
 //BA.debugLineNum = 230;BA.debugLine="If sStar.dY > 0 Then";
if (_vv7.dY>0) { 
 //BA.debugLineNum = 231;BA.debugLine="sStar.Y = sStar.Y + 6";
_vv7.Y = (float) (_vv7.Y+6);
 }else {
 //BA.debugLineNum = 233;BA.debugLine="sStar.Y = sStar.Y -6";
_vv7.Y = (float) (_vv7.Y-6);
 };
 //BA.debugLineNum = 236;BA.debugLine="sPuck.X = sPuck.X + (sPuck.dX * 2)";
_vv0.X = (float) (_vv0.X+(_vv0.dX*2));
 };
 //BA.debugLineNum = 239;BA.debugLine="If sStar.Y < 0 OR sStar.Y > LW.Graphics.Height Then";
if (_vv7.Y<0 || _vv7.Y>_v5.Graphics().getHeight()) { 
 //BA.debugLineNum = 240;BA.debugLine="sStar.y = LW.Graphics.Height";
_vv7.Y = (float) (_v5.Graphics().getHeight());
 //BA.debugLineNum = 241;BA.debugLine="sStar.X = 0dip";
_vv7.X = (float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)));
 //BA.debugLineNum = 242;BA.debugLine="sPuck.X = sStar.X + 5dip";
_vv0.X = (float) (_vv7.X+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 243;BA.debugLine="sPuck.Y = LW.Graphics.Height - 48dip";
_vv0.Y = (float) (_v5.Graphics().getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48)));
 //BA.debugLineNum = 244;BA.debugLine="sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)";
_vv7.dY = (float) ((_vvv3.y-_vv7.Y)/(double)((_vv7.X-_vvv3.x)/(double)_vv7.dX));
 //BA.debugLineNum = 245;BA.debugLine="sPuck.dY = sStar.dy";
_vv0.dY = _vv7.dY;
 //BA.debugLineNum = 246;BA.debugLine="allowPlay = False";
_vvv2 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 247;BA.debugLine="playTimer.Enabled = True";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _playt_tick() throws Exception{
 //BA.debugLineNum = 283;BA.debugLine="Sub playT_tick";
 //BA.debugLineNum = 285;BA.debugLine="allowPlay = True";
_vvv2 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 288;BA.debugLine="playTimer.Enabled = False";
_vvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 289;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim LW As lgLiveWallpaper";
_v5 = new anywheresoftware.b4a.libgdx.lgLiveWallpaper.LW_Manager();
 //BA.debugLineNum = 8;BA.debugLine="Dim GL As lgGL";
_v6 = new anywheresoftware.b4a.libgdx.graphics.lgGL();
 //BA.debugLineNum = 9;BA.debugLine="Dim Batch As lgSpriteBatch";
_v7 = new anywheresoftware.b4a.libgdx.graphics.lgSpriteBatch();
 //BA.debugLineNum = 10;BA.debugLine="Dim Camera As lgOrthographicCamera";
_v0 = new anywheresoftware.b4a.libgdx.graphics.lgOrthographicCamera();
 //BA.debugLineNum = 11;BA.debugLine="Dim texSkater As lgTexture";
_vv1 = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 12;BA.debugLine="Dim texPuck As lgTexture";
_vv2 = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 13;BA.debugLine="Dim texBGP As lgTexture";
_vv3 = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 14;BA.debugLine="Dim texBGL As lgTexture";
_vv4 = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 15;BA.debugLine="Dim texBG As lgTexture";
_vv5 = new anywheresoftware.b4a.libgdx.graphics.lgTexture();
 //BA.debugLineNum = 16;BA.debugLine="Dim lstSmileys As List";
_vv6 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Dim sStar As typSmiley";
_vv7 = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 18;BA.debugLine="Dim sPuck As typSmiley";
_vv0 = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typsmiley();
 //BA.debugLineNum = 19;BA.debugLine="Type typPoint(x As Float, y As Float)";
;
 //BA.debugLineNum = 20;BA.debugLine="Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)";
;
 //BA.debugLineNum = 21;BA.debugLine="Dim playTimer As Timer";
_vvv1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 22;BA.debugLine="Dim allowPlay As Boolean";
_vvv2 = false;
 //BA.debugLineNum = 23;BA.debugLine="Dim shootingPoint As typPoint";
_vvv3 = new flm.b4a.lgdxlivewallpaper.wallpaperservice._typpoint();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
com.badlogic.gdx.backends.android.AndroidApplicationConfiguration _config = null;
 //BA.debugLineNum = 45;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 47;BA.debugLine="Dim Config As lgConfiguration";
_config = new com.badlogic.gdx.backends.android.AndroidApplicationConfiguration();
 //BA.debugLineNum = 48;BA.debugLine="Config.useAccelerometer = False";
_config.useAccelerometer = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 49;BA.debugLine="Config.useCompass = False";
_config.useCompass = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 52;BA.debugLine="LW.Initialize(Config, True, \"LG\")";
_v5.Initialize(processBA,_config,anywheresoftware.b4a.keywords.Common.True,"LG");
 //BA.debugLineNum = 53;BA.debugLine="playTimer.Initialize(\"playT\",5000)";
_vvv1.Initialize(processBA,"playT",(long) (5000));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
}
