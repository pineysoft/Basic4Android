package pineysoft.squarepaddocks.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_startscreen{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("pnlstartscreen").vw.setTop((int)((0d * scale)));
views.get("pnlstartscreen").vw.setLeft((int)((0d * scale)));
views.get("pnlstartscreen").vw.setWidth((int)((100d / 100 * width)));
views.get("pnlstartscreen").vw.setHeight((int)((100d / 100 * height)));
views.get("titleimage").vw.setLeft((int)((75d * scale)));
views.get("titleimage").vw.setWidth((int)((100d / 100 * width)-(75d * scale) - ((75d * scale))));
views.get("titleimage").vw.setTop((int)((10d * scale)));
views.get("pnlshading").vw.setLeft((int)((5d / 100 * width)));
views.get("pnlshading").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
views.get("pnlshading").vw.setTop((int)((15d / 100 * height)));
views.get("pnlshading").vw.setHeight((int)((85d / 100 * height) - ((15d / 100 * height))));
views.get("pnlselectionleft").vw.setTop((int)((views.get("pnlshading").vw.getTop())));
views.get("pnlselectionleft").vw.setHeight((int)((views.get("pnlshading").vw.getTop() + views.get("pnlshading").vw.getHeight()) - ((views.get("pnlshading").vw.getTop()))));
views.get("pnlselectionleft").vw.setLeft((int)((5d / 100 * width)));
views.get("pnlselectionleft").vw.setWidth((int)((45d / 100 * width) - ((5d / 100 * width))));
views.get("pnlselectionright").vw.setTop((int)((views.get("pnlshading").vw.getTop())));
views.get("pnlselectionright").vw.setHeight((int)((100d / 100 * height)));
views.get("pnlselectionright").vw.setTop((int)((views.get("pnlshading").vw.getTop())));
views.get("pnlselectionright").vw.setHeight((int)((views.get("pnlshading").vw.getTop() + views.get("pnlshading").vw.getHeight()) - ((views.get("pnlshading").vw.getTop()))));
views.get("pnlselectionright").vw.setLeft((int)((55d / 100 * width)));
views.get("pnlselectionright").vw.setWidth((int)((95d / 100 * width) - ((55d / 100 * width))));
views.get("lblplayers").vw.setHeight((int)(((views.get("pnlselectionleft").vw.getHeight())-75d)/7d));
views.get("lblplayers").vw.setTop((int)((2d * scale)));
views.get("lblplayers").vw.setWidth((int)((views.get("pnlselectionleft").vw.getWidth())*.9d));
views.get("lblplayers").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lblplayers").vw.getWidth())));
views.get("lbldroids").vw.setTop((int)((views.get("lblplayers").vw.getTop() + views.get("lblplayers").vw.getHeight())+(5d * scale)));
views.get("lbldroids").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
views.get("lbldroids").vw.setWidth((int)((views.get("lblplayers").vw.getWidth())));
views.get("lbldroids").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lbldroids").vw.getWidth())));
views.get("lblrows").vw.setWidth((int)((views.get("lbldroids").vw.getWidth())));
views.get("lblrows").vw.setTop((int)((views.get("lbldroids").vw.getTop() + views.get("lbldroids").vw.getHeight())+(5d * scale)));
views.get("lblrows").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
views.get("lblrows").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lblrows").vw.getWidth())));
views.get("lblcolumns").vw.setWidth((int)((views.get("lbldroids").vw.getWidth())));
views.get("lblcolumns").vw.setTop((int)((views.get("lblrows").vw.getTop() + views.get("lblrows").vw.getHeight())+(5d * scale)));
views.get("lblcolumns").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
views.get("lblcolumns").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lblcolumns").vw.getWidth())));
views.get("lblsound").vw.setWidth((int)((views.get("lbldroids").vw.getWidth())));
views.get("lblsound").vw.setTop((int)((views.get("lblcolumns").vw.getTop() + views.get("lblcolumns").vw.getHeight())+(5d * scale)));
//BA.debugLineNum = 45;BA.debugLine="lblSound.Height = lblPlayers.Height"[startscreen/General script]
views.get("lblsound").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 46;BA.debugLine="lblSound.Right = pnlSelectionLeft.Width"[startscreen/General script]
views.get("lblsound").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lblsound").vw.getWidth())));
//BA.debugLineNum = 48;BA.debugLine="lblDifficulty.Width = lblDroids.Width"[startscreen/General script]
views.get("lbldifficulty").vw.setWidth((int)((views.get("lbldroids").vw.getWidth())));
//BA.debugLineNum = 49;BA.debugLine="lblDifficulty.Top = lblSound.Bottom + 5dip"[startscreen/General script]
views.get("lbldifficulty").vw.setTop((int)((views.get("lblsound").vw.getTop() + views.get("lblsound").vw.getHeight())+(5d * scale)));
//BA.debugLineNum = 50;BA.debugLine="lblDifficulty.Height = lblPlayers.Height"[startscreen/General script]
views.get("lbldifficulty").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 51;BA.debugLine="lblDifficulty.Right = pnlSelectionLeft.Width"[startscreen/General script]
views.get("lbldifficulty").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lbldifficulty").vw.getWidth())));
//BA.debugLineNum = 53;BA.debugLine="lblGameModeName.Width = lblDroids.Width"[startscreen/General script]
views.get("lblgamemodename").vw.setWidth((int)((views.get("lbldroids").vw.getWidth())));
//BA.debugLineNum = 54;BA.debugLine="lblGameModeName.Top = lblDifficulty.Bottom + 5dip"[startscreen/General script]
views.get("lblgamemodename").vw.setTop((int)((views.get("lbldifficulty").vw.getTop() + views.get("lbldifficulty").vw.getHeight())+(5d * scale)));
//BA.debugLineNum = 55;BA.debugLine="lblGameModeName.Height = lblPlayers.Height"[startscreen/General script]
views.get("lblgamemodename").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 56;BA.debugLine="lblGameModeName.Right = pnlSelectionLeft.Width"[startscreen/General script]
views.get("lblgamemodename").vw.setLeft((int)((views.get("pnlselectionleft").vw.getWidth()) - (views.get("lblgamemodename").vw.getWidth())));
//BA.debugLineNum = 59;BA.debugLine="spnPlayers.Left = 0dip"[startscreen/General script]
views.get("spnplayers").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 60;BA.debugLine="spnPlayers.Top = lblPlayers.Top"[startscreen/General script]
views.get("spnplayers").vw.setTop((int)((views.get("lblplayers").vw.getTop())));
//BA.debugLineNum = 61;BA.debugLine="spnPlayers.Width = lblPlayers.Width"[startscreen/General script]
views.get("spnplayers").vw.setWidth((int)((views.get("lblplayers").vw.getWidth())));
//BA.debugLineNum = 62;BA.debugLine="spnPlayers.Height = lblPlayers.Height"[startscreen/General script]
views.get("spnplayers").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 64;BA.debugLine="spnDroids.Top = lblDroids.Top"[startscreen/General script]
views.get("spndroids").vw.setTop((int)((views.get("lbldroids").vw.getTop())));
//BA.debugLineNum = 65;BA.debugLine="spnDroids.Left = 0dip"[startscreen/General script]
views.get("spndroids").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 66;BA.debugLine="spnDroids.Width = lblRows.Width"[startscreen/General script]
views.get("spndroids").vw.setWidth((int)((views.get("lblrows").vw.getWidth())));
//BA.debugLineNum = 67;BA.debugLine="spnDroids.Height  = lblPlayers.Height"[startscreen/General script]
views.get("spndroids").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 69;BA.debugLine="sbRows.Top = lblRows.Top"[startscreen/General script]
views.get("sbrows").vw.setTop((int)((views.get("lblrows").vw.getTop())));
//BA.debugLineNum = 70;BA.debugLine="sbRows.Left = 0dip"[startscreen/General script]
views.get("sbrows").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 71;BA.debugLine="sbRows.Width = lblRows.Width"[startscreen/General script]
views.get("sbrows").vw.setWidth((int)((views.get("lblrows").vw.getWidth())));
//BA.debugLineNum = 72;BA.debugLine="sbRows.Height = lblPlayers.Height"[startscreen/General script]
views.get("sbrows").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 74;BA.debugLine="sbColumns.Top = lblColumns.Top"[startscreen/General script]
views.get("sbcolumns").vw.setTop((int)((views.get("lblcolumns").vw.getTop())));
//BA.debugLineNum = 75;BA.debugLine="sbColumns.Left = 0dip"[startscreen/General script]
views.get("sbcolumns").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 76;BA.debugLine="sbColumns.Width = lblColumns.Width"[startscreen/General script]
views.get("sbcolumns").vw.setWidth((int)((views.get("lblcolumns").vw.getWidth())));
//BA.debugLineNum = 77;BA.debugLine="sbColumns.Height  = lblPlayers.Height"[startscreen/General script]
views.get("sbcolumns").vw.setHeight((int)((views.get("lblplayers").vw.getHeight())));
//BA.debugLineNum = 79;BA.debugLine="chkSounds.Top = lblSound.Top"[startscreen/General script]
views.get("chksounds").vw.setTop((int)((views.get("lblsound").vw.getTop())));
//BA.debugLineNum = 80;BA.debugLine="chkSounds.Left = 0dip"[startscreen/General script]
views.get("chksounds").vw.setLeft((int)((0d * scale)));
//BA.debugLineNum = 81;BA.debugLine="chkSounds.Height = lblColumns.Height"[startscreen/General script]
views.get("chksounds").vw.setHeight((int)((views.get("lblcolumns").vw.getHeight())));
//BA.debugLineNum = 82;BA.debugLine="chkSounds.Width = 40dip"[startscreen/General script]
views.get("chksounds").vw.setWidth((int)((40d * scale)));
//BA.debugLineNum = 84;BA.debugLine="spnDifficulty.Top = lblDifficulty.Top"[startscreen/General script]
views.get("spndifficulty").vw.setTop((int)((views.get("lbldifficulty").vw.getTop())));
//BA.debugLineNum = 85;BA.debugLine="spnDifficulty.Height = lblDifficulty.Height"[startscreen/General script]
views.get("spndifficulty").vw.setHeight((int)((views.get("lbldifficulty").vw.getHeight())));
//BA.debugLineNum = 86;BA.debugLine="spnDifficulty.Width = lblDifficulty.Width"[startscreen/General script]
views.get("spndifficulty").vw.setWidth((int)((views.get("lbldifficulty").vw.getWidth())));
//BA.debugLineNum = 89;BA.debugLine="lblGameModeDescr.Top = lblGameModeName.Top"[startscreen/General script]
views.get("lblgamemodedescr").vw.setTop((int)((views.get("lblgamemodename").vw.getTop())));
//BA.debugLineNum = 90;BA.debugLine="lblGameModeDescr.Height = lblGameModeName.Height"[startscreen/General script]
views.get("lblgamemodedescr").vw.setHeight((int)((views.get("lblgamemodename").vw.getHeight())));
//BA.debugLineNum = 91;BA.debugLine="lblGameModeDescr.Width = lblGameModeName.Width"[startscreen/General script]
views.get("lblgamemodedescr").vw.setWidth((int)((views.get("lblgamemodename").vw.getWidth())));
//BA.debugLineNum = 95;BA.debugLine="If 100%y - (pnlShading.Bottom + 10dip) > 80dip Then"[startscreen/General script]
if (((100d / 100 * height)-((views.get("pnlshading").vw.getTop() + views.get("pnlshading").vw.getHeight())+(10d * scale))>(80d * scale))) { 
;
//BA.debugLineNum = 96;BA.debugLine="btnContinue.Height = 80dip"[startscreen/General script]
views.get("btncontinue").vw.setHeight((int)((80d * scale)));
//BA.debugLineNum = 97;BA.debugLine="Else"[startscreen/General script]
;}else{ 
;
//BA.debugLineNum = 98;BA.debugLine="btnContinue.Height = 100%y - (pnlShading.Bottom + 10dip)"[startscreen/General script]
views.get("btncontinue").vw.setHeight((int)((100d / 100 * height)-((views.get("pnlshading").vw.getTop() + views.get("pnlshading").vw.getHeight())+(10d * scale))));
//BA.debugLineNum = 99;BA.debugLine="End If"[startscreen/General script]
;};
//BA.debugLineNum = 100;BA.debugLine="btnContinue.Width = 25%x"[startscreen/General script]
views.get("btncontinue").vw.setWidth((int)((25d / 100 * width)));
//BA.debugLineNum = 101;BA.debugLine="btnContinue.HorizontalCenter = 50%x"[startscreen/General script]
views.get("btncontinue").vw.setLeft((int)((50d / 100 * width) - (views.get("btncontinue").vw.getWidth() / 2)));
//BA.debugLineNum = 102;BA.debugLine="btnContinue.Bottom = 100%y - 10dip"[startscreen/General script]
views.get("btncontinue").vw.setTop((int)((100d / 100 * height)-(10d * scale) - (views.get("btncontinue").vw.getHeight())));
//BA.debugLineNum = 104;BA.debugLine="icon1.Left = -100dip"[startscreen/General script]
views.get("icon1").vw.setLeft((int)(0-(100d * scale)));
//BA.debugLineNum = 105;BA.debugLine="icon2.Left = -100dip"[startscreen/General script]
views.get("icon2").vw.setLeft((int)(0-(100d * scale)));
//BA.debugLineNum = 106;BA.debugLine="icon3.Left = -100dip"[startscreen/General script]
views.get("icon3").vw.setLeft((int)(0-(100d * scale)));
//BA.debugLineNum = 107;BA.debugLine="icon4.Left = -100dip"[startscreen/General script]
views.get("icon4").vw.setLeft((int)(0-(100d * scale)));
//BA.debugLineNum = 108;BA.debugLine="icon5.Left = -120dip"[startscreen/General script]
views.get("icon5").vw.setLeft((int)(0-(120d * scale)));
//BA.debugLineNum = 109;BA.debugLine="icon6.Left = -120dip"[startscreen/General script]
views.get("icon6").vw.setLeft((int)(0-(120d * scale)));

}
}