package team379.pathfinding;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team379.Globals;

public class Orbiter extends PathFinder {
	//how close to the target location before time to switch directions.
	private static final int DISTANCE_THRESHOLD = 2;
	
	private final Random rand = new Random();
	
	private int directionIndex;
	private MapLocation target;
	private int radius;
	
	private int radiusVariability = 3;
	
	public Orbiter(int radius) {
		this(radius, -1);
	}
	
	public Orbiter(int radius, int directionIndex) {
		this.radius = radius;
		this.directionIndex = directionIndex;
	}
	
	@Override
	public PathFindResult move(RobotController rc, MapLocation center) throws Exception {
		if(target == null || 
				rc.getLocation().distanceSquaredTo(target) < DISTANCE_THRESHOLD) {
			calculateTarget(center);
		}
		return super.move(rc, target);
	}
	
	public void calculateTarget(MapLocation center) {
		//time to figure out a new place to go
		if(directionIndex < 0) {
			directionIndex = rand.nextInt(8);
		} else {
			directionIndex++;
			directionIndex = directionIndex % 8;
		}
		Direction dir = Globals.movableDirections[directionIndex];
		target = center.add(dir, calculateRadius());
		super.reset();
	}
	
	private int calculateRadius() {
		return radius + rand.nextInt(radiusVariability) - (radiusVariability / 2);
	}
	
	public MapLocation getTarget() {
		return target;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public void setRadiusVariability(int radiusVariability) {
		this.radiusVariability = radiusVariability;
	}
	
	public int getDirectionIndex() {
		return directionIndex;
	}
	
	public void setDirectionIndex(int directionIndex) {
		this.directionIndex = directionIndex;
	}
}
