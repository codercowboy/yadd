package com.codercowboy.util.yadd;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

/*
 * Sources:
 *   http://www.mkyong.com/java/how-to-read-file-in-java-fileinputstream/
 *   http://stackoverflow.com/questions/415953/generate-md5-hash-in-java
 */

public class FileIndexer {
	protected static final int BUFFER_SIZE = 1024 * 64;
	protected MessageDigest md;
	
	public static List<FileInfo> indexFolder(File folder) throws Exception {
		if (folder == null) {
			return new LinkedList<FileInfo>();
		} 
		String baseFolderAbsolutePath = folder.getAbsolutePath() + File.separator;
		if (!folder.isDirectory()) {
			return Arrays.asList(indexFile(folder, baseFolderAbsolutePath));
		}
		return indexFolder(folder, baseFolderAbsolutePath);
	}
	
	protected static List<FileInfo> indexFolder(File folder, String baseFolderAbsolutePath) throws Exception {
		List<FileInfo> files = new LinkedList<FileInfo>();
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(indexFolder(file, baseFolderAbsolutePath));
			} else {
				files.add(indexFile(file, baseFolderAbsolutePath));
			}
		}
		return files;
	}
	
	public static FileInfo indexFile(File file, String baseFolderAbsolutePath) throws Exception {
		FileInfo fi = new FileInfo();
		fi.setRelativePath(file.getAbsolutePath().substring(baseFolderAbsolutePath.length()));
		fi.setFileSize(file.length());
		fi.setMd5Checksum(getMD5ChecksumForFile(file));
		return fi;
	}
	
	public static String getMD5ChecksumForFile(File file) throws Exception {
		FileInputStream fis = null;
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.reset();
			fis = new FileInputStream(file);
			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = fis.read(buffer)) != -1) {				
				md.update(buffer, 0, bytesRead);
			}
			return new String(Hex.encodeHex(md.digest()));
	    } finally {
	    	if (fis != null) {
	    		fis.close();
	    	}
	    }
	}
}
