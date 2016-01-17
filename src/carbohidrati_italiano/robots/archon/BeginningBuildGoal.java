package carbohidrati_italiano.robots.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import carbohidrati_italiano.robots.EmptyGoal;
import carbohidrati_italiano.robots.Goal;
import carbohidrati_italiano.robots.RobotBase;
import carbohidrati_italiano.robots.Signals;

public class BeginningBuildGoal implements Goal {
	
	private int guardsToMake = -1;
	
	private boolean hasScout = false;
	private int guardsMade = 0;

	@Override
	public Goal achieveGoal(RobotController rc, RobotBase robot) throws Exception {
		if(!rc.isCoreReady()) {
			return null;
		}
		
		if(guardsToMake < 0) {
			guardsToMake = calculateGuardsToMake(rc);
			rc.setIndicatorString(1, "I am making " + guardsToMake + " gaurds.");
		}
		
		
		if(!hasScout) {
			Direction scoutBuilt = ArchonUtils.findPlaceAndBuild(rc, RobotType.SCOUT);
			
			if(scoutBuilt != null) {
				hasScout = true;
				return null;
			} else {
				//uh oh, the archon is trapped!
				return new Goal() {
					@Override public String getName() {
						return "Trapped!";
					}
				};
			}
		}
		
		if(guardsMade < guardsToMake) {
			if(!rc.hasBuildRequirements(RobotType.GUARD)) {
				return null;
			}
			
			Direction guardBuilt = ArchonUtils.findPlaceAndBuild(rc, RobotType.GUARD);
			if(guardBuilt != null) {
				guardsMade++;
				rc.broadcastMessageSignal(Signals.THIS_IS_MY_ID.getValue(), rc.getID(), 2);
				return null;
			} else {
				//no more room for guards
				return new EmptyGoal();
			}
		}
		
		
		return new EmptyGoal();
	}

	@Override
	public String getName() {
		return "Beginning Build Goal";
	}
	
	private int calculateGuardsToMake(RobotController rc) {
		MapLocation[] archons = rc.getInitialArchonLocations(rc.getTeam());
		MapLocation myLocation = rc.getLocation();
		
		double closestArchonDistance = Double.MAX_VALUE;
		
		for(MapLocation archonLoc : archons) {
			if(archonLoc.equals(myLocation)) {
				continue;
			}
			
			double distance = myLocation.distanceSquaredTo(archonLoc);
			if(distance < closestArchonDistance) {
				closestArchonDistance = distance;
			}
		}
		
		rc.setIndicatorString(2, "closest archon distance: " + closestArchonDistance);
		
		if (closestArchonDistance < 16) {
			return 3;
		} else if (closestArchonDistance < 120) {
			return 4;
		} else {
			return 5;
		}
	}

}
