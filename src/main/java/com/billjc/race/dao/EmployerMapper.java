package com.billjc.race.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.billjc.race.pojo.Employer;

@Mapper
public interface EmployerMapper {
	public List<Employer> selectEmployerData();
}
