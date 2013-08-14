package com.mindfine.youdaodict.pronouncer;


public interface Pronouncer {
	
	public enum PronounceType {
		PRONOUNCE_EN,
		PRONOUNCE_US,
		PRONOUNCE_MUTE
	}
	
	/**
	 * 读出单词
	 * @param word 要读的单词
	 */
	public void pronounce(String word);
	public void pronounce(String word2, PronounceType pType);
	public PronounceType getPronounceType();
	public void setPronounceType(PronounceType pronounceType);
}
