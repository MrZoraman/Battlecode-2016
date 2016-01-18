package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Robot;

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
