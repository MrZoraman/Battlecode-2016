package carbohidrati_italiano.robots.guard;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;
import carbohidrati_italiano.robots.Signals;

public class InitialGoal implements Goal {
	
	private int archonId = -1;

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		Signal[] signals = rc.emptySignalQueue();
		
		for(Signal s : signals) {
			int[] messages = s.getMessage();
			if(messages == null) {
				continue;
			}
			
			Signals id = Signals.toSignal(messages[0]);
			if(id == null) {
				continue;
			}
			
			switch(id) {
			case THIS_IS_MY_ID:
				if(archonId < 0) {
					archonId = messages[1];
					rc.setIndicatorString(1, "My Archon is at: " + archonId);
				}
				break;
			default:
				continue;
			}
		}
		
		return new PatrolAroundArchonGoal(archonId);
	}

	@Override
	public String getName() {
		return "Protect Archon";
	}
}
