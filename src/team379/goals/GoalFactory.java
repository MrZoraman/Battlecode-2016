package team379.goals;

import battlecode.common.RobotController;
import team379.Globals;
import team379.RobotMemory;

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
	
	public static Goal createGoal(RobotController rc) {
		switch(rc.getType()) {
		case ARCHON:
			break;
		case GUARD:
			RobotMemory.setAggressionRange(Globals.GUARD_AGGRESSION_RANGE);
			return new PatrolAroundArchonGoal(rc);
		case SCOUT:
			break;
		case SOLDIER:
			RobotMemory.setAggressionRange(Globals.SOLDIER_AGGRESSION_RANGE);
			return new PatrolAroundArchonGoal(rc);
		case TTM:
			break;
		case TURRET:
			break;
		case VIPER:
			break;
		default:
			break;
		}
		return new EmptyGoal();
	}
}
