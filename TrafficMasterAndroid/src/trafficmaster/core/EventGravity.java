package trafficmaster.core;

import java.io.Serializable;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * An <code>enum</code> representing the gravity of a certain <code>Event</code>.
 * @author Zielony
 * @version 1.0
 * @see Event
 * @see JsonSerializable
 */
@JsonSerializable
public enum EventGravity implements Serializable  {
	/**High gravity*/
	HIGH(100),
	/**Medium gravity*/
	MEDIUM(50),
	/**Low gravity*/
	LOW(0);
	/**
	 * The measure of gravity of this enum.
	 */
	private int gravity;
	/**A special constructor.*/
	private EventGravity(int gravity) {
		this.gravity = gravity;
	}
	/**
	 * Gets: the gravity of this enum as number.
	 * @return the gravity of this enum as number.
	 */
	public int getGravity() {
		return gravity;
	}
	/**
	 * Sets: the gravity of this enum as number.
	 * @param gravity the gravity of this enum as number.
	 */
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}
}