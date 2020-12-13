package com.billjc.race.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billjc.race.dao.EmployeeMapper;
import com.billjc.race.dao.EmployerMapper;
import com.billjc.race.pojo.Employee;
import com.billjc.race.pojo.Employer;
import com.billjc.race.service.ValidateService;
import com.billjc.race.util.ValidateXMLUtil;

/**
 * 用于service业务层具体实现
 *
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 */
@Service("validateService")
public class ValidateServiceImpl implements ValidateService{

	@Autowired
	private EmployeeMapper employeeDao;
	
	@Autowired
	private EmployerMapper employerDao;

	@Override
	public int validateXMLChange(String fileName) {
		List<String> fileList = new ArrayList<String>(); 
		if (fileName.indexOf(",") > -1) {
			String[] fileArray = fileName.split(",");
			for (int i=0; i<fileArray.length; i++) {
				fileList.add(fileArray[i].trim().replace("\\s*", ""));
			}
		} else {
			fileList.add(fileName.trim().replace("\\s*", ""));
		}
		int result = ValidateXMLUtil.validateXMLChange(fileList, 0);
		return result;
	}

	@Override
	public List<Employee> selectEmployeeInfo() {
		return employeeDao.selectEmployeeInfo();
	}
	
	@Override
	public List<Employer> selectEmployerData() {
		return employerDao.selectEmployerData();
	}
}
