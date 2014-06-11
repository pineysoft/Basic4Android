Type=Service
Version=3.8
B4A=true
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
#End Region

Sub Process_Globals
	Public MyIP As String = "N/A"
	Public WifiStatus As String = "Diconnected"
	Public BTStatus  As String = "Diconnected"
	Public WifiConnected, BTConnected As Boolean
	Public progressValue As Int
	Public progressText, lblFile As String
	Public SendingFile As Boolean
	Public ReceivingFile As Boolean
	
	Private admin As BluetoothAdmin
	Private timer1 As Timer
	Private serial1 As Serial
	Private socket1 As Socket
	Private server As ServerSocket
	Private port As Int = 21341
	Private uuid As String = "dabcabcd-afac-11de-8a39-0800200c9a6"
	Private astream As AsyncStreams
	Private pe As PhoneEvents
	Private countingStream As CountingInputStream
	Private totalSizeForSending As Long
	Private currentFile As String
End Sub

Sub Service_Create
	'start listening for BT and wifi connections
	server.Initialize(port, "server")
	Try
		server.Listen
	Catch
		WifiStatus = "Error listening: " & LastException
		UpdateUI
	End Try
	admin.Initialize("admin")
	serial1.Initialize("serial1")
	If serial1.IsEnabled Then serial1.Listen2("na", uuid)
	pe.Initialize("pe")
	pe_ConnectivityChanged("", "", Null)
	timer1.Initialize("timer1", 1000)
End Sub

Sub admin_StateChanged (NewState As Int, OldState As Int)
	If NewState = admin.STATE_ON Then serial1.Listen2("na", uuid)
End Sub

Sub pe_ConnectivityChanged (NetworkType As String, State As String, Intent As Intent)
	MyIP = server.GetMyWifiIP
	UpdateUI
End Sub

Sub Service_Start (StartingIntent As Intent)

End Sub
Private Sub UpdateUI
	CallSub(Main, "UpdateUI")
End Sub
Private Sub UpdateProgress
	CallSub(Main, "UpdateProgress")
End Sub
Public Sub Disconnect
	If WifiConnected OR BTConnected Then
		astream.Close
		AStream_Terminated
	End If
End Sub
Public Sub ConnectBT(Address As String)
	serial1.Connect2(Address, uuid)
	BTStatus = "Trying to connect..."
	UpdateUI
End Sub

Public Sub ConnectWifi(Ip As String)
	socket1.Initialize("socket1")
	socket1.Connect(Ip, port, 30000)
	WifiStatus = "Trying to connect..."
	UpdateUI
End Sub

Public Sub SendFile(Dir As String, FileName As String)
	Dim totalSizeForSending As Long = File.Size(Dir, FileName)
	Dim In As InputStream = File.OpenInput(Dir, FileName)
	countingStream.Initialize(In)
	currentFile = FileName.SubString(FileName.LastIndexOf("/") + 1)
	astream.Write(currentFile.GetBytes("UTF8")) 'write the file name
	astream.WriteStream(countingStream, totalSizeForSending)
	lblFile = "Sending: " & currentFile
	timer1.Enabled = True
	SendingFile = True
	UpdateProgress
End Sub

Public Sub SendMessage(msg As String)
	astream.Write(msg.GetBytes("UTF8"))
End Sub

Private Sub socket1_Connected (Successful As Boolean)
	'client connected to server
	If Successful Then
		WifiConnected = True
		StartAStream(socket1.InputStream, socket1.OutputStream)
		WifiStatus = "Connected"
	Else
		WifiStatus = "Error: " & LastException
	End If
	UpdateUI
End Sub

Private Sub server_NewConnection (Successful As Boolean, NewSocket As Socket)
	'server accepted client
	If Successful Then
		WifiConnected = True
		StartAStream(NewSocket.InputStream, NewSocket.OutputStream)
		WifiStatus = "Connected"
	Else
		WifiStatus = "Error: " & LastException
	End If
	UpdateUI
	server.Listen
End Sub

Private Sub serial1_Connected (Success As Boolean)
	If Success Then
		BTStatus = "Connected"
		BTConnected = True
		StartAStream(serial1.InputStream, serial1.OutputStream)
	Else
		BTStatus = "Error: " & LastException.Message
	End If
	UpdateUI
End Sub

Private Sub StartAStream (In As InputStream, out As OutputStream)
	Log("StartAStream")
	astream.InitializePrefix(In, True, out, "astream")
'	If File.ExternalWritable Then
'		astream.StreamFolder = File.DirDefaultExternal
'	Else
'		astream.StreamFolder = File.DirInternalCache
'	End If
End Sub

Sub Astream_Error
	Log("Error: " & LastException)
	astream.Close
	AStream_Terminated 'manually call this method as it will not be called
	'when we explicitly close the connection.
End Sub

Sub AStream_Terminated
	timer1.Enabled = False
	If BTConnected Then
		BTStatus = "Disconnected"
	Else If WifiConnected Then
		WifiStatus = "Disconnected"
	End If
	BTConnected = False
	WifiConnected = False
	ReceivingFile = False
	SendingFile = False
	UpdateUI
End Sub

Sub AStream_NewStream (Dir As String, FileName As String)
	'this event is raised when a file was received successfully
	Timer1_Tick
	timer1.Enabled = False
	lblFile = currentFile & " completed"
	ReceivingFile = False
	UpdateProgress
	File.Copy(Dir, FileName, Dir, currentFile)
	File.Delete(Dir, FileName)
End Sub

Sub AStream_NewData (Buffer() As Byte)
	'get the file name
	timer1.Enabled = True
	Dim currentMsg As String = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	UpdateGame(currentMsg)
End Sub

Sub UpdateGame(msg)
	CallSubDelayed2("GameActivity","UpdateGameMessages",msg)
End Sub

Sub Timer1_Tick
	Dim count, total As Long
	If SendingFile Then
		count = countingStream.count
		total = totalSizeForSending
		If count = total Then
			lblFile = currentFile & " completed"
			'stop the timer.
			'when a file is received the NewStream event will be raised
			SendingFile = False
			timer1.Enabled = False
		End If
	Else If ReceivingFile Then
		count = astream.StreamReceived
		total = astream.StreamTotal
	End If
	progressValue = 100 * count / total
	progressText = NumberFormat2(count / 1000, 0, 0, 0, True) & _
			"kb / " & NumberFormat2(total / 1000, 0, 0, 0, True) & "kb"
	UpdateProgress		
End Sub

Sub Service_Destroy

End Sub

