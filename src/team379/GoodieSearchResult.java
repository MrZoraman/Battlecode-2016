package team379;

import battlecode.common.MapLocation;

public class GoodieSearchResult {
	private final int goodies;
	private final MapLocation location;
	
	GoodieSearchResult(int goodies, MapLocation location) {
		this.goodies = goodies;
		this.location = location;
	}

	public int getGoodies() {
		return goodies;
	}

	public MapLocation getLocation() {
		return location;
	}
}
