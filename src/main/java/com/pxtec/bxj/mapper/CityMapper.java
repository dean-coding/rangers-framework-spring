package com.pxtec.bxj.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pxtec.bxj.model.City;
import com.pxtec.bxj.util.MyMapper;

/**
 * dao层继承自定义通用mapper 
 * @version 1.0
 * @author fuhw
 */
@Mapper
public interface CityMapper extends MyMapper<City> {
	
	City findByNameDefine(String name);
}
