package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import carbohidrati_italiano.pathfinding.PathFinder;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public abstract class Goal
{
	protected final RobotMemory memory;
	protected final PathFinder pathFinder;
	
	public Goal(RobotMemory memory) {
		this.memory = memory;
		this.pathFinder = new PathFinder();
	}
	
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		return null;
	};
	
	public abstract String getName();
}
