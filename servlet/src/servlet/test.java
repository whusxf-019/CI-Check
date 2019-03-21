package servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import test.DatabaseController;

/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public test() {
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
	 */
	//type ����ִ�к��ֲ���
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //��ȡ�����͵�����
		
		
        String type=request.getParameter("type");
        PrintWriter os=response.getWriter();      
        String user = request.getParameter("user");
    	Connection conn = DatabaseController.getCon();
        String myStatement = null;
        
        if(type.equals("check_name")) {
        	String userAccount = request.getParameter("userAccount");
        	
        	if(user.equals("true")) {
        		 myStatement = "select userAccount from user where userAccount='"+userAccount+"'";
        	}else if(user.equals("false")) {
        		 myStatement = "select adminAccount from administrator where adminAccount='"+userAccount+"'";
        	}else {
       	 		os.print("false");
       	 		return;
       	 	}
            ResultSet rs = DatabaseController.select(conn,myStatement);
            try {
                rs.last();
                if(rs.getRow()==0){
                	os.print("true");//û���ظ�������
                }else {
                    os.print("false");
                }
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        	
            //��ͻ��˷�������
            os.flush();
            os.close();
        }
        //增加登陆时对isLogin的更新，登陆时设置为”1“
        else if(type.equals("login_confirm")) {
        	 String userAccount = request.getParameter("userAccount");
        	 String password = request.getParameter("password");
        	 String updateStatement=null;
        	 if(user.equals("true")) {
        		 myStatement =  "select password from user where userAccount='"+userAccount+"'";
        		 updateStatement="update user set isLogin = '1' where userAccount='"+userAccount+"'";
        	 }else if(user.equals("false")) {
        		 myStatement = "select password from administrator where adminAccount='"+userAccount+"'";
        		 updateStatement="update administrator set isLogin = '1' where adminAccount='"+userAccount+"'";
                 
        	 }else {
        	 		os.print("false");
           	 		return;
           	 }
             ResultSet rs = DatabaseController.select(conn,myStatement);
             //
             try {
                 while (rs.next()) {
                	 String aString = rs.getString(1);
                     if (!rs.getString(1).equals(password)) {
                    	 os.print("false");
                    	 return;
                     }else{
                         DatabaseController.excute(conn, updateStatement);
                		 
                    	 os.print("true");
                    	 return;
                     }
                 }
                 os.print("false");//�û���������
             }catch (SQLException e){
                 e.printStackTrace();
             }
        }
        else if(type.equals("signup_confirm")) {
        	String userAccount = request.getParameter("userAccount");
       	 	String password = request.getParameter("password");
       	 	String password_re = request.getParameter("password_re");
       	 	String userName =URLDecoder.decode(request.getParameter("userName"),"utf-8");
       	 	//�ж��Ƿ�Ϊ��ͨ�û�
       	 	if(user.equals("true")) {
       	 		myStatement = "INSERT INTO user VALUES('"+userName+"','"+password+"','"+userAccount+"','0')";
       	 	}else if(user.equals("false")) {
       	 		myStatement = "INSERT INTO administrator VALUES('"+userName+"','"+password+"','"+userAccount+"','0')";
       	 	}else {
       	 		os.print("false");
       	 		return;
       	 	}
            int result = DatabaseController.excute(conn,myStatement);
            if(result == 0) {
           		os.print("true");
            }else{
            	os.print("false");
             }
        }
        DatabaseController.closeConn(conn);
        
	}

}
