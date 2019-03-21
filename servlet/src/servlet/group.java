package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.crypto.provider.RSACipher;

import test.DatabaseController;

/**
 * Servlet implementation class group
 */
@WebServlet("/group")
public class group extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public group() {
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
		String userAccount = request.getParameter("userAccount");
		String adminAccount = request.getParameter("adminAccount");
		String myStatement = null;
		Connection conn = DatabaseController.getCon();

		String adminName = null;

		if (type.equals("join_group")) {
			ArrayList<String> other_members = new ArrayList<>();
			String userName = getUserName(userAccount, conn);
			myStatement = "insert into `group` values('" + userAccount + "','" + adminAccount + "')";
			if (DatabaseController.excute(conn, myStatement) == 0) {
				// 成员已经加入，通知其他所有成员与管理员新成员加入
				myStatement = "select userAccount from `group` where adminAccount = '" + adminAccount + "'";
				ResultSet rs = DatabaseController.select(conn, myStatement);
				try {
					while (rs.next()) {
						if (!rs.getString("userAccount").equals(userAccount)) {
							other_members.add(rs.getString("userAccount"));
						}
					} // end while
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // end try

				for (String member : other_members) {
					myStatement = "insert into usermessage values('user_group_message','"+userName+"_"+userAccount+"' ," + "str_to_date('" + DatabaseController.getNowTime()
							+ "','%Y-%m-%d %H:%i:%s'),'" + member + "' , 'other_join','未读')";
					if (DatabaseController.excute(conn, myStatement) != 0) {
						break;
					}
				}
				// 通知本人已加入分组
				adminName = getAdminName(adminAccount, conn);
				myStatement = "insert into usermessage values('user_group_message','"+adminName+"_"+adminAccount+"' ," + "str_to_date('"
						+ DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'" + userAccount
						+ "' , 'by_admin_agree','未读')";
				if (DatabaseController.excute(conn, myStatement) != 0) {

				}
				// 通知管理员
				myStatement = "insert into adminmessage values('admin_group_message','" + userName + "_" + userAccount
						+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
						+ adminAccount + "' , 'someone_join','未读')";
				if (DatabaseController.excute(conn, myStatement) != 0) {
					out.print("false");
				} // end if
			}
		} else if (type.equals("quit_group")) {
			String subType = request.getParameter("subType");
			adminAccount = other.getAdminAccount(userAccount, conn);
			// 从group删除该组员
			myStatement = "delete from `group` where userAccount = '" + userAccount + "'";
			if (DatabaseController.excute(conn, myStatement) != 0) {
				out.print("false");
				return;
			} // end if

			// 通知
			if (subType.equals("quit")) {
				String userName = getUserName(userAccount, conn);
				// 通知管理员
				myStatement = "insert into `adminmessage` values('admin_group_message','" + userName + "_" + userAccount
						+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
						+ adminAccount + "' , 'quit','未读')";
				if (DatabaseController.excute(conn, myStatement) != 0) {
					out.print("false");
				} else {
					out.print("true");
				} // end if
			} // end if
			else if (subType.equals("by_admin_delete")) {
				// 通知用户
				adminName = getAdminName(adminAccount, conn);
				myStatement = "insert into `usermessage` values('user_group_message','" + adminName + "_" + adminAccount
						+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
						+ userAccount + "' , 'by_admin_delete','未读')";
				if (DatabaseController.excute(conn, myStatement) != 0) {
					out.print("false");
				} else {
					out.print("true");
				} // end if
			}
		} else if (type.equals("invite")) {
			// 获取管理员名字;
			adminName = getAdminName(adminAccount, conn);
			if (adminName == null)
				return;
			// 管理员姓名放在content中
			myStatement = "insert into usermessage values('user_group_message','" + adminName + "_" + adminAccount
					+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
					+ userAccount + "' , 'by_admin_invite','未读')";
			if (DatabaseController.excute(conn, myStatement) != 0) {
				out.print("false");
			} // end if
			else
				out.print("true");
		} // end if
		else if (type.equals("disagree")) {
			// 获取管理员名字;
			adminName = getAdminName(adminAccount, conn);
			if (adminName.equals(""))
				return;
			// 管理员姓名放在content中
			myStatement = "insert into usermessage values('user_group_message','" + adminName + "_" + adminAccount
					+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
					+ userAccount + "' , 'by_admin_disagree','未读')";
			if (DatabaseController.excute(conn, myStatement) != 0) {
				out.print("false");
			} // end if
		} // end if
		else if (type.equals("apply_join")) {
			String userName = getUserName(userAccount, conn);
			if (userName.equals(""))
				return;
			// 管理员姓名放在content中
			myStatement = "insert into adminmessage values('admin_group_message','" + userName + "_" + userAccount
					+ "' ," + "str_to_date('" + DatabaseController.getNowTime() + "','%Y-%m-%d %H:%i:%s'),'"
					+ adminAccount + "' , 'apply_join','未读')";
			if (DatabaseController.excute(conn, myStatement) != 0) {
				out.print("false");
			} // end if
			else {
				out.print("true");
			}
		} // end if
		DatabaseController.closeConn(conn);

	}

	public static String getAdminName(String adminAccount, Connection conn) {
		// 找到管理员姓名
		String myStatement = null;
		myStatement = "select adminName from administrator where adminAccount = '" + adminAccount + "'";
		ResultSet rs = DatabaseController.select(conn, myStatement);
		try {
			if (rs.next()) {
				String adminName = rs.getString("adminName");
				return adminName;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getUserName(String userAccount, Connection conn) {
		// 找到管理员姓名
		String myStatement = null;
		myStatement = "select userName from user where userAccount = '" + userAccount + "'";
		ResultSet rs = DatabaseController.select(conn, myStatement);
		try {
			if (rs.next()) {
				String userName = rs.getString("userName");
				return userName;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
