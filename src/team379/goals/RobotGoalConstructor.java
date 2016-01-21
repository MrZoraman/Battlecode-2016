package team379.goals;

import team379.robots.RobotMemory;

@FunctionalInterface
public interface RobotGoalConstructor {
	public Goal createGoal(RobotMemory memory);
}
