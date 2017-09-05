/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.zowayix.ROMRecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author megan
 */
public class Hash {

	public static String md5(InputStream is) throws IOException {
		try {
			return hashInputStream(is, "md5");
		} catch (NoSuchAlgorithmException ex) {
			//Shouldn't happen, Java implementations are required to support MD5
			throw new RuntimeException(ex);
		}
	}

	public static String md5(File f) throws IOException {
		try {
			return hashFile(f, "md5");
		} catch (NoSuchAlgorithmException ex) {
			//Shouldn't happen, Java implementations are required to support MD5
			throw new RuntimeException(ex);
		}
	}

	public static String sha1(InputStream is) throws IOException {
		try {
			return hashInputStream(is, "sha-1");
		} catch (NoSuchAlgorithmException ex) {
			//Shouldn't happen, Java implementations are required to support SHA-1
			throw new RuntimeException(ex);
		}
	}

	public static String sha1(File f) throws IOException {
		try {
			return hashFile(f, "sha-1");
		} catch (NoSuchAlgorithmException ex) {
			//Shouldn't happen, Java implementations are required to support SHA-1
			throw new RuntimeException(ex);
		}
	}

	public static String crc32(InputStream is) throws IOException {
		//Fuckin MessageDigest doesn't even fucking have CRC32 what the fuck shit tit balls
		final int BUF_SIZE = 1024 * 1024;

		CRC32 thing = new CRC32();

		byte[] buf = new byte[BUF_SIZE];
		int bytesRead;
		while ((bytesRead = is.read(buf)) >= 0) {
			thing.update(buf, 0, bytesRead);
		}
		return String.format("%08x", thing.getValue());
	}

	public static String crc32(File f) throws IOException {
		//Fuckin MessageDigest doesn't even fucking have CRC32 what the fuck shit tit balls
		/*final int BUF_SIZE = 1024 * 1024;

		CRC32 thing = new CRC32();

		byte[] buf = new byte[BUF_SIZE];
		try (final FileInputStream fis = new FileInputStream(f)) {
			int bytesRead;
			while ((bytesRead = fis.read(buf)) >= 0) {
				thing.update(buf, 0, bytesRead);
			}
		}
		//return DatatypeConverter.printHexBinary(md.digest());
		return String.format("%08x", thing.getValue());*/
		try (final FileInputStream fis = new FileInputStream(f)) {
			return crc32(fis);
		}
	}

	public static String hashInputStream(InputStream is, String algorithm) throws IOException, NoSuchAlgorithmException {
		final int BUF_SIZE = 1024 * 1024;

		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] buf = new byte[BUF_SIZE];
		int bytesRead;
		while ((bytesRead = is.read(buf)) > -1) {
			md.update(buf, 0, bytesRead);
		}
		return DatatypeConverter.printHexBinary(md.digest());
	}

	public static String hashFile(File f, String algorithm) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		/*final int BUF_SIZE = 1024 * 1024;
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] buf = new byte[BUF_SIZE];
		try (final FileInputStream fis = new FileInputStream(f)) {
			int bytesRead;
			while ((bytesRead = fis.read(buf)) >= 0) {
				md.update(buf, 0, bytesRead);
			}
		}
		return DatatypeConverter.printHexBinary(md.digest());*/
		try (final FileInputStream fis = new FileInputStream(f)) {
			return hashInputStream(fis, algorithm);
		}
	}

}
