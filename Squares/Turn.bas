Type=Class
Version=3.8
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim Square As GameSquare
	Dim Edge As Int
	Dim PlayerNum As Int
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize(mSquare As GameSquare, mEdge As Int, mPlayerNum As Int)
	Square = mSquare
	Edge = mEdge
	PlayerNum = mPlayerNum
End Sub