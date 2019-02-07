package com.healthsystem.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImageUtil {

	private ImageUtil() {

	}

	public static boolean isJPEG(InputStream is) {
		try {
			DataInputStream ins = new DataInputStream(is);
			try {
				if (ins.readInt() == 0xffd8ffe0) {
					return true;
				} else {
					return false;

				}
			} finally {
				ins.close();
			}

		} catch (Exception e) {
			return false;
		}
	}
}
