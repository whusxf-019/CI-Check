package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.catalina.tribes.util.StringManager;
import org.apache.jasper.tagplugins.jstl.core.Out;

import com.google.gson.Gson;

import com.sun.javafx.collections.MappingChange.Map;

import Util.GsonUtils;
import test.DatabaseController;

/**
 * Servlet implementation class message
 */
@WebServlet("/message")
public class message extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static 	Connection conn =DatabaseController.getCon();
	private Lock lock=new ReentrantLock();
	public static HashMap<String, Integer> userCount = new HashMap<>();
	public static HashMap<String, Integer> adminCount = new HashMap<>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public message() {
        super();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<String> arrayList = new ArrayList<>();
				for(String key:userCount.keySet()) {
					arrayList.add(key);
				}
				lock.lock();
		        for(String key:arrayList)
		        {
		        	int a = userCount.get(key);
		        	if(a<=0) {
		        		String statement = "update user set isLogin = '0' where userAccount = '"+key+"'";
		        		if(DatabaseController.excute(conn, statement)==0) {
		        			System.out.println("修改成功");
		        			userCount.remove(key);
		        		}
		        	}else {
			        	a--;
			        	userCount.put(key, a);
		        	}
		        }
				ArrayList<String> adminlist = new ArrayList<>();
				for(String key:adminCount.keySet()) {
					adminlist.add(key);
				}
		        for(String key:adminlist)
		        {
		        	int a = adminCount.get(key);
		        	if(a<=0) {
		        		String statement = "update administrator set isLogin = '0' where adminAccount = '"+key+"'";
		        		if(DatabaseController.excute(conn, statement)==0) {
		        			System.out.println("修改成功");
		        			adminCount.remove(key);
		        		}
		        	}else {
			        	a--;
			        	adminCount.put(key, a);
					}
		        }
		        lock.unlock();
			}
		}, 0,(long) (1000*1.5));
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
		String subType = request.getParameter("subType");
		ArrayList<HashMap<String, String>> maps = new ArrayList<>();
		HashMap<String, ArrayList<HashMap<String, String>>> result = new HashMap<>();
		String myStatement = null;
		if(type.equals("user")) {
			String userAccount = request.getParameter("userAccount");
			lock.lock();
			if(userCount.keySet().contains(userAccount)) {
				int a = userCount.get(userAccount);
				a++;
				userCount.put(userAccount, a);
			}else{
				userCount.put(userAccount,5);
			}
			lock.unlock();
				try {
					if(subType.equals("first")) {
						//获取当前时间，查看当前考勤表是否有未打卡的表项
						Date nowTime = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String now = formatter.format(nowTime);
						
						myStatement = "select * from checkin where startTime < '"+now+"' and endTime > '"+now+"' and userAccount = '"+userAccount+"' and state = '"+"未打卡"+"'";
						ResultSet rSet = DatabaseController.select(conn, myStatement);
						if(rSet.next()) {							
							myStatement = "select * from userMessage where userAccount = '"+userAccount+"' and type = 'check_time' Order by addTime DESC limit 1";
							rSet = DatabaseController.select(conn, myStatement);
							while(rSet.next()) {
								HashMap<String, String> hashMap = new HashMap<>();
								hashMap.put("type",rSet.getString("type"));
								hashMap.put("content",rSet.getString("content"));
								hashMap.put("addTime",rSet.getString("addTime"));
								hashMap.put("userAccount",rSet.getString("userAccount"));
								hashMap.put("subType",rSet.getString("subType"));
								hashMap.put("state",rSet.getString("state"));
								maps.add(hashMap);
							}
							myStatement = "select * from userMessage where userAccount = '"+userAccount+"' and state = '未读' and type<>'check_time'";
							ResultSet rs = DatabaseController.select(conn, myStatement);
							while(rs.next()) {
								HashMap<String, String> hashMap = new HashMap<>();
								hashMap.put("type",rs.getString("type"));
								hashMap.put("content",rs.getString("content"));
								hashMap.put("addTime",rs.getString("addTime"));
								hashMap.put("userAccount",rs.getString("userAccount"));
								hashMap.put("subType",rs.getString("subType"));
								hashMap.put("state",rs.getString("state"));
								maps.add(hashMap);
							}
						}
						result.put("result", maps);
						String rString = GsonUtils.toJson(result);
						out.print(rString);
						
						//把未读改已读
						changeUserState(conn, userAccount);
					}else {
					myStatement = "select * from userMessage where userAccount = '"+userAccount+"' and state = '未读'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					while(rSet.next()) {
						HashMap<String, String> hashMap = new HashMap<>();
						hashMap.put("type",rSet.getString("type"));
						hashMap.put("content",rSet.getString("content"));
						hashMap.put("addTime",rSet.getString("addTime"));
						hashMap.put("userAccount",rSet.getString("userAccount"));
						hashMap.put("subType",rSet.getString("subType"));
						hashMap.put("state",rSet.getString("state"));
						maps.add(hashMap);
					}
					result.put("result", maps);
					String rString = GsonUtils.toJson(result);
					out.print(rString);
					
					//把未读改已读
					changeUserState(conn, userAccount);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.print("false");
					e.printStackTrace();
				}
		}
		else if(type.equals("admin")){
			String adminAccount = request.getParameter("adminAccount");
			lock.lock();
			if(adminCount.keySet().contains(adminAccount)) {
				int a = adminCount.get(adminAccount);
				a++;
				adminCount.put(adminAccount, a);
			}else{
				adminCount.put(adminAccount,5);
			}
			lock.unlock();
			try {
				myStatement = "select * from adminMessage where adminAccount = '"+adminAccount+"' and state = '未读'";
				ResultSet rSet = DatabaseController.select(conn, myStatement);
				while(rSet.next()) {
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("type",rSet.getString("type"));
					hashMap.put("content",rSet.getString("content"));
					hashMap.put("addTime",rSet.getString("addTime"));
					hashMap.put("adminAccount",rSet.getString("adminAccount"));
					hashMap.put("subType",rSet.getString("subType"));
					hashMap.put("state",rSet.getString("state"));
					maps.add(hashMap);
				}
				result.put("result", maps);
				String rString = GsonUtils.toJson(result);
				out.print(rString);
				
				//把未读改已读
				changeAdminState(conn, adminAccount);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("false");
				e.printStackTrace();
			}
		}else if(type.equals("user_init_message")) {
			String userAccount = request.getParameter("userAccount");
			String limit = request.getParameter("limit");
			myStatement = "select * from adminMessage where userAccount = '"+userAccount+"' and state = '已读' LIMIT "+limit;
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				while(rSet.next()) {
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("type",rSet.getString("type"));
					hashMap.put("content",rSet.getString("content"));
					hashMap.put("addTime",rSet.getString("addTime"));
					hashMap.put("adminAccount",rSet.getString("adminAccount"));
					hashMap.put("subType",rSet.getString("subType"));
					hashMap.put("state",rSet.getString("state"));
					maps.add(hashMap);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.put("result", maps);
			String rString = GsonUtils.toJson(result);
			out.print(rString);
		}
		else if(type.equals("admin_init_message")) {
			String adminAccount = request.getParameter("adminAccount");
			String limit = request.getParameter("limit");
			myStatement = "select * from adminMessage where adminAccount = '"+adminAccount+"' and state = '已读' limit "+limit;
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				while(rSet.next()) {
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("type",rSet.getString("type"));
					hashMap.put("content",rSet.getString("content"));
					hashMap.put("addTime",rSet.getString("addTime"));
					hashMap.put("adminAccount",rSet.getString("adminAccount"));
					hashMap.put("subType",rSet.getString("subType"));
					hashMap.put("state",rSet.getString("state"));
					maps.add(hashMap);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.put("result", maps);
			String rString = GsonUtils.toJson(result);
			out.print(rString);
		}
		}
	public boolean changeAdminState(Connection conn,String adminAccount) {
		String myStatement = "update adminmessage set state = '已读' where adminAccount = '"+adminAccount+"' and state = '未读' ";
		if (DatabaseController.excute(conn, myStatement)==0)
			return true;
		else {
			return false;
		}
	}
	public boolean changeUserState(Connection conn,String userAccount) {
		String myStatement = "update usermessage set state = '已读' where userAccount = '"+userAccount+"' and state = '未读' ";
		if (DatabaseController.excute(conn, myStatement)==0)
			return true;
		else {
			return false;
		}
	}
}

