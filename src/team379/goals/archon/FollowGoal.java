package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team379.Globals;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.goals.PatrolAroundArchonGoalBase;
import team379.signals.SignalData;
import team379.signals.SignalType;

public class FollowGoal extends PatrolAroundArchonGoalBase {
	
	private RobotType nextRobot = null;
	private RobotFactory rf = new RobotFactory();

	private final int babyThreshold = 15;
	private int babyCoolDown = babyThreshold;

	public FollowGoal(RobotController rc) {
		super(rc);
		orbiter.setRubbleThreshold(Globals.RUBBLE_THRESHOLD_MIN());
		System.out.println("following archon: " + RobotMemory.getArchonId());
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		RobotInfo newArchon = findNewLeader(rc);
		if(newArchon != null) {
			RobotMemory.setArchonId(newArchon.ID);
			RobotMemory.setArchonLocation(newArchon.location);
			orbiter.setCenter(newArchon.location);
			//time to broadcast!
			SignalData sd = new SignalData(SignalType.NEW_LEADER, newArchon.location, (short) newArchon.ID);
			int[] data = sd.toInts();
			rc.broadcastMessageSignal(data[0], data[1], rc.getType().sensorRadiusSquared + 10);//TODO: magic number!
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
		
		super.achieveGoal(rc);
		
		return null;
	}

	@Override
	public String getName() {
		return "Making Babies";
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		return null;
	}

	private RobotInfo findNewLeader(RobotController rc) throws Exception {
		int lowestArchonId = RobotMemory.getArchonId();
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
		
		return newArchon;
	}
}
