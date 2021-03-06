package team379.goals;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class PatrolAroundArchonPassively extends PatrolAroundArchonGoalBase {

	public PatrolAroundArchonPassively(RobotType type) {
		super(type);
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		MapLocation myLocation = rc.getLocation();
		int closestBaddieDistanceSquared = Integer.MAX_VALUE;
		RobotInfo closestBaddie = null;
		for(RobotInfo baddie : baddies) {
			int distanceSquared = myLocation.distanceSquaredTo(baddie.location);
			
			if(distanceSquared > rc.getType().attackRadiusSquared) {
				continue;
			}
			
			if(distanceSquared < closestBaddieDistanceSquared) {
				closestBaddieDistanceSquared = distanceSquared;
				closestBaddie = baddie;
			}
		}
		
		if(closestBaddie != null) {
			if(rc.isWeaponReady()) {
				rc.attackLocation(closestBaddie.location);
			}
		}
		
		return null;
	}
	
	@Override
	protected Goal onRubbleFound(Direction dir) {
		return new ClearRubbleGoal(dir);
	}

}
