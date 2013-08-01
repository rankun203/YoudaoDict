package com.mindfine.youdaodict;

import com.mindfine.youdaodict.fetcher.YoudaoCollinsFetcher;

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
				}
			}
		}

		System.out.println("sourceWord=" + word + ", saveToDir="
				+ saveToDir + ", srcWordFile=" + srcWordFile + ", wantDic=" + wantDic);

		System.out.println(new YoudaoCollinsFetcher().getResFromWord(word));
		
	}
}
