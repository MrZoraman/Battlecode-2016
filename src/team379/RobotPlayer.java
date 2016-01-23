package team379;

import battlecode.common.RobotController;

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
        Robot robot = new Robot();
        if(robot != null) {
        	robot.run(rc);
        }
    }
}