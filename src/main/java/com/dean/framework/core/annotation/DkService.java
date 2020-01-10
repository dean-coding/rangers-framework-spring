package com.dean.framework.core.annotation;

import java.lang.annotation.*;

/**
 * @author fuhw/Dean
 * @date 2018-10-29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DkService {

    String value() default "";
}
