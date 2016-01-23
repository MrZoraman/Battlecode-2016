package team379.signals;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of signal.
 * @author Matt
 *
 */
public enum SignalType {
	
	/**
	 * Used to broadcast the id of a robot.
	 */
	THIS_IS_MY_ID(1),
	
	/**
	 * Used to broadcast a new radius to orbit at.
	 */
	NEW_RADIUS(2);
	
	/**
	 * Stores a map of signals to their short representation.
	 */
	private static Map<Short, SignalType> signalsMap = new HashMap<>();
	
	/**
	 * Populate the signals map
	 */
	static {
		SignalType[] arrayOfSignals = SignalType.values();
		for(SignalType s : arrayOfSignals) {
			signalsMap.put(s.getValue(), s);
		}
	}
	
	/**
	 * Get a signal type given an integer
	 * @param value The short representation of the signal type.
	 * @return A SignalType, or null is the integer is out of range.
	 */
	public static SignalType toSignal(short value) {
		return signalsMap.get(value);
	}
	
	/**
	 * The short representation of the signal
	 */
	private short value;
	
	/**
	 * Constructs the SignalType
	 * @param value The short representation (int is cast to short)
	 */
	private SignalType(int value) {
		this.value = (short) value;
	}
	
	/**
	 * Gets the short representation of this SignalType.
	 * @return The short representation.
	 */
	public short getValue() {
		return value;
	}
}
