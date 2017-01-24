package JInternet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class MicrophoneInputReader{

	private static final int EXTERNAL_BUFFER_SIZE = 32768;
	private static double lastRecievedTime = 0;
	private SourceDataLine speakers;
	public MicrophoneInputReader(){
		speakers = getSoundLine();
		PackageProcessing.addToKeyRoutines(new KeyJ5());
		PackageProcessing.addToKeyRoutines(new KeyJ6());
	}
	private SourceDataLine getSoundLine(){
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for(Mixer.Info info: mixers){
			if(info.getName().equals("Primary Sound Driver")){
				Mixer m = AudioSystem.getMixer(info);
				try {
					return (SourceDataLine) m.getLine(m.getSourceLineInfo()[0]);
				} catch (LineUnavailableException e) {
					
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	class KeyJ5 extends PeerKey{
		private static final String key = "J5";
		public KeyJ5() {
			super(key);
			// TODO Auto-generated constructor stub
		}
		@Override
		public String getKey(){
			return key;
		}
		public String getDescription(){
			return new String(key + ": " + new String("Located in MicrophoneInputReader, reads audio based on the data in the packet"));
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			lastRecievedTime = System.currentTimeMillis();
			// TODO Auto-generated method stub
			byte[]data = pack.getData();
			InputStream byteInput = new ByteArrayInputStream(data);
			//AudioInputStream inputStream = new AudioInputStream(byteInput, speakers.getFormat(), (long) (44100));
			try {
				speakers.open(speakers.getFormat());
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setSpeakers(true);
			int nBytesRead = 0;
			byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
			while (nBytesRead != -1)
			{
				try
				{
					nBytesRead = byteInput.read(abData, 0, abData.length);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				if (nBytesRead >= 0)
				{
					int	nBytesWritten = speakers.write(abData, 0, nBytesRead);
				}
			}
		}
	}
	public static synchronized boolean inRecieveTimeRange(double time){
		if(lastRecievedTime > time - 500){
			return true;
		}
		return false;
	}
	private synchronized void setSpeakers(boolean on){
		if(on){
			speakers.start();
		}else{
			speakers.drain();
			speakers.close();
		}
	}
	class KeyJ6 extends PeerKey{
		private static final String key = "J6";
		public KeyJ6() {
			super(key);
			// TODO Auto-generated constructor stub
		}
		@Override
		public String getKey(){
			return key;
		}
		public String getDescription(){
			return new String(key + ": " + new String("Located in MicrophoneInputReader, closes the sourceDataLine on the speakers to prevent echo"));
		}
		@Override
		public void routine(int id, DatagramPacket pack) {
			setSpeakers(false);
		}
	}
}
