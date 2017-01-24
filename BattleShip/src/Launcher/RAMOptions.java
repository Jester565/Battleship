package Launcher;

import java.awt.Graphics2D;

import JBasics.Button;
import JBasics.ShapeRenderer;
import pack.StartUpOptionScreen;

public class RAMOptions extends StartUpOptionScreen{
	
	private ShapeRenderer sr;
	public boolean lowRam = false;
	private boolean done = false;
	public RAMOptions(){
		super();
		sr = new ShapeRenderer();
	}

	@Override
	public boolean finished() {
		return done;
	}

	@Override
	public void draw(Graphics2D g) {
		if(Button.hitDrawnButton(sr, "High Ram (64-Bit OS)", 400, 600, 1200, 300, .7f, .7f, .7f, 1)){
			done = true;
		}else if(Button.hitDrawnButton(sr, "Low Ram (32-Bit OS)", 400, 100, 1200, 300, .7f, .7f, .7f, 1)){
			done = true;
			lowRam = true;
		}
	}
}
