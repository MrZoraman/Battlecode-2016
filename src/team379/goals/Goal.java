package team379.goals;

import battlecode.common.RobotController;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public abstract class Goal
{
	protected final RobotMemory memory;
	
	public Goal(RobotMemory memory) {
		this.memory = memory;
	}
	
	public abstract Goal achieveGoal(RobotController rc, Robot robot) throws Exception;
	
	public abstract String getName();
}
