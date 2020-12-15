package com.billjc.race.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import redis.clients.jedis.Jedis;

/**
 * 配置中心工具类
 *
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 */
public class ConfigClientUtil {
	
	private static Log log  = LogFactory.getLog(ConfigClientUtil.class);
	
	public static String getConfigCenterValue(){
    	HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/billjcRace/api/config/");
		HttpResponse response = null;
		String mapperConfigValue = "";
		try {
			response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				mapperConfigValue = EntityUtils.toString(responseEntity);
	    		@SuppressWarnings("resource")
				Jedis edis = new Jedis();
	    		edis.set("xmlFilePath", mapperConfigValue);
			}
		} catch (ClientProtocolException e) {
			log.error(e.toString());
			return "error";
		} catch (IOException e) {
			log.error(e.toString());
			return "error";
		} finally {
			httpClient = null;
			if (response != null) {
				response = null;
			}
		}
		return mapperConfigValue;
    }
}
