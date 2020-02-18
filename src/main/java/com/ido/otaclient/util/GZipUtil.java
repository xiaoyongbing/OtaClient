package com.ido.otaclient.util;

import android.util.Log;

import com.ido.otaclient.base.IDLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * GZip工具类
 */
public class GZipUtil {
	private static final String TAG = "GZipUtil";
	/**
	 * gzip 压缩
	 * @param str
	 * @return
	 */
	public static byte[] gzipStringToByte(byte[] str) {
		byte[] data = null;
		if (str != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				GZIPOutputStream gos = new GZIPOutputStream(baos);
				gos.write(str);
				gos.flush();
				gos.finish();
				gos.close();

				data = baos.toByteArray();
				baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * gzip 压缩
	 * @param str
	 * @return
	 */
	public static byte[] gzipStringToByte(String str) {
		byte[] data = null;
		if (str != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				GZIPOutputStream gos = new GZIPOutputStream(baos);
				gos.write(str.getBytes(Charset.defaultCharset()));
				gos.flush();
				gos.finish();
				gos.close();

				data = baos.toByteArray();
				baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * gzip 解压
	 * @param str
	 * @return
	 */
	public static String ungzipString(byte[] str) {
		String result = null;

		if (str != null) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(str);
				GZIPInputStream gis = new GZIPInputStream(bais);
				int bytes = 0;
				byte[] buf = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while (-1 != (bytes = gis.read(buf, 0, buf.length))) {
					baos.write(buf, 0, bytes);
				}

				result = baos.toString();
				baos.flush();
				baos.close();
				gis.close();
				bais.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * gzip 解压
	 * @param str
	 * @return
	 */
	public static String ungzipString(String str) {
		String result = null;

		if (str != null) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes(Charset.defaultCharset()));
				GZIPInputStream gis = new GZIPInputStream(bais);
				int bytes = 0;
				byte[] buf = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while (-1 != (bytes = gis.read(buf, 0, buf.length))) {
					baos.write(buf, 0, bytes);
				}

				result = baos.toString();
				baos.flush();
				baos.close();
				gis.close();
				bais.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void UnZipFolder(String zipFileString, String outPathString, String szName) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		while ((zipEntry = inZip.getNextEntry()) != null) {
			//szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				//获取部件的文件夹名
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {
				Log.e(TAG, outPathString + File.separator + szName);
				File file = new File(outPathString + File.separator + szName);
				if (!file.exists()) {
					Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				// 获取文件的输出流
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// 读取（字节）字节到缓冲区
				while ((len = inZip.read(buffer)) != -1) {
					// 从缓冲区（0）位置写入（字节）字节
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}



	/**
	 * 压缩文件和文件夹
	 *
	 * @param srcFileString 要压缩的文件或文件夹
	 * @param zipFileString 压缩完成的Zip路径
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
		//创建ZIP
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
		//创建文件
		File file = new File(srcFileString);
		//压缩
		IDLog.d("---->"+file.getParent()+"==="+file.getAbsolutePath());
		ZipFiles(file.getParent()+ File.separator, file.getName(), outZip);
		//完成和关闭
		outZip.finish();
		outZip.close();
	}


	/**
	 * 压缩文件
	 *
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @throws Exception
	 */
	private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
		IDLog.d("folderString:" + folderString + "\n" +
				"fileString:" + fileString + "\n==========================");
		if (zipOutputSteam == null)
			return;
		File file = new File(folderString + fileString);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}
			zipOutputSteam.closeEntry();
		} else {
			//文件夹
			String fileList[] = file.list();
			//没有子文件和压缩
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();
			}
			//子文件和递归
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString+fileString+"/",  fileList[i], zipOutputSteam);
			}
		}
	}


	/**
	 * 解压zip到指定的路径
	 *
	 * @param zipFileString ZIP的名称
	 * @param outPathString 要解压缩路径
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				//获取部件的文件夹名
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {
				Log.e(TAG, outPathString + File.separator + szName);
				File file = new File(outPathString + File.separator + szName);
				if (!file.exists()) {
					Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				// 获取文件的输出流
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// 读取（字节）字节到缓冲区
				while ((len = inZip.read(buffer)) != -1) {
					// 从缓冲区（0）位置写入（字节）字节
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}


}
