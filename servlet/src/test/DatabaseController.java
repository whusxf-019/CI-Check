package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class DatabaseController {

    //�������ݿ�
    public static Connection getCon(){
        Connection con = null;
//        String user="root";
//        String password="Wonder123";
//        String className="com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://localhost:3306/db?useUnicode=true&characterEncoding=utf8";
//        try {
//            Class.forName(className);
//            con= DriverManager.getConnection(url,user,password);
//            if(con != null) {}
//        } catch (Exception e) {
//        	System.out.println("数据据连接失败");
//            e.printStackTrace();
//        }
        Context ctx;
		try {
			ctx = new InitialContext();
	        //参数java:/comp/env为固定路径   
	        Context envContext = (Context)ctx.lookup("java:/comp/env"); 
	        //参数jdbc/mysqlds为数据源和JNDI绑定的名字
	        DataSource ds = (DataSource)envContext.lookup("jdbc/mysqlds"); 
	        con = ds.getConnection();   

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        if(con != null) {
            return con;
        }
        else
            return null;
    }

    //����ɾ����
    public static int excute(Connection conn,String statement){
        try {
            Statement myStatement = conn.createStatement();
            myStatement.executeUpdate(statement);
            return 0;
        }catch (SQLException e){
            e.getMessage();
            return -1;
        }
    }

    //��
    public static ResultSet select(Connection conn,String statement){
        try {
            Statement myStatement = conn.createStatement();
            ResultSet rs = myStatement.executeQuery(statement);
            return rs;
        }catch (SQLException e){
            e.getMessage();
            return null;
        }
    }
    public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

    public static String getNowTime() {
		Date nowTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = formatter.format(nowTime);
		return now;
    }
}
