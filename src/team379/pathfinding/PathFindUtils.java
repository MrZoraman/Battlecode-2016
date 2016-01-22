package team379.pathfinding;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.robots.RobotMemory;

public class PathFindUtils {
	private PathFindUtils() { }
	
	public static ArchonLocateResult findArchonLocation(RobotController rc, RobotMemory memory, RobotInfo[] nearbyRobots, MapLocation lastKnownArchonLocation) {
		if(memory.getStaleness() < 6) {	//TODO: magic number!
			return new ArchonLocateResult(false, lastKnownArchonLocation);
		}
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.ID == memory.getArchonId()) {
				//I found my archon!
				return new ArchonLocateResult(true, ri.location);
			}
		}
		
		if(lastKnownArchonLocation == null) {
			//I'm really lost!
			return new ArchonLocateResult(false, lastKnownArchonLocation);
		}
		
		//if I can't find my archon, I'll look for friends in the general direction I last remember the archon being.
		//They might be following the archon, so if I go to them, I'll probably find the archon.
		Direction archonDirection = rc.getLocation().directionTo(lastKnownArchonLocation);	//TODO: sometimes this is null!
		Direction archonDirectionLeft = archonDirection.rotateLeft();
		Direction archonDirectionRight = archonDirection.rotateRight();
		
		RobotInfo closestFriendInDirection = null;
		double closestFriendDistance = Double.MAX_VALUE;
		
		for(RobotInfo ri : nearbyRobots) {
			if(ri.team == rc.getTeam()) {
				Direction friendDirection = rc.getLocation().directionTo(ri.location);
				if(friendDirection == archonDirection 
						|| friendDirection == archonDirectionLeft 
						|| friendDirection == archonDirectionRight) {
					double distance = rc.getLocation().distanceSquaredTo(ri.location);
					if(distance < closestFriendDistance) {
						closestFriendDistance = distance;
						closestFriendInDirection = ri;
					}
				}
			}
		}
		
		if(closestFriendInDirection != null) {
			return new ArchonLocateResult(false, closestFriendInDirection.location);
		}
		
		//if There are no friends in the direction of where I think the archon is, I'll just go to where I think the archon is.
		//I only looked for friends in the direction of the archon because otherwise I might accidentally cluster with other
		//friends without an archon to guide us.
		return new ArchonLocateResult(false, lastKnownArchonLocation);
	}
}
