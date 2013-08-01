package com.mindfine.youdaodict;

/**
 * <h3>爬单词<h3><br>
 * 1.指定单词和爬到的数据存储的位置,示例：java FetchWord -w many -d /home/mindfine/dict/<br>
 * 2.指定单词数据文件及数据存储位置,示例：java FetchWord -s /home/mindfine/dict.txt -d
 * /home/mindfine/dict<br>
 * 
 * @author mindfine
 */
public class FetchWord {
	private static String sourceWord = "";
	private static String saveToDic = "";
	private static String srcWordFile = "";
	public static String fetchFromURL = "http://dict.youdao.com/search";

	// resolve args from commandLine
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg != null && arg != "") {
				if (arg.equals("-w")) {
					sourceWord = args[i + 1];
				} else if (arg.equals("-s")) {
					srcWordFile = args[i + 1];
				} else if (arg.equals("-d")) {
					saveToDic = args[i + 1];
				}
			}
		}

		System.out.println("sourceWord=" + sourceWord + ", saveToDic="
				+ saveToDic + ", srcWordFile=" + srcWordFile);
		
		
	}
}
