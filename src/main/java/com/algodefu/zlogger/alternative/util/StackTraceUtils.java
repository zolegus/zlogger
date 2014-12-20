package com.algodefu.zlogger.alternative.util;

import java.net.URL;

/**
 * @author oleg.zherebkin
 */
public class StackTraceUtils {

	public static String getCodeLocation(Class type) {
		try {
			if (type != null) {
				// file:/C:/java/maven-2.0.8/repo/com/icegreen/greenmail/1.3/greenmail-1.3.jar
				URL resource = type.getProtectionDomain().getCodeSource().getLocation();
				if (resource != null) {
					String locationStr = resource.toString();
					// now lets remove all but the file name
					String result = getCodeLocation(locationStr, '/');
					if (result != null) {
						return result;
					}
					return getCodeLocation(locationStr, '\\');
				}
			}
		} catch (Exception e) {
			// ignore
		}
		return "na";
	}

	public static String getCodeLocation(String locationStr, char separator) {
		int idx = locationStr.lastIndexOf(separator);
		if (isFolder(idx, locationStr)) {
			idx = locationStr.lastIndexOf(separator, idx - 1);
			return locationStr.substring(idx + 1);
		} else if (idx > 0) {
			return locationStr.substring(idx + 1);
		}
		return null;
	}
	
	
	public static String getImplementationVersion(Class clazz) {
		if (clazz == null) return null;
		
		final Package pkg = clazz.getPackage();
		if (pkg != null) {
			String v = pkg.getImplementationVersion();
			if (v != null) return v;
		}
		return null;
	}
	
	
	private static boolean isFolder(int idx, String text) {
		return (idx != -1 && idx + 1 == text.length());
	}

	public static Class loadClass(String className) {
		return loadClass(Thread.currentThread().getContextClassLoader(), className);
	}
	
	public static Class loadClass(ClassLoader cl, String className) {
		if (cl == null) {
			return null;
		}
		try {
			return cl.loadClass(className);
		} catch (ClassNotFoundException e1) {
			return null;
		} catch (NoClassDefFoundError e1) {
			return null;
		} catch (Exception e) {
			e.printStackTrace(); // this is unexpected
			return null;
		}
	}
}
