package team379.goals.archon;

import battlecode.common.RobotController;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;

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
