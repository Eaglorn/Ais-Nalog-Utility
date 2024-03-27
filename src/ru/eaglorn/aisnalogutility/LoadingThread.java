package ru.eaglorn.aisnalogutility;

import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingThread extends Thread {
	private @Setter String processText = "";
	private @Setter int type = 0;
	private @Getter @Setter boolean work = true;
	private @val JProgressBar progressBar = new JProgressBar();

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		while (work) {
			app.getFrame().getContentPane().removeAll();
			app.getFrame().setSize(490, 235);
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridLayout layout = new GridLayout(6, 0, 0, 0);
			panel.setLayout(layout);
			String messageFirst = "";
			String messageSecond = "";
			switch (type) {
			case (1): {
				messageFirst = "Выполняется установка АИС Налог-3 ПРОМ.";
				messageSecond = "Во время установки не запускайте АИС Налог-3 ПРОМ!";
				break;
			}
			case (2): {
				messageFirst = "Выполняется установка АИС Налог-3 ОЕ.";
				messageSecond = "Во время установки не запускайте АИС Налог-3 ОЕ!";
				break;
			}
			default: {
				messageFirst = "Выполняется установка фиксов.";
				messageSecond = "Во время установки не запускайте АИС Налог-3 ПРОМ!";
			}
			}
			panel.add(new JLabel(messageFirst, SwingConstants.CENTER));
			panel.add(new JLabel(messageSecond, SwingConstants.CENTER));
			panel.add(new JLabel("После завершения установки программа закроется.", SwingConstants.CENTER));
			panel.add(new JLabel("", SwingConstants.CENTER));
			panel.add(new JLabel(processText, SwingConstants.CENTER));
			progressBar.setIndeterminate(true);
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
