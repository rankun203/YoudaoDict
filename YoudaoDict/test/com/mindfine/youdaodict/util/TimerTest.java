package com.mindfine.youdaodict.util;

import org.junit.Test;

import com.mindfine.youdaodict.Timer;

public class TimerTest {
	
	@Test
	public void testTime() throws InterruptedException{
		Timer t = new Timer();
		Thread.sleep(2000);
		System.out.println(t.loop());
		System.out.println(t.stop());
	}

}
