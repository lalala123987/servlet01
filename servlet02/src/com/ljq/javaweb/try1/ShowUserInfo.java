package com.ljq.javaweb.try1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ShowUserInfo extends ViewBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);//不创建新ID
        if (session != null) { //判断session是否存在
            if (!session.isNew()) {//判断session是否新的，但似乎无用
                Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/servlet02/login.html");
                if (!RC.check()) {  //验证来源链接
                    if (session.getAttribute("username") != null) {
                        super.processTemplate("UserInfo", request, response);
                    }
                }
            }
        } else response.sendError(403, "禁止访问");
    }
}

