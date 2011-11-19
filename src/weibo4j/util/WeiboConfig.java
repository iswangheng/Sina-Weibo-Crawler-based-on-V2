package weibo4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.net.URL;

import weibo4j.examples.Log;

public class WeiboConfig {
	private static String filePath = "config.properties";
	public WeiboConfig() {} 
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(new FileInputStream(getFilePath())); 
		} catch (FileNotFoundException e) {
			Log.logInfo("Not found: "+getFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    }

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		WeiboConfig.filePath = filePath;
	} 
}
