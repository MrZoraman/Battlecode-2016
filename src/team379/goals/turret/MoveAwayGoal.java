package team379.goals.turret;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team379.Globals;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.ArchonLocator;

public class MoveAwayGoal implements Goal{

	private int directionIndex = 0;
	private final Random rand = new Random();

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		
		rc.pack();
		findMove(rc);
		return null;
	}

	@Override
	public String getName() {
		return "Moving Away!";
	}
		
	private void findMove(RobotController rc) throws GameActionException {
		directionIndex = rand.nextInt(8);
		Direction dir = Globals.movableDirections[directionIndex];
		move(rc, dir);
	}
	
	private void move(RobotController rc, Direction dir) throws GameActionException {
		if(rc.canMove(dir)) {
			rc.move(dir);
		}
	}
	private boolean testDistance(RobotController rc) {
//		if(Math.abs(rc.getLocation().x - alr.getLocation().x) > Globals.TURRET_ARCHON_DISTANCE) {
//			return false;
//		} else if(Math.abs(rc.getLocation().y - alr.getLocation().y) > Globals.TURRET_ARCHON_DISTANCE) {
//			return false;
//		}
		return true;
	}

}
