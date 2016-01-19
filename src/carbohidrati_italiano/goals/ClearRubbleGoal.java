package carbohidrati_italiano.goals;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class ClearRubbleGoal extends Goal {
	
	public ClearRubbleGoal(RobotMemory memory, Direction rubbleDir) {
		super(memory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Clearing rubble.";
	}

}
