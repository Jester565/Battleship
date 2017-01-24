package Launcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import pack.Core;
import pack.MouseInput;
import JBasics.Button;
import JBasics.FileManager;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.SoundParent;
import JBasics.Sprite;
import JBasics.StreamSound;
import JBasics.TextField;

public class TitleScreen {

	private ShapeRenderer sr;
	private Sprite title;
	private Sprite title2;
	private Sprite background;
	private double mapLocTimer = 0;
	private double mX = 0;
	private double mY = 0;
	private double mW = 6000;
	private double mH= 6000;
	private double mVX = 1;
	private double mVY = 1;
	private double staticTimer = 0;
	private Sound staticNoise;
	private SoundParent radioChatter;
	private Sound beep;
	private static SoundParent rangerTheme;
	private Sprite static1;
	private Sprite static2;
	private Sprite profile1;
	private Sprite profile2;
	private ArrayList<Sprite> connectImgs;
	private ArrayList<Line> lines;
	private int stage = 0;
	private double removeProfileTimer = 0;
	private double connectTimer = 0;
	private int connectStage = 0;
	private double connectStageTimer = 0;
	private TextField firstName;
	private double saveTimer = 0;
	private TextField lastName;
	private ArrayList<PlayerProfile> playerProfiles;
	private Profiles profileSaver;
	private static PlayerProfile selectedProfile;
	private InternetManager im;
	public TitleScreen(InternetManager im){
		this.im = im;
		beep = new Sound("inputPressed.wav",.76d);
		sr = new ShapeRenderer();
		lines = new ArrayList<Line>();
		title = new Sprite("titlefront.png");
		title2 = new Sprite("titlefront2.png");
		background = new Sprite("systemBackground.png");
		static1 = new Sprite("static1.png");
		static2 = new Sprite("static2.png");
		staticNoise = new Sound("static.wav",.6d);
		if(Main.badRam){
			radioChatter = new StreamSound("radioChatter.wav",.73d);
		}else{
			radioChatter = new Sound("radioChatter.wav",.73d);
		}
		radioChatter.play();
		radioChatter.setLoop(true);
		if(Main.badRam){
			rangerTheme = new StreamSound("rangerTheme.wav", .76d);
		}else{
			rangerTheme = new Sound("rangerTheme.wav", .76d);
		}
		rangerTheme.play();
		rangerTheme.setLoop(true);
		profile1 = new Sprite("profile1.png");
		profile2 = new Sprite("profile2.png");
		firstName = new TextField(true,724,737,578,27);
		lastName = new TextField(true,724,686,578,27);
		connectImgs = new ArrayList<Sprite>();
		connectImgs.add(new Sprite("scan0.png"));
		connectImgs.add(new Sprite("scan1.png"));
		connectImgs.add(new Sprite("scan2.png"));
		connectImgs.add(new Sprite("scan3.png"));
		profileSaver = new Profiles();
		TutorialManager.setTutorialPage(20, 400, "Title Screen / Basics", new ArrayList<String>(Arrays.asList("Playing: To play the game hit play.", 
				"Window Mode/Exit: To exit the game press Escape. To change the screen from windowed to fullscreen, or vice versa, press Left Control","Volume: To change the volume click the speaker"
				+ " icon in the upper right corner and adjust the slider to the prefered volume.  Keep in mind the audio will take about 1 second to reach the volume of the slider. When finished, click the icon"
				+ " again.", "Tutorial Access: To get to the tutorial at any time, click the ? mark button in the top right corner.")));
	}
	public void reset(){
		selectedProfile = null;
	}
	public void draw(){
		manageMap();
		if(!im.isConnected()){
			if(stage <= 0){
				drawTitle();
			}else if(stage <= 1){
				drawProfile();
			}else if(stage <= 2){
				im.setUserName(selectedProfile.getName());
				drawConnect();
			}
		}
		scheduleSaveManager();
	}
	public static PlayerProfile getPlayerProfile(){
		return selectedProfile;
	}
	private void drawConnect(){
		if(connectTimer == 0){
			im.sendBroadcastLan(im.getServerNamePack());
		}
		connectTimer += Core.rate;
		connectStageTimer += Core.rate;
		if(connectStageTimer > 30){
			connectStageTimer = 0;
			connectStage++;
			if(connectStage >= connectImgs.size()){
				connectStage = 0;
			}
		}
		if(connectTimer < 200){
			connectImgs.get(connectStage).simpleDraw(0, 0, 1980, 1080);
		}else{
			im.drawServerList();
		}
	}
	private void drawTitle(){
		if(!Button.overButton(640, 585, 690, 155)){
			title.simpleDraw(0,0,1980,1080);
		}else{
			title2.simpleDraw(0,0,1980,1080);
			if(MouseInput.left()){
				TutorialManager.setTutorialPage(20, 400, "Profile Screen", new ArrayList<String>(Arrays.asList("Creating a Profile: Enter in your first name and last name in the textfield."
						+ " Click begin when you are finished. You will not be allowed to continue until you have entered in a first and last name or selected a profile (see next page)."
						+ " Your Profile will be saved and keep track of your rank which is based on how much you play and your skill.  Your profile is also how other people find you.",
						"Loading a Profile: In order to load a profile, you must have created one earlier.  The name and rank of your profile(s) will appear below the text \"Load Profile\". To "
						+ "load it just click the button with the profile's information inside. To delete a profile, click the red button next to the profile's information.")));
				beep.play();
				stage++;
			}
		}
	}
	private void drawProfile(){
		removeProfileTimer -= Core.rate;
		if(!Button.overButton(975, 620, 150, 40)){
			profile1.simpleDraw(0,0,1980,1080);
		}else{
			profile2.simpleDraw(0,0,1980,1080);
			if(MouseInput.left()){
				firstName.setFinalMessage();
				lastName.setFinalMessage();
				if(firstName.finalMessage.length() > 0 && lastName.finalMessage.length()> 0){
					selectedProfile = new PlayerProfile(new String(firstName.finalMessage + " " + lastName.finalMessage));
					playerProfiles.add(selectedProfile);
					beep.stopAndReset();
					TutorialManager.setTutorialPage(20, 400, "Server Screen", new ArrayList<String>(Arrays.asList("Joining a Server: To join a server click the button with the server's name that you "
							+ "want to join. If the server did not appear upon entering the screen, press refresh.", "Public Network Connections: In order to connect to a host you must enter in the ip in "
							+ "the textfield below the server list. When entering the IP it should be in this format \"127.232.862.14\".  It is recommended that the client and host connect "
							+ "with Hamachi.", "Trouble Shooting: If a server is not appearing or you are freezing when trying to join a server and are forced to the menu screen, you most "
							+ "most likely have to open your windows firewall. Click the windows button on your desktop (bottom left). Next click Control Panel. Now go to System and Security."
							+ " Click Windows Firewall. Now click \"Turn Windows Firewall On or Off\". Now turn off the firewall of the appropiate network type.  Try the "
							+ " connection once again.  REMEMBER TO TURN YOUR FIREWALL BACK ON. Windows will allow you to turn it on in the bottom right corner by clicking the flag symbol.")));
					beep.play();
					stage++;
				}
			}
		}
		firstName.draw(true);
		lastName.draw(true);
		int pHeight = 485;
		if(playerProfiles.size() > 0){
			for(int i = 0; i < playerProfiles.size();i++){
				PlayerProfile p = playerProfiles.get(i);
				if(Button.hitDrawnButton(sr, "Name: " + p.getName() + "     Rank: " + Integer.toString(p.getExp()/2000), 609, pHeight, 800, 44, .8f, .8f, .8f, .5f)){
					selectedProfile = p;
					beep.stopAndReset();
					beep.play();
					stage++;
					TutorialManager.setTutorialPage(20, 400, "Server Screen", new ArrayList<String>(Arrays.asList("Joining a Server: To join a server click the button with the server's name that you "
							+ "want to join. If the server did not appear upon entering the screen, press refresh.", "Public Network Connections: In order to connect to a host you must enter in the ip in "
							+ "the textfield below the server list. When entering the IP it should be in this format \"127.232.862.14\".  It is recommended that the client and host connect "
							+ "with Hamachi.", "Trouble Shooting: If a server is not appearing or you are freezing when trying to join a server and are forced to the menu screen, you most "
							+ "most likely have to open your windows firewall. Click the windows button on your desktop (bottom left). Next click Control Panel. Now go to System and Security."
							+ " Click Windows Firewall. Now click \"Turn Windows Firewall On or Off\". Now turn off the firewall of the appropiate network type.  Try the "
							+ " connection once again.  REMEMBER TO TURN YOUR FIREWALL BACK ON. Windows will allow you to turn it on in the bottom right corner by clicking the flag symbol.")));
				}
				if(Button.hitDrawnButton(sr, " ", 1409, pHeight, 20, 44, 1, .2f, .2f, .6f) && removeProfileTimer < 0){
					playerProfiles.remove(i);
					removeProfileTimer = 30;
					i--;
				}
				pHeight -= 50;
			}
		}else{
			sr.drawText("No Profiles", 870, 370,70,1,1,1,1);
		}
	}
	public void setToGameMusic(){
		rangerTheme.pause();
		staticNoise.setVolume(.48d);
		radioChatter.setVolume(.48d);
	}
	public static void playWin(){
		rangerTheme.play();
		//rangerTheme.setTime(146);
		rangerTheme.setVolume(.79d);
	}
	public static void playLose(){
		rangerTheme.play();
		//rangerTheme.setTime(184);
		rangerTheme.setVolume(.79d);
	}
	public void scheduleSaveManager(){
		saveTimer += Core.timePassed;
		if(saveTimer > 15000){
			profileSaver.saveObjectsOverride(new ArrayList<Object>(Arrays.asList(playerProfiles)));
			saveTimer = 0;
		}
	}
	public void setToMenuMusic(){
		rangerTheme.stopAndReset();
		rangerTheme.play();
		staticNoise.setVolume(.65d);
		radioChatter.setVolume(.65d);
	}
	private void manageMap(){
		staticTimer -= Core.rate;
		mapLocTimer -= Core.rate;
		background.simpleDraw((int)mX, (int)mY, (int)mW, (int)mH);
		mX += mVX * Core.rate;
		mY += mVY * Core.rate;
		if(mX > 0 || mY > 0 || mX + mW < 1980 || mY + mH < 1080 || mapLocTimer < 0){
			changeZoom();
		}
		lineManager();
		manageStatic();
		sr.drawRectangle(true, 0, 0, 1980, 1080,0,0,0,.7f);
	}
	private void manageStatic(){
		if(staticTimer > 0){
			if(Math.random() < .5d){
				static1.simpleDraw(0, 0, (int)(Math.random() * 500 + 1980), (int)(Math.random() * 500 + 1100));
			}else{
				static2.simpleDraw(0, 0, (int)(Math.random() * 500 + 1980), (int)(Math.random() * 500 + 1100));
			}
		}else{
			if(staticNoise.playing()){
				staticNoise.stopAndReset();
			}
		}
	}
	private void changeZoom(){
		double mS = Math.random() * 2 + 1.5d;
		mW = mS * 1980;
		mH = mS * 1080;
		mX = Math.random() * -mW/2 - mW/4;
		mY = Math.random() * -mH/2 - mH/4;
		mVX = Math.random() * 5-2.5d;
		mVY = Math.random() * 5-2.5d;
		mapLocTimer = Math.random() * 300d + 300d;
		lines.clear();
		staticTimer = Math.random() * 30 + 4;
		staticNoise.play();
	}
	private void lineManager(){
		if(Math.random()/Core.rate < .04d){
			lines.add(new Line());
		}
		for(Line l: lines){
			l.draw(mVX * Core.rate, mVY * Core.rate);
		}
	}
	private class Line{
		private double x;
		private double y;
		private double x2;
		private double y2;
		private double vX;
		private double vY;
		private float alpha = 1;
		private double stopTimer = 0;
		public Line(){
			x = Math.random() * 1980;
			y = Math.random() * 1080;
			x2 = x;
			y2 = y;
			vX = Math.random() * 5d - 2.5d;
			vY = Math.random() * 5d - 2.5d;
			stopTimer = Math.random()*200d + 100;
		}
		public void draw(double bVX, double bVY){
			stopTimer -= Core.rate;
			x += bVX;
			y += bVY;
			x2 += bVX;
			y2 += bVY;
			if(stopTimer >= 0){
				x2 += vX * Core.rate;
				y2 += vY * Core.rate;
			}else{
				alpha -= (float)(Core.rate * .01d);
				if(alpha > 0)
					sr.drawCircle(false, (int)x2, (int)y2, (int)Math.abs(stopTimer),0,1,0,alpha);
			}
			if(alpha > 0)
				sr.drawLine((int)x, (int)y, (int)x2, (int)y2,2,.1f,1f,.1f,alpha);
		}
	}
	private class Profiles extends FileManager{
		public Profiles(){
			super("profiles.sav");
		}

		protected void createObjects(ObjectOutputStream oos) throws IOException {
			oos.writeObject(new ArrayList<PlayerProfile>());
		}
		protected void loadObjects(ObjectInputStream ois) throws ClassNotFoundException, IOException {
			playerProfiles = (ArrayList<PlayerProfile>)ois.readObject();
		}
	}
}
