package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Robot;

public class EmptyGoal implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Empty Goal";
	}

}
