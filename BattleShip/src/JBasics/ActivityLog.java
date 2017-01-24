package JBasics;

import java.util.ArrayList;


public class ActivityLog {

	private ShapeRenderer sr;
	private ArrayList<Message> messages;
	private int messageHeight = 20;
	private int spacingHeight = 0;
	public ActivityLog()
	{
		messages = new ArrayList<Message>();
		sr = new ShapeRenderer();
	}
	public ActivityLog(int startingHeight){
		messages = new ArrayList<Message>();
		sr = new ShapeRenderer();
		this.messageHeight = startingHeight;
	}
	public ActivityLog(int startingHeight, int spacingHeight){
		messages = new ArrayList<Message>();
		sr = new ShapeRenderer();
		this.messageHeight = startingHeight;
		this.spacingHeight = spacingHeight;
	}
	public void addToLog(String s){
		messages.add(new Message(s));
	}
	public void displayStrings(int x, int y, int w, int h, float fontSize){
		int initialY = y;
		for(int i = messages.size() - 1; i >= 0; i--){
			if(y < initialY + h){
				y += messages.get(i).draw(sr, x + 5, y + (int)(fontSize * 1.2f) + 4, w, messageHeight, (int)fontSize,1,1,1,1,false,spacingHeight);
			}else{
				messages.remove(i);
				i--;
			}
		}
	}
	public void clearMessages(){
		messages.clear();
	}
}
