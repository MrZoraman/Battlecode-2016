package carbohidrati_italiano.robots.guard;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class ReturnToArchonGoal implements Goal {
	
	private final MapLocation archonLocation;
	private final int archonId;
	
	public ReturnToArchonGoal(MapLocation archonLocation, int archonId) {
		this.archonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		MapLocation myLocation = rc.getLocation();
		
		Direction dir = myLocation.directionTo(archonLocation);
		if(rc.isCoreReady()) {
			if(rc.canMove(dir)) {
				rc.move(dir);
			}
		}
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(24, rc.getTeam());
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == archonId) {
				return new PatrolAroundArchonGoal(archonId);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
