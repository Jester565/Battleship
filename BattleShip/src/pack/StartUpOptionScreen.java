package pack;

import java.awt.Graphics2D;

public abstract class StartUpOptionScreen {

	public StartUpOptionScreen(){
		
	}
	public abstract boolean finished();
	public abstract void draw(Graphics2D g);
}
