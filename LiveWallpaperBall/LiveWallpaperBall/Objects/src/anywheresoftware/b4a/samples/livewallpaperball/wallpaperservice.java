package anywheresoftware.b4a.samples.livewallpaperball;

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
		    processBA = new BA(this, null, null, "anywheresoftware.b4a.samples.livewallpaperball", "anywheresoftware.b4a.samples.livewallpaperball.wallpaperservice");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "anywheresoftware.b4a.samples.livewallpaperball.wallpaperservice", processBA, _service);
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
public static anywheresoftware.b4a.objects.WallpaperInternalService.LWManager _lwm = null;
public static int _x = 0;
public static int _y = 0;
public static int _vx = 0;
public static int _vy = 0;
public static boolean _initialized = false;
public static int _boxsize = 0;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _smileybitmap = null;
public static int _backgroundcolor = 0;
public static int _degrees = 0;
public anywheresoftware.b4a.samples.livewallpaperball.main _main = null;
public static String  _drawbackground(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub DrawBackground (Engine As LWEngine)";
 //BA.debugLineNum = 43;BA.debugLine="Engine.Canvas.DrawColor(backgroundColor)";
_engine.getCanvas().DrawColor(_backgroundcolor);
 //BA.debugLineNum = 44;BA.debugLine="Engine.RefreshAll";
_engine.RefreshAll();
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_sizechanged(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub lwm_SizeChanged (Engine As LWEngine)";
 //BA.debugLineNum = 35;BA.debugLine="If initialized = False Then";
if (_initialized==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 36;BA.debugLine="initialized = True";
_initialized = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 37;BA.debugLine="x = Engine.ScreenWidth / 2";
_x = (int) (_engine.getScreenWidth()/(double)2);
 //BA.debugLineNum = 38;BA.debugLine="y = Engine.ScreenHeight / 2";
_y = (int) (_engine.getScreenHeight()/(double)2);
 };
 //BA.debugLineNum = 40;BA.debugLine="DrawBackground (Engine)";
_drawbackground(_engine);
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_tick(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub lwm_Tick (Engine As LWEngine)";
 //BA.debugLineNum = 52;BA.debugLine="If x > Engine.ScreenWidth Then";
if (_x>_engine.getScreenWidth()) { 
 //BA.debugLineNum = 53;BA.debugLine="vx = -1 * Abs(vx)";
_vx = (int) (-1*anywheresoftware.b4a.keywords.Common.Abs(_vx));
 }else if(_x<_boxsize) { 
 //BA.debugLineNum = 55;BA.debugLine="vx = Abs(vx)";
_vx = (int) (anywheresoftware.b4a.keywords.Common.Abs(_vx));
 };
 //BA.debugLineNum = 57;BA.debugLine="If y  + boxSize > Engine.ScreenHeight Then";
if (_y+_boxsize>_engine.getScreenHeight()) { 
 //BA.debugLineNum = 58;BA.debugLine="vy = -1 * Abs(vy)";
_vy = (int) (-1*anywheresoftware.b4a.keywords.Common.Abs(_vy));
 }else if(_y<_boxsize) { 
 //BA.debugLineNum = 60;BA.debugLine="vy = Abs(vy)";
_vy = (int) (anywheresoftware.b4a.keywords.Common.Abs(_vy));
 };
 //BA.debugLineNum = 62;BA.debugLine="Engine.Rect.Top = y";
_engine.Rect.setTop(_y);
 //BA.debugLineNum = 63;BA.debugLine="Engine.Rect.Left = x";
_engine.Rect.setLeft(_x);
 //BA.debugLineNum = 64;BA.debugLine="Engine.Rect.Bottom = y + boxSize";
_engine.Rect.setBottom((int) (_y+_boxsize));
 //BA.debugLineNum = 65;BA.debugLine="Engine.Rect.Right = x + boxSize";
_engine.Rect.setRight((int) (_x+_boxsize));
 //BA.debugLineNum = 66;BA.debugLine="Engine.Canvas.DrawRect(Engine.Rect, backgroundColor, True, 1) 'Erase the previous smiley";
_engine.getCanvas().DrawRect((android.graphics.Rect)(_engine.Rect.getObject()),_backgroundcolor,anywheresoftware.b4a.keywords.Common.True,(float) (1));
 //BA.debugLineNum = 67;BA.debugLine="Engine.Refresh(Engine.Rect)";
_engine.Refresh((android.graphics.Rect)(_engine.Rect.getObject()));
 //BA.debugLineNum = 69;BA.debugLine="x = x + vx";
_x = (int) (_x+_vx);
 //BA.debugLineNum = 70;BA.debugLine="y = y + vy";
_y = (int) (_y+_vy);
 //BA.debugLineNum = 71;BA.debugLine="Engine.Rect.Top = y";
_engine.Rect.setTop(_y);
 //BA.debugLineNum = 72;BA.debugLine="Engine.Rect.Left = x";
_engine.Rect.setLeft(_x);
 //BA.debugLineNum = 73;BA.debugLine="Engine.Rect.Bottom = y + boxSize";
_engine.Rect.setBottom((int) (_y+_boxsize));
 //BA.debugLineNum = 74;BA.debugLine="Engine.Rect.Right = x + boxSize";
_engine.Rect.setRight((int) (_x+_boxsize));
 //BA.debugLineNum = 75;BA.debugLine="Degrees = (Degrees + 10) Mod 360";
_degrees = (int) ((_degrees+10)%360);
 //BA.debugLineNum = 76;BA.debugLine="Engine.Canvas.DrawBitmapRotated(smileyBitmap, Null, Engine.Rect, Degrees) 'Draw the new smiley";
_engine.getCanvas().DrawBitmapRotated((android.graphics.Bitmap)(_smileybitmap.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_engine.Rect.getObject()),(float) (_degrees));
 //BA.debugLineNum = 77;BA.debugLine="Engine.Refresh(Engine.Rect)";
_engine.Refresh((android.graphics.Rect)(_engine.Rect.getObject()));
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_touch(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine,int _action,float _tx,float _ty) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub lwm_Touch (Engine As LWEngine, Action As Int, tx As Float, ty As Float)";
 //BA.debugLineNum = 81;BA.debugLine="If Action <> 0 Then Return '0 is the value of Activity.ACTION_DOWN";
if (_action!=0) { 
if (true) return "";};
 //BA.debugLineNum = 82;BA.debugLine="If tx > x - boxSize AND tx < x + boxSize AND _ 		ty > y - boxSize AND ty < y + boxSize Then";
if (_tx>_x-_boxsize && _tx<_x+_boxsize && _ty>_y-_boxsize && _ty<_y+_boxsize) { 
 //BA.debugLineNum = 84;BA.debugLine="vx = -vx 'change the smiley direction if the user pressed on it";
_vx = (int) (-_vx);
 //BA.debugLineNum = 85;BA.debugLine="vy = -vy";
_vy = (int) (-_vy);
 };
 //BA.debugLineNum = 88;BA.debugLine="Engine.Canvas.DrawCircle(tx, ty, 5dip, Colors.Red, False, 2dip)";
_engine.getCanvas().DrawCircle(_tx,_ty,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.False,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 89;BA.debugLine="Engine.Rect.Left = tx - 7dip";
_engine.Rect.setLeft((int) (_tx-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))));
 //BA.debugLineNum = 90;BA.debugLine="Engine.Rect.Top = ty - 7dip";
_engine.Rect.setTop((int) (_ty-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))));
 //BA.debugLineNum = 91;BA.debugLine="Engine.Rect.Right = tx + 7dip";
_engine.Rect.setRight((int) (_tx+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))));
 //BA.debugLineNum = 92;BA.debugLine="Engine.Rect.Bottom = ty + 7dip";
_engine.Rect.setBottom((int) (_ty+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (7))));
 //BA.debugLineNum = 93;BA.debugLine="Engine.Refresh(Engine.Rect)";
_engine.Refresh((android.graphics.Rect)(_engine.Rect.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_visibilitychanged(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine,boolean _visible) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub lwm_VisibilityChanged (Engine As LWEngine, Visible As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="If Visible Then";
if (_visible) { 
 //BA.debugLineNum = 48;BA.debugLine="DrawBackground (Engine)";
_drawbackground(_engine);
 };
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim lwm As LWManager";
_lwm = new anywheresoftware.b4a.objects.WallpaperInternalService.LWManager();
 //BA.debugLineNum = 8;BA.debugLine="Dim x, y, vx, vy As Int";
_x = 0;
_y = 0;
_vx = 0;
_vy = 0;
 //BA.debugLineNum = 9;BA.debugLine="Dim initialized As Boolean: initialized = False";
_initialized = false;
 //BA.debugLineNum = 9;BA.debugLine="Dim initialized As Boolean: initialized = False";
_initialized = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 10;BA.debugLine="Dim boxSize As Int";
_boxsize = 0;
 //BA.debugLineNum = 11;BA.debugLine="Dim smileyBitmap As Bitmap";
_smileybitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim backgroundColor As Int";
_backgroundcolor = 0;
 //BA.debugLineNum = 13;BA.debugLine="backgroundColor = 0xff79CDCD";
_backgroundcolor = (int) (0xff79cdcd);
 //BA.debugLineNum = 14;BA.debugLine="Dim Degrees As Int";
_degrees = 0;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 17;BA.debugLine="lwm.Initialize(\"lwm\", True)";
_lwm.Initialize("lwm",anywheresoftware.b4a.keywords.Common.True,processBA);
 //BA.debugLineNum = 18;BA.debugLine="lwm.StartTicking(30) 'Start the wallpaper timer (30 milliseconds).";
_lwm.StartTicking((int) (30));
 //BA.debugLineNum = 19;BA.debugLine="vx = 10dip 'smiley speed";
_vx = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10));
 //BA.debugLineNum = 20;BA.debugLine="vy = 10dip";
_vy = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10));
 //BA.debugLineNum = 21;BA.debugLine="boxSize = 40dip";
_boxsize = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40));
 //BA.debugLineNum = 22;BA.debugLine="smileyBitmap.Initialize(File.DirAssets, \"smiley.gif\")";
_smileybitmap.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"smiley.gif");
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
}
