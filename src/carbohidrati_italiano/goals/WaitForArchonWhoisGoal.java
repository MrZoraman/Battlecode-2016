package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.Signals;

public class WaitForArchonWhoisGoal implements Goal {
	
	private int archonId = -1;
	
	private final int opponentAggressionRange;
	
	public WaitForArchonWhoisGoal(int opponentAggressionRange) {
		this.opponentAggressionRange = opponentAggressionRange;
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
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
		
		return new PatrolAroundArchonGoal(archonId, opponentAggressionRange);
	}

	@Override
	public String getName() {
		return "Protect Archon";
	}
}
