Type=Class
Version=3.8
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim PLAYER_TYPE_HUMAN As Int
	Dim PLAYER_TYPE_DROID As Int
	
	Dim TOP_SIDE As Int
	Dim RIGHT_SIDE As Int
	Dim BOTTOM_SIDE As Int
	Dim LEFT_SIDE As Int
	
	Dim HORIZONTAL As Int
	Dim VERTICAL As Int
	
	Dim SIDE_TAKEN As Char
	Dim SIDE_AVAILABLE As Char
	
	Dim CHECKBOX_ON As Int = 0
	Dim CHECKBOX_OFF As Int = 1
	
	Dim DIFFICULTY_EASY As String = "E"
	Dim DIFFICULTY_MEDIUM As String = "M"
	Dim DIFFICULTY_HARD As String = "H"
	
	Dim GAMETYPE_MODE_LOC As String = "L"
	Dim GAMETYPE_MODE_BT As String = "B"
	Dim GAMETYPE_MODE_WIFI As String = "W"
	
	Dim GAME_MSG_SET_GAME_TYPE As String = "M"
	Dim GAME_MSG_SET_SETTINGS As String = "S"
	Dim GAME_MSG_PROCESS_TURN As String = "T"
	Dim GAME_MSG_START_GAME As String = "G"
	Dim GAME_MSG_SET_PLAYERS As String = "P"
	Dim GAME_MSG_CLOSE_GAME As String = "X"
	Dim GAME_MSG_PROCESS_CHAT As String = "C"
	Dim GAME_MSG_SET_COLUMNS As String = "GC"
	Dim GAME_MSG_SET_ROWS As String = "GR"
	Dim GAME_MSG_CLOSE_START As String = "X2"
	
	Dim GAME_MASTER As Int = 0
	Dim GAME_SLAVE As Int = 1
	
	Dim BG_COLOUR As Int = Colors.RGB(30,144,255)
	Dim CURRENT_SIDE_COLOUR As Int = Colors.Red
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize
	PLAYER_TYPE_HUMAN = 0
	PLAYER_TYPE_DROID = 1
	
	TOP_SIDE = 0
	RIGHT_SIDE = 1
	BOTTOM_SIDE = 2
	LEFT_SIDE = 3
	
	SIDE_TAKEN = "X"
	SIDE_AVAILABLE = "O"
	
	HORIZONTAL = 0
	VERTICAL = 1
	
End Sub