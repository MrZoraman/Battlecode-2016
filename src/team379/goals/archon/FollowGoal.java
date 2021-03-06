package team379.goals.archon;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Globals;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.goals.PatrolAroundArchonGoalBase;
import team379.signals.SignalData;
import team379.signals.SignalReader;
import team379.signals.SignalType;

public class FollowGoal extends PatrolAroundArchonGoalBase {
	
	private static RobotType nextRobot = null;
	private static RobotFactory rf = new RobotFactory();

	private static final int babyThreshold = 15;
	private static int babyCoolDown = babyThreshold;

	public FollowGoal(RobotType type) {
		super(type);
		//super(null);
		//super(rc);
		orbiter.setRubbleThreshold(Globals.RUBBLE_THRESHOLD_MIN());
//		System.out.println("following archon: " + RobotMemory.getArchonId());
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		RobotInfo newArchon = findNewLeader(rc);
		if(newArchon != null) {
//			System.out.println("becomming a follower!");
			RobotMemory.setArchonId(newArchon.ID);
			RobotMemory.setArchonLocation(newArchon.location);
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
			orbiter.setCenter(newArchon.location);
			
			//return new FollowGoal(rc);
			//return new FollowGoal(rc.getType());
		}
		
		RobotInfo[] bots = rc.senseNearbyRobots(2, Team.NEUTRAL);
		if(bots.length > 0) {
			return new ActivateGoal(new FollowGoal(rc.getType()));
		}
		
		SignalReader.consume(rc, this);
		
		if(nextRobot == null) {
			nextRobot = rf.nextBot();
		}
		
		if(babyCoolDown >= babyThreshold 
				&& ArchonUtils.findPlaceAndBuild(rc, Direction.NORTH, nextRobot) != null) {
			nextRobot = rf.nextBot();
			babyCoolDown = 0;
		}
		
		babyCoolDown++;
		
		move(rc);
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
	
	@Override
	public void consume(SignalData data) {
		//System.out.println("follow consume: " + data.getType() + " from " + data.getSenderId());
		if(data.getType() == SignalType.THIS_IS_MY_ID) {
			short archonId = data.getOtherInfo();
			if(archonId == RobotMemory.getArchonId()) {
				//al.updateArchonLocation(data.getLocation());
				orbiter.setCenter(data.getLocation());
			}
		} else if (data.getType() == SignalType.NEW_LEADER) {
			System.out.println("-------------------------changing leaders.");
			if(data.getSenderId() == RobotMemory.getArchonId()) {
				short archonId = data.getOtherInfo();
				RobotMemory.setArchonId(archonId);
				MapLocation archonLocation = data.getLocation();
				RobotMemory.setArchonLocation(archonLocation);
				orbiter.setCenter(archonLocation);
			}
		}
	}
	
//	@Override
//	public void consume(SignalData data) {
//		if(data.getType() == SignalType.THIS_IS_MY_ID) {
//			short archonId = data.getOtherInfo();
//			if(archonId == RobotMemory.getArchonId()) {
//				//al.updateArchonLocation(data.getLocation());
//				orbiter.setCenter(data.getLocation());
//			}
//		} else if (data.getType() == SignalType.NEW_LEADER) {
//			System.out.println("changing leaders.");
//			if(data.getSenderId() == RobotMemory.getArchonId()) {
//				short archonId = data.getOtherInfo();
//				RobotMemory.setArchonId(archonId);
//				MapLocation archonLocation = data.getLocation();
//				RobotMemory.setArchonLocation(archonLocation);
//				orbiter.setCenter(archonLocation);
//			}
//		}
//	}
}
