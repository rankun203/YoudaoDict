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

	public void download(String word, String saveTo){
		String url0 = queryURL + "?type=1&audio=" + word;
		String url1 = queryURL + "?type=2&audio=" + word;
		
		NetTool tool = new NetTool();
		tool.download(url0, saveTo + "en/", word + ".mp3");
		tool.download(url1, saveTo + "us/", word + ".mp3");
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
	
	
	public static void main(String[] args) {
		if(args.length > 0) {
			new YoudaoPronouncer().pronounce(args[0]);			
		}
	}

}
