package JBasics;

public abstract class BoxCheck {

	public static boolean pointInsideBox(int pX, int pY, int x, int y, int w, int h){
		if(pX <= x+w && pX >= x && pY <= y+h && pY >= y)
			return true;
		return false;
	}
	public static boolean boxInsideBox(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){
		if(x1 < x2 + w2 && x1 +w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2){
			return true;
		}
		return false;
	}
	public static boolean pointInsideBox(int pX, int pY, int x, int y, int w, int h,float degrees){
		if(pX <= x+Math.cos(degrees*(Math.PI/180))*w && pX >= x && pY <= y+h && pY >= y)
			return true;
		return false;
	}
}
