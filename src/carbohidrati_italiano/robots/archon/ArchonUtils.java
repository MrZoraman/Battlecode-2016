package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.Globals;

public class ArchonUtils {
	private ArchonUtils() { }
	
	public static boolean safeBuild(RobotController rc, RobotType type, Direction dir) throws Exception {
		if(rc.hasBuildRequirements(type) && rc.canBuild(dir, type)) {
			rc.build(dir, type);
			return true;
		}
		
		return false;
	}
	
	public static Direction findPlaceAndBuild(RobotController rc, RobotType type) throws Exception {
		for(Direction dir : Globals.movableDirections) {
			if(safeBuild(rc, RobotType.SCOUT, dir)) {
				return dir;
			}
		}
		
		return null;
	}
}
