package team379.goals.archon;

import battlecode.common.RobotController;
import team379.goals.Goal;
import team379.signals.SignalData;
import team379.signals.SignalType;

public abstract class ArchonGoalBase implements Goal {
	
	private static double broadcastRadius = 5;		//TODO: magic number!

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		SignalData signalData = new SignalData(SignalType.THIS_IS_MY_ID, rc.getLocation(), (short) rc.getID());
		int[] data = signalData.toInts();
		int radiusSquared = (int) Math.pow(broadcastRadius, 2);
		//System.out.println("broadcasting! " + radiusSquared);
		rc.broadcastMessageSignal(data[0], data[1], radiusSquared);
		return null;
	}
	
	protected void boostBroadcastRadius(double squares) {
		broadcastRadius += squares;
	}
}
