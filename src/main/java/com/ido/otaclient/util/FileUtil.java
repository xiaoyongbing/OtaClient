package com.ido.otaclient.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * 文件操作辅助类
 */
public class FileUtil {

	private static final String TAG = "FileUtil";


	/**
	 * 确保目录存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean ensureDir(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return ensureDir(file);
	}

	/**
	 * 确保目录存在
	 *
	 * @param path
	 * @return
	 */
	public static boolean ensureDir(File path) {
		boolean ret = false;

		if (!path.exists() || !path.isDirectory()) {
			try {
				ret = path.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} else {
			ret = true;
		}

		return ret;
	}


	/**
	 * 确保文件所在的目录存在
	 * 
	 * @param path
	 *            文件全路径
	 * @return
	 */
	public static boolean ensureFileParentDir(String path) {
		String parentDir = getFileParentAbsPath(path);
		return ensureDir(parentDir);
	}

	/**
	 * 确保文件存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean ensureFile(String path) {
		if (null == path) {
			return false;
		}

		boolean ret = false;

		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			try {
				file.createNewFile();
				ret = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName) {
		String fileType = null;
		if (fileName != null) {
			int idx = fileName.lastIndexOf(".");
			if (idx > 0) {
				fileType = fileName.substring(idx + 1, fileName.length())
						.toLowerCase();
			}
		}
		return fileType;
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromPath(String filePath) {
		String name = null;
		if (filePath != null) {
			int idx = filePath.lastIndexOf("/");
			if (idx > 0) {
				name = filePath.substring(idx + 1, filePath.length());
			} else {
				name = filePath;
			}
		}
		return name;
	}

	/**
	 * 获取无后缀文件名
	 *
	 * @param filePath
	 * @return
	 */
	public static String getNoSuffixFileNameFromPath(String filePath) {
		String name = null;
		if(filePath != null){
			name = getFileNameFromPath(filePath);
			name = name.replaceAll("[.][^.]+$", "");
		}
		return name;
	}


	/**
	 * 返回文件的所在的目录的绝对路径
	 * 
	 * @param filePath
	 * @return 返回文件的所在的目录的绝对路径,不含最后的斜杠分隔符
	 */
	public static String getFileParentAbsPath(String filePath) {
		File file = new File(filePath);
		return file.getParent();
	}

	/**
	 * 判断两个路径是否相等 大小写不敏感 : 存储卡的文件系统一般为FAT, 大小写不敏感
	 * 
	 * @param pathSrc
	 * @param pathDst
	 * @return
	 */
	public static boolean isPathEqual(final String pathSrc, final String pathDst) {
		if (pathSrc == null || pathDst == null) {
			return false;
		}

		String path1 = pathSrc.endsWith("/") ? pathSrc : pathSrc + "/";
		String path2 = pathDst.endsWith("/") ? pathDst : pathDst + "/";
		boolean isEqual = path1.equalsIgnoreCase(path2);
		return isEqual;
	}

	/**
	 * 压缩文件到zip. 如果耗时可以放在子线程里进行
	 * 
	 * @param srcFilePath
	 * @return 如果成功，zip文件名，失败null
	 */
	public static String zipFile(final String srcFilePath) {
		if (srcFilePath == null)
			return null;

		File srcFile = new File(srcFilePath);
		if (!srcFile.exists())
			return null;
		String destFileName = null;
		FileInputStream srcInput=null;
		BufferedInputStream srcBuffer = null;
		FileOutputStream destFileStream = null;
		BufferedOutputStream destBuffer = null;
		ZipOutputStream zipStream = null;
		try {
			srcInput = new FileInputStream(srcFile);
			srcBuffer = new BufferedInputStream(srcInput);
			byte[] buf = new byte[1024];
			int len;
			destFileName = srcFilePath + ".zip";
			File destFile = new File(destFileName);
			if (destFile.exists())
				destFile.delete();

			destFileStream = new FileOutputStream(destFileName);
			destBuffer = new BufferedOutputStream(destFileStream);
			zipStream = new ZipOutputStream(destBuffer);// 压缩包
			ZipEntry zipEntry = new ZipEntry(srcFile.getName());// 这是压缩包名里的文件名
			zipStream.putNextEntry(zipEntry);// 写入新的 ZIP 文件条目并将流定位到条目数据的开始处

			while ((len = srcBuffer.read(buf)) != -1) {
				zipStream.write(buf, 0, len);
				zipStream.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtil.close(srcInput);
			IOUtil.close(srcBuffer);
			IOUtil.close(destFileStream);
			IOUtil.close(destBuffer);
			IOUtil.close(zipStream);
		}

		return destFileName;
	}

	/**
	 * 获取文件类型（后缀）
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getFileTypeByName(String name, String defaultValue) {
		String type = defaultValue;
		if (name != null) {
			int idx = name.lastIndexOf(".");
			if (idx != -1) {
				type = name.substring(idx + 1, name.length());
			}
		}
		return type;
	}

	/**
	 * 判断文件是否存在
	 */
	public static boolean isFileExist(String path) {

		if (TextUtils.isEmpty(path)) {
			return false;
		}

		File file = new File(path);
		return file.isFile();
	}

	/**
	 * 判断文件夹是否存在
	 */
	public static boolean isDirExist(String path) {

		if (TextUtils.isEmpty(path)) {
			return false;
		}

		File file = new File(path);
		return file.isDirectory();
	}

	/**
	 * 获取文件大小，单位字节
	 */
	public static long getFileSize(String path) {
		
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		
		long size = 0;
		File file = new File(path);
		if (file.isFile()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return 若是文件不存在或者被删除都会返回 true
	 */
	public static boolean deleteFile(String path) {
		if (null == path) {
			return true;
		}
		boolean ret = true;

		File file = new File(path);
		if (file.exists()) {
			ret = file.delete();
		}
		return ret;
	}

	/**
	 * Java：判断文件的编码
	 * 
	 * @param sourceFile
	 *            需要判断编码的文件
	 * @return String 文件编码
	 */
	public static String getFilecharset(File sourceFile) {
		String charset = null;
		byte[] first3Bytes = new byte[3];
		BufferedInputStream bis = null;
		try {
			// boolean checked = false;

			bis = new BufferedInputStream(
					new FileInputStream(sourceFile));
			bis.mark(0);

			int read = bis.read(first3Bytes, 0, 3);
			System.out.println("字节大小：" + read);
			
			if (read == -1) {
                bis.close();
                return null;
            }

			if (first3Bytes[0] == (byte) 0x5c
			 && first3Bytes[1] == (byte) 0x75) {
				charset = "ANSI";   // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {

				charset = "UTF-16LE"; // 文件编码为 Unicode
				// checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {

				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				// checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {

				charset = "UTF-8"; // 文件编码为 UTF-8
				// checked = true;
			} else {
			    charset = "GBK";
            }
			  
			
			bis.reset();

			/*
			 * if (!checked) { int loc = 0;
			 * 
			 * while ((read = bis.read()) != -1) { loc++; if (read >= 0xF0)
			 * break; if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
			 * break; if (0xC0 <= read && read <= 0xDF) { read = bis.read(); if
			 * (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF) // (0x80 // -
			 * 0xBF),也可能在GB编码内 continue; else break; } else if (0xE0 <= read &&
			 * read <= 0xEF) {// 也有可能出错，但是几率较小 read = bis.read(); if (0x80 <=
			 * read && read <= 0xBF) { read = bis.read(); if (0x80 <= read &&
			 * read <= 0xBF) { charset = "UTF-8"; break; } else break; } else
			 * break; } } // System.out.println( loc + " " +
			 * Integer.toHexString( read ) // ); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(bis);
		}

		return charset;
	}


	/**
	 * 复制文件
	 * @param pathFrom
	 * @param pathTo
	 * @throws IOException
     */
	public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo, boolean rewrite) throws IOException {

		if(pathFrom.equals(pathTo)){
			return;
		}

		File from = new File(pathFrom);
		File to = new File(pathTo);

		if(rewrite && to.exists()){
			to.delete();
		}

		FileInputStream is = new FileInputStream(from);
		FileOutputStream os = new FileOutputStream(to);
		copyFile(is,os);

	}

	public static void copyFile(FileInputStream is, FileOutputStream os){
		FileChannel outputChannel = null;
		FileChannel inputChannel = null;
		try {
			inputChannel = is.getChannel();
			outputChannel = os.getChannel();
			inputChannel.transferTo(0, inputChannel.size(), outputChannel);
			inputChannel.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		finally {
			IOUtil.close(inputChannel);
			IOUtil.close(outputChannel);
		}
	}

	/**
	 * 判断一个本地文件是否是图片
	 * @param absolutePath
	 * @return
	 */
	public static boolean isFilePicture(String absolutePath){
	    boolean isPicture = false;
	    
	    if (!TextUtils.isEmpty(absolutePath)) {
//            String[] tmpStrs = absolutePath.split("\\.");
            String[] tmpStrs = TextUtils.split(absolutePath, "\\.");
            if (tmpStrs.length > 1) {

                final String[] PIC_SUFFIX = {
                        "jpg",
                        "png",
                        "bmp",
                        "jpeg",
                        "gif"
                };

                String suffix = tmpStrs[tmpStrs.length - 1];

                for (int i = 0; i < PIC_SUFFIX.length; i++) {
                    if (PIC_SUFFIX[i].equalsIgnoreCase(suffix)) {
                        isPicture = true;
                        break;
                    }
                }
            }
        }

	    // 尝试解码看是否是一张图片
	    if (!isPicture) {

	        Options options = new Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(absolutePath, options);
	        if (options.outHeight > 0 && options.outWidth > 0) {
	            isPicture = true;
	        }
        }
	    
	    return isPicture;
	}


	/**
     * 判断一个本地文件是否是图片
	 * @param activity
     * @param uri
     * @return
     * 注意：这个方法不靠谱
     */
	@Deprecated
    public static boolean isFilePicture(Activity activity, Uri uri){
        String absolutePath = FileUtil.getPath(activity,uri);
        return isFilePicture(absolutePath);
    }

	/**
	 * 判断一个本地文件是否是视频
	 * @param absolutePath
	 * @return
	 */
	public static boolean isFileVideo(String absolutePath){

		if(TextUtils.isEmpty(absolutePath)){
			return false;
		}

		return isFileVideo(new File(absolutePath));
	}

	/**
	 * 判断一个本地文件是否是视频
	 * @param absoluteFile
	 * @return
	 */
	public static boolean isFileVideo(File absoluteFile){
		if(absoluteFile == null || !absoluteFile.exists() || absoluteFile.isDirectory()){
			return false;
		}

		final String absolutePath = absoluteFile.getAbsolutePath().toLowerCase();
		return absolutePath.endsWith(".mp4");
	}

	/**
	 * 判断是否存在SD卡
	 * @return
     */
	public static boolean isSDCardExist() {
        return Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment
                .getExternalStorageState());
    }

	/**
	 * 将文件读取到内存。
	 * <p>该方法适用于读取小文件，因为这是一次性的</p>
	 * @param file 文件实例
	 * @return 如果 file 为null、指向的文件不存在或者是文件夹将返回 byte[0]
     */
	public static byte[] readFile(File file) {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[(int) file.length()];
			fis.read(buf);
			return buf;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}finally {
			IOUtil.close(fis);
		}
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 * @author paulburke
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 * @author paulburke
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 * @author paulburke
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 * @author paulburke
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} catch (IllegalArgumentException ex) {
			Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", ex.getMessage()));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.<br>
	 * <br>
	 * Callers should check whether the path is local before assuming it
	 * represents a local file.
	 *
	 * @param context The context.
	 * @param uri     The Uri to query.
	 * @author paulburke
	 */
	/*@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
//				final String[] split = docId.split(":");
				final String[] split = TextUtils.split(docId, ":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				//解决#2338的bug ----start-----
				//Caused by java.lang.NumberFormatException
				//For input string: "raw:/storage/emulated/0/Download/FULL VERSION.mp4"
				//可以发现raw后面返回的其实就是真实路径，所以我们直接取出来返回就好了
				if (id.startsWith("raw:")) {
					return id.replaceFirst("raw:", "");
				}
				//解决#2338的bug ----end-----
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
//				final String[] split = docId.split(":");
				final String[] split = TextUtils.split(docId,":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri)) {
				return uri.getLastPathSegment();
			}

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}*/


	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			//一些三方的文件浏览器会进入到这个方法中，例如ES
			//QQ文件管理器不在此列
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();


			if (isQQMediaDocument(uri)) {
				String path = uri.getPath();
				File fileDir = Environment.getExternalStorageDirectory();
				File file = new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
				return file.exists() ? file.toString() : null;
			}

			return getDataColumn(context, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {// File

			return uri.getPath();
		}
		return null;
	}

	/**
	 * 使用第三方qq文件管理器打开
	 *
	 * @param uri
	 *
	 * @return
	 */
	public static boolean isQQMediaDocument(Uri uri) {
		return "com.tencent.mtt.fileprovider".equals(uri.getAuthority());
	}


	public static Uri toProviderUri(Context context, String path){
		File file = new File(path);
		String authority = context.getPackageName() + ".provider";
		return FileProvider.getUriForFile(context, authority, file);
	}

	/**
	 * file --> uri
	 * @param context
	 * @param path
	 *
	 * @return
	 */
	public static Uri getUriFromFile(Context context, String path) {
		if (context == null || path == null) {
			throw new NullPointerException();
		}
		File file = new File(path);
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			String authority = context.getPackageName() + ".provider";
			return FileProvider.getUriForFile(context, authority, file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}

	/** 删除文件名的后缀 */
	public static String deleteSuffix(String name) {

		int lastIndex = name.lastIndexOf(".");

		// 没有后缀
		if(lastIndex < -1){
			return name;
		}
		else{
			return name.substring(0,lastIndex);
		}
	}

	/**
	 * 添加记录到媒体库
	 * @param context 	context
	 * @param path		文件绝对路径
	 */
	public static void addToMediaStore(Context context, String path) {
		Intent sanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(new File(path));
		sanIntent.setData(uri);
		context.sendBroadcast(sanIntent);
	}

	/**
	 * 从媒体库删除记录
	 * @param context	context
	 * @param path		文件绝对路径
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static boolean removeFromMediaStore(Context context, String path){
		Objects.requireNonNull(context);
		Objects.requireNonNull(path);
		Uri uri = MediaStore.Files.getContentUri("external");
		String selection = MediaStore.Files.FileColumns.DATA + " LIKE ? ";
		ContentResolver contentResolver = context.getContentResolver();
		contentResolver.delete(uri,selection,new String[]{path});
		return deleteFile(path);
	}

	/**
	 * 保存二进制数据
	 * @param path 要保存到的路径
	 * @param data 要保存的数据
	 * @return true 保存成功, 其它保存失败
	 */
	public static boolean save(String path, byte[] data) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(path));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(bos);
		}
		return isFileExist(path);
	}

	/**
	 * 解压Zip文件
	 * @param desPath 解压目的地目录
	 * @param zipFile zip文件名(包含去路径)
	 * @return true 表示解压成功,false 表示解压失败.
	 */
	public static boolean unpackZip(String desPath, String zipFile) {
		if (!zipFile.endsWith(".zip")) {
			return false;
		}
		File des = new File(desPath);
		if (!des.exists()) {
			des.mkdir();
		}
		InputStream is = null;
		ZipInputStream zis = null;
		FileOutputStream fout = null;
		try {
			String filename;
			is = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count;

			while ((ze = zis.getNextEntry()) != null) {
				String zeName = ze.getName();
				if (zeName.contains(File.separator)) {
					filename = zeName.substring(zeName.indexOf(File.separator) + 1);
				} else {
					filename = zeName;
				}

				// Need to create directories if not exists, or
				// it will generate an Exception...
				if (ze.isDirectory()) {
					File fmd = new File(desPath + filename);
					fmd.mkdirs();
					continue;
				}

				fout = new FileOutputStream(desPath + filename);

				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);
				}

				fout.close();
				zis.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			IOUtil.close(is);
			IOUtil.close(zis);
			IOUtil.close(fout);
		}

		return true;
	}

	/**
	 * 从指定的目录中,获取指定后缀的第一个固件模块升级文件(不包含隐藏文件)
	 * @param dirPath 需要获取文件的目录
	 * @param suffix 后缀类型
	 * @return 返回符合指定后缀的第一个固件模块升级文件
	 */
	public static File getFirstUpgradeFileBySuffix(String dirPath, String suffix) {
		File file = new File(dirPath);
		if (!file.exists()) {
			return null;
		}
		if (file.isFile()) {
			if (file.getName().endsWith(suffix)) {
				return file;
			}
		} else {
			for (File f : file.listFiles()) {
				if (f.isFile()) {
					if (f.isHidden()) {
						//Ignore hidden file
						continue;
					}
					if (f.getName().endsWith(suffix)) {
						return f;
					}
				} else if (f.isDirectory()) {
					File sub =  getFirstUpgradeFileBySuffix(f.getPath(), suffix);
					if (sub != null && sub.getName().endsWith(suffix)) {
						return sub;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 创建文件
	 * @param dirPath
	 * @return
	 */
	public static String createDir(String dirPath){
		//因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
		try{
			File file=new File(dirPath);
			if(file.getParentFile().exists()){
				file.mkdir();
				Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
				return file.getAbsolutePath();

			}
			else {
				createDir(file.getParentFile().getAbsolutePath());
				file.mkdir();
				Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return dirPath;
	}


	public static String createFile(File file){
		try{
			if(file.getParentFile().exists()){
				Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
				file.createNewFile();
			}
			else {
				//创建目录之后再创建文件
				createDir(file.getParentFile().getAbsolutePath());
				file.createNewFile();
				Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}


}
