package carbohidrati_italiano;

import battlecode.common.Direction;
import battlecode.common.RobotType;

public class Globals {
	private Globals() { }
	
	public static final Direction[] movableDirections = {
		Direction.NORTH, 
		Direction.NORTH_EAST, 
		Direction.EAST, 
		Direction.SOUTH_EAST,
        Direction.SOUTH, 
        Direction.SOUTH_WEST, 
        Direction.WEST, 
        Direction.NORTH_WEST
    };
	
	public static final RobotType[] playerTypes = {
		RobotType.SCOUT,
		RobotType.SOLDIER,
		RobotType.SOLDIER,
		RobotType.SOLDIER,
        RobotType.GUARD,
        RobotType.GUARD,
        RobotType.VIPER,
        RobotType.TURRET
	};
}
