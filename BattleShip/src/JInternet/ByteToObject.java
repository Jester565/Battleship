package JInternet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import JInternet.PeerManager.Peer;

public abstract class ByteToObject {

	public ByteToObject(){
		
	}
	public static ArrayList<ArrayList<Integer>> bytesTo2DArrayList(DatagramPacket pack){
		byte[] bytes = pack.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream load = new ObjectInputStream(inputStream);
			try {
				//String s = (String)load.readObject();
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Integer>> list = (ArrayList<ArrayList<Integer>>)load.readObject();
				load.close();
				return list;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] ArrayList2DToBytes(String key, ArrayList<ArrayList<Integer>> list){
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream writer = new ObjectOutputStream(outputStream);
			//writer.writeObject(key);
			writer.writeObject(list);
			writer.close();
		} catch (IOException e) {
			
		}
		byte[] keyBytes = key.getBytes();
		byte[] arrayList = ((ByteArrayOutputStream)outputStream).toByteArray();
		byte[] totalBytes = new byte[keyBytes.length+arrayList.length];
		for (int i = 0; i < totalBytes.length; ++i){
		    if(i < keyBytes.length){
		    	totalBytes[i] = keyBytes[i];
		    }else{
		    	totalBytes[i] = arrayList[i-keyBytes.length];
		    }
		}
		return totalBytes;
	}
	public static ArrayList<ArrayList<Double>> bytesTo2DArrayListDouble(DatagramPacket pack){
		byte[] bytes = pack.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream load = new ObjectInputStream(inputStream);
			try {
				//String s = (String)load.readObject();
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Double>> list = (ArrayList<ArrayList<Double>>)load.readObject();
				load.close();
				return list;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] ArrayList2DToBytesDouble(String key, ArrayList<ArrayList<Double>> list){
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream writer = new ObjectOutputStream(outputStream);
			//writer.writeObject(key);
			writer.writeObject(list);
			writer.close();
		} catch (IOException e) {
			
		}
		byte[] keyBytes= key.getBytes();
		byte[] arrayList = ((ByteArrayOutputStream)outputStream).toByteArray();
		byte[] totalBytes = new byte[keyBytes.length+arrayList.length];
		for (int i = 0; i < totalBytes.length; ++i){
		    if(i < keyBytes.length){
		    	totalBytes[i] = keyBytes[i];
		    }else{
		    	totalBytes[i] = arrayList[i-keyBytes.length];
		    }
		}
		return totalBytes;
	}
	public static ArrayList<Double> bytesToArrayListDouble(DatagramPacket pack){
		byte[] bytes = pack.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream load = new ObjectInputStream(inputStream);
			try {
				//String s = (String)load.readObject();
				@SuppressWarnings("unchecked")
				ArrayList<Double> list = (ArrayList<Double>)load.readObject();
				load.close();
				return list;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] arrayListToBytesDouble(String key, ArrayList<Double> list){
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream writer = new ObjectOutputStream(outputStream);
			//writer.writeObject(key);
			writer.writeObject(list);
			writer.close();
		} catch (IOException e) {
			
		}
		byte[] keyBytes = key.getBytes();
		byte[] arrayList = ((ByteArrayOutputStream)outputStream).toByteArray();
		byte[] totalBytes = new byte[keyBytes.length+arrayList.length];
		for (int i = 0; i < totalBytes.length; ++i){
		    if(i < keyBytes.length){
		    	totalBytes[i] = keyBytes[i];
		    }else{
		    	totalBytes[i] = arrayList[i-keyBytes.length];
		    }
		}
		return totalBytes;
	}
	public static ArrayList<Float> bytesToArrayListFloat(DatagramPacket pack){
		byte[] bytes = pack.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream load = new ObjectInputStream(inputStream);
			try {
				//String s = (String)load.readObject();
				@SuppressWarnings("unchecked")
				ArrayList<Float> list = (ArrayList<Float>)load.readObject();
				load.close();
				return list;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] arrayListToBytesFloat(String key, ArrayList<Float> list){
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream writer = new ObjectOutputStream(outputStream);
			writer.writeObject(list);
			writer.close();
		} catch (IOException e) {
			
		}
		byte[] keyBytes= key.getBytes();
		byte[] arrayList = ((ByteArrayOutputStream)outputStream).toByteArray();
		byte[] totalBytes = new byte[keyBytes.length+arrayList.length];
		for (int i = 0; i < totalBytes.length; ++i){
		    if(i < keyBytes.length){
		    	totalBytes[i] = keyBytes[i];
		    }else{
		    	totalBytes[i] = arrayList[i-keyBytes.length];
		    }
		}
		return totalBytes;
	}
	public static ArrayList<Integer> bytesToArrayList(DatagramPacket pack){
		byte[] bytes = pack.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream load = new ObjectInputStream(inputStream);
			try {
				@SuppressWarnings("unchecked")
				ArrayList<Integer> list = (ArrayList<Integer>)load.readObject();
				load.close();
				return list;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] arrayListToBytes(String key, ArrayList<Integer> list){
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream writer = new ObjectOutputStream(outputStream);
			writer.writeObject(list);
			writer.close();
		} catch (IOException e) {
			
		}
		byte[] keyBytes = key.getBytes();
		byte[] arrayList = ((ByteArrayOutputStream)outputStream).toByteArray();
		byte[] totalBytes = new byte[keyBytes.length+arrayList.length];
		for (int i = 0; i < totalBytes.length; ++i){
		    if(i < keyBytes.length){
		    	totalBytes[i] = keyBytes[i];
		    }else{
		    	totalBytes[i] = arrayList[i-keyBytes.length];
		    }
		}
		return totalBytes;
	}
	public static String bytesToString(DatagramPacket pack){
		String s = new String(pack.getData());
		return s;
	}
	public static ArrayList<String> bytesToStringArray(DatagramPacket pack){
		String s = new String(pack.getData());
		ArrayList<String> strings = new ArrayList<String>();
		while(s.indexOf("~")!=-1){
			int index = s.indexOf("~");
			strings.add(s.substring(0, index));
			s = s.substring(index + 1);
		}
		strings.add(s);
		return strings;
	}
	public static byte[] stringToBytes(String key, String message){
		return new String(key + message).getBytes();
	}
	public static int bytesToInt(DatagramPacket pack){
		return Integer.parseInt(bytesToString(pack));
	}
	public static byte[] intToBytes(String key, int num){
		return new String(key+Integer.toString(num)).getBytes();
	}
	public static double bytesToDouble(DatagramPacket pack){
		return Double.parseDouble(bytesToString(pack));
	}
	public static byte[] doubleToBytes(String key, double num){
		return new String(key+Double.toString(num)).getBytes();
	}
	public static byte[] objectsToBytes(String key, String locString, ArrayList<Object> objs){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] keyData = key.getBytes();
		try {
			ObjectOutputStream oos = new ObjectOutputStream (baos);
			if(locString!=null){
				oos.writeObject(locString);
			}
			if(objs!=null){
				for(int i = 0; i < objs.size(); i++){
					oos.writeObject(objs.get(i));
				}
			}
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] objectBytes = baos.toByteArray();
		byte[] total = new byte[objectBytes.length + keyData.length];
		for(int i = 0; i < total.length; i++){
			if(i < keyData.length)
				total[i] = keyData[i];
			else
				total[i] = objectBytes[i-keyData.length];
		}
		return total;
	}
	public static byte[] objectsToBytes(String key, ArrayList<Object> objs){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] keyData = key.getBytes();
		try {
			ObjectOutputStream oos = new ObjectOutputStream (baos);
			if(objs!=null){
				for(int i = 0; i < objs.size(); i++){
					oos.writeObject(objs.get(i));
				}
			}
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] objectBytes = baos.toByteArray();
		byte[] total = new byte[objectBytes.length + keyData.length];
		for(int i = 0; i < total.length; i++){
			if(i < keyData.length)
				total[i] = keyData[i];
			else
				total[i] = objectBytes[i-keyData.length];
		}
		return total;
	}
	public static ArrayList<Object> bytesToObjects(DatagramPacket pack){
		return bytesToObjects(pack.getData());
	}
	public static ArrayList<Object> bytesToObjects(byte[] data){
		ArrayList<Object> objs = new ArrayList<Object>();
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			while(true){
				objs.add(ois.readObject());
			}
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
		try {
			ois.close();
			bais.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objs;
	}
	public static void addPeersInfo(DatagramPacket pack, List<Peer> peers){
		byte[] newData = addPeersInfo(pack.getData(), peers);
		pack.setData(newData);
		pack.setLength(newData.length);
	}
	public static byte[] intToFiveByteStringForm(int num){
		String s = Integer.toString(num);
		while(s.length() < 5){
			s = "0"+s;
		}
		return s.getBytes();
	}
	public static byte[] addPeersInfo(byte[] data, List<Peer> peers){
		byte[] newData = new byte[peers.size() + data.length + 1];
		for(int i = 0; i < newData.length; i++){
			if(i < InternetCore.PACKET_INFO_LENGTH)
				newData[i] = data[i];
			else if(i < InternetCore.PACKET_INFO_LENGTH + peers.size())
				newData[i] = new Integer(peers.get(i - InternetCore.PACKET_INFO_LENGTH).getID()).byteValue();
			else if(i <= InternetCore.PACKET_INFO_LENGTH + peers.size())
				newData[i] = new Integer(-1).byteValue();
			else
				newData[i] = data[i + InternetCore.PACKET_INFO_LENGTH + peers.size() + 1];
		}
		return newData;
	}
	public static byte[] byteToByteList(byte b){
		byte[] data = new byte[1];
		data[0] = b;
		return data;
	}
	public static void addPeerInfo(DatagramPacket pack, int peerID){
		addData(pack, ByteToObject.byteToByteList(new Integer(peerID).byteValue()),InternetCore.PACKET_INFO_LENGTH);
	}
	public static void addPeerInfo(DatagramPacket pack, Peer peers){
		addPeerInfo(pack,peers.getID());
	}
	public static byte[] addPeerInfo(byte[] data, int peerID){
		return addData(data, ByteToObject.byteToByteList(new Integer(peerID).byteValue()),InternetCore.PACKET_INFO_LENGTH);
	}
	public static byte[] addPeerInfo(byte[] data, Peer peers){
		return addPeerInfo(data, peers.getID());
	}
	public static void addData(DatagramPacket pack, byte[] data, int index){
		byte[] total = addData(pack.getData(), data, 2);
		pack.setData(total);
		pack.setLength(total.length);
	}
	public static byte[] getData(byte[] data, int index1, int index2){
		byte[]isolatedData = new byte[index2 - index1];
		for(int i = 0; i < isolatedData.length; i++){
			isolatedData[i] = data[i + index1];
		}
		return isolatedData;
	}
	public static byte[] addData(byte[] mainData, byte[] data, int index){
		byte[] total = new byte[mainData.length + data.length];
		for(int i = 0; i < total.length; i++){
			if(i < index)
				total[i] = mainData[i];
			else if(i < index + data.length)
				total[i] = data[i-index];
			else
				total[i] = mainData[i - data.length];
		}
		return total;
	}
	public static byte[] addData(byte[] data1, byte[] data2){
		return addData(data1,data2,data1.length);
	}
	public static byte[] mergeData(byte[] data1, byte[] data2){
		byte[] total = new byte[data1.length + data2.length];
		for(int i = 0; i < total.length; i++){
			if(i < data1.length)
				total[i] = data1[i];
			else
				total[i] = data2[i-data1.length];
		}
		return total;
	}
	public static byte[] removeData(byte[] data, int index1, int index2){
		byte[] total = new byte[index1 + (data.length - index2)];
		for(int i = 0; i < data.length; i++){
			if(i < index1)
				total[i] = data[i];
			else if(i >= index2)
				total[i - (index2 - index1)] = data[i];
		}
		return total;
	}
	public static void removeData(DatagramPacket pack, int index1, int index2){
		byte[] data = removeData(pack.getData(), index1, index2);
		pack.setData(data);
		pack.setLength(data.length);
	}
}
