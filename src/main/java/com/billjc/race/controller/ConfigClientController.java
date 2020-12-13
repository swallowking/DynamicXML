package com.billjc.race.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 获取配置中心Mapper文件存放路径
 *
 * @author swallow
 * @version 1.0
 * @since   2020/12/7
 */
@RestController
@RefreshScope
@RequestMapping("/api")
public class ConfigClientController {

	private static Log log  = LogFactory.getLog(ConfigClientController.class);

//	@Value("${mapperFilePath}")
    private String configValue;

    /**
     * 返回配置文件的值
     */
    @GetMapping("/config")
    public String returnConfigValue(){
    	log.info("### get config value ###");
    	return configValue;
    }
}
