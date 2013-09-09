package trafficmaster.core.beans;

import javax.ejb.Remote;

import trafficmaster.core.Course;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Position;

@Remote
public interface LoggerBeanRemote {

	public Course startNewCourse(String lineName, Position position);
	
	public boolean endCourse(int courseID);
	
	public GlobalSettings logIn(String username, String password) throws IllegalArgumentException;
	
}
