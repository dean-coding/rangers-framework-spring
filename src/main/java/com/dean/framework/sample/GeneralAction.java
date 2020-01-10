package com.dean.framework.sample;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 普通
 *
 * @author fuhw/Dean
 * @date 2018-10-23
 */
public class GeneralAction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("处理Get请求。。。。。");
        PrintWriter out = resp.getWriter();
        out.println("Hello Servlet-Get");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("处理Post请求。。。。。");
        PrintWriter out = resp.getWriter();
        out.println("Hello Servlet-Post");
    }
}
