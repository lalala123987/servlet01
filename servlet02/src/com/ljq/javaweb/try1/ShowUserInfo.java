package com.ljq.javaweb.try1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ShowUserInfo extends ViewBaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        System.out.println("6:"+session.getId());
        if (!session.isNew()){//TODO 此处需要删除程序才能运行，但是删除后会有安全隐患，可以伪造refer后再使用新session并使用post直接访问即可修改数据
            System.out.println("7:"+session.getId());
            Referer_Check RC = new Referer_Check(request.getHeader("referer"),"/servlet02/login.html");
            if (!RC.check()){  //验证来源链接
                System.out.println("8:"+session.getId());
                if (session.getAttribute("username")==null) { //验证是否是已初始化的session
                    System.out.println("9:"+session.getId());
                    try {
                        session.setAttribute("username",request.getParameter("username"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    response.sendRedirect("UserInfo.html");
                    super.processTemplate(request,response);//TODO 为何只有两个参数，下一行理应去除，或者考虑先重定向再渲染

                }
                else {
                    System.out.println("Username"+session.getAttribute("username"));
                    System.out.println("getUN:"+request.getParameter("username"));
                    if (session.getAttribute("username").equals(request.getParameter("username"))){
                        super.processTemplate(request,response);
                        response.sendRedirect("UserInfo.html");
                    }
                    else response.sendError(404,"禁止访问");
                }
            }
            else response.sendError(404,"禁止访问");
        }
        else response.sendError(403,"禁止访问");
    }
}

