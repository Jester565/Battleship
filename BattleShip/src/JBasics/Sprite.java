package JBasics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import pack.Core;

public class Sprite{
	private float x = -700;
	private float y = -700;
	private float originX = 0;
	private float originY = 0;
	private double width = 0;
	private double height = 0;
	private int xFlip = 1;
	private int yFlip = 1;
	private double theta = 0;
	private double xScale = 1;
	private double yScale = 1;
	private BufferedImage img = null;
	private String modifiedPath;
	public Sprite(){
		getFilePath();
	}
	public Sprite(String loc){
		getFilePath();
		setImage(loc);
	}
	private void getFilePath(){
		modifiedPath = System.getProperty("user.dir") + "\\src\\Data\\";
	}
	public int getWidth()
	{
		return img.getWidth();
	}
	public int getHeight()
	{
		return img.getHeight();
	}
	public void setToDefault()
	{
		x = -700;
		y = -700;
		originX = 0;
		originY = 0;
		width = 0;
		height = 0;
		xFlip = 1;
		yFlip = 1;
		theta = 0;
		xScale = 1;
		yScale = 1;
	}
	
	public void setImage(String s){
		InputStream is = getClass().getClassLoader().getResourceAsStream("Data/" + s);
		try {
		    img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y)
	{
		this.x = (float)(x);
		this.y = (float)(y);
	}
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setDegrees(double degrees)
	{
		this.theta = degrees * (Math.PI/180);
	}
	
	public void setRadians(double theta)
	{
		this.theta = theta;
	}
	
	public void setFlip(boolean x, boolean y)
	{
		if(x)
			xFlip = -1;
		else
			xFlip = 1;
		if(y)
			yFlip = -1;
		else
			yFlip = 1;
	}
	
	public void setOrigin(double originX, double originY)
	{
		this.originX = (float) originX;
		this.originY = (float) originY;
	}
	
	public void setOriginCenter()
	{
		originX = (float) (width/2);
		originY = (float) (height/2);
	}
	public void setScale(double xyScale)
	{
		
	}
	
	public void draw()
	{
		Graphics2D g = Core.getGraphics();
		width = img.getWidth();
		height = img.getHeight();
		int x = (int) (this.x*xScale + (width*xScale) * (Math.abs(xFlip-1)/2));
		int y = (int) ((-this.y*yScale+Core.s.getHeight())-height*yScale + (height*yScale) * (Math.abs(yFlip-1)/2));
		g.scale(xScale,yScale);
		g.rotate(theta, x + originX*xFlip*xScale, y + originY*yFlip*yScale);
		g.drawImage(img, x, y, (int)(width * xFlip), (int)(height*yFlip),null);
		g.rotate(-theta, -(x+originX*xFlip*xScale), -(y+originY*yFlip*yScale));
		g.scale(1,1);
	}
	
	public void simpleDraw(int x, int y, int w, int h){
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
	}
	public void simpleDraw(int x, int y){
		double windowOff = 25;
		if(Core.s.isFullScreen())
		{
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - img.getHeight()))), (int)(img.getWidth()*xScale), (int)(img.getHeight()*yScale), null);
	}
	public void simpleDraw(int x, int y, int w, int h, double rads){
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.rotate(rads, (int)(x*xScale) + (w*xScale)/2,(int)(Core.s.getHeight()+(yScale*(- y - h)))+(int)(h*yScale)/2);
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
		g.rotate(-rads);
	}
	public void simpleDraw(int x, int y, int w, int h, double rads, int orgX, int orgY){
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.rotate(rads, (x*xScale) + (w*xScale)/2 + xScale*orgX*(w/img.getWidth()),Core.s.getHeight()+(yScale*(- y - h))+(h*yScale)/2 + yScale*orgY *(h/img.getHeight()));
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
		g.rotate(-rads);
	}
	public void simpleDraw(int x, int y, int w, int h, int alpha){
		setAlpha(alpha);
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
	}
	public void simpleDraw(int x, int y, int alpha){
		setAlpha(new Integer(alpha).byteValue());
		double windowOff = 25;
		if(Core.s.isFullScreen())
		{
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - img.getHeight()))), (int)(img.getWidth()*xScale), (int)(img.getHeight()*yScale), null);
	}
	public void simpleDraw(int x, int y, int w, int h, double rads, int alpha){
		setAlpha(alpha);
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.rotate(rads, (int)(x*xScale) + (w*xScale)/2,(int)(Core.s.getHeight()+(yScale*(- y - h)))+(int)(h*yScale)/2);
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
		g.rotate(-rads);
	}
	public void simpleDraw(int x, int y, int w, int h, double rads, int orgX, int orgY, int alpha){
		setAlpha(alpha);
		double windowOff = 25;
		if(Core.s.isFullScreen()){
			windowOff = 0;
		}
		xScale =(double)((double)(Core.s.getWidth())/1980d);
		yScale = (double)((double)(Core.s.getHeight()-windowOff)/1080d);
		Graphics2D g = Core.getGraphics();
		g.rotate(rads, (x*xScale) + (w*xScale)/2 + xScale*orgX*(w/img.getWidth()),Core.s.getHeight()+(yScale*(- y - h))+(h*yScale)/2 + yScale*orgY *(h/img.getHeight()));
		g.drawImage(img, (int)(x*xScale), (int)(Core.s.getHeight()+(yScale*(- y - h))), (int)(w*xScale), (int)(h*yScale), null);
		g.rotate(-rads);
	}
	/*
	public void setAlpha(int a){
		a &= 0x000000ff;
		//System.out.println("a: " + a);
		for(int i = 0; i < img.getWidth(); i++){
			for(int j = 0; j < img.getHeight(); j++){
				int argb = img.getRGB(i, j);
				int alpha = ~(argb >> 24);
				//System.out.println("IAlpha: " + alpha);
				//System.out.println("Last Change: " + lastAlphaChange);
				if(i == 0 && j == 0)
					System.out.println("Ialpha: " + alpha);
				alpha = ((int)(alpha * .5d));
				//System.out.println("fAlpha: " + alpha);
				if(i == 0 && j == 0){
					System.out.println("IArgb: " + argb +"\nFalpha: " + alpha);
					System.out.println("Falpha: " + alpha);
				}
				if(i == 0 && j == 0)
					System.out.println("I2Argb: " + argb);
				argb &= 0x00ffffff;
				alpha <<= 24;
				if(i==0 && j == 0)
					System.out.println("I3Argb: " + argb);
				argb |= alpha;
				if(i == 0 && j == 0)
					System.out.println("fargb: " + argb);
				img.setRGB(i, j, argb);
			}
		}
		lastAlphaChange = 255 - a;
	}
	*/
	private void setAlpha(int a){
		
	}
	private void setAlpha(float aScale){
	
	}
}