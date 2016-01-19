package carbohidrati_italiano.goals.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class ArchonUtils {
	private ArchonUtils() { }
	
	public static boolean safeBuild(RobotController rc, RobotType type, Direction dir) throws Exception {
		if(type == null || dir == null) {
			return true;
		}
		if(rc.hasBuildRequirements(type) && rc.canBuild(dir, type)) {
			rc.build(dir, type);
			return true;
		}
		
		return false;
	}
	
	public static Direction findPlaceAndBuild(RobotController rc, Direction start, RobotType type) throws Exception {
		for(int ii = 0; ii < 8; ii++) {
			Direction dir = findNextDir(rc, start, type);
			if(safeBuild(rc, type, dir)) {
				return dir;
			}
		}
		
		return null;
	}
		
	private static Direction findNextDir(RobotController rc, Direction start, RobotType type) {
		Direction next = start;
		for(int ii = 0; ii < 4; ii++) {
			next = next.rotateLeft().rotateLeft();
			if(rc.canBuild(next, type)) {
				return next;
			}
		}
		
		next = next.rotateLeft();
		
		for(int ii = 0; ii < 4; ii++) {
			next = next.rotateLeft().rotateLeft();
			if(rc.canBuild(next, type)) {
				return next;
			}
		}
		
		return null;
	}
}
