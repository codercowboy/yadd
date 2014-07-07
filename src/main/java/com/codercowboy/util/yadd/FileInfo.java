package com.codercowboy.util.yadd;

import java.io.Serializable;

public class FileInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String relativePath;
	protected long fileSize;
	protected String md5Checksum;
	
	public String getRelativePath() { return relativePath; }
	public void setRelativePath(String relativePath) { this.relativePath = relativePath; }
	public long getFileSize() { return fileSize; }
	public void setFileSize(long fileSize) { this.fileSize = fileSize; }
	public String getMd5Checksum() { return md5Checksum; }
	public void setMd5Checksum(String md5Checksum) { this.md5Checksum = md5Checksum; }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
		result = prime * result
				+ ((md5Checksum == null) ? 0 : md5Checksum.hashCode());
		result = prime * result
				+ ((relativePath == null) ? 0 : relativePath.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FileInfo))
			return false;
		FileInfo other = (FileInfo) obj;
		if (fileSize != other.fileSize)
			return false;
		if (md5Checksum == null) {
			if (other.md5Checksum != null)
				return false;
		} else if (!md5Checksum.equals(other.md5Checksum))
			return false;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileInfo [relativePath=" + relativePath + ", fileSize="
				+ fileSize + ", md5Checksum=" + md5Checksum + "]";
	}			
}
