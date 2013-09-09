package trafficmaster.core.beans;

import java.util.List;

import javax.ejb.Remote;

import trafficmaster.core.Event;
import trafficmaster.core.Position;

@Remote
public interface NewsFeedBeanRemote {

	/**
	 * Gets <code>howMany</code> latest <code>Event</code>s.
	 * @param howMany the number of <code>Event</code>s to be fetched.
	 * @return a <code>List</code> of <code>howMany<code> latest <code>
	 * Event</code>s sorted descending by <code>occurrenceDate<code>.
	 */
	public List<Event> getEvents(int howMany);
	/**
	 * Saves the <code>event</code> in the system without further ado.
	 * @param event the <code>Event</code> to be saved.
	 */
	public boolean reportEvent(Event event);
	/**
	 * Returns a <code>List</code> of <code>Event</code>s related to the <code>currentPosition
	 * </code> (affecting it directly) or an empty <code>List</code> if none affecting are
	 * found or the <code>currentPosition</code> is <code>null</code>.
	 * @param currentPosition the current <code>Position</code> of the requestor.
	 * @return a <code>List</code> of <code>Event</code>s related to the <code>currentPosition
	 * </code> (affecting it directly) or an empty <code>List</code> if none affecting are
	 * found or the <code>currentPosition</code> is <code>null</code>.
	 */
	public List<Event> getRelatedEvents(Position currentPosition);
	
	
}


