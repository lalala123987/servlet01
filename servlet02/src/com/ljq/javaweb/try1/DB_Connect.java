package com.ljq.javaweb.try1;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB_Connect {
    public Connection connect(String path) throws SQLException, ClassNotFoundException, IOException {
        FileInputStream in = new FileInputStream(path);
        Properties prop = new Properties();
        prop.load(in);
        String driver=prop.getProperty("driver");
        String my_url=prop.getProperty("my_url");
        String my_user=prop.getProperty("my_user");
        String my_password=prop.getProperty("my_password");
        Class.forName(driver);
        return DriverManager.getConnection(my_url, my_user, my_password);
    }
}
