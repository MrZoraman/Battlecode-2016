package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;

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
