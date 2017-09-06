package io.github.zowayix.ROMRecognizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.table.AbstractTableModel;

/**
 * Shit that I can't be fucked putting anywhere else
 * @author megan
 */
public class Utils {

	private Utils() {
	}
	

	public static String formatByteSize(long bytes, boolean isMetric) {
		int baseUnit = isMetric ? 1000 : 1024;
		if (bytes < baseUnit) {
			return bytes + " bytes";
		}
		int exp = (int) (Math.log(bytes) / Math.log(baseUnit));
		char suffix = "KMGTPE".charAt(exp - 1);
		double d = bytes / Math.pow(baseUnit, exp);
		return (Math.floor(d) == d ? (long) d : String.format("%.2f", d)) + " " + suffix + (isMetric ? "iB" : "B");
	}

	public static String getFileExtension(File f) {
		return getFileExtension(f.getName());
	}

	public static String getFileExtension(String s) {
		final int index = s.lastIndexOf('.');
		if (index == -1) {
			return "";
		}
		return s.substring(index + 1);
	}

	public static void setTableValue(AbstractTableModel model, int rowNumber, String columnName, Object value) {
		model.setValueAt(value, rowNumber, model.findColumn(columnName));
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
