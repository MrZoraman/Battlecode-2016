package team379.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.RobotMemory;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFinder;

public class ReturnToArchonGoal implements Goal {
	private final PathFinder pf = new PathFinder();
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocator al = new ArchonLocator(RobotMemory.getArchonLocation(), RobotMemory.getArchonId());
		
		MapLocation archonLocation = al.getArchonLocation(rc);
		if(!al.isLost()) {
			RobotMemory.setArchonLocation(archonLocation);
		}
		
		pf.setTarget(archonLocation);
		PathFindResult result = pf.move(rc);
		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
			pf.reset();
		}
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == RobotMemory.getArchonId()) {
				return new PatrolAroundArchonGoal();
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Returning to Archon.";
	}

}
