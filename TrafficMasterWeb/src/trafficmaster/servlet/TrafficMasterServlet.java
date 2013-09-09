package trafficmaster.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.serializable.ISerializableFactory;
import trafficmaster.serializable.json.JsonFactory;

import com.ibytecode.clientutility.JNDILookupClass;

/**
 * A custom <code>Servlet</code> subtype dedicated to a specific set of tasks : 
 * <ol> 
 * 	<li>The communication is made using a strandard and constantly the same protocol
 *  which translates into plain text and </li>
 *  <li>The function of a Servlet can be broken down to serializing the input data
 *  (herein automatized), executing an action (defined during class inheritance) and
 *  sending the deserialized results to the client (automatized).</li>
 *  <li>Exception handling is defined on the user side</li>
 * </ol> 
 * @author Zielony
 * @version 1.0
 * @param <InputType> The type of the action input.
 * @param <OutputType> The type of the action output.
 */
public abstract class TrafficMasterServlet<InputType, OutputType> extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	/* TODO : Te elementy powinny byæ zdefiniowane w GlobalSettings lub negocjowane z
	 * klientem, a tutaj pobierane i stosowane, ¿eby zwiêkszyæ uniweralnoœæ i 
	 * przenoœnoœæ*/
	protected static final String MODULE_NAME = "TrafficMasterWeb"; 
	protected static final String PLAIN_TEXT_MIME = "text/plain";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
	public TrafficMasterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Currently the only supported HTTP method, gets the serialized input, deserializes
	 * it using a deserializer, executes the defined action and pushes the output back
	 * to the client.
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*The user-sent data in a strinfigied form*/
		String serializedData;
		/*MIME type setup - currently only plain text - and PrintWriter setup*/
		response.setContentType(PLAIN_TEXT_MIME);
		PrintWriter out = response.getWriter();
		
		try {
			/* Uploading the data in the serialized, stringfied form */ 
			serializedData = request.getParameter(HttpParameter.DATA.toString());
			/*Fetching an instance of ISerializableFactory to deserialize the input*/
			ISerializableFactory factory = (ISerializableFactory) doLookup(
					JsonFactory.class, ISerializableFactory.class);
			/* Deserializing the input data to its rightful Java form */ 
			InputType input = (InputType)factory.deserialize(serializedData);
			/*Executing the specially defined action and retrivig the output*/
			OutputType output = this.execute(input);
			/*Serializing the output to String and sending it back to the user*/
			out.print(factory.serialize(output));
		} 
		catch (Exception e) {
			/*In case of error notyfing the client*/
			e.printStackTrace();
			response.sendError(500);
		}		
	}
	/**
	 * A function, where the core action is executed. The user-sent <code>input</code>
	 * is received, processed according to the application needs and as a result of that
	 * the <code>output</code> is produced and returned to be sent to the user.
	 * @param input the <code>InputData</code>-typed user-sent data that make up the
	 * input for this action.
	 * @return the <code>OutputData<code> to be sent to the user as a result of this
	 * action.
	 * @throws Exception any type of <code>Exception</code> that the action execution
	 * may produce.
	 */
	protected abstract OutputType execute(InputType input) throws Exception;
	
	/**
	 * Post method is not supported in the project for now.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		;
	}

	/**
	 * Using a standard JNDI lookup method, fetches a proxy for the desired EJB.
	 * @param beanClass the <code>Class</code> of the bean implementation.
	 * @param interfaceClass the <code>Class</code> of bean's remote interface.
	 * @return the bean's proxy instance or <code>null</code> in case of an exception
	 * on the way.
	 */
	protected Object doLookup(Class beanClass, Class interfaceClass) {
		try {
			Context context = JNDILookupClass.getInitialContext();
			Object result = context.lookup(JNDILookupClass.getLookupName("",
					MODULE_NAME , beanClass, interfaceClass, ""));
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
}

