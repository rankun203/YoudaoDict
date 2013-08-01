package com.mindfine.youdaodict.fetcher;

/**
 * Fetcher即系统调用以查询单词的工具
 * @author mindfine
 */
public interface Fetcher {
	
	public enum DicType {
		/**
		 * 未知的词典类型
		 */
		UNKNOWN,
		/**
		 * 有道柯林斯词典
		 */
		youdaoCollins
	}

	public enum StyleType {
		/**
		 * 未知的样式类型
		 */
		UNKNOWN,
		/**
		 * 纯文本方式显示
		 */
		plain,
		/**
		 * 将样式直接写入HTML中
		 */
		singleStyle,
		/**
		 * 将样式分布在HTML页面之外的css文件中
		 */
		separateStyle
	}

	/**
	 * 从单词中获取释义
	 * @param word 指定的单词
	 * @return 释义字符串
	 */
	public String getResFromWord(String word);
	/**
	 * 获取样式类型
	 * @return 当前的styleType
	 */
	public Fetcher.StyleType getStyleType();
	/**
	 * 设置样式类型
	 * @param styleType 要设置的类型
	 */
	public void setStyleType(Fetcher.StyleType styleType);

}
