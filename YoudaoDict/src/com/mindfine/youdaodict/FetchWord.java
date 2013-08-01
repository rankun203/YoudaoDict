package com.mindfine.youdaodict;

import com.mindfine.youdaodict.fetcher.Fetcher;

/**
 * <h3>爬单词<h3><br>
 * 1.指定单词和爬到的数据存储的位置,示例：java FetchWord -w many -d /home/mindfine/dict/<br>
 * 2.指定单词数据文件及数据存储位置,示例：java FetchWord -s /home/mindfine/dict.txt -d
 * /home/mindfine/dict<br>
 * 
 * @author mindfine
 */
public class FetchWord {
	private static String word = "";
	private static String saveToDir = "";
	private static String srcWordFile = "";
	private static String wantDic = "";

	private Fetcher fetcher;

	// resolve args from commandLine
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg != null && arg != "") {
				if (arg.equals("-w")) {
					word = args[i + 1];
				} else if (arg.equals("-s")) {
					srcWordFile = args[i + 1];
				} else if (arg.equals("-d")) {
					saveToDir = args[i + 1];
				} else if (arg.equals("-c")) {
					wantDic = args[i + 1];
				} else if (arg.equals("-h")) {
					printHelp();
				} else {
					printHelp();
				}
			}
		}

		if (word != null && !word.equals("")) {
			
		}

	}

	private static void printHelp() {
		System.out.println("描述：网页词典内容截取器\r\n" +
"用法：FetchWord -w|-s -d\r\n" +
"  -w          指定单词\r\n" +
"  -d          指定路径存放抓取结果\r\n" +
"  -s          指定单词列表文件，程序扫描该文件读取所有单词并抓取其释义\r\n" +
"  -h          显示本帮助\r\n" +
"\r\n" +
"请在项目主页报告程序的错误，欢迎任何形式的复制和转发\r\n" +
"项目主页：https://github.com/rankun203/YoudaoDict\r\n");
	}

	public Fetcher getFetcher() {
		return fetcher;
	}

	public void setFetcher(Fetcher fetcher) {
		this.fetcher = fetcher;
	}
}
