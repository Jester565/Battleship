package JBasics;


public class Coordinate {

	private double x;
	private double y;
	public Coordinate(){
		
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public void setXY(double x, double y){
		setX(x);
		setY(y);
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
