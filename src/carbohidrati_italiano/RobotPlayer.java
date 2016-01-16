package carbohidrati_italiano;

import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.robots.Archon;
import carbohidrati_italiano.robots.NotTurret;
import carbohidrati_italiano.robots.Turret;

public class RobotPlayer {

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    public static void run(RobotController rc) {

        if (rc.getType() == RobotType.ARCHON) {
            Archon a = new Archon();
            a.run(rc);
        } else if (rc.getType() != RobotType.TURRET) {
            NotTurret nt = new NotTurret();
            nt.run(rc);
        } else if (rc.getType() == RobotType.TURRET) {
        	Turret t = new Turret();
        	t.run(rc);
        }
    }
}