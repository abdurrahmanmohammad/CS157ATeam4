package adminPortal;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configurations.Configurations;
import courses.Courses;

/**
 * Servlet implementation class insertConfiguration
 */
@WebServlet("/insertConfiguration")
public class insertConfiguration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public insertConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String configID = request.getParameter("configID"); // configID is
		// autogenerated
		String adminID = request.getParameter("adminID");
		String term = request.getParameter("term");
		int year = Integer.parseInt(request.getParameter("year"));
		String days = request.getParameter("days");
		String time = request.getParameter("time");
		String room = request.getParameter("room");
		int seats = Integer.parseInt(request.getParameter("seats"));
		Configurations.insert(term, year, days, time, room, seats);
		RequestDispatcher req = request.getRequestDispatcher("manageConfigurations.jsp?adminID=" + adminID);
		req.forward(request, response);
	}

}
