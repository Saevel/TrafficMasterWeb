package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Course;
import trafficmaster.core.Position;
import trafficmaster.core.beans.LoggerBean;
import trafficmaster.core.beans.LoggerBeanRemote;

/**
 * Servlet implementation class StartNewCourse
 */
@WebServlet("/StartNewCourse")
public class StartNewCourse extends TrafficMasterServlet<List, Course> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public StartNewCourse() {
        super();
        // TODO Auto-generated constructor stub
    }


	@Override
	protected Course execute(List input) throws Exception {
		/*Fetching the input parameters*/
		String lineName = (String)input.get(0);
		Position position = (Position)input.get(1);
		/*Obtaning the LoggerBean proxy*/
		LoggerBeanRemote logger = (LoggerBeanRemote) doLookup(LoggerBean.class,
				LoggerBeanRemote.class);
		/*Forwarding the data and returning the result*/
		return logger.startNewCourse(lineName, position);
	}

}
