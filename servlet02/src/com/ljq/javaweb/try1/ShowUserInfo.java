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
        HttpSession session = request.getSession(false);//TODO:不创建ID,尚不知是否可以伪造ID，如果能，可绕过
        //if (!session.isNew()){//TODO 此处需要删除程序才能运行，但是删除后会有安全隐患，可以伪造refer后再使用新session并使用post直接访问即可修改数据
        Referer_Check RC = new Referer_Check(request.getHeader("referer"),"/servlet02/login.html");
        if (!RC.check()){  //验证来源链接
            if (session == null){
                response.sendRedirect("403.html");
            }else {
                if (session.getAttribute("username")==null) { //TODO 此处逻辑有问题，如果是未登录用户
                    System.out.println("9:"+session.getId());
                    try {
                        session.setAttribute("username",request.getParameter("username"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.processTemplate("UserInfo",request,response);
                    System.out.println("arrive");
                }
                else { //TODO:实际已登录用户也无法到达此界面，因为需要通过SUI的渲染
                    System.out.println("Username"+session.getAttribute("username"));
                    System.out.println("getUN:"+request.getParameter("username"));
                    if (session.getAttribute("username").equals(request.getParameter("username"))){
                        System.out.println("10");
                        super.processTemplate("UserInfo",request,response);
                        System.out.println("arrive");
                    }
                    else response.sendRedirect("403.html");
                }
            }
        }
        else response.sendRedirect("403.html");
    }
    //else response.sendError(403,"禁止访问");
    //}
}

