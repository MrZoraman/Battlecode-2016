package team379.goals.turret;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team379.Globals;
import team379.goals.Goal;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFinder;

public class TurretFindArchonGoal implements Goal {

	private PathFinder pf = new PathFinder();
	private static ArchonLocator al;
	private MapLocation archonLocation;
	
	public TurretFindArchonGoal(ArchonLocator inAl) {
		al = inAl;
	}
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		archonLocation = al.getArchonLocation(rc);
		pf.setRubbleThreshold(0);
		
		double archonDistance = archonDistance(archonLocation, rc);
		if(archonDistance > Globals.TURRET_ARCHON_DISTANCE()) {
			if(rc.getType() == RobotType.TURRET) {
				rc.pack();
			}
			pf.setTarget(archonLocation);
			pf.move(rc);
			return null;
		}
		if(rc.getType() == RobotType.TTM) {
			rc.unpack();
		}
		return new TurretGoal();
	}

	@Override
	public String getName() {
		return "Finding my Archon";
	}
	
	private double archonDistance(MapLocation mp, RobotController rc) {
		return Math.sqrt(Math.pow(Math.abs(mp.x - rc.getLocation().x), 2) + Math.pow(Math.abs(mp.y - rc.getLocation().y), 2));
	}

}
