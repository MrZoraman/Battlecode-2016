package team379.goals;

import battlecode.common.RobotController;
import team379.pathfinding.PathFinder;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public abstract class Goal
{
	protected final RobotMemory memory;
	protected final PathFinder pathFinder;
	
	public Goal(RobotMemory memory) {
		this.memory = memory;
		this.pathFinder = new PathFinder();
	}
	
	public abstract Goal achieveGoal(RobotController rc, Robot robot) throws Exception;
	
	public abstract String getName();
}
