package JInternet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;

public class PeerManager {

	private ArrayList<Peer> peers;
	private Key keyJ0;
	private Key keyD0;
	private Key keyA5;
	private User user;
	public PeerManager(User user){
		peers = new ArrayList<Peer>();
		keyJ0 = new KeyJ0();
		keyD0 = new KeyD0();
		keyA5 = new KeyA5();
		this.user = user;
		PackageProcessing.addToKeyRoutines(keyA5);
		PackageProcessing.addToKeyRoutines(keyJ0);
		PackageProcessing.addToKeyRoutines(keyD0);
	}
	public void dispose(){
		PackageProcessing.removeKey(keyJ0);
		PackageProcessing.removeKey(keyD0);
		PackageProcessing.removeKey(keyA5);
	}
	public void draw(){
		
	}
	public synchronized void addPeer(DatagramPacket pack){
		peers.add(new Peer(pack));
	}
	public synchronized ArrayList<Peer> getPeers(){
		return peers;
	}
	public Peer getPeer(InetAddress address, int port){
		for(Peer p: peers){
			if(p.equals(address, port))
				return p;
		}
		return null;
	}
	public Peer getPeer(String name){
		for(Peer p: peers){
			if(p.name.equals(name))
				return p;
		}
		return null;
	}
	public boolean containsPeer(Peer p){
		for(Peer listPeer: peers){
			if(p.equals(listPeer))
				return true;
		}
		return false;
	}
	public Peer getPeer(int id){
		for(Peer p: peers){
			if(p.id == id)
				return p;
		}
		try {
			String ids = new String();
			if(peers.size() > 0){
				for(Peer p: peers){
					ids += p.getID() + " ";
				}
			}else{
				ids = "well there are no ID's";
			}
			throw new Exception("id " + id + " was not found.  Our current IDs are " + ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public synchronized void removePeer(int id){
		for(int i = 0; i < peers.size(); i++){
			if(peers.get(i).getID() == id){
				peers.remove(i);
				return;
			}
		}
		System.out.println("Peer with id " + id + " was not found and could not be removed");
	}
	public void sortByName(){
		peers.sort(new CompareByName());
	}
	private class CompareByName implements Comparator<Peer>{

		@Override
		public int compare(Peer arg0, Peer arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
		
	}
	public class Peer{
		
		private String name;
		private InetAddress address;
		private int port;
		private int id;
		public Peer(DatagramPacket pack){
			address = pack.getAddress();
			port = pack.getPort();
			ArrayList<Object> setUpObjs = ByteToObject.bytesToObjects(pack);
			setUp(setUpObjs);
			System.out.println("Peer " + name + " was added and his id is " + id);
		}
		public void draw(){
			
		}
		public String getName(){
			return name;
		}
		public InetAddress getAddress(){
			return address;
		}
		public int getPort(){
			return port;
		}
		public int getID(){
			return id;
		}
		public boolean equals(int id){
			return (this.id == id);
		}
		public boolean equals(Object obj){
			Peer p = (Peer)obj;
			return (p.getID() == id);
		}
		public boolean equals(InetAddress address, int port){
			if(this.address.equals(address) && this.port == port)
				return true;
			return false;
		}
		protected void setUp(ArrayList<Object> setUpObjs){
			name = (String)setUpObjs.get(0);
			id = (Integer)setUpObjs.get(1);
		}
	}
	public class KeyA5 implements Key{
		private String key = "A5";
		public KeyA5() {
			
		}
		@Override
		public String getKey(){
			return key;
		}
		public boolean run(String key, DatagramPacket pack) {
			if(this.key.equals(key)){
				int id = ByteToObject.bytesToInt(pack);
				removePeer(id);
				return true;
			}
			return false;
		}
		public String getDescription() {
			return key + " deletes a peer based on the id number";
		}
		
	}
	public class KeyJ0 extends PeerKey{
		public KeyJ0(){
			super("J0");
		}
		public String getDescription() {
			return new String(key + " Takes objects and creates a peer");
		}
		@Override
		public String getKey(){
			return key;
		}
		public void routine(int id, DatagramPacket pack) {
			addPeer(pack);
		}
	}
	public class KeyD0 extends PeerKey{
		public KeyD0(){
			super("D0");
		}
		@Override
		public String getKey(){
			return key;
		}
		public String getDescription() {
			return new String(key + " Takes objects and creates a peer");
		}

		public void routine(int id, DatagramPacket pack) {
			addPeer(pack);
			user.sendPlayerData(id);
		}
	}
}
