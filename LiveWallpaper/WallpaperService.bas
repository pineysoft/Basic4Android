Type=Service
Version=3.8
B4A=true
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
	Dim texBGP As lgTexture
	Dim texBGL As lgTexture
	Dim texBG As lgTexture	
	Dim lstSmileys As List
	Dim sStar As typSmiley
	Dim sPuck As typSmiley
	Type typPoint(x As Float, y As Float)
	Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)
	Dim playTimer As Timer
	Dim allowPlay As Boolean
	Dim shootingPoint As typPoint

End Sub

Sub Service_Create
	
End Sub

Sub GetShootingPosition() As typPoint
	Dim newPoint As typPoint
	newPoint.Initialize
	If texBG = texBGP Then
		Log("Shooting Poistion for Portrait")
		newPoint.x = LW.Graphics.Width / 2
		newPoint.y = LW.Graphics.Height * .2
	Else
		newPoint.x = LW.Graphics.Width * .7
		newPoint.y = LW.Graphics.Height / 2
	End If
	
	Return newPoint
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
	texPuck.Initialize("puck.png")
	texBGP.Initialize("hockeyboardportrait_stars.png")
	texBGL.Initialize("hockeyboardlandscape_stars.png")
	
	' Set it to portrait background
	texBG = texBGP
	
	'Creates the list of smileys
	sStar.Initialize

End Sub

Sub CreateTextures
	'Creates the list of smileys
	lstSmileys.Initialize
	sStar.Initialize
	sPuck.Initialize	
	shootingPoint = GetShootingPosition
	
	If texBG = texBGP Then
		sStar.X = 80dip
		sStar.Y = LW.Graphics.Height + 64dip
		sStar.dY = 5dip		
		Log("Sstar.dx = (" & shootingPoint.x & " - " & sStar.X & ") / ((" & shootingPoint.y & " - " & sStar.Y & ") / " & sStar.dY)
		sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)
		sStar.Color.setRGBA(1, 1, 1, .75)
		
		sPuck.X = sStar.X + 5dip
		sPuck.Y = LW.Graphics.Height + 12dip
		sPuck.dX = sStar.dX
		sPuck.dY = sStar.dY
		sPuck.Color.setRGBA(1, 1, 1, .75)	
	Else
		sStar.X = 0
		sStar.Y = LW.Graphics.Height
		sStar.dX = 5dip
		sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)
		sStar.Color.setRGBA(1, 1, 1, .75)
		
		sPuck.X = sStar.X
		sPuck.Y = LW.Graphics.Height - 32dip
		sPuck.dX = sStar.dx
		sPuck.dY = sStar.dY
		sPuck.Color.setRGBA(1, 1, 1, .75)			
	End If
	Log("X Movement is " & sStar.dX)
End Sub

Sub LG_Resize(Width As Int, Height As Int)
	Log("--- RESIZE ---")

	'Sets the camera viewport
	Camera.Initialize
	Camera.SetToOrtho(False)
	
	If LW.Graphics.Width > LW.Graphics.Height Then
		If texBG = texBGP Then
			texBG = texBGL		
		End If		
	Else
		If texBG = texBGL Then
			texBG = texBGP
		End If
	End If
	
	shootingPoint = GetShootingPosition
End Sub

Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)

End Sub

Sub LG_PreviewStateChange(IsPreview As Boolean)

End Sub

Sub LG_Render
	'Clears the screen and colors the background in blue
	GL.glClearColor(0, 0, 0, 1)
	GL.glClear(GL.GL10_COLOR_BUFFER_BIT)

	'If the screen is touched, the hockey player starts from there
	If LW.Input.JustTouched Then
		sStar.X = Abs(LW.Input.X)
		sStar.Y = LW.Graphics.Height - Abs(LW.Input.Y)
		sPuck.X = sStar.X - 5dip
		sPuck.Y =  LW.Graphics.Height - Abs(LW.Input.Y) - 48dip
		If texBG = texBGP Then
			sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)
			sPuck.dX = sStar.dX
		Else
			sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)		
			sPuck.dY = sStar.dY
		End If
		allowPlay = True
		playTimer.Enabled = False
	End If

	'Updates the position of smileys
	LG_Update

	'Uses the coordinate system specified by the camera
	Batch.ProjectionMatrix = Camera.Combined

	'Draws the smileys (the rendering is limited to within the bounds of ClipBounds)
	Batch.Begin
	
	Batch.DrawTex2(texBG, 0,0,LW.Graphics.Width, LW.Graphics.Height)
	
	Batch.DrawTex2(texSkater, sStar.X, sStar.Y, 64dip, 64dip)
	Batch.DrawTex2(texPuck, sPuck.X, sPuck.Y + 34dip, 14dip, 10dip)
	
	Batch.End

End Sub

Sub LG_Update
	'Updates the matrices of the camera
	Camera.Update

	' Timer determines if we are going to allow moving the Textures
	If allowPlay Then		
		If texBG = texBGP Then
			' keep moving the player and puck until they are 2/3 way down the screen and in the middle
			If sStar.y > shootingPoint.y Then 
				sStar.x = sStar.x + sStar.dx
				sStar.y = sStar.y - sStar.dy
				sPuck.x = sPuck.x + sPuck.dx
				sPuck.y = sPuck.y - sPuck.dy			
			Else
				' if the are in the shooting position
				' then we move the player off to the right speeding up the movement
				If sStar.dX > 0 Then
					sStar.x = sStar.x + 6
				Else
					sStar.x = sStar.x - 6
				End If
				' Shoot the puck towards the goal with slightly quicker movement
				sPuck.Y = sPuck.Y - (sPuck.dy * 2)
			End If
			' If the player has now moved off the right of the screen, then we reset the coordinates
			If sStar.X > LW.Graphics.Width OR sStar.X < 0 Then
				sStar.X = 80dip
				sStar.Y = LW.Graphics.Height + 64dip
				sPuck.X = sStar.X + 5dip
				sPuck.Y = LW.Graphics.Height + 12dip
				sStar.dX = (shootingPoint.x - sStar.X) / ((sStar.Y - shootingPoint.y) / sStar.dY)				
				sPuck.dX = sStar.dX
				allowPlay = False
				playTimer.Enabled = True				
			End If	
		Else
			' keep moving the player and puck until they are 2/3 way down the screen and in the middle
			If sStar.x < shootingPoint.x Then 
				sStar.x = sStar.x + sStar.dx
				sStar.y = sStar.y - sStar.dy
				sPuck.x = sPuck.x + sPuck.dx
				sPuck.y = sPuck.y - sPuck.dy			
			Else
				' if the are in the shooting position
				' then we move the player off to the right speeding up the movement
				If sStar.dY > 0 Then
					sStar.Y = sStar.Y + 6
				Else
					sStar.Y = sStar.Y -6
				End If
				' Shoot the puck towards the goal with slightly quicker movement
				sPuck.X = sPuck.X + (sPuck.dX * 2)
			End If
			' If the player has now moved off the right of the screen, then we reset the coordinates
			If sStar.Y < 0 OR sStar.Y > LW.Graphics.Height Then
				sStar.y = LW.Graphics.Height
				sStar.X = 0dip	
				sPuck.X = sStar.X + 5dip
				sPuck.Y = LW.Graphics.Height - 48dip
				sStar.dY = (shootingPoint.Y - sStar.Y) / ((sStar.X - shootingPoint.X) / sStar.dX)
				sPuck.dY = sStar.dy
				allowPlay = False
				playTimer.Enabled = True				
			End If			
		End If
	End If

End Sub

Sub LG_Pause
	playTimer.Enabled = False
	allowPlay = False
	Log("--- PAUSE ---")
End Sub

Sub LG_Resume
	Log("--- RESUME ---")
	playTimer.Enabled = True
	allowPlay = False
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
	texBGL.dispose
	texBGP.dispose
	Batch.dispose
End Sub

Sub playT_tick
	' Allow player to move every tick of the timer
	allowPlay = True
	
	' Disable the timer until after the play has been completed. It gets restarted in the LG_Update sub.
	playTimer.Enabled = False
End Sub