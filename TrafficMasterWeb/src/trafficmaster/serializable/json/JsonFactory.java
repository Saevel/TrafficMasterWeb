package trafficmaster.serializable.json;

import java.io.NotSerializableException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import trafficmaster.serializable.json.JsonSerializable;
import trafficmaster.serializable.json.JsonTransient;

import trafficmaster.serializable.ISerializableFactory;

/**
 * An <code>ISerializableFactory</code> that serializes/deserializes POJOs to/from the
 * JSON format via <code>JSONObject</code> and <code>JSONArray</code> objects from the 
 * <code>org.json</code> package. It requires an object to have no cyclic references, 
 * not to be a nameless <code>String</code> to be serialized. A field, in order to be 
 * serialized, needs to provide a name and a <code>public</code> getter (and setter to
 * be deserialized) following the standard Java conventions. Fields that do not fullfill
 * the latter condition, will simply be omitted. Also, there is a set of reserved names, 
 * represented by <code>reservedNames Collection</code>, that are forbidden as field names.
 * Finally, the <code>JsonTransient</code> annotation shows the class that these fields are
 * not to be serialized/deserialized. <code>Collections, Arrays</code> and <code> Maps
 * </code> are supported and fully recreated both ways. Mixing them between themselves or with
 * plain object functionatilites may result in partial serialization or unpredictable behavior.
 * @author Zielony
 * @version 1.0
 * @see JsonSerializable
 * @see JsonTransient
 * @see ISerializableFactory
 */
@Stateless
@JsonTransient
public class JsonFactory implements ISerializableFactory {
	
	/**An identifier under which the object class is stored*/
	protected static final String CLASS_PARAMETER_NAME = "class";
	/**An identifier under which array content is stored*/
	protected static final String ARRAY_CONTENT_NAME = "ArrayContent";
	/**An identifier under which collection content is stored*/
	protected static final String COLLECTION_CONTENT = "CollectionContent";
	/**An identifier under which map key set is stored*/
	protected static final String MAP_KEY_SET = "MapKeySet";
	/**An identifier under which map value set is stored*/
	protected static final String MAP_VALUE_SET = "MapValueSet";
	/**An identifier under which primitive value is stored*/
	protected static final String PRIMITIVE_VALUE = "PrimitiveValue";
	/**A <code>List</code> of reserved names - for internal class usage and inheritance only*/
	public static final List<String> reservedNames = new LinkedList<String>();
	
	static { /*Adding the class constants to the reserved names*/
		reservedNames.add(ARRAY_CONTENT_NAME);
		reservedNames.add(CLASS_PARAMETER_NAME);
		reservedNames.add(COLLECTION_CONTENT);
		reservedNames.add(MAP_KEY_SET);
		reservedNames.add(MAP_VALUE_SET);
		reservedNames.add(PRIMITIVE_VALUE);
	}
	
	public static final List<Class> primitiveWrappers = new LinkedList<Class>();
	
	static {
		primitiveWrappers.add(String.class);
		primitiveWrappers.add(Integer.class);
		primitiveWrappers.add(Short.class);
		primitiveWrappers.add(Byte.class);
		primitiveWrappers.add(Character.class);
		primitiveWrappers.add(Long.class);
		primitiveWrappers.add(Float.class);
		primitiveWrappers.add(Double.class);
	}
	
	/**A sole instance of <code>JsonFactory</code> available*/
	private static final JsonFactory INSTANCE = new JsonFactory();
	
	@Override
	public String serialize(Object input) throws NotSerializableException {
		/*If the input is null , returning it directly in a String form*/
		if(input == null) {
			return (String)null;
		}
		/*Getting the input object class*/
		Class objectClass = input.getClass();
		JSONObject json;
		/*If the input is a primitive, returning it in a String form*/
		if(objectClass.isPrimitive() || primitiveWrappers.contains(objectClass)) {
			json = new JSONObject();
			try {
				json.put(CLASS_PARAMETER_NAME, objectClass.getClass());
				json.put(PRIMITIVE_VALUE, input.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				throw new NotSerializableException("Cannot serialize primitive!");
			}
			
			return input.toString();
		}
		/*If the class is JsonSerializable or primitive or String, it is recursively serialized using JSON syntax*/
		if(objectClass.isAnnotationPresent(JsonSerializable.class) || input instanceof Collection || input instanceof Map || objectClass.isArray()) {
			json = serialize(input, objectClass);
		}
		else { /*If not, its not being serializable is marked*/
			throw new NotSerializableException("The object is not annotated as JsonSerializable!");
		}
		/*Returning the stringified JSON-structured object*/
		return json.toString();
	}

	@Override
	public Object deserialize(String input) throws IllegalArgumentException {
		/*The JSONObject from which the field/ object will be deserialized*/
		JSONObject json;
		try {/*Creating from String*/	
			 json = new JSONObject(input);
		} catch (JSONException e) {
			throw new IllegalArgumentException("The String provided as input parameter is not a valid JSON String!");
		}
		/*The class of the desired output*/
		Class outputClass;
		try {/*Getting the class name declared in the JSON and instantiating it*/
			String className = json.getString(CLASS_PARAMETER_NAME);
			outputClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find a class listed as the JSONized object class");
		} catch (JSONException e) {
			throw new IllegalArgumentException("Cannot retrieve the JSONized object class from the JSONObject");
		}
		/*The final output object*/
		Object output;
		/*Ignoring interfaces, abstracts and annotations and they cannot be used to deserialize*/
		if(outputClass.isInterface() || outputClass.isAnnotation() || Modifier.isAbstract(outputClass.getModifiers())) {
			throw new IllegalArgumentException("The provided type is not a valid, instantiable class:" + outputClass.getName());
		}
		
		try {/*Instatiating the desired output class*/
			output = outputClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("The provided type does not provide a no-argument, public constructor : " + outputClass.getName());
		}
		/*Deserialization of pure primitives*/
		if(outputClass.isPrimitive() || primitiveWrappers.contains(outputClass)) {
			output = this.deserializePrimitive(outputClass, input);
		} else { /*Triggering further deserialization*/
			deserialize(json, output, outputClass);
		}
		/*Returning the result*/
		return output;
	}
	
	/**
	 * An internal method serializing objects/primitives basing on their internal structure, inherintance
	 * scheme and possible belonging to special classes such as <code>Map</code>, <code>Collection</code>
	 * or arrays - to the <code>JSONObject</code> form.
	 * @param input the object to be serialized
	 * @param inputClass the class of the object to be serialized 
	 * @return the <code>JSONObject</code> contanining the representation of <code>input</code>
	 * @throws NotSerializableException if the <code>input</code> cannot be serialized for any reason.
	 */
	protected JSONObject serialize(Object input, Class inputClass) throws NotSerializableException  {
		/*If the object/property is JsonTransient, it is not serialized as all, which is marked by a null*/
		if(inputClass.isAnnotationPresent(JsonTransient.class)) {
			return null;
		}
		/*A JSONObject used to represent the serialized Object*/
		JSONObject json = new JSONObject();
		/*If the object is in fact primitive, it is simply wrapped*/
		if(inputClass.isPrimitive() || inputClass.equals(String.class)) {	
			throw new UnsupportedOperationException("Strings and primitives cannot be converted to JSON without a name specified!");
		}
		else {/*Otherwise*/
			try {
				/*Until we reach Object in class hierarchy, the superclasses are hierarchically and
				 * recursively serialized. Interfaces are by nature omitted.*/
				Class superClass = inputClass.getSuperclass();
				if(!superClass.isInterface() && !superClass.equals(Object.class) ) {
					json = serialize(input, inputClass.getSuperclass());
				}
				/*Inserting the class name into the object*/
				json.put(CLASS_PARAMETER_NAME, inputClass.getName());
			} catch (JSONException e) {
				throw new NotSerializableException("Cannot put the class name into the JSONObject!");
			}
			/*If the input is an array*/
			if(inputClass.isArray()) {
				try {
					/*Checking the component type of the array*/
					Class componentType =  inputClass.getComponentType();
					/*Correcting the class name to the canonical name - > clearer*/
					json.put(CLASS_PARAMETER_NAME, inputClass.getName());
					/*If the array type is primitive simple org.json wrapping will do*/
					if(componentType.isPrimitive() || componentType.equals(String.class)) {
						json.put(ARRAY_CONTENT_NAME, JSONObject.wrap(input));
					} else { /*If not, recursive serialization is needed*/
						json.put(ARRAY_CONTENT_NAME, this.serializeCollection(Arrays.asList(input)));
					}
					
				} catch (JSONException e) {
					throw new NotSerializableException("Cannot process array!");
				}
			}
			else if(input instanceof Collection) {
				try {/*If the object is a Collection, its content is coverted to a JSONArray and
				put under a predesigned name*/
					json.put(COLLECTION_CONTENT, serializeCollection((Collection)input));
				} catch(JSONException e) {
					throw new NotSerializableException("Cannot insert collection contento into the JSONObject"); 
				}
			}
			else if(input instanceof Map) {
				try {/*If the object is a Map , both its keySet and valueSet are converted to
				JSONArrays like Collections and put under a predesigned names*/
					Map map = (Map)input;
					json.put(MAP_KEY_SET, map.keySet());
					json.put(MAP_VALUE_SET, map.values());
				} catch (JSONException e) {
					throw new NotSerializableException("Cannot insert map content into the JSONObject!");
				}
			}
			else {
				/*Represents the filed currently checked in the loop*/
				Field currentField = null;
				/*Getting all the fields in the object*/
				Field[] fields = inputClass.getDeclaredFields();
				if(fields == null || fields.length == 0) {
					return json;
				}
				/*Iterating over all fields*/
				for(int i=0;i<fields.length;i++) {
					/*Locally caching the field*/
					currentField = fields[i];
					/*If the field is static or JsonTransient, it is ignored by default*/
					if(Modifier.isStatic(currentField.getModifiers()) || currentField.isAnnotationPresent(JsonTransient.class)) {
						continue;
					}
					if(reservedNames.contains(currentField.getName())) {
						/*If a field goes by a reserved name, an exception is thrown*/
						throw new NotSerializableException("The field :" + currentField.getName() + " is named with a reserved name and therefore cannot be serialized!");
					}
					/*Getting the field value*/
					Object fieldValue = get(currentField,input);
					
					try {/*Serializing the field value and putting it in the JSONObject under its field name*/
						if(fieldValue == null ){
							json.put(currentField.getName(), (String)null);
						}/*If the value is primitive or String, simply inserting it into the JSON*/
						else if(fieldValue.getClass().isPrimitive() || primitiveWrappers.contains(fieldValue.getClass())) {
							json.put(currentField.getName(), fieldValue);
						}
						else {/*If the value is an object, serializing recursively*/
							json.put(currentField.getName(),serialize(fieldValue, fieldValue.getClass()));
						}
					} catch (JSONException e) {;
						throw new NotSerializableException("Cannot insert a serialized field into a JSONArray");
					}			
				}
			}	
		}
		/*Returning the JSONObject representation of the object*/
		return json;
	}
	/**
	 * Deserializes the entity from the <code>JSONObject</code> form to an appropriate java class.
	 * @param json the <code>JSONObject</code> form to be deserialized.
	 * @param output the resulting Java <code>Object</code>.
	 * @param outputClass the <code>Class</code> in terms of which we are considering the <code>output Object</code>.
	 * @return the Java object that was represented by <code>json</code>
	 * @throws IllegalArgumentException if deserialization failed for any reason.
	 */
	protected Object deserialize(JSONObject json, Object output, Class outputClass) throws IllegalArgumentException {
		/*Checking the superclass of the outputClass*/
		Class superClass = outputClass.getSuperclass();
		/*If the superclass is neither abstract, nor interface , nor Object, recursively deserializing superclasses until Object is reached*/
		if(!superClass.isInterface() && !Modifier.isAbstract(superClass.getModifiers()) && !superClass.equals(Object.class)) {
			deserialize(json,output,superClass);
		}
		/*If the outputClass represents an array*/
		if(outputClass.isArray()) {
			JSONArray jsonArray;
			try {/*Retriving the JSONArray contaning its content*/
				jsonArray = json.getJSONArray(ARRAY_CONTENT_NAME);
			} catch (JSONException e) {
				throw new IllegalArgumentException("Cannot retrieve the JSONArray stocking the array content from the JSONObject!");
			}/*Delegating to a special array deserialization function*/
			output = this.deserializeArray(jsonArray, outputClass.getComponentType());
		}
		else {
			/*Verifying if the outputClass represents a Collection */
			if(output instanceof Collection) {
				/*Retriving the array stocking its content*/
				JSONArray jsonArray;
				try {
					jsonArray = json.getJSONArray(COLLECTION_CONTENT);
					try {/*Delegating to special method for deserialization of Collections*/
						output = this.deserializeCollection(jsonArray, (Collection) outputClass.newInstance());
					} catch (Exception e) {
						throw new IllegalArgumentException("The Collection class type does not provide a public no-argument constructor!");
					}
				} catch (JSONException e) {
					throw new IllegalArgumentException("Cannot retireve the JSONArray representing the Collection content from the JSONObject!");
				}
			}/*Checking if output is a Map*/
			else if(output instanceof Map) {
				/*Retriving the JSONArrays containing key and value sets*/
				try {
					JSONArray keyJsonArray = json.getJSONArray(MAP_KEY_SET);
					JSONArray valueJsonArray = json.getJSONArray(MAP_VALUE_SET);
					try {/*Delegating a special function for Map deseriation*/
						output = this.deserializeMap(keyJsonArray, valueJsonArray, (Map) outputClass.newInstance());
					} catch (Exception e) {
						throw new IllegalArgumentException("The Map class type does not provide a public no-argument constructor!");
					}
				} catch (JSONException e) {
					throw new IllegalArgumentException("Cannot retireve the JSONArray representing the Collection content from the JSONObject");
				}
			}
			else {
				/*Getting all the objects fields*/
				Field[] fields = outputClass.getDeclaredFields();
				/*Currently checked field*/
				Field currentField;
				/*Current field IMPLEMENTATION class*/
				Class currentClass;
				/*JSONObject representing the current element*/
				JSONObject currentJson;
				/*The retrieved value for the current element*/
				Object currentValue = new Object();

				try {/*Iterating over all fields*/
					for(int i=0;i<fields.length;i++) {
						/*Local caching*/
						currentField = fields[i];
						/*currentClass starting as the field Type (WARNING: MAY BE AN INTERFACE OR ANYTHING - CANNOT BE PASSED FURTHER) */
						currentClass = currentField.getType();
						/*If current field is JsonTransient or static - skipping it*/
						if(currentField.isAnnotationPresent(JsonTransient.class) || Modifier.isStatic(currentField.getModifiers())) {
							continue;
						}
						/*If the field is primitive or String - assigning directly as the value*/
						if(currentClass.isPrimitive() || primitiveWrappers.contains(currentClass)) {
							/*Herein the value in the JSON should direclty be the value*/
							currentValue = json.get(currentField.getName());
						}
						else { /*If the field is an object  - complex and recursive deserialization*/
							currentValue = new Object();
							/*Retriving the JSONObject representing it*/
							currentJson = json.getJSONObject(currentField.getName());
							/*Switching currentClass, to the class recorded in the JSON instead of the
							 *  field one - more accurate and no risk of having an interface etc.*/
							currentClass = Class.forName(currentJson.getString(CLASS_PARAMETER_NAME));
							try {/*If the field is an array, using special array deserialization*/
								if(currentClass.isArray()) {
									currentValue = this.deserializeArray(currentJson.getJSONArray(ARRAY_CONTENT_NAME), currentClass.getComponentType());
								}
								else {/*Creating a new instance of the appropriate class*/
									currentValue = currentClass.newInstance();
								}
							} catch (Exception e) {
								throw new IllegalArgumentException("Cannot instantiate object member field class!");
							}
							/*Deserializing from currentJson, to currentValue treated as the class 
							 * whose name was retrieved from Json*/
							currentValue = this.deserialize(currentJson, currentValue, currentClass);
						}
						/*Object, String or Primitive - setting the value if possbile*/
						set(currentField, currentValue, output);
					}
				} catch(JSONException e) {
					/*Ignoring the lack of fields for the sake of graceful degradation*/
					;//throw new IllegalArgumentException("Some of the member fields of the object could not be read from the JSONObject");
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("Class of the retrieved object field is unkown!" );
				}
			}
		}
		/*Returning the output*/
		return output;
	}
	
	/**
	 * Serializes the array entries by serializing them to <code></code>
	 * @param input the array whose content needs serialization.
	 * @return The <code>JSONArray</code> containing the serialized contents of the array.
	 * @throws NotSerializableException if there were any exceptions during array content serialization.
	 */
	protected JSONArray serializeArray(Object[] input) throws NotSerializableException {
		/*Initializing the JSONArray*/
		JSONArray json = new JSONArray();
		/*Iterating over all the array elements*/
		for(int i=0;i<input.length;i++) {
			/*Serializing every array element with respect to its class and inserting into
			 * the JSONArray*/
			if(input[i].getClass().equals(String.class) || input[i].getClass().isPrimitive()) {
				json.put(input[i]);
			}
			else {
				json.put(serialize(input[i], input[i].getClass()));
			}
		}
		/*Returing the filled JSONArray*/
		return json;
	}
	/**
	 * Deserializes a typed array from a <code>JSONArray</code>.
	 * @param jsonArray the <code>JSONArray</code> where the array contents are stocked.
	 * @param output the <code>Array</code> that is the outcome of deserialization
	 * @return output of deserialization
	 * @throws IllegalArgumentException if there are errors during deserialization.
	 */
	protected Object deserializeArray(JSONArray jsonArray, Class componentType) throws IllegalArgumentException {
		
		Object result = Array.newInstance(componentType, jsonArray.length());
		try {
			for(int i=0;i<jsonArray.length();i++) {
				Array.set(result, i, jsonArray.get(i));
			}
		} catch(JSONException e) {
			throw new IllegalArgumentException("Cannot retireve an array element from a JSONArray");
		}
		
		return result;
	}
	
	/**
	 * Serializes the <code>Collection</code> content by serializing all its elements into
	 * <code>JSONObject</code>s and inserting them in a <code>JSONArray</code>.
	 * @param input the <code>Collection</code> whose content needs to be serialized.
	 * @return the <code>JSONArray</code> contaning serialized <code>input</code> content.
	 * @throws NotSerializableException if for any reason the serialization was unsuccessful.
	 */
	protected JSONArray serializeCollection(Collection input) throws NotSerializableException {
		/*Initializing the JSONArray*/
		JSONArray json = new JSONArray();
		/*If the input is not empty - processing, else returning an empty JSONObject*/
		if(!input.isEmpty()) {
			/*Obtaning the iterator*/
			Iterator<?> iterator = input.iterator();
			/*Next object generally*/
			Object next = null;
			/*Entry count*/
			int i=0; 
			/*Iterating over all the content*/
			while(iterator.hasNext()) {
				
				next = iterator.next();
				i++;
				/*If the element is a string, putting it directly inside*/
				if(next.getClass().equals(String.class) || next.getClass().isPrimitive()) {
					json.put(next);
				}
				else {
					/*Inserting the recursively serialized entries into the JSONArray*/
					json.put(serialize(next, next.getClass()));
				}
			}
		}
		/*Returning the JSONArray*/
		return json;
	}
	/**
	 * Deserializes a <code>Collection</code> from a <code>JSONArray</code> that stocks its 
	 * content.
	 * @param jsonArray the <code>JSONArray</code> stocking the <code>output</code>'s content.
	 * @param output the result <code>Collection</code>.
	 * @return output - the result <code>Collection</code>.
	 */
	protected Collection deserializeCollection(JSONArray jsonArray, Collection output) {
		try {
			/*The JSONObject for the possibly objective array element*/
			JSONObject retrievedJson;
			/*The array element's class*/
			Class retrievedClass;
			/*The array element itself*/
			Object retrievedObject;
			/*Iterating over all the entries in the JSONArray*/
			for(int i=0;i<jsonArray.length();i++) {
				try {
					/*Trying to get an object and its class*/
					retrievedJson = jsonArray.getJSONObject(i);
					retrievedClass = Class.forName(retrievedJson.getString(CLASS_PARAMETER_NAME));
					/*If primitive or String, passing to direct input*/
					if(retrievedClass.isPrimitive() || retrievedClass.equals(String.class)) {
						throw new JSONException("Triggered to pass to direct input");
					}
					/*Creating a new object of the retrievedClass*/
					retrievedObject = retrievedClass.newInstance();
					/*If a typical Object, recursively deserializing and adding to the collection*/
					output.add(deserialize(retrievedJson,retrievedObject, retrievedClass));
				}
				catch(JSONException e) {/*If a primitive or String, inputting direcly to the collection*/
					output.add(jsonArray.get(i));
				}
			}
		/*Serving various exceptions*/	
		} catch(JSONException e) {
			throw new IllegalArgumentException("The array data cannot be retrieved from the JSONArray!");
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("The array element type class is not known !");
		} catch (Exception e) {
			throw new IllegalArgumentException("The array element type does not provide a no-argument public constructor!");
		}
		
		return output;
	}
	/**
	 * Deserializes a <code>Map</code> using two <code>JSONArrays</code> stocking its keySet and
	 * valueSet respectively.
 	 * @param keyJsonArray the <code>JSONArray</code> stocking the serialized key set.
	 * @param valueJsonArray the <code>JSONArray</code> stocking the serialized value set.
	 * @param output the <code>Map</code> to which the input is saved.
	 * @return output - the <code>Map</code> with data inserted.
	 */
	protected Map deserializeMap(JSONArray keyJsonArray, JSONArray valueJsonArray, Map output) {
		// FIXME : keySet type and valueSet type ?!
		/*If the lengths of the keySet and valueSet are no consistent - throwing an exception*/
		if(keyJsonArray.length() != valueJsonArray.length()) {
			throw new IllegalArgumentException("keySet and valueSet in a Map are of different lengths!");
		}
		/*Classes of the key and value elements*/
		Class keyClass;
		Class valueClass;
		/*value or JSON (depends, aggregated) of the key and value*/
		Object keyJson;
		Object valueJson;
		try {/*Iterating over all keys and values*/
			for(int i=0;i<keyJsonArray.length();i++) {
				/*Gettings what's on that position in the JSONArray - either a JSONObject for a
				 * complex object or a primitive // String for a simple piece of data.*/
				keyJson = keyJsonArray.get(i);
				valueJson = valueJsonArray.get(i);
				if(keyJson instanceof JSONObject) { /*If the key is complex, getting its real class*/
					keyClass = Class.forName(((JSONObject) keyJson).getString(CLASS_PARAMETER_NAME));
					/*Deserializing the key to the keyJsonVariable*/
					keyJson = this.deserialize((JSONObject) keyJson, keyClass.newInstance(), keyClass);
				}/*If the key is simple, its primitive or String value was already uploaded to the keyJson
				variable via the .get function*/
				if(valueJson instanceof JSONObject) { /*If the value is complex, gettings its real class*/
					valueClass = Class.forName(((JSONObject)valueJson).getString(CLASS_PARAMETER_NAME));
					/*eserializing the value to the valueJson variable.*/
					valueJson = this.deserialize((JSONObject)valueJson, valueClass.newInstance(), valueClass);
				}/*If the value is primitive or String, its value was already uploaded to valueJson variable
				via the .get function*/
				/*Inserting the key-value pair into the Map*/
				output.put(keyJson, valueJson);
			}/*Serving a number of specific mistakes*/
		} catch(JSONException e) {
			throw new IllegalArgumentException("Could not retrieve the class name from the Map key//value JSONObject");
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Map Key/Value class unkown!");
		} catch (Exception e) {
			throw new IllegalArgumentException("The Map Key/Value class does not provide a public no-argument constructor!");
		}
		/*Returning the result*/
		return output;
	}
	
	/**
	 * Gets the <code>field</code> value from the <code>contaningObject</code> using a getter
	 * if it is present, named and types according to the scheme. If not, a <code>null</code> is returned.
	 * @param field the <code>Field</code> whose value is to retrieved.
	 * @param containingObject the <code>Object</code> from which the value is retrieved.
	 * @return the value of the <code>field</code> for the <code>contaningObject</code> or
	 * <code>null</code> if no schematically named getter is present of invokable.
	 */
	private static Object get(Field field, Object containingObject) {
		/*Obtaning the field's name*/
		String fieldName = field.getName();
		/*Bulding the getter name according to the scheme*/
		StringBuilder builder = new StringBuilder();
		builder.append("get");
		builder.append(Character.toUpperCase(fieldName.charAt(0)));
		builder.append(fieldName.substring(1));
		
		String getterName = builder.toString();

		Method getter;		
		try {
			/*Obtaining the getter if present and invoking it, returning the value, if possible*/
			getter = containingObject.getClass().getMethod(getterName);
			return getter.invoke(containingObject);
			
		} catch (Exception e) {
			return null;
			/*Getter access and invocation exceptions are ignored, so that inacessible 
			 * fields are not serialized without causing program workflow obstruction
			 */
		} 
	}
	/**
	 * Sets the <code>field value</code> for the <code>targetObject</code> using a standard 
	 * public setter, if such exists, otherwise does nothing.
	 * @param field the <code>Field</code> to be set.
	 * @param value the <code>Object</code> to be assigned to <code>field</code>.
	 * @param targetObject the <code>Object</code> over which the set is executed.
	 */
	protected static void set(Field field, Object value, Object targetObject) {
		/*Getting the field Name*/
		String fieldName = field.getName();
		/*Building the schematic setter name*/
		StringBuilder builder = new StringBuilder();
		builder.append("set");
		builder.append(Character.toUpperCase(fieldName.charAt(0)));
		builder.append(fieldName.substring(1));
		
		String setterName = builder.toString();
		
		Method setter;
		/*Getting the class of the target object*/
		Class targetClass = targetObject.getClass();
		
		try {
			/*Accessing the setter from the target class if possible - using the field type,
			 * as it is the input type in the setter declaration*/
			setter = targetClass.getMethod(setterName,field.getType());
			/*Invoking the setter using the value type  - polymorpism?*/
			setter.invoke(targetObject, value);
		} catch (Exception e) {
			e.printStackTrace();
			/*All the exceptions here are ignored so that a lack of properly named
			setter does not break the executions process. It will only result in
			fields not being set*/
		}
	}
	
	public Object deserializePrimitive(Class outputClass, String stringified) {
		
		if(outputClass.equals(Integer.class)) {
			return Integer.parseInt(stringified);
		} else if (outputClass.equals(Byte.class)) {
			return Byte.parseByte(stringified);
		} else if (outputClass.equals(Short.class)) {
			return Short.parseShort(stringified);
		} else if (outputClass.equals(Float.class)) {
			return Float.parseFloat(stringified);
		} else if (outputClass.equals(Double.class)) {
			return Double.parseDouble(stringified);
		} else if(outputClass.equals(Long.class)) {
			return Long.parseLong(stringified);
		} else if (outputClass.equals(Character.class) ||
				outputClass.equals(String.class)) {
			return stringified;
		} else {
			throw new IllegalArgumentException("Unkown primitive class!");
		}
	}
}