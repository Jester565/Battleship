package pack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

public class ScreenManager {
private GraphicsDevice vc;
private JFrame window;
private DisplayMode displayMode = null;
private int changeWindowTimer = 0;
public static Font f;
public Graphics2D graphicSave;
public ScreenManager(){
	try {
		InputStream is = getClass().getClassLoader().getResourceAsStream("Data/timeburner_regular.ttf");
		InputStream bufferedIn = new BufferedInputStream(is);
		f = Font.createFont(Font.TRUETYPE_FONT, bufferedIn);
		f=f.deriveFont(Font.PLAIN);
	} catch (FontFormatException e2) {
		e2.printStackTrace();
	} catch (IOException e2) {
		e2.printStackTrace();
	}
	//f = new Font("Times New Roman",Font.PLAIN,20);
	GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	e.registerFont(f);
	vc = e.getDefaultScreenDevice();
	setDisplayMode();
}
private void setDisplayMode(){
	final DisplayMode modes1[] = {
		new DisplayMode(1920,1080,32,0),
		new DisplayMode(1920,1080,24,0),
		new DisplayMode(1920,1080,16,0),
		new DisplayMode(1680,1050,32,0),
		new DisplayMode(1680,1050,24,0),
		new DisplayMode(1680,1050,16,0),
		new DisplayMode(1600,900,32,0),
		new DisplayMode(1600,900,24,0),
		new DisplayMode(1600,900,16,0),
		new DisplayMode(1400,900,32,0),
		new DisplayMode(1400,900,24,0),
		new DisplayMode(1400,900,16,0),
		new DisplayMode(1366,768,32,0),
		new DisplayMode(1366,768,24,0),
		new DisplayMode(1366,768,16,0),
		new DisplayMode(1280,800,32,0),
		new DisplayMode(1280,800,24,0),
		new DisplayMode(1280,800,16,0),
		new DisplayMode(1024,768,32,0),
		new DisplayMode(1024,768,24,0),
		new DisplayMode(1024,768,16,0),
		new DisplayMode(800,600,24,0),
		new DisplayMode(800,600,16,0),
		new DisplayMode(640,480,32,0),
		new DisplayMode(640,480,24,0),
		new DisplayMode(640,480,16,0),
	};
	displayMode = findCompatibleMode(modes1);
}
//compares the list of display modes to a list of modes that are compatible with the graphics card and returns the first that match
public DisplayMode findCompatibleMode(DisplayMode[] displayModes)
{
	DisplayMode[] goodModes = vc.getDisplayModes();
	for(DisplayMode mode: displayModes)
	{
		for(DisplayMode goodMode: goodModes)
		{
			if(displayModesMatch(goodMode,mode))
			{
				return mode;
			}
		}
	}
	return null;
}
//compares width,height,bitdepth,and refreshRate for each display mode to see if the modes match
public boolean displayModesMatch(DisplayMode m1, DisplayMode m2)
{
	if(m1.getWidth()!=m2.getWidth()||m1.getHeight()!=m2.getHeight())
	{
		return false;
	}
	if(m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth())
	{
		return false;
	}
	if(m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate())
	{
		return false;
	}
	return true;
}
//gets the display mode of the graphics device
public DisplayMode getDisplayMode()
{
	return vc.getDisplayMode();
}
//creates or changes to a windowed display that can be resized
public void setToWindow()
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	JFrame f;
	if(vc.getFullScreenWindow()!=null)
	{
		f = (JFrame) vc.getFullScreenWindow();
	}
	else
	{
		f = new JFrame();
	}
	restoreScreen();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setLocation(0, 0);
	f.setUndecorated(false);
	f.setIgnoreRepaint(true);
	f.setResizable(true);
	f.setVisible(true);
	f.setSize((int)width,(int)height - (int)(height/30));
	f.createBufferStrategy(2);
	window = f;
}
//changes to a full screen mode by setting the graphics device window and the proper display mode that was found from the method find compatible modes
public void setToFullScreen()
{
	if(displayMode == null)
	{
		setDisplayMode();
	}
	JFrame frame;
	if(window != null)
	{
		frame = window;
		window.dispose();
		window = null;
	}
	else
	{
		frame = new JFrame();
	}
	frame.setLocation(0, 0);
	frame.setUndecorated(true);
	frame.setIgnoreRepaint(true);
	frame.setResizable(false);
	frame.setVisible(true);
	vc.setFullScreenWindow(frame);
	if(displayMode != null && vc.isDisplayChangeSupported())
	{
		try{
			vc.setDisplayMode(displayMode);
		}catch(Exception ex)
		{
			
		}
	}
	frame.createBufferStrategy(2);
}
//gets the graphics2d object from the window's buffer strategy
public Graphics2D getGraphics()
{
	Window w = getScreenWindow();
	if(w != null){
		BufferStrategy s = w.getBufferStrategy();
		try{
			graphicSave = (Graphics2D)s.getDrawGraphics();
		}catch(IllegalStateException ex){
			
		}
		return graphicSave;
	}
	return null;
}
//moves the buffers along to keep the frames going
public void update()
{
	changeWindowTimer--;
	Window w = getScreenWindow();
	//getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	if(w != null)
	{
		BufferStrategy s = w.getBufferStrategy();
		if(!s.contentsLost())
		{
			//makes the next buffer visible
			try{
				s.show();
			}catch(IllegalStateException ex){
				
			}
		}
	}
}
//returns the window that the game is using based on whether or not the game is fullscreen
public Window getScreenWindow()
{
	if(vc.getFullScreenWindow()!=null)
	{
		return vc.getFullScreenWindow();
	}
	else
	{
		return window;
	}
}
//returns true if the game is full screen
public boolean isFullScreen()
{
	if(vc.getFullScreenWindow()!=null)
	{
		return true;
	}
	return false;
}
//gets screen width
public int getWidth()
{
	if(getScreenWindow() !=null)
	{
		return getScreenWindow().getWidth();
	}
	return 0;
}
//gets screen height
public int getHeight()
{
	if(getScreenWindow() !=null)
	{
		return getScreenWindow().getHeight();
	}
	return 0;
}
//disposes of the screens
public void restoreScreen()
{
	Window w = getScreenWindow();
	if (w!=null)
	{
		w.dispose();
	}
	vc.setFullScreenWindow(null);
}
public void setBackGroundColor(float r, float g, float b, float a)
{
	getScreenWindow().setBackground(new Color(r,g,b,a));
}
public void setForeGroundColor(float r, float g, float b, float a)
{
	getScreenWindow().setForeground(new Color(r,g,b,a));
}
public void changedScreenMode()
{
	if(changeWindowTimer < 0 && window != null)
	{
		changeWindowTimer = 20;
		Core.keyListener.resetInputMemory();
		Core.mouseListener.resetInputMemory();
		setToFullScreen();
	}
	else if (changeWindowTimer < 0)
	{
		changeWindowTimer = 20;
		Core.keyListener.resetInputMemory();
		Core.mouseListener.resetInputMemory();
		setToWindow();
	}
}
//not too sure on this one... i never really use it but its there
public BufferedImage createCompatibleImage(int width, int height, int t)
{
	Window w = getScreenWindow();
	if(w != null){
		GraphicsConfiguration gc = w.getGraphicsConfiguration();
		return gc.createCompatibleImage(width,height,t);
	}
	return null;
}
}
