package user.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateUserServlet
 */
@WebServlet(urlPatterns = "/addServlet", initParams = {
		@WebInitParam(name = "SQLDriver", value = "com.mysql.cj.jdbc.Driver"),
		@WebInitParam(name = "dbUrl", value = "jdbc:mysql://localhost/mydb"),
		@WebInitParam(name = "dbUser", value = "root"), @WebInitParam(name = "dbPassword", value = "root") })

public class CreateUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init(ServletConfig config) {
		try {
			System.out.println("Create user init()");
			Class.forName(config.getInitParameter("SQLDriver"));
			connection = DriverManager.getConnection(config.getInitParameter("dbUrl"),
					config.getInitParameter("dbUser"), config.getInitParameter("dbPassword"));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get all the parameters from the form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			// create statement object
			Statement statement = connection.createStatement();
			// varchar values need to be in single quotes
			int result = statement.executeUpdate("insert into user values('" + firstName + "', '" + lastName + "', '"
					+ email + "', '" + password + "')");
			System.out.println(result + " row(s) got inserted.");

			// writing back to browser
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");

			if (result > 0) {
				out.println("<h2>USER CREATED</h2>");
			} else {
				out.println("<h2>Error creating the User</h2>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// end of doPost()

	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}// end of CreateUserServlet class