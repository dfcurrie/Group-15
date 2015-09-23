package java_proj_group_15;

import java.util.TimerTask;

public class Time extends TimerTask {

	private int curTime = 0;
	private boolean run = false;

	@Override
	public void run() {
		//System.out.println(curTime++);
		curTime++;
	}

	public int getCurTime() {
		return curTime;
	}

	public void setCurTime(int curTime) {
		this.curTime = curTime;
	}

	public void resetTime() {
		curTime = 0;
	}
}
