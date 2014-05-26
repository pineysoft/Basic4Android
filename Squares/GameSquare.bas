Type=Class
Version=3.8
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim ColPos As Int
	Dim RowPos As Int
	Dim TopLeft As Point
	Dim TopRight As Point
	Dim BottomLeft As Point	
	Dim BottomRight As Point
	Dim sides(4) As Char
	Dim Occupied As Boolean
	Dim SpConstants As Constants
	Dim sidesTaken As Int
	Dim fillLabel As Label
End Sub

Public Sub Initialize(x As Int, y As Int, width As Int, height As Int, row As Int, col As Int)
	TopLeft.Initialize(x,y)
	TopRight.Initialize(x+width,y)
	BottomLeft.Initialize(x,y+height)
	BottomRight.Initialize(x+width,y+height)
	SpConstants.Initialize
	ColPos = col
	RowPos = row
	sides(0) = "O"
	sides(1) = "O"
	sides(2) = "O"
	sides(3) = "O"
	
End Sub

Public Sub CalculateClosestEdge(xypos As Point) As Int
	Dim deltax As Double
	Dim deltaY As Double
	Dim deg As Double
	
	' If the press was outside the boundaries of the board then choose the correct side. No need to
	' check the angle.
	If xypos.Pos2 < TopLeft.Pos2 Then
		Return SpConstants.TOP_SIDE
	Else If xypos.Pos2 > BottomLeft.Pos2 Then
		Return SpConstants.BOTTOM_SIDE
	End If
	
	If xypos.Pos1 < TopLeft.Pos1 Then
		Return SpConstants.LEFT_SIDE
	Else If xypos.Pos1 > TopRight.Pos1 Then
		Return SpConstants.RIGHT_SIDE
	End If
	
	' Left Side
	If xypos.Pos1 < TopLeft.Pos1 + (TopRight.Pos1 - TopLeft.Pos1) / 2 Then
		If xypos.Pos2 < (TopLeft.Pos2 + (BottomLeft.Pos2 - TopLeft.Pos2) / 2) Then
			deltax = TopLeft.Pos1 - xypos.Pos1
			deltaY = TopLeft.Pos2 - xypos.Pos2
			deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)
			Log("Top Left Deg = " & deg)
			If deg > -135 Then
				Return SpConstants.LEFT_SIDE
			Else
				Return SpConstants.TOP_SIDE
			End If
		Else
			deltax = BottomLeft.Pos1 - xypos.Pos1
			deltaY = BottomLeft.Pos2 - xypos.Pos2
			deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)
			Log("Bottom Left Deg = " & deg)
			If deg > 135 Then
				Return SpConstants.BOTTOM_SIDE
			Else
				Return SpConstants.LEFT_SIDE
			End If		
		End If
	Else
		If xypos.Pos2 < (TopLeft.Pos2 + (BottomLeft.Pos2 - TopLeft.Pos2) / 2) Then
			deltax = TopRight.Pos1 - xypos.Pos1
			deltaY = TopRight.Pos2 - xypos.Pos2
			deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)
			Log("Top Right Deg = " & deg)
			If deg < -45 Then
				Return SpConstants.RIGHT_SIDE
			Else
				Return SpConstants.TOP_SIDE
			End If		
		Else
			deltax = BottomRight.Pos1 - xypos.Pos1
			deltaY = BottomRight.Pos2 - xypos.Pos2
			deg = ATan2(deltaY, deltax)* (180/ 3.14159265359)
			Log("Bottom Right Deg = " & deg)
			If deg < 45 Then
				Return SpConstants.BOTTOM_SIDE
			Else
				Return SpConstants.RIGHT_SIDE
			End If		
		End If	
	End If
	
End Sub

Public Sub IsSideTaken(side As Int) As Boolean

	Return sides(side) = SpConstants.SIDE_TAKEN
			
End Sub

Public Sub MarkSideTaken(side As Int, markTaken As Boolean)

	If markTaken Then
		If sides(side) =SpConstants.SIDE_AVAILABLE Then
			sides(side) = SpConstants.SIDE_TAKEN		
			sidesTaken = sidesTaken + 1
		End If
	Else
		sides(side) = SpConstants.SIDE_AVAILABLE		
		sidesTaken = sidesTaken - 1
	End If
End Sub

Public Sub TakeSide(cnv As Canvas, side As Int)
	DrawEdge2(cnv,side,Colors.Red)
	MarkSideTaken(side,True)
End Sub

Public Sub GetFreeEdges(exclude As Int) As List
	Dim freeSides As List
	Dim iLoop As Int
	
	freeSides.Initialize
	
	For iLoop = 0 To 3
		If sides(iLoop) = "O" Then 
			If iLoop <> exclude Then
				freeSides.Add(iLoop)
			End If
		End If	
	Next

	Return freeSides
End Sub
Public Sub DrawEdge(cnv As Canvas, startPoint As Point, endPoint As Point, color As Int, direction As Int)

	Dim line As Rect
	If direction = SpConstants.VERTICAL Then
		line.Initialize(startPoint.Pos1-2dip, startPoint.Pos2+4dip, endPoint.Pos1+2dip, endPoint.Pos2-4dip)
	Else
		line.Initialize(startPoint.Pos1+4dip, startPoint.Pos2-2dip, endPoint.Pos1-4dip, endPoint.Pos2+2dip)
	End If	
	
	cnv.DrawRect(line,color,True,2dip)
End Sub
Public Sub DrawEdge2(cnv As Canvas, side As Int, color As Int)
	Dim startPoint As Point
	Dim endPoint As Point
	Dim direction As Int
	Select side
		Case SpConstants.TOP_SIDE
			startPoint = TopLeft
			endPoint = TopRight
			direction = SpConstants.HORIZONTAL
		Case SpConstants.RIGHT_SIDE
			startPoint = TopRight
			endPoint = BottomRight
			direction = SpConstants.VERTICAL
		Case SpConstants.BOTTOM_SIDE
			startPoint = BottomLeft
			endPoint = BottomRight
			direction = SpConstants.HORIZONTAL
		Case SpConstants.LEFT_SIDE
			startPoint = TopLeft
			endPoint = BottomLeft
			direction = SpConstants.VERTICAL
	End Select
	
	DrawEdge(cnv,startPoint, endPoint, color, direction)
End Sub

Public Sub RemoveSide(cnv As Canvas, side As Int)
	DrawEdge2(cnv, side, SpConstants.BG_COLOUR)
	MarkSideTaken(side,False)
End Sub

Public Sub RedrawSide(cnv As Canvas, side As Int)
	DrawEdge2(cnv, side, SpConstants.CURRENT_SIDE_COLOUR)
End Sub