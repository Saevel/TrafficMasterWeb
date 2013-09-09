package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Line;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

/**
 * Servlet implementation class GetLinesForStop
 */
@WebServlet("/GetLinesForStop")
public class GetLinesForStop extends TrafficMasterServlet<Integer, List<Line>> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetLinesForStop() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected List<Line> execute(Integer input) throws Exception {
		
		TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote)this.doLookup(
				TrafficNetworkBean.class, TrafficNetworkBeanRemote.class);
		
		return trafficBean.getLinesForStop(input);
	}

	

}
