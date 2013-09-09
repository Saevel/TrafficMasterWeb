package trafficmaster.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Stop;
import trafficmaster.core.beans.ArrivalsBean;
import trafficmaster.core.beans.ArrivalsBeanRemote;

/**
 * Servlet implementation class GetArrivals
 */
@WebServlet("/GetArrivals")
public class GetArrivals extends TrafficMasterServlet<List, Map<Date,Stop>> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetArrivals() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Map<Date, Stop> execute(List input) throws Exception {
		/*Retriving the input parameters*/
		Integer stopID = (Integer)input.get(0);
		Integer lineID = (Integer)input.get(1);
		Date timeContext = (Date)input.get(2);
		/*Getting the arrivals bean proxy*/
		ArrivalsBeanRemote arrivals = (ArrivalsBeanRemote)this.doLookup(
				ArrivalsBean.class, ArrivalsBeanRemote.class);
		/*Forwarding and returning data*/
		return arrivals.getArrivals(stopID, lineID, timeContext);
	}

}
