package JBasics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import pack.Core;

public class ShapeRenderer {
	int screenHeight = Core.s.getHeight();
	public static double xScale = 1980d/(double)Core.s.getWidth();
	static double yScale = 1080d/(double)Core.s.getHeight();
	private static Graphics2D g = Core.s.getGraphics(); 
public ShapeRenderer()
{
	
}

private void update()
{
	double windowOff = 25;
	if(Core.s.isFullScreen())
	{
		windowOff = 0;
	}
	g = Core.getGraphics();
	xScale = 1980d/(Core.s.getWidth());
	yScale = 1080d/(Core.s.getHeight() - windowOff);
	screenHeight = Core.s.getHeight();
}

public void setColor(float re, float gr, float bl)
{
	update();
	g.setColor(new Color(re,gr,bl));
}

public void setColor(float re, float gr, float bl, float a)
{
	update();
	g.setColor(new Color(re,gr,bl,a));
}
public void drawBorderlessRectangle(boolean filled, int x, int y, int width, int height)
{
	update();
	g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
}
public void drawBorderlessRectangle(boolean unScaled, int x, int y, int width, int height, float re, float gr, float bl, float a)
{
	update();
	g.setColor(new Color(re,gr,bl,a));
	if(unScaled)
		g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
	else
		g.fillRect((int)(x), (int)(screenHeight-(y+height)), (int)(width), (int)(height));
}
public void drawRectangle(boolean filled, int x, int y, int width, int height)
{
	update();
	if(filled)
	{
		g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0,1));
	}
	g.drawRect((int)(x/xScale)-2, (int)(screenHeight-(y+height)/yScale)-2, (int)(width/xScale)+4, (int)(height/yScale)+4);
}

public void drawRectangle(boolean filled, int x, int y, int width, int height, float re, float gr, float bl, float a)
{
	update();
	if(a > 1){
		a = 1;
	}else if(a < 0){
		a = 0;
	}
	g.setColor(new Color(re,gr,bl,a));
	if(filled)
	{
		g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0,a));
	}
	g.drawRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
}
public void drawUnscaledRectangle(boolean xScaleOn, boolean yScaleOn, boolean filled, int x, int y, int width, int height)
{
	update();
	double xScale = 1;
	double yScale = 1;
	if(xScaleOn)
		xScale = this.xScale;
	if(yScaleOn)
		yScale = this.yScale;
	if(filled)
	{
		g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0,1));
	}
	g.drawRect((int)(x/xScale)-2, (int)(screenHeight-(y+height)/yScale)-2, (int)(width/xScale)+4, (int)(height/yScale)+4);
}

public void drawUnscaledRectangle(boolean xScaleOn, boolean yScaleOn, boolean filled, int x, int y, int width, int height, float re, float gr, float bl, float a){
	update();
	double xScale = 1;
	double yScale = 1;
	if(xScaleOn){
		xScale = this.xScale;
	}
	if(yScaleOn){
		yScale = this.yScale;
	}
	g.setColor(new Color(re,gr,bl,a));
	if(filled)
	{
		g.fillRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0,a));
	}
	g.drawRect((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
}
public void drawCircle(boolean filled, int x, int y, int radius, float re, float gr, float bl, float a)
{
	update();
	
	g.setColor(new Color(re,gr,bl,a));
	if(filled){
		g.fillOval((int)((x-radius)/xScale), (int)(screenHeight - (y+radius)/yScale), (int)((radius*2)/xScale), (int)((radius*2)/yScale));
		g.setColor(new Color(0,0,0,a));
	}
	g.drawOval((int)((x-radius)/xScale), (int)(screenHeight - (y+radius)/yScale), (int)((radius*2)/xScale), (int)((radius*2)/yScale));
}

public void drawCircle(boolean filled, int x, int y, int width, int height, float re, float gr, float bl, float a)
{
	update();
	g.setColor(new Color(re,gr,bl,a));
	if(filled)
	{
		g.fillOval((int)(x/xScale),(int)( screenHeight -(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0,a));
	}
	g.drawOval((int)(x/xScale), (int)(screenHeight-(y+height)/yScale),(int)( width/xScale), (int)(height/yScale));
}

public void drawCircle(boolean filled, int x, int y, int width, int height)
{
	update();
	if(filled)
	{
		g.fillOval((int)(x/xScale), (int) ((int)(screenHeight-(y+height))/yScale), (int)(width/xScale), (int)(height/yScale));
		g.setColor(new Color(0,0,0));
	}
	g.drawOval((int)(x/xScale), (int)(screenHeight-(y+height)/yScale), (int)(width/xScale), (int)(height/yScale));
}

public void drawCircle(boolean filled, int x, int y, int radius)
{
	update();
	if(filled)
	{
		g.fillOval((int)((x-radius)/xScale), (int)(screenHeight-(y+radius)/yScale), (int)((radius*2)/xScale), (int)((radius*2)/yScale));
		g.setColor(new Color(0,0,0));
	}
	g.drawOval((int)((x-radius)/xScale), (int)(screenHeight-(y+radius)/yScale), (int)((radius*2)/xScale),(int)((radius*2)/yScale));
}

public void drawLine(int x1, int y1, int x2, int y2)
{
	update();
	g.drawLine((int)(x1/xScale), (int)(screenHeight-y1/yScale), (int)(x2/xScale), (int)(screenHeight-y2/yScale));
}

public void drawLine(int x1, int y1, int x2, int y2, float re, float ge, float bl, float a)
{
	update();
	g.setColor(new Color(re,ge,bl,a));
	g.drawLine((int)(x1/xScale), (int)(screenHeight-y1/yScale), (int)(x2/xScale), (int)(screenHeight-y2/yScale));
}
public void drawLine(int x1, int y1, int x2, int y2, int w, float re, float ge, float bl, float a)
{
	update();
	g.setColor(new Color(re,ge,bl,a));
	g.setStroke(new BasicStroke(w));
	g.drawLine((int)(x1/xScale), (int)(screenHeight-y1/yScale), (int)(x2/xScale), (int)(screenHeight-y2/yScale));
	g.setStroke(new BasicStroke(1));
}
public void drawText(boolean backGround, String s, float x, float y, float br, float bg, float bb, float ba)
{
	update();
	drawRectangle(true,(int)x, (int)y + (int)(g.getFont().getSize()/1.3f), (int)(getFontWidth(s,g.getFont().getSize()) * xScale * 1.2f), (int)(g.getFont().getSize() * 1.3f),br,bg,bb,ba);
	drawText(s, x, y);
}
public void drawCenteredText(String s, int x, int y, float fontSize, float r, float gr, float b, float a){
	update();
	if(a > 1){
		a = 1;
	}else if(a < 0){
		a = 0;
	}
	Font font = g.getFont();
	g.setColor(new Color(r, gr, b, a));
	font.deriveFont((float)((float)fontSize/(float)yScale));
	g.setFont(font);
	g.drawString(s, (int)(x/xScale) - getFontWidth(s,fontSize)/2, (int)(screenHeight-(y)/yScale));
}
public void drawText(boolean backGround, String s, float x, float y, float fontSize, float br, float bg, float bb, float ba)
{
	update();
	Font font = g.getFont();
	font.deriveFont((float)((float)fontSize/(float)yScale));
	g.setFont(font);
	drawRectangle(true,(int)x, (int)y + (int)(g.getFont().getSize()/1.3f), (int)(getFontWidth(s,g.getFont().getSize()) * ShapeRenderer.xScale * 1.2f), (int)(g.getFont().getSize() * 1.3f),br,bg,bb,ba);
	drawText(s, x, y);
}
public void drawText(String s, float x, float y)
{
	update();
	g.drawString(s, (int)(x/xScale), (int)(screenHeight-(y+g.getFont().getSize())/yScale));
}
public void drawText(String s, float x, float y, float fontSize)
{
	update();
	Font font = g.getFont();
	font.deriveFont((float)((float)fontSize/(float)yScale));
	g.setFont(font);
	g.drawString(s, (int)(x/xScale), (int)(screenHeight-(y+g.getFont().getSize())/yScale));
}
public void drawText(String s, float x, float y, float re, float gr, float bl, float a)
{
	update();
	g.setColor(new Color(re,gr,bl));
	g.drawString(s, (int)(x/xScale), (int)(screenHeight-(y+g.getFont().getSize())/yScale));
}

public void drawText(String s, float x, float y, float fontSize, float re, float gr, float bl, float a)
{
	update();
	Font font = g.getFont();
	font = font.deriveFont((float) ((float)fontSize/(float)yScale));
	g.setFont(font);
	g.setColor(new Color(re,gr,bl,a));
	g.drawString(s, (int)(x/xScale), (int)(screenHeight-(y)/yScale));
}
public void drawText(String s, float x, float y, float w, float fontSize){
	int indexOff = 0;
	String sub =s;
	while(getFontWidth(s,fontSize,0,s.length()-indexOff) > w && indexOff < s.length()){
		sub = s.substring(0,s.length()-indexOff);
		indexOff++;
	}
	drawText(sub,x,y,fontSize);
}
public void drawText(String s, float x, float y, float w, float fontSize,float r, float g, float b, float a){
	int indexOff = 0;
	String sub =s;
	while(getFontWidth(s,fontSize,0,s.length()-indexOff) > w && indexOff < s.length()){
		sub = s.substring(0,s.length()-indexOff);
		indexOff++;
	}
	drawText(sub,x,y,fontSize,r,g,b,a);
}
public static int getFontWidth(String s, float fontSize)
{
	Font font = g.getFont();
	font = font.deriveFont((float) (fontSize/yScale));
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics();
	return fm.stringWidth(s);
}

public static int getFontWidth(String s, float fontSize,int fontPos)
{
	Font font = g.getFont();
	font = font.deriveFont((float) (fontSize/yScale));
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics();
	s = s.substring(0,fontPos);
	return fm.stringWidth(s);
}

public static int getFontWidth(String s, float fontSize,int beginPos,int endPos)
{
	Font font = g.getFont();
	font = font.deriveFont((float) (fontSize/yScale));
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics();
	s = s.substring(beginPos,endPos);
	return fm.stringWidth(s);
}
}
