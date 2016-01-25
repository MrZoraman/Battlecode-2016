package team379;

import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team379.goals.archon.BeginningBuildGoal;

/**
 * Entry point for the code
 * @author Matt
 *
 */
public class RobotPlayer {

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    public static void run(RobotController rc) {
    	RobotMemory.setOrbitConstant(Globals.INITIAL_ORBIT_CONSTANT());
    	Robot robot;
    	if(rc.getType() == RobotType.ARCHON) {
    		robot = new Robot(new BeginningBuildGoal());
    	} else {
    		robot = new Robot();
    	}
    	robot.run(rc);
    }
}