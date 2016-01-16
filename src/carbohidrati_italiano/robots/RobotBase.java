package carbohidrati_italiano.robots;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.Team;

public abstract class RobotBase {
	
	RobotBase() { }
	
	public abstract void doWork(RobotController rc) throws Exception;
	public abstract void init(RobotController rc) throws Exception;
	
	protected Team myTeam;
	protected Team enemyTeam;
	
	public final void run(RobotController rc) {
		try {
			myTeam = rc.getTeam();
			enemyTeam = myTeam.opponent();
			init(rc);
			while(true) {
				doWork(rc);
				Clock.yield();
			}
		} catch (Exception e) {
			
		}
	}
}
