package JBasics;


public class FPSLogger {

	private int lastSecond = 0;
	private int frames = 0;
	private String frameRate = "0";
	public FPSLogger()
	{
		
	}
	public void printFPS()
	{
		frames++;
		if((int)(System.currentTimeMillis()/1000d)>lastSecond)
		{
			lastSecond = (int)(System.currentTimeMillis()/1000d);
			System.out.println(frames);
			frames = 0;
		}
	}
	public void drawFPS(ShapeRenderer sr, int x, int y, float font)
	{
		frames++;
		if((int)(System.currentTimeMillis()/1000d)>lastSecond)
		{
			lastSecond = (int)(System.currentTimeMillis()/1000d);
			frameRate = Integer.toString(frames);
			frames = 0;
		}
		sr.drawText(frameRate, x, y,font,1,1,1,1);
	}
}
