package trafficmaster.core;

import java.io.Serializable;

import trafficmaster.serializable.json.JsonSerializable;


/**
 * A class that indicates the status of a course at a given moment.
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 */
@JsonSerializable
public enum Status implements Serializable {
	/**
	 * The means of transport is driving normally
	 */
	DRIVING("Driving"),
	/**
	 * The means of transport is staying at a stop
	 */
	AT_A_STOP("At a stop"),
	/**
	 * The means of transport is in a traffic jam
	 */
	JAMMED("Jammed"),
	/**
	 * The means of transport has taken part in an accident
	 */
	ACCIDENT("Accident"),
	/**
	 * The means of transport is considerably late.
	 */
	LATE("Late"),
	/**
	 * The status of the means of transport is currently unknown
	 */
	UNKNOWN("Unknown");
	String name;
	
	Status(String name) {
		this.name = name;
	}
}