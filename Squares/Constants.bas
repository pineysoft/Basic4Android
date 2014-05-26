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