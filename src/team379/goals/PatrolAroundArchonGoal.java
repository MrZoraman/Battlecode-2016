package team379.goals;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
import team379.RobotMemory;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalReader;
import team379.signals.consumers.HeadArchonIdentifier;

public class PatrolAroundArchonGoal implements Goal {
	
	private static Orbiter orbiter;
	
	private Goal nextGoal = null;
	
	public PatrolAroundArchonGoal() {
		if(orbiter == null) {
			orbiter = new Orbiter(RobotMemory.getArchonLocation(), 10.0, 3);	//TODO: them magic numbers!
		} else {
			orbiter.setTarget(RobotMemory.getArchonLocation());
		}
	}
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		HeadArchonIdentifier hai = new HeadArchonIdentifier();
		SignalReader.consume(rc, hai);
		
		if(hai.getArchonId() < RobotMemory.getArchonId()) {
			orbiter.setCenter(hai.getArchonLocation());
			RobotMemory.setArchonId(hai.getArchonId());
			RobotMemory.setArchonLocation(hai.getArchonLocation());
		}
		
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		if(findBaddies(rc, nearbyRobots)) {
			return new DefenseGoal();
		}
		
		move(rc);
		
		return null;
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
			} else if(ri.team != myTeam && myLocation.distanceSquaredTo(ri.location) < RobotMemory.getAggressionRange()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void move(RobotController rc) throws Exception {
		PathFindResult result = orbiter.move(rc);
		
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			if(!determineDestructibleRubble(rc, rc.getLocation())) {
				//orbiter.calculateTarget(archonLocation);
			}
			break;
		case TRAPPED:
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
		
		nextGoal = new ClearRubbleGoal(dir);
		return true;
	}
}
