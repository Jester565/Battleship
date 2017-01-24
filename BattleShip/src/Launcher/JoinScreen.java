package Launcher;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;

import pack.Core;
import pack.MouseInput;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.Sprite;
import JBasics.TextField;
import JInternet.ByteToObject;
import JInternet.PackageProcessing;
import JInternet.PeerKey;
import JInternet.PeerManager.Peer;
import Launcher.InternetManager.ModifiedPeerManager.ModifiedPeer;

public class JoinScreen {

	private InternetManager im;
	private TextField tf;
	private ShapeRenderer sr;
	private Sprite joinBackground;
	private int x = 530;
	private int y = 700;
	private ScreenMessage sm;
	private static boolean inGame = false;
	private PlayerProfile pp;
	private Sprite button1;
	private Sprite overButton;
	private Sprite playerInfoBack;
	private double sendTimer = 0;
	private int peerStart = 0;
	private Sprite challengeButton;
	private boolean tutorialSet = false;
	private Sprite challengeButton2;
	private Sprite challengeMenuBack;
	private Sprite messageBox;
	private Sprite messageBox2;
	private Sprite joinGameBack;
	private Sprite messageBox3;
	private Peer joinPeer = null;
	private ArrayList<JoinMessage> joinMessages;
	private Sound countDown;
	private double countNum = 7;
	public boolean drawBoard = false;
	private Sound beep;
	public JoinScreen(InternetManager im){
		this.im = im;
		sm = new ScreenMessage();
		sr = new ShapeRenderer();
		beep = new Sound("inputPressed.wav",.76d);
		joinGameBack = new Sprite("joinGameBack.png");
		tf = new TextField(true, x, y-20, 600, 40);
		joinBackground = new Sprite("JoinMenu.png");
		overButton = new Sprite("playerButtonOver.png");
		button1 = new Sprite("playerButton.png");
		playerInfoBack = new Sprite("profileInfo.png");
		challengeButton = new Sprite("challengeButton.png");
		challengeButton2 = new Sprite("challengeButton2.png");
		challengeMenuBack = new Sprite("challengeMenu.png");
		messageBox = new Sprite("messageBox.png");
		messageBox2 = new Sprite("messageBox2.png");
		messageBox3 = new Sprite("messageBox3.png");
		countDown = new Sound("countdown.wav",.76d);
		joinMessages = new ArrayList<JoinMessage>();
		PackageProcessing.addToKeyRoutines(new KeyJ1());
		PackageProcessing.addToKeyRoutines(new KeyJ2());
		PackageProcessing.addToKeyRoutines(new KeyJ3());
	}
	public void reset(){
		inGame = false;
		countNum = 7;
		drawBoard = false;
		joinPeer = null;
		joinMessages.clear();
		peerStart = 0;
		tutorialSet = false;
		sendTimer = 0;
		countDown.stopAndReset();
	}
	public Peer getOpponent(){
		return joinPeer;
	}
	public void setPlayerProfile(PlayerProfile pp){
		this.pp = pp;
	}
	public static boolean getInGame(){
		return inGame;
	}
	public synchronized void draw(){
		if(!tutorialSet){
			TutorialManager.setTutorialPage(5, 20, "Join Screen", new ArrayList<String>(Arrays.asList("Finding an Opponent: In order to challenge someone specific, they must be on your "
					+ "server. If they are on the server, you can use scroll wheel to go up and down through the opponent list until you find the opponent. You can also use the text field "
					+ "titled \"Name Filter\" where you can enter in the name of the opponent to find them the format should be (FirstName) [Space] (LastName). For example \"Alex Craig\". "
					+ "The Name Filter search will not take capitalization into account.", 
					"Challenging an Opponent: To challenge an opponent click the button with the opponent's profile information inside. Then a screen will go up confirming you would lik"
					+ "e to challenge. Say yes and you will see a green box in the challenge menu (located to the right of the opponent list).", "Accepting a Challenge: If your adversary "
					+ "has sent you a challenge, it will initially appear in your challenge menu a red box. Once it fades away, you will be able to see who the challenge invite is"
					+ " from and can accept the invite by clicking on the box.  When asked for confirmation, click Yes.")));
			tutorialSet = true;
		}
		if(joinPeer == null){
			if(countNum < 7){
				countNum = 7;
				countDown.stopAndReset();
			}
			inGame = false;
			joinBackground.simpleDraw(x - 170, y - 550, 900, 600);
			drawYourInfo();
			ArrayList<Peer> peerList = im.getPeerManager().getPeers();
			tf.draw(true);
			int y2 = y;
			if(MouseInput.wheelUp() && peerStart > 0){
				peerStart--;
			}else if(MouseInput.wheelDown() && peerStart < peerList.size() - 6){
				peerStart++;
			}
			if(tf.getMessage().length() == 0){
				int index = 0;
				for(Peer p: peerList){
					if(index >= peerStart && index <= peerStart + 6){
						drawButton((ModifiedPeer)p,x-157,y2-110);
						y2-=80;
					}
					index++;
				}
			}else{
				int index = 0;
				for(Peer p: peerList){
					if(p.getName().length() >= tf.getMessage().length() && p.getName().toLowerCase().substring(0,tf.getMessage().length()).equals(tf.getMessage().toLowerCase())){
						if(index >= peerStart && index <= peerStart + 6){
							drawButton((ModifiedPeer)p,x-157,y2-110);
							y2-=80;
						}
					}
					index++;
				}
			}
			if(y2 == y){
				sr.drawText("No Players Found", x + 150, y - 300, 50, 1,1,1,1);
			}
			drawChallengeMenu(x + 750, y + 100);
			if(sm.on())
				sm.draw();
		}else{
			if(countNum > 0){
				drawJoin();
			}else{
				drawBoard = true;
			}
		}
		sendInfo();
	}
	private void drawJoin(){
		if(countNum == 7){
			countDown.play();
		}
		countNum -= Core.timePassed/1000d;
		inGame = true;
		int rectW = 10;
		int maxH = 400;
		for(int i = 0; i < 1016/rectW; i++){
			double cenTime = ((1-countNum%1d));
			double timePos = (i*rectW)/(1016d);
			double height = (1/Math.abs(timePos*20 - cenTime*20)) * (double)maxH;
			height = (maxH * height - maxH)/(height + maxH * .75d);
			sr.drawRectangle(true, 450+ 53 + i * rectW, 238, rectW, (int)height,0, (float)Math.random() * .4f + .3f, 0,.5f);
		}
		joinGameBack.simpleDraw(450, 200);
		sr.drawCircle(true, 1000, 740, (int)((1-countNum%1d) * 120),.2f,.2f,.2f,.6f);
		sr.drawCircle(true, 1000, 740, (int)((1-countNum%1d) * 100),.2f,.2f,.2f,.6f);
		sr.drawCenteredText(Integer.toString((int)countNum), 1000, 660, 200, 0, .4f, 0, 1);
		try{
			sr.drawCenteredText("Joining " + joinPeer.getName(), 1000, 600, 30, .7f, .7f, .7f, 1);
			if(Button.hitDrawnButton(sr, "Cancel", 900, 400, 200, 50, .5f, .5f, .5f, .9f) && countNum < 5.5d || !im.getPeerManager().containsPeer(joinPeer)){
				im.send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J3", "Cancel"), joinPeer));
				joinPeer = null;
			}
		}catch(NullPointerException ex){
			
		}
		sr.drawRectangle(true,0, 0, 2000, 1200,0,0,0,((float)(7f-countNum))/9f);
	}
	private void drawChallengeMenu(int x, int y){
		challengeMenuBack.simpleDraw(x, y - 690);
		for(int i = 0; i < joinMessages.size(); i++){
			if(i > joinMessages.size()-7){
				joinMessages.get(i).draw(x + 38, y - 55 * (joinMessages.size() - i + 1), 372, 50);
			}
			if(!im.getPeerManager().containsPeer(joinMessages.get(i).getPeer())){
				joinMessages.remove(i);
				i--;
			}
		}
	}
	private void sendChallenge(Peer p){
		Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J1", "Challenge"), p));
		joinMessages.add(new JoinMessage(false, p, "Challenge"));
	}
	private void drawYourInfo(){
		int y = 800;
		int x = this.x - 157;
		playerInfoBack.simpleDraw(x, y - 6,861,140);
		PlayerProfile p = TitleScreen.getPlayerProfile();
		sr.drawText(p.getName(), x + 61, y + 25, 291, 30,1,1,1,1);
		if(inGame){
			sr.drawText("In Game", x + 512, y + 25, 30,1,0,0,1);
		}else{
			sr.drawText("In Lobby", x + 512, y + 25, 30,0,1,0,1);
		}
		sr.drawText(Integer.toString(p.getRank()), x + 778, y + 25,30,1,1,1,1);
	}
	private void drawButton(ModifiedPeer p, int x, int y){
		sr.drawLine(x+12, y + 7, (x+6)+(int)((sendTimer/7000d)*840d), y+7,4,0,.8f,.2f,1);
		sr.drawLine(x+12, y + 63, (x+6)+(int)((sendTimer/7000d)*840d), y+63,4,0,.8f,.2f,1);
		sr.drawLine((x+6)+(int)((sendTimer/7000d)*840d), y + 7, (x+6)+(int)((sendTimer/7000d)*840d), y + 63,0,0,.5f,1);
		if(!sm.on() && Button.overButton(x, y, 861, 70)){
			overButton.simpleDraw(x, y,861,70);
			if(MouseInput.left()){
				beep.stopAndReset();
				beep.play();
				sm = new ScreenMessage(p,"Challenge ");
			}
		}else{
			button1.simpleDraw(x, y,861,70);
		}
		sr.drawText(p.getName(), x + 61, y + 25, 291, 30,1,1,1,1);
		if(p.getInGame()){
			sr.drawText("In Game", x + 512, y + 25, 30,1,0,0,1);
		}else{
			sr.drawText("In Lobby", x + 512, y + 25, 30,0,1,0,1);
		}
		sr.drawText(Integer.toString(p.getRank()), x + 778, y + 25,30,1,1,1,1);
	}
	private void sendInfo(){
		sendTimer += Core.timePassed;
		if(sendTimer > 7000){
			im.send(true, ByteToObject.objectsToBytes("D1", new ArrayList<Object>(Arrays.asList(new Boolean(inGame),new Integer(pp.getExp()/1000)))));
			sendTimer = 0;
		}
	}
	public class KeyJ3 extends PeerKey{
		private static final String key = "J3";
		public KeyJ3(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": cancels game joining";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			if(joinPeer != null && joinPeer.equals(id)){
				joinPeer = null;
			}
		}
	}
	public class KeyJ2 extends PeerKey{
		private static final String key = "J2";
		public KeyJ2(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": recieves if the player is ready, starts if ready";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			Peer p = im.getPeerManager().getPeer(id);
			if(p!=null && !inGame){
				joinPeer = p;
			}else{
				im.send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J3", "Cancel"), id));
			}
		}
	}
	private void sendAccept(Peer p){
		im.send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J2", "d"), p));
	}
	public class KeyJ1 extends PeerKey{
		private static final String key = "J1";
		public KeyJ1(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": adds a challenge to the challenge list";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			Peer p = im.getPeerManager().getPeer(id);
			joinMessages.add(new JoinMessage(true, p, ByteToObject.bytesToString(pack)));
		}
	}
	private class JoinMessage{
		private Peer p;
		private String message;
		private boolean cordsSet = false;
		private double y;
		private double lineX = 0;
		private boolean lineRight = false;
		private float alpha = 0;
		private boolean shift = false;
		private boolean recieved;
		public JoinMessage(boolean recieved, Peer p, String message){
			this.p = p;
			this.message = message;
			this.recieved = recieved;
		}
		public void draw(int x, int y2, int w, int h){
			if(!cordsSet){
				y =y2;
				cordsSet = true;
			}
			if(y > y2){
				y -= Core.rate;
			}else if (y < y2 -4){
				y += Core.rate;
			}
			if(!shift){
				alpha += (float)(Core.rate/100d);
			}else{
				if(alpha > .04f){
					alpha -= (Core.rate/70d);
				}
				if(recieved){
					if(Button.overButton(x, y, w, h) && !sm.on()){
						messageBox2.simpleDraw(x, (int)y);
						if(MouseInput.left()){
							beep.stopAndReset();
							beep.play();
							sm = new ScreenMessage(p,"Accept Invite From ");
						}
					}else{
						messageBox.simpleDraw(x, (int)y);
					}
				}else{
					messageBox3.simpleDraw(x, (int)y);
				}
				sr.drawText(p.getName() + ": " + message, x + 20, (int)y + 13, 315, 30,0,1,.3f,.9f);
			}
			if(lineRight){
				lineX += Core.rate * 5;
				if(lineX > w){
					lineX = w;
					lineRight = false;
				}
			}else{
				lineX -= Core.rate * 5;
				if(lineX < 0){
					lineX = 0;
					lineRight = true;
				}
			}
			if(alpha > .93f){
				shift = true;
			}
			if(alpha > 1){
				alpha = 1;
			}
			if(recieved){
				sr.drawRectangle(true, x, (int)y, w, 50,.3f,0,0,alpha);
			}else{
				sr.drawRectangle(true, x, (int)y, w, 50,0,.3f,0,alpha);
			}
			sr.drawRectangle(true, x + (int)lineX, (int)y, 5, 50 ,1, 0 ,0,alpha);
			sr.drawRectangle(true, (x + (w-(int)lineX)), (int)y, 5, 50 ,0, 0,1,alpha);
		}
		public Peer getPeer(){
			return p;
		}
	}
	private class ScreenMessage{
		private Peer p;
		private String msg;
		private boolean on = false;
		private boolean lowerAlpha = false;
		private float alpha = 0;
		public ScreenMessage(){
			on = false;
		}
		public ScreenMessage(Peer p, String msg){
			this. p = p;
			this.msg = msg;
			on = true;
		}
		public void draw(){
			if(alpha < 0){
				alpha = 0;
			}else if (alpha > 1){
				alpha = 1;
			}
			sr.drawRectangle(true, 0, 0, 2000, 1100,0f,0f,0f,alpha);
			if(!lowerAlpha && alpha <= .8f){
				alpha += (float)(Core.rate/40d);
			}
			if(Button.overButton(866, 431, 260, 60)){
				challengeButton2.simpleDraw(443, 400);
				if(alpha > 1){
					alpha = 1;
				}else if (alpha < 0){
					alpha = 0;
				}
				sr.drawCenteredText(msg + p.getName() + "?", 1003, 530, 35, 0, 1, 0, alpha);
				if(MouseInput.left() && alpha > .8f){
					beep.stopAndReset();
					beep.play();
					if(msg.indexOf("Challenge") != -1){
						sendChallenge(p);
						lowerAlpha = true;
					}else{
						sendAccept(p);
						joinPeer = p;
						lowerAlpha = true;
					}
				}
			}else{
				challengeButton.simpleDraw(443, 400);
				sr.drawCenteredText(msg + p.getName() + "?", 1003, 530, 35, 0, 1, 0, alpha);
				if(MouseInput.left() && alpha > .8f){
					lowerAlpha = true;
				}
			}
			if(lowerAlpha && alpha > .03f){
				alpha -= (float)(Core.rate/40d);
			}else if(lowerAlpha){
				on = false;
			}
		}
		public boolean on(){
			return on;
		}
	}
}
