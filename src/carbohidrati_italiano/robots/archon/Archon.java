package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.dstarlite.DStarLite;
import carbohidrati_italiano.robots.RobotBase;

public class Archon extends RobotBase {
	
	public Archon() {
		super(new StandardGoal());
	}

	Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
    RobotType[] robotTypes = {RobotType.SCOUT, RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.GUARD, RobotType.GUARD, RobotType.VIPER, RobotType.TURRET};
    
    int myAttackRange = 0;
    
    private boolean headArchon = false;

	@Override
	protected void init(RobotController rc) throws Exception {
		MapLocation[] archons = rc.getInitialArchonLocations(myTeam);
		MapLocation myLocation = rc.getLocation();
		
		if(myLocation.equals(archons[0])) {
			headArchon = true;
		}
		
		if(headArchon) {
			//start at one, because the head archon is at index 0 (that's me!)
			for(int ii = 1; ii < archons.length; ii++) {
				//DStarLite pf = new DStarLite();
				
			}
		}
		
	}
	
	protected boolean isHeadArchon() {
		return headArchon;
	}

}
