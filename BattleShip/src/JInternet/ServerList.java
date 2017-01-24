package JInternet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import pack.Core;
import JBasics.Button;
import JBasics.ShapeRenderer;
import JBasics.TextField;
import Launcher.Main;

public class ServerList {

	private ArrayList<Server> servers;
	private InetAddress selectedAddress = null;
	private ShapeRenderer sr;
	private TextField ip;
	private Key keyA0;
	private double connectErrorTimer = 0;
	/**
	 * Creates a list of servers based on names recieved from key A0.
	 */
	public ServerList(){
		ip = new TextField(true,0,0,0,0);
		sr = new ShapeRenderer();
		servers = new ArrayList<Server>();
		keyA0 = new KeyA0();
		PackageProcessing.addToKeyRoutines(keyA0);
	}
	public void dispose(){
		PackageProcessing.removeKey(keyA0);
	}
	/**
	 * Adds a server to the list by removing the server name from the packet.
	 * @param packet
	 */
	public synchronized void addToList(DatagramPacket packet){
		if(!inList(packet)){
			ArrayList<String> serverInfo = ByteToObject.bytesToStringArray(packet);
			String serverName = serverInfo.get(0);
			double serverPing = System.currentTimeMillis() - Double.valueOf(serverInfo.get(1));
			servers.add(new Server(serverName + "  Ping: " + Double.toString(serverPing), packet.getAddress(), packet.getPort()));
		}
	}
	private boolean inList(DatagramPacket pack){
		for(Server serv: servers){
			if(serv.equals(pack)){
				return true;
			}
		}
		return false;
	}
	public void clearList(){
		servers.clear();
	}
	public InetAddress getSelectedAddress(){
		return selectedAddress;
	}
	public void setSelectedAddressToNull(){
		selectedAddress = null;
	}
	/**
	 * Draws the server list at the given coordinates. Displays servers starting from the top and going further down. Calls drawBackGround(int x, int y, int w, int h) and other
	 * overridable methods.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public synchronized void draw(int x, int y, int w, int h){
		connectErrorTimer -= Core.rate;
		drawBackGround(x, y - (servers.size() + 1) * h, w, (servers.size() + 2) * h + h/2);
		for(Server server:servers){
			server.draw(x, y, w, h);
			y -= h;
		}
		if(refreshButton(x + w/4, y - h, w/2, (int)(h * 1.2))){
			refreshAction();
		}
	}
	public void directConnectManager(int x, int y, int w, int h,float r, float g, float b, float a,float br, float bg, float bb, float ba){
		ip.draw(true, " ", x, y, w, h, r, g, b, a);
		if(Button.hitDrawnButton(sr, "Send Request", x + w/4, y - h/1.7d, w/2, h/2, br, bg, bb, ba)){
			ip.setFinalMessage();
		}
		if(ip.finalMessage != null){
			refreshAction();
			Main.getIMananager().send(ip.finalMessage, Main.getIMananager().getServerNamePack());
			ip.resetFinalMessage();
		}
	}
	/**
	 * Sets what to draw behind the serverList.
	 * @param x
	 * @param y
	 * @param w
	 * @param h - Determined by (serverList.size() + 2) * h.
	 */
	protected void drawBackGround(int x, int y, int w, int h){
		sr.drawRectangle(true, x, y, w, h,0,0,1,.3f);
	}
	/**
	 * Set what each button looks like. Default is a simple rectangle button.
	 * @param name
	 * @param ip
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	protected boolean serverButton(String name, int x, int y, int w, int h){
		if(Button.hitDrawnButton(sr, name +" "+ip, x, y, w, h, .1f, .1f, .8f, 1))
			return true;
		return false;
	}
	protected boolean refreshButton(int x, int y, int w, int h){
		if(Button.hitDrawnButton(sr, "Refresh", x, y, w, h, 1, 0, 1, 1))
			return true;
		return false;
	}
	/**
	 * Action when refreshButton(String name, String ip, int x, int w, int h is true.
	 */
	protected void refreshAction(){
		clearList();
	}
	class Server{
		String name;
		InetAddress ip;
		int port = 0;
		Server(String name , InetAddress ip, int port){
			this.name = name;
			this.ip = ip;
			this.port = port;
		}
		void draw(int x, int y, int w, int h){
			if(serverButton(name, x, y, w, h)){
				serverClicked();
			}
		}
		public boolean equals(DatagramPacket pack){
			if(pack.getAddress().equals(ip) && pack.getPort() == port){
				return true;
			}
			return false;
		}
		protected void serverClicked(){
			if(connectErrorTimer < 0){
				connectErrorTimer = 20;
				selectedAddress = ip;
			}
		}
	}
	class KeyA0 implements Key{
		String key = "A0";
		public boolean run(String key, DatagramPacket pack){
			if(this.key.equals(key)){
				addToList(pack);
				return true;
			}
			return false;
		}
		public String getDescription(){
			return new String(key + " Recieves server name and the time the request was send in millis");
		}
		@Override
		public String getKey(){
			return key;
		}
	}
}

