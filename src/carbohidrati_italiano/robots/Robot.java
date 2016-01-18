package carbohidrati_italiano.robots;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.Team;
import carbohidrati_italiano.goals.Goal;

public class Robot{
	
	public Robot(Goal initialGoal) {
		currentGoal = initialGoal;
	}
	
	protected Team myTeam;
	protected Team enemyTeam;
	
	private Goal currentGoal;
	
	public final void run(RobotController rc) {
		try {
			myTeam = rc.getTeam();
			enemyTeam = myTeam.opponent();
			updateGoalString(rc, currentGoal);
			while(true) {
				Goal newGoal = currentGoal.achieveGoal(rc, this);
				if(newGoal != null) {
					currentGoal = newGoal;
					updateGoalString(rc, newGoal);
				}
				Clock.yield();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Uh oh! I've exited!");
	}
	
	private void updateGoalString(RobotController rc, Goal goal) {
		rc.setIndicatorString(0, "Goal: " + goal.getName());
	}
}
