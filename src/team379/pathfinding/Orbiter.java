package team379.pathfinding;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 * A special kind of path finder that "orbits" around a specific point.
 * @author Matt
 *
 */
public class Orbiter extends PathFinder {
	/**
	 * The default direction the robot will move to to get in orbit.
	 */
	private static final Direction DEFAULT_DIRECTION = Direction.NORTH;
	
	/**
	 * How close to the target location the robot can get before it decides to switch directions.
	 */
	private static final double DISTANCE_THRESHOLD = 0.8;
	
	/**
	 * The random number generator for the orbit range variability.
	 */
	private final Random rand = new Random();
	
	/**
	 * The direction from the center the pathfinding target is.
	 */
	private Direction compassDirection = DEFAULT_DIRECTION;
	
	/**
	 * The distance from the center this robot will orbit.
	 */
	private double radius;
	
	/**
	 * The variability of the orbit (aka the "width").
	 */
	private int orbitRange;
	
	/**
	 * The center/reference point of this robot's orbit.
	 */
	private MapLocation center;
	
	/**
	 * Constructor.
	 * @param radius The orbit radius in blocks.
	 * @param center The center/reference point of the orbit.
	 * @param range The range/variability of the orbit radius.
	 */
	public Orbiter(MapLocation center, double radius, int range) {
		this.center = center;
		this.radius = radius;
		this.orbitRange = range;
	}
	
	/**
	 * Constructor.
	 * @param radius The orbit radius in blocks.
	 * @param center The center/reference point of the orbit.
	 * @param range The range/variability of the orbit radius squared.
	 */
	public Orbiter(MapLocation center, int radiusSquared, int range) {
		this(center, Math.sqrt(radiusSquared), range);
	}
	
	/**
	 * calculates the target (some point on the orbit path) for the path finder to move towards.
	 * @param newDirection If the direction from the center should be switched. Set to false if
	 * 	the target is just being recalculated due to a center shift.
	 */
	private void calculateNextTarget(boolean newDirection) {
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
	
	@Override
	public PathFindResult move(RobotController rc) throws Exception {
		//make sure there is a target
		if(compassDirection == null || getTarget() == null) {
			calculateNextTarget(true);
		} else {
			//get the distance to target
			int distanceSquared = getTarget().distanceSquaredTo(rc.getLocation());
			double distance = Math.sqrt(distanceSquared);
			//if I'm close enough, recalculate target
			if(distance < DISTANCE_THRESHOLD) {
				calculateNextTarget(true);
			}
		}
		
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
			radius *= 0.85;
		}
		//return radius + rand.nextInt(orbitRange) - (orbitRange / 2);
		return radius;
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
	 * Gets the range/variabilty of the orbit.
	 * @return The orbit range in squares.
	 */
	public int getOrbitRange() {
		return orbitRange;
	}

	/**
	 * Sets the orbit range.
	 * @param orbitRange The orbit range in squares.
	 */
	public void setOrbitRange(int orbitRange) {
		this.orbitRange = orbitRange;
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
