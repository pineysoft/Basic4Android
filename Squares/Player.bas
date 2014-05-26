Type=Class
Version=3.8
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim Name As String
	Dim Score As Int
	Dim Colour As Int
	Dim PlayerImage As Bitmap
	Dim PlayerType As Int ' 0 for human - 1 for droid
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize(NameIn As String, ColourIn As Int)
	Name = NameIn
	Colour = ColourIn
End Sub

Public Sub Initialize2(NameIn As String, ColourIn As Int, PType As Int)
	Name = NameIn
	Colour = ColourIn
	PlayerType = PType
End Sub