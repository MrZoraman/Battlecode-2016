package carbohidrati_italiano.goals.archon;

import battlecode.common.RobotController;
import carbohidrati_italiano.goals.Goal;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

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
