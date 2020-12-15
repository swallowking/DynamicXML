package com.billjc.race.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * 定时任务执行
 *
 * @author swallow
 * @since 2020/12/5
 * @version 1.0
 */
@Component
public class XMLScheduleTask {
	
	private static Log log  = LogFactory.getLog(XMLScheduleTask.class);
	
	RefreshMapperCache refreshMapper = RefreshMapperCache.getInstance();
    
    @Scheduled(cron = "0/60 * * * * ?")//每60秒执行
    private void run(){
    	try{
			ValidateXMLUtil.validateXMLChange(new ArrayList<>(), 1);
    	} catch (Exception e) {
    		log.error(e.toString());
    	}
    }

}
