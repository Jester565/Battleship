package Launcher;

import pack.Core;
import pack.MouseInput;
import JBasics.Button;
import JBasics.ButtonObj;
import JBasics.ShapeRenderer;
import JBasics.Sound;
import JBasics.Sprite;

public class VolumeControl {

	private Sprite volumeIcon;
	private ShapeRenderer sr;
	private ButtonObj volButton;
	private ButtonObj circle;
	private double circScale = 1;
	private double xOff = 0;
	public VolumeControl(){
		volumeIcon = new Sprite("volumeButton.png");
		volButton = new ButtonObj();
		circle = new ButtonObj();
		sr = new ShapeRenderer();
	}
	public void draw(int x, int y, int w, int h){
		boolean allowClick = true;
		if(xOff < w * 4 && volButton.isPressed()){
			xOff += Core.rate*3;
			allowClick = false;
		}else if(!volButton.isPressed() && xOff > 0){
			xOff -= Core.rate*3;
			circle.setPressed(false);
			allowClick = false;
		}
		if(xOff > 0){
			sr.drawRectangle(true, x + w - (int)xOff, y + h/20, (int)xOff, h - h/10,.2f,.2f,.2f,.6f);
			sr.drawRectangle(true, x - (int)(19*xOff/20d) + w, y + h/3, (int)(xOff * .9d), h/3 ,.5f,.5f,.5f,.6f);
			if(circle.isPressed()){
				circle.hitButtonTimed(sr, " ", 0, 0, 2000, 1000, 0, 0, 0, 0);
				sr.drawCircle(true, x - (int)(19*xOff/20d) +  (int)(xOff * .9d * circScale) + w, y + h/2, h/4,0,1,0,1);
				if(MouseInput.getX() < x - (int)(19*xOff/20d) + w){
					circScale = 0;
				}else if(MouseInput.getX() > x - (int)(19*xOff/20d) + (int)(xOff * .9d) + w){
					circScale = 1;
				}else{
					circScale = ((double)MouseInput.getX() - (x - (int)(19*xOff/20d) + w))/((xOff * .9d));
				}
				Sound.setMasterScale(circScale);
			}else{
				if(allowClick){
					circle.hitButtonTimed(sr, " ", x -(int)(19*xOff/20d) + (int)(xOff * .9d * circScale) + w - h/4, y + h/4, h/2, h/2, 0, 0, 0, 0);
				}
				if(Button.overButton(x - (int)(19*xOff/20d) + w + (int)(xOff * .9d * circScale) - h/4, y + h/4, h/2, h/2)){
					sr.drawCircle(true, x - (int)(19*xOff/20d) + w +  (int)(xOff * .9d * circScale), y + (int)(h/2), (int)(h/3),0,0,1,1);
				}else{
					sr.drawCircle(true, x - (int)(19*xOff/20d) + w +  (int)(xOff * .9d * circScale), y + h/2, h/4,0,0,1,1);
				}
			}
		}
		volButton.hitButtonTimed(sr, " ", x - xOff, y, w, h, .3f, .3f, .3f, .2f);
		if(Button.overButton(x - xOff, y, w, h)){
			sr.drawRectangle(true, x - (int)xOff, y, w, h,1,1,1,.4f);
		}
		volumeIcon.simpleDraw(x - (int)(xOff), y, w, h);
	}
}
