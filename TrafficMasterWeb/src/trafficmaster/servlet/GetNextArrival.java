package trafficmaster.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.beans.ArrivalsBean;
import trafficmaster.core.beans.ArrivalsBeanRemote;

/**
 * Servlet implementation class GetNextArrival
 */
@WebServlet("/GetNextArrival")
public class GetNextArrival extends TrafficMasterServlet<List, Date> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetNextArrival() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Date execute(List input) throws Exception {
		/*Fetching the paramters*/
		Integer stopID = (Integer)input.get(0);
		Integer lineID = (Integer)input.get(1);
		Date timeContext = (Date) input.get(2);
		/*Obtaining the ArrivalsBean proxy*/
		ArrivalsBeanRemote arrivals = (ArrivalsBeanRemote)this.doLookup(
				ArrivalsBean.class, ArrivalsBeanRemote.class);
		/*Forwarding data and the result etc.*/
		return arrivals.getNextArrival(stopID, lineID, timeContext);
	}

}
