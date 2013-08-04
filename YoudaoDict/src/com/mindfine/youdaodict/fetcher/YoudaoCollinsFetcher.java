package com.mindfine.youdaodict.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
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
	public boolean log = true;

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
			return "未找到相关单词，请检查拼写。";
		}
		return rtn;
	}

	/**
	 * 解析整个页面，得到纯字符解释
	 * @param doc 页面文档对象
	 * @param word 单词
	 * @return 查到的字符串
	 */
	private String getPlainExplain(Document doc, String word) {
		StringBuilder s = new StringBuilder();
		LinkedList<String> titList = new LinkedList<String>();//存储大标题的链表
		s.append("----------http://dict.youdao.com----------\r\n");
		try {
			Element collinsResult = doc.getElementById("collinsResult");
			if(collinsResult == null) {
				return "单词\"" + word + "\"在Collins英汉词典中没有找到相关解释，请选择其他词典 :)";
			}
			Elements transContainers = collinsResult.select("div.trans-container");
			for(Element transContainer : transContainers) {//如果有多个transContainer
				//找transContainer下的所有transContent
				Elements transContents = transContainer.select("div.trans-content");
				for(Element transContent : transContents) {//如果有多个transContent
					//找transContent下的所有标题prContainers
					Elements prContainers = transContent.select("div.pr-container");
					for(Element prContainer : prContainers) {//如果有多个包含大分类的框框每个框框有一系列大分类
						//找出这个框框下的所有大标题
						Elements wordGroups = prContainer.select("p.wordGroup");
						for(Element wordGroup : wordGroups) {
							//拿出这个大标题的序号
							Elements contentTitles = (wordGroup.select("a.nav-js"));
							if(contentTitles != null && contentTitles.size() > 0) {
								titList.add(contentTitles.first().text());
							}
						}
					}
					//找出transContent下所有wtContainer
					Elements wtContainers = transContent.select("div.wt-container");
					for(Element wtContainer : wtContainers) {
						if(titList.size() > 0) {//如果还有什么大标题小标题的话，取出一个来
							s.append("* " + titList.pop() + "\r\n----------------------------------------------\r\n");
						}

						//下面是标题，h4栏
						Element h4Tit = wtContainer.select("h4").first();
						s.append("\r\n");
						appendCtn(s, h4Tit.select("span.title"));
						s.append(" ");
						appendCtn(s, h4Tit.select("em.phonetic"));
						//几星词汇
						String star = h4Tit.select("span.star").attr("class");
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
							if(starNo > 0) {
								s.append(" [");
								for (int i = 0; i < starNo; i++) {
									if (i != (starNo - 1)) {
										s.append("* ");
									} else {
										s.append("*");
									}
								}
								s.append("]");
							}
							s.append(" ");
						}
						//变形
						Elements additionalPtn = h4Tit.select("span.additional");
						if (additionalPtn.size() > 0) {
							s.append(additionalPtn.get(0).text() + " ");
						}
						Elements h4TitRank = h4Tit.select("span.rank");
						if(h4TitRank.size() > 0) {
							s.append("|" + h4Tit.select("span.rank").text() + "| ");							
						}
						if (additionalPtn.size() > 1) {
							s.append(additionalPtn.get(1).text().trim() + " ");
						}

						//collins-intro部分
						Element collinsIntro = wtContainer.select("p.collins-intro").first();
						if (collinsIntro != null) {
							s.append("\r\n");
							s.append("+---------------------------------\r\n");
							s.append("|" + collinsIntro.text());
							s.append("\r\n+---------------------------------\r\n");
						}
						
						s.append("\r\n\r\n意义\r\n------------------------------------------------------\r\n");

						Elements ulOls = wtContainer.select("ul.ol");
						for(Element ulOl : ulOls) {
							
							for (Element meanItem : ulOl.children()) {
								
								Elements collinsMajorTrans = meanItem.select("div.collinsMajorTrans");
								if(collinsMajorTrans.size() > 0) {
									String meanNo = collinsMajorTrans.first().select("span.collinsOrder").first().text(); 
									appendCtn(s, meanNo);
								}

								String meanStr = collinsMajorTrans.select("p").text();//解释文字
								//把句子中的关键词加上括号
								Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
								Matcher m = p.matcher(meanStr);
								if (m.find()) {
									String wordTemp = m.group();
									s.append(meanStr.replace(wordTemp, "[" + wordTemp + "]"));
								} else {
									s.append(meanStr);
								}
								s.append("\r\n");

								//例句
								Elements examplesLists = meanItem.select("div.exampleLists");//例句们
								for (Element exampleList : examplesLists) {
									s.append("    ~");
									appendCtn(s, exampleList.select("span.collinsOrder"));
									s.append(exampleList.select("div.examples").get(0).child(0).text());
									s.append("\r\n         ");
									s.append(exampleList.select("div.examples").get(0).child(1).text());
									s.append("\r\n");
								}
								if(collinsMajorTrans.size() > 0) {
									s.append("------------------------------------------------------\r\n");
								}
							}
						}
						//---
					}
				}
			}
			
			
	
			
		} catch (NullPointerException ne) {
			System.out.println("解析未能完全成功，请检查" + getClass().getName() + "\r\n或将此Bug报告给https://github.com/rankun203/YoudaoDict/issues");
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
	 * 添加了非空判断的功能
	 * @param s 要给谁后面追加字符串？
	 * @param string 要从哪个元素中提取字符串？
	 */
	private boolean appendCtn(StringBuilder s, Element ele) {
		if (ele != null) {
			String firstText = ele.text();
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
