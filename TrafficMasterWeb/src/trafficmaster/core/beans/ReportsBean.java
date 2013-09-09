package trafficmaster.core.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibytecode.clientutility.JNDILookupClass;

import trafficmaster.core.Course;
import trafficmaster.core.Event;
import trafficmaster.core.EventGravity;
import trafficmaster.core.EventType;
import trafficmaster.core.Position;
import trafficmaster.core.State;
import trafficmaster.core.Status;
import trafficmaster.core.Stop;

/**
 * Session Bean implementation class ReportsBean
 */
@Stateless
public class ReportsBean implements ReportsBeanRemote {

	
	@PersistenceContext
	EntityManager manager;
	
    public boolean reportPosition(Position position, int courseID) {
    	
    	Query courseStateQuery = manager.createQuery("SELECT c.state FROM Course AS c WHERE c.ID = :courseID");
    	courseStateQuery.setParameter("courseID", courseID);
    	State state = (State)courseStateQuery.getSingleResult();
    	
    	state.setPosition(position);
    	
    	manager.merge(state);
    	
    	return true;
    }
    
    public boolean reportStateChange(State newState, int courseID) {
    	
    	Query courseQuery = manager.createQuery("SELECT c FROM Course AS c WHERE c.ID = :courseID");
    	courseQuery.setParameter("courseID", courseID);
    	Course course = (Course)courseQuery.getSingleResult();
    	/*Only active courses can receive state upates*/
    	if(!course.getActive()) {
    		return false;
    	}
    	
    	Status newStatus = newState.getStatus();
    	course.setState(newState);
    	/*Saving the course update*/
    	manager.persist(newState.getPosition());
    	manager.merge(course);
    	
    	
    	if(newStatus.equals(Status.ACCIDENT) || newStatus.equals(Status.JAMMED) || newStatus.equals(Status.LATE)) {
    		
    		Context context = null;
			try {/*Retriving TrafficNetworkBean*/
				context = JNDILookupClass.getInitialContext();
				TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote) context.lookup(
	    				JNDILookupClass.getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
	    						TrafficNetworkBeanRemote.class, ""));
				/*Check all the stops on our current line*/
				List<Stop> stops = trafficBean.getStopsForLine(course.getLine().getID());
			
				Event event = new Event();
				/*All the associated people mentioned here*/
				event.getAffected().add(newState.getPosition());
	    		event.getAffected().addAll(stops);
	    		/*Activisation*/
	    		event.setActive(true);	
	    		
	    		/*Type-specific behavior defined here*/
	    		switch(newStatus) {
	    			case ACCIDENT :
	    				event.setType(EventType.ACCIDENT);
	    				event.setGravity(EventGravity.HIGH);
	    				break;
	    			case JAMMED : 
	    				event.setType(EventType.TRAFFIC_JAM);
	    				event.setGravity(EventGravity.HIGH);
	    				break;
	    			case LATE :
	    				event.setType(EventType.OTHER);
	    				event.setGravity(EventGravity.MEDIUM);
	    				break;
	    			default:
	    				break;
	    		}
	    		/*Saving the event(s)*/
	    		manager.persist(event);
	    		
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}	
    	}
    	
    	course.setState(newState);
    	
    	manager.merge(course);
    	
    	return true;
    }
}
