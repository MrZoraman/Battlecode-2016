package carbohidrati_italiano.robots;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class Turret extends RobotBase {
	
	Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
    RobotType[] robotTypes = {RobotType.SCOUT, RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.GUARD, RobotType.GUARD, RobotType.VIPER, RobotType.TURRET};
    
    Random rand;
    int myAttackRange = 0;
    Team myTeam;
    Team enemyTeam;

	@Override
	public void doWork(RobotController rc) throws Exception {
		if (rc.isWeaponReady()) {
            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, enemyTeam);
            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
            if (enemiesWithinRange.length > 0) {
                for (RobotInfo enemy : enemiesWithinRange) {
                    // Check whether the enemy is in a valid attack range (turrets have a minimum range)
                    if (rc.canAttackLocation(enemy.location)) {
                        rc.attackLocation(enemy.location);
                        break;
                    }
                }
            } else if (zombiesWithinRange.length > 0) {
                for (RobotInfo zombie : zombiesWithinRange) {
                    if (rc.canAttackLocation(zombie.location)) {
                        rc.attackLocation(zombie.location);
                        break;
                    }
                }
            }
        }
	}

	@Override
	public void init(RobotController rc) throws Exception {
		rand = new Random(rc.getID());
	    myTeam = rc.getTeam();
	    enemyTeam = myTeam.opponent();
	    myAttackRange = rc.getType().attackRadiusSquared;
	}

}
