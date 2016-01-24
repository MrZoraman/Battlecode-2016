package team379.pathfinding;

import java.util.HashSet;
import java.util.Set;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import team379.RobotMemory;
import team379.signals.SignalConsumer;
import team379.signals.SignalData;
import team379.signals.SignalType;

/**
 * Keeps track of the archon location.
 * @author Matt
 *
 */
public class ArchonLocator implements SignalConsumer {
	
	/**
	 * This is how many times the ArchonLocator can assume 
	 * 	the location of the archon before it goes out and searches for it.
	 */
	private static final int ARCHON_STALENESS_THRESHOLD = 6;
	
	/**
	 * This is the last known location of the archon. If {@link #locationStaleness} is 0,
	 * 	then this is the location of the archon.
	 */
	private MapLocation lastKnownArchonLocation;
	
	/**
	 * How many turns since the ArchonLocatoin has been updated.
	 */
	private int locationStaleness = 0;
	
	/**
	 * If this is true, then I don't know where the archon is. I'm just going in a direction I think will get me
	 * 	close to my archon.
	 */
	private boolean lost = false;
	
	/**
	 * The id of the archon I am following.
	 */
	private int archonId;
	
	/**
	 * Constructor
	 * @param archonLocation The initial location of the archon. Cannot be null.
	 */
	public ArchonLocator(MapLocation archonLocation, int archonId) {
		if(archonLocation == null) {
			throw new IllegalArgumentException("archonLocation cannot be null!");
		}
		
		this.lastKnownArchonLocation = archonLocation;
		this.archonId = archonId;
	}
	
	/**
	 * Gets the location of the archon.
	 * @param nearbyRobots the Robots next to the ArchonLocator.
	 * @param rc The RobotController
	 * @return The {@link battlecode.common.MapLocation MapLocation} where the archon is supposedly located.
	 */
	public MapLocation getArchonLocation(RobotController rc, RobotInfo[] nearbyRobots) {
		//make sure the last known archon location isn't stale
		if(locationStaleness < ARCHON_STALENESS_THRESHOLD) {
			return lastKnownArchonLocation;
		}
		
		//the location is stale! I haven't actually seen/looked for the archon in 6 turns...
		//make sure nearbyRobots is not null
		if(nearbyRobots == null) {
			nearbyRobots = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		}
		
		//my location
		MapLocation myLoc = rc.getLocation();
		//The distance to my closest friend (in the direction of where I remember the archon being)
		int closestFriendDistanceSquared = Integer.MAX_VALUE;
		//My closest friend (in the direction of where I remember the archon being)
		RobotInfo closestFriend = null;
		//the direction I last remember my archon being in
		Direction archonDirection = myLoc.directionTo(lastKnownArchonLocation);
		//the general directions I last remember my archon being in
		Set<Direction> archonDirections = new HashSet<>();
		archonDirections.add(archonDirection);
		archonDirections.add(archonDirection.rotateLeft());
		archonDirections.add(archonDirection.rotateRight());
		
		for(RobotInfo ri : nearbyRobots) {
			//make sure this is a friendly robot
			if(ri.team != rc.getTeam()) {
				continue;
			}
			
			//I found my archon!
			if(ri.ID == archonId) {
				updateArchonLocation(ri.location, ri.ID);
				return ri.location;
			}
			
			//direction to robot
			Direction directionToRobot = myLoc.directionTo(ri.location);
			//if robot is not in the general direction of the archon, don't follow it
			if(!archonDirections.contains(directionToRobot)) {
				continue;
			}
			
			//find closest friend
			int distanceSquaredToRobot = myLoc.distanceSquaredTo(ri.location);
			if(distanceSquaredToRobot < closestFriendDistanceSquared) {
				closestFriendDistanceSquared = distanceSquaredToRobot;
				closestFriend = ri;
			}
		}
		
		//if I get to this point, I have no idea where my archon actually is...
		lost = true;
		
		//did I find a friend in the direction of my archon?
		if(closestFriend != null) {
			//assume the friend is the archon. Perhaps getting closer to them will get me closer to my archon.
			return closestFriend.location;
		}
		
		//I found no friends in the general direction I think my archon is in. I'll just return where I last remember
		//the archon being and hope for the best.
		return lastKnownArchonLocation;
	}
	
	/**
	 * Gets the location of the archon. If the archon cannot be found, it will look around with a maximum sensor radius.
	 * @param rc The RobotController
	 * @return The {@link battlecode.common.MapLocation MapLocation} where the archon is supposedly located. 
	 */
	public MapLocation getArchonLocation(RobotController rc) {
		return getArchonLocation(rc, null);
	}
	
	/**
	 * Update the location of the archon. Call this method if you are sure that this is the archon's location.
	 * @param loc The location of the archon.
	 * @param archonId The id of the archon.
	 */
	public void updateArchonLocation(MapLocation loc, int archonId) {
		this.archonId = archonId;
		updateArchonLocation(loc);
	}
	
	/**
	 * Update the location of the archon. Call this method if you are sure that this is the archon's location.
	 * @param loc The location of the archon.
	 */
	public void updateArchonLocation(MapLocation loc) {
		RobotMemory.setArchonLocation(loc);
		this.lastKnownArchonLocation = loc;
		this.locationStaleness = 0;
		this.lost = false;
	}
	
	/**
	 * checks if the archon locator is lost or not.
	 * @return True if the archon locator has no idea where the archon is, and is just picking a direction 
	 * 	and hoping for the best. False if otherwise.
	 */
	public boolean isLost() {
		return lost;
	}

	@Override
	public void consume(SignalData data) {
		if(data.getType() == SignalType.THIS_IS_MY_ID) {
			updateArchonLocation(data.getLocation());
		}
	}
}
