package carbohidrati_italiano.goals;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.pathfinding.ArchonLocateResult;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFindUtils;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class PatrolAroundArchonGoal extends Goal {
	private final Random rand = new Random();
	
	private MapLocation whereIWantToGo = null;
	
	private int directionIndex = -1;
	
	private Goal nextGoal = null;
	
	public PatrolAroundArchonGoal(RobotMemory memory) {
		super(memory);
		pathFinder.setRouteMovesUntilFail(4);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, memory.getArchonId(), nearbyRobots, memory.getLastKnownArchonLocation());
		
		if(alr.foundTheArchon()) {
			memory.setLastKnownArchonLocation(alr.getLocation());
		}
		
		if(findBaddies(rc, nearbyRobots)) {
			return new DefenseGoal(memory);
		}
		
		//if the archon is out of my sensor range, go back to the archon
		if(rc.getLocation().distanceSquaredTo(alr.getLocation()) > rc.getType().sensorRadiusSquared) {
			return new ReturnToArchonGoal(memory);
		}
		
		move(rc, alr.getLocation());
		
		return nextGoal;
	}

	@Override
	public String getName() {
		return "Patrollin'";
	}
	
	private boolean findBaddies(RobotController rc, RobotInfo[] nearbyRobots) {
		MapLocation myLocation = rc.getLocation();
		Team myTeam = rc.getTeam();
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.team == Team.ZOMBIE) {
				if(ri.type == RobotType.ZOMBIEDEN) {
					continue;
				}
				return true;
			} else if(ri.team != myTeam && myLocation.distanceSquaredTo(ri.location) < memory.getOpponentAggressionRange()) {
				return true;
			}
		}
		
		return false;
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
			if(!determineDestructibleRubble(rc, myLocation)) {
				calculateWhereIWantToGo(archonLocation);
			}
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
	
	private boolean determineDestructibleRubble(RobotController rc, MapLocation myLocation) {
		Direction[] dirs = new Direction[3];
		dirs[0] = myLocation.directionTo(whereIWantToGo);
		dirs[1] = dirs[0].rotateLeft();
		dirs[2] = dirs[1].rotateRight();
		
		Direction dir = null;
		for(int ii = 0; ii < dirs.length; ii++) {
			double rubble = rc.senseRubble(myLocation.add(dirs[ii]));
			
			if(rubble > Globals.RUBBLE_THRESHOLD_MIN && rubble < Globals.RUBBLE_THRESHOLD_MAX) {
				dir = dirs[ii];
				break;
			}
		}
		
		if(dir == null) {
			return false;
		}
		
		nextGoal = new ClearRubbleGoal(memory, dir);
		return true;
	}
}
