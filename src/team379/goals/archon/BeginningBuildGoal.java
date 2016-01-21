package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team379.goals.Goal;
import team379.robots.Robot;
import team379.robots.RobotMemory;
import team379.robots.Signals;

public class BeginningBuildGoal extends Goal {
	
	public BeginningBuildGoal() {
		super(new RobotMemory(0, 0, 0));
	}
	
	private Direction lastPlacedDir = Direction.NORTH;
	private BuildQueue buildQueue = new BuildQueue();
	private boolean init = false;
	private RobotType next = null;

	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		if(!rc.isCoreReady()) {
			return null;
		}
		
		if(!init) {
			init(rc);
		}
		
		if(next == null) {
			next = buildQueue.getNextRobot();
		}
		
		if(next != null) {
			Direction dir = ArchonUtils.findPlaceAndBuild(rc, lastPlacedDir, next);
			
			if(dir != null) {
				lastPlacedDir = dir;
				next = null;
				rc.broadcastMessageSignal(Signals.THIS_IS_MY_ID.getValue(), rc.getID(), 2);
			}
		}
		
		if(buildQueue.isEmpty()) {
			MapLocation lastKnownArchonLocation = rc.getInitialArchonLocations(rc.getTeam())[0];
			
			RobotMemory memory = new RobotMemory(0, 0, 0);
			memory.setLastKnownArchonLocation(lastKnownArchonLocation);
			return new LeadGoal(memory);
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
		buildQueue.setDelay(friendlyArchons);
		
		buildQueue.enqueue(RobotType.SCOUT);

		int guardsToMake = calculateGuardsToMake(rc);
		for(int ii = 0; ii < guardsToMake; ii++) {
			buildQueue.enqueue(RobotType.GUARD);
		}
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
