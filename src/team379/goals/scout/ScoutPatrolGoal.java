package team379.goals.scout;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Signal;
import team379.goals.Goal;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class ScoutPatrolGoal extends ScoutGoalBase {
	private final Orbiter orbiter;

	public ScoutPatrolGoal(RobotMemory memory) {
		super(memory);
		orbiter = new Orbiter(memory.getPatrolRadius());
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
//		Goal nextGoal = super.achieveGoal(rc, robot);
//		if(nextGoal != null) {
//			return nextGoal;
//		}
//		
//		move(rc, memory.getLastKnownArchonLocation());
//		
//		RobotInfo[] nearbyRobots = rc.senseHostileRobots(rc.getLocation(), rc.getType().sensorRadiusSquared);
//		
//		
//		for(RobotInfo ri : nearbyRobots) {
//			
//		}
		
		Signal[] signals = rc.emptySignalQueue();
		if(signals.length > 0) {
			if(rc.isCoreReady() && rc.canMove(Direction.WEST)) {
				rc.move(Direction.WEST);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Patrolling";
	}

	private void move(RobotController rc, MapLocation archonLocation) throws Exception {
		if(!rc.isCoreReady()) {
			return;
		}
		
		PathFindResult result = orbiter.move(rc, archonLocation);
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			orbiter.calculateTarget(archonLocation);
			break;
		case ROBOT_IN_WAY:
			break;
		case ROBOT_IN_WAY_AND_NOT_MOVING:
			orbiter.calculateTarget(archonLocation);
			break;
		case STUCK:
			orbiter.calculateTarget(archonLocation);
			break;
		case SUCCESS:
			break;
		default:
			break;
		
		}
	}
}
