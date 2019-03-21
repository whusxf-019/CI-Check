package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import FaceManager.FaceAdd;
import FaceMatch.FaceSearch;

/**
 * Servlet implementation class AddController
 */
@WebServlet("/AddController")
public class AddController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * if success return face_token    @
	 * else return "0"                 @
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String image = request.getParameter("image");
		String group_id = request.getParameter("group_id");
		String user_id = request.getParameter("user_id");
		String face_token = FaceAdd.add(image, group_id, user_id);
		PrintWriter out = response.getWriter();
		if(face_token==null) {
			out.write("0");
		}else {
			out.write(face_token);
		}
        out.flush();
        out.close();  
	}

}
