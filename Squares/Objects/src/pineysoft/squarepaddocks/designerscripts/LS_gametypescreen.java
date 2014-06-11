package pineysoft.squarepaddocks.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_gametypescreen{

public static void LS_320x480_1(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="btnStartGame.Bottom = 100%y - 5dip"[gametypescreen/320x480,scale=1]
views.get("btnstartgame").vw.setTop((int)((100d / 100 * height)-(5d * scale) - (views.get("btnstartgame").vw.getHeight())));
//BA.debugLineNum = 3;BA.debugLine="txtMessage.Bottom = btnStartGame.Top - 5dip"[gametypescreen/320x480,scale=1]
views.get("txtmessage").vw.setTop((int)((views.get("btnstartgame").vw.getTop())-(5d * scale) - (views.get("txtmessage").vw.getHeight())));

}
public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="lblWifiStatus.SetLeftAndRight(lblWifiStatus.Left, 100%x)"[gametypescreen/General script]
views.get("lblwifistatus").vw.setLeft((int)((views.get("lblwifistatus").vw.getLeft())));
views.get("lblwifistatus").vw.setWidth((int)((100d / 100 * width) - ((views.get("lblwifistatus").vw.getLeft()))));
//BA.debugLineNum = 4;BA.debugLine="lblBTStatus.SetLeftAndRight(lblBTStatus.Left, 100%x)"[gametypescreen/General script]
views.get("lblbtstatus").vw.setLeft((int)((views.get("lblbtstatus").vw.getLeft())));
views.get("lblbtstatus").vw.setWidth((int)((100d / 100 * width) - ((views.get("lblbtstatus").vw.getLeft()))));
//BA.debugLineNum = 5;BA.debugLine="ProgressBar1.SetLeftAndRight(0, 100%x)"[gametypescreen/General script]
views.get("progressbar1").vw.setLeft((int)(0d));
views.get("progressbar1").vw.setWidth((int)((100d / 100 * width) - (0d)));
//BA.debugLineNum = 6;BA.debugLine="lblProgress.Right = 100%x"[gametypescreen/General script]
views.get("lblprogress").vw.setLeft((int)((100d / 100 * width) - (views.get("lblprogress").vw.getWidth())));
//BA.debugLineNum = 7;BA.debugLine="lblFile.SetLeftAndRight(0, lblProgress.Left - 5dip)"[gametypescreen/General script]
views.get("lblfile").vw.setLeft((int)(0d));
views.get("lblfile").vw.setWidth((int)((views.get("lblprogress").vw.getLeft())-(5d * scale) - (0d)));

}
}