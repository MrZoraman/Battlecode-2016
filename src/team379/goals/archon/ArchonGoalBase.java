package team379.goals.archon;

import battlecode.common.RobotController;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.signals.SignalData;
import team379.signals.SignalType;

public abstract class ArchonGoalBase extends Goal {

	public ArchonGoalBase(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		SignalData signalData = new SignalData(SignalType.THIS_IS_MY_ID, rc.getLocation(), (short) rc.getID());
		int[] data = signalData.toInts();
		rc.broadcastMessageSignal(data[0], data[1], memory.getBroadcastRadius());
		return null;
	}
	
	protected void boostBroadcastRadius(int squares) {
		double radius = Math.sqrt(memory.getBroadcastRadius());
		radius += squares;
		memory.setBroadcastRadius((int) Math.pow(radius, 2));
	}
}
