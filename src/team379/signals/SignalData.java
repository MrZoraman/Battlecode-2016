package team379.signals;

import battlecode.common.MapLocation;

//    int           int
//short short   short short

//type other info   loc          

public class SignalData {
	private final SignalType type;
	private final MapLocation location;
	private final short otherInfo;
	
	public SignalData(int a, int b) {
		short[] nums = split(a);
		type = SignalType.toSignal(nums[0]);
		otherInfo = nums[1];
		nums = split(b);
		location = new MapLocation(nums[0], nums[1]);
	}
	
	public int[] toInts() {
		int a = combine((short) type.getValue(), otherInfo);
		int b = combine((short) location.x, (short) location.y);
		return new int[]{a, b};
	}
	
	public SignalType getType() {
		return type;
	}
	
	public MapLocation getLocation() {
		return location;
	}
	
	public short getOtherInfo() {
		return otherInfo;
	}
	
	public SignalData(SignalType type, MapLocation location, short otherInfo) {
		this.type = type;
		this.location = location;
		this.otherInfo = otherInfo;
	}
	
	private int combine(short s1, short s2) {
		return (int) s1 << 16 | s2 & 0xFFFF;
	}
	
	private short[] split(int num) {
		short s1 = (short)(num >> 16);
		short s2 = (short)num;
		return new short[]{s1, s2};
	}
}
