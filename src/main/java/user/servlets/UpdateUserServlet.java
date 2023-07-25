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
 * Servlet implementation class UpdateUserServlet
 */
@WebServlet("/updateServlet")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init() {
		try {
			System.out.println("update user init()");
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
		System.out.println("update user doPost()");
		String email = request.getParameter("email");
		String newPassword = request.getParameter("newPassword");
		String currentPassword = request.getParameter("currentPassword");

		try {
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(
					"select * from user where email = '" + email + "' and password = '" + currentPassword + "'");

			// resultSet.next() moves the pointer in current row to the next row in
			// resultSet
			// This method returns true, unless it is the last row of the resultSet, at
			// which it returns false
			// Our resultSet will contain only one record as no two users can have same
			// email address

			// For writing back to the browser
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");

			if (!resultSet.next()) {
				System.out.println("This user doesn't exist.");
				out.println("<h2>This user doesn't exist.</h2>");
			} else {

				int result = statement.executeUpdate("update user set password = '" + newPassword + "' where email = '"
						+ email + "' AND password = '" + currentPassword + "'");
				System.out.println(result + " rows got updated");

				// writing back to the browser
				if (result > 0) {
					out.println("<h2>Password of the user was updated.</h2");
				} else {
					out.println("<h2>Password of the user could not be updated.</h2");
				}

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

}// end of UpdateUserServlet class