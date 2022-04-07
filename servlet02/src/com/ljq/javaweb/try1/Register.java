package com.ljq.javaweb.try1;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Objects;


public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"),"/servlet02/register.html");
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
        if (SpiderState==0){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username.length() > 255) {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print("<br><h1 style=\"text-align:center;font-size:2.5em;\">账号过长，请重新输入</h1>");
            } else if (username.length() <= 2) {
                response.sendRedirect("register.html");
            } else if (password.length() < 6) {
                response.sendRedirect("register.html");
            } else if (password.length() > 255) {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print("<br><h1 style=\"text-align:center;font-size:2.5em;\">密码过长，请重新输入</h1>");
            } else {
                int register_state;
                try {
                    String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");
                    DB_Write try01 = new DB_Write(username, password, path);
                    register_state = try01.write();
                    switch (register_state) {
                        case 0 -> response.sendRedirect("ServerError.html");
                        case 1 -> response.sendRedirect("UsernameExist.html");
                        case 2 -> response.sendRedirect("SignUpSuccess.html");
                        case 3 -> response.sendRedirect("SignUpFail.html");
                    }
                } catch (Exception e) {
                    PrintWriter out = response.getWriter();
                    out.print("<br><h1 style=\"text-align:center;font-size:2.5em;\">配置文件出错，请检查配置文件</h1>");
                }
            }
        }
    }
}







