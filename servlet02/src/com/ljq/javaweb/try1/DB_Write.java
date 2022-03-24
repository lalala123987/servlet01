package com.ljq.javaweb.try1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB_Write {
    String username;
    String password;
    String path;

    public DB_Write(String username, String password, String path) {
        this.username = username;
        this.password = password;
        this.path = path;
    }

    public final int write() {
        //如果报错，返回0；如果用户名已存在，返回1；如果注册成功，返回2；如果注册失败，返回3
        int ans = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            DB_Connect connect = new DB_Connect();
            conn = connect.connect(path);

            //检查用户名是否已经存在
            Username_Check check = new Username_Check(username, path);
            if (check.check()) {
                ans = 1;
            } else {
                String sql = "INSERT IGNORE INTO userinfo (UserName,Password) VALUES (?,?);";
                ps = conn.prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, password);
                int count = ps.executeUpdate();

                ans = (count == 1 ? 2 : 3);
            }

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {//释放
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ans;
    }
}