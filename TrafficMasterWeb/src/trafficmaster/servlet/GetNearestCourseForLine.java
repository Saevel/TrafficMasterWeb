package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Course;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

/**
 * Servlet implementation class GetNearestCourseForLine
 */
@WebServlet("/GetNearestCourseForLine")
public class GetNearestCourseForLine extends TrafficMasterServlet<List, Course> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetNearestCourseForLine() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Course execute(List input) throws Exception {
		
		Integer lineID = (Integer)input.get(0);
		Integer stopID = (Integer)input.get(1);
		
		TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote)this.doLookup(
				TrafficNetworkBean.class, TrafficNetworkBeanRemote.class);
		
		return trafficBean.getNearestCourseForLine(lineID, stopID);
	}

	
}
