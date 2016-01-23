package team379;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.goals.WaitForArchonWhoisGoal;

/**
 * Represents a Robot that periodically executes a goal.
 * @author Matt
 *
 */
public class Robot {
	
	/**
	 * The current goal that is being achieved. At first it's the WaitForArchonWhois goal.
	 */
	private Goal currentGoal;
	
	public Robot(Goal currentGoal) {
		this.currentGoal = currentGoal;
	}
	
	public Robot() {
		this(new WaitForArchonWhoisGoal());
	}
	
	/**
	 * Runs the robot. If this returns then the robot explodes.
	 * @param rc The RobotController for the robot.
	 */
	public final void run(RobotController rc) {
		while(true) {
			try {
				Goal newGoal = currentGoal.achieveGoal(rc);
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
