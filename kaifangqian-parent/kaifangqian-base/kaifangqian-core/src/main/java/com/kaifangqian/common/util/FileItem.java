package com.kaifangqian.common.util;

import org.apache.http.entity.ContentType;

import java.io.*;

/**
 * 鏂囦欢鍏冩暟鎹€�
 * @author zzk
 * 2017骞�10鏈�26鏃ヤ笅鍗�4:01:28
 */
public class FileItem implements Serializable{

	//涓婁紶鏂囦欢鏃跺€欑殑瀛楁鍚嶇О
	private String fieldName;
	//鏂囦欢鍚嶇О
	private String fileName;
	//鏂囦欢绫诲瀷
	private ContentType mimeType = ContentType.DEFAULT_BINARY;
	private byte[] content;
	private File file;

    /**
     * 鍩轰簬瀛楁鍚嶇О鍜屾枃浠剁殑鏋勯€犲櫒
     * 
     */
	public FileItem(String fieldName, File file) {
		this.fieldName = fieldName;
		this.file = file;
		init(fieldName, file);
	}
	/**
     * 鍩轰簬瀛楁鍚嶇О鍜屾枃浠惰矾寰勭殑鏋勯€犲櫒
     * 
     */
	public FileItem(String fieldName, String filePath) {
		this.fieldName = fieldName;
		this.file = new File(filePath);
		init(fieldName, file);
	}
	/**
	 * 鍩轰簬瀛楁鍚嶇О銆佹枃浠跺悕绉般€佹枃浠跺唴瀹圭殑鏋勯€犲櫒
	 * @param fieldName
	 * @param fileName
	 * @param content
	 */
	public FileItem(String fieldName, String fileName, byte[] content) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.content = content;
	}
	/**
	 * 鑾峰彇鏂囦欢鍚嶇О
	 * @return
	 * @auther zzk
	 * 2017骞�10鏈�26鏃ヤ笅鍗�4:05:35
	 */
	public String getFileName() {
		if (this.fileName == null && this.file != null && this.file.exists()) {
			this.fileName = file.getName();
		}
		return this.fileName;
	}
	/**
	 * 鑾峰彇鏂囦欢涓婁紶鐨勫瓧娈靛悕
	 * @return
	 * @auther zzk
	 * 2017骞�10鏈�26鏃ヤ笅鍗�4:05:20
	 */
	public String getFieldName() {
		return this.fieldName;
	}
	
	public ContentType getMimeType() {
		return mimeType;
	}

	public byte[] getContent() throws IOException {
		if (this.content == null && this.file != null && this.file.exists()) {
			InputStream in = null;
			ByteArrayOutputStream out = null;

			try {
				in = new FileInputStream(this.file);
				out = new ByteArrayOutputStream();
				int ch;
				while ((ch = in.read()) != -1) {
					out.write(ch);
				}
				this.content = out.toByteArray();
			} finally {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			}
		}
		return this.content;
	}
	
	private void init(String fieldName,File file){
		this.fieldName = fieldName;
		this.file = file;
		this.fileName = file.getName();
	}

}