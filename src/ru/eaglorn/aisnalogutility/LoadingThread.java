package ru.eaglorn.aisnalogutility;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingThread extends Thread {
	private JLabel label = null;
	
	private int number = 0;
	private @Setter String processText = "";
	private boolean reverse = false;
	
	
	private @Setter int type = 0;
	
	private @Getter @Setter boolean work = true;

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		while (work) {
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

			app.getFrame().getContentPane().removeAll();
			app.getFrame().setSize(565, 180);
			JPanel panel = new JPanel();
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

			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}
}
