package trafficmaster.core;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import trafficmaster.serializable.json.JsonSerializable;
import javax.persistence.ManyToOne;/**
 * Represents a single course on a given traffic line.
 * @author Zielony
 * @version 1.0
 * @see Line
 * @see State
 * @see JsonSerializable
 * @see JsonFactory
 */
@Entity
@JsonSerializable
public class Course implements Serializable  {
	/**
	 * The unique object identifier within the class
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	/**
	 * The <code>Line</code> this <code>Course</code> is tied to
	 * */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch=FetchType.EAGER)
	private Line line;	
	/**
	 * The state this course is currently in
	 */
	@OneToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	private State state;
	/**
	 * Whether the course is already in progress (<code>true</code>) or not (<code>false</code>)
	 */
	private boolean active;
	
	/** Gets: the <code>Line</code> this <code>Course</code> is tied to.
	 * @return the <code>Line</code> this <code>Course</code> is tied to. 
	 */
	public Line getLine() {
		return line;
	}
	/**
	 * Sets: the <code>Line</code> this <code>Course</code> is tied to.
	 * @param line the <code>Line</code> this <code>Course</code> is tied to.
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	/**
	 * Gets: the state the course is currently in.
	 * @return the state the course is currently in.
	 */
	public State getState() {
		return state;
	}
	/**
	 * Sets: the state the course is currently in.
	 * @param state the state the course is currently in.
	 */
	public void setState(State state) {
		this.state = state;
	}
	/**
	 * Gets: Whether the course is currently active.
	 * @return <code>true</code> if the course is active and <code>false</code> if conversely.
	 */
	public boolean getActive() {
		return active;
	}
	/**
	 * Sets: whether the course is currently active.
	 * @param active <code>true</code> if the course is active and <code>false</code> if conversely.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * Gets: this entity's ID
	 * @return this entity's ID 
	 */
	public int getID() {
		return ID;
	}
	/**
	 * Sets : this entity's ID
	 * @param ID this entity's ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
}