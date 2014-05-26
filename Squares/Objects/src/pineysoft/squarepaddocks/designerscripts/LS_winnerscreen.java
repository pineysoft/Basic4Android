package pineysoft.squarepaddocks.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_winnerscreen{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("pnlouter").vw.setTop((int)((0d * scale)));
views.get("pnlouter").vw.setLeft((int)(0d));
views.get("pnlouter").vw.setWidth((int)((100d / 100 * width)));
views.get("pnlouter").vw.setHeight((int)((100d / 100 * height)));
views.get("pnldisplay").vw.setHeight((int)((75d / 100 * height)));
views.get("pnldisplay").vw.setWidth((int)((75d / 100 * width)));
views.get("pnldisplay").vw.setTop((int)((60d * scale)));
views.get("pnldisplay").vw.setHeight((int)((100d / 100 * height)-(60d * scale) - ((60d * scale))));
views.get("pnldisplay").vw.setLeft((int)((15d / 100 * width)));
views.get("pnldisplay").vw.setWidth((int)((85d / 100 * width) - ((15d / 100 * width))));
views.get("imageicon").vw.setTop((int)((20d * scale)));
views.get("imageicon").vw.setLeft((int)((views.get("pnldisplay").vw.getWidth())/2d - (views.get("imageicon").vw.getWidth() / 2)));
views.get("lblwinner").vw.setTop((int)((views.get("imageicon").vw.getTop() + views.get("imageicon").vw.getHeight())+(20d * scale)));
views.get("lblwinner").vw.setLeft((int)((views.get("pnldisplay").vw.getWidth())/2d - (views.get("lblwinner").vw.getWidth() / 2)));
views.get("lblscores").vw.setTop((int)((views.get("lblwinner").vw.getTop() + views.get("lblwinner").vw.getHeight())+(5d * scale)));
views.get("lblscores").vw.setLeft((int)((views.get("pnldisplay").vw.getWidth())/2d - (views.get("lblscores").vw.getWidth() / 2)));
views.get("btnok").vw.setWidth((int)((50d / 100 * width)));
views.get("btnok").vw.setHeight((int)((40d * scale)));
views.get("btnok").vw.setTop((int)((views.get("lblscores").vw.getTop() + views.get("lblscores").vw.getHeight())+(10d * scale)));
views.get("btnok").vw.setLeft((int)((views.get("pnldisplay").vw.getWidth())/2d - (views.get("btnok").vw.getWidth() / 2)));

}
}