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
	
	private int newArchonId = 0;
	private MapLocation newArchonLocation = null;
	
	private static final int MOVE_DELAY = 20;
	private int moveCooldown = MOVE_DELAY;

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		this.rc = rc;
		
		findNewLeader();
		if(newArchonLocation != null) {
			RobotMemory.setArchonId(newArchonId);
			RobotMemory.setArchonLocation(newArchonLocation);
			//time to broadcast!
			SignalData sd = new SignalData(SignalType.NEW_LEADER, newArchonLocation, (short) newArchonId);
			int[] data = sd.toInts();
			rc.broadcastMessageSignal(data[0], data[1], rc.getType().sensorRadiusSquared + 10);//TODO: magic number!
			return new FollowGoal(rc);
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
		
		
		
		
		PathFindResult result = pf.move(rc);
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			//boost rubble breaking threshold //TODO: 
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
			System.out.println("I'm trapped!");
			RobotInfo[] bots = rc.senseNearbyRobots(2, Team.NEUTRAL);
			if(bots.length > 0 && rc.isCoreReady()) {
				for(RobotInfo ri : bots) {
					rc.activate(ri.location);
				}
				pf.setTarget(calculateTarget());
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
	
	private void findNewLeader() throws Exception {
		int lowestArchonId = rc.getID();
		MapLocation lowestArchonLocation = null;
		RobotInfo[] robots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo ri : robots) {
			if(ri.type != RobotType.ARCHON) {
				continue;
			}
			
			if(ri.ID < lowestArchonId) {
				lowestArchonId = ri.ID;
				lowestArchonLocation = ri.location;
			}
		}
		
		
		
		if(lowestArchonLocation == null) {
			SignalReader.consume(rc, data -> {
				if(data.getType() == SignalType.THIS_IS_MY_ID) {
					int archonId = data.getOtherInfo();
					if(archonId < rc.getID()) {
						newArchonId = archonId;
						newArchonLocation = data.getLocation();
					}
				}
			});
		} else {
			newArchonId = lowestArchonId;
			newArchonLocation = lowestArchonLocation;
		}
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
			//System.out.println("found zombie den!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.TURRET);
		if(target != null) {
			//System.out.println("found turret!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.SOLDIER);
		if(target != null) {
			//System.out.println("found soldier!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.GUARD);
		if(target != null) {
			//System.out.println("found guard!");
			return target;
		}
		
		target = findClosestParts();
		if(target != null) {
			//System.out.println("found parts!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.SCOUT);
		if(target != null) {
			//System.out.println("found scout!");
			return target;
		}
		
		target = findClosestNeutral(robots, RobotType.VIPER);
		if(target != null) {
			//System.out.println("found viper!");
			return target;
		}
		
		//there is nothing interesting around me..
		
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
	
	private int calculateOrbitConstant(RobotController rc) {
		int robots = rc.getRobotCount();
		return 0;
	}

	@Override
	public String getName() {
		return "Leading";
	}
}
