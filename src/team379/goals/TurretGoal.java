package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.pathfinding.ArchonLocateResult;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.PathFindUtils;
import team379.pathfinding.PathFinder;
import team379.robots.Robot;
import team379.robots.RobotMemory;

public class TurretGoal extends Goal{
	private final PathFinder pf = new PathFinder();

	public TurretGoal(RobotMemory mem) {
		super(mem);
	}
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		ArchonLocateResult alr = PathFindUtils.findArchonLocation(rc, memory.getArchonId(), nearbyRobots, memory.getLastKnownArchonLocation());
		
		int myAttackRange = rc.getType().attackRadiusSquared;
		RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, rc.getTeam().opponent());
        RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
        if(zombiesWithinRange.length > 0) {
        	if(rc.getType() == RobotType.TTM) {
    			rc.unpack();
    		}
        	if(rc.isWeaponReady()) {
        		rc.attackLocation(zombiesWithinRange[0].location);
        	}
        	return null;
        } else if (enemiesWithinRange.length > 0) {
        	if(rc.getType() == RobotType.TTM) {
    			rc.unpack();
    		}
        	if(rc.isWeaponReady()) {
        		rc.attackLocation(enemiesWithinRange[0].location);
        	}
        	return null;
        } else if(alr.foundTheArchon()) {
			memory.setLastKnownArchonLocation(alr.getLocation());
        	if(rc.getType() == RobotType.TURRET) {
        		rc.pack();
        	}
    		PathFindResult result = pf.move(rc, alr.getLocation());
    		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
    			pf.reset();
    		}
    		for(RobotInfo ri : nearbyRobots) {
    			if(ri.ID == memory.getArchonId()) {
    				return null;
    			}
    		}
        }
        return null;
	}
	@Override
	public String getName() {
		return "Being a Turret";
	}
}
