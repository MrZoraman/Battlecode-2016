package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.signals.SignalType;

public class BeginningBuildGoal extends ArchonGoalBase {
	
	private Direction lastPlacedDir = Direction.NORTH;
	private BuildQueue buildQueue = new BuildQueue();
	private boolean init = false;
	private RobotType next = null;
	private BuildDistributor bd;

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		
		if(bd == null) {
			int myIndex = 0;
			MapLocation[] locs = rc.getInitialArchonLocations(rc.getTeam());
			for(int ii = 0; ii < locs.length; ii++) {
				if(locs[ii] == rc.getLocation()) {
					myIndex = ii;
				}
			}
			bd = new BuildDistributor(myIndex, locs.length);
		}
		
		if(!rc.isCoreReady()) {
			return null;
		}
		
		if(!init) {
			init(rc);
		}
		
		if(!bd.tryBuild()) {
			return null;
		}
		
		if(next == null) {
			next = buildQueue.getNextRobot();
		}
		
		if(next != null) {
			Direction dir = ArchonUtils.findPlaceAndBuild(rc, lastPlacedDir, next);
			
			if(dir != null) {
				lastPlacedDir = dir;
				next = null;
				rc.broadcastMessageSignal(SignalType.THIS_IS_MY_ID.getValue(), rc.getID(), 2);
			}
		}
		
		if(buildQueue.isEmpty()) {
			MapLocation lastKnownArchonLocation = rc.getInitialArchonLocations(rc.getTeam())[0];
			return new LeadGoal();
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Beginning Build Goal";
	}
	
	private void init(RobotController rc) {
		init = true;
		
		int friendlyArchons = rc.getInitialArchonLocations(rc.getTeam()).length;
		//buildQueue.setDelay(friendlyArchons);
		
		buildQueue.enqueue(RobotType.TURRET);

		int guardsToMake = calculateGuardsToMake(rc);
		for(int ii = 0; ii < guardsToMake; ii++) {
			buildQueue.enqueue(RobotType.GUARD);
		}
		
//		int soldiersToMake = 25;
//		for(int ii = 0; ii < soldiersToMake; ii++) {
//			buildQueue.enqueue(RobotType.SOLDIER);
//		}
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
