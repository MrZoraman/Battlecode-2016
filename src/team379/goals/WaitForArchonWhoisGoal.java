package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Signal;
import team379.pathfinding.ArchonLocator;
import team379.robots.Robot;
import team379.signals.SignalType;

public class WaitForArchonWhoisGoal implements Goal {
	
	private int archonId = -1;
	
	public WaitForArchonWhoisGoal() {
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		Signal[] signals = rc.emptySignalQueue();
		
		for(Signal s : signals) {
			int[] messages = s.getMessage();
			if(messages == null) {
				continue;
			}
			
			SignalType id = SignalType.toSignal(messages[0]);
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
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(4);
		ArchonLocateResult alr = ArchonLocator.findArchonLocation(rc, memory, nearbyRobots, null);
		
		RobotMemory mem = new RobotMemory(archonId, memory.getOpponentAggressionRange(), rc.getType());
		mem.setLastKnownArchonLocation(alr.getLocation());
		return rgc.createGoal(mem);
	}

	@Override
	public String getName() {
		return "Protect Archon";
	}
}
