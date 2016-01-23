package team379.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.signals.SignalReader;
import team379.signals.SignalType;

/**
 * Waits for a notication from and archon containing the id. It then finds the location of the archon,
 * 	then passes the info into the {@link team379.goals.GoalFactory GoalFactory}.
 * @author Matt
 *
 */
public class WaitForArchonWhoisGoal implements Goal {
	
	/**
	 * The archon id. -1 means no archon id has been found yet.
	 */
	private int archonId = -1;

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		//read the signals to see if the archonId has come through
		SignalReader.consume(rc, data -> {
			if(data.getType() == SignalType.THIS_IS_MY_ID) {
				archonId = data.getOtherInfo();
			}
		});
		
		if(archonId < 0) {
			//no archon id yet, try again next turn.
			return null;
		}
		
		//I know what my archon id is!
		
		//get friendly robots in range
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		
		//the location of my archon
		MapLocation archonLocation = null;
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == archonId) {
				archonLocation = ri.location;
			}
		}
		
		if(archonLocation == null) {
			//I didn't find my archon in range... I have no idea what to do so I'm going to wait here. Maybe I'll
			//sense the archon next round. (In theory flow control should never get to this point)
			return null;
		}
		
		//I know where my archon is!
		//Time to let the goal factory do the rest...
		return GoalFactory.createGoal(rc, archonLocation);
	}

	@Override
	public String getName() {
		return "Waiting for archon id...";
	}
}
