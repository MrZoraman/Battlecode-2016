package team379.goals.scout;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team379.GoodieSearchResult;
import team379.Goodies;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.goals.PatrolAroundArchonGoalBase;
import team379.signals.SignalData;
import team379.signals.SignalType;

public class ScoutPatrolGoal extends PatrolAroundArchonGoalBase {
	
	private int goodieTotal = 0;
	private MapLocation goodieLocation = null;
	private int timeSinceLastBroadcast = 0;
	private static final int MAX_BROADCAST_SILENCE = 30;
	private int baddieCooldown = 0;

	public ScoutPatrolGoal(RobotType type) {
		super(type);
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		
		if(goodieLocation == null) {
			goodieLocation = rc.getLocation();
		}
		
		GoodieSearchResult result = Goodies.scanGoodies(rc);
		if(result.getGoodies() > goodieTotal) {
			goodieTotal = result.getGoodies();
			goodieLocation = result.getLocation();
		}
		
		
		if(goodieTotal >= Goodies.ZOMBIE_DEN.getValue() || timeSinceLastBroadcast >= MAX_BROADCAST_SILENCE) {
			broadcast(rc);
			timeSinceLastBroadcast = 0;
		} else {
			timeSinceLastBroadcast++;
		}
		
		
		
//		if(orbiter.isAtTarget()) {
//			targetsMet++;
//			short goodieTotal = Goodies.scanGoodies(rc);
//			if(this.goodieTotal < goodieTotal) {
//				this.goodieTotal = goodieTotal;
//				//this.goodiesDirection = RobotMemory.getArchonLocation().directionTo(rc.getLocation());
//				goodieLocation = rc.getLocation();
//			}
//		}
		
//		if(targetsMet > 8) {
//			targetsMet = 0;
////			short goodiesDirectionInt = -1;
////			for(short ii = 0; ii < Globals.movableDirections.length; ii++) {
////				if(Globals.movableDirections[ii] == goodiesDirection) {
////					goodiesDirectionInt = ii;
////				}
////			}
//			//System.out.println("my best location is " + goodieLocation + " with " + goodieTotal);
//			broadcast(rc);
//		}
		
		return null;
	}
	
	private void broadcast(RobotController rc) throws Exception {
		//System.out.println("broadcasting (with goodie count of " + goodieTotal + ".");
		SignalData sd = new SignalData(SignalType.FOUND_STUFF, goodieLocation, (short) goodieTotal);
		goodieTotal = 0;
		goodieLocation = null;
		int[] data = sd.toInts();
		rc.broadcastMessageSignal(data[0], data[1], rc.getLocation().distanceSquaredTo(RobotMemory.getArchonLocation()) + 50);//TODO: magic number!
	}

	@Override
	public String getName() {
		return "Patrolling";
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		if(baddies.length > 10) {
			if(baddieCooldown <= 0) {
				orbiter.calculateNextTarget(true);
				baddieCooldown = 30;
			} else {
				baddieCooldown--;
			}
		}
		return null;
	}
}
