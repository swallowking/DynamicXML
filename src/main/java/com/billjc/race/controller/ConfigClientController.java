package com.billjc.race.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 获取配置中心配置mapper文件路径
 *
 * @author swallow
 * @version 1.0
 * @since   2020/12/7
 */
@RestController
@RefreshScope
@RequestMapping("/api")
public class ConfigClientController {
//	@Value("${xmlPath}")//mapperFilePath
//    private String configValue;
//
//    /**
//     * 返回配置文件的值
//     */
//    @GetMapping("/config")
//    @ResponseBody
//    public String returnConfigValue(){
//        return configValue;
//    }
}
