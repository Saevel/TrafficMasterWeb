package trafficmaster.core.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.ScheduleType;
import trafficmaster.core.Stop;
import trafficmaster.helpers.DateUtils;

/**
 * Session Bean implementation class ArrivalsBean
 */
@Stateless
public class ArrivalsBean implements ArrivalsBeanRemote {

	@PersistenceContext(name="JPADB")
	EntityManager manager;
	
	@Override
	public Map<Date,Stop> getArrivalsForCourse(int courseID, Date timeContext) {
		
		/*Querying for all the arrivals on this date for this course*/
		Query arrivalQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE" +
				" a.course.ID = :courseID AND a.scheduleType = :scheduleType ORDER BY " +
				"a.arrivalTime");
		/*Query parameter setup with schedule type inferred*/
		arrivalQuery.setParameter("stopID", courseID);
		arrivalQuery.setParameter("scheduleType", getScheuleTypeByDate(timeContext));
		/*Used collections*/
		Map<Date,Stop> mapped = new HashMap<Date,Stop>();	
		List<Arrival> arrivals = arrivalQuery.getResultList();
		/*Iteration helpers*/
		Iterator<Arrival> i = arrivals.iterator();
		Arrival nextArrival;
		/*Rewriting the result to the desired form*/
		
		while(i.hasNext()) {
			nextArrival = i.next();
			if(nextArrival.getPosition() instanceof Stop) {
				mapped.put(nextArrival.getArrivalTime(),(Stop)nextArrival.getPosition());
			}
		}
		return mapped;
	}

	@Override
	public Map<Date,Course> getArrivalsForStop(int stopID, Date timeContext) {
		/*Querying for all the arrivals on this date for this stop*/
		Query arrivalQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE" +
				" a.position.ID = :stopID AND a.scheduleType = :scheduleType ORDER BY " +
				"a.arrivalTime");
		/*Query parameter setup with schedule type inferred*/
		arrivalQuery.setParameter("stopID", stopID);
		arrivalQuery.setParameter("scheduleType", getScheuleTypeByDate(timeContext));
		/*Used collections*/
		Map<Date,Course> mapped = new HashMap<Date,Course>();	
		List<Arrival> arrivals = arrivalQuery.getResultList();
		/*Iteration helpers*/
		Iterator<Arrival> i = arrivals.iterator();
		Arrival nextArrival;
		/*Rewriting the result to the desired form*/
		while(i.hasNext()) {
			nextArrival = i.next();
			mapped.put(nextArrival.getArrivalTime(),nextArrival.getCourse());
		}
		return mapped;
	}

	@Override
	public Map<Date,Stop> getArrivals(int stopID, int courseID, Date timeContext) {
		/*Querying for all the matching arrivals*/
		Query arrivalQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE " +
				"a.position.ID = :stopID AND a.course.ID = :courseID AND a.scheduleType = " +
				":scheduleType ORDER BY a.arrivalTime");
		/*Parameter setup with schedule type deduction*/
		arrivalQuery.setParameter("stopID",stopID);
		arrivalQuery.setParameter("courseID", courseID);
		arrivalQuery.setParameter("scheduleType", getScheuleTypeByDate(timeContext));
		/*For iteration and retrieval*/
		Map<Date,Stop> mapped = new HashMap<Date,Stop>();
		List<Arrival> arrivals = (List<Arrival>)arrivalQuery.getResultList();
		Iterator<Arrival> i = arrivals.iterator();
		Arrival nextArrival = null;
		/*Mapping the stops to their arrival times */
		while(i.hasNext()) {
			nextArrival = i.next();
			if(nextArrival.getPosition() instanceof Stop) {
				mapped.put( nextArrival.getArrivalTime(),(Stop)nextArrival.getPosition());
			}
			
		}
		/*Returning the result*/
		return mapped;
		
	}

	@Override
	public Date getNextArrival(int stopID, int courseID, Date timeContext) {
		/*Querying for all arrivals of that stop to that line in a given time context*/
		Query arrivalQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE" +
				" a.stop.ID = :stopID AND a.course.ID= : courseID AND a.scheduleType= " +
				":arrivalScheduleType ORDER BY a.arrivalTime");
		arrivalQuery.setParameter("stopID", stopID);
		arrivalQuery.setParameter("lineID", courseID);
		/*Specifying the time context as type of schedule and passing as the query parameter*/
		arrivalQuery.setParameter("arrivalScheduleType", getScheuleTypeByDate(timeContext));
		
		/*Retringin the arrivals list*/
		List<Arrival> arrivals = (List<Arrival>) arrivalQuery.getResultList();
		
		if(arrivals == null || arrivals.isEmpty()) {
			return null;
		}
		/*Iteration variables*/		
		Iterator<Arrival> i = arrivals.iterator();
		Arrival nextArrival = null;
		/*Since the arrivals are already sorted by date - searching for the first one that is
		 * later than the timeContext*/
		while(i.hasNext()) {
			nextArrival = i.next();
			if(DateUtils.getTimeDifferenceInMinutes(nextArrival.getArrivalTime(),timeContext)<0) {
				break;
			}
		}
		/*Returning the result, if any*/		
		if(nextArrival == null) {
			return null;
		}
		else {
			return nextArrival.getArrivalTime();
		}
		
	}

	private ScheduleType getScheuleTypeByDate(Date timeContext) {
		int dayAsNumber = timeContext.getDay();
		if(dayAsNumber>0 && dayAsNumber<6) {
			return ScheduleType.WEEK;
		}
		else if(dayAsNumber == 0) {
			return ScheduleType.SUNDAY;
		}
		else if(dayAsNumber == 6 ){
			return ScheduleType.SATURDAY;
		}
		return null;
	}

	@Override
	public List<Arrival> getAllConnectedStopsArrivals(int stopID) {
		Query killerQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE " +
				"a.course.ID IN ( SELECT A.course.ID FROM Arrival AS A WHERE " +
				"a.position.ID = :stopID ) ORDER BY a.arrivalTime");
		
		killerQuery.setParameter("stopID", stopID);
		
		List<Arrival> arrivals = killerQuery.getResultList();
		
		return arrivals;
	}
	
	
}
