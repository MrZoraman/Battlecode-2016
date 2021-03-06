package team379.goals.archon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
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
	
	/*
	 * scouts look for interesting things. They send back the location and value of ONE important thing.
	 * 
	 * archons are constantly looking in their immediate area for interesting things. They recieve locations
	 * from scouts for important things outside their range.
	 * 
	 * the archon steadily moves towards its target.
	 * 
	 * The archon has a threshold for what it considers "chump change", which is (less than) the value of a zombie den
	 * 
	 * If the archon's target value is below this threshold, then it is open to new targets suggested by the scouts.
	 * 
	 * If the archon has a high value target, it will only change targets if:
	 * 		the new target is high value and is closer and the current target is not very high value
	 * 		or
	 * 		the target is a very high value target (greater than or equal to the value of a friendly archon)
	 * 
	 * If the archon can see a zombie den, it will not change targets until the zombie den is gone.
	 */
	
	private static final int MOVE_PACE = 20;
	private static final int HIGH_VALUE_TARGET = Goodies.ZOMBIE_DEN.getValue();
	private static final int VERY_HIGH_VALUE_TARGET = Goodies.FRIENDLY_ARCHON.getValue();
	
	private final Pacer movePacer = new Pacer(MOVE_PACE, true);
	private final ArchonPathFinder pf = new ArchonPathFinder();
	private final Set<MapLocation> destroyedDens = new HashSet<>();
	
	private static int targetValue = 0;
	
	private static boolean iSeeZombieDen = false;
	
	private static RobotController rc = null;
	
	private static int newArchonId;
	private static MapLocation newArchonLocation;
	
	private static int orbitConstant = Globals.INITIAL_ORBIT_CONSTANT();
	

	private static RobotType nextRobot = null;
	private static RobotFactory rf = new RobotFactory();

	private static final int babyThreshold = 15;
	private static int babyCoolDown = babyThreshold;
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		if(LeadGoal.rc == null) {
			LeadGoal.rc = rc;
		}

		SignalReader.consume(rc, this);

		findNewLeader();
		if(newArchonLocation != null) {
//			System.out.println("becomming a follower!");
			RobotMemory.setArchonId(newArchonId);
			RobotMemory.setArchonLocation(newArchonLocation);
//			//time to broadcast!
//			SignalData sd = new SignalData(SignalType.NEW_LEADER, newArchonLocation, (short) newArchonId);
//			int[] data = sd.toInts();
//			System.out.println("BROADCASTING.... NEW LEADER------------- (" + rc.getID() + ")");
//			rc.broadcastMessageSignal(data[0], data[1], 100);//TODO: magic number!
			
			SignalData signalData = new SignalData(SignalType.NEW_LEADER, RobotMemory.getArchonLocation(), (short) RobotMemory.getArchonId());
			int[] data = signalData.toInts();
			int radiusSquared = (int) Math.pow(35, 2);
			//System.out.println("broadcasting! " + radiusSquared);
			rc.broadcastMessageSignal(data[0], data[1], radiusSquared);
			//return null;
			
			
			//return new FollowGoal(rc);
			return new FollowGoal(rc.getType());
		}
		
		rc.setIndicatorString(2, "iSeeZombieDen: " + iSeeZombieDen);
		
		//if(!iSeeZombieDen) {
			//update zombie dens
			MapLocation zombieDen = findClosestZombieDen(rc);
			if(zombieDen != null) {
				MapLocation attackPosition = findZombieDenAttackPosition(rc, zombieDen);
//				System.out.println("found zombie den! My attack position is " + attackPosition);
				iSeeZombieDen = true;
				pf.setTarget(attackPosition);
				RobotMemory.setOrbitConstant((int) Math.sqrt(rc.getType().sensorRadiusSquared));
				destroyedDens.add(zombieDen);
//				rc.setIndicatorString(1, "target: " + pf.getTarget());
			} else {
				//System.out.println("setting seezombieden to false");
				iSeeZombieDen = false;
				RobotMemory.setOrbitConstant(orbitConstant);
				
			}
		//}
		
		//System.out.println("at target: " + pf.isAtTarget());
		if(!iSeeZombieDen && (pf.getTarget() == null || pf.isAtTarget())) {
//			System.out.println("time to find something else to do.");
//			System.out.println("I see zombie den: " + iSeeZombieDen);
//			System.out.println("target is null: " + (pf.getTarget() == null));
//			System.out.println("at target: " + pf.isAtTarget());
			targetValue = 0;
			pf.setTarget(null);
//			rc.setIndicatorString(1, "target: " + pf.getTarget());
			GoodieSearchResult localGoodies = Goodies.scanGoodies(rc);
			if(localGoodies.getGoodies() > 0) {
//				System.out.println("local goodies found: " + localGoodies.getLocation() + " (" + localGoodies.getGoodies() + ")");
				trySwitchTargets(localGoodies.getGoodies(), localGoodies.getLocation());
			}
		}
		
		RobotInfo[] bots = rc.senseNearbyRobots(2, Team.NEUTRAL);
		if(bots.length > 0) {
			return new ActivateGoal(new LeadGoal());
		}
		
		if(movePacer.pace()) {
			PathFindResult result = pf.move(rc);
			//System.out.println(result);
//			if (result == PathFindResult.TRAPPED) {
				
//				if(bots.length > 0 && rc.isCoreReady()) {
//					for(RobotInfo ri : bots) {
//						rc.activate(ri.location);
//					}
//				}
//			}
		}
		
		int newOrbitConstant = calculateOrbitConstant(rc);
		//System.out.println("orbit constant: " + newOrbitConstant + "(" + rc.getRobotCount() + ") robots");
		if(newOrbitConstant != orbitConstant) {
//			System.out.println("spreading out from " + orbitConstant + " to " + newOrbitConstant);
			orbitConstant = newOrbitConstant;
			RobotMemory.setOrbitConstant(newOrbitConstant);
			this.boostBroadcastRadius(2);
			
		}
		
		if(nextRobot == null) {
			nextRobot = rf.nextBot();
		}
		
		if(babyCoolDown >= babyThreshold 
				&& ArchonUtils.findPlaceAndBuild(rc, Direction.NORTH, nextRobot) != null) {
			nextRobot = rf.nextBot();
			babyCoolDown = 0;
		}
		
		babyCoolDown++;
		
		return null;
	}

	@Override
	public void consume(SignalData data) {
		//System.out.println("leader consume: " + data.getType() + " from " + data.getSenderId());
		if(data.getType() == SignalType.FOUND_STUFF) {
			trySwitchTargets(data.getOtherInfo(), data.getLocation());
		} else if (data.getType() == SignalType.THIS_IS_MY_ID) {
			int archonId = data.getOtherInfo();
			if(archonId < rc.getID()) {
				newArchonId = archonId;
				newArchonLocation = data.getLocation();
				//System.out.println("|||||||||||||||Foomf");
			}
		}
	}
	
	private void trySwitchTargets(int proposedTargetValue, MapLocation proposedLocation) {
		if(iSeeZombieDen || proposedTargetValue <= 0) {
			return;
		}
		
		if(destroyedDens.contains(proposedLocation)) {
//			System.out.println("...but it's just outdated zombieden info. discard!");
			return;
		}
		
//		System.out.println("I'm being proposed somethign! My current target value is " + targetValue);
		
		if(isHighValueTarget(targetValue)) {
			if(isHighValueTarget(proposedTargetValue) && !isVeryHighValueTarget(targetValue)) {
				MapLocation myLoc = rc.getLocation();
				int distanceSquaredToCurrentTarget = myLoc.distanceSquaredTo(pf.getTarget());
				int distanceSquaredToProposedTarget = myLoc.distanceSquaredTo(proposedLocation);
				if((distanceSquaredToCurrentTarget > distanceSquaredToProposedTarget) 
						|| (isVeryHighValueTarget(proposedTargetValue))) {
					targetValue = proposedTargetValue;
					pf.setTarget(proposedLocation);
//					System.out.println("target switched to: " + pf.getTarget() + " with a value of " + targetValue);
//					rc.setIndicatorString(1, "target: " + pf.getTarget());
				}
			}
		} else {
			targetValue = proposedTargetValue;
			pf.setTarget(proposedLocation);
//			System.out.println("target switched to: " + pf.getTarget() + " with a value of " + targetValue);
//			rc.setIndicatorString(1, "target: " + pf.getTarget());
		}
	}
	
	private MapLocation findClosestZombieDen(RobotController rc) {
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
		
		return closestDen;
	}
	
	private MapLocation findZombieDenAttackPosition(RobotController rc, MapLocation zombieDen) {
		Direction dirToDen = rc.getLocation().directionTo(zombieDen);
		MapLocation attackPosition = zombieDen.add(dirToDen.opposite(), RobotMemory.getOrbitConstant());
		return attackPosition;
	}
	
	private void findNewLeader() throws Exception {
		int lowestArchonId = rc.getID();
		MapLocation lowestArchonLocation = null;
		RobotInfo[] robots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo ri : robots) {
			if(ri.type != RobotType.ARCHON || ri.team != rc.getTeam()) {
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
			//System.out.println("+++++++++++++++++++++++++++++++++found new leader from search!");
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
	
	private int calculateOrbitConstant(RobotController rc) {
		int robotCount = rc.getRobotCount();
		if(robotCount > 100) {
			return 14;
		} else if(robotCount > 90) {
			return 13;
		} else if(robotCount > 80) {
			return 12;
		} else if(robotCount > 70) {
			return 11;
		} else if(robotCount > 60) {
			return 10;
		} else if(robotCount > 50) {
			return 9;
		} else if(robotCount > 40) {
			return 8;
		} else if (robotCount > 30) {
			return 7;
		} else if (robotCount > 20) {
			return 6;
		} else {
			return Globals.INITIAL_ORBIT_CONSTANT();
		}
	}
}
