package com.billjc.race.init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.billjc.race.util.ConfigClientUtil;
/**
 * 初始化启动
 * 
 * @author  swallow
 * @version 1.0
 * @since   2020/12/5
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.billjc.race.controller*,com.billjc.race.service*"})
@MapperScan("com.billjc.race.dao*")
@Import({com.billjc.race.util.SpringUtils.class,com.billjc.race.util.XMLScheduleTask.class})
@EnableScheduling
public class SpringBootApplicationStart extends SpringBootServletInitializer {

	public static void main(String[] args){
		SpringApplication.run(SpringBootApplicationStart.class, args);
//		ConfigClientUtil.getConfigCenterValue();
	}
}
