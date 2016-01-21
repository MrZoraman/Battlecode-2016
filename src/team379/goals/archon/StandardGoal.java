package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class StandardGoal extends Goal {

	public StandardGoal(RobotMemory memory) {
		super(memory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		rc.setIndicatorString(0, "Round is: " + rc.getRoundNum());
		return null;
	}

	@Override
	public String getName() {
		return "Standard Goal";
	}

}
