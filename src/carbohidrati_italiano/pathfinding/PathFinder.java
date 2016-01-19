package carbohidrati_italiano.pathfinding;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import static carbohidrati_italiano.pathfinding.PathFindResult.*;

public class PathFinder {
	@FunctionalInterface
	private interface DirectionTranslator {
		Direction translateDirection(Direction dir);
	}
	
	private class DirectionResult {
		Direction dir;
		boolean robotInWay = false;
	}
	
	private static final DirectionTranslator[] translators;
	
	static {
		translators = new DirectionTranslator[8];
		translators[0] = dir -> dir;							//north
		translators[1] = dir -> dir.rotateLeft();				//north-west
		translators[2] = dir -> dir.rotateRight().rotateRight();//north-east
		translators[3] = dir -> dir.rotateRight();				//east
		translators[4] = dir -> dir.opposite();					//west
		translators[5] = dir -> dir.rotateLeft();				//south-west
		translators[6] = dir -> dir.rotateLeft().rotateLeft();	//south-east
		translators[7] = dir -> dir.rotateRight();				//south
	}
	
	private Direction directionFrom = null;
	
	private int routeMovesUntilFail = 24;
	private int routeTries = 0;
	
	private int robotInWayRetries = 6;
	private int robotRetries;
	
	private int distanceDeltaGiveUp = 2;
	private int distanceDelta = 13;
	private int lastDistance = 0;
	private int distanceTries = 0;
	
	public PathFindResult move(RobotController rc, MapLocation target) throws Exception {
		if(!rc.isCoreReady()) {
			return CORE_DELAY;
		}
	
		MapLocation myLocation = rc.getLocation();
		
		int distanceToGoal = myLocation.distanceSquaredTo(target);
		int calculatedDelta = Math.abs(lastDistance - distanceToGoal);
		lastDistance = distanceToGoal;
		if(calculatedDelta < distanceDelta) {
			distanceTries++;
		} else {
			distanceTries = 0;
		}
		
		if(distanceTries > distanceDeltaGiveUp) {
			return COULD_NOT_FIND_ROUTE;
		}
		
		DirectionResult result = getDirection(rc, myLocation, target);
		if(result.dir != null) {
			if(routeMovesUntilFail > 0 && routeTries >= routeMovesUntilFail) {
				return COULD_NOT_FIND_ROUTE;
			} else {
				routeTries++;
				rc.move(result.dir);
				directionFrom = result.dir.opposite();
				return SUCCESS;
			}
		}
		
		if(result.robotInWay) {
			if(robotInWayRetries >= robotRetries) {
				return ROBOT_IN_WAY_AND_NOT_MOVING;
			} else {
				robotRetries++;
				return ROBOT_IN_WAY;
			}
		}
		
		return STUCK;
	}
	
	private DirectionResult getDirection(RobotController rc, MapLocation myLocation, MapLocation target) throws Exception {
		Direction targetDir = myLocation.directionTo(target);
		DirectionResult result = new DirectionResult();
		for(int ii = 0; ii < translators.length; ii++) {
			targetDir = translators[ii].translateDirection(targetDir);
			
			if(directionFrom != null && targetDir == directionFrom) {
				continue;
			}
			
			if(rc.canMove(targetDir)) {
				result.dir = targetDir;
				break;
			}
			
			MapLocation loc = myLocation.add(targetDir);
//			if(!rc.onTheMap(loc)) {
//				continue;
//			}
			
//			double rubble = rc.senseRubble(loc);
//			if(rubble < 100.0) {
//				continue;
//			}
			
			RobotInfo ri = rc.senseRobotAtLocation(loc);
			if(ri != null) {
				result.robotInWay = true;
			}
		}
		
		if(directionFrom != null && result.dir == null && !result.robotInWay) {
			targetDir = directionFrom.opposite();
			if(rc.canMove(targetDir)) {
				result.dir = targetDir;
			}
		}
		
		return result;
	}
	
	public void reset() {
		routeTries = 0;
		robotRetries = 0;
		directionFrom = null;
		distanceTries = 0;
	}

	public int getRouteMovesUntilFail() {
		return routeMovesUntilFail;
	}

	public void setRouteMovesUntilFail(int routeMovesUntilFail) {
		this.routeMovesUntilFail = routeMovesUntilFail;
	}
}
