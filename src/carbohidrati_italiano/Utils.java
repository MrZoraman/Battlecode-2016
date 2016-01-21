package carbohidrati_italiano;

public class Utils {
	private Utils() { }
	
	static int combine(short s1, short s2) {
		return (int) s1 << 16 | s2 & 0xFFFF;
	}
	
	static short[] split(int num) {
		short s1 = (short)(num >> 16);
		short s2 = (short)num;
		return new short[]{s1, s2};
	}
	
	static long combine(int a, int b) {
		return (long) a << 32 | b & 0xFFFFFFFFL;
	}
	
	static int[] split(long num) {
		int a = (int)(num >> 32);
		int b = (int)num;
		return new int[]{a, b};
	}
}
