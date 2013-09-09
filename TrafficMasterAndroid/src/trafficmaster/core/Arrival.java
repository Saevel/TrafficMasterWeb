package trafficmaster.core;

import java.io.Serializable;
import java.util.Date;

import trafficmaster.serializable.json.JsonSerializable;
/**
 * A schedule entry depicting the relation between the <code>line</code>
 * and the <code>stop</code> by providing the <code>arrivals</code> - the
 * times at which the <code>line</code> is bound to arrive at the <code>stop
 * </code>. 
 * @author Zielony
 * @version 1.0
 */

@JsonSerializable
public class Arrival implements Serializable {
	/**
	 * The <code>Line</code> for which the arrival is scheduled.
	 */
	
	private Course course;
	/**
	 * The <code>Stop</code> at which the <code>line</code> arrives.
	 */
	
	private Position position;
	/**
	 * All the arrival times.
	 */

	private Date arrivalTime;
	/**
	 * The type of the schedule, in which the arrival appears.
	 */
	
	private ScheduleType scheduleType;
	/**
	 * The ID for this entity.
	 */
	
	private int ID;
	/**
	 * Gets: the <code>Line</code> for which this arrival is scheduled.
	 * @return the <code>Line</code> for which this arrival is scheduled.
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * Sets: the <code>Line</code> for which this arrival is scheduled.
	 * @param line the <code>Line</code> for which this arrival is scheduled.
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
	/**
	 * Gets: the type of the schedule to which this arrival belongs.
	 * @return the type of the schedule to which this arrival belongs.
	 */
	public ScheduleType getScheduleType() {
		return scheduleType;
	}
	/**
	 * Sets: the type of the schedule to which this arrival belongs.
	 * @param scheduleType the type of the schedule to which this arrival belongs.
	 */
	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}
	/**
	 * Gets: the Id of this entity.
	 * @return the Id of this entity.
	 */
	public int getID() {
		return ID;
	}
	/**
	 * Sets: the Id of this entity.
	 * @param ID the Id of this entity.
	 */
	public void setID(int iD) {
		ID = iD;
	}
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
}