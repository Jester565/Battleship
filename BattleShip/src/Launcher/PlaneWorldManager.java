package Launcher;

import java.util.ArrayList;

import JBasics.ShapeRenderer;

public class PlaneWorldManager {

	private ArrayList<PlaneWorld> planeWorlds;
	public PlaneWorldManager(ShapeRenderer sr, int numPlanes, int x, int y,int w, int h){
		planeWorlds = new ArrayList<PlaneWorld>();
		double planeHeight = ((double)(h)-(PlaneWorld.SURROUND_DISTANCE * numPlanes * 2d))/(double)numPlanes;
		System.out.println(planeHeight);
		for(int i = 0; i < numPlanes; i++){
			planeWorlds.add(new PlaneWorld(sr,numPlanes - i - 1,x + PlaneWorld.SURROUND_DISTANCE, y + (int)(PlaneWorld.SURROUND_DISTANCE * 2+planeHeight)*i + PlaneWorld.SURROUND_DISTANCE,w-PlaneWorld.SURROUND_DISTANCE*2,(int)(planeHeight)));
		}
	}
	public void skip(){
		for(PlaneWorld pw: planeWorlds){
			pw.skip();
		}
	}
	public ArrayList<PlaneWorld> getPlanes(){
		return planeWorlds;
	}
	public void draw(){
		for(int i = 0; i < planeWorlds.size(); i++){
			planeWorlds.get(i).draw(planeWorlds); 
		}
	}
}
