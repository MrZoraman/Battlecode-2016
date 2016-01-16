package carbohidrati_italiano.robots;

import battlecode.common.Clock;
import battlecode.common.RobotController;

public abstract class RobotBase {
	
	RobotBase() { }
	
	public abstract void doWork(RobotController rc) throws Exception;
	public abstract void init(RobotController rc) throws Exception;
	
	public final void run(RobotController rc) {
		try {
			init(rc);
			while(true) {
				doWork(rc);
				Clock.yield();
			}
		} catch (Exception e) {
			
		}
	}
}
