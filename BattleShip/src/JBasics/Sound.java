package JBasics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound extends SoundParent{
	private Clip clip = null;
	private AudioInputStream ais = null;
	private FloatControl volumeControl;
	private boolean noMaster = false;
	static double masterScale = .1d;
	private static HashMap<FloatControl, Double> allVolumes;
	public Sound(){
		super();
		initiateAllVolume();
	}
	public Sound(String s){
		super();
		initiateAllVolume();
		setClip(s);
	}
	public Sound(boolean noScale, String s, double volumeScale){
		super();
		noMaster = true;
		initiateAllVolume();
		setClip(s);
		setVolume(volumeScale);
	}
	public Sound(String s, double volumeScale){
		super();
		initiateAllVolume();
		setClip(s);
		setVolume(volumeScale);
	}
	private void initiateAllVolume(){
		if(allVolumes == null){
			allVolumes = new HashMap<FloatControl, Double>();
		}
	}
	public static void setMasterScale(double volScale){
		if(volScale <= 0){
			volScale = .1d;
		}
		for(FloatControl volumeControl: allVolumes.keySet()){
			volumeControl.setValue((float) (((allVolumes.get(volumeControl))*volScale)*(Math.abs(volumeControl.getMinimum()) + Math.abs(volumeControl.getMaximum())) + volumeControl.getMinimum()));
		}
		masterScale = volScale;
	}
	public static void addToVolumes(FloatControl volume, double scale){
		allVolumes.put(volume, scale);
	}
	public static void removeFromVolumes(FloatControl volume){
		allVolumes.remove(volume);
	}
	public static void changeVolume(FloatControl volume, double newScale){
		allVolumes.replace(volume, newScale);
	}
	public void setTime(int seconds){
		clip.setMicrosecondPosition(seconds * 1000);
	}
	public void setClip(String s){
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("Data/" + s);
			InputStream bufferedIn = new BufferedInputStream(is);
			ais = AudioSystem.getAudioInputStream(bufferedIn);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
		try {
			clip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		init();
	}

	private void init(){
		try {
			clip.open(ais);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		allVolumes.put(volumeControl,1d);
		volumeControl.setValue((float) ((masterScale)*(Math.abs(volumeControl.getMinimum()) + Math.abs(volumeControl.getMaximum())) + volumeControl.getMinimum()));
	}
	public void setLoop(boolean on){
		if(on){
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}else{
			clip.loop(0);
		}
	}
	public void pause(){
		clip.stop();
	}
	public void stopAndReset(){
		clip.stop();
		clip.setFramePosition(0);
	}
	public void play(){
		clip.start();
	}
	public void drainClose(){
		clip.stop();
		clip.drain();
		clip.close();
	}
	public boolean playing(){
		return clip.isRunning();
	}
	public void setVolume(double scale){
		allVolumes.replace(volumeControl, scale);
		if(!noMaster)
			volumeControl.setValue((float) ((masterScale * scale)*(Math.abs(volumeControl.getMinimum()) + Math.abs(volumeControl.getMaximum())) + volumeControl.getMinimum()));
		else
			volumeControl.setValue((float) ((scale)*(Math.abs(volumeControl.getMinimum()) + Math.abs(volumeControl.getMaximum())) + volumeControl.getMinimum()));
	}
}