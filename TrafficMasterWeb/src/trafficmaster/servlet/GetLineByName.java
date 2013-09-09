package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Line;
import trafficmaster.core.beans.TrafficNetworkBean;
import trafficmaster.core.beans.TrafficNetworkBeanRemote;

/**
 * Servlet implementation class GetLineByName
 */
@WebServlet("/GetLineByName")
public class GetLineByName extends TrafficMasterServlet<String, Line> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetLineByName() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Line execute(String input) throws Exception {
		
		TrafficNetworkBeanRemote trafficBean = (TrafficNetworkBeanRemote)this.doLookup(
				TrafficNetworkBean.class, TrafficNetworkBeanRemote.class);
		
		return trafficBean.getLineByName(input);
	}
}
