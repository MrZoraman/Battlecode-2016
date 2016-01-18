package carbohidrati_italiano.goals;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.robots.Robot;

public class PatrolAroundArchonGoal implements Goal {
	
	private final Random rand = new Random();
	private final int archonId;
	private final int senseDistance;
	private final int opponentAggressionRange;
	
	private MapLocation archonLocation;
	
	public PatrolAroundArchonGoal(int archonId, int senseDistance, int opponentAggressionRange) {
		this.archonId = archonId;
		this.senseDistance = senseDistance;
		this.opponentAggressionRange = opponentAggressionRange;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		
		//where is my archon?
		archonLocation = rc.senseRobot(archonId).location;
		
		if(findBaddies(rc)) {
			return new DefenseGoal(archonLocation, archonId, senseDistance);
		}
		
		move(rc);
		
		return null;
	}

	@Override
	public String getName() {
		return "Patrollin'";
	}
	
	private boolean findBaddies(RobotController rc) {
		RobotInfo[] robots = rc.senseHostileRobots(rc.getLocation(), senseDistance);
		
		MapLocation myLocation = rc.getLocation();
		
		for(RobotInfo ri : robots) {
			if(ri.team == Team.ZOMBIE) {
				return true;
			} else if(myLocation.distanceSquaredTo(ri.location) < opponentAggressionRange) {
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
		
		if(distance < 4) {
			rc.setIndicatorString(2, "too close!");
			//too close!
			dir = archonLocation.directionTo(currentLocation);
			int dirTries = 0;
			while(!rc.canMove(dir)) {
				dir = dir.rotateRight();
				dirTries++;
				if(dirTries > 8) {
					return;
				}
			}
		} else if(distance < 12) {
			rc.setIndicatorString(2, "I have breathing room!");
			//I have breathing room
			dir = Globals.movableDirections[rand.nextInt(Globals.movableDirections.length - 1)];
			int dirTries = 0;
			while(!rc.canMove(dir)) {
				dir = dir.rotateRight();
				dirTries++;
				if(dirTries > 8) {
					System.out.println("uh oh!");
					return;
				}
			}
		} else {
			rc.setIndicatorString(2, "too far away!");
			//too far away!
			dir = currentLocation.directionTo(archonLocation);
//			int num = rand.nextInt(3);
//			switch(num) {
//			case 0:
//				dir.rotateLeft();
//				break;
//			case 1:
//				dir.rotateRight();
//				break;
//			}
			
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
