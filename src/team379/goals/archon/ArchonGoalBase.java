package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class ArchonGoalBase extends Goal {

	public ArchonGoalBase(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		//rc.broadcastMessageSignal()
		return null;
	}

	@Override
	public String getName() {
		return "Archon Goal Base";
	}

}
