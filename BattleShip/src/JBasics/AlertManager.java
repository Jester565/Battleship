package JBasics;

import java.util.ArrayList;

import pack.Core;

public class AlertManager {

	public static final int MIDDLE_OF_SCREEN = 0;
	public static final int TOP_OF_SCREEN = 1;
	private ArrayList<CenterAlert> centerAlerts;
	private ShapeRenderer sr;
	public AlertManager(){
		centerAlerts = new ArrayList<CenterAlert>();
		sr = new ShapeRenderer();
	}
	public void addAlert(int type, String message){
		if(type == MIDDLE_OF_SCREEN)
			centerAlerts.add(new CenterAlert(message));
	}
	public void addAlert(int type, double framesDisplayed, String message){
		if(type == MIDDLE_OF_SCREEN)
			centerAlerts.add(new CenterAlert(message, framesDisplayed));
	}
	public void draw(){
		if(centerAlerts.size() > 0){
			centerAlerts.get(0).draw(sr, 700, 400, 600, 150);
			if(centerAlerts.get(0).dead)
				centerAlerts.remove(0);
		}
	}
	public class CenterAlert{
		float alpha = .2f;
		boolean on = true;
		boolean dead = false;
		double framesDisplayed = Double.MAX_VALUE;
		String message = "This alert is not a bug...";
		public CenterAlert(String message){
			this.message = message;
		}
		public CenterAlert(String message, double framesDisplayed){
			this.message = message;
			this.framesDisplayed = framesDisplayed;
		}
		public void draw(ShapeRenderer sr, int x, int y, int w, int h){
			framesDisplayed -= Core.rate;
			if(on && alpha < .8f)
				alpha += .02f;
			if(!on && alpha > .09f){
				alpha -= .02f;
				if(alpha < .1f){
					dead = true;
				}
			}
			sr.drawRectangle(true, x, y, w, h, .7f, .1f, .1f, alpha);
			sr.drawRectangle(true, x, y, w, h, .5f, .1f, .1f, alpha);
			sr.drawCenteredText(message, x + w/2, y +(h/2), 20,0,0,0,alpha);
			if(framesDisplayed < 0 || Button.hitButton(x, y, w, h)){
				on = false;
			}
		}
	}
}

