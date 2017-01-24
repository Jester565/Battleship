package Launcher;

import java.net.DatagramPacket;
import java.util.ArrayList;

import pack.MouseInput;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.Sprite;
import JInternet.ByteToObject;
import JInternet.InternetCore;
import JInternet.Key;
import JInternet.PackageProcessing;
import JInternet.PeerKey;
import JInternet.PeerManager;
import JInternet.ServerList;
import JInternet.User;

public class InternetManager extends InternetCore{
	private Sprite serverBackground;
	private Sprite refresh1;
	private Sprite refresh2;
	private Sprite connecting;
	private ShapeRenderer sr;
	private Main m;
	public InternetManager(Main m){
		super();
		this.m = m;
		sr = new ShapeRenderer();
		serverBackground = new Sprite("serverList.png");
		connecting = new Sprite("connecting.png");
		refresh1 = new Sprite("refresh1.png");
		refresh2 = new Sprite("refresh2.png");
	}
	@Override
	protected void pingTooHighResponse(){
		super.pingTooHighResponse();
		m.pingTooHighResponse();
	}
	@Override
	protected void drawConnectingScreen(){
		connecting.simpleDraw(680, 420);
	}
	@Override
	protected void createServerList(){
		serverList = new ServerListModified();
	}
	@Override
	protected void createPeerManager(User user){
		peerManager = new ModifiedPeerManager(user);
	}
	@Override
	protected void createUser(){
		user = new ModifiedUser(this);
	}
	public class ModifiedUser extends User{

		public ModifiedUser(InternetCore ic) {
			super(ic);
		}
		protected void playerSendSetup(ArrayList<Object> objs){
			super.playerSendSetup(objs);
			objs.add(new Boolean(JoinScreen.getInGame()));
			objs.add(new Integer(TitleScreen.getPlayerProfile().getRank()));
		}
	}
	public class ModifiedPeerManager extends PeerManager{
		public ModifiedPeerManager(User user){
			super(user);
			PackageProcessing.addToKeyRoutines(new KeyD1());
		}
		@Override
		public void addPeer(DatagramPacket pack){
			getPeers().add(new ModifiedPeer(pack));
		}
		private class KeyD1 extends PeerKey{
			static final String key = "D1";
			public KeyD1() {
				super(key);
			}
			@Override
			public String getKey(){
				return key;
			}
			@Override
			public String getDescription() {
				return key + " Recieves info such as rank and in game status";
			}
			@Override
			public void routine(int id, DatagramPacket pack) {
				Peer p = getPeer(id);
				if(p!=null)
					((ModifiedPeer)p).setInfo(ByteToObject.bytesToObjects(pack));
			}
			
		}
		public class ModifiedPeer extends Peer{
			private int rank = 0;
			private boolean inGame = false;
			public ModifiedPeer(DatagramPacket pack) {
				super(pack);
				
			}
			@Override
			protected void setUp(ArrayList<Object> objs){
				super.setUp(objs);
				inGame = (Boolean)objs.get(2);
				rank = (Integer)objs.get(3);
			}
			public void setInfo(ArrayList<Object> objs){
				inGame = (Boolean)objs.get(0);
				rank = (Integer)objs.get(1);
			}
			public boolean getInGame(){
				return inGame;
			}
			public int getRank(){
				return rank;
			}
		}
	}
	public class ServerListModified extends ServerList{
		private Sound beep;
		public ServerListModified(){
			super();
			beep = new Sound("inputPressed.wav",.76d);
		}
		protected void refreshAction(){
			super.refreshAction();
			sendBroadcastLan(getServerNamePack());
		}
		@Override
		protected void drawBackGround(int x, int y, int w, int h){
			serverBackground.simpleDraw(x, 300, w, 600);
		}
		protected boolean serverButton(String name, int x, int y, int w, int h){
			x += w/10;
			w -= w/5;
			h -= h/15;
			if(Button.hitDrawnButton(sr, name, x, y - 37, w, h, .7f, .7f, .7f, .5f)){
				if(!beep.playing()){
					beep.stopAndReset();
					beep.play();
				}
				return true;
			}
			return false;
		}
		public void draw(int x, int y, int w, int h){
			super.draw(x, y, w, h);
			directConnectManager(x, y - h * 6- (int)(h/1.5d), w, h/2, .8f, .8f, .8f, .9f, .8f, .8f, .8f, .9f);
		}
		protected boolean refreshButton(int x, int y, int w, int h){
			y=320;
			if(Button.overButton(x, y, w, h)){
				refresh2.simpleDraw(x, y, w, h);
				if(MouseInput.left()){
					if(!beep.playing()){
						beep.stopAndReset();
						beep.play();
					}
					return true;
				}
			}else{
				refresh1.simpleDraw(x, y, w, h);
			}
			return false;
		}
	}
}
