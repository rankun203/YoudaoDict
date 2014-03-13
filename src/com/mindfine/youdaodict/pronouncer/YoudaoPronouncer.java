package com.mindfine.youdaodict.pronouncer;

import java.io.IOException;

public class YoudaoPronouncer implements Pronouncer {
	
	private String queryURL = "http://dict.youdao.com/dictvoice";
	private String exe = "mpg321";
	public PronounceType pronounceType = PronounceType.PRONOUNCE_EN;
	
	@Override
	public void pronounce(String word) {
		pronounce(word, PronounceType.PRONOUNCE_MUTE);
	}
	public void pronounce(String word, PronounceType pType) {
		String dialect = null;
		if(pType == PronounceType.PRONOUNCE_EN) {
			dialect = "1";
		} else if (pType == PronounceType.PRONOUNCE_EN) {
			dialect = "2";
		} else if (pType == PronounceType.PRONOUNCE_MUTE) {
			return;
		}
		
		String url = queryURL + "?type=" + dialect + "&audio=" + word;
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
			String args0 = args[0];
			if(args0 != null && !args0.equals("")) {
				if(args0.contains("-en")) {
					args0 = args0.replace("-en", "");
					new YoudaoPronouncer().pronounce(args0, PronounceType.PRONOUNCE_EN);
				} else if(args0.contains("-us")) {
					args0 = args0.replace("-us", "");
					new YoudaoPronouncer().pronounce(args0, PronounceType.PRONOUNCE_US);
				}
			}
		}
	}
	@Override
	public PronounceType getPronounceType() {
		return pronounceType;
	}
	@Override
	public void setPronounceType(PronounceType pronounceType) {
		this.pronounceType = pronounceType;
	}
	public String getExe() {
		return exe;
	}
	public void setExe(String exe) {
		this.exe = exe;
	}

}
