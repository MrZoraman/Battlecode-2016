package carbohidrati_italiano.robots.guard;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Signal;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.Utils;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;
import carbohidrati_italiano.robots.Signals;

public class ProtectArchonGoal implements Goal {
	
	private int archonId = -1;
	
	private Random rand = new Random();

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		Signal[] signals = rc.emptySignalQueue();
		
		for(Signal s : signals) {
			int[] messages = s.getMessage();
			if(messages == null) {
				continue;
			}
			
			Signals id = Signals.toSignal(messages[0]);
			if(id == null) {
				continue;
			}
			
			switch(id) {
			case THIS_IS_MY_ID:
				if(archonId < 0) {
					archonId = messages[1];
					rc.setIndicatorString(1, "My Archon is at: " + archonId);
				}
				break;
			default:
				continue;
			}
		}
		
		patrol(rc);
		
		return null;
	}

	@Override
	public String getName() {
		return "Protect Archon";
	}
	
	private void patrol(RobotController rc) throws Exception {
		if(!rc.isCoreReady()) {
			return;
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
	}
}
