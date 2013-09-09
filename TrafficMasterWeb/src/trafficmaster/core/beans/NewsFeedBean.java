package trafficmaster.core.beans;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Collections;

import trafficmaster.core.Event;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Position;

/**
 * Session Bean implementation class NewsFeedBean
 */
@Stateless
public class NewsFeedBean implements NewsFeedBeanRemote, Serializable {
	
	@PersistenceContext(name="JPADB")
	EntityManager manager;
	
    @Override
    public List<Event> getEvents(int howMany)  {
		/* Getting "howMany" the active Events sorted by occurrenceDate*/
    	Query generalQuery = manager.createQuery("SELECT e FROM Event AS e WHERE e.active = TRUE");
    	generalQuery.setMaxResults(howMany);
    	List<Event> retrievedEvents = (List<Event>)generalQuery.getResultList();
    	/*Returning the result*/
    	return retrievedEvents;
    }

	@Override
	public boolean reportEvent(Event event) {
		/*If there is anything to save, simply saving it*/
		if(event!=null) {
			manager.persist(event);
		}
		return true;
	}
	
	@Override
	public List<Event> getRelatedEvents(Position currentPosition) {
		/*A position that is null is not tolerated - an empty list is returned*/
		if(currentPosition == null) {
			return new LinkedList<Event>();
		}
		/*Checking the minimal distinction distance for positions*/
		Query settingsQuery = manager.createQuery("SELECT g.positionDistinctionDistance FROM GlobalSettings AS g");
		double minDistance = (Double)settingsQuery.getSingleResult();
		/*Getting all the possible active events*/
		Query eventQuery = manager.createQuery("SELECT e FROM Event AS e WHERE e.active = TRUE");
		List<Event> events = (List<Event>)eventQuery.getResultList();
		/*Event iterator*/
		Iterator<Event> eventIterator = events.iterator();
		/*The iterator of all the positions affected by a chosen Event*/
		Iterator<Position> affectedIterator;
		/*The return variable - the events related to the currentPosition*/
		List<Event> relatedEvents = new LinkedList<Event>();
		/*Event retrieved in an iteration*/
		Event nextEvent;
		
		while(eventIterator.hasNext()) {
			/*Checking the next event*/
			nextEvent = eventIterator.next();
			
			/*Checking positions affeced by this event*/
			affectedIterator = nextEvent.getAffected().iterator();
			
			while(affectedIterator.hasNext()) {
				/*If any of the positions affected is indiscernible from currentPosition we
				 * consier the Event to be related to the currentPosition, hence adding it to the
				 * result list*/
				
				double distance = affectedIterator.next().getDistanceTo(currentPosition);
				
				if(distance<minDistance) {
					relatedEvents.add(nextEvent);
					break;
				}
			}
		}
		return relatedEvents;
	}
}