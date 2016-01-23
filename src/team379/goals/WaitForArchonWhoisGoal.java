package team379.goals;

import battlecode.common.RobotController;
import team379.HeadArchonIdentifier;
import team379.signals.SignalReader;

/**
 * Waits for a notification from and archon containing the id. It then finds the location of the archon,
 * 	then passes the info into the {@link team379.goals.GoalFactory GoalFactory}.
 * @author Matt
 *
 */
public class WaitForArchonWhoisGoal implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		HeadArchonIdentifier hai = new HeadArchonIdentifier();
		//read the signals to see if the archonId has come through
		SignalReader.consume(rc, hai);
		
		if(hai.getArchonLocation() == null) {
			//no archon id yet, try again next turn.
			return null;
		}
		
		//I know who and where my archon is!
		//Time to let the goal factory do the rest...
		return GoalFactory.createGoal(rc, hai.getArchonLocation());
	}

	@Override
	public String getName() {
		return "Waiting for archon id...";
	}
}
