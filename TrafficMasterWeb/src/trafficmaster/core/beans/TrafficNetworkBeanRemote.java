package trafficmaster.core.beans;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import trafficmaster.core.Course;
import trafficmaster.core.Line;
import trafficmaster.core.Position;
import trafficmaster.core.Stop;

@Remote
public interface TrafficNetworkBeanRemote {
	/**
	 * Gets a few <code>Stop</code>s situated nearest to the <code>Location</code>.
	 * @param location the <code>Location</code> near which the stops are researched.
	 * @param howMany how many closest <code>Stop</code>s are being requested.
	 * @return a <code>Set</code> of <code>howMany Stop</code>s nearest to the <code>location</code>.
	 */
	List<Stop> getNearbyStops(Position location, int howMany);
	/**
	 * Fetches all the <code>Stop<code>s through which the <code>Line</code> referenced by <code>
	 * lineID </code> passes.
	 * @param lineID the <code>ID</code> of the reference <code>Line</code>.
	 * @return a <code>List</code> of all the <code>Stop</code>s through which the <code>
	 * Line</code> under <code>lineID</code> passes.
	 */
	List<Stop> getStopsForLine(int lineID);
	/**
	 * Fetches all the <code>Lines</code> that pass through the <code>Stop</code> given by
	 * <code>stopID</code>. 
	 * @param stopID the <code>ID</code> of the reference <code>Stop</code>.
	 * @return a <code>List</code> of all the <code>Line</code>s passing through the <code>
	 * Stop</code> given by <code>stopID</code>.
	 */
	List<Line> getLinesForStop(int stopID);
	/**
	 * Fetches a <code>List</code> of names of all the possible <code>Line</code>s.
	 * @return a <code>List</code> of names of all the possible <code>Line</code>s.
	 */
	List<String> getAllLineNames();
	/**
	 * Fetches a <code>List</code> of names of all the possible <code>Stop</code>s.
	 * @return a <code>List</code> of names of all the possible <code>Stop</code>s.
	 */
	List<String> getAllStopNames();
	/**
	 * Gets: a <code>Line</code> with the given <code>name</code>.
	 * @param name the <code>name</code> of the <code>Line</code>.
	 * @return the <code>Line</code> connected to the <code>name</code>.
	 * @throws IllegalArgumentException if there is no <code>Line</code> under the 
	 * <code>name</code>.
	 */
	Line getLineByName(String name) throws IllegalArgumentException;
	
	Stop getStopByName(String name) throws IllegalArgumentException;
	
	Course getNearestCourseForLine( int lineID, int stopID );
	
}
