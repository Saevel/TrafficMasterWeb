package trafficmaster.core;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import trafficmaster.serializable.json.JsonSerializable;
import trafficmaster.serializable.json.JsonTransient;

import javax.persistence.Enumerated;

/**
 * Represents a communication line in the traffic system
 * @author Zielony
 * @version 1.0
 * @see MeansOfTransport
 * @see Position
 * @see JsonSerializable
 */
@Entity
@JsonSerializable
public class Line implements Serializable {
	/**
	 * The unique object identifier within the class
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	/**
	 * A name of the line.
	 */
	private String name;
	/**
	 * The direction of the line.
	 */
	private boolean direction;
	/**
	 * The means of transport used by the line.
	 */
	@Enumerated(EnumType.ORDINAL)
	private MeansOfTransport meansOfTransport;
	/**
	 * A list of all the positions traversed on the way
	 */
	@JsonTransient
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch=FetchType.EAGER)
	private List<Arrival> arrivals;
	/**
	 * Gets: the line name.
	 * @return the line name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets: the line name.
	 * @param name the line name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets: the line's means of transport.
	 * @return the line's means of transport.
	 */
	public MeansOfTransport getMeansOfTransport() {
		return meansOfTransport;
	}
	/**
	 * Sets: the line's means of transport.
	 * @param meansOfTransport the line's means of transport.
	 */
	public void setMeansOfTransport(MeansOfTransport meansOfTransport) {
		this.meansOfTransport = meansOfTransport;
	}
	/**
	 * Gets: the line's direction.
	 * @return the line's direction.
	 */
	public boolean getDirection() {
		return direction;
	}
	/**
	 * Sets: the line's direction.
	 * @param direction the line's direction.
	 */
	public void setDirection(boolean direction) {
		this.direction = direction;
	}
	/**
	 * Gets: all the locationss passed by the line.
	 * @return all the locationss passed by the line.
	 */
	public List<Arrival> getArrivals() {
		return arrivals;
	}
	/**
	 * Sets: all the locationss passed by the line.
	 * @param locationsPassed all the locationss passed by the line.
	 */
	public void setArrivals(List<Arrival> arrivals) {
		this.arrivals = arrivals;
	}
	/**
	 * Gets: this entity's ID.
	 * @return this entity's ID.
	 */
	public int getID() {
		return ID;
	}
	/**
	 * Sets: this entity's ID.
	 * @param ID this entity's ID.
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
}