package carbohidrati_italiano.robots.archon;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import carbohidrati_italiano.dstarlite.DStarLite;
import carbohidrati_italiano.robots.RobotBase;

public class Archon extends RobotBase {
	
	public Archon() {
		super(new StandardGoal());
	}
    
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
				DStarLite pf = new DStarLite();
				
			}
		}
		
	}
	
	protected boolean isHeadArchon() {
		return headArchon;
	}

}
