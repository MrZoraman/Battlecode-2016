package team379.signals;

import battlecode.common.MapLocation;

//          long                          long
//    int           int             int           int
//short short   short short     short short   short short

//type  loc                   other info

public class SignalData {
	private final SignalType type;
	private final MapLocation location;
	private final short[] otherInfo;
	
	public SignalData(long a, long b) {
		int[] nums = split(a);
		short[] shorts = split(nums[0]);
		this.type = SignalType.toSignal(shorts[0]);
		short x = shorts[1];
		shorts = split(nums[1]);
		short y = shorts[0];
		this.location = new MapLocation(x, y);
		otherInfo = new short[6];
		otherInfo[0] = shorts[1];
		nums = split(b);
		shorts = split(nums[0]);
		otherInfo[1] = shorts[0];
		otherInfo[2] = shorts[1];
		shorts = split(nums[1]);
		otherInfo[3] = shorts[0];
		otherInfo[4] = shorts[1];
	}
	
	public long[] toLongs() {
		int num1 = combine((short) type.getValue(), (short) location.x);
		int num2 = combine((short) location.y, otherInfo[0]);
		long a = combine(num1, num2);
		num1 = combine(otherInfo[1], otherInfo[2]);
		num2 = combine(otherInfo[3], otherInfo[4]);
		long b = combine(num1, num2);
		return new long[]{a, b};
	}
	
	public SignalType getType() {
		return type;
	}
	
	public MapLocation getLocation() {
		return location;
	}
	
	public short[] getOtherInfo() {
		return otherInfo;
	}
	
	public SignalData(SignalType type, MapLocation location, short... otherInfo) {
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
	
	private long combine(int a, int b) {
		return (long) a << 32 | b & 0xFFFFFFFFL;
	}
	
	private int[] split(long num) {
		int a = (int)(num >> 32);
		int b = (int)num;
		return new int[]{a, b};
	}
}
