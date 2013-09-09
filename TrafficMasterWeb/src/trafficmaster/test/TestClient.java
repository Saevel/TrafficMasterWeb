package trafficmaster.test;

import java.io.NotSerializableException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.Event;
import trafficmaster.core.EventGravity;
import trafficmaster.core.EventType;
import trafficmaster.core.FunctionType;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Line;
import trafficmaster.core.MeansOfTransport;
import trafficmaster.core.Position;
import trafficmaster.core.ScheduleType;
import trafficmaster.core.State;
import trafficmaster.core.Status;
import trafficmaster.core.Stop;
import trafficmaster.core.beans.DaoTestBean;
import trafficmaster.core.beans.DaoTestBeanRemote;
import trafficmaster.core.beans.LoggerBean;
import trafficmaster.core.beans.LoggerBeanRemote;
import trafficmaster.core.beans.NewsFeedBean;
import trafficmaster.core.beans.NewsFeedBeanRemote;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;
import trafficmaster.serializable.json.JsonFactory;

import com.ibytecode.clientutility.JNDILookupClass;

public class TestClient {

	static Position ciolkowskiego7 = new Position();
	static Position mojDom = new Position();
	static Position rondoLotnikow = new Position();
	
	static Course firstCourse = new Course();
	
	static Line directLine = new Line();
	
	static Arrival arrival = new Arrival();
	static Arrival secondArrival = new Arrival();
	
	static Date siodmaPietnascie = new Date();
	static Date osmaJedenasice = new Date();
	static Date dziewiataDwadziescia = new Date();
	static Date dziesiataPiec = new Date();
	
	static {
		ciolkowskiego7.setLatitude(51.729591);
		ciolkowskiego7.setLongitude(19.460466);
		ciolkowskiego7.setName("Ciolkowskiego 7 Ulica");
		rondoLotnikow.setLatitude(51.731300);
		rondoLotnikow.setLongitude(19.452948);
		rondoLotnikow.setName("Rondo Lotnikow Lwowskich");
		mojDom.setLatitude(51.729453);
		mojDom.setLongitude(19.460308);
		mojDom.setName("Moj Dom");
	}
	
	static Stop przystanekCiolkowskiego = new Stop();
	static Stop przystanekComarch = new Stop(); 
	
	static Course course = new Course();
	static State state = new State();
	
	static {
		course.setActive(true);
		course.setLine(directLine);
		
		state.setDelay(new Date());
		state.setPosition(ciolkowskiego7);
		state.setStatus(Status.AT_A_STOP);
		
		course.setState(state);
	}
	
	static {
		przystanekComarch.setLatitude(51.774629);
		przystanekComarch.setLongitude(19.477826);
		przystanekComarch.setName("Comarch");
		przystanekComarch.setStopName("Przystanek:Comarch");
		
		przystanekCiolkowskiego.setLatitude(51.728773);
		przystanekCiolkowskiego.setLongitude(19.457442);
		przystanekCiolkowskiego.setName("Ciolkowskiego");
		przystanekCiolkowskiego.setStopName("Przystanek:Ciolkowskiego");
	}
	
	static {
		directLine.setName("DirectLine");
		directLine.setDirection(true);
		directLine.setMeansOfTransport(MeansOfTransport.BUS);
	}	
		
	static {
		siodmaPietnascie.setHours(7);
		siodmaPietnascie.setMinutes(15);
		osmaJedenasice.setHours(8);
		osmaJedenasice.setHours(11);
		dziewiataDwadziescia.setHours(9);
		dziewiataDwadziescia.setMinutes(20);
		dziesiataPiec.setHours(10);
		dziesiataPiec.setMinutes(5);
	}
	
	static {	
		arrival.setScheduleType(ScheduleType.WEEK);
		arrival.setCourse(course);
		arrival.setPosition(przystanekCiolkowskiego);
		arrival.setArrivalTime(siodmaPietnascie);
		
		secondArrival.setScheduleType(ScheduleType.WEEK);
		secondArrival.setCourse(course);
		secondArrival.setPosition(przystanekComarch);
		secondArrival.setArrivalTime(osmaJedenasice);
		
	}
	
	public static void main(String[] args) {
		saveArrivals();
		saveGlobalSettings();
		
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			TrafficNetworkBeanRemote traffic = (TrafficNetworkBeanRemote)initialContext.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
							TrafficNetworkBeanRemote.class, ""));
			JsonFactory factory  = new JsonFactory();
			
			System.out.println(traffic.getStopByName(przystanekComarch.getStopName()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("DISTANCE: "+ mojDom.getDistanceTo(ciolkowskiego7));
		
	}
	
	static void getRelatedEvents() {
		
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			NewsFeedBeanRemote newsFeed = (NewsFeedBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", NewsFeedBean.class, NewsFeedBeanRemote.class, ""));
			List<Event>relatedEvents = newsFeed.getRelatedEvents(mojDom);
			if(!relatedEvents.isEmpty()) {
				System.out.println(relatedEvents.get(0).getName());
			}
			else {
				System.out.println("EmptyList");
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void reportSampleEvent() {
		
		List<Position> affected = new LinkedList<Position>();
		affected.add(ciolkowskiego7);
		affected.add(rondoLotnikow);
		
		Date date = new Date();
		date.setYear(113);
		date.setHours(15);
		date.setMinutes(10);
		date.setDate(1);
		date.setMonth(8);
		
		Event sampleEvent = new Event();
		
		sampleEvent.setActive(true);
		sampleEvent.setDescription("This is a sample event used for tests");
		sampleEvent.setGravity(EventGravity.MEDIUM);
		sampleEvent.setName("SampleEvent");
		sampleEvent.setType(EventType.OTHER);
		sampleEvent.setAffected(affected);
		sampleEvent.setOcurrenceDate(date);
		
		Context initialContext;
		
		try {
			initialContext = JNDILookupClass.getInitialContext();
			NewsFeedBeanRemote newsFeed = (NewsFeedBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", NewsFeedBean.class, NewsFeedBeanRemote.class, ""));
			newsFeed.reportEvent(sampleEvent);
			System.out.println("EVENT REPORTED!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void saveGlobalSettings() {
		
		GlobalSettings settings = new GlobalSettings();
		
		settings.setAverageWalkVelocity(1.39);
		settings.setChangeScoreDecayFunction(FunctionType.LINEAR);
		settings.setPositionDistinctionDistance(30);
		settings.setTimeScoreDecayFunction(FunctionType.LINEAR);
		settings.setWalkScoreDecayFunction(FunctionType.LINEAR);
		settings.setCityName("Lodz");
		settings.setDelayPenaltyPerMinute(10);
		Context initialContext;
		
		try {
			initialContext = JNDILookupClass.getInitialContext();
			DaoTestBeanRemote DAO = (DaoTestBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", DaoTestBean.class, DaoTestBeanRemote.class, ""));
			
			DAO.saveGlobalSettings(settings);
			
			System.out.println("GLOBAL SETTINGS SAVED!");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static  void getNearestStop() {
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			TrafficNetworkBeanRemote trafficNetwork = (TrafficNetworkBeanRemote)initialContext.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
							TrafficNetworkBeanRemote.class, ""));
			List<Stop> nearbyStops = trafficNetwork.getNearbyStops(mojDom, 1);
			Iterator<Stop> i = nearbyStops.iterator();
			while(i.hasNext()) {
				System.out.println(i.next().getStopName());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveExampleStops() {
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			DaoTestBeanRemote DAO = (DaoTestBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", DaoTestBean.class, DaoTestBeanRemote.class, ""));
			
			DAO.saveStop(przystanekCiolkowskiego);
			DAO.saveStop(przystanekComarch);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	static void saveArrivals() {
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			DaoTestBeanRemote DAO = (DaoTestBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", DaoTestBean.class, DaoTestBeanRemote.class, ""));
			
			DAO.saveArrival(arrival);
			DAO.saveArrival(secondArrival);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	static void saveLine() {
		try {
			Context initialContext = JNDILookupClass.getInitialContext();
			DaoTestBeanRemote DAO = (DaoTestBeanRemote)initialContext.lookup(JNDILookupClass.getLookupName("", "TrafficMasterWeb", DaoTestBean.class, DaoTestBeanRemote.class, ""));
			
			DAO.saveLine(directLine);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void getStopsForLine() throws NamingException {
		
		Context initialContext = JNDILookupClass.getInitialContext();
		TrafficNetworkBeanRemote trafficNetwork = (TrafficNetworkBeanRemote)initialContext.lookup(
				JNDILookupClass.getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
						TrafficNetworkBeanRemote.class, ""));
		
		Stop stop = trafficNetwork.getStopByName(przystanekCiolkowskiego.getStopName());
		List<Line>lines = trafficNetwork.getLinesForStop(stop.getID());
		System.out.println(lines.get(0).getName());
	}
	
	
	public static void getNewCourse() throws NamingException {
		Context initialContext = JNDILookupClass.getInitialContext();
		LoggerBeanRemote logger = (LoggerBeanRemote)initialContext.lookup(
				JNDILookupClass.getLookupName("", "TrafficMasterWeb", LoggerBean.class,
						LoggerBeanRemote.class, ""));
		
		course = logger.startNewCourse(directLine.getName(), ciolkowskiego7);
		
		JsonFactory factory = new JsonFactory();
		
		try {
			System.out.println(factory.serialize(course));
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
