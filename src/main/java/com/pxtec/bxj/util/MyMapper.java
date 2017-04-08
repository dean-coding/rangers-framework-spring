package com.pxtec.bxj.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自定义mapper接口，继承通用mapper (该接口不能被扫描到，否则会出错)
 * @param <T>
 * @version 1.0
 * @author fuhw
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {}
