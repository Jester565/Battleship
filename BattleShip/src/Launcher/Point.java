package Launcher;

import pack.Core;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.Sprite;

public class Point {

	private int x;
	private int y;
	private int mag;
	private double pheta;
	private int rowNum;
	private ShapeRenderer sr;
	private Sprite shipPart1;
	private Sprite shipPart2;
	private Sprite cannotPlace;
	private Sprite takenPoint;
	private Sprite availiblePoint;
	private boolean hit = false;
	private double imageRot;
	private boolean shipOn = false;
	private float explodeAlpha = 0;
	int r;
	int shipR;
	public Point(ShapeRenderer sr, int x, int y, int mag, double pheta, int rowNum){
		this.x = x;
		this.y = y;
		this.mag = mag;
		this.pheta = pheta;
		this.sr = sr;
		this.rowNum = rowNum;
		shipPart1 = new Sprite("shipPart.png");
		shipPart2 = new Sprite("shipPart2.png");
		cannotPlace = new Sprite("noPlaceShipPoint.png");
		availiblePoint = new Sprite("availibleShipPoint.png");
		takenPoint = new Sprite("takenShipPoint.png");
		int radiusCoefficient = (int)(Math.log(mag) * 3 + 4);
		r = radiusCoefficient;
		shipR = radiusCoefficient * 2;
	}
	public boolean isHit(){
		return hit;
	}
	public boolean isHitSpecial(){
		if(shipOn && hit){
			return true;
		}
		return false;
	}
	public void fire(){
		explodeAlpha = 1;
		hit = true;
	}
	public void fire(boolean shipHere){
		explodeAlpha = 1;
		shipOn = shipHere;
		hit = true;
	}
	public boolean equals(int mag, double rads){
		if(this.mag == mag && this.pheta == rads){
			return true;
		}
		return false;
	}
	public void draw(){
		explodeAlpha -= ((float)Core.rate)/80f;
		imageRot += Core.rate;
		sr.drawCircle(true, x, y, r,0,1,0,.4f);
		if(hit && shipOn)
			sr.drawCircle(true, x, y, r,1,0,0,1);
		else if(hit)
			sr.drawCircle(true, x, y, r,0,0,1,1);
		else
			sr.drawCircle(true, x, y, r,0,1,0,1);
		if(explodeAlpha > 0){
			sr.drawCircle(true, x, y, (int)(32*(3f/(explodeAlpha+.2f))),0,0,1,explodeAlpha);
		}
		if(Button.overButton(x - r*2, y - r*2, r*4, r*4)){
			if(hit){
				sr.drawCircle(true, x, y, r + 2,1,0,0,.4f);
			}else{	
				sr.drawCircle(true, x, y, r + 2,.2f,1,.2f,.4f);
			}
		}
	}
	public void drawShipPart(int cenX, int cenY){
		double angle = Math.atan2(-(y-cenY), (x-cenX));
		if(!hit){
			shipPart1.simpleDraw(x - shipR, y-shipR, shipR*2, shipR*2,angle);
		}else{
			shipPart2.simpleDraw(x - shipR, y-shipR, shipR*2, shipR*2,angle);
		}
		if(explodeAlpha > 0){
			sr.drawCircle(true, x, y, (int)(32*(3f/(explodeAlpha+.2f))),1,0,0,explodeAlpha);
		}
	}
	public void drawShipPoint(){
		availiblePoint.simpleDraw(x - r*2, y - r*2, r*4, r*4, imageRot*(Math.PI/180d));
	}
	public void drawNonShipPoint(){
		takenPoint.simpleDraw(x - r*2, y - r*2, r*4, r*4, imageRot*(Math.PI/180d));
	}
	public void drawCannotPlacePoint(){
		cannotPlace.simpleDraw(x - r*2, y - r*2, r*4, r*4, imageRot*(Math.PI/180d));
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getMag(){
		return mag;
	}
	public int getRowNum(){
		return rowNum;
	}
	public boolean equals(int mag, int rowNum){
		if(this.mag == mag && this.rowNum == rowNum){
			return true;
		}
		return false;
	}
	public int distanceFrom(int x2, int y2){
		return (int)(Math.sqrt(Math.pow(x - x2,2) + Math.pow(y - y2,2)));
	}
	public boolean equals(Object obj){
		Point p = (Point)obj;
		if(p.getMag() == mag && p.getRowNum() == rowNum){
			return true;
		}
		return false;
	}
}
