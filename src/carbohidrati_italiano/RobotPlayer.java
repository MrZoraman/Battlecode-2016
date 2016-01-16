package carbohidrati_italiano;

import battlecode.common.RobotController;
import carbohidrati_italiano.robots.RobotBase;
import carbohidrati_italiano.robots.RobotFactory;

public class RobotPlayer {

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    public static void run(RobotController rc) {
        RobotBase robot = RobotFactory.getRobot(rc.getType());
        if(robot != null) {
        	robot.run(rc);
        }
    }
}