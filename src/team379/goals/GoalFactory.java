package team379.goals;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team379.Globals;
import team379.RobotMemory;
import team379.goals.scout.ScoutPatrolGoal;
import team379.goals.turret.MoveAwayGoal;

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
	
	public static Goal createGoal(RobotController rc) throws GameActionException {
		switch(rc.getType()) {
		case ARCHON:
			break;
		case GUARD:
			RobotMemory.setAggressionRange(Globals.GUARD_AGGRESSION_RANGE());
			return createPatrolGoal(rc);
		case SCOUT:
			return new ScoutPatrolGoal(rc.getType());
		case SOLDIER:
			return createPatrolGoal(rc);
		case TTM:
			break;
		case TURRET:
			return new MoveAwayGoal(rc);
		case VIPER:
			break;
		default:
			break;
		}
		return new EmptyGoal();
	}
	
	public static PatrolAroundArchonGoalBase createPatrolGoal(RobotController rc) {
		return new PatrolAroundArchonPassively(rc.getType());
	}
}
