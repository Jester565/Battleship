package JInternet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import JBasics.AlertManager;
import pack.Core;

public abstract class InternetCore{
	public static final int PACKET_INFO_LENGTH = 2;
	public static final int DIGIT_AMOUNT = 6;
	public static final int RECIEVE_AMOUNT = 60000;
	public static final int udpRecievePort = 1024 + (int)(Math.random() * 48000);
	public static final int tcpRecievePort = udpRecievePort + 1;
	public static final int udpSendPort = 2650;
	protected User user;
	protected int pingUpdateTimer = 0;
	protected PeerManager peerManager;
	protected InternetDI internetDisplay;
	protected TCPManager tcpManager;
	protected UDPManager udpManager;
	protected ServerList serverList;
	protected boolean serverListOn = false;
	protected boolean connected = false;
	private boolean connecting = false;
	protected InetAddress host = null;
	private Thread tcpThread;
	private Thread udpThread;
	protected double ping = 0;
	protected double curPing = 0;
	protected double maxPing = 15000;
	/**
	 * The lowest level of Internet connectivity. Contains basic routines that can be easily overwritten by its children.
	 */
	public InternetCore(){
		new PackageProcessing();
		createServerList();
		createInternetDI();
		udpManager = new UDPManager(internetDisplay);
		udpThread = new Thread(udpManager);
		udpThread.setName("UDP Thread");
		udpThread.start();
		createUser();
		createPeerManager(user);
		new DataBaseProcessing();
		PackageProcessing.addToKeyRoutines(new KeyA3());
		PackageProcessing.addToKeyRoutines(new KeyA4());
	}
	public synchronized void setConnected(boolean mode){
		connected = mode;
		if(mode == false){
			serverListOn = true;
			connecting = false;
			serverList.refreshAction();
		}
	}
	public UDPManager getUDPManger(){
		return udpManager;
	}
	public synchronized boolean isConnected(){
		return connected;
	}
	protected void createUser(){
		user = new User(this);
	}
	protected void createServerList(){
		serverList = new ServerList();
	}
	protected void createInternetDI(){
		internetDisplay = new InternetDI();
	}
	protected void createPeerManager(User user){
		peerManager = new PeerManager(user);
	}
	public void setUserName(String name){
		user.setName(name);
	}
	/**
	 * Packet containing System.currentTimeMillis() in string form with the key of A0. Meant to be sent to recieve a server list
	 * @return
	 */
	public DatagramPacket getServerNamePack(){
		byte[] data = ByteToObject.stringToBytes("A0", Double.toString(System.currentTimeMillis()));
		return new DatagramPacket(data, data.length);
	}
	public void manage(){
		if(connected){
			curPing += Core.timePassed;
			if(curPing > maxPing){
				pingTooHighResponse();
				System.out.println("ping high");
			}
		}
	}
	/**
	 * Creates a connection with the given address. Calls multipleHostResponse(InetAddress[] hosts) when several servers are found with the address. Calls noSuchHostResponse() when
	 * no servers are found.
	 * @param hostAddress
	 */
	public void connectTo(InetAddress hostAddress){
		host = hostAddress;
		if(host != null){
			for(int i = 0; i < 5; i++){
				send(host, ByteToObject.stringToBytes("A1", user.getName()));
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < 5; i++){
				send(host, ByteToObject.stringToBytes("A1", user.getName()));
			}
			startTCPManager();
		}else{
			noSuchHostResponse();
		}
		connecting = true;
		serverList.setSelectedAddressToNull();
	}
	public PeerManager getPeerManager(){
		return peerManager;
	}
	/**
	 * Creates a connection with the given address name. Calls multipleHostResponse(InetAddress[] hosts) when several servers are found with the address. Calls noSuchHostResponse() when
	 * no servers are found.
	 * @param hostAddress
	 */
	public void connectTo(String hostName){
		InetAddress host = null;
		try {
			host = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(host != null){
			for(int i = 0; i < 5; i++){
				send(host, ByteToObject.stringToBytes("A1", user.getName()));
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < 5; i++){
				send(host, ByteToObject.stringToBytes("A1", user.getName()));
			}
			startTCPManager();
		}else{
			noSuchHostResponse();
		}
		connecting = true;
		serverList.setSelectedAddressToNull();
	}
	/**
	 * Clears all default primitive data types and disposes of all classes in Internet Core
	 */
	@SuppressWarnings("deprecation")
	public void reset(){
		serverListOn = true;
		connected = false;
		host = null;
		tcpThread.stop();
		PackageProcessing.reset();
		serverList = new ServerList();
		internetDisplay = new InternetDI();
		new DataBaseProcessing();
		createUser();
		peerManager = new PeerManager(user);
		PackageProcessing.addToKeyRoutines(new KeyA3());
		PackageProcessing.addToKeyRoutines(new KeyA4());
	}
	private void startTCPManager(){
		tcpManager = new TCPManager(internetDisplay, udpManager, host, this);
		tcpThread = new Thread((Runnable) tcpManager);
		tcpThread.setName("TCP Thread");
		tcpThread.start();
	}
	/**
	 * returns the ping required to disconnect
	 * @return
	 */
	public double getPing(){
		return ping;
	}
	/**
	 * Sends a packet to all devices in the private network using the broadcast IP.
	 * @param pack
	 */
	public void sendBroadcastLan(DatagramPacket pack){
		send(getBroadcastIP(), pack);
		setServerListOn(true);
	}
	private InetAddress getBroadcastIP()
	{
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			
		}
		for(int i = ip.getHostAddress().length(); i >0;i--)
		{
			String s = ip.getHostAddress().substring(i-1,i);
			if(s.equals("."))
			{
				s = ip.getHostAddress().substring(0,i);
				s += "255";
				try {
					ip = InetAddress.getByName(s);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				i = -1;
			}
		}
		return ip;
	}
	public void setServerListOn(boolean on){
		serverListOn = on;
	}
	protected void drawConnectingScreen(){
		
	}
	/**
	 * Draws the server list at the coordinates. Server list is meant to be overridden but in its default state, it displays the multipleHostResponse(InetAddress[] host) 
	 * and is called after sendBroadcastLan().
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawServerList(){
		if(serverListOn){
			serverList.draw(700, 800, 600, 80);
			if(serverList.getSelectedAddress()!=null){
				connectTo(serverList.getSelectedAddress());
			}
			if(connecting){
				drawConnectingScreen();
			}
			if(connected){
				serverListOn = false;
			}
		}
	}
	/**
	 * Response to a high ping. Default response resets InternetCore.
	 */
	protected void pingTooHighResponse(){
		connected = false;
		connecting = false;
		serverListOn = true;
		serverList.setSelectedAddressToNull();
		curPing = 0;
		createPeerManager(user);
		serverList.refreshAction();
		Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "Ping too High. Disconnected.");
	}
	/**
	 * Response when there is no host found when calling connectTo(InetAddress address) or connectTo(String hostname).
	 */
	protected void noSuchHostResponse(){
		Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "There is no server under this name...");
	}
	/**
	 * <-<-<-OBSELETE->->->-> Called when there are multiple hosts for a host name. Default response is to create a server list and send a message to all possible hosts.
	 * @param hosts
	 */
	protected void multipleHostResponse(InetAddress[] hosts){
		for(int i = 0; i < hosts.length; i++){
			send(hosts[i], ByteToObject.doubleToBytes("A0", System.currentTimeMillis()));
		}
		Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "Multiple Hosts Found. Opening list of servers...");
		serverList.clearList();
		serverListOn = true;
	}
	/**
	 * Sends packet to the serverIP. Will not send if serverIP is null.
	 * @param safe - UDP-false  TCP-true
	 * @param pack - pack to send
	 */
	public void send(boolean safe, DatagramPacket pack){
		if(safe)
			tcpManager.send(pack);
		else
			udpManager.send(pack);
	}
	/**
	 * Sends packet to the serverIP. Will not send if serverIP is null.
	 * @param safe
	 * @param bytes
	 */
	public void send(boolean safe, byte[] bytes){
		if(safe)
			tcpManager.send(bytes);
		else
			udpManager.send(bytes);
	}
	/**
	 * Uses UDP to send the packet to the given address.
	 * @param hostAddress
	 * @param pack
	 */
	protected void send(InetAddress hostAddress, DatagramPacket pack){
		udpManager.send(hostAddress, pack);
	}
	/**
	 * Uses UDP to send the bytes to the given address.
	 * @param hostAddress
	 * @param bytes
	 */
	protected void send(InetAddress hostAddress, byte[] bytes){
		udpManager.send(hostAddress, bytes);
	}
	/**
	 * Uses UDP to send the packet to the given hostName.
	 * @param hostAddressName
	 * @param pack
	 */
	protected void send(String hostAddressName, DatagramPacket pack){
		try {
			InetAddress hostAddress = InetAddress.getByName(hostAddressName);
			send(hostAddress, pack);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Uses UDP to send the bytes to the given name.
	 * @param hostAddressName
	 * @param bytes
	 */
	protected void send(String hostAddressName, byte[] bytes){
		try {
			InetAddress hostAddress = InetAddress.getByName(hostAddressName);
			send(hostAddress, bytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	protected ArrayList<Object> basicA3Objects(){
		return null;
	}
	/**
	 * Draws packetTracker based on whether or not packetTrackerMode is on or off
	 */
	public void drawPacketTracker(){
		internetDisplay.manage();
	}
	public class KeyA3 implements Key{
		private String key = "A3";
		public boolean run(String key, DatagramPacket pack){
			if(this.key.equals(key)){
				send(true,ByteToObject.doubleToBytes("A3", ByteToObject.bytesToDouble(pack)));
				curPing = 0;
				ping = ByteToObject.bytesToDouble(pack)*2;
				return true;
			}
			return false;
		}
		@Override
		public String getKey(){
			return key;
		}
		public String getDescription(){
			return new String(key + " :recieves a ping sends back a pong located in InternetCore");
		}
	}
	public class KeyA4 implements Key{
		private String key = "A4";
		public boolean run(String key, DatagramPacket pack){
			if(this.key.equals(key)){
				Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, ByteToObject.bytesToString(pack));
				reset();
				return true;
			}
			return false;
		}
		@Override
		public String getKey(){
			return key;
		}
		public String getDescription(){
			return new String(key + ": Hello");
		}
	}
}
