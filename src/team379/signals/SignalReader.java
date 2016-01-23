package team379.signals;

import battlecode.common.RobotController;
import battlecode.common.Signal;

/**
 * Parses signals and sends them to a signal consumer
 * @author Matt
 *
 */
public class SignalReader {
	/**
	 * Static class!
	 */
	private SignalReader() { }
	
	/**
	 * Emties the signal queue of a robot, parses the signals, then sends them to the consumer.
	 * @param rc The RobotController
	 * @param consumer The consumer that reads the signals.
	 */
	public static void consume(RobotController rc, SignalConsumer consumer) {
		//empty the signal queue
		Signal[] signals = rc.emptySignalQueue();
		for(int ii = 0; ii < signals.length; ii++) {
			//make sure it's a friendly signal
			if(signals[ii].getTeam() != rc.getTeam()) {
				continue;
			}
			
			//get the data
			int[] message = signals[ii].getMessage();
			//if no data, discard signal
			if(message.length <= 0) {
				continue;
			}
			
			//parse the data
			SignalData data = new SignalData(message[0], message[1]);
			//send the data to the consumer
			consumer.consume(data);
		}
	}
}
