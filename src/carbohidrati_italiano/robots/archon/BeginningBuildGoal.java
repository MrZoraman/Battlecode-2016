package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.Utils;
import carbohidrati_italiano.robots.EmptyGoal;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class BeginningBuildGoal implements Goal {
	
	private static final int GUARDS_TO_MAKE = 3;
	
	private boolean hasScout = false;
	private int guardsMade = 0;

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		if(!rc.isCoreReady()) {
			return null;
		}
		
		if(!hasScout) {
			Direction scoutBuilt = ArchonUtils.findPlaceAndBuild(rc, RobotType.SCOUT);
			
			if(scoutBuilt != null) {
				hasScout = true;
				return null;
			} else {
				//uh oh, the archon is trapped!
			}
		}
		
		if(guardsMade < GUARDS_TO_MAKE) {
			if(rc.hasBuildRequirements(RobotType.GUARD)) {
				return null;
			}
			
			Direction guardBuilt = ArchonUtils.findPlaceAndBuild(rc, RobotType.GUARD);
			if(guardBuilt != null) {
				guardsMade++;
				return null;
			} else {
				//no more room for guards
				return new EmptyGoal();
			}
		}
		
		
		return new EmptyGoal();
	}

}
