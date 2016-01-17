package carbohidrati_italiano.robots.guard;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.Utils;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class PatrolAroundArchonGoal implements Goal {
	
	private final Random rand = new Random();
	private final int archonId;
	
	private MapLocation archonLocation;
	
	public PatrolAroundArchonGoal(int archonId) {
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		
		//where is my archon?
		archonLocation = rc.senseRobot(archonId).location;
		
		if(findBaddies(rc)) {
			return new GuardDefenseGoal(archonLocation, archonId);
		}
		
		move(rc);
		
		return null;
	}

	@Override
	public String getName() {
		return "Patrollin'";
	}
	
	private boolean findBaddies(RobotController rc) {
		RobotInfo[] robots = rc.senseHostileRobots(rc.getLocation(), 24);
		
		MapLocation myLocation = rc.getLocation();
		
		for(RobotInfo ri : robots) {
			if(ri.team == Team.ZOMBIE) {
				return true;
			} else if(myLocation.distanceSquaredTo(ri.location) < Globals.GUARD_ATTACK_ROAM_RANGE) {
				return true;
			}
		}
		
		return false;
	}
	
	private void move(RobotController rc) throws Exception {
		if(!rc.isCoreReady()) {
			return;
		}
		//where am I?
		MapLocation currentLocation = rc.getLocation();
		//how far away is it from me?
		double distance = archonLocation.distanceSquaredTo(currentLocation);
		//what direction do I want to go?		
		Direction dir = null;
		
		if(distance < 6) {
			//too close!
			dir = archonLocation.directionTo(currentLocation);
			int dirTries = 0;
			while(!rc.canMove(dir)) {
				dir = Utils.nextOrdinal(dir);
				dirTries++;
				if(dirTries > 8) {
					return;
				}
			}
		} else if(distance < 12) {
			//I have breathing room
			dir = Globals.movableDirections[rand.nextInt(Globals.movableDirections.length - 1)];
			int dirTries = 0;
			while(!rc.canMove(dir)) {
				dir = Utils.nextOrdinal(dir);
				dirTries++;
				if(dirTries > 8) {
					return;
				}
			}
		} else {
			//too far away!
			dir = currentLocation.directionTo(archonLocation);
			if(!rc.canMove(dir)) {
				dir.rotateLeft();
			}
			if(!rc.canMove(dir)) {
				dir.rotateRight().rotateRight();
			}
			if(!rc.canMove(dir)) {
				return;
			}
		}
		
		//time to move!
		rc.move(dir);
		return;
	}

}
