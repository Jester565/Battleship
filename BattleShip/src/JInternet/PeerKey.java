package JInternet;

import java.net.DatagramPacket;

public abstract class PeerKey implements Key{
	protected String key;
	public PeerKey(String key){
		this.key = key;
	}
	public boolean run(String key, DatagramPacket pack){
		if(this.key.equals(key)){
			int id = new Integer(pack.getData()[0]);
			ByteToObject.removeData(pack, 0, 1);
			routine(id, pack);
			return true;
		}
		return false;
	}
	public abstract void routine(int id, DatagramPacket pack);
}