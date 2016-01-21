package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Signal;
import team379.pathfinding.ArchonLocateResult;
import team379.pathfinding.PathFindUtils;
import team379.robots.Robot;
import team379.robots.RobotMemory;
import team379.robots.Signals;

public class WaitForArchonWhoisGoal extends Goal {
	
	private final RobotGoalConstructor rgc;
	
	private int archonId = -1;
	
	public WaitForArchonWhoisGoal(int opponentAggressionRange, int patrolRadius, RobotGoalConstructor rgc) {
		super(new RobotMemory(-1, opponentAggressionRange, patrolRadius));
		this.rgc = rgc;
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
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(4);
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, archonId, nearbyRobots, null);
		
		RobotMemory mem = new RobotMemory(archonId, memory.getOpponentAggressionRange(), memory.getPatrolRadius());
		mem.setLastKnownArchonLocation(alr.getLocation());
		return rgc.createGoal(mem);
	}

	@Override
	public String getName() {
		return "Protect Archon";
	}
}
