package JBasics;

import pack.Core;

public class ButtonObj{

	double timer = 0;
	boolean pressed = false;
	public ButtonObj(){
		
	}
	public ButtonObj(boolean pressed){
		this.pressed = pressed;
	}
	public boolean hitButtonTimed(ShapeRenderer sr, String s,double x, double y, double w, double h,float r, float g, float b, float a)
	{
		float cOff = 1f;
		timer-=Core.rate;
		if(pressed)
			cOff = 2f;
		if(Button.hitDrawnButton(sr, s, x, y, w, h, r/cOff, g/cOff, b/cOff, a) && timer < 0){
			pressed = !pressed;
			timer = 50;
		}
		return pressed;
	}
	public boolean clickButton(ShapeRenderer sr, String s, double x, double y, double w, double h, float r, float g, float b, float a){
		timer -= Core.rate;
		if(Button.hitDrawnButton(sr, s, x, y, w, h, r, g, b, a) && timer < 0){
			timer = 20;
			return true;
		}
		return false;
	}
	public void setPressed(boolean on){
		pressed = on;
	}
	public boolean isPressed(){
		return pressed;
	}
}
