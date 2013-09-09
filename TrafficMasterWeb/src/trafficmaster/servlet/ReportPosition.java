package trafficmaster.servlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trafficmaster.core.Position;
import trafficmaster.core.beans.ReportsBean;
import trafficmaster.core.beans.ReportsBeanRemote;

import java.util.List;

/**
 * Servlet implementation class ReportPosition
 */
@WebServlet("/ReportPosition")
public class ReportPosition extends TrafficMasterServlet<List,Boolean> {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see TrafficMasterServlet#TrafficMasterServlet()
     */
    public ReportPosition() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Boolean execute(List input) throws Exception {
		
		Position position = (Position)input.get(0);
		Integer courseID = (Integer)input.get(1);
		
		ReportsBeanRemote reports = (ReportsBeanRemote) this.doLookup(
				ReportsBean.class, ReportsBeanRemote.class);
				
		return reports.reportPosition(position, courseID);	
	}


}
