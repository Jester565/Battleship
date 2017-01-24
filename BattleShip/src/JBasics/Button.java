package JBasics;

import pack.MouseInput;

public abstract class Button {
	
	public static boolean overButton(double x, double y, double w, double h)
	{
		if(MouseInput.getX()>x && MouseInput.getX()<x+w && MouseInput.getY()>y && MouseInput.getY()<y+h){
			return true;
		}
		return false;
	}
	public static boolean hitButton(double x, double y, double w, double h)
	{
		if(MouseInput.left() && MouseInput.getX()>x && MouseInput.getX()<x+w && MouseInput.getY()>y && MouseInput.getY()<y+h){
			return true;
		}
		return false;
	}
	public static boolean hitDrawnButton(ShapeRenderer sr, String s,double x, double y, double w, double h,float r, float g, float b, float a){
		float fontSize =  (float)(w/s.length());
		if(fontSize > (float)(h/1.5d)){
			fontSize = (float)(h/1.5d);
		}
		if(a > 1){
			a = 1;
		}else if (a<0){
			a = 0;
		}
		if(overButton(x,y,w,h)){
			sr.drawRectangle(true, (int)x, (int)y, (int)w, (int)h, Math.abs(r-.3f),  Math.abs(g-.3f),  Math.abs(b-.3f), a);
			sr.drawCenteredText(s, (int)(x + w/2), (int)(y+ h/7), fontSize,0,0,0,a);
			if(MouseInput.left()){
				return true;
			}
		}
		else{
			sr.drawRectangle(true, (int)x, (int)y, (int)w, (int)h, r, g, b, a);
			sr.drawCenteredText(s, (int)(x + w/2), (int)(y + h/7), fontSize,0,0,0,a);
		}
		return false;
	}
	public static boolean inCircle(double x, double y, double cenX, double cenY, double radius){
		double r = Math.sqrt(Math.pow(x - cenX,2) + Math.pow(y-cenY, 2));
		if(r <= radius){
			return true;
		}
		return false;
	}
}
