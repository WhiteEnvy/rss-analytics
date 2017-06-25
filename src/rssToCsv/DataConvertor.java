package rssToCsv;

import java.util.Timer;
import java.util.TimerTask;

import rssToCsv.Utils.EmailSender;

public class DataConvertor {

	public static void main(String args[]) throws Exception {
		EmailSender.getConfig();
		
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					RssFeedManager.UpdateData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, 0, 1000);
		
		InputManager.init();
	}
}