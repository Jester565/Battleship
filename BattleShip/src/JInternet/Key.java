package JInternet;

import java.net.DatagramPacket;

public interface Key {
	public boolean run(String key, DatagramPacket pack);
	public String getDescription();
	public String getKey();
}
