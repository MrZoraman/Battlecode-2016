package team379.goals.archon;

import battlecode.common.RobotController;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;

public class LeadGoal extends ArchonGoalBase {

	public LeadGoal(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		super.achieveGoal(rc, robot);
		return null;
	}

	@Override
	public String getName() {
		return "Leading";
	}
}
