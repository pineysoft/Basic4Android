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
	Dim Camera As lgOrthographicCamera
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
	'...
End Sub

Sub LG_Resize(Width As Int, Height As Int)
	'Sets the camera viewport
	Camera.Initialize
	Camera.SetToOrtho2(False, Width, Height)
End Sub

Sub LG_OffsetChange(X_Offset As Float, Y_Offset As Float, X_OffsetStep As Float, Y_OffsetStep As Float, X_PixelOffset As Int, Y_PixelOffset As Int)
	'...
End Sub

Sub LG_PreviewStateChange(IsPreview As Boolean)
	'...
End Sub

Sub LG_Render
	'Clears the screen
	GL.glClear(GL.GL10_COLOR_BUFFER_BIT)

	'Processes touch events
	If LW.Input.JustTouched Then
		'...
	End If

	'Updates
	LG_Update

	'Draws
	'...
End Sub

Sub LG_Update
	'Updates the matrices of the camera
	Camera.Update

	'...
End Sub

Sub LG_Pause
End Sub

Sub LG_Resume
End Sub

Sub LG_Dispose
	'Disposes all resources
	'...
End Sub
