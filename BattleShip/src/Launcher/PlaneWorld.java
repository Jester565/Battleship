package Launcher;

import java.util.ArrayList;

import pack.Core;
import JBasics.ShapeRenderer;
import JBasics.Sprite;

public class PlaneWorld{
	private int x;
	private int y;
	private int w;
	private int h;
	private double wX;
	private double wY;
	private double vX = .9d;
	private double vY = 0;
	private double gridSpace = 20;
	private double gridMovement = 0;
	private double vGridSpace = 20;
	private double vGridMovement = 0;
	public static final int SURROUND_DISTANCE = 60;
	private Sprite airPort;
	private Sprite plane;
	private Sprite passForward;
	private ShapeRenderer sr;
	private double bombVY = 0;
	private boolean zoomOut = false;
	private int startDis = 0;
	private double bombOffX = 0;
	private int planeNumber = 0;
	private double bombOffY = 0;
	private boolean soundTriggered = false;
	private double bombFlashTimer = 0;
	public PlaneWorld(ShapeRenderer sr, int planeNumber, int x, int y, int w, int h){
		this.sr = sr;
		this.x = x;
		this.w = w;
		this.y = y;
		this.h = h;
		this.planeNumber = (int) planeNumber;
		passForward = new Sprite("passFoward.png");
		startDis = (int) (planeNumber * w/2d);
		wX = -startDis;
		airPort = new Sprite("airPort.png");
		plane = new Sprite("plane.png");
	}
	private double getWX(){
		return wX;
	}
	private double getWY(){
		return wY;
	}
	private double getPlaneAngle(){
		return Math.atan2(-vY*2, vX);
	}
	private void drawBack(){
		sr.drawRectangle(true, x, y, w, h,0,0,0,.9f);
		gridMovement -= vX * Core.rate;
		vGridMovement -= vY * Core.rate;
		if(Math.abs(gridMovement) > gridSpace){
			gridMovement = 0;
		}
		for(int i = -1; i < w/gridSpace + 2;i++){
			sr.drawRectangle(true, x + (int)gridMovement + (int)gridSpace *i, y,2, h,0,1,0,.4f);
		}
		if(Math.abs(vGridMovement) > vGridSpace){
			vGridMovement = 0;
		}
		for(int i = -1; i < h/vGridSpace + 2;i++){
			sr.drawRectangle(true, x, y + (int)vGridMovement + (int)vGridSpace *i,w, 2,0,1,0,.4f);
		}
	}
	private void drawFront(){
		float r = 0f;
		float g = 0f;
		float b = 0f;
		float a = 1f;
		sr.drawRectangle(true, x - SURROUND_DISTANCE * 3, y, SURROUND_DISTANCE * 3, h, r, b, b, a);
		sr.drawRectangle(true, x + w, y, SURROUND_DISTANCE * 3, h, r, g, b, a);
		sr.drawRectangle(true, x - SURROUND_DISTANCE * 3, y + h, w + SURROUND_DISTANCE * 6, SURROUND_DISTANCE, r, g, b, a);
		sr.drawRectangle(true, x - SURROUND_DISTANCE * 3, y - SURROUND_DISTANCE, w + SURROUND_DISTANCE * 6, SURROUND_DISTANCE, r, g, b, a);
		if(!zoomOut)
			plane.simpleDraw(x + w/2, y + h/2,w/6,(h/4),getPlaneAngle());
		else
			sr.drawCircle(true, x + w/2, y + h/2, 5,0,0,1,1);
		if(vY != 0){
			passForward.simpleDraw(x , y + h);
		}
	}
	public void skip(){
		if(planeNumber == 0){
			wX = 2743.85;
			wY = 77.3183618;
			vX = 9;
			vY = 0;
		}else if(planeNumber == 1){
			wX = 864.1519472488727; 
			wY = 8.287363851762967;
			vX = 9.149999999999997; 
			vY = 0.6560000000000005;
		}else if(planeNumber == 2){
			wX = -33.793171006473074;
			wY = 0.0;
			vX = 0.9;
			vY = 0.0;
		}else if(planeNumber == 3){
			 wX = -223.79317100647884;
			 wY = 0.0;
			 vX = 0.9;
			 vY = 0.0;
		}
	}
	public boolean bombDropped(){
		return (vY == 0 && wX > w * 3);
	}
	public boolean bombDone(){
		return (zoomOut && h/2 + bombOffY <= 0 && wX > w* 3);
	}
	private void drawBomb(){
		if(vY == 0 && wX > w * 3){
			bombFlashTimer += Core.rate;
			bombOffX -= Core.rate * vX/15d;
			if(!zoomOut){
				bombVY += .02d * Core.rate;
			}else{
				bombVY += .001d * Core.rate;
			}
			bombOffY -= Core.rate * bombVY;
			if(((int)bombFlashTimer/4)%2 ==0 && h/2 + bombOffY > 0){
				if(!zoomOut)
					sr.drawCircle(true, x + w/2 + (int)bombOffX + 6, y + h/2 + (int)bombOffY, 6,1,0,0,1);
				else
					sr.drawCircle(true, x + w/2 + (int)bombOffX + 6, y + h/2 + (int)bombOffY, 3,1,0,0,1);
			}
		}
		if(bombOffY < -h/2 && !zoomOut){
			vX /=10;
			vY /=10;
			bombOffY /= 10;
			bombVY /= 5;
			vGridSpace /= 5;
			gridSpace /= 5;
			zoomOut = true;
		}
	}
	private void manageNavs(){
		wX += Core.rate * vX;
		wY += Core.rate * vY;
		if(wX > 0 && wX < w*2.3d){
			if(!soundTriggered){
				soundTriggered = true;
			}
			vX += .03d * Core.rate;
			if(wX > w*1.7d){
				vY +=.016d * Core.rate;
			}
		}else{
			if(vY > 0){
				vY -= .002d * Core.rate;
			}
			if(vY < 0){
				vY = 0;
			}
		}
	}
	public void draw(ArrayList<PlaneWorld> planeWorlds){
		manageNavs();
		drawBack();
		if(wX < (w*3*4d)/5d)
			airPort.simpleDraw(x - (int)wX - 20, y - (int)wY + h/4, w * 3, h);
		if(wX < 0){
			sr.drawRectangle(true, x, y + h/2, w, 4,0,.6f,0,1);
		}
		if(!zoomOut){
			drawOtherPlanes(planeWorlds);
		}
		drawBomb();
		drawFront();
		//System.out.println(planeNumber + ": wX = " + wX + " wY = " + wY + "vX = " + vX + " vY = " + vY);
	}
	private void drawOtherPlanes(ArrayList<PlaneWorld> planeWorlds){
		for(PlaneWorld planeWorld : planeWorlds){
			if(!planeWorld.equals(this) && !planeWorld.zoomOut){
				double adaptedX = planeWorld.getWX() - wX + w/2;
				double adaptedY = planeWorld.getWY() - wY + h/2;
				if(adaptedX >= -SURROUND_DISTANCE && adaptedX <= w + SURROUND_DISTANCE && adaptedY >= -SURROUND_DISTANCE/2 && adaptedY <= h + SURROUND_DISTANCE/2 ){
					plane.simpleDraw((int)adaptedX + x, (int)adaptedY + y,w/6,(h/4),planeWorld.getPlaneAngle());
				}
			}
		}
	}
	public boolean passForward(){
		return vY != 0;
	}
}