package carbohidrati_italiano.robots.guard;

import java.util.ArrayList;
import java.util.List;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class GuardDefenseGoal implements Goal {
	
	public GuardDefenseGoal(MapLocation archonLocation, int archonId) {
		if(archonLocation == null) {
			throw new IllegalStateException("archonlocation can't be null!");
		}
		this.archonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	private final MapLocation archonLocation;
	private final int archonId;
	
	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		RobotInfo[] robots = rc.senseHostileRobots(rc.getLocation(), 24);
		
		Team enemyTeam = rc.getTeam().opponent();
		
		List<RobotInfo> zombies = new ArrayList<RobotInfo>();
		List<RobotInfo> opponents = new ArrayList<RobotInfo>();
		
		for(RobotInfo ri : robots) {
			if(ri.team == enemyTeam) {
				opponents.add(ri);
			} else {
				zombies.add(ri);
			}
		}
		
		if(zombies.size() > 0) {
			rc.setIndicatorString(1, "defending against zombies!");
			defend(rc, zombies, false);
		} else if (opponents.size() > 0) {
			rc.setIndicatorString(1, "defending against the opponent!");
			defend(rc, opponents, true);
		} else {
			return new ReturnToArchonGoal(archonLocation, archonId);
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Defending the archon!";
	}
	
	private void defend(RobotController rc, List<RobotInfo> baddies, boolean opponent) throws Exception {
		double closestBaddieDistance = Double.MAX_VALUE;
		RobotInfo closestBaddie = null;
		
		MapLocation myLocation = rc.getLocation();
		
		for(RobotInfo ri : baddies) {
			double distance = myLocation.distanceSquaredTo(ri.location);
			if(distance < closestBaddieDistance) {
				closestBaddieDistance = distance;
				closestBaddie = ri;
			}
		}
		
		if(closestBaddie == null) {
			//this should never be null, but just in case...
			return;
		}
		
		if(closestBaddieDistance <= 2) {
			if(rc.isWeaponReady()) {
				rc.attackLocation(closestBaddie.location);
			}
		} else {
			if(rc.isCoreReady()) {
				Direction dir = myLocation.directionTo(closestBaddie.location);
				if(rc.canMove(dir)) {
					rc.move(dir);
				}
			}
		}
	}
}
