package carbohidrati_italiano.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.pathfinding.ArchonLocateResult;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFindUtils;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;

public class ReturnToArchonGoal implements Goal {
	
	private final int archonId;
	private final int opponentAggressionRange;
	private final PathFinder pathFinder = new PathFinder();
	
	private MapLocation lastKnownArchonLocation;
	
	public ReturnToArchonGoal(MapLocation lastKnownArchonLocation, int archonId, int opponentAggressionRange) {
		this.lastKnownArchonLocation = lastKnownArchonLocation;
		this.archonId = archonId;
		this.opponentAggressionRange = opponentAggressionRange;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, archonId, nearbyRobots, lastKnownArchonLocation);
		
		if(alr.foundTheArchon()) {
			lastKnownArchonLocation = alr.getLocation();
		}
		
		
		PathFindResult result = pathFinder.move(rc, alr.getLocation());
		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
			pathFinder.reset();
		}
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == archonId) {
				return new PatrolAroundArchonGoal(archonId, lastKnownArchonLocation, opponentAggressionRange);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
