package team379.robots;

import battlecode.common.RobotType;

/**
 * Creates a robot of a specific type.
 * @author Matt
 *
 */
public class RobotFactory {
	/**
	 * Constructs and returns a Robot given the type of that robot.
	 * @param type The type of the robot.
	 * @return A Robot with the proper setup completed.
	 */
	public static Robot getRobot(RobotType type) {
		switch(type) {
		case ARCHON:
			break;
		case GUARD:
			break;
		case SCOUT:
			break;
		case SOLDIER:
			break;
		case TTM:
			break;
		case TURRET:
			break;
		case VIPER:
			break;
		default:
			return null;
		}
		return null;
	}
	
	/**
	 * Static class!
	 */
	private RobotFactory() { }
}
