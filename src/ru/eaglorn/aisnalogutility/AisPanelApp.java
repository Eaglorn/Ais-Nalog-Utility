package ru.eaglorn.aisnalogutility;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;

public class AisPanelApp {
	private @Getter JPanel panel = new JPanel();
	private int width = 260;

	public AisPanelApp() {
		App app = AisNalogUtility.getApp();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(7, 0, 0, 0);
		panel.setLayout(layout);
		panel.add(new JLabel("Установленная версия: " + app.getPromVersion(), SwingConstants.CENTER));
		panel.add(buttonInstallProm());
		panel.add(new JLabel("Актуальная версия: " + AisNalogUtility.getData().getConfigApp().getPromVersion(),
				SwingConstants.CENTER));
		panel.add(new JSeparator());
		panel.add(new JLabel("Установленная версия: " + app.getOeVersion(), SwingConstants.CENTER));
		panel.add(buttonInstallOe());
		panel.add(new JLabel("Актуальная версия: " + AisNalogUtility.getData().getConfigApp().getOeVersion(),
				SwingConstants.CENTER));
		panel.setMinimumSize(new Dimension(width, 0));
		app.addWidth(width);
	}

	private JButton buttonInstallProm() {
		App app = AisNalogUtility.getApp();
		JButton buttonInstall = new JButton("Установить АИС-Налог 3 ПРОМ");
		buttonInstall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.setType(1);
				loadingThread.start();

				Thread thread = new PromAppThread();
				thread.start();
			}
		});
		return buttonInstall;
	}

	private JButton buttonInstallOe() {
		App app = AisNalogUtility.getApp();
		JButton buttonInstall = new JButton("Установить АИС-Налог 3 ОЭ");
		buttonInstall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.setType(2);
				loadingThread.start();

				Thread thread = new OeAppThread();
				thread.start();
			}
		});
		return buttonInstall;
	}

}
