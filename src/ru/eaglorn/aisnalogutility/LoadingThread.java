package ru.eaglorn.aisnalogutility;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

public class LoadingThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(LoadingThread.class);

	private JPanel panel = null;

	private @Getter @Setter boolean work = true;
	private @Setter String processText = "";

	private int number = 0;
	private JLabel label = null;
	private boolean reverse = false;
	
	private @Setter int type = 0;

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		while (work) {
			try {
				StringBuilder text = new StringBuilder();
				if (!reverse) {
					if (number == 37)
						reverse = !reverse;
					for (int i = 0; i < 39; i++) {
						if (i == number) {
							text.append("███");
						} else {
							text.append("_");
						}
					}
					number++;
				} else {
					if (number == 1)
						reverse = !reverse;
					for (int i = 0; i < 39; i++) {
						if (i == number) {
							text.append("███");
						} else {
							text.append("_");
						}
					}
					number--;
				}

				app.getFrame().removeAll();
				app.getFrame().setSize(565, 180);
				panel = new JPanel();
				StringBuilder labelJLabel = new StringBuilder();
				labelJLabel.append("<html><div style='text-align: center;'>");
				switch(type) {
					case(1) : {
						labelJLabel.append("Выполняется установка АИС Налог-3 ПРОМ.<br>Во время установки не запускайте АИС Налог-3 ПРОМ!<br>После завершения установки программа закроется.<br><br>");
						break;
					}
					default: labelJLabel.append("Выполняется установка фиксов.<br>Во время установки не запускайте АИС Налог-3 ПРОМ!<br>После завершения установки программа закроется.<br><br>");
				}
				labelJLabel.append(processText + "<br><br>" + text.toString() + "<br></div></html>");
				label = new JLabel(labelJLabel.toString(), SwingConstants.CENTER);
				panel.add(label);
				app.getFrame().add(panel);
				app.getFrame().revalidate();
				app.getFrame().repaint();

				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
}
