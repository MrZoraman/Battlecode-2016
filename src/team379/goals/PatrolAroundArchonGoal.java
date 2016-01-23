package team379.goals;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
import team379.OrbitCalculator;
import team379.Robot;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.signals.SignalData;
import team379.signals.SignalType;

public class PatrolAroundArchonGoal implements Goal {
	private final Orbiter orbiter;
	
	private Goal nextGoal = null;
	
	public PatrolAroundArchonGoal(MapLocation center) {
		orbiter = new Orbiter(center, 5, 2);			//TODO: thems be some magic numbers
	}
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
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
			} else if(ri.team != myTeam && myLocation.distanceSquaredTo(ri.location) < memory.getOpponentAggressionRange()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void move(RobotController rc, MapLocation archonLocation) throws Exception {
		PathFindResult result = orbiter.move(rc);
		System.out.println("move result: " + result);
		
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
		
		nextGoal = new ClearRubbleGoal(memory, dir);
		return true;
	}
}
