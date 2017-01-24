package Launcher;

import java.util.ArrayList;

import pack.Core;
import pack.MouseInput;
import JBasics.ShapeRenderer;

public class PointManager {

	private int x;
	private int y;
	private int r;
	private double degs = 0;
	private ShapeRenderer sr;
	private ArrayList<Point> points;
	private Board b;
	public PointManager(Board b, ShapeRenderer sr, int x, int y, int r){
		this.sr = sr;
		this.x = x;
		this.y = y;
		this.r = r;
		this.b =b;
		points = new ArrayList<Point>();
		createPoints();
	}
	private void createPoints(){
		addPoints(0,0);
		addPoints(Math.PI/6d,1);
		addPoints(Math.PI/4d,2);
		addPoints(Math.PI/3d,3);
	}
	public ArrayList<Point> getPoints(){
		return points;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getRadius(){
		return r;
	}
	private void addPoints(double pheta,int rowOff){
		int off = 0;
		if(pheta == Math.PI/3){
			off = 2;
		}else if(pheta == Math.PI/6){
			off = -2;
		}
		for(int j = 1; j <= 6; j++){
			int rad = (int)(r/6d * j);
			for(int i = 0; i < 4; i++){
				if(pheta != 0){
					if(i == 0){
						points.add(new Point(sr,(int)(Math.cos(pheta) * rad + x),(int)(Math.sin(pheta) * rad + y),j,pheta,rowOff));
					}else if(i==1){
						points.add(new Point(sr,(int)(Math.cos(-pheta + Math.PI) * rad + x),(int)(Math.sin(-pheta + Math.PI) * rad + y),j,pheta,rowOff + 4 - off));
					}else if(i==2){
						points.add(new Point(sr,(int)(Math.cos(pheta + Math.PI) * rad + x),(int)(Math.sin(pheta + Math.PI) * rad + y),j,pheta,rowOff + 8));
					}else{
						points.add(new Point(sr,(int)(Math.cos(-pheta + 2*Math.PI) * rad + x),(int)(Math.sin(-pheta + 2* Math.PI) * rad + y),j,pheta, rowOff + 12-off));
					}
				}else if(i==0){
					points.add(new Point(sr,(int)(Math.cos(pheta) * rad + x),(int)(Math.sin(pheta) * rad + y),j,pheta,rowOff));
					points.add(new Point(sr,(int)(Math.cos(Math.PI/2) * rad + x),(int)(Math.sin(Math.PI/2) * rad + y),j,pheta,rowOff + 4));
					points.add(new Point(sr,(int)(Math.cos(pheta + Math.PI) * rad + x),(int)(Math.sin(pheta + Math.PI) * rad + y),j,pheta,rowOff + 8));
					points.add(new Point(sr,(int)(Math.cos(pheta + 3*Math.PI/2) * rad + x),(int)(Math.sin(pheta + 3* Math.PI/2) * rad + y),j,pheta, rowOff + 12));
				}
			}
		}
	}
	private void drawPoints(){
		for(Point p:points){
			p.draw();
		}
	}
	public void fire(int mag, double angle){
		for(Point p: points){
			if(p.equals(mag,angle)){
				p.fire();
			}
		}
	}
	public boolean setShipPoints(Ship s, Point cenPoint, boolean vertical){
		int index = 0;
		int mag = cenPoint.getMag();
		int rowIndex = cenPoint.getRowNum();
		ArrayList<Point> shipPoints = new ArrayList<Point>(	);
		while(index < s.getLength()){
			if(vertical){
				Point p = getPoint(mag + index - s.getLength()/2,rowIndex);
				if(p!=null){
					if(!b.getShipManager().inShipList(mag + index - s.getLength()/2,rowIndex)){
						shipPoints.add(p);
						p.drawShipPoint();
					}else{
						p.drawNonShipPoint();
					}
				}
			}else{
				int modRowIndex = rowIndex - s.getLength()/2 + index;
				if(modRowIndex < 0){
					modRowIndex = 15 + modRowIndex + 1;
				}else if(modRowIndex > 15){
					modRowIndex = modRowIndex - 16;
				}
				Point p = getPoint(mag,modRowIndex);
				if(p!=null){
					if(!b.getShipManager().inShipList(mag,modRowIndex)){
						shipPoints.add(p);
						p.drawShipPoint();
					}else{
						p.drawNonShipPoint();
					}
				}
			}
			index++;
		}
		if(MouseInput.left() && shipPoints.size() == s.getLength()){
			s.setPoints(shipPoints);
			return true;
		}else if (shipPoints.size() != s.getLength()){
			for(Point p: shipPoints){
				p.drawCannotPlacePoint();
			}
		}
		return false;
	}
	public Point getPoint(int mag, int rowIndex){
		for(Point p: points){
			if(p.getMag() == mag && p.getRowNum() == rowIndex){
				return p;
			}
		}
		return null;
	}
	public Point getNearestPoint(int x, int y){
		Point minPoint = null;
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < points.size(); i++){
			if(points.get(i).distanceFrom(x, y) < minDistance){
				minPoint = points.get(i);
				minDistance = minPoint.distanceFrom(x, y);
			}
		}
		return minPoint;
	}
	public void drawBackground(){
		sr.drawCircle(true, x, y, r + r/30, 0,0,0,.4f);
		sr.drawCircle(true, x, y, r+r/50, 0,0,0,1);
	}
	public void draw(){
		drawLine();
		drawCircles();
		drawPoints();
	}
	private void drawCircles(){
		double rad = r/6d;
		for(int i = 1; i <= 6; i++){
			sr.drawCircle(false, x, y, (int)(rad * i), 0,1,0,1);
		}
	}
	private void drawLine(){
		if(!Main.lowPower){
			float alpha = .7f;
			degs += .5d * Core.rate;
			double pheta = degs;
			while(alpha > 0){
				sr.drawLine(x, y, (int)(Math.cos(pheta* (Math.PI)/180d) * r) + x, (int)(Math.sin(pheta * (Math.PI)/180d) * r) + y,0,1,0,alpha);
				pheta -= .1d;
				alpha -= .02f;
			}
		}
	}
}
