package team379.goals;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.OrbitCalculator;
import team379.RobotMemory;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;
import team379.signals.SignalReader;
import team379.signals.consumers.HeadArchonIdentifier;

public abstract class PatrolAroundArchonGoalBase implements Goal {
	
	/**
	 * How much to boost the rubble threshold if it must be raised due to being stuck.
	 */
	private static final int RUBBLE_BOOST_AMOUNT = 100;
	
	protected static Orbiter orbiter;
	
	public PatrolAroundArchonGoalBase(RobotController rc) {
		OrbitCalculator oc = new OrbitCalculator(RobotMemory.getOrbitConstant(), rc.getType());
		if(orbiter == null) {
			orbiter = new Orbiter(RobotMemory.getArchonLocation(), oc.calculateRadius());
		} else {
			orbiter.setCenter(RobotMemory.getArchonLocation());
			orbiter.setRadius(oc.calculateRadius());
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
	
	private Goal move(RobotController rc) throws Exception {
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
}
