package trafficmaster.helpers;

import java.util.LinkedList;
import java.util.List;

import trafficmaster.core.Line;
import trafficmaster.core.RouteFragment;

public class RoutingResult {

	private double score=0;
	
	private Line currentLine = null;
	
	private TrafficGraphNode currentNode = null;
	
	private List<RouteFragment> path = new LinkedList<RouteFragment>();

	private int changeCount = 0;
	
	private double distanceCount = 0.0;
	
	public RoutingResult(TrafficGraphNode currentNode) {
		this.currentNode = currentNode;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Line currentLine) {
		this.currentLine = currentLine;
	}

	public TrafficGraphNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(TrafficGraphNode currentNode) {
		this.currentNode = currentNode;
	}

	public List<RouteFragment> getPath() {
		return path;
	}

	public void setPath(List<RouteFragment> path) {
		this.path = path;
	}

	public int getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(int changeCount) {
		this.changeCount = changeCount;
	}

	public double getDistanceCount() {
		return distanceCount;
	}

	public void setDistanceCount(double distanceCount) {
		this.distanceCount = distanceCount;
	}
}
