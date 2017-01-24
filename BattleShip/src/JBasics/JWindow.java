package JBasics;

import pack.MouseInput;

public class JWindow {

	private final static double scalingSizeX = 1980;
	private final static double scalingSizeY = 1080;
	protected ShapeRenderer sr;
	protected int wX;
	protected int wY;
	protected int wW;
	protected int wH;
	private int dragTimer = 0;
	public boolean on = true;
	private boolean dragged = false;
	private boolean dragV = false;
	private boolean dragH = false;
	private String name = "Window";
	public JWindow(){
		sr = new ShapeRenderer();
	}
	public JWindow(String name, int x, int y, int w, int h){
		sr = new ShapeRenderer();
		wX = x;
		wY = y;
		wW = w;
		wH = h;
		this.name = name;
	}
	public void draw(){
		dragTimer--;
		sr.drawRectangle(true, wX, wY, wW, wH,0,1,.2f,.5f);
		if(Button.hitDrawnButton(sr, "X", wX + wW - 20, wY + wH - 20, 20, 20, 1f, 0, 0, 1f))
			on = false;
		if(Button.hitDrawnButton(sr, " ", wX + 20, wY + wH - 20, wW - 40, 20, 0, 1, 0f, .3f)){
			if(dragV && dragTimer < 0){
				dragV = false;
				dragTimer = 5;
			}
			if(!dragV && dragTimer < 0){
				dragV = true;
				dragTimer = 5;
			}
		}
		if(Button.hitDrawnButton(sr, " ", wX + wW - 20, wY, 20, wH - 20, 0, 1, 0f, .3f)){
			if(dragH && dragTimer < 0){
				dragH = false;
				dragTimer = 5;
			}
			if(!dragH && dragTimer < 0){
				dragH = true;
				dragTimer = 5;
			}
		}
		if(Button.hitDrawnButton(sr, " ", wX, wY + wH - 20, 20, 20, 0, 0, 1f, 1f)){
			if(dragged && dragTimer < 0){
				dragged = false;
				dragTimer = 5;
			}
			if(!dragged && dragTimer < 0){
				dragged = true;
				dragTimer = 5;
			}
		}
		if(dragged){
			wX = MouseInput.getX() - 10;
			wY = MouseInput.getY() - wH + 10;
		}
		if(dragV){
			if(MouseInput.getY() > wY + 100){
				wH = MouseInput.getY() - wY + 5;
			}
		}
		if(dragH){
			if(MouseInput.getX() > wX + 100){
				wW = MouseInput.getX() - wX + 5;
			}
		}
		String cutName = name;
		if(ShapeRenderer.getFontWidth(name, 17) + 23 > wW - 33){
			for(int i = 0; i < name.length(); i++){
				if(ShapeRenderer.getFontWidth(name.substring(0,i), 17) +23 > wW - 33){
					cutName = name.substring(0, i);
					break;
				}
			}
		}
		sr.drawText(cutName, wX + 23, wY + wH - 17, 17,1,1,1,1);
	}
	public void drawRect(boolean filled, int x, int y, int width, int height){
		sr.drawRectangle(filled, (int)(x * (wW/scalingSizeX)) + wX, (int)(y * (wH/scalingSizeY)) + wY, (int)(width * (wW/scalingSizeX)), (int)(height* (wH/scalingSizeY)));
	}
	public void drawRect(boolean filled, int x, int y, int width, int height, float r, float g, float b, float a){
		sr.drawRectangle(filled, (int)(x * (wW/scalingSizeX)) + wX, (int)(y * (wH/scalingSizeY)) + wY, (int)(width * (wW/scalingSizeX)), (int)(height* (wH/scalingSizeY)), r, g, b, a);
	}
	public void drawText(String s, int x, int y, float fontSize){
		sr.drawText(s, (int)(x * (wW/scalingSizeX)) + wX, (int)(y * (wH/scalingSizeY)) + wY, (float)((double)fontSize * ((double)wW/scalingSizeX)));
	}
	public void drawText(String s, int x, int y, float fontSize, float r, float g, float b, float a){
		sr.drawText(s, (int)(x * (wW/scalingSizeX)) + wX, (int)(y * (wH/scalingSizeY)) + wY, (float)(fontSize * (float)(wW/scalingSizeX)), r, g, b, a);
	}
	public boolean drawHitButton(String s, double x, double y, double w, double h, float r, float g, float b, float a){
		return Button.hitDrawnButton(sr, s, (int)(x * (wW/scalingSizeX)) + wX, (int)(y * (wH/scalingSizeY)) + wY, (int)(w * (wW/scalingSizeX)), (int)(h* (wH/scalingSizeY)), r, g, b, a);
	}
}
