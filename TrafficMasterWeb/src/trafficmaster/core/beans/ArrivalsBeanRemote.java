package trafficmaster.core.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.Stop;

@Remote
public interface ArrivalsBeanRemote {
	/**
	 * Gets a list of arrival of all the <code>Line</code>s at the <code>stop</code> within the given
	 * <code>timeContenxt</code>
	 * @param stop the <code>Stop</code> for which the arrivals are to be obtained.
	 * @param timeContext the <code>Date</code> around which the arrivals are researched.
	 * @return  all the suitable <code>Arrival</code>s. 
	 */
	Map<Date, Course> getArrivalsForStop(int stopID,Date timeContext);
	/**
	 * Gets all the arrivals of the <code>line</code> at the <code>stop</code>.
	 * @param stop the <code>Stop</code> for which the arrivals are to be obtained.
	 * @param line the <code>Line</code> for which the arrivals are obtained.
	 * @param timeContext the <code>Date</code> around which the arrivals are researched.
	 * @return the suitable <code>Arrival</code> object. 
	 */
	Map<Date, Stop> getArrivals(int stopID, int lineID, Date timeContext);
	/**
	 * Gets the next arrival of the <code>line</code> at the <code>stop</code>.
	 * @param stop the <code>Stop</code> for which the arrivals are to be obtained.
	 * @param line the <code>Line</code> for which the arrivals are obtained.
	 * @param timeContext the <code>Date</code> around which the arrivals are researched.
	 * @return the <code>Date</code> giving the time of the desired arrival.
	 */
	Date getNextArrival(int stopID, int lineID, Date timeContext);
	
	/**
	 * 
	 * @param courseID
	 * @param timeContext
	 * @return
	 */
	Map<Date,Stop> getArrivalsForCourse(int courseID, Date timeContext);
	/**
	 * Gets all the <code>Arrival</code>s for all the <code>Stop</code>s that are
	 * connected to the <code>Stop<code> given by <code>stopID<code> by any kind of
	 * <code>MeansOfTransport</code> and ordered from the earliest to the latest.
	 * @param stopID the <code>ID</code> of the reference <code>Stop</code>.
	 * @return  All the <code>Arrival</code>s for all the <code>Stop</code>s that are
	 * connected to the <code>Stop<code> given by <code>stopID<code> by any kind of
	 * <code>MeansOfTransport</code> and ordered from the earliest to the latest. 
	 */
	List<Arrival> getAllConnectedStopsArrivals(int stopID);
	
}
