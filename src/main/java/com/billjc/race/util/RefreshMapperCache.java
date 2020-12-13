package com.billjc.race.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.Resource;
/**
 * 清除Mybatis sqlSessionFactory Configuration配置，
 * 重新加载 Resource 数据源
 *
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 */
public class RefreshMapperCache {
    private Log log  = LogFactory.getLog(RefreshMapperCache.class);
    private static SqlSessionFactory sqlSessionFactory;
    private Resource[] mapperLocations;
    private Map<String,Object> changeResourceNameMap = new HashMap<String, Object>();
    private static RefreshMapperCache refreshMapperSingleton = new RefreshMapperCache();
    
    public static RefreshMapperCache getInstance() {
        return refreshMapperSingleton;
    }

    public RefreshMapperCache(){}

    public int refreshMapper() {
        try {
            Configuration configuration = sqlSessionFactory.getConfiguration();
            // 清理缓存数据
            this.removeConfig(configuration);
            // 重新加载
            for (Resource configLocation : mapperLocations) {
                try {
            		XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
                    		configLocation.getInputStream(), 
                    		configuration, configLocation.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (IOException e) {
                    return -1;
                } catch (BuilderException e){
                	continue;
                }
            }
            changeResourceNameMap.clear();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e.toString());
            return -1;
        }
        return 1;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactoryValue) {
        sqlSessionFactory = sqlSessionFactoryValue;
    }

	public void setChangeResourceNameList(Map<String,Object> changeResourceNameListValue) {
		changeResourceNameMap = changeResourceNameListValue;
	}

	public void setMapperLocations(Resource[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	/**
     * 清空Configuration中缓存
     *
     * @param configuration MapperConfiguration
     * @throws Exception 异常
     */
    private void removeConfig(Configuration configuration) throws Exception {
        Class<?> classConfig = configuration.getClass();
        clearMap(classConfig, configuration, "mappedStatements");
        clearMap(classConfig, configuration, "caches");
        clearMap(classConfig, configuration, "resultMaps");
        clearMap(classConfig, configuration, "parameterMaps");
        clearMap(classConfig, configuration, "keyGenerators");
        clearMap(classConfig, configuration, "sqlFragments");
        clearSet(classConfig, configuration, "loadedResources");

    }

    @SuppressWarnings("rawtypes")
    private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
        Field field = classConfig.getDeclaredField(fieldName);
        field.setAccessible(true);
        Map mapConfig = (Map) field.get(configuration);
        mapConfig.clear();
    }

    @SuppressWarnings("rawtypes")
    private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
        Field field = classConfig.getDeclaredField(fieldName);
        field.setAccessible(true);
        Set setConfig = (Set) field.get(configuration);
        setConfig.clear();
    }

}
