package trafficmaster.core.beans;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibytecode.clientutility.JNDILookupClass;

import trafficmaster.core.Course;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Line;
import trafficmaster.core.Position;
import trafficmaster.core.Status;
import trafficmaster.core.Stop;

/**
 * Session Bean implementation class LoggerBean
 */
@Stateless
public class LoggerBean implements LoggerBeanRemote {

	@PersistenceContext
	EntityManager manager;
	
    public GlobalSettings logIn(String username, String password) {
    	/*Uploading and returning the global application settings*/	
    	Query settingsQuery = manager.createQuery("SELECT S FROM GlobalSettings AS S");
    	GlobalSettings settings = (GlobalSettings) settingsQuery.getSingleResult();
    	return settings;
    }

	@Override
	public Course startNewCourse(String lineName, Position position) {
	
		Context context;
		try {
			context = JNDILookupClass.getInitialContext();
			TrafficNetworkBeanRemote trafficNetwork = (TrafficNetworkBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
					TrafficNetworkBeanRemote.class, ""));
			/*Retriving the line  by name*/
			Line line = (Line)trafficNetwork.getLineByName(lineName);
	    	/*Retriving the nearby stop*/
			Stop stop = (Stop)trafficNetwork.getNearbyStops(position, 1);
			/*Getting the course for a specified line and starting from the specified stop*/
			Course course = trafficNetwork.getNearestCourseForLine(line.getID(), stop.getID());
			/*Making the course active*/
			course.setActive(true);
			/*Setting the status to AT_STOP*/
			course.getState().setStatus(Status.AT_A_STOP);
	    	/*Updating the results */
			manager.merge(course);
			/*Returnign the course to the driver as a permission to run it.*/
			return course;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean endCourse(int courseID) {
		/*Getting the course whose ID has been submitted*/
		Query courseQuery = manager.createQuery("SELECT c FROM Course AS c WHERE c.id = :courseID");
		courseQuery.setParameter("courseID", courseID);
		Course course = (Course)courseQuery.getSingleResult();
		/*If there is no such course , reporting failure to terminate*/
		if(course == null) {
			return false;
		}
		/*If there is such a course, making it inactive and marking its journey as ended.*/
		course.setActive(false);
		course.getState().setStatus(Status.AT_A_STOP);
		/*Saving changes*/
		manager.merge(course);
		/*Marking the termination as successful*/
		return true;
	}

}
