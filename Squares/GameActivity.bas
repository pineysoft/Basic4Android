Type=Activity
Version=3.8
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private currentPlayer As Short
	Private numberOfPlayers As Int
	Private numberOfDroids As Int
	Private playerColours As List ' List of Ints
	Private inGame As Boolean
	Private giggleSound As Int
	Private displayingDebug As Int
	Dim sounds As SoundPool
	Dim SPConstants As Constants
	Dim difficulty As String
	Dim vibrate As PhoneVibrate
	Dim soundsOn As Boolean = True
	Dim GameMode As String
	Dim IsMaster As Boolean = False
	Dim myTurn As Boolean
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private gameSquares(,) As GameSquare
	Dim gameTurns As List	
	Private gameWidth As Int = 7
	Private gameHeight As Int = 9
	Private columnSpacing As Int 
	Private rowSpacing As Int
	Private panel1 As Panel
	Private canv As Canvas
	Private players As List
	Private checkBoxImages As List
	Private lblPlayer1 As Label
	Private lblPlayer1Image As Label
	Private lblPlayer2 As Label
	Private lblPlayer2Image As Label
	Private lblPlayer3 As Label
	Private lblPlayer3Image As Label
	Private lblPlayer4 As Label
	Private lblPlayer4Image As Label
	Private btnContinue As Button
	Private spnPlayers As Spinner
	Private sbColumns As SeekBar
	Private sbRows As SeekBar
	Private pnlStartScreen As Panel
	Private icon1 As ImageView
	Private icon2 As ImageView
	Private icon3 As ImageView
	Private icon4 As ImageView
	Private lblColumns As Label
	Private lblPlayers As Label
	Private lblRows As Label
	Private pnlBase As Panel
	Private btnCurrPlayer As Button
	Private icon5 As ImageView
	Private icon6 As ImageView
	Private pnlDisplay As Panel
	Private pnlOuter As Panel
	Private imageIcon As ImageView
	Private lblWinner As Label
	Dim anim1 As AnimationPlus
	Dim anim2 As AnimationPlus
	Dim anim3 As AnimationPlus
	Dim anim4 As AnimationPlus
	Dim anim5 As AnimationPlus
	Dim anim6 As AnimationPlus	
	Dim animShading As AnimationPlus
	Dim animPanel1 As AnimationPlus
	Dim animPanel2 As AnimationPlus
	Dim animStartScr As AnimationPlus
	Dim AnimGameScr As AnimationPlus	
	Private spnDroids As Spinner
	Private lblScores As Label
	Private spnDifficulty As Spinner
	Private pnlSelectionLeft As Panel
	Private pnlSelectionRight As Panel
	Private pnlShading As Panel
	Private lblDifficulty As Label
	Private lblDroids As Label
	Private lblSound As Label
	Private lblDebugDisplay As Label
	Type MyChain(square As GameSquare, chainCount As Int)
	Dim start1 As Int
	Dim start2 As Int
	Dim start3 As Int
	Dim start4 As Int
	Dim start5 As Int
	Dim start6 As Int	
	Private chkSounds As ImageView
	Private lblGameModeDescr As Label
	Private txtMessage As EditText
	Private btnStartGame As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	SPConstants.Initialize
	If FirstTime Then
		Activity.LoadLayout("StartScreen")
		If NetConn.BTStatus = "Connected" Then
			GameMode = SPConstants.GAMETYPE_MODE_LOC
		End If		
		DisplayFrontScreen
		AnimateCharacters	
		CreateColours
		LoadImages
		LoadSpinners
		UpdateLabels
		RestoreDefaults	
		InitialiseSounds
		UpdateGameModeSettings
	Else
		Activity.LoadLayout("StartScreen")
		AnimateCharacters		
		CreateColours
		LoadImages
		LoadSpinners
		RestoreDefaults
		UpdateGameModeSettings
	End If
End Sub

Sub SendMessage(msg As String)
	CallSubDelayed2(NetConn,"SendMessage",msg)
End Sub
Sub ShowChatWindow(msg As Boolean)

	txtMessage.Visible = msg
	lblPlayer3.Visible = (msg = False)
	lblPlayer3Image.Visible = (msg = False)
	lblPlayer4.Visible = (msg = False)
	lblPlayer4Image.Visible = (msg = False)		
	
End Sub

Sub UpdateGameModeSettings
	lblGameModeDescr.Text = "Local"
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
		If IsMaster Then 
			myTurn = True
			Dim settings As String = GetSettings					
			SendMessage(settings)				
		Else
			myTurn = False
		End If
		If GameMode = SPConstants.GAMETYPE_MODE_BT Then
			lblGameModeDescr.Text = "Bluetooth"
		Else
			lblGameModeDescr.Text = "Wifi"
		End If
	Else
		myTurn = True
	End If
End Sub
Sub GetSettings() As String
	Dim settings As String
	
	settings = SPConstants.GAME_MSG_SET_SETTINGS & "," & sbRows.Value & "," & sbColumns.Value & "," & spnDifficulty.SelectedItem
	
	Return settings
End Sub

Sub SetSettings(settings As String) 
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(settings,",")
	
	sbRows.Value = lstSettings.Get(1)
	sbColumns.Value = lstSettings.Get(2)
	
	For iLoop = 0 To spnDifficulty.Size - 1
		If spnDifficulty.GetItem(iLoop) = lstSettings.Get(3) Then
			spnDifficulty.SelectedIndex = iLoop
			Exit
		End If
	Next	
End Sub

Sub AStream_NewData (Buffer() As Byte)
	ProcessStreamMessage(BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
End Sub

Sub AStream_Error
	ToastMessageShow("Connection is broken.", True)
	Activity.Finish		
End Sub

Sub AStream_Terminated
	AStream_Error
End Sub

Sub UpdateGameMessages(msg As String)
	ProcessStreamMessage(msg)
End Sub
Sub ProcessStreamMessage(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	Dim selection As String = lstSettings.Get(0)
	
	ToastMessageShow(msg,False)
	Select selection
		' "G"
		Case SPConstants.GAME_MSG_SET_GAME_TYPE
			ProcessGameMode(msg)
		' "S"
		Case SPConstants.GAME_MSG_SET_SETTINGS
			SetSettings(msg)
		' "T"
		Case SPConstants.GAME_MSG_PROCESS_TURN
			ProcessStreamTurn(msg)
		' "G"
		Case SPConstants.GAME_MSG_START_GAME
			ReverseAnimate			
		' "P"
		Case SPConstants.GAME_MSG_SET_PLAYERS
			ProcessStreamPlayers(msg)			
		' "X"
		Case SPConstants.GAME_MSG_CLOSE_GAME
			ToastMessageShow("The Other Player closed the Game", False)
			CloseGame	
		' "C"
		Case SPConstants.GAME_MSG_PROCESS_CHAT
			ProcessChat(msg)
		' "GC"
		Case SPConstants.GAME_MSG_SET_COLUMNS
			ProcessColumnChange(msg)
		' "GR"
		Case SPConstants.GAME_MSG_SET_ROWS
			ProcessRowChange(msg)
		' "X2"
		Case SPConstants.GAME_MSG_CLOSE_START
			CloseStartScreen
	End Select
End Sub

Sub ProcessGameMode(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	GameMode = lstSettings.Get(1)
	UpdateGameModeSettings
End Sub
Sub ProcessStreamTurn(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	Dim currentSquare As GameSquare = gameSquares(lstSettings.Get(2), lstSettings.Get(3))
	
	CalculateMove2(currentSquare, lstSettings.Get(4))
	
	Dim value As Int = lstSettings.Get(1)
	If value = 0 Then
		myTurn = True
	Else
		myTurn = False
	End If
End Sub

Sub ProcessStreamPlayers(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	
	If GameMode = SPConstants.GAMETYPE_MODE_BT AND players.IsInitialized = False Then
		CreatePlayers
	End If
	
	Dim iLoop As Int
	For iLoop = 0 To 1
		Dim bm As Bitmap
		Dim ply As Player = players.Get(iLoop)
		bm.Initialize(File.DirAssets, "monsterImage" & (lstSettings.get(iLoop + 1)) & ".png")		
		ply.PlayerImage = bm
		ply.PlayerImageNum = lstSettings.get(iLoop + 1)
		Dim vImage As View = GetViewByTag1(pnlBase, "I" & (iLoop + 1))
		If vImage Is Label Then
			Dim lblImage As Label = vImage	
			lblImage.SetBackgroundImage(ply.PlayerImage)
		End If	
		If iLoop = 0 Then
			btnCurrPlayer.SetBackgroundImage(ply.PlayerImage)
		End If
	Next
End Sub

Sub ProcessChat(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	
	ToastMessageShow(lstSettings.Get(1),False)
End Sub

Sub ProcessRowChange(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	Dim value As Int = lstSettings.Get(1)
	sbRows.value = value
	lblRows.Text = (value + 4)
End Sub

Sub ProcessColumnChange(msg As String)
	Dim strF As StringFunctions
	strF.Initialize
	Dim lstSettings As List = strF.Split(msg,",")
	Dim value As Int = lstSettings.Get(1)
	sbColumns.value = value
	lblColumns.Text = (value + 4)
End Sub

Sub ShowSplashScreen
	pnlShading.Visible = False
	pnlSelectionLeft.Left = 0 - pnlSelectionLeft.Width
	pnlSelectionRight.Left = 100%x
	UpdateLabels
	AnimateCharacters
End Sub

Sub AnimateCharacters
	ResetIcons
	anim1.InitializeTranslate("", 0,0,75%x+(icon1.Width/2),25%y-start1)
	anim1.Duration = 1000
	anim1.StartOffset = 0
	anim1.PersistAfter = False
	anim1.Start(icon1)
	anim2.InitializeTranslate("", 0,0,-(75%x+(icon2.width/2)),45%y-start2)
	anim2.Duration = 1000
	anim2.StartOffset = 0
	anim2.PersistAfter = False
	anim2.Start(icon2)
	anim3.InitializeTranslate("", 0,0,50%x+(icon3.width/2),25%y-start3)
	anim3.Duration = 1000
	anim3.StartOffset = 0
	anim3.PersistAfter = False
	anim3.Start(icon3)
	anim4.InitializeTranslate("", 0,0,-(50%x+(icon4.width/2)),45%y-start4)
	anim4.Duration = 1000	
	anim4.StartOffset = 0
	anim4.PersistAfter	 = False
	anim4.Start(icon4)
	anim5.InitializeTranslate("", 0,0,25%x+(icon5.width/2),25%y-start5)
	anim5.Duration = 1000
	anim5.StartOffset = 0
	anim5.PersistAfter = False
	anim5.Start(icon5)
	anim6.InitializeTranslate("LastIcon", 0,0,-(25%x+(icon6.width/2)),45%y-start6)
	anim6.Duration = 1000	
	anim6.StartOffset = 0
	anim6.PersistAfter = False
	anim6.Start(icon6)	

End Sub

Sub ReverseAnimate
	
	anim1.InitializeTranslate("", 0,0,-(75%x+(icon1.Width/2)),start1-25%y)
	anim1.Duration = 700
	anim1.PersistAfter = True
	anim1.Start(icon1)
	anim2.InitializeTranslate("", 0,0,75%x+(icon2.Width/2),start2-45%y)
	anim2.Duration = 700
	anim2.PersistAfter = True
	anim2.Start(icon2)
	anim3.InitializeTranslate("", 0,0,-(50%x+(icon3.Width/2)),start3-25%y)
	anim3.Duration = 700
	anim3.PersistAfter = True
	anim3.Start(icon3)
	anim4.InitializeTranslate("", 0,0,50%x+(icon4.Width/2),start4-45%y)
	anim4.Duration = 700	
	anim4.PersistAfter = True
	anim4.Start(icon4)
	anim5.InitializeTranslate("", 0,0,-(25%x+(icon5.Width/2)),start5-25%y)
	anim5.Duration = 700
	anim5.PersistAfter = True
	anim5.Start(icon5)
	anim6.InitializeTranslate("LastIconEnd", 0,0,25%x+(icon6.Width/2),start6-45%y)
	anim6.Duration = 700	
	anim6.PersistAfter = True
	anim6.Start(icon6)	
End Sub

Sub LastIconEnd_AnimationEnd
	Activity.LoadLayout("layout1")
	Activity.LoadLayout("winnerScreen")
	pnlOuter.Left = -100%x
	InitGamePlay
	SaveDefaults
	inGame = True
	AnimateGameScreens
End Sub

Sub AnimateGameScreens
	animStartScr.InitializeTranslate("",0,0,-100%x,0)
	animStartScr.Duration = 400
	animStartScr.PersistAfter = True
	animStartScr.Start(pnlStartScreen)
	
	AnimGameScr.InitializeTranslate("GameScr",100%x,0,0,0)
	AnimGameScr.Duration = 400
	AnimGameScr.PersistAfter = True
	AnimGameScr.Start(pnlBase)	
End Sub

Sub GameScr_AnimationEnd
	animStartScr.Stop(pnlStartScreen)
	pnlStartScreen.Left = -100%x
	AnimGameScr.Stop(pnlBase)
	pnlBase.Left = 0dip
End Sub

Sub LastIcon_AnimationEnd
	Log("Icon1 " & icon1.Top & " : " & icon1.left)
	anim1.Stop(icon1)
	anim2.Stop(icon2)
	anim3.Stop(icon3)
	anim4.Stop(icon4)
	anim5.Stop(icon5)
	anim6.Stop(icon6)
	Log("Icon1 " & icon1.Top & " : " & icon1.left)
	SetIconAtPosition(icon1,75%x-(icon1.Width/2),25%y)
	Log("Icon1 " & icon1.Top & " : " & icon1.left)
	SetIconAtPosition(icon2,25%x-(icon2.Width/2),45%y)
	SetIconAtPosition(icon3,50%x-(icon3.Width/2),25%y)
	SetIconAtPosition(icon4,50%x-(icon4.Width/2),45%y)
	SetIconAtPosition(icon5,25%x-(icon5.Width/2),25%y)
	SetIconAtPosition(icon6,75%x-(icon6.Width/2),45%y)
	If pnlSelectionLeft.Left <= 0dip Then
		AnimateShading
	End If
End Sub

Sub SetIconAtPosition(icon As ImageView,left As Int, top As Int)
	icon.left = left
	icon.top = top
End Sub

Sub ResetIcons
	start1 = Rnd(0,100%y)
	start2 = Rnd(0,100%y)
	start3 = Rnd(0,100%y)
	start4 = Rnd(0,100%y)
	start5 = Rnd(0,100%y)
	start6 = Rnd(0,100%y)
	
	SetIconAtPosition(icon1,-(icon1.Width),start1)
	SetIconAtPosition(icon2,100%x,start2)
	SetIconAtPosition(icon3,-(icon3.Width),start3)
	SetIconAtPosition(icon4,100%x,start4)
	SetIconAtPosition(icon5,-(icon5.Width),start5)
	SetIconAtPosition(icon6,100%x,start6)
End Sub

Sub AnimateShading
	Log("StartShading Anim")
	animShading.InitializeScaleCenter("Shading", 0.1,0.1,1,1, pnlShading)
	animShading.Duration = 750
	animShading.PersistAfter = True
	pnlShading.Visible = True	
	animShading.Start(pnlShading)
End Sub

Sub Shading_AnimationEnd
	Log("Start Left Panel Anim")
	AnimatePanelLeft
End Sub

Sub AnimatePanelLeft
	animPanel1.InitializeTranslate("LeftPanel",-(pnlSelectionLeft.Width),0,pnlSelectionLeft.width + 5%x,0)
	animPanel1.PersistAfter = True
	animPanel1.Duration = 500
	animPanel1.Start(pnlSelectionLeft)
End Sub

Sub LeftPanel_AnimationEnd
	Log("Start Right Panel Anim")
	AnimatePanelRight
End Sub
Sub AnimatePanelRight
	Log(100%x & " " & pnlSelectionRight.Width & " " & pnlSelectionRight.Top)
	animPanel2.InitializeTranslate("RightPanel",100%x,0,-45%x,0)
	animPanel2.PersistAfter = True
	animPanel2.Duration = 500
	animPanel2.Start(pnlSelectionRight)
End Sub

Sub RightPanel_AnimationEnd
	animPanel1.Stop(pnlSelectionLeft)
	pnlSelectionLeft.Top = pnlShading.Top
	pnlSelectionLeft.Left = 5%x
	
	animPanel2.Stop(pnlSelectionRight)
	pnlSelectionRight.Top = pnlShading.Top
	pnlSelectionRight.Left = 55%x

End Sub

Sub CreateColours()
	playerColours.Initialize
	playerColours.Add(Colors.Yellow)
	playerColours.Add(Colors.Blue)
	playerColours.Add(Colors.Green)
	playerColours.Add(Colors.Red)	
End Sub

Sub LoadImages

	checkBoxImages.Initialize
	Dim bm As Bitmap 
	bm.Initialize(File.DirAssets, "checkboxOn.png")
	checkBoxImages.Add(bm)
	Dim bm2 As Bitmap
	bm2.Initialize(File.DirAssets, "checkboxOff.png")
	checkBoxImages.Add(bm2)
End Sub

Sub LoadSpinners

	spnPlayers.AddAll(Array As Int(2,3,4))
	spnDroids.AddAll(Array As Int(0,1))
	spnDifficulty.AddAll(Array As String("Easy","Medium","Hard"))

End Sub

Sub UpdateLabels
	lblRows.Text = "Rows : " & (sbRows.Value + 4)
	lblColumns.Text = "Columns : " & (sbColumns.Value + 4)
End Sub

Sub InitialiseSounds
	sounds.Initialize(10)
	giggleSound = sounds.Load(File.DirAssets, "Giggle1.mp3")
End Sub
Sub CreatePlayers
	Dim iLoop As Int
	players.Initialize
	Dim imagesTaken As Map
	imagesTaken.Initialize
	For iLoop = 1 To numberOfPlayers
		Dim playerVal As Player
		playerVal.Initialize("Player " & iLoop, playerColours.Get(iLoop - 1))		
		If iLoop <= numberOfPlayers - numberOfDroids Then
			playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN
		Else
			playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID
		End If
		Dim v As View = GetViewByTag1(pnlBase, "P" & iLoop)
		If v Is Label Then
			Dim lbl As Label = v
			If playerVal.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then
				If GameMode <> SPConstants.GAMETYPE_MODE_LOC  Then
					If iLoop = 1 Then						
						If IsMaster Then 
							lbl.Text = "You"
						Else
							lbl.Text = "Them"
						End If
					Else
						If IsMaster Then
							lbl.Text = "Them"
						Else
							lbl.Text = "You"
						End If
					End If						
				Else
					lbl.Text = "Player " & iLoop
				End If
			Else 
				lbl.Text = "Droid " & iLoop
			End If			
			If iLoop <= 11 Then
				Dim vImage As View = GetViewByTag1(pnlBase, "I" & iLoop)
				If vImage Is Label Then
					Dim lblImage As Label = vImage
					Dim imageNum As Int = Rnd(0,11)
					Do While imagesTaken.ContainsKey(imageNum)
						imageNum = Rnd(0,11)
					Loop
					imagesTaken.Put(imageNum, imageNum)
					Dim bm As Bitmap
					bm.Initialize(File.DirAssets, "monsterImage" & (imageNum + 1) & ".png")
					playerVal.PlayerImage = bm 'playerImages.Get(imageNum)
					playerVal.PlayerImageNum = imageNum + 1
					lblImage.SetBackgroundImage(playerVal.PlayerImage)
					If playerVal.PlayerType = SPConstants.PLAYER_TYPE_DROID Then
						lblImage.Text = "D"
					End If
				End If
			End If
		End If
		players.Add(playerVal)
	Next
	currentPlayer = 0
	Dim currPlayer As Player = players.get(currentPlayer)
	btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)
	If IsMaster Then
		btnCurrPlayer.Text = "M"
	Else
		btnCurrPlayer.Text = "S"
	End If
		
End Sub

Public Sub InitGamePlay
	gameHeight = sbRows.Value + 4
	gameWidth = sbColumns.Value + 4
	columnSpacing = panel1.Width / (gameWidth + 1)
	rowSpacing = panel1.Height / (gameHeight + 1)
	canv.Initialize(panel1)
	gameTurns.Initialize
	
	CreateBoard
	DrawBoard
	CreatePlayers	
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then
		Dim ply As Player = players.Get(0)
		Dim msg As String = SpConstants.GAME_MSG_SET_PLAYERS & "," & ply.PlayerImageNum
		ply = players.Get(1)
		SendMessage(msg & "," & ply.PlayerImageNum)
	End If
	SetDifficulty
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
		numberOfPlayers = 2
		numberOfDroids = 0
		ShowChatWindow(True)
	Else
		numberOfPlayers = spnPlayers.SelectedItem
		numberOfDroids = spnDroids.SelectedItem
		ShowChatWindow(False)
	End If	
End Sub

Public Sub SetDifficulty
	Select spnDifficulty.SelectedItem
		Case "Easy"
			difficulty = SPConstants.DIFFICULTY_EASY
		Case "Medium"
			difficulty = SPConstants.DIFFICULTY_MEDIUM	
		Case "Hard"
			difficulty = SPConstants.DIFFICULTY_HARD			
	End Select
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Panel1_Touch (Action As Int, X As Float, Y As Float)
	Select Action
    	Case Activity.ACTION_UP
		    If myTurn = True Then
				myTurn = False
				Log ("X,Y = " & X & "," & Y)
				If CalculateMove(X,Y) <> True Then
					myTurn = True
				End If
				panel1.Invalidate
			Else
				ToastMessageShow("Wait your turn...", False)
			End If
	End Select
End Sub

Sub CreateBoard
	Dim colLoop As Int 
	Dim rowLoop As Int
	Dim x As Int = columnSpacing / 2
	Dim y As Int = rowSpacing / 2
	Dim gameSquares(gameHeight,gameWidth) As GameSquare
	
	For rowLoop = 0 To gameHeight - 1
		For colLoop = 0 To gameWidth - 1
			Dim square As GameSquare
			Dim lblSquare As Label
			lblSquare.Initialize("")
			lblSquare.Gravity = Gravity.FILL		
			square.Initialize(x,y,columnSpacing,rowSpacing,rowLoop,colLoop)
			square.fillLabel = lblSquare
			panel1.AddView(lblSquare, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)				
			gameSquares(rowLoop, colLoop) = square
			x = x + columnSpacing
		Next
		x = columnSpacing / 2
		y = y + rowSpacing
	Next
End Sub

Sub DrawBoard
	Dim square As Rect
	For rowLoop = 0 To gameHeight - 1
		For colLoop = 0 To gameWidth - 1
			Dim gSquare As GameSquare = gameSquares(rowLoop, colLoop)
			
			square.Initialize(gSquare.TopLeft.Pos1-4dip,gSquare.TopLeft.Pos2-4dip,gSquare.TopLeft.Pos1+4dip,gSquare.TopLeft.Pos2+4dip)
			canv.DrawRect(square, Colors.LightGray, True, 1dip)
			square.Initialize(gSquare.TopRight.Pos1-4dip,gSquare.TopRight.Pos2-4dip,gSquare.TopRight.Pos1+4dip,gSquare.TopRight.Pos2+4dip)
			canv.DrawRect(square, Colors.LightGray, True, 1dip)
			square.Initialize(gSquare.BottomLeft.Pos1-4dip,gSquare.BottomLeft.Pos2-4dip,gSquare.BottomLeft.Pos1+4dip,gSquare.BottomLeft.Pos2+4dip)
			canv.DrawRect(square, Colors.LightGray, True, 1dip)
			square.Initialize(gSquare.BottomRight.Pos1-4dip,gSquare.BottomRight.Pos2-4dip,gSquare.BottomRight.Pos1+4dip,gSquare.BottomRight.Pos2+4dip)
			canv.DrawRect(square, Colors.LightGray, True, 1dip)
			Log("Drawing " & rowLoop & ", " & colLoop)
		Next
	Next	
End Sub

Sub CalculateMove(xCoord As Int, yCoord As Int) As Boolean
	'canv.DrawCircle(xCoord,yCoord,2dip,Colors.Red,True,1dip)
	'panel1.Invalidate
	Dim touchPoint As Point
	Dim chosenSide As Int
	touchPoint.Initialize(xCoord, yCoord)
	Dim currentSquare As GameSquare = FindSelectedSquare(xCoord, yCoord)
	
	chosenSide = currentSquare.CalculateClosestEdge(touchPoint)
	Log(chosenSide)

	' Don't go any futher as this side is already taken
	If currentSquare.IsSideTaken(chosenSide) Then Return False
	
	Dim itemsClosed As Int = CalculateMove2(currentSquare, chosenSide)	

	If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
		Dim move As String = SPConstants.GAME_MSG_PROCESS_TURN & "," & itemsClosed & "," & currentSquare.RowPos & "," & currentSquare.ColPos & "," & chosenSide				
		SendMessage(move)	
	Else
		myTurn = True
	End If
	
	Return True
End Sub

Sub CalculateMove2(currentSquare As GameSquare, chosenSide As Int) As Int
	Dim currPlayer As Player = players.Get(currentPlayer)
	
	UpdateTurn(canv, currentSquare, chosenSide)
	
	' We got this far, so we can now take the side
	currentSquare.TakeSide(canv,chosenSide)
	
	' And mark the square on the other side
	MarkOtherSide2(currentSquare, chosenSide)
	
	' Once the side has been taken we can check to see if it closed any squares
	Dim numberClosed As Int = CloseCompletedSquares(currPlayer)
	
	If numberClosed = 0 Then
		UpdatePlayerNumber
		currPlayer = players.Get(currentPlayer)
		btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)
		panel1.Invalidate
		Do While currPlayer.PlayerType = SPConstants.PLAYER_TYPE_DROID
			btnCurrPlayer.Text = "D"
			MakeDroidMove(currPlayer)
			UpdatePlayerNumber
			currPlayer = players.Get(currentPlayer)
		Loop
		btnCurrPlayer.Text = ""
		btnCurrPlayer.SetBackgroundImage(currPlayer.PlayerImage)		
	Else
		currPlayer.Score = currPlayer.Score + numberClosed
		If soundsOn Then sounds.Play(giggleSound,1,1,1,1,0)
		If currPlayer.Score > 0 Then
			UpdateScore(currPlayer)
		End If
		myTurn = True
	End If
	
	panel1.Invalidate
	
	CheckAndDisplayWinnerScreen
	
	Return numberClosed
	
End Sub

Sub UpdateScore(currPlayer As Player)
	Dim temp As View = GetViewByTag1(pnlBase, "P" & (currentPlayer + 1))
	If temp Is Label Then
		Dim lbl As Label = temp
		If currPlayer.PlayerType = SPConstants.PLAYER_TYPE_HUMAN Then
			lbl.Text = "Player " & (currentPlayer + 1) & " : " & currPlayer.Score
		Else
			lbl.Text = "Droid " & (currentPlayer + 1) & " : " & currPlayer.Score
		End If
	End If
End Sub

Sub UpdateTurn(cnv As Canvas, currentSquare As GameSquare, chosenSide As Int)
	Dim lastTurn As Turn
	Dim newTurn As Turn
	
	' If there was a previous turn then draw over the line with the gray colour
	If gameTurns.Size > 0 Then
		lastTurn = gameTurns.Get(gameTurns.Size - 1)
		lastTurn.Square.DrawEdge2(canv,lastTurn.Edge,Colors.LightGray)
	End If
	
	' Store this move
	newTurn.Initialize(currentSquare, chosenSide, currentPlayer)
	
	' Add to the list of GameTurns
	gameTurns.Add(newTurn)
End Sub
Sub UpdatePlayerNumber
	currentPlayer = currentPlayer + 1
	If currentPlayer > numberOfPlayers - 1 Then currentPlayer = 0
End Sub

Public Sub MarkOtherSide2(currentSquare As GameSquare, side As Int)
	MarkOtherSide(currentSquare, side, True)
End Sub
Public Sub MarkOtherSide(currentSquare As GameSquare, side As Int, markTaken As Boolean)
	Dim foundCol As Int = -1
	Dim foundRow As Int = -1
	Dim updateSide As Int
	
	foundRow = currentSquare.RowPos
	foundCol = currentSquare.ColPos
	
	' Depending on the side, where will we be marking it
	Select side
		Case SPConstants.TOP_SIDE
			If foundRow > 0 Then
				foundRow = foundRow - 1
				updateSide = SPConstants.BOTTOM_SIDE
			Else
				foundRow = -1
			End If
		Case SPConstants.RIGHT_SIDE
			If foundCol < gameWidth - 1 Then
				foundCol = foundCol + 1
				updateSide = SPConstants.LEFT_SIDE 
			Else
				foundCol = -1
			
			End If
		Case SPConstants.BOTTOM_SIDE
			If foundRow < gameHeight - 1 Then
				foundRow = foundRow + 1
				updateSide = SPConstants.TOP_SIDE
			Else
				foundRow = -1
			End If			
		Case SPConstants.LEFT_SIDE
			If foundCol > 0 Then
				foundCol = foundCol - 1
				updateSide = SPConstants.RIGHT_SIDE
			Else
				foundCol = -1
			End If	
	End Select
	
	If foundRow <> -1 AND foundCol <> -1 Then
		gameSquares(foundRow,foundCol).MarkSideTaken(updateSide, markTaken)
	End If
End Sub

Sub CheckAndDisplayWinnerScreen() As Boolean
	Dim iLoop As Int
	Dim scoreText As String
	Dim temp As Player
	Dim winnerNum As Int
	Dim bestScore As Int = 0
	Dim totalScore As Int = 0
	For iLoop = 0 To players.Size - 1 
		temp = players.Get(iLoop)
		totalScore = totalScore + temp.Score		
		If temp.Score > bestScore Then
			winnerNum = iLoop
			bestScore = temp.Score
		Else If temp.Score = bestScore Then
			winnerNum = -1
		End If
	Next
	If totalScore = gameWidth  * gameHeight Then
		If pnlOuter.IsInitialized Then
			If winnerNum <> -1 Then
				lblWinner.Text = "Player " & (winnerNum + 1) & " is the winner!!!"
				Dim currPlayer As Player = players.Get(winnerNum)
				imageIcon.Bitmap = currPlayer.PlayerImage					
				pnlOuter.Left = 0dip
				imageIcon.Visible = True
			Else
				imageIcon.Visible = False
				lblWinner.Text = "IT'S A TIE"
				pnlOuter.Left = 0dip
			End If
		End If
		For iLoop = 1 To players.Size
			Dim P As Player = players.Get(iLoop - 1)
			scoreText = scoreText & "Player " & iLoop & ": " & P.Score & " " 
		Next
		lblScores.Text = scoreText
		Return True
	Else
		Return False
	End If
End Sub
Sub MakeDroidMove(currPlayer As Player)
	Dim found3s As List
	Dim found2s As List = FindAllForSides(2)
	Dim found1s As List = FindAllForSides(1)
	Dim found0s As List = FindAllForSides(0)
	Dim sideClaimed As Boolean
	Dim numberClosed As Int = 1
	Dim exitLoop As Boolean
	Dim forceAnyAlways As Boolean
	
	If difficulty = SPConstants.DIFFICULTY_EASY Then
		forceAnyAlways = True
	End If
	
	Do While exitLoop = False
		found3s = FindAllForSides(3)
		If found3s.Size > 0 Then
			TakeEasy3s(found3s, currPlayer)
			Log("Checking Doubles")
			TakeDoubles
					
			' Once the side has been taken we can check to see if it closed any squares
			Dim numberClosed As Int = CloseCompletedSquares(currPlayer)
			If numberClosed > 0 Then
				currPlayer.Score = currPlayer.Score + numberClosed
			Else
				exitLoop = True
			End If
			
			' Check to see if we have a winner yet and if we do Display the screen
			If CheckAndDisplayWinnerScreen Then
				exitLoop = True
			End If
		Else
			exitLoop = True
		End If
	Loop	
	
	If difficulty = SPConstants.DIFFICULTY_EASY Then
		If Rnd(1,3) = 1 Then
			If found2s.Size > 0 Then
				Log("Doing easy Move")
				sideClaimed = TakeSingle(found2s, True)
			End If
		End If
	End If
		
	If sideClaimed = False AND found0s.Size > 0 Then
		Log("Checking 0's")
		If TakeSingle(found0s, forceAnyAlways) = False Then
			Log("Checking 0's forced")
			sideClaimed = TakeSingle(found0s, True)
		Else
			sideClaimed = True
		End If
	End If
	If sideClaimed = False AND found1s.Size > 0 Then
		Log("Checking 1's")
		If TakeSingle(found1s , forceAnyAlways) = False Then
			If difficulty <> SPConstants.DIFFICULTY_HARD Then
				Log("Checking 1's forced")
				sideClaimed = TakeSingle(found1s, True)
			End If
		Else
			sideClaimed = True
		End If
	End If
	If sideClaimed = False Then
		Log("Checking 2's")
		If difficulty = SPConstants.DIFFICULTY_HARD Then
			TakeSingle2 ' this one checks the chain count to get the least first
		Else
			If TakeSingle(found2s, forceAnyAlways) = False Then
				Log("Checking 2's forced")			
				TakeSingle(found2s, True)
			End If		
		End If		
	End If
	
	Dim numberClosed As Int = CloseCompletedSquares(currPlayer)
	If numberClosed > 0 Then
		currPlayer.Score = currPlayer.Score + numberClosed
	End If
	
	If currPlayer.Score > 0 Then
		UpdateScore(currPlayer)
	End If
	
	CheckAndDisplayWinnerScreen	
	
End Sub

Public Sub TakeEasy3s(found3s As List, currPlayer As Player) As Boolean
	Dim closeSide As Int = -1

	Log("Checking Found 3's") 
	For Each currentSquare As GameSquare In found3s
		closeSide = -1
		Log("Row: " & currentSquare.RowPos & " Column: " & currentSquare.ColPos)
		If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then
			If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken <> 2 Then
				closeSide = SPConstants.LEFT_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.RIGHT_SIDE) = False Then
			If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken <> 2 Then
				closeSide = SPConstants.RIGHT_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.TOP_SIDE) = False Then
			If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken <> 2 Then
				closeSide = SPConstants.TOP_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.BOTTOM_SIDE) = False Then
			If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken <> 2 Then
				closeSide = SPConstants.BOTTOM_SIDE
			End If
		End If		
		
		If closeSide = -1 Then Return False
		
		' Update the turn list
		UpdateTurn(canv, currentSquare, closeSide)
		
		' We got this far, so we can now take the side
		currentSquare.TakeSide(canv,closeSide)
		
		' And mark the square on the other side
		MarkOtherSide2(currentSquare, closeSide)
	Next
	
	Log("Finished Checking Found 3's")	
	Return True
End Sub

Public Sub TakeDoubles
	Dim found3s As List 
	Dim closeSide As Int = -1
	
	found3s = FindAllForSides(3)
	
	For Each currentSquare As GameSquare In found3s
		If currentSquare.IsSideTaken(SPConstants.LEFT_SIDE) = False Then
			If currentSquare.ColPos = 0 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos - 1).sidesTaken = 2 Then
				closeSide = SPConstants.LEFT_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.RIGHT_SIDE) = False Then
			If currentSquare.ColPos = gameWidth - 1 OR gameSquares(currentSquare.RowPos, currentSquare.ColPos + 1).sidesTaken = 2 Then
				closeSide = SPConstants.RIGHT_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.TOP_SIDE) = False Then
			If currentSquare.RowPos = 0 OR gameSquares(currentSquare.RowPos - 1, currentSquare.ColPos).sidesTaken = 2 Then
				closeSide = SPConstants.TOP_SIDE
			End If
		Else If currentSquare.IsSideTaken(SPConstants.BOTTOM_SIDE) = False Then
			If currentSquare.RowPos = gameHeight - 1 OR gameSquares(currentSquare.RowPos + 1, currentSquare.ColPos).sidesTaken = 2 Then
				closeSide = SPConstants.BOTTOM_SIDE
			End If
		End If		
		
		If closeSide = -1 Then Return
		
		' Update the turn list
		UpdateTurn(canv, currentSquare, closeSide)
		
		' We got this far, so we can now take the side
		currentSquare.TakeSide(canv,closeSide)
		
		' And mark the square on the other side
		MarkOtherSide2(currentSquare, closeSide)			
		
	Next
	
End Sub

Sub TakeSingle(foundValids As List, forceAny As Boolean) As Boolean
	Dim rndnum As Int
	Dim foundSide As Boolean = False
	Dim rndSide As Int
	Dim loopCount As Int
	Dim loopCountInner As Int
	
	Do While foundSide = False AND loopCount < 50
		loopCountInner = 0
		If foundValids.Size > 0 Then
			If foundValids.Size > 1 Then
				rndnum = Rnd(1, foundValids.Size)
			Else
				rndnum = 1
			End If

			Dim foundSquare As GameSquare = foundValids.Get(rndnum - 1)
			
			Do While foundSide = False AND loopCountInner < 8 
				rndSide = Rnd(1,4) - 1
				If foundSquare.IsSideTaken(rndSide) = False Then
					If forceAny = True Then
						foundSide = True					
					Else
						Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)
						If sides < 2 Then
							foundSide = True	
						End If
					End If
				End If
				loopCountInner = loopCountInner + 1
				Log("loop count inner is " & loopCountInner)
			Loop
			
			If foundSide = True Then
				UpdateTurn(canv, foundSquare, rndSide)
				
				foundSquare.TakeSide(canv, rndSide)
				
				MarkOtherSide2(foundSquare, rndSide)
			End If
		End If
		loopCount = loopCount + 1
		Log("loop count is " & loopCount)
	Loop
	
	Return foundSide
End Sub


Sub TakeSingle3(foundValids As List, forceAny As Boolean) As Boolean
	
	Dim rndnum As Int
	Dim foundSide As Boolean = False
	Dim rndSide As Int
	Dim loopCount As Int
	Dim foundSquare As GameSquare
	
	Do While foundSide = False AND loopCount < 50
		If foundValids.Size > 0 Then
			If foundValids.Size > 1 Then
				rndnum = Rnd(1, foundValids.Size)
			Else
				rndnum = 1
			End If
			
			foundSquare = foundValids.Get(rndnum - 1)
			
			Dim edges As List = foundSquare.GetFreeEdges(-1)
			If edges.Size = 0 Then
				rndSide = Rnd(0,3)
			Else If edges.Size = 1 Then
				rndSide = edges.get(0)
			Else
				rndSide = edges.Get(Rnd(0,edges.Size))				
			End If
		
			If foundSquare.IsSideTaken(rndSide) = False Then
				If forceAny = True Then
					foundSide = True					
				Else
					Dim sides As Int = GetOtherSideSquareSides(foundSquare, rndSide)
					If sides < 2 Then
						foundSide = True	
					End If
				End If
			End If
			
			If foundSide = True Then
				UpdateTurn(canv, foundSquare, rndSide)
				
				foundSquare.TakeSide(canv, rndSide)
				
				MarkOtherSide2(foundSquare, rndSide)
			End If
		End If
		loopCount = loopCount + 1
		Log("loop count is " & loopCount)
	Loop
	
	Return foundSide
	
End Sub

Sub TakeSingle2() As Boolean
	Dim rndSide As Int
	Dim foundSquare As GameSquare
	Dim chainList As List = GetChainList
	
	If chainList.Size > 0 Then
		Dim chain As MyChain = chainList.Get(0)
		foundSquare = chain.square
		
		Dim edges As List = foundSquare.GetFreeEdges(-1)
		If edges.Size > 1 Then
			rndSide = edges.Get(Rnd(0,edges.Size))
		Else
			rndSide = edges.get(0)
		End If		
		
		UpdateTurn(canv, foundSquare, rndSide)
		
		foundSquare.TakeSide(canv, rndSide)
		
		MarkOtherSide2(foundSquare, rndSide)
		
		Return True
	End If
	
	Return False
End Sub

Public Sub GetChainList() As List
	Dim found2Sides As List = FindAllForSides(2)
	Dim colPos As Int = 0
	Dim rowPos As Int = 0
	Dim chainCounter As Int
	Dim reachedEnd As Boolean
	Dim checkSquare As GameSquare
	Dim startingSquare As GameSquare
	Dim chainList As List
	Dim edgeloop As Int
	Dim checkedSquares As Map
	Dim excludeEdgeNum As Int = -1
	Dim freeEdge As Int
	chainList.Initialize
	checkedSquares.Initialize
	
	For Each square As GameSquare In found2Sides
		startingSquare = square
		excludeEdgeNum = -1
		If checkedSquares.ContainsKey(square.rowPos & "-" & square.colPos) = False Then
			checkSquare = gameSquares(square.rowPos, square.colPos)
			Dim freeEdges As List = checkSquare.GetFreeEdges(excludeEdgeNum)
			For edgeloop = 0 To freeEdges.Size - 1
				checkSquare = gameSquares(square.rowPos, square.colPos)
				chainCounter = 0
				Do While reachedEnd = False
					rowPos = checkSquare.rowPos
					colPos = checkSquare.colPos
					If chainCounter < 1 Then
						freeEdge = freeEdges.Get(edgeloop)
					Else
						freeEdge = checkSquare.GetFreeEdges(excludeEdgeNum).Get(0)
					End If
					If checkSquare.sidesTaken <> 2 Then 
						reachedEnd = True
					Else
						If freeEdge = SPConstants.TOP_SIDE Then
							If rowPos = 0 Then 
								reachedEnd = True
							Else
								rowPos = rowPos - 1
								excludeEdgeNum = SPConstants.BOTTOM_SIDE 
							End If						
						Else If freeEdge = SPConstants.RIGHT_SIDE  Then
							If colPos = gameWidth - 1 Then
								reachedEnd = True
							Else
								colPos = colPos + 1
								excludeEdgeNum = SPConstants.LEFT_SIDE 
							End If

						Else If freeEdge = SPConstants.BOTTOM_SIDE  Then
							If rowPos = gameHeight - 1 Then
								reachedEnd = True
							Else
								rowPos = rowPos + 1
								excludeEdgeNum = SPConstants.TOP_SIDE
							End If
						Else ' Left hand Side
							If colPos = 0 Then
								reachedEnd = True
							Else
								colPos = colPos - 1					
								excludeEdgeNum = SPConstants.RIGHT_SIDE
							End If
						End If
					End If
					If chainCounter > 1 AND checkSquare.rowPos = startingSquare.rowPos AND checkSquare.colPos = startingSquare.colPos Then
						reachedEnd = True
						edgeloop = freeEdges.Size - 1
					Else
						checkedSquares.Put(checkSquare.rowPos & "-" & checkSquare.colPos, checkSquare)					
						chainCounter = chainCounter + 1						
					End If
					If reachedEnd <> True Then
						If gameSquares(rowPos,colPos).sidesTaken = 2 Then
							checkSquare = gameSquares(rowPos,colPos)
						Else
							reachedEnd = True
						End If
					End If
				Loop			
				Dim newChain As MyChain
				newChain.Initialize()
				newChain.square = square
				newChain.chainCount = chainCounter
				If chainList.Size > 0 Then
					Dim tempitem As MyChain = chainList.Get(chainList.Size -1)
					If tempitem.square.rowPos = square.rowPos AND tempitem.square.colPos = square.colPos Then
						tempitem.chainCount = tempitem.chainCount + chainCounter - 1
					Else
						chainList.Add(newChain)
					End If
				Else
					chainList.Add(newChain)
				End If
					
				reachedEnd = False
			Next
			checkedSquares.Put(square.rowPos & "-" & square.colPos, square)
		End If
	Next
	chainList.SortType("chainCount", True)	
	Return chainList
End Sub

Public Sub DisplayChainCounts
	Dim chains As List = GetChainList
	Dim text As String
	For Each item As MyChain In chains
		text = text & item.square.RowPos & ", " & item.square.ColPos & " : " & item.chainCount & CRLF	
	Next
	lblDebugDisplay.text = text
	lblDebugDisplay.Left = 0dip
	lblDebugDisplay.Width = 100%x
	lblDebugDisplay.Height = 100%y
	lblDebugDisplay.Visible = True
End Sub

Sub GetOtherSideSquareSides(currentSquare As GameSquare, side As Int) As Int

	Dim checkSquare As GameSquare = SubGetOppositeSquare(currentSquare, side)
	
	If checkSquare.IsInitialized Then
		Return checkSquare.sidesTaken
	Else
		Return -1
	End If
	
End Sub

Sub SubGetOppositeSquare(square As GameSquare, side As Int) As GameSquare
	Dim otherSquare As GameSquare
	Select side
		Case SPConstants.TOP_SIDE
			If square.RowPos > 0 Then
				otherSquare = gameSquares(square.RowPos - 1, square.ColPos)
			End If
		Case SPConstants.RIGHT_SIDE
			If square.ColPos < gameWidth - 1 Then
				otherSquare = gameSquares(square.RowPos, square.ColPos + 1)
			End If		
		Case SPConstants.BOTTOM_SIDE
			If square.RowPos < gameHeight - 1 Then
				otherSquare = gameSquares(square.RowPos + 1, square.ColPos)
			End If		
		Case SPConstants.LEFT_SIDE
			If square.RowPos > 0  Then
				otherSquare = gameSquares(square.RowPos, square.ColPos - 1)
			End If				
	End Select
	
	Return otherSquare
End Sub

Sub CloseCompletedSquares(currPlayer As Player) As Int
	Dim allClosed As List = FindAllForSides(4)
	Dim closed As Int
	
	For Each item As GameSquare In allClosed
		If item.Occupied = False Then
			FillTheSquare(item, currPlayer)
			closed = closed + 1
			item.Occupied = True
		End If
	Next
	
	Return closed
End Sub

Sub FillTheSquare(square As GameSquare, currPlayer As Player)
	Dim fillRect As Rect

	If currPlayer.PlayerImage.IsInitialized Then
		If square.fillLabel.IsInitialized Then
			square.fillLabel.SetBackgroundImage(currPlayer.PlayerImage)
		Else
			Dim lblNew As Label
			lblNew.Initialize("")
			lblNew.SetBackgroundImage(currPlayer.PlayerImage)
			lblNew.Gravity = Gravity.FILL
			square.fillLabel = lblNew
			panel1.AddView(lblNew, square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, columnSpacing - 8dip, rowSpacing - 8dip)
		End If
	Else
		fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.Pos2  + 4dip, square.BottomRight.Pos1  - 4dip, square.BottomRight.Pos2  - 4dip)	
		canv.DrawRect(fillRect,currPlayer.colour,True,1dip)
	End If
End Sub

Sub EmptyTheSquare(square As GameSquare)
	Dim fillRect As Rect
	
	If square.fillLabel.IsInitialized Then
		square.fillLabel.SetBackgroundImage(Null)
	Else
		fillRect.Initialize(square.TopLeft.Pos1 + 4dip, square.TopLeft.pos2  + 4dip, square.BottomRight.pos1  - 4dip, square.BottomRight.Pos2  - 4dip)	
		canv.DrawRect(fillRect,SPConstants.BG_COLOUR,True,1dip)
	End If
End Sub

Sub FindSelectedSquare(xpos As Int, ypos As Int) As GameSquare
	Dim colLoop As Int = 0
	Dim rowloop As Int = 0
	Dim foundRow As Int = -1
	Dim foundCol As Int = -1
	Dim square As GameSquare
	
	For rowloop = 0 To gameHeight - 1
		square = gameSquares(rowloop, 0)
		If square.TopLeft.pos2 > ypos Then
			If rowloop = 0 Then
				foundRow = 0
			Else
				foundRow = rowloop - 1
			End If
			rowloop = gameHeight -1
		End If
	Next
	
	If foundRow = -1 Then foundRow = gameHeight-1
	
	For colLoop = 0 To gameWidth - 1
		square = gameSquares(foundRow, colLoop)
		If square.TopLeft.pos1 > xpos  Then
			If colLoop = 0 Then
				foundCol = 0
			Else
				foundCol = colLoop - 1
			End If
			colLoop = gameWidth - 1
		End If
	Next
	
	If foundCol = -1 Then foundCol = gameWidth - 1
	
	Return gameSquares(foundRow,foundCol)
End Sub


Sub GetViewByTag1(searchView As Activity, tag As String) As View

	Dim tempView As View
	Dim vwloop As Int
	For vwloop = 0 To searchView.NumberOfViews - 1
		tempView = searchView.GetView(vwloop)
		If tempView.tag = tag Then
			Return tempView
		End If
	Next
	Return tempView	
End Sub

Sub FindAllForSides(checkSides As Int) As List
	Dim foundSquares As List
	foundSquares.Initialize
	For row = 0 To gameHeight - 1
		For col = 0 To gameWidth - 1
			If gameSquares(row,col).sidesTaken = checkSides Then
				foundSquares.Add(gameSquares(row,col))
			End If
		Next
	Next	
	
	Return foundSquares
End Sub

Sub DisplayFrontScreen
	If pnlBase.IsInitialized Then
		pnlBase.Left = 200%x
	End If
	pnlStartScreen.Left = 0dip
	inGame = False
End Sub

Sub spnPlayers_ItemClick (Position As Int, Value As Object)
	UpdateDroidNumbers
End Sub

Sub UpdateDroidNumbers
	Dim currentItem As Int = spnDroids.SelectedItem
	numberOfPlayers = spnPlayers.SelectedItem 
	spnDroids.Clear
	Dim iLoop As Int
	For iLoop = 0 To numberOfPlayers - 1
		spnDroids.Add(iLoop)
	Next
	For iLoop = 0 To spnDroids.Size - 1
		spnDroids.SelectedIndex = iLoop
		If spnDroids.SelectedItem = currentItem Then
			Exit
		End If
	Next
	If spnDroids.SelectedItem = -1 Then
		spnDroids.SelectedIndex = 0
	End If
End Sub
Sub btnContinue_Click
	vibrate.vibrate(25)
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
		Dim msg As String = SPConstants.GAME_MSG_START_GAME
		SendMessage(msg)
		If IsMaster Then
			myTurn = True
		End If
	Else
		myTurn = True
		numberOfDroids = spnDroids.SelectedItem
	End If
	ReverseAnimate
End Sub

Sub sbRows_ValueChanged (Value As Int, UserChanged As Boolean)
	gameHeight = Value + 4
	lblRows.Text = "Rows : " & (Value + 4)
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then
		Dim msg As String = SPConstants.GAME_MSG_SET_ROWS & "," & Value
		SendMessage(msg)
	End If
End Sub
Sub sbColumns_ValueChanged (Value As Int, UserChanged As Boolean)
	gameWidth = Value + 4
	lblColumns.Text = "Columns : " & (Value	+ 4)
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC AND IsMaster Then
		Dim msg As String = SPConstants.GAME_MSG_SET_COLUMNS & "," & Value
		SendMessage(msg)
	End If
End Sub
Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		If inGame Then
			inGame = False		
			If pnlOuter.Left = 0dip Then
				pnlOuter.Left = -100%x
				DisplayFrontScreen
				Return True				
			Else 
				Dim Answ As Int
				Answ = Msgbox2("Do you want to quit this game, you will lose all progress.", _
			      "W A R N I N G", "Yes", "", "No", Null)
			    If Answ = DialogResponse.NEGATIVE Then
			      Return True
				Else
					If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
						SendMessage(SPConstants.GAME_MSG_CLOSE_GAME)
					End If
					CloseGame
					Return True
				End If			
			End If
		Else
			SendMessage(SPConstants.GAME_MSG_CLOSE_START)
		End If
	End If
	
	Return False
End Sub

Sub CloseGame()
	If pnlOuter.IsInitialized Then
		pnlOuter.Left = -100%x
		DisplayFrontScreen
		AnimateCharacters
	End If
End Sub

Sub CloseStartScreen()
	ToastMessageShow("The other player has return to the start screen - connection is closed", False)
	Activity.Finish
End Sub

Sub RestoreDefaults() As Boolean
	If File.Exists(File.DirInternal, "Square_settings.txt") Then
		Dim defaultsMap As Map = File.ReadMap(File.DirInternal, "Square_settings.txt")
		If defaultsMap.IsInitialized Then
			Dim iLoop As Int
			Dim def_players As Int = defaultsMap.Get("players")
			If GameMode = SPConstants.GAMETYPE_MODE_BT Then
				def_players = 0
			End If
			For iLoop = 0 To spnPlayers.Size - 1
				If spnPlayers.GetItem(iLoop) = def_players Then
					spnPlayers.SelectedIndex = iLoop
					Exit
				End If
			Next
			UpdateDroidNumbers
			Dim def_droids As Int = defaultsMap.Get("droids")
			If GameMode = SPConstants.GAMETYPE_MODE_BT Then
				def_droids = 0
			End If			
			For iLoop = 0 To spnDroids.Size - 1
				If spnDroids.GetItem(iLoop) = def_droids Then
					spnDroids.SelectedIndex = iLoop
					Exit
				End If
			Next			

			sbRows.Value = defaultsMap.Get("rows")
			lblRows.Text = "Rows : " & (sbRows.Value + 4)
			sbColumns.Value = defaultsMap.Get("columns")
			lblColumns.Text = "Columns : " & (sbColumns.value + 4)
			If defaultsMap.Get("Sound") = True Then
				chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))
				soundsOn = True
			Else
				chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))
				soundsOn = False
			End If
			
			Dim def_diff As String = defaultsMap.Get("Difficulty")
			For iLoop = 0 To spnDifficulty.Size - 1
				If spnDifficulty.GetItem(iLoop) = def_diff Then
					spnDifficulty.SelectedIndex = iLoop
					Exit
				End If
			Next			
		Else
			Return False
		End If
	End If
	
	If GameMode <> SPConstants.GAMETYPE_MODE_LOC Then
		spnPlayers.Enabled = False
		spnDroids.Enabled = False
	Else
		spnPlayers.Enabled = True
		spnDroids.Enabled = True
	End If	
	
	Return True
End Sub
Sub SaveDefaults()
	Dim defaults As Map
	defaults.Initialize
	
	defaults.Put("players",spnPlayers.SelectedItem)
	defaults.Put("droids",spnDroids.SelectedItem)
	defaults.Put("rows",sbRows.Value)
	defaults.Put("columns",sbColumns.Value)
	defaults.Put("Sound", soundsOn)
	defaults.Put("Difficulty", spnDifficulty.SelectedItem)
	
	File.WriteMap(File.DirInternal, "Square_settings.txt", defaults)
	
End Sub
Sub RemoveTurn(lastTurn As Turn, currPlayer As Player)
	
	' Did the last turn close a square. If so, need to remove the score and remove the player pic
	If lastTurn.Square.sidesTaken = 4 Then
		currPlayer.Score = currPlayer.Score - 1
		EmptyTheSquare(lastTurn.Square)
	End If
	
	' Remove the drawn side
	lastTurn.Square.RemoveSide(canv, lastTurn.Edge)
	MarkOtherSide(lastTurn.Square,lastTurn.Edge,False)
	
	' Remove that turn
	gameTurns.RemoveAt(gameTurns.Size - 1)
	
	' redraw the last line in red to mark it as the current one
	If gameTurns.Size > 0 Then
		lastTurn = gameTurns.Get(gameTurns.Size - 1)
		lastTurn.Square.RedrawSide(canv, lastTurn.Edge)
	End If
End Sub
' Clicking the Player button causes an Undo to happen
Sub btnCurrPlayer_Click

	#If WithDebug
	displayingDebug = displayingDebug + 1
	
	If displayingDebug = 4 Then displayingDebug = 0
	
	Dim rLoop As Int
	Dim cLoop As Int
	Dim square As GameSquare
	If displayingDebug = 3 Then
		DisplayChainCounts
	Else
		lblDebugDisplay.Left = 100%x
		For rLoop = 0 To gameHeight - 1
			For cLoop = 0 To gameWidth - 1
				square = gameSquares(rLoop, cLoop)
				Select displayingDebug
					Case 0
						square.fillLabel.Text = ""
					Case 1
						square.fillLabel.TextSize = 34
						square.fillLabel.Text = square.sidesTaken
					Case 2
						square.fillLabel.TextSize = 12
						square.fillLabel.Text = ""
						For iLoop = 0 To 3
							square.fillLabel.Text = square.fillLabel.Text & square.sides(iLoop)						
						Next
				End Select
			Next
		Next
	End If
	#End If 
End Sub

Sub btnOk_Click
	SendMessage(SPConstants.GAME_MSG_CLOSE_GAME)
	CloseGame
End Sub

Sub lblDebugDisplay_Click
	lblDebugDisplay.Left = 100%x
	lblDebugDisplay.Visible = False
End Sub
Sub pnlStartScreen_Touch (Action As Int, X As Float, Y As Float)
	#If WithDebug
	Select Action
    	Case Activity.ACTION_UP
			AnimateCharacters
			AnimatePanelLeft
	End Select		
	#End If
End Sub
Sub chkSounds_Click
	If soundsOn Then
		chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_OFF))
		soundsOn = False
	Else
		chkSounds.SetBackgroundImage(checkBoxImages.Get(SPConstants.CHECKBOX_ON))
		soundsOn = True	
	End If
End Sub

Sub txtMessage_EnterPressed
	SendMessage(SPConstants.GAME_MSG_PROCESS_CHAT & "," & txtMessage.Text)
	txtMessage.Text = ""
End Sub