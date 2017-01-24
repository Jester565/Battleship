package JInternet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import JBasics.AlertManager;
import pack.Core;

public class TCPManager implements Runnable{

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private InternetDI idi;
	private InternetCore ic;
	public TCPManager(InternetDI idi, UDPManager udp,InetAddress hostName,InternetCore ic){
		try {
			this.ic = ic;
			this.idi = idi;
			socket = new Socket(hostName,InternetCore.tcpRecievePort+1, InetAddress.getLocalHost(),InternetCore.tcpRecievePort);
			socket.setReuseAddress(true);
			OutputStream outStream = (socket.getOutputStream());
			out = new DataOutputStream(outStream);
			InputStream inStream = socket.getInputStream();
			in = new DataInputStream(inStream);
			udp.setAddress(socket.getInetAddress());
			ic.setConnected(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			ic.setConnected(false);
			Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "Connection Error! See pg. 3 of the Server Screen tutorial.");
		} catch (IOException e) {
			e.printStackTrace();
			ic.setConnected(false);
			Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "Connection Error! See pg. 3 of the Server Screen tutorial.");
		}
	}
	public void run(){
		while(true){
			try {
				byte[] lengthData = new byte[InternetCore.DIGIT_AMOUNT];
				in.read(lengthData,0,InternetCore.DIGIT_AMOUNT);
				byte[] data = new byte[Integer.valueOf(new String(lengthData))];
				in.read(data,0,data.length);
				DatagramPacket packet = new DatagramPacket(data, data.length, socket.getInetAddress(), socket.getPort());
				PackageProcessing.processPackage(packet);
			} catch (IOException e) {
				if(!ic.isConnected()){
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			} catch (NullPointerException e){
				e.printStackTrace();
				ic.setConnected(false);
				Core.addAlert(AlertManager.MIDDLE_OF_SCREEN, "Connection Error! See pg. 3 of the Server Screen tutorial.");
				break;
			}
		}
	}
	public OutputStream getOuputStream(){
		return out;
	}
	public void send(DatagramPacket packet){
		byte[] data = packet.getData();
		data = modifyData(data);
		try {
			idi.addToPack(new String(data).substring(InternetCore.DIGIT_AMOUNT,InternetCore.DIGIT_AMOUNT+2), packet);
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(byte[] data){
		data = modifyData(data);
		try {
			out.write(data);
			idi.addToPack(new String(data).substring(InternetCore.DIGIT_AMOUNT,InternetCore.DIGIT_AMOUNT+2), new DatagramPacket(data, data.length, socket.getInetAddress(),socket.getPort()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
