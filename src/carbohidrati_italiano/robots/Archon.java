package carbohidrati_italiano.robots;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Signal;
import battlecode.common.Team;

public class Archon extends RobotBase {
	
	Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
    RobotType[] robotTypes = {RobotType.SCOUT, RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.GUARD, RobotType.GUARD, RobotType.VIPER, RobotType.TURRET};
    
    int myAttackRange = 0;
    
	@Override
	public void doWork(RobotController rc) throws Exception {
	}

	@Override
	public void init(RobotController rc) throws Exception {
		MapLocation headArchonLocation = rc.getInitialArchonLocations(myTeam)[0];
		MapLocation myLocation = rc.getLocation();
		
		if(myLocation.equals(headArchonLocation)) {
			rc.setIndicatorString(0, "I am the head archon!");
		} else {
			rc.setIndicatorString(0, "I am NOT the head archon :(");
		}
		
	}

}
