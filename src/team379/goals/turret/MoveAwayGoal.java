package team379.goals.turret;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team379.OrbitCalculator;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.Orbiter;
import team379.pathfinding.PathFindResult;

public class MoveAwayGoal implements Goal{

	private static Orbiter orbiter;
	
	public MoveAwayGoal(RobotController rc) throws GameActionException {
		rc.pack();
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
			orbiter.reset();
			orbiter.calculateNextTarget(true);
			break;
		case SUCCESS:
			break;
		case RUBBLE_IN_WAY:
			orbiter.reset();
			orbiter.calculateNextTarget(true);
			break;
		default:
			break;
		
		}
		if(orbiter.isAtTarget()) {
			rc.unpack();
			return new TurretGoal();
		}
		return null;
	}

	@Override
	public String getName() {
		return "Moving Away!";
	}

}
