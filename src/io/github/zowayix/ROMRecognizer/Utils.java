package io.github.zowayix.ROMRecognizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author megan
 */
public class Utils {

	private Utils() {
	}

	public static boolean endsWithIgnoreCase(String s, String suffix) {
		return s.regionMatches(true, s.length() - suffix.length(), suffix, 0, suffix.length());
	}

	public static InputStream cloneInputStream(InputStream is) throws IOException {
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead;
		while ((bytesRead = is.read(buf)) > -1) {
			baos.write(buf, 0, bytesRead);
		}
		baos.flush();
		return new ByteArrayInputStream(baos.toByteArray());
	}
}
