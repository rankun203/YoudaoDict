package com.mindfine.youdaodict.fetcher;

/**
 * Fetcher即系统调用以查询单词的工具
 * @author mindfine
 */
public interface Fetcher {

	public String getResFromWord(String word);

}
