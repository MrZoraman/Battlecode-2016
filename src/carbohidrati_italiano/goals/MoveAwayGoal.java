package carbohidrati_italiano.goals;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.pathfinding.ArchonLocateResult;
import carbohidrati_italiano.pathfinding.PathFindUtils;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class MoveAwayGoal extends Goal{

	private int directionIndex = 0;
	private final Random rand = new Random();
	public MoveAwayGoal(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, memory.getArchonId(), nearbyRobots, memory.getLastKnownArchonLocation());

		if(testDistance(rc, alr)) {
			return new TurretGoal(memory);
		}
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
	private boolean testDistance(RobotController rc, ArchonLocateResult alr) {
		if(Math.abs(rc.getLocation().x - alr.getLocation().x) > Globals.TURRET_ARCHON_DISTANCE) {
			return false;
		} else if(Math.abs(rc.getLocation().y - alr.getLocation().y) > Globals.TURRET_ARCHON_DISTANCE) {
			return false;
		}
		return true;
	}

}
