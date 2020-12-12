package com.billjc.race.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.billjc.race.pojo.Employee;

@Mapper
public interface EmployeeMapper {
	
	public List<Employee> selectEmployeeInfo();

}
