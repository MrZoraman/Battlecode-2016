package team379.pathfinding;

/**
 * The result of the pathfinder
 * @author Matt
 *
 */
public enum PathFindResult {
	/**
	 * The robot cannot move in any direction. There are no robots nearby either, meaning that it is surrounded
	 * 	on all sides by rubble.
	 */
	STUCK,
	
	/**
	 * The robot gave up trying to find a route. After so many tries and not maintaining its progress quota,
	 * 	it has decided that there is no route to the destination.
	 */
	COULD_NOT_FIND_ROUTE,
	
	/**
	 * The robot is completely surrounded by other robots.
	 */
	TRAPPED,
	
	/**
	 * The robot is experiencing core delay.
	 */
	CORE_DELAY,
	
	/**
	 * No issues. The robot has successfully made a step closer (hopefully) to the target destination.
	 */
	SUCCESS,
	
	/**
	 * The robot has no target destination!
	 */
	NO_TARGET
}
