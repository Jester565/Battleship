package pack;

import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.JFrame;
import JBasics.AlertManager;
import JBasics.Sound;

public abstract class Core {
	public static boolean running = true;
	public static ScreenManager s;
	public static KeyInput keyListener;
	public static double rate;
	public static double timePassed;
	public static MouseInput mouseListener;
	public static AlertManager alertManager;
	public static StartUpOptionScreen ouop = null;
	private static boolean ready = false;
	public static void stop(){
		running = false;
		System.exit(0);
	}
	public static synchronized boolean isReady(){
		return ready;
	}
	protected StartUpOptionScreen getOuop(){
		return null;
	}
	public void run(){
		try{
			init();
			gameLoop();
		}finally{
			s.restoreScreen();
			crashActions();
			//System.exit(0);
		}
	}
	private void createLoadThread(){
		if(getLoadScreen() != null){
			 Thread loadThread = new Thread(getLoadScreen());
			 loadThread.start();
		}
	}
	public LoadScreen getLoadScreen(){
		return null;
	}
	public void crashActions(){
		
	}
	//set to full screen
	public void init(){
		s = new ScreenManager();
		s.setToWindow();
		Window w = s.getScreenWindow();
		w.setFont(s.f);
		s.getGraphics().setFont(s.f);
		s.setBackGroundColor(.5f,.5f,.5f,1);
		s.setForeGroundColor(1,1,1,1);
		running = true;
		keyListener = new KeyInput();
		mouseListener = new MouseInput();
		alertManager = new AlertManager();
		createLoadThread();
	}
	public void gameLoop(){
		ready = true;
		double startTime = System.currentTimeMillis();
		double cumTime = startTime;
		while(running){
			if(getLoadScreen() == null || getLoadScreen().isReady()){
				timePassed = System.currentTimeMillis() - cumTime;
				rate = timePassed/(16.3333d);
				cumTime += timePassed;
				Graphics2D g = s.getGraphics();
				drawBackGround(g);
				draw(g);
				mouseListener.update();
				keyListener.update();
				alertManager.draw();
				g.dispose();
				s.update();
				if(17 - (int)timePassed > 0){
					try{
						Thread.sleep(17-(int)timePassed);
					}catch(Exception ex){}
				}
			}
		}
	}
	public static synchronized void addAlert(int type, String msg){
		alertManager.addAlert(type, msg);
	}
	public static synchronized void addAlert(int type, double displayTime, String msg){
		alertManager.addAlert(type, displayTime, msg);
	}
	public static Graphics2D getGraphics(){
		return s.getGraphics();
	}
	public void drawBackGround(Graphics2D g)
	{
		g.setColor(s.getScreenWindow().getBackground());
		g.fillRect(0, 0, s.getWidth(), s.getHeight());
		g.setColor(s.getScreenWindow().getForeground());
	}
	//update animation
	public void draw(Graphics2D g){
		if(KeyInput.keyPressed("Escape"))
			stop();
		if(KeyInput.keyPressed("Ctrl"))
			Core.s.changedScreenMode();
		if(KeyInput.keyPressed("F9"))
			System.out.println("MouseX: " + MouseInput.getX()+ "  MouseY: " + MouseInput.getY());
	}
}
