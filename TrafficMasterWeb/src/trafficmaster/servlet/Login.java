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
import javax.servlet.http.HttpSession;

import trafficmaster.core.GlobalSettings;
import trafficmaster.core.beans.LoggerBean;
import trafficmaster.core.beans.LoggerBeanRemote;
import trafficmaster.serializable.ISerializableFactory;
import trafficmaster.serializable.json.JsonFactory;

import com.ibytecode.clientutility.JNDILookupClass;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	public static final String USER_NAME_PARAMETER = "Name";
	public static final String PASSWORD_PARAMETER = "Password";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*Setting the content type as plain text*/
		response.setContentType("text/plain");
		/*Getting the writer*/
		PrintWriter out = response.getWriter();
		/*Retriving the user name and password*/
		String password = (String) request.getParameter(PASSWORD_PARAMETER);
		String user = (String) request.getParameter(USER_NAME_PARAMETER);
		/*Creating the new user session*/
		HttpSession session = request.getSession();
		/*Registering the */
		session.putValue(USER_NAME_PARAMETER, user);
		session.putValue(PASSWORD_PARAMETER, password);
		
		try {
			Context context = JNDILookupClass.getInitialContext();
			/*Obtainign the logging service*/
			LoggerBeanRemote logger = (LoggerBeanRemote)context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb",
					LoggerBean.class, LoggerBeanRemote.class, ""));
			/*Fetching the current application settings*/
			GlobalSettings settings = logger.logIn(user, password);
			/*Getting the serializator bean*/
			ISerializableFactory serializer = (ISerializableFactory) context.lookup(
					JNDILookupClass.getLookupName("", "TrafficMasterWeb",
							JsonFactory.class, ISerializableFactory.class, ""));
			/*Serializing the settings and returning them to the user*/
			out.println(serializer.serialize(settings));
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
