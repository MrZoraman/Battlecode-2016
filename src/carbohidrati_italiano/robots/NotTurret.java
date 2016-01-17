package carbohidrati_italiano.robots;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class NotTurret extends RobotBase {
	
	public NotTurret(Goal initialGoal) {
		super(initialGoal);
		// TODO Auto-generated constructor stub
	}

	Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
    RobotType[] robotTypes = {RobotType.SCOUT, RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.GUARD, RobotType.GUARD, RobotType.VIPER, RobotType.TURRET};
    
    Random rand;
    int myAttackRange = 0;

//	@Override
//	public void doWork(RobotController rc) throws Exception {
//		int fate = rand.nextInt(1000);
//
//        if (fate % 5 == 3) {
//            // Send a normal signal
//            rc.broadcastSignal(80);
//        }
//
//        boolean shouldAttack = false;
//
//        // If this robot type can attack, check for enemies within range and attack one
//        if (myAttackRange > 0) {
//            RobotInfo[] enemiesWithinRange = rc.senseNearbyRobots(myAttackRange, enemyTeam);
//            RobotInfo[] zombiesWithinRange = rc.senseNearbyRobots(myAttackRange, Team.ZOMBIE);
//            if (enemiesWithinRange.length > 0) {
//                shouldAttack = true;
//                // Check if weapon is ready
//                if (rc.isWeaponReady()) {
//                    rc.attackLocation(enemiesWithinRange[rand.nextInt(enemiesWithinRange.length)].location);
//                }
//            } else if (zombiesWithinRange.length > 0) {
//                shouldAttack = true;
//                // Check if weapon is ready
//                if (rc.isWeaponReady()) {
//                    rc.attackLocation(zombiesWithinRange[rand.nextInt(zombiesWithinRange.length)].location);
//                }
//            }
//        }
//
//        if (!shouldAttack) {
//            if (rc.isCoreReady()) {
//                if (fate < 600) {
//                    // Choose a random direction to try to move in
//                    Direction dirToMove = directions[fate % 8];
//                    // Check the rubble in that direction
//                    if (rc.senseRubble(rc.getLocation().add(dirToMove)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
//                        // Too much rubble, so I should clear it
//                        rc.clearRubble(dirToMove);
//                        // Check if I can move in this direction
//                    } else if (rc.canMove(dirToMove)) {
//                        // Move
//                        rc.move(dirToMove);
//                    }
//                }
//            }
//        }
//	}

	@Override
	public void init(RobotController rc) throws Exception {
		rand = new Random(rc.getID());
	    myAttackRange = rc.getType().attackRadiusSquared;
	}

}
