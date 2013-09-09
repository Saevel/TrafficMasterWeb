package trafficmaster.helpers;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import com.ibytecode.clientutility.JNDILookupClass;

import trafficmaster.abstraction.dijkstra.DijkstraGraphNode;
import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Position;
import trafficmaster.core.Stop;
import trafficmaster.core.beans.ArrivalsBean;
import trafficmaster.core.beans.ArrivalsBeanRemote;
import trafficmaster.core.beans.LoggerBean;
import trafficmaster.core.beans.LoggerBeanRemote;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

public class TrafficGraphNode extends DijkstraGraphNode<Arrival> {

	public TrafficGraphNode(Arrival innerElement) {
		super(innerElement);
	}

	/*@Override
	public int compareTo(DijkstraGraphNode<Arrival> arg0) {
		if(tag==arg0.getTag()) {
			return 0;
		}
		else if(tag>arg0.getTag()) {
			return 1;
		}
		else {
			return -1;
		}
			
	}*/

	@Override
	public List getConnectedNodes() {
		/*Retriving all the nearby stops which we can reach on foot*/
		List<TrafficGraphNode> nodes = new LinkedList<TrafficGraphNode>();
		nodes.addAll(getAllPhysicallyConnected());
		/*If we are on a stop, checking all the passing lines for routing*/
		 if(innerElement.getPosition() instanceof Stop ){
			 nodes.addAll(getAllLogicallyConnected());
		 }
		
		return nodes;
		
	}
	// KLUDGE : zamienic potem na private!!!
	public List<TrafficGraphNode> getAllLogicallyConnected() {
		
		Context context;
		try { /*Getting an instance of the ArrivalsBean*/
			context = JNDILookupClass.getInitialContext();
			ArrivalsBeanRemote arrivalsBean  = (ArrivalsBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb",ArrivalsBean.class,
							ArrivalsBeanRemote.class, ""));
			/*Fetching all the arrivals for all the stops connected to the current one
			 * by any kind of MeansOfTransport*/
			List<Arrival> arrivals = arrivalsBean.getAllConnectedStopsArrivals(
					this.getInnerElement().getPosition().getID());
			
			Iterator<Arrival> i = arrivals.iterator();
			List<TrafficGraphNode> nodeList = new LinkedList<TrafficGraphNode>();
			/*Wrapping all the found Arrivals into graph nodes*/
			while(i.hasNext()) {
				nodeList.add(new TrafficGraphNode(i.next()));
			}
			
			return nodeList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	// KLUDGE potem zamieniæ na prywatne!!!
	public List<TrafficGraphNode> getAllPhysicallyConnected() {

		Context context;
		try { /*Getting an instance of the ArrivalsBean*/
			context = JNDILookupClass.getInitialContext();
			TrafficNetworkBeanRemote trafficBean  = (TrafficNetworkBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb",TrafficNetworkBean.class,
							TrafficNetworkBeanRemote.class, ""));
			/*Getting the LoggerBean*/
			LoggerBeanRemote logger  = (LoggerBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb",LoggerBean.class,
							LoggerBeanRemote.class, ""));
			/*Asking for GlobalSettings*/
			GlobalSettings settings = logger.logIn("", "");
			
			// KLUDGE : Magic numbers!!!
			/* Fetches 4 nearby stops */
			List<Stop> stops = trafficBean.getNearbyStops(innerElement.getPosition(), 4);
			/*Iteration preparations*/
			Arrival helper;
			Stop nextStop;
			List<TrafficGraphNode> nodes = new LinkedList<TrafficGraphNode>();
			Iterator<Stop> i = stops.iterator();
			
			double walkTime=0;
			Date startTime;
			/*Checking all the retrieved stops*/
			while(i.hasNext()) {
				
				nextStop = i.next();
				/*Walking time IN MINUTES*/
				walkTime = (innerElement.getPosition().getDistanceTo(nextStop)/settings.getAverageWalkVelocity())/60;
				/*Creating the new time - the arrival on the connected Stop when going on foot*/
				startTime = (Date)innerElement.getArrivalTime().clone();
				DateUtils.addDelay(0, (int) Math.round(walkTime), startTime);
				/*constructing a helper Arrival object*/
				helper = new Arrival();
				/*Herein the assumed transport type for this type of search in on foot = null*/
				helper.setCourse(null);
				/*The position is each stop in turn*/
				helper.setPosition(nextStop);
				/*The arrival time is the time when we can reach the stop by foot*/
				helper.setArrivalTime(startTime);
				/*Wrapping all up into a TrafficGraphNode as required*/
				nodes.add(new TrafficGraphNode(helper));
			}
			
			return nodes;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date getArrivalTime() {
		return this.innerElement.getArrivalTime();
	}
	
	public Position getPosition() {
		return this.innerElement.getPosition();
	}
	
	public Course getCourse() {
		return this.innerElement.getCourse();
	}
	
	public double getPhysicalDistanceTo(TrafficGraphNode node) {
		return innerElement.getPosition().getDistanceTo(node.getPosition());
	}
	
	
}