package com.billjc.race.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
/**
 * 校验XML文件MD5码值与redis缓存值是否一致；如果不同则重新加载XML文件
 *
 * @author   swallow
 * @version  1.0
 * @since    2020/12/5
 *
 */
public class ValidateXMLUtil {

	// 默认编码字符组合
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static Jedis edis = new Jedis();
    private static Log log  = LogFactory.getLog(ValidateXMLUtil.class);

	static {
		try {
			MessageDigest.getInstance("MD5");
//			edis.flushAll();
		} catch (NoSuchAlgorithmException nsaex) {
			log.error(ValidateXMLUtil.class.getName() + "init error!");
			nsaex.printStackTrace();
		}
	}
	
	/**
	 * 单点或分布式服务访问
	 * 变更：支持添加或修改XML
	 *
	 * @param fileNameList
	 * @return
	 */
	public static int validateXMLChange(List<String> fileNameList, int allFlag) {
		try {
			Map<String,Object> changeFileMap = new HashMap<String,Object>();
			boolean redistributeFlag = false;
			if (fileNameList != null && fileNameList.size() > 0) {
				if ("redis".equals(fileNameList.get(0))) {
					redistributeFlag = true;
				}
			}
			if (!redistributeFlag) {
				//单服务情况
				for (int z=0; z<fileNameList.size(); z++) {
					String xmlFileName = fileNameList.get(z);
					String xmlFilePath = ""+
							System.getProperty("user.dir")+
							File.separator+"target"+
//							".."+File.separator+"webapps"+
//							File.separator+"billjcRace"+
//							File.separator+"WEB-INF"+
							File.separator+"classes"+File.separator+"com"+
							File.separator+"billjc"+File.separator+"race"+
							File.separator+"dao"+File.separator+""+xmlFileName+".xml";
					System.out.println(xmlFilePath+"########################################");
					xmlFileCheck(xmlFilePath, xmlFileName, changeFileMap);
				}
			} else {
				//分布式环境下--单文件情况
				String xmlFilePath = edis.get("xmlFilePath");//文件路径
				String xmlFileName = xmlFilePath.substring(
						xmlFilePath.lastIndexOf(File.separatorChar)+1,
						xmlFilePath.indexOf(".xml"));
				xmlFileCheck(xmlFilePath, xmlFileName, changeFileMap);
			}
			
			if (changeFileMap.size() == 0 && allFlag != 1) {
				return 0;
			} else {
				RefreshMapperCache refreshMapper = RefreshMapperCache.getInstance();
				Object obj = SpringUtils.getBean("sqlSessionFactory");
				SqlSessionFactory sqlSessionFactory = null;
				if (obj instanceof SqlSessionFactory) {
					sqlSessionFactory = (SqlSessionFactory)obj;
				}
				refreshMapper.setSqlSessionFactory(sqlSessionFactory);
				Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
						.getResources("classpath*:com/billjc/race/dao/**/*Mapper.xml");
				refreshMapper.setMapperLocations(mapperLocations);
				refreshMapper.setChangeResourceNameList(changeFileMap);
				return refreshMapper.refreshMapper();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 
	 * @param xmlFilePath XML文件路径
	 * @param xmlFileName XML文件名称
	 * @param changeFileMap 变更文件map
	 */
	private static Map<String,Object> xmlFileCheck(String xmlFilePath, String xmlFileName, 
			Map<String,Object> changeFileMap){
		File xmlFile = new File(xmlFilePath);
		String md5Str = getMd5ByFile(xmlFile);
		String xmlCode = edis.get(xmlFileName);
		String targetFilePath = "F:\\eclipseWorkSpace\\billjc-race-project\\src\\main\\java\\com\\billjc\\race\\dao\\EmployeeMapper.xml";
		if (StringUtils.isEmpty(xmlCode)) {//如果为添加
			edis.set(xmlFileName,md5Str);
			changeFileMap.put(xmlFileName,xmlFileName);
			fileOutput(xmlFilePath,xmlFile,targetFilePath);
		} else {
			if (!xmlCode.equals(md5Str)) {//如果为修改
				edis.pexpire(xmlFileName, 1);
				edis.set(xmlFileName, md5Str);
				changeFileMap.put(xmlFileName,xmlFileName);
				fileOutput(xmlFilePath,xmlFile,targetFilePath);
			}
		}
		return changeFileMap;
	}

	/**
	 * 覆盖掉原文件
	 * @param xmlFile
	 */
	private static void fileOutput(String filePath, File xmlFile, String targetFilePath){
		BufferedReader bufr = null;
        BufferedWriter bufw = null;
        try
        {
            bufr = new BufferedReader(new FileReader(filePath));
            bufw = new BufferedWriter(new FileWriter(targetFilePath));
 
            String line = null;
 
            while((line=bufr.readLine())!=null)
            {
                bufw.write(line);
                bufw.newLine();
                bufw.flush();
 
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(bufr!=null)
                    bufr.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("读取关闭失败");
            }
            try
            {
                if(bufw!=null)
                    bufw.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("写入关闭失败");
            }
        }
	}
	/**
	 * 返回文件的MD5码值,用于验证文件是否发生变更修改
	 *
	 * @param file 文件值
	 * @return value MD5编码值
	 */
	private static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
        	in = new FileInputStream(file);
        	byte[] buffer = new byte[1024];
    		int numRead = 0;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
    		while ((numRead = in.read(buffer)) > 0) {
    			md5.update(buffer, 0, numRead);
    		}
            value = bufferToHex(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if(in != null) {
                    in.close();
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        return value;
    }
	
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}
 
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}
 
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
