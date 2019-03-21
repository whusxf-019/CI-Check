package servlet;

import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.javafx.collections.MappingChange.Map;
import com.sun.org.apache.bcel.internal.generic.NEW;

import Util.GsonUtils;
import test.DatabaseController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Servlet implementation class leave
 */
@WebServlet("/leave")
public class leave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public leave() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String type = request.getParameter("type");
		String myStatement = null;
		Connection conn = DatabaseController.getCon(); 
		if(type.equals("insertInto_leave")) {
			//获取当前时间
			Date nowTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = formatter.format(nowTime);
			//获取剩余参数
			String leave_type =  URLDecoder.decode(request.getParameter("leave_type"),"utf-8");
			String userAccount = request.getParameter("userAccount");
			String adminAccount = other.getAdminAccount(userAccount, conn);
			String reason =  URLDecoder.decode(request.getParameter("reason"),"utf-8");
			String startTime = request.getParameter("startTime");//年月日时分秒
			String endTime = request.getParameter("endTime");
			 myStatement = "insert into `leave`(userAccount,adminAccount,startTime,endTime,reason,leave_type,state,handTime) values('"
			 +userAccount+"','"+adminAccount+"',"+"str_to_date('"+startTime+"','%Y-%m-%d %H:%i:%s')"+","+"str_to_date('"+endTime+"','%Y-%m-%d %H:%i:%s'),'"+reason+"','"+leave_type+"'"
			 		+ ",'未审核',str_to_date('"+now+"', '%Y-%m-%d %H:%i:%s'))";
			 if(DatabaseController.excute(conn, myStatement)==0) {
				 myStatement = "insert into adminmessage values('admin_leave_message','"+userAccount+"_"+group.getUserName(userAccount, conn)+"_"+startTime+"' ,"+"str_to_date('"+DatabaseController.getNowTime()+
						 "','%Y-%m-%d %H:%i:%s'),'"+adminAccount+"' , 'apply_leave','未读')";//开始时间存于content
				 if(DatabaseController.excute(conn, myStatement)==0)
					 out.print("true");
			 }
			 else{
				out.print("false");
			}
		}
		//把每条记录选取出来，记录用哈希表存，再用集合存，转为json返回
		else if(type.equals("check_leave_by_user")){
			String userAccount = request.getParameter("userAccount");
			myStatement = "select adminAccount,handTime,startTime,endTime,"
					+ "reason,state,leave_type from `leave` where userAccount = '"+userAccount+"' ORDER BY state DESC";
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap<>();
			try {
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			while(rSet.next()){
				HashMap<String, String> user_leave = new HashMap<String, String>();
				user_leave.put("adminAccount",rSet.getString("adminAccount"));
				user_leave.put("handTime",rSet.getString("handTime"));
				user_leave.put("startTime",rSet.getString("endTime"));
				user_leave.put("endTime",rSet.getString("endTime"));
				user_leave.put("reason",rSet.getString("reason"));
				user_leave.put("state",rSet.getString("state"));
				user_leave.put("leave_type",rSet.getString("leave_type"));
				result.add(user_leave);
				}//end while
			result2.put("result", result);
			String jsonObject = GsonUtils.toJson(result2);
			out.print(jsonObject);
			}catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				out.print("error:"+e.getMessage());
			}//end try
		}//end if
		//把每条记录选取出来，记录用哈希表存，再用集合存，转为json返回
		else if(type.equals("check_leave_by_admin")){
			String adminAccount = request.getParameter("adminAccount");
			String subType = request.getParameter("subType");
			if(subType.equals("checked")) {			
				myStatement = "select userAccount,handTime,startTime,endTime,"
						+ "reason,state,leave_type from `leave` where adminAccount = '"+adminAccount+"' and state <> '未审核'";
			}else {
				myStatement = "select userAccount,handTime,startTime,endTime,"
						+ "reason,state,leave_type from `leave` where adminAccount = '"+adminAccount+"' and state = '未审核'";
			}
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap<>();
			try {
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			while(rSet.next()){
				HashMap<String, String> user_leave = new HashMap<String, String>();
				user_leave.put("userAccount",rSet.getString("userAccount"));
				user_leave.put("handTime",rSet.getString("handTime"));
				user_leave.put("startTime",rSet.getString("startTime"));
				user_leave.put("endTime",rSet.getString("endTime"));
				user_leave.put("reason",rSet.getString("reason"));
				user_leave.put("state",rSet.getString("state"));
				user_leave.put("leave_type",rSet.getString("leave_type"));
				result.add(user_leave);
				}//end while
			result2.put("result", result);
			String jsonObject = GsonUtils.toJson(result2);
			out.print(jsonObject);
			}catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				out.print("error:"+e.getMessage());
			}//end try
		}//end if
		else if(type.equals("update_leave_agree")) {
			String userAccount = request.getParameter("userAccount");
			String startTime = request.getParameter("startTime");//年月日时分秒
			String adminAccount = request.getParameter("adminAccount");
			String adminName = group.getAdminName(adminAccount, conn);
			
			 myStatement = "update `leave` set state = '成功' where userAccount = '"+userAccount+"' and startTime = '"+startTime+"'";
			 if(DatabaseController.excute(conn, myStatement)==0) {
				 myStatement = "insert into usermessage values('user_leave_message','"+adminName+"_"+adminAccount+"' ,"+"str_to_date('"+DatabaseController.getNowTime()+
						 "','%Y-%m-%d %H:%i:%s'),'"+userAccount+"' , 'by_admin_agree','未读')";
				 if(DatabaseController.excute(conn, myStatement)==0)
					 out.print("true");
			 }//end if
			 else{
				out.print("false");
			}
		}//end if
		else if(type.equals("update_leave_disagree")) {
			String userAccount = request.getParameter("userAccount");
			String startTime = request.getParameter("startTime");//年月日时分秒
			String adminAccount = request.getParameter("adminAccount");
			String adminName = group.getAdminName(adminAccount, conn);
			
			 myStatement = "update `leave` set state = '失败' where userAccount = '"+userAccount+"' and startTime = '"+startTime+"'";
			 if(DatabaseController.excute(conn, myStatement)==0) {
				 myStatement = "insert into usermessage values('user_leave_message','"+adminName+"_"+adminAccount+"' ,"+"str_to_date('"+DatabaseController.getNowTime()+
						 "','%Y-%m-%d %H:%i:%s'),'"+userAccount+"' , 'by_admin_disagree','未读')";
				 if(DatabaseController.excute(conn, myStatement)==0)
					 out.print("true");
			 }//end if
			 else{
				out.print("false");
			}
		}//end if
		else if(type.equals("select_user_leave")) {
			String userAccount = request.getParameter("userAccount");
			String startTime = request.getParameter("startTime");//年月日时分秒
			myStatement = "select * from `leave` where userAccount = '"+userAccount+"' and startTime = '"+startTime+"'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if(rSet.next()) {
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("adminAccount", rSet.getString("adminAccount"));
					hashMap.put("userAccount", rSet.getString("userAccount"));
					hashMap.put("startTime", rSet.getString("startTime"));
					hashMap.put("endTime", rSet.getString("endTime"));
					hashMap.put("reason", rSet.getString("reason"));
					hashMap.put("state", rSet.getString("state"));
					hashMap.put("leave_type", rSet.getString("leave_type"));
					hashMap.put("handTime", rSet.getString("handTime"));
					out.print(GsonUtils.toJson(hashMap));
				}
				 else{
					out.print("false");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end if
		DatabaseController.closeConn(conn);
	}//end post

}
