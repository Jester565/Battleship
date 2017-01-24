package Launcher;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;

import JBasics.FileManager;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.Sprite;
import JInternet.ByteToObject;
import pack.Core;
import pack.KeyInput;
import pack.LoadScreen;
import pack.ScreenManager;
import pack.StartUpOptionScreen;

public class Main extends Core{
	int sendTimer = 0;
	public static InternetManager iManager;
	private TitleScreen title;
	private JoinScreen joinScreen;
	private Game game;
	private JLoadScreen jLoad;
	private VolumeControl volC;
	private TutorialManager tm;
	public static boolean badRam = false;
	public static boolean lowPower = false;
	boolean profileSet = false;
	public static void main(String[] args){
		new Main().run();
	}
	public void init(){
		super.init();
		lowPower = (Integer.valueOf(System.getenv("NUMBER_OF_PROCESSORS")) <= 2);
		badRam = (Runtime.getRuntime().maxMemory() <= 1300000000);
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(badRam);
		tm = new TutorialManager();
		iManager = new InternetManager(this);
		title = new TitleScreen(iManager);
		joinScreen = new JoinScreen(iManager);
		game = new Game();
	}
	@Override
	public LoadScreen getLoadScreen(){
		if(jLoad == null)
			jLoad = new JLoadScreen(s);
		return jLoad;
	}
	public void pingTooHighResponse(){
		profileSet = false;
		joinScreen = new JoinScreen(iManager);
		game = new Game();
	}
	public static InternetManager getIMananager(){
		return iManager;
	}
	public void draw(Graphics2D g){
		super.draw(g);
		if(volC == null){
			volC = new VolumeControl();
			Sound.setMasterScale(1d);
		}
		iManager.manage();
		title.draw();
		if(joinScreen.drawBoard){
			game.draw(joinScreen.getOpponent());
			title.setToGameMusic();
		}
		if(game.getEndGame()){
			joinScreen.reset();
			title.setToMenuMusic();
			game = new Game();
		}
		if(iManager.isConnected()){
			if(!profileSet){
				joinScreen.setPlayerProfile(title.getPlayerProfile());
				profileSet = true;
			}
			if(!joinScreen.drawBoard){
				joinScreen.draw();
			}
		}
		volC.draw(1820,980,50,50);
		iManager.drawPacketTracker();
		tm.draw();
	}
	private class JLoadScreen extends LoadScreen{
		private ShapeRenderer sr = new ShapeRenderer();
		private float rectAlpha = 1;
		private Sprite logoWheel;
		private Sprite logo;
		private Sound swoosh;
		private Sound logoSong;
		private double zoomOut = 0;
		private double acceleration = 7;
		private double timer = 0;
		private double circleDegrees = 0;
		public JLoadScreen(ScreenManager s) {
			super(s);
			sr = new ShapeRenderer();
			logoWheel = new Sprite("logoWheel.png");
			logo = new Sprite("jLogo.png");
			logoSong = new Sound(true, "jLogoSong.wav",.8d);
			swoosh = new Sound(true, "swoosh(2).wav",1d);
			logoSong.play();
		}
		@Override
		public void drawLoad(){
			timer += rate;
			rectAlpha -= (float)(rate/150d);
			logoWheel.simpleDraw(500 - (int)(zoomOut/2), 25 - (int)(zoomOut/2), 1000 + (int)zoomOut, 1000+ (int)zoomOut, circleDegrees * (Math.PI/180d));
			logo.simpleDraw(530- (int)(zoomOut/2), 40 - (int)(zoomOut/2),937 + (int)zoomOut,977 + (int)zoomOut);
			if(rectAlpha < 0){
				if(Core.isReady()){
					acceleration += rate;
					swoosh.play();
					zoomOut += rate * acceleration;
					if(zoomOut > 3000){
						logoSong.drainClose();
						sr.drawRectangle(true, 0, 0, 2000, 1100,0,0,0,1);
						readyOn();
					}else{
						sr.drawRectangle(true, 0, 0, 2000, 1100,0,0,0,(float)(zoomOut/3000d));
					}
				}
				circleDegrees += rate/4d;
			}else{
				sr.drawRectangle(true, 0, 0, 2000, 1200,0,0,0,rectAlpha);
			}
		}
	}
}
