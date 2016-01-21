package team379.goals.scout;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;
import team379.robots.Signals;

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
		return "scout goal base";
	}

}
