package trafficmaster.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Criteria;
import trafficmaster.core.Position;
import trafficmaster.core.RouteFragment;
import trafficmaster.core.beans.PlannerBean;
import trafficmaster.core.beans.PlannerBeanRemote;

/**
 * Servlet implementation class GetOptimalRoute
 */
@WebServlet("/GetOptimalRoute")
public class GetOptimalRoute extends TrafficMasterServlet<List, List<RouteFragment> > {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetOptimalRoute() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected List<RouteFragment> execute(List input) throws Exception {
		
		Position from = (Position)input.get(0);
		Position to = (Position)input.get(1);
		Date when = (Date) input.get(2);
		Criteria criteria = (Criteria)input.get(3);
		Date timeContext = (Date)input.get(4);
		
		PlannerBeanRemote planner = (PlannerBeanRemote) doLookup(PlannerBean.class,
				PlannerBeanRemote.class);
		
		return planner.getOptimalRoute(from, to, when, criteria, timeContext);
	}
}
