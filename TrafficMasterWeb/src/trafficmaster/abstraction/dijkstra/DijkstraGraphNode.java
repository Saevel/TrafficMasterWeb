package trafficmaster.abstraction.dijkstra;
import java.util.List;

import trafficmaster.core.Arrival;


public abstract class DijkstraGraphNode<InnerClass> implements Comparable<DijkstraGraphNode<InnerClass>> {

	public DijkstraGraphNode (InnerClass innerElement) {
		this.innerElement = innerElement;
	}
	
	protected InnerClass innerElement;
	
	protected double tag = Double.POSITIVE_INFINITY;

	public abstract List<DijkstraGraphNode<InnerClass>> getConnectedNodes();
	
	public boolean equals(Object o) {
		
		if(o instanceof DijkstraGraphNode<?>) {
			DijkstraGraphNode<?> n = (DijkstraGraphNode<?>)o;
			return innerElement.equals(((DijkstraGraphNode) o).innerElement);
		}
		
		return false;
	}
	
	public double getTag() {
		return tag;
	}
	
	public void setTag(double tag) {
		this.tag = tag;
	}
	
	@Override
	public int compareTo(DijkstraGraphNode node) {
		if(node == null) {
			throw new NullPointerException("GraphNode|compareTo|Cannot compare to a null object");
		}
		
		return (int)Math.signum(this.tag - node.tag);
	}
	
	public InnerClass getInnerElement() {
		return innerElement;
	}

	
}
