package ru.eaglorn.aisnalogutility;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingThread extends Thread {
	private @Setter String processText = "";
	private @Setter int type = 0;
	private @Getter @Setter boolean work = true;

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		while (work) {
			
			app.getFrame().getContentPane().removeAll();
			app.getFrame().setSize(565, 180);
			JPanel panel = new JPanel();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("<html><div style='text-align: center;'>");
			switch (type) {
			case (1): {
				stringBuilder.append(
						"Выполняется установка АИС Налог-3 ПРОМ.<br>Во время установки не запускайте АИС Налог-3 ПРОМ!<br>После завершения установки программа закроется.<br><br>");
				break;
			}
			case (2): {
				stringBuilder.append(
						"Выполняется установка АИС Налог-3 ОЕ.<br>Во время установки не запускайте АИС Налог-3 ОЕ!<br>После завершения установки программа закроется.<br><br>");
				break;
			}
			default: {
				stringBuilder.append(
						"Выполняется установка фиксов.<br>Во время установки не запускайте АИС Налог-3 ПРОМ!<br>После завершения установки программа закроется.<br><br>");
			}
			}
			stringBuilder.append(processText + "<br><br></div></html>");
			JLabel label = new JLabel(stringBuilder.toString(), SwingConstants.CENTER);
			JProgressBar progressBar= new JProgressBar();
			progressBar.setIndeterminate(true);
			panel.add(label);
			panel.add(progressBar);
			app.getFrame().add(panel);
			app.getFrame().revalidate();
			app.getFrame().repaint();
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				log.error(stack.toString());
			}
		}
	}
}
