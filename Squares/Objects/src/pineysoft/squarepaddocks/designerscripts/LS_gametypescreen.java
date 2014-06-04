package pineysoft.squarepaddocks.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_gametypescreen{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[gametypescreen/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="pnlGameTypeSelection.SetLeftAndRight(5%x, 95%x)"[gametypescreen/General script]
views.get("pnlgametypeselection").vw.setLeft((int)((5d / 100 * width)));
views.get("pnlgametypeselection").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
//BA.debugLineNum = 4;BA.debugLine="pnlGameTypeSelection.SetTopAndBottom(10%y,80%y)"[gametypescreen/General script]
views.get("pnlgametypeselection").vw.setTop((int)((10d / 100 * height)));
views.get("pnlgametypeselection").vw.setHeight((int)((80d / 100 * height) - ((10d / 100 * height))));
//BA.debugLineNum = 5;BA.debugLine="rdbLocalPlay.Top = 20dip"[gametypescreen/General script]
views.get("rdblocalplay").vw.setTop((int)((20d * scale)));
//BA.debugLineNum = 6;BA.debugLine="rdbLocalPlay.Left = pnlGameTypeSelection.Width / 2 - 40dip"[gametypescreen/General script]
views.get("rdblocalplay").vw.setLeft((int)((views.get("pnlgametypeselection").vw.getWidth())/2d-(40d * scale)));
//BA.debugLineNum = 7;BA.debugLine="imgLocalPlay.Top = rdbLocalPlay.top"[gametypescreen/General script]
views.get("imglocalplay").vw.setTop((int)((views.get("rdblocalplay").vw.getTop())));
//BA.debugLineNum = 8;BA.debugLine="imgLocalPlay.Left = rdbLocalPlay.Left - imgLocalPlay.Width - 20"[gametypescreen/General script]
views.get("imglocalplay").vw.setLeft((int)((views.get("rdblocalplay").vw.getLeft())-(views.get("imglocalplay").vw.getWidth())-20d));
//BA.debugLineNum = 9;BA.debugLine="rdbBlueTooth.Top = rdbLocalPlay.Bottom + 20dip"[gametypescreen/General script]
views.get("rdbbluetooth").vw.setTop((int)((views.get("rdblocalplay").vw.getTop() + views.get("rdblocalplay").vw.getHeight())+(20d * scale)));
//BA.debugLineNum = 10;BA.debugLine="rdbBlueTooth.Left = rdbLocalPlay.Left"[gametypescreen/General script]
views.get("rdbbluetooth").vw.setLeft((int)((views.get("rdblocalplay").vw.getLeft())));
//BA.debugLineNum = 11;BA.debugLine="imgBluetooth.Top = rdbBlueTooth.Top"[gametypescreen/General script]
views.get("imgbluetooth").vw.setTop((int)((views.get("rdbbluetooth").vw.getTop())));
//BA.debugLineNum = 12;BA.debugLine="imgBluetooth.Left = imgLocalPlay.Left"[gametypescreen/General script]
views.get("imgbluetooth").vw.setLeft((int)((views.get("imglocalplay").vw.getLeft())));
//BA.debugLineNum = 13;BA.debugLine="rdbWifi.Top = rdbBlueTooth.Bottom + 20dip"[gametypescreen/General script]
views.get("rdbwifi").vw.setTop((int)((views.get("rdbbluetooth").vw.getTop() + views.get("rdbbluetooth").vw.getHeight())+(20d * scale)));
//BA.debugLineNum = 14;BA.debugLine="rdbWifi.Left = rdbLocalPlay.Left"[gametypescreen/General script]
views.get("rdbwifi").vw.setLeft((int)((views.get("rdblocalplay").vw.getLeft())));
//BA.debugLineNum = 15;BA.debugLine="imgWifi.Top = rdbWifi.Top"[gametypescreen/General script]
views.get("imgwifi").vw.setTop((int)((views.get("rdbwifi").vw.getTop())));
//BA.debugLineNum = 16;BA.debugLine="imgWifi.Left = imgLocalPlay.Left"[gametypescreen/General script]
views.get("imgwifi").vw.setLeft((int)((views.get("imglocalplay").vw.getLeft())));
//BA.debugLineNum = 17;BA.debugLine="btnDiscoverMe.Right = pnlGameTypeSelection.Width / 2 - 20dip"[gametypescreen/General script]
views.get("btndiscoverme").vw.setLeft((int)((views.get("pnlgametypeselection").vw.getWidth())/2d-(20d * scale) - (views.get("btndiscoverme").vw.getWidth())));
//BA.debugLineNum = 18;BA.debugLine="btnDiscoverMe.Bottom = pnlGameTypeSelection.Bottom - btnDiscoverMe.Height"[gametypescreen/General script]
views.get("btndiscoverme").vw.setTop((int)((views.get("pnlgametypeselection").vw.getTop() + views.get("pnlgametypeselection").vw.getHeight())-(views.get("btndiscoverme").vw.getHeight()) - (views.get("btndiscoverme").vw.getHeight())));
//BA.debugLineNum = 19;BA.debugLine="btnSearch.Left = pnlGameTypeSelection.Width / 2 + 20dip"[gametypescreen/General script]
views.get("btnsearch").vw.setLeft((int)((views.get("pnlgametypeselection").vw.getWidth())/2d+(20d * scale)));
//BA.debugLineNum = 20;BA.debugLine="btnSearch.Bottom = pnlGameTypeSelection.Bottom - btnSearch.Height"[gametypescreen/General script]
views.get("btnsearch").vw.setTop((int)((views.get("pnlgametypeselection").vw.getTop() + views.get("pnlgametypeselection").vw.getHeight())-(views.get("btnsearch").vw.getHeight()) - (views.get("btnsearch").vw.getHeight())));
//BA.debugLineNum = 21;BA.debugLine="btnGTContinue.Left = pnlGameTypeSelection.Width / 2"[gametypescreen/General script]
views.get("btngtcontinue").vw.setLeft((int)((views.get("pnlgametypeselection").vw.getWidth())/2d));
//BA.debugLineNum = 22;BA.debugLine="btnGTContinue.Bottom = 100%y - 20dip"[gametypescreen/General script]
views.get("btngtcontinue").vw.setTop((int)((100d / 100 * height)-(20d * scale) - (views.get("btngtcontinue").vw.getHeight())));

}
}