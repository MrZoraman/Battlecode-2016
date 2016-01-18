package carbohidrati_italiano.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;

public class ReturnToArchonGoal implements Goal {
	
	private final MapLocation archonLocation;
	private final int archonId;
	private final int aggressionRange;
	
	private final PathFinder pathFinder = new PathFinder();
	
	public ReturnToArchonGoal(MapLocation archonLocation, int archonId, int aggressionRange) {
		this.archonLocation = archonLocation;
		this.archonId = archonId;
		this.aggressionRange = aggressionRange;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		if(archonLocation == null) {
			//I'm lost!
		}
		
		
		PathFindResult result = pathFinder.move(rc, archonLocation);
		if(result != PathFindResult.SUCCESS) {
			pathFinder.reset();
		}
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == archonId) {
				return new PatrolAroundArchonGoal(archonId, aggressionRange);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
