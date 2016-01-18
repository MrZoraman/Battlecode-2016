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
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;

public class PatrolAroundArchonGoal implements Goal {
	
	private final Random rand = new Random();
	private final int archonId;
	private final int opponentAggressionRange;
	private final PathFinder pathFinder = new PathFinder();
	private final int patrolRadius;
	
	private MapLocation lastKnownArchonLocation;
	private MapLocation whereIWantToGo = null;
	
	private int directionIndex = -1;
	
	public PatrolAroundArchonGoal(int archonId, MapLocation lastKnownArchonLocation, int opponentAggressionRange, int patrolRadius) {
		this.archonId = archonId;
		this.opponentAggressionRange = opponentAggressionRange;
		this.lastKnownArchonLocation = lastKnownArchonLocation;
		this.patrolRadius = patrolRadius;
		pathFinder.setRouteMovesUntilFail(4);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, archonId, nearbyRobots, lastKnownArchonLocation);
		
		if(alr.foundTheArchon()) {
			lastKnownArchonLocation = alr.getLocation();
		}
		
		if(findBaddies(rc, nearbyRobots)) {
			return new DefenseGoal(lastKnownArchonLocation, archonId, opponentAggressionRange, patrolRadius);
		}
		
		//if the archon is out of my sensor range, go back to the archon
		if(rc.getLocation().distanceSquaredTo(alr.getLocation()) > rc.getType().sensorRadiusSquared) {
			return new ReturnToArchonGoal(lastKnownArchonLocation, archonId, opponentAggressionRange, patrolRadius);
		}
		
		move(rc, alr.getLocation());
		
		return null;
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
			} else if(ri.team != myTeam && myLocation.distanceSquaredTo(ri.location) < opponentAggressionRange) {
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
		whereIWantToGo = archonLocation.add(dir, patrolRadius + rand.nextInt(3) - 1);	//TODO: magic number!
		pathFinder.reset();
	}
}
