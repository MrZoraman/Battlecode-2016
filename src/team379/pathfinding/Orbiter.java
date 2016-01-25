package team379.pathfinding;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team379.RobotMemory;

/**
 * A special kind of path finder that "orbits" around a specific point.
 * @author Matt
 *
 */
public class Orbiter extends PathFinder {
	/**
	 * This is the constant for shortening the radius at the angles to make a more uniform circular shape.
	 */
	private static final double ANGLE_RADIUS_CONSTANT = 0.85;
	
	/**
	 * The default direction the robot will move to to get in orbit.
	 */
	private static final Direction DEFAULT_DIRECTION = Direction.NORTH;
	
	/**
	 * How close to the target location the robot can get before it decides to switch directions.
	 */
	private static final double DISTANCE_THRESHOLD = 1.5;
	
	/**
	 * The direction from the center the pathfinding target is.
	 */
	private Direction compassDirection = DEFAULT_DIRECTION;
	
	/**
	 * The distance from the center this robot will orbit.
	 */
	private double radius;
	
	/**
	 * The center/reference point of this robot's orbit.
	 */
	private MapLocation center;
	
	/**
	 * Constructor.
	 * @param radius The orbit radius in blocks.
	 * @param center The center/reference point of the orbit.
	 * @param range The range/variability of the orbit radius. Setting this to zero or less disables this.
	 */
	public Orbiter(MapLocation center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	/**
	 * Constructor.
	 * @param radius The orbit radius in blocks.
	 * @param center The center/reference point of the orbit.
	 * @param range The range/variability of the orbit radius squared. Setting this to zero or less disables this.
	 */
	public Orbiter(MapLocation center, int radiusSquared) {
		this(center, Math.sqrt(radiusSquared));
	}
	
	/**
	 * calculates the target (some point on the orbit path) for the path finder to move towards.
	 * @param newDirection If the direction from the center should be switched. Set to false if
	 * 	the target is just being recalculated due to a center shift.
	 */
	public void calculateNextTarget(boolean newDirection) {
		//get the next Direction
		if (newDirection) {
			compassDirection = compassDirection.rotateRight();
		}
		
		//calculated the radius
		double calculatedRadius = calculateRadius();
		//calculate the target
		MapLocation calculatedTarget = center.add(compassDirection, (int) calculatedRadius); //this takes radius not squared!
		//set the next target in the base class
		setTarget(calculatedTarget);
	}
	
	/**
	 * Gradually moves the calculated target towards the center until it is on the map.
	 * @param rc The robotController
	 * @param calculatedTarget The calculated target for the path finder
	 * @return The new target that is on the map.
	 * @throws Exception If something goes wrong...
	 */
	private MapLocation putTargetOnMap(RobotController rc, MapLocation calculatedTarget) throws Exception {
		//my location
		MapLocation myLocation = rc.getLocation();
		//direction that takes me to my target
		Direction dirToTarget = myLocation.directionTo(calculatedTarget);
		//the map location in that direction
		MapLocation spaceToTarget = myLocation.add(dirToTarget);
		//make sure that direction is on the map
		if(!rc.onTheMap(spaceToTarget)) {
			//it is not on the map, move the target location closer to the center
			Direction backwards = compassDirection.opposite();
			return putTargetOnMap(rc, calculatedTarget.add(backwards));
		}
		
		//location is on the map
		return calculatedTarget;
	}
	
	@Override
	public PathFindResult move(RobotController rc) throws Exception {
		//make sure there is a target
		if(compassDirection == null || getTarget() == null) {
			calculateNextTarget(true);
		} else {
			//put target on map
			MapLocation target = getTarget();
			target = putTargetOnMap(rc, target);
			setTarget(target);
			
			rc.setIndicatorString(2, "my target is: " + target);
			//get the distance to target
			int distanceSquared = getTarget().distanceSquaredTo(rc.getLocation());
			double distance = Math.sqrt(distanceSquared);
			//if I'm close enough, recalculate target
			if(distance < DISTANCE_THRESHOLD) {
				calculateNextTarget(true);
				atTarget = true;
			} else {
				atTarget = false;
			}
		}
		
		rc.setIndicatorString(1, "center: " + center + ", archonId I am circling: " + RobotMemory.getArchonId());
		
		//have the base class do the rest
		return super.move(rc);
	}
	
	/**
	 * calculates the actual radius (combines the radius plus the range with a little randomness)
	 * @return The final radius for the target to be located at.
	 */
	private double calculateRadius() {
		double radius = this.radius;
		if(compassDirection.isDiagonal()) {
			radius *= ANGLE_RADIUS_CONSTANT;
		}
		return radius;
	}
	
	@Override
	public DirectionTranslator[] generateTranslators() {
		return new DirectionTranslator[] {
			dir -> dir,								//north			assuming north, the following offsets are:
			dir -> dir.rotateLeft(),				//north-west
			dir -> dir.rotateRight().rotateRight(),	//north-east
			dir -> dir.rotateRight(),				//east
			dir -> dir.opposite()					//west
		};
	}

	/**
	 * Getss the radius from the center.
	 * @return The radius in squares.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius to orbit around the center from.
	 * @param radius The radius in squares.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	/**
	 * Sets the radius to orbit around the center from.
	 * @param radiusSquared The radius in radius squared.
	 */
	public void setRadius(int radiusSquared) {
		setRadius(Math.sqrt(radiusSquared));
	}

	/**
	 * Gets the center/target to orbit around.
	 * @return The center of the orbit.
	 */
	public MapLocation getCenter() {
		return center;
	}

	/**
	 * Sets the center of the orbit.
	 * @param center The center to orbit around.
	 */
	public void setCenter(MapLocation center) {
		this.center = center;
		calculateNextTarget(false);
	}
}
