package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Stop;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

/**
 * Servlet implementation class GetStopsForLine
 */
@WebServlet("/GetStopsForLine")
public class GetStopsForLine extends TrafficMasterServlet<Integer, List<Stop>> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetStopsForLine() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected List<Stop> execute(Integer input) throws Exception {
		
		TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote)this.doLookup(
				TrafficNetworkBean.class, TrafficNetworkBeanRemote.class);
		
		return trafficBean.getStopsForLine(input);
	}

	

}
