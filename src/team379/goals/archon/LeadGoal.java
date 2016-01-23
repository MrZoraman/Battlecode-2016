package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;

public class LeadGoal extends ArchonGoalBase {

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		return null;
	}

	@Override
	public String getName() {
		return "Leading";
	}
}
