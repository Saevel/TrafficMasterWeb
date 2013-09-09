package trafficmaster.core.beans;

import javax.ejb.Remote;

import trafficmaster.core.Position;
import trafficmaster.core.State;

@Remote
public interface ReportsBeanRemote {

	public boolean reportStateChange(State newState, int courseID);
	
	public boolean reportPosition(Position position, int courseID);
	
}
