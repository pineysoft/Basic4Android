package b4a.example.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_screen1{

public static void LS_480x320_1(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="btnNewPark.Top = 0dip"[screen1/480x320,scale=1]
views.get("btnnewpark").vw.setTop((int)((0d * scale)));
//BA.debugLineNum = 3;BA.debugLine="btnNewPark.Left = 0dip"[screen1/480x320,scale=1]
views.get("btnnewpark").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 4;BA.debugLine="btnNewPark.Width = 40%X"[screen1/480x320,scale=1]
views.get("btnnewpark").vw.setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 5;BA.debugLine="btnNewPark.Height = 100%y"[screen1/480x320,scale=1]
views.get("btnnewpark").vw.setHeight((int)((100d / 100 * height)));
//BA.debugLineNum = 6;BA.debugLine="btnLastUsed.Left= btnNewPark.Right"[screen1/480x320,scale=1]
views.get("btnlastused").vw.setLeft((int)((views.get("btnnewpark").vw.getLeft() + views.get("btnnewpark").vw.getWidth())));
//BA.debugLineNum = 7;BA.debugLine="btnLastUsed.Top = 0dip"[screen1/480x320,scale=1]
views.get("btnlastused").vw.setTop((int)((0d * scale)));
//BA.debugLineNum = 8;BA.debugLine="btnLastUsed.Width = 35%x"[screen1/480x320,scale=1]
views.get("btnlastused").vw.setWidth((int)((35d / 100 * width)));
//BA.debugLineNum = 9;BA.debugLine="btnLastUsed.Height = 100%y"[screen1/480x320,scale=1]
views.get("btnlastused").vw.setHeight((int)((100d / 100 * height)));
//BA.debugLineNum = 10;BA.debugLine="btnSettings.Top = 0dip"[screen1/480x320,scale=1]
views.get("btnsettings").vw.setTop((int)((0d * scale)));
//BA.debugLineNum = 11;BA.debugLine="btnSettings.Left = btnLastUsed.Right"[screen1/480x320,scale=1]
views.get("btnsettings").vw.setLeft((int)((views.get("btnlastused").vw.getLeft() + views.get("btnlastused").vw.getWidth())));
//BA.debugLineNum = 12;BA.debugLine="btnSettings.Width = 25%x"[screen1/480x320,scale=1]
views.get("btnsettings").vw.setWidth((int)((25d / 100 * width)));
//BA.debugLineNum = 13;BA.debugLine="btnSettings.Height = 50%y"[screen1/480x320,scale=1]
views.get("btnsettings").vw.setHeight((int)((50d / 100 * height)));
//BA.debugLineNum = 14;BA.debugLine="hsvParking.Left = btnLastUsed.Right"[screen1/480x320,scale=1]
views.get("hsvparking").vw.setLeft((int)((views.get("btnlastused").vw.getLeft() + views.get("btnlastused").vw.getWidth())));
//BA.debugLineNum = 15;BA.debugLine="hsvParking.Top = btnSettings.Bottom"[screen1/480x320,scale=1]
views.get("hsvparking").vw.setTop((int)((views.get("btnsettings").vw.getTop() + views.get("btnsettings").vw.getHeight())));
//BA.debugLineNum = 16;BA.debugLine="hsvParking.Width = 25%x"[screen1/480x320,scale=1]
views.get("hsvparking").vw.setWidth((int)((25d / 100 * width)));
//BA.debugLineNum = 17;BA.debugLine="hsvParking.Height = 50%y"[screen1/480x320,scale=1]
views.get("hsvparking").vw.setHeight((int)((50d / 100 * height)));
//BA.debugLineNum = 18;BA.debugLine="lblPlate.Top = hsvParking.Top"[screen1/480x320,scale=1]
views.get("lblplate").vw.setTop((int)((views.get("hsvparking").vw.getTop())));
//BA.debugLineNum = 19;BA.debugLine="lblPlate.Left = hsvParking.Left"[screen1/480x320,scale=1]
views.get("lblplate").vw.setLeft((int)((views.get("hsvparking").vw.getLeft())));
//BA.debugLineNum = 20;BA.debugLine="lblPlate.Width = hsvParking.Width"[screen1/480x320,scale=1]
views.get("lblplate").vw.setWidth((int)((views.get("hsvparking").vw.getWidth())));
//BA.debugLineNum = 21;BA.debugLine="lblPlate.Height = hsvParking.Height / 2"[screen1/480x320,scale=1]
views.get("lblplate").vw.setHeight((int)((views.get("hsvparking").vw.getHeight())/2d));
//BA.debugLineNum = 22;BA.debugLine="lblLastUsed.Height = btnLastUsed.Height / 3"[screen1/480x320,scale=1]
views.get("lbllastused").vw.setHeight((int)((views.get("btnlastused").vw.getHeight())/3d));
//BA.debugLineNum = 23;BA.debugLine="lblLastUsed.Width = btnLastUsed.Width / 1.1"[screen1/480x320,scale=1]
views.get("lbllastused").vw.setWidth((int)((views.get("btnlastused").vw.getWidth())/1.1d));
//BA.debugLineNum = 24;BA.debugLine="lblLastUsed.VerticalCenter = 50%y"[screen1/480x320,scale=1]
views.get("lbllastused").vw.setTop((int)((50d / 100 * height) - (views.get("lbllastused").vw.getHeight() / 2)));
//BA.debugLineNum = 25;BA.debugLine="lblLastUsed.HorizontalCenter = btnLastUsed.Left + btnLastUsed.Width / 2"[screen1/480x320,scale=1]
views.get("lbllastused").vw.setLeft((int)((views.get("btnlastused").vw.getLeft())+(views.get("btnlastused").vw.getWidth())/2d - (views.get("lbllastused").vw.getWidth() / 2)));

}
public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("pnlsplash").vw.setLeft((int)((95d / 100 * width)));
views.get("pnlsplash").vw.setTop((int)((0d * scale)));
views.get("pnlsplash").vw.setWidth((int)((100d / 100 * width)));
views.get("pnlsplash").vw.setHeight((int)((100d / 100 * height)));
views.get("pnlmainscreen").vw.setLeft((int)((0d * scale)));
views.get("pnlmainscreen").vw.setTop((int)((0d * scale)));
views.get("pnlmainscreen").vw.setWidth((int)((100d / 100 * width)));
views.get("pnlmainscreen").vw.setHeight((int)((100d / 100 * height)));
views.get("btnnewpark").vw.setWidth((int)((100d / 100 * width)));
views.get("btnnewpark").vw.setHeight((int)((40d / 100 * height)));
views.get("btnnewpark").vw.setTop((int)((0d * scale)));
views.get("btnnewpark").vw.setLeft((int)((0d * scale)));
views.get("btnlastused").vw.setLeft((int)((0d * scale)));
views.get("btnlastused").vw.setTop((int)((views.get("btnnewpark").vw.getTop() + views.get("btnnewpark").vw.getHeight())));
views.get("btnlastused").vw.setWidth((int)((100d / 100 * width)));
views.get("btnlastused").vw.setHeight((int)((30d / 100 * height)));
views.get("btnsettings").vw.setTop((int)((views.get("btnlastused").vw.getTop() + views.get("btnlastused").vw.getHeight())));
views.get("btnsettings").vw.setLeft((int)((0d * scale)));
views.get("btnsettings").vw.setWidth((int)((100d / 100 * width)));
views.get("btnsettings").vw.setHeight((int)((18d / 100 * height)));
views.get("hsvparking").vw.setLeft((int)((0d * scale)));
views.get("hsvparking").vw.setTop((int)((views.get("btnsettings").vw.getTop() + views.get("btnsettings").vw.getHeight())));
//BA.debugLineNum = 25;BA.debugLine="hsvParking.Width = 100%x"[screen1/General script]
views.get("hsvparking").vw.setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 26;BA.debugLine="hsvParking.Height = 12%y"[screen1/General script]
views.get("hsvparking").vw.setHeight((int)((12d / 100 * height)));
//BA.debugLineNum = 27;BA.debugLine="lblPlate.Top = hsvParking.Top"[screen1/General script]
views.get("lblplate").vw.setTop((int)((views.get("hsvparking").vw.getTop())));
//BA.debugLineNum = 28;BA.debugLine="lblPlate.Left = hsvParking.Left"[screen1/General script]
views.get("lblplate").vw.setLeft((int)((views.get("hsvparking").vw.getLeft())));
//BA.debugLineNum = 29;BA.debugLine="lblPlate.Height = hsvParking.Height"[screen1/General script]
views.get("lblplate").vw.setHeight((int)((views.get("hsvparking").vw.getHeight())));
//BA.debugLineNum = 30;BA.debugLine="lblLastUsed.Height = btnLastUsed.Height / 2"[screen1/General script]
views.get("lbllastused").vw.setHeight((int)((views.get("btnlastused").vw.getHeight())/2d));
//BA.debugLineNum = 31;BA.debugLine="lblLastUsed.Width = btnLastUsed.Width / 2"[screen1/General script]
views.get("lbllastused").vw.setWidth((int)((views.get("btnlastused").vw.getWidth())/2d));
//BA.debugLineNum = 32;BA.debugLine="lblLastUsed.bottom = btnLastUsed.Bottom - 10dip"[screen1/General script]
views.get("lbllastused").vw.setTop((int)((views.get("btnlastused").vw.getTop() + views.get("btnlastused").vw.getHeight())-(10d * scale) - (views.get("lbllastused").vw.getHeight())));
//BA.debugLineNum = 33;BA.debugLine="lblLastUsed.HorizontalCenter = 50%x"[screen1/General script]
views.get("lbllastused").vw.setLeft((int)((50d / 100 * width) - (views.get("lbllastused").vw.getWidth() / 2)));

}
}