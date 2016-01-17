package carbohidrati_italiano.robots.archon;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class StandardGoal implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) {
		rc.setIndicatorString(0, "Round is: " + rc.getRoundNum());
		return null;
	}

}
