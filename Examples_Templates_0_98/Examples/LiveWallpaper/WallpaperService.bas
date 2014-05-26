Type=Service
Version=3.2
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
	Dim ScissorStack As lgScissorStack
	Dim Scissors As lgMathRectangle
	Dim ClipBounds As lgMathRectangle
	Dim ClipBdsValue, ClipBdsInc As Float
	Dim Enabled As Boolean
	Dim texSmiley As lgTexture
	Dim lstSmileys As List
	Type typSmiley(X As Float, Y As Float, dX As Float, dY As Float, Color As lgColor)
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
End Sub

Sub Service_Destroy
	'This event is never raised by libGDX
End Sub

Sub LG_Create
	Log("--- CREATE ---")

	'Initializes the sprite batcher
	Batch.Initialize

	'Loads the smiley image (64x64)
	texSmiley.Initialize("smiley.png")

	'Creates the list of smileys
	CreateSmileys

	'Sets the initial value and the increment of ClipBounds
	ClipBdsValue = 0.2
	ClipBdsInc = 0.005

	'The clipping is disabled by default
	Enabled = False
End Sub

Sub CreateSmileys
	'Creates the list of smileys
	lstSmileys.Initialize
	Dim MU As lgMathUtils
	For i = 1 To 100
		Dim Smiley As typSmiley
		Smiley.Initialize
		Smiley.X = Rnd(64dip * 0.5, LW.Graphics.Width - (64dip * 1.5))
		Smiley.Y = Rnd(64dip * 0.5, LW.Graphics.Height - (64dip * 1.5))
		Smiley.dX = Rnd(3, 6)
		Smiley.dY = Rnd(3, 6)
		If MU.RandomBoolean Then Smiley.dX = -Smiley.dX
		If MU.RandomBoolean Then Smiley.dY = -Smiley.dY
		Smiley.Color.setRGBA(MU.RandomFloat2(0.2, 1), MU.RandomFloat2(0.2, 1), MU.RandomFloat2(0.2, 1), 1)
		lstSmileys.Add(Smiley)
	Next
End Sub

Sub LG_Resize(Width As Int, Height As Int)
	Log("--- RESIZE ---")

	'Sets the camera viewport
	Camera.Initialize
	Camera.SetToOrtho(False)
End Sub

Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)
	Log("OffsetChange: " & X_Offset & "," & Y_Offset & "  " & X_OffsetStep & "," & Y_OffsetStep & "  " & X_PixelOffset & "," & Y_PixelOffset)
End Sub

Sub LG_PreviewStateChange(IsPreview As Boolean)
	Log("PreviewState = " & IsPreview)
End Sub

Sub LG_Render
	'Clears the screen and colors the background in blue
	GL.glClearColor(0, 0, 0.4, 1)
	GL.glClear(GL.GL10_COLOR_BUFFER_BIT)

	'If the screen is touched, the effect is stopped/restarted
	If LW.Input.JustTouched Then
		Enabled = Not(Enabled)
		Log("X=" & LW.Input.X & ", Y=" & LW.Input.Y)
	End If

	'Updates the position of smileys
	LG_Update

	'Uses the coordinate system specified by the camera
	Batch.ProjectionMatrix = Camera.Combined

	'Draws the smileys (the rendering is limited to within the bounds of ClipBounds)
	Batch.Begin
	If Enabled Then
		Dim X As Int = ClipBdsValue * LW.Graphics.Width
		Dim Y As Int = ClipBdsValue * LW.Graphics.Height
		ClipBounds.Set(X, Y, LW.Graphics.Width - (X * 2), LW.Graphics.Height - (Y * 2))
		ScissorStack.CalculateScissors(Camera, 0, 0, Camera.ViewportWidth, Camera.ViewportHeight, Batch.TransformMatrix, ClipBounds, Scissors)
		ScissorStack.PushScissors(Scissors)
	End If

	Dim Smiley As typSmiley
	For i = 0 To lstSmileys.Size - 1
		Smiley = lstSmileys.Get(i)
		Batch.Color = Smiley.Color
		Batch.DrawTex2(texSmiley, Smiley.X, Smiley.Y, 64dip, 64dip)
	Next

	If Enabled Then
		Batch.Flush
		ScissorStack.PopScissors
	End If
	Batch.End

	'Increases/decreases the bounds
	If Enabled Then
		ClipBdsValue = ClipBdsValue + ClipBdsInc
		If ClipBdsValue < 0.03 OR ClipBdsValue > 0.45 Then ClipBdsInc = -ClipBdsInc
	End If
End Sub

Sub LG_Update
	'Updates the matrices of the camera
	Camera.Update

	'Computes the new position of each smiley
	'When an edge is reached, the smiley moves in the opposite direction
	For i = 0 To lstSmileys.Size - 1
		Dim Smiley As typSmiley = lstSmileys.Get(i)
		Smiley.x = Smiley.x + Smiley.dx
		Smiley.y = Smiley.y + Smiley.dy
		If Smiley.x < Abs(Smiley.dx) OR Smiley.x >= LW.Graphics.Width - 64dip Then
			Smiley.dx = -Smiley.dx
		End If
		If Smiley.y < Abs(Smiley.dy) OR Smiley.y >= LW.Graphics.Height - 64dip Then
			Smiley.dy = -Smiley.dy
		End If
	Next
End Sub

Sub LG_Pause
	Log("--- PAUSE ---")
End Sub

Sub LG_Resume
	Log("--- RESUME ---")

	'Rebuilds the list of smileys
	CreateSmileys

	'Resets the initial value and the increment of ClipBounds
	ClipBdsValue = 0.2
	ClipBdsInc = 0.005
End Sub

Sub LG_Dispose
	Log("--- DISPOSE ---")

	'Disposes all resources used by lgTexture and lgSpriteBatch
	texSmiley.dispose	
	Batch.dispose
End Sub
