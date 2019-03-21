package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.DatabaseController;

/**
 * Servlet implementation class report
 */
@WebServlet("/report")
public class report extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public report() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String type = request.getParameter("type");
		String myStatement = null;
		Connection conn = DatabaseController.getCon();

		// 未打卡人数
		if (type.equals("admin_daily_report_unchecked")) {
			String adminAccount = request.getParameter("adminAccount");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') and state = '未打卡'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			// 把数据打包成json，未写完
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 已打卡人数
		else if (type.equals("admin_daily_report_checked")) {
			String adminAccount = request.getParameter("adminAccount");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') and state = '已打卡'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			// 把数据打包成json，未写完
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// 总人数
		else if (type.equals("admin_daily_report_all")) {
			String adminAccount = request.getParameter("adminAccount");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') ";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			// 把数据打包成json，未写完
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}


		// 月表总人数
		else if (type.equals("admin_monthly_report_all")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m') ='"+month+"'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// 月表打卡人数
		else if (type.equals("admin_monthly_report_checked")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m') = '"+month+"'and state='已打卡'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 月表未打卡人数
		else if (type.equals("admin_monthly_report_unchecked")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(state) as count from checkin where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(checkedTime,'%Y-%m') = '"+month+"'and state='未打卡'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表病假人数
		else if (type.equals("admin_monthly_report_ill")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '病假'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表婚假人数
		else if (type.equals("admin_monthly_report_marry")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '婚假'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表丧假人数
		else if (type.equals("admin_monthly_report_die")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '丧假'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表事假人数
		else if (type.equals("admin_monthly_report_something")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '事假'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表产假人数
		else if (type.equals("admin_monthly_report_born")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '产假'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表其他请假类型人数
		else if (type.equals("admin_monthly_report_other")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' and leave_type = '其他'";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//月表请假总人数
		else if (type.equals("admin_monthly_report_leave_all")) {
			String adminAccount = request.getParameter("adminAccount");
			String month = request.getParameter("month");

			myStatement = "select count(userAccount) as count from `leave` where adminAccount = '" + adminAccount
					+ "' and DATE_FORMAT(startTime,'%Y-%m') = '"+month+"'and state='成功' ";
			ResultSet rSet = DatabaseController.select(conn, myStatement);
			try {
				if (rSet.next()) {
					out.print(rSet.getString("count"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// 用户报表总人数
				else if (type.equals("user_report_all")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");


					myStatement = "select count(state) as count from checkin where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') between '"+startTime+"' and '"+endTime+"'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

				}
				
					// 用户报表打卡人数
				else if (type.equals("user_report_checked")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");


					myStatement = "select count(state) as count from checkin where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') between '"+startTime+"' and '"+endTime+"' and state = '已打卡'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

				}
				
					// 用户报表未打卡人数
				else if (type.equals("user_report_unchecked")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");


					myStatement = "select count(state) as count from checkin where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(checkedTime,'%Y-%m-%d') between '"+startTime+"' and '"+endTime+"' and state = '未打卡'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

				}
				
				//用户报表病假人数
				else if (type.equals("user_report_ill")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '病假'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表婚假人数
				else if (type.equals("user_report_marry")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '婚假'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表丧假人数
				else if (type.equals("user_report_die")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '丧假'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表事假人数
				else if (type.equals("user_report_something")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '事假'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表产假人数
				else if (type.equals("user_report_born")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '产假'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表其他请假类型人数
				else if (type.equals("user_report_other")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' and leave_type = '其他'";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//用户报表请假总人数
				else if (type.equals("user_report_leave_all")) {
					String userAccount = request.getParameter("userAccount");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");

					myStatement = "select count(userAccount) as count from `leave` where userAccount = '" + userAccount
							+ "' and DATE_FORMAT(startTime,'%Y-%m-%d') between '"+startTime+"'and '"+endTime+"' and state='成功' ";
					ResultSet rSet = DatabaseController.select(conn, myStatement);
					try {
						if (rSet.next()) {
							out.print(rSet.getString("count"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

		DatabaseController.closeConn(conn);
	}
}
