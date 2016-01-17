package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.Utils;
import carbohidrati_italiano.robots.EmptyGoal;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;
import carbohidrati_italiano.robots.Signals;

public class BeginningBuildGoal implements Goal {
	
	private static final int GUARDS_TO_MAKE = 5;
	
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
				return new Goal() {
					@Override public String getName() {
						return "Trapped!";
					}
				};
			}
		}
		
		if(guardsMade < GUARDS_TO_MAKE) {
			if(!rc.hasBuildRequirements(RobotType.GUARD)) {
				return null;
			}
			
			Direction guardBuilt = ArchonUtils.findPlaceAndBuild(rc, RobotType.GUARD);
			if(guardBuilt != null) {
				guardsMade++;
				rc.broadcastMessageSignal(Signals.THIS_IS_MY_ID.getValue(), rc.getID(), 2);
				return null;
			} else {
				//no more room for guards
				return new EmptyGoal();
			}
		}
		
		
		return new EmptyGoal();
	}

	@Override
	public String getName() {
		return "Beginning Build Goal";
	}

}
