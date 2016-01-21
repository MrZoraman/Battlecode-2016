package team379.robots;

import battlecode.common.MapLocation;

public class RobotMemory {
	private final int opponentAggressionRange;
	private final int patrolRadius;
	
	private MapLocation lastKnownArchonLocation;
	private int archonId;
	
	public RobotMemory(int archonId, int opponentAggressionRange, int patrolRadius) {
		this.archonId = archonId;
		this.opponentAggressionRange = opponentAggressionRange;
		this.patrolRadius = patrolRadius;
	}

	public MapLocation getLastKnownArchonLocation() {
		return lastKnownArchonLocation;
	}

	public void setLastKnownArchonLocation(MapLocation lastKnownArchonLocation) {
		this.lastKnownArchonLocation = lastKnownArchonLocation;
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
}
