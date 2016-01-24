package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class PatrolAroundArchonAggressively extends PatrolAroundArchonGoalBase {

	public PatrolAroundArchonAggressively(RobotController rc) {
		super(rc);
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		return new DefenseGoal();
	}
}
