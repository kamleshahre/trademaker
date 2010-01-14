package org.lifeform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class FileUtil {
	public static List<int[]> readIntegerData(final String filename) throws Exception {
		Vector<int[]> result = new Vector<int[]>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				int maxlen = st.countTokens();
				int len = 0;
				int[] array = new int[maxlen];
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					if (tok.startsWith("#")) // commented int
						continue;
					array[len++] = Integer.parseInt(tok);
				}
				if (len != maxlen)
					array = ArrayUtils.trim(array, len);
				result.add(array);
				line = in.readLine();
			}
		} finally {
			in.close();
		}
		return result;
	}

	public static List<double[]> readDoubleData(final String filename)
			throws Exception {
		Vector<double[]> result = new Vector<double[]>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				int maxlen = st.countTokens();
				int len = 0;
				double[] array = new double[maxlen];
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					if (tok.startsWith("#")) // commented int
						continue;
					array[len++] = Double.parseDouble(tok);
				}
				if (len != maxlen) {
					array = ArrayUtils.trim(array, len);
				}
				result.add(array);
				line = in.readLine();
			}
		} finally {
			in.close();
		}
		return result;
	}

	public static List<String> getFilesNames(final String dirName) {
		List<String> fileList = new ArrayList<String>();
		File fileDir = new File(dirName);
		String[] dirList = fileDir.list();
		for (int i = 0; i < dirList.length; i++) {
			File f = new File(fileDir, dirList[i]);
			if (f.isDirectory()) {
				String filePath = f.getPath();
				List<String> subDirFileList = getFilesNames(filePath);
				fileList.addAll(subDirFileList);
				continue;
			}
			fileList.add(f.getPath());
		}
		return fileList;
	}

}
