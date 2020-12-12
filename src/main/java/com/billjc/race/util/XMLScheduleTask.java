package com.billjc.race.util;

import java.util.ArrayList;

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
	
	RefreshMapperCache refreshMapper = RefreshMapperCache.getInstance();
    
    @Scheduled(cron = "0/60 * * * * ?")//每60秒执行
    private void run(){
    	try{
			ValidateXMLUtil.validateXMLChange(new ArrayList<>(), 1);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
