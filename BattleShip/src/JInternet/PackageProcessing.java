package JInternet;

import java.net.DatagramPacket;
import java.util.ArrayList;

import JBasics.Button;
import JBasics.JWindow;
import JBasics.ShapeRenderer;
import pack.Core;
import pack.MouseInput;

public class PackageProcessing {

	private static ArrayList<Key> keyRoutines;
	private static ArrayList<PackTracker> packTrackers;
	public PackageProcessing(){
		keyRoutines = new ArrayList<Key>();
	}
	public static void addToKeyRoutines(Key cpk){
		clearListOf(cpk);
		keyRoutines.add(cpk);
	}
	private synchronized static void clearListOf(Key cpk){
		for(int i = 0; i < keyRoutines.size(); i++){
			Key k = keyRoutines.get(i);
			if(cpk.getKey().equals(k.getKey())){
				System.out.println("Key: " + k.getKey() + " was replaced.");
				keyRoutines.remove(i);
				i--;
			}
		}
	}
	public static void reset(){
		keyRoutines.clear();
		if(packTrackers != null)
			packTrackers.clear();
	}
	public static void addPackTracker(String key){
		if(packTrackers == null){
			packTrackers = new ArrayList<PackTracker>();
		}
		boolean found = false;
		for(PackTracker track:packTrackers){
			if(track.getKey().equals(key)){
				found = true;
				break;
			}
		}
		if(!found)
			packTrackers.add(new PackTracker(key));
	}
	public static void removeKey(Key key){
		for(int i = 0; i < keyRoutines.size(); i++){
			if(keyRoutines.get(i).equals(key)){
				keyRoutines.remove(i);
				return;
			}
		}
	}
	public static synchronized ArrayList<Key> getProcessKeys(){
		return keyRoutines;
	}
	public static synchronized void processPackage(DatagramPacket pack){
		String locationKey = trimPackKey(pack);
		iterateKeys(locationKey,pack);
		addToPackTrackers(locationKey, pack);
	}
	private static String trimPackKey(DatagramPacket pack){
		byte[] packData = pack.getData();
		byte[] keyData = new byte[InternetCore.PACKET_INFO_LENGTH];
		byte[] trimData = new byte[pack.getData().length - InternetCore.PACKET_INFO_LENGTH];
		for(int i = 0; i < packData.length; i++){
			if(i < InternetCore.PACKET_INFO_LENGTH)
				keyData[i] = packData[i];
			else
				trimData[i - InternetCore.PACKET_INFO_LENGTH] = packData[i];
		}
		pack.setData(trimData);
		pack.setLength(trimData.length);
		return new String(keyData);
	}
	public static synchronized void addToPackTrackers(String locationKey, DatagramPacket pack){
		if(packTrackers != null){
			for(PackTracker tracker: packTrackers){
				if(tracker.saveData(locationKey, pack)){
					break;
				}
			}
		}
	}
	public static synchronized ArrayList<PackTracker> getPackTrackers(){
		return packTrackers;
	}
	private synchronized static void iterateKeys(String locationKey,DatagramPacket pack){
		for(Key key: keyRoutines){
			if(key.run(locationKey,pack))
				return;
		}
		try {
			throw new Exception("Key " + locationKey + " was not found under keyRoutines...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class PackTracker{
	private String key;
	private boolean sendPack = false;
	private double clickTimer = 0;
	private ArrayList<DatagramPacket> packs;
	private ArrayList<Double> dataTimes;
	private ArrayList<PackInfo> packInfos;
	private ShapeRenderer sr;
	private static final int UPDATE_TIME = 100;
	private double updateTimer = UPDATE_TIME;
	private ArrayList<PackInfo> savedPacks;
	public PackTracker(String key){
		this.key = key;
		packs = new ArrayList<DatagramPacket>();
		dataTimes = new ArrayList<Double>();
		packInfos = new ArrayList<PackInfo>();
		sr = new ShapeRenderer();
		savedPacks = new ArrayList<PackInfo>();
	}
	public PackTracker(boolean send, String key){
		sendPack = send;
		this.key = key;
		packs = new ArrayList<DatagramPacket>();
		dataTimes = new ArrayList<Double>();
		packInfos = new ArrayList<PackInfo>();
		sr = new ShapeRenderer();
		savedPacks = new ArrayList<PackInfo>();
	}
	public String getKey(){
		return key;
	}
	public synchronized void addToPackInfo(DatagramPacket pack){
		packInfos.add(new PackInfo(pack, System.currentTimeMillis()));
	}
	public synchronized boolean saveData(String key, DatagramPacket pack){
		if(this.key.equals(key)){
			dataTimes.add((double) System.currentTimeMillis());
			packs.add(pack);
			return true;
		}
		return false;
	}
	public synchronized void updateData(){
		for(int i = 0; i < packs.size(); i++){
			packInfos.add(new PackInfo(packs.get(i), dataTimes.get(i)));
		}
		dataTimes.clear();
		packs.clear();
	}
	public void drawTimeLine(int x, int y, int w, int h, double timeRange){
		updateTimer -= Core.rate;
		clickTimer -= Core.rate;
		if(!sendPack && updateTimer < 0){
			updateData();
			updateTimer = UPDATE_TIME;
		}
		for(int i = 0; i < packInfos.size(); i++){
			packInfos.get(i).draw(x, y, w, h, timeRange);
		}
		sr.drawRectangle(true, x, y, w, h/10,0,0,1,.5f);
	}
	public void drawTimeLine(JWindow win, int x, int y, int w, int h, double timeRange){
		clickTimer -= Core.rate;
		updateTimer -= Core.rate;
		if(updateTimer < 0){
			updateData();
			updateTimer = UPDATE_TIME;
		}
		for(int i = 0; i < packInfos.size(); i++){
			packInfos.get(i).draw(win,x, y, w, h, timeRange);
		}
		win.drawRect(true, x, y, w, h/10,0,0,1,.5f);
	}
	public void addToSavedPacks(PackInfo info){
		boolean found = false;
		for(PackInfo pInfo: savedPacks){
			if(pInfo.equals(info)){
				found = true;
				break;
			}
		}
		if(!found)
			savedPacks.add(info);
	}
	public void drawSavedPacks(JWindow win, int x, int y, int w, int h){
		win.drawRect(true, x, y, w, h,0,0,1,.3f);
		int pH = y;
		for(int i = 0; i < savedPacks.size(); i++){
			if(pH < h + y){
				pH += w;
				savedPacks.get(i).drawSaved(win, x, pH, w, w);
			}
		}
	}
	public void remove(PackInfo info){
		for(int i = 0; i < packInfos.size(); i++){
			if(packInfos.get(i).equals(info)){
				packInfos.remove(i);
				break;
			}
		}
	}
	class PackInfo{
		
		private DatagramPacket pack;
		private double time;
		private boolean pressed;
		private InfoWindow window;
		private boolean saved = false;
		private boolean outOfRange = false;
		public PackInfo(DatagramPacket pack, double time){
			this.pack = pack;
			this.time = time;
			window = new InfoWindow();
		}
		public boolean equals(Object obj){
			if(((PackInfo)obj).time == time)
				return true;
			return false;
		}
		public void draw(int x, int y, int w, int h, double timeRange){
			double pX = x + (time - System.currentTimeMillis())*(w/timeRange) + w;
			double pY = y + h/3d;
			double pW = 10;
			double pH = (h)/3d;
			if(!pressed){
				sr.drawRectangle(true, (int)pX, (int)pY, (int)pW, (int)pH,0,0,1,1);
			}else{
				sr.drawRectangle(true, (int)pX, (int)pY, (int)pW, (int)pH,1,0,0,1);
			}
			if(Button.overButton(pX,pY,pW,pH) && clickTimer < 0){
				sr.drawText(key, (int)(pX - 3),(int)(pY + pH + 3), 20,0,0,0,1);
				if(MouseInput.left()){
					window = new InfoWindow();
					clickTimer = 20;
					pressed = true;
				}
			}
			if(pressed){
				window.draw();
			}
			if(System.currentTimeMillis() - time > timeRange){
				outOfRange = true;
			}else{
				outOfRange = false;
			}
			if(!pressed){
				if(outOfRange){
					remove(this);
				}
			}
		}
		public void draw(JWindow jwin, int x, int y, int w, int h, double timeRange){
			double pX = x + (time - System.currentTimeMillis())*(w/timeRange) + w;
			double pY = y + h/7d;
			double pW = 10;
			double pH = (h)/3d;
			if(!pressed){
				jwin.drawRect(true, (int)pX, (int)pY, (int)pW, (int)pH,0,0,1,1);
			}else{
				jwin.drawRect(true, (int)pX, (int)pY, (int)pW, (int)pH,1,0,0,1);
			}
			if(jwin.drawHitButton(" ", pX,pY,pW,pH,1,0,1,0) && clickTimer < 0){
				if(MouseInput.left()){
					window = new InfoWindow();
					clickTimer = 20;
					pressed = true;
				}
			}
			if(pressed){
				window.draw();
			}
			if(System.currentTimeMillis() - time > timeRange){
				outOfRange = true;
			}else{
				outOfRange = false;
			}
			if(saved){
				addToSavedPacks(this);
			}
			if(!pressed){
				if(outOfRange){
					remove(this);
				}
			}
		}
		public void drawSaved(JWindow jwin, int x, int y, int w, int h){
			if(pressed && jwin.drawHitButton(" ", x, y, w, h, 1, 0, 0, 1) && clickTimer < 0){
				pressed = false;
				clickTimer = 20;
			}else if(jwin.drawHitButton(" ", x, y, w, h, 0, 0, 1, 1) && clickTimer < 0){
				window = new InfoWindow();
				pressed = true;
				clickTimer = 20;
			}
			if(pressed && outOfRange){
				window.draw();
			}
		}
		public void removeWindow(){
			pressed = false;
		}
		class InfoWindow extends JWindow{
			
			private double saveTimer = 0;
			InfoWindow(){
				super("Packet Info " + key + Double.toString(time),300 + (int)(Math.random()*1000), 500, 1000, 500);
				
			}
			public void draw(){
				super.draw();
				if(!on){
					removeWindow();
				}
				saveTimer -= Core.rate;
				drawText("Packet " + key, 900, 900, 20, 0, 0, 1, 1);
				drawText("Packet Info In String: " + new String(pack.getData()), 100, 800, 50, 0,0,0,1);
				drawText("Packet Port: " + Integer.toString(pack.getPort()), 100, 700, 50,0,0,0,1);
				drawText("Packet Address: " + pack.getAddress().toString(), 100, 600, 50,0,0,0,1);
				drawText("Time Recieved: " + Double.toString((System.currentTimeMillis()-time)/1000d) +" seconds", 100, 500, 50,0,0,0,1);
				if(drawHitButton("Save", 1830, 900,70,70,0,.4f,1,1)){
					if(!saved && saveTimer < 0){
						saveTimer = 20;
						saved = true;
					}
					if(saved && saveTimer < 0){
						saveTimer = 20;
						saved= false;
					}
				}
			}
		}
	}
}
