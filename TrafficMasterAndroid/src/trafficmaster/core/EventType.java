package trafficmaster.core;

import java.io.Serializable;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * Defines certain types of <code>Event</code>s that may appear in the city traffic.
 * @author Zielony
 * @verion 1.0
 * @see Event
 * @see JsonSerializable
 */
@JsonSerializable
public enum EventType implements Serializable {
	/**
	 * Event: roadworks.
	 */
	ROADWORKS("Roadworks"),
	/**
	 * Event : accident.
	 */
	ACCIDENT("Accident"),
	/**
	 * Event: traffic jam.
	 */
	TRAFFIC_JAM("Traffic Jam"),
	/**
	 * Event: traffic alteration.
	 */
	ALTERATION("Alteration"),
	/**
	 * Event: a public event.
	 */
	PUBLIC_EVENT("Public Event"),
	/**
	 * Event: other event.
	 */
	OTHER("OTHER");
	/**
	 * The type of this event as a <code>String</code>.
	 */
	private String type;
	/**
	 * A simple constructor for enum purposes
	 * @param type the type of this event as a <code>String</code>.
	 */ 
	EventType(String type) {
		this.type = type;
	}
	/**
	 * Gets: the type of this event as a <code>String</code>.
	 * @return <code>String</code>.
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets: the type of this event as a <code>String</code>.
	 * @param type the type of this event as a <code>String</code>.
	 */
	public void setType(String type) {
		this.type = type;
	}
}