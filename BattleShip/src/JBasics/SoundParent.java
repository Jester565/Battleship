package JBasics;

public abstract class SoundParent {

	public abstract void setLoop(boolean mode);
	public abstract void play();
	public abstract void pause();
	public abstract void setVolume(double scale);
	public abstract boolean playing();
	public abstract void stopAndReset();
	public abstract void drainClose();
}
