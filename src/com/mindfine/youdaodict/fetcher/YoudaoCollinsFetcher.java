package com.mindfine.youdaodict.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mindfine.youdaodict.explain.YoudaoCollinsExplain;
import com.mindfine.youdaodict.explain.YoudaoCollinsExplain.ExplainUnit;
import com.mindfine.youdaodict.explain.YoudaoCollinsExplain.ExplainUnit.ExplainUnitItem;
import com.mindfine.youdaodict.explain.YoudaoCollinsExplain.ExplainUnit.ExplainUnitItem.ItemExampleUnit;

/**
 * 从有道词典截取Collins的释义
 * 
 * @author mindfine
 */
public class YoudaoCollinsFetcher implements Fetcher {
	public String fetchFromURL = "http://dict.youdao.com/search";
	public Fetcher.StyleType styleType;
	public boolean debug = true;
	public YoudaoCollinsExplain exp;

	/**
	 * 初始化一个词典释义对象，用来填充释义
	 */
	public YoudaoCollinsFetcher () {
		exp = new YoudaoCollinsExplain();
	}
	@Override
	public String getResFromWord(String word) {
		return jsoupFetcher(word);
	}
	public Document prepareDoc(String word){
		String queryUrl = fetchFromURL + "?q=" + word;
		Document doc = null;
		try {
			URL qu = new URL(queryUrl);
			doc = Jsoup.parse(qu, 16000);
		} catch (MalformedURLException me) {
			System.err.println("ERROR, queryUrl is invalid.");
			me.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("open connection failed.\r\nNetwork may not accessible.");
			return null;
		}
		return doc;
	}
	public String getSplitedFromWord(String word) {
		Document doc = prepareDoc(word);
		if(doc == null) {
			return null;
		}
		String rtn = null;
		try{
			queryPlainExplain(doc, word);
			rtn = getExp().toShellStyleString();
		} catch (NullPointerException ne) {
			return null;
		}
		return rtn;
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
					queryPlainExplain(doc, word);
					if(getExp().getUsageList().size() > 0) {
						rtn = getExp().toString();
					} else {
						rtn = null;
					}
				}
			} else {
				Element collinsRes = doc.getElementById("collinsResult");
				rtn = collinsRes.html();
			}
		} catch (MalformedURLException e) {
			System.err.println("ERROR, queryUrl is invalid.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("open connection failed.\r\nNetwork may not accessible.");
			return null;
		} catch (NullPointerException e) {
			return null;
		}

		return rtn;
	}

	/**
	 * 解析整个页面，得到纯字符解释
	 * @param doc 页面文档对象
	 * @param word 单词
	 * @return 查到的字符串
	 */
	private boolean queryPlainExplain(Document doc, String word) {
		exp.setWord(word);
		
		LinkedList<String> titList = new LinkedList<String>();//存储大标题的链表
		try {
			Element collinsResult = doc.getElementById("collinsResult");
			if(collinsResult == null) {
				return false;
			}
			Elements transContainers = collinsResult.select("div.trans-container");
			for(int i = 0; i < transContainers.size(); i++) {
				Element transContainer = transContainers.get(i);
				if(transContainer != null) {
					//如果有多个transContainer
					//找transContainer下的所有transContent
					Elements transContents = transContainer.select("div.trans-content");
					for(int j = 0; j < transContents.size(); j++) {
						Element transContent = transContents.get(i);
						if(transContent != null) {
							//如果有多个transContent
							//找transContent下的所有标题prContainers
							Elements prContainers = transContent.select("div.pr-container");
							for(int k = 0; k < prContainers.size(); k++) {
								Element prContainer = prContainers.get(i);
								if(prContainer != null) {
									
								}
							}
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
								ExplainUnit unit = exp.new ExplainUnit();
								
								if(titList.size() > 0) {//如果还有什么大标题小标题的话，取出一个来
									unit.setUsage(titList.pop());
								}

								//第一行，包括单词、音标、星星、等级等
								Elements h4Tits = wtContainer.select("h4");
								if(h4Tits != null && h4Tits.size() > 0) {
									Element h4Tit = wtContainer.select("h4").first();
									if(h4Tit != null) {
										//音标
										Elements elesPhoneTic = h4Tit.select("em.phonetic");
										if(elesPhoneTic.size() > 0) {
											unit.setPhonetic(elesPhoneTic.first().text().trim());
										}
										//星级
										Elements elesStar = h4Tit.select("span.star");
										if(elesStar.size() > 0) {
											String star = elesStar.attr("class").trim();
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
												unit.setStar(starNo);
											}
										}
										//变形之一
										Elements additionalPtn = h4Tit.select("span.additional");
										if (additionalPtn.size() > 0) {
											String formStr = additionalPtn.get(0).text().trim();
											String [] forms = formStr.split("\\W");
											List<String> formsList = new ArrayList<String>();
											for(String form_ : forms) {
												if(form_ != null && !form_.equals("")){
													formsList.add(form_);
												}
											}
											unit.setForm(formsList);
										}
										//等级，CET4、CET6等等
										Elements h4TitRank = h4Tit.select("span.rank");
										if(h4TitRank.size() > 0) {
											String rankStr = h4TitRank.get(0).text().trim();
											String [] ranks = rankStr.split("\\W");
											List<String> ranksList = new ArrayList<String>();
											for(String rank_ : ranks) {
												ranksList.add(rank_);
											}
											unit.setRank(ranksList);
										}
										//或许还有一个变形
										if (additionalPtn.size() > 1) {
											String additionalStr = additionalPtn.get(1).text().trim();
											String [] addis = additionalStr.split("\\W");
											List<String> additionalsList = new ArrayList<String>();
											for(String form_ : addis) {
												additionalsList.add(form_);
											}
											unit.setAdditional(additionalsList);
										}
										
									}
								}

								//collins-intro部分
								Elements intros = wtContainer.select("p.collins-intro");
								if(intros != null && intros.size() > 0) {
									unit.setCollinsIntro(intros.first().text().trim());
								}

								Elements ulOls = wtContainer.select("ul.ol");
								for(Element ulOl : ulOls) {
									for (Element meanItem : ulOl.children()) {
										//该单词该类别的解释，比如1.COUNT statement 意思 例……
										ExplainUnitItem explainUnitItem = unit.new ExplainUnitItem();
										//同上
										Elements collinsMajorTrans = meanItem.select("div.collinsMajorTrans");
										String meanStr = collinsMajorTrans.select("p").text();//解释文字
										//加了关键词后的句子
										String modMeanStr = "";
										//把句子中的关键词加上括号
										Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
										Matcher m = p.matcher(meanStr);
										if (m.find()) {
											String wordTemp = m.group();
											modMeanStr = meanStr.replace(wordTemp, "<" + wordTemp + ">");
										} else {
											modMeanStr = meanStr;
										}
										explainUnitItem.setTypeAndExplain(modMeanStr);
										
										Elements examplesLists = meanItem.select("div.exampleLists");//例句们
										for (Element exampleList : examplesLists) {
											ItemExampleUnit exampleUnit = explainUnitItem.new ItemExampleUnit();
											exampleUnit.setExample(exampleList.select("div.examples").get(0).child(0).text().trim());
											exampleUnit.setHanTrans(exampleList.select("div.examples").get(0).child(1).text().trim());
											explainUnitItem.getUnits().add(exampleUnit);
										}
										unit.getExplainUnitItems().add(explainUnitItem);
									}
								}
								exp.getUsageList().add(unit);
							}
						}
					}
				}
			}

		} catch (NullPointerException ne) {
			System.out.println("解析未能完全成功，请检查" + getClass().getName() + "\r\n或将此Bug报告给https://github.com/rankun203/YoudaoDict/issues");
			return true;
		}
		return true;
	}

	/**
	 * @param word 要查询的单词
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
	public YoudaoCollinsExplain getExp() {
		return exp;
	}
}
