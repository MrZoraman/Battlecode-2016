package team379.goals;

import java.util.ArrayDeque;
import java.util.Queue;

import battlecode.common.RobotController;
import battlecode.common.Signal;
import team379.Robot;
import team379.RobotMemory;
import team379.signals.SignalData;
import team379.signals.SignalType;

public abstract class ArchonListenerGoal extends Goal {

	public ArchonListenerGoal(RobotMemory memory) {
		super(memory);
	}
	
	protected Queue<SignalData> signals = new ArrayDeque<>();

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		Signal[] signals = rc.emptySignalQueue();
		boolean heardFromArchon = false;
		int lowestArchonId = Integer.MAX_VALUE;
		for(Signal s : signals) {
			int[] data = s.getMessage();
			if(data.length < 0) {
				continue;
			}
			SignalData sdata = new SignalData(data[0], data[1]);
			if(sdata.getType() == SignalType.THIS_IS_MY_ID) {
				heardFromArchon = true;
				int archonId = s.getID();
				if(archonId < lowestArchonId) {
					memory.setLastKnownArchonLocation(sdata.getLocation());
					memory.setArchonId(archonId);
					lowestArchonId = archonId;
				}
			} else {
				this.signals.add(sdata);
			}
		}
		
		if(heardFromArchon) {
			memory.resetStaleness();
		} else {
			memory.incrementStaleness();
		}
		
		return null;
	}

}
