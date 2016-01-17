package carbohidrati_italiano.robots;

import java.util.HashMap;
import java.util.Map;

public enum Signals {
	
	THIS_IS_MY_ID(1);
	
	private static Map<Integer, Signals> signalsMap = new HashMap<>();
	
	static {
		Signals[] arrayOfSignals = Signals.values();
		for(Signals s : arrayOfSignals) {
			signalsMap.put(s.getValue(), s);
		}
	}
	
	public static Signals toSignal(int value) {
		return signalsMap.get(value);
	}
	
	private int value;
	
	private Signals(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
