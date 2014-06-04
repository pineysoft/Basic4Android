package flm.b4a.lgdxlivewallpaper.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_sstars{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[sstars/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="btnSetWallpaper.HorizontalCenter = 50%x"[sstars/General script]
views.get("btnsetwallpaper").vw.setLeft((int)((50d / 100 * width) - (views.get("btnsetwallpaper").vw.getWidth() / 2)));
//BA.debugLineNum = 4;BA.debugLine="btnSetWallpaper.VerticalCenter = 50%x"[sstars/General script]
views.get("btnsetwallpaper").vw.setTop((int)((50d / 100 * width) - (views.get("btnsetwallpaper").vw.getHeight() / 2)));

}
}