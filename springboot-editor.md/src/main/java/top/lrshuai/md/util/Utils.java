package top.lrshuai.md.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class Utils {

	 	/**
		 * 上传文件
		 * @param filePath 文件名 
		 * @param in   io流
		 * @return  返回最终的路径
		 * @throws IOException 
		 */
		public static String uploadImg(String rootPATH,String filePath,InputStream in) throws IOException{
			System.out.println("rootPATH="+rootPATH);
			System.out.println("filePath="+filePath);
			String rusultPath = rootPATH+filePath;
			createFile(rusultPath);
			File realFile =new File(rusultPath);
			FileUtils.copyInputStreamToFile(in, realFile);
			return "/upload"+filePath;
		}
		
		
		/**
		 * 创建文件，如果文件夹不存在将被创建
		 * 
		 * @param destFileName
		 *            文件路径
		 */
		public static File createFile(String destFileName) {
			File file = new File(destFileName);
			if (file.exists())
				return null;
			if (destFileName.endsWith(File.separator))
				return null;
			if (!file.getParentFile().exists()) {
				if (!file.getParentFile().mkdirs())
					return null;
			}
			try {
				if (file.createNewFile())
					return file;
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		/**
		 * 返回随机数
		 * 
		 * @param n 个数
		 * @return
		 */
		public static String random(int n) {
			if (n < 1 || n > 10) {
				throw new IllegalArgumentException("cannot random " + n + " bit number");
			}
			Random ran = new Random();
			if (n == 1) {
				return String.valueOf(ran.nextInt(10));
			}
			int bitField = 0;
			char[] chs = new char[n];
			for (int i = 0; i < n; i++) {
				while (true) {
					int k = ran.nextInt(10);
					if ((bitField & (1 << k)) == 0) {
						bitField |= 1 << k;
						chs[i] = (char) (k + '0');
						break;
					}
				}
			}
			return new String(chs);
		}
}
