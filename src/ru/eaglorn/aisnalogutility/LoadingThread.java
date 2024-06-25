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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingThread extends Thread {
	private @Setter String processText = "";
	private @Setter int type = 0;
	private @Setter int typeFix = 0;
	private @Getter @Setter boolean work = true;
	private JProgressBar progressBar = new JProgressBar();

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		app.getFrame().getContentPane().removeAll();
		app.getFrame().setSize(490, 235);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridLayout layout = new GridLayout(9, 0, 0, 0);
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
		panel.add(new JLabel("", SwingConstants.CENTER));
		JLabel labelPocessText = new JLabel(processText, SwingConstants.CENTER);
		panel.add(labelPocessText);
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		progressBar.setIndeterminate(true);
		panel.add(progressBar);
		app.getFrame().add(panel);
		app.getFrame().revalidate();
		app.getFrame().repaint();
		while (work) {
			try {
				Thread.sleep(100);
				labelPocessText.setText(processText);
				labelPocessText.revalidate();
				labelPocessText.repaint();
			} catch (InterruptedException e) {
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				log.error(stack.toString());
				Thread.currentThread().interrupt();
			}
		}
	}
}
