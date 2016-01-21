package team379.goals.scout;

import java.util.Map;

import battlecode.common.MapLocation;

public class ScoutInfo {
	private final int enemiesFound;
	private final boolean zombiePanic;
	private final boolean partsFound;
	
	private final MapLocation coordinates;
	
	public ScoutInfo(int enemiesFound, boolean zombiePanic, boolean partsFound, MapLocation coordinates) {
		this.enemiesFound = enemiesFound;
		this.zombiePanic = zombiePanic;
		this.partsFound = partsFound;
		this.coordinates = coordinates;
	}
}
