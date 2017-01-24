package Launcher;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import pack.Core;
import pack.KeyInput;
import pack.MouseInput;
import JBasics.AlertManager;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.SoundParent;
import JBasics.Sprite;
import JBasics.StreamSound;
import JInternet.ByteToObject;
import JInternet.ChatBox;
import JInternet.PackageProcessing;
import JInternet.PeerKey;
import JInternet.PeerManager.Peer;
import Launcher.InternetManager.ModifiedPeerManager.ModifiedPeer;

public class Game {

	private Board ourBoard;
	private Board enemyBoard;
	private static SoundParent music;
	private Sprite system;
	private static ChatBox chat;
	private ShapeRenderer sr;
	private ArrayList<GridPlane> gridPlanes;
	private double opponentLineY = 0;
	private Sound missedSound;
	private Sound hitSound;
	private boolean skip = false;
	private boolean opponentLineUp = true;
	private boolean chatInit = false;
	public static Sound inputClick;
	private Sprite pointError;
	private Sound lockBeep;
	private GameInputMenu gim;
	private HashMap<String,String> cordHistory;
	private boolean gameOn = false;
	private boolean opponentReady = false;
	private boolean ourReady = false;
	private boolean tutorialSet = false;
	private double gamePowerRectPos = 0;
	private boolean gamePowerRectIn = true;
	private boolean ourTurn = false;
	private float lockCordAlpha = 0;
	private boolean firstRun = true;
	private boolean attackSent = false;
	private double planeWorldWidth = 0;
	private double showErrorTimer = 600;
	private double finishTimer = 0;
	private float fadeAlpha = 1;
	private int instructionStage = 0;
	private Sprite planeBack;
	private Sprite shipInstructions;
	private Sprite pointInstructions;
	private Sprite launchInstructions;
	private ArrayList<Point> selectedPoints;
	private TypeWordEffect gameReadyText;
	private TypeWordEffect inputOffText;
	private boolean endGame = false;
	private PlaneWorldManager pwm;
	private Sprite fireButton;
	private Sprite fireButton2;
	private ExpManager expManager;
	private double placeSelectPointTimer = 0;
	private double instructionAlpha = 1;
	private Sound missedShip;
	private Sound powUp;
	private Sound shipHit;
	private Sound shipDestroyed;
	private Sound readyOn;
	private Sound readyOff;
	private Sound carrierDown;
	private Sound submarineDown;
	private Sound destroyerDown;
	private Sound transporterDown;
	public Game(){
		gim = new GameInputMenu();
		missedShip = new Sound("shipMissed.wav");
		if(chat == null){
			chat = new ChatBox(Main.getIMananager());
		}
		ourBoard = new Board(true,540,35,1000,1000);
		enemyBoard = new Board(false,540,35,1000,1000);
		if(music == null){
			if(Main.badRam){
				music = new StreamSound("loadingSound.wav",.7d);
			}else{
				music = new Sound("loadingSound.wav",.7d);
			}
		}
		shipInstructions = new Sprite("shipInstructions.png");
		pointInstructions = new Sprite("pointInstructions.png");
		launchInstructions = new Sprite("launchInstructions.png");
		system = new Sprite("systemForground.png");
		cordHistory = new HashMap<String,String>();
		lockBeep = new Sound("lockBeep.wav",.9d);
		inputClick = new Sound("inputPressed.wav",.76d);
		readyOn = new Sound("playerReady.wav",.8d);
		readyOff = new Sound("playerNotReady.wav",.8d);
		selectedPoints = new ArrayList<Point>();
		gameReadyText = new TypeWordEffect("The Game Has Begun", 1000,500,100,0,.6f,0,1);
		inputOffText = new TypeWordEffect("Offline...", 246, 701,30 ,1,0,0,1);
		planeBack = new Sprite("planeBox.png");
		pointError = new Sprite("pointError.png");
		sr = new ShapeRenderer();
		pwm = new PlaneWorldManager(sr,4,1553 - PlaneWorld.SURROUND_DISTANCE,80,500,900);
		gridPlanes = new ArrayList<GridPlane>();
		missedSound = new Sound("targetMissed.wav",.92f);
		hitSound = new Sound("targetHit.wav",.92f);
		transporterDown = new Sound("transporterDown.wav",.92f);
		submarineDown = new Sound("submarineDown.wav",.92f);
		destroyerDown = new Sound("destroyerDown.wav",.92f);
		fireButton = new Sprite("fireButton.png");
		fireButton2 = new Sprite("fireButton2.png");
		carrierDown = new Sound("carrierDown.wav",.92f);
		shipHit = new Sound("shipHit.wav",.92f);
		shipDestroyed = new Sound("shipDown.wav",.92f);
		expManager = new ExpManager(300,60);
		PackageProcessing.addToKeyRoutines(new KeyJ7());
		PackageProcessing.addToKeyRoutines(new KeyJ8());
		PackageProcessing.addToKeyRoutines(new KeyJ9());
		PackageProcessing.addToKeyRoutines(new KeyK0());
		PackageProcessing.addToKeyRoutines(new KeyK1());
	}
	private void manageBackGround(Peer opponent){
		if(!chatInit){
			chat.enableVoice(opponent.getID());
			chatInit = true;
		}
		sr.drawRectangle(true, 0, 0, 2000, 1100,.3f,.3f,.3f,.5f);
		system.simpleDraw(0, 0, 1980,1080);
		music.play();
		music.setLoop(true);
	}
	private void setSelectPointManager(Peer opponent){
		placeSelectPointTimer -= Core.rate;
		Point p = enemyBoard.getPoint(MouseInput.getX(), MouseInput.getY());
		if(p!=null){
			if(selectedPoints.size() < 4){
				p.drawShipPoint();
				if(MouseInput.left() && placeSelectPointTimer < 0){
					boolean found = false;
					for(int i = 0; i < selectedPoints.size(); i++){
						if(selectedPoints.get(i).equals(p)){
							found = true;
							p.drawNonShipPoint();
						}
					}
					if(!found){
						inputClick.stopAndReset();
						inputClick.play();
						placeSelectPointTimer = 30;
						selectedPoints.add(p);
					}
				}
			}else{
				p.drawNonShipPoint();
			}
			if(MouseInput.right()){
				for(int i = 0; i < selectedPoints.size(); i++){
					if(selectedPoints.get(i).equals(p)){
						inputClick.stopAndReset();
						inputClick.play();
						selectedPoints.remove(i);
						break;
					}
				}
			}
		}
	}
	private boolean pointCheck(int radius, double radian){
		for(Point p: selectedPoints){
			if(!p.equals(radius,radian)){
				return false;
			}
		}
		return true;
	}
	private void drawAttackButton(Peer opponent, int x, int y, int w, int h){
		if(lockCordAlpha > .5f){
			if(Button.overButton(x, y, w, h)){
				fireButton2.simpleDraw(x, y-3);
				if(MouseInput.left()){
					inputClick.stopAndReset();
					inputClick.play();
					sendAttack(opponent,gim.getSelectedRadius(),gim.getSelectedRadian());
				}
			}else{
				fireButton.simpleDraw(x, y-3);
			}
		}
		if(selectedPoints.size() == 4){
			if(lockCordAlpha < .9f){
				lockCordAlpha += (float)(Core.rate/40d);
			}
			if(lockCordAlpha > 1){
				lockCordAlpha = 1;
			}
		}else{
			if(lockCordAlpha > .1f){
				lockCordAlpha -=(float)(Core.rate/40d);
			}
			if(lockCordAlpha < 0){
				lockCordAlpha = 0;
			}
		}
	}
	public void endGame(){
		endGame = true;
		music.stopAndReset();
	}
	public boolean getEndGame(){
		return endGame;
	}
	private void ourTurnActions(Peer opponent){
		if(attackSent){
			attackSentAction();
		}
		enemyBoard.draw();
		gim.draw(66, 426, 359, 549);
		if(!attackSent){
			drawAttackButton(opponent,984,496,113,78);
			if(gim.inputLocked()){
				setSelectPointManager(opponent);
			}
			for(int i = 0; i < selectedPoints.size(); i++){
				selectedPoints.get(i).drawShipPoint();
			}
		}else{
			manageError(opponent);
		}
		if(attackSent && planeWorldWidth >= 1000){
			drawGridPlanes(opponent);
		}
	}
	private void opponentTurnActions(Peer opponent){
		ourBoard.draw();
		inputOffText.draw(.6d);
		if(inputOffText.done()){
			inputOffText.reset();
		}
	}
	private void gameOnManager(Peer opponent){
		if(ourTurn){
			ourTurnActions(opponent);
		}else{
			opponentTurnActions(opponent);
		}
		expManager.draw();
	}
	private void gameOffManager(){
		instructionStage = 1;
		ourBoard.draw();
		sr.drawRectangle(true, 1615, 872, 293, 50,.2f,.2f,.2f,.8f);
		sr.drawRectangle(true, 1619, 876, 287, 44,0f,0f,0f,.4f);
		String s = new String("Opponent Ready");
		if(!tutorialSet){
			TutorialManager.setTutorialPage(30, 390, "Board Set-Up", new ArrayList<String>(Arrays.asList("Selecting a Ship: To select a ship to place, left click a ship in the box titled"
					+ " \"Your Fleet\".  Once a ship is selected, its box will be highlighted white.", "Placing a Ship: Once a ship is selected, drag the mouse over the board.  There "
							+ "will be green squares around the points your ship will be placed. If these sqares are red, it means there is something obstructing the ship and, therefore, "
							+ "cannot be placed. You can also right click to change the orientation of the ship. Once the squares are all green, you can left click to place the ship. "
							+ "If you want to cancel ship placement left click somewhere outside the board. Click "
							+ "ready when you are finished setting up your ship. You can check if your opponent is ready by looking at their status above ship list.",
							"Recalling a Ship: If you placed a ship and want to change its location you can recall it to the ship list. You can do this by clicking \"Recall\" in the "
							+ "ship list. Once recall is selected, left click the ship you want to remove once it is surrounded by red rectangles. To get out of recall mode,"
							+ " click the recall button or select a ship from your ship list.")));
			tutorialSet = true;
		}
		if(!opponentReady){
			s = "Opponent Not Ready";
			sr.drawCenteredText(s, 1761, 887, 22, 1, 0, 0, 1);
		}else{
			sr.drawCenteredText(s, 1761, 887, 22, 0, 1, 0, 1);
		}
	}
	public void draw(Peer opponent){
		manageBackGround(opponent);
		chat.draw(25, 37, 454, 250, 22);
		drawOpponentInfo((ModifiedPeer)opponent, 1740, 41, 204, 154, 23);
		if(!gameOn){
			gameOffManager();
		}else{
			gameOnManager(opponent);
		}
		if(gamePowerRectPos > 2 || !gameOn){
			if(opponentReady && ourReady){
				if(gamePowerRectIn){
					if(gamePowerRectPos == 0){
						powUp.play();
						TutorialManager.setTutorialPage(1600, 100, "Communications", new ArrayList<String>(Arrays.asList("Text Coms: To message your opponent with typed messages, you can"
								+ " enter a message in the textfield located in the box labelled communications in the bottom left of the screen. Press enter while the textfield is selected"
								+ " to send.", "Voice Coms: To talk to your opponent, hold \"G\" while talking. If you are transmitting, a blue microphone symbol will appear at the top left"
										+ "of the chat box. If the enemy is talking a red microphone symbol will appear at the top right of the chat box.")));
						TutorialManager.setTutorialPage(1560, 290, "Attacking", new ArrayList<String>(Arrays.asList("Attack Basics: Battle Ship uses a polar coordinate system to attack"
								+ ". Therefore, on each attack turn, you will enter a radius and a radian. The radius and radian will be used to attack all 4 quadrants of the circle."
								+ " This means that for each turn you will be attacking 4 points.", "Locking Coordinates: In the box labelled \"Input\", in the top left, select a "
										+ "radius and radian by left clicking on the value you want to use. Once a radius and radian are selected, a button saying \"Lock Input\" "
										+ "will appear. To lock your coordinates left click the button.", "Selecting Points: After you lock coordinates, you must select the points "
												+ "on the board that represent your radius and radian.  Hovering your mouse above the board, you can see a green square will rotate around"
												+ " a point. If the point represents your coordinate, you can left click to select the point. If you place an incorrect point, you can right "
												+ "click to remove the selected point. Once there are 4 selected points, the fire option will appear in the center of the board. Click it "
												+ "to begin the attack.", "Attack Process: When planes appear in the right side of the screen, you can press space to skip through the launch."
												+ " Whether a point hit a ship or not will be told once the bomb (the red dot below the plane) has dropped. The status of your hit will have"
												+ " visual ques (Red if Hit Blue if Not) and audio ques (the voice).")));
					}
					sr.drawRectangle(true, 0, 0, (int)gamePowerRectPos,1100,.2f,.2f,.2f,1);
					sr.drawRectangle(true, 1990 - (int)gamePowerRectPos, 0, (int)gamePowerRectPos,1100,.2f,.2f,.2f,1);
					if(gamePowerRectPos > 950){
						lockBeep.play();
					}
					if(gamePowerRectPos > 1000){
						gameReadyText.draw(.1d);
					}else{
						gamePowerRectPos += Core.rate * 20;
					}
					if(gameReadyText.done()){
						gamePowerRectIn = false;
						powUp.stopAndReset();
						lockBeep.setLoop(false);
						lockBeep.stopAndReset();
					}
				}else{
					sr.drawRectangle(true, 0, 0, (int)gamePowerRectPos,1100,.2f,.2f,.2f,1);
					sr.drawRectangle(true, 1990 - (int)gamePowerRectPos, 0, (int)gamePowerRectPos,1100,.2f,.2f,.2f,1);
					gameOn = true;
					ourBoard.setShipList(false);
					gamePowerRectPos -= Core.rate * 20;
				}
			}
		}
		if(ourReady != ourBoard.shipsReady()){
			ourReady = ourBoard.shipsReady();
			if(opponentReady && ourReady){
				ourTurn = coinFlip(opponent);
			}
			Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.objectsToBytes("J7", new ArrayList<Object>(Arrays.asList(new Boolean(ourReady)))),opponent));
		}
		if(!Main.getIMananager().getPeerManager().containsPeer(opponent)){
			endGame();
			Main.addAlert(AlertManager.MIDDLE_OF_SCREEN, "The opponent disconnected");
		}
		fadeAlpha -= (float)(Core.rate/300d);
		if(fadeAlpha > 0){
			sr.drawRectangle(true, 0, 0, 2000,1200,0,0,0,fadeAlpha);
		}
		drawInstructions();
	}
	private void drawInstructions(){
		int x = 500;
		int y = 11;
		if(KeyInput.keyPressed("I")){
			instructionAlpha = 1;
		}
		instructionAlpha -= Core.rate/800d;
		if(instructionAlpha > 0 && !Button.overButton(0, 0, 2000, 52)){
			if(instructionStage == 1){
				shipInstructions.simpleDraw(x, y);
			}else if(instructionStage == 2){
				pointInstructions.simpleDraw(x, y);
			}else{
				launchInstructions.simpleDraw(x, y);
			}
		}
	}
	private boolean coinFlip(Peer opponent){
		double chance = Math.random();
		if(chance < .5d){
			Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("K1", "0"),opponent));
			return true;
		}
		Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("K1", "1"),opponent));
		return false;
	}
	private void endTurn(Peer opponent){
		expManager.reset();
		pwm = new PlaneWorldManager(sr,4,1553 - PlaneWorld.SURROUND_DISTANCE,80,500,900);
		planeWorldWidth = 0;
		lockCordAlpha = 0;
		firstRun = false;
		gim = new GameInputMenu();
		selectedPoints.clear();
		attackSent = false;
		ourTurn = false;
		finishTimer = 0;
		gridPlanes.clear();
		skip = false;
		Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.stringToBytes("J9", "HE"),opponent));
	}
	private void sendAttack(Peer opponent, String mag, String rad){
		if(!attackSent){
			int iMag = Integer.valueOf(mag);
			double iRad = 0;
			if(rad.equals("PI/6")){
				iRad = Math.PI/6d;
			}else if(rad.equals("PI/4")){
				iRad = Math.PI/4;
			}else if(rad.equals("PI/3")){
				iRad = Math.PI/3;
			}
			if(pointCheck(iMag, iRad)){
				createGridPlanes();
				cordHistory.put(mag,rad);
			}else{
				showErrorTimer = 0;
				lockBeep.stopAndReset();
				lockBeep.setTime(0);
				lockBeep.play();
			}
			selectedPoints = orderPoints(selectedPoints);
			attackSent = true;
		}
	}
	private void createGridPlanes(){
		for(int i = selectedPoints.size()-1;i >= 0; i--){
			gridPlanes.add(new GridPlane(selectedPoints.get(i)));
		}
		gridPlanes.sort(new RadComp());
		if(firstRun){
			instructionAlpha = 1;
		}
		instructionStage = 3;
	}
	private void manageError(Peer opponent){
		if(showErrorTimer < 500){
			showErrorTimer += Core.rate;
			if(((int)showErrorTimer/20)%2 == 0){
				pointError.simpleDraw(770, 440);
			}
			if(!lockBeep.playing()){
				showErrorTimer = 600;
				endTurn(opponent);
			}
		}
	}
	private void attackSentAction(){
		if(showErrorTimer > 500){
			planeBack.simpleDraw(1040, 35,(int)planeWorldWidth,1000);
			if(planeWorldWidth < 1000){
				planeWorldWidth += Core.rate * 6;
			}else{
				pwm.draw();
			}
		}
	}
	private void drawGridPlanes(Peer opponent){
		boolean planeCalled = false;
		boolean allowSkip = true;
		for(int i = pwm.getPlanes().size()-1; i >=0; i--){
			if(pwm.getPlanes().get(i).passForward()){
				boolean drawGrid = false;
				if(!pwm.getPlanes().get(i).bombDone() && !planeCalled){
					drawGrid = true;
					planeCalled = true;
				}
				gridPlanes.get(i).draw(drawGrid,Core.rate/2.7d);
			}else{
				boolean drawGrid = false;
				if(!pwm.getPlanes().get(i).bombDone() && !planeCalled){
					drawGrid = true;
					planeCalled = true;
				}else if(pwm.getPlanes().get(i).bombDone()){
					gridPlanes.get(i).sendAttack(opponent);
				}
				gridPlanes.get(i).draw(drawGrid,0);
			}
			if(pwm.getPlanes().get(i).bombDropped()){
				allowSkip = false;
			}
			selectedPoints.get(i).drawShipPoint();
		}
		if(KeyInput.keyPressed("Space") && allowSkip){
			if(!skip){
				pwm.skip();
				skipGridPlanes();
				skip = true;
			}
		}
		if(!planeCalled){
			finishTimer += Core.rate;
			if(finishTimer > 300){
				endTurn(opponent);
			}
		}
	}
	private void skipGridPlanes(){
		for(int i = 0; i < gridPlanes.size(); i++){
			gridPlanes.get(i).skip(i);
		}
	}
	private ArrayList<Point> orderPoints(ArrayList<Point> points){
		ArrayList<Point> modPoints = new ArrayList<Point>();
		while(points.size() > 0){
			double minAngle =  Math.atan2(points.get(0).getY() - 534, points.get(0).getX() - 1054);
			int minIndex = 0;
			for(int i = 1; i < points.size(); i++){
				double angle = Math.atan2(points.get(i).getY() - 534, points.get(i).getX() - 1054);
				if(angle < minAngle){
					minAngle = angle;
					minIndex = i;
				}
			}
			modPoints.add(points.remove(minIndex));
		}
		return modPoints;
	}
	private void drawOpponentInfo(ModifiedPeer p, int x, int y, int w, int h, int fontHeight){
		if(opponentLineUp){
			opponentLineY += Core.rate;
			if(opponentLineY > h){
				opponentLineUp = false;
			}
		}else{
			opponentLineY -= Core.rate;
			if(opponentLineY < 0){
				opponentLineUp = true;
			}
		}
		sr.drawText("Name:", x, y + h - fontHeight, fontHeight, 1,1,1,.7f);
		sr.drawText(p.getName(), x, y + h - fontHeight * 2, w-10,fontHeight,1,1,1,.7f);
		sr.drawText("Rank:", x, y + h - fontHeight * 4, fontHeight, 1,1,1,.7f);
		sr.drawText(Integer.toString(p.getRank()), x, y + h - fontHeight * 5,fontHeight,1,1,1,1);
		sr.drawRectangle(true, x, y + (int)opponentLineY, w, fontHeight/4,0,1,0,1);
		sr.drawRectangle(true, x, y - (int)opponentLineY + h, w, fontHeight/4,0,1,0,1);
	}
	private synchronized void playReadyOn(){
		readyOff.pause();
		readyOn.stopAndReset();
		readyOn.play();
	}
	private synchronized void playReadyOff(){
		readyOn.pause();
		readyOff.stopAndReset();
		readyOff.play();
	}
	private class KeyJ9 extends PeerKey{
		private static final String key = "J9";
		public KeyJ9(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": When recieved will set turn to our turn";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			expManager.reset();
			ourTurn = true;
		}
	}
	private class KeyJ8 extends PeerKey{
		private static final String key = "J8";
		public KeyJ8(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": recieves the magnitude and radius of the other players attacks";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			ArrayList<Object> objs = ByteToObject.bytesToObjects(pack);
			int x = (Integer)objs.get(0);
			int y = (Integer)objs.get(1);
			Point p = ourBoard.getPointManager().getNearestPoint(x, y);
			p.fire();
			Ship s = ourBoard.getShipManager().getShip(p);
			int shipID = 0;
			if(s != null && s.destroyed()){
				shipID = s.getLength();
				shipDestroyed.stopAndReset();
				shipDestroyed.play();
			}else if(s!=null){
				shipHit.stopAndReset();
				shipHit.play();
			}else{
				missedShip.stopAndReset();
				missedShip.play();
			}
			Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.objectsToBytes("K0", new ArrayList<Object>(Arrays.asList(new Integer(x), new Integer(y),new Boolean(s != null), new Integer(shipID)))),id));
		}
	}
	private class KeyK0 extends PeerKey{
		private static final String key = "K0";
		public KeyK0(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": Recieves if a point was a hit or not";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			ArrayList<Object> objs = ByteToObject.bytesToObjects(pack);
			int x = (Integer)objs.get(0);
			int y = (Integer)objs.get(1);
			boolean hit = (Boolean)objs.get(2);
			int sID = (Integer)objs.get(3);
			Point p = enemyBoard.getPointManager().getNearestPoint(x, y);
			p.fire(hit);
			if(sID != 0){
				expManager.addKillShipExp(sID);
				if(sID == 5){
					carrierDown.stopAndReset();
					carrierDown.play();
				}else if(sID == 4){
					submarineDown.stopAndReset();
					submarineDown.play();
				}else if(sID == 3){
					destroyerDown.stopAndReset();
					destroyerDown.play();
				}else{
					transporterDown.stopAndReset();
					transporterDown.play();
				}
			}else if(hit){
				hitSound.stopAndReset();
				hitSound.play();
			}else{
				missedSound.stopAndReset();
				missedSound.play();
			}
		}
	}
	private class KeyK1 extends PeerKey{
		private static final String key = "K1";
		public KeyK1(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + "Recieves a string of 0 or 1 representing whether or not its our turn";
		}
		@Override
		public synchronized void routine(int id, DatagramPacket pack) {
			String turn = ByteToObject.bytesToString(pack);
			if(turn.equals("0")){
				ourTurn = false;
			}else{
				ourTurn = true;
			}
		}
	}
	private class KeyJ7 extends PeerKey{
		private static final String key = "J7";
		public KeyJ7(){
			super(key);
		}
		@Override
		public String getKey(){
			return key;
		}
		@Override
		public String getDescription() {
			return key + ": sets the opponent ready boolean in ourBoard";
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			opponentReady = (boolean)(ByteToObject.bytesToObjects(pack).get(0));
			if(opponentReady)
				playReadyOn();
			else
				playReadyOff();
		}
	}
	
	private class GameInputMenu{
		private ArrayList<String> degrees;
		private ArrayList<String> radius;
		private String selectedDegree = null;
		private String selectedRadius = null;
		private boolean locked = false;
		private float lockAlpha = 0;
		private double lockRectHeight = 0;
		private boolean lockRectUp = true;
		private double showLockTimer = 0;
		private double enterPointFlashTimer = 0;
		private Sprite lock;
		private Sound powDown;
		public GameInputMenu(){
			degrees = new ArrayList<String>(Arrays.asList("0", "PI/6","PI/4","PI/3"));
			radius = new ArrayList<String>(Arrays.asList("1", "2" , "3", "4", "5", "6"));
			lock = new Sprite("lock.png");
			powUp = new Sound("powUp.wav",.76d);
			powDown = new Sound("powDown.wav",.76d);
		}
		public boolean inputLocked(){
			return locked;
		}
		public void draw(int x, int y, int w, int h){
			if(!lockRectUp){
				drawLock(x,y,w,h);
			}else{
				drawUnLock(x,y,w,h);
			}
			if(locked){
				if(lockRectUp){
					if(lockRectHeight == 0){
						powUp.play();
					}
					lockRectHeight += Core.rate*4;
					sr.drawRectangle(true, x, y, w, (int)lockRectHeight,.5f,.5f,.5f,1);
					sr.drawRectangle(true, x, y - (int)lockRectHeight + h, w, (int)lockRectHeight,.5f,.5f,.5f,1);
					if(lockRectHeight > h/2){
						lockRectUp = false;
						powUp.stopAndReset();
						powDown.stopAndReset();
						lockBeep.stopAndReset();
						lockBeep.play();
					}
				}else{
					if(showLockTimer < 120){
						sr.drawRectangle(true, x, y, w, (int)lockRectHeight,.5f,.5f,.5f,1);
						sr.drawRectangle(true, x, y - (int)lockRectHeight + h, w, (int)lockRectHeight,.5f,.5f,.5f,1);
						showLockTimer += Core.rate;
						if(((int)showLockTimer/10)%2 == 0){
							lock.simpleDraw(x, y + h/5, w, w);
						}
					}else if(lockRectHeight > 2){
						if(lockRectHeight > h/2){
							powDown.play();
						}
						lockBeep.pause();
						lockRectHeight -= Core.rate*4;
						sr.drawRectangle(true, x, y, w, (int)lockRectHeight,.5f,.5f,.5f,1);
						sr.drawRectangle(true, x, y - (int)lockRectHeight + h, w, (int)lockRectHeight,.5f,.5f,.5f,1);
					}
				}
			}
		}
		public String getSelectedRadian(){
			return selectedDegree;
		}
		public String getSelectedRadius(){
			return selectedRadius;
		}
		private void drawLock(int x, int y, int w, int h){
			if(!lockRectUp){
				int fontSize = 30;
				enterPointFlashTimer += Core.rate/3;
				if((int)(enterPointFlashTimer/10)%2 == 0){
					sr.drawCenteredText("Enter Points in Radar", x + w/2, y + h/2 - (int)(fontSize/2), fontSize, 1, .7f, .7f, 1);
				}
				sr.drawCenteredText("Radian", x + w/2, y + 3*h/4,fontSize,1,1,1,1);
				sr.drawCenteredText(selectedDegree, x + w/2, y + 3*h/4 - fontSize,fontSize,1,1,1,1);
				sr.drawCenteredText("Radius", x + w/2, y + h/4,fontSize,1,1,1,1);
				sr.drawCenteredText(selectedRadius, x + w/2, y + h/4 - fontSize,fontSize,1,1,1,1);
			}
		}
		private void drawUnLock(int x, int y, int w, int h){
			float r = .2f,g = .2f,b = .2f,a = .4f;
			float tR = 1f, tG = 1f, tB = 1f, tA = 1f;
			float fontSize = 25;
			int buttonHeight = (int)fontSize * 2;
			sr.drawRectangle(true, x, y + buttonHeight, w/2, h - buttonHeight,r,g,b,a);
			sr.drawRectangle(true, x + w/2, y + buttonHeight, w/2, h - buttonHeight,r,g,b,a);
			sr.drawCenteredText("Radian", x + 3*w/4, y + h - (int)fontSize, fontSize, tR, tG, tB, tA);
			sr.drawCenteredText("Radius", x + w/4, y + h - (int)fontSize, fontSize, tR, tG, tB, tA);
			double radianHeight = (int) (((double)h - (double)fontSize - buttonHeight)/((double)degrees.size()));
			double radiusHeight = (int) (((double)h - (double)fontSize - buttonHeight)/((double)radius.size()));
			for(int i = 0; i < degrees.size(); i++){
				if(Button.hitDrawnButton(sr, degrees.get(i), x + w/12+ w/2, y + h - fontSize - radianHeight * (i+1), w/2 - w/6, radianHeight * .9d, r * 2,  g * 2, b * 2, a * 2)){
					selectedDegree = degrees.get(i);
					inputClick.stopAndReset();
					inputClick.play();
				}
				if(selectedDegree != null && degrees.get(i).equals(selectedDegree)){
					sr.drawRectangle(true,x + w/12+ w/2, y + h - (int)fontSize - (int)(radianHeight * (i+1)), w/2 - w/6, (int)(radianHeight * .9d),0,0,0,.6f);
				}
			}
			for(int i = 0; i < radius.size(); i++){
				if(Button.hitDrawnButton(sr, radius.get(i), x + w/12, y + h - fontSize - radiusHeight * (i+1), w/2 - w/6, radiusHeight * .9d, r * 2,  g * 2, b * 2, a * 2)){
					selectedRadius = radius.get(i);
					inputClick.stopAndReset();
					inputClick.play();
				}
				if(selectedRadius != null && radius.get(i).equals(selectedRadius)){
					sr.drawRectangle(true,x + w/12, y + h - (int)fontSize - (int)(radiusHeight * (i+1)), w/2 - w/6, (int)(radiusHeight * .9d),0,0,0,.6f);
				}
			}
			int buttonWidth = w/2;
			if(selectedRadius != null && selectedDegree != null && inCordHistory(selectedRadius,selectedDegree)){
				if(lockAlpha < .7f){
					lockAlpha += (float)(Core.rate/40d);
				}
			}else if(lockAlpha > 0){
				lockAlpha -= (float)(Core.rate/40d);
				if(lockAlpha < 0){
					lockAlpha = 0;
				}
			}
			if(Button.hitDrawnButton(sr, "Lock Input", x + w/2 - buttonWidth/2, y, buttonWidth, buttonHeight * .9d, 0, .4f, 0,lockAlpha) && selectedRadius != null && selectedDegree != null && inCordHistory(selectedRadius,selectedDegree) && !expManager.defeat()){
				locked = true;
				if(firstRun){
					instructionAlpha = 1;
				}
				instructionStage = 2;
				inputClick.stopAndReset();
				inputClick.play();
			}else if(selectedRadius != null && selectedDegree != null && !inCordHistory(selectedRadius,selectedDegree)){
				sr.drawCenteredText("Cordinates Already Called", x + w/2, y + 7, 26, 1, .2f, .2f, 1);
			}
		}
		private boolean inCordHistory(String radius, String radian){
			if(cordHistory.get(radius) != null && cordHistory.get(radius).equals(radian))
				return false;
			return true;
		}
	}
	private class TypeWordEffect{
		private int wordIndex = 0;
		private String word;
		private int x;
		private int y;
		private float fontSize;
		private float r = 1;
		private float g = 1;
		private float b = 1;
		private float a = 1;
		private float br;
		private float bg;
		private float bb;
		private float bAOff;
		private double timer = 0;
		private boolean backGroundOn = false;
		public TypeWordEffect(String word, int x, int y, float fontSize, float r, float g, float b, float a){
			this.word = word;
			this.x = x;
			this.y = y;
			this.fontSize = fontSize;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
		public TypeWordEffect(String word, float br, float bg, float bb,float aOff){
			this.word = word;
			this.br = br;
			this.bg = bg;
			this.bb = bb;
			this.bAOff = aOff;
			backGroundOn = true;
		}
		public TypeWordEffect(String word){
			this.word = word;
		}
		public void draw(double secPerLetter){
			timer += Core.timePassed/1000d;
			if(wordIndex < word.length() && timer > secPerLetter){
				wordIndex++;
				timer = 0;
			}
			if(wordIndex >= word.length()){
				a -= (float)(Core.rate/40d);
			}
			if(((int)(timer*2))%2==0 || wordIndex < word.length()){
				if(a > 0){
					drawBackGround(true, x, y, fontSize);
					sr.drawCenteredText(word.substring(0,wordIndex), x, y, fontSize, r, g, b, a);
				}
			}
		}
		public void draw(double secPerLetter, double alphaFadeDividen, boolean centered, boolean flashOn, int x, int y, float fontSize, float r, float g, float b){
			timer += Core.timePassed/1000d;
			if(wordIndex < word.length() && timer > secPerLetter){
				wordIndex++;
				timer = 0;
			}
			if(wordIndex >= word.length()){
				a -= (float)(Core.rate/alphaFadeDividen);
			}
			if(((int)(timer*2))%2==0 || wordIndex < word.length() || !flashOn){
				if(a > 0){
					drawBackGround(centered, x, y, fontSize);
					if(centered){
						sr.drawCenteredText(word.substring(0,wordIndex), x, y, fontSize, r, g, b, a);
					}else{
						sr.drawText(word.substring(0,wordIndex), x, y, fontSize, r, g, b, a);
					}
				}
			}
		}
		private void drawBackGround(boolean centered, int x, int y,float fontSize){
			if(backGroundOn){
				float modAlpha = a - bAOff;
				if(modAlpha < 0){
					modAlpha = 0;
				}
				int w = sr.getFontWidth(word, fontSize,0,wordIndex) + 5;
				if(centered){
					sr.drawRectangle(true, x - w/2, y-2, w, (int)fontSize + 4, br,bg,bb,modAlpha);
				}else{
					sr.drawRectangle(true, x, y-2, w, (int)fontSize + 4, br,bg,bb,modAlpha);
				}
			}
		}
		public boolean done(){
			return a <= 0;
		}
		public void reset(){
			wordIndex = 0;
			timer = 0;
			a = 1;
		}
	}
	private class GridPlane{
		private double vX;
		private double vY;
		private double x;
		private double y;
		private Sprite gridCover;
		private double rad;
		private int mag;
		private boolean sentAttack = false;
		private boolean called = false;
		double targetX;
		double targetY;
		private Point linkedPoint;
		private Sprite plane;
		private Sprite hitCover;
		private Sprite missCover;
		private float endAlpha = 1;
		public GridPlane(Point target){
			rad = Math.atan2(target.getY() - 535, target.getX() - 1054);
			mag = target.getMag();
			x = 1054;
			y = 535;
			vX = Math.cos(rad) * mag;
			vY = Math.sin(rad) * mag;
			targetX = target.getX();
			targetY = target.getY();
			plane = new Sprite("gridPlane.png");
			gridCover = new Sprite("circleCover.png");
			linkedPoint = target;
			hitCover = new Sprite("hitCover.png");
			missCover = new Sprite("missCover.png");
		}
		public void skip(int num){
			if(num == 3){
				x = 1054 + vX * 85.215;
				y = 535 + vY * 85.215;
			}else if(num == 2){
				x = 1054 + vX * 9.213;
				y = 535 + vY * 9.213;
			}
		}
		public void draw(boolean drawCover, double timeScale){
			if(inRange()){
				x += vX * timeScale;
				y += vY * timeScale;
			}else if(!drawCover && called){
				endAlpha -= .01f * (float)Core.rate;
				if(endAlpha > 0){
					if(linkedPoint.isHitSpecial()){
						hitCover.simpleDraw(544, 33,1000,1000,-Math.atan2(vY, vX) + Math.PI/4);
					}else{
						missCover.simpleDraw(544, 33,1000,1000,-Math.atan2(vY, vX) + Math.PI/4);
					}
				}
			}
			if(!sentAttack){
				plane.simpleDraw((int)x - 15, (int)y - 15,30,30);
			}
			if(drawCover){
				gridCover.simpleDraw(540, 35,1000,1000,-Math.atan2(vY, vX) + Math.PI/4);
				called = true;
				endAlpha = 1;
			}
		}
		boolean inRange(){
			return (4 < Math.abs(535 - targetY) - Math.abs(535 - y) || 4 < Math.abs(1054 - targetX) - Math.abs(1054 - x));
		}
		public double getRad(){
			return -Math.atan2(vY, vX) + Math.PI/4;
		}
		public void sendAttack(Peer opponent){
			if(!sentAttack){
				Main.getIMananager().send(true, ByteToObject.addPeerInfo(ByteToObject.objectsToBytes("J8", new ArrayList<Object>(Arrays.asList(new Integer((int)targetX), new Integer((int)targetY)))),opponent));
				sentAttack = true;
			}
		}
	}
	private class ExpManager{
		private double waitTimer = 0;
		private ArrayList<ExpSlot>slots;
		private float fontSize = 30;
		private int y;
		private double turnSwipePos = 0;
		private double turnSwipePosVel = 0;
		private int x = 700;
		private Sprite oTurnSwipe;
		private Sprite eTurnSwipe;
		private Sprite vicSwipe;
		private Sprite defSwipe;
		private ExpBar expBar;
		private float expAlpha = 0;
		private double barY = -200;
		public ExpManager(int y){
			this.y = y;
			slots = new ArrayList<ExpSlot>();
			expBar = new ExpBar();
			eTurnSwipe = new Sprite("eTurn.png");
			oTurnSwipe = new Sprite("ourTurn.png");
			vicSwipe = new Sprite("victoryTurn.png");
			defSwipe = new Sprite("defeatTurn.png");
		}
		public ExpManager(int y, float fontSize){
			slots = new ArrayList<ExpSlot>();
			this.y = y;
			expBar = new ExpBar();
			this.fontSize = fontSize;
			eTurnSwipe = new Sprite("eTurn.png");
			oTurnSwipe = new Sprite("ourTurn.png");
			vicSwipe = new Sprite("victoryTurn.png");
			defSwipe = new Sprite("defeatTurn.png");
		}
		private void createExp(){
			for(int i = 0; i < selectedPoints.size(); i++){
				if(selectedPoints.get(i).isHitSpecial()){
					addSlot("Hit Ship", 50);
				}
			}
			if(gridPlanes.size() > 0)
				addSlot("Proper Cords",50);
			if(victory()){
				addSlot("Victory",500);
				TitleScreen.playWin();
			}
		}
		public void addKillShipExp(int shipID){
			if(shipID == 5){
				addSlot("Destroyed Carrier", 300);
			}else if(shipID == 4){
				addSlot("Destroyed Submarine", 250);
			}else if(shipID == 3){
				addSlot("Destroyed Destroyer", 200);
			}else{
				addSlot("Destroyed Transporter", 150);
			}
		}
		private void addSlot(String msg, int exp){
			slots.add(new ExpSlot(msg,exp,y-(int)((slots.size()+1) * fontSize),y));
		}
		private void draw(){
			drawTurnSwipe();
		}
		private void drawSlots(){
			if(expAlpha < .5f){
				expAlpha += Core.rate/30d;
			}
			int targetOff = 0;
			for(int i = 0; i < slots.size(); i++){
				slots.get(i).draw(x, y-(int)((i) * fontSize) + targetOff);
				if(i == 0 && slots.get(i).done){
					targetOff = (int)fontSize;
				}
				if(i == 0 && slots.get(i).getAlpha() < 0){
					slots.remove(i);
					i--;
				}
			}
		}
		private void drawTurnSwipe(){
			if(turnSwipePos == 0){
				turnSwipePosVel = 30;
				waitTimer = 0;
			}
			if(turnSwipePos > 2200 && turnSwipePos < 2900){
				if(slots.size() > 0){
					if(barY < y + 70){
						barY += Core.rate * 5;
					}
					if(turnSwipePosVel > 0d){
						turnSwipePosVel -=1.2d * Core.rate;
					}else{
						turnSwipePosVel = 0;
						if(expAlpha > 0)
							sr.drawRectangle(true, x - (int)(fontSize/3f), y - (int)(fontSize * 2), sr.getFontWidth("Destroyed Transporter", fontSize) + (int)fontSize, (int)(fontSize * 2.5f),0,0,0,(float)expAlpha);
						drawSlots();
					}
				}else if(waitTimer > 150 && turnSwipePosVel < 5){
					expAlpha -= Core.rate/40d;
					turnSwipePosVel += .5d * Core.rate;
				}else if(turnSwipePosVel > 8){
					expAlpha -= Core.rate/40d;
					turnSwipePosVel -=.8d * Core.rate;
				}else{
					waitTimer += Core.rate;
				}
				expBar.draw(400,(int)barY);
			}else if(turnSwipePosVel < 30){
				if(defeat() || victory()){
					endGame();
				}
				turnSwipePosVel += Core.rate;
				expBar.draw(400,(int)barY);
			}else{
				barY = -200;
			}
			if(waitTimer > 150){
				barY -= Core.rate * 10;
			}
			turnSwipePos += turnSwipePosVel * Core.rate;
			if(turnSwipePos < 6000){
				if(ourTurn){
					if(victory()){
						vicSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}else if(defeat()){
						defSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}else{
						oTurnSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}
				}else{
					if(victory()){
						vicSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}else if(defeat()){
						defSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}else{
						eTurnSwipe.simpleDraw((int)turnSwipePos - 4000, y + 240);
					}
				}
			}
		}
		public void reset(){
			turnSwipePos = 0;
			if(ourTurn){
				createExp();
			}else{
				if(defeat()){
					addSlot("Defeat",250);
					TitleScreen.playLose();
				}
			}
		}
		private class ExpSlot{
			private String msg;
			private int exp;
			private double y;
			private int targetY;
			private boolean done = false;
			private TypeWordEffect expTyped;
			public ExpSlot(String msg, int exp, int y, int targetY){
				this.msg = msg;
				this.exp = exp;
				this.targetY = targetY;
				this.y = y;
				expTyped = new TypeWordEffect(Integer.toString(exp) + " ");
			}
			public float getAlpha(){
				float initialAlpha = ((fontSize* 2f - (float)(targetY - y))/(fontSize*2f));
				if(initialAlpha > 1){
					initialAlpha = 1;
				}
				return initialAlpha -(1f-expTyped.a);
			}
			public void draw(int x, int y2){
				float alpha = getAlpha();
				if(y <= y2){
					y += Core.rate*2;
				}
				if(y >= targetY){
					expTyped.draw(.4d,20,false,false,x + sr.getFontWidth(msg, fontSize) + (int)(fontSize/2f),(int)y,fontSize,1,1,1);
					if(alpha <.9f){
						y += Core.rate/3d;
						if(!done){
							expBar.addToExp(exp);
							done = true;
						}
					}
				}
				if(alpha > 1){
					alpha = 1;
				}
				if(alpha > 0){
					sr.drawText(msg, x, (int)y,fontSize,1,1,1,alpha);
				}
			}
		}
		private boolean defeat(){
			for(Ship s: ourBoard.getShipManager().getShips()){
				for(Point point:s.getPoints()){
					if(!point.isHit()){
						return false;
					}
				}
			}
			return true;
		}
		private boolean victory(){
			int vCount = 0;
			for(Point point: enemyBoard.getPointManager().getPoints()){
				if(point.isHitSpecial()){
					vCount++;
				}
			}
			if(vCount >= 17){
				return true;
			}
			return false;
		}
		private class ExpBar{
			private PlayerProfile ourProfile;
			private Sprite img;
			private double addToExp = 0;
			private int fontSize = 130;
			public ExpBar(){
				img = new Sprite("expBar.png");
			}
			public void draw(int x, int y){
				ourProfile = TitleScreen.getPlayerProfile();
				if(addToExp > 0){
					addToExp -= 2;
					ourProfile.addExp(2);
				}
				double greenPercent = (ourProfile.getExp()%2000)/2000d;
				int proportion = 5;
				img.simpleDraw(x, y);
				sr.drawCenteredText(Integer.toString(ourProfile.getRank()), x + fontSize, y + 15,fontSize,0,0,0,1);
				sr.drawCenteredText(Integer.toString(ourProfile.getRank() + 1) , x +  fontSize * (proportion + 3), y + 15,fontSize,0,0,0,1);
				sr.drawCenteredText(Integer.toString(ourProfile.getExp()%2000) + "/2000", x + (int)(fontSize * 4.6d), y + (int)(fontSize * .79d), (int)(fontSize/3.5d),0,0,0,1);
				sr.drawRectangle(true, x + fontSize * 2 + 15, y + (int)(fontSize/2.8888d), (int)(fontSize * proportion * greenPercent * .93d), (int)(fontSize/2.452d),0,1,0,.6f);
			}
			public void addToExp(int exp){
				addToExp += exp;
			}
		}
	}
	private class RadComp implements Comparator{
		@Override
		public int compare(Object arg0, Object arg1) {
			GridPlane gp = (GridPlane)arg0;
			GridPlane gp1 = (GridPlane)arg1;
			if(gp.getRad() > gp1.getRad()){
				return -1;
			}else if(gp.getRad() < gp1.getRad()){
				return 1;
			}
			return 0;
		}
	}
}
