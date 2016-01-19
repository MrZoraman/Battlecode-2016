package carbohidrati_italiano.goals;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class ClearRubbleGoal extends Goal {
	
	private RobotMemory memory;
	private Direction dir;
	public ClearRubbleGoal(RobotMemory inMemory, Direction rubbleDir) {
		super(inMemory);
		dir = rubbleDir;
		memory = inMemory;
	}

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		int myAttackRange = rc.getType().attackRadiusSquared;
		RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, rc.getTeam().opponent());
        RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
        if (enemiesWithinRange.length > 0 || zombiesWithinRange.length > 0) {
        	return new DefenseGoal(memory);
        } else if(rc.senseRubble(rc.getLocation().add(dir)) > Globals.RUBBLE_THRESHOLD_MIN) {
        	if(rc.isCoreReady()) {
        		rc.clearRubble(dir);
        	}
        	rc.setIndicatorString(2, "Clearing Rubble!");
        	return null;
        }
        rc.setIndicatorString(2, "Returning to Patrol");
        return new PatrolAroundArchonGoal(memory);
	}

	@Override
	public String getName() {
		return "Clearing rubble.";
	}

}
