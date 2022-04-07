package com.ljq.javaweb.try1;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;

public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"),"/servlet02/login.html");
            if (uAC.check()){
                response.sendError(403,"通过user-agent检测到爬虫");
                SpiderState = 1;
            }
            else if (RC.check()){
                response.sendError(403,"通过referer检测到爬虫");
                SpiderState = 1;
            }
        } catch (NullPointerException e) {
            response.sendError(403,"检测到爬虫");
            SpiderState = 1;
        }
        if (SpiderState==0) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");
                Username_Check check = new Username_Check(username, path);
                if (check.check()) {
//                    ResourceBundle bundle = ResourceBundle.getBundle("DB_Info");
//                    String driver = bundle.getString("driver");
//                    Class.forName(driver);
                    DB_Connect connect = new DB_Connect();
                    conn = connect.connect(path);
                    String sql = "select*from userinfo where username=? and password=?";//?是占位符
                    ps = conn.prepareStatement(sql);
                    //给占位符？传值，第一个问号下标是1，jdbc的下标从1开始
                    ps.setString(1, username);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/html");
                        PrintWriter out = response.getWriter();
                        out.print("<br><h1 style=\"text-align:center;font-size:2.5em;\">登录成功</h1>");
                    } else {
                        response.sendRedirect("wrongpassword.html"); //重定向到密码错误页面
                    }
                } else {
                    response.sendRedirect("wrongusername.html"); //重定向到账号错误页面
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
        }

    }
}
