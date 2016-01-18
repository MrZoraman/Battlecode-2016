package carbohidrati_italiano.goals.archon;

import battlecode.common.RobotController;
import carbohidrati_italiano.goals.Goal;
import carbohidrati_italiano.robots.Robot;

public class StandardGoal implements Goal {

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
