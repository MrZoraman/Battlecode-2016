package team379.goals.scout;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.signals.SignalType;

public class ScoutGoalBase implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		Signal[] signals = rc.emptySignalQueue();
		
		for(Signal s : signals) {
			int[] messages = s.getMessage();
			if(messages == null) {
				continue;
			}
			
			SignalType id = SignalType.toSignal((short) messages[0]);
			if(id == null) {
				continue;
			}
			
			switch(id) {
			case THIS_IS_MY_ID:
				if(RobotMemory.getArchonId() <= 0) {
					RobotMemory.setArchonId(messages[1]);
					rc.setIndicatorString(1, "My Archon is at: " + RobotMemory.getArchonId());
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
