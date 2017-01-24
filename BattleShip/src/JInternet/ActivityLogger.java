package JInternet;

import java.util.ArrayList;

import JBasics.ShapeRenderer;

public class ActivityLogger {

	private static ArrayList<String>events;
	private ShapeRenderer sr;
	public ActivityLogger(){
		events = new ArrayList<String>();
		sr = new ShapeRenderer();
	}
	public static synchronized void add(String s){
		events.add(s);
	}
	public synchronized void draw(int x, int y, int w, int h){
		sr.drawRectangle(true, x, y, w, h,0,0,0,1);
		int yPos = y + h/20;
		for(String s: events){
			sr.drawText(s, x + w/20, yPos, 14, 1,1,1,1);
		}
	}
}
