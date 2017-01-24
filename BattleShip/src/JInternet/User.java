package JInternet;

import java.net.DatagramPacket;
import java.util.ArrayList;


public class User {

	private static String name;
	private Key keyA1;
	private static int id;
	private static InternetCore ic;
	public User(InternetCore ic){
		this.ic = ic;
		name = "Client";
		id = 0;
		keyA1 = new KeyA1();
		PackageProcessing.addToKeyRoutines(keyA1);
	}
	public void dispose(){
		PackageProcessing.removeKey(keyA1);
	}
	public User(String name, int id){
		
	}
	public String getName(){
		return name;
	}
	public int getID(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setID(int id){
		this.id = id;
		sendPlayerData();
	}
	public void sendPlayerData(){
		ArrayList<Object> objs = new ArrayList<Object>();
		playerSendSetup(objs);
		ic.send(true,ByteToObject.objectsToBytes("D0", objs));
	}
	public void sendPlayerData(int id){
		ArrayList<Object> objs = new ArrayList<Object>();
		playerSendSetup(objs);
		ic.send(true,ByteToObject.addPeerInfo(ByteToObject.objectsToBytes("J0",objs), id));
	}
	protected void playerSendSetup(ArrayList<Object> objs){
		objs.add(name);
		objs.add(new Integer(id));
	}
	public class KeyA1 implements Key{
		String key = "A1";
		public boolean run(String key, DatagramPacket pack){
			if(this.key.equals(key)){
				ArrayList<Object> setUp = ByteToObject.bytesToObjects(pack);
				setName((String)setUp.get(0));
				setID((Integer)setUp.get(1));
				DataBaseProcessing.callDataBaseReciever("BASICS", null);
				return true;
			}
			return false;
		}
		public String getDescription(){
			return new String(key + " Contains modified name. After name is processed an A2 key is sent.");
		}
		@Override
		public String getKey(){
			return key;
		}
	}
}
