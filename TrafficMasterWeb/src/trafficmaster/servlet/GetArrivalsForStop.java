package trafficmaster.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Course;
import trafficmaster.core.beans.ArrivalsBean;
import trafficmaster.core.beans.ArrivalsBeanRemote;

/**
 * Servlet implementation class GetArrivalsForStopServlet
 */
@WebServlet("/GetArrivalsForStopServlet")
public class GetArrivalsForStop extends TrafficMasterServlet<List<?>, Map<Date,Course>> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetArrivalsForStop() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Map<Date, Course> execute(List<?> input) throws Exception {
		/*Retriving the parameters*/
		Integer stopID = (Integer)input.get(0);
		Date timeContext = (Date)input.get(1);
		/*Getting the ArrivalsBeanRemote instance*/
		ArrivalsBeanRemote arrivals = (ArrivalsBeanRemote)doLookup(
				ArrivalsBean.class, ArrivalsBeanRemote.class);
		/*Forwarding the data etc.*/
		return arrivals.getArrivalsForStop(stopID, timeContext);
	}
}
