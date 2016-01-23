package team379.signals.consumers;

import battlecode.common.MapLocation;
import team379.signals.SignalConsumer;
import team379.signals.SignalData;
import team379.signals.SignalType;

/**
 * Consumes signals and locates the head archon (the archon with the lowest id)
 * @author Matt
 *
 */
public class HeadArchonIdentifier implements SignalConsumer {

	/**
	 * The id of the archon.
	 */
	private int archonId = Integer.MAX_VALUE;
	
	/**
	 * The location of the archon.
	 */
	private MapLocation archonLocation = null;
	
	@Override
	public void consume(SignalData data) {
		if(data.getType() == SignalType.THIS_IS_MY_ID) {
			int id = data.getOtherInfo();
			if(id < archonId) {
				archonId = id;
				archonLocation = data.getLocation();
			}
		}
	}

	/**
	 * Gets the id of the head archon.
	 * @return The head archon id.
	 */
	public int getArchonId() {
		return archonId;
	}
	
	/**
	 * Gets the location of the head archon.
	 * @return The head archon location.
	 */
	public MapLocation getArchonLocation() {
		return archonLocation;
	}
}
