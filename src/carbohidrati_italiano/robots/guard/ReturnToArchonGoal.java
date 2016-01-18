package carbohidrati_italiano.robots.guard;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class ReturnToArchonGoal implements Goal {
	
	private final MapLocation archonLocation;
	private final int archonId;
	
	private final PathFinder pathFinder = new PathFinder();
	
	public ReturnToArchonGoal(MapLocation archonLocation, int archonId) {
		this.archonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		pathFinder.move(rc, archonLocation);
		
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
