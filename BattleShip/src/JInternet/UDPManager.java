package JInternet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPManager implements Runnable{

	private DatagramSocket socket;
	private DatagramPacket packet;
	private InetAddress address;
	private InternetDI idi;
	public UDPManager(InternetDI idi){
		try {
			socket = new DatagramSocket(InternetCore.udpRecievePort);
			socket.setReuseAddress(true);
			this.idi = idi;
		} catch (SocketException e) {
			e.printStackTrace();
		}
		resetPacket();
	}
	public void setAddress(InetAddress address){
		this.address = address;
	}
	
	public void run(){
		while(true){
			try {
				socket.receive(packet);
				//getSendInfo(packet);
				byte[] data = packet.getData();
				byte[] sizeData = new byte[InternetCore.DIGIT_AMOUNT];
				for(int i = 0; i < InternetCore.DIGIT_AMOUNT; i++){
					sizeData[i] = data[i];
				}
				int size = Integer.valueOf(new String(sizeData));
				byte[] cutData = new byte[size];
				for(int i = 0; i < size;i++){
					cutData[i] = data[i + InternetCore.DIGIT_AMOUNT];
				}
				packet.setData(cutData);
				packet.setLength(cutData.length);
				PackageProcessing.processPackage(packet);
				resetPacket();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void getSendInfo(DatagramPacket pack){
		byte[]data = pack.getData();
		int addressPortLength = new Integer(data[0]);
		byte[]address = ByteToObject.getData(data, 1, addressPortLength + 1);
		byte[] port = ByteToObject.getData(data, addressPortLength + 1, addressPortLength + 6);
		try {
			InetAddress senderAddress = InetAddress.getByAddress(address);
			pack.setAddress(senderAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pingString = new String(port);
		int senderPort = Integer.valueOf(pingString);
		data = ByteToObject.removeData(data, 0, addressPortLength + 6);
		pack.setData(data);
		pack.setLength(data.length);
		pack.setPort(senderPort);
	}
	public void send(InetAddress address, byte[] bytes){
		bytes = modifyData(bytes);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, InternetCore.udpSendPort);
		idi.addToPack(new String(packet.getData()).substring(InternetCore.DIGIT_AMOUNT,InternetCore.DIGIT_AMOUNT+2), packet);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(InetAddress address, DatagramPacket pack){
		send(address, pack.getData());
	}
	public void send(DatagramPacket pack){
		send(address, pack.getData());
	}
	public void send(byte[] bytes){
		send(address, bytes);
	}
	private byte[] modifyData(byte[] bytes){
		byte[] data = new byte[InternetCore.DIGIT_AMOUNT + bytes.length];
		String s = new String(Integer.toString(bytes.length));
		while(s.length() < InternetCore.DIGIT_AMOUNT){
			s = "0" + s;
		}
		byte[] length = s.getBytes();
		for(int i = 0; i < data.length; i++){
			if(i < length.length)
				data[i] = length[i];
			else if(i >= length.length)
				data[i] = bytes[i - InternetCore.DIGIT_AMOUNT];
		}
		return data;
	}
	private void resetPacket(){
		byte[] data = new byte[InternetCore.RECIEVE_AMOUNT];
		packet = new DatagramPacket(data,data.length);
	}
}
