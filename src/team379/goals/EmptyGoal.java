package team379.goals;

import battlecode.common.RobotController;

/**
 * Goal that does nothing.
 * @author Matt
 *
 */
public class EmptyGoal implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "Empty Goal";
	}

}
