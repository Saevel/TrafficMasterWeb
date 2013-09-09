package trafficmaster.core;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;


import trafficmaster.serializable.json.JsonSerializable;
import trafficmaster.serializable.json.JsonTransient;


/**
 * Represents a stop of an arbitrary means of communication with its physical location.
 * 
 * @author Zielony
 * @version 1.0
 * @see Position
 * @see JsonSerializable
 */
@Entity
@JsonSerializable
@DiscriminatorValue("STOP")
public class Stop extends Position implements Serializable {
	/**
	 * The stop's name.
	 */
	private String stopName;
	@JsonTransient
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},fetch=FetchType.EAGER)
	private Set<Arrival> arrivals;
	/**
	 * A default constructor.
	 */
	public Stop() {
		;
	}
	/**
	 * Gets: the stop's name
	 * @return the stop's name 
	 */
	public String getStopName() {
		return stopName;
	}
	/**
	 * Sets: the stop's name
	 * @param stopName the stop's name
	 */
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	/**
	 * Gets: all the <code>arrivals<code> to which this object is assigned.
	 * @return all the <code>arrivals<code> to which this object is assigned.
	 */
	public Set<Arrival> getArrivals() {
		return this.arrivals;
	}
	/**
	 * Sets: all the <code>arrivals<code> to which this object is assigned.
	 * @param arrivals all the <code>arrivals<code> to which this object is assigned.
	 */
	public void setArrivals(Set<Arrival> arrivals) {
		this.arrivals = arrivals;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Stop) {
			Stop s = (Stop)o;
			if(this.ID == s.ID) {
				return true;
			}
		}
			return false;
	}
	
}