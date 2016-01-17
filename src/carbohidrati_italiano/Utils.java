package carbohidrati_italiano;

import battlecode.common.Direction;

public class Utils {
	private Utils() { }
	
	public static Direction nextOrdinal(Direction dir) {
		switch(dir) {
		case EAST:
			return Direction.SOUTH_EAST;
		case NORTH:
			return Direction.NORTH_EAST;
		case NORTH_EAST:
			return Direction.EAST;
		case NORTH_WEST:
			return Direction.NORTH;
		case SOUTH:
			return Direction.SOUTH_WEST;
		case SOUTH_EAST:
			return Direction.SOUTH;
		case SOUTH_WEST:
			return Direction.WEST;
		case WEST:
			return Direction.NORTH_WEST;
		default:
			return Direction.NONE;
		}
	}
	
	public static Direction rotate90(Direction dir) {
		return nextOrdinal(nextOrdinal(dir));
	}
}
