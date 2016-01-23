package team379.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 * Creates a goal for a robot given the robot's type and the archon location.
 * @author Matt
 *
 */
public class GoalFactory {
	/**
	 * Static class!
	 */
	private GoalFactory() { }
	
	public static Goal createGoal(RobotController rc, MapLocation archonLocation) {
		return new EmptyGoal();
	}
}
