package java_proj_group_15;

import java.util.TimerTask;

public class Time extends TimerTask {
	
	private int curTime = 0;
	
	@Override
	public void run(){
		System.out.println(curTime++);
	}
	
	public int getCurTime() {
		return curTime;
	}

}
