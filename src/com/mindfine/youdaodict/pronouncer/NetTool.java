package com.mindfine.youdaodict.pronouncer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTool {
	public void download(String downFromUrl, String saveTo, String fileName) {
		prepare(saveTo, fileName);
		int nStartPos = 0;
		int nRead = 0;
		RandomAccessFile oSavedFile = null;
		try {
			URL url = new URL(downFromUrl);
			// 打开连接
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			// 获得文件长度
			long nEndPos = getFileSize(downFromUrl);
			oSavedFile = new RandomAccessFile(saveTo + fileName, "rw");
			httpConnection
					.setRequestProperty("User-Agent", "Internet Explorer");
			String sProperty = "bytes=" + nStartPos + "-";
			// 告诉服务器book.rar这个文件从nStartPos字节开始传
			httpConnection.setRequestProperty("RANGE", sProperty);
			InputStream input = httpConnection.getInputStream();
			byte[] b = new byte[1024];
			// 读取网络文件,写入指定的文件中
			while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
				oSavedFile.write(b, 0, nRead);
				nStartPos += nRead;
			}
			httpConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(oSavedFile != null) {
				try {
					oSavedFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void prepare(String saveTo, String fileName) {
		File saveToFile = new File(saveTo);
		File fileNameFile = new File(saveTo + fileName);
		if(!saveToFile.exists()) {
			if(!saveToFile.mkdirs()) {
				System.err.println("创建文件夹失败！");
			}
		}
		if(!fileNameFile.exists()) {
			try {
				if(!fileNameFile.createNewFile()) {
					System.out.println("未知原因，创建文件失败！");
				}
			} catch (IOException e) {
				System.out.println("操作发生异常，创建文件失败！");
				e.printStackTrace();
			}
		}
	}

	// 获得文件长度
	public static long getFileSize(String sURL) {
		int nFileLength = -1;
		try {
			URL url = new URL(sURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "Internet Explorer");

			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				System.err.println("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nFileLength;
	}

}