package JInternet;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneInputSender implements Runnable{

	private static final int micDataSize = 10000;
	private TargetDataLine line;
	private boolean running = true;
	private Semaphore sem;
	private InternetCore ic;
	private int peerSend;
	public MicrophoneInputSender(InternetCore ic, Semaphore sem, int peerSend){
		line = getCaptureLine();
		this.ic = ic;
		this.sem = sem;
		this.peerSend = peerSend;
	}
	private TargetDataLine getCaptureLine(){
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for(Mixer.Info info: mixers){
			if(info.getName().equals("Primary Sound Capture Driver")){
				Mixer m = AudioSystem.getMixer(info);
				try {
					return (TargetDataLine) m.getLine(m.getTargetLineInfo()[0]);
				} catch (LineUnavailableException e) {
					
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	public void drain(){
		line.flush();
	}
	public synchronized void run(){
			while(true){
				try {
					sem.acquire();
					try {
						line.open(line.getFormat());
						line.start();
						AudioInputStream stream = new AudioInputStream(line);
						//stream = AudioSystem.getAudioInputStream(format, stream);
						byte[] data = new byte[micDataSize];
						try {
							stream.read(data, 0, micDataSize);
							byte[] key = new String("J5").getBytes();
							
							ic.send(false, ByteToObject.addPeerInfo(ByteToObject.addData(key, data), peerSend));
							//System.out.println("sending");
						} catch (IOException e) {
					
						}
						} catch (LineUnavailableException e) {
					}
				} catch (InterruptedException e1) {
					
				}
				
			}
		}
	public synchronized boolean running(){
		return running;
	}
}

