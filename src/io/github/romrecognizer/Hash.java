/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.romrecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author megan
 */
public class Hash {
	
	public static String md5(File f) throws IOException {
		try {
			return hashFile(f, "md5");
		} catch (NoSuchAlgorithmException ex) {
			//Shouldn't happen, Java implementations are required to support MD5
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

	public static String hashFile(File f, String algorithm) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		final int BUF_SIZE = 1024 * 1024;
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] buf = new byte[BUF_SIZE];
		try (final FileInputStream fis = new FileInputStream(f)) {
			int bytesRead;
			while ((bytesRead = fis.read(buf)) >= 0) {
				md.update(buf, 0, bytesRead);
			}
		}
		return DatatypeConverter.printHexBinary(md.digest());
	}

}
