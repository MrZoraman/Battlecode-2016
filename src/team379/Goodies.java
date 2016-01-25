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
			if(theirTeam == Team.NEUTRAL)
				return NEUTRAL_ARCHON.getValue();
			else if (theirTeam == myTeam)
				return FRIENDLY_ARCHON.getValue();
			else
				return 0;
		case GUARD: return GUARD.getValue();
		case SCOUT: return SCOUT.getValue();
		case SOLDIER: return SOLDIER.getValue();
		case TURRET: return TURRET.getValue();
		case VIPER: return VIPER.getValue();
		case ZOMBIEDEN: return ZOMBIE_DEN.getValue();
		default: return 0;
		}
	}
	
	public static short scanGoodies(RobotController rc) {
		short goodieTotal = 0;
		int sensorRadiusSquared = RobotType.ARCHON.sensorRadiusSquared;//rc.getType().sensorRadiusSquared;
		RobotInfo[] neutrals = rc.senseNearbyRobots(sensorRadiusSquared, Team.NEUTRAL);
		for(RobotInfo neutral : neutrals) {
			goodieTotal += Goodies.getValue(neutral.type, rc.getTeam(), neutral.team);
		}
		RobotInfo[] zombies = rc.senseNearbyRobots(sensorRadiusSquared, Team.ZOMBIE);
		for(RobotInfo zombie : zombies) {
			goodieTotal += Goodies.getValue(zombie.type, rc.getTeam(), zombie.team);
		}
		MapLocation[] partLocations = rc.sensePartLocations(sensorRadiusSquared);
		for(MapLocation partLocation : partLocations) {
			double rubble = rc.senseRubble(partLocation);
			if(rubble > Globals.RUBBLE_THRESHOLD_MAX()) {
				continue;
			}
			
			double parts = rc.senseParts(partLocation);
			goodieTotal += (short) (parts * PARTS.getValue());
		}
		RobotInfo[] friends = rc.senseNearbyRobots(sensorRadiusSquared, rc.getTeam());
		int lowestArchonId = RobotMemory.getArchonId();
		for(RobotInfo friend : friends) {
			if(friend.type == RobotType.ARCHON) {
				if(friend.ID < lowestArchonId) {
					goodieTotal += Goodies.FRIENDLY_ARCHON.getValue();
				}
			}
		}
		return goodieTotal;
	}
}
