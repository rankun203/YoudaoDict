package com.mindfine.youdaodict.pronouncer;

public interface Pronouncer {
	public int PRONOUNCE_EN = 1;
	public int PRONOUNCE_US = 2;
	
	/**
	 * 读出单词
	 * @param word 要读的单词
	 */
	public void pronounce(String word);
	
}
