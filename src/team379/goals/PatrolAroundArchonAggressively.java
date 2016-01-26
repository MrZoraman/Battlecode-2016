package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class PatrolAroundArchonAggressively extends PatrolAroundArchonGoalBase {

	public PatrolAroundArchonAggressively(RobotType type) {
		super(type);
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		return new DefenseGoal();
	}
}
