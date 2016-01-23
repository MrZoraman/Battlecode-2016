package team379.goals.turret;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFinder;

public class TurretGoal implements Goal{
	private final PathFinder pf = new PathFinder();
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		// alr = ArchonLocator.findArchonLocation(rc, memory.getArchonId(), nearbyRobots, memory.getLastKnownArchonLocation());
		
		int myAttackRange = rc.getType().attackRadiusSquared;
		RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, rc.getTeam().opponent());
        RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
        /*
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
        */
        return null;
	}
	@Override
	public String getName() {
		return "Being a Turret";
	}
}
