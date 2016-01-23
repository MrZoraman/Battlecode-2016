package team379.signals;

import battlecode.common.MapLocation;

//    int           int
//short short   short short

//type other info   loc          

/**
 * Fits various information into two integers.
 * @author Matt
 *
 */
public class SignalData {
	/**
	 * The type of signal this is
	 */
	private final SignalType type;
	
	/**
	 * The location contained in this signal
	 */
	private final MapLocation location;
	
	/**
	 * Any other info that is in this signal
	 */
	private final short otherInfo;
	
	/**
	 * Constructs/parses the SignalData given two integers.
	 * @param a First int
	 * @param b Second int
	 */
	public SignalData(int a, int b) {
		//first int
		//split into two
		short[] nums = split(a);
		//first short is the type
		type = SignalType.toSignal(nums[0]);
		//second short is other info
		otherInfo = nums[1];
		//second int
		nums = split(b);
		//second int contains the location
		location = new MapLocation(nums[0], nums[1]);
	}
	
	/**
	 * Creates signal data with the given info.
	 * @param type The type of signal
	 * @param location The location to store in this data
	 * @param otherInfo Any other info that is relevant to the type of signal
	 */
	public SignalData(SignalType type, MapLocation location, short otherInfo) {
		this.type = type;
		this.location = location;
		this.otherInfo = otherInfo;
	}
	
	/**
	 * Combines the SignalData into two integers.
	 * @return An array of two integers containing all of the data.
	 */
	public int[] toInts() {
		int a = combine((short) type.getValue(), otherInfo);
		int b = combine((short) location.x, (short) location.y);
		return new int[]{a, b};
	}
	
	/**
	 * Gets the {@link SignalType type} of signal this SignalData represents.
	 * @return The signal type.
	 */
	public SignalType getType() {
		return type;
	}
	
	/**
	 * Gets the location info stored in this data.
	 * @return The Location info.
	 */
	public MapLocation getLocation() {
		return location;
	}
	
	/**
	 * Gets any other info stored in this SignalData
	 * @return A short containing miscellaneous info
	 */
	public short getOtherInfo() {
		return otherInfo;
	}
	
	/**
	 * Combines two shorts into an int
	 * @param s1 The first short
	 * @param s2 The second short
	 * @return an int containing the data of the two shorts
	 */
	private int combine(short s1, short s2) {
		return (int) s1 << 16 | s2 & 0xFFFF;
	}
	
	/**
	 * Splits an int into two shorts
	 * @param num The int to split
	 * @return A two dimentional short array containing the data found in the int
	 */
	private short[] split(int num) {
		short s1 = (short)(num >> 16);
		short s2 = (short)num;
		return new short[]{s1, s2};
	}
}
