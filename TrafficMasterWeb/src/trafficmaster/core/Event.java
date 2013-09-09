package trafficmaster.core;
import java.io.Serializable;


import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;

import trafficmaster.serializable.json.JsonSerializable;

/**
 * Represents an event that may occurr in the city traffic.
 * 
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 * @see EventType
 * @see EventGravity
 */
@Entity
@JsonSerializable
public class Event implements Serializable  {
	/**
	 * The unique object identifier within the class
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	/**
	 * The name of the event.
	 */
	private String name;
	/**
	 * A flag that shows if the event is currently active
	 */
	private boolean active;
	/**
	 * A description of the event.
	 */
	
	private String description;
	/**
	 * The type of the event.
	 */
	@Enumerated(EnumType.ORDINAL)
	private EventType type = EventType.OTHER;
	/**
	 * The gravity of the event.
	 */
	@Enumerated(EnumType.ORDINAL)
	private EventGravity gravity = EventGravity.MEDIUM;
	/**
	 * The locations affected by the event.
	 */
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},fetch=FetchType.EAGER)
	private Collection<Position> affected;
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurenceDate;
	/**
	 * Gets: the event name.
	 * @return the event name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets: the event name.
	 * @param name the event name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets : event short description.
	 * @return event short description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * sets: event short description.
	 * @param shortDescription event short description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Gets: the event type.
	 * @return the event type.
	 */
	public EventType getType() {
		return type;
	}
	/**
	 * Sets: the event type.
	 * @param type the event type.
	 */
	public void setType(EventType type) {
		this.type = type;
	}
	/**
	 * Gets: the event gravity.
	 * @return the event gravity.
	 */
	public EventGravity getGravity() {
		return gravity;
	}
	/**
	 * Sets: the event gravity.
	 * @param gravity the event gravity.
	 */
	public void setGravity(EventGravity gravity) {
		this.gravity = gravity;
	}
	/**
	 * Gets: the affected locations.
	 * @return the affected locations.
	 */
	public Collection<Position> getAffected() {
		return affected;
	}
	/**
	 * Sets: the affected locations.
	 * @param affected the affected locations.
	 */
	public void setAffected(Collection<Position> affected) {
		this.affected = affected;
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
	
	public Date getOccurenceDate() {
		return occurenceDate;
	}
	
	public void setOcurrenceDate(Date occurrenceDate) {
		this.occurenceDate = occurrenceDate;
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}