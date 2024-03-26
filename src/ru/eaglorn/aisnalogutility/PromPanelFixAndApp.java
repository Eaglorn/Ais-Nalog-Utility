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

public class PromPanelFixAndApp {
	private @Getter JLabel info = new JLabel("", SwingConstants.CENTER);
	private JButton buttonUninstalled = new JButton("Установить новые фиксы");
	private JButton buttonAll = new JButton("Установить все фиксы");
	private JButton buttonChecked = new JButton("Установить только выбранные фиксы");
	private JButton buttonUnchecked = new JButton("Установить все фиксы кроме выбранных");
	private @Getter JPanel panel = null;
	private int width = 320;

	public PromPanelFixAndApp() {
		App app = AisNalogUtility.getApp();
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(9, 0, 0, 0);
		panel.setLayout(layout);
		panel.add(info);
		panel.add(createButtonUninstalled());
		panel.add(createButtonAll());
		panel.add(createButtonChecked());
		panel.add(createButtonUnchecked());
		panel.add(new JSeparator());
		panel.add(new JLabel("Установленная версия: " + app.getPromVersion(), SwingConstants.CENTER));
		panel.add(buttonInstallProm());
		panel.add(new JLabel("Актуальная версия: " + AisNalogUtility.getData().getConfigApp().getPromVersion(),
				SwingConstants.CENTER));
		if (!app.isPromInstalled()) {
			buttonUninstalled.setEnabled(false);
			buttonAll.setEnabled(false);
			buttonChecked.setEnabled(false);
			buttonUnchecked.setEnabled(false);
		}
		panel.setMinimumSize(new Dimension(width, 0));
		app.addWidth(width);
	}

	private JButton createButtonUninstalled() {
		App app = AisNalogUtility.getApp();
		buttonUninstalled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();
				Thread thread = new PromFixThread(0);
				thread.start();
			}
		});
		return buttonUninstalled;
	}

	private JButton createButtonAll() {
		App app = AisNalogUtility.getApp();
		buttonAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();
				Thread thread = new PromFixThread(1);
				thread.start();
			}
		});
		return buttonAll;
	}

	private JButton createButtonChecked() {
		App app = AisNalogUtility.getApp();
		buttonChecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();
				Thread thread = new PromFixThread(2);
				thread.start();
			}
		});
		return buttonChecked;
	}

	private JButton createButtonUnchecked() {
		App app = AisNalogUtility.getApp();
		buttonUnchecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();
				Thread thread = new PromFixThread(3);
				thread.start();
			}
		});
		return buttonUnchecked;
	}

	private JButton buttonInstallProm() {
		App app = AisNalogUtility.getApp();
		String text;
		if (app.isPromInstalled()) {
			if (app.getPromVersion().equals(AisNalogUtility.getData().getConfigApp().getPromVersion())) {
				text = "Переустановить АИС-Налог 3 ПРОМ";
			} else {
				text = "Установить новую версию\nАИС-Налог 3 ПРОМ";
			}
		} else {
			text = "Установить АИС-Налог 3 ПРОМ";
		}
		JButton button = new JButton(text);
		button.addActionListener(new ActionListener() {
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
		return button;
	}
}
