package com.billjc.race.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.billjc.race.pojo.Employee;
import com.billjc.race.service.ValidateService;
/**
 * 用于动态校验XML文件，返回校验结果
 *
 * @author  swallow
 * @version 1.0
 * @since   2020/12/5
 */
@Controller
@RequestMapping("/api") 
public class DynamicValiController{
	@Autowired
	private ValidateService validateService;

	/**
	 * 校验XML文件是否发生变更
	 * 1.发生变更--重新加载XML配置文件; 2.未发生变更--返回未发生变更信息; 3.执行出错--返回错误信息
	 *
	 * @param request  请求信息
	 * @param response 返回信息
	 * @return result  校验结果
	 * @throws IOException IO流异常
	 */
	@RequestMapping(value="/dynamic/validateXml",method=RequestMethod.GET)  
	public String validateXMLChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String fileName = request.getParameter("id").trim().replace("\\s*", "");
    	if (StringUtils.isEmpty(fileName)) {
    		response.getWriter().write("参数输入错误！");
    		return "error";
    	} else {
    		int result = validateService.validateXMLChange(fileName);
    	    if (result == 1) {
    	    	return "index"; 
    	    } else if (result == 0) {
    	    	return "noChange";
    	    } else {
    	    	return "error";
    	    }
    	}
	 }

    /**
     * 结果集显示
     *
	 * @param request  请求信息
	 * @param response 返回信息
     * @return result  结果集数据
     * @throws IOException 输出流异常
     */
    @RequestMapping(value="/dynamic/data",method=RequestMethod.GET)  
	public String dataInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	try{
    		List<Employee> dataList = validateService.selectEmployeeInfo();
        	response.getWriter().write("<h3>data is:</br>");
        	for (int i=0; i<dataList.size(); i++) {
        		Employee record = dataList.get(i);
        		response.getWriter().write(
        				setNullToString(record.getId())+"&nbsp;"+
        				"name:&nbsp;"+setNullToString(record.getName())+"&nbsp;"+
        				"&nbsp;age:&nbsp;"+setNullToString(record.getAge()));
        		response.getWriter().write("</br>");
        	}
        	response.getWriter().write("</h3>");
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "error";
    	}
    	
    	return "record";
	 }

    /**
     * null值转换string
     *
     * @param obj 传参对象
     * @return string 字符串返回
     */
    private Object setNullToString(Object obj){
    	if (obj == null || StringUtils.isEmpty(obj)) {
    		return "";
    	}
    	return obj.toString();
    }
}