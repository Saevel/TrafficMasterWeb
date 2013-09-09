package trafficmaster.core;
import java.io.NotSerializableException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import trafficmaster.serializable.json.JsonFactory;
import trafficmaster.serializable.json.JsonSerializable;



public class MainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		Criteria criteria = new Criteria();
		
		
		
		String serialized = null;
		
		try {
			serialized = new JsonFactory().serialize(criteria);
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(serialized);
	}
}
