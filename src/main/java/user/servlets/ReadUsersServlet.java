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
 * Servlet implementation class ReadUsersServlet
 */
@WebServlet("/ReadUsersServlet")
public class ReadUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
 
	public void init() {
		try {
			System.out.println("ReadUsersServlet init()");
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "root");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from user");
			PrintWriter out = response.getWriter();
			out.print("<table border = '1' cellspacing = '8'>"
					+ "<tr>"
					+ 	"<th>Full Name</th>"
					+ 	"<th>Email Address</th>"
					+ "</tr>");
			while (resultSet.next()) {
				out.print("<tr>"
						+ 	"<td>"
						+ 		resultSet.getString(1) //firstName
						+		"&nbsp"
						+		resultSet.getString(2) //lastName
						+	"</td>"
						+	"<td>"
						+		resultSet.getString(3) //email
						+	"</td>"
						+ "</tr>");
			}
			out.print("</table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
