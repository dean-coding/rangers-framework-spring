package com.pxtec.bxj.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxtec.bxj.model.City;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityMapperTest {

	@Autowired
	private CityMapper cityMapper;
	
	@Before
	public void setUp() throws Exception{}
	
	@After
	public void setAfter() throws Exception{
		Example example = new Example( City.class );
		Criteria criteria = example.createCriteria();
		criteria.andGreaterThan( "id", 0 );
		cityMapper.deleteByExample( example );
	}

	/**
	 * 测试mapper.xml中自定义的查询 
	 * @author fuhw
	 */
	@Test
	public void testFindByNameDefine(){
		String name = "辽宁";
		City city = new City(name,"东北省");
	    cityMapper.insertSelective(city);
		
		City findByNameDefine = cityMapper.findByNameDefine( name );
		assertNotNull( findByNameDefine );
		assertEquals( findByNameDefine.getName(),name );
	}
	
	/**
	 * 测试通用mapper
	 * 插入，分页查询
	 * @author fuhw
	 */
	@Test
	public void testFindByCommon() {
		List<City> cities = new ArrayList<City>();
		for(int i=0;i<15;i++) {
			cities.add( new City( "china", "number" + i ) );
		}
		cityMapper.insertList( cities );
		
		PageHelper.startPage( 0, 10 );
		List<City> selectAll = cityMapper.selectAll();
		
		assertNotNull( selectAll );
		assertEquals( selectAll.size(), 10 );
		
		PageInfo<City> pageInfo = new PageInfo<>( selectAll );
		assertEquals( pageInfo.getPages(), 2 );
		assertEquals( pageInfo.getTotal(), 15 );
	}
}
