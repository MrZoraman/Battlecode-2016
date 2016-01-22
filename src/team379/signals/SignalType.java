package team379.signals;

import java.util.HashMap;
import java.util.Map;

public enum SignalType {
	
	THIS_IS_MY_ID(1),
	NEW_RADIUS(2);
	
	private static Map<Integer, SignalType> signalsMap = new HashMap<>();
	
	static {
		SignalType[] arrayOfSignals = SignalType.values();
		for(SignalType s : arrayOfSignals) {
			signalsMap.put(s.getValue(), s);
		}
	}
	
	public static SignalType toSignal(int value) {
		return signalsMap.get(value);
	}
	
	private int value;
	
	private SignalType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
