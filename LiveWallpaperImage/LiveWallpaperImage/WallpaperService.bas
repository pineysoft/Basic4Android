Type=Service
Version=3.5
@EndOfDesignText@
#Region Module Attributes
	#StartAtBoot: False
#End Region

'Service module
Sub Process_Globals
	Dim imageName As String : imageName = "drawboardportrait_complete.png"
	Dim lwm As LWManager
	Dim LiveEngine As LWEngine
	Dim image As Bitmap
	Dim resized As Bitmap
	Dim x, y As Int
	Dim vx As Int = 10dip 'puck speed
	Dim vy As Int = 10dip
	Dim Degrees As Int
	Dim boxsize As Int = 40dip
End Sub
Sub Service_Create
	lwm.Initialize("lwm", True)
End Sub

Sub Service_Start (StartingIntent As Intent)

End Sub

Sub Service_Destroy

End Sub

Sub LWM_SizeChanged (Engine As LWEngine)
	'This event is raised when the engine is first ready (and when the screen size changes).
	'We need to hold a reference to the non-preview engine to allow us to later change the
	'image.
	If Engine.IsPreview = False Then LiveEngine = Engine
End Sub

Sub LWM_OffsetChanged (Engine As LWEngine)
	image = LoadBitmap(File.DirAssets, "drawboardportrait_complete.png")
	resized = ResizeImage(image, Engine.FullWallpaperWidth, Engine.FullWallpaperHeight)
	If resized.IsInitialized Then
		Engine.Rect.Left = -Engine.CurrentOffsetX
		Engine.Rect.Top = -Engine.CurrentOffsetY
		Engine.Rect.Right = -Engine.CurrentOffsetX + Engine.FullWallpaperWidth
		Engine.Rect.Bottom = -Engine.CurrentOffsetY + Engine.FullWallpaperHeight
		Engine.Canvas.DrawBitmap(resized, Null, Engine.Rect)
	Else
		Engine.Canvas.DrawColor(Colors.Black)
		Engine.Canvas.DrawText("No image selected", 120dip, 120dip, Typeface.DEFAULT_BOLD, 30, Colors.White, "LEFT")
	End If
	Engine.RefreshAll
End Sub

Sub ResizeImage(original As Bitmap, TargetX As Int, TargetY As Int) As Bitmap
   Dim origRatio As Float = original.Width / original.Height
   Dim targetRatio As Float = TargetX / TargetY
   Dim scale As Float
   
   If targetRatio > origRatio Then
      scale = TargetY / original.Height
   Else
      scale = TargetX / original.Width
   End If
   
   Dim c As Canvas
   Dim b As Bitmap
   b.InitializeMutable(TargetX, TargetY)
   c.Initialize2(b)
   'set the background
   c.DrawColor(Colors.LightGray)
   Dim r As Rect
   Dim w = original.Width * scale, h = original.Height * scale As Int
   r.Initialize(TargetX / 2 - w / 2, TargetY / 2 - h / 2, TargetX / 2 + w / 2, TargetY / 2+ h / 2)
   c.DrawBitmap(original, Null, r)
   Return b
End Sub

Sub lwm_Touch (Engine As LWEngine, Action As Int, tx As Float, ty As Float)
	If Action <> 0 Then Return '0 is the value of Activity.ACTION_DOWN
	x = tx
	y = ty
	
	Engine.Canvas.DrawCircle(tx, ty, 5dip, Colors.Black, True, 2dip)
	Engine.Refresh(Engine.Rect)
	
	lwm.StartTicking(30) 'Start the wallpaper timer (30 milliseconds).

End Sub

Sub lwm_Tick (Engine As LWEngine)
	
	If y > 0 Then
		y = y - vy

		Engine.Rect.Top = y
		Engine.Rect.Left = x
		Engine.Rect.Bottom = y + boxsize
		Engine.Rect.Right = x + boxsize
	
		Engine.Canvas.DrawCircle(x,y,5dip, Colors.Black,True, 2dip) 'Draw the new smiley
		Engine.Refresh(Engine.Rect)		
	Else
		lwm.StopTicking
	End If

End Sub