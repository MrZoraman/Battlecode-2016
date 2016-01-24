package team379.goals.archon;

import java.util.ArrayDeque;
import java.util.Queue;

import battlecode.common.RobotType;

public class BuildQueue {
	private final Queue<RobotType> queue = new ArrayDeque<>();
	
	public void enqueue(RobotType type) {
		queue.add(type);
	}
	
	public RobotType getNextRobot() {
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
