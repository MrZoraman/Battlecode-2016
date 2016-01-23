package team379.signals;

/**
 * Consumes/reads signal data.
 * @author Matt
 *
 */
@FunctionalInterface
public interface SignalConsumer {
	/**
	 * Read the signal data
	 * @param data The data being read.
	 */
	public void consume(SignalData data);
}
