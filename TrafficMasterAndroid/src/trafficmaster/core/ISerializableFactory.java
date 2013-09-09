package trafficmaster.core;

import java.io.NotSerializableException;
import java.io.Serializable;
/**
 * Defines an interface for a "factory" that serializes POJOs to
 * <code>String<code>s following a special internal format (i.e.
 * stringifed XML or JSON) and deserializes them conversely. It is
 * not forced, but strongly avised that such a "factory" is implemented
 * following the Singleton structural pattern.
 * @author Zielony
 * @version 1.0
 */
public interface ISerializableFactory extends Serializable {
	/**
	 * Given a proper (serializable in the sense of a particular implementation)
	 * input, serializes in from the Java object-oriented form to a <code>String
	 * </code> with an inner structure specific to the particular implementation.
	 * @param input the Java object to be serialized
	 * @return The <code>String</code> representing the serialized object
	 * @throws NotSerializableException if the <code>input</code> parameter is 
	 * null or not serializable in the sense of the particular implementation
	 */
	public String serialize(Object input) throws NotSerializableException;
	/**
	 * Given a <code>String</code> that must, with regards to its content strcture,
	 * match the requirements of the particular implementation, deserializes it to a
	 * form of a Java object that it represents. 
	 * @param input the <code>String</code> representing the serialized object
	 * @return the <code>Object</code> being the result of deserialization
	 * @throws IllegalArgumentException if the <code>input</code> parameter is 
	 * <code>null</code> or does not match the requirements of the particular 
	 * implementation with regards to its content structure.
	 */
	public Object deserialize(String input) throws IllegalArgumentException; 
}
