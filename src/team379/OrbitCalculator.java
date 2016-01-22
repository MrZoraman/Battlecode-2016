package team379;

import battlecode.common.RobotType;

public class OrbitCalculator {
	private final double radius;

	private int calculatedRadius; 
	private int calculatedRange;
	
	public OrbitCalculator(int radiusSquared, RobotType type) {
		this.radius = Math.sqrt(radiusSquared);
		
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
		calculatedRadius = square(4);
		calculatedRange = 2;
	}
	
	private void calculateGuard() {
		//System.out.println("radius I've been given: " + radius);
		if(radius <= 5) {
			calculatedRadius = Globals.GUARD_PATROL_RADIUS;
			calculatedRange = 3;
		} else {
			double maxRadius = radius;
			double minRadius = radius * 0.75;
			double midRadius = (minRadius + maxRadius) / 2;
			calculatedRadius = square(midRadius * 2);
			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
		}
	}
	
	private void calculateSoldier() {
		if(radius <= 5) {
			calculatedRadius = Globals.SOLDIER_PATROL_RADIUS;
			calculatedRange = 3;
		} else {
			double maxRadius = radius * 0.75;
			double minRadius = 3;
			double midRadius = (minRadius + maxRadius) / 2;
			calculatedRadius = square(midRadius * 2);
			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
		}
	}
	
	private void calculateScout() {
		if(radius <= 5) {
			calculatedRadius = Globals.SCOUT_PATROL_RADIUS;
			calculatedRange = 3;
		} else {
			double maxRadius = radius * 1.65;
			double minRadius = radius * 1.2;
			double midRadius = (minRadius + maxRadius) / 2;
			calculatedRadius = square(midRadius * 2);
			calculatedRange = (int) (calculatedRadius - minRadius) * 2;
		}
	}

	public int getCalculatedRadius() {
		return calculatedRadius;
	}

	public int getCalculatedRange() {
		return calculatedRange;
	}
}
