package com.mindfine.youdaodict;

public class Timer {
	private long startTime;
	private long lastLoop;
	public Timer(){
		startTime = System.currentTimeMillis();
		lastLoop = startTime;
	}
	
	public long loop(){
		long thisLoop = System.currentTimeMillis() - lastLoop;
		lastLoop = System.currentTimeMillis();
		return thisLoop;
	}
	
	public long stop(){
		long expandTime = System.currentTimeMillis() - startTime;
		startTime = 0;
		lastLoop = 0;
		return expandTime;
	}

}
