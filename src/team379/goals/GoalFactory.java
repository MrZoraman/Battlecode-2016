package team379.goals;

import battlecode.common.RobotController;
import team379.Globals;
import team379.RobotMemory;
import team379.goals.scout.ScoutPatrolGoal;

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
		RobotMemory.setOrbitConstant(Globals.INITIAL_ORBIT_CONSTANT());
		switch(rc.getType()) {
		case ARCHON:
			break;
		case GUARD:
			RobotMemory.setAggressionRange(Globals.GUARD_AGGRESSION_RANGE());
			return createPatrolGoal(rc);
		case SCOUT:
			return new ScoutPatrolGoal(rc);
		case SOLDIER:
			RobotMemory.setAggressionRange(Globals.SOLDIER_AGGRESSION_RANGE());
			return createPatrolGoal(rc);
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
	
	public static PatrolAroundArchonGoalBase createPatrolGoal(RobotController rc) {
		return new PatrolAroundArchonPassively(rc);
	}
}
