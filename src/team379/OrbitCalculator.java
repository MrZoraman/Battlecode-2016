package team379;

import battlecode.common.RobotType;

/**
 * Calculates the radius of various units given a single "orbit constant". This orbit constant
 * 	is the outermost radius of the orbit circle in squares/tiles.
 *
 */
public class OrbitCalculator {
	
	//these are percentage of the orbit constant each unit will orbit at or something, I dunno
//	private static final double GUARD_PATROL_RADIUS = 1.0;
//	private static final double SOLDIER_PATROL_RADIUS = 0.85;
//	private static final double SCOUT_PATROL_RADIUS = 3.0;
//	private static final double TURRET_PATROL_RADIUS = 0.75;
//	private static final double ARCHON_PATROL_RADIUS = 0.6;
//	private static final double DEFAULT_PATROL_RADIUS = 0.6;
	
	private static final int GUARD_OFFSET = 0;
	private static final int SOLDIER_OFFSET = -1;
	private static final int SCOUT_OFFSET = 5;
	private static final int TURRET_OFFSET = -2;
	private static final int DEFAULT_OFFSET = -1;
	
	private static final int ARCHON_RADIUS = 3;
	
	private final double patrolRadius;
	
	public OrbitCalculator(int orbitConstant, RobotType type) {
		if(type == RobotType.ARCHON) {
			patrolRadius = ARCHON_RADIUS;
		} else {
			int radiusOffset = getRadiusOffset(type);
			patrolRadius = orbitConstant + radiusOffset;
		}
	}
	
	private int getRadiusOffset(RobotType type) {
		switch(type) {
		case GUARD: return GUARD_OFFSET;
		case SCOUT: return SCOUT_OFFSET;
		case SOLDIER: return SOLDIER_OFFSET;
		case TURRET: return TURRET_OFFSET;
		default: return DEFAULT_OFFSET;
		}
	}
	
	public double calculateRadius() {
		return patrolRadius;
	}
}
