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
	
	public static final int GUARD_AGGRESSION_RANGE = 14;
	public static final int SOLDIER_AGGRESSION_RANGE = 14;
	
	public static final int GUARD_PATROL_RADIUS = 6;
	public static final int SOLDIER_PATROL_RADIUS = 6;
	public static final int SCOUT_PATROL_RADIUS = 10;
	
	public static final double RUBBLE_THRESHOLD_MIN = 50.0;
	public static final double RUBBLE_THRESHOLD_MAX = 600.0;
	
	public static final int TURRET_ARCHON_DISTANCE = 12;
	
}
