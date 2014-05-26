Type=Service
Version=3.5
@EndOfDesignText@
#Region Module Attributes
	#StartAtBoot: False
#End Region

'Service module
Sub Process_Globals
	Dim LW As lgLiveWallpaper
	Dim GL As lgGL
	Dim Batch As lgSpriteBatch
	Dim Camera As lgOrthographicCamera
	Dim texSkater As lgTexture
	Dim texPuck As lgTexture
	Dim texBG As lgTexture
	Dim lstSmileys As List
	Dim sStar As typSmiley
	Dim sPuck As typSmiley
	Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)
	Dim playTimer As Timer
	Dim allowPlay As Boolean
End Sub

Sub Service_Create
End Sub

Sub Service_Start (StartingIntent As Intent)
	'Disables the accelerometer and the compass
	Dim Config As lgConfiguration
	Config.useAccelerometer = False
	Config.useCompass = False

	'Creates the libGDX surface and allows touch events
	LW.Initialize(Config, True, "LG")
	playTimer.Initialize("playT",5000)
End Sub

Sub Service_Destroy
	'This event is never raised by libGDX
End Sub

Sub LG_Create
	Log("--- CREATE ---")

	'Initializes the sprite batcher
	Batch.Initialize

	'Loads the smiley image (64x64)
	texSkater.Initialize("skater.png")
	texPuck.Initialize("Puck.png")
	texBG.Initialize("HockeyBoardPortrait_Stars.png")
	
	'Creates the list of smileys
	sStar.Initialize

End Sub

Sub CreateTextures
	'Creates the list of smileys
	lstSmileys.Initialize

	sStar.Initialize
	sStar.X = 80dip
	sStar.Y = LW.Graphics.Height + 64dip
	sStar.dX = 1dip
	sStar.dY = 5dip
	sStar.Color.setRGBA(1, 1, 1, .75)
	
	sPuck.Initialize
	sPuck.X = sStar.X + 5dip
	sPuck.Y = LW.Graphics.Height + 12dip
	sPuck.dX = 1dip
	sPuck.dY = 5dip
	sPuck.Color.setRGBA(1, 1, 1, .75)		

End Sub

Sub LG_Resize(Width As Int, Height As Int)
	Log("--- RESIZE ---")

	'Sets the camera viewport
	Camera.Initialize
	Camera.SetToOrtho(False)
End Sub

Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)

End Sub

Sub LG_PreviewStateChange(IsPreview As Boolean)

End Sub

Sub LG_Render
	'Clears the screen and colors the background in blue
	GL.glClearColor(0, 0, 0, 1)
	GL.glClear(GL.GL10_COLOR_BUFFER_BIT)

	'If the screen is touched, the effect is stopped/restarted
	If LW.Input.JustTouched Then
		Log("X=" & LW.Input.X & ", Y=" & LW.Input.Y)
		sStar.X = Abs(LW.Input.X)
		sStar.Y = Abs(LW.Input.Y)
	End If

	'Updates the position of smileys
	LG_Update

	'Uses the coordinate system specified by the camera
	Batch.ProjectionMatrix = Camera.Combined

	'Draws the smileys (the rendering is limited to within the bounds of ClipBounds)
	Batch.Begin

	Batch.DrawTex2(texBG, 0,0,LW.Graphics.Width, LW.Graphics.Height)
	
	Batch.Color = sStar.Color
	Batch.DrawTex2(texSkater, sStar.X, sStar.Y, 64dip, 64dip)
	Batch.DrawTex2(texPuck, sPuck.X, sPuck.Y + 34dip, 14dip, 10dip)

	Batch.End

End Sub

Sub LG_Update
	'Updates the matrices of the camera
	Camera.Update

	' Timer determines if we are going to allow moving the Textures
	If allowPlay Then		
		' keep moving the player and puck until they are 2/3 way down the screen and in the middle
		If sStar.y > 180dip Then 
			sStar.x = sStar.x + sStar.dx
			sStar.y = sStar.y - sStar.dy
			sPuck.x = sPuck.x + sPuck.dx
			sPuck.y = sPuck.y - sPuck.dy			
		Else
			' if the are in the shooting position
			' then we move the player off to the right speeding up the movement
			sStar.x = sStar.x + sStar.dx * 4
			' Shoot the puck towards the goal with slightly quicker movement
			sPuck.Y = sPuck.Y - (sPuck.dy * 2)
		End If
		' If the player has now moved off the right of the screen, then we reset the coordinates
		If sStar.X > LW.Graphics.Width Then
			sStar.y = LW.Graphics.Height + 64dip
			sStar.X = 80dip	
			sPuck.X = sStar.X + 5dip
			sPuck.Y = LW.Graphics.Height + 12dip
			allowPlay = False
			playTimer.Enabled = True				
		End If		
	End If

End Sub

Sub LG_Pause
	playTimer.Enabled = False
	Log("--- PAUSE ---")
End Sub

Sub LG_Resume
	Log("--- RESUME ---")
	playTimer.Enabled = True

	CreateTextures

	allowPlay = False
	playTimer.Enabled = True
			
End Sub

Sub LG_Dispose
	Log("--- DISPOSE ---")

	'Disposes all resources used by lgTexture and lgSpriteBatch
	texSkater.dispose	
	texPuck.dispose
	texBG.dispose
	Batch.dispose
End Sub

Sub playT_tick
	' Allow player to move every tick of the timer
	allowPlay = True
	
	' Disable the timer until after the play has been completed. It gets restarted in the LG_Update sub.
	playTimer.Enabled = False
End Sub