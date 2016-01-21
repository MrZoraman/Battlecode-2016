package carbohidrati_italiano.goals.scout;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.goals.Goal;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class ScoutPatrolGoal extends ScoutGoalBase {
	
	private final Random rand = new Random();
	
	private MapLocation whereIWantToGo = null;
	
	private int directionIndex = -1;

	public ScoutPatrolGoal(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		Goal nextGoal = super.achieveGoal(rc, robot);
		if(nextGoal != null) {
			return nextGoal;
		}
		
		move(rc, memory.getLastKnownArchonLocation());
		
		RobotInfo[] nearbyRobots = rc.senseHostileRobots(rc.getLocation(), rc.getType().sensorRadiusSquared);
		
		
		for(RobotInfo ri : nearbyRobots) {
			
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

		//where am I?
		MapLocation myLocation = rc.getLocation();
		
		if(whereIWantToGo == null || myLocation.distanceSquaredTo(whereIWantToGo) < 2) {	//TODO: magic number!
			calculateWhereIWantToGo(archonLocation);
		}
		
		PathFindResult result = pathFinder.move(rc, whereIWantToGo);
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			calculateWhereIWantToGo(archonLocation);
			break;
		case ROBOT_IN_WAY:
			break;
		case ROBOT_IN_WAY_AND_NOT_MOVING:
			calculateWhereIWantToGo(archonLocation);
			break;
		case STUCK:
			calculateWhereIWantToGo(archonLocation);
			break;
		case SUCCESS:
			break;
		default:
			break;
		
		}
	}
	
	private void calculateWhereIWantToGo(MapLocation archonLocation) {
		//time to figure out a new place to go
		if(directionIndex < 0) {
			directionIndex = rand.nextInt(8);
		} else {
			directionIndex++;
			directionIndex = directionIndex % 8;
		}
		Direction dir = Globals.movableDirections[directionIndex];
		//int radius = dir.isDiagonal() ? PATROL_RADIUS - 4 : PATROL_RADIUS;		//TODO: magic number!
		whereIWantToGo = archonLocation.add(dir, memory.getPatrolRadius() + rand.nextInt(3) - 1);	//TODO: magic number!
		pathFinder.reset();
	}
}
