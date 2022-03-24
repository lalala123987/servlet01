package com.ljq.javaweb.try1;

import java.io.IOException;
import java.sql.*;

public class Username_Check {
    String username;
    String path;

    public Username_Check(String username,String path) {
        this.username = username;
        this.path = path;
    }

    public final boolean check() throws SQLException {
        //如果数据库中存在该用户，则返回True，如果不存在，则返回Flase
        Connection conn = null;
        boolean ans = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            DB_Connect connect = new DB_Connect();
            conn = connect.connect(path);
            String sql = "SELECT * FROM userinfo WHERE UserName=?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            ans = rs.next();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return ans;
    }
}
