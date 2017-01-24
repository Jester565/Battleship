package Launcher;

import java.util.ArrayList;

import pack.Core;
import pack.MouseInput;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.Sprite;

public class Ship {

	private ArrayList<Point> points;
	private Sprite shipImg;
	private ShapeRenderer sr;
	private boolean selected = false;
	private double selectTimer = 20;
	private int length;
	private double lX = 0;
	private boolean lXRight = true;
	public Ship(ShapeRenderer sr, ArrayList<Point> points,String file, String name, int length){
		this.points = points;
		this.sr= sr;
		shipImg = new Sprite(file);
		this.length = length;
	}
	public ArrayList<Point> getPoints(){
		return points;
	}
	public Ship(ShapeRenderer sr,String file, String name, int length){
		this.sr= sr;
		shipImg = new Sprite(file);
		this.length = length;
	}
	public boolean placed(){
		return (points != null);
	}
	public int getLength(){
		return length;
	}
	public void setPoints(ArrayList<Point> points){
		this.points = points;
	}
	public void draw(int cenX, int cenY){
		if(points!=null){
			drawPoints(cenX, cenY);
		}
	}
	public void manageDelete(){
		for(Point p: points){
			p.drawCannotPlacePoint();
		}
		if(MouseInput.left()){
			points = null;
		}
	}
	public void drawButton(ShipManager sm, PointManager pm, int x, int y, int w, int h, boolean rot){
		selectTimer -= Core.rate;
		int lW = 7;
		if(lXRight){
			lX += Core.rate;
			if(lX > w - lW){
				lXRight = false;
			}
		}else{
			lX -= Core.rate;
			if(lX < 0){
				lXRight = true;
			}
		}
		if(Button.hitDrawnButton(sr, " ", x, y, w, h, 0, 0, 0, .2f) && !selected){
			Game.inputClick.stopAndReset();
			Game.inputClick.play();
			selected = true;
			sm.setRecall(false);
			selectTimer = 20;
		}
		if(selected){
			sr.drawRectangle(true, x, y, w, h,.7f,.9f,.7f,.3f);
			sr.drawRectangle(true, x + (int)lX, y, lW, h,0,.3f,0,.4f);
			sr.drawRectangle(true, x - (int)lX + w, y, lW, h,0,0,.3f,.4f);
			placeShip(pm,rot);
			if(!Button.inCircle(MouseInput.getX(), MouseInput.getY(), pm.getX(), pm.getY(), pm.getRadius())&&selectTimer < 0 && MouseInput.left()){
				Game.inputClick.stopAndReset();
				Game.inputClick.play();
				selected = false;
			}
		}
		shipImg.simpleDraw(x, y, w, h);
	}
	public boolean destroyed(){
		for(Point p: points){
			if(!p.isHit()){
				return false;
			}
		}
		return true;
	}
	private void placeShip(PointManager pm, boolean rot){
		if(Button.inCircle(MouseInput.getX(), MouseInput.getY(), pm.getX(), pm.getY(), pm.getRadius()) && pm.setShipPoints(this, pm.getNearestPoint(MouseInput.getX(), MouseInput.getY()), rot)){
			Game.inputClick.stopAndReset();
			Game.inputClick.play();
			selected = false;
		}
	}
	private void drawPoints(int cenX, int cenY){
		for(Point p: points){
			p.drawShipPart(cenX, cenY);
		}
		for(int i = 0; i < points.size()-1; i++){
			sr.drawLine(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY(),10,0,0,1,1);
			sr.drawLine(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY(),8,0,0,0,1);
		}
	}
	public boolean inPointList(Point p){
		if(points != null){
			for(Point p2: points){
				if(p.equals(p2)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean inPointList(int mag, int rowIndex){
		if(points != null){
			for(Point p2: points){
				if(p2.equals(mag,rowIndex)){
					return true;
				}
			}
		}
		return false;
	}
}
