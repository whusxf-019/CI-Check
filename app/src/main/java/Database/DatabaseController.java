package Database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {

    //连接数据库
    public static Connection getCon(){
        Connection con = null;
        String user="whudb%shixuefeng";
        String password="Shixuefeng123";
        String className="com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://whudb.mysqldb.chinacloudapi.cn:3306/db";
        try {
            Class.forName(className);
            con= DriverManager.getConnection(url,user,password);
            if(con != null)
                System.out.println("创建数据库连接成功！");
        } catch (Exception e) {
            Log.e("DB","创建数据库连接失败！");
            e.printStackTrace();
        }
        if(con != null)
            return con;
        else
            return null;
    }

    //增、删、改
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

    //查
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




}
