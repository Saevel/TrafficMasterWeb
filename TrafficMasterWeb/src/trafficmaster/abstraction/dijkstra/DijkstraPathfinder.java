package trafficmaster.abstraction.dijkstra;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 
 * @author Zielony
 * @version 1.0
 * @param <TNode> the class, extending <code>GraphNode</code> that is a 
 * GraphNode wrapper implementation for a specific <code>class</code>
 * inherent to the structure "underneath" the graph.
 */
public abstract class DijkstraPathfinder<TNode extends DijkstraGraphNode> {

	protected DijkstraPathfinder() {
		;
	}
	
	protected boolean externalCall = true;
	
	/**
	 * The nodes of the graph that have already been used before and therefore contain
	 * information to be retrieved, such as tag values.
	 */
	protected List<TNode> investigatedNodes = new LinkedList<TNode>();
	/**
	 * The current state of search, storing the inner information such as path state
	 * at a time or current weight sum.
	 */
	protected DijkstraResult currentResult = new DijkstraResult();
	/**
	 * Gets the locally optimal (in the sense of Dijkstra Algorithm) path from <code>from
	 * </code> to <code>to</code>
	 * @param from
	 * @param to
	 * @return
	 * @throws NoSuchElementException
	 */
	
	public void refresh() {
		externalCall = true;
	}
	
	public DijkstraResult findOptimalPath(TNode from, TNode to) throws NoSuchElementException {
		
		if(externalCall) {
			from.setTag(0);
			externalCall = false;
		}
		
		if(!investigatedNodes.contains(from)) {
			investigatedNodes.add(from);
		}
		
		/*Adding the currently visited node to the path in currentResult*/
		currentResult.addToPath(from);
		/*If the path if 'from here to here' returning the current state of the search as
		 * its final result*/
		if(from.equals(to)) {
			return currentResult;
		}
		/*Getting all the nodes connected to the current one in the graph*/
		List<TNode> connectedNodes = from.getConnectedNodes();
		/*If there are none connected, the part of the graph is isolated and no further search
		 * is possible*/
		if(connectedNodes.isEmpty()) {
			throw new NoSuchElementException("The Dijkstra search has reached an impasse - isolated graph!");
		}
		/*If the destination is direclty connected to the currentNode, passing there and
		 * finalizing the search*/
		if(connectedNodes.contains(to)) {
			currentResult.addToPath(to);
			currentResult.incrementScore(getWeight(from,to));
			return currentResult;
		}
		/*Getting the iterator to browse the connectedNodes*/
		Iterator<TNode> nodeIterator =  connectedNodes.iterator();
		/*The nth node during the iteration*/
		TNode iNode;
		/*Helper variables for finding the minimal tag and its node*/
		double minimalTag = Double.POSITIVE_INFINITY;
		TNode minimalTagNode = null;
		double minimalTagWeight = Double.POSITIVE_INFINITY;
		/*Helper variables while browsing*/
		double currentTag;
		double currentWeight;
		
		while(nodeIterator.hasNext()) {
			
			iNode = nodeIterator.next();
			/*If any connected node has not yet been discovered, adding it to the discoery
			 * list and setting its tag to infinity */
			if(investigatedNodes.contains(iNode)) {
				int index = investigatedNodes.indexOf(iNode);
				iNode.setTag(investigatedNodes.get(index).getTag());
			}
			else {
				iNode.setTag(Double.POSITIVE_INFINITY);
				investigatedNodes.add(iNode);
			}
			/*Updating the tags for those not marked as visited already*/
			if(!currentResult.pathContains(iNode)) {
				
				/*Mathematical tag update*/
				currentWeight = getWeight(from,to);
				currentTag = Math.min(iNode.getTag(), (from.getTag() + currentWeight));
				iNode.setTag(currentTag);
				/*finding the minimal tag value and its node*/
				if(currentTag<=minimalTag && !currentResult.pathContains(iNode)) {
					minimalTag = currentTag;
					minimalTagNode = iNode;
					minimalTagWeight = currentWeight;
				}
			}	
		}
		/*If there were no adjacent nodes not yet visited , the algorithm is in deadlock and must
		 * be interrupted*/
		if(minimalTagNode == null) {
			throw new NoSuchElementException("The Dijkstra search has reached an impasse - no connected, not visited tags!");
		}
		/*Incrementing the weight for the chosen passage*/
		currentResult.incrementScore(minimalTagWeight);
		/*Iteratively repeating until the ultimate destination is reached*/
		return findOptimalPath(minimalTagNode, to);
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @return the weight of travelling from <code>from</code> to <code>to</code> or
	 * <code>Double.POSITIVE_INFINITY</code> if both are equal (to prevent deadlocks
	 * in the algorithm).
	 * @throws NullPointerException
	 */
	public abstract double getWeight(TNode from, TNode to);
	
}
