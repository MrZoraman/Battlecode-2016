package team379.robots;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import team379.goals.Goal;

/**
 * Represents a Robot that periodically executes a goal.
 * @author Matt
 *
 */
public class Robot {
	
	/**
	 * The constructor. Takes in the initial goal.
	 * @param initialGoal The first goal to start working on.
	 */
	public Robot(Goal initialGoal) {
		currentGoal = initialGoal;
	}
	
	/**
	 * The current goal that is being achieved.
	 */
	private Goal currentGoal;
	
	/**
	 * Runs the robot. If this returns then the robot explodes.
	 * @param rc The RobotController for the robot.
	 */
	public final void run(RobotController rc) {
		while(true) {
			try {
				Goal newGoal = currentGoal.achieveGoal(rc, this);
				if(newGoal != null) {
					currentGoal = newGoal;
					updateGoalString(rc, newGoal);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				Clock.yield();
			}
		}
	}
	
	/**
	 * Updates the goal string.
	 * @param rc The RobotController
	 * @param goal The goal to represent in the top bar.
	 */
	private void updateGoalString(RobotController rc, Goal goal) {
		rc.setIndicatorString(0, "Goal: " + goal.getName());
	}
}
