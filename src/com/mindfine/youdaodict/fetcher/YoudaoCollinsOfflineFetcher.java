package com.mindfine.youdaodict.fetcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * 词库有两个文件：youdaocollins.idx、youdaocollins.exp<br>
 * 先生成.exp词库及解释文件，然后再根据这份文件生成.idx索引文件<br>
 * 词库文件内容描述了单词的位置，该位置按字节描述：<br>
 * 单词名称:单词类型 :解释位置:解释长度<br>
 * name:CET4,TEM4:1300:30<br>
 * what:TEM8:1330:54<br>
 * @author mindfine
 */
public class YoudaoCollinsOfflineFetcher implements Fetcher{
	
	Fetcher.StyleType styleType = Fetcher.StyleType.plain;
	HashMap<String, Indexer> idxMap = new HashMap<String, Indexer>();
	
	protected class Indexer {
		String wordName;
		String type;
		long bytePos;
		int byteCount;
		public Indexer(String wordName, String type, String bytePos, String byteCount){
			this.wordName = wordName;
			this.type = type;
			this.bytePos = Long.parseLong(bytePos);
			this.byteCount = Integer.parseInt(byteCount);
		}
	}

	@Override
	public String getResFromWord(String word) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(System.getProperty("user.dir") + "/youdaocollins.idx");
			br = new BufferedReader(fr);
			String bufTpr = null;//缓存着每一条索引
			while((bufTpr = br.readLine()) != null) {
				String [] units = bufTpr.split(":");
				if(units.length >= 4) {
					String wordName = units[0];
					String type = units[1];
					String bytePos = units[2];
					String byteCount = units[3];
					idxMap.put(wordName, new Indexer(wordName, type, bytePos, byteCount));
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("词典索引未找到");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("索引读取错误");
			e.printStackTrace();
		} finally {
				try {
					if(fr != null) {
						fr.close();
					} if (br != null) {
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		//从map中获取单词索引，调用getLinesFrom方法获取单词解释
		Indexer indexer = idxMap.get(word);
		if(indexer != null) {
			String explainStr = getExplainByBytes(indexer.bytePos, indexer.byteCount);
			return explainStr;
		} else {
			return null;
		}
	}
	

	public String getLinesFrom(long start, int length){
		StringBuilder explain = new StringBuilder();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(System.getProperty("user.dir") + "/youdaocollins.exp");
			br = new BufferedReader(fr);
			for(int i = 0; i < start - 1; i++) {
				br.readLine();
			}
			for(int i = 0; i < length; i++) {
				String tpr = br.readLine();
				if (tpr != null && !tpr.equals("")) {
					explain.append(tpr + "\r\n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("读取词库发生错误");
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return explain.toString();
	}
	public String getExplainByBytes(long skip, int length){
		RandomAccessFile raf = null;
		String explain = null;
		try {
			raf = new RandomAccessFile(System.getProperty("user.dir") + "/youdaocollins.exp", "r");
			raf.skipBytes((int) skip);
			byte[] temp = new byte[length];
			raf.read(temp);

			explain = new String(temp);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return explain;
	}

	@Override
	public StyleType getStyleType() {
		return styleType;
	}

	@Override
	public void setStyleType(StyleType styleType) {
	}


	@Override
	public String getSplitedFromWord(String word2) {
		// TODO 未实现离线查询分裂词库
		return null;
	}
	

}
