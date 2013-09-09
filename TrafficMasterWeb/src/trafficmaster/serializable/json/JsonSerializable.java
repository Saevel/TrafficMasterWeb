package trafficmaster.serializable.json;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
/**
 * An annotation signalizing that a particular field can and should
 * be serialized by the <code>JSONFactory</code> when passed as an
 * argument to the <code>serialize</code> method and deserialized when
 * a matching <code>String</code> is passed to a <code>deserialize</code>
 * method. By definition , the engine assumes all the object's fields 
 * as serializable, unless annotated with JsonTransient, but only those
 * providing a schematically named getter will be serialized, and those
 * providing a schematiccally named setter will be deserialized. Static
 * fields are ignored by default. Cyclic object structures are not supported
 * and will cause <code>NotSerializableException</code>.
 * @author Zielony
 * @version 1.0
 * @see ISerializableFactory
 */
@Target({ElementType.FIELD,ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerializable {

}
