package com.ibm.ds.aspera;

import java.io.UnsupportedEncodingException;

import com.ibm.ds.aspera.utils.StorageUtil;

import py4j.GatewayServer;

public class Storage {
	
	
	public void uploadFile(String bucketName, String fileName, String fileAndpath) throws UnsupportedEncodingException {
		
		StorageUtil storageUtil = new StorageUtil();
		storageUtil.uploadItem(bucketName, fileName, fileAndpath);
    	
	}
	

	public void deleteFile(String bucketName, String fileName) throws UnsupportedEncodingException {
		
		StorageUtil storageUtil = new StorageUtil();
		storageUtil.deleteItem(bucketName, fileName);
	}
	
	public String sayHi(String name) {
		
		return ("Hello " + name); 
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//System.out.println("TEST Jar");
		//StorageUtil storageUtil = new StorageUtil();
		//_cosClient = storageUtil.connectCos();
		//Storage storage = new Storage();
		//storage.uploadFile("thomastest", "result.xlsx", "C:\\result.xlsx");
		//storage.deleteFile("thomastest", "result.xlsx");
		
		
		Storage storage = new Storage();
        GatewayServer server = new GatewayServer(storage);
        server.start();
        
        System.out.println("---===GatewayServer started===---");
	}

}
