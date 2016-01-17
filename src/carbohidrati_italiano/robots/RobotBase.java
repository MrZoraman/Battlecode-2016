package carbohidrati_italiano.robots;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.dstarlite.DStarLite;

public abstract class RobotBase{
	
	public RobotBase(Goal initialGoal) {
		currentGoal = initialGoal;
	}
	
	protected void init(RobotController rc) throws Exception { };
	
	protected Team myTeam;
	protected Team enemyTeam;
	
	private Goal currentGoal;
	
	private final DStarLite pf = new DStarLite();
	
	public final void run(RobotController rc) {
		try {
			MapLocation myLocation = rc.getLocation();
			pf.init(myLocation.x, myLocation.y, myLocation.x, myLocation.y);
			myTeam = rc.getTeam();
			enemyTeam = myTeam.opponent();
			updateGoalString(rc, currentGoal);
			init(rc);
			while(true) {
				updatePathFinder(rc);
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
	
	protected void updatePathFinder(RobotController rc) throws Exception {
		MapLocation myLocation = rc.getLocation();
		for(Direction dir : Globals.movableDirections) {
			MapLocation newLocation = myLocation.add(dir);
			if(!rc.onTheMap(newLocation)) {
				pf.updateCell(newLocation.x, newLocation.y, -1);
				continue;
			}
			double rubble = rc.senseRubble(newLocation);
			if(rubble > 100.0) {
				pf.updateCell(newLocation.x, newLocation.y, -1);
			}
		}
	}
	
	public DStarLite getPathFinder() {
		return pf;
	}
}
