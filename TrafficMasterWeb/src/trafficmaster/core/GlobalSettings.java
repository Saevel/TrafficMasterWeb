package trafficmaster.core;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * Represents the most basic and common settings for the whole application.
 * @author Zielony
 * @version 1.0
 */
@Entity
@JsonSerializable
@Singleton
public class GlobalSettings implements Serializable {
	/**
	 * The ID of the bean used to store it in a relational database.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	/**
	 * Average human velocity while walking
	 */
	private double averageWalkVelocity;
	/**
	 * A threshold distance below which positions are non-discernable
	 */
	private double positionDistinctionDistance;
	/**
	 * A function used to calculate score based on the distance.
	 */
	@Enumerated(EnumType.ORDINAL)
	private FunctionType timeScoreDecayFunction;
	/**
	 * A function used to calculate score based on the number of changes.
	 */
	@Enumerated(EnumType.ORDINAL)
	private FunctionType changeScoreDecayFunction;
	/**
	 * A function used to calculate score based on the walking distance.
	 */
	@Enumerated(EnumType.ORDINAL)
	private FunctionType walkScoreDecayFunction;
	
	private double delayPenaltyPerMinute;
	
	/**
	 * The name of the city which is being served by the app.
	 */
	private String cityName;
	/**
	 * Gets: the average walking velocity of a human.
	 * @return the average walking velocity of a human.
	 */
	public double getAverageWalkVelocity() {
		return averageWalkVelocity;
	}
	/**
	 * Sets: the average walking velocity of a human.
	 * @param averageWalkVelocity the average walking velocity of a human.
	 */
	public void setAverageWalkVelocity(double averageWalkVelocity) {
		this.averageWalkVelocity = averageWalkVelocity;
	}
	/**
	 * Gets: the distance below which physical positions are considered indiscernible.
	 * @return the distance below which physical positions are considered indiscernible.
	 */
	public double getPositionDistinctionDistance() {
		return positionDistinctionDistance;
	}
	/**
	 * Sets: the distance below which physical positions are considered indiscernible.
	 * @param positionDistinctionDistance the distance below which physical positions are considered indiscernible.
	 */
	public void setPositionDistinctionDistance(double positionDistinctionDistance) {
		this.positionDistinctionDistance = positionDistinctionDistance;
	}
	/**
	 * Gets: the function used to assign points to a path on the basis of travelling time.
	 * @return the function used to assign points to a path on the basis of travelling time.
	 */
	public FunctionType getTimeScoreDecayFunction() {
		return timeScoreDecayFunction;
	}
	/**
	 * Sets : the function used to assign points to a path on the basis of travelling time.
	 * @param timeScoreDecayFunction the function used to assign points to a path on the basis of travelling time.
	 */
	public void setTimeScoreDecayFunction(FunctionType timeScoreDecayFunction) {
		this.timeScoreDecayFunction = timeScoreDecayFunction;
	}
	/**
	 * Gets: the function used to assign points to a path on the basis of the number of changes on the way.
	 * @return the function used to assign points to a path on the basis of the number of changes on the way.
	 */
	public FunctionType getChangeScoreDecayFunction() {
		return changeScoreDecayFunction;
	}
	/**
	 * Sets: the function used to assign points to a path on the basis of the number of changes on the way.
	 * @param changeScoreDecayFunction the function used to assign points to a path on the basis of the number of changes on the way.
	 */
	public void setChangeScoreDecayFunction(FunctionType changeScoreDecayFunction) {
		this.changeScoreDecayFunction = changeScoreDecayFunction;
	}
	/**
	 * Gets: the function used to assign points to a path on the basis of the walking distance.
	 * @return the function used to assign points to a path on the basis of the walking distance.
	 */
	public FunctionType getWalkScoreDecayFunction() {
		return walkScoreDecayFunction;
	}
	/**
	 * Sets : the function used to assign points to a path on the basis of the walking distance.
	 * @param walkScoreDecayFunction the function used to assign points to a path on the basis of the walking distance.
	 */
	public void setWalkScoreDecayFunction(FunctionType walkScoreDecayFunction) {
		this.walkScoreDecayFunction = walkScoreDecayFunction;
	}
	public double getDelayPenaltyPerMinute() {
		return delayPenaltyPerMinute;
	}
	public void setDelayPenaltyPerMinute(double delayPenaltyPerMinute) {
		this.delayPenaltyPerMinute = delayPenaltyPerMinute;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}	
}