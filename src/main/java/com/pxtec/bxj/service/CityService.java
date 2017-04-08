package com.pxtec.bxj.service;

import com.github.pagehelper.PageHelper;
import com.pxtec.bxj.mapper.CityMapper;
import com.pxtec.bxj.model.City;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public List<City> getAll(City city) {
        if (city.getPage() != null && city.getRows() != null) {
            PageHelper.startPage(city.getPage(), city.getRows());
        }
        return cityMapper.selectAll();
    }

    public City getById(Integer id) {
        return cityMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 自定义的Mapper查询
     * @param name
     * @return
     * @author fuhw
     */
    public City findByNameDefine(String name) {
    	return cityMapper.findByNameDefine( name );
    }

    public void deleteById(Integer id) {
        cityMapper.deleteByPrimaryKey(id);
    }

    public City save(City city) {
        if (city.getId() != null) {
            cityMapper.updateByPrimaryKey(city);
        } else {
            cityMapper.insert(city);
        }
        return cityMapper.selectByPrimaryKey( city.getId() );
    }
    
    
}
