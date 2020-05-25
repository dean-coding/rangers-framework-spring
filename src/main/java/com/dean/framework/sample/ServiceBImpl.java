package com.dean.framework.sample;

import com.dean.framework.core.annotation.DkAutowired;
import com.dean.framework.core.annotation.DkService;

/**
 *
 * @author fuhw/Dean
 * @date 2020-05-18
 */
@DkService
public class ServiceBImpl implements ServiceB {

    @DkAutowired
    private ServiceA serviceA;

    @Override
    public String getName() {
        return "ServiceB";
    }

    @Override
    public String print() {
        return getName() + ": Hello" + serviceA.getName();
    }
}
