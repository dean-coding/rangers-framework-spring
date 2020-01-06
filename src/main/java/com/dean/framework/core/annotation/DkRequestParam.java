package com.dean.framework.core.annotation;

import java.lang.annotation.*;

/**
 *
 * @author fuhw/Dean
 * @date 2018-10-29
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DkRequestParam {

    String value() default "";

}
