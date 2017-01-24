package Launcher;

import JBasics.Button;
import JBasics.ShapeRenderer;

public class Board {

	private ShapeRenderer sr;
	private PointManager pm;
	private ShipManager sm;
	private boolean shipList = true;
	public Board(boolean shipListOn, int x, int y, int w, int h){
		this.shipList = shipListOn;
		sr = new ShapeRenderer();
		pm = new PointManager(this,sr,x + w/2, y + h/2, w/2);
		sm = new ShipManager(pm,sr, (int)(x + w*1.06d), y + h/3, w/3, h/2);
	}
	public PointManager getPointManager(){
		return pm;
	}
	public void draw(){
		pm.drawBackground();
		sm.draw();
		if(shipList){
			sm.drawShipList();
		}
		pm.draw();
	}
	public Point getPoint(int x, int y){
		if(Button.inCircle(x, y, pm.getX(), pm.getY(), pm.getRadius())){
			return pm.getNearestPoint(x, y);
		}
		return null;
	}
	public void setShipList(boolean mode){
		shipList = mode;
	}
	public boolean shipsReady(){
		return sm.isReady();
	}
	public ShipManager getShipManager(){
		return sm;
	}
}
