package team379.goals.turret;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import team379.Robot;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.pathfinding.PathFindResult;
import team379.pathfinding.ArchonLocator;
import team379.pathfinding.PathFinder;
import team379.signals.SignalReader;
import team379.signals.SignalType;
import team379.signals.consumers.HeadArchonIdentifier;

public class TurretGoal implements Goal{
	private final PathFinder pf = new PathFinder();
	
	private static ArchonLocator al;
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		if(al == null) {
			al = new ArchonLocator(RobotMemory.getArchonLocation(), RobotMemory.getArchonId());
		}
		SignalReader.consume(rc, data ->{
			if(data.getType() == SignalType.THIS_IS_MY_ID) {
				al.updateArchonLocation(data.getLocation());
			}
		});
		
		MapLocation mp = al.getArchonLocation(rc);
		double archonDistance = ArchonDistance(mp);
		
        return null;
	}
	@Override
	public String getName() {
		return "Being a Turret";
	}
	
	private double ArchonDistance(MapLocation mp) {
		double loc = 0;
		
		
		
		return loc;
	}
}
