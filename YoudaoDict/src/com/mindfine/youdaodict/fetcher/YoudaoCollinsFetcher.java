package com.mindfine.youdaodict.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 从有道词典截取Collins的释义
 * 
 * @author mindfine
 */
public class YoudaoCollinsFetcher implements Fetcher {
	public String fetchFromURL = "http://dict.youdao.com/search";
	public Fetcher.StyleType styleType;

	@Override
	public String getResFromWord(String word) {
		return jsoupFetcher(word);
	}

	/**
	 * @param word 要查询的单词
	 * @return 返回使用Jsoup取得的释义数据
	 */
	public String jsoupFetcher(String word) {
		String rtn = "";
		String queryUrl = fetchFromURL + "?q=" + word;
		try {
			URL qu = new URL(queryUrl);
			Document doc = Jsoup.parse(qu, 16000);
			if (styleType == StyleType.plain) {
				if(doc != null) {
					rtn = getPlainExplain(doc, word);
				}
			} else {
				Element collinsRes = doc.getElementById("collinsResult");
				rtn = collinsRes.html();
			}
		} catch (MalformedURLException e) {
			System.err.println("ERROR, queryUrl is invalid.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("open connection failed.\r\nNetwork may not accessible.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("NullPointer");
			return null;
		}
		return rtn;
	}

	private String getPlainExplain(Document doc, String word) {
		StringBuilder s = new StringBuilder();
		Element ctnEle = doc.getElementById("collinsResult").child(0).child(0)
				.child(0).child(0);
		Element headEle = ctnEle.child(0);
		Element introEle = ctnEle.select("p.collins-intro").first();
		Element meansEle = ctnEle.select("ul.ol").first();
		s.append("----------http://dict.youdao.com----------\r\n\r\n");
		appendCtn(s, headEle.select("span.title"));
		s.append(" ");
		appendCtn(s, headEle.select("em.phonetic"));
		
		//几星词汇
		String star = headEle.select("span.star").attr("class");
		int starNo = 0;
		if (star != null) {
			if (star.indexOf("1") != -1)
				starNo = 1;
			else if (star.indexOf("2") != -1)
				starNo = 2;
			else if (star.indexOf("3") != -1)
				starNo = 3;
			else if (star.indexOf("4") != -1)
				starNo = 4;
			else if (star.indexOf("5") != -1)
				starNo = 5;
			s.append(" [");
			for (int i = 0; i < starNo; i++) {
				if (i != (starNo - 1)) {
					s.append("* ");
				} else {
					s.append("*");
				}
			}
			s.append("] ");
		}
		Elements additionalPtn = headEle.select("span.additional");
		if (additionalPtn.size() > 0) {
			s.append(additionalPtn.get(0).text() + " ");
		}
		s.append("|" + headEle.select("span.rank").text() + "| ");
		if (additionalPtn.size() > 1) {
			s.append(additionalPtn.get(1).text().trim() + " ");
		}
		if (introEle != null) {
			s.append("\r\n");
			s.append("+---------------------------------\r\n");
			s.append("|" + introEle.text());
			s.append("\r\n+---------------------------------\r\n");
		}

		s.append("\r\n------------------------------------------------------\r\n");
		// 开始解析释义部分
		for (Element meanItem : meansEle.children()) {
			if(meanItem.children().size() > 0) {
				Element statItem = meanItem.child(0);// 标有数字的句子
				String meanNo = statItem.child(0).text().replaceAll("&nbsp;", "");
				appendCtn(s, meanNo);

				String statItem1 = statItem.child(1).text();// 例子的正文文字
				//把句子中的关键词加上括号
				Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(statItem1);
				if (m.find()) {
					String wordTemp = m.group();
					s.append(statItem1.replace(wordTemp, "[" + wordTemp + "]"));
				}

				s.append("\r\n");
				Elements examplesLists = meanItem.select("div.exampleLists");//例句们
				for (Element exampleList : examplesLists) {
					s.append("    ~");
					appendCtn(s, exampleList.select("span.collinsOrder"));
					s.append(exampleList.select("div.examples").get(0).child(0).text());
					s.append("\r\n         ");
					s.append(exampleList.select("div.examples").get(0).child(1).text());
					s.append("\r\n");
				}
			}
			s.append("------------------------------------------------------\r\n");
		}
		return new String(s);
	}

	/**
	 * 添加了非空判断的功能
	 * @param s 要给谁后面追加字符串？
	 * @param string 追加的字符串是？
	 */
	private boolean appendCtn(StringBuilder s, String string) {
		if (string != null && !string.equals("")) {
			s.append(string);
			return true;
		}
		return false;
	}
	/**
	 * 添加了非空判断的功能
	 * @param s 要给谁后面追加字符串？
	 * @param string 要从哪个元素中提取字符串？
	 */
	private boolean appendCtn(StringBuilder s, Elements eles) {
		if (eles != null) {
			String firstText = eles.first().text();
			s.append(firstText);
			return true;
		}
		return false;
	}

	/**
	 * @param word
	 *            要查询的单词
	 * @return 根据单词得到的整个页面
	 */
	public String fetchPage(String word) {
		String queryUrl = fetchFromURL + "?q=" + word;
		String returnString = "";

		try {
			StringBuffer htmlBuffer = new StringBuffer();
			URL qu = new URL(queryUrl);
			URLConnection conn = qu.openConnection();
			InputStream srcIs = conn.getInputStream();
			int ch;
			while ((ch = srcIs.read()) != -1) {
				htmlBuffer.append((char) ch);
			}
			returnString = new String(htmlBuffer);
			returnString = new String(returnString.getBytes("ISO8859-1"),
					"utf-8");
		} catch (MalformedURLException e) {
			System.err.println("ERROR, queryUrl is invalid.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("open connection failed.");
			e.printStackTrace();
		}
		return returnString;
	}

	@Override
	public Fetcher.StyleType getStyleType() {
		return styleType;
	}

	@Override
	public void setStyleType(Fetcher.StyleType styleType) {
		this.styleType = styleType;
	}
}
