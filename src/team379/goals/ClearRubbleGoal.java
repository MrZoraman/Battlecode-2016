package team379.goals;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import team379.Globals;

public class ClearRubbleGoal implements Goal {
	
	private Direction dir;
	
	public ClearRubbleGoal(Direction rubbleDir) {
		dir = rubbleDir;
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		int myAttackRange = rc.getType().attackRadiusSquared;
		RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, rc.getTeam().opponent());
        RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
        if (enemiesWithinRange.length > 0 || zombiesWithinRange.length > 0) {
        	return new DefenseGoal();
        } else if(rc.senseRubble(rc.getLocation().add(dir)) > Globals.RUBBLE_THRESHOLD_MIN) {
        	if(rc.isCoreReady()) {
        		rc.clearRubble(dir);
        	}
        	rc.setIndicatorString(2, "Clearing Rubble!");
        	return null;
        }
        rc.setIndicatorString(2, "Returning to Patrol");
        return new PatrolAroundArchonGoal();
	}

	@Override
	public String getName() {
		return "Clearing rubble.";
	}

}
