package trafficmaster.core;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * Represents a part of a <code>Route</code> traversed only using one <code>Course</code>
 * @author Zielony
 * @version 1.0
 * @see Course
 * @see Stop
 * @see Date
 * @see JsonSerializable
 */
@JsonSerializable
public class RouteFragment implements Serializable {
	/**
	 * The course which the sub route follows
	 */
	private Course course;
	/**
	 * Arrivals at locations traversed on the way
	 */
	private Map<Date,Stop> arrivals;
	/**
	 * Gets: the course realizing the sub route.
	 * @return  the course realizing the sub route.
	 */
	
	
	public Map<Date,Stop> getArrivals() {
		return arrivals;
	}
	public void setArrivals(Map<Date,Stop> arrivals) {
		this.arrivals = arrivals;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
}