package JInternet;

import java.net.DatagramPacket;
import java.util.ArrayList;

import JBasics.Button;
import JBasics.JWindow;
import JBasics.ShapeRenderer;
import JBasics.TextField;
import pack.Core;
import pack.KeyInput;

public class InternetDI {

	private ArrayList<PacketUI> packetUIs;
	private ShapeRenderer sr;
	private int x = 1700;
	private int y = 100;
	private double removeTimer = 0;
	private int onTimer = 0;
	private boolean on = false;
	private boolean sendTimeLine = false;
	private TextField field;
	private ArrayList<PackTracker> sendTrackers;
	public InternetDI(){
		sendTrackers = new ArrayList<PackTracker>();
		packetUIs = new ArrayList<PacketUI>();
		sr = new ShapeRenderer();
		field = new TextField(500,500,500,500);
	}
	public void refresh(){
		packetUIs.clear();
		for(Key cpk: PackageProcessing.getProcessKeys()){
			packetUIs.add(new PacketUI(cpk.getDescription().substring(0,2),cpk.getDescription()));
		}
		packetUIs = arrangePackets();
	}
	public void sendTimeLineSwitch(){
		if(sendTimeLine && onTimer < 0){
			sendTimeLine = false;
			onTimer = 20;
		}
		if(!sendTimeLine && onTimer < 0){
			sendTimeLine = true;
			onTimer = 20;
		}
	}
	private ArrayList<PacketUI> arrangePackets(){
		ArrayList<PacketUI> arranged = new ArrayList<PacketUI>();
		for(int i = 0; i < packetUIs.size(); i++){
			addToArranged(arranged, packetUIs.get(i));
		}
		return arranged;
	}
	private void addToArranged(ArrayList<PacketUI> arranged, PacketUI pUI){
		if(arranged.size() == 0){
			arranged.add(pUI);
		}else{
			for(int i = 0; i < arranged.size(); i ++){
				//changes here
				if(i == 0 && pUI.getKey().compareTo(arranged.get(i).getKey())<0){
					arranged.add(i,pUI);
					break;
				}else if(i > 0 && arranged.get(i-1).getKey().compareTo(pUI.getKey()) < 0 && arranged.get(i).getKey().compareTo(pUI.getKey()) > 0){
					arranged.add(i,pUI);
					break;
				}else if(i == arranged.size()-1){
					arranged.add(pUI);
					break;
				}
			}
		}
	}
	public void manage(){
		onTimer--;
		if(KeyInput.keyPressed("F1")){
			if(on && onTimer < 0){
				on = false;
				onTimer = 20;
			}
			if(!on && onTimer < 0){
				on = true;
				onTimer = 20;
			}		
		}
		if(on)
			draw();
	}
	private void draw(){
		removeTimer -= Core.rate;
		int y = this.y;
		for(int i = packetUIs.size()-1; i >= 0; i--){
			if(y > 90)
				packetUIs.get(i).draw(sr, x, y, 30, 30);
			y+=32;
			if(y > 900)
				break;
		}
		if(sendTimeLine){
			drawSendTimeLine();
		}
		if(Button.hitDrawnButton(sr, "UP", x, 950, 60, 40, 1, 0, 1, .4f) && onTimer < 0){
			this.y += 300;
			onTimer = 20;
		}
		if(Button.hitDrawnButton(sr, "DOWN", x, 5, 60, 40, 1, 0, 1, .4f) && onTimer < 0){
			this.y -= 300;
			onTimer = 20;
		}
		if(Button.hitDrawnButton(sr, "Refresh", 10, 950, 70, 40, 1, 0, 0, .4f) && onTimer < 0){
			refresh();
			onTimer = 20;
		}
		if(Button.hitDrawnButton(sr, "Send TimeLine", 10, 800, 100, 40, 0, 1, 1, .4f) && onTimer < 0){
			sendTimeLineSwitch();
		}
		sr.drawText("y: " + Integer.toString(y), 20, 20);
	}
	private void drawSendTimeLine(){
		field.draw(true, "Key:", 100, 200, 100, 30, 1, 1, 1, 1);
		if(field.finalMessage != null){
			sendTrackers.add(new PackTracker(field.finalMessage));
			field.reset();
		}
		sr.drawRectangle(true, 200, 200, 1450, 200,1,0,0,.4f);
		int keyShownX = 200;
		for(int i = 0; i < sendTrackers.size(); i++){
			sendTrackers.get(i).drawTimeLine(200, 200, 1450, 200, 20000);
			keyShownX += 40;
			sr.drawText(sendTrackers.get(i).getKey(), keyShownX, 170,30,0,0,0,1);
			if(Button.hitButton(keyShownX, 170, 30, 30) && removeTimer < 0){
				sendTrackers.remove(i);
				removeTimer = 20;
				break;
			}
		}
		if(Button.hitDrawnButton(sr, "Wipe", 100, 300, 100, 100, 1, 0, 0, .6f))
			sendTrackers.clear();
	}
	public void addToPack(String key, DatagramPacket pack){
		for(PackTracker tracker: sendTrackers){
			if(tracker.getKey().equals(key)){
				tracker.addToPackInfo(pack);
				break;
			}
		}
	}
}
class PacketUI{
	private String key;
	private boolean trackPack = false;
	private int trackPackTimer = 0;
	private String description;
	private boolean pressed = false;
	private int pressedTimer = 0;
	private PacketWindow pWindow;
	PacketUI(String key, String description){
		this.key = key;
		this.description = description;
	}
	public String getKey(){
		return key;
	}
	public void draw(ShapeRenderer sr, int x, int y, int w, int h){
		pressedTimer--;
		if(Button.overButton(x, y, w, h))
			sr.drawText(true, description, 10, 40, 20, 0, 0, 0, .7f);
				if(pressed == false && Button.hitDrawnButton(sr, key, x, y, w, h, .7f, .7f, .7f, .8f) && pressedTimer < 0){
					pressed = true;
					pWindow = new PacketWindow();
					pressedTimer = 20;
				}
				if(pressed && Button.hitDrawnButton(sr, key, x, y, w, h, 0, 1, 0, .8f) && pressedTimer < 0){
					pressed = false;
					pressedTimer = 20;
				}
		if(pressed){
			pWindow.draw();
		}
	}
	class PacketWindow extends JWindow{
		
		PacketWindow(){
			super("Packet: " + key, 500, 350, 1000, 300);
		}
		public void draw(){
			super.draw();
			trackPackTimer--;
			if(drawHitButton("Track", 900, 10, 200, 200, 0, 0, 1, .7f)){
				if(trackPack && trackPackTimer < 0){
					trackPack = false;
					trackPackTimer = 30;
				}
				if(!trackPack && trackPackTimer < 0){
					trackPack = true;
					trackPackTimer = 30;
					PackageProcessing.addPackTracker(key);
				}
			}
			if(trackPack){
				for(PackTracker track:PackageProcessing.getPackTrackers()){
					if(track.getKey().equals(key)){
						track.drawSavedPacks(this, 20, 300, 50, 400);
						track.drawTimeLine(this, 20, 200, 1900, 500, 20000);
					}
				}
			}
			drawText(description, 10, 20, 30,1,0,1,1);
			if(!on){
				pressed = false;
			}
		}
	}
}

