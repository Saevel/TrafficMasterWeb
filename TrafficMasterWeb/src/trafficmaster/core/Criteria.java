package trafficmaster.core;
import java.io.Serializable;
import java.util.Date;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * An <code>enum</code> representing different types of criteria used to rank routes while
 * choosing the optimal one and their specific settings.
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 */
@JsonSerializable
public class Criteria implements Serializable {
	
	private int maximalWalkingDistance;
	
	private int maximalChangeCount;
	
	private double walkScoreCoefficient;
	
	private double changePenalty;
	
	private Date maximalTravelTime;
	
	private double minuteScoreCoefficient;

	public int getMaximalWalkingDistance() {
		return maximalWalkingDistance;
	}

	public void setMaximalWalkingDistance(int maximalWalkingDistance) {
		this.maximalWalkingDistance = maximalWalkingDistance;
	}

	public int getMaximalChangeCount() {
		return maximalChangeCount;
	}

	public void setMaximalChangeCount(int maximalChangeCount) {
		this.maximalChangeCount = maximalChangeCount;
	}

	public double getWalkScoreCoefficient() {
		return walkScoreCoefficient;
	}

	public void setWalkScoreCoefficient(double walkScoreCoefficient) {
		this.walkScoreCoefficient = walkScoreCoefficient;
	}

	public Date getMaximalTravelTime() {
		return maximalTravelTime;
	}

	public void setMaximalTravelTime(Date maximalTravelTime) {
		this.maximalTravelTime = maximalTravelTime;
	}

	public double getMinuteScoreCoefficient() {
		return minuteScoreCoefficient;
	}

	public void setMinuteScoreCoefficient(double minuteScoreCoefficient) {
		this.minuteScoreCoefficient = minuteScoreCoefficient;
	}

	public double getChangePenalty() {
		return changePenalty;
	}

	public void setChangePenalty(double changePenalty) {
		this.changePenalty = changePenalty;
	}
	
}
