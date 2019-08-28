package com.mip.biz.oms.yf_sync.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mip.core.basebiz.service.ServiceRuntimeException;

public class DBUtils {
    private static Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static Connection getConnection(){
        PropertiesUtil propertiesUtil = new PropertiesUtil();

        propertiesUtil.loadFile("config/mysql-db.properties");
        String driver = propertiesUtil.getPropertyValue("driver");
        String url = propertiesUtil.getPropertyValue("url");
        String username  = propertiesUtil.getPropertyValue("username");
        String password = propertiesUtil.getPropertyValue("password");

        try {
            //初始化驱动类com.mysql.jdbc.Driver
            Class.forName(driver);
            return DriverManager.getConnection(url,username, password);
            //该类就在 mysql-connector-java-5.0.8-bin.jar中,如果忘记了第一个步骤的导包，就会抛出ClassNotFoundException
        } catch (Exception e) {
        	throw new ServiceRuntimeException("获取Mysql连接错误",e);
        }
    }

    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            }catch (Exception e){
            	throw new ServiceRuntimeException("关闭Mysql连接错误",e);
            }
        }
    }

}
