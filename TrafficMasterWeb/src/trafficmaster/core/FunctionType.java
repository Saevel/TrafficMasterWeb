package trafficmaster.core;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * Defines a type of function that may be used as the decay function in measurming the score
 * of a route on the basis of certain factors (walking distance, travel time etc.)
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 */
@JsonSerializable
public enum FunctionType implements Serializable {
	/** y = A*x + B*/
	LINEAR("LINEAR"),
	/**y = A*x^2 + B*x + C*/
	QUADRATIC("LINEAR"),
	/**y = A*(e^(C*x)) + D*/
	EXPONENTIAL("EXPONENTIAL"),
	/** y = A/(x+B) + C*/
	INVERSE_PROPOTIONAL("INVERSE PROPORTIONAL");
	@Id
	private String type;
	/**
	 * A constructor.
	 * @param type the type of this function as a <code>String</code>.
	 */
	FunctionType(String type) {
		this.setType(type);
	}
	/**
	 * Gets: the type of this function as a <code>String</code>.
	 * @return the type of this function as a <code>String</code>.
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets: the type of this function as a <code>String</code>.
	 * @param type the type of this function as a <code>String</code>.
	 */
	public void setType(String type) {
		this.type = type;
	}
}