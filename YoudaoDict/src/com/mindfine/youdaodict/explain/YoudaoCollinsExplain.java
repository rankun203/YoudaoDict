package com.mindfine.youdaodict.explain;

import java.util.LinkedList;
import java.util.List;


/**
 * @author mindfine
 * 解释对象，存储一个解释的用途、音标、解释等等信息
 */
public class YoudaoCollinsExplain {
	private String word;
	/**
	 * 英语发音地址
	 */
	private String enVoiceUri;
	/**
	 * 美语发音地址
	 */
	private String usVoiceUri;
	/**
	 * 一个单词的不同用法，每个用法是一个独立的ExplainUnit，usageList里面装的就是这个单词的ExplainUnit们
	 */
	private List <ExplainUnit> usageList;
	
	public YoudaoCollinsExplain() {usageList = new LinkedList<ExplainUnit>();}
	public YoudaoCollinsExplain(String word) {
		this.word = word;
		usageList = new LinkedList<ExplainUnit>();
	}

	public class ExplainUnit {
		/**
		 * 当前解释的用法，比如说SORT OR KIND
		 */
		private String usage;
		/**
		 * 音标，[taIp]
		 */
		private String phonetic;
		/**
		 * 星标等级，3即三星[* * *]
		 */
		private int star;
		/**
		 * 复数、过去式、分词等等
		 */
		private List<String> form;
		/**
		 * CET4、CET8、TEM8等等
		 */
		private List <String> rank;
		/**
		 * 附加的一些相关单词
		 */
		private List<String> additional;
		/**
		 * 一些介绍文字
		 */
		private String collinsIntro;
		private List<ExplainUnitItem> explainUnitItems;
		
		public ExplainUnit () {explainUnitItems = new LinkedList<ExplainUnitItem>();}
		
		/**
		 * 每条独立的解释，包含英文用法示例，中文解释，英文例子，例子翻译等等
		 */
		public class ExplainUnitItem {
			/**
			 * 这一条意思的主要部份，包括类型（比如V-T/V-I）和英语用法，和汉语解释
			 */
			private String typeAndExplain;
			/**
			 * 这一条意思的所有例子
			 */
			private List<ItemExampleUnit> units;
			/**
			 * 初始化存储例子们的链表
			 */
			public ExplainUnitItem () {units = new LinkedList<ItemExampleUnit>();}
			/**
			 * 每一个例子，比如name的第一条解释有两个例子，每一个例子都是一个ItemExampleUnit对象
			 */
			public class ItemExampleUnit {
				/**
				 * 例子英文
				 */
				private String example;
				/**
				 * 例子中文翻译
				 */
				private String hanTrans;
				public String getExample() {return example;}
				public void setExample(String example) {this.example = example;}
				public String getHanTrans() {return hanTrans;}
				public void setHanTrans(String hanTrans) {this.hanTrans = hanTrans;}
			}

			public String getTypeAndExplain() {return typeAndExplain;}
			public void setTypeAndExplain(String typeAndExplain) {this.typeAndExplain = typeAndExplain;}
			public List<ItemExampleUnit> getUnits() {return units;}
			public void setUnits(List<ItemExampleUnit> units) {this.units = units;}
		}

		public String getUsage() {return usage;}
		public void setUsage(String usage) {this.usage = usage;}
		public String getPhonetic() {return phonetic;}
		public void setPhonetic(String phonetic) {this.phonetic = phonetic;}
		public int getStar() {return star;}
		public void setStar(int star) {this.star = star;}
		public List<String> getForm() {return form;}
		public void setForm(List<String> form) {this.form = form;}
		public List<String> getRank() {return rank;}
		public void setRank(List<String> rank) {this.rank = rank;}
		public List<String> getAdditional() {return additional;}
		public void setAdditional(List<String> additional) {this.additional = additional;}
		public String getCollinsIntro() {return collinsIntro;}
		public void setCollinsIntro(String collinsIntro) {this.collinsIntro = collinsIntro;}
		public List<ExplainUnitItem> getExplainUnitItems() {return explainUnitItems;}

	}

	public String getWord() {return word;}
	public void setWord(String word) {this.word = word;}
	public List<ExplainUnit> getUsageList() {return usageList;}
	public String getEnVoiceUri() {return enVoiceUri;}
	public void setEnVoiceUri(String enVoiceUri) {this.enVoiceUri = enVoiceUri;}
	public String getUsVoiceUri() {return usVoiceUri;}
	public void setUsVoiceUri(String usVoiceUri) {this.usVoiceUri = usVoiceUri;}
}
