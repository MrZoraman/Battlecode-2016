package team379.robots;

import battlecode.common.MapLocation;
import battlecode.common.RobotType;

public class RobotMemory {
	private final int opponentAggressionRange;
	
	private int patrolRadius;
	private final RobotType type;
	
	private int lastPatrolOrdinal = -1;
	
	private MapLocation lastKnownArchonLocation;
	private int archonId;
	private int archonLocationStaleness = 0;
	
	private int broadcastRadius = RobotType.ARCHON.sensorRadiusSquared;
	
	public RobotMemory(int archonId, int opponentAggressionRange, RobotType type) {
		this.archonId = archonId;
		this.opponentAggressionRange = opponentAggressionRange;
		this.type = type;
	}

	public MapLocation getLastKnownArchonLocation() {
		return lastKnownArchonLocation;
	}

	public void setLastKnownArchonLocation(MapLocation lastKnownArchonLocation) {
		this.lastKnownArchonLocation = lastKnownArchonLocation;
		resetStaleness();
	}

	public int getArchonId() {
		return archonId;
	}

	public int getOpponentAggressionRange() {
		return opponentAggressionRange;
	}

	public int getPatrolRadius() {
		return patrolRadius;
	}
	
	public void setArchonId(int id) {
		this.archonId = id;
	}

	public int getLastPatrolOrdinal() {
		return lastPatrolOrdinal;
	}

	public void setLastPatrolOrdinal(int lastPatrolOrdinal) {
		this.lastPatrolOrdinal = lastPatrolOrdinal;
	}

	public int getBroadcastRadius() {
		return broadcastRadius;
	}

	public void setBroadcastRadius(int broadcastRadius) {
		this.broadcastRadius = broadcastRadius;
	}
	
	public void incrementStaleness() {
		archonLocationStaleness++;
	}
	
	public void resetStaleness() {
		archonLocationStaleness = 0;
	}
	
	public int getStaleness() {
		return archonLocationStaleness;
	}
	
	public RobotType getType() {
		return type;
	}
	
	public void setPatrolRadius(int radius) {
		this.patrolRadius = radius;
	}
}
