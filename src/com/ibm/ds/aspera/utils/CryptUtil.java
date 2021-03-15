package com.ibm.ds.aspera.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class CryptUtil {
	
	public static final String KEY_ALGORITHM = "DES";

	public static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_CBC = "DES/CBC/PKCS5Padding";
	
    /**
     * BASE64 decrypt
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
 
    /**
     * BASE64 encrypt
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
 

	
    /**
     * DES decrypt
     */	
    public static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) {
        
    	try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }
    
    /**
     * DES encrypt
     */	
    public static byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes) {
        
    	try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    
	/**
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec des = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(des);
		return secretKey;
	}    
    
    /**
     * DES decrypt
     * @throws Exception 
     */	
    public static byte[] DES_ECB_Decrypt(byte[] data, byte[] key) throws Exception {
        
    		Key k = toKey(key);
    		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
    		cipher.init(Cipher.DECRYPT_MODE, k, new SecureRandom());
    		return cipher.doFinal(data);
    }
    
    /**
     * DES encrypt
     * @throws Exception 
     */	
    public static byte[] DES_ECB_Encrypt(byte[] data, byte[] key) throws Exception {
        
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(Cipher.ENCRYPT_MODE, k, new SecureRandom());
		return cipher.doFinal(data);
    }    

    private static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }    
    
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
             
        }
        return d;
    } 
    
    /**
     * DES decrypt
     * @throws Exception 
     */	
    public static byte[] DES_NoPadding_Decrypt(byte[] data, byte[] key) throws Exception {
    	
    		Key k = toKey(key);
    		DESKeySpec keySpec = new DESKeySpec(data);
    		
    		Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
    		cipher.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(keySpec.getKey()));
    		return cipher.doFinal(data);
    }
    
    
    
 
    public static void main(String[] args) throws Exception {
        
        //String content = "123";
        //String key = "";

        //System.out.println("content:" + content);
        //byte[] encrypted = DES_CBC_Encrypt(content.getBytes(), key.getBytes());
        //System.out.println("encrypt " + byteToHexString(encrypted));

        //byte[] decrypted = DES_CBC_Decrypt(encrypted, key.getBytes());
        //System.out.println("decrypt:" + new String(decrypted));
        
        //byte[] ecb_encrypted = DES_ECB_Encrypt(content.getBytes(), key.getBytes());
        //System.out.println("encrypt " + byteToHexString(ecb_encrypted));
        
        //byte[] ecb_decrypted = DES_ECB_Decrypt(ecb_encrypted, key.getBytes()); 
		//System.out.println("decrypt:" + new String(ecb_decrypted));
        
    }
}

