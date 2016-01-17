package carbohidrati_italiano.robots.guard;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.Utils;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class PatrolAroundArchonGoal implements Goal {
	
	private final Random rand = new Random();
	private final int archonId;
	
	public PatrolAroundArchonGoal(int archonId) {
		this.archonId = archonId;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		if(!rc.isCoreReady()) {
			return null;
		}
		
		//where am I?
		MapLocation currentLocation = rc.getLocation();
		//where is my archon?
		MapLocation archonLocation = rc.senseRobot(archonId).location;
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
					return null;
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
					return null;
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
				return null;
			}
		}
		
		//time to move!
		rc.move(dir);
		return null;
	}

	@Override
	public String getName() {
		return "Patrollin'";
	}

}
