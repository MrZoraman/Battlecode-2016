package team379.goals;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team379.OrbitCalculator;
import team379.RobotMemory;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalConsumer;
import team379.signals.SignalData;
import team379.signals.SignalReader;
import team379.signals.SignalType;

public abstract class PatrolAroundArchonGoalBase implements Goal, SignalConsumer {
	
	/**
	 * How much to boost the rubble threshold if it must be raised due to being stuck.
	 */
	private static final int RUBBLE_BOOST_AMOUNT = 100;
	
	protected static Orbiter orbiter;
	private static RobotType type;
	//protected static ArchonLocator al;
	
	public PatrolAroundArchonGoalBase(RobotType type) {
		OrbitCalculator oc = new OrbitCalculator(RobotMemory.getOrbitConstant(), type);
		if(orbiter == null) {
			orbiter = new Orbiter(RobotMemory.getArchonLocation(), oc.calculateRadius());
			PatrolAroundArchonGoalBase.type = type;
		} else {
			orbiter.setCenter(RobotMemory.getArchonLocation());
			orbiter.setRadius(oc.calculateRadius());
		}
	}
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
//		if(al == null) {
//			al = new ArchonLocator(RobotMemory.getArchonLocation(), RobotMemory.getArchonId());
//		}
		//HeadArchonIdentifier hai = new HeadArchonIdentifier();
		SignalReader.consume(rc, this);
		
//		if(hai.getArchonId() < RobotMemory.getArchonId()) {
//			orbiter.setCenter(hai.getArchonLocation());
//			RobotMemory.setArchonId(hai.getArchonId());
//			RobotMemory.setArchonLocation(hai.getArchonLocation());
//		}
		
		
		RobotInfo[] nearbyRobots = rc.senseHostileRobots(rc.getLocation(), rc.getType().sensorRadiusSquared);
		if(nearbyRobots.length > 0) {
			Goal nextGoal = baddiesFound(rc, nearbyRobots);
			if(nextGoal != null) {
				return nextGoal;
			}
		}
		
		return move(rc);
	}
	
	protected abstract Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception;

	@Override
	public String getName() {
		return "Patrollin'";
	}
	
	protected Goal move(RobotController rc) throws Exception {
		PathFindResult result = orbiter.move(rc);
		
		if(orbiter.isAtTarget()) {
			orbiter.resetRubbleThreshold();
		}
		
		switch(result) {
		case CORE_DELAY:
			break;
		case COULD_NOT_FIND_ROUTE:
			orbiter.reset();
			orbiter.calculateNextTarget(true);
			break;
		case TRAPPED:
			orbiter.reset();
			orbiter.calculateNextTarget(true);
			break;
		case STUCK:
			orbiter.boostRubbleThreshold(RUBBLE_BOOST_AMOUNT);
			orbiter.reset();
			break;
		case SUCCESS:
			break;
		case RUBBLE_IN_WAY:
			return new ClearRubbleGoal(orbiter.getRubbleDirection());
		default:
			break;
		
		}
		
		return null;
	}
	
	@Override
	public void consume(SignalData data) {
		//System.out.println("consuming " + data.getType());
		if(data.getType() == SignalType.THIS_IS_MY_ID) {
			short archonId = data.getOtherInfo();
			if(archonId == RobotMemory.getArchonId()) {
				//al.updateArchonLocation(data.getLocation());
				orbiter.setCenter(data.getLocation());
			}
		} else if (data.getType() == SignalType.NEW_LEADER) {
			//if(data.getSenderId() == RobotMemory.getArchonId()) {
				//System.out.println("(my archon is " + RobotMemory.getArchonId() +")-------------------------changing leaders as requested by " + data.getSenderId() + " to " + data.getOtherInfo() + ".");
				short archonId = data.getOtherInfo();
				RobotMemory.setArchonId(archonId);
				MapLocation archonLocation = data.getLocation();
				RobotMemory.setArchonLocation(archonLocation);
				orbiter.setCenter(archonLocation);
			//}
		} else if (data.getType() == SignalType.SPREAD_OUT) {
			OrbitCalculator oc = new OrbitCalculator(data.getOtherInfo(), type);
			RobotMemory.setOrbitConstant(data.getOtherInfo());
			orbiter.setRadius(oc.calculateRadius());
		}
	}
}
