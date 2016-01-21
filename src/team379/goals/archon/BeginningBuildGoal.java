package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team379.goals.EmptyGoal;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;
import team379.robots.Signals;

public class BeginningBuildGoal extends Goal {
	
	public BeginningBuildGoal() {
		super(new RobotMemory(0, 0, 0));
	}

	private int guardsToMake = -1;
	
	private boolean hasScout = false;
	private int guardsMade = 0;
	
	private Direction lastPlacedDir = Direction.NORTH;

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		if(!rc.isCoreReady()) {
			return null;
		}
		
		if(guardsToMake < 0) {
//			guardsToMake = calculateGuardsToMake(rc);
			guardsToMake = 1000;
			rc.setIndicatorString(1, "I am making " + guardsToMake + " gaurds.");
		}
		
		
		if(!hasScout) {
			Direction scoutDir = ArchonUtils.findPlaceAndBuild(rc, Direction.NORTH, RobotType.GUARD);
			
			if(scoutDir != null) {
				hasScout = true;
				lastPlacedDir = scoutDir;
				return null;
			} else {
				//uh oh, the archon is trapped!
				return new BeginningBuildGoal();
			}
		}
		
		if(guardsMade < guardsToMake) {
			if(!rc.hasBuildRequirements(RobotType.GUARD)) {
				return null;
			}
			
			Direction guardDir = ArchonUtils.findPlaceAndBuild(rc, lastPlacedDir, RobotType.GUARD);
			if(guardDir != null) {
				guardsMade++;
				lastPlacedDir = guardDir;
				rc.broadcastMessageSignal(Signals.THIS_IS_MY_ID.getValue(), rc.getID(), 2);
				return null;
			} else {
				//no more room for guards
				return new BeginningBuildGoal();
			}
		}
		
		MapLocation lastKnownArchonLocation = rc.getInitialArchonLocations(rc.getTeam())[0];
		
		RobotMemory memory = new RobotMemory(0, 0, 0);
		memory.setLastKnownArchonLocation(lastKnownArchonLocation);
		return new LeadGoal(memory);
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
