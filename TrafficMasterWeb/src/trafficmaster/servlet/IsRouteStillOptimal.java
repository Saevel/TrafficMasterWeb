package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.beans.NewsFeedBean;
import trafficmaster.core.beans.NewsFeedBeanRemote;
import trafficmaster.core.beans.PlannerBean;
import trafficmaster.core.beans.PlannerBeanRemote;

/**
 * Servlet implementation class IsRouteStillOptimal
 */
@WebServlet("/IsRouteStillOptimal")
public class IsRouteStillOptimal extends TrafficMasterServlet<Object, Boolean > {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public IsRouteStillOptimal() {
        super();
        // TODO Auto-generated constructor stub
    }
	@Override
	protected Boolean execute(Object input) throws Exception {
		
		PlannerBeanRemote planner = (PlannerBeanRemote) doLookup(PlannerBean.class,
				PlannerBeanRemote.class);
		
		// TODO : dodaæ pobieranie rzeczy z sesji 
		
		return planner.isRouteStillOptimal();
	}

	
}
