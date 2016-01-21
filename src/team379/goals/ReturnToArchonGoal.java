package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.pathfinding.ArchonLocateResult;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.PathFindUtils;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class ReturnToArchonGoal extends Goal {
	
	public ReturnToArchonGoal(RobotMemory memory) {
		super(memory);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, memory.getArchonId(), nearbyRobots, memory.getLastKnownArchonLocation());
		
		if(alr.foundTheArchon()) {
			memory.setLastKnownArchonLocation(alr.getLocation());
		}
		
		
		PathFindResult result = pathFinder.move(rc, alr.getLocation());
		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
			pathFinder.reset();
		}
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == memory.getArchonId()) {
				return new PatrolAroundArchonGoal(memory);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
