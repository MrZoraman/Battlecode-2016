package team379.goals;

import battlecode.common.RobotController;
import team379.Robot;

public class EmptyGoal extends Goal {

	public EmptyGoal() {
		super(null);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "Empty Goal";
	}

}
