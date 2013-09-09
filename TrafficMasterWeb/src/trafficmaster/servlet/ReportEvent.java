package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Event;
import trafficmaster.core.beans.NewsFeedBean;
import trafficmaster.core.beans.NewsFeedBeanRemote;

/**
 * Servlet implementation class ReportEvent
 */
@WebServlet("/ReportEvent")
public class ReportEvent extends TrafficMasterServlet<Event, Boolean> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public ReportEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Boolean execute(Event input) throws Exception {
		
		NewsFeedBeanRemote newsFeed = (NewsFeedBeanRemote) doLookup(
				NewsFeedBean.class, NewsFeedBeanRemote.class);
		
		return newsFeed.reportEvent(input);
	}

}
