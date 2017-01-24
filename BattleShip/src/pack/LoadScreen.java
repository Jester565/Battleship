package pack;

import java.awt.Graphics2D;

public abstract class LoadScreen implements Runnable{

	protected boolean ready = false;
	private double optionTimer = 0;
	private double timePassed = 0;
	protected double rate = 0;
	private boolean optionMode = false;
	private ScreenManager s;
	public LoadScreen(ScreenManager s){
		this.s = s;
	}
	public synchronized boolean isReady(){
		return ready;
	}
	@Override
	public void run(){
		double startTime = System.currentTimeMillis();
		double cumTime = startTime;
		while(!ready || !Core.isReady()){
			timePassed = System.currentTimeMillis() - cumTime;
			rate = timePassed/(16.3333d);
			cumTime += timePassed;
			Graphics2D g = s.getGraphics();
			draw();
			Core.mouseListener.update();
			Core.keyListener.update();
			g.dispose();
			s.update();
			try{
				Thread.sleep(17-(int)timePassed);
			}catch(Exception ex){}
		}
			
	}
	public void drawBackGround(Graphics2D g){
		g.setColor(s.getScreenWindow().getBackground());
		g.fillRect(0, 0, s.getWidth(), s.getHeight());
		g.setColor(s.getScreenWindow().getForeground());
	}
	private void draw(){
		if(KeyInput.keyPressed("Escape"))
			Core.stop();
		if(KeyInput.keyPressed("Ctrl"))
			Core.s.changedScreenMode();
		optionTimer -= rate;
		if(optionTimer < 0){
			if(KeyInput.keyPressed(changeModeKey())){
				optionMode = !optionMode;
				optionTimer = 25;
			}
		}
		if(optionMode){
			drawStartUpOptions();
		}else{
			drawLoad();
		}
	}
	public synchronized void readyOn(){
		ready = true;
	}
	public synchronized void drawStartUpOptions() {
		
	}
	public synchronized void drawLoad(){
		
	}
	protected String changeModeKey(){
		return "Space";
	}
	
}
