package cn.focus.crawl.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
    public static Connection getConnection()
    {
        Connection con = null;  //创建用于连接数据库的Connection对象  
        try {  
            Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动  
              
            con = DriverManager.getConnection(  
                    "jdbc:mysql://10.10.90.156:3306/crawl_data_test", "develop", "p3m12d");// 创建数据连接  
              
        } catch (Exception e) {  
            System.out.println("数据库连接失败" + e.getMessage());  
        }
        System.out.println("success!");
        return con; //返回所建立的数据库连接  
    }
    public static void main(String[] args){
        DBHelper.getConnection();
    }
}
