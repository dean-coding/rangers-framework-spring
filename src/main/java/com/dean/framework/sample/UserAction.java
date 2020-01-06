package com.dean.framework.sample;

import com.dean.framework.core.annotation.DkAutowired;
import com.dean.framework.core.annotation.DkController;
import com.dean.framework.core.annotation.DkRequestMapping;
import com.dean.framework.core.annotation.DkRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fuhw/Dean
 * @date 2018-10-23
 */
@DkController
@DkRequestMapping("/sample")
public class UserAction {

    @DkAutowired
    private UserService userService;
    @DkRequestMapping("/add.do")
    public void addUser(@DkRequestParam("name") String name,
                        HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().write(userService.add(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DkRequestMapping("/remove.do")
    public void removeUser(@DkRequestParam("name") String name,
                           HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().write(userService.remove(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DkRequestMapping("/query.do")
    public void query(@DkRequestParam("name") String name,
                      HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().write(userService.query(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
