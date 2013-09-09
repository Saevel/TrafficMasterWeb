package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Stop;
import trafficmaster.core.beans.ArrivalsBean;
import trafficmaster.core.beans.ArrivalsBeanRemote;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * Servlet implementation class GetArrivalsForCourse
 */
@WebServlet("/GetArrivalsForCourse")
public class GetArrivalsForCourse extends TrafficMasterServlet<List, Map<Date,Stop>>{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetArrivalsForCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Map<Date, Stop> execute(List input) throws Exception {
		/*Fetching the input parameters*/
		Integer courseID = (Integer)input.get(0);
		Date timeContext = (Date)input.get(1);
		/*Obtaining the ArrivalsBean proxy*/
		ArrivalsBeanRemote arrivals = (ArrivalsBeanRemote)this.doLookup(
				ArrivalsBean.class, ArrivalsBeanRemote.class);
		/*Returinign the results etc.*/
		return arrivals.getArrivalsForCourse(courseID, timeContext);
	}

}
