package team379;

public class Pacer {
	private final int interval;
	private int value;
	
	public Pacer(int interval, boolean startNow) {
		this.interval = interval;
		if(startNow) {
			this.value = interval;
		} else {
			this.value = 0;
		}
	}
	
	public boolean pace() {
		if(value >= interval) {
			value = 0;
			return true;
		}
		
		value++;
		return false;
	}
}
