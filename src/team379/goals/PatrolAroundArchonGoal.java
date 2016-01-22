package team379.goals;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
import team379.OrbitCalculator;
import team379.pathfinding.ArchonLocateResult;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.robots.Robot;
import team379.robots.RobotMemory;
import team379.signals.SignalData;
import team379.signals.SignalType;

public class PatrolAroundArchonGoal extends ArchonListenerGoal {
	private final Orbiter orbiter;
	
	private Goal nextGoal = null;
	
	public PatrolAroundArchonGoal(RobotMemory memory) {
		super(memory);
		OrbitCalculator oc = new OrbitCalculator(memory.getPatrolRadius(), memory.getType());
		this.orbiter = new Orbiter(oc.getCalculatedRadius(), memory.getLastPatrolOrdinal());
		orbiter.setRadiusVariability(oc.getCalculatedRange());
		//orbiter.setRouteMovesUntilFail(4);
	}
	
	@Override
	public Goal achieveGoal(RobotController rc, Robot robot) throws Exception {
		super.achieveGoal(rc, robot);
		
		for(SignalData sd : signals) {
			if(sd.getType() == SignalType.NEW_RADIUS) {
				OrbitCalculator oc = new OrbitCalculator(sd.getOtherInfo(), memory.getType());
				orbiter.setRadius(oc.getCalculatedRadius());
				orbiter.setRadiusVariability(oc.getCalculatedRange());
			}
		}
		signals.clear();
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		
		ArchonLocateResult alr = ArchonLocator.findArchonLocation(rc, memory, nearbyRobots, memory.getLastKnownArchonLocation());
		
		if(alr.foundTheArchon()) {
			memory.setLastKnownArchonLocation(alr.getLocation());
		}
		
		if(findBaddies(rc, nearbyRobots)) {
			return new DefenseGoal(memory);
		}
		
		//if the archon is out of my assigned radius, go back to where I should be
		if(rc.getLocation().distanceSquaredTo(memory.getLastKnownArchonLocation()) > Math.pow(orbiter.getRadius() + orbiter.getRadiusVariability(), 2)) {
//			System.out.println("too far away!");
//			System.out.println("   distance: " + rc.getLocation().distanceSquaredTo(memory.getLastKnownArchonLocation()));
//			System.out.println("   orbiter radius: " + orbiter.getRadius());
//			System.out.println("   orbiter radius variability: " + orbiter.getRadiusVariability());
			return new ReturnToArchonGoal(memory);
		}
		
		move(rc, alr.getLocation());
		memory.setLastPatrolOrdinal(orbiter.getDirectionIndex());
		
		return nextGoal;
	}

	@Override
	public String getName() {
		return "Patrollin'";
	}
	
	private boolean findBaddies(RobotController rc, RobotInfo[] nearbyRobots) {
		MapLocation myLocation = rc.getLocation();
		Team myTeam = rc.getTeam();
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.team == Team.ZOMBIE) {
				if(ri.type == RobotType.ZOMBIEDEN) {
					continue;
				}
				return true;
			} else if(ri.team != myTeam && myLocation.distanceSquaredTo(ri.location) < memory.getOpponentAggressionRange()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void move(RobotController rc, MapLocation archonLocation) throws Exception {
		PathFindResult result = orbiter.move(rc, archonLocation);
		System.out.println("move result: " + result);
		
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			if(!determineDestructibleRubble(rc, rc.getLocation())) {
				//orbiter.calculateTarget(archonLocation);
			}
			break;
		case ROBOT_IN_WAY:
			break;
		case ROBOT_IN_WAY_AND_NOT_MOVING:
			//orbiter.calculateTarget(archonLocation);
			break;
		case STUCK:
			//orbiter.calculateTarget(archonLocation);
			break;
		case SUCCESS:
			break;
		default:
			break;
		
		}
	}
	
	private boolean determineDestructibleRubble(RobotController rc, MapLocation myLocation) {
		Direction[] dirs = new Direction[3];
		dirs[0] = myLocation.directionTo(orbiter.getTarget());
		dirs[1] = dirs[0].rotateLeft();
		dirs[2] = dirs[1].rotateRight();
		
		Direction dir = null;
		for(int ii = 0; ii < dirs.length; ii++) {
			double rubble = rc.senseRubble(myLocation.add(dirs[ii]));
			
			if(rubble > Globals.RUBBLE_THRESHOLD_MIN && rubble < Globals.RUBBLE_THRESHOLD_MAX) {
				dir = dirs[ii];
				break;
			}
		}
		
		if(dir == null) {
			return false;
		}
		
		nextGoal = new ClearRubbleGoal(memory, dir);
		return true;
	}
}
