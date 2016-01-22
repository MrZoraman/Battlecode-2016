package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public abstract class ArchonGoalBase extends Goal {

	public ArchonGoalBase(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		rc.broadcastSignal(rc.getType().sensorRadiusSquared);
		return null;
	}
}
