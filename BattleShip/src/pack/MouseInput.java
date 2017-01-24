package pack;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseInput implements MouseListener,MouseWheelListener,MouseMotionListener{

	private static MouseWheelEvent wheelEvent;
	private static boolean wheelUpdated;
	private static MouseEvent event;
	private static boolean updated;
	private static boolean leftHeld = false;
	private static double lastMouseClick = 0;
	MouseInput()
	{
		Window w = Core.s.getScreenWindow();
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		w.addMouseWheelListener(this);
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelUpdated = true;
		wheelEvent = e;
	}
	public void mouseClicked(MouseEvent e) {
		e.consume();
		updated = true;
		event = e;
		if(e.getButton() == MouseEvent.BUTTON1){
			lastMouseClick = System.currentTimeMillis();
			leftHeld = true;
		}
		e.consume();
	}
	public void mouseEntered(MouseEvent e) {
		e.consume();
	}
	public void mouseExited(MouseEvent e) {
		e.consume();
	}
	public void mousePressed(MouseEvent e) {
		updated = true;
		event = e;
		e.consume();
	}
	public void mouseReleased(MouseEvent e) {
		updated = true;
		event = e;
		if(e.getButton() == MouseEvent.BUTTON1)
			lastMouseClick = System.currentTimeMillis();
		e.consume();
	}
	public void mouseDragged(MouseEvent e) {
		updated = true;
		event = e;
		e.consume();
	}
	public void mouseMoved(MouseEvent e) {
		updated = true;
		event = e;
		e.consume();
	}
	public static int getX()
	{
		if(event != null)
			return (int)(event.getX()*(1980d/(double)Core.s.getWidth()));
		return 0;
	}
	public static int getY()
	{
		double windowOff = 25;
		if(Core.s.isFullScreen())
			windowOff = 0;
		if(event != null)
			return (int)((Core.s.getHeight()-event.getY())*(1080d/(double)(Core.s.getHeight()-windowOff)));
		return 0;
	}
	public static boolean leftHeld(){
		return leftHeld;
	}
	public static boolean left()
	{
		if(updated && event.getButton() == MouseEvent.BUTTON1 ){
			
			return true;
		}else if (System.currentTimeMillis() - lastMouseClick < 50){
			return true;
		}
		return false;
	}
	public static boolean center()
	{
		if(updated && event.getButton() == MouseEvent.BUTTON2)
			return true;
		return false;
	}
	public static boolean right()
	{
		if(updated && event.getButton() == MouseEvent.BUTTON3)
			return true;
		return false;
	}
	public static boolean wheelUp()
	{
		if(wheelUpdated && wheelEvent.getWheelRotation()==-1)
			return true;
		return false;
	}
	public static boolean wheelDown()
	{
		if(wheelUpdated && wheelEvent.getWheelRotation()==1)
			return true;
		return false;
	}
	public static int wheelRotation()
	{
		if(wheelUpdated)
			return wheelEvent.getWheelRotation();
		return 0;
	}
	public void resetInputMemory()
	{
		update();
		event = null;
		wheelEvent = null;
	}
	public void update()
	{
		wheelUpdated = false;
		updated = false;
	}
}
