package pineysoft.squarepaddocks.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("pnlbase").vw.setTop((int)((0d * scale)));
views.get("pnlbase").vw.setLeft((int)((0d * scale)));
views.get("pnlbase").vw.setWidth((int)((100d / 100 * width)));
views.get("pnlbase").vw.setHeight((int)((100d / 100 * height)));
views.get("lblplayer2").vw.setWidth((int)((30d / 100 * width)));
views.get("lblplayer2image").vw.setTop((int)((100d / 100 * height)-(2d * scale) - (views.get("lblplayer2image").vw.getHeight())));
views.get("lblplayer2image").vw.setLeft((int)((2d * scale)));
views.get("lblplayer2").vw.setTop((int)((views.get("lblplayer2image").vw.getTop() + views.get("lblplayer2image").vw.getHeight()) - (views.get("lblplayer2").vw.getHeight())));
views.get("lblplayer2").vw.setLeft((int)((views.get("lblplayer2image").vw.getLeft() + views.get("lblplayer2image").vw.getWidth())+(2d * scale)));
views.get("lblplayer1").vw.setWidth((int)((30d / 100 * width)));
views.get("lblplayer1image").vw.setTop((int)((views.get("lblplayer2image").vw.getTop())-(2d * scale) - (views.get("lblplayer1image").vw.getHeight())));
views.get("lblplayer1image").vw.setLeft((int)((views.get("lblplayer2image").vw.getLeft())));
views.get("lblplayer1").vw.setTop((int)((views.get("lblplayer1image").vw.getTop())));
views.get("lblplayer1").vw.setLeft((int)((views.get("lblplayer1image").vw.getLeft() + views.get("lblplayer1image").vw.getWidth())+(2d * scale)));
views.get("lblplayer3").vw.setWidth((int)((30d / 100 * width)));
views.get("lblplayer3").vw.setLeft((int)((100d / 100 * width) - (views.get("lblplayer3").vw.getWidth())));
views.get("lblplayer3").vw.setTop((int)((views.get("lblplayer1").vw.getTop())));
views.get("lblplayer3image").vw.setTop((int)((views.get("lblplayer3").vw.getTop())));
views.get("lblplayer3image").vw.setLeft((int)((views.get("lblplayer3").vw.getLeft())-(2d * scale) - (views.get("lblplayer3image").vw.getWidth())));
//BA.debugLineNum = 31;BA.debugLine="lblPlayer4.Width = 30%x"[layout1/General script]
views.get("lblplayer4").vw.setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 32;BA.debugLine="lblPlayer4.Right = 100%x"[layout1/General script]
views.get("lblplayer4").vw.setLeft((int)((100d / 100 * width) - (views.get("lblplayer4").vw.getWidth())));
//BA.debugLineNum = 33;BA.debugLine="lblPlayer4.Top = lblPlayer2.Top"[layout1/General script]
views.get("lblplayer4").vw.setTop((int)((views.get("lblplayer2").vw.getTop())));
//BA.debugLineNum = 34;BA.debugLine="lblPlayer4Image.Top = lblPlayer2.Top"[layout1/General script]
views.get("lblplayer4image").vw.setTop((int)((views.get("lblplayer2").vw.getTop())));
//BA.debugLineNum = 35;BA.debugLine="lblPlayer4Image.Left = lblPlayer3Image.Left"[layout1/General script]
views.get("lblplayer4image").vw.setLeft((int)((views.get("lblplayer3image").vw.getLeft())));
//BA.debugLineNum = 40;BA.debugLine="btnCurrPlayer.SetLeftAndRight(lblPlayer1.Right + 2dip, lblPlayer3Image.Left - 2dip)"[layout1/General script]
views.get("btncurrplayer").vw.setLeft((int)((views.get("lblplayer1").vw.getLeft() + views.get("lblplayer1").vw.getWidth())+(2d * scale)));
views.get("btncurrplayer").vw.setWidth((int)((views.get("lblplayer3image").vw.getLeft())-(2d * scale) - ((views.get("lblplayer1").vw.getLeft() + views.get("lblplayer1").vw.getWidth())+(2d * scale))));
//BA.debugLineNum = 41;BA.debugLine="btnCurrPlayer.SetTopAndBottom(lblPlayer1.Top, lblPlayer2Image.Bottom )"[layout1/General script]
views.get("btncurrplayer").vw.setTop((int)((views.get("lblplayer1").vw.getTop())));
views.get("btncurrplayer").vw.setHeight((int)((views.get("lblplayer2image").vw.getTop() + views.get("lblplayer2image").vw.getHeight()) - ((views.get("lblplayer1").vw.getTop()))));
//BA.debugLineNum = 44;BA.debugLine="Panel1.Width = 100%x"[layout1/General script]
views.get("panel1").vw.setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 45;BA.debugLine="Panel1.Height = lblPlayer1.Top - 2dip"[layout1/General script]
views.get("panel1").vw.setHeight((int)((views.get("lblplayer1").vw.getTop())-(2d * scale)));
//BA.debugLineNum = 46;BA.debugLine="Panel1.Bottom = lblPlayer1Image.Top - 2dip"[layout1/General script]
views.get("panel1").vw.setTop((int)((views.get("lblplayer1image").vw.getTop())-(2d * scale) - (views.get("panel1").vw.getHeight())));

}
}