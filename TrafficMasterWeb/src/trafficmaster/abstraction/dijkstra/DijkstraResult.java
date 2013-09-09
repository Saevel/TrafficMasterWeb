package trafficmaster.abstraction.dijkstra;
import java.util.LinkedList;
import java.util.List;


public class DijkstraResult {

	private double score = 0;
	
	private List<DijkstraGraphNode> path = new LinkedList<DijkstraGraphNode>();
	
	public double getScore() {
		return this.score;
	}
	
	public void resetScore() {
		this.score= 0;
	}
	
	public void incrementScore(double increment) {
		this.score += increment;
	}
	
	public void resetPath() {
		path.clear();
	}
	
	public void addToPath(DijkstraGraphNode node) {
		path.add(node);
	}
	
	public List<DijkstraGraphNode> getPath() {
		return path;
	}
	
	public boolean pathContains(DijkstraGraphNode node) {
		return path.contains(node);
	}
	
}
