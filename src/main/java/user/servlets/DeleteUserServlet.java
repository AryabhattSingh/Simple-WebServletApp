package user.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteUser
 */
@WebServlet("/deleteServlet")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "root");
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement
					.executeQuery("select * from user where email = '" + email + "' AND password = '" + password + "'");

			// resultSet.next() moves the pointer in current row to the next row in
			// resultSet
			// This method returns true, unless it is the last row of the resultSet, at
			// which it returns false
			// Our resultSet will contain only one record as no two users can have same
			// email address

			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			if (resultSet.next()) { // it will return false
				out.print("<h2>Sorry, this user doesn't exist</h2>");
			} else {
				int result = statement.executeUpdate("delete from user where email = '" + email + "'");
				System.out.println(result + " row(s) got deleted.");
				out.print("<h2>User got deleted<h2>");
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

}// end of DeleteUser class