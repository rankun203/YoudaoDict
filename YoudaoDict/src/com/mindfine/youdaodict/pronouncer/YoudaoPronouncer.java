package com.mindfine.youdaodict.pronouncer;

import java.io.IOException;

public class YoudaoPronouncer implements Pronouncer {
	
	private String queryURL = "http://dict.youdao.com/dictvoice";
	public int PRONOUNCE_EN = 1;
	public int PRONOUNCE_US = 2;
	private String exe = "mpg321";
	
	@Override
	public void pronounce(String word) {
		pronounce(word, PRONOUNCE_EN);
	}
	public void pronounce(String word, int type) {
		String url = queryURL + "?type=" + type + "&audio=" + word;

		if(word != null && !word.equals("")) {
			playByLocal(url);
		}
	}

	private void playByLocal(String url) {
		try {
			Process p = Runtime.getRuntime().exec("mpg321 " + url);
			p.waitFor();
		} catch (IOException e) {
			System.out.println("朗读单词失败.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public String getQueryURL() {
		return queryURL;
	}

	public void setQueryURL(String queryURL) {
		this.queryURL = queryURL;
	}
	
	

}
