package com.mindfine.youdaodict;

import com.mindfine.youdaodict.fetcher.Fetcher;
import com.mindfine.youdaodict.fetcher.YoudaoCollinsFetcher;
import com.mindfine.youdaodict.fetcher.YoudaoCollinsOfflineFetcher;
import com.mindfine.youdaodict.pronouncer.Pronouncer;
import com.mindfine.youdaodict.pronouncer.YoudaoOfflinePronouncer;
import com.mindfine.youdaodict.pronouncer.YoudaoPronouncer;

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
	private static String saveToFile = "";
	private static String srcWordFile = "";
	private static String wantDic = "youdaocollins";
	private static Fetcher.DicType dicType;
	private static String wantStyle = "";
	private static Fetcher.StyleType styleType;
	private static String method = "";
	private static boolean offline = false;
	private static Fetcher fetcher;
	private static Pronouncer pronouncer;


	/**
	 * Entry of this program. resolve args from commandLine
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0] != null && !args[0].equals("") && !args[0].startsWith("-")) {
			styleType = Fetcher.StyleType.plain;
			dicType = Fetcher.DicType.youdaoCollins;
			fetchAndPrint(args[0]);
			System.exit(0);
		}

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg != null && !arg.equals("")) {
				if (arg.equals("-iw")) {
					word = args[i + 1];
				} else if (arg.equals("-is")) {
					srcWordFile = args[i + 1];
				} else if (arg.equals("-od")) {
					saveToDir = args[i + 1];
				} else if (arg.equals("-of")) {
					saveToFile = args[i + 1];
				} else if (arg.equals("-m")) {
					method = args[i + 1];
				} else if (arg.equals("-c")) {
					wantDic = args[i + 1];
				} else if (arg.equals("-s")) {
					wantStyle = args[i + 1];
				} else if (arg.equals("-h")) {
					printHelpAndExit();
				} else if (arg.equals("-io")) {
					offline = true;
				}
			}
		}

		resolveDicType();
		resolveStyleType();

		if (word != null && !word.equals("")) {
			if (method != null && !method.equals("")) {
				if (method.equals("print")) {
					fetchAndPrint(word);
				} else if (method.equals("folder")) {
					fetchAndSavetoDir(word);
				} else if (method.equals("file")) {
					fetchAndSavetoFile(word);
				} else {
					System.err.println("未知的输出方式，仅支持：print、folder、file三种方式");
					System.exit(0);
				}
			} else {
				fetchAndPrint(word);
			}
		} else if (srcWordFile != null && !srcWordFile.equals("")) {
			if (method != null && !method.equals("")) {
				if (method.equals("print")) {
					fetchAllAndPrint();
				} else if (method.equals("folder")) {
					fetchAllAndSavetoDir();
				} else if (method.equals("file")) {
					fetchAllAndSavetoFile();
				} else {
					System.err.println("未知的输出方式，仅支持：print、folder、file三种方式");
					System.exit(0);
				}
			} else {
				fetchAllAndPrint();
			}
		} else {
			System.err.println("不知道你要查什么单词，请用-iw指定单词或用-is指定包含单词列表的文件");
			System.exit(0);
		}
	}

	/**
	 * 解析用户指定的输出类型，将类型存入StyleType类型的变量styleType中
	 */
	private static void resolveStyleType() {
		if (wantStyle != null && !wantStyle.equals("")) {
			if (wantStyle.equals("plain")) {
				styleType = Fetcher.StyleType.plain;
			} else if (wantStyle.equals("singleStyle")) {
				styleType = Fetcher.StyleType.singleStyle;
			} else if (wantStyle.equals("separateStyle")) {
				styleType = Fetcher.StyleType.separateStyle;
			} else {
				styleType = Fetcher.StyleType.UNKNOWN;
			}
		} else {
			styleType = Fetcher.StyleType.plain;
		}
	}

	/**
	 * 解析用户指定的词典类型，将类型存入DicType类型的变量dicType中
	 */
	private static void resolveDicType() {
		if (wantDic != null && !wantDic.equals("")) {
			if (wantDic.equals("youdaocollins")) {
				dicType = Fetcher.DicType.youdaoCollins;
			} else {
				dicType = Fetcher.DicType.UNKNOWN;
			}
		} else {
			dicType = Fetcher.DicType.youdaoCollins;
		}
	}

	private static void fetchAllAndSavetoDir() {
		// TODO Auto-generated method stub

	}

	private static void fetchAllAndSavetoFile() {
		// TODO Auto-generated method stub

	}

	private static void fetchAllAndPrint() {
		// TODO Auto-generated method stub

	}

	private static void fetchAndSavetoFile(String word2) {
		// TODO Auto-generated method stub

	}

	private static void fetchAndSavetoDir(String word2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取单词的释义，然后打印这些释义
	 * 
	 * @param word2
	 *            要处理的单词
	 */
	private static void fetchAndPrint(String word2) {
		if (dicType != Fetcher.DicType.UNKNOWN) {
			if (dicType == Fetcher.DicType.youdaoCollins) {
				if(offline == true) {
					fetcher = new YoudaoCollinsOfflineFetcher();
					String rtnStr = fetcher.getResFromWord(word2);
					if(rtnStr == null) {
						System.out.println("你所查询的单词暂未收录。");
					} else {
						System.out.println(rtnStr);
					}
					pronouncer = new YoudaoOfflinePronouncer();
					pronouncer.pronounce(word2);
				} else {
					fetcher = new YoudaoCollinsFetcher();
					fetcher.setStyleType(styleType);
					String rtnStr = fetcher.getResFromWord(word2);
					if(rtnStr == null) {
						System.out.println("你所查询的单词暂未收录。");
					} else {
						System.out.println(rtnStr);
					}
					pronouncer = new YoudaoPronouncer();
					pronouncer.pronounce(word2);
				}
			}
		} else {
			System.out.println("抱歉，系统暂不支持该词典");
		}
	}

	private static void printHelpAndExit() {
		System.out.println("描述：网页词典内容截取器\r\n" +
"\r\n" +  
"用法：FetchWord [-m <outputType>][ -iw <word>|-is <file>][ -d <dir>|-of <file>][ -s <outputStyle> ][ -c <dictionary>][ -p <pronounceLocal>][ -e <executableMp3PlayerLocation>][ | -h]\r\n" +
"  \r\n" +
"  -iw     指定单词  \r\n" +
"  -is     指定单词列表文件，程序扫描该文件读取所有单词并抓取其释义  \r\n" +
"  -io     标记为离线查词，需要相关词库及读音文件，此选项不跟任何参数  \r\n" +
"  -od     指定一个文件夹存放抓取结果  \r\n" +
"  -of     指定一个文件存放抓取结果  \r\n" +
"  -m      指定输出类型  \r\n" +
"           默认是  \r\n" +
"            print  \r\n" +
"           可能的值有  \r\n" +
"            print    将结果打印在屏幕上  \r\n" +
"            folder   将结果存入指定的文件夹中  \r\n" +
"                      参数列表中必须包含-d选项并指明存储的位置  \r\n" +
"            file     将结果存入单个文件中  \r\n" +
"                      参数列表中必须包含-f选项并指明存储文件的位置  \r\n" +
"           通常情况下，不指定输出位置，程序将直接打印，否则输出结果到指定位置  \r\n" +
"            只要输出类型不特殊注明为其它输出方式：  \r\n" +
"             如果使用-of指定了输出文件，则输出结果到单独的文件  \r\n" +
"             如果使用-od指定了输出文件夹，则输出结果到指定的文件夹  \r\n" +
"  -c      指定词典  \r\n" +
"           默认值是  \r\n" +
"            youdaocollins  \r\n" +
"           可能的值有  \r\n" +
"            youdaocollins   从有道词典网站获取Collins词典的释义  \r\n" +
"  -s      指定输出内容的格式  \r\n" +
"           默认是  \r\n" +
"            plain  \r\n" +
"           可能的值有  \r\n" +
"            plain           纯文本方式显示内容  \r\n" +
"            singleStyle     将样式直接写入HTML中  \r\n" +
"			separateStyle   将样式分布在HTML页面之外的css文件中  \r\n" +
"  -p      读出单词的读音，如果未发声，说明没有相应的声音文件  \r\n" +
"           默认值为  \r\n" +
"            en  \r\n" +
"           可能的值有  \r\n" +
"            en              使用英国口音读出单词  \r\n" +
"            us              使用美国口音读出单词  \r\n" +
"            no              不要读出单词  \r\n" +
"  -e      设置可执行发声工具的路径，系统将调用该工具读出单词  \r\n" +
"           默认使用/usr/bin/mpg321  \r\n" +
"           可能的值有  \r\n" +
"            mpg321  \r\n" +
"  \r\n" +
"  -h      显示本帮助  \r\n" +
"  \r\n" +
"用法示例：  \r\n" +
"  \r\n" +
"1.查看帮助  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -h  \r\n" +
"  \r\n" +
"2.获取 hello 的释义并将结果打印出来  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -m print  \r\n" +
"  \r\n" +
"3.获取 hello 的释义并将结果存入 \"/home/mindfine/dict/\" 文件夹中  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -od /home/mindfine/dict/  \r\n" +
"  \r\n" +
"4.获取 hello 的释义并将结果存入 \"/home/mindfine/dict.txt\" 文件中  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -of /home/mindfine/dict.txt  \r\n" +
"  \r\n" +
"5.解析 \"/home/mindfine/words.txt\" 中的所有单词，并将释义存入 \"/home/mindfine/dict/\" 文件夹中  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -is /home/mindfine/words.txt -od /home/mindfine/dict/  \r\n" +
"  \r\n" +
"6.解析 \"/home/mindfine/words.txt\" 中的所有单词，并将释义写入 \"/home/mindfine/dict.txt\" 文件中  \r\n" +
"java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -is /home/mindfine/words.txt -of /home/mindfine/dict.txt  \r\n" +
"  \r\n" +
"  \r\n" +
"源单词文件的格式为：  \r\n" +
"hello  \r\n" +
"many  \r\n" +
"tiny  \r\n" +
"funny  \r\n" +
"...  \r\n" +
"  \r\n" +
"请在项目主页报告程序的错误，欢迎任何形式的复制和转发  \r\n" +
"项目主页：https://github.com/rankun203/YoudaoDict");
		System.exit(0);
	}

	public Fetcher getFetcher() {
		return fetcher;
	}

	public void setFetcher(Fetcher fetcher) {
		this.fetcher = fetcher;
	}
}
