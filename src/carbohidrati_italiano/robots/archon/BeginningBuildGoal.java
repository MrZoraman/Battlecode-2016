package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.robots.EmptyGoal;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class BeginningBuildGoal implements Goal {

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		rc.build(Direction.NORTH, RobotType.SCOUT);
		return new EmptyGoal();
	}

}
