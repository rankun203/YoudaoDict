package com.mindfine.youdaodict.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * YoudaoCollinsFetcher可以从有道词典拦截Collins的释义
 * 
 * @author mindfine
 */
public class YoudaoCollinsFetcher implements Fetcher {
	public static String fetchFromURL = "http://dict.youdao.com/search";

	@Override
	public String getResFromWord(String word) {
		return jsoupFetcher(word);
	}

	
	public String jsoupFetcher(String word){
		String rtn = "";
		String queryUrl = fetchFromURL + "?q=" + word;
		try {
			URL qu = new URL(queryUrl);
			Document doc = Jsoup.parse(qu, 16000);
			Element collinsRes = doc.getElementById("collinsResult");
			rtn = collinsRes.html();
		} catch (MalformedURLException e) {
			System.err.println("ERROR, queryUrl is invalid.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("open connection failed.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			return null;
		}
		return rtn;
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
}
