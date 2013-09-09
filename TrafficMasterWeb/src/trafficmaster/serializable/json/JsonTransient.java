package trafficmaster.serializable.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Signalizes that the specific property or class is not to be
 * serialized or deserialized by the <code>JsonSerializable</code>
 * factory object.
 * @author Zielony
 * @version 1.0
 */
@Target({ElementType.FIELD,ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTransient {

}
