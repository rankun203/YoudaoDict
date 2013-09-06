package com.mindfine.youdaodict.explain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mindfine.youdaodict.explain.YoudaoCollinsExplain.ExplainUnit.ExplainUnitItem;
import com.mindfine.youdaodict.explain.YoudaoCollinsExplain.ExplainUnit.ExplainUnitItem.ItemExampleUnit;


/**
 * @author mindfine
 * 解释对象，存储一个解释的用途、音标、解释等等信息
 */
public class YoudaoCollinsExplain {
	private String word = "";
	/**
	 * 一个单词的不同用法，每个用法是一个独立的ExplainUnit，usageList里面装的就是这个单词的ExplainUnit们
	 */
	private List <ExplainUnit> usageList;
	
	public YoudaoCollinsExplain() {usageList = new LinkedList<ExplainUnit>();}
	public YoudaoCollinsExplain(String word) {
		this.word = word;
		usageList = new ArrayList<ExplainUnit>();
	}

	public class ExplainUnit {
		/**
		 * 当前解释的用法，比如说SORT OR KIND
		 */
		private String usage = "";
		/**
		 * 音标，[taIp]
		 */
		private String phonetic = "";
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
		private String collinsIntro = "";
		private List<ExplainUnitItem> explainUnitItems;
		
		public ExplainUnit () {explainUnitItems = new ArrayList<ExplainUnitItem>();}
		
		/**
		 * 每条独立的解释，包含英文用法示例，中文解释，英文例子，例子翻译等等
		 */
		public class ExplainUnitItem {
			/**
			 * 这一条意思的主要部份，包括类型（比如V-T/V-I）和英语用法，和汉语解释
			 */
			private String typeAndExplain = "";
			/**
			 * 这一条意思的所有例子
			 */
			private List<ItemExampleUnit> units;
			/**
			 * 初始化存储例子们的链表
			 */
			public ExplainUnitItem () {units = new ArrayList<ItemExampleUnit>();}
			/**
			 * 每一个例子，比如name的第一条解释有两个例子，每一个例子都是一个ItemExampleUnit对象
			 */
			public class ItemExampleUnit {
				/**
				 * 例子英文
				 */
				private String example = "";
				/**
				 * 例子中文翻译
				 */
				private String hanTrans = "";
				public String getExample() {return example;}
				public void setExample(String example) {this.example = example;}
				public String getHanTrans() {return hanTrans;}
				public void setHanTrans(String hanTrans) {this.hanTrans = hanTrans;}
			}

			public String getTypeAndExplain() {return typeAndExplain;}
			public void setTypeAndExplain(String typeAndExplain) {this.typeAndExplain = typeAndExplain;}
			public List<ItemExampleUnit> getUnits() {return units;}
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
	@Override
	public String toString() {
		StringBuilder exp = new StringBuilder();
		List<ExplainUnit> usageListTmp = getUsageList();
		if(usageListTmp != null && usageListTmp.size() > 0) {
			for(ExplainUnit explainUnitTmp : usageListTmp) {
				String usageTmp = explainUnitTmp.getUsage();
				// +3 某些提示信息
				if(usageTmp != null && !usageTmp.equals("")) {
					exp.append(explainUnitTmp.getUsage() + "\r\n");
				}
				exp.append(getWord() + " ");
				exp.append(explainUnitTmp.getPhonetic() + " ");
				int starTmp = explainUnitTmp.getStar();
				if(starTmp != 0) {
					exp.append('[');
					for(int i = 0; i < starTmp; i++) {
						if(i != starTmp - 1) {
							exp.append("* ");
						} else {
							exp.append('*');
						}
					}
					exp.append("] ");				
				}
				List<String> formsTmp = explainUnitTmp.getForm();
				if(formsTmp != null && formsTmp.size() > 0) {
					exp.append('(');
					for(int i = 0; i < formsTmp.size(); i++) {
						if(i != (formsTmp.size() - 1)) {
							exp.append(formsTmp.get(i) + ", ");
						} else {
							exp.append("" + formsTmp.get(i));
						}
					}				
					exp.append(") ");
				}
				List<String> ranksTmp = explainUnitTmp.getRank();
				if(ranksTmp != null && ranksTmp.size() > 0) {
					exp.append('|');
					for(int i = 0; i < ranksTmp.size(); i++) {
						if(i != (ranksTmp.size() - 1)) {
							exp.append(ranksTmp.get(i) + ' ');
						} else {
							exp.append("" + ranksTmp.get(i));
						}
					}				
					exp.append('|');
				}
				List<String> additionalsTmp = explainUnitTmp.getAdditional();
				if(additionalsTmp != null && additionalsTmp.size() > 0) {
					exp.append('(');
					for(int i = 0; i < additionalsTmp.size(); i++) {
						if(i != (additionalsTmp.size() - 1)) {
							exp.append(additionalsTmp.get(i) + ", ");
						} else {
							exp.append("" + additionalsTmp.get(i));
						}
					}
					exp.append(") ");
				}
				exp.append("\r\n\r\n");
				exp.append("意义\r\n");
				exp.append("-------------------------------------------\r\n");
				int noTmp = 1;
				for(ExplainUnitItem itemTmp : explainUnitTmp.getExplainUnitItems()) {
					exp.append(noTmp + ". ");
					exp.append(itemTmp.getTypeAndExplain() + "\r\n");
					List<ItemExampleUnit> itemsTmp = itemTmp.getUnits();
					if(itemsTmp != null && itemsTmp.size() > 0) {
						for(ItemExampleUnit tmp : itemsTmp) {
							exp.append("    ~例：");
							exp.append(tmp.getExample() + "\r\n");
							exp.append("         " + tmp.getHanTrans() + "\r\n");
						}
					}
					noTmp++;
					exp.append("----------------------------------------------------\r\n");
				}
			}
		}
		return new String(exp);
	}
	public List<String> toSplitedString() {
		List<String> expList = new ArrayList<String>();
		
		List<ExplainUnit> usageListTmp = getUsageList();
		if(usageListTmp != null && usageListTmp.size() > 0) {
			for(ExplainUnit explainUnitTmp : usageListTmp) {
				String usageTmp = explainUnitTmp.getUsage();
				// +3 某些提示信息
				if(usageTmp != null && !usageTmp.equals("")) {
					expList.add(explainUnitTmp.getUsage());
				}
				expList.add(getWord());
				expList.add(explainUnitTmp.getPhonetic());
				
				StringBuilder starStr = new StringBuilder();
				int starTmp = explainUnitTmp.getStar();
				if(starTmp != 0) {
					starStr.append('[');
					for(int i = 0; i < starTmp; i++) {
						if(i != starTmp - 1) {
							starStr.append("* ");
						} else {
							starStr.append('*');
						}
					}
					starStr.append("] ");				
				}
				expList.add(new String(starStr));
				
				StringBuilder formsStr = new StringBuilder();
				List<String> formsTmp = explainUnitTmp.getForm();
				if(formsTmp != null && formsTmp.size() > 0) {
					formsStr.append('(');
					for(int i = 0; i < formsTmp.size(); i++) {
						if(i != (formsTmp.size() - 1)) {
							formsStr.append(formsTmp.get(i) + ", ");
						} else {
							formsStr.append("" + formsTmp.get(i));
						}
					}				
					formsStr.append(") ");
				}
				expList.add(new String(formsStr));
				
				StringBuilder ranksStr = new StringBuilder();
				List<String> ranksTmp = explainUnitTmp.getRank();
				if(ranksTmp != null && ranksTmp.size() > 0) {
					ranksStr.append('|');
					for(int i = 0; i < ranksTmp.size(); i++) {
						if(i != (ranksTmp.size() - 1)) {
							ranksStr.append(ranksTmp.get(i) + ' ');
						} else {
							ranksStr.append("" + ranksTmp.get(i));
						}
					}				
					ranksStr.append('|');
				}
				expList.add(new String(ranksStr));
				
				StringBuilder additionalsStr = new StringBuilder();
				List<String> additionalsTmp = explainUnitTmp.getAdditional();
				if(additionalsTmp != null && additionalsTmp.size() > 0) {
					additionalsStr.append('(');
					for(int i = 0; i < additionalsTmp.size(); i++) {
						if(i != (additionalsTmp.size() - 1)) {
							additionalsStr.append(additionalsTmp.get(i) + ", ");
						} else {
							additionalsStr.append("" + additionalsTmp.get(i));
						}
					}
					additionalsStr.append(") ");
				}
				expList.add(new String(additionalsStr));
				
				int noTmp = 1;
				for(ExplainUnitItem itemTmp : explainUnitTmp.getExplainUnitItems()) {
					expList.add(noTmp + "");
					String typeAndExplain = itemTmp.getTypeAndExplain().trim();
					int firstSpaceTmp = typeAndExplain.indexOf(" ");
					String typeTmp = typeAndExplain.substring(0, firstSpaceTmp);
					String explainTmp = typeAndExplain.substring(typeAndExplain.indexOf(" "), typeAndExplain.length());
					expList.add(typeTmp.trim());
					expList.add(explainTmp.trim());
					List<ItemExampleUnit> itemsTmp = itemTmp.getUnits();
					if(itemsTmp != null && itemsTmp.size() > 0) {
						for(ItemExampleUnit tmp : itemsTmp) {
							expList.add(tmp.getExample());
							expList.add(tmp.getHanTrans());
						}
					}
					noTmp++;
				}
			}
		}
		return expList;
	}
	
  	public String toShellStyleString(){
		StringBuilder tps = new StringBuilder();
		
		List<ExplainUnit> usageListTmp = getUsageList();
		if(usageListTmp != null && usageListTmp.size() > 0) {
			for(ExplainUnit explainUnitTmp : usageListTmp) {
				String usageTmp = explainUnitTmp.getUsage();
				// +3 某些提示信息
				if(usageTmp != null && !usageTmp.equals("")) {
					tps.append("\\033[1m\\033[4m" + explainUnitTmp.getUsage() + "\\033[0m\\n");
				}
				String firstLineTmp = "";
				tps.append("\\033[34m\\033[1m" + getWord() + "\\033[0m");
				tps.append(" " + explainUnitTmp.getPhonetic() + " ");
				firstLineTmp += getWord() + " " + explainUnitTmp.getPhonetic() + " ";
				
				StringBuilder starStr = new StringBuilder();
				int starTmp = explainUnitTmp.getStar();
				if(starTmp != 0) {
					for(int i = 0; i < starTmp; i++) {
						if(i != starTmp - 1) {
							starStr.append("* ");
						} else {
							starStr.append("*");
						}
					}
				}

				tps.append(" \\033[31m\\033[1m" + new String(starStr) + "\\033[0m  ");
				firstLineTmp += " " + new String(starStr) + "  ";
				
				StringBuilder formsStr = new StringBuilder();
				List<String> formsTmp = explainUnitTmp.getForm();
				if(formsTmp != null && formsTmp.size() > 0) {
					formsStr.append("(");
					for(int i = 0; i < formsTmp.size(); i++) {
						if(i != (formsTmp.size() - 1)) {
							formsStr.append(formsTmp.get(i) + ", ");
						} else {
							formsStr.append("" + formsTmp.get(i));
						}
					}
					formsStr.append(")  ");
				}
				tps.append("\\033[36m" + new String(formsStr) + "\\033[0m");
				firstLineTmp += new String(formsStr);
				
				StringBuilder ranksStr = new StringBuilder();
				List<String> ranksTmp = explainUnitTmp.getRank();
				if(ranksTmp != null && ranksTmp.size() > 0) {
					for(int i = 0; i < ranksTmp.size(); i++) {
						if(i != (ranksTmp.size() - 1)) {
							ranksStr.append(ranksTmp.get(i) + ' ');
						} else {
							ranksStr.append("" + ranksTmp.get(i));
						}
					}				
				}
				tps.append("\\033[1m" + new String(ranksStr) + "\\033[0m");
				firstLineTmp += new String(ranksStr);
				
				StringBuilder additionalsStr = new StringBuilder();
				List<String> additionalsTmp = explainUnitTmp.getAdditional();
				if(additionalsTmp != null && additionalsTmp.size() > 0) {
					additionalsStr.append('(');
					for(int i = 0; i < additionalsTmp.size(); i++) {
						if(i != (additionalsTmp.size() - 1)) {
							additionalsStr.append(additionalsTmp.get(i) + ", ");
						} else {
							additionalsStr.append("" + additionalsTmp.get(i));
						}
					}
					additionalsStr.append(") ");
				}
				tps.append("\\033[36m" + new String(additionalsStr) + "\\033[0m\\n");
				firstLineTmp += new String(additionalsStr);
				tps.append("\\033[36m");
				for(int q = 0; q < firstLineTmp.length(); q++) {
					tps.append('-');
				}
				tps.append("\\033[0m\\n");
				int noTmp = 1;
				for(ExplainUnitItem itemTmp : explainUnitTmp.getExplainUnitItems()) {
					if(noTmp < 10) {
						tps.append("\\033[40;37m\\033[1m " + noTmp + "\\033[0m. \\033[1m");
					} else {
						tps.append("\\033[40;37m\\033[1m" + noTmp + "\\033[0m. \\033[1m");
					}
					//TYPE 替换方式为搜索句中的所有大写字母
					String typeAndExplain = itemTmp.getTypeAndExplain().trim();
					int firstSpaceTmp = typeAndExplain.indexOf(" ");
					String typeTmp = typeAndExplain.substring(0, firstSpaceTmp);
					String explainTmp = typeAndExplain.substring(typeAndExplain.indexOf(" "), typeAndExplain.length());
					tps.append(typeTmp.trim() + "\\033[0m ");
					String highLightedExplainTmp = explainTmp.trim();
					//把[关键字]替换成带有颜色的关键字
					Pattern pTmp = Pattern.compile("\\<(.*?)\\>");
					Matcher mTmp = pTmp.matcher(highLightedExplainTmp);
					while(mTmp.find()) {
						String mTmpNext = mTmp.group();
						highLightedExplainTmp = highLightedExplainTmp.replace(mTmpNext, "\\033[0m\\033[34m\\033[1m" + mTmpNext.substring(1, mTmpNext.length() - 1) + "\\033[0m");
					}
					tps.append(highLightedExplainTmp + "\\n");
					List<ItemExampleUnit> itemsTmp = itemTmp.getUnits();
					if(itemsTmp != null && itemsTmp.size() > 0) {
						for(ItemExampleUnit tmp : itemsTmp) {
							tps.append("\\033[42;37m\\033[1m例\\033[0m：" + tmp.getExample() + "\\n");
							tps.append("    " + tmp.getHanTrans() + "\\n");
						}
					}
					tps.append("\\033[36m----------------------------------------------\\033[0m\\n");
					noTmp++;
				}
			}
		}
		return new String(tps);
	}
}
