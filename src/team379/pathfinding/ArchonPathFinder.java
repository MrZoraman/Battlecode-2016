package team379.pathfinding;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team379.Globals;

public class ArchonPathFinder extends PathFinder {
	
	@Override
	public PathFindResult move(RobotController rc) throws Exception {
		MapLocation target = getTarget();
		if(target == null) {
			return PathFindResult.NO_TARGET;
		}
		target = validateTarget(rc, target);
		setTarget(target);
		return super.move(rc);
	}
	
	private MapLocation validateTarget(RobotController rc, MapLocation initialTarget) throws Exception {
		//my location
		MapLocation myLocation = rc.getLocation();
		//get direction to target
		Direction dirToTarget = myLocation.directionTo(initialTarget);
		//distance to target
		int distanceSquaredToTarget = myLocation.distanceSquaredTo(initialTarget);
		//make sure target is within distance
		if(distanceSquaredToTarget > rc.getType().sensorRadiusSquared) {
			//the target is outside my sensor range. Get space adjacent to me
			MapLocation adjacent = myLocation.add(dirToTarget);
			//make sure it is on the map
			if(!rc.onTheMap(adjacent)) {
				return validateTarget(rc, initialTarget.subtract(dirToTarget));
			}
//			//make sure location isn't on rubble
//			double rubble = rc.senseRubble(adjacent);
//			if(rubble > Globals.RUBBLE_THRESHOLD_MIN()) {
//				return validateTarget(rc, initialTarget.subtract(dirToTarget));
//			}
			return initialTarget;
		}
		
		//the target is within my sensor range
		
		//make sure location is on the map
		if(!rc.onTheMap(initialTarget)) {
			return validateTarget(rc, initialTarget.subtract(dirToTarget));
		}
		
		//make sure location is not on rubble
		double rubble = rc.senseRubble(initialTarget);
		if(rubble > Globals.RUBBLE_THRESHOLD_MIN()) {
			return validateTarget(rc, initialTarget.subtract(dirToTarget));
		}
		
		//looks like this location is ok to go to!
		return initialTarget;
	}
}
