package team379;

import battlecode.common.MapLocation;

/**
 * Contains all info that persists across the goals
 * @author Matt
 *
 */
public class RobotMemory {
	/**
	 * Static class!
	 */
	private RobotMemory() { }
	
	private static int archonId = -1;
	private static MapLocation archonLocation = null;
	private static int aggressionRange = 0;
	private static int orbitConstant = 0;
	private static double rubbleThreshold = Globals.RUBBLE_THRESHOLD_MAX();
	
	public static int getArchonId() {
		return archonId;
	}
	
	public static void setArchonId(int archonId) {
		RobotMemory.archonId = archonId;
	}
	
	public static MapLocation getArchonLocation() {
		return archonLocation;
	}
	
	public static void setArchonLocation(MapLocation archonLocation) {
		RobotMemory.archonLocation = archonLocation;
	}

	public static int getAggressionRange() {
		return aggressionRange;
	}

	public static void setAggressionRange(int aggressionRange) {
		RobotMemory.aggressionRange = aggressionRange;
	}

	public static int getOrbitConstant() {
		return orbitConstant;
	}

	public static void setOrbitConstant(int orbitConstant) {
		RobotMemory.orbitConstant = orbitConstant;
	}

	public static double getRubbleThreshold() {
		return rubbleThreshold;
	}

	public static void setRubbleThreshold(double rubbleThreshold) {
		RobotMemory.rubbleThreshold = rubbleThreshold;
	}
}
