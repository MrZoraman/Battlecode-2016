package carbohidrati_italiano.goals;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.pathfinding.ArchonLocateResult;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFindUtils;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;

public class PatrolAroundArchonGoal implements Goal {
	
	private static final int PATROL_RADIUS = 14;
	
	private final Random rand = new Random();
	private final int archonId;
	private final int opponentAggressionRange;
	private final PathFinder pathFinder = new PathFinder();
	
	private MapLocation lastKnownArchonLocation;
	private MapLocation whereIWantToGo = null;
	
	private int directionIndex = -1;
	
	public PatrolAroundArchonGoal(int archonId, MapLocation lastKnownArchonLocation, int opponentAggressionRange) {
		this.archonId = archonId;
		this.opponentAggressionRange = opponentAggressionRange;
		this.lastKnownArchonLocation = lastKnownArchonLocation;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, archonId, nearbyRobots, lastKnownArchonLocation);
		
		if(alr.foundTheArchon()) {
			lastKnownArchonLocation = alr.getLocation();
		}
		
		if(findBaddies(rc, nearbyRobots)) {
			return new DefenseGoal(lastKnownArchonLocation, archonId);
		}
		
		//if the archon is out of my sensor range, go back to the archon
		if(rc.getLocation().distanceSquaredTo(alr.getLocation()) > rc.getType().sensorRadiusSquared) {
			return new ReturnToArchonGoal();
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
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.team == Team.ZOMBIE) {
				return true;
			} else if(myLocation.distanceSquaredTo(ri.location) < opponentAggressionRange) {
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
			//time to figure out a new place to go
			if(directionIndex < 0) {
				directionIndex = rand.nextInt(8);
			} else {
				directionIndex++;
				directionIndex = directionIndex % 8;
			}
			Direction dir = Globals.movableDirections[directionIndex];
			int radius = dir.isDiagonal() ? PATROL_RADIUS - 4 : PATROL_RADIUS;		//TODO: magic number!
			whereIWantToGo = archonLocation.add(dir, radius + rand.nextInt(3) - 1);	//TODO: magic number!
			pathFinder.reset();
		}
		
		PathFindResult result = pathFinder.move(rc, whereIWantToGo);
		if(result != PathFindResult.SUCCESS || result != PathFindResult.CORE_DELAY) {	//TODO: smarter decision making here
			pathFinder.reset();
		}
	}
}
