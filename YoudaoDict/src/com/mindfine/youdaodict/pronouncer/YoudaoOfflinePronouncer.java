package com.mindfine.youdaodict.pronouncer;

import java.io.IOException;

public class YoudaoOfflinePronouncer implements Pronouncer {
	
	private String exe = "mpg321";
	private PronounceType pronounceType = PronounceType.PRONOUNCE_EN;
	
	@Override
	public void pronounce(String word) {
		pronounce(word, PronounceType.PRONOUNCE_MUTE);
	}
	@Override
	public void pronounce(String word, PronounceType pType) {
		if(word != null && !word.equals("")) {
			String uri = "";
			if (pType != null) {
				if(pType == PronounceType.PRONOUNCE_EN) {
					uri = System.getProperty("user.dir") + "/speech/en/" + word + ".mp3";
				} else if (pType == PronounceType.PRONOUNCE_US) {
					uri = System.getProperty("user.dir") + "/speech/us/" + word + ".mp3";
				} else if (pType == PronounceType.PRONOUNCE_MUTE) {
					return;
				}
			}
			playByLocal(uri);
		}
	}

	private void playByLocal(String uri) {
		try {
			Process p = Runtime.getRuntime().exec("mpg321 " + uri);
			p.waitFor();
		} catch (IOException e) {
			System.out.println("朗读单词失败.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length > 0) {
			new YoudaoPronouncer().pronounce(args[0]);			
		}
	}
	public String getExe() {
		return exe;
	}
	public void setExe(String exe) {
		this.exe = exe;
	}
	public PronounceType getPronounceType() {
		return pronounceType;
	}
	public void setPronounceType(PronounceType pronounceType) {
		this.pronounceType = pronounceType;
	}

}
