package trafficmaster.core.beans;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibytecode.clientutility.JNDILookupClass;

import trafficmaster.core.Arrival;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Line;
import trafficmaster.core.Stop;
/**
 * Session Bean implementation class DaoTestBean
 */
@Stateless
public class DaoTestBean implements DaoTestBeanRemote {

	@PersistenceContext(name="JPADB")
	EntityManager manager;
	
	@Override
	public void saveGlobalSettings(GlobalSettings settings) {
		manager.persist(settings);
	}

	@Override
	public void saveStop(Stop stop) {
		try {
			manager.persist(stop);
		} catch(Exception e) {
			try {
				manager.merge(stop);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void saveArrivals(Collection<Arrival> arrivals) {
		if(arrivals == null || arrivals.isEmpty()) {
			return;
		}
		
		Iterator<Arrival> i = arrivals.iterator();
		while(i.hasNext()) {
			saveArrival(i.next());
		}
	}
	
	public void saveArrival(Arrival arrival) {
		manager.persist(arrival);	
	}
	
	public void saveLine(Line line) {
		try {
			manager.persist(line);
		} catch(Exception e) {
			try {
				manager.merge(line);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
