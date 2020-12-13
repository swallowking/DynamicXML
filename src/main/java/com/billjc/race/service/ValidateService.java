package com.billjc.race.service;

import java.util.List;

import com.billjc.race.pojo.Employee;

/**
 * @category service层业务接口
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 */
public interface ValidateService {
	
	public int validateXMLChange(String fileName);
	
	public List<Employee> selectEmployeeInfo();
	
}
