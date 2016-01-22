package team379.pathfinding;

import static team379.pathfinding.PathFindResult.*;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

/**
 * This is the path finder. It is given a target location, and it will try to move to that location.
 * If the space in the direction of the target is taken up, the path finder will try to find another
 * direction to go.
 * 
 * @author Matt
 *
 */
public class PathFinder {
	
	/**
	 * This functional interface represents a rotation/translation of a direction. The goal is
	 * 	to have a set of these that each move around the compass a little. With them in the right
	 * 	order, I can try to find the path of least resistance that still generally takes me to
	 * 	my target location.
	 * 
	 * @author Matt
	 *
	 */
	@FunctionalInterface
	private interface DirectionTranslator {
		/**
		 * Translates a direction.
		 * @param dir The direction to transform
		 * @return The new direction.
		 */
		Direction translateDirection(Direction dir);
	}
	
	/**
	 * The result of the pathfinder trying to find a good direction to move in.
	 * @author Matt
	 *
	 */
	private class DirectionResult {
		Direction dir;
		boolean robotInWay = false;
	}
	
	private static final DirectionTranslator[] translators = new DirectionTranslator[] {
		dir -> dir,								//north
		dir -> dir.rotateLeft(),				//north-west
		dir -> dir.rotateRight().rotateRight(),	//north-east
		dir -> dir.rotateRight(),				//east
		dir -> dir.opposite(),					//west
		dir -> dir.rotateLeft(),				//south-west
		dir -> dir.rotateLeft().rotateLeft(),	//south-east
		dir -> dir.rotateRight()				//east
	};
	
	/**
	 * How many moves that are below the {@link #distanceDelta} threshold before the pathfinder fails?
	 */
	private int distanceDeltaGiveUp = 13;
	
	/**
	 * The movement delta threshold. The pathfinder counts how many moves it has made in a row that are
	 * below this threshold. If it counts {@link #distanceDeltaGiveUp} moves in a row, it will fail.
	 */
	private double distanceDelta = 0.8;
	
	/**
	 * The last distance the pathfinder has moved. This is used to calculate the delta distance to compare
	 * to the {@link #distanceDelta}
	 */
	private double lastDistance = 0;
	
	/**
	 * How many times in a row has the distance delta been below the {@link #distanceDelta} threshold?
	 */
	private int distanceTries = 0;
	
	/**
	 * The target location to go to.
	 */
	private MapLocation target;
	
	/**
	 * Moves the PathFinder (hopefully) towards the target location.
	 * 
	 * @param rc The RobotContnroller
	 * @param target The target location to move towards
	 * @return the {@link team379.pathfinding.PathFindResult PathFindResult} containing info about the possible issues
	 * 	encountered during the movement.
	 * @throws Exception If something goes wrong...
	 */
	public PathFindResult move(RobotController rc) throws Exception {
		//make sure I have a target
		if(target == null) {
			return NO_TARGET;
		}
		
		//Make sure the core is ready
		if(!rc.isCoreReady()) {
			return CORE_DELAY;
		}
	
		//where am I?
		MapLocation myLocation = rc.getLocation();
		
		//how far away (in blocks) is my target?
		double distanceToTarget = Math.sqrt(myLocation.distanceSquaredTo(target));
		//how much change since the last move?
		double calculatedDelta = lastDistance - distanceToTarget;
		//record the distance for later
		lastDistance = distanceToTarget;
		
		//record how many times in a row I've been below the threshold, or reset it if I'm good.
		if(calculatedDelta < distanceDelta) {
			distanceTries++;
		} else {
			distanceTries = 0;
		}
		
		//if it is time to give up, give up.
		if(distanceTries > distanceDeltaGiveUp) {
			return COULD_NOT_FIND_ROUTE;
		}
		
		//get the best direction to my target
		DirectionResult result = getDirection(rc, myLocation, target);
		
		//did I find a way to go?
		if(result.dir != null) {
			//go where I want to go
			rc.move(result.dir);
			//all is good
			return SUCCESS;
		}
		
		//I coudln't go anywhere and robots were blocking my path
		if(result.robotInWay) {
			return TRAPPED;
		}
		
		//I am stuck!
		return STUCK;
	}
	
	/**
	 * Gets the best direction to the target. This is how the pathfinder maneuvers around obstacles.
	 * @param rc The RobotController
	 * @param myLocation The current location of the path finder
	 * @param target The target location
	 * @return the {@link DirectionResult}, which contains the direction, and if robots were in the way.
	 * @throws Exception if Something goes wrong...
	 */
	private DirectionResult getDirection(RobotController rc, MapLocation myLocation, MapLocation target) throws Exception {
		//create the result to return
		DirectionResult result = new DirectionResult();
		
		//what direction is the target?
		Direction targetDir = myLocation.directionTo(target);
		//start going through the translations until a direction works. The first translation isn't actually a translation.
		for(int ii = 0; ii < translators.length; ii++) {
			//find the translation
			targetDir = translators[ii].translateDirection(targetDir);
			
			//if this direction is valid, then this is the direction I will go.
			if(rc.canMove(targetDir)) {
				result.dir = targetDir;
				break;
			}
			
			//from this point onwards the direction is invalid
			
			MapLocation loc = myLocation.add(targetDir);
			//the maplocation that contains something in my way
			
			//check if there's a robot in that space
			RobotInfo ri = rc.senseRobotAtLocation(loc);
			if(ri != null) {
				//there's a robot that has blocked my best path. This info is saved.
				result.robotInWay = true;
			}
		}
		
		//return the result (the direction may be null!)
		return result;
	}
	
	/**
	 * Reset the pathfinder (usually good to do each time a new destination has been decided)
	 */
	public void reset() {
		distanceTries = 0;
	}
	
	/**
	 * Gets the map location where the path finder is trying to get to.
	 * @return The Target
	 */
	public MapLocation getTarget() {
		return target;
	}
	
	/**
	 * Sets the pathfinder target.
	 * @param target The location the path finder is trying to get to.
	 */
	public void setTarget(MapLocation target) {
		this.target = target;
	}
}
