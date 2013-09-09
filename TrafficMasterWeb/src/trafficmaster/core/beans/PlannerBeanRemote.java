package trafficmaster.core.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import trafficmaster.core.Criteria;
import trafficmaster.core.Position;
import trafficmaster.core.RouteFragment;

@Remote
public interface PlannerBeanRemote {

	/**
	 * Provides the <code>Route</code> encapsulating the optimal way to get from <code>from</code>
	 * to <code>to</code> starting at <code>when</code>, taking into accounts both the schedule
	 * data, the current traffic situation and traffic patterns and statistics for the region.
	 * @param from the start <code>Location</code> for the search
	 * @param to the destination of the search.
	 * @param when a <code>Date</code> giving the temporal context to the search.
	 * @param criteria a set of <code>Criteria</code> applied to rank <code>Route</code>
	 * optimality.
	 * instance, <code>false</code> if searches have already been conducted.
	 * @return the <code>Route</code> encapsulating the optimal way to get from <code>from</code>
	 * to <code>to</code> starting at <code>when</code>.
	 */
	List<RouteFragment> getOptimalRoute(Position from, Position to, Date when, Criteria criteria, Date timeContext);
	/**
	 * Checks if the formerly obtained <code>Route</code> (if existent) is still optimal considering
	 * the formely supplied data.
	 * @return <code>true</code> if the <code>Route</code> is still optimal , <code>false</code>
	 * otherwise.
	 * @throws IllegalStateException if no <code>Route</code> has been optained yet.
	 */
	Boolean isRouteStillOptimal() throws IllegalStateException;
	
}
