package System;

import gui.MainWindow;
public class ReaderThread implements Runnable{
	AgentReader agent;
	MainWindow frame;
	Thread thr;
	boolean is_running = false;
	Router[] routers;
	public ReaderThread(AgentReader a,MainWindow frame){
		agent = a;
		thr = new Thread(this);
		this.frame = frame;
	}
	@Override
	public void run() {
		while(!thr.isInterrupted()) {
			routers = agent.getData();
			String ip = agent.getAddress();
			String str = "ROUTER: "+ ip+'\n';
			for(short i = 0;i < routers.length;i++) {
				str+= routers[i].toString()+'\n';
			}
			if(frame != null) {
				frame.getTextArea().setText(str);
				frame.getTextArea().revalidate();
			}
			else System.out.println(str);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {return;}
		}
	
	}
	public boolean is_running() {
		return is_running;
	}
	public void start() {
		thr.start();
		is_running = true;
	}
	public void stop() {
		thr.interrupt();
		is_running = false;
	}
}
