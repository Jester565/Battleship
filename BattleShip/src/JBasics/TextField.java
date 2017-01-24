package JBasics;

import pack.Core;
import pack.KeyInput;
import pack.MouseInput;

public class TextField {

	private int x;
	private int y;
	private int w;
	private int h;
	private int cursorOff = 0;
	private int cursorX = 0;
	private boolean extendable = false;
	private double cursorFlashTimer = 0;
	private int cursorPos = -1;
	private String savedTypedKey = "";
	private boolean fieldSelected = false;
	public String finalMessage = null;
	String message = "";
	ShapeRenderer sr;
	public TextField(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		sr =  new ShapeRenderer();
	}
	public TextField(boolean extendable, int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.extendable = extendable;
		sr =  new ShapeRenderer();
	}
	public boolean isSelected(){
		return fieldSelected;
	}
	public void reset(){
		finalMessage = null;
		fieldSelected = false;
		message = "";
		cursorX = x + 30;
		cursorPos = -1;
		cursorOff = 0;
	}
	public void setFinalMessage()
	{
		finalMessage = message;
	}
	public String getMessage(){
		return message;
	}
	public void resetFinalMessage(){
		finalMessage = null;
	}
	public void getInput()
	{
		String typedKey = null;
		if(KeyInput.typedKey == null)
			savedTypedKey = "";
		if(KeyInput.typedKey != null && !KeyInput.typedKey.equals(savedTypedKey) || KeyInput.getKeyTypedUpdated()){
			typedKey = KeyInput.typedKey;
			savedTypedKey = typedKey;
		}
		String realTyped = "";
		if(typedKey != null && fieldSelected)
		{
			cursorFlashTimer = 25;
			if(typedKey.length()<2)
			{
				if(!KeyInput.keyPressed("Shift"))
					typedKey = typedKey.toLowerCase();
				realTyped = typedKey;
				cursorPos++;
			}
			else if(typedKey.equals("Backspace"))
			{
				if(cursorPos > -1)
				{
					message = message.substring(0,cursorPos)+message.substring(cursorPos+realTyped.length()+1,message.length());
					cursorPos--;
					realTyped = "";
				}
			}
			else if(typedKey.equals("Space"))
			{
				realTyped = " ";
				cursorPos++;
			}
			else if(typedKey.equals("Period"))
			{
				realTyped = ".";
				cursorPos++;
			}
			else if(typedKey.equals("Minus"))
			{
				realTyped = "-";
				cursorPos++;
			}
			else if (typedKey.equals("Enter"))
			{
				realTyped = "";
				fieldSelected = false;
				finalMessage = message;
			}
			else
			{
				realTyped = "";
			}
			if(!realTyped.equals(""))
			{
				message = message.substring(0,cursorPos)+realTyped+message.substring(cursorPos+realTyped.length()-1,message.length());
			}
		}
		if(!extendable && ShapeRenderer.getFontWidth(message,  h-(int)(h/4d))*ShapeRenderer.xScale > w-5)
		{
			message = message.substring(0, message.length()-1);
			cursorPos--;
		}
	}
	private void drawMessage(){
	
		
		if(extendable && fieldSelected){
			if(cursorX > x+w){
				cursorOff++;
			}else if(cursorX < x + 20 && cursorOff > 0){
				cursorOff--;
			}
		}
		if(extendable){
			String s = refinedExtendable();
			int endPoint = s.length();
			for(int i = 1; i < s.length();i++){
				if(ShapeRenderer.getFontWidth(s, h - (int)(h/4d), i) * ShapeRenderer.xScale > w-2){
					endPoint = i;
					break;
				}
			}
			sr.drawText(s.substring(0,endPoint), x+5, y+(int)(h/3.7d), h - (int)(h/4d), 0, 0, 0, 1);
		}else{
			sr.drawText(message, x+5, y+(int)(h/5d), h - (int)(h/4d), 0, 0, 0, 1);
		}
	}
	private String refinedExtendable(){
		if(extendable){
			String s = message.substring(cursorOff);
			return s;
		}
		return null;
	}
	public void draw(boolean drawBackGround)
	{
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h,.6f,.6f,.6f,1);
		}
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void draw(boolean drawBackGround, String string)
	{
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h,.6f,.6f,.6f,1);
		}
		sr.drawText(string, x+5, y+(int)(h/3.7d)+h, h - (int)(h/4d), 0, 0, 0, 1);
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void draw(boolean drawBackGround, int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h,.6f,.6f,.6f,1);
		}
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void draw(boolean drawBackGround, int x, int y, int w, int h,float r, float g, float b, float a)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h,r,g,b,a);
		}
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void draw(boolean drawBackGround, String string, int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h,.6f,.6f,.6f,1);
		}
		sr.drawText(string, x+5, y+(int)(h/3.7d)+h, h - (int)(h/4d), 0, 0, 0, 1);
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void draw(boolean drawBackGround, String string, int x, int y, int w, int h, float r, float g, float b, float a)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		getInput();
		if(drawBackGround)
		{
			sr.drawRectangle(true, x, y, w, h, r,g,b,a);
		}
		sr.drawText(string, x+5, y+(int)(h/3.7d)+h, h - (int)(h/4d), 0, 0, 0, 1);
		drawMessage();
		if(fieldSelected)
			cursorManager(h);
		mouseOnTextManager();
	}
	public void cursorManager(int h)
	{
		cursorFlashTimer-=Core.rate;
		if(cursorFlashTimer < 0)
		{
			cursorFlashTimer = 40;
		}
		if(extendable){
			cursorX = (int)(x+ShapeRenderer.getFontWidth(refinedExtendable(), h-(int)(h/4d),cursorPos+1 - cursorOff)*ShapeRenderer.xScale + 5);
		}
		else{
			cursorX = (int)(x+ShapeRenderer.getFontWidth(message, h-(int)(h/4d),cursorPos+1)*ShapeRenderer.xScale + 5);
		}
		if(cursorFlashTimer < 25)
		{
			sr.drawRectangle(true, cursorX, y + (int)(h/6d), (int)(h/15d), h-(int)(h/4d),0,0,0,1);
		}
		if(KeyInput.typedKey!=null&&KeyInput.typedKey.equals("Left")||MouseInput.wheelDown())
		{
			if(cursorPos>-1)
			{
				cursorPos--;
			}
		}
		else if(KeyInput.typedKey!=null&&KeyInput.typedKey.equals("Right")||MouseInput.wheelUp())
		{
			if(cursorPos < message.length()-1)
			{
				cursorPos++;
			}
		}
		
	}
	private void mouseOnTextManager()
	{
		if(Button.hitButton(x-5, y, w, h))
		{
			fieldSelected = true;
			int smallestDistance = Integer.MAX_VALUE;
			int smallestCursorPos = Integer.MAX_VALUE;
			for(int i = 0; i < message.length();i++)
			{
				int distance = Math.abs(MouseInput.getX()-(x+ShapeRenderer.getFontWidth(message,  h-(int)(h/4d),i)+ShapeRenderer.getFontWidth(message,  h-(int)(h/4d),i,i+1)+5));
				if(distance < smallestDistance)
				{
					smallestDistance = distance;
					smallestCursorPos = i;
				}
			}
			if(MouseInput.getX()>x&&message.length()>1)
			{
				cursorFlashTimer = 25 ;
				cursorPos = smallestCursorPos + cursorOff;
			}
			else
			{
				cursorFlashTimer = 25;
				cursorPos = -1 + cursorOff;
			}
		}
		else if(MouseInput.left())
		{
			fieldSelected = false;
		}
	}
}

