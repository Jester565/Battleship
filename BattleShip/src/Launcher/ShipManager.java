package Launcher;

import java.util.ArrayList;

import pack.Core;
import pack.MouseInput;
import JBasics.Button;
import JBasics.ButtonObj;
import JBasics.ShapeRenderer;
import JBasics.Sprite;

public class ShipManager {

	private int x;
	private int y;
	private int w;
	private int h;
	private ArrayList<Ship> ships;
	private ShapeRenderer sr;
	private boolean rotated = false;
	private double rotTimer = 0;
	private boolean recallOn;
	private Sprite shipBack;
	private ButtonObj readyButton;
	private PointManager pm;
	private ButtonObj recall;
	private float readyAlpha = 0;
	private boolean ready = false;
	public ShipManager(PointManager pm, ShapeRenderer sr, int x, int y, int w, int h){
		this.pm = pm;
		this.sr = sr;
		this.x= x;
		this.y = y;
		this.w= w;
		this.h = h;
		ships = new ArrayList<Ship>();
		recall = new ButtonObj();
		readyButton = new ButtonObj();
		shipBack = new Sprite("shipBack.png");
		createShips();
	}
	public ArrayList<Ship> getShips(){
		return ships;
	}
	public void setRecall(boolean on){
		recall.setPressed(on);
		recallOn = false;
	}
	public boolean getRecall(){
		return recallOn;
	}
	private void createShips(){
		ships.add(new Ship(sr, "Carrier.png","Carrier",5));
		ships.add(new Ship(sr, "Submarine.png", "Submarine",4));
		ships.add(new Ship(sr, "Destroyer.png", "Destroyer",3));
		ships.add(new Ship(sr, "Destroyer2.png", "Destroyer",3));
		ships.add(new Ship(sr, "Transporter.png", "Transporter",2));
	}
	public void draw(){
		for(Ship s: ships){
			s.draw(pm.getX(), pm.getY());
		}
	}
	public void drawShipList(){
		rotTimer-=Core.rate;
		int y2 = y + h/17;
		int rate = (w)/ships.size();
		if(MouseInput.right() && rotTimer < 0){
			rotated = !rotated;
			rotTimer = 20;
		}
		shipBack.simpleDraw(x,y,w,h);
		for(Ship s: ships){
			if(!s.placed()){
				s.drawButton(this,pm, x + w/20, y2, (w*9)/10, rate,rotated);
				y2+=rate;
			}
		}
		if(!ready)
			recallOn = recall.hitButtonTimed(sr, "Recall", x + w/20, y2, (w*9)/10, rate, .7f, .5f, .5f, .6f);
		if(recallOn){
			if(Button.inCircle(MouseInput.getX(), MouseInput.getY(), pm.getX(), pm.getY(), pm.getRadius())){
				Point p = pm.getNearestPoint(MouseInput.getX(),MouseInput.getY());
				Ship s = getShip(p);
				if(s!=null)
					s.manageDelete();
			}
		}
		if(shipsReady() && readyAlpha < .8f){
			readyAlpha += Core.rate/60d;
		}else if(readyAlpha > 0){
			readyAlpha -= Core.rate/60d;
		}
		if(!ready && readyAlpha > 0 && readyButton.clickButton(sr, "Ready", x, y - h/5, w, rate, .5f, .5f, .7f, readyAlpha) && shipsReady()||ready && readyAlpha > 0 && readyButton.clickButton(sr, "Ready", x, y - h/5, w, rate, .2f, .2f, .2f, readyAlpha) && shipsReady()){
			ready = !ready;
			recallOn = false;
		}
	}
	public boolean isReady(){
		return ready;
	}
	private boolean shipsReady(){
		for(Ship s: ships){
			if(!s.placed())
				return false;
		}
		return true;
	}
	public boolean inShipList(Point p){
		for(Ship s: ships){
			if(s.inPointList(p))
				return true;
		}
		return false;
	}
	public boolean inShipList(int mag, int rowIndex){
		for(Ship s: ships){
			if(s.inPointList(mag,rowIndex))
				return true;
		}
		return false;
	}
	public Ship getShip(Point p){
		for(Ship s: ships){
			if(s.inPointList(p))
				return s;
		}
		return null;
	}
	public Ship getShip(int mag, int rowIndex){
		for(Ship s: ships){
			if(s.inPointList(mag,rowIndex))
				return s;
		}
		return null;
	}
}
