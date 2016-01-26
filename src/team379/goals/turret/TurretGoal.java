package team379.goals.turret;

import java.util.ArrayList;
import java.util.List;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalReader;

public class TurretGoal implements Goal{
	
	private static ArchonLocator al;
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		if(al == null) {
			al = new ArchonLocator(RobotMemory.getArchonLocation(), RobotMemory.getArchonId());
		}
		SignalReader.consume(rc, al);
		
		MapLocation mp = al.getArchonLocation(rc);
		double archonDistance = archonDistance(mp, rc);
		if(archonDistance > Globals.TURRET_ARCHON_DISTANCE()) {
			if(rc.getType() == RobotType.TURRET) {
				rc.pack();
			}
			return new TurretFindArchonGoal(al);
		}
		if(rc.getType() == RobotType.TTM) {
			rc.unpack();
		}
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
		}
        return null;
	}
	@Override
	public String getName() {
		return "Being a Turret";
	}
	
	private double archonDistance(MapLocation mp, RobotController rc) {
		return Math.sqrt(Math.pow(Math.abs(mp.x - rc.getLocation().x), 2) + Math.pow(Math.abs(mp.y - rc.getLocation().y), 2));
	}
	
	private void defend(RobotController rc, List<RobotInfo> baddies) throws Exception {
//		double closestBaddieDistance = Double.MAX_VALUE;
		int closestbaddieSquared = Integer.MAX_VALUE;
		RobotInfo closestBaddie = null;
		
		MapLocation myLocation = rc.getLocation();
		
		for(RobotInfo ri : baddies) {
			int distanceSquared = myLocation.distanceSquaredTo(ri.location);
			if(distanceSquared < closestbaddieSquared) {
				closestbaddieSquared = distanceSquared;
				closestBaddie = ri;
			}
		}
		
		if(closestBaddie == null) {
			//this should never be null, but just in case...
			return;
		}
		
//		if(rc.getLocation().equals(closestBaddie.location)) {
//			return;
//		}
		
		System.out.println("my type: " + rc.getType());
		System.out.println("my location: " + rc.getLocation());
		System.out.println("closest distance squared: " + closestbaddieSquared);
		if(closestbaddieSquared > 5 && closestbaddieSquared <= rc.getType().attackRadiusSquared) {
			if(rc.isWeaponReady()) {
				rc.attackLocation(closestBaddie.location);
			}
		} 
	}
}
