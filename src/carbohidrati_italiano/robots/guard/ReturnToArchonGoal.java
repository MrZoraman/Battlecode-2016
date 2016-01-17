package carbohidrati_italiano.robots.guard;

import java.util.List;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.dstarlite.DStarLite;
import carbohidrati_italiano.dstarlite.State;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class ReturnToArchonGoal implements Goal {
	
	private final MapLocation archonLocation;
	private final int archonId;
	
	private List<State> path = null;
	private int pathIndex = 0;
	
	public ReturnToArchonGoal(MapLocation archonLocation, int archonId) {
		this.archonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		MapLocation myLocation = rc.getLocation();
		DStarLite pf = robot.getPathFinder();
		if(path == null) {
			pf.updateStart(myLocation.x, myLocation.y);
			pf.updateGoal(archonLocation.x, archonLocation.y);
			pf.replan();
			path = robot.getPathFinder().getPath();
		}
		
		if(rc.isCoreReady()) {
			State s = path.get(pathIndex);
			pathIndex++;
			MapLocation nextLocation = new MapLocation(s.x, s.y);
			Direction dir = myLocation.directionTo(nextLocation);
			if(rc.canMove(dir)) {
				rc.move(dir);
			} else {
				pf.updateCell(nextLocation.x, nextLocation.y, -1);
				path = null;
				pathIndex = 0;
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
