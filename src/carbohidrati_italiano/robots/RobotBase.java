package carbohidrati_italiano.robots;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.Team;

public abstract class RobotBase{
	
	public RobotBase(Goal initialGoal) {
		currentGoal = initialGoal;
	}
	
	protected void init(RobotController rc) throws Exception { };
	
	protected Team myTeam;
	protected Team enemyTeam;
	
	private Goal currentGoal;
	
	public final void run(RobotController rc) {
		try {
			myTeam = rc.getTeam();
			enemyTeam = myTeam.opponent();
			init(rc);
			while(true) {
				Goal newGoal = currentGoal.achieveGoal(rc, this);
				if(newGoal != null) {
					currentGoal = newGoal;
				}
				Clock.yield();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Uh oh! I've exited!");
	}
}
