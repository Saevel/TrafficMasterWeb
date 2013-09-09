package trafficmaster.core;

import java.io.Serializable;
import java.util.Collection;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * A bean class whose elements represent the physical location on the map correspoding to an entity
 * from real life.
 *
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 */
@JsonSerializable
public class Position implements Serializable {	
	public int ID;
	/**
	 * The latitude of the location.
	 */
	protected double latitude;
	/**
	 * The longitude of the location.
	 */
	protected double longitude;
	/**
	 * The name of the location.
	 */
	protected String name;

	protected Collection<Event> events;
	/**
	 * Earth's radius expressed in meters.
	 */

	private static final float EARTH_RADIUS = 6371000;
	/**
	 * Calculates the distance between the current location and another one specified(in meters).
	 * @param otherPosition another location, to which the distance is calculated.
	 * @return the distance between the two locations(in meters).
	 */
	public double getDistanceTo(Position otherPosition) {

		double firstFactor = Math.sin(toRad((latitude-otherPosition.latitude)/2));
		firstFactor *= firstFactor;
		
		double secondFactor = Math.cos(toRad(latitude))*Math.cos(toRad(otherPosition.latitude));
		
		double thirdFactor = Math.sin(toRad((longitude-otherPosition.longitude)/2));
		thirdFactor *= thirdFactor;

		return (2*EARTH_RADIUS*Math.asin(Math.sqrt(firstFactor)+secondFactor*thirdFactor));
	}
	
	private double toRad(double degrees) {
		return (degrees*Math.PI/(180));
	}
	
	
	/**
	 * Gets: the location's latitude.
	 * @return the location's latitude.
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * Sets: the location's latitude.
	 * @param latitude the location's latitude.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * Gets: the location's longitude.
	 * @return the location's longitude.
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * Sets: the location's longitude.
	 * @param longitude the location's longitude.
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * Gets: the location's name.
	 * @return the location's name. 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets: the location's name.
	 * @param name the location's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
		
	}
	
	public Collection<Event> getEvents() {
		return this.events;
	}
	
	public void setEvents(Collection<Event> events) {
		this.events = events;
	}
	
	public boolean equals(Object o ) {
		if(o instanceof Position) {
			Position p  = (Position)o;
			if(this.ID == p.ID) {
				return true;
			}
		}
		
		return false;
	}
}