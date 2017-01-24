package JInternet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class DataBaseProcessing {

	protected static ArrayList<DataBaseReciever> dataBaseRecievers;
	protected Key keyA2;
	public DataBaseProcessing(){
		dataBaseRecievers = new ArrayList<DataBaseReciever>();
		keyA2 = new KeyA2();
		PackageProcessing.addToKeyRoutines(keyA2);
	}
	public static synchronized void addDataBaseReciever(DataBaseReciever db){
		dataBaseRecievers.add(db);
	}
	public static synchronized void callDataBaseReciever(String type, ArrayList<Object> objs){
		for(DataBaseReciever db: dataBaseRecievers){
			if(db.processDataBase(type, objs)){
				return;
			}
		}
		try {
			throw new Exception("Reciever " + type + " was not found in the list.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	public class KeyA2 implements Key{
		String key = "A2";
		public boolean run(String key, DatagramPacket pack){
			if(this.key.equals(key)){
				ArrayList<Object> objs = ByteToObject.bytesToObjects(pack);
				String type = (String)(objs.get(0));
				for(DataBaseReciever db: dataBaseRecievers){
					if(db.processDataBase(type, objs)){
						return true;
					}
				}
				try {
					throw new Exception("Reciever " + type + " was not found in the list.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
				
			}
			return false;
		}
		public String getDescription(){
			return new String(key + " Processes a packet of objects based on the string in the object");
		}
		@Override
		public String getKey(){
			return key;
		}
	}
	public abstract class DataBaseReciever{
		String type;
		public DataBaseReciever(String type){
			this.type = type;
		}
		public synchronized boolean processDataBase(String type, ArrayList<Object> objs){
			if(this.type.equals(type)){
				try {
					action(objs);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
		protected synchronized void action(ArrayList<Object> objs) throws IOException, ClassNotFoundException{
			
		}
	}
}
