package carbohidrati_italiano.robots.guard;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;

public class Guard extends RobotBase{


	public Guard() {
		super(new ProtectArchonGoal());
	}

	@Override
	public void init(RobotController rc) throws Exception {
		
	}

}
