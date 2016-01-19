package carbohidrati_italiano.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import carbohidrati_italiano.pathfinding.ArchonLocateResult;
import carbohidrati_italiano.pathfinding.PathFindResult;
import carbohidrati_italiano.pathfinding.PathFindUtils;
import carbohidrati_italiano.robots.Robot;
import carbohidrati_italiano.robots.RobotMemory;

public class TurretGoal extends Goal{

	private RobotMemory memory;
	public TurretGoal(RobotMemory mem) {
		super(mem);
		memory = mem;
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
    		PathFindResult result = pathFinder.move(rc, alr.getLocation());
    		if(result != PathFindResult.SUCCESS && result != PathFindResult.CORE_DELAY) {
    			pathFinder.reset();
    		}
    		for(RobotInfo ri : nearbyRobots) {
    			if(ri.ID == memory.getArchonId()) {
    				return new TurretGoal(memory);
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
