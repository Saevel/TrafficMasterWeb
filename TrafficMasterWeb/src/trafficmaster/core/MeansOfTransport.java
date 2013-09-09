package trafficmaster.core;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONObject;

import trafficmaster.serializable.json.JsonSerializable;


/**
 * An <code>enum</code> defining all the possible means of transport to be taken in the traffic.
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 */
@JsonSerializable
public enum MeansOfTransport implements Serializable {
	
	BUS("Bus"),
	TRAM("Tram"),
	TRAIN("Train"),
	BY_FOOT("By Foot"),
	SUBWAY("Subway");

	private String type;
	 
	MeansOfTransport(String type) {
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}