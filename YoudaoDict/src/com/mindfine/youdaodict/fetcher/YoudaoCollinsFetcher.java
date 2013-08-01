package com.mindfine.youdaodict.fetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 从有道词典截取Collins的释义
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
	public String jsoupFetcher(String word){
		String rtn = "";
		String queryUrl = fetchFromURL + "?q=" + word;
		try {
			URL qu = new URL(queryUrl);
			Document doc = Jsoup.parse(qu, 16000);
			if(styleType == StyleType.plain) {
				rtn = getPlainExplain(doc);
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
			return null;
		}
		return rtn;
	}
	private String getPlainExplain(Document doc) {
		StringBuilder s = new StringBuilder();
		Element ctnEle = doc.getElementById("collinsResult").child(0).child(0).child(0).child(0);
		Element headEle = ctnEle.child(0);
		Element meansEle = ctnEle.child(1);
		s.append("http://dict.youdao.com------------------------------\r\n");
		s.append("[" + headEle.select("span.title").text() + "] ");
		s.append(" " + headEle.select("em.phonetic").text());
		String star = headEle.select("span.star").attr("class");
		int starNo = 0;
		if(star != null){
			if(star.indexOf("1") != -1)			starNo = 1;
			else if (star.indexOf("2") != -1)	starNo = 2;
			else if (star.indexOf("3") != -1)	starNo = 3;
			else if (star.indexOf("4") != -1)	starNo = 4;
			else if (star.indexOf("5") != -1)	starNo = 5;
			s.append(" ");
			for(int i = 0; i < starNo; i++){
				s.append("* ");
			}
		}
		Elements additionalPtn = headEle.select("span.additional");
		if(additionalPtn.size() > 0) {
			s.append(additionalPtn.get(0).text() + " ");
		}
		s.append("|" + headEle.select("span.rank").text() + "| ");
		if(additionalPtn.size() > 1) {
			s.append(additionalPtn.get(1).text() + " ");
		}
		
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		s.append(headEle.select("span.title").html());
		
		return new String(s);
	}

	/**
	 * @param word 要查询的单词
	 * @return 根据单词得到的整个页面
	 */
	public String fetchPage(String word){
		String queryUrl = fetchFromURL + "?q=" + word;
		String returnString = "";

		try {
			StringBuffer htmlBuffer = new StringBuffer();
			URL qu = new URL(queryUrl);
			URLConnection conn = qu.openConnection();
			InputStream srcIs = conn.getInputStream();
			int ch;
			while ((ch = srcIs.read()) != -1) {
				htmlBuffer.append((char)ch);
			}
			returnString = new String(htmlBuffer);
			returnString = new String(returnString.getBytes("ISO8859-1"), "utf-8");
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
