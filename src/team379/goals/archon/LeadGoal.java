package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.GoodieSearchResult;
import team379.Goodies;
import team379.Pacer;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.ArchonPathFinder;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalConsumer;
import team379.signals.SignalData;
import team379.signals.SignalReader;
import team379.signals.SignalType;

public class LeadGoal extends ArchonGoalBase implements SignalConsumer {
	
	private static final int MOVE_PACE = 20;
	private static final int HIGH_VALUE_TARGET = Goodies.ZOMBIE_DEN.getValue();
	private static final int VERY_HIGH_VALUE_TARGET = Goodies.FRIENDLY_ARCHON.getValue();
	
	private final Pacer movePacer = new Pacer(MOVE_PACE, true);
	private final ArchonPathFinder pf = new ArchonPathFinder();
	
	private int targetValue = 0;
	
	private boolean iSeeZombieDen = false;
	
	private RobotController rc = null;
	
	private int newArchonId;
	private MapLocation newArchonLocation;
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		if(this.rc == null) {
			this.rc = rc;
		}

		SignalReader.consume(rc, this);

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
		
		//update zombie dens
		MapLocation zombieDen = findZombieDenAttackPosition(rc);
		if(zombieDen != null) {
			iSeeZombieDen = true;
			pf.setTarget(zombieDen);
		} else {
			iSeeZombieDen = false;
		}
		
		if(!iSeeZombieDen && (pf.getTarget() == null || pf.isAtTarget())) {
			targetValue = 0;
			pf.setTarget(null);
			GoodieSearchResult localGoodies = Goodies.scanGoodies(rc);
			if(localGoodies.getGoodies() > 0) {
				trySwitchTargets(localGoodies.getGoodies(), localGoodies.getLocation());
			}
		}
		
		if(movePacer.pace()) {
			if (pf.move(rc) == PathFindResult.TRAPPED) {
				RobotInfo[] bots = rc.senseNearbyRobots(2, Team.NEUTRAL);
				if(bots.length > 0 && rc.isCoreReady()) {
					for(RobotInfo ri : bots) {
						rc.activate(ri.location);
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public void consume(SignalData data) {
		if(data.getType() == SignalType.FOUND_STUFF) {
			trySwitchTargets(data.getOtherInfo(), data.getLocation());
		} else if (data.getType() == SignalType.THIS_IS_MY_ID) {
			int archonId = data.getOtherInfo();
			if(archonId < rc.getID()) {
				newArchonId = archonId;
				newArchonLocation = data.getLocation();
			}
		}
	}
	
	private void trySwitchTargets(int proposedTargetValue, MapLocation proposedLocation) {
		if(iSeeZombieDen) {
			return;
		}
		
		if(isHighValueTarget(targetValue)) {
			if(isHighValueTarget(proposedTargetValue) && !isVeryHighValueTarget(targetValue)) {
				MapLocation myLoc = rc.getLocation();
				int distanceSquaredToCurrentTarget = myLoc.distanceSquaredTo(pf.getTarget());
				int distanceSquaredToProposedTarget = myLoc.distanceSquaredTo(proposedLocation);
				if((distanceSquaredToCurrentTarget > distanceSquaredToProposedTarget) 
						|| (isVeryHighValueTarget(proposedTargetValue))) {
					targetValue = proposedTargetValue;
					pf.setTarget(proposedLocation);
				}
			}
		} else {
			targetValue = proposedTargetValue;
			pf.setTarget(proposedLocation);
		}
	}
	
	private MapLocation findZombieDenAttackPosition(RobotController rc) {
		RobotInfo[] zombies = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.ZOMBIE);
		int closestDenSquared = Integer.MAX_VALUE;
		MapLocation closestDen = null;
		for(RobotInfo zombie : zombies) {
			if(zombie.type != RobotType.ZOMBIEDEN) {
				continue;
			}
			
			int distanceSquared = rc.getLocation().distanceSquaredTo(zombie.location);
			if(distanceSquared < closestDenSquared) {
				closestDenSquared = distanceSquared;
				closestDen = zombie.location;
			}
		}
		
		if(closestDen == null) {
			return null;
		}
		
		Direction dirToDen = rc.getLocation().directionTo(closestDen);
		MapLocation attackPosition = closestDen.add(dirToDen.opposite(), RobotMemory.getOrbitConstant());
		return attackPosition;
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
		
		if(lowestArchonLocation != null) {
			newArchonId = lowestArchonId;
			newArchonLocation = lowestArchonLocation;
		}
		
//		if(lowestArchonLocation == null) {
//			SignalReader.consume(rc, data -> {
//				if(data.getType() == SignalType.THIS_IS_MY_ID) {
//					int archonId = data.getOtherInfo();
//					if(archonId < rc.getID()) {
//						newArchonId = archonId;
//						newArchonLocation = data.getLocation();
//					}
//				}
//			});
//		} else {
//			newArchonId = lowestArchonId;
//			newArchonLocation = lowestArchonLocation;
//		}
	}

	@Override
	public String getName() {
		return "Leading";
	}
	
	private boolean isHighValueTarget(int value) {
		return value >= HIGH_VALUE_TARGET;
	}
	
	private boolean isVeryHighValueTarget(int value) {
		return value >= VERY_HIGH_VALUE_TARGET;
	}
}
