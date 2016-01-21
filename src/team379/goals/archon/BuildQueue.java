package team379.goals.archon;

import java.util.ArrayDeque;
import java.util.Queue;

import battlecode.common.RobotType;

public class BuildQueue {
	private final Queue<RobotType> queue = new ArrayDeque<>();
	
	private int delay = 0;
	private int delayCounter = 0;
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void enqueue(RobotType type) {
		queue.add(type);
	}
	
	public RobotType getNextRobot() {
		RobotType next = null;
		if(delayCounter >= delay) {
			delayCounter = 0;
			next = queue.poll();
		}
		
		delayCounter++;
		return next;
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
