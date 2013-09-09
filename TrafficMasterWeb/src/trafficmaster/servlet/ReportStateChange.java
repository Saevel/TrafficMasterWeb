package trafficmaster.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.State;
import trafficmaster.core.beans.ReportsBean;
import trafficmaster.core.beans.ReportsBeanRemote;

/**
 * Servlet implementation class ReportStateChange
 */
@WebServlet("/ReportStateChange")
public class ReportStateChange extends TrafficMasterServlet<List,Boolean> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public ReportStateChange() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Boolean execute(List input) throws Exception {
	
		State newState = (State)input.get(0);
		Integer courseID = (Integer)input.get(1);
		
		ReportsBeanRemote reports = (ReportsBeanRemote) this.doLookup(
				ReportsBean.class, ReportsBeanRemote.class);
		
		return reports.reportStateChange(newState, courseID);
	}

	

}
