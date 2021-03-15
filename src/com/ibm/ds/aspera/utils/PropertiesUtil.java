package com.ibm.ds.aspera.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesUtil {

	public static String GetValueByKey(String filePath, String key) {
		
		Properties pps = new Properties();
		try {
			//InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			InputStream in =(InputStream) PropertiesUtil.class.getResourceAsStream(filePath); 
			pps.load(in);
			String value = pps.getProperty(key);
			//System.out.println(key + " = " + value);
			return value;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void GetAllProperties(String filePath) throws IOException {
		
		Properties pps = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(filePath));
		pps.load(in);
		Enumeration en = pps.propertyNames();

		while (en.hasMoreElements()) {
			String strKey = (String) en.nextElement();
			String strValue = pps.getProperty(strKey);
			System.out.println(strKey + "=" + strValue);
		}

	}

	public static void WriteProperties(String filePath, String pKey, String pValue) throws IOException {
		
		Properties pps = new Properties();

		InputStream in = new FileInputStream(filePath);
		pps.load(in);
		OutputStream out = new FileOutputStream(filePath);
		pps.setProperty(pKey, pValue);
		pps.store(out, "Update " + pKey + " name");
	
	}

	public static void main(String[] args) throws IOException {
		
		//String value = GetValueByKey("/aspera.properties", "COS_ENDPOINT");
		//System.out.println(value);
		//GetAllProperties("aspera.properties");
		//WriteProperties("aspera.properties", "long", "212");
	}
}
