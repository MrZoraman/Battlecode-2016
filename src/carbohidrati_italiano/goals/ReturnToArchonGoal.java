package carbohidrati_italiano.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;

public class ReturnToArchonGoal implements Goal {
	
	private final MapLocation archonLocation;
	private final int archonId;
	
	private final PathFinder pathFinder = new PathFinder();
	
	public ReturnToArchonGoal(MapLocation archonLocation, int archonId) {
		this.archonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		PathFindResult result = pathFinder.move(rc, archonLocation);
		if(result != PathFindResult.SUCCESS) {
			pathFinder.reset();
		}
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(24, rc.getTeam());
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == archonId) {
				return new PatrolAroundArchonGoal(archonId, 24, Globals.GUARD_AGGRESSION_RANGE);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
