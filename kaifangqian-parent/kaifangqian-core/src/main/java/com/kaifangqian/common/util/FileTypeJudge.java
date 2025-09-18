package com.kaifangqian.common.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/** 
 * 文件类型判断类 
 */  
public final class FileTypeJudge {  
      
    /** 
     * Constructor 
     */  
    private FileTypeJudge() {}  
      
    /** 
     * 将文件头转换成16进制字符串 
     *  
     * @param
     * @return 16进制字符串 
     */  
    private static String bytesToHexString(byte[] src){  
          
        StringBuilder stringBuilder = new StringBuilder();     
        if (src == null || src.length <= 0) {     
            return null;     
        }     
        for (int i = 0; i < src.length; i++) {     
            int v = src[i] & 0xFF;     
            String hv = Integer.toHexString(v);     
            if (hv.length() < 2) {     
                stringBuilder.append(0);     
            }     
            stringBuilder.append(hv);     
        }     
        return stringBuilder.toString();     
    }  
    /**
     * 获得文件流
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    private static InputStream getFileContentStream(String filePath) throws IOException {  
    	InputStream inputStream = new FileInputStream(filePath);  
    	return inputStream;
    }  
    /**
     * 获得文件流
     * @param filebytes 文件字节
     * @return
     * @throws IOException
     */
    private static InputStream getFileContentStream(byte[] filebytes) throws IOException {  
    	InputStream inputStream = new ByteArrayInputStream(filebytes);
    	return inputStream;
    }  
    /**
     * 得到文件头
     * @param inputStream  文件流
     * @return
     * @throws IOException
     */
    private static String getFileHead(InputStream inputStream) throws IOException {  
    	if(inputStream!=null){
    		byte[] b = new byte[28];  
    		inputStream.read(b, 0, 28);  
    		return bytesToHexString(b);  
    	}else{
    		return null;
    	}
    }  
    /**
     * 得到文件头
     * @param filebytes  文件字节
     * @return
     * @throws IOException
     */
    private static String getFileHead(byte[] filebytes) throws IOException {  
    	InputStream inputStream=null;
		try {
			inputStream = getFileContentStream(filebytes);
			String fileHead = getFileHead(inputStream);
			return fileHead;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    /** 
     * 得到文件头 
     *  
     * @param filePath 文件路径 
     * @return 文件头 
     * @throws IOException
     */  
    private static String getFileHead(String filePath) throws IOException{  
          
        InputStream inputStream = null;  
          
        try {  
            inputStream = getFileContentStream(filePath);  
            return getFileHead(inputStream); 
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }   
    /** 
     * 获得文件类型 
     *  
     * @param filePath 文件路径 
     * @return 文件类型 
     */  
    public static FileType getType(String filePath) throws IOException {  
          
        String fileHead = getFileHead(filePath);  
          
        if (fileHead == null || fileHead.length() == 0) {  
            return null;  
        }  
          
        fileHead = fileHead.toUpperCase();  
          
        FileType[] fileTypes = FileType.values();  
          
        for (FileType type : fileTypes) {  
            if (fileHead.startsWith(type.getValue())) {  
                return type;  
            }  
        }  
          
        return null;  
    }  
    /**
     * 获得文件类型 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static FileType getType(InputStream inputStream) throws IOException {  
    	
    	String fileHead = getFileHead(inputStream);  
    	
    	if (fileHead == null || fileHead.length() == 0) {  
    		return null;  
    	}  
    	
    	fileHead = fileHead.toUpperCase();  
    	
    	FileType[] fileTypes = FileType.values();  
    	
    	for (FileType type : fileTypes) {  
    		if (fileHead.startsWith(type.getValue())) {  
    			return type;  
    		}  
    	}  
    	
    	return null;  
    }  
    /**
     * 获得文件类型 
     * @param filebytes
     * @return
     * @throws IOException 
     */
    public static FileType getType(byte[] filebytes) throws IOException {  
		String fileHead = getFileHead(filebytes);
		if (fileHead == null || fileHead.length() == 0) {
			return null;
		}
		fileHead = fileHead.toUpperCase();
        System.out.println(fileHead);
		FileType[] fileTypes = FileType.values();
		for (FileType type : fileTypes) {
			if (fileHead.startsWith(type.getValue())) {
				return type;
			}
		}
		return null;  
    }  
    /**
     * 通过传入文件字节数组，判断文件是否为指定类型
     * @param filebytes 文件
     * @param fileType  类型
     * @return
     * @throws IOException
     * @auther zzk
     * 2017年1月17日下午1:35:09
     */
    public static boolean checkType(byte[] filebytes,FileType fileType) throws IOException {  
    	FileType fileType2 = getType(filebytes);
    	return fileType.equals(fileType2);  
    }  
    /**
     * 通过传入文件路径，判断文件是否为指定类型
     * @param path  文件路径
     * @param fileType 类型
     * @return
     * @throws IOException
     * @auther zzk
     * 2017年1月17日下午1:35:55
     */
    public static boolean checkType(String path,FileType fileType) throws IOException {  
    	FileType fileType2 = getType(path);
    	return fileType.equals(fileType2);  
    }  
    /**
     * 通过传入文件流，判断文件是否为指定类型
     * @param inputStream  文件流
     * @param fileType   类型
     * @return
     * @throws IOException
     * @auther zzk
     * 2017年1月17日下午1:36:24
     */
    public static boolean checkType(InputStream inputStream,FileType fileType) throws IOException {  
    	FileType fileType2 = getType(inputStream);
    	return fileType.equals(fileType2);  
    }




} 
