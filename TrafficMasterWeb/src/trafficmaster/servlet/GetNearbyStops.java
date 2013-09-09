package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Position;
import trafficmaster.core.Stop;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

/**
 * Servlet implementation class GetNearbyStops
 */
@WebServlet("/GetNearbyStops")
public class GetNearbyStops extends TrafficMasterServlet<List, List<Stop>> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetNearbyStops() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected List<Stop> execute(List input) throws Exception {
		Position location = (Position) input.get(0);
		Integer howMany = (Integer)input.get(1);
		
		TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote)this.doLookup(
				TrafficNetworkBean.class, TrafficNetworkBeanRemote.class);
		
		return trafficBean.getNearbyStops(location, howMany);
	}
}
