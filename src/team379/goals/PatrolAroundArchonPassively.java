package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class PatrolAroundArchonPassively extends PatrolAroundArchonGoalBase {

	public PatrolAroundArchonPassively(RobotController rc) {
		super(rc);
	}

	@Override
	protected Goal baddiesFound(RobotInfo[] baddies) {
		return null;
	}

}
