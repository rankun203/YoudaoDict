package com.mindfine.youdaodict.pronouncer;

import java.io.IOException;

public class YoudaoOfflinePronouncer implements Pronouncer {
	
	private String exe = "mpg321";
	
	@Override
	public void pronounce(String word) {
		pronounce(word, PRONOUNCE_EN);
	}
	public void pronounce(String word, int type) {
		if(word != null && !word.equals("")) {
			String uri = "";
			if(type == PRONOUNCE_EN) {
				uri = System.getProperty("user.dir") + "/speech/en/" + word + ".mp3";
			} else if (type == PRONOUNCE_US) {
				uri = System.getProperty("user.dir") + "/speech/us/" + word + ".mp3";
			}
			playByLocal(uri);
		}
	}

	private void playByLocal(String uri) {
		try {
			Process p = Runtime.getRuntime().exec("mpg321 " + uri);
		} catch (IOException e) {
			System.out.println("朗读单词失败.");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length > 0) {
			new YoudaoPronouncer().pronounce(args[0]);			
		}
	}

}
