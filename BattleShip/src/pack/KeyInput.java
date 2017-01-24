package pack;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
 
public class KeyInput implements KeyListener{
private static ArrayList<String>keysPressed;
private static double timeUntilRemoveKey = 40;
private static boolean keyTypedUpdated = false;
private static double timeKeyTyped = 0;
public static String typedKey = null;
KeyInput(){
	keysPressed = new ArrayList<String>();
	Window w = Core.s.getScreenWindow();
	w.addKeyListener(this);
}
public static synchronized boolean keyPressed(String s)
{
	for(String key:keysPressed)
	{
		if(s.equals(key))
			return true;
	}
	return false;
}
public void keyPressed(KeyEvent e) {
	int keyCode = e.getKeyCode();
	boolean added = false;
	for(String s: keysPressed)
	{
		if(s.equals(KeyEvent.getKeyText(keyCode)))
		{
				added = true;
		}
	}
	if(added == false)
	{
		keysPressed.add(KeyEvent.getKeyText(keyCode));
	}
	timeKeyTyped = System.currentTimeMillis();
	typedKey = KeyEvent.getKeyText(keyCode);
	keyTypedUpdated = true;
	e.consume();
}

public void keyReleased(KeyEvent e) {
	int keyCode = e.getKeyCode();
	for(int i = 0; i < keysPressed.size();i++)
	{
		if(keysPressed.get(i).equals(KeyEvent.getKeyText(keyCode)))
		{
			keysPressed.remove(i);
		}
	}
	e.consume();
}

public void keyTyped(KeyEvent e) {
	
}
public void resetInputMemory()
{
	update();
	keysPressed.clear();
}
public static boolean getKeyTypedUpdated(){
	return keyTypedUpdated;
}
public synchronized void update()
{
	if(timeKeyTyped + timeUntilRemoveKey < System.currentTimeMillis())
		typedKey = null;
	keyTypedUpdated = false;
}
}
