package Launcher;

import java.util.ArrayList;
import java.util.HashMap;

import pack.Core;
import pack.MouseInput;
import JBasics.ActivityLog;
import JBasics.Button;
import JBasics.ButtonObj;
import JBasics.ShapeRenderer;
import JBasics.Sprite;

public class TutorialManager {

	private ShapeRenderer sr;
	private Sprite phone;
	private Sprite phoneRight;
	private Sprite phoneLeft;
	private Sprite phonePower;
	private Sprite help1;
	private Sprite help2;
	private Sprite menu1;
	private Sprite menu2;
	private ButtonObj helpButton;
	private ButtonObj menuButton;
	private static ArrayList<String> titleOrdered;
	private static String title = "Title Screen";
	private static ArrayList<String> messages;
	private static HashMap<String, ArrayList<String>> savedTutorials;
	private static int page = 0;
	private static int x = 0;
	private static int y = 0;
	private double buttonTimer = 0;
	private static ActivityLog al;
	public TutorialManager(){
		sr = new ShapeRenderer();
		menuButton = new ButtonObj();
		titleOrdered = new ArrayList<String>();
		menu1 = new Sprite("menuButton1.png");
		menu2 = new Sprite("menuButton2.png");
		phone = new Sprite("tutorialPhone.png");
		phoneRight = new Sprite("tutorialPhone1.png");
		phoneLeft = new Sprite("tutorialPhone2.png");
		phonePower = new Sprite("tutorialPhone3.png");
		messages = new ArrayList<String>();
		help1 = new Sprite("help.png");
		help2 = new Sprite("help2.png");
		helpButton = new ButtonObj();
		savedTutorials = new HashMap<String, ArrayList<String>>();
		al = new ActivityLog(0, 7);
	}
	public void draw(){
		drawHelpButton(1880, 975, 60, 60);
		
		if(helpButton.isPressed()){
			drawTutorialPhone();
			manageMenu();
		}
	}
	private void drawHelpButton(int x, int y, int w, int h){
		helpButton.hitButtonTimed(sr, " ", x, y, w, h, 0, 0, 0, 0);
		if(Button.overButton(x, y, w, h)){
			help2.simpleDraw(x, y,w,h);
		}else{
			help1.simpleDraw(x, y,w,h);
		}
	}
	private void manageMenu(){
		menuButton.hitButtonTimed(sr, "", x + 24, y + 527,33, 37, 0, 0, 0, 0);
		if(Button.overButton(x + 24, y + 527,33, 37)){
			menu2.simpleDraw(x + 22, y + 527);
		}else{
			menu1.simpleDraw(x + 22, y + 527);
		}
		if(menuButton.isPressed()){
			drawMenu();
		}
	}
	private void drawMenu(){
		int buttonHeight = 60;
		int buttonDistance = 4;
		int yOff = y + 524 - buttonHeight;
		for(int i = 0; i < titleOrdered.size(); i++){
			for(String s:savedTutorials.keySet()){
				if(s.equals(titleOrdered.get(i)) && Button.hitDrawnButton(sr, s, x + 25, yOff, 365, buttonHeight, .2f, .5f, .2f, .8f)){
					setTutorialPage(s,savedTutorials.get(s));
					menuButton.setPressed(false);
				}
			}
			yOff -= (buttonHeight + buttonDistance);
		}	
	}
	private void drawTutorialPhone(){
		buttonTimer -= Core.rate;
		if(Button.overButton(x + 334, y + 7, 72, 55) && page < messages.size() - 1 && !menuButton.isPressed()){
			phoneRight.simpleDraw(x, y);
			if(MouseInput.left() && buttonTimer < 0){
				page ++;
				changePage();
				buttonTimer = 30;
			}
		}else if(Button.overButton(x + 11, y + 7, 72, 55) && page > 0 && !menuButton.isPressed()){
			phoneLeft.simpleDraw(x, y);
			if(MouseInput.left() && buttonTimer < 0){
				page --;
				changePage();
				buttonTimer = 30;
			}
		}else if(Button.overButton(x + 146, y + 8, 125, 55)){
			phonePower.simpleDraw(x, y);
			if(MouseInput.left()){
				helpButton.setPressed(false);
			}
		}else{
			phone.simpleDraw(x, y);
		}
		sr.drawCenteredText(title, x + 209, y + 590,35,0,1,0,1);
		if(!menuButton.isPressed()){
			drawPhoneStrings(20);
		}
	}
	private static void changePage(){
		al.clearMessages();
		if(messages.get(page) == null){
			al.addToLog("The Instructor Did Not Feel Instruction Was Necessary Here");
		}else{
			al.addToLog(messages.get(page));
		}
	}
	private void drawPhoneStrings(float fontSize){
		sr.drawCenteredText("Page: " + Integer.toString(page + 1) + "/" + Integer.toString(messages.size()), x + 209, y + 535, fontSize, 1, 1, 1, 1);
		al.displayStrings(x + 20, y + 57, 363, 450, fontSize);
	}
	public static void setTutorialPage(int x2, int y2, String title2, ArrayList<String> pages){
		title = title2;
		messages = pages;
		page = 0;
		x = x2;
		y = y2;
		changePage();
		if(!titleInList(title2)){
			savedTutorials.put(title2, pages);
			titleOrdered.add(title2);
		}
	}
	public static void setTutorialPage(String title2, ArrayList<String> pages){
		title = title2;
		messages = pages;
		page = 0;
		changePage();
		savedTutorials.put(title2, pages);
	}
	public static void clearTutorialPage(){
		title = "N/A";
		al.clearMessages();
		messages.clear();
	}
	private static boolean titleInList(String title){
		for(String s: titleOrdered){
			if(title.equals(s)){
				return true;
			}
		}
		return false;
	}
}
