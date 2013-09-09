package trafficmaster.core.beans;

import trafficmaster.helpers.DateUtils;
import trafficmaster.helpers.TrafficGraphNode;
import trafficmaster.helpers.RoutingResult;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateful;
import javax.naming.Context;

import com.ibytecode.clientutility.JNDILookupClass;

import trafficmaster.abstraction.dijkstra.DijkstraGraphNode;
import trafficmaster.abstraction.dijkstra.DijkstraPathfinder;
import trafficmaster.abstraction.dijkstra.DijkstraResult;
import trafficmaster.core.Arrival;
import trafficmaster.core.Course;
import trafficmaster.core.Criteria;
import trafficmaster.core.GlobalSettings;
import trafficmaster.core.Line;
import trafficmaster.core.Position;
import trafficmaster.core.RouteFragment;
import trafficmaster.core.State;
import trafficmaster.core.Status;
import trafficmaster.core.Stop;

/**
 * Session Bean implementation class PlannerBean
 */
@Stateful
public class PlannerBean extends DijkstraPathfinder<TrafficGraphNode> implements PlannerBeanRemote {

	private Criteria criteria;
	private int changeCount;
	private int overallDuration;
	private double walkingDistance;
	
	@Override
	public List<RouteFragment> getOptimalRoute(Position from, Position to,
			Date when, Criteria criteria, Date timeContext) {

		changeCount = 0;
		this.criteria = criteria;
		this.overallDuration = 0;
		this.walkingDistance = 0;
		
		Context context;
		
		try { /*Retriving EJB*/
			context = JNDILookupClass.getInitialContext();
			TrafficNetworkBean bean = (TrafficNetworkBean)context.lookup(JNDILookupClass.
				getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
						TrafficNetworkBeanRemote.class, ""));
			/*Building the desired from Arrival*/
			Arrival fromArrival = new Arrival();
			/*The start position is "from"*/
			fromArrival.setPosition(from);
			/*The starting time is "when"*/
			fromArrival.setArrivalTime(when);
			/*The starting course is undefined*/
			fromArrival.setCourse(null);
			
			/*Buiding the desired "to" arrival*/
			Arrival toArrival = new Arrival();
			/*The final course is "by foot"*/
			toArrival.setCourse(null);
			/*The final position is "to*/
			toArrival.setPosition(to);
			/*The arrival time is undefined (will be inferred by the algorithm"*/
			
			/*Wrapping the desired arrivals into TrafficGraphNodes to abstract from their 
			 * physical characteristics and perceive them as plain graph nodes*/
			TrafficGraphNode fromNode = new TrafficGraphNode(fromArrival);
			TrafficGraphNode toNode = new TrafficGraphNode(toArrival);
			/*Executing the generic Dijkstra algorithm to find the optimal path*/
			DijkstraResult result = this.findOptimalPath(fromNode, toNode);
			
			List<DijkstraGraphNode> path = result.getPath();
			Iterator<DijkstraGraphNode> i = path.iterator();
			
			TrafficGraphNode node;
			
			Line nextLine, currentLine;
			
			while(i.hasNext()) {
				
				node = (TrafficGraphNode) i.next();
				currentLine = node.getCourse().getLine();
				
				// TODO przepisanie z obecnej formy na List<RouteFragment> i zwrócienie
				
			}
			
		}
		catch(Exception e) {
			return null;
		}
		
		return null;
	}

	@Override
	public Boolean isRouteStillOptimal() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getWeight(TrafficGraphNode from, TrafficGraphNode to) {
		
		double score = 0;
		Context context;
		
		/*There is no connection from a node it iself - prevents infinite looping*/
		if(from.equals(to)) {
			return Double.POSITIVE_INFINITY;
		}
		
		try { /*Checking the nearest courses to the current location*/
			context = JNDILookupClass.getInitialContext();
			TrafficNetworkBean bean = (TrafficNetworkBean)context.lookup(JNDILookupClass.
				getLookupName("", "TrafficMasterWeb", TrafficNetworkBean.class,
						TrafficNetworkBeanRemote.class, ""));
		
			GlobalSettings settings = getGlobalSettings();
			
			
			/*Obtaining the course object and veryfing the user status*/
			if(to.getCourse() != null ) {
				State state = to.getCourse().getState();
				Status status = state.getStatus();
				/*Jammed, inactive or damaged courses are ignored by default*/
				if(status.equals(Status.ACCIDENT) || status.equals(Status.JAMMED)) {
					return Double.POSITIVE_INFINITY;
				}/*For a delay , a specified penalty is added*/
				else if(status.equals(Status.LATE)) {
					score += DateUtils.timeToMinutes(state.getDelay())
							*settings.getDelayPenaltyPerMinute();
					
				}
			}
		
	} catch(Exception e) {
		
		return Double.POSITIVE_INFINITY;
	}
		
		/*If the lines are not equal , a change has taken place*/
		if( from.getInnerElement().getCourse() == null ||
				!from.getInnerElement().getCourse().equals(to.getInnerElement().getCourse())) {
			changeCount ++;
			/*If there were too many changes already, reporting the change as impossible*/
			if(changeCount >= criteria.getMaximalChangeCount()) {
				return Double.POSITIVE_INFINITY;
			}
			else {/*If not, adding the punishment for a change and continuing*/
				score += criteria.getChangePenalty();
			}
		}
		/*If the fragment is to be traversed by foot, walking penalties are applied*/
		if(to.getCourse() == null) {
			
			walkingDistance += from.getPhysicalDistanceTo(from);
			/*if the overall walking distance is surpassed, the route is blocked*/
			if(walkingDistance >= criteria.getMaximalWalkingDistance()) {
				return Double.POSITIVE_INFINITY;
			}
			/*If not, the score is augmented by a propper value*/
			score+= criteria.getWalkScoreCoefficient()*from.getPhysicalDistanceTo(to);
		}
		/*Getting the duration of the voysage between the delegations.*/
		int duration = DateUtils.getTimeDifferenceInMinutes(to.getArrivalTime(), from.getArrivalTime());
		/*In any case, ensuring the duration is not negative, as this woul totally revert its effect*/
		duration = (int) (duration * Math.signum(duration));
		/*applying the penalty for travel time*/
		score += criteria.getMinuteScoreCoefficient()*duration;
		
		return score;
	}

	private double getWalkingTimeInMinutes(double distance) {
		return (distance/getGlobalSettings().getAverageWalkVelocity())/60;
	}
	

	public GlobalSettings getGlobalSettings() {
		
		Context context;
		
		try {
			context = JNDILookupClass.getInitialContext();
			LoggerBeanRemote logger = (LoggerBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb", LoggerBean.class,
					LoggerBeanRemote.class, ""));
			
		GlobalSettings settings =  logger.logIn("", "");
		
		return settings;
		
		} catch(Exception e) {
			return null;
		}
	}
	
}

