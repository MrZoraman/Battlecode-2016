package team379.goals;

import java.util.ArrayList;
import java.util.List;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.pathfinding.PathFindResult;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class DefenseGoal extends Goal {
	
	public DefenseGoal(RobotMemory memory) {
		super(memory);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		RobotInfo[] robots = rc.senseHostileRobots(rc.getLocation(), rc.getType().sensorRadiusSquared);
		
		Team enemyTeam = rc.getTeam().opponent();
		
		List<RobotInfo> zombies = new ArrayList<RobotInfo>();
		List<RobotInfo> opponents = new ArrayList<RobotInfo>();
		
		for(RobotInfo ri : robots) {
			if(ri.type == RobotType.ZOMBIEDEN) {
				continue;
			}
			if(ri.team == enemyTeam) {
				opponents.add(ri);
			} else {
				zombies.add(ri);
			}
		}
		
		if(zombies.size() > 0) {
			rc.setIndicatorString(1, "defending against zombies!");
			defend(rc, zombies);
		} else if (opponents.size() > 0) {
			rc.setIndicatorString(1, "defending against the opponent!");
			defend(rc, opponents);
		} else {
			return new ReturnToArchonGoal(memory);
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Defending the archon!";
	}
	
	private void defend(RobotController rc, List<RobotInfo> baddies) throws Exception {
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
		
		if(closestBaddieDistance <= rc.getType().attackRadiusSquared) {
			if(rc.isWeaponReady()) {
				rc.attackLocation(closestBaddie.location);
			}
		} else {
			PathFindResult result = pathFinder.move(rc, closestBaddie.location);
			if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
				pathFinder.reset();
			}
		}
	}
}
