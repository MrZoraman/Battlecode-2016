package carbohidrati_italiano.goals;

import carbohidrati_italiano.robots.RobotMemory;

@FunctionalInterface
public interface RobotGoalConstructor {
	public Goal createGoal(RobotMemory memory);
}
