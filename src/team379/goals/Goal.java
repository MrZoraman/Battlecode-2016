package team379.goals;

import battlecode.common.RobotController;

/**
 * A goal (represents something to do).
 * @author Matt
 *
 */
public interface Goal {
	/**
	 * Try to achieve the goal
	 * @param rc The RobotController
	 * @return The next goal to complete (or null to keep doing the same goal)
	 * @throws Exception If something goes wrong...
	 */
	public Goal achieveGoal(RobotController rc) throws Exception;
	
	/**
	 * Gets the name of the goal. This is displayed in the status window at index 0.
	 * @return The name of the goal.
	 */
	public String getName();
}
