package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Event;
import trafficmaster.core.Position;
import trafficmaster.core.beans.NewsFeedBean;
import trafficmaster.core.beans.NewsFeedBeanRemote;

/**
 * Servlet implementation class GetRelatedEvents
 */
@WebServlet("/GetRelatedEvents")
public class GetRelatedEvents extends TrafficMasterServlet<Position, List<Event> > {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public GetRelatedEvents() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected List<Event> execute(Position input) throws Exception {
		
		NewsFeedBeanRemote newsFeed = (NewsFeedBeanRemote) doLookup(
				NewsFeedBean.class, NewsFeedBeanRemote.class);
		
		return newsFeed.getRelatedEvents(input);
	}

	

}
