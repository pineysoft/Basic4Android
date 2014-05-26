Type=Class
Version=3.8
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim Pos1 As Int
	Dim Pos2 As Int
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize(a As Int, b As Int)
	Pos1 = a
	Pos2 = b
End Sub