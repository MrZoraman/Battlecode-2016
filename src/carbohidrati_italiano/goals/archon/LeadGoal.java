package carbohidrati_italiano.goals.archon;

import battlecode.common.RobotController;
import carbohidrati_italiano.goals.Goal;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class LeadGoal extends Goal {

	public LeadGoal(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "Leading";
	}
}
