package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Goodies;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.ArchonPathFinder;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalConsumer;
import team379.signals.SignalData;
import team379.signals.SignalReader;
import team379.signals.SignalType;

public class LeadGoal extends ArchonGoalBase implements SignalConsumer {

	private ArchonPathFinder pf = new ArchonPathFinder();
	private RobotController rc;
	
	private short destinationGoodies = 0;
	
	private static final int MOVE_DELAY = 20;
	private int moveCooldown = MOVE_DELAY;

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		this.rc = rc;
		
		if(findNewLeader()) {
			return new FollowGoal();
		}
		
		SignalReader.consume(rc, this);
		
		short goodies = Goodies.scanGoodies(rc);
		if(pf.getTarget() == null || pf.isAtTarget() || goodies > destinationGoodies) {
			destinationGoodies = goodies;
			pf.setTarget(calculateTarget());
		}
		
		if(moveCooldown < MOVE_DELAY) {
			moveCooldown++;
			return null;
		}
		
		moveCooldown = 0;
		
//		short goodies = Goodies.scanGoodies(rc);
//		//get sidetracked
//		if(goodies > destinationGoodies) {
//			destinationGoodies = goodies;
//			pf.setTarget(calculateTarget());
//		}
		
		
		PathFindResult result = pf.move(rc);
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			//boost rubble breaking threshold
			break;
		case NO_TARGET:
			break;
		case RUBBLE_IN_WAY:
			break;
		case STUCK:
			break;
		case SUCCESS:
			break;
		case TRAPPED:
			RobotInfo[] bots = rc.senseNearbyRobots(2, Team.NEUTRAL);
			if(bots.length == 0 && rc.isCoreReady()) {
				for(RobotInfo ri : bots) {
					rc.activate(ri.location);
				}
			}
			break;
		default:
			break;
		}
		
		return null;
	}

	@Override
	public void consume(SignalData data) {
		if(data.getType() == SignalType.FOUND_STUFF) {
			short scoutGoodieCount = data.getOtherInfo();
			//System.out.println("message from scout! (goodie count: " + scoutGoodieCount + ")");
			short myGoodieCount = Goodies.scanGoodies(rc);
			//System.out.println("my goodie count: " + myGoodieCount);
			if(myGoodieCount > scoutGoodieCount) {
				pf.setTarget(calculateTarget());
				destinationGoodies = myGoodieCount;
			} else {
				pf.setTarget(data.getLocation());
				destinationGoodies = scoutGoodieCount;
			}
		}
	}
	
	private boolean findNewLeader() throws Exception {
		int lowestArchonId = rc.getID();
		RobotInfo newArchon = null;
		RobotInfo[] robots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo ri : robots) {
			if(ri.type != RobotType.ARCHON) {
				continue;
			}
			
			if(ri.ID < lowestArchonId) {
				lowestArchonId = ri.ID;
				newArchon = ri;
			}
		}
		
		if(newArchon != null) {
			//time to broadcast!
			SignalData sd = new SignalData(SignalType.NEW_LEADER, newArchon.location, (short) newArchon.ID);
			int[] data = sd.toInts();
			rc.broadcastMessageSignal(data[0], data[1], rc.getType().sensorRadiusSquared + 10);//TODO: magic number!
			return true;
		}
		
		return false;
	}
	
	private MapLocation calculateTarget() {
		RobotInfo[] robots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared);
		//see if there are any neutral archons
		MapLocation target = findClosestNeutral(robots, RobotType.ARCHON);
		if(target != null) {
			System.out.println("found neutral archon!");
			return target;
		}
		
		target = findClosestZombieDen(robots);
		if(target != null) {
			System.out.println("found zombie den!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.TURRET);
		if(target != null) {
			System.out.println("found turret!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.SOLDIER);
		if(target != null) {
			System.out.println("found soldier!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.GUARD);
		if(target != null) {
			System.out.println("found guard!");
			return target;
		}
		
		target = findClosestParts();
		if(target != null) {
			System.out.println("found parts!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.SCOUT);
		if(target != null) {
			System.out.println("found scout!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.VIPER);
		if(target != null) {
			System.out.println("found viper!");
			return target;
		}
		
		return null;
	}
	
	private MapLocation findClosestParts() {
		int closestDistanceSquared = Integer.MAX_VALUE;
		MapLocation closestParts = null;
		MapLocation[] partLocations = rc.sensePartLocations(rc.getType().sensorRadiusSquared);
		for(MapLocation partsLocation : partLocations) {
			int distanceSquared = rc.getLocation().distanceSquaredTo(partsLocation);
			if(distanceSquared < closestDistanceSquared) {
				closestDistanceSquared = distanceSquared;
				closestParts = partsLocation;
			}
		}
		
		return closestParts;
	}
	
	private MapLocation findClosestZombieDen(RobotInfo[] robots) {
		int closestDistanceSquared = Integer.MAX_VALUE;
		RobotInfo closestDen = null;
		for(RobotInfo ri : robots) {
			if(ri.team == rc.getTeam()) {
				continue;
			}
			
			if(ri.type != RobotType.ZOMBIEDEN) {
				continue;
			}
			
			int distanceSquared = rc.getLocation().distanceSquaredTo(ri.location);
			if(distanceSquared < closestDistanceSquared) {
				closestDistanceSquared = distanceSquared;
				closestDen = ri;
			}
		}
		
		if(closestDen != null) {
			Direction dirToDen = rc.getLocation().directionTo(closestDen.location);
			return closestDen.location.add(dirToDen.opposite(), RobotMemory.getOrbitConstant());
		}
		
		return null;
	}
	
	private MapLocation findClosestNeutral(RobotInfo[] robots, RobotType type) {
		int closestDistanceSquared = Integer.MAX_VALUE;
		RobotInfo closestNeutral = null;
		for(RobotInfo ri : robots) {
			if(ri.team == rc.getTeam()) {
				continue;
			}
			
			if(ri.team != Team.NEUTRAL || ri.type != type) {
				continue;
			}
			
			int distanceSquared = rc.getLocation().distanceSquaredTo(ri.location);
			if(distanceSquared < closestDistanceSquared) {
				closestDistanceSquared = distanceSquared;
				closestNeutral = ri;
			}
		}
		
		if(closestNeutral != null) {
			Direction dirToArchon = rc.getLocation().directionTo(closestNeutral.location);
			return closestNeutral.location.subtract(dirToArchon);
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Leading";
	}
}
