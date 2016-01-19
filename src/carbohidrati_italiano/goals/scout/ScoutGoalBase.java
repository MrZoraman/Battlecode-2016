package carbohidrati_italiano.goals.scout;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import carbohidrati_italiano.goals.Goal;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;
import carbohidrati_italiano.robots.Signals;

public class ScoutGoalBase extends Goal {

	public ScoutGoalBase(RobotMemory memory) {
		super(memory);
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
				if(memory.getArchonId() <= 0) {
					memory.setArchonId(messages[1]);
					rc.setIndicatorString(1, "My Archon is at: " + memory.getArchonId());
				}
				break;
			default:
				continue;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

}
