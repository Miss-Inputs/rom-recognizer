/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.romrecognizer;

/**
 *
 * @author megan
 */
public class Game {

	private String name;
	private String description;
	private String romName;
	private long size;
	private String crc32;
	private String md5;
	private String sha1;
	private String status;
	private DataFile dataFile;

	public Game(String name, String description, String romName, long size, String crc32, String md5, String sha1, String status, DataFile dataFile) {
		this.name = name;
		this.description = description;
		this.romName = romName;
		this.size = size;
		this.crc32 = crc32;
		this.md5 = md5;
		this.sha1 = sha1;
		this.status = status;
		this.dataFile = dataFile;
	}

	@Override
	public String toString() {
		return String.format("Name: %s%nDesc: %s%nROM name: %s%nSize: %d%nCRC32: %s%nMD5: %s%nSHA-1: %s%nStatus: %s%nPlatform: %s",
				name, description, romName, size, crc32, md5, sha1, status, dataFile.getName());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the romName
	 */
	public String getRomName() {
		return romName;
	}

	/**
	 * @param romName the romName to set
	 */
	public void setRomName(String romName) {
		this.romName = romName;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the crc32
	 */
	public String getCrc32() {
		return crc32;
	}

	/**
	 * @param crc32 the crc32 to set
	 */
	public void setCrc32(String crc32) {
		this.crc32 = crc32;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	/**
	 * @return the sha1
	 */
	public String getSha1() {
		return sha1;
	}

	/**
	 * @param sha1 the sha1 to set
	 */
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the dataFile
	 */
	public DataFile getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile the dataFile to set
	 */
	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
}
