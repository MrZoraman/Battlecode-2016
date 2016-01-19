package carbohidrati_italiano.goals;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class MoveAwayGoal extends Goal{

	private int directionIndex = -1;
	public MoveAwayGoal(RobotMemory memory) {
		super(memory);
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		rc.pack();
		
		return null;
	}

	@Override
	public String getName() {
		return "Moving Away!";
	}

}
