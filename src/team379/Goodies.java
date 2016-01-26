package team379;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public enum Goodies {

	PARTS(1),
	
	FRIENDLY_ARCHON(6000),
	
	NEUTRAL_ARCHON(2000),
	ZOMBIE_DEN(1500),
	
	SCOUT(18),
	SOLDIER(40),
	GUARD(36),
	VIPER(0),
	TURRET(150);
	
	private short value;
	
	private Goodies(int value) {
		this.value = (short) value;
	}
	
	public short getValue() {
		return value;
	}
	
	private static short getValue(RobotType type, Team myTeam, Team theirTeam) {
		switch(type) {
		case ARCHON:
			if(theirTeam == Team.NEUTRAL) {
				System.out.println("found neutral archon");
				return NEUTRAL_ARCHON.getValue();
			} else if (theirTeam == myTeam) {
				//System.out.println("found friendly archon");
				return FRIENDLY_ARCHON.getValue();
			} else {
				return 0;
			}
		case GUARD: 
			//System.out.println("found guard");
			return GUARD.getValue();
		case SCOUT: 
			//System.out.println("found scout");
			return SCOUT.getValue();
		case SOLDIER: 
			//System.out.println("found soldier");
			return SOLDIER.getValue();
		case TURRET: 
			//System.out.println("found turret");
			return TURRET.getValue();
		case VIPER: 
			//System.out.println("found viper");
			return VIPER.getValue();
		case ZOMBIEDEN: 
			//System.out.println("found zombie den");
			return ZOMBIE_DEN.getValue();
		default: return 0;
		}
	}
	
//	public static short scanGoodies(RobotController rc) {
//		System.out.println("scanning goodies at " + rc.getLocation());
//		short goodieTotal = 0;
//		int sensorRadiusSquared = RobotType.ARCHON.sensorRadiusSquared;//rc.getType().sensorRadiusSquared;
//		RobotInfo[] neutrals = rc.senseNearbyRobots(sensorRadiusSquared, Team.NEUTRAL);
//		for(RobotInfo neutral : neutrals) {
//			goodieTotal += Goodies.getValue(neutral.type, rc.getTeam(), neutral.team);
//		}
//		RobotInfo[] zombies = rc.senseNearbyRobots(sensorRadiusSquared, Team.ZOMBIE);
//		for(RobotInfo zombie : zombies) {
//			goodieTotal += Goodies.getValue(zombie.type, rc.getTeam(), zombie.team);
//		}
//		MapLocation[] partLocations = rc.sensePartLocations(sensorRadiusSquared);
//		for(MapLocation partLocation : partLocations) {
//			double rubble = rc.senseRubble(partLocation);
//			if(rubble > Globals.RUBBLE_THRESHOLD_MAX()) {
//				continue;
//			}
//			
//			double parts = rc.senseParts(partLocation);
//			//System.out.println("found parts (" + parts + ")");
//			goodieTotal += (short) (parts * PARTS.getValue());
//		}
//		RobotInfo[] friends = rc.senseNearbyRobots(sensorRadiusSquared, rc.getTeam());
//		int lowestArchonId = RobotMemory.getArchonId();
////		for(RobotInfo friend : friends) {
////			if(friend.type == RobotType.ARCHON) {
////				if(friend.ID < lowestArchonId) {
////					goodieTotal += Goodies.FRIENDLY_ARCHON.getValue();
////				}
////			}
////		}
//		return goodieTotal;
//	}
	
	public static GoodieSearchResult scanGoodies(RobotController rc) {
		int goodieTotal = 0;
		
		int highestGoodie = 0;
		MapLocation mostImportantGoodie = null;
		
		MapLocation myLoc = rc.getLocation();
		Team myTeam = rc.getTeam();
		
		//friendly archons
//		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
//		GoodieSearchResult result = scanRobots(nearbyRobots, RobotType.ARCHON, myLoc, myTeam, myTeam);
//		goodieTotal += result.getGoodies();
//		if(result.getGoodies() > highestGoodie) {
//			highestGoodie = result.getGoodies();
//			mostImportantGoodie = result.getLocation();
//		}
		
		//zombie dens
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		GoodieSearchResult result = scanRobots(nearbyRobots, RobotType.ZOMBIEDEN, myLoc, myTeam, Team.ZOMBIE);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//neutral archons
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.ARCHON, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//neutral turrets
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.TURRET, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//neutral soldiers
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.SOLDIER, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//neutral guards
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.GUARD, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//parts
		int partsFound = scanParts(rc);
		goodieTotal += partsFound;
		if(partsFound > highestGoodie) {
			highestGoodie = partsFound;
			mostImportantGoodie = myLoc;
		}
		
		//neutral scouts
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.SCOUT, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		//neutral vipers
		nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		result = scanRobots(nearbyRobots, RobotType.VIPER, myLoc, myTeam, Team.NEUTRAL);
		goodieTotal += result.getGoodies();
		if(result.getGoodies() > highestGoodie) {
			highestGoodie = result.getGoodies();
			mostImportantGoodie = result.getLocation();
		}
		
		if(mostImportantGoodie == null) {
			mostImportantGoodie = myLoc;
		}
		
		return new GoodieSearchResult(goodieTotal, mostImportantGoodie);
	}
	
	private static GoodieSearchResult scanRobots(RobotInfo[] robots, RobotType type, MapLocation myLoc, Team myTeam, Team theirTeam) {
		int goodieTotal = 0;
		int closestDistanceSquared = Integer.MAX_VALUE;
		MapLocation loc = null;
		for(RobotInfo robot : robots) {
			if(robot.type != type || robot.team != theirTeam) {
				continue;
			}
			
			goodieTotal += getValue(type, myTeam, theirTeam);
			
			int distanceSquared = myLoc.distanceSquaredTo(robot.location);
			if(distanceSquared < closestDistanceSquared) {
				closestDistanceSquared = distanceSquared;
				loc = robot.location;
			}
		}
		
		return new GoodieSearchResult(goodieTotal, loc);
	}
	
	private static int scanParts(RobotController rc) {
		int partsFound = 0;
		MapLocation[] partLocations = rc.sensePartLocations(RobotType.ARCHON.sensorRadiusSquared);
		for(MapLocation partLocation : partLocations) {
			partsFound += rc.senseParts(partLocation);
		}
		return partsFound;
	}
}
