package com.dean.framework.sample;

import com.dean.framework.core.annotation.DkAutowired;
import com.dean.framework.core.annotation.DkService;

/**
 * TODO
 *
 * @author fuhw/Dean
 * @date 2020-05-18
 */
@DkService
public class ServiceAImpl implements ServiceA {

    @DkAutowired
    private ServiceB serviceB;

    @Override
    public String getName() {
        return "ServiceA";
    }

    @Override
    public String print() {
        return getName() + ": Hello" + serviceB.getName();
    }
}
