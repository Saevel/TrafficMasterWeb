package trafficmaster.core.beans;

import java.util.Collection;

import javax.ejb.Remote;

import trafficmaster.core.Arrival;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Line;
import trafficmaster.core.Stop;

@Remote
public interface DaoTestBeanRemote {

	public void saveGlobalSettings(GlobalSettings settings);

	public void saveStop(Stop stop);

	public void saveArrival(Arrival arrival);
	
	public void saveArrivals(Collection<Arrival> arrivals);
	
	public void saveLine(Line line);
}
