package com.billjc.race.service;

import java.util.List;

import com.billjc.race.pojo.Employee;
import com.billjc.race.pojo.Employer;

/**
 * @category service业务层接口
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 */
public interface ValidateService {
	
	public int validateXMLChange(String fileName);
	
	public List<Employee> selectEmployeeInfo();
	
	public List<Employer> selectEmployerData();
	
}
