package team379.signals;

import battlecode.common.RobotController;
import battlecode.common.Signal;

/**
 * Parses signals and sends them to signal consumers
 * @author Matt
 *
 */
public class SignalReader {
	/**
	 * Static class!
	 */
	private SignalReader() { }
	
	/**
	 * Empties the signal queue of a robot, parses the signals, then sends them to the consumers.
	 * @param rc The RobotController
	 * @param consumer The consumers that read the signals.
	 */
	public static void consume(RobotController rc, SignalConsumer... consumers) {
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
			data.setSenderId(signals[ii].getID());
			//send the data to the consumers
			for(SignalConsumer sc : consumers) {
				sc.consume(data);
			}
		}
	}
}
