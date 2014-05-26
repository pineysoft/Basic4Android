package b4a.SSLiveWallPaper;

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
		    processBA = new BA(this, null, null, "b4a.SSLiveWallPaper", "b4a.SSLiveWallPaper.wallpaperservice");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.SSLiveWallPaper.wallpaperservice", processBA, _service);
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
public static String _imagename = "";
public static anywheresoftware.b4a.objects.WallpaperInternalService.LWManager _lwm = null;
public static anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _liveengine = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _image = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _resized = null;
public static int _x = 0;
public static int _y = 0;
public static int _vx = 0;
public static int _vy = 0;
public static int _degrees = 0;
public static int _boxsize = 0;
public b4a.SSLiveWallPaper.main _main = null;
public static String  _lwm_offsetchanged(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub LWM_OffsetChanged (Engine As LWEngine)";
 //BA.debugLineNum = 38;BA.debugLine="If Engine.ScreenWidth > Engine.ScreenHeight Then";
if (_engine.getScreenWidth()>_engine.getScreenHeight()) { 
 //BA.debugLineNum = 39;BA.debugLine="image = LoadBitmap(File.DirAssets, \"drawboardlandscape_complete.png\")";
_image = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"drawboardlandscape_complete.png");
 }else {
 //BA.debugLineNum = 41;BA.debugLine="image = LoadBitmap(File.DirAssets, \"drawboardportrait_complete.png\")";
_image = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"drawboardportrait_complete.png");
 };
 //BA.debugLineNum = 43;BA.debugLine="resized = ResizeImage(image, Engine.FullWallpaperWidth, Engine.FullWallpaperHeight)";
_resized = _resizeimage(_image,_engine.getFullWallpaperWidth(),_engine.getFullWallpaperHeight());
 //BA.debugLineNum = 44;BA.debugLine="If resized.IsInitialized Then";
if (_resized.IsInitialized()) { 
 //BA.debugLineNum = 45;BA.debugLine="Engine.Rect.Left = -Engine.CurrentOffsetX";
_engine.Rect.setLeft((int) (-_engine.getCurrentOffsetX()));
 //BA.debugLineNum = 46;BA.debugLine="Engine.Rect.Top = -Engine.CurrentOffsetY";
_engine.Rect.setTop((int) (-_engine.getCurrentOffsetY()));
 //BA.debugLineNum = 47;BA.debugLine="Engine.Rect.Right = -Engine.CurrentOffsetX + Engine.FullWallpaperWidth";
_engine.Rect.setRight((int) (-_engine.getCurrentOffsetX()+_engine.getFullWallpaperWidth()));
 //BA.debugLineNum = 48;BA.debugLine="Engine.Rect.Bottom = -Engine.CurrentOffsetY + Engine.FullWallpaperHeight";
_engine.Rect.setBottom((int) (-_engine.getCurrentOffsetY()+_engine.getFullWallpaperHeight()));
 //BA.debugLineNum = 49;BA.debugLine="Engine.Canvas.DrawBitmap(resized, Null, Engine.Rect)";
_engine.getCanvas().DrawBitmap((android.graphics.Bitmap)(_resized.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_engine.Rect.getObject()));
 }else {
 //BA.debugLineNum = 51;BA.debugLine="Engine.Canvas.DrawColor(Colors.Black)";
_engine.getCanvas().DrawColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 52;BA.debugLine="Engine.Canvas.DrawText(\"No image selected\", 120dip, 120dip, Typeface.DEFAULT_BOLD, 30, Colors.White, \"LEFT\")";
_engine.getCanvas().DrawText(processBA,"No image selected",(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (30),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"LEFT"));
 };
 //BA.debugLineNum = 54;BA.debugLine="Engine.RefreshAll";
_engine.RefreshAll();
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_sizechanged(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Sub LWM_SizeChanged (Engine As LWEngine)";
 //BA.debugLineNum = 34;BA.debugLine="If Engine.IsPreview = False Then LiveEngine = Engine";
if (_engine.getIsPreview()==anywheresoftware.b4a.keywords.Common.False) { 
_liveengine = _engine;};
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_tick(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub lwm_Tick (Engine As LWEngine)";
 //BA.debugLineNum = 100;BA.debugLine="If y > 0 Then";
if (_y>0) { 
 //BA.debugLineNum = 101;BA.debugLine="y = y - vy";
_y = (int) (_y-_vy);
 //BA.debugLineNum = 103;BA.debugLine="Engine.Rect.Top = y - 10";
_engine.Rect.setTop((int) (_y-10));
 //BA.debugLineNum = 104;BA.debugLine="Engine.Rect.Left = x - 10";
_engine.Rect.setLeft((int) (_x-10));
 //BA.debugLineNum = 105;BA.debugLine="Engine.Rect.Bottom = y + boxsize";
_engine.Rect.setBottom((int) (_y+_boxsize));
 //BA.debugLineNum = 106;BA.debugLine="Engine.Rect.Right = x + boxsize";
_engine.Rect.setRight((int) (_x+_boxsize));
 //BA.debugLineNum = 108;BA.debugLine="Engine.Canvas.DrawCircle(x,y,10dip, Colors.Black,True, 2dip) 'Draw the new smiley";
_engine.getCanvas().DrawCircle((float) (_x),(float) (_y),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),anywheresoftware.b4a.keywords.Common.Colors.Black,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 109;BA.debugLine="Engine.Refresh(Engine.Rect)";
_engine.Refresh((android.graphics.Rect)(_engine.Rect.getObject()));
 }else {
 //BA.debugLineNum = 111;BA.debugLine="lwm.StopTicking";
_lwm.StopTicking();
 };
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public static String  _lwm_touch(anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine _engine,int _action,float _tx,float _ty) throws Exception{
 //BA.debugLineNum = 81;BA.debugLine="Sub lwm_Touch (Engine As LWEngine, Action As Int, tx As Float, ty As Float)";
 //BA.debugLineNum = 82;BA.debugLine="If Action <> 0 Then Return '0 is the value of Activity.ACTION_DOWN";
if (_action!=0) { 
if (true) return "";};
 //BA.debugLineNum = 83;BA.debugLine="x = tx";
_x = (int) (_tx);
 //BA.debugLineNum = 84;BA.debugLine="y = ty";
_y = (int) (_ty);
 //BA.debugLineNum = 86;BA.debugLine="Engine.Rect.Top = y - 10";
_engine.Rect.setTop((int) (_y-10));
 //BA.debugLineNum = 87;BA.debugLine="Engine.Rect.Left = x - 10";
_engine.Rect.setLeft((int) (_x-10));
 //BA.debugLineNum = 88;BA.debugLine="Engine.Rect.Bottom = y + boxsize";
_engine.Rect.setBottom((int) (_y+_boxsize));
 //BA.debugLineNum = 89;BA.debugLine="Engine.Rect.Right = x + boxsize";
_engine.Rect.setRight((int) (_x+_boxsize));
 //BA.debugLineNum = 91;BA.debugLine="Engine.Canvas.DrawCircle(tx, ty, 10dip, Colors.Black, True, 2dip)";
_engine.getCanvas().DrawCircle(_tx,_ty,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),anywheresoftware.b4a.keywords.Common.Colors.Black,anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 92;BA.debugLine="Engine.Refresh(Engine.Rect)";
_engine.Refresh((android.graphics.Rect)(_engine.Rect.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="lwm.StartTicking(30) 'Start the wallpaper timer (30 milliseconds).";
_lwm.StartTicking((int) (30));
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim imageName As String : imageName = \"drawboardportrait_complete.png\"";
_imagename = "";
 //BA.debugLineNum = 7;BA.debugLine="Dim imageName As String : imageName = \"drawboardportrait_complete.png\"";
_imagename = "drawboardportrait_complete.png";
 //BA.debugLineNum = 8;BA.debugLine="Dim lwm As LWManager";
_lwm = new anywheresoftware.b4a.objects.WallpaperInternalService.LWManager();
 //BA.debugLineNum = 9;BA.debugLine="Dim LiveEngine As LWEngine";
_liveengine = new anywheresoftware.b4a.objects.WallpaperInternalService.LWEngine();
 //BA.debugLineNum = 10;BA.debugLine="Dim image As Bitmap";
_image = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim resized As Bitmap";
_resized = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim x, y As Int";
_x = 0;
_y = 0;
 //BA.debugLineNum = 13;BA.debugLine="Dim vx As Int = 10dip 'puck speed";
_vx = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10));
 //BA.debugLineNum = 14;BA.debugLine="Dim vy As Int = 10dip";
_vy = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10));
 //BA.debugLineNum = 15;BA.debugLine="Dim Degrees As Int";
_degrees = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim boxsize As Int = 80dip";
_boxsize = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80));
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _resizeimage(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _original,int _targetx,int _targety) throws Exception{
float _origratio = 0f;
float _targetratio = 0f;
float _scale = 0f;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _c = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _b = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _r = null;
int _w = 0;
int _h = 0;
 //BA.debugLineNum = 57;BA.debugLine="Sub ResizeImage(original As Bitmap, TargetX As Int, TargetY As Int) As Bitmap";
 //BA.debugLineNum = 58;BA.debugLine="Dim origRatio As Float = original.Width / original.Height";
_origratio = (float) (_original.getWidth()/(double)_original.getHeight());
 //BA.debugLineNum = 59;BA.debugLine="Dim targetRatio As Float = TargetX / TargetY";
_targetratio = (float) (_targetx/(double)_targety);
 //BA.debugLineNum = 60;BA.debugLine="Dim scale As Float";
_scale = 0f;
 //BA.debugLineNum = 62;BA.debugLine="If targetRatio > origRatio Then";
if (_targetratio>_origratio) { 
 //BA.debugLineNum = 63;BA.debugLine="scale = TargetY / original.Height";
_scale = (float) (_targety/(double)_original.getHeight());
 }else {
 //BA.debugLineNum = 65;BA.debugLine="scale = TargetX / original.Width";
_scale = (float) (_targetx/(double)_original.getWidth());
 };
 //BA.debugLineNum = 68;BA.debugLine="Dim c As Canvas";
_c = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim b As Bitmap";
_b = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 70;BA.debugLine="b.InitializeMutable(TargetX, TargetY)";
_b.InitializeMutable(_targetx,_targety);
 //BA.debugLineNum = 71;BA.debugLine="c.Initialize2(b)";
_c.Initialize2((android.graphics.Bitmap)(_b.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="c.DrawColor(Colors.LightGray)";
_c.DrawColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 74;BA.debugLine="Dim r As Rect";
_r = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim w = original.Width * scale, h = original.Height * scale As Int";
_w = (int) (_original.getWidth()*_scale);
_h = (int) (_original.getHeight()*_scale);
 //BA.debugLineNum = 76;BA.debugLine="r.Initialize(TargetX / 2 - w / 2, TargetY / 2 - h / 2, TargetX / 2 + w / 2, TargetY / 2+ h / 2)";
_r.Initialize((int) (_targetx/(double)2-_w/(double)2),(int) (_targety/(double)2-_h/(double)2),(int) (_targetx/(double)2+_w/(double)2),(int) (_targety/(double)2+_h/(double)2));
 //BA.debugLineNum = 77;BA.debugLine="c.DrawBitmap(original, Null, r)";
_c.DrawBitmap((android.graphics.Bitmap)(_original.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_r.getObject()));
 //BA.debugLineNum = 78;BA.debugLine="Return b";
if (true) return _b;
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return null;
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 19;BA.debugLine="lwm.Initialize(\"lwm\", True)";
_lwm.Initialize("lwm",anywheresoftware.b4a.keywords.Common.True,processBA);
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
}
