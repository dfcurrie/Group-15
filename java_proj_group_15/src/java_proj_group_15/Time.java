package java_proj_group_15;

import java.util.TimerTask;

public class Time extends TimerTask {

	private int curTime = 0;

	//simply just instantiates the timer.
	@Override
	public void run() {
	curTime++;
	}
	
	//Accessor for getting current time
	public int getCurTime() {
		return curTime;
	}

	//Mutator for setCurTime
	public void setCurTime(int curTime) {
		this.curTime = curTime;
	}

	//Mutator to reset the time
	public void resetTime() {
		curTime = 0;
	}
}
