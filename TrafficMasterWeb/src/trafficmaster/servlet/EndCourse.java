package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.beans.LoggerBean;
import trafficmaster.core.beans.LoggerBeanRemote;

/**
 * Servlet implementation class EndCourse
 */
@WebServlet("/EndCourse")
public class EndCourse extends TrafficMasterServlet<Integer, Boolean> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public EndCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Boolean execute(Integer input) throws Exception {
		/*Obtaning the bean proxy*/
		LoggerBeanRemote logger = (LoggerBeanRemote) doLookup(LoggerBean.class,
				LoggerBeanRemote.class);
		/*Forwarding data and harvesting the result*/
		return logger.endCourse(input);
	}

}
