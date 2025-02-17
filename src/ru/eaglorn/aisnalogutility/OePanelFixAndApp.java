package ru.eaglorn.aisnalogutility;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;

public class OePanelFixAndApp {
	private @Getter JPanel panel = new JPanel();
	private int width = 320;

	public OePanelFixAndApp() {
		App app = AisNalogUtility.getApp();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(9, 0, 0, 0));
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("Установленная версия: " + app.getOeVersion(), SwingConstants.CENTER));
		panel.add(buttonInstallOe());
		panel.add(new JLabel("Актуальная версия: " + AisNalogUtility.getData().getConfigApp().getOeVersion(),
				SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		panel.add(new JLabel("", SwingConstants.CENTER));
		app.addWidth(width);
	}

	private JButton buttonInstallOe() {
		App app = AisNalogUtility.getApp();
		String text;
		if (app.isOeInstalled()) {
			if (app.getOeVersion().equals(AisNalogUtility.getData().getConfigApp().getOeVersion())) {
				text = "Переустановить АИС-Налог 3 ОЭ";
			} else {
				text = "Установить новую версию АИС-Налог 3 ОЭ";
			}
		} else {
			text = "Установить АИС-Налог 3 ОЭ";
		}
		JButton button = new JButton(text);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.setType(2);
				loadingThread.start();
				Thread thread = new OeAppThread();
				thread.start();
			}
		});
		return button;
	}
}
