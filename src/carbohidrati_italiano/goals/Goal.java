package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Robot;

public interface Goal {
	public default Goal achieveGoal(RobotController rc, Robot robot) throws Exception { return null; };
	
	public String getName();
}
