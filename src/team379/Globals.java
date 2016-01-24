package team379;

import battlecode.common.Direction;

public class Globals {
	public static final Direction[] movableDirections = {
		Direction.NORTH, 
		Direction.NORTH_EAST, 
		Direction.EAST, 
		Direction.SOUTH_EAST,
        Direction.SOUTH, 
        Direction.SOUTH_WEST, 
        Direction.WEST, 
        Direction.NORTH_WEST
    };
	
	public static final int INITIAL_ORBIT_CONSTANT() { return 5; } //Radius
	
	public static final int GUARD_AGGRESSION_RANGE() { return 16; }
	
	public static final double RUBBLE_THRESHOLD_MIN() { return 50.0; }
	public static final double RUBBLE_THRESHOLD_MAX() { return 1000.0; }
	
	public static final double TURRET_ARCHON_DISTANCE() { return 0.75 * INITIAL_ORBIT_CONSTANT(); }
	
}
