package trafficmaster.core;

import java.io.Serializable;

import trafficmaster.serializable.json.JsonSerializable;
/**
 * Links a <code>Stop</code> and a <code>Line</code> passing through it to their <code>arrivals</code>
 * - the times from the schedule. 
 * @author Zielony
 * @version 1.0
 */
@JsonSerializable
public enum ScheduleType implements Serializable {
	/**
	 * A schedule for the week.
	 */
	WEEK("Week"),
	/**
	 * A schedule for saturdays. 
	 */
	SATURDAY("Saturday"),
	/**
	 * A schedule for sundays
	 */
	SUNDAY("Sunday");

	private String type;
	
	ScheduleType(String type) {
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
