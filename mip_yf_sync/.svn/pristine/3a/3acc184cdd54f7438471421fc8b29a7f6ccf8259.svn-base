package com.mip.biz.oms.yf_sync.db;

import java.io.IOException;
import java.util.Properties;

import com.mip.core.basebiz.service.ServiceRuntimeException;

public class PropertiesUtil {
    static Properties properties = new Properties();

    public PropertiesUtil() {
    }

    public static void loadFile(String fileName){
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
        	throw new ServiceRuntimeException("读取配置文件错误",e);
        }
    }

    public static String getPropertyValue(String key){
        return properties.getProperty(key);
    }
}
