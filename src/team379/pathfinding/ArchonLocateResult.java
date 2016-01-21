package team379.pathfinding;

import battlecode.common.MapLocation;

public class ArchonLocateResult {
	private final boolean foundTheArchon;
	private final MapLocation location;
	
	ArchonLocateResult(boolean foundTheArchon, MapLocation location) {
		this.foundTheArchon = foundTheArchon;
		this.location = location;
	}
	
	public boolean foundTheArchon() {
		return foundTheArchon;
	}
	
	public MapLocation getLocation() {
		return location;
	}
}
