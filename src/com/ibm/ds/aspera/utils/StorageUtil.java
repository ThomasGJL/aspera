package com.ibm.ds.aspera.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;

import py4j.GatewayServer;

public class StorageUtil
{

    private static AmazonS3 _cosClient;
    
    
    public StorageUtil() throws UnsupportedEncodingException {
    	
    	SDKGlobalConfiguration.IAM_ENDPOINT = PropertiesUtil.GetValueByKey("/aspera.properties", "COS_AUTH_ENDPOINT");

        String token = "token";
        byte[] key  = CryptUtil.hexStringToBytes(PropertiesUtil.GetValueByKey("/aspera.properties", "COS_API_KEY_ID"));
        byte[] decrypted = CryptUtil.DES_CBC_Decrypt(key, token.getBytes());
        String api_key = new String(decrypted, "UTF-8");
        String service_instance_id = PropertiesUtil.GetValueByKey("/aspera.properties", "COS_SERVICE_CRN");
        String endpoint_url = PropertiesUtil.GetValueByKey("/aspera.properties", "COS_ENDPOINT");
        String location = PropertiesUtil.GetValueByKey("/aspera.properties", "LOCATION"); 

        _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
    }
    

    /**
     * @param bucketName
     * @param clientNum
     * @param api_key
     * @param service_instance_id
     * @param endpoint_url
     * @param location
     * @return AmazonS3
     */
    public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
    {
        AWSCredentials credentials;
        credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return cosClient;
    }

    /**
     * @param bucketName
     * @param cosClient
     */
    public static List<String> listObjects(String bucketName)
    {
        System.out.println("Listing objects in bucket " + bucketName);
        
        ObjectListing objectListing = _cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        //int strArraySize = objectListing.getObjectSummaries().size();
        List<String> itemList  = new ArrayList<String>();
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            if (objectSummary.getSize() > 0) {
            	itemList.add(objectSummary.getKey());
            }
        }
        
        return itemList;
    }


    /**
     * @param cosClient
     */
    public static void listBuckets()
    {
        System.out.println("Listing buckets");
        final List<Bucket> bucketList = _cosClient.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
    }
    
    
    public static void uploadItem(String bucketName, String itemName, String fileAndpath) {
        
    	System.out.printf("Uploading item: %s\n", itemName);
        _cosClient.putObject(bucketName, itemName, new File(fileAndpath));
        System.out.printf("Item: %s uploaded!\n", itemName);
    }
    
    
    public static void deleteItem(String bucketName, String itemName) {
        
    	System.out.printf("Deleting item: %s\n", itemName);
        _cosClient.deleteObject(bucketName, itemName);
        System.out.printf("Item: %s deleted!\n", itemName);
    }
    
    public static void downloadItem(String bucketName, String itemName, String fileName) {
        
    	System.out.printf("Downloading item: %s\n", itemName);
    	GetObjectRequest request = new GetObjectRequest(bucketName, itemName);
    	_cosClient.getObject(request, new File(fileName));
        System.out.printf("Item: %s downloaded!\n", itemName);
    }
    
    
	public String sayHi(String name) {
		
		return ("Hello " + name); 
	}
    
    
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
    	
    	//StorageUtil storageUtil = new StorageUtil();
    	//storageUtil.connectCos();
    	
    	//String bucketName = "thomastest-private";
    	//String bucketName = "branding-dev-private";
    	//listBuckets();
        //listObjects(bucketName);
        
    	/**
    	List<String> itemList = listObjects(bucketName);
    	for (int i=0;i<itemList.size();i++) {
    		downloadItem(bucketName, itemList.get(i), "C:\\eclipse_neon\\gitspace\\fileobj\\media\\" + itemList.get(i));
    	}
    	**/
    	
        //uploadItem(bucketName, "original_images/result.xlsx", "C:\\result.xlsx");
        //deleteItem(bucketName, "result.xlsx");
    	//downloadItem(bucketName, "result.xlsx", "result.xlsx");
    	//downloadItem(bucketName, "original_images/Cognitive_NPS_Workflow_v1.0.png", "C:\\eclipse_neon\\gitspace\\dashboard-dev\\Cognitive_NPS_Workflow_v1.0.png");
    	
    	
    	StorageUtil storage = new StorageUtil();
        GatewayServer server = new GatewayServer(storage);
        server.start();
        
        //System.out.println("---===GatewayServer started===---");
    }

}
