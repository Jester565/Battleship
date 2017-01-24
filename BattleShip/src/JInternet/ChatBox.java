package JInternet;

import java.net.DatagramPacket;
import java.util.concurrent.Semaphore;

import pack.KeyInput;
import JBasics.ActivityLog;
import JBasics.ShapeRenderer;
import JBasics.Sprite;
import JBasics.TextField;
import JInternet.PeerManager.Peer;
import Launcher.Main;

public class ChatBox{

	private TextField userMessage;
	private ActivityLog al;
	private MicrophoneInputReader mir;
	private MicrophoneInputSender mis;
	private Thread misThread;
	private Sprite yourMic;
	private Sprite opMic;
	private Semaphore micSem;
	private boolean micUsed = false;
	private boolean micOn = false;
	private InternetCore ic;
	private int targetID;
	public ChatBox(InternetCore ic){
		this.ic = ic;
		yourMic = new Sprite("yourMic.png");
		opMic = new Sprite("enemyMic.png");
		micSem = new Semaphore(0,true);
		al = new ActivityLog(20, 10);
		userMessage = new TextField(true,0,0,50,10);
		PackageProcessing.addToKeyRoutines(new KeyJ4());
	}
	public ChatBox(InternetCore ic, int targetID){
		this.ic = ic;
		al = new ActivityLog();
		userMessage = new TextField(true,0,0,50,10);
		PackageProcessing.addToKeyRoutines(new KeyJ4());
		enableVoice(targetID);
		this.targetID = targetID;
	}
	public void enableVoice(int targetPeer){
		mis = new MicrophoneInputSender(Main.getIMananager(),micSem,targetPeer);
		mir = new MicrophoneInputReader();
		misThread = new Thread(mis);
		misThread.start();
		micOn = true;
		this.targetID = targetPeer;
	}
	protected boolean sendOn(){
		if(KeyInput.keyPressed("G") && !userMessage.isSelected()){
			return true;
		}
		return false;
	}
	private void manageMicrophone(int logoX, int logoY, int logoW, int logoH, int logo2X, int logo2Y){
		if(MicrophoneInputReader.inRecieveTimeRange(System.currentTimeMillis())){
			opMic.simpleDraw(logo2X, logo2Y, logoW, logoH);
		}
		if(sendOn()){
			yourMic.simpleDraw(logoX, logoY, logoW, logoH);
			if(!micUsed){
				mis.drain();
			}
			micUsed = true;
			if(micSem.availablePermits() <= 0){
				micSem.release();
			}
		}else{
			if(micUsed){
				ic.send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J6", "MICSTOP"),targetID));
				micUsed = false;
			}
		}
	}
	public void draw(int x, int y, int w, int h, float fontSize){
		manageUserMessage(x,y,w,(int)fontSize * 2,fontSize);
		al.displayStrings(x, y + 5, w, (int)(h*.86d), fontSize);
		if(micOn){
			manageMicrophone(x, y + h + (int)(fontSize*1.5f), (int)fontSize, (int)fontSize, x + w - (int)fontSize, y + h + (int)(fontSize*1.5f));
		}
	}
	private void manageUserMessage(int x, int y, int w, int h, float fontSize){
		userMessage.draw(true, x, y, w, h, .6f, .6f, .6f, 1);
		if(userMessage.finalMessage != null){
			al.addToLog(new String(Main.getIMananager().user.getName()+ ": " + userMessage.finalMessage));
			ic.send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J4", userMessage.finalMessage),targetID));
			userMessage.reset();
		}
	}
	public class KeyJ4 extends PeerKey{
		private static final String key = "J4";
		public KeyJ4(){
			super(key);
		}
		@Override
		public String getDescription() {
			return key +": Adds message to the chat box";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			Peer p = ic.getPeerManager().getPeer(id);
			al.addToLog(p.getName() + ": " + ByteToObject.bytesToString(pack));
		}
		@Override
		public String getKey(){
			return key;
		}
	}
}
