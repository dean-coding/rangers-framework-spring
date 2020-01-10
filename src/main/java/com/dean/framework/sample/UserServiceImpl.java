package com.dean.framework.sample;

import com.dean.framework.core.annotation.DkService;

/**
 * @author fuhw/Dean
 * @date 2018-10-23
 */
@DkService
public class UserServiceImpl implements UserService {

    public String add(String name) {
        return "You add user : name= " + name;
    }

    public String remove(String name) {
        return "You remove user : name=" + name;
    }

    public String query(String name) {
        return "You query user : name=" + name;
    }
}
