package trafficmaster.core.beans;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.Line;
import trafficmaster.core.Position;
import trafficmaster.core.Stop;

import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TrafficNetworkBean
 */
@Stateless
public class TrafficNetworkBean implements TrafficNetworkBeanRemote {

	/***
	 * A <code>Comparator</code> aimed at enabling to sort <code>Stop</code>s with regard to
	 * their distance to <code>referencePosition</code> from the nearest to the furthest.
	 * @author Zielony
	 * @version 1.0
	 */
	private class DescendingPositionProximityComparator implements Comparator<Position> {
		/**
		 * The position from which the distance is measured.
		 */
		private Position referencePosition;
		/**
		 * A constructor settings the <code>referencePosition</code>.
		 * @param referencePosition
		 */
		public DescendingPositionProximityComparator(Position referencePosition) {
			this.referencePosition = referencePosition;
		}
		
		@Override
		public int compare(Position pos1, Position pos2) {
			/*Getting the distances to the referencePosition*/
			double distance1 = pos1.getDistanceTo(referencePosition);
			double distance2 = pos2.getDistanceTo(referencePosition);
			/*If the distances are equal , marking it*/
			if(distance1==distance2) {
				return 0;
			}/*If pos1 is further to referencePosition than pos2, it should be further
			on the sorted List. */
			else if(distance1>distance2) {
				return +1;
			}/*If pos1 is closer to referencePosition than pos2, it should be earlier
			on the sorted List.*/
			else {
				return -1;
			}
		}
	}
	/**
	 * An <code>EntityManager</code> for EntityBeans manipulation.
	 */
	@PersistenceContext(name="JPADB")
	EntityManager manager;
	
	@Override
	public List<Stop> getNearbyStops(Position location, int howMany) {
		/*Selecting all the stops possible*/
		Query stopQuery = manager.createQuery("SELECT s FROM Position AS s WHERE s.stopName IS NOT NULL");
		List<Stop> stops = stopQuery.getResultList();
		/*Sorting the stops - from the nearest to the user location to the furthest*/
		Collections.sort(stops, new DescendingPositionProximityComparator(location));
		/*If the is less or equal stops than required, returning all there is*/
		int size = stops.size();
		if(stops.size()<=howMany) {
			return stops;
		}/*If there is more stops than required returning only first "howMany" */
		else {
			return new LinkedList(stops.subList(0, howMany));
		}
	}

	@Override
	public List<Stop> getStopsForLine(int lineID) {
		Query stopLineQuery = manager.createQuery("SELECT a.position FROM Arrival AS a WHERE a.course.line.ID=:Id ");
		stopLineQuery.setParameter("Id", lineID);
		try {
			List<Stop> stops = (List<Stop>)stopLineQuery.getResultList();
			return stops;
		} catch(Exception e) {
			return new LinkedList<Stop>();
		}
	}

	@Override
	public List<Line> getLinesForStop(int stopID) {
		Query lineStopQuery = manager.createQuery("SELECT a.course.line FROM Arrival AS a WHERE a.position.ID=:Id ");
		lineStopQuery.setParameter("Id", stopID);
		try {
			List<Line> lines = (List<Line>)lineStopQuery.getResultList();
			return lines;
		} catch(Exception e) {
			return new LinkedList<Line>();
		}
	}

	@Override
	public List<String> getAllLineNames() {
		Query lineNameQuery = manager.createQuery("SELECT l.name FROM Line AS l");
		try {
			List<String> lineNames = (List<String>)lineNameQuery.getResultList();
			Collections.sort(lineNames);
			return lineNames;
		} catch(Exception e) {
			return new LinkedList<String>();
		}
	}

	@Override
	public List<String> getAllStopNames() {
		Query stopNameQuery = manager.createQuery("SELECT s.stopName FROM Position AS s WHERE s.stopName IS NOT NULL");
		try {
			List<String> stopNames = (List<String>)stopNameQuery.getResultList();
			Collections.sort(stopNames);
			return stopNames;
		} catch(Exception e) {
			return new LinkedList<String>();
		}
	}

	@Override
	public Line getLineByName(String name) throws IllegalArgumentException {
		Query lineQuery = manager.createQuery("SELECT l FROM Line AS l WHERE l.name=:lineName");
		lineQuery.setParameter("lineName", name);
		try {
			Line line = (Line)lineQuery.getSingleResult();
			return line;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Stop getStopByName(String name) throws IllegalArgumentException {
		Query stopQuery = manager.createQuery("SELECT s FROM Position AS s WHERE s.stopName=:thisStopName AND s.stopName IS NOT NULL");
		stopQuery.setParameter("thisStopName", name);
		try {
			Stop stop = (Stop)stopQuery.getSingleResult();
			return stop;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public Course getNearestCourseForLine(int lineID, int stopID) {
		/*Selecting all the courses wit a non-null state.*/
		Query courseQuery = manager.createQuery("SELECT c FROM Course AS c WHERE c.line.ID = :lineID AND c.state IS NOT NULL");
		courseQuery.setParameter("lineID", lineID);
		List<Course> courses = courseQuery.getResultList();
		
		Query stopQuery = manager.createQuery("SELECT s FROM Position AS s WHERE s.ID = :stopID");
		stopQuery.setParameter("stopID", stopID);
		Stop stop = (Stop)stopQuery.getSingleResult();
		
		/*Current and minimal components - a draw*/
		double currentDistance;
		double minimalDistance = Double.POSITIVE_INFINITY;
		Course minimalDistanceCourse = null;
		Course currentCourse;
		Arrival courseArrival;
		Query arrivalQuery = manager.createQuery("SELECT a FROM Arrival AS a WHERE a.course.line.ID = :lineID AND a.stop.ID = :stopID");
		Iterator<Course> courseIterator = courses.iterator();
		while(courseIterator.hasNext()) {
			// -- || --- 
			currentCourse = courseIterator.next();
			
			currentDistance = currentCourse.getState().getPosition().getDistanceTo(stop);
			/*Finding minimal distance from Position*/
			if(currentDistance <= minimalDistance) {
				
				arrivalQuery.setParameter("lineID", lineID);
				arrivalQuery.setParameter("stopID", stopID);			
				
				minimalDistance = currentDistance;
				minimalDistanceCourse = currentCourse;
			}
		}
		
		return minimalDistanceCourse;
		
	}
}
