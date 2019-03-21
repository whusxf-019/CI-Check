package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.util.StringManager;
import org.apache.jasper.tagplugins.jstl.core.Out;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.recompile;

import Util.GsonUtils;
import test.DatabaseController;

/**
 * Servlet implementation class checkin
 */
@WebServlet("/checkin")
public class checkin extends HttpServlet {
	
	//计时器放在哈希表中，可以开关特定的计时器,键值为adminAccount_setStartTime
	private		HashMap<String, Timer> timers = new HashMap<>();	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkin() {
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
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Connection conn = DatabaseController.getCon();
		String type = request.getParameter("type");
		//每天检查一次定时器
		if(type.equals("set_check_time")) {
			char[] dates = request.getParameter("week").toCharArray();
			String adminAccount = request.getParameter("adminAccount");
			String startTime =  URLDecoder.decode(request.getParameter("startTime"),"utf-8");
			String endTime =  URLDecoder.decode(request.getParameter("endTime"),"utf-8");
			String address = URLDecoder.decode(request.getParameter("address"),"utf-8");

			
			//向checkitimetable中添加定时器记录
			String statement = "insert into CheckTimeTable values('"+adminAccount+"', str_to_date('"+startTime+"','%H:%i:%s'),"
					+ "str_to_date('"+endTime+"','%H:%i:%s'), '"+address+"','"+request.getParameter("week")+"')";
			if(DatabaseController.excute(conn, statement)==-1) {
				out.print("false");
				return;
			}
			//每天检查一次定时器
			checkEveryDay(dates,adminAccount,startTime,endTime,address);
			out.print("true");
		}
		
		//史雪峰添加；删除checktimetable中的信息
		else if(type.equals("deleteinfo")) {
			String adminAccount = request.getParameter("adminAccount");
			String startTime =  URLDecoder.decode(request.getParameter("startTime"),"utf-8");
			String statement = "delete from CheckTimeTable where adminAccount = '"+adminAccount+"' AND setStartTime = '"+startTime+"'";
			if(DatabaseController.excute(conn, statement)==0) {
				Timer myTimer = timers.get(adminAccount+"_"+startTime);
				if(myTimer!=null) {
					myTimer.cancel();
				}
				out.print("true");
			}else {
				out.print("false");
			}
			
		}
		
		//更改checkin表数据，完成签到
		else if(type.equals("check_confirm")) {
			//更改checkin表数据，完成签到
			String userAccount = request.getParameter("userAccount");
			String endTime = request.getParameter("endTime");
			Date nowTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = formatter.format(nowTime);
			String myStatement = "update checkin  set checkedTime = str_to_date('"+now+"','%Y-%m-%d %H:%i:%s'),state = '已打卡' where userAccount = '"
			+userAccount+"' and endTime = '"+endTime+"'";
			if(DatabaseController.excute(conn, myStatement)==0) {
				out.print("true");
			}else {
				out.print("false");
			}//end if
		}//end if
		else if(type.equals("get_check_time_table")) {
			//返回checkTimeTable表中的记录
			String userAccount = request.getParameter("userAccount");
			String adminAccount = other.getAdminAccount(userAccount, conn);
			String myStatement = "select * from checktimetable where adminAccount = '"+adminAccount+"'";
			ResultSet rs = DatabaseController.select(conn, myStatement);
			ArrayList<HashMap<String, String>> result = new ArrayList<>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap();
			try {
				while(rs.next()) {
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("adminAccount", rs.getString("adminAccount"));
					hashMap.put("setStartTime",rs.getString("setStartTime"));
					hashMap.put("setEndTime", rs.getString("setEndTime"));
					hashMap.put("address",rs.getString("address"));
					hashMap.put("check_day_for_week",rs.getString("check_day_for_week"));
					result.add(hashMap);
				}
				result2.put("result",result);
				String jsonObject = GsonUtils.toJson(result2);
				out.print(jsonObject);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//end if
		//展示该管理员设置的定时器
		else if(type.equals("show_checktime_info")) {
			String adminAccount = request.getParameter("adminAccount");
			String myStatement = "select * from CheckTimeTable where adminAccount = '"+adminAccount+"'";
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap<>();
			try {
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			while(rSet.next()){
				HashMap<String, String> user_leave = new HashMap<String, String>();
				user_leave.put("adminAccount",rSet.getString("adminAccount"));
				user_leave.put("address",rSet.getString("address"));
				user_leave.put("setStartTime",rSet.getString("setStartTime"));
				user_leave.put("setEndTime",rSet.getString("setEndTime"));
				user_leave.put("check_day_for_week",rSet.getString("check_day_for_week"));
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
		}
		
		//查找考勤记录
		else if(type.equals("get_check_info_by_user")) {
			String userAccount = request.getParameter("userAccount");
			String day = request.getParameter("day");
			String myStatement = "select checkedTime,state,adminAccount from checkin where userAccount = '"+userAccount+"' and DATE_FORMAT(checkedTime,'%Y-%m-%d') = DATE_FORMAT('"+day+"','%Y-%m-%d') ";
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap<>();
			try {
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			while(rSet.next()){
				HashMap<String, String> user_leave = new HashMap<String, String>();
				user_leave.put("adminAccount",rSet.getString("adminAccount"));
				user_leave.put("checkedTime",rSet.getString("checkedTime"));
				user_leave.put("state",rSet.getString("state"));
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
		}
		//查找考勤记录
		else if(type.equals("getCheckInfoByAdminForDay")) {
			String adminAccount = request.getParameter("adminAccount");
			String day = request.getParameter("day");//年月日时分秒
			String myStatement = "select checkedTime,state,userAccount " + 
					"from checkin " + 
					"where DATE_FORMAT(checkedTime,'%Y-%m-%d') = DATE_FORMAT('"+day+"','%Y-%m-%d') and adminAccount = '"+adminAccount+"'";
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, ArrayList<HashMap<String, String>>> result2 = new HashMap<>();
			try {
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			while(rSet.next()){
				HashMap<String, String> user_leave = new HashMap<String, String>();
				user_leave.put("userAccount",rSet.getString("userAccount"));
				user_leave.put("checkedTime",rSet.getString("checkedTime"));
				user_leave.put("state",rSet.getString("state"));
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
		}
    	DatabaseController.closeConn(conn);
	}//end post
	
	public void checkEveryDay(char[] dates,String adminAccount,String startTime,String endTime,String address) {
		  //
		  // Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)

		
			Date now = new Date();
			Calendar calendarstart11 = null;//该变量为外部类使用
			try {
				calendarstart11 = changeTime(startTime);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}
		    Timer timer = new Timer();
		    timers.put(adminAccount+"_"+startTime,timer);
		    System.out.println(calendarstart11.getTime());
		    System.out.println(now);
		    timer.scheduleAtFixedRate(new TimerTask() {
		      public void run() {
		        System.out.println("计时器启动");
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date());
		        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
		        if(day == 0){  
		        	day = 7;  
		        }  
		        for(char a : dates) {
		        		if((day+48) == a) {
		        			Calendar calendarstart = null;
		        			Calendar calendarend = null;
			        			try {
			        			    //在定时器中update_checkin_table中会忽略
			        				//当前时间在startTime和endTime之间的情况
			        			    //下面代码来修复这个漏洞
			        				calendarend = changeTime(endTime);
			        				calendarstart = changeTime(startTime);
			        				if(calendarstart.getTime().before(now)&&calendarend.getTime().after(now)) {
					        			update_checkin_table(adminAccount,startTime,endTime,address);
			        				}else if(calendarend.getTime().before(now)){
										return;
									}else {
					        			update_checkin_table(adminAccount,startTime,endTime,address);
									}
			        			} catch (ParseException e) {
			        				// TODO Auto-generated catch block
			        				e.printStackTrace();
			        				return;
			        			}
		        			break;
		        		}//end if
		        	}//end for
		      	}//end run
		    }, calendarstart11.getTime(), 1000*60*60*24);// 每天检查一次
		    	    //1000 * 60 * 60 * 24
	}
	
	public void update_checkin_table(String adminAccount,String startTime,String endTime,String address) {
		//先将endtime和startTime时间补上当前的年月日
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = null;
		String end = null;
		try {
			start = formatter.format(changeTime(startTime).getTime());
			end = formatter.format(changeTime(endTime).getTime());
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}
		
		
		ArrayList<String> left_users = new ArrayList<>();
		Date nowTime = new Date();
		String now = formatter.format(nowTime);
		
		//将当前管理员下所有用户选出
		ArrayList<String> users = new ArrayList<String>();
		String myStatement = null;
		myStatement = "select userAccount from `group` where "
				+ "adminAccount = '"+adminAccount+"'";
		Connection conn1 = DatabaseController.getCon();
		ResultSet rs = DatabaseController.select(conn1, myStatement);
		if(rs==null) return;
		try {
			while (rs.next()) {
				users.add(rs.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//查看请假表
		
		myStatement = "select p.userAccount from `group` p,`leave` l where p.userAccount = l.userAccount"
				+ " And p.adminAccount = '"+adminAccount+"' And l.state = '成功' And endTime > '"
							+now+"' And startTime < '"+now+"'";
		rs = DatabaseController.select(conn1, myStatement);
		try {
			while (rs.next()) {
				left_users.add(rs.getString(1));			
			}
		}catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		//向checkin表中添加每个成员的
		for(String x : left_users) {
			myStatement = "Insert into checkin(userAccount,adminAccount,startTime,endTime,state,checkedTime) values"
					+ "('"+x+"','"+adminAccount+"', str_to_date('"+start+"','%Y-%m-%d %H:%i:%s'),str_to_date('"+end+"','%Y-%m-%d %H:%i:%s'),"
					+"'请假',str_to_date('"+now+"' , '%Y-%m-%d %H:%i:%s')) ";
			if(DatabaseController.excute(conn1, myStatement)!=0)
				break;
		}
		for(String x : users) {
			myStatement = "Insert into checkin(userAccount,adminAccount,startTime,endTime,state,checkedTime) values"
					+ "('"+x+"','"+adminAccount+"', str_to_date('"+start+"','%Y-%m-%d %H:%i:%s'),str_to_date('"+end+"','%Y-%m-%d %H:%i:%s'),"
					+"'未打卡',str_to_date('"+now+"', '%Y-%m-%d %H:%i:%s')) ";
			if(DatabaseController.excute(conn1, myStatement)!=0)
				continue;
		}
		
		//通知用户,把消息放到用户消息表
		for(String x: users) {
			myStatement = "Insert into userMessage values('check_time','"+end+"_"+address+"_"+start+"',str_to_date('"+now+"','%Y-%m-%d %H:%i:%s'),'"+x+"','check_time','未读' )";
			DatabaseController.excute(conn1, myStatement);
		}
		DatabaseController.closeConn(conn1);
	}
	
	//将时分秒转换为当前年月日时分秒
	public Calendar changeTime(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		Date timeStart = new java.sql.Time(sdf.parse(date).getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,timeStart.getHours());
		calendar.set(Calendar.MINUTE,timeStart.getMinutes());
		calendar.set(Calendar.SECOND,timeStart.getSeconds());
		System.out.println(calendar.getTime());
		return calendar;
	}
}

