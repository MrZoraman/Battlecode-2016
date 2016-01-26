package team379.goals.archon;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import team379.goals.Goal;

public class ActivateGoal extends ArchonGoalBase {
	
	private Goal nextGoal;
	
	public ActivateGoal(Goal nextGoal) {
		this.nextGoal = nextGoal;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(2, Team.NEUTRAL);
		
		if(nearbyRobots.length <= 0) {
			return nextGoal;
		}
		
		for(RobotInfo robot : nearbyRobots) {
			if(rc.isCoreReady()) {
				rc.activate(robot.location);
			}
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "activating";
	}

}
