package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.Robot;
import team379.RobotMemory;
import team379.pathfinding.ArchonLocateResult;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFinder;

public class ReturnToArchonGoal extends Goal {
	private final PathFinder pf = new PathFinder();
	
	public ReturnToArchonGoal(RobotMemory memory) {
		super(memory);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = ArchonLocator.findArchonLocation(rc, memory, nearbyRobots, memory.getLastKnownArchonLocation());
		
		if(alr.foundTheArchon()) {
			memory.setLastKnownArchonLocation(alr.getLocation());
		}
		
		
		PathFindResult result = pf.move(rc, alr.getLocation());
		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
			pf.reset();
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
