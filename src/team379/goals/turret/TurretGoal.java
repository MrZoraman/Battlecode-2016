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
import team379.signals.consumers.HeadArchonIdentifier;

public class TurretGoal implements Goal{
	private final PathFinder pf = new PathFinder();
	
	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		HeadArchonIdentifier hai = new HeadArchonIdentifier();
		SignalReader.consume(rc, data ->{ 
			
		});
		MapLocation mp = RobotMemory.getArchonLocation();
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
