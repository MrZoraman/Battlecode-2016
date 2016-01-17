package carbohidrati_italiano.robots;

import battlecode.common.RobotController;

public interface Goal {
	public default Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception { return null; };
	
	public String getName();
}
