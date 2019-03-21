package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.PUBLIC_MEMBER;

import Util.GsonUtils;
import test.DatabaseController;

/**
 * Servlet implementation class other
 */
@WebServlet("/other")
public class other extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public other() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		System.out.print("sss");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
				String type = request.getParameter("type");
				response.setHeader("Content-type", "application/json;charset=UTF-8");
		        PrintWriter out=response.getWriter();
				String myStatement = null;
				Connection conn =DatabaseController.getCon();
				//管理员搜索组员
				if(type.equals("check_me")){
					//找到所有成员的说有信息
					String adminAccount = request.getParameter("adminAccount");
					ArrayList<HashMap<String, String>> result = new ArrayList<>();
					HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap();
					myStatement = "select u.userName,u.userAccount from `group` a,user u where a.adminAccount = '"+adminAccount+"' AND a.userAccount = u.userAccount" ;
					ResultSet rs = DatabaseController.select(conn, myStatement);
					try {
						while(rs.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							String uName = rs.getString(1);
							String uAccount = rs.getString(2);
							hashMap.put("userName", uName);
							hashMap.put("userAccount", uAccount);
							result.add(hashMap);
						}
						result2.put("result", result);
						out.print(GsonUtils.toJson(result2));
					} catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
						out.print("false");
					}
				}
				//��Ҫ��֤userAccount��Ϊ�գ�����user������Ϣ
				else if (type.equals("update_user_info")) {
					String userName = request.getParameter("userName");
					String userAccount = request.getParameter("userAccount");
					String password = request.getParameter("password");
					if(!password.equals("")) {
						myStatement = "update user set  password = '"+password+"' where userAccount = '"+userAccount+"'";
						DatabaseController.excute(conn, myStatement);
					}
					if(!userName.equals("")) {
						myStatement = "update user set  userName = '"+userName+"' where userAccount = '"+userAccount+"'";
					}
					if(DatabaseController.excute(conn, myStatement) == 0) {
						out.print("true");
					}else {
						out.print("false");
					}
				}
				//如果user有组就把组长信息拿出来，没有就只拿user信息
				else if (type.equals("see_user_info")) {
					String userAccount = request.getParameter("userAccount");
					ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
					HashMap<String , ArrayList<HashMap<String, String>>> result = new HashMap();
					myStatement = "select a.adminName,g.adminAccount,u.userName,u.userAccount"
							+ " from `user` u,`group` g , administrator a where u.userAccount like '%"+
							userAccount+"%' and u.userAccount = g.userAccount and g.adminAccount = a.adminAccount";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					boolean isEmpty = true;
					try {
						while(rSet.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("userAccount",rSet.getString("userAccount"));
							hashMap.put("userName",rSet.getString("userName"));
							hashMap.put("adminAccount", rSet.getString("adminAccount"));
							hashMap.put("adminName", rSet.getString("adminName"));
							hashMaps.add(hashMap);
							isEmpty = false;
						}
						if (!isEmpty) {
							result.put("result", hashMaps);
							out.print(GsonUtils.toJson(result));
						}else {

							myStatement = "select u.userName,u.userAccount"
									+ " from `user` u where u.userAccount like '%"+userAccount+"%'";
							ResultSet rs = DatabaseController.select(conn, myStatement);
							while(rs.next()) {
								HashMap<String, String> hashMap = new HashMap<>();
								hashMap.put("userAccount",rs.getString("userAccount"));
								hashMap.put("userName",rs.getString("userName"));
								hashMap.put("adminAccount", "");
								hashMap.put("adminName","");
								hashMaps.add(hashMap);	
							}
							result.put("result", hashMaps);
							out.print( GsonUtils.toJson(result));
							}
						}catch (SQLException e) {
							// TODO: handle exception
							e.printStackTrace();
							out.print("false");
						}//end try
					
				}//end if
				else if (type.equals("see_admin_info")) {
					ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
					HashMap<String , ArrayList<HashMap<String, String>>> result = new HashMap();
					String adminAccount = request.getParameter("adminAccount");
					myStatement = "select adminName,adminAccount from administrator where adminAccount like '%"+adminAccount+"%'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						while(rSet.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("adminAccount",rSet.getString("adminAccount"));
							hashMap.put("adminName",rSet.getString("adminName"));
							hashMaps.add(hashMap);
						}
						result.put("result", hashMaps);
						out.print( GsonUtils.toJson(result));
					}catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
						out.print("false");
					}//end try
				}
				//普通用户搜索本组组员
				else if(type.equals("check_me_by_user")){
					//找到所有成员的说有信息
					String userAccount = request.getParameter("userAccount");
					String adminAccount = getAdminAccount(userAccount, conn);
					ArrayList<HashMap<String, String>> result = new ArrayList<>();
					HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap();
					myStatement = "select u.userName,u.userAccount from `group` a,user u where a.adminAccount = '"+adminAccount+"' AND a.userAccount = u.userAccount" ;
					ResultSet rs = DatabaseController.select(conn, myStatement);
					try {
						while(rs.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							String uName = rs.getString(1);
							String uAccount = rs.getString(2);
							hashMap.put("userName", uName);
							hashMap.put("userAccount", uAccount);
							result.add(hashMap);
						}
						result2.put("result", result);
						out.print(GsonUtils.toJson(result2));
					} catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
						out.print("false");
					}
				}
				else if(type.equals("check_if_have_group")) {
					String userAccount = request.getParameter("userAccount");
					String result = getAdminAccount(userAccount, conn);
					if(result.equals("false")) {
						out.print("false");
					}
					else {
						out.print("true");
					}
				}
				//判断用户是否已经登陆
				else if(type.equals("user_isLogin")){
					String userAccount=request.getParameter("userAccount");
					myStatement="select isLogin from `user` where userAccount = '"+userAccount+"'";
					ResultSet rs = DatabaseController.select(conn, myStatement);
					try {
						if(rs.next()) {
							String result=rs.getString("isLogin");
							out.print(result);
						}
					} catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
						out.print("false");
					}
				}
				//判断管理员是否已经登陆
				else if(type.equals("admin_isLogin")){
					String adminAccount=request.getParameter("adminAccount");
					myStatement="select isLogin from administrator where adminAccount = '"+adminAccount+"'";
					ResultSet rs = DatabaseController.select(conn, myStatement);
					try {
						if(rs.next()) {
							String result=rs.getString("isLogin");
							out.print(result);
						}
					} catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
						out.print("false");
					}
				}
				//退出时，管理员把isLogin设置为”0“
				else if(type.equals("admin_exit")){
					String adminAccount=request.getParameter("adminAccount");
					myStatement="update administrator set isLogin = '0' where adminAccount='"+adminAccount+"'";			
					if(DatabaseController.excute(conn, myStatement)==0) {
						out.print("true");
					}
					else {
						out.print("false");
					}
				
				}
				//退出时，用户把isLogin设置为”0“
				else if(type.equals("user_exit")){
					String userAccount=request.getParameter("userAccount");
					myStatement="update user set isLogin = '0' where userAccount='"+userAccount+"'";
					if(DatabaseController.excute(conn, myStatement)==0) {
						out.print("true");
					}
					else {
						out.print("false");
					}
				}
				//查看自己的信息
				else if(type.equals("user_lookSelf")){
					String userAccount = request.getParameter("userAccount");
					ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
					HashMap<String , ArrayList<HashMap<String, String>>> result = new HashMap();
					myStatement = "select a.adminName,g.adminAccount,u.userName,u.userAccount"
							+ " from `user` u,`group` g , administrator a where u.userAccount = '"+
							userAccount+"' and u.userAccount = g.userAccount and g.adminAccount = a.adminAccount";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					boolean isEmpty = true;
					try {
						while(rSet.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("userAccount",rSet.getString("userAccount"));
							hashMap.put("userName",rSet.getString("userName"));
							hashMap.put("adminAccount", rSet.getString("adminAccount"));
							hashMap.put("adminName", rSet.getString("adminName"));
							hashMaps.add(hashMap);
							isEmpty = false;
						}
						if (!isEmpty) {
							result.put("result", hashMaps);
							out.print(GsonUtils.toJson(result));
						}else {

							myStatement = "select u.userName,u.userAccount"
									+ " from `user` u where u.userAccount like '%"+userAccount+"%'";
							ResultSet rs = DatabaseController.select(conn, myStatement);
							while(rs.next()) {
								HashMap<String, String> hashMap = new HashMap<>();
								hashMap.put("userAccount",rs.getString("userAccount"));
								hashMap.put("userName",rs.getString("userName"));
								hashMap.put("adminAccount", "");
								hashMap.put("adminName","");
								hashMaps.add(hashMap);	
							}
							result.put("result", hashMaps);
							out.print( GsonUtils.toJson(result));
							}
						}catch (SQLException e) {
							// TODO: handle exception
							e.printStackTrace();
							out.print("false");
						}//end try
				}
				//查看自己的信息
				else if(type.equals("admin_lookSelf")){
					String adminAccount = request.getParameter("adminAccount");
					ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
					HashMap<String , ArrayList<HashMap<String, String>>> result = new HashMap();
					myStatement = "select *"
							+ " from administrator where adminAccount = '"+
							adminAccount+"' ";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						while(rSet.next()) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("adminAccount", rSet.getString("adminAccount"));
							hashMap.put("adminName", rSet.getString("adminName"));
							hashMaps.add(hashMap);
						}
							result.put("result", hashMaps);
							out.print( GsonUtils.toJson(result));
						}catch (SQLException e) {
							// TODO: handle exception
							e.printStackTrace();
							out.print("false");
						}//end try
				}
				//更新管理员的信息
				else if(type.equals("admin_updateInfo")){
					String adminAccount=request.getParameter("adminAccount");
					String adminName=URLDecoder.decode(request.getParameter("adminName"),"utf-8");
					myStatement="update administrator set adminName = '"+adminName+"' where adminAccount='"+adminAccount+"'";

					if(DatabaseController.excute(conn, myStatement)!=0) {
						out.print("false");
					}else {
						out.print("true");
					}					
				}
				//更新用户的信息
				else if(type.equals("user_updateInfo")){
					String userAccount=request.getParameter("userAccount");
					String userName=URLDecoder.decode(request.getParameter("userName"),"utf-8");
					myStatement="update user set userName = '"+userName+"' where userAccount='"+userAccount+"'";

					if(DatabaseController.excute(conn, myStatement)!=0) {
						out.print("false");
					}else {
						out.print("true");
					}					
				}
				DatabaseController.closeConn(conn);
	}
	public static String getAdminAccount(String userAccount,Connection conn) {
		//找到组长的账号
		String myStatement = null;
		myStatement = "select adminAccount from `group` where userAccount = '"+userAccount+"'";
		ResultSet rs = DatabaseController.select(conn, myStatement);
		try {
			if(rs.next()) {
				String adminAccount = rs.getString("adminAccount");
				return adminAccount;
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "false";
	}

}
