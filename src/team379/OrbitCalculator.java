package team379;

import battlecode.common.RobotType;

/**
 * Calculates the radius of various units given a single "orbit constant". This orbit constant
 * 	is the outermost radius of the orbit circle in squares/tiles.
 *
 */
public class OrbitCalculator {
	private final double orbitConstant;
	
	//these are percentage of the orbit constant each unit will orbit at or something, I dunno
	private static final double GUARD_PATROL_RADIUS = 0.95;
	private static final double SOLDIER_PATROL_RADIUS = 0.5;
	
	public OrbitCalculator(int orbitConstant, RobotType type) {
		this.orbitConstant = orbitConstant;
		
		switch(type) {
		case SOLDIER:
			calculateSoldier();
			break;
		case GUARD:
			calculateGuard();
			break;
		case SCOUT:
			calculateScout();
			break;
		default:
			calculateDefault();
		}
	}
	
	private int square(double radius) {
		return (int) Math.pow(radius, 2);
	}
	
	private void calculateDefault() {
//		calculatedRadius = square(4);
//		calculatedRange = 2;
	}
	
	private void calculateGuard() {
//		//System.out.println("radius I've been given: " + radius);
//		if(radius <= 5) {
//			//calculatedRadius = Globals.GUARD_PATROL_RADIUS;
//			calculatedRadius = 5;
//			calculatedRange = 3;
//		} else {
//			double maxRadius = radius;
//			double minRadius = radius * 0.75;
//			double midRadius = (minRadius + maxRadius) / 2;
//			calculatedRadius = square(midRadius * 2);
//			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
//		}
	}
	
	private void calculateSoldier() {
//		if(radius <= 5) {
//			//calculatedRadius = Globals.SOLDIER_PATROL_RADIUS;
//			calculatedRadius = 5;
//			calculatedRange = 3;
//		} else {
//			double maxRadius = radius * 0.75;
//			double minRadius = 3;
//			double midRadius = (minRadius + maxRadius) / 2;
//			calculatedRadius = square(midRadius * 2);
//			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
//		}
	}
	
	private void calculateScout() {
//		if(radius <= 5) {
//			//calculatedRadius = Globals.SCOUT_PATROL_RADIUS;
//			calculatedRadius = 5;
//			calculatedRange = 3;
//		} else {
//			double maxRadius = radius * 1.65;
//			double minRadius = radius * 1.2;
//			double midRadius = (minRadius + maxRadius) / 2;
//			calculatedRadius = square(midRadius * 2);
//			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
//		}
	}

	public int getCalculatedRadius() {
//		return calculatedRadius;
		return 6;
	}

	public int getCalculatedRange() {
//		return calculatedRange;
		return 0;
	}
}
