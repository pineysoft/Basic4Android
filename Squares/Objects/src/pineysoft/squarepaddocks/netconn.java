package pineysoft.squarepaddocks;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class netconn extends android.app.Service {
	public static class netconn_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, netconn.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static netconn mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return netconn.class;
	}
	@Override
	public void onCreate() {
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "pineysoft.squarepaddocks", "pineysoft.squarepaddocks.netconn");
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        processBA.setActivityPaused(false);
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "pineysoft.squarepaddocks.netconn", processBA, _service);
		}
        BA.LogInfo("** Service (netconn) Create **");
        processBA.raiseEvent(null, "service_create");
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		handleStart(intent);
    }
    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    	handleStart(intent);
		return android.app.Service.START_NOT_STICKY;
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (netconn) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = new anywheresoftware.b4a.objects.IntentWrapper();
    			if (intent != null) {
    				if (intent.hasExtra("b4a_internal_intent"))
    					iw.setObject((android.content.Intent) intent.getParcelableExtra("b4a_internal_intent"));
    				else
    					iw.setObject(intent);
    			}
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() {
        BA.LogInfo("** Service (netconn) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
	}
public anywheresoftware.b4a.keywords.Common __c = null;
public static String _myip = "";
public static String _wifistatus = "";
public static String _btstatus = "";
public static boolean _wificonnected = false;
public static boolean _btconnected = false;
public static int _progressvalue = 0;
public static String _progresstext = "";
public static String _lblfile = "";
public static boolean _sendingfile = false;
public static boolean _receivingfile = false;
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _admin = null;
public static anywheresoftware.b4a.objects.Timer _timer1 = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.objects.SocketWrapper _socket1 = null;
public static anywheresoftware.b4a.objects.SocketWrapper.ServerSocketWrapper _server = null;
public static int _port = 0;
public static String _uuid = "";
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.phone.PhoneEvents _pe = null;
public static anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingInput _countingstream = null;
public static long _totalsizeforsending = 0L;
public static String _currentfile = "";
public b4a.example.dateutils _dateutils = null;
public pineysoft.squarepaddocks.main _main = null;
public pineysoft.squarepaddocks.gameactivity _gameactivity = null;
public static String  _admin_statechanged(int _newstate,int _oldstate) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub admin_StateChanged (NewState As Int, OldState As Int)";
 //BA.debugLineNum = 47;BA.debugLine="If NewState = admin.STATE_ON Then serial1.Listen2(\"na\", uuid)";
if (_newstate==_admin.STATE_ON) { 
_serial1.Listen2("na",_uuid,processBA);};
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub Astream_Error";
 //BA.debugLineNum = 147;BA.debugLine="Log(\"Error: \" & LastException)";
anywheresoftware.b4a.keywords.Common.Log("Error: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA)));
 //BA.debugLineNum = 148;BA.debugLine="astream.Close";
_astream.Close();
 //BA.debugLineNum = 149;BA.debugLine="AStream_Terminated 'manually call this method as it will not be called";
_astream_terminated();
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _astream_newdata(byte[] _buffer) throws Exception{
String _currentmsg = "";
 //BA.debugLineNum = 178;BA.debugLine="Sub AStream_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 180;BA.debugLine="timer1.Enabled = True";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 181;BA.debugLine="Dim currentMsg As String = BytesToString(Buffer, 0, Buffer.Length, \"UTF8\")";
_currentmsg = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 182;BA.debugLine="UpdateGame(currentMsg)";
_updategame(_currentmsg);
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _astream_newstream(String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub AStream_NewStream (Dir As String, FileName As String)";
 //BA.debugLineNum = 169;BA.debugLine="Timer1_Tick";
_timer1_tick();
 //BA.debugLineNum = 170;BA.debugLine="timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="lblFile = currentFile & \" completed\"";
_lblfile = _currentfile+" completed";
 //BA.debugLineNum = 172;BA.debugLine="ReceivingFile = False";
_receivingfile = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 173;BA.debugLine="UpdateProgress";
_updateprogress();
 //BA.debugLineNum = 174;BA.debugLine="File.Copy(Dir, FileName, Dir, currentFile)";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,_dir,_currentfile);
 //BA.debugLineNum = 175;BA.debugLine="File.Delete(Dir, FileName)";
anywheresoftware.b4a.keywords.Common.File.Delete(_dir,_filename);
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 154;BA.debugLine="timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 155;BA.debugLine="If BTConnected Then";
if (_btconnected) { 
 //BA.debugLineNum = 156;BA.debugLine="BTStatus = \"Disconnected\"";
_btstatus = "Disconnected";
 }else if(_wificonnected) { 
 //BA.debugLineNum = 158;BA.debugLine="WifiStatus = \"Disconnected\"";
_wifistatus = "Disconnected";
 };
 //BA.debugLineNum = 160;BA.debugLine="BTConnected = False";
_btconnected = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 161;BA.debugLine="WifiConnected = False";
_wificonnected = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 162;BA.debugLine="ReceivingFile = False";
_receivingfile = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 163;BA.debugLine="SendingFile = False";
_sendingfile = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 164;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _connectbt(String _address) throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Public Sub ConnectBT(Address As String)";
 //BA.debugLineNum = 71;BA.debugLine="serial1.Connect2(Address, uuid)";
_serial1.Connect2(processBA,_address,_uuid);
 //BA.debugLineNum = 72;BA.debugLine="BTStatus = \"Trying to connect...\"";
_btstatus = "Trying to connect...";
 //BA.debugLineNum = 73;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _connectwifi(String _ip) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Public Sub ConnectWifi(Ip As String)";
 //BA.debugLineNum = 77;BA.debugLine="socket1.Initialize(\"socket1\")";
_socket1.Initialize("socket1");
 //BA.debugLineNum = 78;BA.debugLine="socket1.Connect(Ip, port, 30000)";
_socket1.Connect(processBA,_ip,_port,(int) (30000));
 //BA.debugLineNum = 79;BA.debugLine="WifiStatus = \"Trying to connect...\"";
_wifistatus = "Trying to connect...";
 //BA.debugLineNum = 80;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _disconnect() throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Public Sub Disconnect";
 //BA.debugLineNum = 65;BA.debugLine="If WifiConnected OR BTConnected Then";
if (_wificonnected || _btconnected) { 
 //BA.debugLineNum = 66;BA.debugLine="astream.Close";
_astream.Close();
 //BA.debugLineNum = 67;BA.debugLine="AStream_Terminated";
_astream_terminated();
 };
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _pe_connectivitychanged(String _networktype,String _state,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub pe_ConnectivityChanged (NetworkType As String, State As String, Intent As Intent)";
 //BA.debugLineNum = 51;BA.debugLine="MyIP = server.GetMyWifiIP";
_myip = _server.GetMyWifiIP();
 //BA.debugLineNum = 52;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Public MyIP As String = \"N/A\"";
_myip = "N/A";
 //BA.debugLineNum = 7;BA.debugLine="Public WifiStatus As String = \"Diconnected\"";
_wifistatus = "Diconnected";
 //BA.debugLineNum = 8;BA.debugLine="Public BTStatus  As String = \"Diconnected\"";
_btstatus = "Diconnected";
 //BA.debugLineNum = 9;BA.debugLine="Public WifiConnected, BTConnected As Boolean";
_wificonnected = false;
_btconnected = false;
 //BA.debugLineNum = 10;BA.debugLine="Public progressValue As Int";
_progressvalue = 0;
 //BA.debugLineNum = 11;BA.debugLine="Public progressText, lblFile As String";
_progresstext = "";
_lblfile = "";
 //BA.debugLineNum = 12;BA.debugLine="Public SendingFile As Boolean";
_sendingfile = false;
 //BA.debugLineNum = 13;BA.debugLine="Public ReceivingFile As Boolean";
_receivingfile = false;
 //BA.debugLineNum = 15;BA.debugLine="Private admin As BluetoothAdmin";
_admin = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 16;BA.debugLine="Private timer1 As Timer";
_timer1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 17;BA.debugLine="Private serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 18;BA.debugLine="Private socket1 As Socket";
_socket1 = new anywheresoftware.b4a.objects.SocketWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private server As ServerSocket";
_server = new anywheresoftware.b4a.objects.SocketWrapper.ServerSocketWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private port As Int = 21341";
_port = (int) (21341);
 //BA.debugLineNum = 21;BA.debugLine="Private uuid As String = \"dabcabcd-afac-11de-8a39-0800200c9a6\"";
_uuid = "dabcabcd-afac-11de-8a39-0800200c9a6";
 //BA.debugLineNum = 22;BA.debugLine="Private astream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 23;BA.debugLine="Private pe As PhoneEvents";
_pe = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 24;BA.debugLine="Private countingStream As CountingInputStream";
_countingstream = new anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingInput();
 //BA.debugLineNum = 25;BA.debugLine="Private totalSizeForSending As Long";
_totalsizeforsending = 0L;
 //BA.debugLineNum = 26;BA.debugLine="Private currentFile As String";
_currentfile = "";
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _sendfile(String _dir,String _filename) throws Exception{
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
 //BA.debugLineNum = 83;BA.debugLine="Public Sub SendFile(Dir As String, FileName As String)";
 //BA.debugLineNum = 84;BA.debugLine="Dim totalSizeForSending As Long = File.Size(Dir, FileName)";
_totalsizeforsending = anywheresoftware.b4a.keywords.Common.File.Size(_dir,_filename);
 //BA.debugLineNum = 85;BA.debugLine="Dim In As InputStream = File.OpenInput(Dir, FileName)";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
_in = anywheresoftware.b4a.keywords.Common.File.OpenInput(_dir,_filename);
 //BA.debugLineNum = 86;BA.debugLine="countingStream.Initialize(In)";
_countingstream.Initialize((java.io.InputStream)(_in.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="currentFile = FileName.SubString(FileName.LastIndexOf(\"/\") + 1)";
_currentfile = _filename.substring((int) (_filename.lastIndexOf("/")+1));
 //BA.debugLineNum = 88;BA.debugLine="astream.Write(currentFile.GetBytes(\"UTF8\")) 'write the file name";
_astream.Write(_currentfile.getBytes("UTF8"));
 //BA.debugLineNum = 89;BA.debugLine="astream.WriteStream(countingStream, totalSizeForSending)";
_astream.WriteStream((java.io.InputStream)(_countingstream.getObject()),_totalsizeforsending);
 //BA.debugLineNum = 90;BA.debugLine="lblFile = \"Sending: \" & currentFile";
_lblfile = "Sending: "+_currentfile;
 //BA.debugLineNum = 91;BA.debugLine="timer1.Enabled = True";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="SendingFile = True";
_sendingfile = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 93;BA.debugLine="UpdateProgress";
_updateprogress();
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _sendmessage(String _msg) throws Exception{
 //BA.debugLineNum = 96;BA.debugLine="Public Sub SendMessage(msg As String)";
 //BA.debugLineNum = 97;BA.debugLine="astream.Write(msg.GetBytes(\"UTF8\"))";
_astream.Write(_msg.getBytes("UTF8"));
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Private Sub serial1_Connected (Success As Boolean)";
 //BA.debugLineNum = 126;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 127;BA.debugLine="BTStatus = \"Connected\"";
_btstatus = "Connected";
 //BA.debugLineNum = 128;BA.debugLine="BTConnected = True";
_btconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 129;BA.debugLine="StartAStream(serial1.InputStream, serial1.OutputStream)";
_startastream((anywheresoftware.b4a.objects.streams.File.InputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper(), (java.io.InputStream)(_serial1.getInputStream())),(anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper(), (java.io.OutputStream)(_serial1.getOutputStream())));
 }else {
 //BA.debugLineNum = 131;BA.debugLine="BTStatus = \"Error: \" & LastException.Message";
_btstatus = "Error: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage();
 };
 //BA.debugLineNum = 133;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _server_newconnection(boolean _successful,anywheresoftware.b4a.objects.SocketWrapper _newsocket) throws Exception{
 //BA.debugLineNum = 112;BA.debugLine="Private Sub server_NewConnection (Successful As Boolean, NewSocket As Socket)";
 //BA.debugLineNum = 114;BA.debugLine="If Successful Then";
if (_successful) { 
 //BA.debugLineNum = 115;BA.debugLine="WifiConnected = True";
_wificonnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 116;BA.debugLine="StartAStream(NewSocket.InputStream, NewSocket.OutputStream)";
_startastream((anywheresoftware.b4a.objects.streams.File.InputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper(), (java.io.InputStream)(_newsocket.getInputStream())),(anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper(), (java.io.OutputStream)(_newsocket.getOutputStream())));
 //BA.debugLineNum = 117;BA.debugLine="WifiStatus = \"Connected\"";
_wifistatus = "Connected";
 }else {
 //BA.debugLineNum = 119;BA.debugLine="WifiStatus = \"Error: \" & LastException";
_wifistatus = "Error: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA));
 };
 //BA.debugLineNum = 121;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 122;BA.debugLine="server.Listen";
_server.Listen();
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 31;BA.debugLine="server.Initialize(port, \"server\")";
_server.Initialize(processBA,_port,"server");
 //BA.debugLineNum = 32;BA.debugLine="Try";
try { //BA.debugLineNum = 33;BA.debugLine="server.Listen";
_server.Listen();
 } 
       catch (Exception e27) {
			processBA.setLastException(e27); //BA.debugLineNum = 35;BA.debugLine="WifiStatus = \"Error listening: \" & LastException";
_wifistatus = "Error listening: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA));
 //BA.debugLineNum = 36;BA.debugLine="UpdateUI";
_updateui();
 };
 //BA.debugLineNum = 38;BA.debugLine="admin.Initialize(\"admin\")";
_admin.Initialize(processBA,"admin");
 //BA.debugLineNum = 39;BA.debugLine="serial1.Initialize(\"serial1\")";
_serial1.Initialize("serial1");
 //BA.debugLineNum = 40;BA.debugLine="If serial1.IsEnabled Then serial1.Listen2(\"na\", uuid)";
if (_serial1.IsEnabled()) { 
_serial1.Listen2("na",_uuid,processBA);};
 //BA.debugLineNum = 41;BA.debugLine="pe.Initialize(\"pe\")";
_pe.Initialize(processBA,"pe");
 //BA.debugLineNum = 42;BA.debugLine="pe_ConnectivityChanged(\"\", \"\", Null)";
_pe_connectivitychanged("","",(anywheresoftware.b4a.objects.IntentWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.IntentWrapper(), (android.content.Intent)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 43;BA.debugLine="timer1.Initialize(\"timer1\", 1000)";
_timer1.Initialize(processBA,"timer1",(long) (1000));
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 211;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _socket1_connected(boolean _successful) throws Exception{
 //BA.debugLineNum = 100;BA.debugLine="Private Sub socket1_Connected (Successful As Boolean)";
 //BA.debugLineNum = 102;BA.debugLine="If Successful Then";
if (_successful) { 
 //BA.debugLineNum = 103;BA.debugLine="WifiConnected = True";
_wificonnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 104;BA.debugLine="StartAStream(socket1.InputStream, socket1.OutputStream)";
_startastream((anywheresoftware.b4a.objects.streams.File.InputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper(), (java.io.InputStream)(_socket1.getInputStream())),(anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper(), (java.io.OutputStream)(_socket1.getOutputStream())));
 //BA.debugLineNum = 105;BA.debugLine="WifiStatus = \"Connected\"";
_wifistatus = "Connected";
 }else {
 //BA.debugLineNum = 107;BA.debugLine="WifiStatus = \"Error: \" & LastException";
_wifistatus = "Error: "+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(processBA));
 };
 //BA.debugLineNum = 109;BA.debugLine="UpdateUI";
_updateui();
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _startastream(anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in,anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out) throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Private Sub StartAStream (In As InputStream, out As OutputStream)";
 //BA.debugLineNum = 137;BA.debugLine="Log(\"StartAStream\")";
anywheresoftware.b4a.keywords.Common.Log("StartAStream");
 //BA.debugLineNum = 138;BA.debugLine="astream.InitializePrefix(In, True, out, \"astream\")";
_astream.InitializePrefix(processBA,(java.io.InputStream)(_in.getObject()),anywheresoftware.b4a.keywords.Common.True,(java.io.OutputStream)(_out.getObject()),"astream");
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
long _count = 0L;
long _total = 0L;
 //BA.debugLineNum = 189;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 190;BA.debugLine="Dim count, total As Long";
_count = 0L;
_total = 0L;
 //BA.debugLineNum = 191;BA.debugLine="If SendingFile Then";
if (_sendingfile) { 
 //BA.debugLineNum = 192;BA.debugLine="count = countingStream.count";
_count = _countingstream.getCount();
 //BA.debugLineNum = 193;BA.debugLine="total = totalSizeForSending";
_total = _totalsizeforsending;
 //BA.debugLineNum = 194;BA.debugLine="If count = total Then";
if (_count==_total) { 
 //BA.debugLineNum = 195;BA.debugLine="lblFile = currentFile & \" completed\"";
_lblfile = _currentfile+" completed";
 //BA.debugLineNum = 198;BA.debugLine="SendingFile = False";
_sendingfile = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 199;BA.debugLine="timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 }else if(_receivingfile) { 
 //BA.debugLineNum = 202;BA.debugLine="count = astream.StreamReceived";
_count = _astream.getStreamReceived();
 //BA.debugLineNum = 203;BA.debugLine="total = astream.StreamTotal";
_total = _astream.getStreamTotal();
 };
 //BA.debugLineNum = 205;BA.debugLine="progressValue = 100 * count / total";
_progressvalue = (int) (100*_count/(double)_total);
 //BA.debugLineNum = 206;BA.debugLine="progressText = NumberFormat2(count / 1000, 0, 0, 0, True) & _ 			\"kb / \" & NumberFormat2(total / 1000, 0, 0, 0, True) & \"kb\"";
_progresstext = anywheresoftware.b4a.keywords.Common.NumberFormat2(_count/(double)1000,(int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.True)+"kb / "+anywheresoftware.b4a.keywords.Common.NumberFormat2(_total/(double)1000,(int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.True)+"kb";
 //BA.debugLineNum = 208;BA.debugLine="UpdateProgress";
_updateprogress();
 //BA.debugLineNum = 209;BA.debugLine="End Sub";
return "";
}
public static String  _updategame(String _msg) throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub UpdateGame(msg)";
 //BA.debugLineNum = 186;BA.debugLine="CallSubDelayed2(\"GameActivity\",\"UpdateGameMessages\",msg)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)("GameActivity"),"UpdateGameMessages",(Object)(_msg));
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static String  _updateprogress() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Private Sub UpdateProgress";
 //BA.debugLineNum = 62;BA.debugLine="CallSub(Main, \"UpdateProgress\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"UpdateProgress");
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _updateui() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Private Sub UpdateUI";
 //BA.debugLineNum = 59;BA.debugLine="CallSub(Main, \"UpdateUI\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"UpdateUI");
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
}
