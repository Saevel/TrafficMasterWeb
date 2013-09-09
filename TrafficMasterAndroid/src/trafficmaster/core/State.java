package trafficmaster.core;

import java.io.Serializable;
import java.util.Date;

import trafficmaster.serializable.json.JsonSerializable;


/**
 * Represents the state of a particular <code>Course</code> may be at a given <code>Time</code>.
 * @author Zielony
 * @version 1.0
 * @see Time
 * @see Location
 * @see JSONSerializable
 * @see JSONFactory
 * @see Status
 */
@JsonSerializable
public class State implements Serializable {
	/**
	 * The unique object identifier within the class
	 */
	private int ID;
	/**
	 * The velocity of the vehicle at measurement time.
	 */
	protected float currentVelocity;
	/**
	 * The average velocity of the vehicle over a certain period of time.
	 */
	protected float averageVelocity;
	/**
	 * Delay of the course.
	 */
	protected Date delay;
	/**
	 * Current location of the vehicle.
	 */
	protected Position location;
	
	protected Status status;
	
	public float getCurrentVelocity() {
		return currentVelocity;
	}
	/**
	 * Sets: the current velocity.
	 * @param currentVelocity the current velocity.
	 */
	public void setCurrentVelocity(float currentVelocity) {
		this.currentVelocity = currentVelocity;
	}
	/**
	 * Gets: the average velocity.
	 * @return the average velocity.
	 */
	public float getAverageVelocity() {
		return averageVelocity;
	}
	/**
	 * Sets: the average velocity.
	 * @param averageVelocity the average velocity.
	 */
	public void setAverageVelocity(float averageVelocity) {
		this.averageVelocity = averageVelocity;
	}
	/**
	 * Gets: the current course delay.
	 * @return the current course delay.
	 */
	public Date getDelay() {
		return delay;
	}
	/**
	 * Sets: the current course delay.
	 * @param the current course delay.
	 */
	public void setDelay(Date delay) {
		this.delay = delay;
	}
	/**
	 * Gets: the course location.
	 * @return the course location.
	 */
	public Position getPosition() {
		return location;
	}
	/**
	 * Sets: the course location.
	 * @param location the course location.
	 */
	public void setPosition(Position location) {
		this.location  = location;
	}
	/**
	 * Gets: the current course <code>Status</code>.
	 * @return the current course <code>Status</code>.
	 */

	public Status getStatus() {
		return status;
	}
	/**
	 * Sets: the current course <code>Status</code>.
	 * @param status the current course <code>Status</code>.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * Gets: this entity's ID.
	 * @return this entity's ID.
	 */
	public int getID() {
		return ID;
	}
	/**
	 * Sets: this entity's ID.
	 * @param ID this entity's ID.
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
}