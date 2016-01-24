package team379;

import battlecode.common.RobotType;

/**
 * Calculates the radius of various units given a single "orbit constant". This orbit constant
 * 	is the outermost radius of the orbit circle in squares/tiles.
 *
 */
public class OrbitCalculator {
	
	//these are percentage of the orbit constant each unit will orbit at or something, I dunno
	private static final double GUARD_PATROL_RADIUS = 1.0;
	private static final double SOLDIER_PATROL_RADIUS = 0.85;
	private static final double SCOUT_PATROL_RADIUS = 3.0;
	private static final double TURRET_PATROL_RADIUS = 0.75;
	private static final double DEFAULT_PATROL_RADIUS = 0.6;

	private final double orbitConstant;
	private final double patrolRadius;
	
	public OrbitCalculator(int orbitConstant, RobotType type) {
		this.orbitConstant = orbitConstant;
		
		switch(type) {
		case SOLDIER:
			patrolRadius = SOLDIER_PATROL_RADIUS;
			break;
		case GUARD:
			patrolRadius = GUARD_PATROL_RADIUS;
			break;
		case SCOUT:
			patrolRadius = SCOUT_PATROL_RADIUS;
			break;
		case TURRET:
			patrolRadius = TURRET_PATROL_RADIUS;
			break;
		default:
			patrolRadius = DEFAULT_PATROL_RADIUS;
		}
	}
	
	public double calculateRadius() {
		return orbitConstant * patrolRadius;
	}
}
